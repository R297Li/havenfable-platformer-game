package com.AdventureGame.Entity.Enemies;

import com.AdventureGame.Entity.GameObject;
import com.AdventureGame.Handler.GameObjectHandler;
import com.AdventureGame.TileMap.TileMap;

public abstract class Enemy extends GameObject {

    public Enemy(TileMap tileMap, GameObjectHandler gameObjectHandler) {
        super(tileMap, gameObjectHandler);
    }


    public boolean isDirectlyFacingPlayer() {
        for (GameObject player : gameObjectHandler.playerObjects) {
            if (this.isOnScreen() && player.isOnScreen()) {
                if (_isFacingRight) {
                    if (_x < player.getX() && _y - (_spriteHeight / 2) <= player.getY() && _y + (_spriteHeight / 2) >= player.getY()) {
                        return true;
                    }
                }
                else {
                    if (_x > player.getX() && _y - (_spriteHeight / 2) <= player.getY() && _y + (_spriteHeight / 2) >= player.getY()) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public boolean isFacingPlayerDirection() {
        for (GameObject player : gameObjectHandler.playerObjects) {
            if (this.isOnScreen() && player.isOnScreen()) {
                if (_isFacingRight) {
                    if (_x < player.getX()) {
                        return true;
                    }
                }
                else {
                    if (_x > player.getX()) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public boolean isPlayerInRange(int range) {
        for (GameObject player : gameObjectHandler.playerObjects) {
            if (this.isOnScreen() && player.isOnScreen()) {
                if (_isFacingRight) {
                    if (_x < player.getX() && (_x + range) > player.getX()) {
                        if ((_y - (_spriteHeight / 2)) < player.getY() && (_y + (_spriteHeight / 2)) > player.getY()) {
                            return true;
                        }
                    }
                }
                else {
                    if (_x > player.getX() && (_x - range) < player.getX()) {
                        if ((_y - (_spriteHeight / 2)) < player.getY() && (_y + (_spriteHeight / 2)) > player.getY()) {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }
}
