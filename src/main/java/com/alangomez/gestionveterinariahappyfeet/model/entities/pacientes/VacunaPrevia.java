package com.alangomez.gestionveterinariahappyfeet.model.entities.pacientes;

import java.time.LocalDate;

public class VacunaPrevia {

    private int id;
    private int mascotaId;
    private int tipoVacunaId;
    private LocalDate fechaAplicacion;
    private String lugarAplicacion;
    private String observaciones;

    public VacunaPrevia(int id, int mascotaId, int tipoVacunaId, LocalDate fechaAplicacion, String lugarAplicacion, String observaciones) {
        this.id = id;
        this.mascotaId = mascotaId;
        this.tipoVacunaId = tipoVacunaId;
        this.fechaAplicacion = fechaAplicacion;
        this.lugarAplicacion = lugarAplicacion;
        this.observaciones = observaciones;
    }

    public VacunaPrevia(int mascotaId, int tipoVacunaId, LocalDate fechaAplicacion, String lugarAplicacion, String observaciones) {
        this(0, mascotaId, tipoVacunaId, fechaAplicacion, lugarAplicacion, observaciones);
    }

    public int getId() {
        return id;
    }

    public int getMascotaId() {
        return mascotaId;
    }

    public int getTipoVacunaId() {
        return tipoVacunaId;
    }

    public LocalDate getFechaAplicacion() {
        return fechaAplicacion;
    }

    public String getLugarAplicacion() {
        return lugarAplicacion;
    }

    public String getObservaciones() {
        return observaciones;
    }
}
