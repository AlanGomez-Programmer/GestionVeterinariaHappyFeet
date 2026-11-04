package com.alangomez.gestionveterinariahappyfeet.model.dao.inventario;

import com.alangomez.gestionveterinariahappyfeet.model.dao.OperacionesDAO;
import com.alangomez.gestionveterinariahappyfeet.model.entities.servicios.Estado;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TipoMovimientoDAO extends OperacionesDAO{

    public List<Estado> listarTiposMovimientos() throws SQLException{
        try (Connection con = obtenerConexion()){
            return listarTiposMovimientos(con);
        }
    }

    public List<Estado> listarTiposMovimientos(Connection con) throws SQLException{
        String sql = "SELECT * FROM tipos_movimientos_inventario ORDER BY id";
        List<Estado> lista = new ArrayList<>();
        try (
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
