package main.java.com.proyecto.ui;

import main.java.com.proyecto.Gestor.Gestor;
import main.java.com.proyecto.Modelos.Habito;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;

public class HabitWindow extends JDialog {
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

    public HabitWindow(JFrame parent, Gestor gestor2) {
        super(parent, "Nuevo Hábito", true);
        this.gestor = gestor2;

        setSize(450, 320);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());
        getContentPane().setBackground(COLOR_FONDO);

        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(COLOR_TARJETA);
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        ;

        JLabel titulo = new JLabel("CREAR NUEVO HÁBITO");
        titulo.setFont(FUENTE_TITULO);
        titulo.setForeground(COLOR_PRIMARIO);
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        titulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        mainPanel.add(titulo);

        JPanel row1 = crearFila("Nombre:", 200);
        JTextField txtName = (JTextField) row1.getComponent(1);
        mainPanel.add(row1);

        JPanel row2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        row2.setBackground(COLOR_TARJETA);

        JLabel lblFreq = new JLabel("Frecuencia:");
        lblFreq.setFont(FUENTE_LABEL);
        lblFreq.setForeground(COLOR_TEXTO);

        JComboBox<String> cmbFreq = new JComboBox<>(new String[]{
                "DIARIO", "SEMANAL", "MENSUAL"
        });
        cmbFreq.setFont(FUENTE_LABEL);
        cmbFreq.setPreferredSize(new Dimension(140, 28));

        row2.add(lblFreq);
        row2.add(cmbFreq);
        mainPanel.add(row2);

        JPanel row3 = crearFila("Fecha (AAAA-MM-DD):", 120);
        JTextField txtDate = (JTextField) row3.getComponent(1);
        mainPanel.add(row3);

        JPanel row4 = crearFila("Hora (HH:mm):", 80);
        JTextField txtHour = (JTextField) row4.getComponent(1);
        mainPanel.add(row4);

        JPanel row5 = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 12));
        row5.setBackground(COLOR_TARJETA);

        JButton btnGuardar = crearBoton("Guardar", COLOR_SECUNDARIO);
        JButton btnCancelar = crearBoton("Cancelar", COLOR_ALERTA);

        row5.add(btnGuardar);
        row5.add(btnCancelar);
        mainPanel.add(row5);

        add(mainPanel, BorderLayout.CENTER);

        btnGuardar.addActionListener(e -> {
            try {

                String nombre = txtName.getText().trim();
                String freqStr = ((String) cmbFreq.getSelectedItem()).toUpperCase(); // COMBOBOX
                String fechaStr = txtDate.getText().trim();
                String horaStr = txtHour.getText().trim();

                if (nombre.isEmpty() || freqStr.isEmpty() ||
                        fechaStr.isEmpty() || horaStr.isEmpty()) {
                    JOptionPane.showMessageDialog(this,
                            "Todos los campos son obligatorios.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Habito.Frequency frecuencia;
                try {
                    frecuencia = Habito.Frequency.valueOf(freqStr);
                } catch (IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(this,
                            "Frecuencia inválida. Debe ser DIARIO, SEMANAL o MENSUAL.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                LocalDate fecha = LocalDate.parse(fechaStr);
                LocalTime hora = LocalTime.parse(horaStr);

                Habito nuevoHabito = gestor.createHabit(nombre, frecuencia, fecha, hora);

                JOptionPane.showMessageDialog(this,
                        "Hábito creado exitosamente.\nID: " + nuevoHabito.getId());
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
        btn.setPreferredSize(new Dimension(125, 34));
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFont(FUENTE_BOTON);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createLineBorder(color.darker(), 2));
        return btn;
    }
}
