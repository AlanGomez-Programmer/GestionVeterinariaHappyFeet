package com.alangomez.gestionveterinariahappyfeet.view.inventario;

import com.alangomez.gestionveterinariahappyfeet.model.entities.inventario.Existencia;
import com.alangomez.gestionveterinariahappyfeet.model.entities.inventario.Lote;
import com.alangomez.gestionveterinariahappyfeet.model.entities.inventario.Producto;
import com.alangomez.gestionveterinariahappyfeet.service.inventario.ObservadorInventario;
import com.alangomez.gestionveterinariahappyfeet.util.EntradaConsola;
import com.alangomez.gestionveterinariahappyfeet.util.TipoTexto;
import com.alangomez.gestionveterinariahappyfeet.view.VistaBase;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class VistaExistencias extends VistaBase implements ObservadorInventario {

    private static final String FORMATO_FILA = "%-5s %-28s %-15s %-12s %-8s%n";

    public int menuExistencias(){
        System.out.println("""

            ==============================
                    Stock y Alertas
            ==============================
                1. Registrar entrada de stock
                2. Ver inventario por lote
                3. Ver alertas (stock bajo y vencimientos)
                4. Retirar productos vencidos
                0. Regresar
            """);
        return EntradaConsola.ingresarEnteroEnRango("Ingrese una opción", 0, 4);
    }

    public Lote pedirDatosLote(){
        String codigo = EntradaConsola.ingresarTexto(TipoTexto.ALFANUMERICO, "Código del lote");
        LocalDate fabricacion = null;
        if (EntradaConsola.confirmar("¿Conoce la fecha de fabricación?")){
            fabricacion = EntradaConsola.ingresarFechaNoFutura("Fecha de fabricación");
        }
        LocalDate vencimiento = EntradaConsola.ingresarFechaNoPasada("Fecha de vencimiento");
        return new Lote(codigo, fabricacion, vencimiento);
    }

    public boolean tieneLote(){
        return EntradaConsola.confirmar("¿El producto tiene lote y fecha de vencimiento?");
    }

    public int pedirDiasAviso(){
        return EntradaConsola.ingresarEnteroEnRango("¿Con cuántos días de anticipación avisar vencimientos?", 1, 365);
    }

    public void mostrarExistencias(String titulo, List<Existencia> existencias, Map<Integer, String> productos){
        System.out.println("\n---- " + titulo + " ----");
        if (existencias.isEmpty()){
            System.out.println("No hay registros.");
            return;
        }
        System.out.printf(FORMATO_FILA, "ID", "PRODUCTO", "LOTE", "VENCE", "STOCK");
        existencias.forEach(e -> System.out.printf(FORMATO_FILA, e.getId(),
                productos.getOrDefault(e.getProductoId(), "-"), valorOGuion(e.getCodigoLote()),
                formatearFecha(e.getFechaVencimiento()), e.getCantidadStock()));
    }

    public void mostrarStockBajo(List<Producto> productos, Map<Integer, Integer> stock){
        System.out.println("\n---- Productos con stock bajo ----");
        if (productos.isEmpty()){
            System.out.println("Todos los productos están por encima de su stock mínimo.");
            return;
        }
        productos.forEach(p -> System.out.println("- " + p.getNombre() + ": " + stock.getOrDefault(p.getId(), 0)
                + " disponibles (mínimo " + p.getStockMinimo() + ")"));
    }

    @Override
    public void alertaStockBajo(String producto, int stockDisponible, int stockMinimo){
        System.out.println("*** ALERTA: " + producto + " quedó con " + stockDisponible
                + " unidades (stock mínimo " + stockMinimo + "). Es necesario reabastecer ***");
    }
}
