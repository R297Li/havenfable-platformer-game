package com.AdventureGame.Entity.Players;

import com.AdventureGame.Entity.*;
import com.AdventureGame.Entity.SpecialAtk.FireBall;
import com.AdventureGame.Entity.Misc.AnimationAction;
import com.AdventureGame.Handler.GameObjectHandler;
import com.AdventureGame.Handler.Movement;
import com.AdventureGame.Handler.ResourceHandler;
import com.AdventureGame.Handler.ResourceType;
import com.AdventureGame.TileMap.TileMap;
import com.AdventureGame.main.Game;

import java.awt.event.KeyEvent;
import java.util.HashMap;

public class Dragon extends Player {

    public Dragon(TileMap tileMap, String spritePath, GameObjectHandler gameObjectHandler, ObjectID id) {
        super(tileMap, spritePath, gameObjectHandler);

        this._state = ObjectState.ALIVE;
        this._id = id;

        this._movementAndKeyMap = new HashMap<Integer, Movement>();
        setKeys();

        this._spriteWidth = 30;
        this._spriteHeight = 30;
        this._collisionWidth = 20;
        this._collisionHeight = 20;

        this._moveSpeed = 0.3;
        this._maxSpeed = 1.6;
        this._stopSpeed = 0.4;
        this._fallSpeed = 0.15;
        this._maxFallSpeed = 4.0;
        this._jumpStart = -4.8;
        this._stopJumpSpeed = 0.3;

        this._isFacingRight = true;

        this._health = 8;
        this._maxHealth = 8;

        this._specialAtkCharge = 2500;
        this._maxSpecialAtkCharge = 2500;

        this._specialAtkCost = 200;
        this._specialAtkDamage = 5;

        this._basicAtkDamage = 2;
        this._basicAtkRange = 40;
        this._animationAndSpriteMap = ResourceHandler.dragonAnimationSpriteMap;

        this._currentAction = AnimationAction.IDLE;
        setAnimation(_animationAndSpriteMap.get(AnimationAction.IDLE).getImages(), 400);
    }

    public void update() {
        // Update Position
        getNextPosition();
        checkTileMapCollision();
        setPosition(_xTemp, _yTemp);

        if (_currentAction == AnimationAction.BASICATK || _currentAction == AnimationAction.SPECIALATK1) {
            if (_animation.hasPlayedOnce()) {
                this._isBasicAtking = false;
                this._isSpecialAtking1 = false;
            }
        }

        this._specialAtkCharge += 1;
        this._specialAtkCharge = Game.clamp(_specialAtkCharge, 0, _maxSpecialAtkCharge);

        if (_isSpecialAtking1 && _currentAction != AnimationAction.SPECIALATK1) {
            if (_specialAtkCharge > _specialAtkCost) {
                this._specialAtkCharge -= _specialAtkCost;

                FireBall fireBall = new FireBall(_tileMap, _isFacingRight, gameObjectHandler, ResourceType.ORANGE_FIREBALL);
                fireBall.setPosition(_x, _y);
                fireBall.setSpecialAtkDamage(_specialAtkDamage);
                gameObjectHandler.addGameObject(fireBall);
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
            setAnimationAction(AnimationAction.BASICATK, 50, 60);
        }
        else if (_isSpecialAtking1) {
            setAnimationAction(AnimationAction.SPECIALATK1, 100, 30);
        }
        // Descending
        else if (_dy > 0) {
            if (_isGliding) {
                setAnimationAction(AnimationAction.GLIDING, 100, 30);
            }
            else if (_currentAction != AnimationAction.FALLING) {
                setAnimationAction(AnimationAction.FALLING, 100, 30);
            }
        }
        // Flying/In air
        else if (_dy < 0) {
            setAnimationAction(AnimationAction.JUMPING, -1, 30);
        }
        else if (_isMovingLeft || _isMovingRight) {
            setAnimationAction(AnimationAction.WALKING, 40, 30);
        }
        else {
            setAnimationAction(AnimationAction.IDLE, 400, 30);
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
        setNextChangeInHorizontalPosition();

        // Cannot move while attacking
        // Except in air
        if (_currentAction == AnimationAction.BASICATK || _currentAction == AnimationAction.SPECIALATK1) {
            if (!_isJumping || !_isFalling) {
                this._dx = 0;
            }
        }

        if (_isJumping && !_isFalling) {
            this._dy = _jumpStart;
            this._isFalling = true;
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
    }

    public void setKeys() {
        if (_id == ObjectID.PLAYER1) {
            this._movementAndKeyMap.put(KeyEvent.VK_A, Movement.LEFT);
            this._movementAndKeyMap.put(KeyEvent.VK_D, Movement.RIGHT);
            this._movementAndKeyMap.put(KeyEvent.VK_S, Movement.DOWN);
            this._movementAndKeyMap.put(KeyEvent.VK_W, Movement.JUMP);
            this._movementAndKeyMap.put(KeyEvent.VK_SPACE, Movement.BASICATK);
            this._movementAndKeyMap.put(KeyEvent.VK_E, Movement.SPECIALATK);
            this._movementAndKeyMap.put(KeyEvent.VK_SHIFT, Movement.GLIDE);
            this._movementAndKeyMap.put(KeyEvent.VK_ESCAPE, Movement.PAUSE);
        }
        else if (_id == ObjectID.PLAYER2) {
            this._movementAndKeyMap.put(KeyEvent.VK_LEFT, Movement.LEFT);
            this._movementAndKeyMap.put(KeyEvent.VK_RIGHT, Movement.RIGHT);
            this._movementAndKeyMap.put(KeyEvent.VK_DOWN, Movement.DOWN);
            this._movementAndKeyMap.put(KeyEvent.VK_UP, Movement.JUMP);
            this._movementAndKeyMap.put(KeyEvent.VK_BACK_SLASH, Movement.BASICATK);
            this._movementAndKeyMap.put(KeyEvent.VK_ENTER, Movement.SPECIALATK);
            this._movementAndKeyMap.put(KeyEvent.VK_CONTROL, Movement.GLIDE);
        }
    }
}
