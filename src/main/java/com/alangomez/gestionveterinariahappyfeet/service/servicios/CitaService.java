package com.alangomez.gestionveterinariahappyfeet.service.servicios;

import com.alangomez.gestionveterinariahappyfeet.model.dao.pacientes.MascotaDAO;
import com.alangomez.gestionveterinariahappyfeet.model.dao.servicios.CitaDAO;
import com.alangomez.gestionveterinariahappyfeet.model.dao.servicios.EstadoDAO;
import com.alangomez.gestionveterinariahappyfeet.model.dao.servicios.VeterinarioDAO;
import com.alangomez.gestionveterinariahappyfeet.model.entities.pacientes.mascota.Mascota;
import com.alangomez.gestionveterinariahappyfeet.model.entities.servicios.Cita;
import com.alangomez.gestionveterinariahappyfeet.model.entities.servicios.Estado;
import com.alangomez.gestionveterinariahappyfeet.model.entities.servicios.Veterinario;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class CitaService {

    public static final String PROGRAMADA = "Programada";
    public static final String EN_PROCESO = "En proceso";
    public static final String FINALIZADA = "Finalizada";
    public static final String CANCELADA = "Cancelada";

    private static final int MINUTOS_ENTRE_CITAS = 30;

    // A qué estados se puede pasar desde cada estado (Finalizada solo se alcanza al atender la cita)
    private static final Map<String, List<String>> TRANSICIONES = Map.of(
            PROGRAMADA, List.of(EN_PROCESO, CANCELADA),
            EN_PROCESO, List.of(CANCELADA)
    );

    private final CitaDAO citaDAO;
    private final EstadoDAO estadoDAO;
    private final MascotaDAO mascotaDAO;
    private final VeterinarioDAO veterinarioDAO;

    public CitaService(CitaDAO citaDAO, EstadoDAO estadoDAO, MascotaDAO mascotaDAO, VeterinarioDAO veterinarioDAO) {
        this.citaDAO = citaDAO;
        this.estadoDAO = estadoDAO;
        this.mascotaDAO = mascotaDAO;
        this.veterinarioDAO = veterinarioDAO;
    }

    public void programar(Cita cita) throws SQLException {
        Mascota mascota = mascotaDAO.buscarMascota(cita.getMascotaId());
        if (mascota == null || !mascota.isActivo()) {
            throw new IllegalArgumentException("La mascota no existe o está inactiva");
        }
        Veterinario veterinario = veterinarioDAO.buscarVeterinario(cita.getVeterinarioId());
        if (veterinario == null || !veterinario.isActivo()) {
            throw new IllegalArgumentException("El veterinario no existe o está inactivo");
        }
        if (cita.getFechaHora() == null || !cita.getFechaHora().isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("La cita debe programarse en una fecha y hora futura");
        }
        int canceladaId = idEstado(CANCELADA);
        boolean seCruza = citaDAO.listarCitasPorVeterinario(cita.getVeterinarioId()).stream()
                .filter(otra -> otra.getEstadoId() != canceladaId)
                .anyMatch(otra -> Math.abs(Duration.between(otra.getFechaHora(), cita.getFechaHora()).toMinutes())
                        < MINUTOS_ENTRE_CITAS);
        if (seCruza) {
            throw new IllegalArgumentException("El veterinario " + veterinario.getNombreCompleto()
                    + " ya tiene una cita a menos de " + MINUTOS_ENTRE_CITAS + " minutos de ese horario");
        }
        cita.setEstadoId(idEstado(PROGRAMADA));
        citaDAO.agregarCita(cita);
    }

    public List<Cita> listarPorFecha(LocalDate fecha) throws SQLException {
        return citaDAO.listarCitasPorFecha(fecha);
    }

    public List<Cita> listarPendientes() throws SQLException {
        List<Cita> pendientes = new ArrayList<>(citaDAO.listarCitasPorEstado(idEstado(PROGRAMADA)));
        pendientes.addAll(citaDAO.listarCitasPorEstado(idEstado(EN_PROCESO)));
        return pendientes.stream()
                .sorted(Comparator.comparing(Cita::getFechaHora))
                .toList();
    }

    public Cita buscarPorId(int id) throws SQLException {
        Cita cita = citaDAO.buscarCita(id);
        if (cita == null) {
            throw new IllegalArgumentException("No existe una cita con el id " + id);
        }
        return cita;
    }

    public List<Estado> estadosSiguientes(Cita cita) throws SQLException {
        String estadoActual = nombreEstado(cita.getEstadoId());
        List<String> siguientes = TRANSICIONES.getOrDefault(estadoActual, List.of());
        if (siguientes.isEmpty()) {
            throw new IllegalArgumentException("Una cita " + estadoActual.toLowerCase() + " ya no puede cambiar de estado");
        }
        return estadoDAO.listarEstadosCitas().stream()
                .filter(estado -> siguientes.contains(estado.getNombre()))
                .toList();
    }

    public void cambiarEstado(int citaId, int nuevoEstadoId) throws SQLException {
        Cita cita = buscarPorId(citaId);
        boolean permitido = estadosSiguientes(cita).stream()
                .anyMatch(estado -> estado.getId() == nuevoEstadoId);
        if (!permitido) {
            throw new IllegalArgumentException("No se puede pasar la cita a ese estado");
        }
        citaDAO.actualizarEstado(citaId, nuevoEstadoId);
    }

    public Cita buscarCitaParaAtender(int citaId) throws SQLException {
        Cita cita = buscarPorId(citaId);
        String estado = nombreEstado(cita.getEstadoId());
        if (!estado.equals(PROGRAMADA) && !estado.equals(EN_PROCESO)) {
            throw new IllegalArgumentException("Solo se pueden atender citas programadas o en proceso. Esta cita está " + estado);
        }
        return cita;
    }

    public int idEstado(String nombre) throws SQLException {
        return estadoDAO.listarEstadosCitas().stream()
                .filter(estado -> estado.getNombre().equals(nombre))
                .map(Estado::getId)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "No existe el estado de cita '" + nombre + "'. Ejecute de nuevo data.sql"));
    }

    private String nombreEstado(int estadoId) throws SQLException {
        return estadoDAO.listarEstadosCitas().stream()
                .filter(estado -> estado.getId() == estadoId)
                .map(Estado::getNombre)
                .findFirst()
                .orElse("-");
    }
}
