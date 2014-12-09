package gui;

import javax.swing.*;
import java.awt.*;

public class SplashScreen extends JWindow {

    private static final long serialVersionUID = 1L;
    private int duration;

    public SplashScreen(int d) {
        duration = d;
    }

    public void showSplash() {
        JPanel content = (JPanel) getContentPane();
        content.setBackground(Color.white);

        // Sets the size and position of the window
        int width = 450;
        int height = 253;
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screen.width - width) / 2;
        int y = (screen.height - height) / 2;
        setBounds(x, y, width, height);

        // Builds the splash screen
        JLabel label = new JLabel(new ImageIcon(getClass().getResource("/img/uranus.jpg")));
        content.add(label, BorderLayout.CENTER);

        // Sets it visible
        setVisible(true);

        // Waits the specified time
        try {
            Thread.sleep(duration);
        } catch (Exception e) {}

        setVisible(false);
    }

    public void showSplashAndExit() {
        showSplash();
        System.exit(0);
    }
}