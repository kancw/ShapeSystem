package clevis.model;

/**
 * Abstract base class for all shapes in Clevis.
 * Each shape has a name and a z-order (rendering priority).
 */
public abstract class Shape {
    /**
     * The unique name of the shape.
     */
    protected String name;
    /**
     * The z-order (rendering priority) of the shape.
     * Higher values mean the shape is drawn later (on top).
     */
    protected int zOrder;

    /**
     * Constructs a Shape with the given name and z-order.
     * @param name   the unique name of the shape
     * @param zOrder the rendering order
     */
    public Shape(String name, int zOrder){
        this.name = name;
        this.zOrder = zOrder;
    }

    /**
     * Moves the shape by the given delta values.
     *
     * @param dx the amount to move along the x-axis
     * @param dy the amount to move along the y-axis
     */
    public abstract void move(double dx, double dy);

    /**
     * Returns the bounding box of the shape.
     *
     * @return an array [minX, minY, maxX, maxY] representing the bounding box
     */
    public abstract double[] getBoundingBox();

    /**
     * Returns a textual description of the shape.
     *
     * @return formatted description string
     */
    public abstract String describe();

    /**
     * Checks whether this shape covers the given point.
     *
     * @param x the x-coordinate of the point
     * @param y the y-coordinate of the point
     * @return true if the point lies within the shape, false otherwise
     */
    public abstract boolean coverPoint(double x, double y);

    /**
     * Returns the z-order of this shape.
     *
     * @return the z-order value (higher means drawn later)
     */
    public int getZOrder() {
        return zOrder;
    }

    /**
     * Returns the name of this shape.
     *
     * @return the shape's name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the x-coordinate anchor of the shape.
     *
     * @return the x-coordinate
     */
    public abstract double getX();

    /**
     * Returns the y-coordinate anchor of the shape.
     *
     * @return the y-coordinate
     */
    public abstract double getY();
}