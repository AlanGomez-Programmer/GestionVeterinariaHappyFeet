package com.alangomez.gestionveterinariahappyfeet.model.entities.servicios;

import java.time.LocalDate;

public class Veterinario {

    private int id;
    private int especialidadId;
    private String nombreCompleto;
    private String documentoIdentidad;
    private String licenciaProfesional;
    private String telefono;
    private String email;
    private LocalDate fechaContratacion;
    private boolean activo;

    public Veterinario(int id, int especialidadId, String nombreCompleto, String documentoIdentidad,
            String licenciaProfesional, String telefono, String email, LocalDate fechaContratacion, boolean activo) {
        this.id = id;
        this.especialidadId = especialidadId;
        this.nombreCompleto = nombreCompleto;
        this.documentoIdentidad = documentoIdentidad;
        this.licenciaProfesional = licenciaProfesional;
        this.telefono = telefono;
        this.email = email;
        this.fechaContratacion = fechaContratacion;
        this.activo = activo;
    }

    public Veterinario(int especialidadId, String nombreCompleto, String documentoIdentidad,
            String licenciaProfesional, String telefono, String email, LocalDate fechaContratacion) {
        this(0, especialidadId, nombreCompleto, documentoIdentidad, licenciaProfesional,
                telefono, email, fechaContratacion, true);
    }

    public int getId() {
        return id;
    }

    public int getEspecialidadId() {
        return especialidadId;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public String getDocumentoIdentidad() {
        return documentoIdentidad;
    }

    public String getLicenciaProfesional() {
        return licenciaProfesional;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getEmail() {
        return email;
    }

    public LocalDate getFechaContratacion() {
        return fechaContratacion;
    }

    public boolean isActivo() {
        return activo;
    }
}
