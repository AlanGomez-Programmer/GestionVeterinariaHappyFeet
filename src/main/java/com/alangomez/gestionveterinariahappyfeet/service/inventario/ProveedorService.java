package com.alangomez.gestionveterinariahappyfeet.service.inventario;

import com.alangomez.gestionveterinariahappyfeet.model.dao.inventario.ProveedorDAO;
import com.alangomez.gestionveterinariahappyfeet.model.entities.inventario.Proveedor;
import java.sql.SQLException;
import java.util.List;

public class ProveedorService {

    private final ProveedorDAO proveedorDAO;

    public ProveedorService(ProveedorDAO proveedorDAO) {
        this.proveedorDAO = proveedorDAO;
    }

    public void registrar(Proveedor proveedor) throws SQLException {
        boolean repetido = proveedorDAO.listarProveedores().stream()
                .anyMatch(p -> p.getNombreEmpresa().equalsIgnoreCase(proveedor.getNombreEmpresa()));
        if (repetido) {
            throw new IllegalArgumentException("Ya existe el proveedor " + proveedor.getNombreEmpresa());
        }
        proveedorDAO.agregarProveedor(proveedor);
    }

    public List<Proveedor> listarActivos() throws SQLException {
        return proveedorDAO.listarProveedores().stream()
                .filter(Proveedor::isActivo)
                .toList();
    }
}
