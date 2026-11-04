package com.alangomez.gestionveterinariahappyfeet.service.inventario;

import com.alangomez.gestionveterinariahappyfeet.model.dao.inventario.ExistenciaDAO;
import com.alangomez.gestionveterinariahappyfeet.model.dao.inventario.MovimientoInventarioDAO;
import com.alangomez.gestionveterinariahappyfeet.model.dao.inventario.ProductoDAO;
import com.alangomez.gestionveterinariahappyfeet.model.dao.inventario.TipoMovimientoDAO;
import com.alangomez.gestionveterinariahappyfeet.model.entities.inventario.ConsumoLote;
import com.alangomez.gestionveterinariahappyfeet.model.entities.inventario.Existencia;
import com.alangomez.gestionveterinariahappyfeet.model.entities.inventario.Lote;
import com.alangomez.gestionveterinariahappyfeet.model.entities.inventario.MovimientoInventario;
import com.alangomez.gestionveterinariahappyfeet.model.entities.inventario.Producto;
import com.alangomez.gestionveterinariahappyfeet.model.entities.servicios.Estado;
import com.alangomez.gestionveterinariahappyfeet.util.SesionUsuario;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class InventarioService {

    private static final String ENTRADA = "Entrada";
    private static final String SALIDA = "Salida";
    private static final String VENCIMIENTO = "Vencimiento";

    private final ExistenciaDAO existenciaDAO;
    private final ProductoDAO productoDAO;
    private final MovimientoInventarioDAO movimientoDAO;
    private final TipoMovimientoDAO tipoMovimientoDAO;
    private final ProductoService productoService;
    private final List<ObservadorInventario> observadores = new ArrayList<>();

    public InventarioService(ExistenciaDAO existenciaDAO, ProductoDAO productoDAO, MovimientoInventarioDAO movimientoDAO,
            TipoMovimientoDAO tipoMovimientoDAO, ProductoService productoService) {
        this.existenciaDAO = existenciaDAO;
        this.productoDAO = productoDAO;
        this.movimientoDAO = movimientoDAO;
        this.tipoMovimientoDAO = tipoMovimientoDAO;
        this.productoService = productoService;
    }

    public void agregarObservador(ObservadorInventario observador) {
        observadores.add(observador);
    }

    // Registra la compra de un producto: crea el lote (si tiene), suma el stock y guarda el movimiento
    public void registrarEntrada(int productoId, Lote lote, int cantidad) throws SQLException {
        Producto producto = productoService.buscarActivoPorId(productoId);
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor que cero");
        }
        boolean necesitaLote = !productoService.nombreTipo(producto.getTipoProductoId()).equals(ProductoService.MATERIAL_MEDICO);
        if (necesitaLote && (lote == null || lote.getFechaVencimiento() == null)) {
            throw new IllegalArgumentException("Las vacunas y medicamentos necesitan lote y fecha de vencimiento");
        }
        if (lote != null && lote.getFechaVencimiento() != null && !lote.getFechaVencimiento().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("No se puede ingresar un lote que ya está vencido");
        }

        Connection con = existenciaDAO.obtenerConexion();
        try {
            con.setAutoCommit(false);
            Integer loteId = lote == null ? null : existenciaDAO.agregarLote(con, lote);
            int existenciaId = existenciaDAO.agregarExistencia(con, productoId, loteId, cantidad);
            movimientoDAO.registrarMovimiento(con, new MovimientoInventario(existenciaId,
                    idTipoMovimiento(con, ENTRADA), SesionUsuario.obtenerInstancia().getUsuarioId(),
                    cantidad, 0, cantidad, "Ingreso de mercadería", null, null));
            con.commit();
        } catch (SQLException | IllegalArgumentException e) {
            con.rollback();
            throw e;
        } finally {
            con.setAutoCommit(true);
            con.close();
        }
    }

    // Descuenta stock dentro de una transacción que ya está abierta.
    // Usa primero los lotes que vencen antes y nunca usa lotes vencidos.
    public List<ConsumoLote> descontar(Connection con, int productoId, int cantidad, String motivo,
            Integer consultaId, Integer procedimientoId) throws SQLException {
        List<Existencia> disponibles = existenciaDAO.listarExistenciasPorProducto(con, productoId).stream()
                .filter(existencia -> !existencia.estaVencido())
                .toList();
        int totalDisponible = disponibles.stream().mapToInt(Existencia::getCantidadStock).sum();
        if (totalDisponible < cantidad) {
            throw new IllegalArgumentException("Stock insuficiente: se necesitan " + cantidad
                    + " y solo hay " + totalDisponible + " disponibles sin vencer");
        }

        int salidaId = idTipoMovimiento(con, SALIDA);
        List<ConsumoLote> consumos = new ArrayList<>();
        int faltante = cantidad;
        for (Existencia existencia : disponibles) {
            if (faltante == 0) {
                break;
            }
            int tomar = Math.min(faltante, existencia.getCantidadStock());
            int stockNuevo = existencia.getCantidadStock() - tomar;
            existenciaDAO.actualizarStock(con, existencia.getId(), stockNuevo);
            movimientoDAO.registrarMovimiento(con, new MovimientoInventario(existencia.getId(), salidaId,
                    SesionUsuario.obtenerInstancia().getUsuarioId(), tomar, existencia.getCantidadStock(),
                    stockNuevo, motivo, consultaId, procedimientoId));
            consumos.add(new ConsumoLote(existencia.getId(), tomar));
            faltante -= tomar;
        }
        return consumos;
    }

    // Avisa a los observadores si algún producto quedó en o por debajo de su stock mínimo
    public void revisarStockBajo(Collection<Integer> productosIds) throws SQLException {
        Map<Integer, Integer> stock = stockDisponiblePorProducto();
        for (int productoId : productosIds) {
            Producto producto = productoDAO.buscarProducto(productoId);
            int disponible = stock.getOrDefault(productoId, 0);
            if (producto != null && disponible <= producto.getStockMinimo()) {
                observadores.forEach(o -> o.alertaStockBajo(producto.getNombre(), disponible, producto.getStockMinimo()));
            }
        }
    }

    public List<Existencia> listarExistencias() throws SQLException {
        return existenciaDAO.listarExistencias();
    }

    public Map<Integer, Integer> stockDisponiblePorProducto() throws SQLException {
        return existenciaDAO.listarExistencias().stream()
                .filter(existencia -> !existencia.estaVencido())
                .collect(Collectors.groupingBy(Existencia::getProductoId,
                        Collectors.summingInt(Existencia::getCantidadStock)));
    }

    public List<Producto> productosBajoMinimo() throws SQLException {
        Map<Integer, Integer> stock = stockDisponiblePorProducto();
        return productoService.listarActivos().stream()
                .filter(producto -> stock.getOrDefault(producto.getId(), 0) <= producto.getStockMinimo())
                .toList();
    }

    public List<Existencia> existenciasPorVencer(int dias) throws SQLException {
        LocalDate limite = LocalDate.now().plusDays(dias);
        return existenciaDAO.listarExistencias().stream()
                .filter(existencia -> existencia.venceAntesDe(limite))
                .toList();
    }

    public List<Existencia> existenciasVencidas() throws SQLException {
        return existenciaDAO.listarExistencias().stream()
                .filter(Existencia::estaVencido)
                .toList();
    }

    // Saca del inventario todos los lotes vencidos y deja registrado el movimiento
    public int retirarVencidos() throws SQLException {
        List<Existencia> vencidas = existenciasVencidas();
        if (vencidas.isEmpty()) {
            throw new IllegalArgumentException("No hay productos vencidos para retirar");
        }
        Connection con = existenciaDAO.obtenerConexion();
        try {
            con.setAutoCommit(false);
            int vencimientoId = idTipoMovimiento(con, VENCIMIENTO);
            for (Existencia existencia : vencidas) {
                existenciaDAO.actualizarStock(con, existencia.getId(), 0);
                movimientoDAO.registrarMovimiento(con, new MovimientoInventario(existencia.getId(), vencimientoId,
                        SesionUsuario.obtenerInstancia().getUsuarioId(), existencia.getCantidadStock(),
                        existencia.getCantidadStock(), 0, "Retiro por vencimiento", null, null));
            }
            con.commit();
        } catch (SQLException | IllegalArgumentException e) {
            con.rollback();
            throw e;
        } finally {
            con.setAutoCommit(true);
            con.close();
        }
        return vencidas.size();
    }

    private int idTipoMovimiento(Connection con, String nombre) throws SQLException {
        return tipoMovimientoDAO.listarTiposMovimientos(con).stream()
                .filter(tipo -> tipo.getNombre().equals(nombre))
                .map(Estado::getId)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "No existe el tipo de movimiento '" + nombre + "'. Ejecute de nuevo data.sql"));
    }
}
