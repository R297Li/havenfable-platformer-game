package com.AdventureGame.Entity;

import com.AdventureGame.Entity.Misc.Animation;
import com.AdventureGame.Entity.Misc.AnimationAction;
import com.AdventureGame.Entity.Misc.DeathAnimation;
import com.AdventureGame.Entity.Misc.Sprite;
import com.AdventureGame.Handler.GameObjectHandler;
import com.AdventureGame.Handler.Movement;
import com.AdventureGame.TileMap.TileMap;
import com.AdventureGame.main.Game;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.LinkedHashMap;

public abstract class GameObject extends MapObject {

    protected int _health;
    protected int _maxHealth;
    protected boolean _isInvincible;

    protected ObjectID _id;
    protected ObjectState _state;
    protected boolean _isFlinching;
    protected boolean _isSpecialAtking1;
    protected boolean _isSpecialAtking2;
    protected boolean _isSpecialAtking3;
    protected boolean _isBasicAtking;
    protected long _flinchTimeMS;
    protected boolean _isCollided;

    // Special Atk properties
    protected int _specialAtkCharge;
    protected int _maxSpecialAtkCharge;
    protected int _specialAtkCost;
    protected int _specialAtkDamage;

    // Basic Atk properties
    protected int _basicAtkDamage;
    protected int _basicAtkRange;

    // Animation and Sprite Hashmap
    protected LinkedHashMap<AnimationAction, Sprite> _animationAndSpriteMap;

    // Movement and keybind Hashmap
    protected HashMap<Integer, Movement> _movementAndKeyMap;

    protected GameObjectHandler gameObjectHandler;

    public GameObject(TileMap tileMap, GameObjectHandler gameObjectHandler) {
        super(tileMap);
        this.gameObjectHandler = gameObjectHandler;
    }

    public int getHealth() {
        return _health;
    }

    public int getMaxHealth() {
        return _maxHealth;
    }

    public void setHealth(int health) { this._health = health; }

    public void setMaxHealth(int health) { this._maxHealth = health; }

    public boolean isInvincible() {
        return _isInvincible;
    }

    public void setIsInvincible() { this._isInvincible = true; }

    public int getSpecialAtkCharge() {
        return _specialAtkCharge;
    }

    public int getMaxSpecialAtkCharge() {
        return _maxSpecialAtkCharge;
    }

    public int getBasicAtkDamage() {
        return _basicAtkDamage;
    }

    public int getSpecialAtkDamage() {
        return _specialAtkDamage;
    }

    public void setState(ObjectState state) {
        this._state = state;
    }

    public ObjectState getState() {
        return _state;
    }

    public boolean isDead() {
        return (_health <= 0);
    }

    public void setIsSpecialAtking1() {
        this._isSpecialAtking1 = true;
    }

    public void setIsSpecialAtking2() {
        this._isSpecialAtking2 = true;
    }

    public void setIsSpecialAtking3() {
        this._isSpecialAtking3 = true;
    }

    public void setIsBasicAtking() {
        this._isBasicAtking = true;
    }

    public void setSpecialAtkDamage(int specialAtkDamage) { this._specialAtkDamage = specialAtkDamage; }

    public boolean isSpecialAtking() {
        return _isSpecialAtking1;
    }

    public boolean isBasicAtking() {
        return _isBasicAtking;
    }

    public boolean isFalling() { return _isFalling; }

    public boolean hasJumpedOnce() { return _hasJumpedOnce; }

    public ObjectID getID() {
        return _id;
    }

    public void setID(ObjectID id) {
        this._id = id;
    }

    public boolean getIsCollided() { return _isCollided; }

    public void setIsCollided() { this._isCollided = true; }

    public void setIsFlinching() { this._isFlinching = true; }

    public HashMap<Integer, Movement> getMovementAndKeyMap() {
        return _movementAndKeyMap;
    }

    public void setNextChangeInHorizontalPosition() {
        if (_isMovingLeft) {
            this._dx -= _moveSpeed;
            this._dx = Game.clamp(_dx, -_maxSpeed, 0);
        }
        else if (_isMovingRight) {
            this._dx += _moveSpeed;
            this._dx = Game.clamp(_dx, 0, _maxSpeed);
        }
        else {
            if (_dx > 0) {
                this._dx -= _stopSpeed;
                this._dx = Game.clamp(_dx, 0, _maxSpeed);
            }
            else if (_dx < 0) {
                this._dx += _stopSpeed;
                this._dx = Game.clamp(_dx, -_maxSpeed, 0);
            }
        }
    }

    public void setAnimation(BufferedImage[] sprites, int delayBetweenFramesMS) {
        if (_animation == null) {
            this._animation = new Animation();
        }
        _animation.setFrames(sprites);
        _animation.setDelayBetweenFrames(delayBetweenFramesMS);
    }

    public void hitByAttack(int damage) {
        if (_isFlinching || _state == ObjectState.DEAD || _isInvincible) {
            return;
        }

        this._health -= damage;
        if (_health <= 0) {
            this._health = 0;
            this._state = ObjectState.DEAD;

            if (this._id == ObjectID.ENEMY || this._id == ObjectID.BOSS) {
                gameObjectHandler.addGameObject(new DeathAnimation(_tileMap, gameObjectHandler, (int)this._x, (int)this._y));
            }

        }
        this._isFlinching = true;
        this._flinchTimeMS = System.nanoTime();
    }

    public void setAnimationAction(AnimationAction action, int delayBetweenFramesMS, int spriteWidth) {
        if (_currentAction != action) {
            this._currentAction = action;
            setAnimation(_animationAndSpriteMap.get(action).getImages(), delayBetweenFramesMS);
            this._spriteWidth = spriteWidth;
        }
    }

    public void respawnPlayer() {
        if (isFallingOutOfWorld()) {
            int health = getHealth() - 2;
            int maxHealth = getMaxHealth() - 2;
            setHealth(health);
            setMaxHealth(maxHealth);
        }

        if (isDead()) {
            setState(ObjectState.DEAD);
        }
        else {
            setIsFlinching();
        }

        setPosition(100,190);

        if (isOnScreen()) {
            setState(ObjectState.ALIVE);
        }
    }

    public abstract void update();
    public abstract void collision();
}
