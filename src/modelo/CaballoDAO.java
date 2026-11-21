package modelo;

import java.sql.Connection; 
import java.sql.PreparedStatement; 
import java.sql.ResultSet; 
import java.sql.SQLException; 
import java.util.ArrayList; 
import java.util.List; 

public class CaballoDAO { // DAO de caballos: la unica clase que escribe SQL sobre la tabla caballo

    private static final String SELECT = "SELECT id, nombre, raza, edad, pelaje, estado FROM caballo"; // parte comun de los SELECT; static final = constante compartida

    public void insertar(Caballo c) throws SQLException { // guarda un caballo nuevo; throws = puede fallar 
        String sql = "INSERT INTO caballo (nombre, raza, edad, pelaje, estado) VALUES (?, ?, ?, ?, ?)"; // orden SQL se guarda lo que se rellena dfespues 
        try (Connection con = Conexion.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) { // abre la conexion y prepara el SQL
            ps.setString(1, c.getNombre()); // rellena el hueco 1
            ps.setString(2, c.getRaza()); // hueco 2
            ps.setInt(3, c.getEdad()); // hueco 3
            ps.setString(4, c.getPelaje()); // hueco 4
            ps.setString(5, c.getEstado()); // hueco 5
            ps.executeUpdate(); // ejecuta el INSERT: escribe el caballo en la base (executeUpdate se usa para I, U y D)
        }
    } 

    public void modificar(Caballo c) throws SQLException { // actualiza un caballo que ya existe
        String sql = "UPDATE caballo SET nombre = ?, raza = ?, edad = ?, pelaje = ?, estado = ? WHERE id = ?"; // WHERE id = ? limita el cambio a esa fila; sin WHERE se modificarian todas
        try (Connection con = Conexion.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) { // abre la conexion y prepara el SQL; se cierran solos
            ps.setString(1, c.getNombre());
            ps.setString(2, c.getRaza());
            ps.setInt(3, c.getEdad());
            ps.setString(4, c.getPelaje()); 
            ps.setString(5, c.getEstado()); 
            ps.setInt(6, c.getId()); 
            ps.executeUpdate(); // ejecuta el UPDATE
        }
    } 

    public void eliminar(int id) throws SQLException { // borra el caballo con ese id
        String sql = "DELETE FROM caballo WHERE id = ?"; // borra solo la fila cuyo id coincide
        try (Connection con = Conexion.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) { // abre la conexion y prepara el SQL; se cierran solos
            ps.setInt(1, id); //id a borrar
            ps.executeUpdate(); // DELETE
        } 
    } 
    public List<Caballo> listarTodos() throws SQLException { // devuelve todos los caballos
        return consultar(SELECT + " ORDER BY nombre"); // usa el SELECT comun, ordenado por nombre, sin parametros
    } 

    public List<Caballo> buscar(String texto) throws SQLException { // caballos cuyo nombre o raza contienen el texto (buscador)
        String patron = "%" + texto + "%"; // los % significan "cualquier cosa antes y despues": busca el texto en cualquier parte
        return consultar(SELECT + " WHERE nombre LIKE ? OR raza LIKE ? ORDER BY nombre", patron, patron); // LIKE compara con el patron; se pasa dos veces: una por cada ?
    } 

    private List<Caballo> consultar(String sql, String... parametros) throws SQLException { // metodo comun a los SELECT; String... recibe 0 o mas parametros (varargs)
        List<Caballo> lista = new ArrayList<>(); // lista vacia donde se van agregando los caballos
        try (Connection con = Conexion.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) { // abre la conexion y prepara el SQL; se cierran solos
            for (int i = 0; i < parametros.length; i++) { // recorre los parametros recibidos con un indice i
                ps.setString(i + 1, parametros[i]); // rellena el hueco numero i+1 (los huecos de JDBC empiezan en 1, el arreglo en 0)
            } 
            try (ResultSet rs = ps.executeQuery()) { // ejecuta el SELECT y obtiene las filas; executeQuery se usa para SELECT
                while (rs.next()) { // pasa a la siguiente fila; devuelve false cuando no quedan mas
                    Caballo c = new Caballo(rs.getString("nombre"), rs.getString("raza"), // crea un Caballo leyendo las columnas por nombre: nombre y raza...
                            rs.getInt("edad"), rs.getString("pelaje"), rs.getString("estado")); // ...edad (entero), pelaje y estado
                    c.setId(rs.getInt("id")); // le pone el id que tiene en la base (el constructor no lo recibe)
                    lista.add(c); // agrega el caballo a la lista
                } 
            } 
        } 
        return lista; // devuelve la lista con todos los caballos encontrados
    } 
}
