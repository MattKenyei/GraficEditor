package shapes;

import java.awt.Color;
import java.awt.Graphics;

public abstract class Shape {
    public Color color;
    public int size;
    public int x, y; // Позиция фигуры

    public Shape(Color color, int size, int x, int y) {
        this.color = color;
        this.size = size;
        this.x = x;
        this.y = y;
    }

    public abstract void draw(Graphics g);

    public void resize(int newSize) {
        this.size = newSize;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void move(int deltaX, int deltaY) {
        this.x += deltaX;
        this.y += deltaY;
    }
}
