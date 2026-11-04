package com.alangomez.gestionveterinariahappyfeet.model.dao.pacientes;

import com.alangomez.gestionveterinariahappyfeet.model.dao.OperacionesDAO;
import com.alangomez.gestionveterinariahappyfeet.model.entities.pacientes.VacunaPrevia;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class VacunaPreviaDAO extends OperacionesDAO{

    public boolean agregarVacunaPrevia(VacunaPrevia vacuna) throws SQLException{
        String sql = """
                INSERT INTO historial_vacunas_previas
                    (mascota_id, tipo_vacuna_id, fecha_aplicacion, lugar_aplicacion, observaciones)
                VALUES (?, ?, ?, ?, ?)
                """;
        try (
                Connection con = obtenerConexion();
                PreparedStatement ps = con.prepareStatement(sql);
            ){
            ps.setInt(1, vacuna.getMascotaId());
            ps.setInt(2, vacuna.getTipoVacunaId());
            if (vacuna.getFechaAplicacion() != null){
                ps.setObject(3, vacuna.getFechaAplicacion());
            } else {
                ps.setNull(3, Types.DATE);
            }
            ponerTextoOpcional(ps, 4, vacuna.getLugarAplicacion());
            ponerTextoOpcional(ps, 5, vacuna.getObservaciones());
            return ps.executeUpdate() == 1;
        }
    }

    public List<VacunaPrevia> listarVacunasPorMascota(int mascotaId) throws SQLException{
        String sql = "SELECT * FROM historial_vacunas_previas WHERE mascota_id = ? ORDER BY fecha_aplicacion";
        List<VacunaPrevia> lista = new ArrayList<>();
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

    private VacunaPrevia mapear(ResultSet rs) throws SQLException{
        return new VacunaPrevia(
                rs.getInt("id"),
                rs.getInt("mascota_id"),
                rs.getInt("tipo_vacuna_id"),
                rs.getObject("fecha_aplicacion", LocalDate.class),
                rs.getString("lugar_aplicacion"),
                rs.getString("observaciones")
        );
    }

}
