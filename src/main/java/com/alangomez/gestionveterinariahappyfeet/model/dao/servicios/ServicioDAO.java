package com.alangomez.gestionveterinariahappyfeet.model.dao.servicios;

import com.alangomez.gestionveterinariahappyfeet.model.dao.OperacionesDAO;
import com.alangomez.gestionveterinariahappyfeet.model.entities.servicios.Servicio;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ServicioDAO extends OperacionesDAO{

    public List<Servicio> listarServicios() throws SQLException{
        String sql = "SELECT * FROM catalogo_servicios WHERE activo = TRUE ORDER BY tipo_servicio_id, nombre";
        List<Servicio> lista = new ArrayList<>();
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

    public Servicio buscarServicio(int id) throws SQLException{
        String sql = "SELECT * FROM catalogo_servicios WHERE id = ?";
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

    private Servicio mapear(ResultSet rs) throws SQLException{
        return new Servicio(
                rs.getInt("id"),
                rs.getInt("tipo_servicio_id"),
                rs.getString("nombre"),
                rs.getString("descripcion"),
                rs.getBigDecimal("precio"),
                rs.getBoolean("activo")
        );
    }

}
