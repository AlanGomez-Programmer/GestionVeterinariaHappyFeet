package com.alangomez.gestionveterinariahappyfeet.model.dao.inventario;

import com.alangomez.gestionveterinariahappyfeet.model.dao.OperacionesDAO;
import com.alangomez.gestionveterinariahappyfeet.model.entities.inventario.Existencia;
import com.alangomez.gestionveterinariahappyfeet.model.entities.inventario.Lote;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ExistenciaDAO extends OperacionesDAO{

    private static final String CONSULTA_BASE = """
            SELECT i.id, i.producto_id, i.cantidad_stock, l.codigo, l.fecha_vencimiento
            FROM inventario i
            LEFT JOIN lotes l ON l.id = i.lote_id
            """;

    public int agregarLote(Connection con, Lote lote) throws SQLException{
        String sql = "INSERT INTO lotes (codigo, fecha_fabricacion, fecha_vencimiento) VALUES (?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
            ps.setString(1, lote.getCodigo());
            ponerFechaOpcional(ps, 2, lote.getFechaFabricacion());
            ponerFechaOpcional(ps, 3, lote.getFechaVencimiento());
            ps.executeUpdate();
            try (ResultSet llaves = ps.getGeneratedKeys()){
                if (llaves.next()){
                    return llaves.getInt(1);
                }
                throw new SQLException("No se obtuvo el id del lote registrado");
            }
        }
    }

    public int agregarExistencia(Connection con, int productoId, Integer loteId, int cantidad) throws SQLException{
        String sql = "INSERT INTO inventario (producto_id, lote_id, cantidad_stock) VALUES (?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
            ps.setInt(1, productoId);
            if (loteId != null){
                ps.setInt(2, loteId);
            } else {
                ps.setNull(2, Types.INTEGER);
            }
            ps.setInt(3, cantidad);
            ps.executeUpdate();
            try (ResultSet llaves = ps.getGeneratedKeys()){
                if (llaves.next()){
                    return llaves.getInt(1);
                }
                throw new SQLException("No se obtuvo el id del inventario registrado");
            }
        }
    }

    public List<Existencia> listarExistencias() throws SQLException{
        String sql = CONSULTA_BASE + " WHERE i.cantidad_stock > 0 ORDER BY i.producto_id, l.fecha_vencimiento";
        try (Connection con = obtenerConexion()){
            return listar(con, sql, null);
        }
    }

    // Lotes con stock de un producto, el que vence primero va primero (los sin fecha al final)
    public List<Existencia> listarExistenciasPorProducto(Connection con, int productoId) throws SQLException{
        String sql = CONSULTA_BASE + """
                 WHERE i.producto_id = ? AND i.cantidad_stock > 0
                 ORDER BY l.fecha_vencimiento IS NULL, l.fecha_vencimiento
                """;
        return listar(con, sql, productoId);
    }

    public boolean actualizarStock(Connection con, int existenciaId, int nuevaCantidad) throws SQLException{
        String sql = "UPDATE inventario SET cantidad_stock = ? WHERE id = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)){
            ps.setInt(1, nuevaCantidad);
            ps.setInt(2, existenciaId);
            return ps.executeUpdate() == 1;
        }
    }

    private List<Existencia> listar(Connection con, String sql, Integer productoId) throws SQLException{
        List<Existencia> lista = new ArrayList<>();
        try (PreparedStatement ps = con.prepareStatement(sql)){
            if (productoId != null){
                ps.setInt(1, productoId);
            }
            try (ResultSet rs = ps.executeQuery()){
                while(rs.next()){
                    lista.add(new Existencia(
                            rs.getInt("id"),
                            rs.getInt("producto_id"),
                            rs.getString("codigo"),
                            rs.getObject("fecha_vencimiento", LocalDate.class),
                            rs.getInt("cantidad_stock")
                    ));
                }
            }
        }
        return lista;
    }

    private void ponerFechaOpcional(PreparedStatement ps, int posicion, LocalDate fecha) throws SQLException {
        if (fecha != null) {
            ps.setObject(posicion, fecha);
        } else {
            ps.setNull(posicion, Types.DATE);
        }
    }

}
