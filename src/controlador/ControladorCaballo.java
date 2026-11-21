package controlador; // paquete (carpeta) donde esta esta clase: los controladores

import java.sql.SQLException; // error que puede tirar la base de datos
import java.time.LocalDateTime; // fecha y hora juntas
import modelo.Caballo; // el caballo (clase del modelo)
import modelo.CaballoDAO; // clase que guarda y lee caballos en la base
import modelo.Movimiento; // la cabalgata
import modelo.MovimientoDAO; // clase que consulta las cabalgatas en la base
import vista.CaballoDialog; // el formulario de caballo que este controlador maneja

public class ControladorCaballo { // controlador del formulario de alta y modificacion de caballos

    private CaballoDialog vista; // el formulario: de aca leo los datos y en el muestro mensajes
    private CaballoDAO caballoDAO = new CaballoDAO(); // DAO para guardar el caballo en la base
    private MovimientoDAO movimientoDAO = new MovimientoDAO(); // DAO para consultar las cabalgatas del caballo
    private Caballo existente; // caballo que se esta modificando; null si es uno nuevo
    private boolean guardado; // false al inicio; pasa a true si se guardo bien (lo lee ControladorPrincipal)

    public ControladorCaballo(CaballoDialog vista, Caballo existente) { // constructor: lo llama ControladorPrincipal con el formulario y el caballo
        this.vista = vista; // guarda el formulario recibido
        this.existente = existente; // guarda el caballo a modificar (o null si es nuevo)
        if (existente != null) { // si se esta modificando un caballo...
            vista.cargar(existente); // ...llena el formulario con sus datos
        } // fin del if
        vista.getBtnGuardar().addActionListener(e -> guardar()); // al apretar Guardar, ejecuta guardar()
        vista.getBtnCancelar().addActionListener(e -> vista.dispose()); // al apretar Cancelar, cierra el formulario sin guardar
    } // fin del constructor

    public boolean isGuardado() { // getter: dice si se guardo
        return guardado; // devuelve true o false; ControladorPrincipal lo usa para recargar la tabla
    } // fin de isGuardado

    private void guardar() { // se ejecuta al apretar Guardar
        String error = validarCampos(); // revisa los datos; devuelve un texto de error o null si esta todo bien
        if (error != null) { // si hay un error...
            vista.mostrarMensaje(error); // ...se lo muestra al usuario
            return; // ...y sale sin guardar nada
        } // fin del if
        Caballo caballo = crearCaballo(); // arma el objeto Caballo con lo que escribio el usuario
        try { // consultar o guardar en la base puede fallar
            String pendientes = buscarCabalgatasPendientes(caballo); // revisa si tiene cabalgatas a futuro y se lo quiere pasar a descanso
            if (pendientes != null) { // si hay cabalgatas pendientes...
                vista.mostrarMensaje(pendientes); // ...muestra el aviso
                return; // ...y sale sin guardar
            } // fin del if
            if (existente == null) { // si es un caballo nuevo...
                caballoDAO.insertar(caballo); // ...lo inserta en la base (INSERT)
            } else { // si no, se esta modificando uno existente...
                caballoDAO.modificar(caballo); // ...actualiza sus datos en la base (UPDATE)
            } // fin del if/else
        } catch (SQLException ex) { // si fallo la base
            vista.mostrarMensaje("Error en la base de datos: " + ex.getMessage()); // muestra el error
            return; // sale sin marcar como guardado
        } // fin del catch
        guardado = true; // llego hasta aca: se guardo bien
        vista.dispose(); // cierra el formulario
    } // fin de guardar

    private String validarCampos() { // revisa nombre y edad; devuelve error o null
        if (vista.getNombre().isEmpty()) { // si el nombre esta vacio...
            return "El nombre es obligatorio"; // ...devuelve el error
        } // fin del if
        try { // la edad viene como texto: puede no ser un numero
            if (Integer.parseInt(vista.getTextoEdad()) < 0) { // convierte el texto a entero y pregunta si es negativo
                return "La edad no puede ser negativa"; // devuelve el error
            } // fin del if
        } catch (NumberFormatException ex) { // si el texto no era un numero entero
            return "La edad debe ser un numero entero"; // devuelve el error
        } // fin del catch
        return null; // todo bien: null significa "sin error"
    } // fin de validarCampos

    private String buscarCabalgatasPendientes(Caballo caballo) throws SQLException { // regla: no pasar a descanso un caballo con cabalgatas a futuro
        if (existente == null || "Disponible".equals(caballo.getEstado())) { // si es nuevo o va a quedar Disponible...
            return null; // ...no hay nada que controlar
        } // fin del if
        for (Movimiento m : movimientoDAO.listarPorCaballo(existente.getId())) { // recorre las cabalgatas que ya tiene ese caballo
            if (m.getInicio().isAfter(LocalDateTime.now())) { // si alguna empieza despues de este momento (es a futuro)
                return caballo.getNombre() + " tiene cabalgatas pendientes. Eliminalas o cambialas de caballo antes de ponerlo en descanso."; // devuelve el aviso
            } // fin del if
        } // fin del for
        return null; // no encontro ninguna pendiente: sin error
    } // fin de buscarCabalgatasPendientes

    private Caballo crearCaballo() { // arma un Caballo con los datos del formulario
        Caballo caballo = new Caballo(vista.getNombre(), vista.getRaza(), // crea el objeto con nombre y raza...
                Integer.parseInt(vista.getTextoEdad()), vista.getPelaje(), vista.getEstado()); // ...edad (texto pasado a entero), pelaje y estado
        if (existente != null) { // si se esta modificando...
            caballo.setId(existente.getId()); // ...le pone el id del caballo original, para que el UPDATE sepa cual fila cambiar
        } // fin del if
        return caballo; // devuelve el caballo armado
    } // fin de crearCaballo
} // fin de la clase