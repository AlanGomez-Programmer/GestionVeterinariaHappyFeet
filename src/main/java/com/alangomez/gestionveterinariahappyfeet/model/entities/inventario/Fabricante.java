package com.alangomez.gestionveterinariahappyfeet.model.entities.inventario;

public class Fabricante {

    private int id;
    private String nombre;
    private String pais;

    public Fabricante(int id, String nombre, String pais) {
        this.id = id;
        this.nombre = nombre;
        this.pais = pais;
    }

    public Fabricante(String nombre, String pais) {
        this(0, nombre, pais);
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getPais() {
        return pais;
    }
}
