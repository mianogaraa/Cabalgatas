package controlador;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import javax.swing.SwingUtilities;
import modelo.Fechas;
import modelo.Movimiento;
import modelo.MovimientoDAO;
import vista.VentanaPrincipal;

public class ControladorAgenda {

    private VentanaPrincipal vista;
    private MovimientoDAO movimientoDAO = new MovimientoDAO();
    private LocalDate dia = LocalDate.now();
    private boolean recargaPendiente;

    public ControladorAgenda(VentanaPrincipal vista) {
        this.vista = vista;
        vista.getBtnDiaAnterior().addActionListener(e -> cambiarDia(-1));
        vista.getBtnDiaSiguiente().addActionListener(e -> cambiarDia(1));
        vista.getBtnHoy().addActionListener(e -> irAHoy());
        vista.getTxtFechaAgenda().addActionListener(e -> irAFechaEscrita());
        vista.getModeloMovimientos().addTableModelListener(e -> programarRecarga());
        vista.getModeloCaballos().addTableModelListener(e -> programarRecarga());
        cargar();
    }

    private void cambiarDia(int cantidad) {
        dia = dia.plusDays(cantidad);
        cargar();
    }

    private void irAHoy() {
        dia = LocalDate.now();
        cargar();
    }

    private void irAFechaEscrita() {
        try {
            dia = Fechas.leer(vista.getTextoFechaAgenda());
        } catch (DateTimeParseException ex) {
            vista.mostrarMensaje("La fecha debe tener el formato DD-MM-AAAA (por ejemplo 25-10-2026)");
        }
        cargar();
    }

    private void programarRecarga() {
        if (recargaPendiente) {
            return;
        }
        recargaPendiente = true;
        SwingUtilities.invokeLater(() -> {
            recargaPendiente = false;
            cargar();
        });
    }

    private void cargar() {
        vista.setFechaAgenda(Fechas.aTexto(dia));
        vista.getModeloAgenda().setRowCount(0);
        try {
            List<Movimiento> cabalgatas = movimientoDAO.listarPorFecha(dia.toString());
            double total = 0;
            for (Movimiento m : cabalgatas) {
                vista.getModeloAgenda().addRow(new Object[]{
                    m.getHoraInicio(), m.getCaballo().getNombre(), m.getTipoRecorrido(), m.getCliente()});
                total += m.calcularPrecioFinal();
            }
            vista.setResumenAgenda(armarResumen(cabalgatas.size(), total));
        } catch (SQLException ex) {
            vista.mostrarMensaje("Error en la base de datos: " + ex.getMessage());
        }
    }

    private String armarResumen(int cantidad, double total) {
        String nombreDia = dia.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.forLanguageTag("es-AR"));
        nombreDia = nombreDia.substring(0, 1).toUpperCase() + nombreDia.substring(1);
        if (cantidad == 0) {
            return nombreDia + ": no hay cabalgatas este dia";
        }
        return nombreDia + ": " + cantidad + " cabalgata(s) - Total $" + String.format("%.0f", total);
    }
}
