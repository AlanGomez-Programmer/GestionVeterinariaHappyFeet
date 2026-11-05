package com.alangomez.gestionveterinariahappyfeet.model.dao.pacientes;

import com.alangomez.gestionveterinariahappyfeet.model.dao.OperacionesDAO;
import com.alangomez.gestionveterinariahappyfeet.model.entities.pacientes.Duenio;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DuenioDAO extends OperacionesDAO {

    public boolean agregarDuenio(Duenio duenio) throws SQLException {
        String sql = """
            INSERT INTO duenios(nombre_completo, documento_identidad, direccion,
                                 telefono, email, contacto_emergencia)
            VALUES (?, ?, ?, ?, ?, ?)
            """;
        try (Connection con = obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, duenio.getNombreCompleto());
            ps.setString(2, duenio.getDocumentoIdentidad());
            ponerTextoOpcional(ps, 3, duenio.getDireccion());
            ponerTextoOpcional(ps, 4, duenio.getTelefono());
            ps.setString(5, duenio.getEmail());
            ponerTextoOpcional(ps, 6, duenio.getContactoEmergencia());
            return ps.executeUpdate() == 1;
        }
    }

    public List<Duenio> listarDuenios() throws SQLException {
        String sql = "SELECT * FROM duenios";
        List<Duenio> lista = new ArrayList<>();

        try (Connection con = obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(mapear(rs));
            }
            return lista;
        }
    }

    public Duenio buscarDuenio(int id) throws SQLException {
        String sql = "SELECT * FROM duenios WHERE id = ?";

        try (Connection con = obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapear(rs) : null;
            }
        }
    }

    public Duenio buscarPorDocumento(String documento) throws SQLException {
        String sql = "SELECT * FROM duenios WHERE documento_identidad = ?";

        try (Connection con = obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, documento);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapear(rs) : null;
            }
        }
    }

    public boolean desactivarDuenio(int id) throws SQLException {
        String sql = "UPDATE duenios SET activo = FALSE WHERE id = ?";

        try (Connection con = obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() == 1;
        }
    }

    public boolean existeEmail(String email) throws SQLException {
        return existe("SELECT COUNT(*) AS cantidad FROM duenios WHERE email = ?", email);
    }

    public boolean existeDocumentoPersonal(String documentoPersonal) throws SQLException {
        return existe("SELECT COUNT(*) AS cantidad FROM duenios WHERE documento_identidad = ?",
                documentoPersonal);
    }

    private boolean existe(String sql, String valor) throws SQLException {
        try (Connection con = obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, valor);
            try (ResultSet rs = ps.executeQuery()) {
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

    private Duenio mapear(ResultSet rs) throws SQLException {
        return new Duenio(
                rs.getInt("id"),
                rs.getString("nombre_completo"),
                rs.getString("documento_identidad"),
                rs.getString("direccion"),
                rs.getString("telefono"),
                rs.getString("email"),
                rs.getString("contacto_emergencia"),
                rs.getObject("fecha_registro", LocalDateTime.class),
                rs.getBoolean("activo")
        );
    }
}
