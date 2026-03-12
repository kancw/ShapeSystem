package clevis.model;

/**
 * Represents a circle shape with a center and radius.
 */
public class Circle extends Shape {

    /**
     * The x-coordinate of the circle's center.
     */
    private double centerX;
    /**
     * The y-coordinate of the circle's center.
     */
    private double centerY;
    /**
     * The radius of the circle.
     */
    private double r;

    /**
     * Constructs a Circle object.
     *
     * @param name     the name of the circle
     * @param centerX  the x-coordinate of the center
     * @param centerY  the y-coordinate of the center
     * @param radius   the radius of the circle
     * @param zOrder   the rendering order
     */
    public Circle(String name, double centerX, double centerY, double radius, int zOrder){
        super(name, zOrder); 
        this.setCenterX(centerX);
        this.setCenterY(centerY);
        this.setR(radius);
    }

    /**
     * Moves the circle by the given delta values.
     *
     * @param dx amount to move along the x-axis
     * @param dy amount to move along the y-axis
     */
    @Override
    public void move(double dx, double dy){ 
        setCenterX(getCenterX() + dx);
        setCenterY(getCenterY() + dy);
    }

    /**
     * Returns the bounding box of the circle.
     *
     * @return an array [minX, minY, maxX, maxY]
     */
    @Override
    public double[] getBoundingBox(){
        return new double[]{getCenterX() - getR(), getCenterY() - getR(), 2* getR(), 2* getR()};
    }
    private static final double boundary = 0.05;

    /**
     * Checks whether the circle covers the given point.
     * Currently tests if the point lies on the circumference within a tolerance.
     *
     * @param px x-coordinate of the point
     * @param py y-coordinate of the point
     * @return true if the point lies on the circle's edge
     */
    @Override
    public boolean coverPoint(double px, double py){
        double dist = Math.sqrt(Math.pow(px - getCenterX(), 2) + Math.pow(py - getCenterY(), 2));
        return Math.abs(dist - getR()) < boundary;
    }

    /**
     * Returns a textual description of the circle.
     *
     * @return formatted description string
     */
    @Override
    public String describe(){
        return String.format("Circle %s: center=(%.2f,%.2f), r=%.2f", name, getCenterX(), getCenterY(), getR());
    }

    /** @return x-coordinate of the circle's center */
    @Override
    public double getX() { 
        return getCenterX();
    }

    /** @return y-coordinate of the circle's center */
    @Override
    public double getY() { 
        return getCenterY();
    }

    /** @return x-coordinate of the circle's center */
    public double getCenterX() {
        return centerX;
    }

    /**
     * Sets the x-coordinate of the circle's center.
     *
     * @param centerX the new x-coordinate of the center
     */
    public void setCenterX(double centerX) {
        this.centerX = centerX;
    }

    /** @return radius of the circle */
    public double getR() {
        return r;
    }

    /**
     * Sets the radius of the circle.
     *
     * @param r the new radius
     */
    public void setR(double r) {
        this.r = r;
    }

    /**
     * Returns the y-coordinate of the circle's center.
     *
     * @return the y-coordinate of the center
     */
    public double getCenterY() {
        return centerY;
    }

    /**
     * Sets the y-coordinate of the circle's center.
     *
     * @param centerY the new y-coordinate of the center
     */
    public void setCenterY(double centerY) {
        this.centerY = centerY;
    }
}
