package cabalgatas;

import controlador.ControladorAgenda;
import controlador.ControladorPrincipal;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import modelo.Conexion;
import vista.VentanaPrincipal;

public class Cabalgatas {

    public static void main(String[] args) {
        try {
            Conexion.crearTablas();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "No se pudo preparar la base de datos: " + ex.getMessage());
            return;
        }
        SwingUtilities.invokeLater(() -> {
            VentanaPrincipal vista = new VentanaPrincipal();
            new ControladorPrincipal(vista);
            new ControladorAgenda(vista);
            vista.setVisible(true);
        });
    }
}
