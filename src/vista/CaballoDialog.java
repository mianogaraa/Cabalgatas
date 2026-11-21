package vista; // paquete donde esta esta clase: vista

import javax.swing.JComboBox; // JComboBox: lista desplegable para elegir una opcion
import javax.swing.JFrame; // JFrame: ventana principal, la que recibe el dialogo como padre
import javax.swing.JTextField; // JTextField: campo de texto de una linea
import modelo.Caballo; // Caballo: clase del modelo; se usa en cargar() para llenar el formulario al editar

public class CaballoDialog extends FormularioDialog { // formulario de caballo; extends FormularioDialog = hereda de la clase base (que a su vez es un JDialog)

    private JTextField txtNombre = new JTextField(18); // campo para el nombre; 18 = ancho en columnas; private: el controlador lo lee con getNombre()
    private JTextField txtRaza = new JTextField(18); // campo de texto para la raza (mismo patron que el anterior)
    private JTextField txtEdad = new JTextField(18); // campo de texto para la edad; se guarda como texto y el controlador lo convierte a numero
    private JTextField txtPelaje = new JTextField(18); // campo de texto para el pelaje
    private JComboBox<String> cmbEstado = new JComboBox<>(new String[]{"Disponible", "En descanso"}); // desplegable de Strings con las dos opciones de estado; <String> indica el tipo de cada opcion

    public CaballoDialog(JFrame padre, String titulo) { // constructor: recibe la ventana padre y el titulo ("Nuevo caballo" o "Editar caballo")
        super(padre, titulo, 5); // llama al constructor del padre FormularioDialog: arma el dialogo con 5 filas en la grilla
        agregarFila("Nombre", txtNombre); // agregarFila viene del padre; version JTextField: aplica estilo y agrega etiqueta mas campo
        agregarFila("Raza", txtRaza); // agrega la fila de raza
        agregarFila("Edad", txtEdad); // agrega la fila de edad
        agregarFila("Pelaje", txtPelaje); // agrega la fila de pelaje
        agregarFila("Estado", cmbEstado); // el combo no es JTextField, asi que usa la sobrecarga de agregarFila para cualquier Component
        armar(); // armar viene del padre: agrega la grilla al centro, los botones abajo, y ajusta tamanio y posicion
    } // fin del constructor

    public void cargar(Caballo c) { // carga los datos de un caballo en el formulario; el controlador lo llama al editar
        txtNombre.setText(c.getNombre()); // pone el nombre del caballo en el campo
        txtRaza.setText(c.getRaza()); // pone la raza en el campo
        txtEdad.setText(String.valueOf(c.getEdad())); // el campo es texto, asi que String.valueOf convierte el numero de edad a String
        txtPelaje.setText(c.getPelaje()); // pone el pelaje en el campo
        cmbEstado.setSelectedItem(c.getEstado()); // deja seleccionada en el desplegable la opcion igual al estado del caballo
    } // fin del metodo

    public String getNombre() { // getter: devuelve el nombre escrito; el ControladorCaballo lo lee (la vista no tiene logica)
        return txtNombre.getText().trim(); // getText toma el texto del campo y trim saca los espacios de los extremos
    } // fin del metodo

    public String getRaza() { // getter: devuelve la raza escrita; lo usa ControladorCaballo
        return txtRaza.getText().trim(); // devuelve el texto del campo de raza sin espacios sobrantes
    } // fin del metodo

    public String getTextoEdad() { // getter: devuelve la edad como texto; el controlador la valida y la convierte a numero
        return txtEdad.getText().trim(); // devuelve el texto del campo de edad sin espacios sobrantes
    } // fin del metodo

    public String getPelaje() { // getter: devuelve el pelaje escrito; lo usa ControladorCaballo
        return txtPelaje.getText().trim(); // devuelve el texto del campo de pelaje sin espacios sobrantes
    } // fin del metodo

    public String getEstado() { // getter: devuelve el estado elegido en el desplegable; lo usa ControladorCaballo
        return (String) cmbEstado.getSelectedItem(); // getSelectedItem devuelve Object, por eso se castea a String
    } // fin del metodo
} // fin de la clase