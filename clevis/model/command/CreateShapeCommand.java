package clevis.model.command;

import clevis.model.Shape;
import clevis.model.ShapeManager;

/**
 * CreateShapeCommand represents a command to create a new Shape
 * and add it to the ShapeManager.
 * It supports undo/redo operations
 * This is part of the Command design pattern, enabling
 * flexible undo/redo functionality in the CLEVIS system.
 */
public class CreateShapeCommand implements Command {
    private final ShapeManager manager;
    private final Shape shape;

    /**
     * Constructs a CreateShapeCommand with the given manager and shape.
     *
     * @param manager the ShapeManager that manages all shapes
     * @param shape   the Shape to be created
     */
    public CreateShapeCommand(ShapeManager manager, Shape shape) {
        this.manager = manager;
        this.shape = shape;
    }

    /**
     * Undo the creation of the shape by removing it
     * from the ShapeManager.
     */
    @Override
    public void undo() {
        manager.removeShape(shape);
    }

    /**
     * Redo the creation of the shape by re-adding it
     * directly to the ShapeManager.
     */
    @Override
    public void redo() {
        manager.addShapeDirectly(shape);
    }
}
