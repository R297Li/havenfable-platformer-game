package com.AdventureGame.Entity.Misc;

import com.AdventureGame.Handler.ResourceHandler;

import javax.imageio.ImageIO;
import javax.xml.crypto.dsig.spec.ExcC14NParameterSpec;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Timer {

    private static long _startTime;
    private static long _currentTimeInSeconds;
    private static long _endTime;
    private static long _totalTimeElapsedInSeconds;

    private static BufferedImage _image;
    private static Font _font;
    private static Color _fontColor;

    public Timer() {
        this._startTime = System.currentTimeMillis();
        this._currentTimeInSeconds = 0;

        this._font = new Font("Arial", Font.PLAIN, 10);
        this._fontColor = Color.BLACK;

        try {
            this._image = ImageIO.read(getClass().getResourceAsStream(ResourceHandler.hudTimerFilePath));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void draw(Graphics2D g) {
        g.setFont(_font);
        g.setColor(_fontColor);

        g.drawImage(_image, 265, 5, null);
        g.drawString(getCurrentTime(), 273, 16);
    }

    public static void startTimer() {
        _startTime = System.currentTimeMillis();
        _currentTimeInSeconds = 0;
    }

    public static void stopTimer() {
        _endTime = System.currentTimeMillis();
    }

    public static void updateCurrentTime() {
        _currentTimeInSeconds = (System.currentTimeMillis() - _startTime) / 1000;
    }

    public static String getCurrentTime() {
        return formatTime(_currentTimeInSeconds);
    }

    public static void setTimeElapsedInSeconds() {
        _totalTimeElapsedInSeconds = (_endTime - _startTime) / 1000;
    }

    public static long getTimeElapsedInSeconds() {
        return _totalTimeElapsedInSeconds;
    }

    public static String getElapsedTime() {
        return formatTime(_totalTimeElapsedInSeconds);
    }

    private static String formatTime(long timeInSeconds) {
        long minutes = (timeInSeconds % 3600) / 60;
        long seconds = timeInSeconds % 60;

        return String.format("%02d:%02d", minutes, seconds);
    }
}
