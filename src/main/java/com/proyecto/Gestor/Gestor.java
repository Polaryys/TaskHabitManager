package main.java.com.proyecto.Gestor;

import main.java.com.proyecto.Modelos.Tarea;
import main.java.com.proyecto.Modelos.Actividad;
import main.java.com.proyecto.Datos.DataGlobal;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Random;

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
                prioridad
        );

        dataGlobal.guardarTarea(tarea);

        return tarea;
    }


}


