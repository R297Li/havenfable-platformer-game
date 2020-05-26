package com.AdventureGame.Entity.Misc;

import com.AdventureGame.Entity.GameObject;
import com.AdventureGame.Entity.Misc.AnimationAction;
import com.AdventureGame.Entity.ObjectID;
import com.AdventureGame.Entity.ObjectState;
import com.AdventureGame.Handler.GameObjectHandler;
import com.AdventureGame.Handler.ResourceHandler;
import com.AdventureGame.TileMap.TileMap;

import java.awt.*;
import java.awt.image.BufferedImage;

public class DeathAnimation extends GameObject {

    public DeathAnimation(TileMap tileMap, GameObjectHandler gameObjectHandler, int x, int y) {
        super(tileMap, gameObjectHandler);

        this._id = ObjectID.EXPLOSION;
        this._state = ObjectState.ALIVE;

        this._x = x;
        this._y = y;

        this._spriteWidth = 30;
        this._spriteHeight = 30;

        this._animationAndSpriteMap = ResourceHandler.deathAnimationSpriteMap;
        setAnimationAction(AnimationAction.IDLE, 70, _spriteWidth);
    }

    public void update() {
        _animation.update();
        if (_animation.hasPlayedOnce()) {
            gameObjectHandler.removeGameObject(this);
        }
    }

    @Override
    public void collision() {}

    public void draw(Graphics2D g) {
        setMapPosition();

        BufferedImage image = _animation.getImage();
        double screenXPosition = _x + _mapXPosition - (_spriteWidth / 2);
        double screenYPosition = _y + _mapYPosition - (_spriteHeight / 2);

        g.drawImage(image, (int)screenXPosition, (int)screenYPosition, null);
    }

}
