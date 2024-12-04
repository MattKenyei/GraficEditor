package shapes;

import java.awt.Color;
import java.awt.Graphics;

public class Triangle extends Shape {
    public Triangle(Color color, int size, int x, int y) {
        super(color, size, x, y);
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(color);
        int[] xPoints = {x, x + size / 2, x - size / 2};
        int[] yPoints = {y, y + size, y + size};
        g.fillPolygon(xPoints, yPoints, 3);
    }
}
