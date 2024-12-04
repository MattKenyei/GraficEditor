package shapes;

import java.awt.Color;
import java.awt.Graphics;

public class Oval extends Shape {
    public Oval(Color color, int size, int x, int y) {
        super(color, size, x, y);
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(color);
        g.fillOval(x, y, size, size / 2); // Размер овал не равен размеру по вертикали
    }
}
