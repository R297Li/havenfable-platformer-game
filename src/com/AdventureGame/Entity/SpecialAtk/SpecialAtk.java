package com.AdventureGame.Entity.SpecialAtk;

import com.AdventureGame.Entity.GameObject;
import com.AdventureGame.Entity.ObjectState;
import com.AdventureGame.Entity.Misc.AnimationAction;
import com.AdventureGame.Handler.GameObjectHandler;
import com.AdventureGame.TileMap.TileMap;

import java.awt.*;

public class SpecialAtk extends GameObject {

    public SpecialAtk(TileMap tileMap, GameObjectHandler gameObjectHandler) {
        super(tileMap, gameObjectHandler);
    }

    @Override
    public void update() {
        checkTileMapCollision();
        setPosition(_xTemp, _yTemp);

        if (_dx == 0) {
            setIsCollided();
        }

        _animation.update();

        if (_isCollided && _currentAction != AnimationAction.DEAD) {
            setAnimationAction(AnimationAction.DEAD, 70, _spriteWidth);
            this._dx = 0;
        }

        if (_isCollided && _animation.hasPlayedOnce()) {
            this._state = ObjectState.DEAD;
            gameObjectHandler.removeGameObject(this);
        }
    }

    @Override
    public void collision() {}

    @Override
    public void draw(Graphics2D g) {
        setMapPosition();
        drawNextPosition(g);
    }
}
