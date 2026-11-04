package com.alangomez.gestionveterinariahappyfeet.service.inventario;

import com.alangomez.gestionveterinariahappyfeet.model.dao.inventario.DetalleProductoDAO;
import com.alangomez.gestionveterinariahappyfeet.model.dao.inventario.ProductoDAO;
import com.alangomez.gestionveterinariahappyfeet.model.dao.inventario.TipoProductoDAO;
import com.alangomez.gestionveterinariahappyfeet.model.entities.inventario.MaterialMedico;
import com.alangomez.gestionveterinariahappyfeet.model.entities.inventario.Medicamento;
import com.alangomez.gestionveterinariahappyfeet.model.entities.inventario.Producto;
import com.alangomez.gestionveterinariahappyfeet.model.entities.inventario.TipoProducto;
import com.alangomez.gestionveterinariahappyfeet.model.entities.inventario.Vacuna;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class ProductoService {

    public static final String VACUNA = "Vacuna";
    public static final String MEDICAMENTO = "Medicamento";
    public static final String MATERIAL_MEDICO = "Material médico";

    private final ProductoDAO productoDAO;
    private final DetalleProductoDAO detalleDAO;
    private final TipoProductoDAO tipoProductoDAO;

    public ProductoService(ProductoDAO productoDAO, DetalleProductoDAO detalleDAO, TipoProductoDAO tipoProductoDAO) {
        this.productoDAO = productoDAO;
        this.detalleDAO = detalleDAO;
        this.tipoProductoDAO = tipoProductoDAO;
    }

    public void registrarVacuna(Producto producto, Vacuna vacuna) throws SQLException {
        prepararProducto(producto, VACUNA);
        Connection con = productoDAO.obtenerConexion();
        try {
            con.setAutoCommit(false);
            int productoId = productoDAO.agregarProducto(con, producto);
            producto.setId(productoId);
            detalleDAO.agregarVacuna(con, productoId, vacuna);
            con.commit();
        } catch (SQLException e) {
            con.rollback();
            throw e;
        } finally {
            con.setAutoCommit(true);
            con.close();
        }
    }

    public void registrarMedicamento(Producto producto, Medicamento medicamento) throws SQLException {
        prepararProducto(producto, MEDICAMENTO);
        Connection con = productoDAO.obtenerConexion();
        try {
            con.setAutoCommit(false);
            int productoId = productoDAO.agregarProducto(con, producto);
            producto.setId(productoId);
            detalleDAO.agregarMedicamento(con, productoId, medicamento);
            con.commit();
        } catch (SQLException e) {
            con.rollback();
            throw e;
        } finally {
            con.setAutoCommit(true);
            con.close();
        }
    }

    public void registrarMaterialMedico(Producto producto, MaterialMedico material) throws SQLException {
        prepararProducto(producto, MATERIAL_MEDICO);
        Connection con = productoDAO.obtenerConexion();
        try {
            con.setAutoCommit(false);
            int productoId = productoDAO.agregarProducto(con, producto);
            producto.setId(productoId);
            detalleDAO.agregarMaterialMedico(con, productoId, material);
            con.commit();
        } catch (SQLException e) {
            con.rollback();
            throw e;
        } finally {
            con.setAutoCommit(true);
            con.close();
        }
    }

    public List<Producto> listarActivos() throws SQLException {
        return productoDAO.listarProductos().stream()
                .filter(Producto::isActivo)
                .toList();
    }

    public List<Producto> listarTodos() throws SQLException {
        return productoDAO.listarProductos();
    }

    public Producto buscarPorId(int id) throws SQLException {
        Producto producto = productoDAO.buscarProducto(id);
        if (producto == null) {
            throw new IllegalArgumentException("No existe un producto con el id " + id);
        }
        return producto;
    }

    public Producto buscarActivoPorId(int id) throws SQLException {
        Producto producto = buscarPorId(id);
        if (!producto.isActivo()) {
            throw new IllegalArgumentException("El producto " + producto.getNombre() + " está inactivo");
        }
        return producto;
    }

    public List<Medicamento> listarMedicamentos() throws SQLException {
        return detalleDAO.listarMedicamentos();
    }

    public String nombreTipo(int tipoProductoId) throws SQLException {
        return tipoProductoDAO.listarTiposProductos().stream()
                .filter(tipo -> tipo.getId() == tipoProductoId)
                .map(TipoProducto::getNombre)
                .findFirst()
                .orElse("-");
    }

    public void desactivar(int id) throws SQLException {
        buscarActivoPorId(id);
        productoDAO.desactivarProducto(id);
    }

    private void prepararProducto(Producto producto, String tipo) throws SQLException {
        if (productoDAO.existeProducto(producto.getNombre())) {
            throw new IllegalArgumentException("Ya existe un producto llamado " + producto.getNombre());
        }
        if (producto.getPrecioVenta() == null || producto.getPrecioVenta().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El precio de venta no puede ser negativo");
        }
        if (producto.getStockMinimo() < 0) {
            throw new IllegalArgumentException("El stock mínimo no puede ser negativo");
        }
        int tipoId = tipoProductoDAO.listarTiposProductos().stream()
                .filter(t -> t.getNombre().equals(tipo))
                .map(TipoProducto::getId)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "No existe el tipo de producto '" + tipo + "'. Ejecute de nuevo data.sql"));
        producto.setTipoProductoId(tipoId);
    }
}
