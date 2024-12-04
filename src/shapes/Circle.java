package shapes;

import java.awt.Color;
import java.awt.Graphics;

public class Circle extends Shape {
    public Circle(Color color, int size, int x, int y) {
        super(color, size, x, y);
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(color);
        g.fillOval(x, y, size, size);
    }
}