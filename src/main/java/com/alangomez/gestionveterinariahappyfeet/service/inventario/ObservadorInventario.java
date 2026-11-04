package com.alangomez.gestionveterinariahappyfeet.service.inventario;

public interface ObservadorInventario {

    void alertaStockBajo(String producto, int stockDisponible, int stockMinimo);

}
