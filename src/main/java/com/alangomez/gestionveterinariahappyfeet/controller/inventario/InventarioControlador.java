package com.alangomez.gestionveterinariahappyfeet.controller.inventario;

import com.alangomez.gestionveterinariahappyfeet.view.inventario.VistaInventario;

public class InventarioControlador {

    private final VistaInventario vista;
    private final ProductoControlador productoControlador;
    private final ProveedorControlador proveedorControlador;
    private final ExistenciaControlador existenciaControlador;

    public InventarioControlador(VistaInventario vista, ProductoControlador productoControlador,
            ProveedorControlador proveedorControlador, ExistenciaControlador existenciaControlador) {
        this.vista = vista;
        this.productoControlador = productoControlador;
        this.proveedorControlador = proveedorControlador;
        this.existenciaControlador = existenciaControlador;
    }

    public void iniciar() {
        int opcion;
        do {
            opcion = vista.menuInventario();
            switch (opcion) {
                case 1 -> productoControlador.iniciar();
                case 2 -> proveedorControlador.iniciar();
                case 3 -> existenciaControlador.iniciar();
                default -> { }
            }
        } while (opcion != 0);
    }
}
