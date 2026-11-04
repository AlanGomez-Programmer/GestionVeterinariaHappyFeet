package com.alangomez.gestionveterinariahappyfeet.model.entities.servicios;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class HistorialServicio {

    private int id;
    private int servicioId;
    private int mascotaId;
    private int veterinarioId;
    private LocalDateTime fecha;
    private BigDecimal precioAplicado;
    private String observaciones;

    public HistorialServicio(int id, int servicioId, int mascotaId, int veterinarioId,
            LocalDateTime fecha, BigDecimal precioAplicado, String observaciones) {
        this.id = id;
        this.servicioId = servicioId;
        this.mascotaId = mascotaId;
        this.veterinarioId = veterinarioId;
        this.fecha = fecha;
        this.precioAplicado = precioAplicado;
        this.observaciones = observaciones;
    }

    public HistorialServicio(int servicioId, int mascotaId, int veterinarioId, BigDecimal precioAplicado) {
        this(0, servicioId, mascotaId, veterinarioId, null, precioAplicado, null);
    }

    public int getId() {
        return id;
    }

    public int getServicioId() {
        return servicioId;
    }

    public int getMascotaId() {
        return mascotaId;
    }

    public int getVeterinarioId() {
        return veterinarioId;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public BigDecimal getPrecioAplicado() {
        return precioAplicado;
    }

    public String getObservaciones() {
        return observaciones;
    }
}
