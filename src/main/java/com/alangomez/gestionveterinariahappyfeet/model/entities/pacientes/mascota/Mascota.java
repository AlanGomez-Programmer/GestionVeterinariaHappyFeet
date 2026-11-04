package com.alangomez.gestionveterinariahappyfeet.model.entities.pacientes.mascota;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Mascota {

    private int id;
    private int duenioId;
    private int razaId;
    private int sexoId;
    private String nombre;
    private LocalDate fechaNacimiento;
    private Integer edadEstimada;
    private Double pesoActual;
    private String microchip;
    private String tatuaje;
    private String urlFoto;
    private String condicionesPreexistentes;
    private LocalDateTime fechaRegistro;
    private boolean activo = true;

    Mascota() {

    }

    public int getId() {
        return id;
    }

    public int getDuenioId() {
        return duenioId;
    }

    public int getRazaId() {
        return razaId;
    }

    public int getSexoId() {
        return sexoId;
    }

    public String getNombre() {
        return nombre;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public Integer getEdadEstimada() {
        return edadEstimada;
    }

    public Double getPesoActual() {
        return pesoActual;
    }

    public String getMicrochip() {
        return microchip;
    }

    public String getTatuaje() {
        return tatuaje;
    }

    public String getUrlFoto() {
        return urlFoto;
    }

    public String getCondicionesPreexistentes() {
        return condicionesPreexistentes;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setId(int id) {
        this.id = id;
    }

    void setDuenioId(int duenioId) {
        this.duenioId = duenioId;
    }

    void setRazaId(int razaId) {
        this.razaId = razaId;
    }

    void setSexoId(int sexoId) {
        this.sexoId = sexoId;
    }

    void setNombre(String nombre) {
        this.nombre = nombre;
    }

    void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    void setEdadEstimada(Integer edadEstimada) {
        this.edadEstimada = edadEstimada;
    }

    void setPesoActual(Double pesoActual) {
        this.pesoActual = pesoActual;
    }

    void setMicrochip(String microchip) {
        this.microchip = microchip;
    }

    void setTatuaje(String tatuaje) {
        this.tatuaje = tatuaje;
    }

    void setUrlFoto(String urlFoto) {
        this.urlFoto = urlFoto;
    }

    void setCondicionesPreexistentes(String condicionesPreexistentes) {
        this.condicionesPreexistentes = condicionesPreexistentes;
    }

    void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    void setActivo(boolean activo) {
        this.activo = activo;
    }
}
