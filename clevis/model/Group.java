package clevis.model;

import java.util.ArrayList;
import java.util.List;


/**
 * Represents a group of shapes. A group delegates operations
 * such as move, coverPoint, and intersect to its member shapes.
 */
public class Group extends Shape {

    /** List of all shapes in the group */
    private final List<Shape> allShapes = new ArrayList<>();

    /**
     * Constructs a Group object.
     *
     * @param name   the name of the group
     * @param zOrder rendering order
     * @param shapes initial member shapes
     */
    public Group(String name, int zOrder, List<Shape> shapes) {
        super(name, zOrder);
        allShapes.addAll(shapes);
    }

    /**
     * Moves all shapes in the group by the given delta values.
     *
     * @param dx amount to move along the x-axis
     * @param dy amount to move along the y-axis
     */
    @Override
    public void move(double dx, double dy){
        for(Shape s : allShapes){
            s.move(dx, dy);
        }
    }

    /**
     * Returns the bounding box of the group, covering all member shapes.
     *
     * @return an array [minX, minY, maxX, maxY]
     */
    @Override
    public double[] getBoundingBox(){
        double minX = Double.MAX_VALUE;
        double minY = Double.MAX_VALUE;
        double maxX = -Double.MAX_VALUE;
        double maxY = -Double.MAX_VALUE;

        for(Shape s : allShapes){
            double[] wholeBoundingBox = s.getBoundingBox();
            minX = Math.min(minX, wholeBoundingBox[0]);
            minY = Math.min(minY, wholeBoundingBox[1]);
            maxX = Math.max(maxX, wholeBoundingBox[0] + wholeBoundingBox[2]);
            maxY = Math.max(maxY, wholeBoundingBox[1] + wholeBoundingBox[3]);
        }
        return new double[]{minX, minY, maxX - minX, maxY - minY};
    }

    /**
     * Checks whether any shape in the group covers the given point.
     *
     * @param x x-coordinate of the point
     * @param y y-coordinate of the point
     * @return true if at least one shape covers the point
     */
    @Override
    public boolean coverPoint(double x, double y){
        for(Shape s : allShapes){
            if(s.coverPoint(x, y)){
                return true;
            }
        }
        return false;
    }

    /**
     * Returns a textual description of the group and its members.
     *
     * @return formatted description string
     */
    @Override
    public String describe(){
        StringBuilder sb = new StringBuilder("Group " + getName() + ": ");
        for (Shape s : allShapes) {
            sb.append(s.getName()).append(" ");
        }
        return sb.toString();
    }

    /**
     * Returns a copy of the group’s member list.
     *
     * @return new list of member shapes
     */
    public List<Shape> getMembers() {
        return new ArrayList<>(allShapes);
    }

    /**
     * Returns the minimum x-coordinate among all member shapes.
     *
     * @return min x-coordinate
     */
    @Override
    public double getX() {
        if(allShapes.isEmpty()){
            return 0;
        }
        double minX = Double.MAX_VALUE;
        for (Shape s : allShapes) {
            minX = Math.min(minX, s.getX());
        }
        return minX;
    }

    /**
     * Returns the minimum y-coordinate among all member shapes.
     *
     * @return min y-coordinate
     */
    @Override
    public double getY() {
        double minY = Double.MAX_VALUE;
        for (Shape s : allShapes) {
            minY = Math.min(minY, s.getY());
        }
        return minY;
    }

    /**
     * Returns the internal list of shapes (modifiable).
     *
     * @return reference to the member list
     */
    public List<Shape> getShapes() {
        return allShapes;
    }
}
