package GameHub;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class Minesweeper extends Screen {

    // Game constants
    static final int tileSize = 70;
    static final int rows = 8;
    static final int cols = 8;

    Minesweeper() {

        // Set the parent's settings
        super(cols * tileSize, rows * tileSize, "Minesweeper");

    }

}
