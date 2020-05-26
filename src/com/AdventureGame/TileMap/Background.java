package com.AdventureGame.TileMap;

import com.AdventureGame.main.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Background {

    private BufferedImage _image;

    private double _x;
    private double _y;
    private double _dx;
    private double _dy;

    private double _moveScale;

    public Background(String path, double moveScale) {
        try {
            this._image = ImageIO.read(getClass().getResourceAsStream(path));
            this._moveScale = moveScale;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setPosition(double x, double y) {
        this._x = (x * _moveScale) % GamePanel.WIDTH;
        this._y = (y * _moveScale) % GamePanel.HEIGHT;
    }

    public void setVector(double dx, double dy) {
        this._dx = dx;
        this._dy = dy;
    }

    public void update() {
        this._x += _dx;
        this._y += _dy;
    }

    public void draw(Graphics2D g) {
        g.setColor(new Color(245, 245, 220));
        g.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);

        g.drawImage(_image, (int)_x, (int)_y, null);

        if (_x < 0) {
            g.drawImage(_image, (int)_x + GamePanel.WIDTH, (int)_y, null);
        }
        if (_x > 0) {
            g.drawImage(_image, (int)_x - GamePanel.WIDTH, (int)_y, null);
        }
    }
}
