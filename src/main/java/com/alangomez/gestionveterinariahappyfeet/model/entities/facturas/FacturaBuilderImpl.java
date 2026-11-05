package com.alangomez.gestionveterinariahappyfeet.model.entities.facturas;

import java.time.LocalDateTime;

public class FacturaBuilderImpl implements FacturaBuilder {

    private Factura factura = new Factura();

    @Override
    public FacturaBuilder id(int id) {
        factura.setId(id);
        return this;
    }

    @Override
    public FacturaBuilder idDuenio(int idDuenio) {
        factura.setDuenioId(idDuenio);
        return this;
    }

    @Override
    public FacturaBuilder idUsuario(int idUsuario) {
        factura.setUsuarioId(idUsuario);
        return this;
    }

    @Override
    public FacturaBuilder tipoFacturaId(int idTipoFactura) {
        factura.setTipoFacturaId(idTipoFactura);
        return this;
    }

    @Override
    public FacturaBuilder metodoPagoId(int idMetodoPago) {
        factura.setMetodoPagoId(idMetodoPago);
        return this;
    }

    @Override
    public FacturaBuilder estadoId(int idEstado) {
        factura.setEstadoId(idEstado);
        return this;
    }

    @Override
    public FacturaBuilder numeroFactura(String numeroFactura) {
        factura.setNumeroFactura(numeroFactura);
        return this;
    }

    @Override
    public FacturaBuilder fechaEmision(LocalDateTime fechaEmision) {
        factura.setFechaEmision(fechaEmision);
        return this;
    }

    @Override
    public FacturaBuilder subtotal(Double subtotal) {
        factura.setSubtotal(subtotal);
        return this;
    }

    @Override
    public FacturaBuilder impuesto(Double impuesto) {
        factura.setImpuesto(impuesto);
        return this;
    }

    @Override
    public FacturaBuilder descuento(Double descuento) {
        factura.setDescuento(descuento);
        return this;
    }

    @Override
    public FacturaBuilder total(Double total) {
        factura.setTotal(total);
        return this;
    }

    @Override
    public FacturaBuilder observaciones(String observaciones) {
        factura.setObservaciones(observaciones);
        return this;
    }

    @Override
    public Factura build() {
        Factura resultado = factura;
        factura = new Factura();
        return resultado;
    }
}
