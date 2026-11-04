package com.alangomez.gestionveterinariahappyfeet.model.entities.pacientes;

import java.time.LocalDateTime;

public class HistorialDuenio {

    private int id;
    private int mascotaId;
    private int duenioId;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
    private String motivo;

    public HistorialDuenio(int id, int mascotaId, int duenioId, LocalDateTime fechaInicio, LocalDateTime fechaFin, String motivo) {
        this.id = id;
        this.mascotaId = mascotaId;
        this.duenioId = duenioId;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.motivo = motivo;
    }

    public HistorialDuenio(int mascotaId, int duenioId, String motivo) {
        this(0, mascotaId, duenioId, null, null, motivo);
    }

    public int getId() {
        return id;
    }

    public int getMascotaId() {
        return mascotaId;
    }

    public int getDuenioId() {
        return duenioId;
    }

    public LocalDateTime getFechaInicio() {
        return fechaInicio;
    }

    public LocalDateTime getFechaFin() {
        return fechaFin;
    }

    public String getMotivo() {
        return motivo;
    }

    public boolean esDuenioActual() {
        return fechaFin == null;
    }

    public void setMascotaId(int mascotaId) {
        this.mascotaId = mascotaId;
    }
}
