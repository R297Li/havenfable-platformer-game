package com.AdventureGame.Entity.Enemies;

import com.AdventureGame.Entity.GameObject;
import com.AdventureGame.Entity.ObjectID;
import com.AdventureGame.Entity.ObjectState;
import com.AdventureGame.Entity.Players.Player;
import com.AdventureGame.Entity.Misc.AnimationAction;
import com.AdventureGame.Handler.GameObjectHandler;
import com.AdventureGame.Handler.ResourceHandler;
import com.AdventureGame.TileMap.TileMap;
import com.AdventureGame.main.Game;

import java.awt.*;

public class Minotaur extends Enemy {

    private int _nextActionState;
    private int _previousActionState;
    private int _dashCounter;
    private boolean _isTaunting;
    private Player _player1;
    private Player _player2;
    private boolean _hasSpecialAtked1;
    private boolean _hasSpecialAtked2;
    private boolean _hasSpecialAtked3;

    private boolean _hasTaunted;

    public Minotaur(TileMap tileMap, GameObjectHandler gameObjectHandler) {
        super(tileMap, gameObjectHandler);

        this._id = ObjectID.BOSS;
        this._state = ObjectState.ALIVE;

        this._isMovingRight = true;
        this._isFacingRight = true;
        this._moveSpeed = 0.3;
        this._maxSpeed = 0.3;
        this._fallSpeed = 0.4;
        this._maxFallSpeed = 10.0;

        this._spriteWidth = 96;
        this._spriteHeight = 64;
        this._collisionWidth = 30;
        this._collisionHeight = 50;

        this._health = 25;
        this._maxHealth = 25;
        this._specialAtkDamage = 2;
        this._basicAtkDamage = 1;
        this._basicAtkRange = 30;

        this._specialAtkCharge = 3000;
        this._maxSpecialAtkCharge = 3000;
        this._specialAtkCost = 100;

        this._nextActionState = 0;
        this._previousActionState= 0;

        this._animationAndSpriteMap = ResourceHandler.minotaurAnimationSpriteMap;

        this._currentAction = AnimationAction.IDLE;
        setAnimation(_animationAndSpriteMap.get(AnimationAction.IDLE).getImages(), 400);
    }

    @Override
    public void update() {
        getNextPosition();
        checkTileMapCollision();
        setPosition(_xTemp, _yTemp);

        switch (_nextActionState) {
            // Face player
            case 0:
                if (isDirectlyFacingPlayer()) {
                    this._nextActionState = 1;
                }
                else if (!isFacingPlayerDirection()) {
                    if (_isFacingRight) {
                        this._isMovingRight = false;
                        this._isMovingLeft = true;
                        this._isFacingRight = false;
                    }
                    else {
                        this._isMovingRight = true;
                        this._isMovingLeft = false;
                        this._isFacingRight = true;
                    }
                    this._dx = 0;
                }

                if (_previousActionState == 1) {
                    this._isTaunting = false;
                    this._isDashing = true;
                    this._nextActionState = 2;
                }

                this._specialAtkCharge += 1;
                this._specialAtkCharge = Game.clamp(_specialAtkCharge, 0, _maxSpecialAtkCharge);

                this._previousActionState = 0;

                break;

            case 1:
                if (_currentAction == AnimationAction.TAUNT) {
                    if (_animation.hasPlayedOnce()) {
                        this._isTaunting = false;
                        this._isDashing = true;
                        this._nextActionState = 2;
                    }
                }
                else if (_hasTaunted) {
                    this._nextActionState = 2;
                }
                else {
                    this._isTaunting = true;
                }

                if (!isDirectlyFacingPlayer()) {
                    this._hasTaunted = true;
                    this._nextActionState = 0;
                }

                this._previousActionState = 1;

                break;

            case 2:
                this._hasTaunted = false;

                if (_previousActionState == 0) {
                    this._nextActionState = 4;
                }
                else if (!_isDashing) {
                    if (_hasSpecialAtked1) {
                        this._nextActionState = 5;
                    }
                    else {
                        this._nextActionState = 3;
                    }
                }

                this._previousActionState = 2;

                break;

            case 3:
                if (_currentAction != AnimationAction.SPECIALATK1) {
                    if (_specialAtkCharge >= _specialAtkCost) {
                        setIsSpecialAtking1();
                        this._specialAtkCharge -= _specialAtkCost;
                    }
                    else {
                        this._nextActionState = 0;
                    }
                }
                else if (_currentAction == AnimationAction.SPECIALATK1) {
                    if (_animation.hasPlayedOnce()) {
                        this._hasTaunted = false;
                        this._isTaunting = false;
                        this._isSpecialAtking1 = false;
                        this._hasSpecialAtked1 = true;
                        this._nextActionState = 0;
                    }
                }

                this._previousActionState = 3;

                break;

            case 4:
                if (_currentAction != AnimationAction.SPECIALATK2) {
                    if (_specialAtkCharge >= _specialAtkCost) {
                        setIsSpecialAtking2();
                        this._specialAtkCharge -= _specialAtkCost;
                    }
                    else {
                        this._nextActionState = 0;
                    }
                }
                else if (_currentAction == AnimationAction.SPECIALATK2) {
                    if (_animation.hasPlayedOnce()) {
                        this._hasTaunted = false;
                        this._isTaunting = false;
                        this._isSpecialAtking2 = false;
                        this._hasSpecialAtked2 = true;
                        this._nextActionState = 0;
                    }
                }

                this._previousActionState = 4;

                break;

            case 5:
                if (_currentAction != AnimationAction.SPECIALATK3) {
                    if (_specialAtkCharge >= _specialAtkCost) {
                        setIsSpecialAtking3();
                        this._specialAtkCharge -= _specialAtkCost;
                    }
                    else {
                        this._nextActionState = 0;
                    }
                }
                else if (_currentAction == AnimationAction.SPECIALATK3) {
                    if (_animation.hasPlayedOnce()) {
                        this._hasTaunted = false;
                        this._isTaunting = false;
                        this._isSpecialAtking3 = false;
                        this._hasSpecialAtked3 = true;
                        this._hasSpecialAtked1 = false;
                        this._hasSpecialAtked2 = false;
                        this._nextActionState = 0;
                    }
                }

                this._previousActionState = 5;

                break;

            case 6:

                this._isSpecialAtking1 = false;
                this._isSpecialAtking2 = false;
                this._isSpecialAtking3 = false;

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

                break;
        }

        if (_isFlinching) {
            long elapsed = (System.nanoTime() - _flinchTimeMS) / 1000000;

            if (elapsed > 400) {
                this._isFlinching = false;
            }
        }

        if (_isTaunting) {
            setAnimationAction(AnimationAction.TAUNT, 150, _spriteWidth);
        }
        else if (_isSpecialAtking1) {
            setAnimationAction(AnimationAction.SPECIALATK1, 50, _spriteWidth);
        }
        else if (_isSpecialAtking2) {
            setAnimationAction(AnimationAction.SPECIALATK2, 50, _spriteWidth);
        }
        else if (_isSpecialAtking3) {
            setAnimationAction(AnimationAction.SPECIALATK3, 50, _spriteWidth);
        }
        else if ((_isMovingLeft || _isMovingRight) && _isDashing) {
            setAnimationAction(AnimationAction.DASHING, 50, _spriteWidth);
        }
        else if (_isMovingLeft || _isMovingRight) {
            setAnimationAction(AnimationAction.WALKING, 80, _spriteWidth);
        }

        _animation.update();
        collision();
    }

    @Override
    public void collision() {

        System.out.println("-------------------");
        System.out.println("X:" + _x);
        System.out.println("Atk Range Right: " + (_x + _basicAtkRange));
        System.out.println("Atk Range Left: " + (_x - _basicAtkRange));

        for (GameObject player : gameObjectHandler.playerObjects) {
            if (this.isOnScreen() && player.isOnScreen()) {

                if (_isSpecialAtking1 || _isSpecialAtking2 || _isSpecialAtking3) {
                    if (_isFacingRight) {
                        if (_x < player.getX() && (_x + _basicAtkRange) > player.getX()) {
                            if ((_y - (_spriteHeight / 2)) < player.getY() && (_y + (_spriteHeight / 2)) > player.getY()) {
                                player.hitByAttack(_specialAtkDamage);
                            }
                        }
                    } else {
                        if (_x > player.getX() && (_x - _basicAtkRange) < player.getX() + player.getWidth()) {
                            if ((_y - (_spriteHeight / 2)) < player.getY() && (_y + (_spriteHeight / 2)) > player.getY()) {
                                player.hitByAttack(_specialAtkDamage);
                            }
                        }
                    }
                }
            }
        }

        if (gameObjectHandler.playerObjects.size() < 1) {
            this._nextActionState = 6;
        }
    }

    @Override
    public void draw(Graphics2D g) {
        setMapPosition();

        if (isOnScreen()) {
            if (_isFlinching) {
                long elapsed = (System.nanoTime() - _flinchTimeMS) / 1000000;

                // Blink every 100ms
                if ((elapsed / 100) % 2 == 0) {
                    return;
                }
            }

            drawNextPosition(g);
        }
    }

    public void getNextPosition() {

        setNextChangeInHorizontalPosition();

        if (!_isDashing && _dashCounter != 0) {
            this._dashCounter = 0;
        }

        if (_isDashing) {
            this._dashCounter++;

            double multiplier = 8 - (_dashCounter * 0.07);

            if (_isFacingRight) {
                this._dx = _moveSpeed * multiplier;
            }
            else {
                this._dx = -_moveSpeed * multiplier;
            }

            if (isPlayerInRange(_basicAtkRange)) {
                this._dx = 0;
                this._isDashing = false;
            }

            if (multiplier <= 0) {
                this._isDashing = false;
            }
        }

        if (_isFalling) {
            this._dy += _fallSpeed;
            this._dy = Game.clamp(_dy, 0, _maxFallSpeed);
        }

        if (_isSpecialAtking1) {
            this._dx = 0;
        }
    }

    @Override
    public Rectangle getRectangle() {
        int x = (int)_x - (_collisionWidth / 2);
        int y = (int)_y - (_collisionHeight / 5);

        return new Rectangle(x, y, _collisionWidth, _collisionHeight);
    }
}
