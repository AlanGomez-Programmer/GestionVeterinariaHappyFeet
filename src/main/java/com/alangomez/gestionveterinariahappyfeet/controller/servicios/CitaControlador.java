package com.alangomez.gestionveterinariahappyfeet.controller.servicios;

import com.alangomez.gestionveterinariahappyfeet.controller.ControladorBase;
import com.alangomez.gestionveterinariahappyfeet.model.entities.pacientes.mascota.Mascota;
import com.alangomez.gestionveterinariahappyfeet.model.entities.servicios.Cita;
import com.alangomez.gestionveterinariahappyfeet.model.entities.servicios.Estado;
import com.alangomez.gestionveterinariahappyfeet.model.entities.servicios.Veterinario;
import com.alangomez.gestionveterinariahappyfeet.service.pacientes.MascotaService;
import com.alangomez.gestionveterinariahappyfeet.service.servicios.CatalogoServiciosService;
import com.alangomez.gestionveterinariahappyfeet.service.servicios.CitaService;
import com.alangomez.gestionveterinariahappyfeet.service.servicios.VeterinarioService;
import com.alangomez.gestionveterinariahappyfeet.view.servicios.VistaCitas;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class CitaControlador extends ControladorBase {

    private final VistaCitas vista;
    private final CitaService citaService;
    private final MascotaService mascotaService;
    private final VeterinarioService veterinarioService;
    private final CatalogoServiciosService catalogoService;

    public CitaControlador(VistaCitas vista, CitaService citaService, MascotaService mascotaService,
            VeterinarioService veterinarioService, CatalogoServiciosService catalogoService) {
        super(vista);
        this.vista = vista;
        this.citaService = citaService;
        this.mascotaService = mascotaService;
        this.veterinarioService = veterinarioService;
        this.catalogoService = catalogoService;
    }

    public void iniciar() {
        int opcion;
        do {
            opcion = vista.menuCitas();
            try {
                switch (opcion) {
                    case 1 -> programar();
                    case 2 -> listarPorFecha();
                    case 3 -> mostrarCitas(citaService.listarPendientes());
                    case 4 -> cambiarEstado();
                    default -> { }
                }
            } catch (IllegalArgumentException e) {
                vista.mostrarError(e.getMessage());
            } catch (SQLException e) {
                manejarErrorBaseDatos("Menú de citas, opción " + opcion, e);
            }
        } while (opcion != 0);
    }

    private void programar() throws SQLException {
        int mascotaId = elegir("Mascotas", aMapa(mascotaService.listarActivas(), Mascota::getId, Mascota::getNombre));
        int veterinarioId = elegir("Veterinarios",
                aMapa(veterinarioService.listarActivos(), Veterinario::getId, Veterinario::getNombreCompleto));
        LocalDateTime fechaHora = vista.pedirFechaHoraFutura("de la cita");
        String motivo = vista.pedirMotivo();

        citaService.programar(new Cita(mascotaId, veterinarioId, fechaHora, motivo));
        vista.mostrarMensaje("Cita programada correctamente");
    }

    private void listarPorFecha() throws SQLException {
        mostrarCitas(citaService.listarPorFecha(vista.pedirFecha()));
    }

    private void cambiarEstado() throws SQLException {
        int id = vista.pedirId("cambiar de estado");
        Cita cita = citaService.buscarPorId(id);
        List<Estado> siguientes = citaService.estadosSiguientes(cita);
        int estadoId = elegir("Nuevo estado", aMapa(siguientes, Estado::getId, Estado::getNombre));
        citaService.cambiarEstado(id, estadoId);
        vista.mostrarMensaje("Estado de la cita actualizado correctamente");
    }

    private void mostrarCitas(List<Cita> citas) throws SQLException {
        vista.mostrarCitas(citas,
                aMapa(mascotaService.listarTodas(), Mascota::getId, Mascota::getNombre),
                aMapa(veterinarioService.listarTodos(), Veterinario::getId, Veterinario::getNombreCompleto),
                aMapa(catalogoService.listarEstadosCitas(), Estado::getId, Estado::getNombre));
    }
}
