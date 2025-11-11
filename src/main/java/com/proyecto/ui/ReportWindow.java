package main.java.com.proyecto.ui;

import main.java.com.proyecto.Gestor.Gestor;
import main.java.com.proyecto.Modelos.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.DayOfWeek;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ReportWindow extends JDialog {
    @SuppressWarnings("unused")
    private Gestor gestor;
    private JPanel mainPanel;
    
    private final Color COLOR_PRIMARIO = new Color(41, 128, 185);
    private final Color COLOR_SECUNDARIO = new Color(52, 152, 219);
    private final Color COLOR_EXITO = new Color(46, 204, 113);
    private final Color COLOR_ALERTA = new Color(231, 76, 60);
    private final Color COLOR_FONDO = new Color(245, 245, 245);
    private final Color COLOR_TARJETA = Color.WHITE;
    private final Color COLOR_TEXTO = new Color(52, 73, 94);

    private final Font FUENTE_TITULO_PRINCIPAL = new Font("Segoe UI", Font.BOLD, 22);
    private final Font FUENTE_TITULO_SECCION = new Font("Segoe UI", Font.BOLD, 18);
    private final Font FUENTE_SUBTITULO = new Font("Segoe UI", Font.BOLD, 16);
    private final Font FUENTE_TEXTO_NORMAL = new Font("Segoe UI", Font.PLAIN, 14);
    private final Font FUENTE_TEXTO_PEQUEÑO = new Font("Segoe UI", Font.PLAIN, 12);
    private final Font FUENTE_METRICA_GRANDE = new Font("Segoe UI", Font.BOLD, 24);
    @SuppressWarnings("unused")
    private final Font FUENTE_METRICA_MEDIANA = new Font("Segoe UI", Font.BOLD, 14);

    private final int ESPACIO_VERTICAL_SECCIONES = 8;
    private final int PADDING_TARJETA = 10;
    private final int ESPACIO_ENTRE_ITEMS = 4;
    private final int PADDING_ITEMS = 6;

    public ReportWindow(JFrame parent, Gestor gestor) {
        super(parent, "RESUMEN SEMANAL", true);
        this.gestor = gestor;
        configurarVentana();
        inicializarComponentes();
        generarReporte();
        setLocationRelativeTo(null);
    }

    private void configurarVentana() {
        setSize(850, 600); 
        setLocationRelativeTo(getParent());
        setLayout(new BorderLayout());
        getContentPane().setBackground(COLOR_FONDO);
    }

    private void inicializarComponentes() {
    
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(COLOR_PRIMARIO);
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15)); 
        topPanel.setPreferredSize(new Dimension(getWidth(), 60)); 
        
        JLabel titulo = new JLabel("REPORTE SEMANAL DE SUS ACTIVIDADES", SwingConstants.CENTER);
        titulo.setFont(FUENTE_TITULO_PRINCIPAL); 
        titulo.setForeground(Color.WHITE);
        topPanel.add(titulo, BorderLayout.CENTER);
        
        
        JPanel botonesPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        botonesPanel.setOpaque(false);
        for (Object[] btn : new Object[][]{
            {"Actualizar", COLOR_SECUNDARIO, (Runnable) () -> { generarReporte(); setLocationRelativeTo(null); }},
            {"Exportar TXT", new Color(39, 174, 96), (Runnable) this::exportarATXT},
            {"Cerrar", COLOR_ALERTA, (Runnable) this::dispose}
        }) {
            JButton boton = crearBoton((String)btn[0], (Color)btn[1]);
            boton.addActionListener(e -> ((Runnable)btn[2]).run());
            botonesPanel.add(boton);
        }
        topPanel.add(botonesPanel, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);
        
        
        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(COLOR_FONDO);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12)); 
        
        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setBorder(null);
        add(scrollPane, BorderLayout.CENTER);
    }

    private JButton crearBoton(String texto, Color color) {
        JButton boton = new JButton(texto);
        boton.setPreferredSize(new Dimension(110, 32)); 
        boton.setBackground(color);
        boton.setForeground(Color.WHITE);
        boton.setFont(FUENTE_TEXTO_NORMAL); 
        boton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(color.darker()),
            BorderFactory.createEmptyBorder(6, 12, 6, 12) 
        ));
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return boton;
    }

    private void generarReporte() {
        try {
            mainPanel.removeAll();
            
            List<Tarea> tareas = cargarTareas();
            List<Habito> habitos = cargarHabitos();
            LocalDate inicioSemana = LocalDate.now().with(DayOfWeek.MONDAY);
            LocalDate finSemana = LocalDate.now().with(DayOfWeek.SUNDAY);
            
            List<Tarea> tareasSemana = filtrarSemana(tareas, inicioSemana, finSemana);
            List<Habito> habitosSemana = filtrarSemana(habitos, inicioSemana, finSemana);
            
            agregarSeccion(crearEncabezado(), ESPACIO_VERTICAL_SECCIONES);
            agregarSeccion(crearResumen(tareasSemana, habitosSemana), ESPACIO_VERTICAL_SECCIONES);
            agregarSeccion(crearTareas(tareasSemana), ESPACIO_VERTICAL_SECCIONES);
            agregarSeccion(crearHabitos(habitosSemana), ESPACIO_VERTICAL_SECCIONES);
            agregarSeccion(crearRecomendaciones(tareasSemana, habitosSemana), 0);
            
            mainPanel.revalidate();
            mainPanel.repaint();
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void agregarSeccion(JPanel panel, int espacio) {
        mainPanel.add(panel);
        if (espacio > 0) mainPanel.add(Box.createVerticalStrut(espacio));
    }

    private JPanel crearEncabezado() {
        JPanel panel = crearPanelTarjeta();
        panel.setLayout(new BorderLayout());
        
        JLabel titulo = new JLabel("FECHA Y HORA GENERADA", SwingConstants.CENTER);
        titulo.setFont(FUENTE_TITULO_SECCION); 
        titulo.setForeground(COLOR_PRIMARIO);
        titulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0)); 
        
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate inicio = LocalDate.now().with(DayOfWeek.MONDAY);
        LocalDate fin = LocalDate.now().with(DayOfWeek.SUNDAY);
        
        JLabel periodo = new JLabel("PERÍODO: " + inicio.format(fmt) + " AL " + fin.format(fmt));
        periodo.setFont(FUENTE_SUBTITULO); 
        periodo.setForeground(COLOR_TEXTO);
        
        JLabel generado = new JLabel("GENERADO: " + LocalDate.now().format(fmt) + " A LAS " + LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm")));
        generado.setFont(FUENTE_TEXTO_NORMAL); 
        generado.setForeground(Color.GRAY);
        
        JPanel info = new JPanel(new GridLayout(2, 1, 2, 2)); 
        info.setOpaque(false);
        info.add(periodo);
        info.add(generado);
        
        panel.add(titulo, BorderLayout.NORTH);
        panel.add(info, BorderLayout.CENTER);
        return panel;
    }

    private JPanel crearResumen(List<Tarea> tareas, List<Habito> habitos) {
        JPanel panel = crearPanelTarjeta();
        panel.setLayout(new BorderLayout());
        
        JLabel titulo = new JLabel("RESUMEN DE PROGRESO", SwingConstants.CENTER);
        titulo.setFont(FUENTE_TITULO_SECCION); 
        titulo.setForeground(COLOR_TEXTO);
        titulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 12, 0));
        
        long tComp = contarCompletados(tareas);
        long hComp = contarCompletados(habitos);
        int totalT = tareas.size(), totalH = habitos.size();
        int porcT = totalT == 0 ? 0 : (int)(tComp * 100.0 / totalT);
        int porcH = totalH == 0 ? 0 : (int)(hComp * 100.0 / totalH);
        int porcG = (totalT + totalH) == 0 ? 0 : (int)((tComp + hComp) * 100.0 / (totalT + totalH));
        
        JPanel metricas = new JPanel(new GridLayout(1, 3, 6, 0)); 
        metricas.setOpaque(false);
        
        metricas.add(crearMetrica("TAREAS", tComp + "/" + totalT, porcT + "%", porcT));
        metricas.add(crearMetrica("HÁBITOS", hComp + "/" + totalH, porcH + "%", porcH));
        metricas.add(crearMetrica("GENERAL", (tComp + hComp) + "/" + (totalT + totalH), porcG + "%", porcG));
        
        panel.add(titulo, BorderLayout.NORTH);
        panel.add(metricas, BorderLayout.CENTER);
        return panel;
    }

    private JPanel crearMetrica(String titulo, String valor, String porcentaje, int porcNum) {
        Color color = porcNum > 70 ? COLOR_EXITO : porcNum > 30 ? COLOR_SECUNDARIO : COLOR_ALERTA;
        JPanel panel = new JPanel(new BorderLayout(2, 2)); 
        panel.setBackground(COLOR_TARJETA);
        panel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(color.brighter(), 1),
            BorderFactory.createEmptyBorder(10, 6, 10, 6) 
        ));
        
        JLabel lblTitulo = new JLabel(titulo, SwingConstants.CENTER);
        lblTitulo.setFont(FUENTE_TEXTO_NORMAL); 
        lblTitulo.setForeground(COLOR_TEXTO);
        
        JLabel lblValor = new JLabel(valor, SwingConstants.CENTER);
        lblValor.setFont(FUENTE_TEXTO_NORMAL); 
        lblValor.setForeground(color);
        
        JLabel lblPorc = new JLabel(porcentaje, SwingConstants.CENTER);
        lblPorc.setFont(FUENTE_METRICA_GRANDE);
        lblPorc.setForeground(color);
        
        JPanel contenido = new JPanel(new BorderLayout(1, 1));
        contenido.setOpaque(false);
        contenido.add(lblTitulo, BorderLayout.NORTH);
        contenido.add(lblValor, BorderLayout.CENTER);
        contenido.add(lblPorc, BorderLayout.SOUTH);
        
        panel.add(contenido, BorderLayout.CENTER);
        return panel;
    }

    private JPanel crearTareas(List<Tarea> tareas) {
        JPanel panel = crearPanelTarjeta();
        panel.setLayout(new BorderLayout());
        
        JLabel titulo = new JLabel("TAREAS POR PRIORIDAD", SwingConstants.CENTER);
        titulo.setFont(FUENTE_TITULO_SECCION); 
        titulo.setForeground(COLOR_TEXTO);
        titulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        
        if (tareas.isEmpty()) {
            panel.add(crearMensajeVacio("No hay tareas esta semana"), BorderLayout.CENTER);
        } else {
            JPanel contenido = new JPanel();
            contenido.setLayout(new BoxLayout(contenido, BoxLayout.Y_AXIS));
            contenido.setOpaque(false);
            contenido.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6)); 
            
            List<Tarea.Priority> ordenPrioridades = List.of(
                Tarea.Priority.ALTA, 
                Tarea.Priority.MEDIA, 
                Tarea.Priority.BAJA
            );
            
            for (Tarea.Priority prioridad : ordenPrioridades) {
                List<Tarea> tareasPrioridad = tareas.stream()
                    .filter(t -> t.getPrioridad() == prioridad)
                    .collect(Collectors.toList());
                
                if (!tareasPrioridad.isEmpty()) {
                    JLabel lblPri = new JLabel(prioridad.toString() + " (" + tareasPrioridad.size() + ")");
                    lblPri.setFont(FUENTE_SUBTITULO); 
                    lblPri.setForeground(obtenerColorPrioridad(prioridad));
                    lblPri.setBorder(BorderFactory.createEmptyBorder(6, 0, 4, 0)); 
                    contenido.add(lblPri);
                    
                    for (Tarea t : tareasPrioridad) {
                        contenido.add(crearItemTarea(t));
                        contenido.add(Box.createVerticalStrut(ESPACIO_ENTRE_ITEMS)); 
                    }
                    contenido.add(Box.createVerticalStrut(8)); 
                }
            }
            
            panel.add(new JScrollPane(contenido), BorderLayout.CENTER);
        }
        
        panel.add(titulo, BorderLayout.NORTH);
        return panel;
    }

    private JPanel crearHabitos(List<Habito> habitos) {
        JPanel panel = crearPanelTarjeta();
        panel.setLayout(new BorderLayout());
        
        JLabel titulo = new JLabel("HÁBITOS POR FRECUENCIA", SwingConstants.CENTER);
        titulo.setFont(FUENTE_TITULO_SECCION); 
        titulo.setForeground(COLOR_TEXTO);
        titulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        
        if (habitos.isEmpty()) {
            panel.add(crearMensajeVacio("No hay hábitos esta semana"), BorderLayout.CENTER);
        } else {
            JPanel contenido = new JPanel();
            contenido.setLayout(new BoxLayout(contenido, BoxLayout.Y_AXIS));
            contenido.setOpaque(false);
            contenido.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6)); 
            
    
            List<Habito.Frequency> ordenFrecuencias = List.of(
                Habito.Frequency.DIARIO,
                Habito.Frequency.SEMANAL, 
                Habito.Frequency.MENSUAL
            );
            
            for (Habito.Frequency frecuencia : ordenFrecuencias) {
                List<Habito> habitosFrecuencia = habitos.stream()
                    .filter(h -> h.getFrecuencia() == frecuencia)
                    .collect(Collectors.toList());
                
                if (!habitosFrecuencia.isEmpty()) {
                    JLabel lblFrec = new JLabel(frecuencia.toString() + " (" + habitosFrecuencia.size() + ")");
                    lblFrec.setFont(FUENTE_SUBTITULO); 
                    lblFrec.setForeground(COLOR_SECUNDARIO);
                    lblFrec.setBorder(BorderFactory.createEmptyBorder(6, 0, 4, 0)); 
                    contenido.add(lblFrec);
                    
                    for (Habito h : habitosFrecuencia) {
                        contenido.add(crearItemHabito(h));
                        contenido.add(Box.createVerticalStrut(ESPACIO_ENTRE_ITEMS)); 
                    }
                    contenido.add(Box.createVerticalStrut(8)); 
                }
            }
            
            panel.add(new JScrollPane(contenido), BorderLayout.CENTER);
        }
        
        panel.add(titulo, BorderLayout.NORTH);
        return panel;
    }

    private JPanel crearRecomendaciones(List<Tarea> tareas, List<Habito> habitos) {
        JPanel panel = crearPanelTarjeta();
        panel.setLayout(new BorderLayout());
        
        JLabel titulo = new JLabel("RECOMENDACIONES", SwingConstants.CENTER);
        titulo.setFont(FUENTE_TITULO_SECCION); 
        titulo.setForeground(COLOR_TEXTO);
        titulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));
        
        JPanel contenido = new JPanel();
        contenido.setLayout(new BoxLayout(contenido, BoxLayout.Y_AXIS));
        contenido.setOpaque(false);
        
        long tAltas = tareas.stream().filter(t -> t.getPrioridad() == Tarea.Priority.ALTA && t.getEstado() == Actividad.State.PENDIENTE).count();
        long tPend = contarPendientes(tareas);
        long hPend = contarPendientes(habitos);
        
        if (tAltas > 0) agregarRecomendacion(contenido, "Prioriza " + tAltas + " tareas ALTA prioridad");
        if (tPend > 3) agregarRecomendacion(contenido, "Divide " + tPend + " tareas en pasos pequeños");
        if (hPend > 0) agregarRecomendacion(contenido, "Completa " + hPend + " hábitos pendientes");
        if (tAltas == 0 && tPend == 0 && hPend == 0) agregarRecomendacion(contenido, "¡Excelente trabajo!");
        
        panel.add(titulo, BorderLayout.NORTH);
        panel.add(contenido, BorderLayout.CENTER);
        return panel;
    }

    private JPanel crearItemTarea(Tarea tarea) {
        JPanel panel = new JPanel(new BorderLayout(6, 2)); 
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(PADDING_ITEMS, 10, PADDING_ITEMS, 10)); 
        
        JLabel estado = new JLabel(tarea.getEstado() == Actividad.State.COMPLETADO ? "" : "");
        estado.setFont(FUENTE_TEXTO_NORMAL); 
        
        JLabel nombre = new JLabel(tarea.getNombre());
        nombre.setFont(FUENTE_SUBTITULO); 
        nombre.setForeground(tarea.getEstado() == Actividad.State.COMPLETADO ? Color.GRAY : COLOR_TEXTO);
        
        JLabel desc = new JLabel("<html><div style='width: 280px; font-size: 12px;'>" + tarea.getDescripcion() + "</div></html>");
        desc.setFont(FUENTE_TEXTO_PEQUEÑO); 
        desc.setForeground(Color.GRAY);
        
        JPanel info = new JPanel(new BorderLayout());
        info.setOpaque(false);
        info.add(nombre, BorderLayout.NORTH);
        info.add(desc, BorderLayout.CENTER);
        
        panel.add(estado, BorderLayout.WEST);
        panel.add(info, BorderLayout.CENTER);
        return panel;
    }

    private JPanel crearItemHabito(Habito habito) {
        JPanel panel = new JPanel(new BorderLayout(6, 0));
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(220, 220, 220), 1),
            BorderFactory.createEmptyBorder(8, 10, 8, 10) 
        ));
        
        JLabel estado = new JLabel(habito.getEstado() == Actividad.State.COMPLETADO ? "" : "");
        estado.setFont(FUENTE_TEXTO_NORMAL); 
        
        JLabel nombre = new JLabel(habito.getNombre());
        nombre.setFont(FUENTE_SUBTITULO); 
        nombre.setForeground(habito.getEstado() == Actividad.State.COMPLETADO ? Color.GRAY : COLOR_TEXTO);
        
        panel.add(estado, BorderLayout.WEST);
        panel.add(nombre, BorderLayout.CENTER);
        return panel;
    }

    private void agregarRecomendacion(JPanel panel, String texto) {
        JLabel lbl = new JLabel("• " + texto);
        lbl.setFont(FUENTE_TEXTO_NORMAL); 
        lbl.setForeground(COLOR_TEXTO);
        lbl.setBorder(BorderFactory.createEmptyBorder(3, 10, 3, 10)); 
        panel.add(lbl);
    }

    private JPanel crearMensajeVacio(String mensaje) {
        JLabel lbl = new JLabel(mensaje, SwingConstants.CENTER);
        lbl.setFont(FUENTE_TEXTO_NORMAL); 
        lbl.setForeground(Color.GRAY);
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(lbl, BorderLayout.CENTER);
        return panel;
    }

    private JPanel crearPanelTarjeta() {
        JPanel panel = new JPanel();
        panel.setBackground(COLOR_TARJETA);
        panel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(220, 220, 220), 1),
            BorderFactory.createEmptyBorder(PADDING_TARJETA, PADDING_TARJETA, PADDING_TARJETA, PADDING_TARJETA) 
        ));
        return panel;
    }

    
    private Color obtenerColorPrioridad(Tarea.Priority p) {
        return p == Tarea.Priority.ALTA ? COLOR_ALERTA : 
               p == Tarea.Priority.MEDIA ? new Color(243, 156, 18) : 
               new Color(39, 174, 96);
    }

    private long contarCompletados(List<? extends Actividad> lista) {
        return lista.stream().filter(a -> a.getEstado() == Actividad.State.COMPLETADO).count();
    }

    private long contarPendientes(List<? extends Actividad> lista) {
        return lista.stream().filter(a -> a.getEstado() == Actividad.State.PENDIENTE).count();
    }

    private <T extends Actividad> List<T> filtrarSemana(List<T> lista, LocalDate inicio, LocalDate fin) {
        return lista.stream()
            .filter(a -> a != null && a.getFecha() != null)
            .filter(a -> !a.getFecha().isBefore(inicio) && !a.getFecha().isAfter(fin))
            .collect(Collectors.toList());
    }

    
    private List<Tarea> cargarTareas() {
        return cargarCSV("src/main/java/com/proyecto/Datos/Tareas.csv", 7, datos ->
            new Tarea(Integer.parseInt(
                datos[0].trim()),
                datos[1].trim(),
                Actividad.State.valueOf(datos[4].trim()),
                LocalDate.parse(datos[6].trim()),
                LocalTime.parse(datos[5].trim()),
                datos[2].trim(),
                Tarea.Priority.valueOf(datos[3].trim())));
    }

    private List<Habito> cargarHabitos() {
        return cargarCSV("src/main/java/com/proyecto/Datos/Habitos.csv", 6, datos ->
            new Habito(Integer.parseInt(
                datos[0].trim()), 
                datos[1].trim(),
                Actividad.State.valueOf(datos[3].trim()),
                LocalDate.parse(datos[5].trim()),
                LocalTime.parse(datos[4].trim()),
                Habito.Frequency.valueOf(datos[2].trim())));
    }

    private <T> List<T> cargarCSV(String ruta, int campos, java.util.function.Function<String[], T> constructor) {
        List<T> items = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(ruta))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(",");
                if (datos.length >= campos && datos[0].trim().matches("\\d+")) {
                    items.add(constructor.apply(datos));
                }
            }
        } catch (Exception e) {
            System.err.println("Error cargando: " + e.getMessage());
        }
        return items;
    }

    private void exportarATXT() {
        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle("Guardar Reporte TXT");
        fc.setSelectedFile(new File("reporte_semanal.txt"));
        
        if (fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try (PrintWriter writer = new PrintWriter(new FileWriter(fc.getSelectedFile()))) {
                
                List<Tarea> tareas = cargarTareas();
                List<Habito> habitos = cargarHabitos();
                LocalDate inicioSemana = LocalDate.now().with(DayOfWeek.MONDAY);
                LocalDate finSemana = LocalDate.now().with(DayOfWeek.SUNDAY);
                
                List<Tarea> tareasSemana = filtrarSemana(tareas, inicioSemana, finSemana);
                List<Habito> habitosSemana = filtrarSemana(habitos, inicioSemana, finSemana);
                
                DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                
            
                writer.println("==========================================");
                writer.println("     REPORTE DE PRODUCTIVIDAD SEMANAL");
                writer.println("==========================================");
                writer.println("PERÍODO: " + inicioSemana.format(fmt) + " al " + finSemana.format(fmt));
                writer.println("GENERADO: " + LocalDate.now().format(fmt) + " a las " + 
                              LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm")));
                writer.println();
                
                writer.println("---------------------------------------");
                writer.println("RESUMEN DE PROGRESO");
                writer.println("----------------------------------------");
                long tComp = contarCompletados(tareasSemana);
                long hComp = contarCompletados(habitosSemana);
                int totalT = tareasSemana.size(), totalH = habitosSemana.size();
                int porcT = totalT == 0 ? 0 : (int)(tComp * 100.0 / totalT);
                int porcH = totalH == 0 ? 0 : (int)(hComp * 100.0 / totalH);
                
                writer.println("TAREAS: " + tComp + "/" + totalT + " (" + porcT + "%)");
                writer.println("HÁBITOS: " + hComp + "/" + totalH + " (" + porcH + "%)");
                writer.println("TOTAL: " + (tComp + hComp) + "/" + (totalT + totalH) + 
                              " (" + (totalT + totalH == 0 ? 0 : (int)((tComp + hComp) * 100.0 / (totalT + totalH))) + "%)");
                writer.println();
                
            
                writer.println("-------------------------------------");
                writer.println("       TAREAS POR PRIORIDAD          ");
                writer.println("-------------------------------------");
                if (tareasSemana.isEmpty()) {
                    writer.println("No hay tareas esta semana");
                } else {
                    for (Tarea.Priority prioridad : List.of(Tarea.Priority.ALTA, Tarea.Priority.MEDIA, Tarea.Priority.BAJA)) {
                        List<Tarea> tareasPrioridad = tareasSemana.stream()
                            .filter(t -> t.getPrioridad() == prioridad)
                            .collect(Collectors.toList());
                        
                        if (!tareasPrioridad.isEmpty()) {
                            writer.println(prioridad.toString() + " (" + tareasPrioridad.size() + "):");
                            for (Tarea t : tareasPrioridad) {
                                writer.println("  • " + t.getNombre() + 
                                             " - " + t.getEstado() + 
                                             " - " + t.getFecha().format(fmt) +
                                             (t.getDescripcion() != null && !t.getDescripcion().isEmpty() ? 
                                              " - " + t.getDescripcion() : ""));
                            }
                            writer.println();
                        }
                    }
                }
                
                writer.println("---------------------------------------");
                writer.println("       HÁBITOS POR FRECUENCIA          ");
                writer.println("---------------------------------------");
                if (habitosSemana.isEmpty()) {
                    writer.println("No hay hábitos esta semana");
                } else {
                    for (Habito.Frequency frecuencia : List.of(Habito.Frequency.DIARIO, Habito.Frequency.SEMANAL, Habito.Frequency.MENSUAL)) {
                        List<Habito> habitosFrecuencia = habitosSemana.stream()
                            .filter(h -> h.getFrecuencia() == frecuencia)
                            .collect(Collectors.toList());
                        
                        if (!habitosFrecuencia.isEmpty()) {
                            writer.println(frecuencia.toString() + " (" + habitosFrecuencia.size() + "):");
                            for (Habito h : habitosFrecuencia) {
                                writer.println("  • " + h.getNombre() + 
                                             " - " + h.getEstado() + 
                                             " - " + h.getFecha().format(fmt));
                            }
                            writer.println();
                        }
                    }
                }
                
                writer.println("--------------------------------------------------");
                writer.println("                RECOMENDACIONES                   ");
                writer.println("--------------------------------------------------");
                long tAltas = tareasSemana.stream().filter(t -> t.getPrioridad() == Tarea.Priority.ALTA && t.getEstado() == Actividad.State.PENDIENTE).count();
                long tPend = contarPendientes(tareasSemana);
                long hPend = contarPendientes(habitosSemana);
                
                if (tAltas > 0) writer.println("• Prioriza " + tAltas + " tareas ALTA prioridad");
                if (tPend > 3) writer.println("• Divide " + tPend + " tareas en pasos pequeños");
                if (hPend > 0) writer.println("• Completa " + hPend + " hábitos pendientes");
                if (tAltas == 0 && tPend == 0 && hPend == 0) writer.println("• ¡Excelente trabajo!");
                
                writer.println("==========================================");
                
                JOptionPane.showMessageDialog(this, "Reporte TXT exportado exitosamente!", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error al exportar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}