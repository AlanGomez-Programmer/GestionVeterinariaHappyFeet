package com.alangomez.gestionveterinariahappyfeet.view.inventario;

import com.alangomez.gestionveterinariahappyfeet.model.entities.inventario.Fabricante;
import com.alangomez.gestionveterinariahappyfeet.model.entities.inventario.Proveedor;
import com.alangomez.gestionveterinariahappyfeet.util.EntradaConsola;
import com.alangomez.gestionveterinariahappyfeet.util.TipoTexto;
import com.alangomez.gestionveterinariahappyfeet.view.VistaBase;
import java.util.List;

public class VistaProveedores extends VistaBase {

    private static final String FORMATO_PROVEEDOR = "%-5s %-28s %-22s %-15s %-25s%n";

    public int menuProveedores(){
        System.out.println("""

            ==============================
                Proveedores y Fabricantes
            ==============================
                1. Registrar Proveedor
                2. Listar Proveedores
                3. Registrar Fabricante
                4. Listar Fabricantes
                0. Regresar
            """);
        return EntradaConsola.ingresarEnteroEnRango("Ingrese una opción", 0, 4);
    }

    public Proveedor pedirDatosProveedor(){
        String nombre = EntradaConsola.ingresarTexto(TipoTexto.ALFANUMERICO, "Nombre de la empresa");
        String contacto = pedirOpcional("¿Desea agregar una persona de contacto?", TipoTexto.SOLO_LETRAS, "Contacto");
        String telefono = null;
        if (EntradaConsola.confirmar("¿Desea agregar un teléfono?")){
            telefono = EntradaConsola.ingresarCadenaNumeros("Teléfono");
        }
        String email = pedirOpcional("¿Desea agregar un correo?", TipoTexto.CORREO, "Correo");
        String direccion = pedirOpcional("¿Desea agregar una dirección?", TipoTexto.ALFANUMERICO, "Dirección");
        return new Proveedor(nombre, contacto, telefono, email, direccion);
    }

    public Fabricante pedirDatosFabricante(){
        String nombre = EntradaConsola.ingresarTexto(TipoTexto.ALFANUMERICO, "Nombre del fabricante");
        String pais = pedirOpcional("¿Desea agregar el país?", TipoTexto.SOLO_LETRAS, "País");
        return new Fabricante(nombre, pais);
    }

    public void mostrarProveedores(List<Proveedor> proveedores){
        if (proveedores.isEmpty()){
            System.out.println("No hay proveedores registrados.");
            return;
        }
        System.out.printf(FORMATO_PROVEEDOR, "ID", "EMPRESA", "CONTACTO", "TELÉFONO", "CORREO");
        proveedores.forEach(p -> System.out.printf(FORMATO_PROVEEDOR, p.getId(), p.getNombreEmpresa(),
                valorOGuion(p.getContacto()), valorOGuion(p.getTelefono()), valorOGuion(p.getEmail())));
    }

    public void mostrarFabricantes(List<Fabricante> fabricantes){
        if (fabricantes.isEmpty()){
            System.out.println("No hay fabricantes registrados.");
            return;
        }
        fabricantes.forEach(f -> System.out.println(f.getId() + " - " + f.getNombre() + " (" + valorOGuion(f.getPais()) + ")"));
    }

    private String pedirOpcional(String pregunta, TipoTexto tipo, String mensaje){
        if (EntradaConsola.confirmar(pregunta)){
            return EntradaConsola.ingresarTexto(tipo, mensaje);
        }
        return null;
    }
}
