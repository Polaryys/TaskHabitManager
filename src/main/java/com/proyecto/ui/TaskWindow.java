package main.java.com.proyecto.ui;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;

import main.java.com.proyecto.Modelos.*;
import main.java.com.proyecto.Gestor.*;

public class TaskWindow extends JDialog {
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

    public TaskWindow(JFrame parent, Gestor gestor2) {
        super(parent, "Nueva Tarea", true);
        this.gestor = gestor2;

        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setSize(420, 330);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());
        getContentPane().setBackground(COLOR_FONDO);

        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(COLOR_TARJETA);
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        BorderFactory.createEmptyBorder(18, 18, 18, 18);

        JLabel titulo = new JLabel("CREAR NUEVA TAREA");
        titulo.setFont(FUENTE_TITULO);
        titulo.setForeground(COLOR_PRIMARIO);
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        titulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        mainPanel.add(titulo);

        JPanel row1 = crearFila("Nombre:", 200);
        JTextField txtName = (JTextField) row1.getComponent(1);
        mainPanel.add(row1);

        JPanel row2 = crearFila("Descripción:", 200);
        JTextField txtDesc = (JTextField) row2.getComponent(1);
        mainPanel.add(row2);

        JPanel row3 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        row3.setBackground(COLOR_TARJETA);

        JLabel lblPriority = new JLabel("Prioridad:");
        lblPriority.setFont(FUENTE_LABEL);
        lblPriority.setForeground(COLOR_TEXTO);

        JComboBox<String> cmbPriority = new JComboBox<>(new String[]{
                "BAJA", "MEDIA", "ALTA"
        });
        cmbPriority.setFont(FUENTE_LABEL);
        cmbPriority.setPreferredSize(new Dimension(120, 28));

        row3.add(lblPriority);
        row3.add(cmbPriority);
        mainPanel.add(row3);

        JPanel row4 = crearFila("Fecha (AAAA-MM-DD):", 120);
        JTextField txtDate = (JTextField) row4.getComponent(1);
        mainPanel.add(row4);

        JPanel row5 = crearFila("Hora (HH:mm):", 80);
        JTextField txtHour = (JTextField) row5.getComponent(1);
        mainPanel.add(row5);

        JPanel row6 = new JPanel(new FlowLayout(FlowLayout.CENTER, 14, 14));
        row6.setBackground(COLOR_TARJETA);

        JButton btnGuardar = crearBoton("Guardar", COLOR_SECUNDARIO);
        JButton btnCancelar = crearBoton("Cancelar", COLOR_ALERTA);

        row6.add(btnGuardar);
        row6.add(btnCancelar);
        mainPanel.add(row6);

        add(mainPanel, BorderLayout.CENTER);

        btnGuardar.addActionListener(e -> {
            try {
                String nombre = txtName.getText().trim();
                String descripcion = txtDesc.getText().trim();
                String prioridadStr = (String) cmbPriority.getSelectedItem(); // COMBOBOX
                String fechaStr = txtDate.getText().trim();
                String horaStr = txtHour.getText().trim();

                if (nombre.isEmpty() || descripcion.isEmpty() || prioridadStr.isEmpty() ||
                    fechaStr.isEmpty() || horaStr.isEmpty()) {
                    JOptionPane.showMessageDialog(this,
                            "Todos los campos son obligatorios.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Tarea.Priority prioridad = Tarea.Priority.valueOf(prioridadStr.toUpperCase());
                LocalDate fecha = LocalDate.parse(fechaStr);
                LocalTime hora = LocalTime.parse(horaStr);

                Tarea nuevaTarea = gestor.createTask(nombre, descripcion, prioridad, fecha, hora);

                JOptionPane.showMessageDialog(this,
                        "Tarea creada exitosamente.\nID: " + nuevaTarea.getId());
                dispose();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnCancelar.addActionListener(e -> dispose());
    }

    private JPanel crearFila(String label, int width) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        row.setBackground(COLOR_TARJETA);

        JLabel lbl = new JLabel(label);
        lbl.setFont(FUENTE_LABEL);
        lbl.setForeground(COLOR_TEXTO);

        JTextField txt = new JTextField();
        txt.setPreferredSize(new Dimension(width, 28));
        txt.setFont(FUENTE_LABEL);

        row.add(lbl);
        row.add(txt);
        return row;
    }

    private JButton crearBoton(String texto, Color color) {
        JButton btn = new JButton(texto);
        btn.setPreferredSize(new Dimension(110, 32));
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFont(FUENTE_BOTON);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createLineBorder(color.darker(), 2));
        return btn;
    }
}
