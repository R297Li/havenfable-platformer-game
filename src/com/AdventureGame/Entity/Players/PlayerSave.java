package com.AdventureGame.Entity.Players;

import com.AdventureGame.Entity.ObjectID;

public class PlayerSave {

    private ObjectID _playerId;
    private int _health;

    public PlayerSave(ObjectID playerId, int health) {
        this._playerId = playerId;
        this._health = health;
    }

    public ObjectID getPlayerId() {
        return _playerId;
    }

    public int getHealth() {
        return _health;
    }

    public void setPlayerId(ObjectID playerId) {
        this._playerId = playerId;
    }

    public void setHealth(int health) {
        this._health = health;
    }

}
