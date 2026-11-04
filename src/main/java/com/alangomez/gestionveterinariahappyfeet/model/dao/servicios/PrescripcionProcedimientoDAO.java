package com.alangomez.gestionveterinariahappyfeet.model.dao.servicios;

import com.alangomez.gestionveterinariahappyfeet.model.dao.OperacionesDAO;
import com.alangomez.gestionveterinariahappyfeet.model.entities.servicios.PrescripcionProcedimiento;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PrescripcionProcedimientoDAO extends OperacionesDAO{

    public boolean agregarPrescripcion(PrescripcionProcedimiento prescripcion) throws SQLException{
        String sql = """
                INSERT INTO prescripciones_procedimientos (consulta_id, catalogo_procedimiento_id, indicaciones)
                VALUES (?, ?, ?)
                """;
        try (
                Connection con = obtenerConexion();
                PreparedStatement ps = con.prepareStatement(sql);
            ){
            ps.setInt(1, prescripcion.getConsultaId());
            ps.setInt(2, prescripcion.getCatalogoProcedimientoId());
            if (prescripcion.getIndicaciones() != null && !prescripcion.getIndicaciones().isBlank()){
                ps.setString(3, prescripcion.getIndicaciones());
            } else {
                ps.setNull(3, Types.VARCHAR);
            }
            return ps.executeUpdate() == 1;
        }
    }

    public List<PrescripcionProcedimiento> listarPrescripcionesPendientes() throws SQLException{
        String sql = """
                SELECT p.* FROM prescripciones_procedimientos p
                LEFT JOIN procedimientos_especiales pe ON pe.prescripcion_id = p.id
                WHERE pe.id IS NULL
                ORDER BY p.fecha_prescripcion
                """;
        List<PrescripcionProcedimiento> lista = new ArrayList<>();
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

    public PrescripcionProcedimiento buscarPrescripcion(int id) throws SQLException{
        String sql = "SELECT * FROM prescripciones_procedimientos WHERE id = ?";
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

    private PrescripcionProcedimiento mapear(ResultSet rs) throws SQLException{
        return new PrescripcionProcedimiento(
                rs.getInt("id"),
                rs.getInt("consulta_id"),
                rs.getInt("catalogo_procedimiento_id"),
                rs.getString("indicaciones"),
                rs.getObject("fecha_prescripcion", LocalDateTime.class)
        );
    }

}
