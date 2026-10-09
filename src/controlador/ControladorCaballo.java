package controlador;

import java.sql.SQLException;
import java.time.LocalDateTime;
import modelo.Caballo;
import modelo.CaballoDAO;
import modelo.Movimiento;
import modelo.MovimientoDAO;
import vista.CaballoDialog;

public class ControladorCaballo {

    private CaballoDialog vista;
    private CaballoDAO caballoDAO = new CaballoDAO();
    private MovimientoDAO movimientoDAO = new MovimientoDAO();
    private Caballo existente;
    private boolean guardado;

    public ControladorCaballo(CaballoDialog vista, Caballo existente) {
        this.vista = vista;
        this.existente = existente;
        if (existente != null) {
            vista.cargar(existente);
        }
        vista.getBtnGuardar().addActionListener(e -> guardar());
        vista.getBtnCancelar().addActionListener(e -> vista.dispose());
    }

    public boolean isGuardado() {
        return guardado;
    }

    private void guardar() {
        String error = validarCampos();
        if (error != null) {
            vista.mostrarMensaje(error);
            return;
        }
        Caballo caballo = crearCaballo();
        try {
            String pendientes = buscarCabalgatasPendientes(caballo);
            if (pendientes != null) {
                vista.mostrarMensaje(pendientes);
                return;
            }
            if (existente == null) {
                caballoDAO.insertar(caballo);
            } else {
                caballoDAO.modificar(caballo);
            }
        } catch (SQLException ex) {
            vista.mostrarMensaje("Error en la base de datos: " + ex.getMessage());
            return;
        }
        guardado = true;
        vista.dispose();
    }

    private String validarCampos() {
        if (vista.getNombre().isEmpty()) {
            return "El nombre es obligatorio";
        }
        try {
            if (Integer.parseInt(vista.getTextoEdad()) < 0) {
                return "La edad no puede ser negativa";
            }
        } catch (NumberFormatException ex) {
            return "La edad debe ser un numero entero";
        }
        return null;
    }

    private String buscarCabalgatasPendientes(Caballo caballo) throws SQLException {
        if (existente == null || "Disponible".equals(caballo.getEstado())) {
            return null;
        }
        for (Movimiento m : movimientoDAO.listarPorCaballo(existente.getId())) {
            if (m.getInicio().isAfter(LocalDateTime.now())) {
                return caballo.getNombre() + " tiene cabalgatas pendientes. Eliminalas o cambialas de caballo antes de ponerlo en descanso.";
            }
        }
        return null;
    }

    private Caballo crearCaballo() {
        Caballo caballo = new Caballo(vista.getNombre(), vista.getRaza(),
                Integer.parseInt(vista.getTextoEdad()), vista.getPelaje(), vista.getEstado());
        if (existente != null) {
            caballo.setId(existente.getId());
        }
        return caballo;
    }
}
