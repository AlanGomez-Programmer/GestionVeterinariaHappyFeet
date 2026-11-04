package com.alangomez.gestionveterinariahappyfeet.model.dao.inventario;

import com.alangomez.gestionveterinariahappyfeet.model.dao.OperacionesDAO;
import com.alangomez.gestionveterinariahappyfeet.model.entities.inventario.Fabricante;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class FabricanteDAO extends OperacionesDAO{

    public boolean agregarFabricante(Fabricante fabricante) throws SQLException{
        String sql = "INSERT INTO fabricantes (nombre, pais) VALUES (?, ?)";
        try (
                Connection con = obtenerConexion();
                PreparedStatement ps = con.prepareStatement(sql);
            ){
            ps.setString(1, fabricante.getNombre());
            if (fabricante.getPais() != null && !fabricante.getPais().isBlank()){
                ps.setString(2, fabricante.getPais());
            } else {
                ps.setNull(2, Types.VARCHAR);
            }
            return ps.executeUpdate() == 1;
        }
    }

    public List<Fabricante> listarFabricantes() throws SQLException{
        String sql = "SELECT * FROM fabricantes ORDER BY nombre";
        List<Fabricante> lista = new ArrayList<>();
        try (
                Connection con = obtenerConexion();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery();
            ){
            while(rs.next()){
                lista.add(new Fabricante(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("pais")
                ));
            }
            return lista;
        }
    }

    public boolean existeFabricante(String nombre) throws SQLException{
        String sql = "SELECT COUNT(*) AS cantidad FROM fabricantes WHERE nombre = ?";
        try (
                Connection con = obtenerConexion();
                PreparedStatement ps = con.prepareStatement(sql);
            ){
            ps.setString(1, nombre);
            try (ResultSet rs = ps.executeQuery()){
                return rs.next() && rs.getInt("cantidad") > 0;
            }
        }
    }

}
