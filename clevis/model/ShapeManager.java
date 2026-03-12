package clevis.model;

import clevis.model.command.Command;
import clevis.model.command.CreateShapeCommand;
import clevis.model.command.DeleteCommand;
import clevis.model.command.GroupCommand;
import clevis.model.command.MoveCommand;
import java.util.*;

/**
 * Manages all the shapes
 * undo and redo methods
 */
public class ShapeManager{
    private final Map<String, Shape> shapes = new LinkedHashMap<>();
    private final List<Shape> zOrderList = new ArrayList<>();
    private final Deque<Command> undoStack = new ArrayDeque<>();
    private final Deque<Command> redoStack = new ArrayDeque<>();
    private int currentZOrder = 0;

    private int nextZOrder() {
        return ++currentZOrder;
    }

    private void addShape(Shape shape) {
        shapes.put(shape.getName(), shape);
        zOrderList.add(shape);
        zOrderList.sort(Comparator.comparingInt(Shape::getZOrder));
    }

    /**
     * Create new rectangle
     * @param name rectangle name
     * @param x rectangle x coordination
     * @param y rectangle y coordination
     * @param w rectangle width
     * @param h rectangle height
     */
    public void createRectangle(String name, double x, double y, double w, double h) {
        checkNameAvailable(name);
        Rectangle r = new Rectangle(name, x, y, w, h, nextZOrder());
        addShape(r);
        undoStack.push(new CreateShapeCommand(this, r));
        redoStack.clear();
    }

    /**
     * Create new Line
     * @param name Line name
     * @param x1 start of line x coordination
     * @param y1 start of line y coordination
     * @param x2 end of line x coordination
     * @param y2 end of line y coordination
     */
    public void createLine(String name, double x1, double y1, double x2, double y2) {
        checkNameAvailable(name);
        Line line = new Line(name, x1, y1, x2, y2, nextZOrder());
        addShape(line);
        undoStack.push(new CreateShapeCommand(this, line));
        redoStack.clear();
    }

    /**
     * Create new circle
     * @param name circle name
     * @param x circle x coordination
     * @param y circle y coordination
     * @param r circle radius
     */
    public void createCircle(String name, double x, double y, double r) {
        checkNameAvailable(name);
        Circle circle = new Circle(name, x, y, r, nextZOrder());
        addShape(circle);
        undoStack.push(new CreateShapeCommand(this, circle));
        redoStack.clear();
    }

    /**
     * Create new square
     * @param name square name
     * @param x square x coordination
     * @param y square y coordination
     * @param s square length
     */
    public void createSquare(String name, double x, double y, double s) {
        checkNameAvailable(name);
        Square square = new Square(name, x, y, s, nextZOrder());
        addShape(square);
        undoStack.push(new CreateShapeCommand(this, square));
        redoStack.clear();
    }

    /**
     * grouping multiple shapes into a single shape
     * @param rawCommand the group of name
     */
    public void group(String rawCommand) {
        String[] tokens = rawCommand.trim().split("\\s+");
        if (tokens.length < 3) {
            throw new IllegalArgumentException("Group command requires at least a name and one member");
        }

        String groupName = tokens[1];
        checkNameAvailable(groupName);

        List<Shape> members = new ArrayList<>();
        for (int i = 2; i < tokens.length; i++) {
            Shape s = shapes.remove(tokens[i]);
            if (s == null) throw new IllegalArgumentException("Shape not found: " + tokens[i]);
            members.add(s);
            zOrderList.remove(s);
        }

        Group group = new Group(groupName, nextZOrder(), members);
        addShape(group);

        undoStack.push(new GroupCommand(this, group, members));
        redoStack.clear();
    }

    /**
     * Ungroup a composite shape
     * @param groupName the name of the group to ungroup
     */
    public void ungroup(String groupName) {
        Shape g = shapes.remove(groupName);
        if (g == null) {
            throw new IllegalArgumentException("Group not found: " + groupName);
        }
        if (!(g instanceof Group)) {
            throw new IllegalArgumentException(groupName + " is a shape, not a group");
        }
        
        Group group = (Group) g;
        for (Shape shapeInGroup : group.getMembers()) {
            shapes.put(shapeInGroup.getName(), shapeInGroup);
        }

        undoStack.push(new GroupCommand(this, g, ((Group) g).getMembers()));
        redoStack.clear();
    }

    /**
     * Moving a shape into a new displacement
     * @param name the name of shape to move
     * @param dx new x coordination
     * @param dy new y coordination
     */
    public void move(String name, double dx, double dy) {
        Shape s = shapes.get(name);
        if(s == null){
            throw new IllegalArgumentException("Shape not found");
        }        
        
        undoStack.push(new MoveCommand(s, dx, dy));
        redoStack.clear();
        s.move(dx, dy);
    }

    /**
     * Deleting a shape
     * @param name the name of shape need to delete
     */
    public void delete(String name) {
    Shape s = shapes.get(name);
    if (s == null) {
        throw new IllegalArgumentException("Shape not found");
    }
    
    List<Shape> deleted = new ArrayList<>();
    collectAll(s, deleted);

    for (Shape shape : deleted) {
        removeShape(shape);
    }

    undoStack.push(new DeleteCommand(this, deleted));
    redoStack.clear();
    }

    /**
     * Collects all shapes, used for deletion
     * @param s the shape to collect
     * @param list the list to add collected shape
     */
    private void collectAll(Shape s, List<Shape> list) {
        list.add(s);
        if (s instanceof Group g) {
            for (Shape m : g.getMembers()) {
                collectAll(m, list);
            }
        }
    }

    /**
     * Calculating the minimum bounding box of a shape
     * @param name the name of the shape
     * @return array [x, y, width, height] of the bounding box
     */
    public double[] getBoundingBox(String name) {
        Shape s = shapes.get(name);
        if(s == null){
            throw new IllegalArgumentException("Shape not found");
        }
        
        double[] b = s.getBoundingBox();
            return new double[] {
                round(b[0]), round(b[1]), round(b[2]), round(b[3])
            };
    }

    /**
     * Finding the topmost shape at a point
     * @param x the x coordination
     * @param y the y coordination
     * @return the name of the topmost shape, if no shapes return null
     */
    public String shapeAt(double x, double y) {
        List<Shape> list = new ArrayList<>(zOrderList);
        Collections.reverse(list);
        for (Shape s : list) {
            if (s.coverPoint(x, y)) return s.getName();
        }
        return null;
    }

    /**
     * check if two shapes intersect
     * @param n1 the name of the first shape
     * @param n2 the name of the second shape
     * @return true if intersect
     */
    public boolean intersect(String n1, String n2) {
        Shape s1 = shapes.get(n1);
        Shape s2 = shapes.get(n2);
        if (s1 == null || s2 == null){
            return false;
        }
        double[] b1 = s1.getBoundingBox();
        double[] b2 = s2.getBoundingBox();
        return (b1[0] <= b2[0] + b2[2] &&
                b2[0] <= b1[0] + b1[2] &&
                b1[1] <= b2[1] + b2[3] &&
                b2[1] <= b1[1] + b1[3]);
    }

    /**
     * Listing all shapes
     * @return a formatted string containing information about all shapes
     */
    public String listAll() {
        if (shapes.isEmpty()) {
            return "No shapes available.";
        }

        StringBuilder sb = new StringBuilder();
        for (Shape s : shapes.values()) {
            if (s instanceof Group) {
                Group group = (Group) s;
                sb.append("Group ").append(group.getName()).append(":");
                for (Shape child : group.getMembers()) {
                    sb.append("\n ").append(child.getName());
                }
            } else {
                sb.append(s.describe());
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    /**
     * Listing a specific shape
     * @param name the name of the shape
     * @return a formatted string with the shape's details
     */
    public String list(String name) {
        Shape s = shapes.get(name);
        if(s != null){
            return s.describe();
        }else{
            return "Shape not found";
        }
    }

    /**
     * Return all shapes currently in the manager
     * @return a map of shape names to shape objects
     */
    public Map<String, Shape> getAllShapes() {
        return shapes;
    }

    /**
     * Checking if a name is it available
     * @param name the name to check
     */
    private void checkNameAvailable(String name) {
        if (shapes.containsKey(name)) {
            throw new IllegalArgumentException("Name already used: " + name);
        }
    }

    private static final int rounding = 100;
    /**
     * Rounds a value to 2 decimal
     * @param val value to be rounded
     * @return return rounded value
     */
    private double round(double val) {
        return (double) Math.round(val * rounding) / rounding;
    }

    /**
     * Adding a shape directly without creating an undo command
     * @param shape the shape to add
     */
    public void addShapeDirectly(Shape shape) {
        shapes.put(shape.getName(), shape);
        zOrderList.add(shape);
    }

    /**
     * Removing a shape directly without creating an undo command
     * @param shape the shape to remove
     */
    public void removeShape(Shape shape) {
        shapes.remove(shape.getName());
        zOrderList.remove(shape);
    }

    /**
     * Undo the last operation
     */
    public void undo() {
        if (undoStack.isEmpty()) {
            System.out.println("Nothing to undo.");
            return;
        }

        Command command = undoStack.pop();
        command.undo(); 
        redoStack.push(command);
    }

    /**
     * Redo the last operation
     */
    public void redo() {
        if (redoStack.isEmpty()) {
            System.out.println("Nothing to redo.");
            return;
        }

        Command command = redoStack.pop();
        command.redo();  
        undoStack.push(command);
    }
}
