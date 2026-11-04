package com.alangomez.gestionveterinariahappyfeet.model.entities.pacientes;

import java.time.LocalDateTime;

public class Duenio {

    private int id;
    private String nombreCompleto;
    private String documentoIdentidad;
    private String direccion;
    private String telefono;
    private String email;
    private String contactoEmergencia;
    private LocalDateTime fechaRegistro;
    private boolean activo;

    public Duenio(int id, String nombreCompleto, String documentoIdentidad, String direccion, String telefono, String email, String contactoEmergencia, LocalDateTime fechaRegistro, boolean activo) {
        this.id = id;
        this.nombreCompleto = nombreCompleto;
        this.documentoIdentidad = documentoIdentidad;
        this.direccion = direccion;
        this.telefono = telefono;
        this.email = email;
        this.contactoEmergencia = contactoEmergencia;
        this.fechaRegistro = fechaRegistro;
        this.activo = activo;
    }

    public Duenio(String nombreCompleto, String documentoIdentidad, String direccion, String telefono, String email, String contactoEmergencia) {
        this(0, nombreCompleto, documentoIdentidad, direccion, telefono, email, contactoEmergencia,null, true);
    }

    public int getId() {
        return id;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public String getDocumentoIdentidad() {
        return documentoIdentidad;
    }

    public String getDireccion() {
        return direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getEmail() {
        return email;
    }

    public String getContactoEmergencia() {
        return contactoEmergencia;
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

    public void setNombre_completo(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public void setDocumentoIdentidad(String documentoIdentidad) {
        this.documentoIdentidad = documentoIdentidad;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setContacto_emergencia(String contactoEmergencia) {
        this.contactoEmergencia = contactoEmergencia;
    }

    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    
    
    
    
}
