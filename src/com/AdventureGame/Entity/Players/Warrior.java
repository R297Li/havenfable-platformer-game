package com.AdventureGame.Entity.Players;

import com.AdventureGame.Entity.*;
import com.AdventureGame.Entity.Misc.AnimationAction;
import com.AdventureGame.Handler.GameObjectHandler;
import com.AdventureGame.Handler.Movement;
import com.AdventureGame.Handler.ResourceHandler;
import com.AdventureGame.TileMap.TileMap;
import com.AdventureGame.main.Game;

import java.awt.event.KeyEvent;
import java.util.HashMap;

public class Warrior extends Player {

    private int _specialAtkCounter;
    private boolean _alreadyDoubleJumped;
    private int _specialAtkRange;

    public Warrior(TileMap tileMap, String spritePath, GameObjectHandler gameObjectHandler, ObjectID id) {
        super(tileMap, spritePath, gameObjectHandler);

        this._state = ObjectState.ALIVE;
        this._id = id;

        this._movementAndKeyMap = new HashMap<Integer, Movement>();
        setKeys();

        this._spriteWidth = 40;
        this._spriteHeight = 40;
        this._collisionWidth = 30;
        this._collisionHeight = 30;

        this._moveSpeed = 1.6;
        this._maxSpeed = 1.6;
        this._stopSpeed = 1.0;
        this._fallSpeed = 0.15;
        this._maxFallSpeed = 4.0;
        this._jumpStart = -4.8;
        this._stopJumpSpeed = 0.3;
        this._doubleJumpStart = -3;

        this._isFacingRight = true;

        this._health = 8;
        this._maxHealth = 8;

        this._specialAtkCharge = 3000;
        this._maxSpecialAtkCharge = 3000;
        this._specialAtkCounter = 0;

        this._specialAtkCost = 300;
        this._specialAtkDamage = 4;
        this._specialAtkRange = 70;

        this._basicAtkDamage = 3;
        this._basicAtkRange = 40;

        this._animationAndSpriteMap = ResourceHandler.warriorAnimationSpriteMap;

        this._currentAction = AnimationAction.IDLE;
        setAnimation(_animationAndSpriteMap.get(AnimationAction.IDLE).getImages(), 400);
    }

    @Override
    public void setJumping(boolean jumping) {
        if (jumping && !_isJumping && _isFalling && !_alreadyDoubleJumped) {
            this._isDoubleJumping = true;
        }

        this._isJumping = jumping;
    }

    public void update() {
        // Update Position
        getNextPosition();
        checkTileMapCollision();
        setPosition(_xTemp, _yTemp);

        if (_currentAction == AnimationAction.BASICATK || _currentAction == AnimationAction.SPECIALATK1 || _currentAction == AnimationAction.BASICATKUPWARDS) {
            if (_animation.hasPlayedOnce()) {
                this._isBasicAtking = false;
                this._isSpecialAtking1 = false;
                this._isInvincible = false;
            }
        }

        this._specialAtkCharge += 1;
        this._specialAtkCharge = Game.clamp(_specialAtkCharge, 0, _maxSpecialAtkCharge);

        if (_isSpecialAtking1 && _currentAction != AnimationAction.SPECIALATK1) {
            if (_specialAtkCharge > _specialAtkCost) {
                this._specialAtkCharge -= _specialAtkCost;
            }
            else {
                this._isSpecialAtking1 = false;
            }
        }

        if (_isFlinching) {
            long elapsed = (System.nanoTime() - _flinchTimeMS) / 1000000;

            // Turn off flinching after 1s
            if (elapsed > 1000) {
                this._isFlinching = false;
            }
        }

        // Set animation
        if (_isBasicAtking) {
            setAnimationAction(AnimationAction.BASICATK, 50, 80);
            setIsInvincible();
        }
        else if (_isSpecialAtking1) {
            setAnimationAction(AnimationAction.SPECIALATK1, 100, 80);
            setIsInvincible();
        }
        // Descending
        else if (_dy > 0) {
            if (_currentAction != AnimationAction.FALLING) {
                setAnimationAction(AnimationAction.FALLING, 100, 40);
            }
        }
        // Flying/In air
        else if (_dy < 0) {
            setAnimationAction(AnimationAction.JUMPING, -1, 40);
        }
        else if (_isDashing && (_isMovingLeft || _isMovingRight)) {
            setAnimationAction(AnimationAction.DASHING, 40, 40);
        }
        else if (_isMovingLeft || _isMovingRight) {
            setAnimationAction(AnimationAction.WALKING, 40, 40);
        }
        else {
            setAnimationAction(AnimationAction.IDLE, 400, 40);
        }

        _animation.update();

        // Set direction
        if (_currentAction != AnimationAction.BASICATK && _currentAction != AnimationAction.SPECIALATK1) {
            if (_isMovingRight) {
                this._isFacingRight = true;
            }
            if (_isMovingLeft) {
                this._isFacingRight = false;
            }
        }

        collision();

        if (isFallingOutOfWorld()) {
            for(GameObject player : gameObjectHandler.playerObjects) {
                player.setState(ObjectState.RESPAWN);
            }
        }
    }

    public void getNextPosition() {
        Double tempMaxSpeed = this._maxSpeed;
        if (_isDashing) {
            this._maxSpeed = tempMaxSpeed * 1.6;
        }

        setNextChangeInHorizontalPosition();

        // Cannot move while attacking
        // Except in air
        if (_currentAction == AnimationAction.BASICATK || _currentAction == AnimationAction.SPECIALATK1) {
            if (!_isJumping || !_isFalling) {
                this._dx = 0;
            }
        }

        if (!_isSpecialAtking1 && _specialAtkCounter != 0) {
            _specialAtkCounter = 0;
        }

        if (_isSpecialAtking1) {
            _specialAtkCounter++;
            if (_isFacingRight) {
                this._dx = _moveSpeed * (3 - (_specialAtkCounter * 0.07));
            }
            else {
                this._dx = -_moveSpeed * (3 - (_specialAtkCounter * 0.07));
            }
        }

        if (_isJumping && !_isFalling) {
            this._dy = _jumpStart;
            this._isFalling = true;
        }

        if (_isDoubleJumping && !_alreadyDoubleJumped) {
            this._dy = _doubleJumpStart;
            this._alreadyDoubleJumped = true;
            this._isDoubleJumping = false;
        }

        if (!_isFalling) {
            this._isDoubleJumping = false;
            this._alreadyDoubleJumped = false;
        }

        if (_isFalling) {
            if (_dy > 0 && _isGliding) {
                this._dy += (_fallSpeed * 0.1);
            }
            else {
                this._dy += _fallSpeed;
            }

            if (_dy > 0) {
                this._isJumping = false;
            }
            if (_dy < 0 && !_isJumping) {
                this._dy += _stopJumpSpeed;
            }
            if (_dy > _maxFallSpeed) {
                this._dy = _maxFallSpeed;
            }
        }

        this._maxSpeed = tempMaxSpeed;
    }

    @Override
    public void collision() {

        System.out.println("------Warrior-------");
        System.out.println("X:" + _x);
        System.out.println("Y:" + _y);
        System.out.println("Atk Range Right: " + (_x + _specialAtkRange));
        System.out.println("Atk Range Left: " + (_x - _specialAtkRange));
        System.out.println("Atk Height Up: " + (_y - (_spriteHeight * 0.75)));
        System.out.println("Atk Height Down: " + (_y + (_spriteHeight * 0.75)));

        for (GameObject enemy : gameObjectHandler.enemyObjects) {
            // Check basic atk
            if (_isBasicAtking) {
                if (_isFacingRight) {
                    if (enemy.getX() > _x && enemy.getX() < _x + _basicAtkRange) {
                        if (enemy.getY() > (_y - (_spriteHeight / 2)) && enemy.getY() < (_y + (_spriteHeight / 2))) {
                            enemy.hitByAttack(_basicAtkDamage);
                        }
                    }
                }
                else {
                    if (enemy.getX() < _x && enemy.getX() > _x - _basicAtkRange) {
                        if (enemy.getY() > (_y - (_spriteHeight / 2)) && enemy.getY() < (_y + (_spriteHeight / 2))) {
                            enemy.hitByAttack(_basicAtkDamage);
                        }
                    }
                }
            }

            if (_isSpecialAtking1) {
                if (_isFacingRight) {
                    if (enemy.getX() > _x && enemy.getX() < _x + _specialAtkRange) {
                        if (enemy.getY() > (_y - (_spriteHeight / 2)) && enemy.getY() < (_y + (_spriteHeight / 2))) {
                            enemy.hitByAttack(_specialAtkDamage);
                        }
                    }
                }
                else {
                    if (enemy.getX() < _x && enemy.getX() > _x - _specialAtkRange) {
                        if (enemy.getY() > (_y - (_spriteHeight / 2)) && enemy.getY() < (_y + (_spriteHeight / 2))) {
                            enemy.hitByAttack(_specialAtkDamage);
                        }
                    }
                }
            }

            // Check enemy collision
            if ((!_isSpecialAtking1 || !_isBasicAtking) && this.isThereIntersection(enemy)) {
                this.hitByAttack(enemy.getBasicAtkDamage());
            }
        }

        for (GameObject enemy : gameObjectHandler.bossObjects) {
            // Check basic atk
            if (_isBasicAtking) {
                if (_isFacingRight) {
                    if (enemy.getX() > _x && enemy.getX() < _x + _basicAtkRange) {
                        if (enemy.getY() > (_y - (_spriteHeight / 2)) && enemy.getY() < (_y + (_spriteHeight / 2))) {
                            enemy.hitByAttack(_basicAtkDamage);
                        }
                    }
                }
                else {
                    if (enemy.getX() < _x && enemy.getX() > _x - _basicAtkRange) {
                        if (enemy.getY() > (_y - (_spriteHeight / 2)) && enemy.getY() < (_y + (_spriteHeight / 2))) {
                            enemy.hitByAttack(_basicAtkDamage);
                        }
                    }
                }
            }

            if (_isSpecialAtking1) {
                if (_isFacingRight) {
                    if (enemy.getX() > _x && enemy.getX() < _x + _specialAtkRange) {
                        if (enemy.getY() > (_y - (_spriteHeight * 0.75)) && enemy.getY() < (_y + (_spriteHeight * 0.75))) {
                            enemy.hitByAttack(_specialAtkDamage);
                        }
                    }
                }
                else {
                    if (enemy.getX() < _x && enemy.getX() > _x - _specialAtkRange) {
                        if (enemy.getY() > (_y - (_spriteHeight * 0.75)) && enemy.getY() < (_y + (_spriteHeight * 0.75))) {
                            enemy.hitByAttack(_specialAtkDamage);
                        }
                    }
                }
            }

            // Check enemy collision
            if ((!_isSpecialAtking1 || !_isBasicAtking) && this.isThereIntersection(enemy)) {
                this.hitByAttack(enemy.getBasicAtkDamage());
            }
        }
    }

    public void setKeys() {
        if (_id == ObjectID.PLAYER1) {
            this._movementAndKeyMap.put(KeyEvent.VK_A, Movement.LEFT);
            this._movementAndKeyMap.put(KeyEvent.VK_D, Movement.RIGHT);
            this._movementAndKeyMap.put(KeyEvent.VK_S, Movement.DOWN);
            this._movementAndKeyMap.put(KeyEvent.VK_W, Movement.JUMP);
            this._movementAndKeyMap.put(KeyEvent.VK_SPACE, Movement.BASICATK);
            this._movementAndKeyMap.put(KeyEvent.VK_E, Movement.SPECIALATK);
            this._movementAndKeyMap.put(KeyEvent.VK_SHIFT, Movement.DASH);
            this._movementAndKeyMap.put(KeyEvent.VK_ESCAPE, Movement.PAUSE);
        }
        else if (_id == ObjectID.PLAYER2) {
            this._movementAndKeyMap.put(KeyEvent.VK_LEFT, Movement.LEFT);
            this._movementAndKeyMap.put(KeyEvent.VK_RIGHT, Movement.RIGHT);
            this._movementAndKeyMap.put(KeyEvent.VK_DOWN, Movement.DOWN);
            this._movementAndKeyMap.put(KeyEvent.VK_UP, Movement.JUMP);
            this._movementAndKeyMap.put(KeyEvent.VK_BACK_SLASH, Movement.BASICATK);
            this._movementAndKeyMap.put(KeyEvent.VK_ENTER, Movement.SPECIALATK);
            this._movementAndKeyMap.put(KeyEvent.VK_CONTROL, Movement.DASH);
        }
    }
}
