package com.alangomez.gestionveterinariahappyfeet.model.entities.servicios;

import java.math.BigDecimal;

public class Servicio {

    private int id;
    private int tipoServicioId;
    private String nombre;
    private String descripcion;
    private BigDecimal precio;
    private boolean activo;

    public Servicio(int id, int tipoServicioId, String nombre, String descripcion, BigDecimal precio, boolean activo) {
        this.id = id;
        this.tipoServicioId = tipoServicioId;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.activo = activo;
    }

    public int getId() {
        return id;
    }

    public int getTipoServicioId() {
        return tipoServicioId;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public boolean isActivo() {
        return activo;
    }
}
