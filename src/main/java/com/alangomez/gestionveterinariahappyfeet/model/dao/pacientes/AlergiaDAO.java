package com.alangomez.gestionveterinariahappyfeet.model.dao.pacientes;

import com.alangomez.gestionveterinariahappyfeet.model.dao.OperacionesDAO;
import com.alangomez.gestionveterinariahappyfeet.model.entities.pacientes.Alergia;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class AlergiaDAO extends OperacionesDAO{

    public boolean agregarAlergia(Connection con, Alergia alergia) throws SQLException{
        String sql = """
                INSERT INTO alergias (mascota_id, catalogo_alergia_id, observaciones)
                VALUES (?, ?, ?)
                """;
        try (PreparedStatement ps = con.prepareStatement(sql)){
            ps.setInt(1, alergia.getMascotaId());
            ps.setInt(2, alergia.getCatalogoAlergiaId());
            if (alergia.getObservaciones() != null && !alergia.getObservaciones().isBlank()){
                ps.setString(3, alergia.getObservaciones());
            } else {
                ps.setNull(3, Types.VARCHAR);
            }
            return ps.executeUpdate() == 1;
        }
    }

    public boolean agregarAlergia(Alergia alergia) throws SQLException{
        try (Connection con = obtenerConexion()){
            return agregarAlergia(con, alergia);
        }
    }

    public List<Alergia> listarAlergiasPorMascota(int mascotaId) throws SQLException{
        String sql = "SELECT * FROM alergias WHERE mascota_id = ?";
        List<Alergia> lista = new ArrayList<>();
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

    private Alergia mapear(ResultSet rs) throws SQLException{
        return new Alergia(
                rs.getInt("id"),
                rs.getInt("mascota_id"),
                rs.getInt("catalogo_alergia_id"),
                rs.getString("observaciones")
        );
    }

}
