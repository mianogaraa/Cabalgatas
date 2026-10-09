package modelo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Conexion {

    private static final String URL = "jdbc:sqlite:cabalgatas.db";

    public static Connection getConexion() throws SQLException {
        Connection con = DriverManager.getConnection(URL);
        try (Statement st = con.createStatement()) {
            st.execute("PRAGMA foreign_keys = ON;");
        }
        return con;
    }

    public static void crearTablas() throws SQLException {
        try (Connection con = getConexion(); Statement st = con.createStatement()) {
            st.execute("CREATE TABLE IF NOT EXISTS caballo ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "nombre TEXT NOT NULL, "
                    + "raza TEXT, "
                    + "edad INTEGER, "
                    + "pelaje TEXT, "
                    + "estado TEXT)");
            st.execute("CREATE TABLE IF NOT EXISTS movimiento ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "caballo_id INTEGER NOT NULL, "
                    + "tipo_recorrido TEXT NOT NULL, "
                    + "fecha TEXT NOT NULL, "
                    + "hora_inicio TEXT NOT NULL, "
                    + "cliente TEXT NOT NULL, "
                    + "duracion REAL NOT NULL, "
                    + "precio REAL NOT NULL, "
                    + "FOREIGN KEY (caballo_id) REFERENCES caballo(id))");
        }
    }
}
