import java.awt.*;
import javax.swing.*;

public class Main {

    public static class Hub extends Screen {

        static String[] featuredGames = { "Minesweeper", "Snake" };

        public Hub() {

            super(600, 300, "GameHub");

            // ===== Add the label ===== //

            // configure label
            JLabel textLabel = new JLabel(
                    "<html><center>Welcome to Esmail's Game Hub!<br>Choose a Game.</center></html>");
            textLabel.setFont(new Font("Arial", Font.BOLD, 25));
            textLabel.setHorizontalAlignment(JLabel.CENTER);
            textLabel.setOpaque(true);

            // configure panel
            JPanel textPanel = new JPanel();
            textPanel.setLayout(new BorderLayout());
            textPanel.add(textLabel);

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

            // Add it to view
            frame.setLayout(new BorderLayout());
            frame.add(textPanel, BorderLayout.NORTH);
            frame.add(panel, BorderLayout.CENTER);
        }
    }

    public static void main(String[] args) {
        Hub MainMenu = new Hub();
        MainMenu.show();
    }
}
