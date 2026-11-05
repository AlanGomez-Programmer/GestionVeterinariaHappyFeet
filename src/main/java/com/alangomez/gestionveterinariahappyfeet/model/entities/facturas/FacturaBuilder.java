package com.alangomez.gestionveterinariahappyfeet.model.entities.facturas;

import java.time.LocalDateTime;

public interface FacturaBuilder {
    
    FacturaBuilder id(int id);
    FacturaBuilder idDuenio(int idDuenio);
    FacturaBuilder idUsuario(int idUsuario);
    FacturaBuilder tipoFacturaId(int idTipoFactura);
    FacturaBuilder metodoPagoId(int idMetodoPago);
    FacturaBuilder estadoId(int idEstado);
    FacturaBuilder numeroFactura(String numeroFactura);
    FacturaBuilder fechaEmision(LocalDateTime fechaEmision);
    FacturaBuilder subtotal(Double subtotal);
    FacturaBuilder impuesto(Double impuesto);
    FacturaBuilder descuento(Double descuento);
    FacturaBuilder total(Double total);
    FacturaBuilder observaciones(String observaciones);
    Factura build();
}
