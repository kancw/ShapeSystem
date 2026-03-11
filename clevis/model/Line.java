package clevis.model;

/**
 * Represents a line segment defined by two endpoints.
 * A line can be moved, described, and tested for intersection
 * with other shapes.
 */
public class Line extends Shape {

    /** x-coordinate of the first endpoint */
    private double x;
    /** y-coordinate of the first endpoint */
    private double y;
    /** x-coordinate of the second endpoint */
    private double x2;
    /** y-coordinate of the second endpoint */
    private double y2;
    /** cover point number */
    private static final double EDGE_TOLERANCE = 0.05;

    /**
     * Constructs a Line object.
     *
     * @param name   the name of the line
     * @param x      x-coordinate of the first endpoint
     * @param y      y-coordinate of the first endpoint
     * @param x2     x-coordinate of the second endpoint
     * @param y2     y-coordinate of the second endpoint
     * @param zOrder rendering order
     */
    public Line(String name, double x, double y, double x2, double y2, int zOrder) {
        super(name, zOrder);
        this.setX(x);
        this.setY(y);
        this.setX2(x2);
        this.setY2(y2);
    }

    /**
     * Moves the line by the given delta values.
     *
     * @param dx amount to move along the x-axis
     * @param dy amount to move along the y-axis
     */
    @Override
    public void move(double dx, double dy){
        setX(getX() + dx);
        setY(getY() + dy);
        setX2(getX2() + dx);
        setY2(getY2() + dy);
    }

    /**
     * Returns the bounding box of the line segment.
     *
     * @return an array [minX, minY, maxX, maxY]
     */
    @Override
    public double[] getBoundingBox(){
        double minX = Math.min(getX(), getX2());
        double minY = Math.min(getY(), getY2());
        double width = Math.abs(getX2() - getX());
        double height = Math.abs(getY2() - getY());
        return new double[]{minX, minY, width, height};
    }

    /**
     * Checks whether the line covers the given point.
     *
     * @param px x-coordinate of the point
     * @param py y-coordinate of the point
     * @return true if the point lies on the line segment, false otherwise
     */
    @Override
    public boolean coverPoint(double px, double py){
        double dx = x2 - x;
        double dy = y2 - y;
        double length = Math.sqrt(dx*dx + dy*dy);

        double distance = Math.abs((px - x) * dy - (py - y) * dx) / length;

        double dot = (px - x) * dx + (py - y) * dy;
        if (dot < 0 || dot > dx*dx + dy*dy) {
            return false;
        }

        return distance < EDGE_TOLERANCE;
    }

    /**
     * Checks whether this line intersects with another shape.
     *
     * @param other the other shape
     * @return true if the line intersects with the other shape
     */
    @Override
    public boolean intersect(Shape other) {
        if (other instanceof Line) {
            Line line2 = (Line) other;

            double d1 = (line2.getX() - getX()) * (getY2() - getY()) - (line2.getY() - getY()) * (getX2() - getX());
            double d2 = (line2.getX2() - getX()) * (getY2() - getY()) - (line2.getY2() - getY()) * (getX2() - getX());
            double d3 = (getX() - line2.getX()) * (line2.getY2() - line2.getY()) - (getY() - line2.getY()) * (line2.getX2() - line2.getX());
            double d4 = (getX2() - line2.getX()) * (line2.getY2() - line2.getY()) - (getY2() - line2.getY()) * (line2.getX2() - line2.getX());

            return (d1 * d2 < 0) && (d3 * d4 < 0);
        } else {
            double[] b1 = this.getBoundingBox();
            double[] b2 = other.getBoundingBox();
            return (b1[0] < b2[0] + b2[2] &&
                    b2[0] < b1[0] + b1[2] &&
                    b1[1] < b2[1] + b2[3] &&
                    b2[1] < b1[1] + b1[3]);
        }
    }

    /**
     * Returns a textual description of the line.
     *
     * @return formatted description string
     */
    @Override
    public String describe() {
        return String.format("Line %s: (%.2f,%.2f) -> (%.2f,%.2f)", name, x, y, x2, y2);
    }

    /**
     * Returns the minimum x-coordinate of the line.
     *
     * @return min x-coordinate
     */
    @Override
    public double getX() {
        return Math.min(x, getX2());
    }

    /**
     * Returns the minimum y-coordinate of the line.
     *
     * @return min y-coordinate
     */
    @Override
    public double getY() {
        return Math.min(y, getY2());
    }

    /** @return x-coordinate of the first endpoint */
    public double getX1() { return x; }
    /** @return y-coordinate of the first endpoint */
    public double getY1() { return y; }
    /** @return x-coordinate of the second endpoint */
    public double getX2() { return x2; }
    /** @return y-coordinate of the second endpoint */
    public double getY2() { return y2; }

    /**
     * return x value
     * @param x the starting of the line in x coordination
     */
    public void setX(double x) {
        this.x = x;
    }

    /**
     * return y value
     * @param y the starting of the line in y coordination
     */
    public void setY(double y) {
        this.y = y;
    }

    /**
     * return x2 value
     * @param x2 the ending of the line in x coordination
     */
    public void setX2(double x2) {
        this.x2 = x2;
    }

    /**
     * return y2 value
     * @param y2 the ending of the line in y coordination
     */
    public void setY2(double y2) {
        this.y2 = y2;
    }
}
