package com.alangomez.gestionveterinariahappyfeet.controller.servicios;

import com.alangomez.gestionveterinariahappyfeet.controller.ControladorBase;
import com.alangomez.gestionveterinariahappyfeet.model.entities.servicios.Especialidad;
import com.alangomez.gestionveterinariahappyfeet.model.entities.servicios.Veterinario;
import com.alangomez.gestionveterinariahappyfeet.service.servicios.CatalogoServiciosService;
import com.alangomez.gestionveterinariahappyfeet.service.servicios.VeterinarioService;
import com.alangomez.gestionveterinariahappyfeet.view.servicios.VistaVeterinarios;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class VeterinarioControlador extends ControladorBase {

    private final VistaVeterinarios vista;
    private final VeterinarioService veterinarioService;
    private final CatalogoServiciosService catalogoService;

    public VeterinarioControlador(VistaVeterinarios vista, VeterinarioService veterinarioService,
            CatalogoServiciosService catalogoService) {
        super(vista);
        this.vista = vista;
        this.veterinarioService = veterinarioService;
        this.catalogoService = catalogoService;
    }

    public void iniciar() {
        int opcion;
        do {
            opcion = vista.menuVeterinarios();
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
                manejarErrorBaseDatos("Menú de veterinarios, opción " + opcion, e);
            }
        } while (opcion != 0);
    }

    private void registrar() throws SQLException {
        int especialidadId = elegir("Especialidades", nombresEspecialidades());
        Veterinario veterinario = vista.pedirDatosVeterinario(especialidadId);
        veterinarioService.registrar(veterinario);
        vista.mostrarMensaje("Veterinario registrado correctamente");
    }

    private void listar() throws SQLException {
        vista.mostrarVeterinarios(veterinarioService.listarActivos(), nombresEspecialidades());
    }

    private void buscar() throws SQLException {
        int id = vista.pedirId("buscar");
        vista.mostrarVeterinarios(List.of(veterinarioService.buscarPorId(id)), nombresEspecialidades());
    }

    private void desactivar() throws SQLException {
        int id = vista.pedirId("eliminar");
        Veterinario veterinario = veterinarioService.buscarActivoPorId(id);
        if (vista.confirmarDesactivacion(veterinario)) {
            veterinarioService.desactivar(id);
            vista.mostrarMensaje("Veterinario eliminado correctamente");
        }
    }

    private Map<Integer, String> nombresEspecialidades() throws SQLException {
        return aMapa(catalogoService.listarEspecialidades(), Especialidad::getId, Especialidad::getNombre);
    }
}
