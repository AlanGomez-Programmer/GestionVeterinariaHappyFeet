package com.alangomez.gestionveterinariahappyfeet.model.dao.inventario;

import com.alangomez.gestionveterinariahappyfeet.model.dao.OperacionesDAO;
import com.alangomez.gestionveterinariahappyfeet.model.entities.inventario.Producto;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAO extends OperacionesDAO{

    public int agregarProducto(Connection con, Producto producto) throws SQLException{
        String sql = """
                INSERT INTO productos (tipo_producto_id, fabricante_id, proveedor_id, nombre, descripcion,
                    unidad_medida, stock_minimo, precio_compra, precio_venta, requiere_receta)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;
        try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
            ps.setInt(1, producto.getTipoProductoId());
            ponerEnteroOpcional(ps, 2, producto.getFabricanteId());
            ponerEnteroOpcional(ps, 3, producto.getProveedorId());
            ps.setString(4, producto.getNombre());
            if (producto.getDescripcion() != null && !producto.getDescripcion().isBlank()){
                ps.setString(5, producto.getDescripcion());
            } else {
                ps.setNull(5, Types.VARCHAR);
            }
            ps.setString(6, producto.getUnidadMedida());
            ps.setInt(7, producto.getStockMinimo());
            if (producto.getPrecioCompra() != null){
                ps.setBigDecimal(8, producto.getPrecioCompra());
            } else {
                ps.setNull(8, Types.DECIMAL);
            }
            ps.setBigDecimal(9, producto.getPrecioVenta());
            ps.setBoolean(10, producto.isRequiereReceta());
            ps.executeUpdate();

            try (ResultSet llaves = ps.getGeneratedKeys()){
                if (llaves.next()){
                    return llaves.getInt(1);
                }
                throw new SQLException("No se obtuvo el id del producto registrado");
            }
        }
    }

    public List<Producto> listarProductos() throws SQLException{
        String sql = "SELECT * FROM productos ORDER BY tipo_producto_id, nombre";
        List<Producto> lista = new ArrayList<>();
        try (
                Connection con = obtenerConexion();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery();
            ){
            while(rs.next()){
                lista.add(mapear(rs));
            }
            return lista;
        }
    }

    public Producto buscarProducto(int id) throws SQLException{
        String sql = "SELECT * FROM productos WHERE id = ?";
        try (
                Connection con = obtenerConexion();
                PreparedStatement ps = con.prepareStatement(sql);
            ){
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()){
                return rs.next() ? mapear(rs) : null;
            }
        }
    }

    public boolean existeProducto(String nombre) throws SQLException{
        String sql = "SELECT COUNT(*) AS cantidad FROM productos WHERE nombre = ?";
        try (
                Connection con = obtenerConexion();
                PreparedStatement ps = con.prepareStatement(sql);
            ){
            ps.setString(1, nombre);
            try (ResultSet rs = ps.executeQuery()){
                return rs.next() && rs.getInt("cantidad") > 0;
            }
        }
    }

    public boolean desactivarProducto(int id) throws SQLException{
        String sql = "UPDATE productos SET activo = FALSE WHERE id = ?";
        try (
                Connection con = obtenerConexion();
                PreparedStatement ps = con.prepareStatement(sql);
            ){
            ps.setInt(1, id);
            return ps.executeUpdate() == 1;
        }
    }

    private void ponerEnteroOpcional(PreparedStatement ps, int posicion, Integer valor) throws SQLException {
        if (valor != null) {
            ps.setInt(posicion, valor);
        } else {
            ps.setNull(posicion, Types.INTEGER);
        }
    }

    private Producto mapear(ResultSet rs) throws SQLException{
        BigDecimal precioCompra = rs.getBigDecimal("precio_compra");
        return new Producto(
                rs.getInt("id"),
                rs.getInt("tipo_producto_id"),
                rs.getObject("fabricante_id", Integer.class),
                rs.getObject("proveedor_id", Integer.class),
                rs.getString("nombre"),
                rs.getString("descripcion"),
                rs.getString("unidad_medida"),
                rs.getInt("stock_minimo"),
                precioCompra,
                rs.getBigDecimal("precio_venta"),
                rs.getBoolean("requiere_receta"),
                rs.getBoolean("activo")
        );
    }

}
