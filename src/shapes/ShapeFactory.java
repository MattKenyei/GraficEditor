// shapes/ShapeFactory.java
package shapes;

import java.awt.Color;

public class ShapeFactory {
    public static Shape createShape(String type, Color color, int size, int x, int y) {
        switch (type.toLowerCase()) {
            case "circle":
                return new Circle(color, size, x, y);
            case "rectangle":
                return new Rectangle(color, size, x, y);
            case "triangle":
                return new Triangle(color, size, x, y);
            case "square":
                return new Square(color, size, x, y);
            default:
                throw new IllegalArgumentException("Unknown shape type");
        }
    }
}
