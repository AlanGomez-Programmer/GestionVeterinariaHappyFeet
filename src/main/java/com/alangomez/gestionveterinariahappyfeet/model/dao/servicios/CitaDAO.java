package com.alangomez.gestionveterinariahappyfeet.model.dao.servicios;

import com.alangomez.gestionveterinariahappyfeet.model.dao.OperacionesDAO;
import com.alangomez.gestionveterinariahappyfeet.model.entities.servicios.Cita;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CitaDAO extends OperacionesDAO{

    public boolean agregarCita(Cita cita) throws SQLException{
        String sql = """
                INSERT INTO citas (mascota_id, veterinario_id, estado_id, fecha_hora, motivo)
                VALUES (?, ?, ?, ?, ?)
                """;
        try (
                Connection con = obtenerConexion();
                PreparedStatement ps = con.prepareStatement(sql);
            ){
            ps.setInt(1, cita.getMascotaId());
            ps.setInt(2, cita.getVeterinarioId());
            ps.setInt(3, cita.getEstadoId());
            ps.setObject(4, cita.getFechaHora());
            if (cita.getMotivo() != null && !cita.getMotivo().isBlank()){
                ps.setString(5, cita.getMotivo());
            } else {
                ps.setNull(5, Types.VARCHAR);
            }
            return ps.executeUpdate() == 1;
        }
    }

    public List<Cita> listarCitasPorFecha(LocalDate fecha) throws SQLException{
        String sql = "SELECT * FROM citas WHERE DATE(fecha_hora) = ? ORDER BY fecha_hora";
        List<Cita> lista = new ArrayList<>();
        try (
                Connection con = obtenerConexion();
                PreparedStatement ps = con.prepareStatement(sql);
            ){
            ps.setObject(1, fecha);
            try (ResultSet rs = ps.executeQuery()){
                while(rs.next()){
                    lista.add(mapear(rs));
                }
            }
            return lista;
        }
    }

    public List<Cita> listarCitasPorVeterinario(int veterinarioId) throws SQLException{
        String sql = "SELECT * FROM citas WHERE veterinario_id = ? ORDER BY fecha_hora";
        List<Cita> lista = new ArrayList<>();
        try (
                Connection con = obtenerConexion();
                PreparedStatement ps = con.prepareStatement(sql);
            ){
            ps.setInt(1, veterinarioId);
            try (ResultSet rs = ps.executeQuery()){
                while(rs.next()){
                    lista.add(mapear(rs));
                }
            }
            return lista;
        }
    }

    public List<Cita> listarCitasPorEstado(int estadoId) throws SQLException{
        String sql = "SELECT * FROM citas WHERE estado_id = ? ORDER BY fecha_hora";
        List<Cita> lista = new ArrayList<>();
        try (
                Connection con = obtenerConexion();
                PreparedStatement ps = con.prepareStatement(sql);
            ){
            ps.setInt(1, estadoId);
            try (ResultSet rs = ps.executeQuery()){
                while(rs.next()){
                    lista.add(mapear(rs));
                }
            }
            return lista;
        }
    }

    public Cita buscarCita(int id) throws SQLException{
        String sql = "SELECT * FROM citas WHERE id = ?";
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

    public boolean actualizarEstado(int citaId, int estadoId) throws SQLException{
        try (Connection con = obtenerConexion()){
            return actualizarEstado(con, citaId, estadoId);
        }
    }

    public boolean actualizarEstado(Connection con, int citaId, int estadoId) throws SQLException{
        String sql = "UPDATE citas SET estado_id = ? WHERE id = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)){
            ps.setInt(1, estadoId);
            ps.setInt(2, citaId);
            return ps.executeUpdate() == 1;
        }
    }

    private Cita mapear(ResultSet rs) throws SQLException{
        return new Cita(
                rs.getInt("id"),
                rs.getInt("mascota_id"),
                rs.getInt("veterinario_id"),
                rs.getInt("estado_id"),
                rs.getObject("fecha_hora", LocalDateTime.class),
                rs.getString("motivo"),
                rs.getString("observaciones")
        );
    }

}
