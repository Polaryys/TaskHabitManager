package main.java.com.proyecto.Modelos;

import java.time.LocalDate;
import java.time.LocalTime;

public class Tarea extends Actividad {
    public enum Priority{
        BAJA,
        MEDIA,
        ALTA
    }

    private String descripcion;
    private Priority prioridad;

    public Tarea(int id, String nombre, State estado, LocalDate fecha, LocalTime hora,
                 String descripcion, Priority prioridad) {
        super(id, nombre, estado, fecha, hora);
        this.descripcion = descripcion;
        this.prioridad = prioridad;
    }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public Priority getPrioridad() { return prioridad; }
    public void setPrioridad(Priority prioridad) { this.prioridad = prioridad; }
}