package main.java.com.proyecto.ui;

import main.java.com.proyecto.Gestor.Gestor;
import main.java.com.proyecto.Modelos.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CompleteWindow extends JPanel { // cambio de jdialog a jpanel 
    private Gestor gestor;
    private JPanel mainPanel; // el panel principal donde muestra todo 
    private List<Tarea> tareasActuales;
    private List<Habito> habitosActuales;
    
    // colores de la clase Colours
    private final Color COLOR_PRIMARIO = Colours.B_Blue;
    private final Color COLOR_FONDO = Colours.Cl_Fondo;
    private final Color COLOR_TARJETA = Colours.Cl_Tarjeta;
    private final Color COLOR_AMARILLO = new Color(241, 196, 15);
    private final Color COLOR_VERDE = Colours.B_Green;
    private final Color COLOR_ROJO = Colours.B_Red;

    // fuentes de la clase Colours
    private final Font FUENTE_TITULO_PRINCIPAL = Colours.B_Grande;
    private final Font FUENTE_TITULO_SECCION = Colours.Ft_Titulo;
    private final Font FUENTE_SUBTITULO = new Font("Segoe UI", Font.BOLD, 18);
    private final Font FUENTE_TEXTO_NORMAL = Colours.Ft_Label;
    private final Font FUENTE_TEXTO_PEQUENO = Colours.Ft_Info;
    private final Font FUENTE_TEXTO_MUY_PEQUENO = new Font("Segoe UI", Font.PLAIN, 13);
    
    public CompleteWindow(Gestor gestor) {  // cuando se crea la ventana organiza la info en su lugar metodo constructor 
        this.gestor = gestor;
        this.tareasActuales = new ArrayList<>();
        this.habitosActuales = new ArrayList<>();
        configurarPanel();
        inicializarComponentes();
        cargarDatosDesdeCSV();
        generarReporte();
    }

    private void configurarPanel() { 
        setLayout(new BorderLayout());
        setBackground(COLOR_FONDO);
    }

    // contruye la parte con el titulo y botones 
    private void inicializarComponentes() {
        // 1. PANEL SUPERIOR AZUL (solo título) - SE MANTIENE IGUAL
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(COLOR_PRIMARIO);
        topPanel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));
        
        JLabel titulo = new JLabel("GESTOR DE TAREAS Y HÁBITOS", SwingConstants.CENTER);
        titulo.setFont(FUENTE_TITULO_PRINCIPAL);
        titulo.setForeground(Color.WHITE);
        topPanel.add(titulo, BorderLayout.CENTER);
        
        add(topPanel, BorderLayout.NORTH);

        // 2. PANEL PRINCIPAL CON SCROLL
        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(COLOR_FONDO);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(0, 25, 15, 25));
        
        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(20);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        add(scrollPane, BorderLayout.CENTER);
    }

    // CREAR BOTONES 
    private JButton crearBoton(String texto, Font fuente, Dimension tamaño, Color colorFondo, java.awt.event.ActionListener action) {
        JButton boton = new JButton(texto);
        boton.setFont(fuente);
        boton.setPreferredSize(tamaño);
        if (colorFondo != null) {
            boton.setBackground(colorFondo);
            
            if (colorFondo == COLOR_AMARILLO){
                    boton.setForeground(Color.BLACK); 
            } else {
                    boton.setForeground(Color.WHITE); 
            }
        }
        boton.addActionListener(action);
        return boton;
    }

    private void cargarDatosDesdeCSV() {
        // Ahora usa el Gestor como fuente principal de datos
        cargarDatosDesdeGestor();
    }

    private void cargarDatosDesdeGestor() {
        this.tareasActuales = gestor.obtenerTareasOrdenadas();
        this.habitosActuales = cargarHabitosOrdenados();
    }

    // actuliza y coloca la info actual en la pantalla 
    private void generarReporte() {
        mainPanel.removeAll();
        mainPanel.add(Box.createVerticalStrut(5));
        
        // AGREGAR BOTONES PRINCIPALES CENTRADOS
        mainPanel.add(crearPanelBotonesPrincipales());
        
        mainPanel.add(crearFraseMotivadora());

        mainPanel.add(crearEncabezado());
        mainPanel.add(Box.createVerticalStrut(5));
        mainPanel.add(crearPendientesHoy(tareasActuales, habitosActuales));
        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(crearCompletados(tareasActuales, habitosActuales));
        mainPanel.add(Box.createVerticalStrut(15));
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    // NUEVO MÉTODO: Crear panel de botones principales centrados
    private JPanel crearPanelBotonesPrincipales() {
        JPanel botonesPanel = new JPanel();
        botonesPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 15));
        botonesPanel.setBackground(COLOR_FONDO);
        botonesPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 10, 0));
        
        // Botones principales (los que estaban en el Main)
        JButton btnNuevaTarea = crearBoton("Nueva Tarea", FUENTE_TEXTO_NORMAL, 
            new Dimension(140, 40), Colours.B_Blue, e -> abrirNuevaTarea());
        
        JButton btnNuevoHabito = crearBoton("Nuevo Hábito", FUENTE_TEXTO_NORMAL, 
            new Dimension(140, 40), Colours.B_Blue, e -> abrirNuevoHabito());
        
        JButton btnReporte = crearBoton("Reporte Semanal", FUENTE_TEXTO_NORMAL, 
            new Dimension(150, 40), Colours.B_Purple, e -> abrirReporteSemanal());
            
        JButton btnEditar = crearBoton("Editar Actividad", FUENTE_TEXTO_NORMAL, 
            new Dimension(150, 40), Colours.B_Green, e -> abrirEditarActividad());
        
        JButton btnEliminar = crearBoton("Eliminar Actividad", FUENTE_TEXTO_NORMAL, 
            new Dimension(150, 40), Colours.B_Red, e -> abrirEliminarActividad());

        // Agregar botones al panel
        botonesPanel.add(btnNuevaTarea);
        botonesPanel.add(btnNuevoHabito);
        botonesPanel.add(btnReporte);
        botonesPanel.add(btnEditar);
        botonesPanel.add(btnEliminar);

        return botonesPanel;
    }

/**
 * Crea un panel con una frase motivadora.
 */
    private JPanel crearFraseMotivadora() {
        JPanel frasePanel = new JPanel();
        frasePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0)); 
        frasePanel.setBackground(COLOR_FONDO); 
        frasePanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0)); 

        JLabel fraseLabel = new JLabel("<html><center>\"La disciplina es el puente entre metas y logros.\"</center></html>", SwingConstants.CENTER);
    
         // Usando fuentes y colores definidos para consistencia
        fraseLabel.setFont(FUENTE_TEXTO_NORMAL.deriveFont(Font.ITALIC, 16)); 
        fraseLabel.setForeground(COLOR_PRIMARIO.darker()); 

        frasePanel.add(fraseLabel);
        return frasePanel;
}

    private void mostrarSoloPendientes() {
        mainPanel.removeAll();
        mainPanel.add(Box.createVerticalStrut(15));
        
        // AGREGAR BOTONES PRINCIPALES CENTRADOS
        mainPanel.add(crearPanelBotonesPrincipales());
        mainPanel.add(Box.createVerticalStrut(20));
        
        mainPanel.add(crearEncabezado());
        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(crearPendientesHoy(tareasActuales, habitosActuales));
        mainPanel.add(Box.createVerticalStrut(15));
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    // MÉTODOS PARA ABRIR LAS VENTANAS
    private void abrirNuevaTarea() {
        TaskWindow dialog = new TaskWindow((JFrame) SwingUtilities.getWindowAncestor(this), gestor);
        dialog.setVisible(true);
        // Actualizar después de cerrar la ventana
        cargarDatosDesdeCSV();
        generarReporte();
    }

    private void abrirNuevoHabito() {
        HabitWindow dialog = new HabitWindow((JFrame) SwingUtilities.getWindowAncestor(this), gestor);
        dialog.setVisible(true);
        // Actualizar después de cerrar la ventana
        cargarDatosDesdeCSV();
        generarReporte();
    }

    private void abrirReporteSemanal() {
        ReportWindow dialog = new ReportWindow((JFrame) SwingUtilities.getWindowAncestor(this), gestor);
        dialog.setVisible(true);
    }

    private void abrirFiltrarPrioridad() {
        // Usar el mismo código que tienes en Main para filtrar por prioridad
        java.util.List<main.java.com.proyecto.Modelos.Tarea> tareas = gestor.obtenerTareasOrdenadas();

        java.util.List<main.java.com.proyecto.Modelos.Tarea> alta = new java.util.ArrayList<>();
        java.util.List<main.java.com.proyecto.Modelos.Tarea> media = new java.util.ArrayList<>();
        java.util.List<main.java.com.proyecto.Modelos.Tarea> baja = new java.util.ArrayList<>();

        for (main.java.com.proyecto.Modelos.Tarea t : tareas) {
            switch (t.getPrioridad()) {
                case ALTA:
                    alta.add(t);
                    break;
                case MEDIA:
                    media.add(t);
                    break;
                case BAJA:
                default:
                    baja.add(t);
                    break;
            }
        }

        String[] columns = {"ID", "Nombre", "Descripción", "Fecha", "Hora", "Estado"};

        javax.swing.table.DefaultTableModel modelAlta = new javax.swing.table.DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        javax.swing.table.DefaultTableModel modelMedia = new javax.swing.table.DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        javax.swing.table.DefaultTableModel modelBaja = new javax.swing.table.DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };

        java.time.format.DateTimeFormatter df = java.time.format.DateTimeFormatter.ISO_LOCAL_DATE;
        java.time.format.DateTimeFormatter tf = java.time.format.DateTimeFormatter.ofPattern("HH:mm");

        for (main.java.com.proyecto.Modelos.Tarea t : alta) {
            modelAlta.addRow(new Object[]{t.getId(), t.getNombre(), t.getDescripcion(), t.getFecha().format(df), t.getHora().format(tf), t.getEstado()});
        }
        for (main.java.com.proyecto.Modelos.Tarea t : media) {
            modelMedia.addRow(new Object[]{t.getId(), t.getNombre(), t.getDescripcion(), t.getFecha().format(df), t.getHora().format(tf), t.getEstado()});
        }
        for (main.java.com.proyecto.Modelos.Tarea t : baja) {
            modelBaja.addRow(new Object[]{t.getId(), t.getNombre(), t.getDescripcion(), t.getFecha().format(df), t.getHora().format(tf), t.getEstado()});
        }

        JTable tableAlta = new JTable(modelAlta);
        JTable tableMedia = new JTable(modelMedia);
        JTable tableBaja = new JTable(modelBaja);

        tableAlta.setFillsViewportHeight(true);
        tableMedia.setFillsViewportHeight(true);
        tableBaja.setFillsViewportHeight(true);

        tableAlta.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        tableMedia.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        tableBaja.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

    // Style tables to match app look: fonts, header, row colors per priority
    // Use BLACK text for Alta and Baja rows for better readability on light tints
    styleTaskTable(tableAlta, Colours.B_Red, new Color(255, 220, 220), Color.BLACK, Color.WHITE);
    styleTaskTable(tableMedia, COLOR_AMARILLO, new Color(255, 245, 210), Color.BLACK, Color.BLACK);
    styleTaskTable(tableBaja, Colours.B_Green, new Color(220, 255, 220), Color.BLACK, Color.WHITE);

    // Enable sorting by clicking columns
    tableAlta.setRowSorter(new javax.swing.table.TableRowSorter<>(modelAlta));
    tableMedia.setRowSorter(new javax.swing.table.TableRowSorter<>(modelMedia));
    tableBaja.setRowSorter(new javax.swing.table.TableRowSorter<>(modelBaja));

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("ALTA (" + alta.size() + ")", new JScrollPane(tableAlta));
        tabs.addTab("MEDIA (" + media.size() + ")", new JScrollPane(tableMedia));
        tabs.addTab("BAJA (" + baja.size() + ")", new JScrollPane(tableBaja));

        JDialog dlg = new JDialog((JFrame) SwingUtilities.getWindowAncestor(this), "Tareas - Tabla por Prioridad", true);
        dlg.getContentPane().setLayout(new BorderLayout(8,8));
        dlg.getContentPane().add(tabs, BorderLayout.CENTER);
        dlg.setSize(900, 600);
        dlg.setLocationRelativeTo(this);
        dlg.setVisible(true);
    }

    private void abrirEditarActividad() {
        EditWindow dialog = new EditWindow((JFrame) SwingUtilities.getWindowAncestor(this), gestor);
        dialog.setVisible(true);
        // Actualizar después de cerrar la ventana
        cargarDatosDesdeCSV();
        generarReporte();
    }

    private void abrirEliminarActividad() {
        DeleteWindow dialog = new DeleteWindow((JFrame) SwingUtilities.getWindowAncestor(this), gestor);
        dialog.setVisible(true);
        // Actualizar después de cerrar la ventana
        cargarDatosDesdeCSV();
        generarReporte();
    }

    //crea el encabeado o titulo con completar actividades y la fecha actual 
    private JPanel crearEncabezado() {
       JPanel panel = crearPanelTarjeta();
        panel.setLayout(new BorderLayout(25, 0));
        panel.setBorder(BorderFactory.createCompoundBorder(
        new LineBorder(COLOR_PRIMARIO, 2), 
        BorderFactory.createEmptyBorder(25, 30, 25, 30)
    ));

        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBackground(COLOR_TARJETA);
        
        JLabel titulo = new JLabel("COMPLETAR ACTIVIDADES");
        titulo.setFont(FUENTE_TITULO_SECCION.deriveFont(Font.BOLD,18));
        titulo.setForeground(COLOR_PRIMARIO);
        titulo.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("EEEE, dd 'de' MMMM 'de' yyyy");
        String fechaCompleta = LocalDate.now().format(fmt);
        JLabel fecha = new JLabel(fechaCompleta);
        fecha.setFont(new Font("Segoe IU", Font.PLAIN, 15)); 
        fecha.setForeground(new Color(80, 80, 80));
        fecha.setAlignmentX(Component.LEFT_ALIGNMENT);

        // PRÓXIMA ACTIVIDAD MÁS IMPORTANTE
        String proximaActividad = obtenerProximaActividadCritica();
        JLabel proxima = new JLabel("Proxima: " + proximaActividad);
        proxima.setFont(new Font("Segoe UI", Font.BOLD, 13));
        proxima.setForeground(Colours.B_Red);
        proxima.setAlignmentX(Component.LEFT_ALIGNMENT);

        leftPanel.add(titulo);
        leftPanel.add(Box.createVerticalStrut(8));
        leftPanel.add(fecha);
        leftPanel.add(Box.createVerticalStrut(8));
        leftPanel.add(proxima);

        // LADO DERECHO: Tiempo, urgencia y ubicación
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBackground(COLOR_TARJETA);
        rightPanel.setAlignmentX(Component.RIGHT_ALIGNMENT);

        // Reloj principal
        JLabel reloj = new JLabel();
        reloj.setFont(new Font("Segoe UI", Font.BOLD, 33));
        reloj.setForeground(Colours.B_Blue);
        reloj.setAlignmentX(Component.RIGHT_ALIGNMENT);

        Timer timer = new Timer(1000, e -> {
        DateTimeFormatter horaFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        reloj.setText(LocalTime.now().format(horaFormatter));
        });
        timer.start();
        
        // Tiempo restante del día
        LocalTime ahora = LocalTime.now();
        LocalTime finDia = LocalTime.of(23, 59);
        long minutosRestantes = java.time.Duration.between(ahora, finDia).toMinutes();
        long horas = minutosRestantes / 60;
        long minutos = minutosRestantes % 60;

        JLabel tiempoRestante = new JLabel(String.format("%dh %02dm restantes", horas, minutos));
        tiempoRestante.setFont(new Font("Segoe UI", Font.BOLD, 13));
        tiempoRestante.setForeground(minutosRestantes < 360 ? Colours.B_Red : new Color(60, 60, 60));
        tiempoRestante.setAlignmentX(Component.RIGHT_ALIGNMENT);

        // Día de la semana
        String diaSemana = LocalDate.now().getDayOfWeek().getDisplayName(
        java.time.format.TextStyle.FULL, 
        new java.util.Locale("es")
        ).toUpperCase();

        JLabel diaLabel = new JLabel(diaSemana);
        diaLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        diaLabel.setForeground(new Color(120, 120, 120));
        diaLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);

        // Hora local de Guatemala
        JLabel zonaLabel = new JLabel("Totonicapán, Guatemala , C.A");
        zonaLabel.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        zonaLabel.setForeground(new Color(150, 150, 150));
        zonaLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);

        rightPanel.add(reloj);
        rightPanel.add(Box.createVerticalStrut(5));
        rightPanel.add(tiempoRestante);
        rightPanel.add(Box.createVerticalStrut(5));
        rightPanel.add(diaLabel);
        rightPanel.add(Box.createVerticalStrut(3));
        rightPanel.add(zonaLabel);
    
        panel.add(leftPanel, BorderLayout.CENTER);
        panel.add(rightPanel, BorderLayout.EAST);

        return panel;
    }
    
    // listar pendientes logica (Dina)

    private JPanel crearPendientesHoy(List<Tarea> tareas, List<Habito> habitos) {
        JPanel panel = crearPanelTarjeta();
        panel.setLayout(new BorderLayout());
        
        // PANEL SUPERIOR con título y botones de acción
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(COLOR_TARJETA);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        
        JLabel titulo = new JLabel("PENDIENTES PARA HOY", SwingConstants.LEFT);
        titulo.setFont(FUENTE_TITULO_SECCION);
        titulo.setForeground(COLOR_ROJO);
        
        // Panel de botones de acción (derecha)
        JPanel botonesAccionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        botonesAccionPanel.setBackground(COLOR_TARJETA);
        
        JButton btnActualizar = crearBoton("Actualizar", FUENTE_TEXTO_PEQUENO, 
            new Dimension(100, 30), Colours.Cl_Buscar, 
            e -> { cargarDatosDesdeCSV(); generarReporte(); JOptionPane.showMessageDialog(this, "Datos actualizados"); });
        
        JButton btnSoloPendientes = crearBoton("Solo Pendientes", FUENTE_TEXTO_PEQUENO, 
            new Dimension(130, 30), COLOR_AMARILLO, e -> mostrarSoloPendientes());

        // añadimos el boton dentro del espacio de marcar tareas y habitos para filtrar por prioridad: 

        JButton btnFiltrarPrioridad = crearBoton("Filtrar Prioridad", FUENTE_TEXTO_PEQUENO, 
        new Dimension(130, 30), Colours.B_Green, e -> abrirFiltrarPrioridad());
        
        botonesAccionPanel.add(btnActualizar);
        botonesAccionPanel.add(btnSoloPendientes);
        botonesAccionPanel.add(btnFiltrarPrioridad); // igual aca 

        headerPanel.add(titulo, BorderLayout.WEST);
        headerPanel.add(botonesAccionPanel, BorderLayout.EAST);
        
        panel.add(headerPanel, BorderLayout.NORTH);
        
        JPanel contenido = new JPanel(); 
        contenido.setLayout(new GridLayout(1, 2, 20, 0)); 
        contenido.setBackground(COLOR_TARJETA);
        contenido.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        contenido.add(crearColumnaHabitos(habitos)); 
        contenido.add(crearColumnaTareas(tareas));  
        panel.add(contenido, BorderLayout.CENTER);
        return panel;
    }

    // listar tareas pendientes 

    private JPanel crearColumnaTareas(List<Tarea> tareas) {
        JPanel columna = new JPanel();
        columna.setLayout(new BoxLayout(columna, BoxLayout.Y_AXIS));
        
        TitledBorder border = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Colours.B_Blue, 2), "TAREAS PENDIENTES");
        border.setTitleFont(FUENTE_SUBTITULO);
        border.setTitleColor(Colours.B_Blue);
        border.setTitleJustification(TitledBorder.CENTER);
        columna.setBorder(border);
        columna.setBackground(Color.WHITE);

        // filtrar y ordenar tareas pendientes SOLO por fecha y hora
        List<Tarea> pendientes = tareas.stream()
            .filter(t -> t.getEstado() == Actividad.State.PENDIENTE)
            .sorted((t1, t2) -> {
                // Primero compara Fecha ascendente: más urgente primero
                int fechaCompare = t1.getFecha().compareTo(t2.getFecha());
                if (fechaCompare != 0) {
                    return fechaCompare;
                }
                // Si fecha es igual compara Hora ascendente: más urgente primero
                return t1.getHora().compareTo(t2.getHora());
            })
            .collect(Collectors.toList());

            // mostrar total pendiente 
        JPanel statsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 8));
        statsPanel.setBackground(Color.WHITE);
        
        long alta = pendientes.stream().filter(t -> t.getPrioridad() == Tarea.Priority.ALTA).count();
        long media = pendientes.stream().filter(t -> t.getPrioridad() == Tarea.Priority.MEDIA).count();
        long baja = pendientes.stream().filter(t -> t.getPrioridad() == Tarea.Priority.BAJA).count();
        String stats = String.format("Total: %d (Alta: %d, Media: %d, Baja: %d)", 
                                     pendientes.size(), alta, media, baja);
        JLabel statsLabel = new JLabel(stats);
       
        statsLabel.setFont(FUENTE_TEXTO_NORMAL);
        statsLabel.setForeground(Colours.Cl_Texto);
        statsPanel.add(statsLabel);
        columna.add(statsPanel);
        columna.add(Box.createVerticalStrut(8));
        
       // listar tareas pendientes.
        for (Tarea tarea : pendientes) {
            columna.add(crearTarjetaTarea(tarea));
            columna.add(Box.createVerticalStrut(12)); 
        }
        
        if (pendientes.isEmpty()) {
            columna.add(crearMensajeVacio("No hay tareas pendientes"));
        }
        return columna;
    }

    // listar habitos pendientes 

    private JPanel crearColumnaHabitos(List<Habito> habitos) {
        JPanel columna = new JPanel();
        columna.setLayout(new BoxLayout(columna, BoxLayout.Y_AXIS));
        
        TitledBorder border = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Colours.B_Green, 2), "HÁBITOS PENDIENTES");
        border.setTitleFont(FUENTE_SUBTITULO);
        border.setTitleColor(Colours.B_Green);
        border.setTitleJustification(TitledBorder.CENTER);
        columna.setBorder(border);
        columna.setBackground(Color.WHITE);
        
        // filtro para habitos pendientes 
        List<Habito> pendientes = habitos.stream()
            .filter(h -> h.getEstado() == Actividad.State.PENDIENTE)
            // ordenar por fecha y hora 
            .sorted((h1, h2) -> {
                // Compara Fecha ascendente más urgente primero
                int fechaCompare = h1.getFecha().compareTo(h2.getFecha());
                if (fechaCompare != 0) return fechaCompare;
                
                // Si fecha es igual, compara Hora ascendente: más urgente primero
                return h1.getHora().compareTo(h2.getHora());
            })
            .collect(Collectors.toList());
        
        JPanel statsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 8));
        statsPanel.setBackground(Color.WHITE);
    
        JLabel statsLabel = new JLabel(String.format("Total pendientes: %d", pendientes.size()));
        statsLabel.setFont(FUENTE_TEXTO_NORMAL);
        statsLabel.setForeground(Colours.Cl_Texto);
        statsPanel.add(statsLabel);
        columna.add(statsPanel);
        
        for (Habito habito : pendientes) {
            columna.add(crearTarjetaHabito(habito));
            columna.add(Box.createVerticalStrut(12)); 
        }
        
        if (pendientes.isEmpty()) {
            columna.add(crearMensajeVacio("No hay hábitos pendientes"));
        }
        
        return columna;
    }

    // hasta aqui ------------------------------------------------------------------ (Dina)

    // crear los titulos de actidades y las divide en 2 partes. 
    private JPanel crearCompletados(List<Tarea> tareas, List<Habito> habitos) {
        JPanel panel = crearPanelTarjeta();
        panel.setLayout(new BorderLayout());
        
        JLabel titulo = new JLabel(" ACTIVIDADES COMPLETADAS", SwingConstants.CENTER);
        titulo.setFont(FUENTE_TITULO_SECCION);
        titulo.setForeground(COLOR_VERDE);
        titulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        panel.add(titulo, BorderLayout.NORTH);
        
        JPanel contenido = new JPanel(new GridLayout(1, 2, 20, 0));
        contenido.setBackground(COLOR_TARJETA);
        contenido.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        contenido.add(crearColumnaTareasCompletadas(tareas));
        contenido.add(crearColumnaHabitosCompletados(habitos));
        panel.add(contenido, BorderLayout.CENTER);
        return panel;
    }

    // crear el diseño de ckeck filtrar tareas completadas y ordenar 

    private JPanel crearColumnaTareasCompletadas(List<Tarea> tareas) {
        JPanel columna = new JPanel();
        columna.setLayout(new BoxLayout(columna, BoxLayout.Y_AXIS));
        TitledBorder border = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(COLOR_VERDE, 2), " TAREAS COMPLETADAS");
        border.setTitleFont(FUENTE_SUBTITULO);
        border.setTitleColor(COLOR_VERDE);
        border.setTitleJustification(TitledBorder.CENTER);
        columna.setBorder(border);
        columna.setBackground(Color.WHITE);

        List<Tarea> completadas = tareas.stream()
            .filter(t -> t.getEstado() == Actividad.State.COMPLETADO)
            .sorted((t1, t2) -> {
                // Ordenar por fecha y hora descendente (más reciente primero)
                int fechaCompare = t2.getFecha().compareTo(t1.getFecha());
                if (fechaCompare != 0) return fechaCompare;
                return t2.getHora().compareTo(t1.getHora());
            })
            .collect(Collectors.toList());
        
        JPanel statsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 8));
        statsPanel.setBackground(Color.WHITE);
        
        JLabel statsLabel = new JLabel(String.format("Total completadas: %d", completadas.size()));
        statsLabel.setFont(FUENTE_TEXTO_NORMAL);
        statsLabel.setForeground(Colours.Cl_Texto);
        statsPanel.add(statsLabel);
        
        columna.add(statsPanel);
        columna.add(Box.createVerticalStrut(10));
        
        for (Tarea tarea : completadas) {
            columna.add(crearTarjetaCompletadaTarea(tarea));
            columna.add(Box.createVerticalStrut(12)); 
        }
        
        if (completadas.isEmpty()) {
            columna.add(crearMensajeVacio("No hay tareas completadas"));
        }
        
        return columna;
    }

    // crea el verde de habitos completados y lo filtra por fecha 
    private JPanel crearColumnaHabitosCompletados(List<Habito> habitos) {
        JPanel columna = new JPanel();
        columna.setLayout(new BoxLayout(columna, BoxLayout.Y_AXIS));
        
        TitledBorder border = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(COLOR_VERDE, 2), "HÁBITOS COMPLETADOS");
        border.setTitleFont(FUENTE_SUBTITULO);
        border.setTitleColor(COLOR_VERDE);
        border.setTitleJustification(TitledBorder.CENTER);
        columna.setBorder(border);
        columna.setBackground(Color.WHITE);
        
        List<Habito> completados = habitos.stream()
            .filter(h -> h.getEstado() == Actividad.State.COMPLETADO)
            .sorted((h1, h2) -> {
                // Ordenar por fecha y hora descendente (más reciente primero)
                int fechaCompare = h2.getFecha().compareTo(h1.getFecha());
                if (fechaCompare != 0) return fechaCompare;
                return h2.getHora().compareTo(h1.getHora());
            })
            .collect(Collectors.toList());
        
        JPanel statsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 8)); // Era '2'
        statsPanel.setBackground(Color.WHITE);
        
        JLabel statsLabel = new JLabel(String.format("Total completados: %d", completados.size()));
        statsLabel.setFont(FUENTE_TEXTO_NORMAL);
        statsLabel.setForeground(Colours.Cl_Texto);
        statsPanel.add(statsLabel);
        
        columna.add(statsPanel);
        
        for (Habito habito : completados) {
            columna.add(crearTarjetaCompletadaHabito(habito));
            columna.add(Box.createVerticalStrut(12)); 
        }
        
        if (completados.isEmpty()) {
            columna.add(crearMensajeVacio("No hay hábitos completados"));
        }
        return columna;
    }

    // crea una tarjeta individual para cada tarea pendiente
    private JPanel crearTarjetaCompletadaTarea(Tarea tarea) {
        return crearTarjetaBase(tarea.getNombre(), tarea.getDescripcion(), tarea.getFecha(), tarea.getHora(), COLOR_VERDE, true, tarea);
    }

    private JPanel crearTarjetaCompletadaHabito(Habito habito) {
        return crearTarjetaBase(habito.getNombre(), "", habito.getFecha(), habito.getHora(), COLOR_VERDE, false, habito);
    }
    
    private JPanel crearTarjetaTarea(Tarea tarea) {
        JPanel tarjeta = new JPanel(new BorderLayout());

        tarjeta.setBorder(BorderFactory.createCompoundBorder(new LineBorder(Color.LIGHT_GRAY, 1), BorderFactory.createEmptyBorder(8, 15, 8, 15)));
        tarjeta.setBackground(Color.WHITE);

        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBackground(Color.WHITE);
        
        JPanel nombrePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        nombrePanel.setBackground(Color.WHITE);
        
        JLabel idLabel = new JLabel("ID: " + tarea.getId() + " • ");
        idLabel.setFont(FUENTE_TEXTO_MUY_PEQUENO);
        idLabel.setForeground(new Color(100, 100, 100));
        
        JLabel nombreLabel = new JLabel(tarea.getNombre());
        nombreLabel.setFont(FUENTE_TEXTO_NORMAL.deriveFont(Font.BOLD));
        nombreLabel.setForeground(Colours.Cl_Texto);
        
        nombrePanel.add(idLabel);
        nombrePanel.add(nombreLabel);
        
        JLabel descripcionLabel = new JLabel(tarea.getDescripcion());
        descripcionLabel.setFont(FUENTE_TEXTO_PEQUENO);
        descripcionLabel.setForeground(new Color(80, 80, 80));

        descripcionLabel.setBorder(BorderFactory.createEmptyBorder(2, 0, 0, 0));
        
        leftPanel.add(nombrePanel, BorderLayout.NORTH);
        leftPanel.add(descripcionLabel, BorderLayout.CENTER);
        
        JPanel rightPanel = crearPanelDerecho(tarea.getFecha(), tarea.getHora(), tarea.getPrioridad().toString(), getColorPrioridad(tarea.getPrioridad()));
        
        JPanel contentPanel = new JPanel(new BorderLayout(12, 0));
        contentPanel.setBackground(Color.WHITE);
        contentPanel.add(leftPanel, BorderLayout.CENTER);
        
        JPanel rightWrapper = new JPanel(new BorderLayout());
        rightWrapper.setOpaque(false); 
        rightWrapper.add(rightPanel, BorderLayout.NORTH); 
        contentPanel.add(rightWrapper, BorderLayout.EAST);
        
        JCheckBox check = new JCheckBox();
        check.setSelected(tarea.getEstado() == Actividad.State.COMPLETADO);
        check.addActionListener(e -> marcarTareaCompleta(tarea, check.isSelected()));
        
        tarjeta.add(check, BorderLayout.WEST);
        tarjeta.add(contentPanel, BorderLayout.CENTER);
        
        check.setOpaque(false);
        contentPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                check.setSelected(!check.isSelected());
                marcarTareaCompleta(tarea, check.isSelected());
            }
        });
        
        return tarjeta;
    }

    // crea tarjetas individuales para cada habito pendite

    private JPanel crearTarjetaHabito(Habito habito) {
        JPanel tarjeta = new JPanel(new BorderLayout());
        
        tarjeta.setBorder(BorderFactory.createCompoundBorder(new LineBorder(Color.LIGHT_GRAY, 1), BorderFactory.createEmptyBorder(8, 15, 8, 15)));
        tarjeta.setBackground(Color.WHITE);
        
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBackground(Color.WHITE);
        
        JPanel nombrePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        nombrePanel.setBackground(Color.WHITE);
        
        JLabel idLabel = new JLabel("ID: " + habito.getId() + " • ");
        idLabel.setFont(FUENTE_TEXTO_MUY_PEQUENO);
        idLabel.setForeground(new Color(100, 100, 100));
        
        JLabel nombreLabel = new JLabel(habito.getNombre());
        nombreLabel.setFont(FUENTE_TEXTO_NORMAL.deriveFont(Font.BOLD));
        nombreLabel.setForeground(Colours.Cl_Texto);
        
        nombrePanel.add(idLabel);
        nombrePanel.add(nombreLabel);
        leftPanel.add(nombrePanel, BorderLayout.NORTH);
        
        JPanel rightPanel = crearPanelDerecho(habito.getFecha(), habito.getHora(), habito.getFrecuencia().toString(), Colours.B_Purple);
        
        JPanel contentPanel = new JPanel(new BorderLayout(12, 0));
        contentPanel.setBackground(Color.WHITE);
        contentPanel.add(leftPanel, BorderLayout.CENTER);
        
        JPanel rightWrapper = new JPanel(new BorderLayout());
        rightWrapper.setOpaque(false);
        rightWrapper.add(rightPanel, BorderLayout.NORTH);
        contentPanel.add(rightWrapper, BorderLayout.EAST);
        
        JCheckBox check = new JCheckBox();
        check.setSelected(habito.getEstado() == Actividad.State.COMPLETADO);
        check.addActionListener(e -> marcarHabitoCompleto(habito, check.isSelected()));
        
        tarjeta.add(check, BorderLayout.WEST);
        tarjeta.add(contentPanel, BorderLayout.CENTER);
        
        check.setOpaque(false);
        contentPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                check.setSelected(!check.isSelected());
                marcarHabitoCompleto(habito, check.isSelected());
            }
        });
        
        return tarjeta;
    }

    // como fabrica para crear tarjetas reutilizables si si crean nuevas actividades

    private JPanel crearTarjetaBase(String nombre, String descripcion, LocalDate fecha, LocalTime hora, Color color, boolean tieneDescripcion, Object actividad) {
        JPanel tarjeta = new JPanel(new BorderLayout());
        
        tarjeta.setBorder(BorderFactory.createCompoundBorder(new LineBorder(new Color(220, 220, 220), 1), BorderFactory.createEmptyBorder(8, 15, 8, 15)));
        tarjeta.setBackground(Color.WHITE);
        
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBackground(Color.WHITE);
        
        JPanel nombrePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        nombrePanel.setBackground(Color.WHITE);
        
        JLabel nombreLabel = new JLabel(nombre);
        nombreLabel.setFont(FUENTE_TEXTO_NORMAL.deriveFont(Font.BOLD));
        nombreLabel.setForeground(color);
        nombrePanel.add(nombreLabel);
        
        if (tieneDescripcion && !descripcion.isEmpty()) {
            JLabel descripcionLabel = new JLabel(descripcion);
            descripcionLabel.setFont(FUENTE_TEXTO_PEQUENO);
            descripcionLabel.setForeground(new Color(100, 100, 100));
            leftPanel.add(nombrePanel, BorderLayout.NORTH);
            leftPanel.add(descripcionLabel, BorderLayout.CENTER);
        } else {
            leftPanel.add(nombrePanel, BorderLayout.CENTER);
        }
        
        JPanel rightPanel = new JPanel(new GridLayout(2, 1, 4, 4));
        rightPanel.setBackground(Color.WHITE);
        
        DateTimeFormatter fechaFormatter = DateTimeFormatter.ofPattern("dd/MM/yy");
        DateTimeFormatter horaFormatter = DateTimeFormatter.ofPattern("HH:mm");
        
        JLabel fechaLabel = new JLabel(fecha.format(fechaFormatter), SwingConstants.RIGHT);
        fechaLabel.setFont(FUENTE_TEXTO_PEQUENO);
        fechaLabel.setForeground(new Color(80, 80, 80));
        
        JLabel horaLabel = new JLabel(hora.format(horaFormatter), SwingConstants.RIGHT);
        horaLabel.setFont(FUENTE_TEXTO_MUY_PEQUENO);
        horaLabel.setForeground(new Color(120, 120, 120));
        
        rightPanel.add(fechaLabel);
        rightPanel.add(horaLabel);
        
        JPanel contentPanel = new JPanel(new BorderLayout(15, 0));
        contentPanel.setBackground(Color.WHITE);
        contentPanel.add(leftPanel, BorderLayout.CENTER);
        contentPanel.add(rightPanel, BorderLayout.EAST);
        
        JCheckBox check = new JCheckBox();
        check.setSelected(true);
        check.setBackground(Color.WHITE);
        
        if (actividad instanceof Tarea) {
            check.addActionListener(e -> { if (!check.isSelected()) marcarTareaCompleta((Tarea) actividad, false); });
        } else {
            check.addActionListener(e -> { if (!check.isSelected()) marcarHabitoCompleto((Habito) actividad, false); });
        }
        
        tarjeta.add(check, BorderLayout.WEST);
        tarjeta.add(contentPanel, BorderLayout.CENTER);
        return tarjeta;
    }

    // crear el panel derecho de las tarjetas con fecha hora
    
    private JPanel crearPanelDerecho(LocalDate fecha, LocalTime hora, String textoExtra, Color colorExtra) {
        JPanel rightPanel = new JPanel(new GridLayout(3, 1, 3, 3));
        rightPanel.setBackground(Color.WHITE);
        
        DateTimeFormatter fechaFormatter = DateTimeFormatter.ofPattern("dd/MM/yy");
        DateTimeFormatter horaFormatter = DateTimeFormatter.ofPattern("HH:mm");
        
        JLabel fechaLabel = new JLabel(fecha.format(fechaFormatter), SwingConstants.RIGHT);
        fechaLabel.setFont(FUENTE_TEXTO_PEQUENO);
        fechaLabel.setForeground(Colours.Cl_Texto);
        
        JLabel horaLabel = new JLabel(hora.format(horaFormatter), SwingConstants.RIGHT);
        horaLabel.setFont(FUENTE_TEXTO_MUY_PEQUENO);
        horaLabel.setForeground(Colours.Cl_Texto);
        
        JPanel tagWrapper = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        tagWrapper.setOpaque(false);

        JLabel extraLabel = new JLabel(textoExtra);
        extraLabel.setFont(FUENTE_TEXTO_PEQUENO.deriveFont(Font.BOLD));
        extraLabel.setForeground(colorExtra);
        extraLabel.setOpaque(true);
        
        Color bgColor = new Color(colorExtra.getRed(), colorExtra.getGreen(), colorExtra.getBlue(), 35);
        extraLabel.setBackground(bgColor);
        extraLabel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(colorExtra.getRed(), colorExtra.getGreen(), colorExtra.getBlue(), 70), 1),
            BorderFactory.createEmptyBorder(2, 8, 2, 8)
        ));
        
        tagWrapper.add(extraLabel);
        rightPanel.add(fechaLabel);
        rightPanel.add(horaLabel);
        rightPanel.add(tagWrapper);
        
        return rightPanel;
    }

    // muestra un mensjae cuando no hay tareas

    private JLabel crearMensajeVacio(String mensaje) {
        JLabel label = new JLabel(mensaje, SwingConstants.CENTER);
        label.setFont(FUENTE_TEXTO_NORMAL);
        label.setForeground(Color.GRAY);
        label.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        return label;
    }

    // devuelve colores diferentes segun la prioridad de la tarea
    private Color getColorPrioridad(Tarea.Priority prioridad) {
        switch (prioridad) {
            case ALTA: return Colours.B_Red;
            case MEDIA: return COLOR_AMARILLO;
            case BAJA: return Colours.B_Green;
            default: return Color.GRAY;
        }
    }

    /*
     * logica principal para marcar tareas y habitos completados (Luisa)
     * MODIFICADO: Ahora usa el Gestor en lugar de manipular CSV directamente
     */

    private void marcarTareaCompleta(Tarea tarea, boolean completada) {
        try {
            // Crear una copia actualizada de la tarea
            Tarea tareaActualizada = new Tarea(
                tarea.getId(),
                tarea.getNombre(),
                completada ? Actividad.State.COMPLETADO : Actividad.State.PENDIENTE,
                tarea.getFecha(),
                tarea.getHora(),
                tarea.getDescripcion(),
                tarea.getPrioridad()
            );
            
            boolean exito = gestor.updateTask(tareaActualizada);
            
            if (exito) {
                if (completada) {
                    JOptionPane.showMessageDialog(this, "¡Tarea completada: " + tarea.getNombre() + "!");
                }
                cargarDatosDesdeGestor();
                generarReporte();
            } else {
                JOptionPane.showMessageDialog(this, "Error al actualizar la tarea", "Error", JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al actualizar tarea: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void marcarHabitoCompleto(Habito habito, boolean completado) {
        try {
            Habito habitoActualizado = new Habito(
                habito.getId(),
                habito.getNombre(),
                completado ? Actividad.State.COMPLETADO : Actividad.State.PENDIENTE,
                habito.getFecha(),
                habito.getHora(),
                habito.getFrecuencia()
            );
            gestor.updateHabito(habitoActualizado);
            
            if (completado) {
                JOptionPane.showMessageDialog(this, "¡Hábito completado: " + habito.getNombre() + "!");
            }
            cargarDatosDesdeGestor();
            generarReporte();
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al actualizar hábito: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // hasta aqui --------------------------------

    // panel basico en blanco con borde gris y buen espacio

    private JPanel crearPanelTarjeta() {
        JPanel panel = new JPanel();
        panel.setBackground(COLOR_TARJETA);
        panel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(220, 220, 220), 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 20))
        );
        return panel;
    }

    // Estiliza una JTable para que coincida con la apariencia de la app
    private void styleTaskTable(JTable table, Color headerBg, Color rowBg, Color rowText, Color headerText) {
        // Header
        javax.swing.table.JTableHeader header = table.getTableHeader();
        header.setBackground(headerBg.darker());
        header.setForeground(headerText);
        header.setFont(FUENTE_TEXTO_PEQUENO.deriveFont(Font.BOLD, 14f));
        header.setReorderingAllowed(false);

        // Table overall
        table.setFont(FUENTE_TEXTO_PEQUENO);
        table.setForeground(rowText);
        table.setRowHeight(28);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setSelectionBackground(headerBg.darker());
        table.setSelectionForeground(headerText);

        // Cell renderer that paints non-selected rows with the provided rowBg and text color
        javax.swing.table.DefaultTableCellRenderer cellRenderer = new javax.swing.table.DefaultTableCellRenderer() {
            private final javax.swing.border.Border pad = BorderFactory.createEmptyBorder(2,8,2,8);
            @Override
            public java.awt.Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                java.awt.Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (isSelected) {
                    c.setBackground(table.getSelectionBackground());
                    c.setForeground(table.getSelectionForeground());
                } else {
                    c.setBackground(rowBg);
                    c.setForeground(rowText);
                }
                if (c instanceof JComponent) ((JComponent)c).setBorder(pad);
                return c;
            }
        };

        // Apply renderer to all columns
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(cellRenderer);
        }
    }

    // ordena las tareas y habitos para mostrarlos en el orden correcto
    private List<Tarea> cargarTareasOrdenadas() {
        return cargarTareas().stream()
            .sorted((t1, t2) -> {
                int estadoCompare = Boolean.compare(t1.getEstado() == Actividad.State.COMPLETADO, t2.getEstado() == Actividad.State.COMPLETADO);
                if (estadoCompare != 0) return estadoCompare;
                // SOLO por fecha y hora, sin prioridad
                int fechaCompare = t1.getFecha().compareTo(t2.getFecha());
                if (fechaCompare != 0) return fechaCompare;
                return t1.getHora().compareTo(t2.getHora());
            })
            .collect(Collectors.toList());
    }

    private List<Habito> cargarHabitosOrdenados() {
        return cargarHabitos().stream()
            .sorted((h1, h2) -> {
                int fechaCompare = h1.getFecha().compareTo(h2.getFecha());
                return fechaCompare != 0 ? fechaCompare : h1.getHora().compareTo(h2.getHora());
            })
            .collect(Collectors.toList());
    }

    // lee los archivos csv en orden
    private List<Tarea> cargarTareas() {
        return cargarCSV("src/main/java/com/proyecto/Datos/Tareas.csv", 7, datos -> {
            try {
                int id = Integer.parseInt(datos[0].trim());
                String nombre = (datos[1].trim());
                String descripcion = (datos[2].trim());
                Tarea.Priority prioridad = parsearPrioridad(datos[3].trim(), id);
                Actividad.State estado = parsearEstado(datos[4].trim(), id);
                LocalTime hora = LocalTime.parse(datos[5].trim());
                LocalDate fecha = LocalDate.parse(datos[6].trim());
                return new Tarea(id, nombre, estado, fecha, hora, descripcion, prioridad);
            } catch (Exception e) {
                System.err.println("Error cargando tarea: " + e.getMessage());
                return null;
            }
        });
    }

    private List<Habito> cargarHabitos() {
        return cargarCSV("src/main/java/com/proyecto/Datos/Habitos.csv", 6, datos -> {
            try {
                int id = Integer.parseInt(datos[0].trim());
                String nombre = (datos[1].trim());
                Habito.Frequency frecuencia = parsearFrecuencia(datos[2].trim(), id);
                Actividad.State estado = parsearEstado(datos[3].trim(), id);
                LocalTime hora = LocalTime.parse(datos[4].trim());
                LocalDate fecha = LocalDate.parse(datos[5].trim());
                return new Habito(id, nombre, estado, fecha, hora, frecuencia);
            } catch (Exception e) {
                System.err.println("Error cargando hábito: " + e.getMessage());
                return null;
            }
        });
    }

    private <T> List<T> cargarCSV(String ruta, int campos, java.util.function.Function<String[], T> constructor) {
        List<T> items = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(ruta))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                linea = linea.trim();
                if (linea.isEmpty() || linea.startsWith("#")) continue;
                String[] datos = linea.split(",", -1);
                if (datos.length >= campos && datos[0].trim().matches("\\d+")) {
                    T item = constructor.apply(datos);
                    if (item != null) items.add(item);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error cargando archivo: " + ruta, "Error", JOptionPane.ERROR_MESSAGE);
        }
        return items;
    }

    // convierte textos como alta media a los valores correctos del sistema
    private Tarea.Priority parsearPrioridad(String prioridadStr, int id) {
        try { return Tarea.Priority.valueOf(prioridadStr.toUpperCase()); }
        catch (IllegalArgumentException e) { return Tarea.Priority.MEDIA; }
    }

    private Habito.Frequency parsearFrecuencia(String frecuenciaStr, int id) {
        try { return Habito.Frequency.valueOf(frecuenciaStr.toUpperCase()); }
        catch (IllegalArgumentException e) { return Habito.Frequency.DIARIO; }
    }

    private Actividad.State parsearEstado(String estadoStr, int id) {
        try { return Actividad.State.valueOf(estadoStr.toUpperCase()); }
        catch (IllegalArgumentException e) { return Actividad.State.PENDIENTE; }
    }

    private String obtenerProximaActividadCritica() {
        // Encontrar la próxima actividad PENDIENTE más urgente (hoy)
        java.util.Optional<Tarea> proximaTarea = tareasActuales.stream()
            .filter(t -> t.getFecha().equals(LocalDate.now()))
            .filter(t -> t.getEstado() == Actividad.State.PENDIENTE)
            .filter(t -> t.getPrioridad() == Tarea.Priority.ALTA)
            .min((t1, t2) -> t1.getHora().compareTo(t2.getHora()));

        java.util.Optional<Habito> proximoHabito = habitosActuales.stream()
            .filter(h -> h.getFecha().equals(LocalDate.now()))
            .filter(h -> h.getEstado() == Actividad.State.PENDIENTE)
            .min((h1, h2) -> h1.getHora().compareTo(h2.getHora()));

        if (proximaTarea.isPresent()) {
            return "ALTA: " + (proximaTarea.get().getNombre().length() > 20 ? 
                   proximaTarea.get().getNombre().substring(0, 20) + "..." : 
                   proximaTarea.get().getNombre());
        } else if (proximoHabito.isPresent()) {
            return "Habito: " + (proximoHabito.get().getNombre().length() > 18 ? 
                   proximoHabito.get().getNombre().substring(0, 18) + "..." : 
                   proximoHabito.get().getNombre());
        } else {
            return "Sin actividades pendientes";
        }
    }
}