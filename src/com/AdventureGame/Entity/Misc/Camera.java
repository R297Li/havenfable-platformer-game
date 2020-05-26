package com.AdventureGame.Entity.Misc;

import com.AdventureGame.Entity.GameObject;
import com.AdventureGame.Entity.ObjectID;
import com.AdventureGame.Entity.Players.Player;
import com.AdventureGame.GameState.MenuSelection;
import com.AdventureGame.Handler.GameObjectHandler;
import com.AdventureGame.TileMap.Background;
import com.AdventureGame.TileMap.TileMap;
import com.AdventureGame.main.GamePanel;

public class Camera {

    private TileMap _tileMap;
    private Background _background;
    private GameObjectHandler _gameObjectHandler;
    private Player _player1;
    private Player _player2;


    public Camera(TileMap tileMap, Background background, GameObjectHandler gameObjectHandler) {
        this._tileMap = tileMap;
        this._background = background;
        this._gameObjectHandler = gameObjectHandler;

        init();
    }

    public void init() {
        for (GameObject player : _gameObjectHandler.playerObjects) {
            if (player.getID() == ObjectID.PLAYER1) {
                this._player1 = (Player)player;
            }
            if (player.getID() == ObjectID.PLAYER2) {
                this._player2 = (Player)player;
            }
        }
    }

    public void update() {
        double x = (GamePanel.WIDTH / 2) - _player1.getX();
        double y = (GamePanel.HEIGHT / 2) - _player1.getY();

        if (_gameObjectHandler.numPlayerSelection == MenuSelection.TWOPLAYERS) {
            x = (GamePanel.WIDTH / 2) - (_player1.getX() + _player2.getX()) / 2;
            y = (GamePanel.HEIGHT / 2) - (_player1.getY() + _player2.getY()) / 2;
        }

        _tileMap.setPosition(x, y);
        _background.setPosition(_tileMap.getX(), _tileMap.getY());
    }
}
