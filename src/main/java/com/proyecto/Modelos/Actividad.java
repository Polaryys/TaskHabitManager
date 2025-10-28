package main.java.com.proyecto.Modelos;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

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

    public static void mostrarReporte() {

        // Crear datos para simular el reporte tanto en: 

        List<Tarea> tareasEjemplo = crearTareas();
        List<Habito> habitosEjemplo = crearHabitos(); 


        LocalDate hoy = LocalDate.now();
        LocalDate inicioSemana = hoy.minusDays(hoy.getDayOfWeek().getValue() - 1); 
        LocalDate finSemana = inicioSemana.plusDays(6);
        
        System.out.println("estructura del reporte semanal ");
        System.out.println("---------------------------------------");
        System.out.println("periodo: " + inicioSemana + " al " + finSemana);
        System.out.println();

        // mostrar un ejemplo de tarea 
        mostrarTareas(tareasEjemplo); 
        System.out.println();

        // mostrar ejemplos de habitos. 
        mostrarHabitos(habitosEjemplo); 
        System.out.println();

        // mostrar una estadistica
        mostrarEstadistica(tareasEjemplo, habitosEjemplo); 
    
    }

    // se crean alfunas tareas como por ejemplo: 
     private static List<Tarea> crearTareas() {
        List<Tarea> tareas = new ArrayList<>(); 

        tareas.add(new Tarea(1, "Repaso de matematicas", State.COMPLETADO, LocalDate.now(),
         LocalTime.now(), "suma y restas", Tarea.Priority.ALTA)); 
        tareas.add(new Tarea(2, "Hacer ejercicio", State.PENDIENTE, LocalDate.now(),
         LocalTime.now(), "Ir al gimnasio", Tarea.Priority.MEDIA)); 
        return tareas;

     }
     // se crean algunos habitos de ejemplo: 
     private static List<Habito> crearHabitos() {
        List<Habito> habitos = new ArrayList<>(); 
        habitos.add(new Habito(1, "Leer 30 minutos", State.COMPLETADO, LocalDate.now(),
         LocalTime.now(), Habito.Frequency.DIARIO));

        habitos.add(new Habito(2, "Correr", State.PENDIENTE, LocalDate.now(),
         LocalTime.now(), Habito.Frequency.SEMANAL));
        return habitos;
     }
     // mostrar lista de tareas 
     private static void mostrarTareas(List<Tarea> tareas) {
        System.out.println("Tareas");
        if (tareas.isEmpty()) {
            System.out.println("No hay tareas esta semana");
            return;
        }
        for (Tarea tarea : tareas) {
            String estado = (tarea.getEstado() == State.COMPLETADO) ? "completado" : "pendiente";
            
            System.out.printf(" - %s [%s] | Prioridad: %s%n",
            tarea.getNombre(),
            estado, 
            tarea.getPrioridad());
        }
                

        }

     // mostrar lista de habitos 
     private static void mostrarHabitos(List<Habito> habitos) {
        System.out.println("Habitos");
        if (habitos.isEmpty()) {
            System.out.println(" no hay habitos registrados para esta semana");
            return;
        }

        for (Habito habito : habitos) {
            String estado = (habito.getEstado() == State.COMPLETADO) ? "Completado" : "Pendiente"; 
            System.out.printf("   - %s [%s] | Frecuencia: %s%n",
            habito.getNombre(), 
            estado, 
            habito.getFrecuencia().toString().toUpperCase());
        }

     }

     // mostrar estadistica: 
     private static void mostrarEstadistica(List<Tarea> tareas, List<Habito> habitos) {
        System.out.println("Estadistica:");

    long tareasCompletadas = tareas.stream()
            .filter(t -> t.getEstado() == State.COMPLETADO)
            .count();

    long habitosCompletados = habitos.stream()
            .filter(h -> h.getEstado() == State.COMPLETADO)
            .count();
    
        System.out.printf("   Tareas completadas: %d de %d%n", tareasCompletadas, tareas.size());
        System.out.printf("   Habitos completados: %d de %d%n", habitosCompletados, habitos.size());
     }

    public static void main(String[] args) {
        Actividad.mostrarReporte();
    }
}
