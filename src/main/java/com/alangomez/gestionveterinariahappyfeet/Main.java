package com.alangomez.gestionveterinariahappyfeet;

import com.alangomez.gestionveterinariahappyfeet.controller.PrincipalControlador;
import com.alangomez.gestionveterinariahappyfeet.controller.inventario.ExistenciaControlador;
import com.alangomez.gestionveterinariahappyfeet.controller.inventario.InventarioControlador;
import com.alangomez.gestionveterinariahappyfeet.controller.inventario.ProductoControlador;
import com.alangomez.gestionveterinariahappyfeet.controller.inventario.ProveedorControlador;
import com.alangomez.gestionveterinariahappyfeet.model.dao.inventario.DetalleProductoDAO;
import com.alangomez.gestionveterinariahappyfeet.model.dao.inventario.ExistenciaDAO;
import com.alangomez.gestionveterinariahappyfeet.model.dao.inventario.FabricanteDAO;
import com.alangomez.gestionveterinariahappyfeet.model.dao.inventario.MovimientoInventarioDAO;
import com.alangomez.gestionveterinariahappyfeet.model.dao.inventario.ProductoDAO;
import com.alangomez.gestionveterinariahappyfeet.model.dao.inventario.ProveedorDAO;
import com.alangomez.gestionveterinariahappyfeet.model.dao.inventario.TipoMovimientoDAO;
import com.alangomez.gestionveterinariahappyfeet.model.dao.inventario.TipoProductoDAO;
import com.alangomez.gestionveterinariahappyfeet.model.dao.servicios.InsumoDAO;
import com.alangomez.gestionveterinariahappyfeet.service.inventario.CatalogoInventarioService;
import com.alangomez.gestionveterinariahappyfeet.service.inventario.InventarioService;
import com.alangomez.gestionveterinariahappyfeet.service.inventario.ProductoService;
import com.alangomez.gestionveterinariahappyfeet.service.inventario.ProveedorService;
import com.alangomez.gestionveterinariahappyfeet.view.inventario.VistaExistencias;
import com.alangomez.gestionveterinariahappyfeet.view.inventario.VistaInventario;
import com.alangomez.gestionveterinariahappyfeet.view.inventario.VistaProductos;
import com.alangomez.gestionveterinariahappyfeet.view.inventario.VistaProveedores;
import com.alangomez.gestionveterinariahappyfeet.controller.pacientes.DuenioControlador;
import com.alangomez.gestionveterinariahappyfeet.controller.pacientes.MascotaControlador;
import com.alangomez.gestionveterinariahappyfeet.controller.pacientes.PacientesControlador;
import com.alangomez.gestionveterinariahappyfeet.controller.servicios.CitaControlador;
import com.alangomez.gestionveterinariahappyfeet.controller.servicios.ConsultaControlador;
import com.alangomez.gestionveterinariahappyfeet.controller.servicios.ProcedimientoControlador;
import com.alangomez.gestionveterinariahappyfeet.controller.servicios.ServiciosControlador;
import com.alangomez.gestionveterinariahappyfeet.controller.servicios.VeterinarioControlador;
import com.alangomez.gestionveterinariahappyfeet.model.dao.inventario.TipoVacunaDAO;
import com.alangomez.gestionveterinariahappyfeet.model.dao.pacientes.AlergiaDAO;
import com.alangomez.gestionveterinariahappyfeet.model.dao.pacientes.CatalogoAlergiaDAO;
import com.alangomez.gestionveterinariahappyfeet.model.dao.pacientes.DuenioDAO;
import com.alangomez.gestionveterinariahappyfeet.model.dao.pacientes.EspecieDAO;
import com.alangomez.gestionveterinariahappyfeet.model.dao.pacientes.HistorialDuenioDAO;
import com.alangomez.gestionveterinariahappyfeet.model.dao.pacientes.MascotaDAO;
import com.alangomez.gestionveterinariahappyfeet.model.dao.pacientes.ProcedimientoQuirurgicoPrevioDAO;
import com.alangomez.gestionveterinariahappyfeet.model.dao.pacientes.RazaDAO;
import com.alangomez.gestionveterinariahappyfeet.model.dao.pacientes.SexoDAO;
import com.alangomez.gestionveterinariahappyfeet.model.dao.pacientes.TipoAlergiaDAO;
import com.alangomez.gestionveterinariahappyfeet.model.dao.pacientes.VacunaPreviaDAO;
import com.alangomez.gestionveterinariahappyfeet.model.dao.servicios.CatalogoProcedimientoDAO;
import com.alangomez.gestionveterinariahappyfeet.model.dao.servicios.CitaDAO;
import com.alangomez.gestionveterinariahappyfeet.model.dao.servicios.ConsultaDAO;
import com.alangomez.gestionveterinariahappyfeet.model.dao.servicios.EspecialidadDAO;
import com.alangomez.gestionveterinariahappyfeet.model.dao.servicios.EstadoDAO;
import com.alangomez.gestionveterinariahappyfeet.model.dao.servicios.HistorialServicioDAO;
import com.alangomez.gestionveterinariahappyfeet.model.dao.servicios.PrescripcionProcedimientoDAO;
import com.alangomez.gestionveterinariahappyfeet.model.dao.servicios.ProcedimientoEspecialDAO;
import com.alangomez.gestionveterinariahappyfeet.model.dao.servicios.ServicioDAO;
import com.alangomez.gestionveterinariahappyfeet.model.dao.servicios.TipoProcedimientoEspecialDAO;
import com.alangomez.gestionveterinariahappyfeet.model.dao.servicios.VeterinarioDAO;
import com.alangomez.gestionveterinariahappyfeet.service.pacientes.CatalogoPacientesService;
import com.alangomez.gestionveterinariahappyfeet.service.pacientes.DuenioService;
import com.alangomez.gestionveterinariahappyfeet.service.pacientes.MascotaService;
import com.alangomez.gestionveterinariahappyfeet.service.servicios.CatalogoServiciosService;
import com.alangomez.gestionveterinariahappyfeet.service.servicios.CitaService;
import com.alangomez.gestionveterinariahappyfeet.service.servicios.ConsultaService;
import com.alangomez.gestionveterinariahappyfeet.service.servicios.ProcedimientoService;
import com.alangomez.gestionveterinariahappyfeet.service.servicios.VeterinarioService;
import com.alangomez.gestionveterinariahappyfeet.view.VistaPrincipal;
import com.alangomez.gestionveterinariahappyfeet.view.pacientes.VistaDuenios;
import com.alangomez.gestionveterinariahappyfeet.view.pacientes.VistaMascota;
import com.alangomez.gestionveterinariahappyfeet.view.pacientes.VistaPacientes;
import com.alangomez.gestionveterinariahappyfeet.view.servicios.VistaCitas;
import com.alangomez.gestionveterinariahappyfeet.view.servicios.VistaConsultas;
import com.alangomez.gestionveterinariahappyfeet.view.servicios.VistaProcedimientos;
import com.alangomez.gestionveterinariahappyfeet.view.servicios.VistaServicios;
import com.alangomez.gestionveterinariahappyfeet.view.servicios.VistaVeterinarios;

public class Main {

    public static void main(String[] args) {
        // ---------- DAO ----------
        DuenioDAO duenioDAO = new DuenioDAO();
        MascotaDAO mascotaDAO = new MascotaDAO();
        VeterinarioDAO veterinarioDAO = new VeterinarioDAO();
        CitaDAO citaDAO = new CitaDAO();
        EstadoDAO estadoDAO = new EstadoDAO();
        ConsultaDAO consultaDAO = new ConsultaDAO();
        ServicioDAO servicioDAO = new ServicioDAO();
        HistorialServicioDAO historialServicioDAO = new HistorialServicioDAO();
        PrescripcionProcedimientoDAO prescripcionDAO = new PrescripcionProcedimientoDAO();
        CatalogoProcedimientoDAO catalogoProcedimientoDAO = new CatalogoProcedimientoDAO();
        TipoProcedimientoEspecialDAO tipoProcedimientoDAO = new TipoProcedimientoEspecialDAO();

        // ---------- Módulo 1: Pacientes ----------
        DuenioService duenioService = new DuenioService(duenioDAO, mascotaDAO);
        MascotaService mascotaService = new MascotaService(
                mascotaDAO,
                duenioDAO,
                new AlergiaDAO(),
                new HistorialDuenioDAO(),
                new VacunaPreviaDAO(),
                new ProcedimientoQuirurgicoPrevioDAO()
        );
        CatalogoPacientesService catalogoPacientesService = new CatalogoPacientesService(
                new EspecieDAO(),
                new RazaDAO(),
                new SexoDAO(),
                new TipoAlergiaDAO(),
                new CatalogoAlergiaDAO(),
                new TipoVacunaDAO(),
                tipoProcedimientoDAO
        );

        DuenioControlador duenioControlador = new DuenioControlador(new VistaDuenios(), duenioService);
        MascotaControlador mascotaControlador = new MascotaControlador(
                new VistaMascota(), mascotaService, duenioService, catalogoPacientesService);
        PacientesControlador pacientesControlador = new PacientesControlador(
                new VistaPacientes(), duenioControlador, mascotaControlador);

        // ---------- Módulo 3: Inventario y farmacia ----------
        ProductoDAO productoDAO = new ProductoDAO();
        TipoProductoDAO tipoProductoDAO = new TipoProductoDAO();
        ProductoService productoService = new ProductoService(productoDAO, new DetalleProductoDAO(), tipoProductoDAO);
        InventarioService inventarioService = new InventarioService(new ExistenciaDAO(), productoDAO,
                new MovimientoInventarioDAO(), new TipoMovimientoDAO(), productoService);
        ProveedorService proveedorService = new ProveedorService(new ProveedorDAO());
        CatalogoInventarioService catalogoInventarioService = new CatalogoInventarioService(
                tipoProductoDAO, new TipoVacunaDAO(), new FabricanteDAO());

        VistaExistencias vistaExistencias = new VistaExistencias();
        inventarioService.agregarObservador(vistaExistencias);

        InventarioControlador inventarioControlador = new InventarioControlador(
                new VistaInventario(),
                new ProductoControlador(new VistaProductos(), productoService, inventarioService, proveedorService,
                        catalogoInventarioService, catalogoPacientesService),
                new ProveedorControlador(new VistaProveedores(), proveedorService, catalogoInventarioService),
                new ExistenciaControlador(vistaExistencias, inventarioService, productoService)
        );

        // ---------- Módulo 2: Servicios médicos y citas ----------
        InsumoDAO insumoDAO = new InsumoDAO();
        CatalogoServiciosService catalogoServiciosService = new CatalogoServiciosService(
                new EspecialidadDAO(), estadoDAO, servicioDAO, catalogoProcedimientoDAO, tipoProcedimientoDAO);
        VeterinarioService veterinarioService = new VeterinarioService(veterinarioDAO, citaDAO, estadoDAO);
        CitaService citaService = new CitaService(citaDAO, estadoDAO, mascotaDAO, veterinarioDAO);
        ConsultaService consultaService = new ConsultaService(consultaDAO, citaDAO, estadoDAO, servicioDAO,
                historialServicioDAO, prescripcionDAO, mascotaDAO, veterinarioDAO, citaService,
                insumoDAO, inventarioService, productoService);
        ProcedimientoService procedimientoService = new ProcedimientoService(new ProcedimientoEspecialDAO(),
                prescripcionDAO, consultaDAO, catalogoProcedimientoDAO, servicioDAO, estadoDAO,
                historialServicioDAO, mascotaDAO, veterinarioDAO, insumoDAO, inventarioService, productoService);

        ServiciosControlador serviciosControlador = new ServiciosControlador(
                new VistaServicios(),
                new VeterinarioControlador(new VistaVeterinarios(), veterinarioService, catalogoServiciosService),
                new CitaControlador(new VistaCitas(), citaService, mascotaService, veterinarioService,
                        catalogoServiciosService),
                new ConsultaControlador(new VistaConsultas(), consultaService, citaService, mascotaService,
                        veterinarioService, catalogoServiciosService, productoService, inventarioService),
                new ProcedimientoControlador(new VistaProcedimientos(), procedimientoService, mascotaService,
                        veterinarioService, catalogoServiciosService, productoService, inventarioService)
        );

        new PrincipalControlador(new VistaPrincipal(), pacientesControlador, serviciosControlador,
                inventarioControlador).iniciar();
    }
}
