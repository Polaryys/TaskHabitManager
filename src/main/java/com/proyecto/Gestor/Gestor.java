package main.java.com.proyecto.Gestor;

import main.java.com.proyecto.Modelos.Tarea;
import main.java.com.proyecto.Modelos.Actividad;
import main.java.com.proyecto.Modelos.Habito;
import main.java.com.proyecto.Datos.DataGlobal;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Random;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Gestor {

    private Random random;
    private DataGlobal dataGlobal;

    public Gestor(DataGlobal dataGlobal) {
        random = new Random();
        this.dataGlobal = dataGlobal;
    }

    private int generarIdAleatorio() {
        return random.nextInt(1001);
    }

    private int generarIDHabit() {
        return 1000 + random.nextInt(9000);
    }

    public Tarea createTask(String nombre,
            String descripcion,
            Tarea.Priority prioridad,
            LocalDate fecha,
            LocalTime hora) {

        Tarea tarea = new Tarea(
                generarIdAleatorio(),
                nombre,
                Actividad.State.PENDIENTE,
                fecha,
                hora,
                descripcion,
                prioridad);

        dataGlobal.guardarTarea(tarea);

        return tarea;
    }

    public Habito createHabit(String nombre,

            Habito.Frequency frecuencia,
            LocalDate fecha,
            LocalTime hora) {

        Habito habito = new Habito(
                generarIDHabit(),
                nombre,
                Actividad.State.PENDIENTE,
                fecha,
                hora,
                frecuencia);

        dataGlobal.guardarHabito(habito);
        return habito;
    }

    public boolean DeleteActivity(int id) {

        Tarea tarea = dataGlobal.SearchTaskId(id);
        if (tarea != null) {
            dataGlobal.DelTarea(id);
            return true;
        }

        Habito habito = dataGlobal.SearchHabitId(id);
        if (habito != null) {
            dataGlobal.DelHabito(id);
            return true;
        }

        return false;
    }

    public String SearchActivity(int id) {
        Tarea tarea = dataGlobal.SearchTaskId(id);
        if (tarea != null) {
            return "TAREA\n" +
                    "ID: " + tarea.getId() + "\n" +
                    "Nombre: " + tarea.getNombre() + "\n" +
                    "Descripción: " + tarea.getDescripcion() + "\n" +
                    "Prioridad: " + tarea.getPrioridad() + "\n" +
                    "Estado: " + tarea.getEstado() + "\n" +
                    "Fecha: " + tarea.getFecha() + "\n" +
                    "Hora: " + tarea.getHora();
        }

        Habito habito = dataGlobal.SearchHabitId(id);
        if (habito != null) {
            return "HÁBITO\n" +
                    "ID: " + habito.getId() + "\n" +
                    "Nombre: " + habito.getNombre() + "\n" +
                    "Frecuencia: " + habito.getFrecuencia() + "\n" +
                    "Estado: " + habito.getEstado() + "\n" +
                    "Fecha: " + habito.getFecha() + "\n" +
                    "Hora: " + habito.getHora();
        }

        return null;
    }

    public Tarea SearchTaskId(int id) {
        return dataGlobal.SearchTaskId(id);
    }

    public Habito SearchHabitId(int id) {
        return dataGlobal.SearchHabitId(id);
    }

    public boolean updateTask(Tarea tareaActualizada) {
        return DataGlobal.updateTarea(tareaActualizada);
    }

    public void updateHabito(Habito h) {
        dataGlobal.updateHabito(h);
    }

    // Método que retorna una lista de tareas ordenadas por prioridad, fecha y hora
    public List<Tarea> obtenerTareasOrdenadas() {
        List<Tarea> tareas = dataGlobal.obtenerTareasList();
        Collections.sort(tareas, new Comparator<Tarea>() {
            @Override
            public int compare(Tarea t1, Tarea t2) {
                //Prioridad primeo, luego fecha, luego hora
                int p1 = t1.getPrioridad().ordinal();
                int p2 = t2.getPrioridad().ordinal();
                if (p1 != p2) {
                    return Integer.compare(p2, p1); // Más alta prioridad primero
                }
                // MIsma prioridad: comparar fecha
                int fechaComp = t1.getFecha().compareTo(t2.getFecha());
                if (fechaComp != 0) return fechaComp;
                // Misma fecha: comparar hora
                return t1.getHora().compareTo(t2.getHora());
            }
        });
        return tareas;
    }
}
