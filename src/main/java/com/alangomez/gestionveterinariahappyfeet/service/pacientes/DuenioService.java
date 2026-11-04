package com.alangomez.gestionveterinariahappyfeet.service.pacientes;

import com.alangomez.gestionveterinariahappyfeet.model.dao.pacientes.DuenioDAO;
import com.alangomez.gestionveterinariahappyfeet.model.dao.pacientes.MascotaDAO;
import com.alangomez.gestionveterinariahappyfeet.model.entities.pacientes.Duenio;
import java.sql.SQLException;
import java.util.List;

public class DuenioService {

    private final DuenioDAO duenioDAO;
    private final MascotaDAO mascotaDAO;

    public DuenioService(DuenioDAO duenioDAO, MascotaDAO mascotaDAO) {
        this.duenioDAO = duenioDAO;
        this.mascotaDAO = mascotaDAO;
    }

    public void registrar(Duenio duenio) throws SQLException {
        if (duenioDAO.existeEmail(duenio.getEmail())) {
            throw new IllegalArgumentException("Ya existe un dueño con el correo " + duenio.getEmail());
        }
        if (duenioDAO.existeDocumentoPersonal(duenio.getDocumentoIdentidad())) {
            throw new IllegalArgumentException(
                    "Ya existe un dueño con el documento " + duenio.getDocumentoIdentidad());
        }
        duenioDAO.agregarDuenio(duenio);
    }

    public List<Duenio> listarActivos() throws SQLException {
        return duenioDAO.listarDuenios().stream()
                .filter(Duenio::isActivo)
                .toList();
    }

    public List<Duenio> listarTodos() throws SQLException {
        return duenioDAO.listarDuenios();
    }

    public Duenio buscarPorId(int id) throws SQLException {
        Duenio duenio = duenioDAO.buscarDuenio(id);
        if (duenio == null) {
            throw new IllegalArgumentException("No existe un dueño con el id " + id);
        }
        return duenio;
    }

    public void desactivar(int id) throws SQLException {
        Duenio duenio = buscarPorId(id);
        if (!duenio.isActivo()) {
            throw new IllegalArgumentException("El dueño " + duenio.getNombreCompleto() + " ya está inactivo");
        }
        int mascotasActivas = mascotaDAO.contarMascotasActivasPorDuenio(id);
        if (mascotasActivas > 0) {
            throw new IllegalArgumentException("El dueño " + duenio.getNombreCompleto() + " tiene "
                    + mascotasActivas + " mascota(s) activa(s). Primero transfiéralas o elimínelas");
        }
        duenioDAO.desactivarDuenio(id);
    }
}