import java.awt.*;
import java.awt.event.*;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;
import javax.swing.*;

public class Snake extends Screen implements ActionListener {

    static final int tileSize = 45;
    static final int rows = 16;
    static final int cols = 16;

    // Smart Enum for readability
    enum Direction {
        // Enum constants
        UP(0, -1),
        DOWN(0, 1),
        LEFT(-1, 0),
        RIGHT(1, 0);

        int dx, dy;

        Direction(int dx, int dy) {
            this.dx = dx;
            this.dy = dy;
        }

    };

    private class point {
        public int r, c;

        public point() {
            r = c = 0;
        }

        public point(int r, int c) {
            this.r = r;
            this.c = c;
        }

        public point(point x) {
            this.r = x.r;
            this.c = x.c;
        }

        public void set(int r, int c) {
            this.r = r;
            this.c = c;
        }

        public boolean equal(point x) {
            return this.r == x.r && this.c == x.c;
        }

        public point move(Direction dir) {
            point newPoint = new point(this);
            newPoint.r += dir.dy;
            newPoint.c += dir.dx;
            return newPoint;
        }

    }

    boolean started = false;

    int[][] grid = new int[rows][cols];

    LinkedList<point> snake = new LinkedList<>();

    LinkedList<point> flattened_grid = new LinkedList<>();

    Direction currentDirection = Direction.DOWN;

    int foodEaten = 0;

    point currentFoodPosition = new point();

    boolean gameOver = false;

    private class GamePanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            // Clear old drawing (default behaviour)
            super.paintComponent(g);
            // Draw new
            draw(g);
        }
    }

    // The canvas for drawing the snake
    GamePanel panel = new GamePanel();

    Snake() {

        super(rows * tileSize, cols * tileSize, "Esmail's Snake");

        // Take care of closing the window properly
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        // Set needed size for the panel
        panel.setPreferredSize(new Dimension(cols * tileSize, rows * tileSize));

        // Separated this concern for more redability
        configureInput();

        // Add the modified Panel
        frame.add(panel);

        // Expand frame to accomodate panel
        frame.pack();

        // Make it dark
        panel.setBackground(Color.BLACK);

        // Allow keyboard listening
        panel.setFocusable(true);

        // View
        frame.setVisible(true);

        Timer timer = new Timer(150, this);
        timer.start();

        setUp();

    }

    // Returns a random integer [0, lim-1] inclusive
    int pickRandomNumber(int lim) {
        Random rand = new Random();
        return rand.nextInt(lim);
    }

    point pickRandomPoint() {
        return new point(pickRandomNumber(rows), pickRandomNumber(cols));
    }

    // ======================== GAME LOGIC ======================== //

    /*
     * This is the function that is called everytime the timer is finished
     * 
     * Handles:
     * Updating snake movements
     * 
     * Detecing collisions:
     * Snake with itself
     * Snake with walls
     * Snake with food
     */
    @Override
    public void actionPerformed(ActionEvent e) {

        if (gameOver || !started)
            return;

        // Handle Collision
        point nextHead = snake.getFirst().move(currentDirection);

        // Collision with walls
        if (!check(nextHead)) {
            gameOver = true;
            System.out.println("Game Over, Hit the Walls!");
            return;
        }

        // Collision with self
        for (point p : snake) {
            if (p.equal(nextHead)) {
                gameOver = true;
                System.out.println("Game Over, Hit your tail!");
                return;
            }
        }

        // Collision with food
        if (nextHead.equal(currentFoodPosition)) {
            handleFoodCollision();
        }

        // Move
        move();

        // Repaint
        panel.repaint();
    }

    private void setUp() {

        // ===== Set Up Grid ===== //

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                flattened_grid.push(new point(r, c));
            }
        }

        // Choose a random position for the starting snake
        point head = pickRandomPoint();

        // Convention: Snake head is always snake[0]
        snake.addLast(head);

        Iterator<point> it = flattened_grid.iterator();

        // Delete the head from available ones
        while (it.hasNext()) {

            point p = it.next();

            if (p.equals(head)) {
                it.remove();
                break;
            }
        }
    }

    // Move snake 1 time
    private void move() {

        if (!started)
            return;

        // Move head (Add new head)
        point newHead = snake.getFirst().move(currentDirection);
        snake.addFirst(newHead);
        removeFromGrid(newHead);

        // delete tail
        point tail = snake.getLast();
        if (foodEaten > 0) {
            foodEaten--;
        } else {
            addToGrid(tail);
            snake.removeLast();
        }
    }

    private void removeFromGrid(point target) {
        Iterator<point> it = flattened_grid.iterator();

        while (it.hasNext()) {
            if (it.next().equal(target)) {
                it.remove();
                return;
            }
        }

    }

    private void addToGrid(point target) {
        flattened_grid.add(new point(target));
    }

    private void handleFoodCollision() {
        // Increment food eaten
        foodEaten++;
        // Regenerate New Food
        generateNewFood();
    }

    private void generateNewFood() {

        int idx = pickRandomNumber(flattened_grid.size());

        currentFoodPosition = new point(flattened_grid.get(idx));

        flattened_grid.remove(idx);
    }

    // Check the coordinates are valid
    boolean check(point x) {
        return (x.r < rows && x.r >= 0 && x.c < cols && x.c >= 0);
    }

    // ======================== DRAWING LOGIC ======================== //

    public void draw(Graphics g) {
        // drawGrid(g);

        // Draw the food
        drawPixel(g, currentFoodPosition.r, currentFoodPosition.c, Color.red);

        // Draw the snake
        for (point p : snake) {
            drawPixel(g, p.r, p.c, Color.green);
        }
    }

    // Responsible for drawing a single rectangle on the canvas
    public void drawPixel(Graphics g, int r, int c, Color color) {

        // Set the drawing color
        g.setColor(color);

        // Draw the rect
        g.fillRect(c * tileSize, r * tileSize, tileSize, tileSize);

    }

    // Responsible for drawing the grid on the canvas
    public void drawGrid(Graphics g) {
        /*
         * 
         * Drawing a line:
         * g.drawLine(x1, y1, x2, y2)
         */

        // ===== Draw Grid ===== //

        // Vertical Lines
        for (int v = 1; v <= cols - 1; v++) {
            g.drawLine(v * tileSize, 0, v * tileSize, windowH);
        }

        // Horizontal Lines
        for (int v = 1; v <= rows - 1; v++) {
            g.drawLine(0, v * tileSize, windowW, v * tileSize);
        }
    }

    // Sets up the logic for handling key presses from the user
    private void configureInput() {
        panel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {

                started = true;

                boolean pressedUP = e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W;
                boolean pressedDOWN = e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S;
                boolean pressedLEFT = e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A;
                boolean pressedRIGHT = e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D;

                // IMPORTANT: Each one will not trigger if its 180 deg from the current
                // direction

                // Up
                if (pressedUP && currentDirection != Direction.DOWN) {
                    currentDirection = Direction.UP;
                }
                // Down
                if (pressedDOWN && currentDirection != Direction.UP) {
                    currentDirection = Direction.DOWN;
                }
                // Left
                if (pressedLEFT && currentDirection != Direction.RIGHT) {
                    currentDirection = Direction.LEFT;
                }
                // Right
                if (pressedRIGHT && currentDirection != Direction.LEFT) {
                    currentDirection = Direction.RIGHT;
                }

                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    started = false;
                }
            }
        });
    }

}
