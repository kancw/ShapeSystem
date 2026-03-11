package clevis.model.command;

/**
 * The Command interface defines the contract for
 * implementing the Command design pattern.
 * Each command should support:
 * - undo(): Reversing the effect of the command
 * - redo(): Reapplying the command after it has been undone
 * This allows flexible control of user actions,
 * such as in applications with undo/redo functionality.
 */
public interface Command {
    /**
     * Undo the action performed by this command.
     * Implementations should restore the system state
     * to what it was before the command was executed.
     */
    void undo();

    /**
     * Redo the action performed by this command.
     * Implementations should reapply the command
     * after it has been undone.
     */
    void redo();
}