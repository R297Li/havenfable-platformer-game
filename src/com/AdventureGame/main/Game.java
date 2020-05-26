package com.AdventureGame.main;

import javax.swing.*;

public class Game {

    private Window _window;
    private GamePanel _gamePanel;

    public Game() {
        String title = "Haven Fable";
        this._gamePanel = new GamePanel();
        this._window = new Window(GamePanel.WIDTH * GamePanel.SCALE, GamePanel.HEIGHT * GamePanel.SCALE, title, new GamePanel());
    }

    public static void main(String[] args) {
        new Game();
    }

    public static int clamp(int position, double minPosition, double maxPosition) {
        double value = clamp((double)position, minPosition, maxPosition);
        return (int)value;
    }

    public static double clamp(double position, double minPosition, double maxPosition) {
        if (position <= minPosition) {
            position = minPosition;
        }
        else if (position >= maxPosition) {
            position = maxPosition;
        }

        return position;
    }
}
