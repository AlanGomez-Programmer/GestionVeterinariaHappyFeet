package com.alangomez.gestionveterinariahappyfeet.model.dao.inventario;

import com.alangomez.gestionveterinariahappyfeet.model.dao.OperacionesDAO;
import com.alangomez.gestionveterinariahappyfeet.model.entities.inventario.MaterialMedico;
import com.alangomez.gestionveterinariahappyfeet.model.entities.inventario.Medicamento;
import com.alangomez.gestionveterinariahappyfeet.model.entities.inventario.Vacuna;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class DetalleProductoDAO extends OperacionesDAO{

    public boolean agregarVacuna(Connection con, int productoId, Vacuna vacuna) throws SQLException{
        String sql = """
                INSERT INTO vacunas (producto_id, tipo_vacuna_id, especie_destino_id, numero_dosis, dias_entre_dosis)
                VALUES (?, ?, ?, ?, ?)
                """;
        try (PreparedStatement ps = con.prepareStatement(sql)){
            ps.setInt(1, productoId);
            ps.setInt(2, vacuna.getTipoVacunaId());
            if (vacuna.getEspecieDestinoId() != null){
                ps.setInt(3, vacuna.getEspecieDestinoId());
            } else {
                ps.setNull(3, Types.INTEGER);
            }
            ps.setInt(4, vacuna.getNumeroDosis());
            if (vacuna.getDiasEntreDosis() != null){
                ps.setInt(5, vacuna.getDiasEntreDosis());
            } else {
                ps.setNull(5, Types.INTEGER);
            }
            return ps.executeUpdate() == 1;
        }
    }

    public boolean agregarMedicamento(Connection con, int productoId, Medicamento medicamento) throws SQLException{
        String sql = """
                INSERT INTO medicamentos (producto_id, principio_activo, presentacion, concentracion, via_administracion)
                VALUES (?, ?, ?, ?, ?)
                """;
        try (PreparedStatement ps = con.prepareStatement(sql)){
            ps.setInt(1, productoId);
            ponerTextoOpcional(ps, 2, medicamento.getPrincipioActivo());
            ponerTextoOpcional(ps, 3, medicamento.getPresentacion());
            ponerTextoOpcional(ps, 4, medicamento.getConcentracion());
            ponerTextoOpcional(ps, 5, medicamento.getViaAdministracion());
            return ps.executeUpdate() == 1;
        }
    }

    public boolean agregarMaterialMedico(Connection con, int productoId, MaterialMedico material) throws SQLException{
        String sql = "INSERT INTO material_medico (producto_id, es_desechable, es_esteril) VALUES (?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql)){
            ps.setInt(1, productoId);
            ps.setBoolean(2, material.isEsDesechable());
            ps.setBoolean(3, material.isEsEsteril());
            return ps.executeUpdate() == 1;
        }
    }

    public List<Medicamento> listarMedicamentos() throws SQLException{
        String sql = "SELECT * FROM medicamentos";
        List<Medicamento> lista = new ArrayList<>();
        try (
                Connection con = obtenerConexion();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery();
            ){
            while(rs.next()){
                lista.add(new Medicamento(
                        rs.getInt("id"),
                        rs.getInt("producto_id"),
                        rs.getString("principio_activo"),
                        rs.getString("presentacion"),
                        rs.getString("concentracion"),
                        rs.getString("via_administracion")
                ));
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

}
