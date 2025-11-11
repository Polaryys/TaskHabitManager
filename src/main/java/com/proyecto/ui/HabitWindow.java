package main.java.com.proyecto.ui;

import main.java.com.proyecto.Gestor.Gestor;
import main.java.com.proyecto.Modelos.Habito;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;

public class HabitWindow extends JDialog {
    private Gestor gestor;

    
    
    

    

    public HabitWindow(JFrame parent, Gestor gestor2) {
        super(parent, "Nuevo Hábito", true);
        this.gestor = gestor2;

        setSize(450, 320);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Colours.Cl_Fondo);

        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(Colours.Cl_Tarjeta);
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        

        JLabel titulo = new JLabel("CREAR NUEVO HÁBITO");
        titulo.setFont(Colours.Ft_Titulo);
        titulo.setForeground(Colours.Cl_Titulo);
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        titulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        mainPanel.add(titulo);

        JPanel row1 = crearFila("Nombre:", 200);
        JTextField txtName = (JTextField) row1.getComponent(1);
        mainPanel.add(row1);

        JPanel row2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        row2.setBackground(Colours.Cl_Tarjeta);

        JLabel lblFreq = new JLabel("Frecuencia:");
        lblFreq.setFont(Colours.Ft_Label);
        lblFreq.setForeground(Colours.Cl_Texto);

        JComboBox<String> cmbFreq = new JComboBox<>(new String[]{
                "DIARIO", "SEMANAL", "MENSUAL"
        });
        cmbFreq.setFont(Colours.Ft_Label);
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
        row5.setBackground(Colours.Cl_Tarjeta);

        JButton btnGuardar = crearBoton("Guardar", Colours.Cl_Guardar);
        JButton btnCancelar = crearBoton("Cancelar", Colours.Cl_Cancelar);

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
        row.setBackground(Colours.Cl_Tarjeta);

        JLabel lbl = new JLabel(label);
        lbl.setFont(Colours.Ft_Label);
        lbl.setForeground(Colours.Cl_Texto);

        JTextField txt = new JTextField();
        txt.setPreferredSize(new Dimension(width, 28));
        txt.setFont(Colours.Ft_Label);

        row.add(lbl);
        row.add(txt);
        return row;
    }

    private JButton crearBoton(String texto, Color color) {
        JButton btn = new JButton(texto);
        btn.setPreferredSize(new Dimension(125, 34));
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFont(Colours.Ft_Boton);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createLineBorder(color.darker(), 2));
        return btn;
    }
}
