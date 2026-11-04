package com.alangomez.gestionveterinariahappyfeet.model.entities.servicios.consulta;

import java.time.LocalDateTime;

public interface ConsultaBuilder {

    ConsultaBuilder id(int id);
    ConsultaBuilder mascotaId(int mascotaId);
    ConsultaBuilder veterinarioId(int veterinarioId);
    ConsultaBuilder citaId(Integer citaId);
    ConsultaBuilder estadoId(int estadoId);
    ConsultaBuilder fechaHora(LocalDateTime fechaHora);
    ConsultaBuilder motivo(String motivo);
    ConsultaBuilder sintomas(String sintomas);
    ConsultaBuilder diagnostico(String diagnostico);
    ConsultaBuilder recomendaciones(String recomendaciones);
    ConsultaBuilder observaciones(String observaciones);
    ConsultaBuilder pesoRegistrado(Double pesoRegistrado);
    ConsultaBuilder temperatura(Double temperatura);
    Consulta build();

}
