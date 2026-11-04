package com.alangomez.gestionveterinariahappyfeet.model.entities.servicios.consulta;

import java.time.LocalDateTime;

public class Consulta {

    private int id;
    private int mascotaId;
    private int veterinarioId;
    private Integer citaId;
    private int estadoId;
    private LocalDateTime fechaHora;
    private String motivo;
    private String sintomas;
    private String diagnostico;
    private String recomendaciones;
    private String observaciones;
    private Double pesoRegistrado;
    private Double temperatura;

    Consulta() {

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

    public Integer getCitaId() {
        return citaId;
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

    public String getSintomas() {
        return sintomas;
    }

    public String getDiagnostico() {
        return diagnostico;
    }

    public String getRecomendaciones() {
        return recomendaciones;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public Double getPesoRegistrado() {
        return pesoRegistrado;
    }

    public Double getTemperatura() {
        return temperatura;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setEstadoId(int estadoId) {
        this.estadoId = estadoId;
    }

    void setMascotaId(int mascotaId) {
        this.mascotaId = mascotaId;
    }

    void setVeterinarioId(int veterinarioId) {
        this.veterinarioId = veterinarioId;
    }

    void setCitaId(Integer citaId) {
        this.citaId = citaId;
    }

    void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    void setSintomas(String sintomas) {
        this.sintomas = sintomas;
    }

    void setDiagnostico(String diagnostico) {
        this.diagnostico = diagnostico;
    }

    void setRecomendaciones(String recomendaciones) {
        this.recomendaciones = recomendaciones;
    }

    void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    void setPesoRegistrado(Double pesoRegistrado) {
        this.pesoRegistrado = pesoRegistrado;
    }

    void setTemperatura(Double temperatura) {
        this.temperatura = temperatura;
    }
}
