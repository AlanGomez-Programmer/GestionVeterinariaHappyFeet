package com.alangomez.gestionveterinariahappyfeet.model.entities.inventario;

import java.time.LocalDate;

public class Existencia {

    private int id;
    private int productoId;
    private String codigoLote;
    private LocalDate fechaVencimiento;
    private int cantidadStock;

    public Existencia(int id, int productoId, String codigoLote, LocalDate fechaVencimiento, int cantidadStock) {
        this.id = id;
        this.productoId = productoId;
        this.codigoLote = codigoLote;
        this.fechaVencimiento = fechaVencimiento;
        this.cantidadStock = cantidadStock;
    }

    public int getId() {
        return id;
    }

    public int getProductoId() {
        return productoId;
    }

    public String getCodigoLote() {
        return codigoLote;
    }

    public LocalDate getFechaVencimiento() {
        return fechaVencimiento;
    }

    public int getCantidadStock() {
        return cantidadStock;
    }

    public boolean estaVencido() {
        return fechaVencimiento != null && fechaVencimiento.isBefore(LocalDate.now());
    }

    public boolean venceAntesDe(LocalDate fecha) {
        return fechaVencimiento != null && !estaVencido() && !fechaVencimiento.isAfter(fecha);
    }
}
