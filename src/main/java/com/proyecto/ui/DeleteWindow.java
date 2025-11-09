package main.java.com.proyecto.ui;

import main.java.com.proyecto.Gestor.Gestor;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class DeleteWindow extends JDialog {
    @SuppressWarnings("unused")
    private Gestor gestor;

    private final Color COLOR_PRIMARIO = new Color(41, 128, 185);
    private final Color COLOR_SECUNDARIO = new Color(52, 152, 219);
    private final Color COLOR_ALERTA = new Color(231, 76, 60);
    private final Color COLOR_FONDO = new Color(245, 245, 245);
    private final Color COLOR_TARJETA = Color.WHITE;
    private final Color COLOR_TEXTO = new Color(52, 73, 94);

    private final Font FUENTE_TITULO = new Font("Segoe UI", Font.BOLD, 18);
    private final Font FUENTE_LABEL = new Font("Segoe UI", Font.PLAIN, 14);
    private final Font FUENTE_BOTON = new Font("Segoe UI", Font.BOLD, 14);
    private final Font FUENTE_INFO = new Font("Segoe UI", Font.PLAIN, 13);

    public DeleteWindow(JFrame parent, Gestor gestor) {
        super(parent, "Eliminar Actividad", true);
        this.gestor = gestor;


        setSize(480, 360);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());
        getContentPane().setBackground(COLOR_FONDO);


        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(12, 12));
        mainPanel.setBackground(COLOR_TARJETA);
        mainPanel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(COLOR_SECUNDARIO, 1),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));


        JLabel titulo = new JLabel("ELIMINAR ACTIVIDAD");
        titulo.setFont(FUENTE_TITULO);
        titulo.setForeground(COLOR_PRIMARIO);
        titulo.setHorizontalAlignment(SwingConstants.CENTER);
        titulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        mainPanel.add(titulo, BorderLayout.NORTH);


        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        topPanel.setBackground(COLOR_TARJETA);

        JLabel lblId = new JLabel("ID:");
        lblId.setFont(FUENTE_LABEL);
        lblId.setForeground(COLOR_TEXTO);

        JTextField txtId = new JTextField(10);
        txtId.setFont(FUENTE_LABEL);
        txtId.setPreferredSize(new Dimension(120, 28));

        JButton btnBuscar = crearBoton("Buscar", COLOR_SECUNDARIO);

        topPanel.add(lblId);
        topPanel.add(txtId);
        topPanel.add(btnBuscar);

        mainPanel.add(topPanel, BorderLayout.CENTER);


        JTextArea infoArea = new JTextArea();
        infoArea.setEditable(false);
        infoArea.setFont(FUENTE_INFO);
        infoArea.setForeground(COLOR_TEXTO);
        infoArea.setBackground(Color.WHITE);
        infoArea.setLineWrap(true);
        infoArea.setWrapStyleWord(true);
        infoArea.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));

        JScrollPane scrollPane = new JScrollPane(infoArea);
        scrollPane.setBorder(new LineBorder(COLOR_SECUNDARIO, 1));
        scrollPane.setPreferredSize(new Dimension(380, 150));

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BorderLayout());
        infoPanel.setBackground(COLOR_TARJETA);
        infoPanel.add(scrollPane, BorderLayout.CENTER);

        mainPanel.add(infoPanel, BorderLayout.SOUTH);


        JButton btnEliminar = crearBoton("Eliminar", COLOR_ALERTA);
        btnEliminar.setEnabled(false);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(COLOR_TARJETA);
        bottomPanel.add(btnEliminar);

        add(mainPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        btnBuscar.addActionListener(e -> {
            try {
                int id = Integer.parseInt(txtId.getText().trim());

                String actividad = gestor.SearchActivity(id);

                if (actividad == null) {
                    infoArea.setText("No se encontró ninguna actividad con ese ID.");
                    btnEliminar.setEnabled(false);
                } else {
                    infoArea.setText(actividad);
                    btnEliminar.setEnabled(true);
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                        "ID inválido. Debe ser un número.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        btnEliminar.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "¿Seguro que deseas eliminar esta actividad?",
                    "Confirmar",
                    JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                int id = Integer.parseInt(txtId.getText().trim());
                boolean eliminado = gestor.DeleteActivity(id);

                if (eliminado) {
                    JOptionPane.showMessageDialog(this, "Actividad eliminada.");
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Error al eliminar.");
                }
            }
        });
    }

    private JButton crearBoton(String texto, Color color) {
        JButton btn = new JButton(texto);
        btn.setPreferredSize(new Dimension(120, 32));
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFont(FUENTE_BOTON);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createLineBorder(color.darker(), 2));
        return btn;
    }
}