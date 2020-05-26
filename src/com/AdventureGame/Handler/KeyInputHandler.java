package com.AdventureGame.Handler;

import com.AdventureGame.Entity.GameObject;
import com.AdventureGame.Entity.ObjectID;
import com.AdventureGame.Entity.Players.Player;
import com.AdventureGame.GameState.GameStateManager;
import com.AdventureGame.GameState.MenuSelection;
import com.AdventureGame.GameState.Pause;
import com.AdventureGame.GameState.State;

import java.util.HashMap;
import java.util.Map;

public class KeyInputHandler {

    private GameStateManager _gsm;
    private GameObjectHandler _gameObjectHandler;
    private Player _player1;
    private Player _player2;
    private HashMap<Integer, Movement> _player1KeyInput;
    private HashMap<Integer, Movement> _player2KeyInput;

    private HashMap<Movement, Boolean> _player1PreviousKeyInput;
    private HashMap<Movement, Boolean> _player2PreviousKeyInput;

    public KeyInputHandler(GameStateManager gsm, GameObjectHandler gameObjectHandler) {
        this._gsm = gsm;
        this._gameObjectHandler = gameObjectHandler;
        this._player1PreviousKeyInput = new HashMap<Movement, Boolean>();
        this._player2PreviousKeyInput = new HashMap<Movement, Boolean>();
    }

    public void init() {
        for (GameObject player : _gameObjectHandler.playerObjects) {
            if (player.getID() == ObjectID.PLAYER1) {
                this._player1 = (Player)player;
                this._player1KeyInput = _player1.getMovementAndKeyMap();
            }
            else if (player.getID() == ObjectID.PLAYER2) {
                this._player2 = (Player)player;
                this._player2KeyInput = _player2.getMovementAndKeyMap();
            }
        }

        if (_player1 != null) {
            for (Map.Entry<Integer, Movement> entry : _player1KeyInput.entrySet()) {
                Movement movement = entry.getValue();

                _player1PreviousKeyInput.put(movement, false);
            }
        }

        if (_player2 != null) {
            for (Map.Entry<Integer, Movement> entry : _player2KeyInput.entrySet()) {
                Movement movement = entry.getValue();

                _player2PreviousKeyInput.put(movement, false);
            }
        }
    }

    public void keyPressed(int k) {
        Movement movement = _player1KeyInput.get(k);

        if (movement != null) {
            boolean value = _player1PreviousKeyInput.get(movement);

            if (!value) {
                setMovement(_player1, movement, true);
                _player1PreviousKeyInput.replace(movement, true);

            }
        }
        else if (_gameObjectHandler.numPlayerSelection == MenuSelection.TWOPLAYERS) {
            movement = _player2KeyInput.get(k);

            if (movement != null) {
                boolean value = _player2PreviousKeyInput.get(movement);

                if (!value) {
                    setMovement(_player2, movement, true);
                    _player2PreviousKeyInput.replace(movement, true);
                }
            }
        }
    }

    public void keyReleased(int k) {
        Movement movement = _player1KeyInput.get(k);

        if (movement != null) {
            boolean value = _player1PreviousKeyInput.get(movement);

            if (value) {
                setMovement(_player1, movement, false);
                _player1PreviousKeyInput.replace(movement, false);
            }
        }
        else if (_gameObjectHandler.numPlayerSelection == MenuSelection.TWOPLAYERS) {
            movement = _player2KeyInput.get(k);

            if (movement != null) {
                boolean value = _player2PreviousKeyInput.get(movement);

                if (value) {
                    setMovement(_player2, movement, false);
                    _player2PreviousKeyInput.replace(movement, false);
                }
            }
        }
    }

    private void setMovement(Player player, Movement movement, boolean value) {
        if (movement == Movement.PAUSE) {
            if (value == true) {
                State currentState = _gsm.getCurrentState();

                if (_gsm.getGameState(State.PAUSE) == null) {
                    _gsm.addState(State.PAUSE, new Pause(_gsm, _gameObjectHandler, this, currentState));
                }
                _gsm.setState(State.PAUSE);
            }
            else {
                _gsm.removeState(State.PAUSE);
            }
        }
        else {
            if (movement == Movement.LEFT) {
                player.setMovingLeft(value);
            } else if (movement == Movement.RIGHT) {
                player.setMovingRight(value);
            } else if (movement == Movement.JUMP) {
                player.setJumping(value);
            } else if (movement == Movement.DOWN) {
                player.setMovingDown(value);
            } else if (movement == Movement.GLIDE) {
                player.setGliding(value);
            } else if (movement == Movement.DASH) {
                if (!player.isFalling()) {
                    player.setDashing(value);
                }
            } else if (movement == Movement.BASICATK && value == true) {
                if (!player.isBasicAtking()) {
                    player.setIsBasicAtking();
                }
            } else if (movement == Movement.SPECIALATK && value == true) {
                if (!player.isSpecialAtking()) {
                    player.setIsSpecialAtking1();
                }
            }
        }
    }
}
