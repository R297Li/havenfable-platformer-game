package com.AdventureGame.Entity.Misc;

import com.AdventureGame.Entity.Misc.AnimationAction;

import java.awt.image.BufferedImage;

public class Sprite {

    private AnimationAction _animationAction;
    private int _numSprites;
    private int _row;
    private BufferedImage[] _images;
    private int _spriteWidth;
    private int _spriteHeight;

    public Sprite(AnimationAction animationAction, int numSprites, int row, int spriteWidth, int spriteHeight) {
        this._numSprites = numSprites;
        this._animationAction = animationAction;
        this._row = row;
        this._spriteWidth = spriteWidth;
        this._spriteHeight = spriteHeight;
    }

    public Sprite(AnimationAction animationAction, int numSprites, int row) {
        this._numSprites = numSprites;
        this._animationAction = animationAction;
        this._row = row;
    }

    public int getNumSprites() {
        return _numSprites;
    }

    public int getRow() {
        return _row;
    }

    public AnimationAction getAnimationAction() {
        return _animationAction;
    }

    public void setNumSprites(int numSprites) {
        this._numSprites = numSprites;
    }

    public void setAnimationAction(AnimationAction animationAction) {
        this._animationAction = animationAction;
    }

    public void setImages(BufferedImage[] images) {
        this._images = images;
    }

    public BufferedImage[] getImages() { return _images; }

    public void setSpriteWidth(int width) {
        this._spriteWidth = width;
    }

    public void setSpriteHeight(int height) {
        this._spriteHeight = height;
    }

    public int getSpriteWidth() {
        return _spriteWidth;
    }

    public int getSpriteHeight() {
        return _spriteHeight;
    }

}
