package com.alangomez.gestionveterinariahappyfeet.model.entities.pacientes;

public class Alergia {

    private int id;
    private int mascotaId;
    private int catalogoAlergiaId;
    private String observaciones;

    public Alergia(int id, int mascotaId, int catalogoAlergiaId, String observaciones) {
        this.id = id;
        this.mascotaId = mascotaId;
        this.catalogoAlergiaId = catalogoAlergiaId;
        this.observaciones = observaciones;
    }

    public Alergia(int mascotaId, int catalogoAlergiaId, String observaciones) {
        this(0, mascotaId, catalogoAlergiaId, observaciones);
    }

    public int getId() {
        return id;
    }

    public int getMascotaId() {
        return mascotaId;
    }

    public int getCatalogoAlergiaId() {
        return catalogoAlergiaId;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setMascotaId(int mascotaId) {
        this.mascotaId = mascotaId;
    }

    public void setCatalogoAlergiaId(int catalogoAlergiaId) {
        this.catalogoAlergiaId = catalogoAlergiaId;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}
