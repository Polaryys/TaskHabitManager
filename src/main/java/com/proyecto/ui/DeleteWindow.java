package main.java.com.proyecto.ui;

import main.java.com.proyecto.Gestor.Gestor;

import javax.swing.*;
import java.awt.*;

public class DeleteWindow extends JDialog {

    public DeleteWindow(JFrame parent, Gestor gestor) {
        super(parent, "Eliminar Actividad", true);
        setSize(350, 200);
        setLocationRelativeTo(parent);
        setLayout(new GridLayout(2, 1, 5, 5));

        JLabel lblInfo = new JLabel("Aquí podrás eliminar una actividad.", SwingConstants.CENTER);
        JButton btnClose = new JButton("Cerrar");

        add(lblInfo);
        add(btnClose);

        btnClose.addActionListener(e -> dispose());
    }
}