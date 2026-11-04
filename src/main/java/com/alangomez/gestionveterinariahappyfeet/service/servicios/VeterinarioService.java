package com.alangomez.gestionveterinariahappyfeet.service.servicios;

import com.alangomez.gestionveterinariahappyfeet.model.dao.servicios.CitaDAO;
import com.alangomez.gestionveterinariahappyfeet.model.dao.servicios.EstadoDAO;
import com.alangomez.gestionveterinariahappyfeet.model.dao.servicios.VeterinarioDAO;
import com.alangomez.gestionveterinariahappyfeet.model.entities.servicios.Estado;
import com.alangomez.gestionveterinariahappyfeet.model.entities.servicios.Veterinario;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class VeterinarioService {

    private final VeterinarioDAO veterinarioDAO;
    private final CitaDAO citaDAO;
    private final EstadoDAO estadoDAO;

    public VeterinarioService(VeterinarioDAO veterinarioDAO, CitaDAO citaDAO, EstadoDAO estadoDAO) {
        this.veterinarioDAO = veterinarioDAO;
        this.citaDAO = citaDAO;
        this.estadoDAO = estadoDAO;
    }

    public void registrar(Veterinario veterinario) throws SQLException {
        if (veterinarioDAO.existeDocumento(veterinario.getDocumentoIdentidad())) {
            throw new IllegalArgumentException("Ya existe un veterinario con el documento " + veterinario.getDocumentoIdentidad());
        }
        if (veterinarioDAO.existeLicencia(veterinario.getLicenciaProfesional())) {
            throw new IllegalArgumentException("Ya existe un veterinario con la licencia " + veterinario.getLicenciaProfesional());
        }
        if (veterinario.getEmail() != null && veterinarioDAO.existeEmail(veterinario.getEmail())) {
            throw new IllegalArgumentException("Ya existe un veterinario con el correo " + veterinario.getEmail());
        }
        if (veterinario.getFechaContratacion() != null && veterinario.getFechaContratacion().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("La fecha de contratación no puede ser futura");
        }
        veterinarioDAO.agregarVeterinario(veterinario);
    }

    public List<Veterinario> listarActivos() throws SQLException {
        return veterinarioDAO.listarVeterinarios().stream()
                .filter(Veterinario::isActivo)
                .toList();
    }

    public List<Veterinario> listarTodos() throws SQLException {
        return veterinarioDAO.listarVeterinarios();
    }

    public Veterinario buscarPorId(int id) throws SQLException {
        Veterinario veterinario = veterinarioDAO.buscarVeterinario(id);
        if (veterinario == null) {
            throw new IllegalArgumentException("No existe un veterinario con el id " + id);
        }
        return veterinario;
    }

    public Veterinario buscarActivoPorId(int id) throws SQLException {
        Veterinario veterinario = buscarPorId(id);
        if (!veterinario.isActivo()) {
            throw new IllegalArgumentException("El veterinario " + veterinario.getNombreCompleto() + " está inactivo");
        }
        return veterinario;
    }

    public void desactivar(int id) throws SQLException {
        Veterinario veterinario = buscarActivoPorId(id);
        List<Integer> estadosPendientes = estadoDAO.listarEstadosCitas().stream()
                .filter(e -> e.getNombre().equals(CitaService.PROGRAMADA) || e.getNombre().equals(CitaService.EN_PROCESO))
                .map(Estado::getId)
                .toList();
        long citasPendientes = citaDAO.listarCitasPorVeterinario(id).stream()
                .filter(cita -> estadosPendientes.contains(cita.getEstadoId()))
                .count();
        if (citasPendientes > 0) {
            throw new IllegalArgumentException("El veterinario " + veterinario.getNombreCompleto() + " tiene "
                    + citasPendientes + " cita(s) pendiente(s). Primero cancélelas o atiéndalas");
        }
        veterinarioDAO.desactivarVeterinario(id);
    }
}
