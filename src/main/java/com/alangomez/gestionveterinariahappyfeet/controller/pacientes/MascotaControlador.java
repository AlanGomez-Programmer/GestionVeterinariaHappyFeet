package com.alangomez.gestionveterinariahappyfeet.controller.pacientes;

import com.alangomez.gestionveterinariahappyfeet.model.entities.inventario.TipoVacuna;
import com.alangomez.gestionveterinariahappyfeet.model.entities.pacientes.Alergia;
import com.alangomez.gestionveterinariahappyfeet.model.entities.pacientes.CatalogoAlergia;
import com.alangomez.gestionveterinariahappyfeet.model.entities.pacientes.Duenio;
import com.alangomez.gestionveterinariahappyfeet.model.entities.pacientes.Especie;
import com.alangomez.gestionveterinariahappyfeet.model.entities.pacientes.Raza;
import com.alangomez.gestionveterinariahappyfeet.model.entities.pacientes.Sexo;
import com.alangomez.gestionveterinariahappyfeet.model.entities.pacientes.TipoAlergia;
import com.alangomez.gestionveterinariahappyfeet.model.entities.pacientes.mascota.Mascota;
import com.alangomez.gestionveterinariahappyfeet.model.entities.servicios.TipoProcedimientoEspecial;
import com.alangomez.gestionveterinariahappyfeet.service.pacientes.CatalogoPacientesService;
import com.alangomez.gestionveterinariahappyfeet.service.pacientes.DuenioService;
import com.alangomez.gestionveterinariahappyfeet.service.pacientes.MascotaService;
import com.alangomez.gestionveterinariahappyfeet.util.RegistroErrores;
import com.alangomez.gestionveterinariahappyfeet.view.VistaMascota;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class MascotaControlador {

    private final VistaMascota vista;
    private final MascotaService mascotaService;
    private final DuenioService duenioService;
    private final CatalogoPacientesService catalogoService;

    public MascotaControlador(VistaMascota vista, MascotaService mascotaService,
            DuenioService duenioService, CatalogoPacientesService catalogoService){
        this.vista = vista;
        this.mascotaService = mascotaService;
        this.duenioService = duenioService;
        this.catalogoService = catalogoService;
    }

    public void iniciar(){
        int opcion;
        do {
            opcion = vista.menuMascotas();
            try {
                switch (opcion) {
                    case 1 -> registrar();
                    case 2 -> listar();
                    case 3 -> verFicha();
                    case 4 -> transferir();
                    case 5 -> agregarAlergia();
                    case 6 -> registrarVacunaPrevia();
                    case 7 -> registrarProcedimientoPrevio();
                    case 8 -> actualizarPeso();
                    case 9 -> desactivar();
                    default -> { }
                }
            } catch (IllegalArgumentException e) {
                vista.mostrarError(e.getMessage());
            } catch (SQLException e) {
                RegistroErrores.registrar("Menú de mascotas, opción " + opcion, e);
                vista.mostrarError("Ocurrió un problema con la base de datos. Revise el archivo errores.log");
            }
        } while (opcion != 0);
    }

    private void registrar() throws SQLException{
        List<Duenio> duenios = duenioService.listarActivos();
        if (duenios.isEmpty()){
            throw new IllegalArgumentException("Primero debe registrar un dueño");
        }
        int duenioId = elegir("Dueños", aMapa(duenios, Duenio::getId, Duenio::getNombreCompleto));
        int especieId = elegir("Especies", aMapa(catalogoService.listarEspecies(), Especie::getId, Especie::getNombre));
        int razaId = elegir("Razas", aMapa(catalogoService.listarRazasPorEspecie(especieId), Raza::getId, Raza::getNombre));
        int sexoId = elegir("Sexos", aMapa(catalogoService.listarSexos(), Sexo::getId, Sexo::getNombre));

        Mascota mascota = vista.pedirDatosMascota(duenioId, razaId, sexoId);
        mascotaService.validarNuevaMascota(mascota);

        List<Alergia> alergias = new ArrayList<>();
        Map<Integer, String> catalogoAlergias = nombresAlergias();
        while (vista.deseaAgregarAlergia()){
            int alergiaId = elegir("Alergias", catalogoAlergias);
            alergias.add(new Alergia(0, alergiaId, vista.pedirObservacionesAlergia()));
        }

        mascotaService.registrar(mascota, alergias);
        vista.mostrarMensaje("Mascota registrada correctamente con el id " + mascota.getId());
    }

    private void listar() throws SQLException{
        vista.mostrarMascotas(
                mascotaService.listarActivas(),
                aMapa(duenioService.listarTodos(), Duenio::getId, Duenio::getNombreCompleto),
                aMapa(catalogoService.listarRazas(), Raza::getId, Raza::getNombre),
                aMapa(catalogoService.listarSexos(), Sexo::getId, Sexo::getNombre));
    }

    private void verFicha() throws SQLException{
        int id = vista.pedirId("consultar");
        Mascota mascota = mascotaService.buscarPorId(id);

        Raza raza = catalogoService.listarRazas().stream()
                .filter(r -> r.getId() == mascota.getRazaId())
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("La raza de la mascota no existe"));
        String especie = aMapa(catalogoService.listarEspecies(), Especie::getId, Especie::getNombre)
                .getOrDefault(raza.getEspecieId(), "-");
        String sexo = aMapa(catalogoService.listarSexos(), Sexo::getId, Sexo::getNombre)
                .getOrDefault(mascota.getSexoId(), "-");
        Map<Integer, String> duenios = aMapa(duenioService.listarTodos(), Duenio::getId, Duenio::getNombreCompleto);

        vista.mostrarFicha(mascota, duenios.getOrDefault(mascota.getDuenioId(), "-"), especie, raza.getNombre(), sexo);
        vista.mostrarAlergias(mascotaService.listarAlergias(id), nombresAlergias());
        vista.mostrarVacunasPrevias(mascotaService.listarVacunasPrevias(id),
                aMapa(catalogoService.listarTiposVacunas(), TipoVacuna::getId, TipoVacuna::getNombre));
        vista.mostrarProcedimientosPrevios(mascotaService.listarProcedimientosPrevios(id),
                aMapa(catalogoService.listarTiposProcedimientos(),
                        TipoProcedimientoEspecial::getId, TipoProcedimientoEspecial::getNombre));
        vista.mostrarHistorialDuenios(mascotaService.listarHistorialDuenios(id), duenios);
    }

    private void transferir() throws SQLException{
        int id = vista.pedirId("transferir");
        Mascota mascota = mascotaService.buscarActivaPorId(id);

        List<Duenio> candidatos = duenioService.listarActivos().stream()
                .filter(d -> d.getId() != mascota.getDuenioId())
                .toList();
        if (candidatos.isEmpty()){
            throw new IllegalArgumentException("No hay otros dueños activos para transferir la mascota");
        }
        int nuevoDuenioId = elegir("Nuevo dueño", aMapa(candidatos, Duenio::getId, Duenio::getNombreCompleto));
        String motivo = vista.pedirMotivoTransferencia();

        mascotaService.transferir(id, nuevoDuenioId, motivo);
        vista.mostrarMensaje("Mascota " + mascota.getNombre() + " transferida correctamente");
    }

    private void agregarAlergia() throws SQLException{
        int id = vista.pedirId("agregar alergia");
        mascotaService.buscarActivaPorId(id);
        int alergiaId = elegir("Alergias", nombresAlergias());
        mascotaService.agregarAlergia(new Alergia(id, alergiaId, vista.pedirObservacionesAlergia()));
        vista.mostrarMensaje("Alergia agregada correctamente");
    }

    private void registrarVacunaPrevia() throws SQLException{
        int id = vista.pedirId("registrar vacuna previa");
        mascotaService.buscarActivaPorId(id);
        int tipoVacunaId = elegir("Tipos de vacuna",
                aMapa(catalogoService.listarTiposVacunas(), TipoVacuna::getId, TipoVacuna::getNombre));
        mascotaService.agregarVacunaPrevia(vista.pedirDatosVacunaPrevia(id, tipoVacunaId));
        vista.mostrarMensaje("Vacuna previa registrada correctamente");
    }

    private void registrarProcedimientoPrevio() throws SQLException{
        int id = vista.pedirId("registrar procedimiento previo");
        mascotaService.buscarActivaPorId(id);
        int tipoId = elegir("Tipos de procedimiento",
                aMapa(catalogoService.listarTiposProcedimientos(),
                        TipoProcedimientoEspecial::getId, TipoProcedimientoEspecial::getNombre));
        mascotaService.agregarProcedimientoPrevio(vista.pedirDatosProcedimientoPrevio(id, tipoId));
        vista.mostrarMensaje("Procedimiento previo registrado correctamente");
    }

    private void actualizarPeso() throws SQLException{
        int id = vista.pedirId("actualizar peso");
        mascotaService.buscarActivaPorId(id);
        mascotaService.actualizarPeso(id, vista.pedirPeso());
        vista.mostrarMensaje("Peso actualizado correctamente");
    }

    private void desactivar() throws SQLException{
        int id = vista.pedirId("eliminar");
        Mascota mascota = mascotaService.buscarActivaPorId(id);
        if (vista.confirmarDesactivacion(mascota)){
            mascotaService.desactivar(id);
            vista.mostrarMensaje("Mascota eliminada correctamente");
        }
    }

    private int elegir(String titulo, Map<Integer, String> opciones){
        vista.mostrarOpciones(titulo, opciones);
        while (true){
            int id = vista.pedirIdOpcion(titulo);
            if (opciones.containsKey(id)){
                return id;
            }
            vista.mostrarError("El ID " + id + " no está en la lista");
        }
    }

    private Map<Integer, String> nombresAlergias() throws SQLException{
        Map<Integer, String> tipos = aMapa(catalogoService.listarTiposAlergias(), TipoAlergia::getId, TipoAlergia::getNombre);
        return aMapa(catalogoService.listarCatalogoAlergias(), CatalogoAlergia::getId,
                c -> c.getNombre() + " (" + tipos.getOrDefault(c.getTipoAlergiaId(), "-") + ")");
    }

    private <T> Map<Integer, String> aMapa(List<T> lista, Function<T, Integer> id, Function<T, String> nombre){
        return lista.stream()
                .collect(Collectors.toMap(id, nombre, (primero, segundo) -> primero, LinkedHashMap::new));
    }
}
