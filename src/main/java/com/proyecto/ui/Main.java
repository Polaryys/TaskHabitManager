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

        Font buttonFont = new Font("Arial", Font.BOLD, 20);
        Color textColor = new Color(232, 232, 232);

        JButton NewTask = new JButton("Nueva Tarea");
        styleButton(NewTask, buttonFont, new Color(14, 84, 129), textColor);

        JButton NewHabit = new JButton("Nuevo Hábito");
        styleButton(NewHabit, buttonFont, new Color(14, 84, 129), textColor);

    JButton ShowTasks = new JButton("Mostrar Tareas");
    styleButton(ShowTasks, buttonFont, new Color(10, 100, 50), textColor);

        JButton EditActivity = new JButton("Editar Actividad");
        styleButton(EditActivity, buttonFont, new Color(22, 125, 4), textColor);

        JButton DeleteActivity = new JButton("Eliminar Actividad");
        styleButton(DeleteActivity, buttonFont, new Color(159, 37, 6), textColor);

        JButton Report = new JButton("Reporte semanal");
        styleButton(Report, buttonFont, new Color(128, 0, 128), textColor);

        JPanel fila1 = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        fila1.add(NewTask);
        fila1.add(NewHabit);
        fila1.add(Report);
        fila1.add(ShowTasks);


        JPanel fila2 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        fila2.add(EditActivity);
        fila2.add(DeleteActivity);

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.add(fila1);
        topPanel.add(fila2);

        add(topPanel, BorderLayout.NORTH);

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


        Report.addActionListener(e -> {
            ReportWindow dialog = new ReportWindow(this, gestor);
            dialog.setVisible(true);
        }
        );

        ShowTasks.addActionListener(e -> {
            // Fetch ordered tasks and show in a dialog
            java.util.List<main.java.com.proyecto.Modelos.Tarea> tareas = gestor.obtenerTareasOrdenadas();

            StringBuilder sb = new StringBuilder();
            sb.append("=== TAREAS ORDENADAS POR PRIORIDAD ===\n\n");

            if (tareas.isEmpty()) {
                sb.append("No hay tareas guardadas.\n");
            } else {
                // They are already ordered; we'll still group them by priority for clarity
                main.java.com.proyecto.Modelos.Tarea.Priority current = null;
                for (main.java.com.proyecto.Modelos.Tarea t : tareas) {
                    if (current != t.getPrioridad()) {
                        current = t.getPrioridad();
                        sb.append("--- PRIORIDAD ").append(current).append(" ---\n");
                    }
                    sb.append(String.format("ID: %d | %s | %s | %s %s | Estado: %s\n",
                            t.getId(), t.getNombre(), t.getDescripcion(), t.getFecha().toString(), t.getHora().toString(), t.getEstado()));
                }
            }

            // Show in a dialog with scroll
            JDialog dlg = new JDialog(this, "Tareas", true);
            JTextArea txt = new JTextArea(sb.toString());
            txt.setEditable(false);
            txt.setFont(new Font("Monospaced", Font.PLAIN, 12));
            JScrollPane sp = new JScrollPane(txt);
            sp.setPreferredSize(new java.awt.Dimension(800, 500));
            dlg.getContentPane().add(sp);
            dlg.pack();
            dlg.setLocationRelativeTo(this);
            dlg.setVisible(true);
        });

    }

    private void styleButton(JButton button, Font font, Color bg, Color fg) {
        button.setFont(font);
        button.setBackground(bg.darker());
        button.setForeground(fg);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);

        Color baseColor = bg.darker();
        Color hoverColor = bg;

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(hoverColor);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(baseColor);
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Main().setVisible(true));
    }
}
