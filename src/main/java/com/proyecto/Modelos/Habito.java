package main.java.com.proyecto.Modelos;

import java.time.LocalDate;
import java.time.LocalTime;

public class Habito extends Actividad {
    public enum Frequency{
        DIARIO,
        SEMANAL,
        MENSUAL
    }
    private Frequency frecuencia;

    public Habito(int id, String nombre, State estado, LocalDate fecha, LocalTime hora,
                  Frequency frecuencia) {
        super(id, nombre, estado, fecha, hora);
        this.frecuencia = frecuencia;
    }

    public Frequency getFrecuencia() { return frecuencia; }
    public void setFrecuencia(Frequency frecuencia) { this.frecuencia = frecuencia; }
}