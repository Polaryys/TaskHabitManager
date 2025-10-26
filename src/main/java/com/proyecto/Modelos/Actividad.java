package main.java.com.proyecto.Modelos;

import java.time.LocalDate;
import java.time.LocalTime;

public class Actividad {

    public enum State{
        COMPLETADO,
        PENDIENTE
    }
    private int id;
    private String nombre;
    private State estado;
    private LocalDate fecha;
    private LocalTime hora;

    public Actividad(int id, String nombre, State estado, LocalDate fecha, LocalTime hora) {
        this.id = id;
        this.nombre = nombre;
        this.estado = estado;
        this.fecha = fecha;
        this.hora = hora;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public State getEstado() { return estado; }
    public void setEstado(State estado) { this.estado = estado; }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    public LocalTime getHora() { return hora; }
    public void setHora(LocalTime hora) { this.hora = hora; }
}