package com.alangomez.gestionveterinariahappyfeet.model.entities.pacientes.mascota;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class MascotaBuilderImpl implements MascotaBuilder{

    private Mascota mascota = new Mascota();

    @Override
    public MascotaBuilder id(int id) {
        mascota.setId(id);
        return this;
    }

    @Override
    public MascotaBuilder duenioId(int duenioId) {
        mascota.setDuenioId(duenioId);
        return this;
    }

    @Override
    public MascotaBuilder razaId(int razaId) {
        mascota.setRazaId(razaId);
        return this;
    }

    @Override
    public MascotaBuilder sexoId(int sexoId) {
        mascota.setSexoId(sexoId);
        return this;
    }

    @Override
    public MascotaBuilder nombre(String nombre) {
        mascota.setNombre(nombre);
        return this;
    }

    @Override
    public MascotaBuilder fechaNacimiento(LocalDate fechaNacimiento) {
        mascota.setFechaNacimiento(fechaNacimiento);
        return this;
    }

    @Override
    public MascotaBuilder edadEstimada(Integer edadEstimada) {
        mascota.setEdadEstimada(edadEstimada);
        return this;
    }

    @Override
    public MascotaBuilder pesoActual(Double pesoActual) {
        mascota.setPesoActual(pesoActual);
        return this;
    }

    @Override
    public MascotaBuilder microchip(String microchip) {
        mascota.setMicrochip(microchip);
        return this;
    }

    @Override
    public MascotaBuilder tatuaje(String tatuaje){
        mascota.setTatuaje(tatuaje);
        return this;
    }

    @Override
    public MascotaBuilder urlFoto(String urlFoto) {
        mascota.setUrlFoto(urlFoto);
        return this;
    }

    @Override
    public MascotaBuilder condicionesPreexistentes(String condicionesPreexistentes) {
        mascota.setCondicionesPreexistentes(condicionesPreexistentes);
        return this;
    }

    @Override
    public MascotaBuilder fechaRegistro(LocalDateTime fechaRegistro) {
        mascota.setFechaRegistro(fechaRegistro);
        return this;
    }

    @Override
    public MascotaBuilder activo(boolean activo) {
        mascota.setActivo(activo);
        return this;
    }

    @Override
    public Mascota build() {
        validar();
        Mascota resultado = mascota;
        mascota = new Mascota();
        return resultado;
    }

    private void validar(){
        if (mascota.getNombre() == null || mascota.getNombre().isBlank()){
            throw new IllegalArgumentException("El nombre de la mascota es obligatorio");
        }
        if (mascota.getDuenioId() <= 0 || mascota.getRazaId() <= 0 || mascota.getSexoId() <= 0){
            throw new IllegalArgumentException("El dueño, la raza y el sexo son obligatorios");
        }
        if (mascota.getFechaNacimiento() == null && mascota.getEdadEstimada() == null){
            throw new IllegalArgumentException("Debe indicar la fecha de nacimiento o la edad estimada");
        }
        if (mascota.getFechaNacimiento() != null && mascota.getFechaNacimiento().isAfter(LocalDate.now())){
            throw new IllegalArgumentException("La fecha de nacimiento no puede ser futura");
        }
        if (mascota.getEdadEstimada() != null && mascota.getEdadEstimada() < 0){
            throw new IllegalArgumentException("La edad estimada no puede ser negativa");
        }
        if (mascota.getPesoActual() != null && mascota.getPesoActual() <= 0){
            throw new IllegalArgumentException("El peso debe ser mayor que cero");
        }
    }
}
