package com.alangomez.gestionveterinariahappyfeet.model.entities.pacientes;

import java.time.LocalDate;

public class ProcedimientoQuirurgicoPrevio {

    private int id;
    private int mascotaId;
    private int tipoProcedimientoId;
    private String descripcion;
    private LocalDate fechaAproximada;

    public ProcedimientoQuirurgicoPrevio(int id, int mascotaId, int tipoProcedimientoId, String descripcion, LocalDate fechaAproximada) {
        this.id = id;
        this.mascotaId = mascotaId;
        this.tipoProcedimientoId = tipoProcedimientoId;
        this.descripcion = descripcion;
        this.fechaAproximada = fechaAproximada;
    }

    public ProcedimientoQuirurgicoPrevio(int mascotaId, int tipoProcedimientoId, String descripcion, LocalDate fechaAproximada) {
        this(0, mascotaId, tipoProcedimientoId, descripcion, fechaAproximada);
    }

    public int getId() {
        return id;
    }

    public int getMascotaId() {
        return mascotaId;
    }

    public int getTipoProcedimientoId() {
        return tipoProcedimientoId;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public LocalDate getFechaAproximada() {
        return fechaAproximada;
    }
}
