package com.alangomez.gestionveterinariahappyfeet.model.dao.servicios;

import com.alangomez.gestionveterinariahappyfeet.model.dao.OperacionesDAO;
import com.alangomez.gestionveterinariahappyfeet.model.entities.servicios.ProcedimientoEspecial;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ProcedimientoEspecialDAO extends OperacionesDAO{

    public boolean agregarProcedimiento(ProcedimientoEspecial procedimiento) throws SQLException{
        String sql = """
                INSERT INTO procedimientos_especiales (mascota_id, veterinario_id, catalogo_procedimiento_id,
                    prescripcion_id, estado_id, fecha_hora)
                VALUES (?, ?, ?, ?, ?, ?)
                """;
        try (
                Connection con = obtenerConexion();
                PreparedStatement ps = con.prepareStatement(sql);
            ){
            ps.setInt(1, procedimiento.getMascotaId());
            ps.setInt(2, procedimiento.getVeterinarioId());
            ps.setInt(3, procedimiento.getCatalogoProcedimientoId());
            if (procedimiento.getPrescripcionId() != null){
                ps.setInt(4, procedimiento.getPrescripcionId());
            } else {
                ps.setNull(4, Types.INTEGER);
            }
            ps.setInt(5, procedimiento.getEstadoId());
            ps.setObject(6, procedimiento.getFechaHora());
            return ps.executeUpdate() == 1;
        }
    }

    public List<ProcedimientoEspecial> listarProcedimientos() throws SQLException{
        String sql = "SELECT * FROM procedimientos_especiales ORDER BY fecha_hora DESC";
        List<ProcedimientoEspecial> lista = new ArrayList<>();
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

    public ProcedimientoEspecial buscarProcedimiento(int id) throws SQLException{
        String sql = "SELECT * FROM procedimientos_especiales WHERE id = ?";
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

    public boolean actualizarProcedimiento(Connection con, ProcedimientoEspecial procedimiento) throws SQLException{
        String sql = """
                UPDATE procedimientos_especiales
                SET estado_id = ?, informacion_preoperatoria = ?, detalle_procedimiento = ?,
                    complicaciones = ?, seguimiento_postoperatorio = ?, proximo_control = ?
                WHERE id = ?
                """;
        try (PreparedStatement ps = con.prepareStatement(sql)){
            ps.setInt(1, procedimiento.getEstadoId());
            ponerTextoOpcional(ps, 2, procedimiento.getInformacionPreoperatoria());
            ponerTextoOpcional(ps, 3, procedimiento.getDetalleProcedimiento());
            ponerTextoOpcional(ps, 4, procedimiento.getComplicaciones());
            ponerTextoOpcional(ps, 5, procedimiento.getSeguimientoPostoperatorio());
            if (procedimiento.getProximoControl() != null){
                ps.setObject(6, procedimiento.getProximoControl());
            } else {
                ps.setNull(6, Types.DATE);
            }
            ps.setInt(7, procedimiento.getId());
            return ps.executeUpdate() == 1;
        }
    }

    private void ponerTextoOpcional(PreparedStatement ps, int posicion, String valor) throws SQLException {
        if (valor != null && !valor.isBlank()) {
            ps.setString(posicion, valor);
        } else {
            ps.setNull(posicion, Types.VARCHAR);
        }
    }

    private ProcedimientoEspecial mapear(ResultSet rs) throws SQLException{
        return new ProcedimientoEspecial(
                rs.getInt("id"),
                rs.getInt("mascota_id"),
                rs.getInt("veterinario_id"),
                rs.getInt("catalogo_procedimiento_id"),
                rs.getObject("prescripcion_id", Integer.class),
                rs.getInt("estado_id"),
                rs.getObject("fecha_hora", LocalDateTime.class),
                rs.getString("informacion_preoperatoria"),
                rs.getString("detalle_procedimiento"),
                rs.getString("complicaciones"),
                rs.getString("seguimiento_postoperatorio"),
                rs.getObject("proximo_control", LocalDate.class)
        );
    }

}
