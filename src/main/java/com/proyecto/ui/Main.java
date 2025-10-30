package main.java.com.proyecto.ui;

import javax.swing.*;
import main.java.com.proyecto.Datos.DataGlobal;
import main.java.com.proyecto.Gestor.Gestor;

import java.awt.*;

public class Main extends JFrame {
    private Gestor gestor;

    public Main() {
        setTitle("Gestor de Actividades");
        setSize(1440, 980);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        DataGlobal dataGlobal = new DataGlobal();
        gestor = new Gestor(dataGlobal);

        // ✅ Botones
        JButton NewTask = new JButton("Nueva Tarea");
        JButton NewHabit = new JButton("Nuevo Hábito");
        JButton EditActivity = new JButton("Editar Actividad");
        JButton DeleteActivity = new JButton("Eliminar Actividad");

        // ✅ Fila 1 (arriba) alineada a la derecha
        JPanel fila1 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        fila1.add(NewTask);
        fila1.add(NewHabit);

        // ✅ Fila 2 (abajo) alineada a la derecha
        JPanel fila2 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        fila2.add(EditActivity);
        fila2.add(DeleteActivity);

        // ✅ Panel principal que apila las dos filas
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));

        topPanel.add(fila1);
        topPanel.add(fila2);

        add(topPanel, BorderLayout.NORTH);

        // ✅ Eventos
        NewTask.addActionListener(e -> {
            TaskWindow dialog = new TaskWindow(this, gestor);
            dialog.setVisible(true);
        });

        NewHabit.addActionListener(e -> {
            HabitWindow dialog = new HabitWindow(this, gestor);
            dialog.setVisible(true);
        });

        EditActivity.addActionListener(e -> {
            EditWindow dialog = new EditWindow(this, gestor);
            dialog.setVisible(true);
        });

        DeleteActivity.addActionListener(e -> {
            DeleteWindow dialog = new DeleteWindow(this, gestor);
            dialog.setVisible(true);
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Main().setVisible(true);
        });
    }
}