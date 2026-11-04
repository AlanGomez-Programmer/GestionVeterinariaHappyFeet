package com.alangomez.gestionveterinariahappyfeet.service.servicios;

import com.alangomez.gestionveterinariahappyfeet.model.dao.pacientes.MascotaDAO;
import com.alangomez.gestionveterinariahappyfeet.model.dao.servicios.CatalogoProcedimientoDAO;
import com.alangomez.gestionveterinariahappyfeet.model.dao.servicios.ConsultaDAO;
import com.alangomez.gestionveterinariahappyfeet.model.dao.servicios.EstadoDAO;
import com.alangomez.gestionveterinariahappyfeet.model.dao.servicios.HistorialServicioDAO;
import com.alangomez.gestionveterinariahappyfeet.model.dao.servicios.PrescripcionProcedimientoDAO;
import com.alangomez.gestionveterinariahappyfeet.model.dao.servicios.ProcedimientoEspecialDAO;
import com.alangomez.gestionveterinariahappyfeet.model.dao.servicios.ServicioDAO;
import com.alangomez.gestionveterinariahappyfeet.model.dao.servicios.VeterinarioDAO;
import com.alangomez.gestionveterinariahappyfeet.model.entities.pacientes.mascota.Mascota;
import com.alangomez.gestionveterinariahappyfeet.model.entities.servicios.CatalogoProcedimiento;
import com.alangomez.gestionveterinariahappyfeet.model.entities.servicios.Estado;
import com.alangomez.gestionveterinariahappyfeet.model.entities.servicios.HistorialServicio;
import com.alangomez.gestionveterinariahappyfeet.model.entities.servicios.PrescripcionProcedimiento;
import com.alangomez.gestionveterinariahappyfeet.model.entities.servicios.ProcedimientoEspecial;
import com.alangomez.gestionveterinariahappyfeet.model.entities.servicios.Servicio;
import com.alangomez.gestionveterinariahappyfeet.model.entities.servicios.Veterinario;
import com.alangomez.gestionveterinariahappyfeet.model.entities.servicios.consulta.Consulta;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class ProcedimientoService {

    public static final String PROGRAMADO = "Programado";
    public static final String PREOPERATORIO = "Preoperatorio";
    public static final String EN_PROCESO = "En proceso";
    public static final String POSTOPERATORIO = "Postoperatorio";
    public static final String FINALIZADO = "Finalizado";
    public static final String SUSPENDIDO = "Suspendido";
    public static final String CANCELADO = "Cancelado";

    // A qué estados se puede pasar desde cada estado
    private static final Map<String, List<String>> TRANSICIONES = Map.of(
            PROGRAMADO, List.of(PREOPERATORIO, CANCELADO),
            PREOPERATORIO, List.of(EN_PROCESO, SUSPENDIDO, CANCELADO),
            EN_PROCESO, List.of(POSTOPERATORIO, SUSPENDIDO),
            POSTOPERATORIO, List.of(FINALIZADO),
            SUSPENDIDO, List.of(PREOPERATORIO, CANCELADO)
    );

    private final ProcedimientoEspecialDAO procedimientoDAO;
    private final PrescripcionProcedimientoDAO prescripcionDAO;
    private final ConsultaDAO consultaDAO;
    private final CatalogoProcedimientoDAO catalogoDAO;
    private final ServicioDAO servicioDAO;
    private final EstadoDAO estadoDAO;
    private final HistorialServicioDAO historialServicioDAO;
    private final MascotaDAO mascotaDAO;
    private final VeterinarioDAO veterinarioDAO;

    public ProcedimientoService(ProcedimientoEspecialDAO procedimientoDAO, PrescripcionProcedimientoDAO prescripcionDAO,
            ConsultaDAO consultaDAO, CatalogoProcedimientoDAO catalogoDAO, ServicioDAO servicioDAO, EstadoDAO estadoDAO,
            HistorialServicioDAO historialServicioDAO, MascotaDAO mascotaDAO, VeterinarioDAO veterinarioDAO) {
        this.procedimientoDAO = procedimientoDAO;
        this.prescripcionDAO = prescripcionDAO;
        this.consultaDAO = consultaDAO;
        this.catalogoDAO = catalogoDAO;
        this.servicioDAO = servicioDAO;
        this.estadoDAO = estadoDAO;
        this.historialServicioDAO = historialServicioDAO;
        this.mascotaDAO = mascotaDAO;
        this.veterinarioDAO = veterinarioDAO;
    }

    public void programar(ProcedimientoEspecial procedimiento) throws SQLException {
        Mascota mascota = mascotaDAO.buscarMascota(procedimiento.getMascotaId());
        if (mascota == null || !mascota.isActivo()) {
            throw new IllegalArgumentException("La mascota no existe o está inactiva");
        }
        Veterinario veterinario = veterinarioDAO.buscarVeterinario(procedimiento.getVeterinarioId());
        if (veterinario == null || !veterinario.isActivo()) {
            throw new IllegalArgumentException("El veterinario no existe o está inactivo");
        }
        if (catalogoDAO.buscarCatalogoProcedimiento(procedimiento.getCatalogoProcedimientoId()) == null) {
            throw new IllegalArgumentException("El procedimiento elegido no existe en el catálogo");
        }
        if (procedimiento.getFechaHora() == null || !procedimiento.getFechaHora().isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("El procedimiento debe programarse en una fecha y hora futura");
        }
        if (procedimiento.getPrescripcionId() != null) {
            validarPrescripcion(procedimiento);
        }
        procedimiento.setEstadoId(idEstado(PROGRAMADO));
        procedimientoDAO.agregarProcedimiento(procedimiento);
    }

    public List<PrescripcionProcedimiento> listarPrescripcionesPendientes() throws SQLException {
        return prescripcionDAO.listarPrescripcionesPendientes();
    }

    public PrescripcionProcedimiento buscarPrescripcion(int id) throws SQLException {
        PrescripcionProcedimiento prescripcion = prescripcionDAO.buscarPrescripcion(id);
        if (prescripcion == null) {
            throw new IllegalArgumentException("No existe una prescripción con el id " + id);
        }
        return prescripcion;
    }

    public int mascotaDePrescripcion(PrescripcionProcedimiento prescripcion) throws SQLException {
        Consulta consulta = consultaDAO.buscarConsulta(prescripcion.getConsultaId());
        if (consulta == null) {
            throw new IllegalArgumentException("La consulta de la prescripción no existe");
        }
        return consulta.getMascotaId();
    }

    public List<ProcedimientoEspecial> listarProcedimientos() throws SQLException {
        return procedimientoDAO.listarProcedimientos();
    }

    public ProcedimientoEspecial buscarPorId(int id) throws SQLException {
        ProcedimientoEspecial procedimiento = procedimientoDAO.buscarProcedimiento(id);
        if (procedimiento == null) {
            throw new IllegalArgumentException("No existe un procedimiento con el id " + id);
        }
        return procedimiento;
    }

    public List<Estado> estadosSiguientes(ProcedimientoEspecial procedimiento) throws SQLException {
        String estadoActual = nombreEstado(procedimiento.getEstadoId());
        List<String> siguientes = TRANSICIONES.getOrDefault(estadoActual, List.of());
        if (siguientes.isEmpty()) {
            throw new IllegalArgumentException("Un procedimiento " + estadoActual.toLowerCase() + " ya no puede cambiar de estado");
        }
        return estadoDAO.listarEstadosProcedimientos().stream()
                .filter(estado -> siguientes.contains(estado.getNombre()))
                .toList();
    }

    public String nombreEstado(int estadoId) throws SQLException {
        return estadoDAO.listarEstadosProcedimientos().stream()
                .filter(estado -> estado.getId() == estadoId)
                .map(Estado::getNombre)
                .findFirst()
                .orElse("-");
    }

    // Cambia el estado y, si el procedimiento termina, lo registra en el historial de servicios
    public void cambiarEstado(ProcedimientoEspecial procedimiento, int nuevoEstadoId) throws SQLException {
        boolean permitido = estadosSiguientes(procedimiento).stream()
                .anyMatch(estado -> estado.getId() == nuevoEstadoId);
        if (!permitido) {
            throw new IllegalArgumentException("No se puede pasar el procedimiento a ese estado");
        }
        String nuevoEstado = nombreEstado(nuevoEstadoId);
        validarDatosDelEstado(procedimiento, nuevoEstado);
        procedimiento.setEstadoId(nuevoEstadoId);

        HistorialServicio historial = null;
        if (nuevoEstado.equals(FINALIZADO)) {
            CatalogoProcedimiento catalogo = catalogoDAO.buscarCatalogoProcedimiento(procedimiento.getCatalogoProcedimientoId());
            Servicio servicio = servicioDAO.buscarServicio(catalogo.getServicioId());
            historial = new HistorialServicio(servicio.getId(), procedimiento.getMascotaId(),
                    procedimiento.getVeterinarioId(), servicio.getPrecio());
        }

        Connection con = procedimientoDAO.obtenerConexion();
        try {
            con.setAutoCommit(false);
            procedimientoDAO.actualizarProcedimiento(con, procedimiento);
            if (historial != null) {
                int historialId = historialServicioDAO.agregarHistorial(con, historial);
                historialServicioDAO.vincularConProcedimiento(con, historialId, procedimiento.getId());
            }
            con.commit();
        } catch (SQLException e) {
            con.rollback();
            throw e;
        } finally {
            con.setAutoCommit(true);
            con.close();
        }
    }

    private void validarDatosDelEstado(ProcedimientoEspecial procedimiento, String nuevoEstado) {
        if (nuevoEstado.equals(PREOPERATORIO) && estaVacio(procedimiento.getInformacionPreoperatoria())) {
            throw new IllegalArgumentException("Para pasar a preoperatorio debe registrar la información preoperatoria");
        }
        if (nuevoEstado.equals(EN_PROCESO) && estaVacio(procedimiento.getDetalleProcedimiento())) {
            throw new IllegalArgumentException("Para iniciar el procedimiento debe registrar el detalle");
        }
        if (nuevoEstado.equals(FINALIZADO) && estaVacio(procedimiento.getSeguimientoPostoperatorio())) {
            throw new IllegalArgumentException("Para finalizar debe registrar el seguimiento postoperatorio");
        }
    }

    private void validarPrescripcion(ProcedimientoEspecial procedimiento) throws SQLException {
        PrescripcionProcedimiento prescripcion = buscarPrescripcion(procedimiento.getPrescripcionId());
        boolean pendiente = prescripcionDAO.listarPrescripcionesPendientes().stream()
                .anyMatch(p -> p.getId() == prescripcion.getId());
        if (!pendiente) {
            throw new IllegalArgumentException("Esa prescripción ya tiene un procedimiento programado");
        }
        if (mascotaDePrescripcion(prescripcion) != procedimiento.getMascotaId()) {
            throw new IllegalArgumentException("La prescripción pertenece a otra mascota");
        }
        if (prescripcion.getCatalogoProcedimientoId() != procedimiento.getCatalogoProcedimientoId()) {
            throw new IllegalArgumentException("El procedimiento no coincide con el prescrito");
        }
    }

    private int idEstado(String nombre) throws SQLException {
        return estadoDAO.listarEstadosProcedimientos().stream()
                .filter(estado -> estado.getNombre().equals(nombre))
                .map(Estado::getId)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "No existe el estado de procedimiento '" + nombre + "'. Ejecute de nuevo data.sql"));
    }

    private boolean estaVacio(String texto) {
        return texto == null || texto.isBlank();
    }
}
