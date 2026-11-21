package vista; // paquete (carpeta) donde esta esta clase: vista, o sea todo lo que se ve en pantalla

import java.awt.BorderLayout; // BorderLayout: layout que divide un contenedor en NORTH, SOUTH, EAST, WEST y CENTER
import java.awt.Dimension; // Dimension: guarda un ancho y un alto, se usa para fijar tamanios
import java.awt.FlowLayout; // FlowLayout: layout que acomoda los componentes uno al lado del otro, en fila
import java.awt.Font; // Font: tipo de letra (nombre, estilo y tamanio)
import java.awt.GridLayout; // GridLayout: layout que reparte el espacio en una grilla de filas y columnas iguales
import java.awt.Image; // Image: imagen en memoria, se usa para poder achicar el logo
import java.net.URL; // URL: direccion de un recurso, aca la ruta de la imagen dentro del proyecto
import javax.swing.BorderFactory; // BorderFactory: fabrica de bordes (vacios, con linea, etc.) para los componentes
import javax.swing.ImageIcon; // ImageIcon: convierte una imagen en un icono que se puede poner en una etiqueta
import javax.swing.JButton; // JButton: boton que se puede apretar
import javax.swing.JFrame; // JFrame: ventana principal con barra de titulo, boton de cerrar, etc.
import javax.swing.JLabel; // JLabel: etiqueta, muestra un texto y/o una imagen que el usuario no edita
import javax.swing.JOptionPane; // JOptionPane: cuadros de dialogo listos (mensajes, confirmaciones)
import javax.swing.JPanel; // JPanel: panel, un contenedor rectangular donde se agrupan otros componentes
import javax.swing.JTable; // JTable: tabla con filas y columnas para mostrar datos
import javax.swing.JTextField; // JTextField: campo de texto de una sola linea donde se escribe
import javax.swing.table.DefaultTableModel; // DefaultTableModel: modelo que guarda los datos (filas y columnas) que muestra una JTable

public class VentanaPrincipal extends JFrame { // clase de la ventana; extends JFrame = hereda de JFrame, o sea ES una ventana y usa sus metodos (setTitle, add...)

    private DefaultTableModel modeloCaballos; // modelo (datos) de la tabla de caballos; private: solo se toca desde esta clase, el controlador usa getters
    private DefaultTableModel modeloMovimientos; // modelo (datos) de la tabla de cabalgatas del caballo seleccionado
    private DefaultTableModel modeloAgenda; // modelo (datos) de la tabla de la agenda del dia
    private JTable tablaCaballos; // tabla que muestra los caballos, usa modeloCaballos
    private JTable tablaMovimientos; // tabla que muestra las cabalgatas (movimientos) del caballo seleccionado
    private JTable tablaAgenda; // tabla que muestra la agenda del dia
    private JButton btnAgregarCaballo; // boton para agregar un caballo
    private JButton btnModificarCaballo; // boton para modificar el caballo seleccionado
    private JButton btnEliminarCaballo; // boton para eliminar el caballo seleccionado
    private JButton btnAgregarMovimiento; // boton para agregar una cabalgata
    private JButton btnGrupoMovimiento; // boton para agregar un grupo de cabalgatas
    private JButton btnModificarMovimiento; // boton para modificar la cabalgata seleccionada
    private JButton btnEliminarMovimiento; // boton para eliminar la cabalgata seleccionada
    private JButton btnDiaAnterior; // boton '<' de la agenda: va al dia anterior
    private JButton btnDiaSiguiente; // boton '>' de la agenda: va al dia siguiente
    private JButton btnHoy; // boton 'Hoy' de la agenda: vuelve a la fecha actual
    private JTextField txtFechaAgenda; // campo de texto donde se muestra y se escribe la fecha de la agenda
    private JTextField txtBusqueda; // campo de texto del buscador de caballos (por nombre o raza)
    private JLabel lblResumenAgenda; // etiqueta que muestra el resumen de la agenda (por ejemplo cuantas cabalgatas hay)

    public VentanaPrincipal() { // constructor: se ejecuta al crear la ventana (new VentanaPrincipal()) y arma toda la pantalla
        setTitle("Gestion de Cabalgatas"); // pone el titulo de la ventana (barra de arriba)
        setSize(1280, 720); // tamanio de la ventana: 1280 de ancho por 720 de alto, en pixeles
        setDefaultCloseOperation(EXIT_ON_CLOSE); // al cerrar la ventana termina el programa entero (EXIT_ON_CLOSE)
        setLocationRelativeTo(null); // centra la ventana en la pantalla (null = relativo a la pantalla, no a otra ventana)
        setLayout(new BorderLayout()); // usa BorderLayout para toda la ventana: norte (encabezado) y centro (contenido)
        getContentPane().setBackground(Estilo.FONDO); // getContentPane es el panel interno de la ventana; le pone el color de fondo definido en la clase Estilo

        modeloCaballos = crearModelo(new String[]{"ID", "Nombre", "Raza", "Edad", "Pelaje", "Estado"}); // crea el modelo con las columnas indicadas (array de nombres) usando el metodo crearModelo de abajo
        tablaCaballos = Estilo.tabla(modeloCaballos, Estilo.VERDE); // arma la tabla de caballos con el estilo de la app (Estilo.tabla) usando ese modelo y encabezado verde
        btnAgregarCaballo = Estilo.boton("Agregar", Estilo.VERDE, "mas"); // crea el boton 'Agregar' con color verde e icono 'mas' (Estilo.boton centraliza el aspecto de los botones)
        btnModificarCaballo = Estilo.boton("Modificar", Estilo.VERDE, "lapiz"); // boton 'Modificar' con icono de lapiz
        btnEliminarCaballo = Estilo.boton("Eliminar", Estilo.VERDE, "tacho"); // boton 'Eliminar' con icono de tacho

        modeloMovimientos = crearModelo(new String[]{"ID", "Tipo", "Fecha", "Hora", "Cliente", "Duracion", "Precio base", "Precio final"}); // modelo de la tabla de cabalgatas con sus 8 columnas
        tablaMovimientos = Estilo.tabla(modeloMovimientos, Estilo.VERDE); // tabla de cabalgatas con estilo verde
        btnAgregarMovimiento = Estilo.boton("Agregar", Estilo.VERDE, "mas"); // boton 'Agregar' de cabalgatas
        btnGrupoMovimiento = Estilo.boton("Grupo", Estilo.VERDE, "mas"); // boton 'Grupo' de cabalgatas (agrega varias juntas)
        btnModificarMovimiento = Estilo.boton("Modificar", Estilo.VERDE, "lapiz"); // boton 'Modificar' de cabalgatas
        btnEliminarMovimiento = Estilo.boton("Eliminar", Estilo.VERDE, "tacho"); // boton 'Eliminar' de cabalgatas

        JPanel izquierda = new JPanel(new GridLayout(2, 1, 0, 10)); // panel izquierdo con GridLayout de 2 filas y 1 columna (dos tarjetas una arriba de otra); 0 y 10 = separacion horizontal y vertical
        izquierda.setOpaque(false); // setOpaque(false): panel transparente, deja ver el fondo que tiene atras
        izquierda.add(Estilo.tarjeta("Caballos", crearBusqueda(), tablaCaballos, btnAgregarCaballo, btnModificarCaballo, btnEliminarCaballo)); // agrega arriba la tarjeta 'Caballos' (Estilo.tarjeta arma titulo + barra de busqueda + tabla + botones)
        izquierda.add(Estilo.tarjeta("Cabalgatas del caballo seleccionado", null, tablaMovimientos, btnAgregarMovimiento, btnGrupoMovimiento, btnModificarMovimiento, btnEliminarMovimiento)); // agrega abajo la tarjeta de cabalgatas; null = esta tarjeta no tiene barra de busqueda

        JPanel centro = new JPanel(new BorderLayout(12, 0)); // panel central con BorderLayout; 12 = separacion horizontal entre sus partes, 0 = vertical
        centro.setBackground(Estilo.FONDO); // le pone el color de fondo de la app
        centro.setBorder(BorderFactory.createEmptyBorder(5, 14, 12, 14)); // borde vacio = margen interno (arriba 5, izquierda 14, abajo 12, derecha 14 pixeles)
        centro.add(izquierda, BorderLayout.CENTER); // pone el panel izquierdo en el CENTER: ocupa todo el espacio que sobra
        centro.add(crearAgenda(), BorderLayout.EAST); // pone la agenda a la derecha (EAST); crearAgenda() devuelve el panel ya armado

        add(crearEncabezado(), BorderLayout.NORTH); // agrega el encabezado (logo y titulo) arriba de la ventana
        add(centro, BorderLayout.CENTER); // agrega el panel central en el medio de la ventana
    } // fin del constructor

    private JPanel crearBusqueda() { // metodo privado: arma la barra de busqueda y devuelve un JPanel; private porque solo lo usa el constructor
        JLabel etiqueta = new JLabel("Buscar por nombre o raza:"); // crea la etiqueta con el texto que explica para que sirve el buscador
        etiqueta.setForeground(Estilo.TEXTO); // color del texto de la etiqueta (Estilo.TEXTO es una constante de color)
        etiqueta.setFont(new Font("SansSerif", Font.BOLD, 12)); // letra SansSerif, en negrita (Font.BOLD), tamanio 12

        txtBusqueda = new JTextField(20); // crea el campo de texto con ancho para unos 20 caracteres
        Estilo.campo(txtBusqueda); // le aplica el estilo comun de campos de texto de la app (Estilo.campo)

        JPanel barra = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0)); // panel con FlowLayout alineado a la izquierda; 8 = separacion horizontal entre componentes, 0 = vertical
        barra.setOpaque(false); // panel transparente
        barra.add(etiqueta); // agrega la etiqueta al panel (queda a la izquierda)
        barra.add(txtBusqueda); // agrega el campo de texto al lado de la etiqueta
        return barra; // devuelve el panel armado para que el constructor lo use en la tarjeta
    } // fin del metodo

    private JPanel crearAgenda() { // metodo privado que arma el recuadro 'Agenda del dia' y lo devuelve como JPanel
        modeloAgenda = crearModelo(new String[]{"Hora", "Caballo", "Tipo", "Cliente"}); // modelo de la tabla de agenda, con 4 columnas
        tablaAgenda = Estilo.tabla(modeloAgenda, Estilo.VERDE); // tabla de la agenda con estilo verde

        btnDiaAnterior = Estilo.boton("<", Estilo.VERDE); // boton '<' (sin icono: por eso usa la version de Estilo.boton con solo texto y color)
        btnDiaSiguiente = Estilo.boton(">", Estilo.VERDE); // boton '>' para pasar al dia siguiente
        btnHoy = Estilo.boton("Hoy", Estilo.VERDE); // boton 'Hoy' para volver a la fecha actual
        txtFechaAgenda = new JTextField(8); // campo de texto de la fecha, con ancho para unos 8 caracteres
        txtFechaAgenda.setHorizontalAlignment(JTextField.CENTER); // centra el texto dentro del campo (JTextField.CENTER)
        Estilo.campo(txtFechaAgenda); // le aplica el estilo comun de campos de texto

        JPanel barra = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 0)); // barra superior de la agenda: FlowLayout centrado, 6 = separacion horizontal entre componentes
        barra.setOpaque(false); // panel transparente
        barra.add(btnDiaAnterior); // agrega el boton '<' (queda primero, a la izquierda)
        barra.add(txtFechaAgenda); // agrega el campo de fecha en el medio
        barra.add(btnDiaSiguiente); // agrega el boton '>' a la derecha de la fecha
        barra.add(btnHoy); // agrega el boton 'Hoy' al final

        lblResumenAgenda = new JLabel(" "); // etiqueta del resumen; arranca con un espacio para que ocupe su alto aunque no tenga texto
        lblResumenAgenda.setForeground(Estilo.TEXTO); // color del texto de la etiqueta
        lblResumenAgenda.setFont(new Font("SansSerif", Font.BOLD, 12)); // letra SansSerif, negrita, tamanio 12
        lblResumenAgenda.setBorder(BorderFactory.createEmptyBorder(10, 14, 12, 14)); // margen vacio alrededor del resumen (arriba 10, izquierda 14, abajo 12, derecha 14)

        JPanel agenda = Estilo.tarjetaConBarra("Agenda del dia", barra, tablaAgenda, lblResumenAgenda); // arma la tarjeta 'Agenda del dia' con la barra de botones arriba, la tabla en el medio y el resumen abajo
        agenda.setPreferredSize(new Dimension(380, 10)); // setPreferredSize: tamanio preferido; ancho 380 fijo y alto 10 (el alto lo estira el BorderLayout al ponerla en el EAST)
        return agenda; // devuelve el panel de la agenda para ponerlo en la ventana
    } // fin del metodo

    private JLabel crearEncabezado() { // metodo privado que arma el encabezado; devuelve un JLabel porque el logo y el titulo van en la misma etiqueta
        JLabel encabezado = new JLabel("Cabalgatas", JLabel.CENTER); // crea la etiqueta con el texto 'Cabalgatas' centrado (JLabel.CENTER)
        encabezado.setFont(new Font("SansSerif", Font.BOLD, 28)); // letra SansSerif, negrita, tamanio 28 (titulo grande)
        encabezado.setForeground(Estilo.TEXTO); // color del texto
        encabezado.setIconTextGap(18); // separacion de 18 pixeles entre el icono (logo) y el texto
        encabezado.setBorder(BorderFactory.createEmptyBorder(8, 0, 8, 0)); // margen vacio de 8 pixeles arriba y abajo
        URL url = getClass().getResource("/imagenes/logoo.png"); // getResource busca el archivo dentro del proyecto (en la carpeta imagenes); si no lo encuentra devuelve null
        if (url != null) { // si la imagen existe (url no es null), la usa; asi el programa no se rompe si falta el logo
            Image imagen = new ImageIcon(url).getImage().getScaledInstance(-1, 80, Image.SCALE_SMOOTH); // carga la imagen y la escala: -1 = alto automatico para mantener la proporcion, 80 = ancho de 80; SCALE_SMOOTH = escalado suave
            encabezado.setIcon(new ImageIcon(imagen)); // convierte la imagen escalada en icono y se lo pone a la etiqueta, a la izquierda del texto
        } // fin del if
        return encabezado; // devuelve la etiqueta lista para ponerla al norte de la ventana
    } // fin del metodo

    private DefaultTableModel crearModelo(String[] columnas) { // metodo privado que crea un modelo de tabla a partir de un array con los nombres de las columnas
        return new DefaultTableModel(columnas, 0) { // crea un DefaultTableModel con esas columnas y 0 filas; las llaves siguientes son una clase anonima (subclase sin nombre) que redefine un metodo
            @Override // @Override: avisa que se esta reescribiendo un metodo heredado de DefaultTableModel
            public boolean isCellEditable(int fila, int columna) { // isCellEditable decide si el usuario puede editar una celda; recibe la fila y la columna
                return false; // devuelve false: ninguna celda se puede editar, las tablas son solo para mostrar
            } // fin del metodo
        }; // fin de la clase anonima; el punto y coma cierra el return
    } // fin del metodo crearModelo

    public void mostrarMensaje(String mensaje) { // metodo publico: el controlador lo llama para mostrar un aviso, la vista no decide cuando mostrarlo
        JOptionPane.showMessageDialog(this, mensaje); // muestra un cuadro de dialogo con el mensaje; this = esta ventana es la duena del cuadro
    } // fin del metodo

    public void setFechaAgenda(String fecha) { // setter publico: el controlador de agenda le pasa la fecha (texto) para mostrarla en el campo
        txtFechaAgenda.setText(fecha); // escribe el texto de la fecha dentro del campo txtFechaAgenda
    } // fin del metodo

    public void setResumenAgenda(String resumen) { // setter publico: ControladorAgenda le pasa el texto del resumen del dia
        lblResumenAgenda.setText(resumen); // escribe ese texto en la etiqueta del resumen
    } // fin del metodo

    public String getTextoBusqueda() { // getter: devuelve el texto escrito en el buscador; lo usa ControladorPrincipal para filtrar los caballos
        return txtBusqueda.getText().trim(); // getText lee lo que hay en el campo y trim() le saca los espacios del principio y del final
    } // fin del metodo

    public void setTextoBusqueda(String texto) { // setter: ControladorPrincipal lo usa para cambiar el texto del buscador (por ejemplo para vaciarlo)
        txtBusqueda.setText(texto); // escribe el texto en el campo de busqueda
    } // fin del metodo

    public JTextField getTxtBusqueda() { // getter: devuelve el campo de busqueda completo para que ControladorPrincipal le agregue un listener de teclado
        return txtBusqueda; // devuelve el atributo privado txtBusqueda
    } // fin del metodo

    public String getTextoFechaAgenda() { // getter: devuelve la fecha escrita en el campo de la agenda; lo usa ControladorAgenda
        return txtFechaAgenda.getText().trim(); // lee el texto del campo y le saca los espacios sobrantes con trim()
    } // fin del metodo

    public DefaultTableModel getModeloCaballos() { // getter: devuelve el modelo de la tabla de caballos; los controladores lo usan para cargar y leer filas (la vista no tiene logica)
        return modeloCaballos; // devuelve el atributo privado modeloCaballos
    } // fin del metodo

    public DefaultTableModel getModeloMovimientos() { // getter: devuelve el modelo de la tabla de cabalgatas; lo usan ControladorPrincipal y ControladorAgenda
        return modeloMovimientos; // devuelve modeloMovimientos
    } // fin del metodo

    public DefaultTableModel getModeloAgenda() { // getter: devuelve el modelo de la tabla de la agenda; lo usa ControladorAgenda para cargar las filas
        return modeloAgenda; // devuelve modeloAgenda
    } // fin del metodo

    public JTable getTablaCaballos() { // getter: devuelve la tabla de caballos; ControladorPrincipal pregunta que fila esta seleccionada
        return tablaCaballos; // devuelve tablaCaballos
    } // fin del metodo

    public JTable getTablaMovimientos() { // getter: devuelve la tabla de cabalgatas; ControladorPrincipal lee la fila seleccionada
        return tablaMovimientos; // devuelve tablaMovimientos
    } // fin del metodo

    public JButton getBtnAgregarCaballo() { // getter: devuelve el boton Agregar caballo; ControladorPrincipal le pone la accion con addActionListener
        return btnAgregarCaballo; // devuelve btnAgregarCaballo
    } // fin del metodo

    public JButton getBtnModificarCaballo() { // getter: boton Modificar caballo, lo usa ControladorPrincipal
        return btnModificarCaballo; // devuelve btnModificarCaballo
    } // fin del metodo

    public JButton getBtnEliminarCaballo() { // getter: boton Eliminar caballo, lo usa ControladorPrincipal
        return btnEliminarCaballo; // devuelve btnEliminarCaballo
    } // fin del metodo

    public JButton getBtnAgregarMovimiento() { // getter: boton Agregar cabalgata, lo usa ControladorPrincipal
        return btnAgregarMovimiento; // devuelve btnAgregarMovimiento
    } // fin del metodo

    public JButton getBtnGrupoMovimiento() { // getter: boton Grupo, lo usa ControladorPrincipal para agregar un grupo de cabalgatas
        return btnGrupoMovimiento; // devuelve btnGrupoMovimiento
    } // fin del metodo

    public JButton getBtnModificarMovimiento() { // getter: boton Modificar cabalgata, lo usa ControladorPrincipal
        return btnModificarMovimiento; // devuelve btnModificarMovimiento
    } // fin del metodo

    public JButton getBtnEliminarMovimiento() { // getter: boton Eliminar cabalgata, lo usa ControladorPrincipal
        return btnEliminarMovimiento; // devuelve btnEliminarMovimiento
    } // fin del metodo

    public JButton getBtnDiaAnterior() { // getter: boton '<' de la agenda, lo usa ControladorAgenda
        return btnDiaAnterior; // devuelve btnDiaAnterior
    } // fin del metodo

    public JButton getBtnDiaSiguiente() { // getter: boton '>' de la agenda, lo usa ControladorAgenda
        return btnDiaSiguiente; // devuelve btnDiaSiguiente
    } // fin del metodo

    public JButton getBtnHoy() { // getter: boton 'Hoy' de la agenda, lo usa ControladorAgenda
        return btnHoy; // devuelve btnHoy
    } // fin del metodo

    public JTextField getTxtFechaAgenda() { // getter: campo de fecha de la agenda; ControladorAgenda le escucha el Enter
        return txtFechaAgenda; // devuelve txtFechaAgenda
    } // fin del metodo
} // fin de la clase VentanaPrincipal