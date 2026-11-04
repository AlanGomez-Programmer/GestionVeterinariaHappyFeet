package com.alangomez.gestionveterinariahappyfeet.model.entities.servicios;

public class PrescripcionMedicamento {

    private int id;
    private int consultaId;
    private int medicamentoId;
    private int cantidad;
    private String dosis;
    private String frecuencia;
    private Integer duracionDias;
    private String instrucciones;

    public PrescripcionMedicamento(int id, int consultaId, int medicamentoId, int cantidad, String dosis,
            String frecuencia, Integer duracionDias, String instrucciones) {
        this.id = id;
        this.consultaId = consultaId;
        this.medicamentoId = medicamentoId;
        this.cantidad = cantidad;
        this.dosis = dosis;
        this.frecuencia = frecuencia;
        this.duracionDias = duracionDias;
        this.instrucciones = instrucciones;
    }

    public PrescripcionMedicamento(int consultaId, int medicamentoId, int cantidad, String dosis,
            String frecuencia, Integer duracionDias, String instrucciones) {
        this(0, consultaId, medicamentoId, cantidad, dosis, frecuencia, duracionDias, instrucciones);
    }

    public int getId() {
        return id;
    }

    public int getConsultaId() {
        return consultaId;
    }

    public int getMedicamentoId() {
        return medicamentoId;
    }

    public int getCantidad() {
        return cantidad;
    }

    public String getDosis() {
        return dosis;
    }

    public String getFrecuencia() {
        return frecuencia;
    }

    public Integer getDuracionDias() {
        return duracionDias;
    }

    public String getInstrucciones() {
        return instrucciones;
    }
}
