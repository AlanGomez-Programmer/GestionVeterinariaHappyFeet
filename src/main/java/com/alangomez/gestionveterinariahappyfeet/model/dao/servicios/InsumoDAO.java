package com.alangomez.gestionveterinariahappyfeet.model.dao.servicios;

import com.alangomez.gestionveterinariahappyfeet.model.dao.OperacionesDAO;
import com.alangomez.gestionveterinariahappyfeet.model.entities.servicios.PrescripcionMedicamento;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

public class InsumoDAO extends OperacionesDAO{

    public boolean agregarInsumoConsulta(Connection con, int consultaId, int existenciaId, int cantidad) throws SQLException{
        String sql = "INSERT INTO insumos_consultas (consulta_id, inventario_id, cantidad) VALUES (?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql)){
            ps.setInt(1, consultaId);
            ps.setInt(2, existenciaId);
            ps.setInt(3, cantidad);
            return ps.executeUpdate() == 1;
        }
    }

    public boolean agregarInsumoProcedimiento(Connection con, int procedimientoId, int existenciaId, int cantidad) throws SQLException{
        String sql = "INSERT INTO insumos_procedimientos_especiales (procedimiento_id, inventario_id, cantidad) VALUES (?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql)){
            ps.setInt(1, procedimientoId);
            ps.setInt(2, existenciaId);
            ps.setInt(3, cantidad);
            return ps.executeUpdate() == 1;
        }
    }

    public boolean agregarPrescripcionMedicamento(Connection con, PrescripcionMedicamento prescripcion) throws SQLException{
        String sql = """
                INSERT INTO prescripciones_medicamentos (consulta_id, medicamento_id, cantidad, dosis,
                    frecuencia, duracion_dias, instrucciones)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;
        try (PreparedStatement ps = con.prepareStatement(sql)){
            ps.setInt(1, prescripcion.getConsultaId());
            ps.setInt(2, prescripcion.getMedicamentoId());
            ps.setInt(3, prescripcion.getCantidad());
            ponerTextoOpcional(ps, 4, prescripcion.getDosis());
            ponerTextoOpcional(ps, 5, prescripcion.getFrecuencia());
            if (prescripcion.getDuracionDias() != null){
                ps.setInt(6, prescripcion.getDuracionDias());
            } else {
                ps.setNull(6, Types.INTEGER);
            }
            ponerTextoOpcional(ps, 7, prescripcion.getInstrucciones());
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

}
