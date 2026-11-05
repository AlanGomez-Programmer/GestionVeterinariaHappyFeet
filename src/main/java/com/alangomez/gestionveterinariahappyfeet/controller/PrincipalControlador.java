package com.alangomez.gestionveterinariahappyfeet.controller;

import com.alangomez.gestionveterinariahappyfeet.controller.facturas.FacturaControlador;
import com.alangomez.gestionveterinariahappyfeet.controller.inventario.InventarioControlador;
import com.alangomez.gestionveterinariahappyfeet.controller.pacientes.PacientesControlador;
import com.alangomez.gestionveterinariahappyfeet.controller.servicios.ServiciosControlador;
import com.alangomez.gestionveterinariahappyfeet.view.VistaPrincipal;

public class PrincipalControlador {

    private final VistaPrincipal vista;
    private final PacientesControlador pacientesControlador;
    private final ServiciosControlador serviciosControlador;
    private final InventarioControlador inventarioControlador;
    private final FacturaControlador facturaControlador;

    public PrincipalControlador(VistaPrincipal vista, PacientesControlador pacientesControlador,
            ServiciosControlador serviciosControlador, InventarioControlador inventarioControlador,
            FacturaControlador facturaControlador) {
        this.vista = vista;
        this.pacientesControlador = pacientesControlador;
        this.serviciosControlador = serviciosControlador;
        this.inventarioControlador = inventarioControlador;
        this.facturaControlador = facturaControlador;
    }

    public void iniciar() {
        int opcion;
        do {
            opcion = vista.menuPrincipal();
            switch (opcion) {
                case 1 -> pacientesControlador.iniciar();
                case 2 -> serviciosControlador.iniciar();
                case 3 -> inventarioControlador.iniciar();
                case 4 -> facturaControlador.iniciar();
                default -> vista.mostrarMensaje("Hasta pronto");
            }
        } while (opcion != 0);
    }
}