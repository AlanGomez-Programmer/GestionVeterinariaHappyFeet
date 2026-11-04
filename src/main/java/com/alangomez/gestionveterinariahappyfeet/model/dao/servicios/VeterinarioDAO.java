package com.alangomez.gestionveterinariahappyfeet.model.dao.servicios;

import com.alangomez.gestionveterinariahappyfeet.model.dao.OperacionesDAO;
import com.alangomez.gestionveterinariahappyfeet.model.entities.servicios.Veterinario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class VeterinarioDAO extends OperacionesDAO{

    public boolean agregarVeterinario(Veterinario veterinario) throws SQLException{
        String sql = """
                INSERT INTO veterinarios (especialidad_id, nombre_completo, documento_identidad,
                    licencia_profesional, telefono, email, fecha_contratacion)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;
        try (
                Connection con = obtenerConexion();
                PreparedStatement ps = con.prepareStatement(sql);
            ){
            ps.setInt(1, veterinario.getEspecialidadId());
            ps.setString(2, veterinario.getNombreCompleto());
            ps.setString(3, veterinario.getDocumentoIdentidad());
            ps.setString(4, veterinario.getLicenciaProfesional());
            ponerTextoOpcional(ps, 5, veterinario.getTelefono());
            ponerTextoOpcional(ps, 6, veterinario.getEmail());
            if (veterinario.getFechaContratacion() != null){
                ps.setObject(7, veterinario.getFechaContratacion());
            } else {
                ps.setNull(7, Types.DATE);
            }
            return ps.executeUpdate() == 1;
        }
    }

    public List<Veterinario> listarVeterinarios() throws SQLException{
        String sql = "SELECT * FROM veterinarios ORDER BY nombre_completo";
        List<Veterinario> lista = new ArrayList<>();
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

    public Veterinario buscarVeterinario(int id) throws SQLException{
        String sql = "SELECT * FROM veterinarios WHERE id = ?";
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

    public boolean existeDocumento(String documento) throws SQLException{
        return existe("SELECT COUNT(*) AS cantidad FROM veterinarios WHERE documento_identidad = ?", documento);
    }

    public boolean existeLicencia(String licencia) throws SQLException{
        return existe("SELECT COUNT(*) AS cantidad FROM veterinarios WHERE licencia_profesional = ?", licencia);
    }

    public boolean existeEmail(String email) throws SQLException{
        return existe("SELECT COUNT(*) AS cantidad FROM veterinarios WHERE email = ?", email);
    }

    public boolean desactivarVeterinario(int id) throws SQLException{
        String sql = "UPDATE veterinarios SET activo = FALSE WHERE id = ?";
        try (
                Connection con = obtenerConexion();
                PreparedStatement ps = con.prepareStatement(sql);
            ){
            ps.setInt(1, id);
            return ps.executeUpdate() == 1;
        }
    }

    private boolean existe(String sql, String valor) throws SQLException{
        try (
                Connection con = obtenerConexion();
                PreparedStatement ps = con.prepareStatement(sql);
            ){
            ps.setString(1, valor);
            try (ResultSet rs = ps.executeQuery()){
                return rs.next() && rs.getInt("cantidad") > 0;
            }
        }
    }

    private void ponerTextoOpcional(PreparedStatement ps, int posicion, String valor) throws SQLException {
        if (valor != null && !valor.isBlank()) {
            ps.setString(posicion, valor);
        } else {
            ps.setNull(posicion, Types.VARCHAR);
        }
    }

    private Veterinario mapear(ResultSet rs) throws SQLException{
        return new Veterinario(
                rs.getInt("id"),
                rs.getInt("especialidad_id"),
                rs.getString("nombre_completo"),
                rs.getString("documento_identidad"),
                rs.getString("licencia_profesional"),
                rs.getString("telefono"),
                rs.getString("email"),
                rs.getObject("fecha_contratacion", LocalDate.class),
                rs.getBoolean("activo")
        );
    }

}
