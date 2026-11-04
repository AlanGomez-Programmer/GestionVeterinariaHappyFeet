package com.alangomez.gestionveterinariahappyfeet.model.dao.servicios;

import com.alangomez.gestionveterinariahappyfeet.model.dao.OperacionesDAO;
import com.alangomez.gestionveterinariahappyfeet.model.entities.servicios.consulta.Consulta;
import com.alangomez.gestionveterinariahappyfeet.model.entities.servicios.consulta.ConsultaBuilderImpl;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ConsultaDAO extends OperacionesDAO{

    public int agregarConsulta(Connection con, Consulta consulta) throws SQLException{
        String sql = """
                INSERT INTO consultas (mascota_id, veterinario_id, cita_id, estado_id, fecha_hora,
                    motivo, sintomas, diagnostico, recomendaciones, observaciones,
                    peso_registrado, temperatura)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;
        try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
            ps.setInt(1, consulta.getMascotaId());
            ps.setInt(2, consulta.getVeterinarioId());
            if (consulta.getCitaId() != null){
                ps.setInt(3, consulta.getCitaId());
            } else {
                ps.setNull(3, Types.INTEGER);
            }
            ps.setInt(4, consulta.getEstadoId());
            ps.setObject(5, consulta.getFechaHora());
            ps.setString(6, consulta.getMotivo());
            ponerTextoOpcional(ps, 7, consulta.getSintomas());
            ponerTextoOpcional(ps, 8, consulta.getDiagnostico());
            ponerTextoOpcional(ps, 9, consulta.getRecomendaciones());
            ponerTextoOpcional(ps, 10, consulta.getObservaciones());
            ponerDecimalOpcional(ps, 11, consulta.getPesoRegistrado());
            ponerDecimalOpcional(ps, 12, consulta.getTemperatura());
            ps.executeUpdate();

            try (ResultSet llaves = ps.getGeneratedKeys()){
                if (llaves.next()){
                    return llaves.getInt(1);
                }
                throw new SQLException("No se obtuvo el id de la consulta registrada");
            }
        }
    }

    public Consulta buscarConsulta(int id) throws SQLException{
        String sql = "SELECT * FROM consultas WHERE id = ?";
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

    public List<Consulta> listarConsultasPorMascota(int mascotaId) throws SQLException{
        String sql = "SELECT * FROM consultas WHERE mascota_id = ? ORDER BY fecha_hora DESC";
        List<Consulta> lista = new ArrayList<>();
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

    private void ponerTextoOpcional(PreparedStatement ps, int posicion, String valor) throws SQLException {
        if (valor != null && !valor.isBlank()) {
            ps.setString(posicion, valor);
        } else {
            ps.setNull(posicion, Types.VARCHAR);
        }
    }

    private void ponerDecimalOpcional(PreparedStatement ps, int posicion, Double valor) throws SQLException {
        if (valor != null) {
            ps.setDouble(posicion, valor);
        } else {
            ps.setNull(posicion, Types.DECIMAL);
        }
    }

    private Consulta mapear(ResultSet rs) throws SQLException{
        BigDecimal peso = rs.getBigDecimal("peso_registrado");
        BigDecimal temperatura = rs.getBigDecimal("temperatura");
        return new ConsultaBuilderImpl()
                    .id(rs.getInt("id"))
                    .mascotaId(rs.getInt("mascota_id"))
                    .veterinarioId(rs.getInt("veterinario_id"))
                    .citaId(rs.getObject("cita_id", Integer.class))
                    .estadoId(rs.getInt("estado_id"))
                    .fechaHora(rs.getObject("fecha_hora", LocalDateTime.class))
                    .motivo(rs.getString("motivo"))
                    .sintomas(rs.getString("sintomas"))
                    .diagnostico(rs.getString("diagnostico"))
                    .recomendaciones(rs.getString("recomendaciones"))
                    .observaciones(rs.getString("observaciones"))
                    .pesoRegistrado(peso == null ? null : peso.doubleValue())
                    .temperatura(temperatura == null ? null : temperatura.doubleValue())
                    .build();
    }

}
