package clevis.model;

/**
 * Represents a rectangle shape defined by its top-left corner,
 * width, and height.
 */
public class Rectangle extends Shape {

    /** x-coordinate of the top-left corner */
    private double x;
    /** y-coordinate of the top-left corner */
    private double y;
    /** width of the rectangle */
    private double width;
    /** height of the rectangle */
    private double height;
    /** cover number number */
    private static final double EDGE_TOLERANCE = 0.05;

    /**
     * Constructs a Rectangle object.
     *
     * @param name   the name of the rectangle
     * @param x      x-coordinate of the top-left corner
     * @param y      y-coordinate of the top-left corner
     * @param width  width of the rectangle
     * @param height height of the rectangle
     * @param zOrder rendering order
     */
    public Rectangle(String name, double x, double y, double width, double height, int zOrder) {
        super(name, zOrder);
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    /**
     * Returns the bounding box of the rectangle.
     *
     * @return an array [minX, minY, maxX, maxY]
     */
    @Override
    public double[] getBoundingBox() {
        return new double[]{x, y, width, height};
    }

    /**
     * Moves the rectangle by the given delta values.
     *
     * @param dx amount to move along the x-axis
     * @param dy amount to move along the y-axis
     */
    @Override
    public void move(double dx, double dy) {
        x += dx; 
        y += dy;
    }

    /**
     * Checks whether the rectangle covers the given point.
     *
     * @param px x-coordinate of the point
     * @param py y-coordinate of the point
     * @return true if the point lies inside the rectangle, false otherwise
     */
    @Override
    public boolean coverPoint(double px, double py) {
        double distLeft   = Math.abs(px - x);
        double distRight  = Math.abs(px - (x + width));
        double distTop    = Math.abs(py - y);
        double distBottom = Math.abs(py - (y + height));

        if (px >= x && px <= x + width && py >= y && py <= y + height) {
            double minDist = Math.min(Math.min(distLeft, distRight), Math.min(distTop, distBottom));
            return minDist < EDGE_TOLERANCE;
        }
        return false;
    }

    /**
     * Checks whether this rectangle intersects with another shape.
     *
     * @param other the other shape
     * @return true if the bounding boxes overlap
     */
    @Override
    public boolean intersect(Shape other) {
        double[] b1 = this.getBoundingBox();
        double[] b2 = other.getBoundingBox();
        return (b1[0] < b2[0] + b2[2] &&
                b2[0] < b1[0] + b1[2] &&
                b1[1] < b2[1] + b2[3] &&
                b2[1] < b1[1] + b1[3]);
    }

    /**
     * Returns a textual description of the rectangle.
     *
     * @return formatted description string
     */
    @Override
    public String describe() {
        return String.format("Rectangle %s: topLeft=(%.2f,%.2f), w=%.2f, h=%.2f", name, x, y, width, height);
    }

    /**
     * Returns the width of the rectangle.
     *
     * @return width
     */
    public double getWidth() {
        return width;
    }

    /**
     * Returns the height of the rectangle.
     *
     * @return height
     */
    public double getHeight() {
        return height;
    }

    /**
     * Returns the x-coordinate of the top-left corner.
     *
     * @return x-coordinate
     */
    @Override
    public double getX() {
        return x;
    }

    /**
     * Returns the y-coordinate of the top-left corner.
     *
     * @return y-coordinate
     */
    @Override
    public double getY() {
        return y;
    }
}
