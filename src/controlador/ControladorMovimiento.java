package controlador; 

import java.sql.SQLException; // error que puede tirar la base de datos
import modelo.Caballo; // el caballo
import modelo.Fechas; // traductor de fechas entre el formato de pantalla y el de la base
import modelo.Movimiento; // la cabalgata (clase abstracta)
import modelo.MovimientoDAO; // clase que guarda y consulta cabalgatas en la base
import modelo.MovimientoDiurno; // cabalgata de dia (hija de Movimiento)
import modelo.MovimientoNocturno; // cabalgata de noche (hija de Movimiento)
import modelo.Validaciones; // reglas compartidas: horario, superposicion y formato de datos
import vista.MovimientoDialog; // el formulario de cabalgata que este controlador maneja

public class ControladorMovimiento { // controlador del formulario de alta y modificacion de cabalgatas

    private MovimientoDialog vista; // el formulario: de aca leo los datos y en el muestro mensajes
    private MovimientoDAO movimientoDAO = new MovimientoDAO(); // DAO para consultar y guardar cabalgatas
    private Movimiento existente; // cabalgata que se esta modificando; null si es nueva
    private Movimiento guardado; // cabalgata guardada; null hasta que se guarde bien (la lee ControladorPrincipal)

    public ControladorMovimiento(MovimientoDialog vista, Movimiento existente) { // constructor: lo llama ControladorPrincipal con el formulario y la cabalgata
        this.vista = vista; // guarda el formulario recibido
        this.existente = existente; // guarda la cabalgata a modificar (o null si es nueva)
        if (existente != null) { // si se esta modificando...
            vista.cargar(existente); // ...llena el formulario con sus datos
        } 
        vista.getBtnGuardar().addActionListener(e -> guardar()); // al apretar Guardar, ejecuta guardar()
        vista.getBtnCancelar().addActionListener(e -> vista.dispose()); // al apretar Cancelar, cierra el formulario sin guardar
    } 

    public Movimiento getGuardado() { // getter: devuelve la cabalgata guardada
        return guardado; // ControladorPrincipal lo usa para seleccionar ese caballo en la tabla; es null si no se guardo
    } 

    private void guardar() { // se ejecuta al apretar Guardar
        String error = validarCampos(); // revisa los datos; devuelve un texto de error o null si esta todo bien
        if (error != null) { // si hay un error...
            vista.mostrarMensaje(error); // ...se lo muestra al usuario
            return; // ...y sale sin guardar nada
        } 
        Movimiento movimiento = crearMovimiento(); // arma el objeto (Diurno o Nocturno) con lo que escribio el usuario
        error = Validaciones.validarHorario(movimiento); // controla que el horario corresponda al tipo (diurno 08-19, nocturno 19-00)
        if (error != null) { // si el horario no corresponde...
            vista.mostrarMensaje(error); // ...muestra el aviso
            return; // ...y sale sin guardar
        } 
        try { // consultar o guardar en la base puede fallar
            error = Validaciones.buscarSuperposicion(movimiento, movimientoDAO); // revisa si el caballo ya tiene otra cabalgata que se pise
            if (error != null) { // si hay superposicion...
                vista.mostrarMensaje(error); // ...muestra el horario que esta ocupado
                return; // ...y sale sin guardar
            } 
            if (existente == null) { // si es una cabalgata nueva...
                movimientoDAO.insertar(movimiento); // ...la inserta en la base (INSERT)
            } else { // si no, se esta modificando una existente...
                movimientoDAO.modificar(movimiento); // ...actualiza sus datos en la base (UPDATE)
            } 
        } catch (SQLException ex) { // si fallo la base
            vista.mostrarMensaje("Error en la base de datos: " + ex.getMessage()); // muestra el error
            return; // sale sin marcar como guardado
        } 
        guardado = movimiento; // se guardo bien: deja la cabalgata para que ControladorPrincipal la lea
        vista.dispose(); // cierra el formulario
    } 

    private String validarCampos() { // revisa los datos del formulario; devuelve error o null
        Caballo caballo = vista.getCaballo(); // caballo elegido en el desplegable
        if (caballo == null) { // si no hay ninguno elegido...
            return "Elegi un caballo"; // ...devuelve el error
        } 
        if (existente == null && !"Disponible".equals(caballo.getEstado())) { // si es una cabalgata nueva y el caballo no esta Disponible...
            return caballo.getNombre() + " esta en descanso, no se le pueden agendar cabalgatas nuevas"; // ...devuelve el error (regla del descanso)
        } 
        if (vista.getCliente().isEmpty()) { // si el cliente esta vacio...
            return "El cliente es obligatorio"; // ...devuelve el error
        } 
        return Validaciones.validarDatos(vista.getFecha(), vista.getHora(), vista.getTextoDuracion(), vista.getTextoPrecio()); // controla fecha, hora, duracion y precio; devuelve su error o null
    } 

    private Movimiento crearMovimiento() { // arma la cabalgata con los datos del formulario
        Caballo caballo = vista.getCaballo(); 
        String fecha = Fechas.aBase(vista.getFecha()); // pasa la fecha de DD-MM-AAAA a AAAA-MM-DD (formato de la base)
        double duracion = Validaciones.leerNumero(vista.getTextoDuracion()); // pasa el texto de la duracion a numero
        double precio = Validaciones.leerNumero(vista.getTextoPrecio()); // pasa el texto del precio a numero
        Movimiento m; // variable del tipo padre: va a guardar un Diurno o un Nocturno (polimorfismo)
        if ("Nocturno".equals(vista.getTipo())) { // si el tipo elegido es Nocturno...
            m = new MovimientoNocturno(caballo, fecha, vista.getHora(), vista.getCliente(), duracion, precio); // ...crea una cabalgata nocturna
        } else { 
            m = new MovimientoDiurno(caballo, fecha, vista.getHora(), vista.getCliente(), duracion, precio); // ...crea una cabalgata diurna
        } 
        if (existente != null) { // si se esta modificando...
            m.setId(existente.getId()); // ...le pone el id de la original, para que el UPDATE sepa cual fila cambiar
        } 
        return m;
    } 
}