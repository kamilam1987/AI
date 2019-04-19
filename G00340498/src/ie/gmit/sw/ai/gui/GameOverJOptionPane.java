package ie.gmit.sw.ai.gui;

import javax.swing.*;
import java.awt.*;

public class GameOverJOptionPane {

    // display the dialog box
    public static void display(String message) {

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel(message));
        panel.add(new JLabel(""));

        // show dialog box
        JOptionPane.showConfirmDialog(null, panel, "Game Over!",
                JOptionPane.DEFAULT_OPTION);
    } // display()

} // class
