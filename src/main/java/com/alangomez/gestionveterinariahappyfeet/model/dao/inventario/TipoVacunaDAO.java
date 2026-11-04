package com.alangomez.gestionveterinariahappyfeet.model.dao.inventario;

import com.alangomez.gestionveterinariahappyfeet.model.dao.OperacionesDAO;
import com.alangomez.gestionveterinariahappyfeet.model.entities.inventario.TipoVacuna;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TipoVacunaDAO extends OperacionesDAO{

    public List<TipoVacuna> listarTiposVacunas() throws SQLException{
        String sql = "SELECT * FROM tipos_vacunas ORDER BY nombre";
        List<TipoVacuna> lista = new ArrayList<>();
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

    public TipoVacuna buscarTipoVacuna(int id) throws SQLException{
        String sql = "SELECT * FROM tipos_vacunas WHERE id = ?";
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

    private TipoVacuna mapear(ResultSet rs) throws SQLException{
        return new TipoVacuna(
                rs.getInt("id"),
                rs.getString("nombre"),
                rs.getString("descripcion")
        );
    }

}
