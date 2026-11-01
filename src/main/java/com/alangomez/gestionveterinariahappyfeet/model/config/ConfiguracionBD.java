package com.alangomez.gestionveterinariahappyfeet.model.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public record ConfiguracionBD(String url, String usuario, String contrasenia) {

    private static final String ARCHIVO_CONFIGURACION = "db.properties";
    
    public static ConfiguracionBD cargar(){
        Properties propiedades = new Properties();
        
        try (
                InputStream entrada = ConfiguracionBD.class
                            .getClassLoader()
                            .getResourceAsStream(ARCHIVO_CONFIGURACION);
             ){
            
            propiedades.load(entrada);
            
            return new ConfiguracionBD(
                    datoObligatorio(propiedades, "url"),
                    datoObligatorio(propiedades, "user"),
                    datoObligatorio(propiedades, "password")
            );
            
        } catch (IOException e){
            throw new IllegalStateException("Error al leer " + ARCHIVO_CONFIGURACION, e);   
        }
    }
    
    public static String datoObligatorio(Properties propiedad, String clave){
        String valor = propiedad.getProperty(clave);
        if (clave == null){
            throw new IllegalStateException("Error: No se tiene la clave " + clave + " en el archivo db.properties");
        }
        return valor.trim();
    }
}
