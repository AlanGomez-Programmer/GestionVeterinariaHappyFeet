package com.alangomez.gestionveterinariahappyfeet.view;

import com.alangomez.gestionveterinariahappyfeet.util.EntradaConsola;

public class VistaPacientes {

    public int menuPacientes(){
        System.out.println("""

            ==============================
                Gestión de Pacientes
            ==============================
                1. Dueños
                2. Mascotas
                0. Regresar
            """);
        return EntradaConsola.ingresarEnteroEnRango("Ingrese una opción", 0, 2);
    }
}