package com.alangomez.gestionveterinariahappyfeet.view.servicios;

import com.alangomez.gestionveterinariahappyfeet.model.entities.servicios.HistorialServicio;
import com.alangomez.gestionveterinariahappyfeet.model.entities.servicios.PrescripcionMedicamento;
import com.alangomez.gestionveterinariahappyfeet.model.entities.servicios.consulta.Consulta;
import com.alangomez.gestionveterinariahappyfeet.model.entities.servicios.consulta.ConsultaBuilderImpl;
import com.alangomez.gestionveterinariahappyfeet.util.EntradaConsola;
import com.alangomez.gestionveterinariahappyfeet.util.TipoTexto;
import com.alangomez.gestionveterinariahappyfeet.view.VistaBase;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class VistaConsultas extends VistaBase {

    private static final String FORMATO_FILA = "%-5s %-17s %-25s %s%n";

    public int menuConsultas(){
        System.out.println("""

            ==============================
                       Consultas
            ==============================
                1. Atender una Cita
                2. Atender sin cita (emergencia)
                3. Ver consultas de una Mascota
                4. Ver detalle de una Consulta
                5. Prescribir Procedimiento especial
                6. Registrar insumos usados en una Consulta
                7. Prescribir Medicamento
                0. Regresar
            """);
        return EntradaConsola.ingresarEnteroEnRango("Ingrese una opción", 0, 7);
    }

    public Consulta pedirDatosConsulta(int mascotaId, int veterinarioId, Integer citaId){
        System.out.println("""
            ==============================
                 Registrar Consulta
            ==============================
            """);
        ConsultaBuilderImpl consulta = new ConsultaBuilderImpl();
        String motivo = EntradaConsola.ingresarTexto(TipoTexto.ALFANUMERICO, "Motivo de la visita");
        String sintomas = pedirTextoOpcional("¿Desea registrar síntomas?", "Síntomas");
        String diagnostico = EntradaConsola.ingresarTexto(TipoTexto.ALFANUMERICO, "Diagnóstico");
        String recomendaciones = pedirTextoOpcional("¿Desea agregar recomendaciones?", "Recomendaciones");

        Double peso = null;
        if (EntradaConsola.confirmar("¿Desea registrar el peso?")){
            peso = EntradaConsola.ingresarDecimalPositivo("Peso en kg");
        }
        Double temperatura = null;
        if (EntradaConsola.confirmar("¿Desea registrar la temperatura?")){
            temperatura = EntradaConsola.ingresarDecimalEnRango("Temperatura en °C", 30, 45);
        }

        return consulta.mascotaId(mascotaId)
               .veterinarioId(veterinarioId)
               .citaId(citaId)
               .fechaHora(LocalDateTime.now())
               .motivo(motivo)
               .sintomas(sintomas)
               .diagnostico(diagnostico)
               .recomendaciones(recomendaciones)
               .pesoRegistrado(peso)
               .temperatura(temperatura)
               .build();
    }

    public PrescripcionMedicamento pedirDatosPrescripcion(int consultaId, int medicamentoId){
        int cantidad = EntradaConsola.ingresarEntero("Cantidad a entregar");
        String dosis = EntradaConsola.ingresarTexto(TipoTexto.ALFANUMERICO, "Dosis (por ejemplo 1 tableta)");
        String frecuencia = EntradaConsola.ingresarTexto(TipoTexto.ALFANUMERICO, "Frecuencia (por ejemplo cada 8 horas)");
        Integer duracionDias = null;
        if (EntradaConsola.confirmar("¿Desea indicar la duración del tratamiento?")){
            duracionDias = EntradaConsola.ingresarEnteroEnRango("Duración en días", 1, 365);
        }
        String instrucciones = pedirTextoOpcional("¿Desea agregar instrucciones?", "Instrucciones");
        return new PrescripcionMedicamento(consultaId, medicamentoId, cantidad, dosis, frecuencia, duracionDias, instrucciones);
    }

    public boolean deseaAgregarOtroServicio(){
        return EntradaConsola.confirmar("¿Desea agregar otro servicio realizado?");
    }

    public String pedirIndicaciones(){
        return pedirTextoOpcional("¿Desea agregar indicaciones?", "Indicaciones");
    }

    public int pedirId(String accion){
        return EntradaConsola.ingresarEntero("Ingrese el id de la consulta a " + accion);
    }

    public void mostrarConsultas(List<Consulta> consultas, Map<Integer, String> veterinarios){
        if (consultas.isEmpty()){
            System.out.println("La mascota no tiene consultas registradas.");
            return;
        }
        System.out.printf(FORMATO_FILA, "ID", "FECHA", "VETERINARIO", "DIAGNÓSTICO");
        consultas.forEach(c -> System.out.printf(FORMATO_FILA,
                c.getId(), formatearFechaHora(c.getFechaHora()),
                veterinarios.getOrDefault(c.getVeterinarioId(), "-"),
                valorOGuion(c.getDiagnostico())));
    }

    public void mostrarDetalleConsulta(Consulta consulta, String mascota, String veterinario,
            List<HistorialServicio> servicios, Map<Integer, String> nombresServicios){
        System.out.printf("""

            ==============================
                Consulta #%d
            ==============================
            Fecha:            %s
            Mascota:          %s
            Veterinario:      %s
            Cita:             %s
            Motivo:           %s
            Síntomas:         %s
            Diagnóstico:      %s
            Recomendaciones:  %s
            Peso:             %s
            Temperatura:      %s
            """,
            consulta.getId(), formatearFechaHora(consulta.getFechaHora()), mascota, veterinario,
            consulta.getCitaId() == null ? "Sin cita" : "#" + consulta.getCitaId(),
            consulta.getMotivo(), valorOGuion(consulta.getSintomas()), valorOGuion(consulta.getDiagnostico()),
            valorOGuion(consulta.getRecomendaciones()),
            consulta.getPesoRegistrado() == null ? "-" : consulta.getPesoRegistrado() + " kg",
            consulta.getTemperatura() == null ? "-" : consulta.getTemperatura() + " °C");
        System.out.println("---- Servicios realizados ----");
        servicios.forEach(s -> System.out.println("- " + nombresServicios.getOrDefault(s.getServicioId(), "-")
                + " | Precio: " + s.getPrecioAplicado()));
    }

    private String pedirTextoOpcional(String pregunta, String mensaje){
        if (EntradaConsola.confirmar(pregunta)){
            return EntradaConsola.ingresarTexto(TipoTexto.ALFANUMERICO, mensaje);
        }
        return null;
    }
}
