package clevis.model.command;

import clevis.model.Shape;
import clevis.model.ShapeManager;

import java.util.ArrayList;
import java.util.List;

/**
 * DeleteCommand represents a command that deletes one or more shapes
 * from the ShapeManager.
 * It supports undo/redo operations:
 * - undo(): restores all deleted shapes back into the manager
 * - redo(): removes the shapes again from the manager
 * This is part of the Command design pattern, enabling
 * flexible undo/redo functionality in the CLEVIS system.
 */
public class DeleteCommand implements Command {

    private final ShapeManager manager;
    private final List<Shape> allDeletedShapes;

    /**
     * Constructs a DeleteCommand with the given manager and shapes.
     *
     * @param manager the ShapeManager that manages all shapes
     * @param shapes  the list of shapes to be deleted
     */
    public DeleteCommand(ShapeManager manager, List<Shape> shapes) {

        this.manager = manager;
        this.allDeletedShapes = new ArrayList<>(shapes);
    }

    /**
     * Undo the deletion by restoring all shapes back into the manager.
     * Shapes are added in reverse order to preserve their original stacking.
     */
    @Override
    public void undo() {
        for (int i = allDeletedShapes.size() - 1; i >= 0; i--) {
            Shape s = allDeletedShapes.get(i);
            manager.addShapeDirectly(s);
        }
    }

    /**
     * Redo the deletion by removing all shapes again from the manager.
     */
    @Override
    public void redo() {
        for (Shape s : allDeletedShapes) {
            manager.removeShape(s);
        }
    }
}