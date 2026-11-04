package com.alangomez.gestionveterinariahappyfeet.model.entities.inventario;

public class Vacuna {

    private int tipoVacunaId;
    private Integer especieDestinoId;
    private int numeroDosis;
    private Integer diasEntreDosis;

    public Vacuna(int tipoVacunaId, Integer especieDestinoId, int numeroDosis, Integer diasEntreDosis) {
        this.tipoVacunaId = tipoVacunaId;
        this.especieDestinoId = especieDestinoId;
        this.numeroDosis = numeroDosis;
        this.diasEntreDosis = diasEntreDosis;
    }

    public int getTipoVacunaId() {
        return tipoVacunaId;
    }

    public Integer getEspecieDestinoId() {
        return especieDestinoId;
    }

    public int getNumeroDosis() {
        return numeroDosis;
    }

    public Integer getDiasEntreDosis() {
        return diasEntreDosis;
    }
}
