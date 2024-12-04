package shapes;

import java.awt.Color;
import java.awt.Graphics;

public class Rectangle extends Shape {
    public Rectangle(Color color, int size, int x, int y) {
        super(color, size, x, y);
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(color);
        g.fillRect(x, y, size, size+20);
    }
}