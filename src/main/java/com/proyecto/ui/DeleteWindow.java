package main.java.com.proyecto.ui;

import main.java.com.proyecto.Gestor.Gestor;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class DeleteWindow extends JDialog {
    @SuppressWarnings("unused")
    private Gestor gestor;

    

    public DeleteWindow(JFrame parent, Gestor gestor) {
        super(parent, "Eliminar Actividad", true);
        this.gestor = gestor;


        setSize(480, 360);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Colours.Cl_Fondo);


        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(12, 12));
        mainPanel.setBackground(Colours.Cl_Tarjeta);
        mainPanel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(Colours.Cl_Guardar, 1),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));


        JLabel titulo = new JLabel("ELIMINAR ACTIVIDAD");
        titulo.setFont(Colours.Ft_Titulo);
        titulo.setForeground(Colours.Cl_Titulo);
        titulo.setHorizontalAlignment(SwingConstants.CENTER);
        titulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        mainPanel.add(titulo, BorderLayout.NORTH);


        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        topPanel.setBackground(Colours.Cl_Tarjeta);

        JLabel lblId = new JLabel("ID:");
        lblId.setFont(Colours.Ft_Label);
        lblId.setForeground(Colours.Cl_Texto);

        JTextField txtId = new JTextField(10);
        txtId.setFont(Colours.Ft_Label);
        txtId.setPreferredSize(new Dimension(120, 28));

        JButton btnBuscar = crearBoton("Buscar", Colours.Cl_Guardar);

        topPanel.add(lblId);
        topPanel.add(txtId);
        topPanel.add(btnBuscar);

        mainPanel.add(topPanel, BorderLayout.CENTER);


        JTextArea infoArea = new JTextArea();
        infoArea.setEditable(false);
        infoArea.setFont(Colours.Ft_Info);
        infoArea.setForeground(Colours.Cl_Texto);
        infoArea.setBackground(Color.WHITE);
        infoArea.setLineWrap(true);
        infoArea.setWrapStyleWord(true);
        infoArea.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));

        JScrollPane scrollPane = new JScrollPane(infoArea);
        scrollPane.setBorder(new LineBorder(Colours.Cl_Guardar, 1));
        scrollPane.setPreferredSize(new Dimension(380, 150));

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BorderLayout());
        infoPanel.setBackground(Colours.Cl_Fondo);
        infoPanel.add(scrollPane, BorderLayout.CENTER);

        mainPanel.add(infoPanel, BorderLayout.SOUTH);


        JButton btnEliminar = crearBoton("Eliminar", Colours.Cl_Cancelar);
        btnEliminar.setEnabled(false);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(Colours.Cl_Tarjeta);
        bottomPanel.add(btnEliminar);

        add(mainPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

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

        btnEliminar.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "¿Seguro que deseas eliminar esta actividad?",
                    "Confirmar",
                    JOptionPane.YES_NO_OPTION);

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

    private JButton crearBoton(String texto, Color color) {
        JButton btn = new JButton(texto);
        btn.setPreferredSize(new Dimension(120, 32));
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFont(Colours.Ft_Boton);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createLineBorder(color.darker(), 2));
        return btn;
    }
}