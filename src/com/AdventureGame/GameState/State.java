package com.AdventureGame.GameState;

import java.util.HashMap;
import java.util.Map;

public enum State {
    MENU(0),
    PAUSE(1),
    END(2),
    LEVEL1(3),
    LEVEL2(4);

    private int _value;
    private static Map _map = new HashMap<>();

    private State(int value) {
        this._value = value;
    }

    static {
        for (State state : State.values()) {
            _map.put(state._value, state);
        }
    }

    public static State valueOf(int state) {
        return (State)_map.get(state);
    }

    public int getValue() {
        return this._value;
    }
}
