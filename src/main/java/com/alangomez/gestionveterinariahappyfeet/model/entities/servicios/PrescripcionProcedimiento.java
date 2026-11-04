package com.alangomez.gestionveterinariahappyfeet.model.entities.servicios;

import java.time.LocalDateTime;

public class PrescripcionProcedimiento {

    private int id;
    private int consultaId;
    private int catalogoProcedimientoId;
    private String indicaciones;
    private LocalDateTime fechaPrescripcion;

    public PrescripcionProcedimiento(int id, int consultaId, int catalogoProcedimientoId,
            String indicaciones, LocalDateTime fechaPrescripcion) {
        this.id = id;
        this.consultaId = consultaId;
        this.catalogoProcedimientoId = catalogoProcedimientoId;
        this.indicaciones = indicaciones;
        this.fechaPrescripcion = fechaPrescripcion;
    }

    public PrescripcionProcedimiento(int consultaId, int catalogoProcedimientoId, String indicaciones) {
        this(0, consultaId, catalogoProcedimientoId, indicaciones, null);
    }

    public int getId() {
        return id;
    }

    public int getConsultaId() {
        return consultaId;
    }

    public int getCatalogoProcedimientoId() {
        return catalogoProcedimientoId;
    }

    public String getIndicaciones() {
        return indicaciones;
    }

    public LocalDateTime getFechaPrescripcion() {
        return fechaPrescripcion;
    }
}
