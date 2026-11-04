package com.alangomez.gestionveterinariahappyfeet.controller.pacientes;

import com.alangomez.gestionveterinariahappyfeet.view.pacientes.VistaPacientes;

public class PacientesControlador {

    private final VistaPacientes vista;
    private final DuenioControlador duenioControlador;
    private final MascotaControlador mascotaControlador;

    public PacientesControlador(VistaPacientes vista, DuenioControlador duenioControlador,
            MascotaControlador mascotaControlador) {
        this.vista = vista;
        this.duenioControlador = duenioControlador;
        this.mascotaControlador = mascotaControlador;
    }

    public void iniciar() {
        int opcion;
        do {
            opcion = vista.menuPacientes();
            switch (opcion) {
                case 1 -> duenioControlador.iniciar();
                case 2 -> mascotaControlador.iniciar();
                default -> { }
            }
        } while (opcion != 0);
    }
}
