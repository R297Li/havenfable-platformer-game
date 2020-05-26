package com.AdventureGame.TileMap;

import com.AdventureGame.GameState.State;

import java.util.HashMap;
import java.util.Map;

public enum TileType {
    NORMAL(0),
    BLOCK(1);

    private int _value;
    private static Map _map = new HashMap<>();

    private TileType(int value) {
        this._value = value;
    }

    static {
        for (TileType tileType : TileType.values()) {
            _map.put(tileType._value, tileType);
        }
    }

    public static TileType valueOf(int tileType) {
        return (TileType) _map.get(tileType);
    }

    public int getValue() {
        return this._value;
    }
}
