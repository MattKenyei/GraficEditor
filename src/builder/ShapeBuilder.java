// builder/ShapeBuilder.java
package builder;

import shapes.Shape;
import shapes.ShapeFactory;

import java.awt.Color;

public class ShapeBuilder {
    private String type;
    private Color color;
    private int size;
    private int x;
    private int y;

    public ShapeBuilder setType(String type) {
        this.type = type;
        return this;
    }

    public ShapeBuilder setColor(Color color) {
        this.color = color;
        return this;
    }

    public ShapeBuilder setSize(int size) {
        this.size = size;
        return this;
    }

    public ShapeBuilder setPosition(int x, int y) {
        this.x = x;
        this.y = y;
        return this;
    }

    public Shape build() {
        return ShapeFactory.createShape(type, color, size, x, y);
    }
}
