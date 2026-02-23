import java.awt.*;
import javax.swing.*;

public class Screen {

    // Creates the window and names it
    JFrame frame;

    // Dimensions
    int windowW = 600;
    int windowH = 300;

    public Screen(int W, int H, String tilte) {

        // Frame title
        frame = new JFrame(tilte);

        // Set dimensions
        windowW = W;
        windowH = H;

        // Resize
        frame.setSize(windowW, windowH);

        // Start the window in the middle of the screen
        frame.setLocationRelativeTo(null);

        // Prevent User resizing
        frame.setResizable(false);

        // By default the program doesn't terminate on hitting 'X' which eats RAM ->
        // This fixes this
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        ImageIcon image = new ImageIcon("assets/icon.png"); // Create an image Icon

        // Set icon to image
        frame.setIconImage(image.getImage());

        // Change color of bg
        /*
         * Custome color:
         * Color.black -> new Color(r,g,b)
         */
        frame.getContentPane().setBackground(Color.black);

        // hide the window at first
        frame.setVisible(false);
    }

    public void show() {
        // Show the window
        frame.setVisible(true);
    }

    public void hide() {
        // hide the window
        frame.setVisible(false);
    }
}
