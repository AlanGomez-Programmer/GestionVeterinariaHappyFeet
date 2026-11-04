package com.alangomez.gestionveterinariahappyfeet.controller.servicios;

import com.alangomez.gestionveterinariahappyfeet.view.servicios.VistaServicios;

public class ServiciosControlador {

    private final VistaServicios vista;
    private final VeterinarioControlador veterinarioControlador;
    private final CitaControlador citaControlador;
    private final ConsultaControlador consultaControlador;
    private final ProcedimientoControlador procedimientoControlador;

    public ServiciosControlador(VistaServicios vista, VeterinarioControlador veterinarioControlador,
            CitaControlador citaControlador, ConsultaControlador consultaControlador,
            ProcedimientoControlador procedimientoControlador) {
        this.vista = vista;
        this.veterinarioControlador = veterinarioControlador;
        this.citaControlador = citaControlador;
        this.consultaControlador = consultaControlador;
        this.procedimientoControlador = procedimientoControlador;
    }

    public void iniciar() {
        int opcion;
        do {
            opcion = vista.menuServicios();
            switch (opcion) {
                case 1 -> veterinarioControlador.iniciar();
                case 2 -> citaControlador.iniciar();
                case 3 -> consultaControlador.iniciar();
                case 4 -> procedimientoControlador.iniciar();
                default -> { }
            }
        } while (opcion != 0);
    }
}
