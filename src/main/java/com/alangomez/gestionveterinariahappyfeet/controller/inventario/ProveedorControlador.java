package com.alangomez.gestionveterinariahappyfeet.controller.inventario;

import com.alangomez.gestionveterinariahappyfeet.controller.ControladorBase;
import com.alangomez.gestionveterinariahappyfeet.service.inventario.CatalogoInventarioService;
import com.alangomez.gestionveterinariahappyfeet.service.inventario.ProveedorService;
import com.alangomez.gestionveterinariahappyfeet.view.inventario.VistaProveedores;
import java.sql.SQLException;

public class ProveedorControlador extends ControladorBase {

    private final VistaProveedores vista;
    private final ProveedorService proveedorService;
    private final CatalogoInventarioService catalogoService;

    public ProveedorControlador(VistaProveedores vista, ProveedorService proveedorService,
            CatalogoInventarioService catalogoService) {
        super(vista);
        this.vista = vista;
        this.proveedorService = proveedorService;
        this.catalogoService = catalogoService;
    }

    public void iniciar() {
        int opcion;
        do {
            opcion = vista.menuProveedores();
            try {
                switch (opcion) {
                    case 1 -> {
                        proveedorService.registrar(vista.pedirDatosProveedor());
                        vista.mostrarMensaje("Proveedor registrado correctamente");
                    }
                    case 2 -> vista.mostrarProveedores(proveedorService.listarActivos());
                    case 3 -> {
                        catalogoService.registrarFabricante(vista.pedirDatosFabricante());
                        vista.mostrarMensaje("Fabricante registrado correctamente");
                    }
                    case 4 -> vista.mostrarFabricantes(catalogoService.listarFabricantes());
                    default -> { }
                }
            } catch (IllegalArgumentException e) {
                vista.mostrarError(e.getMessage());
            } catch (SQLException e) {
                manejarErrorBaseDatos("Menú de proveedores, opción " + opcion, e);
            }
        } while (opcion != 0);
    }
}
