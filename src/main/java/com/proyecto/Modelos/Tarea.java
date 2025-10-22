package main.java.com.proyecto.Modelos;
public class Tarea extends Actividad {
    private String descripcion;
    private int prioridad;

    public Tarea(int id, String nombre, String estado, String fecha, String hora,
                 String descripcion, int prioridad) {
        super(id, nombre, estado, fecha, hora);
        this.descripcion = descripcion;
        this.prioridad = prioridad;
    }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public int getPrioridad() { return prioridad; }
    public void setPrioridad(int prioridad) { this.prioridad = prioridad; }
}