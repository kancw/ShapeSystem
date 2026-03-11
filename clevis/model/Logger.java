package clevis.model;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Logger class for recording commands executed in the CLEVIS system.
 * It writes logs to:
 * - A plain text file (for simple reading or debugging)
 * - An HTML file (for structured viewing in a browser)
 * Each command is numbered sequentially and stored in both formats.
 */
public class Logger {
    
    private int count = 0;
    private final PrintWriter txt;
    private final PrintWriter html;

    /**
     * Constructs a Logger that writes to both HTML and text files.
     *
     * @param htmlPath the file path for the HTML log
     * @param txtPath  the file path for the text log
     * @throws IOException if file creation or writing fails
     */
    public Logger(String htmlPath, String txtPath) throws IOException {
        txt = new PrintWriter(new FileWriter(txtPath, false));
        html = new PrintWriter(new FileWriter(htmlPath, false));
        html.println("<!DOCTYPE html>");
        html.println("<html><head><meta charset=\"UTF-8\">");
        html.println("<title>Clevis Log</title></head><body>");
        html.println("<h2>Clevis Operation Log</h2>");
        html.println("<table border=\"1\">");
        html.println("<tr><th>No.</th><th>Command</th></tr>");
        html.flush();
    }

    /**
     * Logs a command to both text and HTML files.
     *
     * @param command the command string to log
     */
    public void log(String command) {
        count++;
        txt.println(command);
        html.println("<tr><td>" + count + "</td><td>" + command.replace("<", "&lt;").replace(">", "&gt;") + "</td></tr>");
        txt.flush();
        html.flush();
    }

    /**
     * Closes the log files and finalizes the HTML structure.
     */
    public void close() {
        html.println("</table></body></html>");
        txt.close();
        html.close();
    }
}
