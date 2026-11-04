package com.alangomez.gestionveterinariahappyfeet.controller.servicios;

import com.alangomez.gestionveterinariahappyfeet.controller.ControladorBase;
import com.alangomez.gestionveterinariahappyfeet.model.entities.pacientes.mascota.Mascota;
import com.alangomez.gestionveterinariahappyfeet.model.entities.servicios.CatalogoProcedimiento;
import com.alangomez.gestionveterinariahappyfeet.model.entities.servicios.Cita;
import com.alangomez.gestionveterinariahappyfeet.model.entities.servicios.PrescripcionProcedimiento;
import com.alangomez.gestionveterinariahappyfeet.model.entities.servicios.Servicio;
import com.alangomez.gestionveterinariahappyfeet.model.entities.servicios.Veterinario;
import com.alangomez.gestionveterinariahappyfeet.model.entities.servicios.consulta.Consulta;
import com.alangomez.gestionveterinariahappyfeet.service.pacientes.MascotaService;
import com.alangomez.gestionveterinariahappyfeet.service.servicios.CatalogoServiciosService;
import com.alangomez.gestionveterinariahappyfeet.service.servicios.CitaService;
import com.alangomez.gestionveterinariahappyfeet.service.servicios.ConsultaService;
import com.alangomez.gestionveterinariahappyfeet.service.servicios.VeterinarioService;
import com.alangomez.gestionveterinariahappyfeet.view.servicios.VistaConsultas;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ConsultaControlador extends ControladorBase {

    private final VistaConsultas vista;
    private final ConsultaService consultaService;
    private final CitaService citaService;
    private final MascotaService mascotaService;
    private final VeterinarioService veterinarioService;
    private final CatalogoServiciosService catalogoService;

    public ConsultaControlador(VistaConsultas vista, ConsultaService consultaService, CitaService citaService,
            MascotaService mascotaService, VeterinarioService veterinarioService,
            CatalogoServiciosService catalogoService) {
        super(vista);
        this.vista = vista;
        this.consultaService = consultaService;
        this.citaService = citaService;
        this.mascotaService = mascotaService;
        this.veterinarioService = veterinarioService;
        this.catalogoService = catalogoService;
    }

    public void iniciar() {
        int opcion;
        do {
            opcion = vista.menuConsultas();
            try {
                switch (opcion) {
                    case 1 -> atenderCita();
                    case 2 -> atenderSinCita();
                    case 3 -> listarPorMascota();
                    case 4 -> verDetalle();
                    case 5 -> prescribirProcedimiento();
                    default -> { }
                }
            } catch (IllegalArgumentException e) {
                vista.mostrarError(e.getMessage());
            } catch (SQLException e) {
                manejarErrorBaseDatos("Menú de consultas, opción " + opcion, e);
            }
        } while (opcion != 0);
    }

    private void atenderCita() throws SQLException {
        Map<Integer, String> mascotas = aMapa(mascotaService.listarTodas(), Mascota::getId, Mascota::getNombre);
        Map<Integer, String> citas = aMapa(citaService.listarPendientes(), Cita::getId,
                c -> c.getFechaHora().toLocalDate() + " " + c.getFechaHora().toLocalTime()
                        + " - " + mascotas.getOrDefault(c.getMascotaId(), "-")
                        + " (" + c.getMotivo() + ")");
        int citaId = elegir("Citas pendientes", citas);
        Cita cita = citaService.buscarCitaParaAtender(citaId);

        Consulta consulta = vista.pedirDatosConsulta(cita.getMascotaId(), cita.getVeterinarioId(), cita.getId());
        registrar(consulta);
    }

    private void atenderSinCita() throws SQLException {
        int mascotaId = elegir("Mascotas", aMapa(mascotaService.listarActivas(), Mascota::getId, Mascota::getNombre));
        int veterinarioId = elegir("Veterinarios",
                aMapa(veterinarioService.listarActivos(), Veterinario::getId, Veterinario::getNombreCompleto));

        Consulta consulta = vista.pedirDatosConsulta(mascotaId, veterinarioId, null);
        registrar(consulta);
    }

    private void registrar(Consulta consulta) throws SQLException {
        Map<Integer, String> servicios = aMapa(catalogoService.listarServiciosParaConsulta(), Servicio::getId,
                s -> s.getNombre() + " - " + s.getPrecio());
        List<Integer> serviciosRealizados = new ArrayList<>();
        do {
            serviciosRealizados.add(elegir("Servicios realizados", servicios));
        } while (vista.deseaAgregarOtroServicio());

        consultaService.registrarConsulta(consulta, serviciosRealizados);
        vista.mostrarMensaje("Consulta registrada correctamente con el id " + consulta.getId());
    }

    private void listarPorMascota() throws SQLException {
        int mascotaId = elegir("Mascotas", aMapa(mascotaService.listarTodas(), Mascota::getId, Mascota::getNombre));
        vista.mostrarConsultas(consultaService.listarPorMascota(mascotaId),
                aMapa(veterinarioService.listarTodos(), Veterinario::getId, Veterinario::getNombreCompleto));
    }

    private void verDetalle() throws SQLException {
        Consulta consulta = consultaService.buscarPorId(vista.pedirId("consultar"));
        String mascota = mascotaService.buscarPorId(consulta.getMascotaId()).getNombre();
        String veterinario = veterinarioService.buscarPorId(consulta.getVeterinarioId()).getNombreCompleto();
        vista.mostrarDetalleConsulta(consulta, mascota, veterinario,
                consultaService.listarServiciosDeConsulta(consulta.getId()),
                aMapa(catalogoService.listarServicios(), Servicio::getId, Servicio::getNombre));
    }

    private void prescribirProcedimiento() throws SQLException {
        Consulta consulta = consultaService.buscarPorId(vista.pedirId("prescribir un procedimiento"));
        Map<Integer, String> servicios = aMapa(catalogoService.listarServicios(), Servicio::getId, Servicio::getNombre);
        int catalogoId = elegir("Procedimientos", aMapa(catalogoService.listarCatalogoProcedimientos(),
                CatalogoProcedimiento::getId, c -> servicios.getOrDefault(c.getServicioId(), "-")));

        consultaService.prescribirProcedimiento(
                new PrescripcionProcedimiento(consulta.getId(), catalogoId, vista.pedirIndicaciones()));
        vista.mostrarMensaje("Procedimiento prescrito correctamente");
    }
}
