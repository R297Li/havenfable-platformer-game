package com.AdventureGame.main;

import javax.swing.*;
import java.awt.*;

public class Window {

    private JFrame _frame;

    public Window(int width, int height, String title, GamePanel gamePanel) {
        _frame = new JFrame(title);
        _frame.setContentPane(gamePanel);

        Dimension frameDimensions = new Dimension(width, height);

        // Set a constant frame size
        _frame.setPreferredSize(frameDimensions);
        _frame.setMaximumSize(frameDimensions);
        _frame.setMinimumSize(frameDimensions);
        _frame.setResizable(false);

        _frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        _frame.setVisible(true);

        // Passing in null allows window to start at center of screen
        _frame.setLocationRelativeTo(null);


        _frame.pack();
    }
}
