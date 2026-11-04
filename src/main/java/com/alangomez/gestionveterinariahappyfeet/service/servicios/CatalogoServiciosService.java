package com.alangomez.gestionveterinariahappyfeet.service.servicios;

import com.alangomez.gestionveterinariahappyfeet.model.dao.servicios.CatalogoProcedimientoDAO;
import com.alangomez.gestionveterinariahappyfeet.model.dao.servicios.EspecialidadDAO;
import com.alangomez.gestionveterinariahappyfeet.model.dao.servicios.EstadoDAO;
import com.alangomez.gestionveterinariahappyfeet.model.dao.servicios.ServicioDAO;
import com.alangomez.gestionveterinariahappyfeet.model.dao.servicios.TipoProcedimientoEspecialDAO;
import com.alangomez.gestionveterinariahappyfeet.model.entities.servicios.CatalogoProcedimiento;
import com.alangomez.gestionveterinariahappyfeet.model.entities.servicios.Especialidad;
import com.alangomez.gestionveterinariahappyfeet.model.entities.servicios.Estado;
import com.alangomez.gestionveterinariahappyfeet.model.entities.servicios.Servicio;
import com.alangomez.gestionveterinariahappyfeet.model.entities.servicios.TipoProcedimientoEspecial;
import java.sql.SQLException;
import java.util.List;

public class CatalogoServiciosService {

    private final EspecialidadDAO especialidadDAO;
    private final EstadoDAO estadoDAO;
    private final ServicioDAO servicioDAO;
    private final CatalogoProcedimientoDAO catalogoProcedimientoDAO;
    private final TipoProcedimientoEspecialDAO tipoProcedimientoDAO;

    public CatalogoServiciosService(EspecialidadDAO especialidadDAO, EstadoDAO estadoDAO, ServicioDAO servicioDAO,
            CatalogoProcedimientoDAO catalogoProcedimientoDAO, TipoProcedimientoEspecialDAO tipoProcedimientoDAO) {
        this.especialidadDAO = especialidadDAO;
        this.estadoDAO = estadoDAO;
        this.servicioDAO = servicioDAO;
        this.catalogoProcedimientoDAO = catalogoProcedimientoDAO;
        this.tipoProcedimientoDAO = tipoProcedimientoDAO;
    }

    public List<Especialidad> listarEspecialidades() throws SQLException {
        return especialidadDAO.listarEspecialidades();
    }

    public List<Estado> listarEstadosCitas() throws SQLException {
        return estadoDAO.listarEstadosCitas();
    }

    public List<Estado> listarEstadosConsultas() throws SQLException {
        return estadoDAO.listarEstadosConsultas();
    }

    public List<Estado> listarEstadosProcedimientos() throws SQLException {
        return estadoDAO.listarEstadosProcedimientos();
    }

    public List<Servicio> listarServicios() throws SQLException {
        return servicioDAO.listarServicios();
    }

    // Los servicios de una consulta son todos menos los que son procedimientos especiales
    public List<Servicio> listarServiciosParaConsulta() throws SQLException {
        List<Integer> serviciosDeProcedimientos = catalogoProcedimientoDAO.listarCatalogoProcedimientos().stream()
                .map(CatalogoProcedimiento::getServicioId)
                .toList();
        return servicioDAO.listarServicios().stream()
                .filter(servicio -> !serviciosDeProcedimientos.contains(servicio.getId()))
                .toList();
    }

    public List<CatalogoProcedimiento> listarCatalogoProcedimientos() throws SQLException {
        return catalogoProcedimientoDAO.listarCatalogoProcedimientos();
    }

    public List<TipoProcedimientoEspecial> listarTiposProcedimientos() throws SQLException {
        return tipoProcedimientoDAO.listarTiposProcedimientos();
    }
}
