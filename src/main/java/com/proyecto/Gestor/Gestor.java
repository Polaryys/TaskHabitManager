package main.java.com.proyecto.Gestor;

import main.java.com.proyecto.Modelos.Tarea;
import main.java.com.proyecto.Modelos.Actividad;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Gestor {
    private List<Tarea> listaTareas = new ArrayList<>();
    private int count = 1;

    public void createTask(String nombre, String descripcion, Tarea.Priority prioridad,
        LocalDate fecha, LocalTime hora){

            Tarea Task = new Tarea(
                count++,
                nombre,
                Actividad.State.PENDIENTE,
                fecha,
                hora,
                descripcion,
                prioridad
            );

            listaTareas.add(Task);

            }
        }


