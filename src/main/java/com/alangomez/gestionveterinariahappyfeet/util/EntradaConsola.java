package com.alangomez.gestionveterinariahappyfeet.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.Objects;
import java.util.Scanner;

public class EntradaConsola {

     private static final Scanner input = new Scanner(System.in);
    
    private EntradaConsola(){
        // Clase de utilidad: no debe instanciarse
    }
    
    // Validación para ingresar texto
    public static String ingresarTexto(TipoTexto tipo, String mensaje){
        Objects.requireNonNull(tipo, "El tipo de texto no puede ser nulo");
        while(true){
            System.out.print(mensaje + ": ");
            String entrada = input.nextLine().trim();
            if (entrada.isEmpty()){
                System.out.println("Error: El texto no puede estar vacío");
            } else if (!tipo.esValido(entrada)){
                System.out.printf("Error: %s. %n", tipo.getMensajeError());
            } else {
                return entrada;
            }
        }
    }

    public static String ingresarCadenaNumeros(String mensaje){
        while (true){
           try {
                String documentoPersonal = ingresarTexto(TipoTexto.DOCUMENTO_PERSONAL, mensaje);
                return documentoPersonal;
            } catch (NumberFormatException e){
           System.out.println("Solo se aceptan valores numericos");
                }
        }
    }
    
    // Validación para ingresar números enteros mayores a 0
    public static int ingresarEntero(String mensaje){
        while(true){
            String texto = ingresarTexto(TipoTexto.ALFANUMERICO, mensaje);
            try {
                int num = Integer.parseInt(texto);
                if (num > 0){
                    return num;
                }
                System.out.println("Error: No se permiten números menores o iguales a 0");
            } catch (NumberFormatException e) {
                System.out.println("Error: Debe ingresar un número entero válido");
            }
        }
    }
    
    // Validación para ingresar un número dentro de un rango
    public static int ingresarEnteroEnRango(String mensaje, int minimo, int maximo){
        while(true){
            String texto = ingresarTexto(TipoTexto.ALFANUMERICO, mensaje);
            try {
                int num = Integer.parseInt(texto);
                if (num >= minimo && num <= maximo){
                    return num;
                }
                System.out.printf("Error: El número debe estar entre %d y %d%n", minimo, maximo);
            } catch (NumberFormatException e) {
                System.out.println("Error: Debe ingresar un número entero válido");
            }
        }
    }
    
    
    // Validación para ingresar números decimales
    public static double ingresarDecimal(String mensaje){
        while(true){
            String texto = ingresarTexto(TipoTexto.ALFANUMERICO, mensaje);
            try {
                double num = Double.parseDouble(texto);
                return num;
            } catch (NumberFormatException e){
                System.out.println("Error: Debe ingresar un número decimal válido");
            }
        }
    }

    // Validación para ingresar números decimales mayores a 0 (por ejemplo, el peso)
    public static double ingresarDecimalPositivo(String mensaje){
        while(true){
            double num = ingresarDecimal(mensaje);
            if (num > 0){
                return num;
            }
            System.out.println("Error: El número debe ser mayor a 0");
        }
    }
    
    // Validación para ingresar fechas con formato AAAA-MM-DD
    public static LocalDate ingresarFecha(String mensaje){
        while(true){
            String texto = ingresarTexto(TipoTexto.ALFANUMERICO, mensaje + " (AAAA-MM-DD)");
            try {
                return LocalDate.parse(texto);
            } catch (DateTimeParseException e) {
                System.out.println("Error: La fecha debe tener el formato AAAA-MM-DD y ser válida");
            }
        }
    }

    // Validación para fechas que no pueden ser futuras (nacimientos, historiales)
    public static LocalDate ingresarFechaNoFutura(String mensaje){
        while(true){
            LocalDate fecha = ingresarFecha(mensaje);
            if (!fecha.isAfter(LocalDate.now())){
                return fecha;
            }
            System.out.println("Error: La fecha no puede ser futura");
        }
    }
    
    // Validación para ingresar fecha y hora con formato AAAA-MM-DDTHH:MM
    public static LocalDateTime ingresarFechaHora(String mensaje){
        while(true){
            String texto = ingresarTexto(TipoTexto.ALFANUMERICO, mensaje + " (AAAA-MM-DDTHH:MM)");
            try {
                return LocalDateTime.parse(texto);
            } catch (DateTimeParseException e) {
                System.out.println("Error: La fecha y hora deben tener el formato AAAA-MM-DDTHH:MM, por ejemplo 2026-11-05T14:30");
            }
        }
    }
    
    // Validación para confirmar una acción (S/N)
    public static boolean confirmar(String mensaje){
        while(true){
            String respuesta = ingresarTexto(TipoTexto.SOLO_LETRAS, mensaje + " (S/N)");
            if (respuesta.equalsIgnoreCase("S")){
                return true;
            } else if (respuesta.equalsIgnoreCase("N")){
                return false;
            }
            System.out.println("Error: Responda S o N");
        }
    }       
}
