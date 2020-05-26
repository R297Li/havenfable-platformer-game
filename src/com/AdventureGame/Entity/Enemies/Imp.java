package com.AdventureGame.Entity.Enemies;

import com.AdventureGame.Entity.*;
import com.AdventureGame.Entity.SpecialAtk.FireBall;
import com.AdventureGame.Entity.Misc.AnimationAction;
import com.AdventureGame.Handler.GameObjectHandler;
import com.AdventureGame.Handler.ResourceHandler;
import com.AdventureGame.Handler.ResourceType;
import com.AdventureGame.TileMap.TileMap;
import com.AdventureGame.main.Game;

import java.awt.*;

public class Imp extends Enemy {

    private int _actionState;

    public Imp(TileMap tileMap, GameObjectHandler gameObjectHandler) {
        super(tileMap, gameObjectHandler);

        this._id = ObjectID.ENEMY;
        this._state = ObjectState.ALIVE;

        this._isMovingRight = true;
        this._isFacingRight = true;
        this._moveSpeed = 0.7;
        this._maxSpeed = 0.7;
        this._fallSpeed = 0.4;
        this._maxFallSpeed = 10.0;

        this._spriteWidth = 32;
        this._spriteHeight = 28;
        this._collisionWidth = 24;
        this._collisionHeight = 22;

        this._health = 4;
        this._maxHealth = 4;
        this._specialAtkDamage = 2;
        this._basicAtkDamage = 1;

        this._specialAtkCharge = 500;
        this._maxSpecialAtkCharge = 500;
        this._specialAtkCost = 400;

        this._actionState = 0;

        this._animationAndSpriteMap = ResourceHandler.impAnimationSpriteMap;

        this._currentAction = AnimationAction.IDLE;
        setAnimation(_animationAndSpriteMap.get(AnimationAction.IDLE).getImages(), 400);
    }

    @Override
    public void update() {
        getNextPosition();
        checkTileMapCollision();
        setPosition(_xTemp, _yTemp);

        switch (_actionState) {
            case 0:
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

                if (isDirectlyFacingPlayer() && _currentAction != AnimationAction.SPECIALATK1) {
                    if (_specialAtkCharge >= _specialAtkCost) {
                        setIsSpecialAtking1();
                        this._specialAtkCharge -= _specialAtkCost;
                        this._actionState = 1;
                    }
                }

                this._specialAtkCharge += 1;
                this._specialAtkCharge = Game.clamp(_specialAtkCharge, 0, _maxSpecialAtkCharge);

                break;

            case 1:
                if (_isSpecialAtking1) {
                    FireBall fireBall = new FireBall(_tileMap, _isFacingRight, gameObjectHandler, ResourceType.PURPLE_FIREBALL);
                    fireBall.setID(ObjectID.ENEMY_SPECIALATK);
                    fireBall.setPosition(_x, _y);
                    fireBall.setSpecialAtkDamage(_specialAtkDamage);
                    gameObjectHandler.addGameObject(fireBall);

                    this._actionState = 2;
                }

                break;

            case 2:
                if (_currentAction == AnimationAction.SPECIALATK1) {
                    if (_animation.hasPlayedOnce()) {
                        this._isSpecialAtking1 = false;
                        this._actionState = 0;
                    }
                }

                break;
        }

        if (_isFlinching) {
            long elapsed = (System.nanoTime() - _flinchTimeMS) / 1000000;

            if (elapsed > 400) {
                this._isFlinching = false;
            }
        }

        if ((_isMovingLeft || _isMovingRight) && !_isSpecialAtking1) {
            setAnimationAction(AnimationAction.WALKING, 80, _spriteWidth);
        }
        else if (_isSpecialAtking1) {
            setAnimationAction(AnimationAction.SPECIALATK1, 50, _spriteWidth);
        }

        _animation.update();
        collision();
    }

    @Override
    public void collision() {
        for (GameObject player : gameObjectHandler.playerObjects) {
            for (GameObject gameObject : gameObjectHandler.enemySpecialAtkObjects) {
                if (gameObject.isThereIntersection(player)) {
                    player.hitByAttack(gameObject.getSpecialAtkDamage());
                    gameObject.setIsCollided();
                }
            }
        }
    }

    @Override
    public void draw(Graphics2D g) {
        setMapPosition();

        if (isOnScreen()) {
            drawNextPosition(g);
        }
    }

    public void getNextPosition() {
        setNextChangeInHorizontalPosition();

        if (_isFalling) {
            this._dy += _fallSpeed;
            this._dy = Game.clamp(_dy, 0, _maxFallSpeed);
        }

        if (_isSpecialAtking1) {
            this._dx = 0;
        }
    }
}
