package com.alangomez.gestionveterinariahappyfeet.model.dao.pacientes;

import com.alangomez.gestionveterinariahappyfeet.model.dao.OperacionesDAO;
import com.alangomez.gestionveterinariahappyfeet.model.entities.pacientes.Raza;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RazaDAO extends OperacionesDAO{

    public List<Raza> listarRazas() throws SQLException{
        String sql = "SELECT * FROM razas ORDER BY especie_id, nombre";
        List<Raza> lista = new ArrayList<>();
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

    public List<Raza> listarRazasPorEspecie(int especieId) throws SQLException{
        String sql = "SELECT * FROM razas WHERE especie_id = ? ORDER BY nombre";
        List<Raza> lista = new ArrayList<>();
        try (
                Connection con = obtenerConexion();
                PreparedStatement ps = con.prepareStatement(sql);
            ){
            ps.setInt(1, especieId);
            try (ResultSet rs = ps.executeQuery()){
                while(rs.next()){
                    lista.add(mapear(rs));
                }
            }
            return lista;
        }
    }

    public Raza buscarRaza(int id) throws SQLException{
        String sql = "SELECT * FROM razas WHERE id = ?";
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

    private Raza mapear(ResultSet rs) throws SQLException{
        return new Raza(
                rs.getInt("id"),
                rs.getInt("especie_id"),
                rs.getString("nombre"),
                rs.getString("caracteristicas")
        );
    }

}
