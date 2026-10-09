package controlador;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import modelo.Caballo;
import modelo.Fechas;
import modelo.Movimiento;
import modelo.MovimientoDAO;
import modelo.MovimientoDiurno;
import modelo.MovimientoNocturno;
import vista.MovimientoDialog;

public class ControladorMovimiento {

    private static final LocalTime INICIO_DIA = LocalTime.of(8, 0);
    private static final LocalTime INICIO_NOCHE = LocalTime.of(19, 0);

    private MovimientoDialog vista;
    private MovimientoDAO movimientoDAO = new MovimientoDAO();
    private Movimiento existente;
    private Movimiento guardado;

    public ControladorMovimiento(MovimientoDialog vista, Movimiento existente) {
        this.vista = vista;
        this.existente = existente;
        if (existente != null) {
            vista.cargar(existente);
        }
        vista.getBtnGuardar().addActionListener(e -> guardar());
        vista.getBtnCancelar().addActionListener(e -> vista.dispose());
    }

    public Movimiento getGuardado() {
        return guardado;
    }

    private void guardar() {
        String error = validarCampos();
        if (error != null) {
            vista.mostrarMensaje(error);
            return;
        }
        Movimiento movimiento = crearMovimiento();
        error = validarHorario(movimiento);
        if (error != null) {
            vista.mostrarMensaje(error);
            return;
        }
        try {
            error = buscarSuperposicion(movimiento);
            if (error != null) {
                vista.mostrarMensaje(error);
                return;
            }
            if (existente == null) {
                movimientoDAO.insertar(movimiento);
            } else {
                movimientoDAO.modificar(movimiento);
            }
        } catch (SQLException ex) {
            vista.mostrarMensaje("Error en la base de datos: " + ex.getMessage());
            return;
        }
        guardado = movimiento;
        vista.dispose();
    }

    private String validarCampos() {
        Caballo caballo = vista.getCaballo();
        if (caballo == null) {
            return "Elegi un caballo";
        }
        if (existente == null && !"Disponible".equals(caballo.getEstado())) {
            return caballo.getNombre() + " esta en descanso, no se le pueden agendar cabalgatas nuevas";
        }
        try {
            Fechas.leer(vista.getFecha());
        } catch (DateTimeParseException ex) {
            return "La fecha debe tener el formato DD-MM-AAAA (por ejemplo 25-10-2026)";
        }
        try {
            LocalTime.parse(vista.getHora());
        } catch (DateTimeParseException ex) {
            return "La hora debe tener el formato HH:mm (por ejemplo 09:30)";
        }
        if (vista.getCliente().isEmpty()) {
            return "El cliente es obligatorio";
        }
        Double duracion = leerNumero(vista.getTextoDuracion());
        if (duracion == null || duracion <= 0) {
            return "La duracion debe ser un numero mayor a 0";
        }
        Double precio = leerNumero(vista.getTextoPrecio());
        if (precio == null || precio <= 0) {
            return "El precio debe ser un numero mayor a 0";
        }
        return null;
    }

    private String validarHorario(Movimiento m) {
        LocalDate dia = m.getInicio().toLocalDate();
        LocalTime horaInicio = m.getInicio().toLocalTime();

        if ("Nocturno".equals(m.getTipoRecorrido())) {
            LocalDateTime medianoche = dia.plusDays(1).atStartOfDay();
            if (horaInicio.isBefore(INICIO_NOCHE) || m.getFin().isAfter(medianoche)) {
                return "Las cabalgatas nocturnas son de 19:00 a 00:00: tienen que empezar desde las 19:00 y terminar como maximo a medianoche. Para otro horario elegi el tipo Diurno.";
            }
            return null;
        }

        LocalDateTime cierreDia = dia.atTime(INICIO_NOCHE);
        if (horaInicio.isBefore(INICIO_DIA) || m.getFin().isAfter(cierreDia)) {
            return "Las cabalgatas diurnas son de 08:00 a 19:00: tienen que empezar desde las 08:00 y terminar como maximo a las 19:00. Para otro horario elegi el tipo Nocturno.";
        }
        return null;
    }

    private String buscarSuperposicion(Movimiento nuevo) throws SQLException {
        for (Movimiento otro : movimientoDAO.listarPorCaballo(nuevo.getCaballo().getId())) {
            if (existente != null && otro.getId() == existente.getId()) {
                continue;
            }
            if (nuevo.seSuperponeCon(otro)) {
                return nuevo.getCaballo().getNombre() + " ya tiene una cabalgata el " + Fechas.aTexto(otro.getFecha())
                        + " de " + otro.getInicio().toLocalTime() + " a " + otro.getFin().toLocalTime() + ".";
            }
        }
        return null;
    }

    private Movimiento crearMovimiento() {
        Caballo caballo = vista.getCaballo();
        String fecha = Fechas.aBase(vista.getFecha());
        double duracion = leerNumero(vista.getTextoDuracion());
        double precio = leerNumero(vista.getTextoPrecio());
        Movimiento m;
        if ("Nocturno".equals(vista.getTipo())) {
            m = new MovimientoNocturno(caballo, fecha, vista.getHora(), vista.getCliente(), duracion, precio);
        } else {
            m = new MovimientoDiurno(caballo, fecha, vista.getHora(), vista.getCliente(), duracion, precio);
        }
        if (existente != null) {
            m.setId(existente.getId());
        }
        return m;
    }

    private Double leerNumero(String texto) {
        try {
            return Double.parseDouble(texto.replace(',', '.'));
        } catch (NumberFormatException ex) {
            return null;
        }
    }
}
