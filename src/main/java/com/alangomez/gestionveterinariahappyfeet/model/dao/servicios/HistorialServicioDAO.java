package com.alangomez.gestionveterinariahappyfeet.model.dao.servicios;

import com.alangomez.gestionveterinariahappyfeet.model.dao.OperacionesDAO;
import com.alangomez.gestionveterinariahappyfeet.model.entities.servicios.HistorialServicio;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class HistorialServicioDAO extends OperacionesDAO{

    public int agregarHistorial(Connection con, HistorialServicio historial) throws SQLException{
        String sql = """
                INSERT INTO historial_servicios (servicio_id, mascota_id, veterinario_id, precio_aplicado)
                VALUES (?, ?, ?, ?)
                """;
        try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
            ps.setInt(1, historial.getServicioId());
            ps.setInt(2, historial.getMascotaId());
            ps.setInt(3, historial.getVeterinarioId());
            ps.setBigDecimal(4, historial.getPrecioAplicado());
            ps.executeUpdate();

            try (ResultSet llaves = ps.getGeneratedKeys()){
                if (llaves.next()){
                    return llaves.getInt(1);
                }
                throw new SQLException("No se obtuvo el id del historial de servicio");
            }
        }
    }

    public boolean vincularConConsulta(Connection con, int historialId, int consultaId) throws SQLException{
        String sql = "INSERT INTO historial_servicio_consulta (historial_servicio_id, consulta_id) VALUES (?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql)){
            ps.setInt(1, historialId);
            ps.setInt(2, consultaId);
            return ps.executeUpdate() == 1;
        }
    }

    public boolean vincularConProcedimiento(Connection con, int historialId, int procedimientoId) throws SQLException{
        String sql = "INSERT INTO historial_servicio_procedimiento_especial (historial_servicio_id, procedimiento_id) VALUES (?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql)){
            ps.setInt(1, historialId);
            ps.setInt(2, procedimientoId);
            return ps.executeUpdate() == 1;
        }
    }

    public List<HistorialServicio> listarServiciosPorConsulta(int consultaId) throws SQLException{
        String sql = """
                SELECT h.* FROM historial_servicios h
                JOIN historial_servicio_consulta hc ON hc.historial_servicio_id = h.id
                WHERE hc.consulta_id = ?
                """;
        List<HistorialServicio> lista = new ArrayList<>();
        try (
                Connection con = obtenerConexion();
                PreparedStatement ps = con.prepareStatement(sql);
            ){
            ps.setInt(1, consultaId);
            try (ResultSet rs = ps.executeQuery()){
                while(rs.next()){
                    lista.add(mapear(rs));
                }
            }
            return lista;
        }
    }

    private HistorialServicio mapear(ResultSet rs) throws SQLException{
        return new HistorialServicio(
                rs.getInt("id"),
                rs.getInt("servicio_id"),
                rs.getInt("mascota_id"),
                rs.getInt("veterinario_id"),
                rs.getObject("fecha", LocalDateTime.class),
                rs.getBigDecimal("precio_aplicado"),
                rs.getString("observaciones")
        );
    }

}
