package com.alangomez.gestionveterinariahappyfeet.model.entities.pacientes;

public class Raza {

    private int id;
    private int especieId;
    private String nombre;
    private String caracteristicas;

    public Raza(int id, int especieId, String nombre, String caracteristicas) {
        this.id = id;
        this.especieId = especieId;
        this.nombre = nombre;
        this.caracteristicas = caracteristicas;
    }
    
    public Raza(int especieId, String nombre, String caracteristicas) {
        this(0, especieId, nombre, caracteristicas);
    }

    public int getId() {
        return id;
    }

    public int getEspecieId() {
        return especieId;
    }

    public String getNombre() {
        return nombre;
    }

    public String getCaracteristicas() {
        return caracteristicas;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setEspecieId(int especieId) {
        this.especieId = especieId;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setCaracteristicas(String caracteristicas) {
        this.caracteristicas = caracteristicas;
    }
    
    
    
}
