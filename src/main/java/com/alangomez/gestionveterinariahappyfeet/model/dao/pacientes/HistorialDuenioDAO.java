package com.alangomez.gestionveterinariahappyfeet.model.dao.pacientes;

import com.alangomez.gestionveterinariahappyfeet.model.dao.OperacionesDAO;
import com.alangomez.gestionveterinariahappyfeet.model.entities.pacientes.HistorialDuenio;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class HistorialDuenioDAO extends OperacionesDAO{

    public boolean registrarDuenio(Connection con, HistorialDuenio historial) throws SQLException{
        String sql = """
                INSERT INTO historial_duenios (mascota_id, duenio_id, motivo)
                VALUES (?, ?, ?)
                """;
        try (PreparedStatement ps = con.prepareStatement(sql)){
            ps.setInt(1, historial.getMascotaId());
            ps.setInt(2, historial.getDuenioId());
            ps.setString(3, historial.getMotivo());
            return ps.executeUpdate() == 1;
        }
    }

    public boolean cerrarDuenioActual(Connection con, int mascotaId) throws SQLException{
        String sql = """
                UPDATE historial_duenios SET fecha_fin = NOW()
                WHERE mascota_id = ? AND fecha_fin IS NULL
                """;
        try (PreparedStatement ps = con.prepareStatement(sql)){
            ps.setInt(1, mascotaId);
            return ps.executeUpdate() >= 1;
        }
    }

    public List<HistorialDuenio> listarHistorialPorMascota(int mascotaId) throws SQLException{
        String sql = "SELECT * FROM historial_duenios WHERE mascota_id = ? ORDER BY fecha_inicio";
        List<HistorialDuenio> lista = new ArrayList<>();
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

    private HistorialDuenio mapear(ResultSet rs) throws SQLException{
        return new HistorialDuenio(
                rs.getInt("id"),
                rs.getInt("mascota_id"),
                rs.getInt("duenio_id"),
                rs.getObject("fecha_inicio", LocalDateTime.class),
                rs.getObject("fecha_fin", LocalDateTime.class),
                rs.getString("motivo")
        );
    }

}
