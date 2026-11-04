package com.alangomez.gestionveterinariahappyfeet.model.entities.inventario;

public class Proveedor {

    private int id;
    private String nombreEmpresa;
    private String contacto;
    private String telefono;
    private String email;
    private String direccion;
    private boolean activo;

    public Proveedor(int id, String nombreEmpresa, String contacto, String telefono, String email,
            String direccion, boolean activo) {
        this.id = id;
        this.nombreEmpresa = nombreEmpresa;
        this.contacto = contacto;
        this.telefono = telefono;
        this.email = email;
        this.direccion = direccion;
        this.activo = activo;
    }

    public Proveedor(String nombreEmpresa, String contacto, String telefono, String email, String direccion) {
        this(0, nombreEmpresa, contacto, telefono, email, direccion, true);
    }

    public int getId() {
        return id;
    }

    public String getNombreEmpresa() {
        return nombreEmpresa;
    }

    public String getContacto() {
        return contacto;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getEmail() {
        return email;
    }

    public String getDireccion() {
        return direccion;
    }

    public boolean isActivo() {
        return activo;
    }
}
