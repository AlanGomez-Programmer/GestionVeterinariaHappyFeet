package com.alangomez.gestionveterinariahappyfeet.model.dao.pacientes;

import com.alangomez.gestionveterinariahappyfeet.model.dao.OperacionesDAO;
import com.alangomez.gestionveterinariahappyfeet.model.entities.pacientes.ProcedimientoQuirurgicoPrevio;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ProcedimientoQuirurgicoPrevioDAO extends OperacionesDAO{

    public boolean agregarProcedimientoPrevio(ProcedimientoQuirurgicoPrevio procedimiento) throws SQLException{
        String sql = """
                INSERT INTO procedimientos_quirurgicos_previos
                    (mascota_id, tipo_procedimiento_id, descripcion, fecha_aproximada)
                VALUES (?, ?, ?, ?)
                """;
        try (
                Connection con = obtenerConexion();
                PreparedStatement ps = con.prepareStatement(sql);
            ){
            ps.setInt(1, procedimiento.getMascotaId());
            ps.setInt(2, procedimiento.getTipoProcedimientoId());
            if (procedimiento.getDescripcion() != null && !procedimiento.getDescripcion().isBlank()){
                ps.setString(3, procedimiento.getDescripcion());
            } else {
                ps.setNull(3, Types.VARCHAR);
            }
            if (procedimiento.getFechaAproximada() != null){
                ps.setObject(4, procedimiento.getFechaAproximada());
            } else {
                ps.setNull(4, Types.DATE);
            }
            return ps.executeUpdate() == 1;
        }
    }

    public List<ProcedimientoQuirurgicoPrevio> listarProcedimientosPorMascota(int mascotaId) throws SQLException{
        String sql = "SELECT * FROM procedimientos_quirurgicos_previos WHERE mascota_id = ? ORDER BY fecha_aproximada";
        List<ProcedimientoQuirurgicoPrevio> lista = new ArrayList<>();
        try (
                Connection con = obtenerConexion();
                PreparedStatement ps = con.prepareStatement(sql);
            ){
            ps.setInt(1, mascotaId);
            try (ResultSet rs = ps.executeQuery()){
                while(rs.next()){
                    lista.add(mapear(rs));
                }
            }
            return lista;
        }
    }

    private ProcedimientoQuirurgicoPrevio mapear(ResultSet rs) throws SQLException{
        return new ProcedimientoQuirurgicoPrevio(
                rs.getInt("id"),
                rs.getInt("mascota_id"),
                rs.getInt("tipo_procedimiento_id"),
                rs.getString("descripcion"),
                rs.getObject("fecha_aproximada", LocalDate.class)
        );
    }

}
