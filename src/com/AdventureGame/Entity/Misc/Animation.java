package com.AdventureGame.Entity.Misc;

import java.awt.image.BufferedImage;

public class Animation {

    private BufferedImage[] _frames;
    private int _currentFrame;

    private long _startTime;
    private long _delayBetweenFrames;

    private boolean _isOnRepeat;
    private boolean _hasPlayedOnce;

    public Animation() {
        this._isOnRepeat = true;
        this._hasPlayedOnce = false;
    }

    public void setFrames(BufferedImage[] frames) {
        this._frames = frames;
        this._currentFrame = 0;
        this._startTime = System.nanoTime();
        this._isOnRepeat = true;
        this._hasPlayedOnce = false;
    }

    public void setDelayBetweenFrames(long delay) {
        this._delayBetweenFrames = delay;
    }

    public void setCurrentFrame(int frameIndex) {
        this._currentFrame = frameIndex;
    }

    public void update() {
        if (_delayBetweenFrames < 0) {
            return;
        }

        long elapsed = (System.nanoTime() - _startTime) / 1000000;

        if (elapsed > _delayBetweenFrames) {
            this._currentFrame++;
            this._startTime = System.nanoTime();
        }

        if (_currentFrame == _frames.length) {
            this._currentFrame = 0;
            this._hasPlayedOnce = true;
        }
    }

    public int getCurrentFrame() {
        return _currentFrame;
    }

    public BufferedImage getImage() {
        return _frames[_currentFrame];
    }

    public boolean hasPlayedOnce() {
        return _hasPlayedOnce;
    }


}
