package main.java.com.proyecto.ui;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
import main.java.com.proyecto.Modelos.*;
import main.java.com.proyecto.Gestor.*;


public class TaskWindow extends JDialog{
    private Gestor gestor;
    public TaskWindow(JFrame parent, Gestor gestor2 ){
        super(parent, "Nueva  Tarea", true);
        this.gestor = gestor2; // guardar la referencia
        setSize(400, 250);
        setLayout(new GridLayout(4, 2, 5, 5));
        setLocationRelativeTo(parent);

        JLabel TaskName = new JLabel("Nombre:");
        JTextField txtName = new JTextField();

        JLabel TaskDesc = new JLabel("Descripción:");
        JTextField txtDesc = new JTextField();

        JLabel TaskPriority = new JLabel("Prioridad (BAJA, MEDIA, ALTA):");
        JTextField txtPriority = new JTextField();

        JLabel TaskDate = new JLabel("Fecha (AAAA-MM-DD):");
        JTextField txtDate = new JTextField();

        JLabel TaskHour = new JLabel("Hora (HH:mm):");
        JTextField txtHour = new JTextField();

        JButton Guardar = new JButton("Guardar");
        JButton Cancelar = new JButton("Cancelar");

        add(TaskName); add(txtName);
        add(TaskDesc); add(txtDesc);
        add(TaskPriority); add(txtPriority);
        add(TaskDate); add(txtDate);
        add(TaskHour); add(txtHour);
        add(Guardar); add(Cancelar);
        
        Guardar.addActionListener(e ->{
            try{
                String nombre = txtName.getText();
                String descripcion = txtDesc.getText();
                Tarea.Priority prioridad = Tarea.Priority.valueOf(txtPriority.getText().toUpperCase());
                LocalDate fecha = LocalDate.parse(txtDate.getText());
                LocalTime hora = LocalTime.parse(txtHour.getText());
                
                Tarea nuevaTarea = gestor.createTask(nombre, descripcion, prioridad, fecha, hora);

                JOptionPane.showMessageDialog(this,
                        "Tarea creada exitosamente.\nID: " + nuevaTarea.getId());
                dispose();

            } catch (Exception ex){
                JOptionPane.showMessageDialog(this, "Error:" + ex.getMessage());
            }
        });
        
        Cancelar.addActionListener(e -> dispose());

    }
}
