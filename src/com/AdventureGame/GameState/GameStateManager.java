package com.AdventureGame.GameState;

import com.AdventureGame.Entity.Misc.Timer;
import com.AdventureGame.Handler.GameObjectHandler;
import com.AdventureGame.Handler.KeyInputHandler;
import com.AdventureGame.Handler.ResourceHandler;

import java.awt.*;
import java.util.HashMap;

public class GameStateManager {

    private HashMap<State, GameState> _gameStateMap;
    private State _currentState;
    private GameObjectHandler _gameObjectHandler;
    private KeyInputHandler _keyInputHandler;

    public GameStateManager() {

        this._gameStateMap = new HashMap<State, GameState>();
        this._gameObjectHandler = new GameObjectHandler();
        this._keyInputHandler = new KeyInputHandler(this, _gameObjectHandler);
        new ResourceHandler();
        new Timer();

        this._currentState = State.MENU;
        _gameStateMap.put(_currentState, new Menu(this, _gameObjectHandler, _keyInputHandler));
    }

    public void addState(State state, GameState gameState) {
        _gameStateMap.put(state, gameState);
    }

    public void setState(State state) {
        this._currentState = state;
    }

    public void removeState(State state) {
        _gameStateMap.remove(state);
    }

    public State getCurrentState() {
        return _currentState;
    }

    public GameState getCurrentGameState() {
        return _gameStateMap.get(_currentState);
    }

    public GameState getGameState(State state) {
        return _gameStateMap.get(state);
    }

    public void update() {
        _gameStateMap.get(_currentState).update();
    }

    public void draw(Graphics2D g) {
        _gameStateMap.get(_currentState).draw(g);
    }

    public void keyPressed(int k) {
        _gameStateMap.get(_currentState).keyPressed(k);
    }

    public void keyReleased(int k) {
        _gameStateMap.get(_currentState).keyReleased(k);
    }
}
