package com.alangomez.gestionveterinariahappyfeet.controller.facturas;

import com.alangomez.gestionveterinariahappyfeet.controller.ControladorBase;
import com.alangomez.gestionveterinariahappyfeet.model.entities.facturas.Factura;
import com.alangomez.gestionveterinariahappyfeet.model.entities.pacientes.Duenio;
import com.alangomez.gestionveterinariahappyfeet.model.entities.servicios.Servicio;
import com.alangomez.gestionveterinariahappyfeet.service.facturas.FacturaService;
import com.alangomez.gestionveterinariahappyfeet.view.facturas.VistaFacturas;

import java.sql.SQLException;
import java.util.List;

public class FacturaControlador extends ControladorBase {

    private final VistaFacturas vista;
    private final FacturaService facturaService;

    public FacturaControlador(VistaFacturas vista, FacturaService facturaService) {
        super(vista);
        this.vista = vista;
        this.facturaService = facturaService;
    }

    public void iniciar() {
        int opcion;

        do {
            opcion = vista.menuFacturacion();

            try {
                switch (opcion) {
                    case 1 -> realizarFactura();
                    case 2 -> verFactura();
                    case 3 -> cambiarEstado();
                    case 4 -> consultarHistorial();
                    default -> { }
                }
            } catch (IllegalArgumentException e) {
                vista.mostrarError(e.getMessage());
            } catch (SQLException e) {
                manejarErrorBaseDatos("Menú de facturación, opción " + opcion, e);
            }

        } while (opcion != 0);
    }

    private void realizarFactura() throws SQLException {

        String documento = vista.ingresarDocumento();

        Duenio duenio = facturaService.buscarDuenio(documento);

        vista.mostrarMensaje("\nCliente: " + duenio.getNombreCompleto());

        List<Servicio> servicios = facturaService.listarServicios();

        if (servicios.isEmpty()) {
            vista.mostrarMensaje("No hay servicios disponibles para facturar");
            return;
        }

        vista.mostrarServicios(servicios);

        int servicioId = vista.seleccionarServicio();
        int cantidad = vista.ingresarCantidad();

        int tipoFactura = facturaService.obtenerIdTipoFactura(
                vista.ingresarTipoFactura());

        int metodoPago = facturaService.obtenerIdMetodoPago(
                vista.ingresarMetodoPago());

        facturaService.registrar(
                documento,
                tipoFactura,
                metodoPago,
                servicioId,
                cantidad
        );

        vista.mostrarMensaje("Factura registrada correctamente");
    }

    private void verFactura() throws SQLException {

        int facturaId = vista.ingresarIdFactura();

        Factura factura = facturaService.buscarFactura(facturaId);

        Duenio duenio = facturaService.buscarDuenioPorId(
                factura.getDuenioId());

        List<String> detalles = facturaService.listarDetalles(facturaId);

        String estado = facturaService.buscarNombreEstado(
                factura.getEstadoId());

        vista.mostrarFactura(factura, duenio, estado, detalles);
    }

    private void cambiarEstado() throws SQLException {

        int facturaId = vista.ingresarIdFactura();
        int opcionEstado = vista.ingresarEstado();

        int estadoId = facturaService.obtenerIdEstado(opcionEstado);

        facturaService.cambiarEstado(facturaId, estadoId);

        vista.mostrarMensaje("Estado de la factura actualizado correctamente");
    }

    private void consultarHistorial() throws SQLException {

        String documento = vista.ingresarDocumento();

        Duenio duenio = facturaService.buscarDuenio(documento);

        List<Factura> facturas = facturaService.historialPorCliente(duenio.getId());

        double totalGeneral = facturaService.calcularTotalGeneral(facturas);

        vista.mostrarHistorial(duenio, facturas, totalGeneral);
    }
}