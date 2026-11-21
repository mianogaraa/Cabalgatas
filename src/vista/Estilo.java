package vista; // paquete (carpeta) donde esta esta clase: vista, o sea las clases que se ven en pantalla

import java.awt.BasicStroke; // BasicStroke: define como es el trazo de una linea (grosor y como terminan las puntas)
import java.awt.BorderLayout; // BorderLayout: distribuidor que acomoda componentes en norte, sur, este, oeste y centro
import java.awt.Color; // Color: clase que representa un color (se arma con valores de rojo, verde y azul)
import java.awt.Component; // Component: clase base de todo lo que se ve en Swing (botones, etiquetas, celdas, etc.)
import java.awt.Cursor; // Cursor: el dibujito del puntero del mouse (flecha, manito, etc.)
import java.awt.Dimension; // Dimension: guarda un ancho y un alto
import java.awt.FlowLayout; // FlowLayout: distribuidor que acomoda componentes en fila, uno al lado del otro
import java.awt.Font; // Font: tipo de letra (nombre, estilo y tamanio)
import java.awt.Graphics; // Graphics: herramienta basica para dibujar sobre un componente (el pincel)
import java.awt.Graphics2D; // Graphics2D: version mas completa de Graphics, con formas, trazos y suavizado
import java.awt.RenderingHints; // RenderingHints: ajustes de calidad de dibujo, por ejemplo el antialiasing (bordes suaves)
import java.awt.geom.RoundRectangle2D; // RoundRectangle2D: figura de rectangulo con esquinas redondeadas
import javax.swing.BorderFactory; // BorderFactory: fabrica que crea bordes (vacios, de linea, compuestos) para los componentes
import javax.swing.Icon; // Icon: interfaz para cualquier dibujito chico que se pone al lado del texto de un boton
import javax.swing.JButton; // JButton: el boton de Swing
import javax.swing.JComponent; // JComponent: clase base de los componentes de Swing (sirve para recibir cualquier componente)
import javax.swing.JLabel; // JLabel: etiqueta, un texto en pantalla
import javax.swing.JPanel; // JPanel: panel, una caja que contiene otros componentes
import javax.swing.JScrollPane; // JScrollPane: contenedor con barras de desplazamiento (scroll)
import javax.swing.JTable; // JTable: tabla de filas y columnas
import javax.swing.JTextField; // JTextField: campo de texto de una sola linea
import javax.swing.plaf.basic.BasicButtonUI; // BasicButtonUI: aspecto basico de un boton, sin el estilo propio del sistema (nos deja pintarlo a nosotros)
import javax.swing.table.DefaultTableModel; // DefaultTableModel: el modelo, o sea los datos que muestra la tabla
import javax.swing.table.JTableHeader; // JTableHeader: la fila de encabezados de una tabla (los titulos de las columnas)
import javax.swing.table.TableCellRenderer; // TableCellRenderer: el que dibuja cada celda de la tabla

public class Estilo { // clase Estilo: caja de herramientas con colores y metodos para que toda la app tenga el mismo aspecto

    public static final Color FONDO = new Color(238, 235, 227); // public: se ve desde cualquier clase; static: es de la clase, no hace falta crear objeto; final: constante que no cambia. Beige claro (rojo 238, verde 235, azul 227)
    public static final Color VERDE = new Color(122, 132, 102); // VERDE: verde grisaceo (valores de 0 a 255); se usa en la franja de titulo de las tarjetas
    public static final Color GRIS = new Color(158, 158, 148); // GRIS: gris suave, casi beige
    public static final Color TEXTO = new Color(59, 63, 51); // TEXTO: verde muy oscuro, casi negro, para las letras
    public static final Color TARJETA = new Color(247, 245, 239); // TARJETA: beige muy clarito, el fondo de las tarjetas
    public static final Color BORDE = new Color(214, 209, 196); // BORDE: beige medio, para los bordes finos
    public static final Color FILA_ALTERNA = new Color(233, 229, 220); // FILA_ALTERNA: beige un poco mas oscuro, para las filas de la tabla que se intercalan con blanco
    public static final Color SELECCION = new Color(200, 208, 180); // SELECCION: verde clarito, para la fila seleccionada de la tabla

    private Estilo() { // constructor privado: nadie puede hacer new Estilo(), la clase se usa solo con sus metodos estaticos
    } // fin del constructor

    public static JButton boton(String texto, Color color) { // metodo estatico que crea un boton con ese texto y color, sin icono; devuelve el JButton
        return boton(texto, color, null); // llama a la otra version de boton pasando null (o sea, sin icono)
    } // fin del metodo

    public static JButton boton(String texto, Color color, String icono) { // version completa de boton: icono es el nombre del dibujito ("mas", "lapiz", "tacho") o null si no lleva
        JButton boton = new JButton(texto) { // crea el boton y abre una clase anonima (subclase sin nombre) para cambiar como se dibuja
            @Override // avisa que este metodo reemplaza al que ya trae JButton
            protected void paintComponent(Graphics g) { // paintComponent: Swing lo llama cada vez que hay que dibujar el boton; g es el pincel
                Graphics2D g2 = (Graphics2D) g.create(); // g2 es una copia del pincel con mas funciones; trabajar sobre una copia no altera el original
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); // antialiasing: suaviza los bordes de lo que se dibuja para que no se vean escalonados
                Color fondo = getBackground(); // toma el color de fondo actual del boton
                if (getModel().isPressed()) { // getModel guarda el estado del boton; isPressed es true si lo estan apretando
                    fondo = fondo.darker(); // darker: lo oscurece un poco, para dar efecto de apretado
                } else if (getModel().isRollover()) { // si no, pregunta si el mouse esta encima del boton (rollover)
                    fondo = fondo.brighter(); // brighter: lo aclara un poco, como efecto de resaltado
                } // fin del if / else if
                g2.setColor(fondo); // elige el color con el que va a pintar
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14); // fillRoundRect: rellena un rectangulo redondeado; x=0, y=0 (esquina sup izq), ancho y alto del boton, esquinas de 14 px
                g2.dispose(); // dispose: libera la copia del pincel, ya no se usa
                super.paintComponent(g); // llama al dibujo original de JButton: encima del fondo pinta el texto y el icono
            } // fin de paintComponent
        }; // fin de la clase anonima; el punto y coma cierra la declaracion del boton
        boton.setUI(new BasicButtonUI()); // aplica el aspecto basico, asi el sistema no pinta su propio fondo
        boton.setBackground(color); // le pone al boton el color recibido como fondo
        boton.setForeground(Color.WHITE); // color de la letra: blanco
        boton.setFont(new Font("SansSerif", Font.BOLD, 12)); // letra SansSerif (sin adornos), negrita, tamanio 12
        boton.setFocusPainted(false); // saca el recuadro punteado que aparece cuando el boton tiene el foco
        boton.setContentAreaFilled(false); // el boton no pinta su fondo por defecto, porque lo dibujamos nosotros
        boton.setOpaque(false); // no es opaco: deja ver lo que hay detras, necesario por las esquinas redondeadas
        boton.setRolloverEnabled(true); // activa que el boton detecte cuando el mouse pasa por encima
        boton.setBorder(BorderFactory.createEmptyBorder(7, 16, 7, 16)); // borde vacio que hace de margen interno: 7 px arriba, 16 izquierda, 7 abajo, 16 derecha
        boton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); // el puntero se convierte en manito al pasar por el boton
        if (icono != null) { // si recibio nombre de icono (null significa sin icono)
            boton.setIcon(new Icono(icono)); // crea un Icono dibujado por nosotros (clase de abajo) y se lo pone al boton
            boton.setIconTextGap(8); // separa el icono del texto 8 pixeles
        } // fin del if
        return boton; // devuelve el boton ya armado
    } // fin del metodo

    public static JLabel titulo(String texto, Color color) { // crea una etiqueta tipo banda de titulo, con fondo de color
        JLabel titulo = new JLabel(texto); // crea la etiqueta con ese texto
        titulo.setOpaque(true); // opaca, asi se ve el color de fondo (las etiquetas por defecto son transparentes)
        titulo.setBackground(color); // pone el color recibido como fondo
        titulo.setForeground(Color.WHITE); // letra blanca
        titulo.setFont(new Font("SansSerif", Font.BOLD, 14)); // letra SansSerif, negrita, tamanio 14
        titulo.setBorder(BorderFactory.createEmptyBorder(8, 14, 8, 14)); // margen vacio: 8 px arriba y abajo, 14 a izquierda y derecha
        return titulo; // devuelve la etiqueta lista
    } // fin del metodo

    public static void campo(JTextField campo) { // void: no devuelve nada; recibe un campo de texto ya creado y le aplica el estilo
        campo.setFont(new Font("SansSerif", Font.PLAIN, 12)); // letra SansSerif normal (PLAIN), tamanio 12
        campo.setForeground(TEXTO); // color de letra: el verde oscuro TEXTO
        campo.setBorder(BorderFactory.createCompoundBorder( // borde compuesto: junta dos bordes, uno por fuera y otro por dentro
                BorderFactory.createLineBorder(BORDE), // borde de afuera: una linea fina del color BORDE
                BorderFactory.createEmptyBorder(4, 6, 4, 6))); // borde de adentro: margen vacio de 4 arriba, 6 izquierda, 4 abajo, 6 derecha; cierra los parentesis
    } // fin del metodo

    public static JTable tabla(DefaultTableModel modelo, Color colorEncabezado) { // crea una tabla con los datos del modelo y el color de encabezado que se le pase
        JTable tabla = new JTable(modelo) { // crea la tabla con ese modelo y abre una clase anonima para personalizar como pinta las celdas
            @Override // avisa que este metodo reemplaza al de JTable
            public Component prepareRenderer(TableCellRenderer renderer, int fila, int columna) { // prepareRenderer: Swing lo llama por cada celda antes de dibujarla; renderer es el que dibuja la celda
                Component celda = super.prepareRenderer(renderer, fila, columna); // pide la celda ya armada a la version original
                if (isRowSelected(fila)) { // pregunta si esa fila esta seleccionada
                    celda.setBackground(SELECCION); // si esta seleccionada, fondo verde clarito
                } else { // si no esta seleccionada
                    celda.setBackground(fila % 2 == 0 ? Color.WHITE : FILA_ALTERNA); // fila % 2 es el resto de dividir por 2: filas pares blancas, impares beige (el ? : es un if corto)
                } // fin del if / else
                celda.setForeground(TEXTO); // letra de la celda en verde oscuro
                return celda; // devuelve la celda ya pintada para que Swing la dibuje
            } // fin de prepareRenderer
        }; // fin de la clase anonima
        tabla.setRowHeight(26); // cada fila mide 26 px de alto
        tabla.setShowVerticalLines(false); // oculta las lineas verticales entre columnas
        tabla.setGridColor(FILA_ALTERNA); // las lineas de la grilla toman un beige suave
        tabla.setFont(new Font("SansSerif", Font.PLAIN, 12)); // letra SansSerif normal, tamanio 12
        tabla.setFillsViewportHeight(true); // la tabla rellena todo el alto del scroll aunque tenga pocas filas

        JTableHeader encabezado = tabla.getTableHeader(); // obtiene el encabezado de la tabla (la fila con los titulos de columnas)
        encabezado.setBackground(colorEncabezado); // fondo del encabezado: el color recibido
        encabezado.setForeground(Color.WHITE); // letra blanca
        encabezado.setFont(new Font("SansSerif", Font.BOLD, 12)); // letra SansSerif negrita, tamanio 12
        encabezado.setReorderingAllowed(false); // no deja arrastrar las columnas para cambiarlas de lugar
        return tabla; // devuelve la tabla ya configurada
    } // fin del metodo

    public static JPanel tarjeta(String titulo, JComponent barra, JTable tabla, JButton... botones) { // arma una tarjeta con titulo, barra opcional, tabla y botones; JButton... (varargs) acepta cualquier cantidad de botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 8)); // panel con los botones en fila, alineados a la derecha, con 8 px de separacion horizontal y vertical
        panelBotones.setOpaque(false); // transparente: deja ver el fondo de la tarjeta
        for (JButton boton : botones) { // for-each: recorre uno por uno los botones recibidos
            panelBotones.add(boton); // agrega cada boton al panel
        } // fin del for
        return tarjetaConBarra(titulo, barra, tabla, panelBotones); // delega en el otro metodo, pasando el panel de botones como pie de la tarjeta
    } // fin del metodo

    public static JPanel tarjetaConBarra(String titulo, JComponent barra, JTable tabla, JComponent pie) { // igual que tarjeta, pero el pie (parte de abajo) puede ser cualquier componente
        Tarjeta tarjeta = new Tarjeta(); // crea la tarjeta (clase interna de abajo): el panel redondeado con sombra

        JLabel etiqueta = new JLabel(titulo); // etiqueta con el titulo, va sobre la franja verde
        etiqueta.setForeground(Color.WHITE); // letra blanca
        etiqueta.setFont(new Font("SansSerif", Font.BOLD, 15)); // letra SansSerif negrita, tamanio 15
        etiqueta.setBorder(BorderFactory.createEmptyBorder(0, 16, 0, 16)); // margen vacio de 16 px a izquierda y derecha
        etiqueta.setPreferredSize(new Dimension(10, Tarjeta.ALTO_TITULO)); // tamanio preferido: ancho 10 (el layout lo estira) y alto 38, el de la franja del titulo

        JScrollPane scroll = new JScrollPane(tabla); // mete la tabla en un scroll, asi aparecen barras si hay muchas filas
        scroll.setBorder(BorderFactory.createLineBorder(BORDE)); // borde de linea fina del color BORDE
        scroll.getViewport().setBackground(Color.WHITE); // el viewport es la ventana por donde se ve la tabla; le pone fondo blanco

        JPanel centro = new JPanel(new BorderLayout()); // panel central con BorderLayout
        centro.setOpaque(false); // transparente
        centro.setBorder(BorderFactory.createEmptyBorder(10, 12, 0, 12)); // margen vacio: 10 arriba, 12 izquierda, 0 abajo, 12 derecha
        centro.add(scroll, BorderLayout.CENTER); // el scroll con la tabla ocupa el centro, todo el espacio disponible
        if (barra != null) { // la barra es opcional (por ejemplo un buscador); solo se agrega si llego una
            barra.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0)); // le pone 8 px de margen abajo para separarla de la tabla
            centro.add(barra, BorderLayout.NORTH); // la pone arriba de la tabla (norte)
        } // fin del if

        tarjeta.add(etiqueta, BorderLayout.NORTH); // el titulo va arriba de la tarjeta, sobre la franja verde
        tarjeta.add(centro, BorderLayout.CENTER); // el panel central va en el medio
        tarjeta.add(pie, BorderLayout.SOUTH); // el pie va abajo (sur)
        return tarjeta; // devuelve la tarjeta armada
    } // fin del metodo

    private static class Tarjeta extends JPanel { // clase interna privada; static: no depende de un objeto Estilo; extends JPanel: es un panel con dibujo propio

        private static final int RADIO = 18; // RADIO: cuanto se redondean las esquinas, en pixeles
        private static final int SOMBRA = 4; // SOMBRA: cuantos pixeles se corre la sombra hacia abajo y a la derecha
        private static final int ALTO_TITULO = 38; // ALTO_TITULO: alto en pixeles de la franja verde del titulo

        Tarjeta() { // constructor de la tarjeta (mismo nombre que la clase)
            super(new BorderLayout()); // super llama al constructor de JPanel, pasandole un BorderLayout
            setOpaque(false); // transparente: las esquinas redondeadas dejan ver el fondo de atras
            setBorder(BorderFactory.createEmptyBorder(0, 0, SOMBRA, SOMBRA)); // margen vacio de 4 px abajo y a la derecha, reservado para dibujar la sombra
        } // fin del constructor

        @Override // avisa que este metodo reemplaza al de JPanel
        protected void paintComponent(Graphics g) { // Swing lo llama para dibujar la tarjeta; g es el pincel
            Graphics2D g2 = (Graphics2D) g.create(); // copia del pincel con mas funciones
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); // activa el antialiasing (bordes suaves)
            int ancho = getWidth() - SOMBRA; // ancho de la tarjeta sin el espacio reservado para la sombra
            int alto = getHeight() - SOMBRA; // alto de la tarjeta sin el espacio reservado para la sombra

            g2.setColor(new Color(0, 0, 0, 30)); // negro casi transparente: el 4to numero es la opacidad (0 a 255), 30 es muy suave; sera la sombra
            g2.fillRoundRect(SOMBRA, SOMBRA, ancho, alto, RADIO, RADIO); // rectangulo redondeado corrido 4 px a la derecha y abajo (x=4, y=4): se ve como sombra

            g2.setColor(TARJETA); // elige el beige de la tarjeta
            g2.fillRoundRect(0, 0, ancho, alto, RADIO, RADIO); // rectangulo redondeado beige desde la esquina sup izq (0,0): tapa casi toda la sombra

            g2.clip(new RoundRectangle2D.Float(0, 0, ancho, alto, RADIO, RADIO)); // clip: recorta, desde aca solo se pinta dentro de esta forma redondeada (Float: medidas con decimales)
            g2.setColor(VERDE); // elige el verde
            g2.fillRect(0, 0, ancho, ALTO_TITULO); // rectangulo comun verde arriba (x=0, y=0, ancho completo, alto 38): la franja del titulo, con esquinas de arriba redondeadas por el clip
            g2.dispose(); // libera la copia del pincel

            Graphics2D borde = (Graphics2D) g.create(); // otra copia del pincel, nueva y sin el clip, para dibujar el borde
            borde.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); // activa el antialiasing
            borde.setColor(BORDE); // elige el color del borde
            borde.drawRoundRect(0, 0, ancho - 1, alto - 1, RADIO, RADIO); // drawRoundRect: dibuja solo el contorno, sin rellenar; el -1 evita que la linea se corte en el borde
            borde.dispose(); // libera la copia del pincel
        } // fin de paintComponent
    } // fin de la clase Tarjeta

    private static class Icono implements Icon { // clase interna que dibuja iconos chicos; implements Icon: cumple el contrato de Icon (getIconWidth, getIconHeight, paintIcon)

        private final String tipo; // tipo de icono ("mas", "lapiz", "tacho"); final: se asigna una sola vez

        Icono(String tipo) { // constructor: recibe el tipo de icono
            this.tipo = tipo; // guarda el parametro en el atributo (this.tipo es el atributo)
        } // fin del constructor

        @Override // avisa que este metodo viene del contrato Icon
        public int getIconWidth() { // Swing pregunta cuanto mide de ancho el icono
            return 14; // 14 pixeles de ancho
        } // fin del metodo

        @Override // avisa que este metodo viene del contrato Icon
        public int getIconHeight() { // Swing pregunta cuanto mide de alto el icono
            return 14; // 14 pixeles de alto
        } // fin del metodo

        @Override // avisa que este metodo viene del contrato Icon
        public void paintIcon(Component c, Graphics g, int x, int y) { // dibuja el icono; c es el componente donde va, g el pincel, (x, y) la esquina sup izq donde empieza
            Graphics2D g2 = (Graphics2D) g.create(); // copia del pincel con mas funciones
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); // activa el antialiasing
            g2.setColor(Color.WHITE); // dibuja en blanco
            g2.setStroke(new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND)); // trazo de 2 px de grosor (2f es decimal) con puntas redondeadas (CAP_ROUND) y uniones redondeadas (JOIN_ROUND)
            g2.translate(x, y); // mueve el origen (0,0) a (x,y): asi las coordenadas de abajo son relativas al icono
            if (tipo.equals("mas")) { // equals compara textos; si el icono es el signo mas
                g2.drawLine(7, 2, 7, 12); // drawLine(x1, y1, x2, y2): linea vertical de (7,2) a (7,12)
                g2.drawLine(2, 7, 12, 7); // linea horizontal de (2,7) a (12,7); juntas forman un +
            } else if (tipo.equals("lapiz")) { // si no, si el icono es el lapiz
                g2.drawLine(5, 9, 11, 3); // linea diagonal de (5,9) a (11,3), sube hacia la derecha: el cuerpo del lapiz
                g2.fillPolygon(new int[]{1, 1, 5}, new int[]{13, 9, 13}, 3); // fillPolygon: triangulo relleno con vertices (1,13), (1,9), (5,13), la punta abajo a la izquierda; arrays de x, de y y cantidad de puntos
            } else if (tipo.equals("tacho")) { // si no, si el icono es el tacho de basura
                g2.setStroke(new BasicStroke(1.6f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND)); // cambia a un trazo mas finito (1.6 px) para el tacho
                g2.drawLine(2, 3, 12, 3); // tapa horizontal de (2,3) a (12,3)
                g2.drawLine(5, 1, 9, 1); // manija arriba de la tapa, de (5,1) a (9,1)
                g2.drawLine(3, 5, 4, 13); // lado izquierdo del tacho, inclinado, de (3,5) a (4,13)
                g2.drawLine(11, 5, 10, 13); // lado derecho del tacho, inclinado, de (11,5) a (10,13)
                g2.drawLine(4, 13, 10, 13); // base del tacho, de (4,13) a (10,13)
            } // fin del if / else if
            g2.dispose(); // libera la copia del pincel
        } // fin de paintIcon
    } // fin de la clase Icono
} // fin de la clase Estilo