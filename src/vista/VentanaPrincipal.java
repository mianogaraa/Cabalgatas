package vista;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.net.URL;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class VentanaPrincipal extends JFrame {

    private DefaultTableModel modeloCaballos;
    private DefaultTableModel modeloMovimientos;
    private DefaultTableModel modeloAgenda;
    private JTable tablaCaballos;
    private JTable tablaMovimientos;
    private JTable tablaAgenda;
    private JButton btnAgregarCaballo;
    private JButton btnModificarCaballo;
    private JButton btnEliminarCaballo;
    private JButton btnAgregarMovimiento;
    private JButton btnModificarMovimiento;
    private JButton btnEliminarMovimiento;
    private JButton btnDiaAnterior;
    private JButton btnDiaSiguiente;
    private JButton btnHoy;
    private JTextField txtFechaAgenda;
    private JTextField txtBusqueda;
    private JLabel lblResumenAgenda;

    public VentanaPrincipal() {
        setTitle("Gestion de Cabalgatas");
        setSize(1280, 720);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Estilo.FONDO);

        modeloCaballos = crearModelo(new String[]{"ID", "Nombre", "Raza", "Edad", "Pelaje", "Estado"});
        tablaCaballos = Estilo.tabla(modeloCaballos, Estilo.VERDE);
        btnAgregarCaballo = Estilo.boton("Agregar", Estilo.VERDE, "mas");
        btnModificarCaballo = Estilo.boton("Modificar", Estilo.VERDE, "lapiz");
        btnEliminarCaballo = Estilo.boton("Eliminar", Estilo.VERDE, "tacho");

        modeloMovimientos = crearModelo(new String[]{"ID", "Tipo", "Fecha", "Hora", "Cliente", "Duracion", "Precio base", "Precio final"});
        tablaMovimientos = Estilo.tabla(modeloMovimientos, Estilo.VERDE);
        btnAgregarMovimiento = Estilo.boton("Agregar", Estilo.VERDE, "mas");
        btnModificarMovimiento = Estilo.boton("Modificar", Estilo.VERDE, "lapiz");
        btnEliminarMovimiento = Estilo.boton("Eliminar", Estilo.VERDE, "tacho");

        JPanel izquierda = new JPanel(new GridLayout(2, 1, 0, 10));
        izquierda.setOpaque(false);
        izquierda.add(Estilo.tarjeta("Caballos", crearBusqueda(), tablaCaballos, btnAgregarCaballo, btnModificarCaballo, btnEliminarCaballo));
        izquierda.add(Estilo.tarjeta("Cabalgatas del caballo seleccionado", null, tablaMovimientos, btnAgregarMovimiento, btnModificarMovimiento, btnEliminarMovimiento));

        JPanel centro = new JPanel(new BorderLayout(12, 0));
        centro.setBackground(Estilo.FONDO);
        centro.setBorder(BorderFactory.createEmptyBorder(5, 14, 12, 14));
        centro.add(izquierda, BorderLayout.CENTER);
        centro.add(crearAgenda(), BorderLayout.EAST);

        add(crearEncabezado(), BorderLayout.NORTH);
        add(centro, BorderLayout.CENTER);
    }

    private JPanel crearBusqueda() {
        JLabel etiqueta = new JLabel("Buscar por nombre o raza:");
        etiqueta.setForeground(Estilo.TEXTO);
        etiqueta.setFont(new Font("SansSerif", Font.BOLD, 12));

        txtBusqueda = new JTextField(20);
        Estilo.campo(txtBusqueda);

        JPanel barra = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        barra.setOpaque(false);
        barra.add(etiqueta);
        barra.add(txtBusqueda);
        return barra;
    }

    private JPanel crearAgenda() {
        modeloAgenda = crearModelo(new String[]{"Hora", "Caballo", "Tipo", "Cliente"});
        tablaAgenda = Estilo.tabla(modeloAgenda, Estilo.VERDE);

        btnDiaAnterior = Estilo.boton("<", Estilo.VERDE);
        btnDiaSiguiente = Estilo.boton(">", Estilo.VERDE);
        btnHoy = Estilo.boton("Hoy", Estilo.VERDE);
        txtFechaAgenda = new JTextField(8);
        txtFechaAgenda.setHorizontalAlignment(JTextField.CENTER);
        Estilo.campo(txtFechaAgenda);

        JPanel barra = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 0));
        barra.setOpaque(false);
        barra.add(btnDiaAnterior);
        barra.add(txtFechaAgenda);
        barra.add(btnDiaSiguiente);
        barra.add(btnHoy);

        lblResumenAgenda = new JLabel(" ");
        lblResumenAgenda.setForeground(Estilo.TEXTO);
        lblResumenAgenda.setFont(new Font("SansSerif", Font.BOLD, 12));
        lblResumenAgenda.setBorder(BorderFactory.createEmptyBorder(10, 14, 12, 14));

        JPanel agenda = Estilo.tarjetaConBarra("Agenda del dia", barra, tablaAgenda, lblResumenAgenda);
        agenda.setPreferredSize(new Dimension(380, 10));
        return agenda;
    }

    private JLabel crearEncabezado() {
        JLabel encabezado = new JLabel("Cabalgatas", JLabel.CENTER);
        encabezado.setFont(new Font("SansSerif", Font.BOLD, 28));
        encabezado.setForeground(Estilo.TEXTO);
        encabezado.setIconTextGap(18);
        encabezado.setBorder(BorderFactory.createEmptyBorder(8, 0, 8, 0));
        URL url = getClass().getResource("/imagenes/logoo.png");
        if (url != null) {
            Image imagen = new ImageIcon(url).getImage().getScaledInstance(-1, 80, Image.SCALE_SMOOTH);
            encabezado.setIcon(new ImageIcon(imagen));
        }
        return encabezado;
    }

    private DefaultTableModel crearModelo(String[] columnas) {
        return new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };
    }

    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje);
    }

    public void setFechaAgenda(String fecha) {
        txtFechaAgenda.setText(fecha);
    }

    public void setResumenAgenda(String resumen) {
        lblResumenAgenda.setText(resumen);
    }

    public String getTextoBusqueda() {
        return txtBusqueda.getText().trim();
    }

    public void setTextoBusqueda(String texto) {
        txtBusqueda.setText(texto);
    }

    public JTextField getTxtBusqueda() {
        return txtBusqueda;
    }

    public String getTextoFechaAgenda() {
        return txtFechaAgenda.getText().trim();
    }

    public DefaultTableModel getModeloCaballos() {
        return modeloCaballos;
    }

    public DefaultTableModel getModeloMovimientos() {
        return modeloMovimientos;
    }

    public DefaultTableModel getModeloAgenda() {
        return modeloAgenda;
    }

    public JTable getTablaCaballos() {
        return tablaCaballos;
    }

    public JTable getTablaMovimientos() {
        return tablaMovimientos;
    }

    public JButton getBtnAgregarCaballo() {
        return btnAgregarCaballo;
    }

    public JButton getBtnModificarCaballo() {
        return btnModificarCaballo;
    }

    public JButton getBtnEliminarCaballo() {
        return btnEliminarCaballo;
    }

    public JButton getBtnAgregarMovimiento() {
        return btnAgregarMovimiento;
    }

    public JButton getBtnModificarMovimiento() {
        return btnModificarMovimiento;
    }

    public JButton getBtnEliminarMovimiento() {
        return btnEliminarMovimiento;
    }

    public JButton getBtnDiaAnterior() {
        return btnDiaAnterior;
    }

    public JButton getBtnDiaSiguiente() {
        return btnDiaSiguiente;
    }

    public JButton getBtnHoy() {
        return btnHoy;
    }

    public JTextField getTxtFechaAgenda() {
        return txtFechaAgenda;
    }
}
