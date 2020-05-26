package com.AdventureGame.GameState;

import java.util.HashMap;
import java.util.Map;

public enum MenuSelection {
    START("START"),
    HELP("HELP"),
    QUIT("QUIT"),
    RESUME("RESUME"),
    RESTART("RESTART"),
    MAINMENU("MAIN MENU"),
    ONEPLAYER("1 PLAYER"),
    TWOPLAYERS("2 PLAYERS"),
    DRAGON("DRAGON"),
    WARRIOR("WARRIOR");

    private String _string;
    private static Map _map = new HashMap<>();

    private MenuSelection(String string) {
        this._string = string;
    }

    static {
        for (MenuSelection menuSelection : MenuSelection.values()) {
            _map.put(menuSelection._string, menuSelection);
        }
    }

    public String getString() {
        return this._string;
    }
}


