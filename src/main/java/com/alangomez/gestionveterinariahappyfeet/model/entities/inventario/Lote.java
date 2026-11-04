package com.alangomez.gestionveterinariahappyfeet.model.entities.inventario;

import java.time.LocalDate;

public class Lote {

    private int id;
    private String codigo;
    private LocalDate fechaFabricacion;
    private LocalDate fechaVencimiento;

    public Lote(int id, String codigo, LocalDate fechaFabricacion, LocalDate fechaVencimiento) {
        this.id = id;
        this.codigo = codigo;
        this.fechaFabricacion = fechaFabricacion;
        this.fechaVencimiento = fechaVencimiento;
    }

    public Lote(String codigo, LocalDate fechaFabricacion, LocalDate fechaVencimiento) {
        this(0, codigo, fechaFabricacion, fechaVencimiento);
    }

    public int getId() {
        return id;
    }

    public String getCodigo() {
        return codigo;
    }

    public LocalDate getFechaFabricacion() {
        return fechaFabricacion;
    }

    public LocalDate getFechaVencimiento() {
        return fechaVencimiento;
    }
}
