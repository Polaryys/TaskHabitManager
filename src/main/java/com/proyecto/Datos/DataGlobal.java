package main.java.com.proyecto.Datos;
import main.java.com.proyecto.Modelos.*; 
import java.io.*;

public class DataGlobal {
    private static final String FILE_PATH = System.getProperty("user.dir") + "/src/main/java/com/proyecto/Datos/Tareas.csv";

    public void guardarTarea(Tarea tarea) {
        try {
            FileWriter writer = new FileWriter(FILE_PATH, true);
            writer.write(String.format("%d,%s,%s,%s,%s,%s,%s%n",
                    tarea.getId(),
                    tarea.getNombre(),
                    tarea.getDescripcion(),
                    tarea.getPrioridad(),
                    tarea.getEstado(),
                    tarea.getHora(),
                    tarea.getFecha()
            ));
            writer.close();
        } catch (IOException e) {
            System.err.println("Error al guardar la tarea: " + e.getMessage());
        }
    }
}