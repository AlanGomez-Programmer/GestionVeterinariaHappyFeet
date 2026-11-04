package com.alangomez.gestionveterinariahappyfeet.view;

import com.alangomez.gestionveterinariahappyfeet.model.entities.pacientes.Alergia;
import com.alangomez.gestionveterinariahappyfeet.model.entities.pacientes.HistorialDuenio;
import com.alangomez.gestionveterinariahappyfeet.model.entities.pacientes.ProcedimientoQuirurgicoPrevio;
import com.alangomez.gestionveterinariahappyfeet.model.entities.pacientes.VacunaPrevia;
import com.alangomez.gestionveterinariahappyfeet.model.entities.pacientes.mascota.Mascota;
import com.alangomez.gestionveterinariahappyfeet.model.entities.pacientes.mascota.MascotaBuilderImpl;
import com.alangomez.gestionveterinariahappyfeet.util.EntradaConsola;
import com.alangomez.gestionveterinariahappyfeet.util.TipoTexto;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

public class VistaMascota {

    private static final String FORMATO_FILA = "%-5s %-20s %-25s %-12s %-25s%n";
    private static final String FORMATO_OPCION = "%-5s %s%n";
    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public int menuMascotas(){
        System.out.println("""

            ==============================
                       Mascotas
            ==============================
                1. Registrar Mascota
                2. Listar Mascotas
                3. Ver ficha de Mascota
                4. Transferir Mascota a otro dueño
                5. Agregar Alergia
                6. Registrar Vacuna previa
                7. Registrar Procedimiento quirúrgico previo
                8. Actualizar Peso
                9. Eliminar Mascota
                0. Regresar
            """);
        return EntradaConsola.ingresarEnteroEnRango("Ingrese una opción", 0, 9);
    }

    public void mostrarOpciones(String titulo, Map<Integer, String> opciones){
        System.out.println("\n---- " + titulo + " ----");
        System.out.printf(FORMATO_OPCION, "ID", "NOMBRE");
        opciones.forEach((id, nombre) -> System.out.printf(FORMATO_OPCION, id, nombre));
    }

    public int pedirIdOpcion(String titulo){
        return EntradaConsola.ingresarEntero("Seleccione el ID de " + titulo);
    }

    public int pedirId(String accion){
        return EntradaConsola.ingresarEntero("Ingrese el id de la mascota a " + accion);
    }

    public Mascota pedirDatosMascota(int duenioId, int razaId, int sexoId){
        System.out.println("""
            ==============================
                Registrar Mascota
            ==============================
            """);
        MascotaBuilderImpl mascota = new MascotaBuilderImpl();
        String nombre = EntradaConsola.ingresarTexto(TipoTexto.SOLO_LETRAS, "Nombre");

        LocalDate fechaNacimiento = null;
        Integer edadEstimada = null;
        if (EntradaConsola.confirmar("¿Conoce la fecha de nacimiento?")){
            fechaNacimiento = EntradaConsola.ingresarFechaNoFutura("Fecha de nacimiento");
        } else {
            edadEstimada = EntradaConsola.ingresarEnteroEnRango("Edad estimada en años", 0, 50);
        }

        Double peso = null;
        if (EntradaConsola.confirmar("¿Desea registrar el peso?")){
            peso = EntradaConsola.ingresarDecimalPositivo("Peso en kg");
        }
        String microchip = null;
        if (EntradaConsola.confirmar("¿Tiene microchip?")){
            microchip = EntradaConsola.ingresarTexto(TipoTexto.ALFANUMERICO, "Número de microchip");
        }
        String tatuaje = null;
        if (EntradaConsola.confirmar("¿Tiene tatuaje de identificación?")){
            tatuaje = EntradaConsola.ingresarTexto(TipoTexto.ALFANUMERICO, "Tatuaje");
        }
        String urlFoto = null;
        if (EntradaConsola.confirmar("¿Desea agregar una foto?")){
            urlFoto = EntradaConsola.ingresarTexto(TipoTexto.URL, "URL de la foto");
        }
        String condicionesPreexistentes = null;
        if (EntradaConsola.confirmar("¿Tiene condiciones preexistentes?")){
            condicionesPreexistentes = EntradaConsola.ingresarTexto(TipoTexto.ALFANUMERICO, "Condiciones preexistentes");
        }

        return mascota.duenioId(duenioId)
               .razaId(razaId)
               .sexoId(sexoId)
               .nombre(nombre)
               .fechaNacimiento(fechaNacimiento)
               .edadEstimada(edadEstimada)
               .pesoActual(peso)
               .microchip(microchip)
               .tatuaje(tatuaje)
               .urlFoto(urlFoto)
               .condicionesPreexistentes(condicionesPreexistentes)
               .build();
    }

    public boolean deseaAgregarAlergia(){
        return EntradaConsola.confirmar("¿Desea agregar una alergia?");
    }

    public String pedirObservacionesAlergia(){
        if (EntradaConsola.confirmar("¿Desea agregar observaciones?")){
            return EntradaConsola.ingresarTexto(TipoTexto.ALFANUMERICO, "Observaciones");
        }
        return null;
    }

    public String pedirMotivoTransferencia(){
        return EntradaConsola.ingresarTexto(TipoTexto.ALFANUMERICO, "Motivo de la transferencia");
    }

    public VacunaPrevia pedirDatosVacunaPrevia(int mascotaId, int tipoVacunaId){
        LocalDate fechaAplicacion = null;
        if (EntradaConsola.confirmar("¿Conoce la fecha de aplicación?")){
            fechaAplicacion = EntradaConsola.ingresarFechaNoFutura("Fecha de aplicación");
        }
        String lugar = null;
        if (EntradaConsola.confirmar("¿Conoce el lugar de aplicación?")){
            lugar = EntradaConsola.ingresarTexto(TipoTexto.ALFANUMERICO, "Lugar de aplicación");
        }
        String observaciones = null;
        if (EntradaConsola.confirmar("¿Desea agregar observaciones?")){
            observaciones = EntradaConsola.ingresarTexto(TipoTexto.ALFANUMERICO, "Observaciones");
        }
        return new VacunaPrevia(mascotaId, tipoVacunaId, fechaAplicacion, lugar, observaciones);
    }

    public ProcedimientoQuirurgicoPrevio pedirDatosProcedimientoPrevio(int mascotaId, int tipoProcedimientoId){
        String descripcion = EntradaConsola.ingresarTexto(TipoTexto.ALFANUMERICO, "Descripción del procedimiento");
        LocalDate fechaAproximada = null;
        if (EntradaConsola.confirmar("¿Conoce la fecha aproximada?")){
            fechaAproximada = EntradaConsola.ingresarFechaNoFutura("Fecha aproximada");
        }
        return new ProcedimientoQuirurgicoPrevio(mascotaId, tipoProcedimientoId, descripcion, fechaAproximada);
    }

    public double pedirPeso(){
        return EntradaConsola.ingresarDecimalPositivo("Nuevo peso en kg");
    }

    public boolean confirmarDesactivacion(Mascota mascota){
        return EntradaConsola.confirmar("¿Seguro que desea eliminar a " + mascota.getNombre() + "?");
    }

    public void mostrarMascotas(List<Mascota> mascotas, Map<Integer, String> duenios,
            Map<Integer, String> razas, Map<Integer, String> sexos){
        if (mascotas.isEmpty()){
            System.out.println("No hay mascotas registradas.");
            return;
        }
        System.out.printf(FORMATO_FILA, "ID", "NOMBRE", "RAZA", "SEXO", "DUEÑO");
        mascotas.forEach(m -> System.out.printf(FORMATO_FILA,
                m.getId(), m.getNombre(),
                razas.getOrDefault(m.getRazaId(), "-"),
                sexos.getOrDefault(m.getSexoId(), "-"),
                duenios.getOrDefault(m.getDuenioId(), "-")));
    }

    public void mostrarFicha(Mascota mascota, String duenio, String especie, String raza, String sexo){
        System.out.printf("""

            ==============================
                Ficha de %s
            ==============================
            ID:                 %d
            Especie / Raza:     %s / %s
            Sexo:               %s
            Edad:               %s
            Peso:               %s
            Microchip:          %s
            Tatuaje:            %s
            Foto:               %s
            Condiciones:        %s
            Dueño actual:       %s
            Estado:             %s
            """,
            mascota.getNombre(), mascota.getId(), especie, raza, sexo,
            describirEdad(mascota),
            mascota.getPesoActual() == null ? "-" : mascota.getPesoActual() + " kg",
            valorOGuion(mascota.getMicrochip()),
            valorOGuion(mascota.getTatuaje()),
            valorOGuion(mascota.getUrlFoto()),
            valorOGuion(mascota.getCondicionesPreexistentes()),
            duenio,
            mascota.isActivo() ? "Activa" : "Inactiva");
    }

    public void mostrarAlergias(List<Alergia> alergias, Map<Integer, String> nombresAlergias){
        System.out.println("\n---- Alergias ----");
        if (alergias.isEmpty()){
            System.out.println("Sin alergias registradas.");
            return;
        }
        alergias.forEach(a -> System.out.println("- " + nombresAlergias.getOrDefault(a.getCatalogoAlergiaId(), "-")
                + (a.getObservaciones() == null ? "" : " (" + a.getObservaciones() + ")")));
    }

    public void mostrarVacunasPrevias(List<VacunaPrevia> vacunas, Map<Integer, String> tiposVacunas){
        System.out.println("\n---- Vacunas previas ----");
        if (vacunas.isEmpty()){
            System.out.println("Sin vacunas previas registradas.");
            return;
        }
        vacunas.forEach(v -> System.out.println("- " + tiposVacunas.getOrDefault(v.getTipoVacunaId(), "-")
                + " | Fecha: " + formatearFecha(v.getFechaAplicacion())
                + " | Lugar: " + valorOGuion(v.getLugarAplicacion())));
    }

    public void mostrarProcedimientosPrevios(List<ProcedimientoQuirurgicoPrevio> procedimientos,
            Map<Integer, String> tiposProcedimientos){
        System.out.println("\n---- Procedimientos quirúrgicos previos ----");
        if (procedimientos.isEmpty()){
            System.out.println("Sin procedimientos previos registrados.");
            return;
        }
        procedimientos.forEach(p -> System.out.println("- " + tiposProcedimientos.getOrDefault(p.getTipoProcedimientoId(), "-")
                + ": " + valorOGuion(p.getDescripcion())
                + " | Fecha: " + formatearFecha(p.getFechaAproximada())));
    }

    public void mostrarHistorialDuenios(List<HistorialDuenio> historial, Map<Integer, String> duenios){
        System.out.println("\n---- Historial de dueños ----");
        historial.forEach(h -> System.out.println("- " + duenios.getOrDefault(h.getDuenioId(), "-")
                + " | Desde: " + formatearFechaHora(h.getFechaInicio())
                + " | Hasta: " + (h.esDuenioActual() ? "Actual" : formatearFechaHora(h.getFechaFin()))
                + " | Motivo: " + valorOGuion(h.getMotivo())));
    }

    public void mostrarMensaje(String mensaje){
        System.out.println(mensaje);
    }

    public void mostrarError(String mensaje){
        System.out.println("Error: " + mensaje);
    }

    private String describirEdad(Mascota mascota){
        if (mascota.getFechaNacimiento() != null){
            int anios = Period.between(mascota.getFechaNacimiento(), LocalDate.now()).getYears();
            return anios + " años (nació el " + formatearFecha(mascota.getFechaNacimiento()) + ")";
        }
        return mascota.getEdadEstimada() + " años (estimada)";
    }

    private String formatearFecha(LocalDate fecha){
        return fecha == null ? "-" : fecha.format(FORMATO_FECHA);
    }

    private String formatearFechaHora(LocalDateTime fecha){
        return fecha == null ? "-" : fecha.toLocalDate().format(FORMATO_FECHA);
    }

    private String valorOGuion(String valor){
        return valor == null || valor.isBlank() ? "-" : valor;
    }
}