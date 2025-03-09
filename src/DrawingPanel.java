import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.lang.Math;

class DrawingPanel extends JPanel {
    private String selectedTool = "Pencil";
    private Color selectedColor = Color.BLACK;
    private boolean fillMode = false;
    private boolean dottedmode = false;
    private boolean Clear_used = false ;
    private int startX, startY, endX, endY;
    private Shape temporaryShape;
    private int strokeWidth;
    private final List<ShapeData>  shapes = new ArrayList<>(); //Creating arraylist of object ShapeData
    private final List<ShapeData> undoStack = new ArrayList<>(); //same but for the redo and undo
    private final List<ShapeData> ClearStack = new ArrayList<>();
    private BufferedImage canvasImage;
    private BufferedImage TempcanvasImage;

    public DrawingPanel() {
        setBackground(Color.WHITE);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                startX = e.getX();
                startY = e.getY();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (isShapeTool()) {
                    endX = e.getX();
                    endY = e.getY();
                    addShape();
                    temporaryShape = null;
                    repaint();
                }
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (isShapeTool()) {
                    endX = e.getX();
                    endY = e.getY();
                    temporaryShape = createShape(startX, startY, endX, endY);
                    repaint();
                } else if (selectedTool.equals("Pencil") || selectedTool.equals("Eraser")) {
                    int currentX = e.getX();
                    int currentY = e.getY();
                    addFreehandShape(currentX, currentY);
                    startX = currentX;
                    startY = currentY;
                    repaint();
                }
            }
        });
    }

    private boolean isShapeTool() {
        return selectedTool.equals("Rectangle") || selectedTool.equals("Oval")
                || selectedTool.equals("Triangle") || selectedTool.equals("Line");
    }

    private void addFreehandShape(int currentX, int currentY){
        Color color ;
        if(selectedTool.equals("Eraser")){
            color=Color.WHITE;
        }
        else
        {
            color = selectedColor;
            strokeWidth=2; // for pencil
        }
        Stroke stroke = new BasicStroke(strokeWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
        Shape line = new Line2D.Float(startX, startY, currentX, currentY);
        shapes.add(new ShapeData(line, color, false,false, stroke));//we add the eraser , pencil to the list here because we
        //call the method in the mouse drag listener
    }

    private Shape createShape(int x1, int y1, int x2, int y2) {
        switch (selectedTool) {
            case "Rectangle": return new Rectangle(Math.min(x1,x2), Math.min(y1,y2), Math.abs(x2-x1), Math.abs(y2-y1));
            case "Oval": return new Ellipse2D.Float(Math.min(x1,x2),Math.min(y1,y2), Math.abs(x2-x1), Math.abs(y2-y1));
            case "Line": return new Line2D.Float(x1, y1, x2, y2);
            case "Triangle": {
                Polygon triangle = new Polygon();
                triangle.addPoint(x1, y2);
                triangle.addPoint((x1 + x2)/2, y1);
                triangle.addPoint(x2, y2);
                return triangle;
            }
            default: return null;
        }
    }

    private void addShape() { //add to the list of shapes to be drawn everytime the repaint method is called
        Shape shape = createShape(startX, startY, endX, endY);
        shapes.add(new ShapeData(shape, selectedColor, fillMode , dottedmode));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (canvasImage != null) {
            g.drawImage(canvasImage, 0, 0, this); // Draw the loaded image
        }
        Graphics2D g2d = (Graphics2D) g;
        //this enhanced lop for drawing the shapes every time the repaint function is called so we can save the shapes on the app
        for (ShapeData shapeData : shapes) {
            g2d.setColor(shapeData.color);
            g2d.setStroke(shapeData.stroke);
            if(shapeData.dotted)
                g2d.setStroke(new BasicStroke(1,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND,10f,new float[] {5f, 5f}, 0f));
            if (shapeData.fill) {
                g2d.fill(shapeData.shape);
            } else {
                g2d.draw(shapeData.shape);
            }
        }
        //this loop for live preview of the drawing
        if (temporaryShape != null) {
            g2d.setColor(selectedColor);
            g2d.setStroke(new BasicStroke(1));
            if(dottedmode)
                g2d.setStroke(new BasicStroke(1,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND,10f,new float[] {5f, 5f}, 0f));
            else
                g2d.setStroke(new BasicStroke(1));
            if (fillMode) {
                g2d.fill(temporaryShape);
            } else {
                g2d.draw(temporaryShape);
            }
        }
    }

    public void setSelectedTool(String tool) {
        this.selectedTool = tool;
    }

    public void setSelectedColor(Color color) {
        this.selectedColor = color;
    }

    public void setFillMode(boolean fillMode) {
        this.fillMode = fillMode;
    }
    public void setStroke(int size){
        if(selectedTool.equals("Eraser")){
            strokeWidth = size ;
        }
    }

    public void clear() {
        TempcanvasImage=canvasImage;
        canvasImage = null;
        ClearStack.addAll(shapes);
        shapes.clear();
        Clear_used = true;
        repaint();
    }
    public void setDotted(boolean dotted){
        this.dottedmode = dotted;
    }
    public void undo() {
        if (!(shapes.isEmpty())) {
            undoStack.add(shapes.getLast());
            shapes.removeLast();
        }
        if(Clear_used ){
            shapes.addAll(ClearStack);
            Clear_used = false;
            ClearStack.clear();
        }
        if(TempcanvasImage != null){
            canvasImage = TempcanvasImage;
            temporaryShape=null;
        }
        repaint();
    }

    public void redo() {
        if (!(undoStack.isEmpty())) {
            shapes.add(undoStack.getLast());
            undoStack.removeLast();
            repaint();
        }
    }
    public BufferedImage getDrawingImage() {
        BufferedImage image = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        paintComponent(g2d);

        return image;
    }
    public void loadImage(BufferedImage image) {
        if (image != null) {
            // Create a new canvas matching the loaded image size
            canvasImage = new BufferedImage(
                    image.getWidth(),
                    image.getHeight(),
                    BufferedImage.TYPE_INT_ARGB
            );
            Graphics2D g2d = canvasImage.createGraphics();
            g2d.drawImage(image, 0, 0, null);
            g2d.dispose();
            repaint(); // Refresh the panel
        }
    }
}