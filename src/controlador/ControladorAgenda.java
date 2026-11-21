package controlador; 

import java.sql.SQLException; // error que puede tirar la base de datos
import java.time.LocalDate; // fecha sin hora (dia, mes, anio)
import java.time.format.DateTimeParseException; // error cuando un texto no se puede convertir en fecha
import java.time.format.TextStyle; // define como se escribe el nombre del dia
import java.util.List; // lista de objetos
import java.util.Locale;
import javax.swing.SwingUtilities; // herramientas del hilo de Swing
import modelo.Fechas; // traductor de fechas entre texto y LocalDate
import modelo.Movimiento; // la cabalgata
import modelo.MovimientoDAO; // clase que consulta las cabalgatas en la base
import vista.VentanaPrincipal; // la ventana que este controlador maneja

public class ControladorAgenda { // controlador del recuadro "Agenda del dia"

    private VentanaPrincipal vista; // la ventana: de aca leo botones y en ella escribo
    private MovimientoDAO movimientoDAO = new MovimientoDAO(); // DAO para pedir cabalgatas a la base
    private LocalDate dia = LocalDate.now(); // dia que muestra la agenda; arranca en hoy
    private boolean recargaPendiente; // bandera (false al inicio): indica si ya hay una recarga agendada

    public ControladorAgenda(VentanaPrincipal vista) { // constructor: lo llama Cabalgatas.java con la ventana
        this.vista = vista; // guarda la ventana recibida en el atributo
        vista.getBtnDiaAnterior().addActionListener(e -> cambiarDia(-1)); // boton "<": al apretarlo resta un dia
        vista.getBtnDiaSiguiente().addActionListener(e -> cambiarDia(1)); // boton ">": al apretarlo suma un dia
        vista.getBtnHoy().addActionListener(e -> irAHoy()); // boton "Hoy": vuelve a la fecha actual
        vista.getTxtFechaAgenda().addActionListener(e -> irAFechaEscrita()); // al apretar Enter en el campo de fecha, va a esa fecha
        vista.getModeloMovimientos().addTableModelListener(e -> programarRecarga()); // si cambian los datos de la tabla de cabalgatas, programa recarga
        vista.getModeloCaballos().addTableModelListener(e -> programarRecarga()); // lo mismo si cambia la tabla de caballos
        cargar(); // muestra la agenda por primera vez
    }

    private void cambiarDia(int cantidad) { // recibe -1 (dia anterior) o 1 (dia siguiente)
        dia = dia.plusDays(cantidad); // suma (o resta) esa cantidad de dias a la fecha
        cargar(); // vuelve a mostrar la agenda con el dia nuevo
    } 

    private void irAHoy() { // metodo del boton "Hoy"
        dia = LocalDate.now(); // pone la fecha actual
        cargar(); // muestra la agenda de hoy
    } 

    private void irAFechaEscrita() { // metodo del campo de fecha
        try { // intenta convertir el texto, que puede estar mal escrito
            dia = Fechas.leer(vista.getTextoFechaAgenda()); // convierte el texto DD-MM-AAAA en fecha
        } catch (DateTimeParseException ex) { // si el texto no es una fecha valida
            vista.mostrarMensaje("La fecha debe tener el formato DD-MM-AAAA (por ejemplo 25-10-2026)"); // avisa el formato correcto
        } 
        cargar(); // recarga igual; si fallo, dia no cambio y el campo vuelve a la fecha correcta
    } 

    private void programarRecarga() { // se llama cada vez que cambia algun dato de las otras tablas
        if (recargaPendiente) { // si ya hay una recarga agendada...
            return; // ...sale: no hace falta agendar otra
        } // fin del if
        recargaPendiente = true; // anota que ahora hay una recarga agendada
        SwingUtilities.invokeLater(() -> { // pone la tarea en la cola de Swing: corre despues de que terminen los cambios
            recargaPendiente = false; // baja la bandera: ya se puede agendar otra recarga
            cargar(); // recarga la agenda una sola vez
        }); 
    } 

    private void cargar() { // metodo principal: arma todo el recuadro de la agenda
        vista.setFechaAgenda(Fechas.aTexto(dia)); // escribe la fecha en el campo, en formato DD-MM-AAAA
        vista.getModeloAgenda().setRowCount(0); // borra las filas de la tabla para empezar de cero
        try { 
            List<Movimiento> cabalgatas = movimientoDAO.listarPorFecha(dia.toString()); // pide al DAO las cabalgatas de ese dia (AAAA-MM-DD, como en la base)
            double total = 0; // acumulador del total en plata
            for (Movimiento m : cabalgatas) { // recorre las cabalgatas, una por vuelta
                vista.getModeloAgenda().addRow(new Object[]{ // agrega una fila a la tabla de agenda
                    m.getHoraInicio(), m.getCaballo().getNombre(), m.getTipoRecorrido(), m.getCliente()}); // columnas: hora, caballo, tipo y cliente
                total += m.calcularPrecioFinal(); // suma el precio final (polimorfismo: diurna o nocturna calculan distinto)
            } 
            vista.setResumenAgenda(armarResumen(cabalgatas.size(), total)); // muestra el texto resumen con cantidad y total
        } catch (SQLException ex) { 
            vista.mostrarMensaje("Error en la base de datos: " + ex.getMessage()); // muestra el error
        } 
    } 

    private String armarResumen(int cantidad, double total) { // devuelve el texto que va debajo de la tabla
        String nombreDia = dia.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.forLanguageTag("es-AR")); // dia de la semana, completo y en espanol ("lunes")
        nombreDia = nombreDia.substring(0, 1).toUpperCase() + nombreDia.substring(1); // primera letra en mayuscula ("Lunes")
        if (cantidad == 0) { // si ese dia no hay cabalgatas...
            return nombreDia + ": no hay cabalgatas este dia"; // ...devuelve el aviso
        } 
        return nombreDia + ": " + cantidad + " cabalgata(s) - Total $" + String.format("%.0f", total); // ej: "Lunes: 3 cabalgata(s) - Total $45000" (%.0f = sin decimales)
    } 
} 