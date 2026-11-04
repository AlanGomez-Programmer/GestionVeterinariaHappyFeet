package com.alangomez.gestionveterinariahappyfeet.view.inventario;

import com.alangomez.gestionveterinariahappyfeet.util.EntradaConsola;
import com.alangomez.gestionveterinariahappyfeet.view.VistaBase;

public class VistaInventario extends VistaBase {

    public int menuInventario(){
        System.out.println("""

            ==============================
                Inventario y Farmacia
            ==============================
                1. Productos
                2. Proveedores y Fabricantes
                3. Stock y Alertas
                0. Regresar
            """);
        return EntradaConsola.ingresarEnteroEnRango("Ingrese una opción", 0, 3);
    }
}
