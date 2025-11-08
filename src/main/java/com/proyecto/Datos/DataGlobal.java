package main.java.com.proyecto.Datos;

import main.java.com.proyecto.Modelos.*;
import java.util.ArrayList;
import java.util.List;
import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;

public class DataGlobal {
    private static final String FILE_PATH_TAREAS = System.getProperty("user.dir")
            + "/src/main/java/com/proyecto/Datos/Tareas.csv";
    private static final String FILE_PATH_HABITOS = System.getProperty("user.dir")
            + "/src/main/java/com/proyecto/Datos/Habitos.csv";

    public void guardarTarea(Tarea tarea) {
        try (FileWriter writer = new FileWriter(FILE_PATH_TAREAS, true)) {
            writer.write(String.format("%d,%s,%s,%s,%s,%s,%s%n",
                    tarea.getId(),
                    tarea.getNombre(),
                    tarea.getDescripcion(),
                    tarea.getPrioridad(),
                    tarea.getEstado(),
                    tarea.getHora(),
                    tarea.getFecha()));
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
                    habito.getFecha()));
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
                            Tarea.Priority.valueOf(parts[3]));
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
                            Habito.Frequency.valueOf(parts[2]));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

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

    public static boolean updateTarea(Tarea tareaActualizada) {
        File original = new File(FILE_PATH_TAREAS);
        File temporal = new File(FILE_PATH_TAREAS + ".tmp");

        boolean found = false; // <-- declarar fuera del try para poder usarlo después

        try (BufferedReader reader = new BufferedReader(new FileReader(original));
                BufferedWriter writer = new BufferedWriter(new FileWriter(temporal))) {

            String line;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");

                if (parts.length == 0) {
                    writer.write(line + System.lineSeparator());
                    continue;
                }

                int id;
                try {
                    id = Integer.parseInt(parts[0]);
                } catch (NumberFormatException nfe) {
                    writer.write(line + System.lineSeparator());
                    continue;
                }

                if (id == tareaActualizada.getId()) {
                    writer.write(String.format("%d,%s,%s,%s,%s,%s,%s%n",
                            tareaActualizada.getId(),
                            tareaActualizada.getNombre(),
                            tareaActualizada.getDescripcion(),
                            tareaActualizada.getPrioridad(),
                            tareaActualizada.getEstado(),
                            tareaActualizada.getHora(),
                            tareaActualizada.getFecha()));
                    found = true;
                } else {
                    writer.write(line + System.lineSeparator());
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        if (!original.delete()) {
            System.err.println("No se pudo eliminar el archivo original: " + original.getAbsolutePath());
            return false;
        }

        if (!temporal.renameTo(original)) {
            System.err.println("No se pudo renombrar el archivo temporal a original: " + temporal.getAbsolutePath());
            return false;
        }

        return found;
    }

    public boolean updateHabito(Habito habitoActualizado) {
        boolean actualizado = false;
        File originalFile = new File(FILE_PATH_HABITOS);
        File tempFile = new File(FILE_PATH_HABITOS + ".tmp");

        try (BufferedReader reader = new BufferedReader(new FileReader(originalFile));
                BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                int id = Integer.parseInt(parts[0]);

                if (id == habitoActualizado.getId()) {
                    writer.write(String.format("%d,%s,%s,%s,%s,%s%n",
                            habitoActualizado.getId(),
                            habitoActualizado.getNombre(),
                            habitoActualizado.getFrecuencia(),
                            habitoActualizado.getEstado(),
                            habitoActualizado.getHora(),
                            habitoActualizado.getFecha()));
                    actualizado = true;
                } else {
                    writer.write(line + System.lineSeparator());
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        originalFile.delete();
        tempFile.renameTo(originalFile);

        return actualizado;
    }

    // Método que retorna una lista con todas las tareas
    public List<Tarea> obtenerTareasList() {
        List<Tarea> tareas = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH_TAREAS))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(",");
                try {
                    int id = Integer.parseInt(parts[0]);
                    String nombre = parts.length > 1 ? parts[1] : "";
                    String descripcion = parts.length > 2 ? parts[2] : "";
                    Tarea.Priority prioridad = parts.length > 3 ? Tarea.Priority.valueOf(parts[3]) : Tarea.Priority.MEDIA;
                    Actividad.State estado = parts.length > 4 ? Actividad.State.valueOf(parts[4]) : Actividad.State.PENDIENTE;
                    java.time.LocalTime hora = parts.length > 5 ? java.time.LocalTime.parse(parts[5]) : java.time.LocalTime.of(0,0);
                    java.time.LocalDate fecha = parts.length > 6 ? java.time.LocalDate.parse(parts[6]) : java.time.LocalDate.now();

                    Tarea t = new Tarea(id, nombre, estado, fecha, hora, descripcion, prioridad);
                    tareas.add(t);
                } catch (Exception ex) {
                    // salta líneas mal formadas pero continúa
                    System.err.println("Warning: skipping malformed tarea line: " + line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tareas;
    }

}
