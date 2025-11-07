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

public class CompleteWindow extends JDialog { // para crear una ventana 
    private Gestor gestor;
    private JPanel mainPanel;
    
    private final Color COLOR_PRIMARIO = new Color(41, 128, 185);  // elegimos colores 
    private final Color COLOR_FONDO = new Color(245, 245, 245);
    private final Color COLOR_TARJETA = Color.WHITE;
    private final Color COLOR_AMARILLO = new Color(241, 196, 15);

    public CompleteWindow(JFrame parent, Gestor gestor) { // confugurar y colocar el titulo como tal 
        super(parent, "GESTIÓN DE ACTIVIDADES", true);
        this.gestor = gestor;
        configurarVentana();
        inicializarComponentes();
        generarReporte();
        setLocationRelativeTo(null);
    }

    private void configurarVentana() { // pendiente 
        setSize(800, 600);
        setLocationRelativeTo(getParent());
        setLayout(new BorderLayout());
        getContentPane().setBackground(COLOR_FONDO);
    }

    private void inicializarComponentes() {
        // Panel superior
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(COLOR_PRIMARIO);
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        
        JLabel titulo = new JLabel("GESTIÓN DE TAREAS Y HÁBITOS", SwingConstants.CENTER); // crear un titulo grande en azul. 
        titulo.setFont(new Font("Arial", Font.BOLD, 20));
        titulo.setForeground(Color.WHITE);
        topPanel.add(titulo, BorderLayout.CENTER);
        
        // Botones
        JPanel botonesPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0)); // agrega 3 botones 
        botonesPanel.setOpaque(false);
        
        JButton btnActualizar = new JButton("Actualizar");
        btnActualizar.addActionListener(e -> generarReporte());
        botonesPanel.add(btnActualizar);
        
        JButton btnSoloPendientes = new JButton("Solo Pendientes");
        btnSoloPendientes.setBackground(COLOR_AMARILLO);
        btnSoloPendientes.setForeground(Color.WHITE);
        btnSoloPendientes.addActionListener(e -> mostrarSoloPendientes());
        botonesPanel.add(btnSoloPendientes);
        
        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.addActionListener(e -> dispose());
        botonesPanel.add(btnCerrar);
        
        topPanel.add(botonesPanel, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);
        
        // Panel principal
        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(COLOR_FONDO);
        
        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setBorder(null);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void generarReporte() { // pone cada una de las secciones en orden carga tareas y habitos 
        mainPanel.removeAll();
        
        List<Tarea> tareas = cargarTareas();
        List<Habito> habitos = cargarHabitos();
        
        // Agregar secciones
        mainPanel.add(crearEncabezado());
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(crearPendientesHoy(tareas, habitos));
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(crearTodasTareas(tareas));
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(crearTodosHabitos(habitos));
        
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    private void mostrarSoloPendientes() {
        mainPanel.removeAll();
        
        List<Tarea> tareas = cargarTareas();
        List<Habito> habitos = cargarHabitos();
        
        mainPanel.add(crearEncabezado());
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(crearPendientesHoy(tareas, habitos));
        
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    private JPanel crearEncabezado() {
        JPanel panel = crearPanelTarjeta();
        panel.setLayout(new BorderLayout());
        
        JLabel titulo = new JLabel("GESTIÓN DE ACTIVIDADES", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 16));
        
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        JLabel fecha = new JLabel("Fecha: " + LocalDate.now().format(fmt));
        fecha.setFont(new Font("Arial", Font.PLAIN, 12));
        
        panel.add(titulo, BorderLayout.NORTH);
        panel.add(fecha, BorderLayout.SOUTH);
        
        return panel;
    }

    private JPanel crearPendientesHoy(List<Tarea> tareas, List<Habito> habitos) {
        JPanel panel = crearPanelTarjeta();
        panel.setLayout(new BorderLayout());
        
        JLabel titulo = new JLabel("PENDIENTES PARA HOY", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 14));
        titulo.setForeground(Color.RED);
        panel.add(titulo, BorderLayout.NORTH);
        
        JPanel contenido = new JPanel();
        contenido.setLayout(new BoxLayout(contenido, BoxLayout.Y_AXIS));
        contenido.setBackground(COLOR_TARJETA);
        
        // Filtrar pendientes
        List<Tarea> tareasPendientes = tareas.stream()
            .filter(t -> t.getEstado() == Actividad.State.PENDIENTE)
            .collect(Collectors.toList());
            
        List<Habito> habitosPendientes = habitos.stream()
            .filter(h -> h.getEstado() == Actividad.State.PENDIENTE)
            .collect(Collectors.toList());
        
        // Mostrar tareas pendientes
        if (!tareasPendientes.isEmpty()) {
            JLabel subtitulo = new JLabel("Tareas Pendientes:");
            subtitulo.setFont(new Font("Arial", Font.BOLD, 12));
            contenido.add(subtitulo);
            
            for (Tarea tarea : tareasPendientes) {
                contenido.add(crearItemTarea(tarea));
                contenido.add(Box.createVerticalStrut(5));
            }
        }
        
        // Mostrar hábitos pendientes
        if (!habitosPendientes.isEmpty()) {
            JLabel subtitulo = new JLabel("Hábitos Pendientes:");
            subtitulo.setFont(new Font("Arial", Font.BOLD, 12));
            contenido.add(subtitulo);
            
            for (Habito habito : habitosPendientes) {
                contenido.add(crearItemHabito(habito));
                contenido.add(Box.createVerticalStrut(5));
            }
        }
        
        if (tareasPendientes.isEmpty() && habitosPendientes.isEmpty()) {
            contenido.add(new JLabel("¡No hay pendientes para hoy!"));
        }
        
        panel.add(new JScrollPane(contenido), BorderLayout.CENTER);
        return panel;
    }

    private JPanel crearTodasTareas(List<Tarea> tareas) {
        JPanel panel = crearPanelTarjeta();
        panel.setLayout(new BorderLayout());
        
        JLabel titulo = new JLabel("TODAS LAS TAREAS", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 14));
        panel.add(titulo, BorderLayout.NORTH);
        
        JPanel contenido = new JPanel();
        contenido.setLayout(new BoxLayout(contenido, BoxLayout.Y_AXIS));
        contenido.setBackground(COLOR_TARJETA);
        
        if (tareas.isEmpty()) {
            contenido.add(new JLabel("No hay tareas"));
        } else {
            for (Tarea tarea : tareas) {
                contenido.add(crearItemTarea(tarea));
                contenido.add(Box.createVerticalStrut(5));
            }
        }
        
        panel.add(new JScrollPane(contenido), BorderLayout.CENTER);
        return panel;
    }

    private JPanel crearTodosHabitos(List<Habito> habitos) {
        JPanel panel = crearPanelTarjeta();
        panel.setLayout(new BorderLayout());
        
        JLabel titulo = new JLabel("TODOS LOS HÁBITOS", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 14));
        panel.add(titulo, BorderLayout.NORTH);
        
        JPanel contenido = new JPanel();
        contenido.setLayout(new BoxLayout(contenido, BoxLayout.Y_AXIS));
        contenido.setBackground(COLOR_TARJETA);
        
        if (habitos.isEmpty()) {
            contenido.add(new JLabel("No hay hábitos"));
        } else {
            for (Habito habito : habitos) {
                contenido.add(crearItemHabito(habito));
                contenido.add(Box.createVerticalStrut(5));
            }
        }
        
        panel.add(new JScrollPane(contenido), BorderLayout.CENTER);
        return panel;
    }

    private JPanel crearItemTarea(Tarea tarea) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(COLOR_TARJETA);
        panel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        
        // para marcar como completado
        JCheckBox checkCompletado = new JCheckBox();
        checkCompletado.setSelected(tarea.getEstado() == Actividad.State.COMPLETADO);
        checkCompletado.addActionListener(e -> {
            marcarTareaCompleta(tarea, checkCompletado.isSelected());
        });
        
        JLabel nombre = new JLabel(tarea.getNombre());
        nombre.setFont(new Font("Arial", Font.PLAIN, 12));
        
        // Si está completado, se ve tachado
        if (tarea.getEstado() == Actividad.State.COMPLETADO) {
            nombre.setText("<html><strike>" + tarea.getNombre() + "</strike></html>");
            nombre.setForeground(Color.GRAY);
        }
        
        panel.add(checkCompletado, BorderLayout.WEST);
        panel.add(nombre, BorderLayout.CENTER);
        
        return panel;
    }

    private JPanel crearItemHabito(Habito habito) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(COLOR_TARJETA);
        panel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        
        // para marcar como completado
        JCheckBox checkCompletado = new JCheckBox();
        checkCompletado.setSelected(habito.getEstado() == Actividad.State.COMPLETADO);
        checkCompletado.addActionListener(e -> {
            marcarHabitoCompleto(habito, checkCompletado.isSelected());
        });
        
        JLabel nombre = new JLabel(habito.getNombre());
        nombre.setFont(new Font("Arial", Font.PLAIN, 12));
        
        // Si está completado, se ve tachado
        if (habito.getEstado() == Actividad.State.COMPLETADO) {
            nombre.setText("<html><strike>" + habito.getNombre() + "</strike></html>");
            nombre.setForeground(Color.GRAY);
        }
        
        panel.add(checkCompletado, BorderLayout.WEST);
        panel.add(nombre, BorderLayout.CENTER);
        
        return panel;
    }

    private void marcarTareaCompleta(Tarea tarea, boolean completada) {
        if (completada) {
            tarea.setEstado(Actividad.State.COMPLETADO);
            JOptionPane.showMessageDialog(this, "¡Tarea completada: " + tarea.getNombre() + "!");
        } else {
            tarea.setEstado(Actividad.State.PENDIENTE);
        }
        generarReporte();
    }

    private void marcarHabitoCompleto(Habito habito, boolean completado) {
        if (completado) {
            habito.setEstado(Actividad.State.COMPLETADO);
            JOptionPane.showMessageDialog(this, "¡Hábito completado: " + habito.getNombre() + "!");
        } else {
            habito.setEstado(Actividad.State.PENDIENTE);
        }
        generarReporte();
    }

    private JPanel crearPanelTarjeta() {
        JPanel panel = new JPanel();
        panel.setBackground(COLOR_TARJETA);
        panel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(Color.LIGHT_GRAY, 1),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        return panel;
    }

    // MÉTODOS PARA CARGAR DESDE CSV
    private List<Tarea> cargarTareas() {
        List<Tarea> tareas = new ArrayList<>();
        try {
            // RUTA PARA CARGAR LOS HABITOS SE DEBE DE MODIFICAR 
            String ruta = "Datos/Tareas.csv";
            File archivo = new File(ruta);
            
            System.out.println("Buscando archivo de tareas en: " + archivo.getAbsolutePath());
            
            if (!archivo.exists()) {
                JOptionPane.showMessageDialog(this, 
                    "Archivo no encontrado: " + archivo.getAbsolutePath() + 
                    "\nAsegúrate de que existe la carpeta 'Datos' con el archivo 'Tareas.csv'");
                return tareas;
            }
            
            BufferedReader br = new BufferedReader(new FileReader(archivo));
            String linea;
            while ((linea = br.readLine()) != null) {
                linea = linea.trim();
                if (linea.isEmpty()) continue;
                
                String[] datos = linea.split(",");
                if (datos.length >= 7) {
                    try {
                        Tarea tarea = new Tarea(
                            Integer.parseInt(datos[0].trim()),
                            datos[1].trim(),
                            Actividad.State.valueOf(datos[4].trim().toUpperCase()),
                            LocalDate.parse(datos[6].trim()),
                            LocalTime.parse(datos[5].trim()),
                            datos[2].trim(),
                            Tarea.Priority.valueOf(datos[3].trim().toUpperCase())
                        );
                        tareas.add(tarea);
                    } catch (Exception e) {
                        System.err.println("Error procesando línea de tarea: " + linea);
                        System.err.println("Error: " + e.getMessage());
                    }
                } else {
                    System.err.println("Línea con formato incorrecto (se esperaban 7 campos): " + linea);
                }
            }
            br.close();
            
            System.out.println("Tareas cargadas: " + tareas.size());
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error cargando tareas: " + e.getMessage());
            e.printStackTrace();
        }
        return tareas;
    }

    private List<Habito> cargarHabitos() {
        List<Habito> habitos = new ArrayList<>();
        try {
            // RUTA PARA TREAR HABITOS 
            String ruta = "Datos/Habitos.csv";
            File archivo = new File(ruta);
            
            System.out.println("Buscando archivo de hábitos en: " + archivo.getAbsolutePath());
            
            if (!archivo.exists()) {
                JOptionPane.showMessageDialog(this, 
                    "Archivo no encontrado: " + archivo.getAbsolutePath() + 
                    "\nAsegúrate de que existe la carpeta 'Datos' con el archivo 'Habitos.csv'");
                return habitos;
            }
            
            BufferedReader br = new BufferedReader(new FileReader(archivo));
            String linea;
            while ((linea = br.readLine()) != null) {
                linea = linea.trim();
                if (linea.isEmpty()) continue;
                
                String[] datos = linea.split(",");
                if (datos.length >= 6) {
                    try {
                        Habito habito = new Habito(
                            Integer.parseInt(datos[0].trim()),
                            datos[1].trim(),
                            Actividad.State.valueOf(datos[3].trim().toUpperCase()),
                            LocalDate.parse(datos[5].trim()),
                            LocalTime.parse(datos[4].trim()),
                            Habito.Frequency.valueOf(datos[2].trim().toUpperCase())
                        );
                        habitos.add(habito);
                    } catch (Exception e) {
                        System.err.println("Error procesando línea de hábito: " + linea);
                        System.err.println("Error: " + e.getMessage());
                    }
                } else {
                    System.err.println("Línea con formato incorrecto (se esperaban 6 campos): " + linea);
                }
            }
            br.close();
            
            System.out.println("Hábitos cargados: " + habitos.size());
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error cargando hábitos: " + e.getMessage());
            e.printStackTrace();
        }
        return habitos;
    }
}