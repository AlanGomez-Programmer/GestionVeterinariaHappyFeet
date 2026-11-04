package com.alangomez.gestionveterinariahappyfeet.model.entities.inventario;

public class TipoProducto {

    private int id;
    private String nombre;
    private String descripcion;

    public TipoProducto(int id, String nombre, String descripcion) {
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
