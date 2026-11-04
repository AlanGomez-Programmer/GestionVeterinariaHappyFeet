package com.alangomez.gestionveterinariahappyfeet.service.pacientes;

import com.alangomez.gestionveterinariahappyfeet.model.dao.inventario.TipoVacunaDAO;
import com.alangomez.gestionveterinariahappyfeet.model.dao.pacientes.CatalogoAlergiaDAO;
import com.alangomez.gestionveterinariahappyfeet.model.dao.pacientes.EspecieDAO;
import com.alangomez.gestionveterinariahappyfeet.model.dao.pacientes.RazaDAO;
import com.alangomez.gestionveterinariahappyfeet.model.dao.pacientes.SexoDAO;
import com.alangomez.gestionveterinariahappyfeet.model.dao.pacientes.TipoAlergiaDAO;
import com.alangomez.gestionveterinariahappyfeet.model.dao.servicios.TipoProcedimientoEspecialDAO;
import com.alangomez.gestionveterinariahappyfeet.model.entities.inventario.TipoVacuna;
import com.alangomez.gestionveterinariahappyfeet.model.entities.pacientes.CatalogoAlergia;
import com.alangomez.gestionveterinariahappyfeet.model.entities.pacientes.Especie;
import com.alangomez.gestionveterinariahappyfeet.model.entities.pacientes.Raza;
import com.alangomez.gestionveterinariahappyfeet.model.entities.pacientes.Sexo;
import com.alangomez.gestionveterinariahappyfeet.model.entities.pacientes.TipoAlergia;
import com.alangomez.gestionveterinariahappyfeet.model.entities.servicios.TipoProcedimientoEspecial;
import java.sql.SQLException;
import java.util.List;

public class CatalogoPacientesService {

    private final EspecieDAO especieDAO;
    private final RazaDAO razaDAO;
    private final SexoDAO sexoDAO;
    private final TipoAlergiaDAO tipoAlergiaDAO;
    private final CatalogoAlergiaDAO catalogoAlergiaDAO;
    private final TipoVacunaDAO tipoVacunaDAO;
    private final TipoProcedimientoEspecialDAO tipoProcedimientoDAO;

    public CatalogoPacientesService(EspecieDAO especieDAO, RazaDAO razaDAO, SexoDAO sexoDAO,
            TipoAlergiaDAO tipoAlergiaDAO, CatalogoAlergiaDAO catalogoAlergiaDAO,
            TipoVacunaDAO tipoVacunaDAO, TipoProcedimientoEspecialDAO tipoProcedimientoDAO) {
        this.especieDAO = especieDAO;
        this.razaDAO = razaDAO;
        this.sexoDAO = sexoDAO;
        this.tipoAlergiaDAO = tipoAlergiaDAO;
        this.catalogoAlergiaDAO = catalogoAlergiaDAO;
        this.tipoVacunaDAO = tipoVacunaDAO;
        this.tipoProcedimientoDAO = tipoProcedimientoDAO;
    }

    public List<Especie> listarEspecies() throws SQLException {
        return especieDAO.listarEspecies();
    }

    public List<Raza> listarRazas() throws SQLException {
        return razaDAO.listarRazas();
    }

    public List<Raza> listarRazasPorEspecie(int especieId) throws SQLException {
        List<Raza> razas = razaDAO.listarRazasPorEspecie(especieId);
        if (razas.isEmpty()) {
            throw new IllegalArgumentException("La especie seleccionada no tiene razas registradas");
        }
        return razas;
    }

    public List<Sexo> listarSexos() throws SQLException {
        return sexoDAO.listarSexos();
    }

    public List<TipoAlergia> listarTiposAlergias() throws SQLException {
        return tipoAlergiaDAO.listarTiposAlergias();
    }

    public List<CatalogoAlergia> listarCatalogoAlergias() throws SQLException {
        return catalogoAlergiaDAO.listarCatalogoAlergias();
    }

    public List<TipoVacuna> listarTiposVacunas() throws SQLException {
        return tipoVacunaDAO.listarTiposVacunas();
    }

    public List<TipoProcedimientoEspecial> listarTiposProcedimientos() throws SQLException {
        return tipoProcedimientoDAO.listarTiposProcedimientos();
    }
}
