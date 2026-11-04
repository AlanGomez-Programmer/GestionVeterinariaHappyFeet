package com.alangomez.gestionveterinariahappyfeet.view.servicios;

import com.alangomez.gestionveterinariahappyfeet.model.entities.servicios.Veterinario;
import com.alangomez.gestionveterinariahappyfeet.util.EntradaConsola;
import com.alangomez.gestionveterinariahappyfeet.util.TipoTexto;
import com.alangomez.gestionveterinariahappyfeet.view.VistaBase;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class VistaVeterinarios extends VistaBase {

    private static final String FORMATO_FILA = "%-5s %-28s %-20s %-15s %-15s%n";

    public int menuVeterinarios(){
        System.out.println("""

            ==============================
                     Veterinarios
            ==============================
                1. Registrar Veterinario
                2. Listar Veterinarios
                3. Buscar Veterinario por id
                4. Eliminar Veterinario
                0. Regresar
            """);
        return EntradaConsola.ingresarEnteroEnRango("Ingrese una opción", 0, 4);
    }

    public Veterinario pedirDatosVeterinario(int especialidadId){
        System.out.println("""
            ==============================
                Registrar Veterinario
            ==============================
            """);
        String nombre = EntradaConsola.ingresarTexto(TipoTexto.SOLO_LETRAS, "Nombre completo");
        String documento = EntradaConsola.ingresarCadenaNumeros("Documento Personal");
        String licencia = EntradaConsola.ingresarTexto(TipoTexto.ALFANUMERICO, "Licencia profesional");
        String telefono = null;
        if (EntradaConsola.confirmar("¿Desea agregar un teléfono?")){
            telefono = EntradaConsola.ingresarCadenaNumeros("Teléfono");
        }
        String email = null;
        if (EntradaConsola.confirmar("¿Desea agregar un correo?")){
            email = EntradaConsola.ingresarTexto(TipoTexto.CORREO, "Correo");
        }
        LocalDate fechaContratacion = null;
        if (EntradaConsola.confirmar("¿Desea agregar la fecha de contratación?")){
            fechaContratacion = EntradaConsola.ingresarFechaNoFutura("Fecha de contratación");
        }
        return new Veterinario(especialidadId, nombre, documento, licencia, telefono, email, fechaContratacion);
    }

    public int pedirId(String accion){
        return EntradaConsola.ingresarEntero("Ingrese el id del veterinario a " + accion);
    }

    public boolean confirmarDesactivacion(Veterinario veterinario){
        return EntradaConsola.confirmar("¿Seguro que desea eliminar a " + veterinario.getNombreCompleto() + "?");
    }

    public void mostrarVeterinarios(List<Veterinario> veterinarios, Map<Integer, String> especialidades){
        if (veterinarios.isEmpty()){
            System.out.println("No hay veterinarios registrados.");
            return;
        }
        System.out.printf(FORMATO_FILA, "ID", "NOMBRE", "ESPECIALIDAD", "LICENCIA", "TELÉFONO");
        veterinarios.forEach(v -> System.out.printf(FORMATO_FILA,
                v.getId(), v.getNombreCompleto(),
                especialidades.getOrDefault(v.getEspecialidadId(), "-"),
                v.getLicenciaProfesional(), valorOGuion(v.getTelefono())));
    }
}
