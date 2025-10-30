package main.java.com.proyecto.ui;

import main.java.com.proyecto.Gestor.Gestor;

import javax.swing.*;


public class EditWindow extends JDialog {
    @SuppressWarnings("unused")
    private Gestor gestor;

    public EditWindow(JFrame parent, Gestor gestor) {
        super(parent, "Editar Actividad", true);
        this.gestor = gestor;

        setSize(400, 250);
        setLocationRelativeTo(parent);

        JPanel panel = new JPanel();
        panel.add(new JLabel("Aquí podrás editar una actividad próximamente."));
        
        add(panel);
    }
}
