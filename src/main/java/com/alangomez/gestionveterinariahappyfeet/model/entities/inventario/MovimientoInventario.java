package com.alangomez.gestionveterinariahappyfeet.model.entities.inventario;

public class MovimientoInventario {

    private int inventarioId;
    private int tipoMovimientoId;
    private int usuarioId;
    private int cantidad;
    private int stockAnterior;
    private int stockNuevo;
    private String motivo;
    private Integer consultaId;
    private Integer procedimientoId;

    public MovimientoInventario(int inventarioId, int tipoMovimientoId, int usuarioId, int cantidad,
            int stockAnterior, int stockNuevo, String motivo, Integer consultaId, Integer procedimientoId) {
        this.inventarioId = inventarioId;
        this.tipoMovimientoId = tipoMovimientoId;
        this.usuarioId = usuarioId;
        this.cantidad = cantidad;
        this.stockAnterior = stockAnterior;
        this.stockNuevo = stockNuevo;
        this.motivo = motivo;
        this.consultaId = consultaId;
        this.procedimientoId = procedimientoId;
    }

    public int getInventarioId() {
        return inventarioId;
    }

    public int getTipoMovimientoId() {
        return tipoMovimientoId;
    }

    public int getUsuarioId() {
        return usuarioId;
    }

    public int getCantidad() {
        return cantidad;
    }

    public int getStockAnterior() {
        return stockAnterior;
    }

    public int getStockNuevo() {
        return stockNuevo;
    }

    public String getMotivo() {
        return motivo;
    }

    public Integer getConsultaId() {
        return consultaId;
    }

    public Integer getProcedimientoId() {
        return procedimientoId;
    }
}
