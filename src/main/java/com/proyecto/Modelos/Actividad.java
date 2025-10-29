package main.java.com.proyecto.Modelos;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.DayOfWeek;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

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

    public static void mostrarReporteSemanal(List<Tarea> tareas, List<Habito> habitos) {

       
        if (tareas == null || habitos == null) {
            System.out.println("Error: Datos no disponibles");
            return;
        } 

        LocalDate hoy = LocalDate.now();
        LocalDate inicioSemana = hoy.with(DayOfWeek.MONDAY);
        LocalDate finSemana = hoy.with(DayOfWeek.SATURDAY);

        // filtro para actividades de toda la semana
        List<Tarea> tareasSemana = filtrarPorSemana(tareas, inicioSemana, finSemana);
        List<Habito> habitosSemana = filtrarPorSemana(habitos, inicioSemana, finSemana);

        // generar el reporte 
        mostrarEncabezado(inicioSemana, finSemana); 
        mostrarResumen(tareasSemana, habitosSemana);
        mostrarDetalles(tareasSemana, habitosSemana); 
    }

        //filtro
        private static <T extends Actividad> List<T> filtrarPorSemana(List<T> actividades, LocalDate inicio, LocalDate fin) {
            return actividades.stream()
                    .filter(a -> !a.getFecha().isBefore(inicio) && !a.getFecha().isAfter(fin))
                    .collect(Collectors.toList());
        }

        // encabezado 
        private static  void mostrarEncabezado(LocalDate inicio, LocalDate fin) {
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            System.out.println("-----------------------------------------------------");
            System.out.println("       reporte semanal         ");
            System.out.println("-----------------------------------------------------");
            System.out.printf("Periodo: %s al %s%n", inicio.format(fmt), fin.format(fmt));
            System.out.printf("Generando: %s %s%n", 
                LocalDate.now().format(fmt), 
                LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"))); 
            System.out.println();
        }

        // resumen formal 
        private static void mostrarResumen(List<Tarea> tareas, List<Habito> habitos) {
            long tareasComp = contarCompletados(tareas); 
            long habitosComp = contarCompletados(habitos); 

            System.out.println(" resumen  ");
            System.out.println("-----------------------------------------------------");
            System.out.printf("- Tareas: %d/%d completadas (%.0f%%)%n",
                tareasComp, tareas.size(), calcularPorcentaje (tareasComp, tareas.size()));
            System.out.printf("- Habitos: %d/%d completados (%.0f%%)%n", 
                habitosComp, habitos.size(), calcularPorcentaje(habitosComp, habitos.size()));
            System.out.printf("- Productividad general: %.0f%%%n", 
                calcularPorcentaje(tareasComp + habitosComp, tareas.size() + habitos.size()));
            System.out.println();
        }

        private static void mostrarDetalles(List<Tarea> tareas, List<Habito> habitos) {
            mostrarTareas(tareas);
            mostrarHabitos(habitos);
            mostrarRecomendaciones(tareas, habitos);
        }

        // tareas formales: 
        private static void mostrarTareas(List<Tarea> tareas) {
        System.out.println("TAREAS (Agrupadas por Prioridad)");
        System.out.println("--------------------------------------------");

        if (tareas.isEmpty()) {
            System.out.println("no hay tareas registradadas esta semana\n");
            return;
        }

         Map<Tarea.Priority, List<Tarea>> porPrioridad = tareas.stream()
                .collect(Collectors.groupingBy(Tarea::getPrioridad));
        
        mostrarGrupoTareas("ALTA PRIORIDAD", porPrioridad.get(Tarea.Priority.ALTA));
        mostrarGrupoTareas("MEDIA PRIORIDAD", porPrioridad.get(Tarea.Priority.MEDIA));
        mostrarGrupoTareas("BAJA PRIORIDAD", porPrioridad.get(Tarea.Priority.BAJA));
    }

        private static void mostrarGrupoTareas(String titulo, List<Tarea> tareas) {
            if (tareas != null && !tareas.isEmpty()) {
            System.out.println(titulo + ":");
            for (Tarea tarea : tareas) {
                String estado = (tarea.getEstado() == State.COMPLETADO) ? "[COMPLETADA]" : "[PENDIENTE]";
                System.out.printf("   %s %s - %s%n", 
                    estado, tarea.getNombre(), tarea.getDescripcion());
                }

            }
        }
        private static void mostrarHabitos(List<Habito> habitos){
        System.out.println("\nHabitos por Frecuencia");
        System.out.println("--------------------------------------------");
        
        if (habitos.isEmpty()) {
            System.out.println("   No hay habitos registrados para esta semana\n");
            return;
        }

        Map<Habito.Frequency, List<Habito>> porFrecuencia = habitos.stream()
                .collect(Collectors.groupingBy(Habito::getFrecuencia));
        
        mostrarGrupoHabitos("DIARIOS", porFrecuencia.get(Habito.Frequency.DIARIO));
        mostrarGrupoHabitos("SEMANALES", porFrecuencia.get(Habito.Frequency.SEMANAL));
        mostrarGrupoHabitos("MENSUALES", porFrecuencia.get(Habito.Frequency.MENSUAL));
        }
        private static void mostrarGrupoHabitos(String titulo, List<Habito> habitos) {
        if (habitos != null && !habitos.isEmpty()) {
            System.out.println(titulo + ":");
            for (Habito habito : habitos) {
                String estado = (habito.getEstado() == State.COMPLETADO) ? "[COMPLETADO]" : "[PENDIENTE]";
                System.out.printf("   %s %s%n", estado, habito.getNombre());
            }
        }
    }

        // recomendaciones 
        private static void mostrarRecomendaciones(List<Tarea> tareas, List<Habito> habitos) {
            long tareasAltasPend = tareas.stream()
                .filter(t -> t.getPrioridad() == Tarea.Priority.ALTA && t.getEstado() == State.PENDIENTE)
                .count();
            long tareasPend = contarPendientes(tareas);
            long habitosPend = contarPendientes(habitos);

            System.out.println("\nRECOMENDACIONES");
            System.out.println("--------------------------------------------");

        if (tareasAltasPend > 0) 
            System.out.println(" Prioriza " + tareasAltasPend + " tareas de ALTA prioridad pendientes");
        if (tareasPend > 3) 
            System.out.println(" Considerar dividir " + tareasPend + " tareas pendientes en pasos mas pequenos");
        if (habitosPend > 0) 
            System.out.println(" Enfocarse en completar " + habitosPend + " habitos pendientes");
        if (tareasAltasPend == 0 && tareasPend == 0 && habitosPend == 0) 
            System.out.println(" Excelente trabajo Manten este ritmo de productividad");

            System.out.println();
        }

        private static long contarCompletados(List<? extends Actividad> actividades) {
        return actividades.stream().filter(a -> a.getEstado() == State.COMPLETADO).count();
    }

        private static long contarPendientes(List<? extends Actividad> actividades) {
        return actividades.stream().filter(a -> a.getEstado() == State.PENDIENTE).count();
    }

        private static double calcularPorcentaje(long completados, long total) {
        return total > 0 ? (completados * 100.0 / total) : 0;
    }

    public static void main(String[] args) {
        System.out.println("Preparando el reporte semanal\n");

        List<Tarea> tareas = cargarDesdeCSV("Tareas.csv", Actividad::crearTarea);
        List<Habito> habitos = cargarDesdeCSV("Habitos.csv", Actividad::crearHabito);

        System.out.printf("Datos cargados: %d tareas, %d habitos%n%n", tareas.size(), habitos.size());
        Actividad.mostrarReporteSemanal(tareas, habitos);
        
        System.out.println("Reporte Generado con exito");
    }

    // para traer los datos de los archivos csv.

    private static <T> List<T> cargarDesdeCSV(String archivo, java.util.function.Function<String[], T> creador) {
        List<T> items = new ArrayList<>();
        try {
            String ruta = "src/main/java/com/proyecto/Datos/" + archivo;
            java.nio.file.Files.lines(java.nio.file.Paths.get(ruta))
                .skip(1)
                    .map(linea -> linea.split(","))
                    .filter(datos -> datos.length >= (archivo.equals("Tareas.csv") ? 7 : 6))

                    .forEach(datos -> items.add(creador.apply(datos)));
        } catch (Exception e) {
            System.out.println("Error cargando " + archivo + ": " + e.getMessage());
        }
        return items;
    }


     private static Tarea crearTarea(String [] datos) {
        return new Tarea(
            Integer.parseInt(datos[0]), datos[1], State.valueOf(datos[2]),
            LocalDate.parse(datos[3]), LocalTime.parse(datos[4]), 
            datos[5], Tarea.Priority.valueOf(datos[6])
        );
        
    }
        private static Habito crearHabito(String[] datos) {
        return new Habito(
            Integer.parseInt(datos[0]), datos[1], State.valueOf(datos[2]),
            LocalDate.parse(datos[3]), LocalTime.parse(datos[4]), 
            Habito.Frequency.valueOf(datos[5])
        );
    }
}
        
   
    
