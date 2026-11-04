package com.alangomez.gestionveterinariahappyfeet.controller.servicios;

import com.alangomez.gestionveterinariahappyfeet.controller.ControladorBase;
import com.alangomez.gestionveterinariahappyfeet.model.entities.pacientes.mascota.Mascota;
import com.alangomez.gestionveterinariahappyfeet.model.entities.servicios.CatalogoProcedimiento;
import com.alangomez.gestionveterinariahappyfeet.model.entities.servicios.Estado;
import com.alangomez.gestionveterinariahappyfeet.model.entities.servicios.PrescripcionProcedimiento;
import com.alangomez.gestionveterinariahappyfeet.model.entities.servicios.ProcedimientoEspecial;
import com.alangomez.gestionveterinariahappyfeet.model.entities.servicios.Servicio;
import com.alangomez.gestionveterinariahappyfeet.model.entities.servicios.Veterinario;
import com.alangomez.gestionveterinariahappyfeet.service.pacientes.MascotaService;
import com.alangomez.gestionveterinariahappyfeet.service.servicios.CatalogoServiciosService;
import com.alangomez.gestionveterinariahappyfeet.service.servicios.ProcedimientoService;
import com.alangomez.gestionveterinariahappyfeet.service.servicios.VeterinarioService;
import com.alangomez.gestionveterinariahappyfeet.view.servicios.VistaProcedimientos;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class ProcedimientoControlador extends ControladorBase {

    private final VistaProcedimientos vista;
    private final ProcedimientoService procedimientoService;
    private final MascotaService mascotaService;
    private final VeterinarioService veterinarioService;
    private final CatalogoServiciosService catalogoService;

    public ProcedimientoControlador(VistaProcedimientos vista, ProcedimientoService procedimientoService,
            MascotaService mascotaService, VeterinarioService veterinarioService,
            CatalogoServiciosService catalogoService) {
        super(vista);
        this.vista = vista;
        this.procedimientoService = procedimientoService;
        this.mascotaService = mascotaService;
        this.veterinarioService = veterinarioService;
        this.catalogoService = catalogoService;
    }

    public void iniciar() {
        int opcion;
        do {
            opcion = vista.menuProcedimientos();
            try {
                switch (opcion) {
                    case 1 -> programarDesdePrescripcion();
                    case 2 -> programarSinPrescripcion();
                    case 3 -> listar();
                    case 4 -> verDetalle();
                    case 5 -> cambiarEstado();
                    default -> { }
                }
            } catch (IllegalArgumentException e) {
                vista.mostrarError(e.getMessage());
            } catch (SQLException e) {
                manejarErrorBaseDatos("Menú de procedimientos, opción " + opcion, e);
            }
        } while (opcion != 0);
    }

    private void programarDesdePrescripcion() throws SQLException {
        Map<Integer, String> procedimientos = nombresProcedimientos();
        List<PrescripcionProcedimiento> pendientes = procedimientoService.listarPrescripcionesPendientes();
        vista.mostrarPrescripciones(pendientes, procedimientos);
        int prescripcionId = elegir("Prescripciones", aMapa(pendientes, PrescripcionProcedimiento::getId,
                p -> procedimientos.getOrDefault(p.getCatalogoProcedimientoId(), "-")));
        PrescripcionProcedimiento prescripcion = procedimientoService.buscarPrescripcion(prescripcionId);
        int mascotaId = procedimientoService.mascotaDePrescripcion(prescripcion);

        int veterinarioId = elegirVeterinario();
        LocalDateTime fechaHora = vista.pedirFechaHoraFutura("del procedimiento");
        procedimientoService.programar(new ProcedimientoEspecial(mascotaId, veterinarioId,
                prescripcion.getCatalogoProcedimientoId(), prescripcion.getId(), fechaHora));
        vista.mostrarMensaje("Procedimiento programado correctamente");
    }

    private void programarSinPrescripcion() throws SQLException {
        int mascotaId = elegir("Mascotas", aMapa(mascotaService.listarActivas(), Mascota::getId, Mascota::getNombre));
        int catalogoId = elegir("Procedimientos", nombresProcedimientos());
        int veterinarioId = elegirVeterinario();
        LocalDateTime fechaHora = vista.pedirFechaHoraFutura("del procedimiento");
        procedimientoService.programar(new ProcedimientoEspecial(mascotaId, veterinarioId, catalogoId, null, fechaHora));
        vista.mostrarMensaje("Procedimiento programado correctamente");
    }

    private void listar() throws SQLException {
        vista.mostrarProcedimientos(procedimientoService.listarProcedimientos(),
                aMapa(mascotaService.listarTodas(), Mascota::getId, Mascota::getNombre),
                nombresProcedimientos(),
                aMapa(veterinarioService.listarTodos(), Veterinario::getId, Veterinario::getNombreCompleto),
                aMapa(catalogoService.listarEstadosProcedimientos(), Estado::getId, Estado::getNombre));
    }

    private void verDetalle() throws SQLException {
        ProcedimientoEspecial procedimiento = procedimientoService.buscarPorId(vista.pedirId("consultar"));
        vista.mostrarDetalle(procedimiento,
                mascotaService.buscarPorId(procedimiento.getMascotaId()).getNombre(),
                nombresProcedimientos().getOrDefault(procedimiento.getCatalogoProcedimientoId(), "-"),
                veterinarioService.buscarPorId(procedimiento.getVeterinarioId()).getNombreCompleto(),
                procedimientoService.nombreEstado(procedimiento.getEstadoId()));
    }

    private void cambiarEstado() throws SQLException {
        ProcedimientoEspecial procedimiento = procedimientoService.buscarPorId(vista.pedirId("cambiar de estado"));
        List<Estado> siguientes = procedimientoService.estadosSiguientes(procedimiento);
        int estadoId = elegir("Nuevo estado", aMapa(siguientes, Estado::getId, Estado::getNombre));

        switch (procedimientoService.nombreEstado(estadoId)) {
            case ProcedimientoService.PREOPERATORIO ->
                procedimiento.setInformacionPreoperatoria(vista.pedirTexto("Información preoperatoria (exámenes, ayuno, anestesia)"));
            case ProcedimientoService.EN_PROCESO ->
                procedimiento.setDetalleProcedimiento(vista.pedirTexto("Detalle del procedimiento"));
            case ProcedimientoService.POSTOPERATORIO ->
                procedimiento.setComplicaciones(vista.pedirTextoOpcional("¿Hubo complicaciones?", "Complicaciones"));
            case ProcedimientoService.SUSPENDIDO ->
                procedimiento.setComplicaciones(vista.pedirTexto("Motivo de la suspensión"));
            case ProcedimientoService.FINALIZADO -> {
                procedimiento.setSeguimientoPostoperatorio(vista.pedirTexto("Seguimiento postoperatorio"));
                procedimiento.setProximoControl(vista.pedirProximoControl());
            }
            default -> { }
        }
        procedimientoService.cambiarEstado(procedimiento, estadoId);
        vista.mostrarMensaje("Estado del procedimiento actualizado correctamente");
    }

    private int elegirVeterinario() throws SQLException {
        return elegir("Veterinarios",
                aMapa(veterinarioService.listarActivos(), Veterinario::getId, Veterinario::getNombreCompleto));
    }

    private Map<Integer, String> nombresProcedimientos() throws SQLException {
        Map<Integer, String> servicios = aMapa(catalogoService.listarServicios(), Servicio::getId, Servicio::getNombre);
        return aMapa(catalogoService.listarCatalogoProcedimientos(), CatalogoProcedimiento::getId,
                c -> servicios.getOrDefault(c.getServicioId(), "-"));
    }
}
