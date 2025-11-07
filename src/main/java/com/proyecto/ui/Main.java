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

        JPanel fila1 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    fila1.add(NewTask);
    fila1.add(NewHabit);
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

        ShowTasks.addActionListener(e -> {
            // Fetch ordered tasks
            java.util.List<main.java.com.proyecto.Modelos.Tarea> tareas = gestor.obtenerTareasOrdenadas();

            // Separate by priority
            java.util.List<main.java.com.proyecto.Modelos.Tarea> alta = new java.util.ArrayList<>();
            java.util.List<main.java.com.proyecto.Modelos.Tarea> media = new java.util.ArrayList<>();
            java.util.List<main.java.com.proyecto.Modelos.Tarea> baja = new java.util.ArrayList<>();

            for (main.java.com.proyecto.Modelos.Tarea t : tareas) {
                switch (t.getPrioridad()) {
                    case ALTA:
                        alta.add(t);
                        break;
                    case MEDIA:
                        media.add(t);
                        break;
                    case BAJA:
                    default:
                        baja.add(t);
                        break;
                }
            }

            String[] columns = {"ID", "Nombre", "Descripción", "Fecha", "Hora", "Estado"};

            javax.swing.table.DefaultTableModel modelAlta = new javax.swing.table.DefaultTableModel(columns, 0) {
                @Override public boolean isCellEditable(int row, int column) { return false; }
            };
            javax.swing.table.DefaultTableModel modelMedia = new javax.swing.table.DefaultTableModel(columns, 0) {
                @Override public boolean isCellEditable(int row, int column) { return false; }
            };
            javax.swing.table.DefaultTableModel modelBaja = new javax.swing.table.DefaultTableModel(columns, 0) {
                @Override public boolean isCellEditable(int row, int column) { return false; }
            };

            java.time.format.DateTimeFormatter df = java.time.format.DateTimeFormatter.ISO_LOCAL_DATE;
            java.time.format.DateTimeFormatter tf = java.time.format.DateTimeFormatter.ofPattern("HH:mm");

            for (main.java.com.proyecto.Modelos.Tarea t : alta) {
                modelAlta.addRow(new Object[]{t.getId(), t.getNombre(), t.getDescripcion(), t.getFecha().format(df), t.getHora().format(tf), t.getEstado()});
            }
            for (main.java.com.proyecto.Modelos.Tarea t : media) {
                modelMedia.addRow(new Object[]{t.getId(), t.getNombre(), t.getDescripcion(), t.getFecha().format(df), t.getHora().format(tf), t.getEstado()});
            }
            for (main.java.com.proyecto.Modelos.Tarea t : baja) {
                modelBaja.addRow(new Object[]{t.getId(), t.getNombre(), t.getDescripcion(), t.getFecha().format(df), t.getHora().format(tf), t.getEstado()});
            }

            JTable tableAlta = new JTable(modelAlta);
            JTable tableMedia = new JTable(modelMedia);
            JTable tableBaja = new JTable(modelBaja);

            tableAlta.setFillsViewportHeight(true);
            tableMedia.setFillsViewportHeight(true);
            tableBaja.setFillsViewportHeight(true);

            tableAlta.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
            tableMedia.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
            tableBaja.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

            // Create tabbed pane with three tabs
            JTabbedPane tabs = new JTabbedPane();
            tabs.addTab("ALTA (" + alta.size() + ")", new JScrollPane(tableAlta));
            tabs.addTab("MEDIA (" + media.size() + ")", new JScrollPane(tableMedia));
            tabs.addTab("BAJA (" + baja.size() + ")", new JScrollPane(tableBaja));

            // Dialog to show tables
            JDialog dlg = new JDialog(this, "Tareas - Tabla por Prioridad", true);
            dlg.getContentPane().setLayout(new BorderLayout(8,8));
            dlg.getContentPane().add(tabs, BorderLayout.CENTER);
            dlg.setSize(900, 600);
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
