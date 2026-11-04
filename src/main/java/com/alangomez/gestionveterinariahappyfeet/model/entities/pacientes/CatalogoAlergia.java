package com.alangomez.gestionveterinariahappyfeet.model.entities.pacientes;

public class CatalogoAlergia {

    private int id;
    private int tipoAlergiaId;
    private String nombre;
    private String descripcion;

    public CatalogoAlergia(int id, int tipoAlergiaId, String nombre, String descripcion) {
        this.id = id;
        this.tipoAlergiaId = tipoAlergiaId;
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public CatalogoAlergia(int tipoAlergiaId, String nombre, String descripcion) {
        this(0, tipoAlergiaId, nombre, descripcion);
    }

    
    public int getId() {
        return id;
    }

    public int getTipoAlergiaId() {
        return tipoAlergiaId;
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

    public void setTipoAlergiaId(int tipoAlergiaId) {
        this.tipoAlergiaId = tipoAlergiaId;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
    
    
}
