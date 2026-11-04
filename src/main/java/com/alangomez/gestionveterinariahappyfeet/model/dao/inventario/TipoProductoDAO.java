package com.alangomez.gestionveterinariahappyfeet.model.dao.inventario;

import com.alangomez.gestionveterinariahappyfeet.model.dao.OperacionesDAO;
import com.alangomez.gestionveterinariahappyfeet.model.entities.inventario.TipoProducto;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TipoProductoDAO extends OperacionesDAO{

    public List<TipoProducto> listarTiposProductos() throws SQLException{
        String sql = "SELECT * FROM tipos_productos ORDER BY id";
        List<TipoProducto> lista = new ArrayList<>();
        try (
                Connection con = obtenerConexion();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery();
            ){
            while(rs.next()){
                lista.add(new TipoProducto(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("descripcion")
                ));
            }
            return lista;
        }
    }

}
