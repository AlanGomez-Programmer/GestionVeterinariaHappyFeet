package com.alangomez.gestionveterinariahappyfeet.model.dao;

import com.alangomez.gestionveterinariahappyfeet.model.config.ConexionBD;
import java.sql.Connection;
import java.sql.SQLException;

public abstract class OperacionesDAO {

    public Connection obtenerConexion() throws SQLException{
        return ConexionBD.obtenerInstancia().obtenerConexion();
    }
    
}
