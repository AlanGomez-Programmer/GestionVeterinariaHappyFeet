package com.alangomez.gestionveterinariahappyfeet.model.entities.pacientes;

public class Sexo {

    private int id;
    private String nombre;

    public Sexo(int id, String nombre){
        this.id = id;
        this.nombre = nombre;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }
}
