package clevis.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for ShapeManager.
 * Each test verifies a specific feature of the CLEVIS system:
 * creation, movement, grouping, deletion, bounding boxes,
 * intersection, shape lookup, name conflicts, and z-order.
 */
public class ShapeManagerTest {

    private ShapeManager manager;

    /**
     * Initializes a fresh ShapeManager before each test.
     */
    @BeforeEach
    public void setUp() {
        manager = new ShapeManager();  
    }

    /**
     * Tests creation of shapes and verifies they appear in the list.
     */
    @Test
    public void testCreateAndListShapes() {
        manager.createRectangle("r1", 0, 0, 10, 10);
        manager.createCircle("c1", 5, 5, 3);
        manager.createLine("l1", 0, 0, 10, 10);
        manager.createSquare("s1", 20, 20, 5);

        String listAll = manager.listAll();
        assertTrue(listAll.contains("r1"));
        assertTrue(listAll.contains("c1"));
        assertTrue(listAll.contains("l1"));
        assertTrue(listAll.contains("s1"));
    }

    /**
     * Tests moving a shape and verifies undo/redo restores positions correctly.
     */
    @Test
    public void testMoveAndUndoRedo() {
        manager.createRectangle("r1", 0, 0, 10, 10);
        manager.move("r1", 20, 30);
        assertTrue(manager.listAll().contains("20.00"));
        assertTrue(manager.listAll().contains("30.00"));

        manager.undo();
        assertTrue(manager.listAll().contains("0.00"));

        manager.redo();
        assertTrue(manager.listAll().contains("20.00"));
    }

    /**
     * Tests grouping and ungrouping of shapes.
     */
    @Test
    public void testGroupAndUngroup() {
        // Group two shapes together
        manager.createRectangle("r1", 0, 0, 10, 10);
        manager.createCircle("c1", 15, 15, 5);
        manager.group("group g1 r1 c1");

        String all = manager.listAll();
        assertTrue(all.contains("g1"));
        assertTrue(all.contains("r1"));
        assertTrue(all.contains("c1"));

        manager.ungroup("g1");
        all = manager.listAll();
        assertTrue(all.contains("r1"));
        assertTrue(all.contains("c1"));
        assertFalse(manager.listAll().contains("g1"));
    }

    /**
     * Tests deletion of a shape and undo restoration.
     */
    @Test
    public void testDeleteAndUndo() {
        manager.createLine("l1", 0, 0, 10, 10);
        manager.delete("l1");
        assertFalse(manager.listAll().contains("l1"));

        manager.undo();
        assertTrue(manager.listAll().contains("l1"));
    }

    /**
     * Tests bounding box calculation and intersection detection.
     */
    @Test
    public void testBoundingBoxAndIntersect() {
        // Bounding box of rectangle should match expected coordinates
        manager.createRectangle("r1", 0, 0, 10, 10);
        manager.createCircle("c1", 5, 5, 3);

        double[] box = manager.getBoundingBox("r1");
        assertEquals(0.0, box[0], 0.01);
        assertEquals(10.0, box[2], 0.01);

        assertTrue(manager.intersect("r1", "c1"));
    }

    /**
     * Tests shape lookup at specific coordinates.
     */
    @Test
    public void testShapeAt() {
        // Check which shape is at given coordinates
        manager.createRectangle("r1", 0, 0, 20, 20);
        manager.createCircle("c1", 10, 10, 5);

        assertEquals("c1", manager.shapeAt(15, 10));
        assertEquals("r1", manager.shapeAt(0, 0));
        assertEquals(null, manager.shapeAt(100, 100));
    }

    /**
     * Tests that creating shapes with duplicate names throws an exception.
     */
    @Test
    public void testNameConflict() {
        manager.createRectangle("r1", 0, 0, 10, 10);
        assertThrows(IllegalArgumentException.class, () -> {
            manager.createRectangle("r1", 1, 1, 5, 5);
        });
    }

    /**
     * Tests z-order rendering (last created shape should be on top).
     */
    @Test
    public void testZOrder() {
        // Circle created after rectangle should appear on top
        manager.createRectangle("r1", 0, 0, 10, 10);
        manager.createCircle("c1", 0, 0, 5);
        assertEquals("c1", manager.shapeAt(5, 0));
    }
}