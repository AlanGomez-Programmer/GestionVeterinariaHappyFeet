package com.alangomez.gestionveterinariahappyfeet.model.entities.inventario;

public class TipoVacuna {

    private int id;
    private String nombre;
    private String descripcion;

    public TipoVacuna(int id, String nombre, String descripcion) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
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
}
