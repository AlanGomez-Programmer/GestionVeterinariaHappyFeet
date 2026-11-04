package com.alangomez.gestionveterinariahappyfeet.model.entities.servicios;

import java.time.LocalDateTime;

public class Cita {

    private int id;
    private int mascotaId;
    private int veterinarioId;
    private int estadoId;
    private LocalDateTime fechaHora;
    private String motivo;
    private String observaciones;

    public Cita(int id, int mascotaId, int veterinarioId, int estadoId, LocalDateTime fechaHora,
            String motivo, String observaciones) {
        this.id = id;
        this.mascotaId = mascotaId;
        this.veterinarioId = veterinarioId;
        this.estadoId = estadoId;
        this.fechaHora = fechaHora;
        this.motivo = motivo;
        this.observaciones = observaciones;
    }

    public Cita(int mascotaId, int veterinarioId, LocalDateTime fechaHora, String motivo) {
        this(0, mascotaId, veterinarioId, 0, fechaHora, motivo, null);
    }

    public int getId() {
        return id;
    }

    public int getMascotaId() {
        return mascotaId;
    }

    public int getVeterinarioId() {
        return veterinarioId;
    }

    public int getEstadoId() {
        return estadoId;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public String getMotivo() {
        return motivo;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setEstadoId(int estadoId) {
        this.estadoId = estadoId;
    }
}
