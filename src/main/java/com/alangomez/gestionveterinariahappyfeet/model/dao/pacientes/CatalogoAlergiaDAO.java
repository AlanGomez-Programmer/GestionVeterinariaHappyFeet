package com.alangomez.gestionveterinariahappyfeet.model.dao.pacientes;

import com.alangomez.gestionveterinariahappyfeet.model.dao.OperacionesDAO;
import com.alangomez.gestionveterinariahappyfeet.model.entities.pacientes.CatalogoAlergia;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CatalogoAlergiaDAO extends OperacionesDAO{

    public List<CatalogoAlergia> listarCatalogoAlergias() throws SQLException{
        String sql = "SELECT * FROM catalogo_alergias ORDER BY tipo_alergia_id, nombre";
        List<CatalogoAlergia> lista = new ArrayList<>();
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

    public CatalogoAlergia buscarCatalogoAlergia(int id) throws SQLException{
        String sql = "SELECT * FROM catalogo_alergias WHERE id = ?";
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

    private CatalogoAlergia mapear(ResultSet rs) throws SQLException{
        return new CatalogoAlergia(
                rs.getInt("id"),
                rs.getInt("tipo_alergia_id"),
                rs.getString("nombre"),
                rs.getString("descripcion")
        );
    }

}
