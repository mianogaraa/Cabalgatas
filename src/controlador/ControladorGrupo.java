package controlador; // paquete (carpeta) donde esta esta clase: los controladores

import java.sql.SQLException; // error que puede tirar la base de datos
import java.util.ArrayList; // lista que crece sola
import java.util.List; // lista de objetos
import modelo.Caballo; // el caballo
import modelo.Fechas; // traductor de fechas entre el formato de pantalla y el de la base
import modelo.Movimiento; // la cabalgata (clase abstracta)
import modelo.MovimientoDAO; // clase que guarda y consulta cabalgatas en la base
import modelo.MovimientoDiurno; // cabalgata de dia (hija de Movimiento)
import modelo.MovimientoNocturno; // cabalgata de noche (hija de Movimiento)
import modelo.Validaciones; // reglas compartidas: horario, superposicion y formato de datos
import vista.GrupoDialog; // el formulario de grupo que este controlador maneja

public class ControladorGrupo { // controlador del formulario para agendar un grupo de personas

    private GrupoDialog vista; // el formulario: de aca leo los datos y en el muestro mensajes
    private MovimientoDAO movimientoDAO = new MovimientoDAO(); // DAO para consultar y guardar las cabalgatas
    private List<Movimiento> guardados = new ArrayList<>(); // cabalgatas guardadas; vacia hasta que se guarde bien

    public ControladorGrupo(GrupoDialog vista) { // constructor: lo llama ControladorPrincipal con el formulario
        this.vista = vista; // guarda el formulario recibido
        vista.getBtnGuardar().addActionListener(e -> guardar()); // al apretar "Guardar grupo", ejecuta guardar()
        vista.getBtnCancelar().addActionListener(e -> vista.dispose()); // al apretar Cancelar, cierra el formulario sin guardar
    } // fin del constructor

    public List<Movimiento> getGuardados() { // getter: devuelve las cabalgatas guardadas
        return guardados; // ControladorPrincipal lo lee para avisar cuantas se agendaron
    } // fin de getGuardados

    private void guardar() { // se ejecuta al apretar "Guardar grupo"
        String error = validarCampos(); // revisa los datos basicos; devuelve un texto de error o null
        if (error != null) { // si hay un error...
            vista.mostrarMensaje(error); // ...se lo muestra al usuario
            return; // ...y sale sin guardar nada
        } // fin del if
        List<Movimiento> grupo = crearMovimientos(); // crea una cabalgata por cada caballo elegido
        error = Validaciones.validarHorario(grupo.get(0)); // controla el horario con la primera: todas comparten tipo, fecha, hora y duracion
        if (error != null) { // si el horario no corresponde al tipo elegido...
            vista.mostrarMensaje(error); // ...muestra el aviso
            return; // ...y sale sin guardar
        } // fin del if
        try { // consultar o guardar en la base puede fallar
            for (Movimiento m : grupo) { // recorre las cabalgatas del grupo, una por caballo
                error = Validaciones.buscarSuperposicion(m, movimientoDAO); // revisa si ese caballo ya tiene otra cabalgata que se pise
                if (error != null) { // si ese caballo esta ocupado...
                    vista.mostrarMensaje(error); // ...muestra cual caballo y en que horario
                    return; // ...y sale sin guardar ninguna (todavia no se inserto nada)
                } // fin del if
            } // fin del for
            movimientoDAO.insertarVarios(grupo); // guarda todas juntas en una transaccion: todas o ninguna
        } catch (SQLException ex) { // si fallo la base
            vista.mostrarMensaje("Error en la base de datos: " + ex.getMessage()); // muestra el error
            return; // sale sin marcar como guardado
        } // fin del catch
        guardados = grupo; // se guardo bien: deja la lista para que ControladorPrincipal la lea
        vista.dispose(); // cierra el formulario
    } // fin de guardar

    private String validarCampos() { // revisa los datos basicos del formulario; devuelve error o null
        if (vista.getCaballosElegidos().isEmpty()) { // si no se tildo ningun caballo...
            return "Elegi al menos un caballo para el grupo"; // ...devuelve el error: un grupo sin caballos no tiene cabalgatas
        } // fin del if
        if (vista.getCliente().isEmpty()) { // si el nombre del grupo esta vacio...
            return "El nombre del grupo es obligatorio"; // ...devuelve el error
        } // fin del if
        return Validaciones.validarDatos(vista.getFecha(), vista.getHora(), vista.getTextoDuracion(), vista.getTextoPrecio()); // controla fecha, hora, duracion y precio; devuelve su error o null
    } // fin de validarCampos

    private List<Movimiento> crearMovimientos() { // arma la lista de cabalgatas del grupo
        String fecha = Fechas.aBase(vista.getFecha()); // pasa la fecha de DD-MM-AAAA a AAAA-MM-DD (formato de la base)
        double duracion = Validaciones.leerNumero(vista.getTextoDuracion()); // pasa el texto de la duracion a numero
        double precio = Validaciones.leerNumero(vista.getTextoPrecio()); // pasa el texto del precio a numero
        List<Movimiento> grupo = new ArrayList<>(); // lista vacia donde se van agregando las cabalgatas
        for (Caballo caballo : vista.getCaballosElegidos()) { // recorre los caballos tildados, uno por persona
            if ("Nocturno".equals(vista.getTipo())) { // si el tipo elegido es Nocturno...
                grupo.add(new MovimientoNocturno(caballo, fecha, vista.getHora(), vista.getCliente(), duracion, precio)); // ...crea una cabalgata nocturna para ese caballo
            } else { // si no, es Diurno...
                grupo.add(new MovimientoDiurno(caballo, fecha, vista.getHora(), vista.getCliente(), duracion, precio)); // ...crea una cabalgata diurna para ese caballo
            } // fin del if/else
        } // fin del for
        return grupo; // devuelve la lista armada
    } // fin de crearMovimientos
} // fin de la clase 