package com.alangomez.gestionveterinariahappyfeet.controller.inventario;

import com.alangomez.gestionveterinariahappyfeet.controller.ControladorBase;
import com.alangomez.gestionveterinariahappyfeet.model.entities.inventario.Fabricante;
import com.alangomez.gestionveterinariahappyfeet.model.entities.inventario.Producto;
import com.alangomez.gestionveterinariahappyfeet.model.entities.inventario.Proveedor;
import com.alangomez.gestionveterinariahappyfeet.model.entities.inventario.TipoProducto;
import com.alangomez.gestionveterinariahappyfeet.model.entities.inventario.TipoVacuna;
import com.alangomez.gestionveterinariahappyfeet.model.entities.pacientes.Especie;
import com.alangomez.gestionveterinariahappyfeet.service.inventario.CatalogoInventarioService;
import com.alangomez.gestionveterinariahappyfeet.service.inventario.InventarioService;
import com.alangomez.gestionveterinariahappyfeet.service.inventario.ProductoService;
import com.alangomez.gestionveterinariahappyfeet.service.inventario.ProveedorService;
import com.alangomez.gestionveterinariahappyfeet.service.pacientes.CatalogoPacientesService;
import com.alangomez.gestionveterinariahappyfeet.view.inventario.VistaProductos;
import java.sql.SQLException;
import java.util.List;

public class ProductoControlador extends ControladorBase {

    private final VistaProductos vista;
    private final ProductoService productoService;
    private final InventarioService inventarioService;
    private final ProveedorService proveedorService;
    private final CatalogoInventarioService catalogoService;
    private final CatalogoPacientesService catalogoPacientesService;

    public ProductoControlador(VistaProductos vista, ProductoService productoService, InventarioService inventarioService,
            ProveedorService proveedorService, CatalogoInventarioService catalogoService,
            CatalogoPacientesService catalogoPacientesService) {
        super(vista);
        this.vista = vista;
        this.productoService = productoService;
        this.inventarioService = inventarioService;
        this.proveedorService = proveedorService;
        this.catalogoService = catalogoService;
        this.catalogoPacientesService = catalogoPacientesService;
    }

    public void iniciar() {
        int opcion;
        do {
            opcion = vista.menuProductos();
            try {
                switch (opcion) {
                    case 1 -> registrarMedicamento();
                    case 2 -> registrarVacuna();
                    case 3 -> registrarMaterial();
                    case 4 -> listar();
                    case 5 -> desactivar();
                    default -> { }
                }
            } catch (IllegalArgumentException e) {
                vista.mostrarError(e.getMessage());
            } catch (SQLException e) {
                manejarErrorBaseDatos("Menú de productos, opción " + opcion, e);
            }
        } while (opcion != 0);
    }

    private void registrarMedicamento() throws SQLException {
        Producto producto = pedirProducto();
        productoService.registrarMedicamento(producto, vista.pedirDatosMedicamento());
        vista.mostrarMensaje("Medicamento registrado con el id " + producto.getId());
    }

    private void registrarVacuna() throws SQLException {
        int tipoVacunaId = elegir("Tipos de vacuna",
                aMapa(catalogoService.listarTiposVacunas(), TipoVacuna::getId, TipoVacuna::getNombre));
        Integer especieId = null;
        if (vista.deseaElegirEspecie()) {
            especieId = elegir("Especies",
                    aMapa(catalogoPacientesService.listarEspecies(), Especie::getId, Especie::getNombre));
        }
        Producto producto = pedirProducto();
        productoService.registrarVacuna(producto, vista.pedirDatosVacuna(tipoVacunaId, especieId));
        vista.mostrarMensaje("Vacuna registrada con el id " + producto.getId());
    }

    private void registrarMaterial() throws SQLException {
        Producto producto = pedirProducto();
        productoService.registrarMaterialMedico(producto, vista.pedirDatosMaterial());
        vista.mostrarMensaje("Material médico registrado con el id " + producto.getId());
    }

    private Producto pedirProducto() throws SQLException {
        Integer fabricanteId = null;
        List<Fabricante> fabricantes = catalogoService.listarFabricantes();
        if (!fabricantes.isEmpty() && vista.deseaElegirFabricante()) {
            fabricanteId = elegir("Fabricantes", aMapa(fabricantes, Fabricante::getId, Fabricante::getNombre));
        }
        Integer proveedorId = null;
        List<Proveedor> proveedores = proveedorService.listarActivos();
        if (!proveedores.isEmpty() && vista.deseaElegirProveedor()) {
            proveedorId = elegir("Proveedores", aMapa(proveedores, Proveedor::getId, Proveedor::getNombreEmpresa));
        }
        return vista.pedirDatosProducto(fabricanteId, proveedorId);
    }

    private void listar() throws SQLException {
        vista.mostrarProductos(productoService.listarActivos(),
                aMapa(catalogoService.listarTiposProductos(), TipoProducto::getId, TipoProducto::getNombre),
                inventarioService.stockDisponiblePorProducto());
    }

    private void desactivar() throws SQLException {
        int id = vista.pedirId("eliminar");
        Producto producto = productoService.buscarActivoPorId(id);
        if (vista.confirmar("¿Seguro que desea eliminar " + producto.getNombre() + "?")) {
            productoService.desactivar(id);
            vista.mostrarMensaje("Producto eliminado correctamente");
        }
    }
}
