package com.alangomez.gestionveterinariahappyfeet.service.inventario;

import com.alangomez.gestionveterinariahappyfeet.model.dao.inventario.FabricanteDAO;
import com.alangomez.gestionveterinariahappyfeet.model.dao.inventario.TipoProductoDAO;
import com.alangomez.gestionveterinariahappyfeet.model.dao.inventario.TipoVacunaDAO;
import com.alangomez.gestionveterinariahappyfeet.model.entities.inventario.Fabricante;
import com.alangomez.gestionveterinariahappyfeet.model.entities.inventario.TipoProducto;
import com.alangomez.gestionveterinariahappyfeet.model.entities.inventario.TipoVacuna;
import java.sql.SQLException;
import java.util.List;

public class CatalogoInventarioService {

    private final TipoProductoDAO tipoProductoDAO;
    private final TipoVacunaDAO tipoVacunaDAO;
    private final FabricanteDAO fabricanteDAO;

    public CatalogoInventarioService(TipoProductoDAO tipoProductoDAO, TipoVacunaDAO tipoVacunaDAO,
            FabricanteDAO fabricanteDAO) {
        this.tipoProductoDAO = tipoProductoDAO;
        this.tipoVacunaDAO = tipoVacunaDAO;
        this.fabricanteDAO = fabricanteDAO;
    }

    public List<TipoProducto> listarTiposProductos() throws SQLException {
        return tipoProductoDAO.listarTiposProductos();
    }

    public List<TipoVacuna> listarTiposVacunas() throws SQLException {
        return tipoVacunaDAO.listarTiposVacunas();
    }

    public List<Fabricante> listarFabricantes() throws SQLException {
        return fabricanteDAO.listarFabricantes();
    }

    public void registrarFabricante(Fabricante fabricante) throws SQLException {
        if (fabricanteDAO.existeFabricante(fabricante.getNombre())) {
            throw new IllegalArgumentException("Ya existe el fabricante " + fabricante.getNombre());
        }
        fabricanteDAO.agregarFabricante(fabricante);
    }
}
