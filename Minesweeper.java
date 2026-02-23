import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import javax.swing.*;

public class Minesweeper extends Screen {

    private class TileButton extends JButton {
        int row, col;
        int val;
        boolean visited;
        boolean flagged;

        public TileButton(int r, int c) {
            row = r;
            col = c;
            val = 0;
            visited = flagged = false;
        }

        public void setVal(int x) {
            this.val = x;
        }

        public int getVal() {
            return this.val;
        }

        public void flipFlag() {
            this.flagged = !this.flagged;
        }

        public boolean isBomb() {
            return this.val == -1;
        }
    }

    // Game constants
    static final int tileSize = 70;
    static final int rows = 8;
    static final int cols = 8;
    static final int initial_bombs = 10;

    // Game grid
    TileButton[][] grid = new TileButton[rows][cols];

    // Game text Label/Panel
    JPanel textPanel = new JPanel();
    JLabel textLabel = new JLabel();

    // Board Panel
    JPanel boardPanel = new JPanel();

    // direction vectors
    final int[] dx = { 1, -1, 0, 0, 1, 1, -1, -1 };
    final int[] dy = { 0, 0, 1, -1, 1, -1, 1, -1 };

    // Tile emojies
    String[] em = { "💣", "🚩" };
    final int BOMB = 0;
    final int FLAG = 1;

    boolean gameOver = false;

    int cleared = 0;

    /*
     * Logic:
     * grid[i][j] --> the block at the i'th row and j'th column
     * 8 >= grid[i][j] >= 0 (when grid[i][j] is empty)
     * grid[i][j] = -1 (when grid[i][j] has a bomb inside)
     */

    Minesweeper() {

        // Set the parent's settings
        super(cols * tileSize, rows * tileSize, "Esmail's Minesweeper");

        // ====== Set up textLabel ====== //
        textLabel.setFont(new Font("Arial", Font.BOLD, 25));
        textLabel.setHorizontalAlignment(JLabel.CENTER);
        textLabel.setText("Minesweeper");
        textLabel.setOpaque(true);

        // ====== Set up textPanel ====== //
        textPanel.setLayout(new BorderLayout());
        textPanel.add(textLabel);

        // ====== Set up boardPanel ====== //
        boardPanel.setLayout(new GridLayout(rows, cols));
        boardPanel.setBackground(Color.black);

        // ====== Set up buttons ====== //

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {

                // Create the new button
                TileButton tile = new TileButton(r, c);

                // Settings
                tile.setMargin(new Insets(0, 0, 0, 0));
                tile.setFont(new Font("Arial Unicode MS", Font.PLAIN, 45));
                tile.setFocusable(false);

                tile.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        // Get the pressed button
                        TileButton pressedTile = (TileButton) e.getSource();

                        // Left Click
                        if (e.getButton() == MouseEvent.BUTTON1) {
                            pressed(pressedTile);
                        }
                        // Right Click
                        else if (e.getButton() == MouseEvent.BUTTON3 && !pressedTile.visited) {

                            // Flip state
                            pressedTile.flipFlag();

                            // Update text
                            if (pressedTile.flagged) {
                                pressedTile.setText(em[FLAG]);
                            } else {
                                pressedTile.setText("");
                            }
                        }
                    }
                });

                // Add button
                grid[r][c] = tile;
                boardPanel.add(grid[r][c]);
            }
        }

        frame.add(boardPanel);
        frame.add(textPanel, BorderLayout.NORTH);
    }

    // Function Responisble for returning a random
    // number between 0-rows/cols depending on row?
    int pickRandom(boolean row) {
        Random rand = new Random();
        return rand.nextInt(row ? rows : cols);
    }

    // Check the coordinates are valid
    boolean check(int r, int c) {
        return (r < rows && r >= 0 && c < cols && c >= 0);
    }

    // Function responsible for resetting and calculating a new game state
    // (fr, fc) are the coords of the first press

    void setUp(int fr, int fc) {
        // Reset values
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                grid[i][j].val = 0;
                // grid[i][j].setText("-1");
            }
        }

        // Pick (initial_bombs) random cells
        for (int i = 0; i < initial_bombs; i++) {

            int r = pickRandom(true);
            int c = pickRandom(false);

            while (grid[r][c].isBomb() || // a bomb
                    (r == fr && c == fc) // first press
            ) {
                r = pickRandom(true);
                c = pickRandom(false);
            }
            grid[r][c].setVal(-1);
        }

        // Fill in values
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                // Ignore bombs
                if (grid[i][j].isBomb())
                    continue;

                int bomb_count = 0;
                // count the number of bombs in all directions
                for (int k = 0; k < dx.length; k++) {
                    int ni = i + dx[k];
                    int nj = j + dy[k];
                    if (check(ni, nj) && grid[ni][nj].isBomb()) {
                        bomb_count++;
                    }
                }

                grid[i][j].setVal(bomb_count);
            }
        }
    }

    // Apply BFS starting from (r,c)
    // Assumptions:
    // grid[r][c].val = 0
    // Game is not over
    void floodFill(int r, int c) {
        if (grid[r][c].getVal() != 0) {
            System.out.println(r + String.valueOf(c));
        }

        for (int k = 0; k < dx.length; k++) {
            int nr = r + dx[k];
            int nc = c + dy[k];
            if (check(nr, nc) && !grid[nr][nc].visited && !grid[nr][nc].flagged) {
                pressed(grid[nr][nc]);
            }
        }
    }

    void revealBombs() {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (grid[r][c].isBomb()) {
                    grid[r][c].setText(em[BOMB]);
                }
            }
        }
    }

    // Handle a button press
    void pressed(TileButton tile) {
        int r = tile.row;
        int c = tile.col;
        int val = tile.val;
        boolean visited = tile.visited;

        // Don't bother recalculating
        if (visited || gameOver)
            return;

        tile.visited = true;
        cleared++;
        tile.setEnabled(false);

        if (cleared == 1) {
            setUp(r, c);
            val = grid[r][c].getVal();
        }

        // Loss condition
        if (val == -1) {
            revealBombs();
            gameOver = true;
            textLabel.setText("Game Over!");
        } else if (val > 0) {
            // Reveal text
            tile.setText(String.valueOf(val));
        } else {
            floodFill(r, c);
        }

        if (cleared == rows * cols - initial_bombs && !gameOver) {
            gameOver = true;
            textLabel.setText("Mines Cleared!");
        }
    }

}
