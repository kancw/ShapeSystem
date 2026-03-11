package clevis.model.command;

import clevis.model.Shape;

/**
 * MoveCommand represents a command that moves a Shape
 * by a specified change in the X and Y directions.
 * It supports undo/redo operations:
 * - redo(): moves the shape forward by (changeX, changeY)
 * - undo(): moves the shape back by (-changeX, -changeY)
 * This is part of the Command design pattern, enabling
 * flexible undo/redo functionality in the CLEVIS system.
 */
public class MoveCommand implements Command {
    private final Shape shape;
    private final double changeX;
    private final double changeY;

    /**
     * Constructs a MoveCommand with the given shape and displacement
     * @param shape the shape to be moved
     * @param changeX the displacement along the x axis
     * @param changeY the displacement along the y axis
     */
    public MoveCommand(Shape shape, double changeX, double changeY) {
        this.shape = shape;
        this.changeX = changeX;
        this.changeY = changeY;
    }

    /**
     * Redo the move by applying the displacement (changeX, changeY).
     */
    @Override
    public void redo() {
        shape.move(changeX, changeY);
    }

    /**
     * Undo the move by applying the opposite displacement (-changeX, -changeY).
     */
    @Override
    public void undo() {
        shape.move(-changeX, -changeY);
    }
}
