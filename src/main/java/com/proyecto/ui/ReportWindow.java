package main.java.com.proyecto.ui;

import main.java.com.proyecto.Gestor.Gestor;
import main.java.com.proyecto.Modelos.Tarea;
import main.java.com.proyecto.Modelos.Habito;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ReportWindow extends JDialog {
    private Gestor gestor;

     public ReportWindow(JFrame parent, Gestor gestor) {
        super(parent, "Reporte Semanal", true);
        this.gestor = gestor;

        setSize(800, 600);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));

        // ... (resto del código que te envié anteriormente)
    }

    private String generarReporteSemanal() {

        return "";
    }
    
}
