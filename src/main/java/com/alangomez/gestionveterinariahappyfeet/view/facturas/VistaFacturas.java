package com.alangomez.gestionveterinariahappyfeet.view.facturas;

import com.alangomez.gestionveterinariahappyfeet.model.entities.facturas.Factura;
import com.alangomez.gestionveterinariahappyfeet.model.entities.pacientes.Duenio;
import com.alangomez.gestionveterinariahappyfeet.model.entities.servicios.Servicio;
import com.alangomez.gestionveterinariahappyfeet.util.EntradaConsola;
import com.alangomez.gestionveterinariahappyfeet.view.VistaBase;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

public class VistaFacturas extends VistaBase {

    private static final NumberFormat FORMATO_MONEDA = crearFormatoMoneda();
    private static final String FORMATO_FILA_HISTORIAL = "%-10s | %-21s | %s%n";
    private static final String LINEA = "-------------------------------------------------------------";
    private static final String LINEA_TITULO = "=_=_=_=_=_=_=_=_=_=_=_=_=_=_=_=_=_=_=_=_=_=_=_=_=_=_=_=_=_=_";

    private static final DateTimeFormatter FORMATO_FECHA =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public int menuFacturacion() {
        System.out.println("""
            
            ==================================
                      Facturación
            ==================================
                1. Realizar una factura
                2. Ver una factura
                3. Cambiar estado de una factura
                4. Consultar historial Facturación por Cliente
                0. Regresar
            """);
        return EntradaConsola.ingresarEnteroEnRango("Ingrese una opción", 0, 4);
    }

    public String ingresarDocumento() {
        return EntradaConsola.ingresarCadenaNumeros("Ingrese el documento del dueño");
    }

    public int ingresarIdFactura() {
        return EntradaConsola.ingresarEntero("Ingrese el ID de la factura");
    }

    public int ingresarTipoFactura() {
        System.out.println("""
            
            Tipos de factura:
                1. Consulta
                2. Procedimiento especial
                3. Venta directa
            """);
        return EntradaConsola.ingresarEnteroEnRango("Seleccione el tipo", 1, 3);
    }

    public int ingresarMetodoPago() {
        System.out.println("""
            
            Métodos de pago:
                1. Efectivo
                2. Tarjeta
                3. Transferencia
            """);
        return EntradaConsola.ingresarEnteroEnRango("Seleccione el método de pago", 1, 3);
    }

    public int ingresarEstado() {
        System.out.println("""
            
            Estados de factura:
                1. Pendiente
                2. Pagada
                3. Anulada
            """);
        return EntradaConsola.ingresarEnteroEnRango("Seleccione el estado", 1, 3);
    }

    public int ingresarCantidad() {
        return EntradaConsola.ingresarEntero("Ingrese la cantidad");
    }

    public void mostrarServicios(List<Servicio> servicios) {
        System.out.println("\n========== SERVICIOS DISPONIBLES ==========");
        System.out.printf("%-8s %-35s %-15s%n", "ID", "Servicio", "Precio");

        for (Servicio servicio : servicios) {
            System.out.printf("%-8d %-35s %-15s%n",
                    servicio.getId(),
                    servicio.getNombre(),
                    FORMATO_MONEDA.format(servicio.getPrecio()));
        }
    }

    public int seleccionarServicio() {
        return EntradaConsola.ingresarEntero("Ingrese el ID del servicio");
    }

    public void mostrarFactura(Factura factura, Duenio duenio, String nombreEstado,
            List<String> detalles) {

        System.out.println();
        System.out.println(LINEA_TITULO);
        System.out.println("""                
                           
                                              FACTURA HAPPY FEET
                           """);
        System.out.println(LINEA_TITULO);

        System.out.println("-- DATOS DEL CLIENTE --");
        System.out.println("Nombre:       " + duenio.getNombreCompleto());
        System.out.println("Documento:    " + duenio.getDocumentoIdentidad());

        System.out.println("\n-- DATOS DE LA FACTURA --");
        System.out.println("ID:           " + factura.getId());
        System.out.println("Número:       " + factura.getNumeroFactura());
        System.out.println("Fecha:        " + factura.getFechaEmision().format(FORMATO_FECHA));
        System.out.println("Estado:       " + nombreEstado);

        System.out.println("\n-- DETALLE --");
        for (String detalle : detalles) {
            System.out.println(detalle);
        }

        System.out.println(LINEA);
        System.out.println("Subtotal:     " + formatearMoneda(factura.getSubtotal()));
        System.out.println("Impuesto:     " + formatearMoneda(factura.getImpuesto()));
        System.out.println("Descuento:    " + formatearMoneda(factura.getDescuento()));
        System.out.println("TOTAL:        " + formatearMoneda(factura.getTotal()));
        System.out.println(LINEA_TITULO);
    }

    public void mostrarHistorial(Duenio duenio, List<Factura> facturas, double totalGeneral) {

        System.out.println();
        System.out.println(LINEA_TITULO);
        System.out.println("         INFORME DE FACTURACIÓN POR CLIENTE");
        System.out.println(LINEA_TITULO);

        System.out.println("\n-- DATOS DEL CLIENTE --");
        System.out.printf("%-22s%s%n", "Nombre:", duenio.getNombreCompleto());
        System.out.printf("%-22s%s%n", "Documento:", duenio.getDocumentoIdentidad());

        System.out.println();
        System.out.println(LINEA);
        System.out.println("                 HISTORIAL DE FACTURAS");
        System.out.println(LINEA);

        System.out.println();
        System.out.printf(FORMATO_FILA_HISTORIAL, "ID Factura", "Fecha de Emisión", "Total Factura");
        System.out.println(LINEA);

        facturas.forEach(factura -> System.out.printf(FORMATO_FILA_HISTORIAL,
                factura.getId(),
                factura.getFechaEmision().format(FORMATO_FECHA),
                formatearMoneda(factura.getTotal())));

        System.out.println(LINEA);
        System.out.println(">> Monto Total General Facturado: " + formatearMoneda(totalGeneral));
        System.out.println(LINEA);
    }

    private static NumberFormat crearFormatoMoneda() {
        NumberFormat formato = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("es-CO"));
        formato.setMinimumFractionDigits(0);
        formato.setMaximumFractionDigits(0);
        return formato;
    }

    private String formatearMoneda(double valor) {
        return FORMATO_MONEDA.format(valor);
    }
}