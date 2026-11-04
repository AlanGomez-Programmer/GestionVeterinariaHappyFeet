package com.alangomez.gestionveterinariahappyfeet.view.pacientes;

import com.alangomez.gestionveterinariahappyfeet.util.EntradaConsola;
import com.alangomez.gestionveterinariahappyfeet.view.VistaBase;

public class VistaPacientes extends VistaBase {

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