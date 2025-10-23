package main.java.com.proyecto.Modelos;

import java.time.LocalDate;
import java.time.LocalTime;

public class Habito extends Actividad {
    private String frecuencia;

    public Habito(int id, String nombre, String estado, LocalDate fecha, LocalTime hora,
                  String frecuencia) {
        super(id, nombre, estado, fecha, hora);
        this.frecuencia = frecuencia;
    }

    public String getFrecuencia() { return frecuencia; }
    public void setFrecuencia(String frecuencia) { this.frecuencia = frecuencia; }
}