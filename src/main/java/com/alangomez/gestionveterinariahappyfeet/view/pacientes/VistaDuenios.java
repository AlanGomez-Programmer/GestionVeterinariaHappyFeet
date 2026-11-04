package com.alangomez.gestionveterinariahappyfeet.view.pacientes;

import com.alangomez.gestionveterinariahappyfeet.model.entities.pacientes.Duenio;
import com.alangomez.gestionveterinariahappyfeet.util.EntradaConsola;
import com.alangomez.gestionveterinariahappyfeet.view.VistaBase;
import com.alangomez.gestionveterinariahappyfeet.util.TipoTexto;
import java.util.List;

public class VistaDuenios extends VistaBase {

    private static final String FORMATO_FILA = "    %-5s %-30s %-15s %-30s %-15s%n";

    public int menuDuenios() {
        System.out.println("""

            ==============================
                        Dueños
            ==============================
                1. Ingresar Nuevo Dueño
                2. Listar Dueños
                3. Buscar Dueño por id
                4. Eliminar Dueño
                0. Regresar
            """);
        return EntradaConsola.ingresarEnteroEnRango("Ingrese una opción", 0, 4);
    }

    public Duenio pedirDatoNuevoDuenio() {
        System.out.println("""
            ==============================
                Registrar Dueño
            ==============================
            """);
        String nombre = EntradaConsola.ingresarTexto(TipoTexto.ALFANUMERICO, "Nombre completo");
        String documentoIdentidad = EntradaConsola.ingresarCadenaNumeros("Documento Personal");
        String direccion = null;
        if (EntradaConsola.confirmar("Desea agregar una dirección")) {
            direccion = EntradaConsola.ingresarTexto(TipoTexto.ALFANUMERICO, "Dirección");
        }
        String telefono = EntradaConsola.ingresarCadenaNumeros("Teléfono");
        String email = EntradaConsola.ingresarTexto(TipoTexto.CORREO, "Correo");
        String contactoEmergencia = null;
        if (EntradaConsola.confirmar("Desea agregar un número de emergencia")) {
            contactoEmergencia = EntradaConsola.ingresarCadenaNumeros("Número Emergencia");
        }
        return new Duenio(nombre, documentoIdentidad, direccion, telefono, email, contactoEmergencia);
    }

    public int pedirId(String accion) {
        return EntradaConsola.ingresarEntero("Ingrese el id del dueño a " + accion);
    }

    public boolean confirmarDesactivacion(Duenio duenio) {
        return EntradaConsola.confirmar("¿Seguro que desea eliminar a " + duenio.getNombreCompleto() + "?");
    }

    public void mostrarDuenios(List<Duenio> duenios) {
        if (duenios.isEmpty()) {
            System.out.println("No hay dueños registrados.");
            return;
        }
        System.out.printf(FORMATO_FILA, "ID", "NOMBRE", "DOCUMENTO", "CORREO", "TELÉFONO");
        duenios.forEach(d -> System.out.printf(FORMATO_FILA,
                d.getId(), d.getNombreCompleto(), d.getDocumentoIdentidad(),
                d.getEmail(), d.getTelefono() == null ? "-" : d.getTelefono()));
    }

    public void mostrarDuenio(Duenio duenio) {
        mostrarDuenios(List.of(duenio));
    }


}