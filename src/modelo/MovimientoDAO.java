package modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MovimientoDAO {

    private static final String SELECT_BASE = "SELECT m.id, m.caballo_id, m.tipo_recorrido, m.fecha, m.hora_inicio, m.cliente, m.duracion, m.precio, "
            + "c.nombre, c.raza, c.edad, c.pelaje, c.estado "
            + "FROM movimiento m JOIN caballo c ON m.caballo_id = c.id";

    private static final String INSERT = "INSERT INTO movimiento (caballo_id, tipo_recorrido, fecha, hora_inicio, cliente, duracion, precio) VALUES (?, ?, ?, ?, ?, ?, ?)";

    public void insertar(Movimiento m) throws SQLException {
        try (Connection con = Conexion.getConexion(); PreparedStatement ps = con.prepareStatement(INSERT)) {
            cargarParametros(ps, m);
            ps.executeUpdate();
        }
    }

    public void insertarVarios(List<Movimiento> movimientos) throws SQLException {
        try (Connection con = Conexion.getConexion()) {
            con.setAutoCommit(false);
            try (PreparedStatement ps = con.prepareStatement(INSERT)) {
                for (Movimiento m : movimientos) {
                    cargarParametros(ps, m);
                    ps.addBatch();
                }
                ps.executeBatch();
                con.commit();
            } catch (SQLException ex) {
                con.rollback();
                throw ex;
            }
        }
    }

    public void modificar(Movimiento m) throws SQLException {
        String sql = "UPDATE movimiento SET caballo_id = ?, tipo_recorrido = ?, fecha = ?, hora_inicio = ?, cliente = ?, duracion = ?, precio = ? WHERE id = ?";
        try (Connection con = Conexion.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {
            cargarParametros(ps, m);
            ps.setInt(8, m.getId());
            ps.executeUpdate();
        }
    }

    public void eliminar(int id) throws SQLException {
        String sql = "DELETE FROM movimiento WHERE id = ?";
        try (Connection con = Conexion.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public List<Movimiento> listarTodos() throws SQLException {
        return consultar(SELECT_BASE + " ORDER BY m.fecha, m.hora_inicio", null);
    }

    public List<Movimiento> listarPorCaballo(int caballoId) throws SQLException {
        return consultar(SELECT_BASE + " WHERE m.caballo_id = ? ORDER BY m.fecha, m.hora_inicio", caballoId);
    }

    public List<Movimiento> listarPorFecha(String fecha) throws SQLException {
        return consultar(SELECT_BASE + " WHERE m.fecha = ? ORDER BY m.hora_inicio", fecha);
    }

    private void cargarParametros(PreparedStatement ps, Movimiento m) throws SQLException {
        ps.setInt(1, m.getCaballo().getId());
        ps.setString(2, m.getTipoRecorrido());
        ps.setString(3, m.getFecha());
        ps.setString(4, m.getHoraInicio());
        ps.setString(5, m.getCliente());
        ps.setDouble(6, m.getDuracion());
        ps.setDouble(7, m.getPrecio());
    }

    private List<Movimiento> consultar(String sql, Object parametro) throws SQLException {
        List<Movimiento> lista = new ArrayList<>();
        try (Connection con = Conexion.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {
            if (parametro != null) {
                ps.setObject(1, parametro);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapear(rs));
                }
            }
        }
        return lista;
    }

    private Movimiento mapear(ResultSet rs) throws SQLException {
        Caballo c = new Caballo(rs.getString("nombre"), rs.getString("raza"), rs.getInt("edad"), rs.getString("pelaje"), rs.getString("estado"));
        c.setId(rs.getInt("caballo_id"));

        String tipo = rs.getString("tipo_recorrido");
        String fecha = rs.getString("fecha");
        String horaInicio = rs.getString("hora_inicio");
        String cliente = rs.getString("cliente");
        double duracion = rs.getDouble("duracion");
        double precio = rs.getDouble("precio");

        Movimiento m;
        if (tipo.equals("Nocturno")) {
            m = new MovimientoNocturno(c, fecha, horaInicio, cliente, duracion, precio);
        } else {
            m = new MovimientoDiurno(c, fecha, horaInicio, cliente, duracion, precio);
        }
        m.setId(rs.getInt("id"));
        return m;
    }
}
