package com.AdventureGame.Entity.SpecialAtk;

import com.AdventureGame.Entity.ObjectID;
import com.AdventureGame.Entity.ObjectState;
import com.AdventureGame.Entity.Misc.AnimationAction;
import com.AdventureGame.Handler.GameObjectHandler;
import com.AdventureGame.Handler.ResourceHandler;
import com.AdventureGame.Handler.ResourceType;
import com.AdventureGame.TileMap.TileMap;

public class FireBall extends SpecialAtk {

    public FireBall(TileMap tileMap, boolean isFiredRight, GameObjectHandler gameObjectHandler, ResourceType resourceType) {
        super(tileMap, gameObjectHandler);

        this._state = ObjectState.ALIVE;
        this._id = ObjectID.PLAYER_SPECIALATK;
        this._isFacingRight = isFiredRight;
        this._moveSpeed = 3.8;
        this._specialAtkDamage = 2;

        this._dx = (isFiredRight) ? _moveSpeed : -_moveSpeed;

        this._spriteWidth = 30;
        this._spriteHeight = 30;
        this._collisionWidth = 14;
        this._collisionHeight = 14;

        switch (resourceType) {
            case ORANGE_FIREBALL:
                this._animationAndSpriteMap = ResourceHandler.fireBallOrangeAnimationSpriteMap;
                break;
            case PURPLE_FIREBALL:
                this._animationAndSpriteMap = ResourceHandler.fireBallPurpleAnimationSpriteMap;
                break;
        }

        setAnimationAction(AnimationAction.SPECIALATK1, 70, _spriteWidth);
    }
}
