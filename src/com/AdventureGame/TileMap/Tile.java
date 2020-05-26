package com.AdventureGame.TileMap;

import java.awt.image.BufferedImage;

public class Tile {

    private BufferedImage _image;
    private TileType _type;

    public Tile(BufferedImage image, int type) {
        this(image, TileType.valueOf(type));
    }

    public Tile(BufferedImage image, TileType type) {
        this._image = image;
        this._type = type;
    }

    public BufferedImage getImage() {
        return this._image;
    }

    public TileType getType() {
        return this._type;
    }
}
