package main.java.com.proyecto.ui;

import main.java.com.proyecto.Gestor.Gestor;

import javax.swing.*;
import java.awt.*;

public class DeleteWindow extends JDialog {

    private Gestor gestor;

    public DeleteWindow(JFrame parent, Gestor gestor) {
        super(parent, "Eliminar Actividad", true);
        this.gestor = gestor;

        setSize(450, 300);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JLabel lblId = new JLabel("ID:");
        JTextField txtId = new JTextField(10);
        JButton btnBuscar = new JButton("Buscar");

        topPanel.add(lblId);
        topPanel.add(txtId);
        topPanel.add(btnBuscar);

        JTextArea infoArea = new JTextArea();
        infoArea.setEditable(false);
        infoArea.setLineWrap(true);
        infoArea.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(infoArea);

        JButton btnEliminar = new JButton("Eliminar");
        btnEliminar.setEnabled(false); 

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(btnEliminar, BorderLayout.SOUTH);

        btnBuscar.addActionListener(e -> {
            try {
                int id = Integer.parseInt(txtId.getText().trim());

                String actividad = gestor.SearchActivity(id); 

                if (actividad == null) {
                    infoArea.setText("No se encontró ninguna actividad con ese ID.");
                    btnEliminar.setEnabled(false);
                } else {
                    infoArea.setText(actividad);
                    btnEliminar.setEnabled(true);
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                        "ID inválido. Debe ser un número.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        // ---------- Acción al eliminar ----------
        btnEliminar.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "¿Seguro que deseas eliminar esta actividad?",
                    "Confirmar",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {
                int id = Integer.parseInt(txtId.getText().trim());
                boolean eliminado = gestor.DeleteActivity(id);

                if (eliminado) {
                    JOptionPane.showMessageDialog(this, "Actividad eliminada.");
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Error al eliminar.");
                }
            }
        });
    }
}