package GameHub;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class Hub extends Screen {

    static String[] featuredGames = { "Minesweeper", "Snake" };

    public Hub() {

        super(600, 300, "GameHub");

        // Create a grid layout
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 100));

        // Makes panel bg transparent
        panel.setOpaque(false);

        // Make buttons
        for (int i = 0; i < featuredGames.length; i++) {

            // ======== Button Creation ======== //

            JButton button = new JButton(featuredGames[i]);

            // ======== Button Behaviour ======== //

            button.addActionListener(e -> {

                // Simulate Closing (hiding the window)
                this.hide();
                String choice = button.getText();

                // Choose which game to load based on the Button pressed
                switch (choice) {
                    case "Minesweeper":
                        new Minesweeper().show();
                        break;
                    case "Snake":
                        new Snake().show();
                        break;
                }
            });

            button.setFocusable(false);

            // ======== Finalization ======== //

            // Add it to view
            panel.add(button);
        }

        // Add it to frame
        frame.add(panel);
    }
}
