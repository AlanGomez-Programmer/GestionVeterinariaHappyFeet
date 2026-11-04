package com.alangomez.gestionveterinariahappyfeet.model.dao.inventario;

import com.alangomez.gestionveterinariahappyfeet.model.dao.OperacionesDAO;
import com.alangomez.gestionveterinariahappyfeet.model.entities.inventario.Proveedor;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class ProveedorDAO extends OperacionesDAO{

    public boolean agregarProveedor(Proveedor proveedor) throws SQLException{
        String sql = """
                INSERT INTO proveedores (nombre_empresa, contacto, telefono, email, direccion)
                VALUES (?, ?, ?, ?, ?)
                """;
        try (
                Connection con = obtenerConexion();
                PreparedStatement ps = con.prepareStatement(sql);
            ){
            ps.setString(1, proveedor.getNombreEmpresa());
            ponerTextoOpcional(ps, 2, proveedor.getContacto());
            ponerTextoOpcional(ps, 3, proveedor.getTelefono());
            ponerTextoOpcional(ps, 4, proveedor.getEmail());
            ponerTextoOpcional(ps, 5, proveedor.getDireccion());
            return ps.executeUpdate() == 1;
        }
    }

    public List<Proveedor> listarProveedores() throws SQLException{
        String sql = "SELECT * FROM proveedores ORDER BY nombre_empresa";
        List<Proveedor> lista = new ArrayList<>();
        try (
                Connection con = obtenerConexion();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery();
            ){
            while(rs.next()){
                lista.add(new Proveedor(
                        rs.getInt("id"),
                        rs.getString("nombre_empresa"),
                        rs.getString("contacto"),
                        rs.getString("telefono"),
                        rs.getString("email"),
                        rs.getString("direccion"),
                        rs.getBoolean("activo")
                ));
            }
            return lista;
        }
    }

    private void ponerTextoOpcional(PreparedStatement ps, int posicion, String valor) throws SQLException {
        if (valor != null && !valor.isBlank()) {
            ps.setString(posicion, valor);
        } else {
            ps.setNull(posicion, Types.VARCHAR);
        }
    }

}
