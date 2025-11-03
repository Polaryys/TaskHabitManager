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

        setSize(300, 180);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));

        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));

        JButton btnTarea = new JButton("Tarea");
        JButton btnHabito = new JButton("Hábito");

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

    class TaskEdit extends JDialog {
        @SuppressWarnings("unused")
        private Gestor gestor;

        public TaskEdit(JFrame parent, Gestor gestor) {
            super(parent, "Editar Tarea", true);
            this.gestor = gestor;

            setSize(430, 380);
            setLocationRelativeTo(parent);
            setLayout(new BorderLayout());

            JPanel mainPanel = new JPanel();
            mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
            mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            JPanel rowId = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
            JLabel lblId = new JLabel("ID:");
            JTextField txtId = new JTextField();
            txtId.setPreferredSize(new Dimension(100, 25));
            JButton btnBuscar = new JButton("Buscar");

            rowId.add(lblId);
            rowId.add(txtId);
            rowId.add(btnBuscar);
            mainPanel.add(rowId);

            JTextField txtNombre = new JTextField();
            JTextField txtDescripcion = new JTextField();

            String[] prioridades = { "ALTA", "MEDIA", "BAJA" };
            JComboBox<String> comboPrioridad = new JComboBox<>(prioridades);

            String[] estados = { "PENDIENTE", "COMPLETADO" };
            JComboBox<String> comboEstado = new JComboBox<>(estados);

            JTextField txtFecha = new JTextField();
            JTextField txtHora = new JTextField();

            JPanel row1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
            row1.add(new JLabel("Nombre:"));
            txtNombre.setPreferredSize(new Dimension(200, 25));
            row1.add(txtNombre);
            mainPanel.add(row1);

            JPanel row2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
            row2.add(new JLabel("Descripción:"));
            txtDescripcion.setPreferredSize(new Dimension(200, 25));
            row2.add(txtDescripcion);
            mainPanel.add(row2);

            JPanel row3 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
            row3.add(new JLabel("Prioridad:"));
            comboPrioridad.setPreferredSize(new Dimension(120, 25));
            row3.add(comboPrioridad);
            mainPanel.add(row3);

            JPanel row4 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
            row4.add(new JLabel("Estado:"));
            comboEstado.setPreferredSize(new Dimension(120, 25));
            row4.add(comboEstado);
            mainPanel.add(row4);

            JPanel row5 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
            row5.add(new JLabel("Fecha:"));
            txtFecha.setPreferredSize(new Dimension(120, 25));
            row5.add(txtFecha);
            mainPanel.add(row5);

            JPanel row6 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
            row6.add(new JLabel("Hora:"));
            txtHora.setPreferredSize(new Dimension(120, 25));
            row6.add(txtHora);
            mainPanel.add(row6);

            JPanel rowButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
            JButton btnEditar = new JButton("Editar");
            JButton btnCerrar = new JButton("Cerrar");

            btnEditar.setEnabled(false);

            rowButtons.add(btnEditar);
            rowButtons.add(btnCerrar);

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
                    if (Arrays.asList(prioridades).contains(pr)) {
                        comboPrioridad.setSelectedItem(pr);
                    } else {
                        comboPrioridad.setSelectedItem("MEDIA");
                    }

                    String es = t.getEstado().toString().toUpperCase();
                    if (Arrays.asList(estados).contains(es)) {
                        comboEstado.setSelectedItem(es);
                    } else {
                        comboEstado.setSelectedItem("PENDIENTE");
                    }

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
                    main.java.com.proyecto.Modelos.Actividad.State estadoEnum = main.java.com.proyecto.Modelos.Actividad.State
                            .valueOf(comboEstado.getSelectedItem().toString());
                    main.java.com.proyecto.Modelos.Tarea.Priority prioridadEnum = main.java.com.proyecto.Modelos.Tarea.Priority
                            .valueOf(comboPrioridad.getSelectedItem().toString());

                    main.java.com.proyecto.Modelos.Tarea nueva = new main.java.com.proyecto.Modelos.Tarea(
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
    }

    class HabitEdit extends JDialog {
        @SuppressWarnings("unused")
        private Gestor gestor;

        public HabitEdit(JFrame parent, Gestor gestor) {
            super(parent, "Editar Hábito", true);
            this.gestor = gestor;

            setSize(430, 320);
            setLocationRelativeTo(parent);
            setLayout(new BorderLayout());

            JPanel mainPanel = new JPanel();
            mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
            mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            JPanel rowId = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
            JLabel lblId = new JLabel("ID:");
            JTextField txtId = new JTextField();
            txtId.setPreferredSize(new Dimension(100, 25));
            JButton btnBuscar = new JButton("Buscar");
            rowId.add(lblId);
            rowId.add(txtId);
            rowId.add(btnBuscar);
            mainPanel.add(rowId);

            JTextField txtNombre = new JTextField();
            JComboBox<Habito.Frequency> cbFrecuencia = new JComboBox<>(Habito.Frequency.values());
            JComboBox<Actividad.State> cbEstado = new JComboBox<>(Actividad.State.values());
            JTextField txtFecha = new JTextField();
            JTextField txtHora = new JTextField();

            JPanel row1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
            JLabel lblName = new JLabel("Nombre:");
            txtNombre.setPreferredSize(new Dimension(200, 25));
            row1.add(lblName);
            row1.add(txtNombre);
            mainPanel.add(row1);

            JPanel row2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
            JLabel lblFreq = new JLabel("Frecuencia:");
            row2.add(lblFreq);
            row2.add(cbFrecuencia);
            mainPanel.add(row2);

            JPanel row3 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
            JLabel lblEstado = new JLabel("Estado:");
            row3.add(lblEstado);
            row3.add(cbEstado);
            mainPanel.add(row3);

            JPanel row4 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
            JLabel lblDate = new JLabel("Fecha (AAAA-MM-DD):");
            txtFecha.setPreferredSize(new Dimension(120, 25));
            row4.add(lblDate);
            row4.add(txtFecha);
            mainPanel.add(row4);

            JPanel row5 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
            JLabel lblHour = new JLabel("Hora (HH:mm):");
            txtHora.setPreferredSize(new Dimension(80, 25));
            row5.add(lblHour);
            row5.add(txtHora);
            mainPanel.add(row5);

            JPanel rowButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
            JButton btnEditar = new JButton("Editar");
            JButton btnCerrar = new JButton("Cerrar");
            btnEditar.setEnabled(false);
            rowButtons.add(btnEditar);
            rowButtons.add(btnCerrar);
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
    }

}
