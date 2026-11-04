package com.alangomez.gestionveterinariahappyfeet.view.inventario;

import com.alangomez.gestionveterinariahappyfeet.model.entities.inventario.MaterialMedico;
import com.alangomez.gestionveterinariahappyfeet.model.entities.inventario.Medicamento;
import com.alangomez.gestionveterinariahappyfeet.model.entities.inventario.Producto;
import com.alangomez.gestionveterinariahappyfeet.model.entities.inventario.Vacuna;
import com.alangomez.gestionveterinariahappyfeet.util.EntradaConsola;
import com.alangomez.gestionveterinariahappyfeet.util.TipoTexto;
import com.alangomez.gestionveterinariahappyfeet.view.VistaBase;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class VistaProductos extends VistaBase {

    private static final String FORMATO_FILA = "%-5s %-28s %-17s %-8s %-8s %-10s%n";

    public int menuProductos(){
        System.out.println("""

            ==============================
                       Productos
            ==============================
                1. Registrar Medicamento
                2. Registrar Vacuna
                3. Registrar Material médico
                4. Listar Productos
                5. Eliminar Producto
                0. Regresar
            """);
        return EntradaConsola.ingresarEnteroEnRango("Ingrese una opción", 0, 5);
    }

    public Producto pedirDatosProducto(Integer fabricanteId, Integer proveedorId){
        String nombre = EntradaConsola.ingresarTexto(TipoTexto.ALFANUMERICO, "Nombre del producto");
        String descripcion = null;
        if (EntradaConsola.confirmar("¿Desea agregar una descripción?")){
            descripcion = EntradaConsola.ingresarTexto(TipoTexto.ALFANUMERICO, "Descripción");
        }
        String unidad = EntradaConsola.ingresarTexto(TipoTexto.ALFANUMERICO, "Unidad de medida (unidad, frasco, caja...)");
        int stockMinimo = EntradaConsola.ingresarEnteroEnRango("Stock mínimo para alerta", 0, 100000);
        BigDecimal precioCompra = null;
        if (EntradaConsola.confirmar("¿Desea registrar el precio de compra?")){
            precioCompra = BigDecimal.valueOf(EntradaConsola.ingresarDecimalPositivo("Precio de compra"));
        }
        BigDecimal precioVenta = BigDecimal.valueOf(EntradaConsola.ingresarDecimalPositivo("Precio de venta"));
        boolean requiereReceta = EntradaConsola.confirmar("¿Requiere receta?");
        return new Producto(0, 0, fabricanteId, proveedorId, nombre, descripcion, unidad, stockMinimo,
                precioCompra, precioVenta, requiereReceta, true);
    }

    public Medicamento pedirDatosMedicamento(){
        String principioActivo = EntradaConsola.ingresarTexto(TipoTexto.ALFANUMERICO, "Principio activo");
        String presentacion = EntradaConsola.ingresarTexto(TipoTexto.ALFANUMERICO, "Presentación (tabletas, jarabe...)");
        String concentracion = EntradaConsola.ingresarTexto(TipoTexto.ALFANUMERICO, "Concentración (por ejemplo 250 mg)");
        String via = EntradaConsola.ingresarTexto(TipoTexto.ALFANUMERICO, "Vía de administración");
        return new Medicamento(principioActivo, presentacion, concentracion, via);
    }

    public Vacuna pedirDatosVacuna(int tipoVacunaId, Integer especieId){
        int dosis = EntradaConsola.ingresarEnteroEnRango("Número de dosis", 1, 10);
        Integer diasEntreDosis = null;
        if (EntradaConsola.confirmar("¿Tiene refuerzo periódico?")){
            diasEntreDosis = EntradaConsola.ingresarEnteroEnRango("Días entre dosis", 1, 3650);
        }
        return new Vacuna(tipoVacunaId, especieId, dosis, diasEntreDosis);
    }

    public MaterialMedico pedirDatosMaterial(){
        boolean desechable = EntradaConsola.confirmar("¿Es desechable?");
        boolean esteril = EntradaConsola.confirmar("¿Es estéril?");
        return new MaterialMedico(desechable, esteril);
    }

    public boolean deseaElegirFabricante(){
        return EntradaConsola.confirmar("¿Desea indicar el fabricante?");
    }

    public boolean deseaElegirProveedor(){
        return EntradaConsola.confirmar("¿Desea indicar el proveedor?");
    }

    public boolean deseaElegirEspecie(){
        return EntradaConsola.confirmar("¿La vacuna es para una especie en particular?");
    }

    public int pedirId(String accion){
        return EntradaConsola.ingresarEntero("Ingrese el id del producto a " + accion);
    }

    public void mostrarProductos(List<Producto> productos, Map<Integer, String> tipos, Map<Integer, Integer> stock){
        if (productos.isEmpty()){
            System.out.println("No hay productos registrados.");
            return;
        }
        System.out.printf(FORMATO_FILA, "ID", "NOMBRE", "TIPO", "STOCK", "MÍNIMO", "PRECIO");
        productos.forEach(p -> System.out.printf(FORMATO_FILA,
                p.getId(), p.getNombre(), tipos.getOrDefault(p.getTipoProductoId(), "-"),
                stock.getOrDefault(p.getId(), 0), p.getStockMinimo(), p.getPrecioVenta()));
    }
}
