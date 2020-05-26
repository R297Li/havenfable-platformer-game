package com.AdventureGame.Entity.Players;

import com.AdventureGame.Entity.Misc.AnimationAction;
import com.AdventureGame.Entity.GameObject;
import com.AdventureGame.Handler.GameObjectHandler;
import com.AdventureGame.TileMap.TileMap;

import java.awt.*;

public class Player extends GameObject {

    // Gliding
    protected boolean _isGliding;


    public Player(TileMap tileMap, String spritePath, GameObjectHandler gameObjectHandler) {
        super(tileMap, gameObjectHandler);
    }

    public void setGliding(boolean isGliding) {
        this._isGliding = isGliding;
    }
    public void setDashing(boolean isDashing) { this._isDashing = isDashing; }

    @Override
    public void update() {}

    @Override
    public void collision() {

        for (GameObject enemy : gameObjectHandler.enemyObjects) {
            // Check basic atk
            if (_isBasicAtking) {
                if (_isFacingRight) {
                    if (enemy.getX() > _x && enemy.getX() < _x + _basicAtkRange) {
                        if (enemy.getY() >= (_y - (_spriteHeight / 2)) && enemy.getY() <= (_y + (_spriteHeight / 2))) {
                            enemy.hitByAttack(_basicAtkDamage);
                        }
                    }
                }
                else {
                    if (enemy.getX() < _x && enemy.getX() > _x - _basicAtkRange) {
                        if (enemy.getY() >= (_y - (_spriteHeight / 2)) && enemy.getY() <= (_y + (_spriteHeight / 2))) {
                            enemy.hitByAttack(_basicAtkDamage);
                        }
                    }
                }
            }

            for (GameObject gameObject : gameObjectHandler.playerSpecialAtkObjects) {
                if (gameObject.isThereIntersection(enemy)) {
                    enemy.hitByAttack(gameObject.getSpecialAtkDamage());
                    gameObject.setIsCollided();
                }
            }

            // Check enemy collision
            if (this.isThereIntersection(enemy)) {
                this.hitByAttack(enemy.getBasicAtkDamage());
            }
        }

        for (GameObject boss : gameObjectHandler.bossObjects) {
            // Check basic atk
            if (_isBasicAtking) {
                if (_isFacingRight) {
                    if (boss.getX() > _x && boss.getX() < _x + _basicAtkRange) {
                        if (boss.getY() >= (_y - (_spriteHeight / 2)) && boss.getY() <= (_y + (_spriteHeight / 2))) {
                            boss.hitByAttack(_basicAtkDamage);
                        }
                    }
                }
                else {
                    if (boss.getX() < _x && boss.getX() > _x - _basicAtkRange) {
                        if (boss.getY() >= (_y - (_spriteHeight / 2)) && boss.getY() <= (_y + (_spriteHeight / 2))) {
                            boss.hitByAttack(_basicAtkDamage);
                        }
                    }
                }
            }

            for (GameObject gameObject : gameObjectHandler.playerSpecialAtkObjects) {
                if (gameObject.isThereIntersection(boss)) {
                    boss.hitByAttack(gameObject.getSpecialAtkDamage());
                    gameObject.setIsCollided();
                }
            }
        }
    }

    public void draw(Graphics2D g) {
        setMapPosition();
        // Draw Player
        if (_isFlinching) {
            long elapsed = (System.nanoTime() - _flinchTimeMS) / 1000000;

            // Blink every 100ms
            if ((elapsed / 100) % 2 == 0) {
                return;
            }
        }

        drawNextPosition(g);
    }

    public void setAnimationAction(AnimationAction action, int delayBetweenFramesMS, int spriteWidth) {
        if (_currentAction != action) {
            this._currentAction = action;
            setAnimation(_animationAndSpriteMap.get(action).getImages(), delayBetweenFramesMS);
            this._spriteWidth = spriteWidth;
        }
    }
}
