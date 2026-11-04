package com.alangomez.gestionveterinariahappyfeet.controller;

import com.alangomez.gestionveterinariahappyfeet.util.RegistroErrores;
import com.alangomez.gestionveterinariahappyfeet.view.VistaBase;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public abstract class ControladorBase {

    private final VistaBase vista;

    protected ControladorBase(VistaBase vista){
        this.vista = vista;
    }

    protected int elegir(String titulo, Map<Integer, String> opciones){
        if (opciones.isEmpty()){
            throw new IllegalArgumentException("No hay " + titulo.toLowerCase() + " disponibles");
        }
        vista.mostrarOpciones(titulo, opciones);
        while (true){
            int id = vista.pedirIdOpcion(titulo);
            if (opciones.containsKey(id)){
                return id;
            }
            vista.mostrarError("El ID " + id + " no está en la lista");
        }
    }

    protected <T> Map<Integer, String> aMapa(List<T> lista, Function<T, Integer> id, Function<T, String> nombre){
        return lista.stream()
                .collect(Collectors.toMap(id, nombre, (primero, segundo) -> primero, LinkedHashMap::new));
    }

    protected void manejarErrorBaseDatos(String lugar, SQLException e){
        RegistroErrores.registrar(lugar, e);
        vista.mostrarError("Ocurrió un problema con la base de datos. Revise el archivo errores.log");
    }
}
