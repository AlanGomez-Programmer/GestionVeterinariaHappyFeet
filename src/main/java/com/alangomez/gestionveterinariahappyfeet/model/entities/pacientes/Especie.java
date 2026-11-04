package com.alangomez.gestionveterinariahappyfeet.model.entities.pacientes;

public class Especie {

    private int id;
    private String nombre;
    private String descripcion;

    public Especie(int id, String nombre, String descripcion) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
    }
    
    public Especie(String nombre, String descripcion) {
        this(0, nombre, descripcion);
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
}
