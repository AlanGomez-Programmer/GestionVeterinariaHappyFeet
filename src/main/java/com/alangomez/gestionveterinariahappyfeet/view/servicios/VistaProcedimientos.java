package com.alangomez.gestionveterinariahappyfeet.view.servicios;

import com.alangomez.gestionveterinariahappyfeet.model.entities.servicios.PrescripcionProcedimiento;
import com.alangomez.gestionveterinariahappyfeet.model.entities.servicios.ProcedimientoEspecial;
import com.alangomez.gestionveterinariahappyfeet.util.EntradaConsola;
import com.alangomez.gestionveterinariahappyfeet.util.TipoTexto;
import com.alangomez.gestionveterinariahappyfeet.view.VistaBase;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class VistaProcedimientos extends VistaBase {

    private static final String FORMATO_FILA = "%-5s %-17s %-15s %-25s %-15s%n";

    public int menuProcedimientos(){
        System.out.println("""

            ==============================
               Procedimientos especiales
            ==============================
                1. Programar desde una prescripción
                2. Programar sin prescripción
                3. Listar Procedimientos
                4. Ver detalle de un Procedimiento
                5. Cambiar estado de un Procedimiento
                6. Registrar insumos usados
                0. Regresar
            """);
        return EntradaConsola.ingresarEnteroEnRango("Ingrese una opción", 0, 6);
    }

    public int pedirId(String accion){
        return EntradaConsola.ingresarEntero("Ingrese el id del procedimiento a " + accion);
    }

    public String pedirTexto(String mensaje){
        return EntradaConsola.ingresarTexto(TipoTexto.ALFANUMERICO, mensaje);
    }

    public String pedirTextoOpcional(String pregunta, String mensaje){
        if (EntradaConsola.confirmar(pregunta)){
            return EntradaConsola.ingresarTexto(TipoTexto.ALFANUMERICO, mensaje);
        }
        return null;
    }

    public LocalDate pedirProximoControl(){
        if (EntradaConsola.confirmar("¿Desea programar un próximo control?")){
            return EntradaConsola.ingresarFechaNoPasada("Fecha del próximo control");
        }
        return null;
    }

    public void mostrarPrescripciones(List<PrescripcionProcedimiento> prescripciones,
            Map<Integer, String> procedimientos){
        System.out.println("\n---- Prescripciones pendientes ----");
        if (prescripciones.isEmpty()){
            System.out.println("No hay prescripciones pendientes.");
            return;
        }
        prescripciones.forEach(p -> System.out.println(p.getId() + " - "
                + procedimientos.getOrDefault(p.getCatalogoProcedimientoId(), "-")
                + " (consulta #" + p.getConsultaId() + ") "
                + valorOGuion(p.getIndicaciones())));
    }

    public void mostrarProcedimientos(List<ProcedimientoEspecial> procedimientos, Map<Integer, String> mascotas,
            Map<Integer, String> nombresProcedimientos, Map<Integer, String> veterinarios, Map<Integer, String> estados){
        if (procedimientos.isEmpty()){
            System.out.println("No hay procedimientos registrados.");
            return;
        }
        System.out.printf(FORMATO_FILA, "ID", "FECHA Y HORA", "MASCOTA", "PROCEDIMIENTO", "ESTADO");
        procedimientos.forEach(p -> System.out.printf(FORMATO_FILA,
                p.getId(), formatearFechaHora(p.getFechaHora()),
                mascotas.getOrDefault(p.getMascotaId(), "-"),
                nombresProcedimientos.getOrDefault(p.getCatalogoProcedimientoId(), "-"),
                estados.getOrDefault(p.getEstadoId(), "-")));
    }

    public void mostrarDetalle(ProcedimientoEspecial procedimiento, String mascota, String nombreProcedimiento,
            String veterinario, String estado){
        System.out.printf("""

            ==============================
                Procedimiento #%d
            ==============================
            Procedimiento:          %s
            Mascota:                %s
            Veterinario:            %s
            Fecha y hora:           %s
            Estado:                 %s
            Prescripción:           %s
            Info. preoperatoria:    %s
            Detalle:                %s
            Complicaciones:         %s
            Seguimiento postop.:    %s
            Próximo control:        %s
            """,
            procedimiento.getId(), nombreProcedimiento, mascota, veterinario,
            formatearFechaHora(procedimiento.getFechaHora()), estado,
            procedimiento.getPrescripcionId() == null ? "Sin prescripción" : "#" + procedimiento.getPrescripcionId(),
            valorOGuion(procedimiento.getInformacionPreoperatoria()),
            valorOGuion(procedimiento.getDetalleProcedimiento()),
            valorOGuion(procedimiento.getComplicaciones()),
            valorOGuion(procedimiento.getSeguimientoPostoperatorio()),
            formatearFecha(procedimiento.getProximoControl()));
    }
}
