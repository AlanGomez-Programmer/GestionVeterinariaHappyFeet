package com.alangomez.gestionveterinariahappyfeet.controller;

import com.alangomez.gestionveterinariahappyfeet.controller.pacientes.PacientesControlador;
import com.alangomez.gestionveterinariahappyfeet.controller.servicios.ServiciosControlador;
import com.alangomez.gestionveterinariahappyfeet.view.VistaPrincipal;

public class PrincipalControlador {

    private final VistaPrincipal vista;
    private final PacientesControlador pacientesControlador;
    private final ServiciosControlador serviciosControlador;

    public PrincipalControlador(VistaPrincipal vista, PacientesControlador pacientesControlador,
            ServiciosControlador serviciosControlador) {
        this.vista = vista;
        this.pacientesControlador = pacientesControlador;
        this.serviciosControlador = serviciosControlador;
    }

    public void iniciar() {
        int opcion;
        do {
            opcion = vista.menuPrincipal();
            switch (opcion) {
                case 1 -> pacientesControlador.iniciar();
                case 2 -> serviciosControlador.iniciar();
                default -> vista.mostrarMensaje("Hasta pronto");
            }
        } while (opcion != 0);
    }
}
