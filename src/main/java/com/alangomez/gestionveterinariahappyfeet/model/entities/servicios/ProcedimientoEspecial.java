package com.alangomez.gestionveterinariahappyfeet.model.entities.servicios;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class ProcedimientoEspecial {

    private int id;
    private int mascotaId;
    private int veterinarioId;
    private int catalogoProcedimientoId;
    private Integer prescripcionId;
    private int estadoId;
    private LocalDateTime fechaHora;
    private String informacionPreoperatoria;
    private String detalleProcedimiento;
    private String complicaciones;
    private String seguimientoPostoperatorio;
    private LocalDate proximoControl;

    public ProcedimientoEspecial(int id, int mascotaId, int veterinarioId, int catalogoProcedimientoId,
            Integer prescripcionId, int estadoId, LocalDateTime fechaHora, String informacionPreoperatoria,
            String detalleProcedimiento, String complicaciones, String seguimientoPostoperatorio,
            LocalDate proximoControl) {
        this.id = id;
        this.mascotaId = mascotaId;
        this.veterinarioId = veterinarioId;
        this.catalogoProcedimientoId = catalogoProcedimientoId;
        this.prescripcionId = prescripcionId;
        this.estadoId = estadoId;
        this.fechaHora = fechaHora;
        this.informacionPreoperatoria = informacionPreoperatoria;
        this.detalleProcedimiento = detalleProcedimiento;
        this.complicaciones = complicaciones;
        this.seguimientoPostoperatorio = seguimientoPostoperatorio;
        this.proximoControl = proximoControl;
    }

    public ProcedimientoEspecial(int mascotaId, int veterinarioId, int catalogoProcedimientoId,
            Integer prescripcionId, LocalDateTime fechaHora) {
        this(0, mascotaId, veterinarioId, catalogoProcedimientoId, prescripcionId, 0, fechaHora,
                null, null, null, null, null);
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

    public int getCatalogoProcedimientoId() {
        return catalogoProcedimientoId;
    }

    public Integer getPrescripcionId() {
        return prescripcionId;
    }

    public int getEstadoId() {
        return estadoId;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public String getInformacionPreoperatoria() {
        return informacionPreoperatoria;
    }

    public String getDetalleProcedimiento() {
        return detalleProcedimiento;
    }

    public String getComplicaciones() {
        return complicaciones;
    }

    public String getSeguimientoPostoperatorio() {
        return seguimientoPostoperatorio;
    }

    public LocalDate getProximoControl() {
        return proximoControl;
    }

    public void setEstadoId(int estadoId) {
        this.estadoId = estadoId;
    }

    public void setInformacionPreoperatoria(String informacionPreoperatoria) {
        this.informacionPreoperatoria = informacionPreoperatoria;
    }

    public void setDetalleProcedimiento(String detalleProcedimiento) {
        this.detalleProcedimiento = detalleProcedimiento;
    }

    public void setComplicaciones(String complicaciones) {
        this.complicaciones = complicaciones;
    }

    public void setSeguimientoPostoperatorio(String seguimientoPostoperatorio) {
        this.seguimientoPostoperatorio = seguimientoPostoperatorio;
    }

    public void setProximoControl(LocalDate proximoControl) {
        this.proximoControl = proximoControl;
    }
}
