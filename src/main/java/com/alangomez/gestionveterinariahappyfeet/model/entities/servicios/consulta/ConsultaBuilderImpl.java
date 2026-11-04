package com.alangomez.gestionveterinariahappyfeet.model.entities.servicios.consulta;

import java.time.LocalDateTime;

public class ConsultaBuilderImpl implements ConsultaBuilder{

    private Consulta consulta = new Consulta();

    @Override
    public ConsultaBuilder id(int id) {
        consulta.setId(id);
        return this;
    }

    @Override
    public ConsultaBuilder mascotaId(int mascotaId) {
        consulta.setMascotaId(mascotaId);
        return this;
    }

    @Override
    public ConsultaBuilder veterinarioId(int veterinarioId) {
        consulta.setVeterinarioId(veterinarioId);
        return this;
    }

    @Override
    public ConsultaBuilder citaId(Integer citaId) {
        consulta.setCitaId(citaId);
        return this;
    }

    @Override
    public ConsultaBuilder estadoId(int estadoId) {
        consulta.setEstadoId(estadoId);
        return this;
    }

    @Override
    public ConsultaBuilder fechaHora(LocalDateTime fechaHora) {
        consulta.setFechaHora(fechaHora);
        return this;
    }

    @Override
    public ConsultaBuilder motivo(String motivo) {
        consulta.setMotivo(motivo);
        return this;
    }

    @Override
    public ConsultaBuilder sintomas(String sintomas) {
        consulta.setSintomas(sintomas);
        return this;
    }

    @Override
    public ConsultaBuilder diagnostico(String diagnostico) {
        consulta.setDiagnostico(diagnostico);
        return this;
    }

    @Override
    public ConsultaBuilder recomendaciones(String recomendaciones) {
        consulta.setRecomendaciones(recomendaciones);
        return this;
    }

    @Override
    public ConsultaBuilder observaciones(String observaciones) {
        consulta.setObservaciones(observaciones);
        return this;
    }

    @Override
    public ConsultaBuilder pesoRegistrado(Double pesoRegistrado) {
        consulta.setPesoRegistrado(pesoRegistrado);
        return this;
    }

    @Override
    public ConsultaBuilder temperatura(Double temperatura) {
        consulta.setTemperatura(temperatura);
        return this;
    }

    @Override
    public Consulta build() {
        validar();
        Consulta resultado = consulta;
        consulta = new Consulta();
        return resultado;
    }

    private void validar(){
        if (consulta.getMascotaId() <= 0 || consulta.getVeterinarioId() <= 0){
            throw new IllegalArgumentException("La mascota y el veterinario son obligatorios");
        }
        if (consulta.getFechaHora() == null){
            throw new IllegalArgumentException("La fecha y hora de la consulta son obligatorias");
        }
        if (consulta.getMotivo() == null || consulta.getMotivo().isBlank()){
            throw new IllegalArgumentException("El motivo de la consulta es obligatorio");
        }
        if (consulta.getPesoRegistrado() != null && consulta.getPesoRegistrado() <= 0){
            throw new IllegalArgumentException("El peso debe ser mayor que cero");
        }
        if (consulta.getTemperatura() != null && (consulta.getTemperatura() < 30 || consulta.getTemperatura() > 45)){
            throw new IllegalArgumentException("La temperatura debe estar entre 30 y 45 °C");
        }
    }
}
