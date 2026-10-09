package vista;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import modelo.Caballo;

public class CaballoDialog extends JDialog {

    private JTextField txtNombre = new JTextField(18);
    private JTextField txtRaza = new JTextField(18);
    private JTextField txtEdad = new JTextField(18);
    private JTextField txtPelaje = new JTextField(18);
    private JComboBox<String> cmbEstado = new JComboBox<>(new String[]{"Disponible", "En descanso"});
    private JButton btnGuardar = Estilo.boton("Guardar", Estilo.VERDE);
    private JButton btnCancelar = Estilo.boton("Cancelar", Estilo.GRIS);

    public CaballoDialog(JFrame padre, String titulo) {
        super(padre, titulo, true);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Estilo.FONDO);

        Estilo.campo(txtNombre);
        Estilo.campo(txtRaza);
        Estilo.campo(txtEdad);
        Estilo.campo(txtPelaje);

        JPanel formulario = new JPanel(new GridLayout(5, 2, 10, 10));
        formulario.setBackground(Estilo.FONDO);
        formulario.setBorder(BorderFactory.createEmptyBorder(15, 15, 5, 15));
        agregarFila(formulario, "Nombre", txtNombre);
        agregarFila(formulario, "Raza", txtRaza);
        agregarFila(formulario, "Edad", txtEdad);
        agregarFila(formulario, "Pelaje", txtPelaje);
        agregarFila(formulario, "Estado", cmbEstado);

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

    public void cargar(Caballo c) {
        txtNombre.setText(c.getNombre());
        txtRaza.setText(c.getRaza());
        txtEdad.setText(String.valueOf(c.getEdad()));
        txtPelaje.setText(c.getPelaje());
        cmbEstado.setSelectedItem(c.getEstado());
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

    public String getNombre() {
        return txtNombre.getText().trim();
    }

    public String getRaza() {
        return txtRaza.getText().trim();
    }

    public String getTextoEdad() {
        return txtEdad.getText().trim();
    }

    public String getPelaje() {
        return txtPelaje.getText().trim();
    }

    public String getEstado() {
        return (String) cmbEstado.getSelectedItem();
    }
}
