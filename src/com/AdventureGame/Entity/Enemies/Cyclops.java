package com.AdventureGame.Entity.Enemies;

import com.AdventureGame.Entity.ObjectID;
import com.AdventureGame.Entity.ObjectState;
import com.AdventureGame.Entity.Players.Player;
import com.AdventureGame.Entity.Misc.AnimationAction;
import com.AdventureGame.Handler.GameObjectHandler;
import com.AdventureGame.Handler.ResourceHandler;
import com.AdventureGame.TileMap.TileMap;
import com.AdventureGame.main.Game;

import java.awt.*;

public class Cyclops extends Enemy {

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

    public Cyclops(TileMap tileMap, GameObjectHandler gameObjectHandler) {
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

        this._health = 10;
        this._maxHealth = 25;
        this._specialAtkDamage = 2;
        this._basicAtkDamage = 1;
        this._basicAtkRange = 30;

        this._specialAtkCharge = 3000;
        this._maxSpecialAtkCharge = 3000;
        this._specialAtkCost = 100;

        this._nextActionState = 0;
        this._previousActionState= 0;

        this._animationAndSpriteMap = ResourceHandler.cyclopsAnimationSpriteMap;

        this._currentAction = AnimationAction.IDLE;
        setAnimation(_animationAndSpriteMap.get(AnimationAction.IDLE).getImages(), 400);
    }

    @Override
    public void update() {
        getNextPosition();
        checkTileMapCollision();
        setPosition(_xTemp, _yTemp);
    }

    @Override
    public void collision() {

    }

    @Override
    public void draw(Graphics2D g) {

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
}
