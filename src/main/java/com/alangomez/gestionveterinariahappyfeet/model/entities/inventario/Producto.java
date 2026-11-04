package com.alangomez.gestionveterinariahappyfeet.model.entities.inventario;

import java.math.BigDecimal;

public class Producto {

    private int id;
    private int tipoProductoId;
    private Integer fabricanteId;
    private Integer proveedorId;
    private String nombre;
    private String descripcion;
    private String unidadMedida;
    private int stockMinimo;
    private BigDecimal precioCompra;
    private BigDecimal precioVenta;
    private boolean requiereReceta;
    private boolean activo;

    public Producto(int id, int tipoProductoId, Integer fabricanteId, Integer proveedorId, String nombre,
            String descripcion, String unidadMedida, int stockMinimo, BigDecimal precioCompra,
            BigDecimal precioVenta, boolean requiereReceta, boolean activo) {
        this.id = id;
        this.tipoProductoId = tipoProductoId;
        this.fabricanteId = fabricanteId;
        this.proveedorId = proveedorId;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.unidadMedida = unidadMedida;
        this.stockMinimo = stockMinimo;
        this.precioCompra = precioCompra;
        this.precioVenta = precioVenta;
        this.requiereReceta = requiereReceta;
        this.activo = activo;
    }

    public int getId() {
        return id;
    }

    public int getTipoProductoId() {
        return tipoProductoId;
    }

    public Integer getFabricanteId() {
        return fabricanteId;
    }

    public Integer getProveedorId() {
        return proveedorId;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getUnidadMedida() {
        return unidadMedida;
    }

    public int getStockMinimo() {
        return stockMinimo;
    }

    public BigDecimal getPrecioCompra() {
        return precioCompra;
    }

    public BigDecimal getPrecioVenta() {
        return precioVenta;
    }

    public boolean isRequiereReceta() {
        return requiereReceta;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTipoProductoId(int tipoProductoId) {
        this.tipoProductoId = tipoProductoId;
    }
}
