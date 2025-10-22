package main.java.com.proyecto.Modelos;

public class Habito extends Actividad {
    private String frecuencia;

    public Habito(int id, String nombre, String estado, String fecha, String hora,
                  String frecuencia) {
        super(id, nombre, estado, fecha, hora);
        this.frecuencia = frecuencia;
    }

    public String getFrecuencia() { return frecuencia; }
    public void setFrecuencia(String frecuencia) { this.frecuencia = frecuencia; }
}