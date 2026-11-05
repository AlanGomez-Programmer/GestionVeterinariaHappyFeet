package com.alangomez.gestionveterinariahappyfeet.model.dao.facturas;

import com.alangomez.gestionveterinariahappyfeet.model.dao.OperacionesDAO;
import com.alangomez.gestionveterinariahappyfeet.model.entities.facturas.Factura;
import com.alangomez.gestionveterinariahappyfeet.model.entities.facturas.FacturaBuilderImpl;
import com.alangomez.gestionveterinariahappyfeet.model.entities.servicios.Servicio;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class FacturaDAO extends OperacionesDAO {

    public boolean registrarFactura(Factura factura, int servicioId,
            String descripcion, int cantidad, double precioUnitario) throws SQLException {

        String sqlFactura = """
            INSERT INTO facturas (
                duenio_id, usuario_id, tipo_factura_id, metodo_pago_id,
                estado_id, numero_factura, fecha_emision,
                subtotal, impuesto, descuento, total, observaciones
            )
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;

        String sqlDetalle = """
            INSERT INTO items_factura (
                factura_id, tipo_item_id, inventario_id, servicio_id,
                descripcion, cantidad, precio_unitario, subtotal
            )
            VALUES (?, 2, NULL, ?, ?, ?, ?, ?)
            """;

        try (Connection con = obtenerConexion()) {

            con.setAutoCommit(false);

            try {
                int facturaId;

                try (PreparedStatement ps = con.prepareStatement(
                        sqlFactura, Statement.RETURN_GENERATED_KEYS)) {

                    ps.setInt(1, factura.getDuenioId());

                    if (factura.getUsuarioId() > 0) {
                        ps.setInt(2, factura.getUsuarioId());
                    } else {
                        ps.setNull(2, Types.INTEGER);
                    }

                    ps.setInt(3, factura.getTipoFacturaId());
                    ps.setInt(4, factura.getMetodoPagoId());
                    ps.setInt(5, factura.getEstadoId());
                    ps.setString(6, factura.getNumeroFactura());
                    ps.setObject(7, factura.getFechaEmision());
                    ps.setDouble(8, factura.getSubtotal());
                    ps.setDouble(9, factura.getImpuesto());
                    ps.setDouble(10, factura.getDescuento());
                    ps.setDouble(11, factura.getTotal());
                    ps.setString(12, factura.getObservaciones());

                    ps.executeUpdate();

                    try (ResultSet rs = ps.getGeneratedKeys()) {
                        if (!rs.next()) {
                            throw new SQLException("No se pudo obtener el ID de la factura");
                        }
                        facturaId = rs.getInt(1);
                    }
                }

                try (PreparedStatement ps = con.prepareStatement(sqlDetalle)) {

                    ps.setInt(1, facturaId);
                    ps.setInt(2, servicioId);
                    ps.setString(3, descripcion);
                    ps.setInt(4, cantidad);
                    ps.setDouble(5, precioUnitario);
                    ps.setDouble(6, cantidad * precioUnitario);

                    ps.executeUpdate();
                }

                con.commit();
                return true;

            } catch (SQLException e) {
                con.rollback();
                throw e;
            } finally {
                con.setAutoCommit(true);
            }
        }
    }

    public List<Servicio> listarServiciosDisponibles() throws SQLException {

        String sql = """
            SELECT id, tipo_servicio_id, nombre, descripcion, precio, activo
            FROM catalogo_servicios
            WHERE activo = TRUE
            ORDER BY nombre
            """;

        List<Servicio> servicios = new ArrayList<>();

        try (Connection con = obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                servicios.add(new Servicio(
                        rs.getInt("id"),
                        rs.getInt("tipo_servicio_id"),
                        rs.getString("nombre"),
                        rs.getString("descripcion"),
                        rs.getBigDecimal("precio"),
                        rs.getBoolean("activo")
                ));
            }
        }

        return servicios;
    }

    public Servicio buscarServicio(int id) throws SQLException {

        String sql = """
            SELECT id, tipo_servicio_id, nombre, descripcion, precio, activo
            FROM catalogo_servicios
            WHERE id = ? AND activo = TRUE
            """;

        try (Connection con = obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Servicio(
                            rs.getInt("id"),
                            rs.getInt("tipo_servicio_id"),
                            rs.getString("nombre"),
                            rs.getString("descripcion"),
                            rs.getBigDecimal("precio"),
                            rs.getBoolean("activo")
                    );
                }
            }
        }

        return null;
    }

    public boolean actualizarEstado(int facturaId, int estadoId) throws SQLException {

        String sql = """
            UPDATE facturas
            SET estado_id = ?
            WHERE id = ?
            """;

        try (Connection con = obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, estadoId);
            ps.setInt(2, facturaId);

            return ps.executeUpdate() == 1;
        }
    }

    public Factura buscarFactura(int id) throws SQLException {

        String sql = "SELECT * FROM facturas WHERE id = ?";

        try (Connection con = obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapear(rs) : null;
            }
        }
    }

    public List<Factura> listarFacturasPorDuenio(int duenioId) throws SQLException {

        String sql = """
            SELECT *
            FROM facturas
            WHERE duenio_id = ?
            ORDER BY fecha_emision
            """;

        List<Factura> facturas = new ArrayList<>();

        try (Connection con = obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, duenioId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    facturas.add(mapear(rs));
                }
            }
        }

        return facturas;
    }

    public List<String> listarDetalles(int facturaId) throws SQLException {

        String sql = """
            SELECT descripcion, cantidad, precio_unitario, subtotal
            FROM items_factura
            WHERE factura_id = ?
            """;

        List<String> detalles = new ArrayList<>();

        try (Connection con = obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, facturaId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    detalles.add(
                            rs.getString("descripcion")
                            + " | Cantidad: " + rs.getInt("cantidad")
                            + " | Precio: " + rs.getBigDecimal("precio_unitario")
                            + " | Subtotal: " + rs.getBigDecimal("subtotal")
                    );
                }
            }
        }

        return detalles;
    }

    public String buscarNombreEstado(int estadoId) throws SQLException {

        String sql = "SELECT nombre FROM estados_facturas WHERE id = ?";

        try (Connection con = obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, estadoId);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getString("nombre") : "Desconocido";
            }
        }
    }

    public int buscarIdCatalogo(String tabla, String nombre) throws SQLException {

        if (!List.of("tipos_facturas", "metodos_pago", "estados_facturas")
                .contains(tabla)) {
            throw new IllegalArgumentException("Catálogo no permitido");
        }

        String sql = "SELECT id FROM " + tabla + " WHERE nombre = ?";

        try (Connection con = obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, nombre);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
            }
        }

        throw new SQLException("No se encontró el registro: " + nombre);
    }

    private Factura mapear(ResultSet rs) throws SQLException {

        return new FacturaBuilderImpl()
                .id(rs.getInt("id"))
                .idDuenio(rs.getInt("duenio_id"))
                .idUsuario(rs.getInt("usuario_id"))
                .tipoFacturaId(rs.getInt("tipo_factura_id"))
                .metodoPagoId(rs.getInt("metodo_pago_id"))
                .estadoId(rs.getInt("estado_id"))
                .numeroFactura(rs.getString("numero_factura"))
                .fechaEmision(rs.getObject("fecha_emision", LocalDateTime.class))
                .subtotal(rs.getDouble("subtotal"))
                .impuesto(rs.getDouble("impuesto"))
                .descuento(rs.getDouble("descuento"))
                .total(rs.getDouble("total"))
                .observaciones(rs.getString("observaciones"))
                .build();
    }
}