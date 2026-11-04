package com.alangomez.gestionveterinariahappyfeet.model.dao.servicios;

import com.alangomez.gestionveterinariahappyfeet.model.dao.OperacionesDAO;
import com.alangomez.gestionveterinariahappyfeet.model.entities.servicios.Estado;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EstadoDAO extends OperacionesDAO{

    public List<Estado> listarEstadosCitas() throws SQLException{
        return listarEstados("SELECT * FROM estados_citas ORDER BY id");
    }

    public List<Estado> listarEstadosConsultas() throws SQLException{
        return listarEstados("SELECT * FROM estados_consultas ORDER BY id");
    }

    public List<Estado> listarEstadosProcedimientos() throws SQLException{
        return listarEstados("SELECT * FROM estados_procedimientos_especiales ORDER BY id");
    }

    private List<Estado> listarEstados(String sql) throws SQLException{
        List<Estado> lista = new ArrayList<>();
        try (
                Connection con = obtenerConexion();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery();
            ){
            while(rs.next()){
                lista.add(new Estado(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("descripcion")
                ));
            }
            return lista;
        }
    }

}
