package clevis.model;

/**
 * Represents a square shape, that is extanded from rectangle file.
 */
public class Square extends Rectangle {

    /**
     * Constructs a Square object.
     * @param name   the name of the square
     * @param x      the x-coordinate of the top-left corner
     * @param y      the y-coordinate of the top-left corner
     * @param length the length of each side
     * @param zOrder the z-order for rendering
     */
    public Square(String name, double x, double y, double length, int zOrder) {
        super(name, x, y, length, length, zOrder);
    }

    /**
     * Returns a string description of the square.
     */
    @Override
    public String describe() {
        return String.format("Square %s: topLeft=(%.2f,%.2f), side=%.2f", name, getBoundingBox()[0], getBoundingBox()[1], getBoundingBox()[2]);
    }
}
