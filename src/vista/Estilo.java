package vista;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;

public class Estilo {

    public static final Color FONDO = new Color(238, 235, 227);
    public static final Color VERDE = new Color(122, 132, 102);
    public static final Color GRIS = new Color(158, 158, 148);
    public static final Color TEXTO = new Color(59, 63, 51);
    public static final Color TARJETA = new Color(247, 245, 239);
    public static final Color BORDE = new Color(214, 209, 196);
    public static final Color FILA_ALTERNA = new Color(233, 229, 220);
    public static final Color SELECCION = new Color(200, 208, 180);

    private Estilo() {
    }

    public static JButton boton(String texto, Color color) {
        return boton(texto, color, null);
    }

    public static JButton boton(String texto, Color color, String icono) {
        JButton boton = new JButton(texto) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color fondo = getBackground();
                if (getModel().isPressed()) {
                    fondo = fondo.darker();
                } else if (getModel().isRollover()) {
                    fondo = fondo.brighter();
                }
                g2.setColor(fondo);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        boton.setUI(new BasicButtonUI());
        boton.setBackground(color);
        boton.setForeground(Color.WHITE);
        boton.setFont(new Font("SansSerif", Font.BOLD, 12));
        boton.setFocusPainted(false);
        boton.setContentAreaFilled(false);
        boton.setOpaque(false);
        boton.setRolloverEnabled(true);
        boton.setBorder(BorderFactory.createEmptyBorder(7, 16, 7, 16));
        boton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        if (icono != null) {
            boton.setIcon(new Icono(icono));
            boton.setIconTextGap(8);
        }
        return boton;
    }

    public static JLabel titulo(String texto, Color color) {
        JLabel titulo = new JLabel(texto);
        titulo.setOpaque(true);
        titulo.setBackground(color);
        titulo.setForeground(Color.WHITE);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 14));
        titulo.setBorder(BorderFactory.createEmptyBorder(8, 14, 8, 14));
        return titulo;
    }

    public static void campo(JTextField campo) {
        campo.setFont(new Font("SansSerif", Font.PLAIN, 12));
        campo.setForeground(TEXTO);
        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDE),
                BorderFactory.createEmptyBorder(4, 6, 4, 6)));
    }

    public static JTable tabla(DefaultTableModel modelo, Color colorEncabezado) {
        JTable tabla = new JTable(modelo) {
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int fila, int columna) {
                Component celda = super.prepareRenderer(renderer, fila, columna);
                if (isRowSelected(fila)) {
                    celda.setBackground(SELECCION);
                } else {
                    celda.setBackground(fila % 2 == 0 ? Color.WHITE : FILA_ALTERNA);
                }
                celda.setForeground(TEXTO);
                return celda;
            }
        };
        tabla.setRowHeight(26);
        tabla.setShowVerticalLines(false);
        tabla.setGridColor(FILA_ALTERNA);
        tabla.setFont(new Font("SansSerif", Font.PLAIN, 12));
        tabla.setFillsViewportHeight(true);

        JTableHeader encabezado = tabla.getTableHeader();
        encabezado.setBackground(colorEncabezado);
        encabezado.setForeground(Color.WHITE);
        encabezado.setFont(new Font("SansSerif", Font.BOLD, 12));
        encabezado.setReorderingAllowed(false);
        return tabla;
    }

    public static JPanel tarjeta(String titulo, JComponent barra, JTable tabla, JButton... botones) {
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 8));
        panelBotones.setOpaque(false);
        for (JButton boton : botones) {
            panelBotones.add(boton);
        }
        return tarjetaConBarra(titulo, barra, tabla, panelBotones);
    }

    public static JPanel tarjetaConBarra(String titulo, JComponent barra, JTable tabla, JComponent pie) {
        Tarjeta tarjeta = new Tarjeta();

        JLabel etiqueta = new JLabel(titulo);
        etiqueta.setForeground(Color.WHITE);
        etiqueta.setFont(new Font("SansSerif", Font.BOLD, 15));
        etiqueta.setBorder(BorderFactory.createEmptyBorder(0, 16, 0, 16));
        etiqueta.setPreferredSize(new Dimension(10, Tarjeta.ALTO_TITULO));

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createLineBorder(BORDE));
        scroll.getViewport().setBackground(Color.WHITE);

        JPanel centro = new JPanel(new BorderLayout());
        centro.setOpaque(false);
        centro.setBorder(BorderFactory.createEmptyBorder(10, 12, 0, 12));
        centro.add(scroll, BorderLayout.CENTER);
        if (barra != null) {
            barra.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));
            centro.add(barra, BorderLayout.NORTH);
        }

        tarjeta.add(etiqueta, BorderLayout.NORTH);
        tarjeta.add(centro, BorderLayout.CENTER);
        tarjeta.add(pie, BorderLayout.SOUTH);
        return tarjeta;
    }

    private static class Tarjeta extends JPanel {

        private static final int RADIO = 18;
        private static final int SOMBRA = 4;
        private static final int ALTO_TITULO = 38;

        Tarjeta() {
            super(new BorderLayout());
            setOpaque(false);
            setBorder(BorderFactory.createEmptyBorder(0, 0, SOMBRA, SOMBRA));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int ancho = getWidth() - SOMBRA;
            int alto = getHeight() - SOMBRA;

            g2.setColor(new Color(0, 0, 0, 30));
            g2.fillRoundRect(SOMBRA, SOMBRA, ancho, alto, RADIO, RADIO);

            g2.setColor(TARJETA);
            g2.fillRoundRect(0, 0, ancho, alto, RADIO, RADIO);

            g2.clip(new RoundRectangle2D.Float(0, 0, ancho, alto, RADIO, RADIO));
            g2.setColor(VERDE);
            g2.fillRect(0, 0, ancho, ALTO_TITULO);
            g2.dispose();

            Graphics2D borde = (Graphics2D) g.create();
            borde.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            borde.setColor(BORDE);
            borde.drawRoundRect(0, 0, ancho - 1, alto - 1, RADIO, RADIO);
            borde.dispose();
        }
    }

    private static class Icono implements Icon {

        private final String tipo;

        Icono(String tipo) {
            this.tipo = tipo;
        }

        @Override
        public int getIconWidth() {
            return 14;
        }

        @Override
        public int getIconHeight() {
            return 14;
        }

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(Color.WHITE);
            g2.setStroke(new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2.translate(x, y);
            if (tipo.equals("mas")) {
                g2.drawLine(7, 2, 7, 12);
                g2.drawLine(2, 7, 12, 7);
            } else if (tipo.equals("lapiz")) {
                g2.drawLine(5, 9, 11, 3);
                g2.fillPolygon(new int[]{1, 1, 5}, new int[]{13, 9, 13}, 3);
            } else if (tipo.equals("tacho")) {
                g2.setStroke(new BasicStroke(1.6f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.drawLine(2, 3, 12, 3);
                g2.drawLine(5, 1, 9, 1);
                g2.drawLine(3, 5, 4, 13);
                g2.drawLine(11, 5, 10, 13);
                g2.drawLine(4, 13, 10, 13);
            }
            g2.dispose();
        }
    }
}
