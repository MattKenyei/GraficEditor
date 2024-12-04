package shapes;

import java.awt.*;

public class ShapeState {
    public String shapeType;
    public Color color;
    public int size;
    public int x;
    public int y;

    public ShapeState(Shape shape) {
        this.shapeType = shape.getClass().getSimpleName();
        this.color = shape.color;
        this.size = shape.size;
        this.x = shape.x;
        this.y = shape.y;
    }

    public void restore(Shape shape) {
        shape.setColor(color);
        shape.resize(size);
        shape.x = x;
        shape.y = y;
    }
}
