package com.alangomez.gestionveterinariahappyfeet.view.servicios;

import com.alangomez.gestionveterinariahappyfeet.model.entities.servicios.Cita;
import com.alangomez.gestionveterinariahappyfeet.util.EntradaConsola;
import com.alangomez.gestionveterinariahappyfeet.util.TipoTexto;
import com.alangomez.gestionveterinariahappyfeet.view.VistaBase;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class VistaCitas extends VistaBase {

    private static final String FORMATO_FILA = "%-5s %-17s %-15s %-25s %-12s %s%n";

    public int menuCitas(){
        System.out.println("""

            ==============================
                        Citas
            ==============================
                1. Programar Cita
                2. Ver citas de un día
                3. Ver citas pendientes
                4. Cambiar estado de una Cita
                0. Regresar
            """);
        return EntradaConsola.ingresarEnteroEnRango("Ingrese una opción", 0, 4);
    }

    public String pedirMotivo(){
        return EntradaConsola.ingresarTexto(TipoTexto.ALFANUMERICO, "Motivo de la cita");
    }

    public LocalDate pedirFecha(){
        return EntradaConsola.ingresarFecha("Fecha a consultar");
    }

    public int pedirId(String accion){
        return EntradaConsola.ingresarEntero("Ingrese el id de la cita a " + accion);
    }

    public void mostrarCitas(List<Cita> citas, Map<Integer, String> mascotas,
            Map<Integer, String> veterinarios, Map<Integer, String> estados){
        if (citas.isEmpty()){
            System.out.println("No hay citas para mostrar.");
            return;
        }
        System.out.printf(FORMATO_FILA, "ID", "FECHA Y HORA", "MASCOTA", "VETERINARIO", "ESTADO", "MOTIVO");
        citas.forEach(c -> System.out.printf(FORMATO_FILA,
                c.getId(), formatearFechaHora(c.getFechaHora()),
                mascotas.getOrDefault(c.getMascotaId(), "-"),
                veterinarios.getOrDefault(c.getVeterinarioId(), "-"),
                estados.getOrDefault(c.getEstadoId(), "-"),
                valorOGuion(c.getMotivo())));
    }
}
