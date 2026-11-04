package com.alangomez.gestionveterinariahappyfeet.view;

import com.alangomez.gestionveterinariahappyfeet.util.EntradaConsola;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public abstract class VistaBase {

    private static final String FORMATO_OPCION = "%-5s %s%n";
    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter FORMATO_FECHA_HORA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public void mostrarOpciones(String titulo, Map<Integer, String> opciones){
        System.out.println("\n---- " + titulo + " ----");
        System.out.printf(FORMATO_OPCION, "ID", "NOMBRE");
        opciones.forEach((id, nombre) -> System.out.printf(FORMATO_OPCION, id, nombre));
    }

    public int pedirIdOpcion(String titulo){
        return EntradaConsola.ingresarEntero("Seleccione el ID de " + titulo);
    }

    // Pide fecha y hora por separado y repite si el momento ya pasó
    public LocalDateTime pedirFechaHoraFutura(String mensaje){
        while (true){
            LocalDate fecha = EntradaConsola.ingresarFechaNoPasada("Fecha " + mensaje);
            LocalTime hora = EntradaConsola.ingresarHora("Hora " + mensaje);
            LocalDateTime fechaHora = LocalDateTime.of(fecha, hora);
            if (fechaHora.isAfter(LocalDateTime.now())){
                return fechaHora;
            }
            mostrarError("La fecha y hora deben ser futuras");
        }
    }

    public void mostrarMensaje(String mensaje){
        System.out.println(mensaje);
    }

    public void mostrarError(String mensaje){
        System.out.println("Error: " + mensaje);
    }

    protected String formatearFecha(LocalDate fecha){
        return fecha == null ? "-" : fecha.format(FORMATO_FECHA);
    }

    protected String formatearFechaHora(LocalDateTime fecha){
        return fecha == null ? "-" : fecha.format(FORMATO_FECHA_HORA);
    }

    protected String valorOGuion(String valor){
        return valor == null || valor.isBlank() ? "-" : valor;
    }
}
