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

    public TaskWindow(JFrame parent, Gestor gestor2) {
        super(parent, "Nueva Tarea", true);
        this.gestor = gestor2;
        setUndecorated(true);
        setSize(400, 300);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(new Color(205, 205, 205));
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(Color.GRAY, 2),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        JPanel row1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        JLabel lblName = new JLabel("Nombre:");
        JTextField txtName = new JTextField();
        txtName.setPreferredSize(new Dimension(200, 25));
        row1.add(lblName);
        row1.add(txtName);
        mainPanel.add(row1);

        JPanel row2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        JLabel lblDesc = new JLabel("Descripción:");
        JTextField txtDesc = new JTextField();
        txtDesc.setPreferredSize(new Dimension(200, 25));
        row2.add(lblDesc);
        row2.add(txtDesc);
        mainPanel.add(row2);

        JPanel row3 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        JLabel lblPriority = new JLabel("Prioridad (BAJA, MEDIA, ALTA):");
        JTextField txtPriority = new JTextField();
        txtPriority.setPreferredSize(new Dimension(100, 25));
        row3.add(lblPriority);
        row3.add(txtPriority);
        mainPanel.add(row3);

        JPanel row4 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        JLabel lblDate = new JLabel("Fecha (AAAA-MM-DD):");
        JTextField txtDate = new JTextField();
        txtDate.setPreferredSize(new Dimension(100, 25));
        row4.add(lblDate);
        row4.add(txtDate);
        mainPanel.add(row4);

        JPanel row5 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        JLabel lblHour = new JLabel("Hora (HH:mm):");
        JTextField txtHour = new JTextField();
        txtHour.setPreferredSize(new Dimension(80, 25));
        row5.add(lblHour);
        row5.add(txtHour);
        mainPanel.add(row5);

        JPanel row6 = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton btnGuardar = new JButton("Guardar");
        JButton btnCancelar = new JButton("Cancelar");
        row6.add(btnGuardar);
        row6.add(btnCancelar);
        mainPanel.add(row6);

        add(mainPanel, BorderLayout.CENTER);

        btnGuardar.addActionListener(e -> {
            try {

                String nombre = txtName.getText().trim();
                String descripcion = txtDesc.getText().trim();
                String prioridadStr = txtPriority.getText().trim();
                String fechaStr = txtDate.getText().trim();
                String horaStr = txtHour.getText().trim();


                if(nombre.isEmpty() || descripcion.isEmpty() || prioridadStr.isEmpty() 
                    || fechaStr.isEmpty() || horaStr.isEmpty()) {
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
}