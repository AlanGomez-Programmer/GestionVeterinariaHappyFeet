package com.alangomez.gestionveterinariahappyfeet.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class RegistroErrores {

    private static final Path ARCHIVO_LOG = Path.of("errores.log");
    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private RegistroErrores(){
        // Clase de utilidad: no debe instanciarse
    }

    // Guarda el error en el archivo errores.log de la carpeta principal del proyecto
    public static void registrar(String descripcion, Exception error){
        String linea = String.format("[%s] %s -> %s: %s%n",
                LocalDateTime.now().format(FORMATO_FECHA),
                descripcion,
                error.getClass().getSimpleName(),
                error.getMessage());
        try {
            Files.writeString(ARCHIVO_LOG, linea, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            System.out.println("No se pudo escribir en el archivo de errores: " + e.getMessage());
        }
    }
}
