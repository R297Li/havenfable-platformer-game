package com.AdventureGame.GameState;

import com.AdventureGame.Handler.GameObjectHandler;
import com.AdventureGame.Handler.KeyInputHandler;

import java.awt.*;

public abstract class GameState {

    protected GameStateManager _gsm;
    protected GameObjectHandler _gameObjectHandler;
    protected KeyInputHandler _keyInputHandler;

    public abstract void init();
    public abstract void update();
    public abstract void draw(Graphics2D g);
    public abstract void keyPressed(int k);
    public abstract void keyReleased(int k);
}
