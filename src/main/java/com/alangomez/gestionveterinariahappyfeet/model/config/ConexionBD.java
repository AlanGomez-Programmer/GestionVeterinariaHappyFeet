package com.alangomez.gestionveterinariahappyfeet.model.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {

    private static final ConfiguracionBD CONFIGURACION = ConfiguracionBD.cargar();
    private static final ConexionBD INSTANCIA = new ConexionBD();
    private Connection conexion;
    
    private ConexionBD(){
        
    }
    
    public static ConexionBD obtenerInstancia(){
        return INSTANCIA;
    }
    
    public Connection obtenerConexion() throws SQLException{
        if (conexion == null || conexion.isClosed()){
            conexion = DriverManager.getConnection(
                    CONFIGURACION.url(), 
                    CONFIGURACION.usuario(), 
                    CONFIGURACION.contrasenia()
            );
        }
        return conexion;
    }
}
