package com.alangomez.gestionveterinariahappyfeet.controller.pacientes;

import com.alangomez.gestionveterinariahappyfeet.model.entities.pacientes.Duenio;
import com.alangomez.gestionveterinariahappyfeet.service.pacientes.DuenioService;
import com.alangomez.gestionveterinariahappyfeet.controller.ControladorBase;
import com.alangomez.gestionveterinariahappyfeet.view.pacientes.VistaDuenios;
import java.sql.SQLException;

public class DuenioControlador extends ControladorBase {

    private final VistaDuenios vista;
    private final DuenioService duenioService;

    public DuenioControlador(VistaDuenios vista, DuenioService duenioService) {
        super(vista);
        this.vista = vista;
        this.duenioService = duenioService;
    }

    public void iniciar() {
        int opcion;
        do {
            opcion = vista.menuDuenios();
            try {
                switch (opcion) {
                    case 1 -> registrar();
                    case 2 -> listar();
                    case 3 -> buscar();
                    case 4 -> desactivar();
                    default -> { }
                }
            } catch (IllegalArgumentException e) {
                vista.mostrarError(e.getMessage());
            } catch (SQLException e) {
                manejarErrorBaseDatos("Menú de dueños, opción " + opcion, e);
            }
        } while (opcion != 0);
    }

    private void registrar() throws SQLException {
        Duenio duenio = vista.pedirDatoNuevoDuenio();
        duenioService.registrar(duenio);
        vista.mostrarMensaje("Dueño registrado correctamente");
    }

    private void listar() throws SQLException {
        vista.mostrarDuenios(duenioService.listarActivos());
    }

    private void buscar() throws SQLException {
        int id = vista.pedirId("buscar");
        vista.mostrarDuenio(duenioService.buscarPorId(id));
    }

    private void desactivar() throws SQLException {
        int id = vista.pedirId("eliminar");
        Duenio duenio = duenioService.buscarPorId(id);
        if (vista.confirmarDesactivacion(duenio)) {
            duenioService.desactivar(id);
            vista.mostrarMensaje("Dueño eliminado correctamente");
        }
    }
}