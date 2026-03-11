package clevis;

import clevis.model.ShapeManager;

/**
 * Handing user commands' input
 */
public class CommandProcessor {

    /**
     * Using switch-case to identify what method being calling
     * @param rawCommand user commands input
     * @param manager importing shapeManager method
     * @return exit
     */
    public static boolean execute(String rawCommand, ShapeManager manager) {
        if (rawCommand == null || rawCommand.trim().isEmpty()) return false;

        String[] tokens = rawCommand.trim().split("\\s+");
        String cmd = tokens[0].toLowerCase();

        try {
            switch (cmd) {
                case "rectangle" -> {
                    checkArgs(tokens, 6);
                    manager.createRectangle(tokens[1],
                            Double.parseDouble(tokens[2]), Double.parseDouble(tokens[3]),
                            Double.parseDouble(tokens[4]), Double.parseDouble(tokens[5]));
                }
                case "line" -> {
                    checkArgs(tokens, 6);
                    manager.createLine(tokens[1],
                            Double.parseDouble(tokens[2]), Double.parseDouble(tokens[3]),
                            Double.parseDouble(tokens[4]), Double.parseDouble(tokens[5]));
                }
                case "circle" -> {
                    checkArgs(tokens, 5);
                    manager.createCircle(tokens[1],
                            Double.parseDouble(tokens[2]), Double.parseDouble(tokens[3]),
                            Double.parseDouble(tokens[4]));
                }
                case "square" -> {
                    checkArgs(tokens, 5);
                    manager.createSquare(tokens[1],
                            Double.parseDouble(tokens[2]), Double.parseDouble(tokens[3]),
                            Double.parseDouble(tokens[4]));
                }
                case "group" -> manager.group(rawCommand);
                case "ungroup" -> {
                    checkArgs(tokens, 2);
                    manager.ungroup(tokens[1]);
                }
                case "delete" -> {
                    checkArgs(tokens, 2);
                    manager.delete(tokens[1]);
                }
                case "move" -> {
                    checkArgs(tokens, 4);
                    manager.move(tokens[1],
                            Double.parseDouble(tokens[2]), Double.parseDouble(tokens[3]));
                }
                case "boundingbox" -> {
                    checkArgs(tokens, 2);
                    double[] box = manager.getBoundingBox(tokens[1]);
                    System.out.printf("%.2f %.2f %.2f %.2f%n", box[0], box[1], box[2], box[3]);
                    return false;
                }
                case "intersect" -> {
                    checkArgs(tokens, 3);
                    System.out.println(manager.intersect(tokens[1], tokens[2]));
                    return false;
                }
                case "shapeat" -> {
                    checkArgs(tokens, 3);
                    String found = manager.shapeAt(Double.parseDouble(tokens[1]), Double.parseDouble(tokens[2]));
                    System.out.println(found == null ? "" : found);
                    return false;
                }
                case "list" -> {
                    checkArgs(tokens, 2);
                    System.out.println(manager.list(tokens[1]));
                    return false;
                }
                case "listall" -> {
                    System.out.println(manager.listAll());
                    return false;
                }
                case "undo" -> {
                    manager.undo();
                }
                case "redo" -> {
                    manager.redo(); 
                }
                case "quit" -> System.exit(0);
                default -> {
                    System.out.println("Unknown command: " + cmd);
                    return false;
                }

            }
            return true;
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }

    private static void checkArgs(String[] tokens, int expected) {
        if (tokens.length != expected) {
            throw new IllegalArgumentException("Expected " + (expected - 1) + " arguments for command '" + tokens[0] + "'");
        }
    }
}