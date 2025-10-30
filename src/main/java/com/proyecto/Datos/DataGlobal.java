package main.java.com.proyecto.Datos;
import main.java.com.proyecto.Modelos.*; 
import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;

public class DataGlobal {
    private static final String FILE_PATH_TAREAS = System.getProperty("user.dir") + "/src/main/java/com/proyecto/Datos/Tareas.csv";
    private static final String FILE_PATH_HABITOS = System.getProperty("user.dir") + "/src/main/java/com/proyecto/Datos/Habitos.csv";

    public void guardarTarea(Tarea tarea) {
        try (FileWriter writer = new FileWriter(FILE_PATH_TAREAS, true)) {
            writer.write(String.format("%d,%s,%s,%s,%s,%s,%s%n",
                    tarea.getId(),
                    tarea.getNombre(),
                    tarea.getDescripcion(),
                    tarea.getPrioridad(),
                    tarea.getEstado(),
                    tarea.getHora(),
                    tarea.getFecha()
            ));
        } catch (IOException e) {
            System.err.println("Error al guardar la tarea: " + e.getMessage());
        }
    }

    public void guardarHabito(Habito habito) {
        try (FileWriter writer = new FileWriter(FILE_PATH_HABITOS, true)) {
            writer.write(String.format("%d,%s,%s,%s,%s,%s%n",
                    habito.getId(),
                    habito.getNombre(),
                    habito.getFrecuencia(),
                    habito.getEstado(),
                    habito.getHora(),
                    habito.getFecha()
            ));
        } catch (IOException e) {
            System.err.println("Error al guardar el hábito: " + e.getMessage());
        }
    }

    public Tarea SearchTaskId(int id) {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH_TAREAS))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (Integer.parseInt(parts[0]) == id) {
                    return new Tarea(
                            Integer.parseInt(parts[0]),
                            parts[1],
                            Actividad.State.valueOf(parts[4]),
                            LocalDate.parse(parts[6]),
                            LocalTime.parse(parts[5]),
                            parts[2],
                            Tarea.Priority.valueOf(parts[3])
                    );
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Habito SearchHabitId(int id) {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH_HABITOS))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (Integer.parseInt(parts[0]) == id) {
                    return new Habito(
                            Integer.parseInt(parts[0]),
                            parts[1],
                            Actividad.State.valueOf(parts[3]),
                            LocalDate.parse(parts[5]),
                            LocalTime.parse(parts[4]),
                            Habito.Frequency.valueOf(parts[2])
                    );
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    // --- Eliminar ---
    public void DelTarea(int id) {
        eliminarLinea(FILE_PATH_TAREAS, id);
    }

    public void DelHabito(int id) {
        eliminarLinea(FILE_PATH_HABITOS, id);
    }

    private void eliminarLinea(String filePath, int id) {
        File originalFile = new File(filePath);
        File tempFile = new File(filePath + ".tmp");

        try (BufferedReader reader = new BufferedReader(new FileReader(originalFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (Integer.parseInt(parts[0]) != id) {
                    writer.write(line + System.lineSeparator());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        originalFile.delete();
        tempFile.renameTo(originalFile);
    }
}

