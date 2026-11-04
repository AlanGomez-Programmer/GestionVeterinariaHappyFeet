package com.alangomez.gestionveterinariahappyfeet.model.dao.pacientes;

import com.alangomez.gestionveterinariahappyfeet.model.dao.OperacionesDAO;
import com.alangomez.gestionveterinariahappyfeet.model.entities.pacientes.mascota.Mascota;
import com.alangomez.gestionveterinariahappyfeet.model.entities.pacientes.mascota.MascotaBuilderImpl;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MascotaDAO extends OperacionesDAO{

    public int agregarMascota(Connection con, Mascota mascota) throws SQLException{
        String sql = """
                INSERT INTO mascotas (
                    duenio_id, raza_id, sexo_id, nombre, fecha_nacimiento,
                    edad_estimada_anios, peso_actual, microchip, tatuaje, url_foto,
                    condiciones_preexistentes)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;
        try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
            ps.setInt(1, mascota.getDuenioId());
            ps.setInt(2, mascota.getRazaId());
            ps.setInt(3, mascota.getSexoId());
            ps.setString(4, mascota.getNombre());

            if (mascota.getFechaNacimiento() != null){
                ps.setObject(5, mascota.getFechaNacimiento());
            } else {
                ps.setNull(5, Types.DATE);
            }
            if (mascota.getEdadEstimada() != null){
                ps.setInt(6, mascota.getEdadEstimada());
            } else {
                ps.setNull(6, Types.INTEGER);
            }
            if (mascota.getPesoActual() != null){
                ps.setDouble(7, mascota.getPesoActual());
            } else {
                ps.setNull(7, Types.DECIMAL);
            }
            ponerTextoOpcional(ps, 8, mascota.getMicrochip());
            ponerTextoOpcional(ps, 9, mascota.getTatuaje());
            ponerTextoOpcional(ps, 10, mascota.getUrlFoto());
            ponerTextoOpcional(ps, 11, mascota.getCondicionesPreexistentes());
            ps.executeUpdate();

            try (ResultSet llaves = ps.getGeneratedKeys()){
                if (llaves.next()){
                    return llaves.getInt(1);
                }
                throw new SQLException("No se obtuvo el id de la mascota registrada");
            }
        }
    }

    public List<Mascota> listarMascotasActivas() throws SQLException{
        String sql = "SELECT * FROM mascotas WHERE activo = TRUE ORDER BY nombre";
        List<Mascota> lista = new ArrayList<>();
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

    public List<Mascota> listarMascotas() throws SQLException{
        String sql = "SELECT * FROM mascotas ORDER BY nombre";
        List<Mascota> lista = new ArrayList<>();
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

    public Mascota buscarMascota(int id) throws SQLException{
        String sql = "SELECT * FROM mascotas WHERE id = ?";
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

    public int contarMascotasActivasPorDuenio(int duenioId) throws SQLException{
        String sql = "SELECT COUNT(*) AS cantidad FROM mascotas WHERE duenio_id = ? AND activo = TRUE";
        try (
                Connection con = obtenerConexion();
                PreparedStatement ps = con.prepareStatement(sql);
            ){
            ps.setInt(1, duenioId);
            try (ResultSet rs = ps.executeQuery()){
                return rs.next() ? rs.getInt("cantidad") : 0;
            }
        }
    }

    public boolean existeMicrochip(String microchip) throws SQLException {
        String sql = "SELECT COUNT(*) AS cantidad FROM mascotas WHERE microchip = ?";
        try (
                Connection con = obtenerConexion();
                PreparedStatement ps = con.prepareStatement(sql)
            ){
            ps.setString(1, microchip);
            try (ResultSet rs = ps.executeQuery()){
                return rs.next() && rs.getInt("cantidad") > 0;
            }
        }
    }

    public boolean actualizarDuenio(Connection con, int mascotaId, int duenioId) throws SQLException{
        String sql = "UPDATE mascotas SET duenio_id = ? WHERE id = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)){
            ps.setInt(1, duenioId);
            ps.setInt(2, mascotaId);
            return ps.executeUpdate() == 1;
        }
    }

    public boolean actualizarPeso(int id, double peso) throws SQLException{
        try (Connection con = obtenerConexion()){
            return actualizarPeso(con, id, peso);
        }
    }

    public boolean actualizarPeso(Connection con, int id, double peso) throws SQLException{
        String sql = "UPDATE mascotas SET peso_actual = ? WHERE id = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)){
            ps.setDouble(1, peso);
            ps.setInt(2, id);
            return ps.executeUpdate() == 1;
        }
    }

    public boolean desactivarMascota(int id) throws SQLException{
        String sql = "UPDATE mascotas SET activo = FALSE WHERE id = ?";
        try (
                Connection con = obtenerConexion();
                PreparedStatement ps = con.prepareStatement(sql);
            ){
            ps.setInt(1, id);
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

    private Mascota mapear(ResultSet rs) throws SQLException{
        BigDecimal peso = rs.getBigDecimal("peso_actual");
        return new MascotaBuilderImpl()
                    .id(rs.getInt("id"))
                    .duenioId(rs.getInt("duenio_id"))
                    .razaId(rs.getInt("raza_id"))
                    .sexoId(rs.getInt("sexo_id"))
                    .nombre(rs.getString("nombre"))
                    .fechaNacimiento(rs.getObject("fecha_nacimiento", LocalDate.class))
                    .edadEstimada(rs.getObject("edad_estimada_anios", Integer.class))
                    .pesoActual(peso == null ? null : peso.doubleValue())
                    .microchip(rs.getString("microchip"))
                    .tatuaje(rs.getString("tatuaje"))
                    .urlFoto(rs.getString("url_foto"))
                    .condicionesPreexistentes(rs.getString("condiciones_preexistentes"))
                    .fechaRegistro(rs.getObject("fecha_registro", LocalDateTime.class))
                    .activo(rs.getBoolean("activo"))
                    .build();
    }

}
