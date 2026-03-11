package clevis.view;

import clevis.CommandProcessor;
import clevis.model.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import javax.swing.*;

/**
 * BON1 GUI Implementing
 */
public class ClevisGUI extends JFrame {

    private ShapeManager shapeManager;
    private Logger logger;
    private GraphicsPanel graphicsPanel;
    
    private JTextField commandField;
    private JButton executeButton;
    private JTextArea outputArea;
    
    private JButton zoomInButton;
    private JButton zoomOutButton;
    private JButton resetViewButton;

    /**
     * Deciding Log path and calling all the methods
     * @param htmlPath html path
     * @param txtPath txt path
     */
    public ClevisGUI(String htmlPath, String txtPath) {
        super("Clevis – Graphical Vector Graphics Software");
        this.shapeManager = new ShapeManager();
        try {
            this.logger = new Logger(htmlPath, txtPath);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error" , JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (logger != null) {
                logger.close();
            }
        }));
        
        initComponents();

        setupLayout();

        setupListeners();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        final int width = 1200;
        final int height = 800;
        setSize(width, height);
        setVisible(true);

        appendOutput("=== Clevis - Graphical Mode ===");
        appendOutput("Enter commands in the field above.");
        appendOutput("Type 'quit' to exit.\n");
    }
    private void initComponents() {
        graphicsPanel = new GraphicsPanel(shapeManager);

        commandField = new JTextField();

        executeButton = new JButton("Execute");
        outputArea = new JTextArea();
        outputArea.setEditable(false);

        zoomInButton = new JButton("Zoom In");
        zoomOutButton = new JButton("Zoom Out");
        resetViewButton = new JButton("Reset View");
    }

    private void setupLayout() {
        setLayout(new BorderLayout(5, 5));

        //TOP
        JPanel topPanel = new JPanel(new BorderLayout(5, 5));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));
        
        JLabel commandLabel = new JLabel("Command:");
        topPanel.add(commandLabel, BorderLayout.WEST);
        topPanel.add(commandField, BorderLayout.CENTER);
        topPanel.add(executeButton, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        // CENTER
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBorder(BorderFactory.createTitledBorder("Graphics Display"));
        centerPanel.add(graphicsPanel, BorderLayout.CENTER);

        JPanel zoomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        zoomPanel.add(zoomInButton);
        zoomPanel.add(zoomOutButton);
        zoomPanel.add(resetViewButton);
        centerPanel.add(zoomPanel, BorderLayout.SOUTH);
        add(centerPanel, BorderLayout.CENTER);

        // BOTTOM
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBorder(BorderFactory.createTitledBorder("Output"));
        JScrollPane scrollPane = new JScrollPane(outputArea);
        bottomPanel.add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }
    
    private void setupListeners() {
        executeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                executeCommand();
            }
        });
                
        commandField.addActionListener(e -> executeCommand());
        
        zoomInButton.addActionListener(e -> {
            graphicsPanel.zoomIn();
            appendOutput("Zoomed in.");
        });
        
        zoomOutButton.addActionListener(e -> {
            graphicsPanel.zoomOut();
            appendOutput("Zoomed out.");
        });
        
        resetViewButton.addActionListener(e -> {
            graphicsPanel.resetView();
            appendOutput("View reset to default.");
        });

        
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                handleQuit();
            }
        });
    }

    private void executeCommand() {
        String command = commandField.getText().trim();
        
        if (command.isEmpty()) {
            return;
        }
        
        appendOutput("> " + command);
        
        if ("quit".equalsIgnoreCase(command)) {
            handleQuit();
            return;
        }
        
        try {
            boolean success = CommandProcessor.execute(command, shapeManager);
            
            if (success) {
                logger.log(command);
                graphicsPanel.refresh();
                appendOutput("✓ Command executed successfully.");
                
            } else {
                appendOutput("Command completed.");
            }
            
        } catch (Exception e) {
            appendOutput("✗ Error: " + e.getMessage());
        }
        
        commandField.setText("");
        commandField.requestFocus();
    }

    private void appendOutput(String msg) {
        outputArea.append(msg + "\n");
        outputArea.setCaretPosition(outputArea.getDocument().getLength());
    }
    
    private void handleQuit() {
        int choice = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to quit?",
            "Confirm Quit",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );
        
        if (choice == JOptionPane.YES_OPTION) {
            if (logger != null) {
                logger.close();
            }
            System.exit(0);
        }
    }

    /**
     * GUI Main program
     * @param args user input log path
     */
    public static void main(String[] args) {
        String htmlPath = "clevis/log/log.html";
        String txtPath = "clevis/log/log.txt";

        SwingUtilities.invokeLater(() -> {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
        }
        
        new ClevisGUI(htmlPath, txtPath); 
    });
    }
}