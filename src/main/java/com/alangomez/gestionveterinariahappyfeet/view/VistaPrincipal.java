package com.alangomez.gestionveterinariahappyfeet.view;

import com.alangomez.gestionveterinariahappyfeet.util.EntradaConsola;

public class VistaPrincipal extends VistaBase {

    public int menuPrincipal(){
        System.out.println("""

            ==============================
                 Veterinaria Happy Feet
            ==============================
                1. Gestión de Pacientes
                2. Servicios Médicos y Citas
                3. Inventario y Farmacia
                0. Salir
            """);
        return EntradaConsola.ingresarEnteroEnRango("Ingrese una opción", 0, 3);
    }
}
