package com.alangomez.gestionveterinariahappyfeet.model.dao.servicios;

import com.alangomez.gestionveterinariahappyfeet.model.dao.OperacionesDAO;
import com.alangomez.gestionveterinariahappyfeet.model.entities.servicios.CatalogoProcedimiento;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CatalogoProcedimientoDAO extends OperacionesDAO{

    public List<CatalogoProcedimiento> listarCatalogoProcedimientos() throws SQLException{
        String sql = "SELECT * FROM catalogo_procedimientos_especiales ORDER BY tipo_procedimiento_id";
        List<CatalogoProcedimiento> lista = new ArrayList<>();
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

    public CatalogoProcedimiento buscarCatalogoProcedimiento(int id) throws SQLException{
        String sql = "SELECT * FROM catalogo_procedimientos_especiales WHERE id = ?";
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

    private CatalogoProcedimiento mapear(ResultSet rs) throws SQLException{
        return new CatalogoProcedimiento(
                rs.getInt("id"),
                rs.getInt("servicio_id"),
                rs.getInt("tipo_procedimiento_id"),
                rs.getObject("duracion_estimada_minutos", Integer.class),
                rs.getBoolean("requiere_ayuno")
        );
    }

}
