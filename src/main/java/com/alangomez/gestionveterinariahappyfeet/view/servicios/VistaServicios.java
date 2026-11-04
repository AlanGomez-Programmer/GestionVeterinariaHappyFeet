package com.alangomez.gestionveterinariahappyfeet.view.servicios;

import com.alangomez.gestionveterinariahappyfeet.util.EntradaConsola;
import com.alangomez.gestionveterinariahappyfeet.view.VistaBase;

public class VistaServicios extends VistaBase {

    public int menuServicios(){
        System.out.println("""

            ==============================
               Servicios Médicos y Citas
            ==============================
                1. Veterinarios
                2. Citas
                3. Consultas
                4. Procedimientos especiales
                0. Regresar
            """);
        return EntradaConsola.ingresarEnteroEnRango("Ingrese una opción", 0, 4);
    }
}
