package clevis.model.command;

import clevis.model.Group;
import clevis.model.Shape;
import clevis.model.ShapeManager;

import java.util.ArrayList;
import java.util.List;

/**
 * GroupCommand represents a command that groups multiple shapes
 * into a single Group object managed by the ShapeManager.
 * It supports undo/redo operations:
 * - undo(): removes the group and restores individual shapes
 * - redo(): removes the individual shapes and re-adds the group
 * This is part of the Command design pattern, enabling
 * flexible undo/redo functionality in the CLEVIS system.
 */
public class GroupCommand implements Command {

    private final ShapeManager manager;
    private final Group group;   
    private final List<Shape> members;

    /**
     * Constructs a GroupCommand with the given manager, group, and member shapes.
     *
     * @param manager the ShapeManager that manages all shapes
     * @param group   the Group object representing the collection of shapes
     * @param members the list of shapes to be grouped
     */
    public GroupCommand(ShapeManager manager, Shape group, List<Shape> members) {
        this.manager = manager;
        this.group = (Group) group;
        this.members = new ArrayList<>(members);
    }

    /**
     * Undo the grouping by removing the group and restoring
     * all individual member shapes back into the manager.
     */
    @Override
    public void undo() {
        manager.removeShape(group);
        for (Shape s : members) {
            manager.addShapeDirectly(s);
        }
    }

    /**
     * Redo the grouping by removing individual shapes
     * and re-adding the group into the manager.
     */
    @Override
    public void redo() {
        for (Shape s : members) {
            manager.removeShape(s);
        }
        manager.addShapeDirectly(group);
    }
}