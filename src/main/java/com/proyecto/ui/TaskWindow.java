package main.java.com.proyecto.ui;

import javax.swing.*;
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

        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setSize(420, 330);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Colours.Cl_Fondo);

        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(Colours.Cl_Tarjeta);
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        BorderFactory.createEmptyBorder(18, 18, 18, 18);

        JLabel titulo = new JLabel("CREAR NUEVA TAREA");
        titulo.setFont(Colours.Ft_Titulo);
        titulo.setForeground(Colours.Cl_Titulo);
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
        row3.setBackground(Colours.Cl_Tarjeta);

        JLabel lblPriority = new JLabel("Prioridad:");
        lblPriority.setFont(Colours.Ft_Label);
        lblPriority.setForeground(Colours.Cl_Texto);

        JComboBox<String> cmbPriority = new JComboBox<>(new String[]{
                "BAJA", "MEDIA", "ALTA"
        });
        cmbPriority.setFont(Colours.Ft_Label);
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
        row6.setBackground(Colours.Cl_Tarjeta);

        JButton btnGuardar = crearBoton("Guardar", Colours.Cl_Guardar);
        JButton btnCancelar = crearBoton("Cancelar", Colours.Cl_Cancelar);

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
        btn.setPreferredSize(new Dimension(110, 32));
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFont(Colours.Ft_Boton);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createLineBorder(color.darker(), 2));
        return btn;
    }
}
