package vista;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import modelo.Caballo;
import modelo.Fechas;
import modelo.Movimiento;

public class MovimientoDialog extends JDialog {

    private List<Caballo> caballos;
    private JComboBox<Caballo> cmbCaballo = new JComboBox<>();
    private JCheckBox chkSoloDisponibles = new JCheckBox("Mostrar solo caballos disponibles");
    private JComboBox<String> cmbTipo = new JComboBox<>(new String[]{"Diurno", "Nocturno"});
    private JTextField txtFecha = new JTextField(18);
    private JTextField txtHora = new JTextField(18);
    private JTextField txtCliente = new JTextField(18);
    private JTextField txtDuracion = new JTextField(18);
    private JTextField txtPrecio = new JTextField(18);
    private JButton btnGuardar = Estilo.boton("Guardar", Estilo.VERDE);
    private JButton btnCancelar = Estilo.boton("Cancelar", Estilo.GRIS);

    public MovimientoDialog(JFrame padre, String titulo, List<Caballo> caballos) {
        super(padre, titulo, true);
        this.caballos = new ArrayList<>(caballos);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Estilo.FONDO);

        chkSoloDisponibles.setBackground(Estilo.FONDO);
        chkSoloDisponibles.setForeground(Estilo.TEXTO);
        chkSoloDisponibles.addActionListener(e -> filtrar());
        filtrar();

        Estilo.campo(txtFecha);
        Estilo.campo(txtHora);
        Estilo.campo(txtCliente);
        Estilo.campo(txtDuracion);
        Estilo.campo(txtPrecio);

        JPanel formulario = new JPanel(new GridLayout(8, 2, 10, 10));
        formulario.setBackground(Estilo.FONDO);
        formulario.setBorder(BorderFactory.createEmptyBorder(15, 15, 5, 15));
        agregarFila(formulario, "Caballo", cmbCaballo);
        agregarFila(formulario, "", chkSoloDisponibles);
        agregarFila(formulario, "Tipo de recorrido", cmbTipo);
        agregarFila(formulario, "Fecha (DD-MM-AAAA)", txtFecha);
        agregarFila(formulario, "Hora (HH:mm)", txtHora);
        agregarFila(formulario, "Cliente", txtCliente);
        agregarFila(formulario, "Duracion (horas)", txtDuracion);
        agregarFila(formulario, "Precio por hora", txtPrecio);

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 8));
        botones.setBackground(Estilo.FONDO);
        botones.add(btnGuardar);
        botones.add(btnCancelar);

        add(Estilo.titulo(titulo, Estilo.VERDE), BorderLayout.NORTH);
        add(formulario, BorderLayout.CENTER);
        add(botones, BorderLayout.SOUTH);

        pack();
        setResizable(false);
        setLocationRelativeTo(padre);
    }

    private void agregarFila(JPanel panel, String texto, Component campo) {
        JLabel etiqueta = new JLabel(texto);
        etiqueta.setForeground(Estilo.TEXTO);
        panel.add(etiqueta);
        panel.add(campo);
    }

    private void filtrar() {
        Caballo seleccionado = (Caballo) cmbCaballo.getSelectedItem();
        cmbCaballo.removeAllItems();
        for (Caballo c : caballos) {
            if (!chkSoloDisponibles.isSelected() || "Disponible".equals(c.getEstado())) {
                cmbCaballo.addItem(c);
            }
        }
        if (seleccionado != null) {
            seleccionarCaballo(seleccionado.getId());
        }
    }

    private void seleccionarCaballo(int id) {
        for (int i = 0; i < cmbCaballo.getItemCount(); i++) {
            if (cmbCaballo.getItemAt(i).getId() == id) {
                cmbCaballo.setSelectedIndex(i);
                return;
            }
        }
    }

    public void cargar(Movimiento m) {
        seleccionarCaballo(m.getCaballo().getId());
        cmbTipo.setSelectedItem(m.getTipoRecorrido());
        txtFecha.setText(Fechas.aTexto(m.getFecha()));
        txtHora.setText(m.getHoraInicio());
        txtCliente.setText(m.getCliente());
        txtDuracion.setText(String.valueOf(m.getDuracion()));
        txtPrecio.setText(String.valueOf(m.getPrecio()));
    }

    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje);
    }

    public JButton getBtnGuardar() {
        return btnGuardar;
    }

    public JButton getBtnCancelar() {
        return btnCancelar;
    }

    public Caballo getCaballo() {
        return (Caballo) cmbCaballo.getSelectedItem();
    }

    public String getTipo() {
        return (String) cmbTipo.getSelectedItem();
    }

    public String getFecha() {
        return txtFecha.getText().trim();
    }

    public String getHora() {
        return txtHora.getText().trim();
    }

    public String getCliente() {
        return txtCliente.getText().trim();
    }

    public String getTextoDuracion() {
        return txtDuracion.getText().trim();
    }

    public String getTextoPrecio() {
        return txtPrecio.getText().trim();
    }
}
