package com.alangomez.gestionveterinariahappyfeet.service.servicios;

import com.alangomez.gestionveterinariahappyfeet.model.dao.pacientes.MascotaDAO;
import com.alangomez.gestionveterinariahappyfeet.model.dao.servicios.CitaDAO;
import com.alangomez.gestionveterinariahappyfeet.model.dao.servicios.ConsultaDAO;
import com.alangomez.gestionveterinariahappyfeet.model.dao.servicios.EstadoDAO;
import com.alangomez.gestionveterinariahappyfeet.model.dao.servicios.HistorialServicioDAO;
import com.alangomez.gestionveterinariahappyfeet.model.dao.servicios.InsumoDAO;
import com.alangomez.gestionveterinariahappyfeet.model.dao.servicios.PrescripcionProcedimientoDAO;
import com.alangomez.gestionveterinariahappyfeet.model.dao.servicios.ServicioDAO;
import com.alangomez.gestionveterinariahappyfeet.model.dao.servicios.VeterinarioDAO;
import com.alangomez.gestionveterinariahappyfeet.model.entities.inventario.ConsumoLote;
import com.alangomez.gestionveterinariahappyfeet.model.entities.pacientes.mascota.Mascota;
import com.alangomez.gestionveterinariahappyfeet.model.entities.servicios.Cita;
import com.alangomez.gestionveterinariahappyfeet.model.entities.servicios.Estado;
import com.alangomez.gestionveterinariahappyfeet.model.entities.servicios.HistorialServicio;
import com.alangomez.gestionveterinariahappyfeet.model.entities.servicios.PrescripcionMedicamento;
import com.alangomez.gestionveterinariahappyfeet.model.entities.servicios.PrescripcionProcedimiento;
import com.alangomez.gestionveterinariahappyfeet.model.entities.servicios.Servicio;
import com.alangomez.gestionveterinariahappyfeet.model.entities.servicios.Veterinario;
import com.alangomez.gestionveterinariahappyfeet.model.entities.servicios.consulta.Consulta;
import com.alangomez.gestionveterinariahappyfeet.service.inventario.InventarioService;
import com.alangomez.gestionveterinariahappyfeet.service.inventario.ProductoService;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ConsultaService {

    private static final String CONSULTA_FINALIZADA = "Finalizado";

    private final ConsultaDAO consultaDAO;
    private final CitaDAO citaDAO;
    private final EstadoDAO estadoDAO;
    private final ServicioDAO servicioDAO;
    private final HistorialServicioDAO historialServicioDAO;
    private final PrescripcionProcedimientoDAO prescripcionDAO;
    private final MascotaDAO mascotaDAO;
    private final VeterinarioDAO veterinarioDAO;
    private final CitaService citaService;
    private final InsumoDAO insumoDAO;
    private final InventarioService inventarioService;
    private final ProductoService productoService;

    public ConsultaService(ConsultaDAO consultaDAO, CitaDAO citaDAO, EstadoDAO estadoDAO, ServicioDAO servicioDAO,
            HistorialServicioDAO historialServicioDAO, PrescripcionProcedimientoDAO prescripcionDAO,
            MascotaDAO mascotaDAO, VeterinarioDAO veterinarioDAO, CitaService citaService,
            InsumoDAO insumoDAO, InventarioService inventarioService, ProductoService productoService) {
        this.consultaDAO = consultaDAO;
        this.citaDAO = citaDAO;
        this.estadoDAO = estadoDAO;
        this.servicioDAO = servicioDAO;
        this.historialServicioDAO = historialServicioDAO;
        this.prescripcionDAO = prescripcionDAO;
        this.mascotaDAO = mascotaDAO;
        this.veterinarioDAO = veterinarioDAO;
        this.citaService = citaService;
        this.insumoDAO = insumoDAO;
        this.inventarioService = inventarioService;
        this.productoService = productoService;
    }

    // Registra todo lo que pasa en una consulta en un solo paso:
    // la consulta, los servicios realizados, el estado de la cita y el peso de la mascota
    public void registrarConsulta(Consulta consulta, List<Integer> serviciosIds) throws SQLException {
        Mascota mascota = mascotaDAO.buscarMascota(consulta.getMascotaId());
        if (mascota == null || !mascota.isActivo()) {
            throw new IllegalArgumentException("La mascota no existe o está inactiva");
        }
        Veterinario veterinario = veterinarioDAO.buscarVeterinario(consulta.getVeterinarioId());
        if (veterinario == null || !veterinario.isActivo()) {
            throw new IllegalArgumentException("El veterinario no existe o está inactivo");
        }
        if (consulta.getCitaId() != null) {
            Cita cita = citaService.buscarCitaParaAtender(consulta.getCitaId());
            if (cita.getMascotaId() != consulta.getMascotaId()) {
                throw new IllegalArgumentException("La cita pertenece a otra mascota");
            }
        }
        if (serviciosIds.isEmpty()) {
            throw new IllegalArgumentException("Debe registrar al menos un servicio realizado");
        }
        if (serviciosIds.stream().distinct().count() != serviciosIds.size()) {
            throw new IllegalArgumentException("No se puede registrar el mismo servicio dos veces");
        }

        List<Servicio> servicios = new ArrayList<>();
        for (int servicioId : serviciosIds) {
            Servicio servicio = servicioDAO.buscarServicio(servicioId);
            if (servicio == null || !servicio.isActivo()) {
                throw new IllegalArgumentException("El servicio con id " + servicioId + " no existe o está inactivo");
            }
            servicios.add(servicio);
        }
        consulta.setEstadoId(idEstadoConsulta(CONSULTA_FINALIZADA));
        int citaFinalizadaId = citaService.idEstado(CitaService.FINALIZADA);

        Connection con = consultaDAO.obtenerConexion();
        try {
            con.setAutoCommit(false);
            int consultaId = consultaDAO.agregarConsulta(con, consulta);
            consulta.setId(consultaId);
            for (Servicio servicio : servicios) {
                HistorialServicio historial = new HistorialServicio(servicio.getId(), consulta.getMascotaId(),
                        consulta.getVeterinarioId(), servicio.getPrecio());
                int historialId = historialServicioDAO.agregarHistorial(con, historial);
                historialServicioDAO.vincularConConsulta(con, historialId, consultaId);
            }
            if (consulta.getCitaId() != null) {
                citaDAO.actualizarEstado(con, consulta.getCitaId(), citaFinalizadaId);
            }
            if (consulta.getPesoRegistrado() != null) {
                mascotaDAO.actualizarPeso(con, consulta.getMascotaId(), consulta.getPesoRegistrado());
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

    public List<Consulta> listarPorMascota(int mascotaId) throws SQLException {
        return consultaDAO.listarConsultasPorMascota(mascotaId);
    }

    public Consulta buscarPorId(int id) throws SQLException {
        Consulta consulta = consultaDAO.buscarConsulta(id);
        if (consulta == null) {
            throw new IllegalArgumentException("No existe una consulta con el id " + id);
        }
        return consulta;
    }

    public List<HistorialServicio> listarServiciosDeConsulta(int consultaId) throws SQLException {
        return historialServicioDAO.listarServiciosPorConsulta(consultaId);
    }

    public void prescribirProcedimiento(PrescripcionProcedimiento prescripcion) throws SQLException {
        buscarPorId(prescripcion.getConsultaId());
        prescripcionDAO.agregarPrescripcion(prescripcion);
    }

    // Registra los insumos usados en la consulta y los descuenta del inventario
    public void registrarInsumos(int consultaId, Map<Integer, Integer> productosYCantidades) throws SQLException {
        buscarPorId(consultaId);
        for (int productoId : productosYCantidades.keySet()) {
            productoService.buscarActivoPorId(productoId);
        }

        Connection con = consultaDAO.obtenerConexion();
        try {
            con.setAutoCommit(false);
            for (Map.Entry<Integer, Integer> insumo : productosYCantidades.entrySet()) {
                List<ConsumoLote> consumos = inventarioService.descontar(con, insumo.getKey(), insumo.getValue(),
                        "Insumo usado en la consulta #" + consultaId, consultaId, null);
                for (ConsumoLote consumo : consumos) {
                    insumoDAO.agregarInsumoConsulta(con, consultaId, consumo.inventarioId(), consumo.cantidad());
                }
            }
            con.commit();
        } catch (SQLException | IllegalArgumentException e) {
            con.rollback();
            throw e;
        } finally {
            con.setAutoCommit(true);
            con.close();
        }
        inventarioService.revisarStockBajo(productosYCantidades.keySet());
    }

    // Guarda la receta y descuenta el medicamento del inventario
    public void prescribirMedicamento(PrescripcionMedicamento prescripcion, int productoId) throws SQLException {
        buscarPorId(prescripcion.getConsultaId());
        productoService.buscarActivoPorId(productoId);
        if (prescripcion.getCantidad() <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor que cero");
        }

        Connection con = consultaDAO.obtenerConexion();
        try {
            con.setAutoCommit(false);
            insumoDAO.agregarPrescripcionMedicamento(con, prescripcion);
            inventarioService.descontar(con, productoId, prescripcion.getCantidad(),
                    "Medicamento prescrito en la consulta #" + prescripcion.getConsultaId(),
                    prescripcion.getConsultaId(), null);
            con.commit();
        } catch (SQLException | IllegalArgumentException e) {
            con.rollback();
            throw e;
        } finally {
            con.setAutoCommit(true);
            con.close();
        }
        inventarioService.revisarStockBajo(List.of(productoId));
    }

    private int idEstadoConsulta(String nombre) throws SQLException {
        return estadoDAO.listarEstadosConsultas().stream()
                .filter(estado -> estado.getNombre().equals(nombre))
                .map(Estado::getId)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "No existe el estado de consulta '" + nombre + "'. Ejecute de nuevo data.sql"));
    }
}
