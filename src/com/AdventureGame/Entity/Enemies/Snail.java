package com.AdventureGame.Entity.Enemies;

import com.AdventureGame.Entity.ObjectID;
import com.AdventureGame.Entity.ObjectState;
import com.AdventureGame.Entity.Misc.AnimationAction;
import com.AdventureGame.Handler.GameObjectHandler;
import com.AdventureGame.Handler.ResourceHandler;
import com.AdventureGame.TileMap.TileMap;
import com.AdventureGame.main.Game;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Snail extends Enemy {

    private BufferedImage[] _sprites;

    public Snail(TileMap tileMap, GameObjectHandler gameObjectHandler) {
        super(tileMap, gameObjectHandler);

        this._id = ObjectID.ENEMY;
        this._state = ObjectState.ALIVE;

        this._isMovingRight = true;
        this._isFacingRight = true;
        this._moveSpeed = 0.3;
        this._maxSpeed = 0.3;
        this._fallSpeed = 0.2;
        this._maxFallSpeed = 10.0;

        this._spriteWidth = 30;
        this._spriteHeight = 30;
        this._collisionWidth = 20;
        this._collisionHeight = 20;

        this._health = 2;
        this._maxHealth = 2;
        this._basicAtkDamage = 1;

        this._animationAndSpriteMap = ResourceHandler.snailAnimationSpriteMap;

        this._currentAction = AnimationAction.WALKING;
        setAnimation(_animationAndSpriteMap.get(AnimationAction.WALKING).getImages(), 300);
    }

    public void getNextPosition() {
        setNextChangeInHorizontalPosition();

        if (_isFalling) {
            this._dy += _fallSpeed;
            this._dy = Game.clamp(_dy, 0, _maxFallSpeed);
        }
    }

    public void update() {
        getNextPosition();
        checkTileMapCollision();
        setPosition(_xTemp, _yTemp);

        /*
        System.out.println("-----Snail---------");
        System.out.println("Bottom Right Ledge: " + _isBottomRightLedge);
        System.out.println("Bottom Left Ledge: " + _isBottomLeftLedge);
        System.out.println("Bottom Right Collision: " + _isBottomRightCollision);
        System.out.println("Bottom Left Collision: " + _isBottomLeftCollision);
        System.out.println("Top Right Collision: " + _isTopRightCollision);
        System.out.println("Top Left Collision: " + _isTopLeftCollision);


         */
        if (_isFlinching) {
            long elapsed = (System.nanoTime() - _flinchTimeMS) / 1000000;

            if (elapsed > 400) {
                this._isFlinching = false;
            }
        }

        // Change direction when wall is hit
        if (_isMovingRight && (_dx == 0 || _isBottomRightLedge)) {
            this._isMovingRight = false;
            this._isMovingLeft = true;
            this._isFacingRight = false;
            this._dx = 0;
        }
        else if (_isMovingLeft && (_dx == 0 || _isBottomLeftLedge)) {
            this._isMovingRight = true;
            this._isMovingLeft = false;
            this._isFacingRight = true;
            this._dx = 0;
        }

        _animation.update();
    }

    @Override
    public void collision() {}

    public void draw(Graphics2D g) {
        setMapPosition();

        if (isOnScreen()) {
            drawNextPosition(g);
        }
    }
}
