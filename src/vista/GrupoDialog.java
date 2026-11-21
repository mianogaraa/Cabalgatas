package vista; // paquete donde esta esta clase: vista

import java.awt.BorderLayout; // BorderLayout: layout que divide en NORTH, SOUTH, EAST, WEST y CENTER
import java.awt.Color; // Color: clase para colores (aca usamos Color.WHITE)
import java.awt.Dimension; // Dimension: ancho y alto, para fijar el tamanio preferido de un componente
import java.awt.Font; // Font: tipo de letra (aca usamos Font.BOLD para negrita)
import java.awt.GridLayout; // GridLayout: layout que acomoda los elementos en una grilla de filas y columnas
import java.util.ArrayList; // ArrayList: lista que crece sola
import java.util.List; // List: interfaz de lista; se declara con List y se crea con ArrayList
import javax.swing.BorderFactory; // BorderFactory: fabrica de bordes (vacios, de linea) para los componentes
import javax.swing.JCheckBox; // JCheckBox: casilla que se tilda o destilda (una por caballo)
import javax.swing.JComboBox; // JComboBox: lista desplegable para elegir una opcion
import javax.swing.JFrame; // JFrame: ventana principal, la que recibe el dialogo como padre
import javax.swing.JLabel; // JLabel: etiqueta de texto que no se edita
import javax.swing.JPanel; // JPanel: panel, un contenedor donde se agrupan otros componentes
import javax.swing.JScrollPane; // JScrollPane: contenedor con barras de desplazamiento cuando el contenido no entra
import javax.swing.JTextField; // JTextField: campo de texto de una linea
import modelo.Caballo; // Caballo: clase del modelo; cada casilla representa un caballo

public class GrupoDialog extends FormularioDialog { // formulario de grupo; extends FormularioDialog = hereda de la clase base (que es un JDialog)

    private List<Caballo> caballos = new ArrayList<>(); // caballos disponibles; <Caballo> = lista de Caballo; en la misma posicion que su casilla en la otra lista
    private List<JCheckBox> casillas = new ArrayList<>(); // las casillas, una por caballo; casillas.get(i) corresponde a caballos.get(i)
    private JLabel lblCantidad = new JLabel("Personas en el grupo: 0"); // etiqueta que muestra cuantas personas (caballos) hay elegidas; arranca en 0
    private JTextField txtCliente = new JTextField(18); // campo para el nombre del grupo (se guarda como cliente); 18 = ancho en columnas
    private JComboBox<String> cmbTipo = new JComboBox<>(new String[]{"Diurno", "Nocturno"}); // desplegable con los tipos de recorrido; <String> = el tipo de cada opcion
    private JTextField txtFecha = new JTextField(18); // campo para la fecha (DD-MM-AAAA)
    private JTextField txtHora = new JTextField(18); // campo para la hora de inicio (HH:mm)
    private JTextField txtDuracion = new JTextField(18); // campo para la duracion en horas (texto)
    private JTextField txtPrecio = new JTextField(18); // campo para el precio por hora de cada caballo (texto)

    public GrupoDialog(JFrame padre, List<Caballo> todos) { // constructor: recibe la ventana padre y la lista de todos los caballos
        super(padre, "Nuevo grupo", 6); // llama al constructor del padre FormularioDialog: dialogo modal, titulo fijo y 6 filas en la grilla
        getBtnGuardar().setText("Guardar grupo"); // getBtnGuardar viene del padre; aca solo se cambia el texto del boton

        agregarFila("Nombre del grupo", txtCliente); // agregarFila viene del padre; version JTextField: aplica estilo y agrega etiqueta mas campo
        agregarFila("Tipo de recorrido", cmbTipo); // el combo no es JTextField: usa la sobrecarga de agregarFila para cualquier Component
        agregarFila("Fecha (DD-MM-AAAA)", txtFecha); // agrega la fila de la fecha
        agregarFila("Hora (HH:mm)", txtHora); // agrega la fila de la hora
        agregarFila("Duracion (horas)", txtDuracion); // agrega la fila de la duracion
        agregarFila("Precio por hora (cada caballo)", txtPrecio); // agrega la fila del precio por hora

        JPanel lista = new JPanel(new GridLayout(0, 2, 6, 4)); // panel con GridLayout de 2 columnas (0 filas = las que hagan falta) y espacios de 6 y 4 px para las casillas
        lista.setBackground(Color.WHITE); // fondo blanco para el panel de casillas
        for (Caballo c : todos) { // for-each: recorre todos los caballos, uno por vuelta
            if ("Disponible".equals(c.getEstado())) { // solo entran los caballos disponibles (equals con el literal evita errores si el estado es null)
                JCheckBox casilla = new JCheckBox(c.getNombre() + " (" + c.getRaza() + ")"); // crea una casilla con el texto "nombre (raza)"
                casilla.setBackground(Color.WHITE); // fondo blanco para la casilla
                casilla.setForeground(Estilo.TEXTO); // color del texto de la casilla
                casilla.addActionListener(e -> actualizarCantidad()); // lambda (e -> ...): cada vez que se tilda o destilda, actualiza el contador de personas
                caballos.add(c); // guarda el caballo en la lista (misma posicion que su casilla)
                casillas.add(casilla); // guarda la casilla en la lista de casillas
                lista.add(casilla); // pone la casilla en el panel para que se vea
            } // fin del if
        } // fin del for
        JScrollPane scroll = new JScrollPane(lista); // mete el panel de casillas en un JScrollPane: si hay muchos caballos aparece la barra de desplazamiento
        scroll.setPreferredSize(new Dimension(420, 130)); // tamanio preferido: 420 de ancho y 130 de alto
        scroll.setBorder(BorderFactory.createLineBorder(Estilo.BORDE)); // borde de linea con el color de borde del estilo

        JLabel etiquetaLista = new JLabel("Elegi los caballos (uno por persona):"); // etiqueta con la consigna que se muestra arriba de la lista
        etiquetaLista.setForeground(Estilo.TEXTO); // color de texto del estilo
        lblCantidad.setForeground(Estilo.TEXTO); // color de texto del estilo para el contador
        lblCantidad.setFont(lblCantidad.getFont().deriveFont(Font.BOLD)); // toma la fuente actual y deriva una version en negrita (BOLD)

        JPanel panelCaballos = new JPanel(new BorderLayout(0, 5)); // panel con BorderLayout (0 px horizontal, 5 px vertical) que agrupa consigna, lista y contador
        panelCaballos.setOpaque(false); // sin fondo propio: se ve el color del dialogo
        panelCaballos.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15)); // margenes vacios: 5 px arriba y abajo, 15 px a los costados
        panelCaballos.add(etiquetaLista, BorderLayout.NORTH); // consigna arriba (NORTH)
        panelCaballos.add(scroll, BorderLayout.CENTER); // la lista con scroll en el centro
        panelCaballos.add(lblCantidad, BorderLayout.SOUTH); // el contador de personas abajo (SOUTH)

        JPanel centro = new JPanel(new BorderLayout()); // panel contenedor de todo lo que va en el centro del dialogo
        centro.setOpaque(false); // sin fondo propio
        centro.add(getFormulario(), BorderLayout.NORTH); // getFormulario viene del padre: pone la grilla de campos arriba (NORTH)
        centro.add(panelCaballos, BorderLayout.CENTER); // el panel de caballos ocupa el resto (CENTER)
        armar(centro); // armar viene del padre; esta es la sobrecarga que recibe el componente central (en vez de armar() sin parametros)
    } // fin del constructor

    private void actualizarCantidad() { // actualiza la etiqueta del contador; private: solo la usa esta clase
        lblCantidad.setText("Personas en el grupo: " + getCaballosElegidos().size()); // escribe el texto con la cantidad de caballos elegidos (size() = cantidad de elementos de la lista)
    } // fin del metodo

    public List<Caballo> getCaballosElegidos() { // getter calculado: devuelve los caballos con casilla tildada; lo usa ControladorGrupo
        List<Caballo> elegidos = new ArrayList<>(); // lista nueva, vacia, donde se van juntando los caballos tildados
        for (int i = 0; i < casillas.size(); i++) { // for clasico: recorre las posiciones de la lista de casillas
            if (casillas.get(i).isSelected()) { // si la casilla de la posicion i esta tildada...
                elegidos.add(caballos.get(i)); // ...agrega el caballo de la misma posicion
            } // fin del if
        } // fin del for
        return elegidos; // devuelve la lista de caballos elegidos
    } // fin del metodo

    public String getTipo() { // getter: devuelve "Diurno" o "Nocturno"; el controlador decide con eso que clase crear
        return (String) cmbTipo.getSelectedItem(); // getSelectedItem devuelve Object, por eso se castea a String
    } // fin del metodo

    public String getFecha() { // getter: devuelve la fecha escrita (texto); lo usa ControladorGrupo
        return txtFecha.getText().trim(); // getText toma el texto del campo y trim saca los espacios de los extremos
    } // fin del metodo

    public String getHora() { // getter: devuelve la hora escrita; lo usa ControladorGrupo
        return txtHora.getText().trim(); // devuelve el texto del campo de hora sin espacios sobrantes
    } // fin del metodo

    public String getCliente() { // getter: devuelve el nombre del grupo (cliente); lo usa ControladorGrupo
        return txtCliente.getText().trim(); // devuelve el texto del campo de nombre del grupo sin espacios sobrantes
    } // fin del metodo

    public String getTextoDuracion() { // getter: devuelve la duracion como texto; el controlador la valida y la pasa a numero
        return txtDuracion.getText().trim(); // devuelve el texto del campo de duracion sin espacios sobrantes
    } // fin del metodo

    public String getTextoPrecio() { // getter: devuelve el precio como texto; el controlador lo valida y lo pasa a numero
        return txtPrecio.getText().trim(); // devuelve el texto del campo de precio sin espacios sobrantes
    } // fin del metodo
} // fin de la clase