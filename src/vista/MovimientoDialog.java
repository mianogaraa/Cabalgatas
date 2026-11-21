package vista; // paquete donde esta esta clase: vista

import java.util.ArrayList; // ArrayList: lista que crece sola; la usamos para hacer una copia de la lista de caballos
import java.util.List; // List: interfaz de lista; se declara con List y se crea con ArrayList
import javax.swing.JCheckBox; // JCheckBox: casilla que se tilda o destilda
import javax.swing.JComboBox; // JComboBox: lista desplegable para elegir una opcion
import javax.swing.JFrame; // JFrame: ventana principal, la que recibe el dialogo como padre
import javax.swing.JTextField; // JTextField: campo de texto de una linea
import modelo.Caballo; // Caballo: clase del modelo; las opciones del combo son objetos Caballo
import modelo.Fechas; // Fechas: clase del modelo que convierte fechas entre el formato de la base y el que se muestra
import modelo.Movimiento; // Movimiento: la cabalgata; se usa en cargar() para llenar el formulario al editar

public class MovimientoDialog extends FormularioDialog { // formulario de cabalgata; extends FormularioDialog = hereda de la clase base (que es un JDialog)

    private List<Caballo> caballos; // lista de caballos disponibles para elegir; <Caballo> = lista que solo guarda Caballo; private
    private JComboBox<Caballo> cmbCaballo = new JComboBox<>(); // desplegable de objetos Caballo (muestra el nombre porque Caballo.toString devuelve el nombre)
    private JCheckBox chkSoloDisponibles = new JCheckBox("Mostrar solo caballos disponibles"); // casilla para filtrar el desplegable y ver solo los caballos con estado Disponible
    private JComboBox<String> cmbTipo = new JComboBox<>(new String[]{"Diurno", "Nocturno"}); // desplegable con los dos tipos de recorrido; el controlador crea un Diurno o un Nocturno segun esto
    private JTextField txtFecha = new JTextField(18); // campo para la fecha (DD-MM-AAAA); 18 = ancho en columnas
    private JTextField txtHora = new JTextField(18); // campo para la hora de inicio (HH:mm)
    private JTextField txtCliente = new JTextField(18); // campo para el nombre del cliente
    private JTextField txtDuracion = new JTextField(18); // campo para la duracion en horas (texto; el controlador lo pasa a numero)
    private JTextField txtPrecio = new JTextField(18); // campo para el precio por hora (texto; el controlador lo pasa a numero)

    public MovimientoDialog(JFrame padre, String titulo, List<Caballo> caballos) { // constructor: recibe la ventana padre, el titulo y la lista de caballos a mostrar en el combo
        super(padre, titulo, 8); // llama al constructor del padre FormularioDialog: dialogo modal con 8 filas en la grilla
        this.caballos = new ArrayList<>(caballos); // guarda una copia de la lista recibida (asi no se comparte la original); this = el atributo

        chkSoloDisponibles.setBackground(Estilo.FONDO); // color de fondo de la casilla igual al del dialogo
        chkSoloDisponibles.setForeground(Estilo.TEXTO); // color del texto de la casilla
        chkSoloDisponibles.addActionListener(e -> filtrar()); // lambda (e -> ...): al tildar o destildar la casilla se ejecuta filtrar() para rearmar el combo
        filtrar(); // llena el combo por primera vez con los caballos que correspondan

        agregarFila("Caballo", cmbCaballo); // agregarFila viene del padre; el combo no es JTextField, asi que usa la sobrecarga para cualquier Component
        agregarFila("", chkSoloDisponibles); // fila con etiqueta vacia: deja la casilla alineada bajo el combo
        agregarFila("Tipo de recorrido", cmbTipo); // agrega la fila del tipo de recorrido (otro combo)
        agregarFila("Fecha (DD-MM-AAAA)", txtFecha); // JTextField: usa la sobrecarga que ademas le aplica el estilo al campo
        agregarFila("Hora (HH:mm)", txtHora); // agrega la fila de la hora
        agregarFila("Cliente", txtCliente); // agrega la fila del cliente
        agregarFila("Duracion (horas)", txtDuracion); // agrega la fila de la duracion
        agregarFila("Precio por hora", txtPrecio); // agrega la fila del precio por hora
        armar(); // armar viene del padre: agrega la grilla al centro, los botones abajo, y ajusta tamanio y posicion
    } // fin del constructor

    private void filtrar() { // vuelve a llenar el combo segun la casilla; private porque solo se usa dentro de esta clase
        Caballo seleccionado = (Caballo) cmbCaballo.getSelectedItem(); // guarda el caballo elegido ahora (cast de Object a Caballo) para no perder la seleccion al rearmar
        cmbCaballo.removeAllItems(); // vacia el combo
        for (Caballo c : caballos) { // for-each: recorre la lista de caballos, uno por vuelta
            if (!chkSoloDisponibles.isSelected() || "Disponible".equals(c.getEstado())) { // agrega el caballo si la casilla esta destildada o si su estado es Disponible (equals con el literal evita null)
                cmbCaballo.addItem(c); // agrega el caballo como opcion del combo
            } // fin del if
        } // fin del for
        if (seleccionado != null) { // si antes habia un caballo elegido...
            seleccionarCaballo(seleccionado.getId()); // ...lo vuelve a seleccionar buscandolo por id
        } // fin del if
    } // fin del metodo

    private void seleccionarCaballo(int id) { // selecciona en el combo el caballo con ese id; private: es ayuda interna
        for (int i = 0; i < cmbCaballo.getItemCount(); i++) { // for clasico: recorre las posiciones del combo de 0 hasta la ultima
            if (cmbCaballo.getItemAt(i).getId() == id) { // si el caballo de esa posicion tiene el id buscado...
                cmbCaballo.setSelectedIndex(i); // ...lo deja seleccionado en el combo
                return; // termina el metodo: ya lo encontro, no hace falta seguir
            } // fin del if
        } // fin del for
    } // fin del metodo

    public void cargar(Movimiento m) { // carga una cabalgata en el formulario; el controlador lo llama al editar
        seleccionarCaballo(m.getCaballo().getId()); // elige en el combo el caballo de la cabalgata
        cmbTipo.setSelectedItem(m.getTipoRecorrido()); // elige el tipo de recorrido de la cabalgata
        txtFecha.setText(Fechas.aTexto(m.getFecha())); // Fechas.aTexto pasa la fecha de formato base (AAAA-MM-DD) a DD-MM-AAAA (esta sobrecargado: acepta String o LocalDate)
        txtHora.setText(m.getHoraInicio()); // pone la hora de inicio
        txtCliente.setText(m.getCliente()); // pone el nombre del cliente
        txtDuracion.setText(String.valueOf(m.getDuracion())); // String.valueOf pasa el numero de duracion a texto para el campo
        txtPrecio.setText(String.valueOf(m.getPrecio())); // String.valueOf pasa el numero de precio a texto para el campo
    } // fin del metodo

    public Caballo getCaballo() { // getter: devuelve el caballo elegido en el combo; lo usa ControladorMovimiento
        return (Caballo) cmbCaballo.getSelectedItem(); // getSelectedItem devuelve Object, por eso se castea a Caballo
    } // fin del metodo

    public String getTipo() { // getter: devuelve "Diurno" o "Nocturno"; el controlador decide con eso que clase crear
        return (String) cmbTipo.getSelectedItem(); // getSelectedItem devuelve Object, por eso se castea a String
    } // fin del metodo

    public String getFecha() { // getter: devuelve la fecha escrita (texto); el controlador la valida y la convierte
        return txtFecha.getText().trim(); // getText toma el texto del campo y trim saca los espacios de los extremos
    } // fin del metodo

    public String getHora() { // getter: devuelve la hora escrita; lo usa ControladorMovimiento
        return txtHora.getText().trim(); // devuelve el texto del campo de hora sin espacios sobrantes
    } // fin del metodo

    public String getCliente() { // getter: devuelve el cliente escrito; lo usa ControladorMovimiento
        return txtCliente.getText().trim(); // devuelve el texto del campo de cliente sin espacios sobrantes
    } // fin del metodo

    public String getTextoDuracion() { // getter: devuelve la duracion como texto; el controlador la valida y la pasa a numero
        return txtDuracion.getText().trim(); // devuelve el texto del campo de duracion sin espacios sobrantes
    } // fin del metodo

    public String getTextoPrecio() { // getter: devuelve el precio como texto; el controlador lo valida y lo pasa a numero
        return txtPrecio.getText().trim(); // devuelve el texto del campo de precio sin espacios sobrantes
    } // fin del metodo
} // fin de la clase