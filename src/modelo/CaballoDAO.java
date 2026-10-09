package modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CaballoDAO {

    private static final String SELECT = "SELECT id, nombre, raza, edad, pelaje, estado FROM caballo";

    public void insertar(Caballo c) throws SQLException {
        String sql = "INSERT INTO caballo (nombre, raza, edad, pelaje, estado) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = Conexion.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, c.getNombre());
            ps.setString(2, c.getRaza());
            ps.setInt(3, c.getEdad());
            ps.setString(4, c.getPelaje());
            ps.setString(5, c.getEstado());
            ps.executeUpdate();
        }
    }

    public void modificar(Caballo c) throws SQLException {
        String sql = "UPDATE caballo SET nombre = ?, raza = ?, edad = ?, pelaje = ?, estado = ? WHERE id = ?";
        try (Connection con = Conexion.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, c.getNombre());
            ps.setString(2, c.getRaza());
            ps.setInt(3, c.getEdad());
            ps.setString(4, c.getPelaje());
            ps.setString(5, c.getEstado());
            ps.setInt(6, c.getId());
            ps.executeUpdate();
        }
    }

    public void eliminar(int id) throws SQLException {
        String sql = "DELETE FROM caballo WHERE id = ?";
        try (Connection con = Conexion.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public List<Caballo> listarTodos() throws SQLException {
        return consultar(SELECT + " ORDER BY nombre");
    }

    public List<Caballo> buscar(String texto) throws SQLException {
        String patron = "%" + texto + "%";
        return consultar(SELECT + " WHERE nombre LIKE ? OR raza LIKE ? ORDER BY nombre", patron, patron);
    }

    private List<Caballo> consultar(String sql, String... parametros) throws SQLException {
        List<Caballo> lista = new ArrayList<>();
        try (Connection con = Conexion.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {
            for (int i = 0; i < parametros.length; i++) {
                ps.setString(i + 1, parametros[i]);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Caballo c = new Caballo(rs.getString("nombre"), rs.getString("raza"),
                            rs.getInt("edad"), rs.getString("pelaje"), rs.getString("estado"));
                    c.setId(rs.getInt("id"));
                    lista.add(c);
                }
            }
        }
        return lista;
    }
}
