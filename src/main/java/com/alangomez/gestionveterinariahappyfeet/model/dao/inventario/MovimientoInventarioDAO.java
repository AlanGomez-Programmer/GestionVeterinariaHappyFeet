package com.alangomez.gestionveterinariahappyfeet.model.dao.inventario;

import com.alangomez.gestionveterinariahappyfeet.model.dao.OperacionesDAO;
import com.alangomez.gestionveterinariahappyfeet.model.entities.inventario.MovimientoInventario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

public class MovimientoInventarioDAO extends OperacionesDAO{

    public boolean registrarMovimiento(Connection con, MovimientoInventario movimiento) throws SQLException{
        String sql = """
                INSERT INTO movimientos_inventario (inventario_id, tipo_movimiento_id, usuario_id, cantidad,
                    stock_anterior, stock_nuevo, motivo, consulta_id, procedimiento_id)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;
        try (PreparedStatement ps = con.prepareStatement(sql)){
            ps.setInt(1, movimiento.getInventarioId());
            ps.setInt(2, movimiento.getTipoMovimientoId());
            ps.setInt(3, movimiento.getUsuarioId());
            ps.setInt(4, movimiento.getCantidad());
            ps.setInt(5, movimiento.getStockAnterior());
            ps.setInt(6, movimiento.getStockNuevo());
            ps.setString(7, movimiento.getMotivo());
            if (movimiento.getConsultaId() != null){
                ps.setInt(8, movimiento.getConsultaId());
            } else {
                ps.setNull(8, Types.INTEGER);
            }
            if (movimiento.getProcedimientoId() != null){
                ps.setInt(9, movimiento.getProcedimientoId());
            } else {
                ps.setNull(9, Types.INTEGER);
            }
            return ps.executeUpdate() == 1;
        }
    }

}
