package com.alangomez.gestionveterinariahappyfeet.model.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public record ConfiguracionBD(String url, String usuario, String contrasenia) {

    private static final String ARCHIVO_CONFIGURACION = "db.properties";

    public static ConfiguracionBD cargar() {
        Properties propiedades = new Properties();

        try (InputStream entrada = ConfiguracionBD.class
                .getClassLoader()
                .getResourceAsStream(ARCHIVO_CONFIGURACION)) {

            if (entrada == null) {
                throw new IllegalStateException(
                        "No se encontró " + ARCHIVO_CONFIGURACION + " en src/main/resources");
            }

            propiedades.load(entrada);

            return new ConfiguracionBD(
                    datoObligatorio(propiedades, "db.url"),
                    datoObligatorio(propiedades, "db.user"),
                    datoObligatorio(propiedades, "db.password")
            );

        } catch (IOException e) {
            throw new IllegalStateException("Error al leer " + ARCHIVO_CONFIGURACION, e);
        }
    }

    private static String datoObligatorio(Properties propiedades, String clave) {
        String valor = propiedades.getProperty(clave);
        if (valor == null || valor.isBlank()) {
            throw new IllegalStateException(
                    "Error: No se tiene la clave " + clave + " en el archivo " + ARCHIVO_CONFIGURACION);
        }
        return valor.trim();
    }
}