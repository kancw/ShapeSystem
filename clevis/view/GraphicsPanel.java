package clevis.view;

import clevis.model.*;
import clevis.model.Rectangle;
import clevis.model.Shape;
import java.awt.*;
import java.awt.geom.*;
import java.util.*;
import javax.swing.*;

@SuppressWarnings("ALL")
public class GraphicsPanel extends JPanel {
    private ShapeManager shapeManager;
    private double scale = 1.0;
    private double offsetX = 0;
    private double offsetY = 0;

    private static final int PANEL_WIDTH = 800;
    private static final int PANEL_HEIGHT = 600;
    private static final double MIN_SCALE = 0.1; 
    private static final double MAX_SCALE = 10.0;

    /**
     * Initialization
     * @param shapeManager importing shapeManager method
     */
    public GraphicsPanel(ShapeManager shapeManager) {
        this.shapeManager = shapeManager;
        
        setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        resetView();
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        Graphics2D g2d = (Graphics2D) g;
        AffineTransform originalTransform = g2d.getTransform();
        g2d.translate(offsetX, offsetY);
        g2d.scale(scale, -scale);

        Collection<Shape> allShapes = shapeManager.getAllShapes().values();

        for (Shape shape : allShapes) {
            drawShape(g2d, shape);
        }

        g2d.setTransform(originalTransform);
    }
    
    private void drawShape(Graphics2D g2d, Shape shape) {
        if (shape instanceof Rectangle) {
            drawRectangle(g2d, (Rectangle) shape);
        } else if (shape instanceof Circle) {
            drawCircle(g2d, (Circle) shape);
        } else if (shape instanceof Line) {
            drawLine(g2d, (Line) shape);
        } else if (shape instanceof Square) {
            drawSquare(g2d, (Square) shape);
        } else if (shape instanceof Group) {
            drawGroup(g2d, (Group) shape);
        }
    }
    private void drawRectangle(Graphics2D g2d, Rectangle rect) {
        double[] bbox = rect.getBoundingBox();
        double x = bbox[0];
        double y = bbox[1];
        double width = bbox[2];
        double height = bbox[3];
        
        Rectangle2D.Double r = new Rectangle2D.Double(x, y, width, height);
        
        g2d.draw(r);
    }

    private void drawCircle(Graphics2D g2d, Circle circle) {
        double[] bbox = circle.getBoundingBox();
        double x = bbox[0];
        double y = bbox[1];
        double diameter = bbox[2];
        
        Ellipse2D.Double ellipse = new Ellipse2D.Double(x, y, diameter, diameter);
        
        g2d.draw(ellipse);
    }

    private void drawLine(Graphics2D g2d, Line line) {
        Line2D.Double l = new Line2D.Double(
            line.getX1(),
            line.getY1(),
            line.getX2(),
            line.getY2()
        );
        g2d.draw(l);
    }

    private void drawSquare(Graphics2D g2d, Square square) {
        drawRectangle(g2d, square);
    }

    private void drawGroup(Graphics2D g2d, Group group) {
        for (Shape shape : group.getShapes()) {
            drawShape(g2d, shape);
        }
    }
    /**
     * Zoom in button design
     */
    public void zoomIn() {
        final double scaleNum = 1.2;
        if (scale < MAX_SCALE) {
            scale *= scaleNum;
            repaint();
        }
    }

    /**
     * Zoom out button design
     */
    public void zoomOut() {
        final double scaleNum = 1.2;
        if (scale > MIN_SCALE) {
            scale /= scaleNum;
            repaint();
            }
    }

    /**
     * Reset View button design
     */
    public void resetView() {
        final double half = 2.0;
        scale = 1.0;
        offsetX = PANEL_WIDTH / half;
        offsetY = PANEL_HEIGHT / half;
        repaint();
    }

    /**
     * Redrawing
     */
    public void refresh() {
        repaint();
    }
}