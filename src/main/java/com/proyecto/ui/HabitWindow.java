

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

        setSize(450, 300);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel row1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        JLabel lblName = new JLabel("Nombre:");
        JTextField txtName = new JTextField();
        txtName.setPreferredSize(new Dimension(200, 25));
        row1.add(lblName);
        row1.add(txtName);
        mainPanel.add(row1);

        JPanel row2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        JLabel lblFreq = new JLabel("Frecuencia (DIARIO, SEMANAL, MENSUAL):");
        JTextField txtFreq = new JTextField();
        txtFreq.setPreferredSize(new Dimension(150, 25));
        row2.add(lblFreq);
        row2.add(txtFreq);
        mainPanel.add(row2);

        JPanel row3 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        JLabel lblDate = new JLabel("Fecha (AAAA-MM-DD):");
        JTextField txtDate = new JTextField();
        txtDate.setPreferredSize(new Dimension(100, 25));
        row3.add(lblDate);
        row3.add(txtDate);
        mainPanel.add(row3);

        JPanel row4 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        JLabel lblHour = new JLabel("Hora (HH:mm):");
        JTextField txtHour = new JTextField();
        txtHour.setPreferredSize(new Dimension(80, 25));
        row4.add(lblHour);
        row4.add(txtHour);
        mainPanel.add(row4);

        JPanel row5 = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton btnGuardar = new JButton("Guardar");
        JButton btnCancelar = new JButton("Cancelar");
        row5.add(btnGuardar);
        row5.add(btnCancelar);
        mainPanel.add(row5);

        add(mainPanel, BorderLayout.CENTER);

        btnGuardar.addActionListener(e -> {
            try {

                String nombre = txtName.getText().trim();
                String freqStr = txtFreq.getText().trim().toUpperCase();
                String fechaStr = txtDate.getText().trim();
                String horaStr = txtHour.getText().trim();

                if(nombre.isEmpty() || freqStr.isEmpty() || fechaStr.isEmpty() || horaStr.isEmpty()) {
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
}