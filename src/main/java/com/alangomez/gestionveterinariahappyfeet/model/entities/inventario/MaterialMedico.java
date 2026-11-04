package com.alangomez.gestionveterinariahappyfeet.model.entities.inventario;

public class MaterialMedico {

    private boolean esDesechable;
    private boolean esEsteril;

    public MaterialMedico(boolean esDesechable, boolean esEsteril) {
        this.esDesechable = esDesechable;
        this.esEsteril = esEsteril;
    }

    public boolean isEsDesechable() {
        return esDesechable;
    }

    public boolean isEsEsteril() {
        return esEsteril;
    }
}
