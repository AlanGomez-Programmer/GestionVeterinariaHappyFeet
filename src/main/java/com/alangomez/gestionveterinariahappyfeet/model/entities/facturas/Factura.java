package com.alangomez.gestionveterinariahappyfeet.model.entities.facturas;

import java.time.LocalDateTime;

public class Factura {

    private int id;
    private int duenioId;
    private int usuarioId;
    private int tipoFacturaId;
    private int metodoPagoId;
    private int estadoId;
    private String numeroFactura;
    private LocalDateTime fechaEmision;
    private double subtotal;
    private double impuesto;
    private double descuento;
    private double total;
    private String observaciones;
    
    public Factura(){
        
    }

    public int getId() {
        return id;
    }

    public int getDuenioId() {
        return duenioId;
    }

    public int getUsuarioId() {
        return usuarioId;
    }

    public int getTipoFacturaId() {
        return tipoFacturaId;
    }

    public int getMetodoPagoId() {
        return metodoPagoId;
    }

    public int getEstadoId() {
        return estadoId;
    }

    public String getNumeroFactura() {
        return numeroFactura;
    }

    public LocalDateTime getFechaEmision() {
        return fechaEmision;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public double getImpuesto() {
        return impuesto;
    }

    public double getDescuento() {
        return descuento;
    }

    public double getTotal() {
        return total;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDuenioId(int duenioId) {
        this.duenioId = duenioId;
    }

    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }

    public void setTipoFacturaId(int tipoFacturaId) {
        this.tipoFacturaId = tipoFacturaId;
    }

    public void setMetodoPagoId(int metodoPagoId) {
        this.metodoPagoId = metodoPagoId;
    }

    public void setEstadoId(int estadoId) {
        this.estadoId = estadoId;
    }

    public void setNumeroFactura(String numeroFactura) {
        this.numeroFactura = numeroFactura;
    }

    public void setFechaEmision(LocalDateTime fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public void setImpuesto(double impuesto) {
        this.impuesto = impuesto;
    }

    public void setDescuento(double descuento) {
        this.descuento = descuento;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    
}