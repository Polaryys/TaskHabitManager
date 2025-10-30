package main.java.com.proyecto.ui;

import javax.swing.*;
import main.java.com.proyecto.Datos.DataGlobal;
import main.java.com.proyecto.Gestor.Gestor;
import java.awt.*;

public class Main extends JFrame{
    private Gestor gestor;
    private void createInitialActivities() {
        // Tareas peestablecidas
        gestor.createTask(
            "Hacer compras online",
            "Comprar ropa y accesorios en linea",
            Tarea.Priority.BAJA,
            LocalDate.now().plusDays(6),
            LocalTime.of(10, 0)
        );

        gestor.createTask(
            "Reunión de trabajo",
            "Presentación del proyecto mensual",
            Tarea.Priority.ALTA,
            LocalDate.now().plusDays(3),
            LocalTime.of(14, 30)
        );

        gestor.createTask(
            "Ejercicio",
            "Ir al gimnasio",
            Tarea.Priority.MEDIA,
            LocalDate.now(),
            LocalTime.of(18, 0)
        );

        // Habitos preestablecidos
        gestor.createHabit(
            "Meditar",
            LocalDate.now(),
            LocalTime.of(7, 0),
            Habito.Frequency.DIARIO
        );

        gestor.createHabit(
            "Leer",
            LocalDate.now(),
            LocalTime.of(21, 0),
            Habito.Frequency.DIARIO
        );

        gestor.createHabit(
            "Reunión semanal",
            LocalDate.now(),
            LocalTime.of(9, 0),
            Habito.Frequency.SEMANAL
        );
    }

    public Main(){
        setTitle("Gestor de Actividades");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        DataGlobal dataGlobal = new DataGlobal();
        gestor = new Gestor(dataGlobal);
        
        // Create initial tasks and habits only if they haven't been created before
        if (!dataGlobal.isInitialized()) {
            createInitialActivities();
            dataGlobal.setInitialized();
        }

        JButton NewTask = new JButton("Crear Tarea");

        JPanel TopPanel = new JPanel(new BorderLayout());
        TopPanel.add(NewTask, BorderLayout.EAST);
        add(TopPanel, BorderLayout.NORTH);

        NewTask.addActionListener(e->{
            TaskWindow dialog = new TaskWindow(this, gestor);
            dialog.setVisible(true);
        });

    }

    public static void main(String[] args){
        SwingUtilities.invokeLater(() ->{
            new Main().setVisible(true);
        });
    }
    
}
