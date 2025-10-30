package main.java.com.proyecto.ui;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
import main.java.com.proyecto.Modelos.*;
import main.java.com.proyecto.Gestor.*;

public class HabitWindow extends JDialog {

    public HabitWindow(JFrame parent, Gestor gestor) {
        super(parent, "Crear Hábito", true);
        setSize(350, 200);
        setLayout(new GridLayout(3, 2, 5, 5));
        setLocationRelativeTo(parent);

        JLabel lblName = new JLabel("Nombre del hábito:");
        JTextField txtName = new JTextField();

        JButton btnSave = new JButton("Guardar");
        JButton btnCancel = new JButton("Cancelar");

        add(lblName); add(txtName);
        add(btnSave); add(btnCancel);

        btnSave.addActionListener(e -> {
            String nombre = txtName.getText().trim();
            if (nombre.isEmpty()) {
                JOptionPane.showMessageDialog(this, "El nombre es obligatorio.");
                return;
            }

            // Aquí luego agregaremos la lógica real para crear hábitos
            JOptionPane.showMessageDialog(this, "Hábito creado: " + nombre);
            dispose();
        });

        btnCancel.addActionListener(e -> dispose());
    }
}
