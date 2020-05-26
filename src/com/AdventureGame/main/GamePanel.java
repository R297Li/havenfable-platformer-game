package com.AdventureGame.main;

import com.AdventureGame.Handler.KeyInputHandler;
import com.AdventureGame.GameState.GameStateManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

public class GamePanel extends JPanel implements Runnable, KeyListener {

    public static final int WIDTH = 320;
    public static final int HEIGHT = 240;
    public static final int SCALE = 3;

    private Thread _thread;
    private boolean _isThreadRunning = false;
    private int _fps = 60;
    private long _targetTime = 1000 / _fps;

    private BufferedImage _image;
    private Graphics2D _graphics;

    private GameStateManager _gsm;
    private KeyInputHandler _keyInputHandler;

    public GamePanel() {
        super();
        setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
        setFocusable(true);
        requestFocus();
    }

    public void addNotify() {
        super.addNotify();

        if (_thread == null) {
            this._thread = new Thread(this);
            addKeyListener(this);
            _thread.start();
        }
    }

    private void init() {
        this._image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        this._graphics = (Graphics2D)_image.getGraphics();
        this._isThreadRunning = true;

        this._gsm = new GameStateManager();
    }

    @Override
    public void run() {
        this.init();

        long prevTime = System.nanoTime();
        double ticksPerSecond = 55.0;
        double nanoSecPerTick = 1000000000 / ticksPerSecond;
        double delta = 0;
        long timer = System.currentTimeMillis();
        int fps = 0;

        while (_isThreadRunning) {
            long currentTime = System.nanoTime();
            delta += (currentTime - prevTime) / nanoSecPerTick; // Returns a unit of ticks
            prevTime = currentTime;

            while (delta >= 1) {
                this.update();
                delta--;
            }

            if (_isThreadRunning) {
                this.draw();
                this.drawToScreen();
            }

            fps++;

            // Print FPS every second and update timer
            if (System.currentTimeMillis() - timer > 1000) {
                timer += 1000;
                System.out.println("FPS:" + Integer.toString(fps));
                fps = 0;
            }
        }
    }

    private void update() {
        _gsm.update();
    }

    private void draw() {
        _gsm.draw(_graphics);
    }

    private void drawToScreen() {
        Graphics g = this.getGraphics();
        g.drawImage(_image, 0, 0, WIDTH * SCALE,HEIGHT * SCALE, null);
        g.dispose();
    }


    @Override
    public void keyTyped(KeyEvent keyEvent) {

    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {
        _gsm.keyPressed(keyEvent.getKeyCode());
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {
        _gsm.keyReleased(keyEvent.getKeyCode());
    }
}
