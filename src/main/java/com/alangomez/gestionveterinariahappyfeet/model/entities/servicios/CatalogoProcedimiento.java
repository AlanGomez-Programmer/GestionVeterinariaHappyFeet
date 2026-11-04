package com.alangomez.gestionveterinariahappyfeet.model.entities.servicios;

public class CatalogoProcedimiento {

    private int id;
    private int servicioId;
    private int tipoProcedimientoId;
    private Integer duracionEstimadaMinutos;
    private boolean requiereAyuno;

    public CatalogoProcedimiento(int id, int servicioId, int tipoProcedimientoId,
            Integer duracionEstimadaMinutos, boolean requiereAyuno) {
        this.id = id;
        this.servicioId = servicioId;
        this.tipoProcedimientoId = tipoProcedimientoId;
        this.duracionEstimadaMinutos = duracionEstimadaMinutos;
        this.requiereAyuno = requiereAyuno;
    }

    public int getId() {
        return id;
    }

    public int getServicioId() {
        return servicioId;
    }

    public int getTipoProcedimientoId() {
        return tipoProcedimientoId;
    }

    public Integer getDuracionEstimadaMinutos() {
        return duracionEstimadaMinutos;
    }

    public boolean isRequiereAyuno() {
        return requiereAyuno;
    }
}
