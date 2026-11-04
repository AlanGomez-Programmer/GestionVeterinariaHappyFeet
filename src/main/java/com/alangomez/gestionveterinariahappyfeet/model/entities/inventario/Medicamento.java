package com.alangomez.gestionveterinariahappyfeet.model.entities.inventario;

public class Medicamento {

    private int id;
    private int productoId;
    private String principioActivo;
    private String presentacion;
    private String concentracion;
    private String viaAdministracion;

    public Medicamento(int id, int productoId, String principioActivo, String presentacion,
            String concentracion, String viaAdministracion) {
        this.id = id;
        this.productoId = productoId;
        this.principioActivo = principioActivo;
        this.presentacion = presentacion;
        this.concentracion = concentracion;
        this.viaAdministracion = viaAdministracion;
    }

    public Medicamento(String principioActivo, String presentacion, String concentracion, String viaAdministracion) {
        this(0, 0, principioActivo, presentacion, concentracion, viaAdministracion);
    }

    public int getId() {
        return id;
    }

    public int getProductoId() {
        return productoId;
    }

    public String getPrincipioActivo() {
        return principioActivo;
    }

    public String getPresentacion() {
        return presentacion;
    }

    public String getConcentracion() {
        return concentracion;
    }

    public String getViaAdministracion() {
        return viaAdministracion;
    }
}
