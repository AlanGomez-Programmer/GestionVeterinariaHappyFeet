package com.alangomez.gestionveterinariahappyfeet.util;

public class SesionUsuario {

    private static final SesionUsuario INSTANCIA = new SesionUsuario();
    // Mientras no exista el login, se usa el usuario administrador creado en data.sql (id 1)
    private int usuarioId = 1;

    private SesionUsuario(){

    }

    public static SesionUsuario obtenerInstancia(){
        return INSTANCIA;
    }

    public int getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }
}
