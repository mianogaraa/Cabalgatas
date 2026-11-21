package modelo; // paquete (carpeta) donde esta esta clase: el modelo

import java.sql.Connection; // la conexion abierta con la base
import java.sql.DriverManager; // el "administrador" que abre conexiones a partir de una URL
import java.sql.SQLException; // error que puede tirar la base de datos
import java.sql.Statement; // para mandar ordenes SQL simples (sin parametros)

public class Conexion { // unico lugar donde se conoce la base de datos: abre la conexion y crea las tablas

    private static final String URL = "jdbc:sqlite:cabalgatas.db"; // direccion de la base: SQLite usando el archivo cabalgatas.db (se crea solo si no existe)

    public static Connection getConexion() throws SQLException { // devuelve una conexion lista para usar; static = se llama como Conexion.getConexion()
        Connection con = DriverManager.getConnection(URL); // abre la conexion con el archivo de la base
        try (Statement st = con.createStatement()) { // crea un Statement para mandar una orden; try-with-resources lo cierra solo
            st.execute("PRAGMA foreign_keys = ON;"); // SQLite trae las claves foraneas apagadas: esto las prende, asi la base no deja borrar un caballo con cabalgatas
        } // fin del try: se cierra el Statement, la conexion sigue abierta
        return con; // devuelve la conexion; quien la pide (los DAO) es responsable de cerrarla
    } // fin de getConexion

    public static void crearTablas() throws SQLException { // crea las dos tablas si todavia no existen; lo llama el main al arrancar
        try (Connection con = getConexion(); Statement st = con.createStatement()) { // abre la conexion y un Statement; se cierran solos al terminar
            st.execute("CREATE TABLE IF NOT EXISTS caballo (" // crea la tabla caballo solo si no existe (IF NOT EXISTS); asi se puede arrancar muchas veces
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT, " // id: numero entero, clave primaria (identifica la fila) y se suma solo
                    + "nombre TEXT NOT NULL, " // nombre: texto obligatorio
                    + "raza TEXT, " // raza: texto opcional
                    + "edad INTEGER, " // edad: numero entero opcional
                    + "pelaje TEXT, " // pelaje: texto opcional
                    + "estado TEXT)"); // estado: "Disponible" o "En descanso"; con el parentesis se cierra la lista de columnas
            st.execute("CREATE TABLE IF NOT EXISTS movimiento (" // crea la tabla movimiento (la cabalgata) solo si no existe
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT, " // id: clave primaria autoincremental
                    + "caballo_id INTEGER NOT NULL, " // id del caballo que hace la cabalgata: obligatorio; es la columna que enlaza con la tabla caballo
                    + "tipo_recorrido TEXT NOT NULL, " // "Diurno" o "Nocturno": con esto MovimientoDAO decide que clase hija crear
                    + "fecha TEXT NOT NULL, " // fecha guardada como texto AAAA-MM-DD (asi SQL la ordena bien)
                    + "hora_inicio TEXT NOT NULL, " // hora de inicio como texto, por ejemplo 09:30
                    + "cliente TEXT NOT NULL, " // nombre del cliente o del grupo
                    + "duracion REAL NOT NULL, " // duracion en horas; REAL = numero con decimales
                    + "precio REAL NOT NULL, " // precio por hora
                    + "FOREIGN KEY (caballo_id) REFERENCES caballo(id))"); // clave foranea: caballo_id tiene que ser un id que exista en caballo (relacion 1 a N); ultimo parentesis cierra el CREATE TABLE
        } // fin del try: se cierran la conexion y el Statement
    } // fin de crearTablas
} // fin de la clase
