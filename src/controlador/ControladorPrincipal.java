package controlador;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.SQLException;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import modelo.Caballo;
import modelo.CaballoDAO;
import modelo.Fechas;
import modelo.Movimiento;
import modelo.MovimientoDAO;
import vista.CaballoDialog;
import vista.MovimientoDialog;
import vista.VentanaPrincipal;

public class ControladorPrincipal {

    private VentanaPrincipal vista;
    private CaballoDAO caballoDAO = new CaballoDAO();
    private MovimientoDAO movimientoDAO = new MovimientoDAO();
    private List<Caballo> todosLosCaballos;
    private List<Caballo> caballos;
    private List<Movimiento> movimientos;

    public ControladorPrincipal(VentanaPrincipal vista) {
        this.vista = vista;
        vista.getTablaCaballos().getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                cargarMovimientos();
            }
        });
        vista.getTxtBusqueda().addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                cargarCaballos();
            }
        });
        vista.getBtnAgregarCaballo().addActionListener(e -> agregarCaballo());
        vista.getBtnModificarCaballo().addActionListener(e -> modificarCaballo());
        vista.getBtnEliminarCaballo().addActionListener(e -> eliminarCaballo());
        vista.getBtnAgregarMovimiento().addActionListener(e -> agregarMovimiento());
        vista.getBtnModificarMovimiento().addActionListener(e -> modificarMovimiento());
        vista.getBtnEliminarMovimiento().addActionListener(e -> eliminarMovimiento());
        cargarCaballos();
    }

    private void cargarCaballos() {
        String texto = vista.getTextoBusqueda();
        try {
            todosLosCaballos = caballoDAO.listarTodos();
            caballos = texto.isEmpty() ? todosLosCaballos : caballoDAO.buscar(texto);
        } catch (SQLException ex) {
            mostrarError(ex);
            return;
        }
        DefaultTableModel modelo = vista.getModeloCaballos();
        modelo.setRowCount(0);
        for (Caballo c : caballos) {
            modelo.addRow(new Object[]{c.getId(), c.getNombre(), c.getRaza(), c.getEdad(), c.getPelaje(), c.getEstado()});
        }
        cargarMovimientos();
    }

    private void cargarMovimientos() {
        DefaultTableModel modelo = vista.getModeloMovimientos();
        modelo.setRowCount(0);
        Caballo seleccionado = getCaballoSeleccionado();
        if (seleccionado == null) {
            return;
        }
        try {
            movimientos = movimientoDAO.listarPorCaballo(seleccionado.getId());
        } catch (SQLException ex) {
            mostrarError(ex);
            return;
        }
        for (Movimiento m : movimientos) {
            modelo.addRow(new Object[]{m.getId(), m.getTipoRecorrido(), Fechas.aTexto(m.getFecha()), m.getHoraInicio(), m.getCliente(),
                m.getDuracion(), m.getPrecio(), m.calcularPrecioFinal()});
        }
    }

    private void agregarCaballo() {
        CaballoDialog dialogo = new CaballoDialog(vista, "Nuevo caballo");
        ControladorCaballo controlador = new ControladorCaballo(dialogo, null);
        dialogo.setVisible(true);
        if (controlador.isGuardado()) {
            cargarCaballos();
        }
    }

    private void modificarCaballo() {
        Caballo seleccionado = getCaballoSeleccionado();
        if (seleccionado == null) {
            avisar("Elegi un caballo de la tabla");
            return;
        }
        CaballoDialog dialogo = new CaballoDialog(vista, "Modificar caballo");
        ControladorCaballo controlador = new ControladorCaballo(dialogo, seleccionado);
        dialogo.setVisible(true);
        if (controlador.isGuardado()) {
            cargarCaballos();
        }
    }

    private void eliminarCaballo() {
        Caballo seleccionado = getCaballoSeleccionado();
        if (seleccionado == null) {
            avisar("Elegi un caballo de la tabla");
            return;
        }
        try {
            if (!movimientoDAO.listarPorCaballo(seleccionado.getId()).isEmpty()) {
                avisar("No se puede eliminar: el caballo tiene cabalgatas registradas. Eliminalas primero.");
                return;
            }
            if (!confirmar("Eliminar a " + seleccionado.getNombre() + "?")) {
                return;
            }
            caballoDAO.eliminar(seleccionado.getId());
        } catch (SQLException ex) {
            mostrarError(ex);
            return;
        }
        cargarCaballos();
    }

    private void agregarMovimiento() {
        if (todosLosCaballos.isEmpty()) {
            avisar("Primero carga un caballo");
            return;
        }
        MovimientoDialog dialogo = new MovimientoDialog(vista, "Nueva cabalgata", todosLosCaballos);
        ControladorMovimiento controlador = new ControladorMovimiento(dialogo, null);
        dialogo.setVisible(true);
        mostrarResultado(controlador.getGuardado());
    }

    private void modificarMovimiento() {
        Movimiento seleccionado = getMovimientoSeleccionado();
        if (seleccionado == null) {
            avisar("Elegi una cabalgata de la tabla de abajo");
            return;
        }
        MovimientoDialog dialogo = new MovimientoDialog(vista, "Modificar cabalgata", todosLosCaballos);
        ControladorMovimiento controlador = new ControladorMovimiento(dialogo, seleccionado);
        dialogo.setVisible(true);
        mostrarResultado(controlador.getGuardado());
    }

    private void eliminarMovimiento() {
        Movimiento seleccionado = getMovimientoSeleccionado();
        if (seleccionado == null) {
            avisar("Elegi una cabalgata de la tabla de abajo");
            return;
        }
        if (!confirmar("Eliminar la cabalgata de " + seleccionado.getCliente() + "?")) {
            return;
        }
        try {
            movimientoDAO.eliminar(seleccionado.getId());
        } catch (SQLException ex) {
            mostrarError(ex);
            return;
        }
        cargarMovimientos();
    }

    private void mostrarResultado(Movimiento guardado) {
        if (guardado == null) {
            return;
        }
        int id = guardado.getCaballo().getId();
        int fila = buscarFila(id);
        if (fila < 0) {
            vista.setTextoBusqueda("");
            cargarCaballos();
            fila = buscarFila(id);
        }
        if (fila >= 0) {
            vista.getTablaCaballos().setRowSelectionInterval(fila, fila);
        }
        cargarMovimientos();
    }

    private int buscarFila(int caballoId) {
        for (int i = 0; i < caballos.size(); i++) {
            if (caballos.get(i).getId() == caballoId) {
                return i;
            }
        }
        return -1;
    }

    private Caballo getCaballoSeleccionado() {
        int fila = vista.getTablaCaballos().getSelectedRow();
        if (fila < 0) {
            return null;
        }
        return caballos.get(fila);
    }

    private Movimiento getMovimientoSeleccionado() {
        int fila = vista.getTablaMovimientos().getSelectedRow();
        if (fila < 0) {
            return null;
        }
        return movimientos.get(fila);
    }

    private void avisar(String mensaje) {
        JOptionPane.showMessageDialog(vista, mensaje);
    }

    private boolean confirmar(String mensaje) {
        int respuesta = JOptionPane.showConfirmDialog(vista, mensaje, "Confirmar", JOptionPane.YES_NO_OPTION);
        return respuesta == JOptionPane.YES_OPTION;
    }

    private void mostrarError(SQLException ex) {
        JOptionPane.showMessageDialog(vista, "Error en la base de datos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}
