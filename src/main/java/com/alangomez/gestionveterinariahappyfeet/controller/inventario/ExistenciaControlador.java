package com.alangomez.gestionveterinariahappyfeet.controller.inventario;

import com.alangomez.gestionveterinariahappyfeet.controller.ControladorBase;
import com.alangomez.gestionveterinariahappyfeet.model.entities.inventario.Lote;
import com.alangomez.gestionveterinariahappyfeet.model.entities.inventario.Producto;
import com.alangomez.gestionveterinariahappyfeet.service.inventario.InventarioService;
import com.alangomez.gestionveterinariahappyfeet.service.inventario.ProductoService;
import com.alangomez.gestionveterinariahappyfeet.view.inventario.VistaExistencias;
import java.sql.SQLException;
import java.util.Map;

public class ExistenciaControlador extends ControladorBase {

    private final VistaExistencias vista;
    private final InventarioService inventarioService;
    private final ProductoService productoService;

    public ExistenciaControlador(VistaExistencias vista, InventarioService inventarioService,
            ProductoService productoService) {
        super(vista);
        this.vista = vista;
        this.inventarioService = inventarioService;
        this.productoService = productoService;
    }

    public void iniciar() {
        int opcion;
        do {
            opcion = vista.menuExistencias();
            try {
                switch (opcion) {
                    case 1 -> registrarEntrada();
                    case 2 -> vista.mostrarExistencias("Inventario por lote",
                            inventarioService.listarExistencias(), nombresProductos());
                    case 3 -> verAlertas();
                    case 4 -> retirarVencidos();
                    default -> { }
                }
            } catch (IllegalArgumentException e) {
                vista.mostrarError(e.getMessage());
            } catch (SQLException e) {
                manejarErrorBaseDatos("Menú de stock, opción " + opcion, e);
            }
        } while (opcion != 0);
    }

    private void registrarEntrada() throws SQLException {
        int productoId = elegir("Productos", aMapa(productoService.listarActivos(), Producto::getId, Producto::getNombre));
        Producto producto = productoService.buscarActivoPorId(productoId);
        boolean esMaterial = productoService.nombreTipo(producto.getTipoProductoId()).equals(ProductoService.MATERIAL_MEDICO);
        Lote lote = null;
        if (!esMaterial || vista.tieneLote()) {
            lote = vista.pedirDatosLote();
        }
        int cantidad = vista.pedirCantidad("Cantidad que ingresa");
        inventarioService.registrarEntrada(productoId, lote, cantidad);
        vista.mostrarMensaje("Entrada registrada: " + cantidad + " " + producto.getUnidadMedida() + " de " + producto.getNombre());
    }

    private void verAlertas() throws SQLException {
        Map<Integer, String> productos = nombresProductos();
        vista.mostrarStockBajo(inventarioService.productosBajoMinimo(), inventarioService.stockDisponiblePorProducto());
        int dias = vista.pedirDiasAviso();
        vista.mostrarExistencias("Productos que vencen en los próximos " + dias + " días",
                inventarioService.existenciasPorVencer(dias), productos);
        vista.mostrarExistencias("Productos vencidos (no se pueden usar)",
                inventarioService.existenciasVencidas(), productos);
    }

    private void retirarVencidos() throws SQLException {
        if (vista.confirmar("¿Desea retirar del inventario todos los lotes vencidos?")) {
            int retirados = inventarioService.retirarVencidos();
            vista.mostrarMensaje("Se retiraron " + retirados + " lote(s) vencido(s)");
        }
    }

    private Map<Integer, String> nombresProductos() throws SQLException {
        return aMapa(productoService.listarTodos(), Producto::getId, Producto::getNombre);
    }
}
