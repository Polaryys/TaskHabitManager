package main.java.com.proyecto.ui;

import main.java.com.proyecto.Gestor.Gestor;
import main.java.com.proyecto.Modelos.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.Arrays;

import javax.swing.*;
import java.awt.*;

public class EditWindow extends JDialog {

    @SuppressWarnings("unused")
    private Gestor gestor;

    public EditWindow(JFrame parent, Gestor gestor) {
        super(parent, "Editar Actividad", true);
        this.gestor = gestor;

        setSize(350, 200);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(Colours.Cl_Fondo);

        // Panel con fondo blanco y bordes redondeados simulados
        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 25, 25));
        centerPanel.setBackground(Color.WHITE);
        centerPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        JButton btnTarea = new JButton("Tarea");
        styleButton(btnTarea);


        JButton btnHabito = new JButton("Hábito");
        styleButton(btnHabito);

        centerPanel.add(btnTarea);
        centerPanel.add(btnHabito);

        add(centerPanel, BorderLayout.CENTER);

        btnTarea.addActionListener(e -> {
            dispose();
            TaskEdit te = new TaskEdit(parent, gestor);
            te.setVisible(true);
        });

        btnHabito.addActionListener(e -> {
            dispose();
            HabitEdit te = new HabitEdit(parent, gestor);
            te.setVisible(true);
        });
    }

    private void styleButton(JButton btn) {
        btn.setFocusPainted(false);
        btn.setBackground(Colours.Cl_Guardar);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setPreferredSize(new Dimension(110, 40));
        btn.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btn.setBackground(Colours.COLOR_BOTON_HOVER);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                btn.setBackground(Colours.Cl_Guardar);
            }
        });
    }


    class TaskEdit extends JDialog {
        
    private final Color COLOR_PRIMARIO = new Color(41, 128, 185);
    @SuppressWarnings("unused")
    private final Color COLOR_SECUNDARIO = new Color(52, 152, 219);
    private final Color COLOR_FONDO = new Color(245, 245, 245);
    private final Color COLOR_BOTON_HOVER = new Color(33, 97, 140);

    @SuppressWarnings("unused")
    private Gestor gestor;

    public TaskEdit(JFrame parent, Gestor gestor) {
        super(parent, "Editar Tarea", true);
        this.gestor = gestor;

        setSize(430, 430);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());
        getContentPane().setBackground(COLOR_FONDO);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        JPanel rowId = createRow();
        JLabel lblId = styledLabel("ID:");
        JTextField txtId = styledTextField(100);
        JButton btnBuscar = styledButton("Buscar");

        rowId.add(lblId);
        rowId.add(txtId);
        rowId.add(btnBuscar);
        mainPanel.add(rowId);

        JTextField txtNombre = styledTextField(200);
        JTextField txtDescripcion = styledTextField(200);

        String[] prioridades = { "ALTA", "MEDIA", "BAJA" };
        JComboBox<String> comboPrioridad = styledCombo(prioridades);

        String[] estados = { "PENDIENTE", "COMPLETADO" };
        JComboBox<String> comboEstado = styledCombo(estados);

        JTextField txtFecha = styledTextField(120);
        JTextField txtHora = styledTextField(120);

        mainPanel.add(createLabeledRow("Nombre:", txtNombre));
        mainPanel.add(createLabeledRow("Descripción:", txtDescripcion));
        mainPanel.add(createLabeledRow("Prioridad:", comboPrioridad));
        mainPanel.add(createLabeledRow("Estado:", comboEstado));
        mainPanel.add(createLabeledRow("Fecha:", txtFecha));
        mainPanel.add(createLabeledRow("Hora:", txtHora));

        JPanel rowButtons = createRowCenter();
        JButton btnEditar = styledButton("Editar");
        JButton btnCerrar = styledButton("Cerrar");

        btnEditar.setEnabled(false);

        rowButtons.add(btnEditar);
        rowButtons.add(btnCerrar);

        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(rowButtons);

        add(mainPanel, BorderLayout.CENTER);

        btnCerrar.addActionListener(e -> dispose());

        btnBuscar.addActionListener(e -> {
            try {
                int id = Integer.parseInt(txtId.getText().trim());
                Tarea t = gestor.SearchTaskId(id);

                if (t == null) {
                    JOptionPane.showMessageDialog(this,
                            "No se encontró una tarea con ese ID.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    btnEditar.setEnabled(false);
                    return;
                }

                txtNombre.setText(t.getNombre());
                txtDescripcion.setText(t.getDescripcion());
                txtFecha.setText(t.getFecha().toString());
                txtHora.setText(t.getHora().toString());

                String pr = t.getPrioridad().toString().toUpperCase();
                comboPrioridad.setSelectedItem(Arrays.asList(prioridades).contains(pr) ? pr : "MEDIA");

                String es = t.getEstado().toString().toUpperCase();
                comboEstado.setSelectedItem(Arrays.asList(estados).contains(es) ? es : "PENDIENTE");

                btnEditar.setEnabled(true);

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "ID inválido.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnEditar.addActionListener(e -> {

            if (txtNombre.getText().trim().isEmpty() ||
                    txtDescripcion.getText().trim().isEmpty() ||
                    comboPrioridad.getSelectedItem() == null ||
                    comboEstado.getSelectedItem() == null ||
                    txtFecha.getText().trim().isEmpty() ||
                    txtHora.getText().trim().isEmpty()) {

                JOptionPane.showMessageDialog(this,
                        "Todos los campos deben estar llenos.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            LocalDate fecha;
            try {
                fecha = LocalDate.parse(txtFecha.getText().trim());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "La fecha debe tener formato YYYY-MM-DD",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            LocalTime hora;
            try {
                hora = LocalTime.parse(txtHora.getText().trim());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "La hora debe tener formato HH:MM",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int id;
            try {
                id = Integer.parseInt(txtId.getText().trim());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "ID inválido.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                main.java.com.proyecto.Modelos.Actividad.State estadoEnum =
                        main.java.com.proyecto.Modelos.Actividad.State.valueOf(comboEstado.getSelectedItem().toString());

                main.java.com.proyecto.Modelos.Tarea.Priority prioridadEnum =
                        main.java.com.proyecto.Modelos.Tarea.Priority.valueOf(comboPrioridad.getSelectedItem().toString());

                main.java.com.proyecto.Modelos.Tarea nueva =
                        new main.java.com.proyecto.Modelos.Tarea(
                                id,
                                txtNombre.getText().trim(),
                                estadoEnum,
                                fecha,
                                hora,
                                txtDescripcion.getText().trim(),
                                prioridadEnum);

                boolean ok = gestor.updateTask(nueva);

                if (ok) {
                    JOptionPane.showMessageDialog(this,
                            "Tarea actualizada correctamente.");
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this,
                            "No se pudo actualizar la tarea.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (IllegalArgumentException iae) {
                JOptionPane.showMessageDialog(this,
                        "Valor de prioridad/estado inválido.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private JPanel createRow() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        p.setBackground(Color.WHITE);
        return p;
    }

    private JPanel createRowCenter() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        p.setBackground(Color.WHITE);
        return p;
    }

    private JPanel createLabeledRow(String label, JComponent comp) {
        JPanel row = createRow();
        row.add(styledLabel(label));
        row.add(comp);
        return row;
    }

    private JLabel styledLabel(String txt) {
        JLabel lbl = new JLabel(txt);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        return lbl;
    }

    private JTextField styledTextField(int width) {
        JTextField txt = new JTextField();
        txt.setPreferredSize(new Dimension(width, 28));
        txt.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txt.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180)),
                BorderFactory.createEmptyBorder(3, 5, 3, 5)
        ));
        return txt;
    }

    private JComboBox<String> styledCombo(String[] items) {
        JComboBox<String> combo = new JComboBox<>(items);
        combo.setPreferredSize(new Dimension(120, 28));
        combo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        combo.setBackground(Color.WHITE);
        combo.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180)));
        return combo;
    }

    private JButton styledButton(String text) {
        JButton btn = new JButton(text);
        btn.setFocusPainted(false);
        btn.setBackground(COLOR_PRIMARIO);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mouseEntered(java.awt.event.MouseEvent e) {
                btn.setBackground(COLOR_BOTON_HOVER);
            }
            @Override public void mouseExited(java.awt.event.MouseEvent e) {
                btn.setBackground(COLOR_PRIMARIO);
            }
        });
        return btn;
    }
}

    class HabitEdit extends JDialog {

    @SuppressWarnings("unused")
    private Gestor gestor;

    public HabitEdit(JFrame parent, Gestor gestor) {
        super(parent, "Editar Hábito", true);
        this.gestor = gestor;

        setSize(430, 450);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Colours.Cl_Fondo);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        JPanel rowId = createRow();
        JLabel lblId = styledLabel("ID:");
        JTextField txtId = styledTextField(100);
        JButton btnBuscar = styledButton("Buscar");

        rowId.add(lblId);
        rowId.add(txtId);
        rowId.add(btnBuscar);
        mainPanel.add(rowId);

        JTextField txtNombre = styledTextField(200);
        JComboBox<Habito.Frequency> cbFrecuencia = styledComboHabito(Habito.Frequency.values());
        JComboBox<Actividad.State> cbEstado = styledComboEstado(Actividad.State.values());
        JTextField txtFecha = styledTextField(120);
        JTextField txtHora = styledTextField(80);

        mainPanel.add(createLabeledRow("Nombre:", txtNombre));
        mainPanel.add(createLabeledRow("Frecuencia:", cbFrecuencia));
        mainPanel.add(createLabeledRow("Estado:", cbEstado));
        mainPanel.add(createLabeledRow("Fecha:", txtFecha));
        mainPanel.add(createLabeledRow("Hora:", txtHora));

        JPanel rowButtons = createRowCenter();
        JButton btnEditar = styledButton("Editar");
        JButton btnCerrar = styledButton("Cerrar");

        btnEditar.setEnabled(false);
        
        rowButtons.add(btnEditar);
        rowButtons.add(btnCerrar);
        
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(rowButtons);

        add(mainPanel, BorderLayout.CENTER);


        btnCerrar.addActionListener(e -> dispose());

        btnBuscar.addActionListener(e -> {
            try {
                int id = Integer.parseInt(txtId.getText().trim());
                Habito h = gestor.SearchHabitId(id);

                if (h == null) {
                    JOptionPane.showMessageDialog(this,
                            "No se encontró un hábito con ese ID.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    btnEditar.setEnabled(false);
                    return;
                }

                txtNombre.setText(h.getNombre());
                cbFrecuencia.setSelectedItem(h.getFrecuencia());
                cbEstado.setSelectedItem(h.getEstado());
                txtFecha.setText(h.getFecha().toString());
                txtHora.setText(h.getHora().toString());

                btnEditar.setEnabled(true);

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "ID inválido.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnEditar.addActionListener(e -> {
            String nombre = txtNombre.getText().trim();
            String fechaStr = txtFecha.getText().trim();
            String horaStr = txtHora.getText().trim();

            if (nombre.isEmpty() || fechaStr.isEmpty() || horaStr.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Todos los campos son obligatorios.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                LocalDate fecha = LocalDate.parse(fechaStr);
                LocalTime hora = LocalTime.parse(horaStr);
                Habito.Frequency frecuencia = (Habito.Frequency) cbFrecuencia.getSelectedItem();
                Actividad.State estado = (Actividad.State) cbEstado.getSelectedItem();

                Habito hActualizado = new Habito(
                        Integer.parseInt(txtId.getText().trim()),
                        nombre,
                        estado,
                        fecha,
                        hora,
                        frecuencia);

                gestor.updateHabito(hActualizado);

                JOptionPane.showMessageDialog(this,
                        "Hábito actualizado correctamente.");
                dispose();

            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(this,
                        "Formato de fecha o hora inválido.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private JPanel createRow() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        p.setBackground(Color.WHITE);
        return p;
    }

    private JPanel createRowCenter() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        p.setBackground(Color.WHITE);
        return p;
    }

    private JPanel createLabeledRow(String label, JComponent comp) {
        JPanel row = createRow();
        row.add(styledLabel(label));
        row.add(comp);
        return row;
    }

    private JLabel styledLabel(String txt) {
        JLabel lbl = new JLabel(txt);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        return lbl;
    }

    private JTextField styledTextField(int width) {
        JTextField txt = new JTextField();
        txt.setPreferredSize(new Dimension(width, 28));
        txt.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txt.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180)),
                BorderFactory.createEmptyBorder(3, 5, 3, 5)
        ));
        return txt;
    }

    private <T> JComboBox<T> styledComboHabito(T[] items) {
        JComboBox<T> combo = new JComboBox<>(items);
        combo.setPreferredSize(new Dimension(120, 28));
        combo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        combo.setBackground(Color.WHITE);
        combo.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180)));
        return combo;
    }

    private <T> JComboBox<T> styledComboEstado(T[] items) {
        JComboBox<T> combo = new JComboBox<>(items);
        combo.setPreferredSize(new Dimension(120, 28));
        combo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        combo.setBackground(Color.WHITE);
        combo.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180)));
        return combo;
    }

    private JButton styledButton(String text) {
        JButton btn = new JButton(text);
        btn.setFocusPainted(false);
        btn.setBackground(Colours.Cl_Guardar);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mouseEntered(java.awt.event.MouseEvent e) {
                btn.setBackground(Colours.COLOR_BOTON_HOVER);
            }
            @Override public void mouseExited(java.awt.event.MouseEvent e) {
                btn.setBackground(Colours.Cl_Guardar);
            }
        });
        return btn;
    }
}

}
