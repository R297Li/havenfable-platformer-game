package com.AdventureGame.Entity.Misc;

import com.AdventureGame.Entity.GameObject;
import com.AdventureGame.Entity.ObjectID;
import com.AdventureGame.Entity.ObjectState;
import com.AdventureGame.Handler.GameObjectHandler;
import com.AdventureGame.Handler.ResourceHandler;
import com.AdventureGame.Handler.ResourceType;
import com.AdventureGame.TileMap.TileMap;

import java.awt.*;

public class Portal extends GameObject {

    private boolean _isPlayerInPortal;

    public Portal(TileMap tileMap, GameObjectHandler gameObjectHandler, ResourceType resourceType, int x, int y) {
        super(tileMap, gameObjectHandler);

        this._state = ObjectState.ALIVE;
        this._id = ObjectID.PORTAL;
        this._x = x;
        this._y = y;

        this._isPlayerInPortal = false;
        this._isFacingRight = false;

        this._spriteWidth = 64;
        this._spriteHeight = 64;
        this._collisionWidth = 2;
        this._collisionHeight = 34;

        switch (resourceType) {
            case PURPLE_PORTAL:
                this._animationAndSpriteMap = ResourceHandler.portalPurpleAnimationSpriteMap;
                break;
            case GREEN_PORTAL:
                this._animationAndSpriteMap = ResourceHandler.portalGreenAnimationSpriteMap;
                break;
        }

        setAnimationAction(AnimationAction.SPAWN, 100, _spriteWidth);
    }

    public boolean isPlayerInPortal() {
        return _isPlayerInPortal;
    }

    @Override
    public void update() {
        getNextPosition();
        checkTileMapCollision();
        setPosition(_xTemp, _yTemp);

        if (_currentAction == AnimationAction.SPAWN) {
            if (_animation.hasPlayedOnce()) {
                setAnimationAction(AnimationAction.IDLE, 100, _spriteWidth);
            }
        }

        _animation.update();
        collision();
    }

    @Override
    public void collision() {
        for (GameObject player : gameObjectHandler.playerObjects) {
            if (isThereIntersection(player)) {
                this._isPlayerInPortal = true;
            }
        }
    }

    @Override
    public void draw(Graphics2D g) {
        setMapPosition();
        drawNextPosition(g);
    }

    public void setSprites() {
        this._animationAndSpriteMap.put(AnimationAction.IDLE, new Sprite(AnimationAction.IDLE, 8, 0));
        this._animationAndSpriteMap.put(AnimationAction.SPAWN, new Sprite(AnimationAction.SPAWN, 8, 1));
    }

    private void getNextPosition() {
        setNextChangeInHorizontalPosition();
    }
}
