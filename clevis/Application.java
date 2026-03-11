package clevis;

import clevis.model.Logger;
import clevis.model.ShapeManager;
import clevis.view.ClevisGUI;
import java.io.IOException;
import java.util.Scanner;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**handling log and quit */
public class Application {

    /**
     * Main Program
     * @param args user input log path
     */
    public static void main(String[] args) {
        String htmlPath = "clevis/log/log.html";
        String txtPath = "clevis/log/log.txt";
        boolean guiMode = false;

        for (int i = 0; i < args.length; i++) {
            if ("-html".equalsIgnoreCase(args[i]) && i + 1 < args.length) {
                htmlPath = args[++i];
            } else if ("-txt".equalsIgnoreCase(args[i]) && i + 1 < args.length) {
                txtPath = args[++i];
            } else if ("-gui".equalsIgnoreCase(args[i])) {
                guiMode = true;
            }
        }
        
        if (guiMode) {
            final String finalHtmlPath = htmlPath;
            final String finalTxtPath = txtPath;
            
            SwingUtilities.invokeLater(() -> {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception ignored) {
                }
                new ClevisGUI(finalHtmlPath, finalTxtPath);
            });
            return;
        }

        Logger logger = null;
        ShapeManager manager = new ShapeManager();

        try {
            logger = new Logger(htmlPath, txtPath);
        } catch (IOException e) {
            System.err.println("Cannot create log files: " + e.getMessage());
            System.exit(1);
        }

        Runtime.getRuntime().addShutdownHook(new Thread(logger::close));

        System.out.println("=== Clevis – Command Line Vector Graphics Software ===");
        System.out.println("Type 'quit' to exit. Type commands below:\n");

        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                System.out.print("> ");
                String input = scanner.nextLine().trim();

                if (input.isEmpty()) continue;
                if ("quit".equalsIgnoreCase(input)) {
                    System.out.println("Goodbye!");
                    break;
                }

                boolean success = CommandProcessor.execute(input, manager);
                if (success) {
                    logger.log(input);
                }
            }
        }
    }
}