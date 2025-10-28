package main.java.com.proyecto.ui;

import javax.swing.*;
import main.java.com.proyecto.Datos.DataGlobal;
import main.java.com.proyecto.Gestor.Gestor;

import java.awt.*;

public class Main extends JFrame{
    private Gestor gestor;
    public Main(){
        setTitle("Gestor de Actividades");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        DataGlobal dataGlobal = new DataGlobal();
        gestor = new Gestor(dataGlobal);;

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
