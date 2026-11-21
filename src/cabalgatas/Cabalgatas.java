package cabalgatas; // paquete donde esta la clase principal: cabalgatas

import controlador.ControladorAgenda; // ControladorAgenda: controlador del recuadro de agenda del dia
import controlador.ControladorPrincipal; // ControladorPrincipal: controlador de la ventana principal (botones y tablas)
import javax.swing.JOptionPane; // JOptionPane: cuadritos de mensaje listos para usar
import javax.swing.SwingUtilities; // SwingUtilities: herramientas del hilo de Swing; aca usamos invokeLater
import modelo.Conexion; // Conexion: clase del modelo que se conecta a SQLite y crea las tablas
import vista.VentanaPrincipal; // VentanaPrincipal: la ventana principal (la vista del programa)

public class Cabalgatas { // clase de arranque del programa; tiene el main, no tiene logica de negocio

    public static void main(String[] args) { // main: punto de entrada, es donde Java empieza a ejecutar; static porque corre sin crear ningun objeto
        try { // try: intenta ejecutar el codigo que sigue, que puede fallar
            Conexion.crearTablas(); // crea las tablas de la base de datos si todavia no existen
        } catch (Exception ex) { // catch: si algo fallo, atrapa el error en ex y ejecuta este bloque en vez de cortar el programa
            JOptionPane.showMessageDialog(null, "No se pudo preparar la base de datos: " + ex.getMessage()); // muestra un cuadro de error (null = sin ventana padre) con el motivo del fallo
            return; // sale del main: sin base de datos no tiene sentido abrir la ventana
        }
        SwingUtilities.invokeLater(() -> { // invokeLater: pone la tarea en la cola de Swing para crear la interfaz en el hilo grafico; () -> es una lambda
            VentanaPrincipal vista = new VentanaPrincipal(); // crea la vista: la ventana principal (todavia no se ve)
            new ControladorPrincipal(vista); // crea el primer controlador, pasandole la vista; en su constructor engancha los listeners a los botones
            new ControladorAgenda(vista); // crea el segundo controlador (agenda del dia) con la misma vista
            vista.setVisible(true); // ahora si muestra la ventana en pantalla
        });
    }
} 