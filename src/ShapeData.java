import java.awt.*;

class ShapeData {
    Shape shape;
    Color color;
    boolean fill;
    Stroke stroke;
    boolean dotted;

    public ShapeData(Shape shape, Color color, boolean fill , boolean dotted) {
        this(shape, color, fill,dotted , new BasicStroke(1)); // set a default stroke we only change it for eraser and pencil
        this.shape = shape;
        this.color = color;
        this.fill = fill;
        this.dotted = dotted;
    }

    public ShapeData(Shape shape, Color color, boolean fill, boolean dotted, Stroke stroke) {
        this.shape = shape;
        this.color = color;
        this.fill = fill;
        this.dotted = dotted;
        this.stroke = stroke;
    }
}