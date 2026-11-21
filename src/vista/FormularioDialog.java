package vista; // paquete (carpeta) donde esta esta clase: vista, o sea las pantallas

import java.awt.BorderLayout; // BorderLayout: layout que divide la ventana en NORTH, SOUTH, EAST, WEST y CENTER
import java.awt.Component; // Component: clase padre de todos los elementos graficos (botones, campos, paneles)
import java.awt.FlowLayout; // FlowLayout: layout que acomoda los elementos uno al lado del otro, en fila
import java.awt.GridLayout; // GridLayout: layout que acomoda los elementos en una grilla de filas y columnas
import javax.swing.BorderFactory; // BorderFactory: fabrica de bordes (vacios, de linea) para los componentes
import javax.swing.JButton; // JButton: boton que se puede apretar
import javax.swing.JDialog; // JDialog: ventana secundaria (cuadro de dialogo); es la clase de la que heredamos
import javax.swing.JFrame; // JFrame: ventana principal; la usamos como "padre" del dialogo
import javax.swing.JLabel; // JLabel: etiqueta de texto que no se edita
import javax.swing.JOptionPane; // JOptionPane: cuadritos de mensaje listos para usar
import javax.swing.JPanel; // JPanel: panel, un contenedor donde se agrupan otros componentes
import javax.swing.JTextField; // JTextField: campo de texto de una linea donde el usuario escribe

public class FormularioDialog extends JDialog { // clase base de todos los formularios; extends = hereda de JDialog, o sea ya es una ventana de dialogo

    private JFrame padre; // ventana duena del dialogo; private: solo esta clase la toca, y se usa para centrar el dialogo
    private JPanel formulario; // panel con la grilla de etiquetas y campos; private: los hijos lo usan solo con agregarFila y getFormulario
    private JButton btnGuardar = Estilo.boton("Guardar", Estilo.VERDE); // boton Guardar creado con Estilo.boton (verde); private con getter para que el controlador le ponga el listener
    private JButton btnCancelar = Estilo.boton("Cancelar", Estilo.GRIS); // boton Cancelar (gris); igual que el otro, el controlador lo toma con su getter

    public FormularioDialog(JFrame padre, String titulo, int filas) { // constructor: lo llaman las hijas con super(...); recibe ventana padre, titulo y cantidad de filas del formulario
        super(padre, titulo, true); // super(...) llama al constructor de JDialog; el true final lo hace modal (bloquea la ventana de atras hasta cerrarlo)
        this.padre = padre; // guarda la ventana padre en el atributo (this distingue el atributo del parametro)
        setLayout(new BorderLayout()); // define que el dialogo se organiza con BorderLayout (norte, centro, sur)
        getContentPane().setBackground(Estilo.FONDO); // pinta de color de fondo el area de contenido del dialogo

        formulario = new JPanel(new GridLayout(filas, 2, 10, 10)); // crea el panel con GridLayout: filas, 2 columnas (etiqueta y campo) y 10 px de espacio entre celdas
        formulario.setBackground(Estilo.FONDO); // le pone el color de fondo del estilo al panel
        formulario.setBorder(BorderFactory.createEmptyBorder(15, 15, 5, 15)); // borde vacio: margenes de 15 px (arriba, izquierda, abajo, derecha) para que no quede pegado
        add(Estilo.titulo(titulo, Estilo.VERDE), BorderLayout.NORTH); // agrega la barra de titulo verde arriba (NORTH); el panel formulario se agrega despues, en armar()
    } // fin del constructor

    protected void agregarFila(String texto, JTextField campo) { // protected: la ven solo las hijas; sobrecarga 1: version para campos de texto (mismo nombre, otros parametros)
        Estilo.campo(campo); // le aplica el estilo comun (fuente, color, borde) al campo de texto
        agregarFila(texto, (Component) campo); // castea a Component para llamar a la otra version de agregarFila (si no, se llamaria a si misma)
    } // fin del metodo

    protected void agregarFila(String texto, Component campo) { // sobrecarga 2: version para cualquier componente (combo, casilla, etc.); Java elige segun el tipo
        JLabel etiqueta = new JLabel(texto); // crea la etiqueta con el texto que se ve a la izquierda de la fila
        etiqueta.setForeground(Estilo.TEXTO); // le pone el color de texto del estilo
        formulario.add(etiqueta); // agrega la etiqueta a la grilla (columna 1 de la fila)
        formulario.add(campo); // agrega el campo a la grilla (columna 2 de la fila)
    } // fin del metodo

    protected JPanel getFormulario() { // getter protected: deja que una hija (GrupoDialog) tome el panel para ponerlo dentro de otro panel
        return formulario; // devuelve el panel de la grilla
    } // fin del metodo

    protected void armar() { // armar sin parametros: lo llaman las hijas simples al final de su constructor
        armar(formulario); // llama a la otra version de armar usando el panel del formulario como centro
    } // fin del metodo

    protected void armar(Component centro) { // sobrecarga de armar: recibe el componente que va al centro (GrupoDialog le pasa su panel propio)
        JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 8)); // panel de botones con FlowLayout alineado a la derecha y 8 px de separacion
        botones.setBackground(Estilo.FONDO); // color de fondo igual al del dialogo
        botones.add(btnGuardar); // pone el boton Guardar en el panel
        botones.add(btnCancelar); // pone el boton Cancelar a continuacion

        add(centro, BorderLayout.CENTER); // agrega el contenido al centro del dialogo
        add(botones, BorderLayout.SOUTH); // agrega el panel de botones abajo (SOUTH)

        pack(); // ajusta el tamanio de la ventana a lo que necesitan sus componentes
        setResizable(false); // el usuario no puede cambiar el tamanio del dialogo
        setLocationRelativeTo(padre); // centra el dialogo respecto de la ventana padre
    } // fin del metodo

    public void mostrarMensaje(String mensaje) { // public: lo usan los controladores para avisar errores; la vista solo muestra, no decide nada
        JOptionPane.showMessageDialog(this, mensaje); // muestra un cuadro con el mensaje, dentro de este dialogo
    } // fin del metodo

    public JButton getBtnGuardar() { // getter: devuelve el boton Guardar; el controlador (Caballo, Movimiento, Grupo) le agrega el listener
        return btnGuardar; // devuelve el atributo btnGuardar
    } // fin del metodo

    public JButton getBtnCancelar() { // getter: devuelve el boton Cancelar; el controlador lo usa para cerrar con dispose()
        return btnCancelar; // devuelve el atributo btnCancelar
    } // fin del metodo
} // fin de la clase