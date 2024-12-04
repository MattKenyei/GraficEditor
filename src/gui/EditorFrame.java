package gui;

import shapes.*;
import builder.ShapeBuilder;
import shapes.Rectangle;
import shapes.Shape;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Stack;

public class EditorFrame extends JFrame {
    private ArrayList<Shape> shapes;
    private Color currentColor = Color.RED;
    private int currentSize = 50;
    private Shape selectedShape = null; // Фигура, которую мы перемещаем
    private Point dragStartPoint; // Начальная точка перетаскивания
    private Stack<ShapeState> undoStack; // Стек для отмены действий
    private boolean isResizing = false;

    public EditorFrame() {
        shapes = new ArrayList<>();
        setTitle("Graphic Editor");
        undoStack = new Stack<>();
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setFocusable(true);
        requestFocusInWindow();

        // Панель с кнопками
        JPanel controlPanel = new JPanel();
        JButton createCircleButton = new JButton("Create Circle");
        JButton createRectangleButton = new JButton("Create Rectangle");
        JButton createSquareButton = new JButton("Create Square");
        JButton createTriangleButton = new JButton("Create Triangle");
        JButton changeColorButton = new JButton("Change Color");
        JButton resizeButton = new JButton("Resize");
        JButton undoButton = new JButton("Undo");

        createCircleButton.addActionListener(e -> addShape("circle"));
        createRectangleButton.addActionListener(e -> addShape("rectangle"));
        createSquareButton.addActionListener(e -> addShape("square"));
        createTriangleButton.addActionListener(e -> addShape("triangle"));

        changeColorButton.addActionListener(e -> changeCurrentColor());
        resizeButton.addActionListener(e -> resizeCurrentShapes());
        undoButton.addActionListener(e -> undoLastAction());

        controlPanel.add(createCircleButton);
        controlPanel.add(createRectangleButton);
        controlPanel.add(createTriangleButton);
        controlPanel.add(createSquareButton);
        controlPanel.add(changeColorButton);
        controlPanel.add(resizeButton);
        controlPanel.add(undoButton);

        add(controlPanel, BorderLayout.NORTH);

        // Обработчик для выделения и перемещения фигур
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                // Проверка, кликнули ли мы на фигуру
                for (Shape shape : shapes) {
                    if (isClickInsideShape(shape, e.getX(), e.getY())) {
                        if (selectedShape != null) {
                            undoStack.push(new ShapeState(selectedShape)); // Сохраняем состояние для отмены
                        }
                        selectedShape = shape; // Запоминаем выбранную фигуру
                        // Устанавливаем новый цвет для выбранной фигуры
                        selectedShape.setColor(currentColor);
                        dragStartPoint = e.getPoint(); // Запомнить начальную точку перетаскивания
                        repaint(); // Перерисовываем, чтобы обновить цвет
                        if (e.getX() >= selectedShape.x + selectedShape.size - 10 &&
                                e.getY() >= selectedShape.y + selectedShape.size - 10) {
                            isResizing = true;
                        }
                        break;
                    }
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                //selectedShape = null; // Сбрасываем, когда мышь отпущена
                isResizing = false;
            }
        });

        this.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                // Перемещение фигуры, если она выбрана
                if (selectedShape != null) {
                    if (isResizing) {
                        undoStack.push(new ShapeState(selectedShape)); // Сохраняем состояние для отмены
                        int newSize = Math.max(10, e.getX() - selectedShape.x); // Меняем размер по X
                        if (newSize > 0) {
                            selectedShape.resize(newSize); // Изменяем размер фигуры
                        }
                    } else { // Перемещение фигуры
                        undoStack.push(new ShapeState(selectedShape));
                        int deltaX = e.getX() - selectedShape.x - selectedShape.size / 2;
                        int deltaY = e.getY() - selectedShape.y - selectedShape.size / 2;
                        selectedShape.move(deltaX, deltaY); // Перемещаем фигуру
                        dragStartPoint = e.getPoint(); // Обновляем базовую точку
                    }
                    repaint(); // Перерисовываем для отображения изменений
                }
            }
        });

        // Обработчик для клавиатурных событий
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_Z) {
                    undoLastAction(); // Вызываем метод для отмены
                }
            }
        });
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                requestFocusInWindow();
            }
        });
    }
    private void undoLastAction() {
        if (!undoStack.isEmpty()) {
            ShapeState lastState = undoStack.pop(); // Извлекаем последнее состояние

            // Находим фигуру для восстановления
            for (Shape shape : shapes) {
                if (shape.getClass().getSimpleName().equals(lastState.shapeType) &&
                        shape.x == lastState.x && shape.y == lastState.y) {
                    lastState.restore(shape); // Восстанавливаем фигуру из состояния
                    break;
                }
            }

            repaint(); // Перерисовываем для обновления графики
        }
    }

    private void addShape(String type) {
        ShapeBuilder builder = new ShapeBuilder();
        int x = (getWidth() - currentSize) / 2;
        int y = (getHeight() - currentSize) / 2;
        Shape shape = builder.setType(type)
                .setColor(currentColor)
                .setSize(currentSize)
                .setPosition(x, y)
                .build();
        shapes.add(shape);
        undoStack.push(new ShapeState(shape));
        repaint();
    }

    private void changeCurrentColor() {
        Color newColor = JColorChooser.showDialog(this, "Choose a color", currentColor);
        if (newColor != null) {
            currentColor = newColor; // Обновление текущего цвета
        }
    }

    private void resizeCurrentShapes() {
        if (selectedShape == null) {
            JOptionPane.showMessageDialog(this, "No shape selected to resize!");
            return;
        }
        undoStack.push(new ShapeState(selectedShape));
        String input = JOptionPane.showInputDialog(this, "Enter new size (integer):", selectedShape.size);
        try {
            int newSize = Integer.parseInt(input);
            if (newSize > 0) { // Проверка на положительный размер
                selectedShape.resize(newSize); // Изменение размера только выбранной фигуры
                repaint();
            } else {
                JOptionPane.showMessageDialog(this, "Size must be positive!");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid size input!");
        }
    }

    private boolean isClickInsideShape(Shape shape, int mouseX, int mouseY) {
        // Проверяем, находится ли клик внутри фигуры
        if (shape instanceof Circle) {
            int radius = shape.size / 2;
            int centerX = shape.x + radius;
            int centerY = shape.y + radius;
            return (Math.pow(mouseX - centerX, 2) + Math.pow(mouseY - centerY, 2)) <= Math.pow(radius, 2);
        } else if (shape instanceof Rectangle || shape instanceof Square) {
            return mouseX >= shape.x && mouseX <= (shape.x + shape.size) &&
                    mouseY >= shape.y && mouseY <= (shape.y + shape.size);
        } else if (shape instanceof Triangle) {
            int[] xPoints = {shape.x, shape.x + shape.size / 2, shape.x - shape.size / 2};
            int[] yPoints = {shape.y, shape.y + shape.size, shape.y + shape.size};
            Polygon triangle = new Polygon(xPoints, yPoints, 3);
            return triangle.contains(mouseX, mouseY);
        }
        return false;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        for (Shape shape : shapes) {
            shape.draw(g);
        }
    }
}
