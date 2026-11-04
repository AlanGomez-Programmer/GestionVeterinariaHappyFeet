package com.alangomez.gestionveterinariahappyfeet.service.pacientes;

import com.alangomez.gestionveterinariahappyfeet.model.dao.pacientes.AlergiaDAO;
import com.alangomez.gestionveterinariahappyfeet.model.dao.pacientes.DuenioDAO;
import com.alangomez.gestionveterinariahappyfeet.model.dao.pacientes.HistorialDuenioDAO;
import com.alangomez.gestionveterinariahappyfeet.model.dao.pacientes.MascotaDAO;
import com.alangomez.gestionveterinariahappyfeet.model.dao.pacientes.ProcedimientoQuirurgicoPrevioDAO;
import com.alangomez.gestionveterinariahappyfeet.model.dao.pacientes.VacunaPreviaDAO;
import com.alangomez.gestionveterinariahappyfeet.model.entities.pacientes.Alergia;
import com.alangomez.gestionveterinariahappyfeet.model.entities.pacientes.Duenio;
import com.alangomez.gestionveterinariahappyfeet.model.entities.pacientes.HistorialDuenio;
import com.alangomez.gestionveterinariahappyfeet.model.entities.pacientes.ProcedimientoQuirurgicoPrevio;
import com.alangomez.gestionveterinariahappyfeet.model.entities.pacientes.VacunaPrevia;
import com.alangomez.gestionveterinariahappyfeet.model.entities.pacientes.mascota.Mascota;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class MascotaService {

    private final MascotaDAO mascotaDAO;
    private final DuenioDAO duenioDAO;
    private final AlergiaDAO alergiaDAO;
    private final HistorialDuenioDAO historialDuenioDAO;
    private final VacunaPreviaDAO vacunaPreviaDAO;
    private final ProcedimientoQuirurgicoPrevioDAO procedimientoPrevioDAO;

    public MascotaService(MascotaDAO mascotaDAO, DuenioDAO duenioDAO, AlergiaDAO alergiaDAO,
            HistorialDuenioDAO historialDuenioDAO, VacunaPreviaDAO vacunaPreviaDAO,
            ProcedimientoQuirurgicoPrevioDAO procedimientoPrevioDAO) {
        this.mascotaDAO = mascotaDAO;
        this.duenioDAO = duenioDAO;
        this.alergiaDAO = alergiaDAO;
        this.historialDuenioDAO = historialDuenioDAO;
        this.vacunaPreviaDAO = vacunaPreviaDAO;
        this.procedimientoPrevioDAO = procedimientoPrevioDAO;
    }

    public void validarNuevaMascota(Mascota mascota) throws SQLException {
        validarDuenioActivo(mascota.getDuenioId());
        if (mascota.getMicrochip() != null && mascotaDAO.existeMicrochip(mascota.getMicrochip())) {
            throw new IllegalArgumentException("Ya existe una mascota con el microchip " + mascota.getMicrochip());
        }
    }

    public void registrar(Mascota mascota, List<Alergia> alergias) throws SQLException {
        validarNuevaMascota(mascota);
        long alergiasDistintas = alergias.stream()
                .map(Alergia::getCatalogoAlergiaId)
                .distinct()
                .count();
        if (alergiasDistintas != alergias.size()) {
            throw new IllegalArgumentException("No se puede registrar la misma alergia dos veces");
        }

        Connection con = mascotaDAO.obtenerConexion();
        try {
            con.setAutoCommit(false);
            int mascotaId = mascotaDAO.agregarMascota(con, mascota);
            mascota.setId(mascotaId);
            for (Alergia alergia : alergias) {
                alergia.setMascotaId(mascotaId);
                alergiaDAO.agregarAlergia(con, alergia);
            }
            historialDuenioDAO.registrarDuenio(con,
                    new HistorialDuenio(mascotaId, mascota.getDuenioId(), "Registro inicial"));
            con.commit();
        } catch (SQLException e) {
            con.rollback();
            throw e;
        } finally {
            con.setAutoCommit(true);
            con.close();
        }
    }

    public List<Mascota> listarActivas() throws SQLException {
        return mascotaDAO.listarMascotasActivas();
    }

    public Mascota buscarPorId(int id) throws SQLException {
        Mascota mascota = mascotaDAO.buscarMascota(id);
        if (mascota == null) {
            throw new IllegalArgumentException("No existe una mascota con el id " + id);
        }
        return mascota;
    }

    public Mascota buscarActivaPorId(int id) throws SQLException {
        Mascota mascota = buscarPorId(id);
        if (!mascota.isActivo()) {
            throw new IllegalArgumentException("La mascota " + mascota.getNombre() + " está inactiva");
        }
        return mascota;
    }

    public void transferir(int mascotaId, int nuevoDuenioId, String motivo) throws SQLException {
        Mascota mascota = buscarActivaPorId(mascotaId);
        validarDuenioActivo(nuevoDuenioId);
        if (mascota.getDuenioId() == nuevoDuenioId) {
            throw new IllegalArgumentException("La mascota ya pertenece a ese dueño");
        }

        Connection con = mascotaDAO.obtenerConexion();
        try {
            con.setAutoCommit(false);
            historialDuenioDAO.cerrarDuenioActual(con, mascotaId);
            historialDuenioDAO.registrarDuenio(con, new HistorialDuenio(mascotaId, nuevoDuenioId, motivo));
            mascotaDAO.actualizarDuenio(con, mascotaId, nuevoDuenioId);
            con.commit();
        } catch (SQLException e) {
            con.rollback();
            throw e;
        } finally {
            con.setAutoCommit(true);
            con.close();
        }
    }

    public void agregarAlergia(Alergia alergia) throws SQLException {
        buscarActivaPorId(alergia.getMascotaId());
        boolean yaRegistrada = alergiaDAO.listarAlergiasPorMascota(alergia.getMascotaId()).stream()
                .anyMatch(a -> a.getCatalogoAlergiaId() == alergia.getCatalogoAlergiaId());
        if (yaRegistrada) {
            throw new IllegalArgumentException("La mascota ya tiene registrada esa alergia");
        }
        alergiaDAO.agregarAlergia(alergia);
    }

    public void agregarVacunaPrevia(VacunaPrevia vacuna) throws SQLException {
        Mascota mascota = buscarActivaPorId(vacuna.getMascotaId());
        validarFechaHistorial(mascota, vacuna.getFechaAplicacion(), "de aplicación");
        vacunaPreviaDAO.agregarVacunaPrevia(vacuna);
    }

    public void agregarProcedimientoPrevio(ProcedimientoQuirurgicoPrevio procedimiento) throws SQLException {
        Mascota mascota = buscarActivaPorId(procedimiento.getMascotaId());
        validarFechaHistorial(mascota, procedimiento.getFechaAproximada(), "del procedimiento");
        procedimientoPrevioDAO.agregarProcedimientoPrevio(procedimiento);
    }

    public void actualizarPeso(int mascotaId, double peso) throws SQLException {
        buscarActivaPorId(mascotaId);
        if (peso <= 0) {
            throw new IllegalArgumentException("El peso debe ser mayor que cero");
        }
        mascotaDAO.actualizarPeso(mascotaId, peso);
    }

    public void desactivar(int mascotaId) throws SQLException {
        buscarActivaPorId(mascotaId);
        mascotaDAO.desactivarMascota(mascotaId);
    }

    public List<Alergia> listarAlergias(int mascotaId) throws SQLException {
        return alergiaDAO.listarAlergiasPorMascota(mascotaId);
    }

    public List<VacunaPrevia> listarVacunasPrevias(int mascotaId) throws SQLException {
        return vacunaPreviaDAO.listarVacunasPorMascota(mascotaId);
    }

    public List<ProcedimientoQuirurgicoPrevio> listarProcedimientosPrevios(int mascotaId) throws SQLException {
        return procedimientoPrevioDAO.listarProcedimientosPorMascota(mascotaId);
    }

    public List<HistorialDuenio> listarHistorialDuenios(int mascotaId) throws SQLException {
        return historialDuenioDAO.listarHistorialPorMascota(mascotaId);
    }

    private void validarDuenioActivo(int duenioId) throws SQLException {
        Duenio duenio = duenioDAO.buscarDuenio(duenioId);
        if (duenio == null) {
            throw new IllegalArgumentException("No existe un dueño con el id " + duenioId);
        }
        if (!duenio.isActivo()) {
            throw new IllegalArgumentException("El dueño " + duenio.getNombreCompleto() + " está inactivo");
        }
    }

    private void validarFechaHistorial(Mascota mascota, LocalDate fecha, String descripcion) {
        if (fecha == null) {
            return;
        }
        if (fecha.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("La fecha " + descripcion + " no puede ser futura");
        }
        if (mascota.getFechaNacimiento() != null && fecha.isBefore(mascota.getFechaNacimiento())) {
            throw new IllegalArgumentException("La fecha " + descripcion + " no puede ser anterior al nacimiento");
        }
    }
}
