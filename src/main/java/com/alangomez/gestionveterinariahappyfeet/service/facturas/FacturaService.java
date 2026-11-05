package com.alangomez.gestionveterinariahappyfeet.service.facturas;

import com.alangomez.gestionveterinariahappyfeet.model.dao.facturas.FacturaDAO;
import com.alangomez.gestionveterinariahappyfeet.model.entities.facturas.Factura;
import com.alangomez.gestionveterinariahappyfeet.model.entities.pacientes.Duenio;
import com.alangomez.gestionveterinariahappyfeet.model.entities.servicios.Servicio;
import com.alangomez.gestionveterinariahappyfeet.service.pacientes.DuenioService;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class FacturaService {

    private final FacturaDAO facturaDAO;
    private final DuenioService duenioService;

    public FacturaService(FacturaDAO facturaDAO, DuenioService duenioService) {
        this.facturaDAO = facturaDAO;
        this.duenioService = duenioService;
    }

    public void registrar(String documento, int tipoFactura, int metodoPago,
            int servicioId, int cantidad) throws SQLException {

        Duenio duenio = duenioService.buscarPorDocumento(documento);

        Servicio servicio = facturaDAO.buscarServicio(servicioId);

        if (servicio == null) {
            throw new IllegalArgumentException("El servicio seleccionado no existe");
        }

        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a cero");
        }

        double precio = servicio.getPrecio().doubleValue();
        double subtotal = precio * cantidad;

        Factura factura = new Factura();

        factura.setDuenioId(duenio.getId());
        factura.setTipoFacturaId(tipoFactura);
        factura.setMetodoPagoId(metodoPago);
        factura.setEstadoId(facturaDAO.buscarIdCatalogo(
                "estados_facturas", "Pendiente"));

        factura.setNumeroFactura("HF-" + UUID.randomUUID()
                .toString().substring(0, 8).toUpperCase());

        factura.setFechaEmision(LocalDateTime.now());
        factura.setSubtotal(subtotal);
        factura.setImpuesto(0);
        factura.setDescuento(0);
        factura.setTotal(subtotal);
        factura.setObservaciones("Factura generada desde el sistema Happy Feet");

        facturaDAO.registrarFactura(
                factura,
                servicio.getId(),
                servicio.getNombre(),
                cantidad,
                precio
        );
    }

    public Factura buscarFactura(int id) throws SQLException {
        Factura factura = facturaDAO.buscarFactura(id);

        if (factura == null) {
            throw new IllegalArgumentException("No se encontró la factura");
        }

        return factura;
    }

    public List<String> listarDetalles(int facturaId) throws SQLException {
        return facturaDAO.listarDetalles(facturaId);
    }

    public String buscarNombreEstado(int estadoId) throws SQLException {
        return facturaDAO.buscarNombreEstado(estadoId);
    }

    public void cambiarEstado(int facturaId, int estadoId) throws SQLException {

        buscarFactura(facturaId);

        if (estadoId < 1 || estadoId > 3) {
            throw new IllegalArgumentException("El estado seleccionado no es válido");
        }

        facturaDAO.actualizarEstado(facturaId, estadoId);
    }

    public Duenio buscarDuenio(String documento) throws SQLException {
        return duenioService.buscarPorDocumento(documento);
    }

    public List<Factura> historialPorCliente(int duenioId) throws SQLException {
        return facturaDAO.listarFacturasPorDuenio(duenioId);
    }

    public double calcularTotalGeneral(List<Factura> facturas) {

        return facturas.stream()
                .mapToDouble(Factura::getTotal)
                .sum();
    }

    public List<Servicio> listarServicios() throws SQLException {
        return facturaDAO.listarServiciosDisponibles();
    }

    public int obtenerIdTipoFactura(int opcion) throws SQLException {
        String[] tipos = {
            "Consulta",
            "Procedimiento especial",
            "Venta directa"
        };

        return facturaDAO.buscarIdCatalogo("tipos_facturas", tipos[opcion - 1]);
    }

    public int obtenerIdMetodoPago(int opcion) throws SQLException {
        String[] metodos = {
            "Efectivo",
            "Tarjeta",
            "Transferencia"
        };

        return facturaDAO.buscarIdCatalogo("metodos_pago", metodos[opcion - 1]);
    }
    
    public Duenio buscarDuenioPorId(int id) throws SQLException {
        return duenioService.buscarPorId(id);
    }

    public int obtenerIdEstado(int opcion) throws SQLException {

        String[] estados = {
            "Pendiente",
            "Pagada",
            "Anulada"
        };

        if (opcion < 1 || opcion > estados.length) {
            throw new IllegalArgumentException("Estado no válido");
        }

        return facturaDAO.buscarIdCatalogo(
                "estados_facturas", estados[opcion - 1]);
    }
}