package com.alangomez.gestionveterinariahappyfeet.model.entities.pacientes.mascota;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface MascotaBuilder {

    MascotaBuilder id(int id);
    MascotaBuilder duenioId(int duenioId);
    MascotaBuilder razaId(int razaId);
    MascotaBuilder sexoId(int sexoId);
    MascotaBuilder nombre(String nombre);
    MascotaBuilder fechaNacimiento(LocalDate fechaNacimiento);
    MascotaBuilder edadEstimada(Integer edadEstimada);
    MascotaBuilder pesoActual(Double pesoActual);
    MascotaBuilder microchip(String microchip);
    MascotaBuilder tatuaje(String tatuaje);
    MascotaBuilder urlFoto(String urlFoto);
    MascotaBuilder condicionesPreexistentes(String condicionesPreexistentes);
    MascotaBuilder fechaRegistro(LocalDateTime fechaRegistro);
    MascotaBuilder activo(boolean activo);
    Mascota build();

}
