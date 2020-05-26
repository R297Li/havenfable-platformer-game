package com.AdventureGame.GameState;

import com.AdventureGame.Entity.*;
import com.AdventureGame.Entity.Enemies.Imp;
import com.AdventureGame.Entity.Enemies.Minotaur;
import com.AdventureGame.Entity.Enemies.Snail;
import com.AdventureGame.Entity.Misc.*;
import com.AdventureGame.Entity.Players.Dragon;
import com.AdventureGame.Entity.Players.Player;
import com.AdventureGame.Entity.Players.Warrior;
import com.AdventureGame.Handler.GameObjectHandler;
import com.AdventureGame.Handler.KeyInputHandler;
import com.AdventureGame.Handler.ResourceHandler;
import com.AdventureGame.Handler.ResourceType;
import com.AdventureGame.TileMap.Background;
import com.AdventureGame.TileMap.TileMap;

import java.awt.*;

public class LevelTwo extends GameState {

    private TileMap _tileMap;
    private Background _background;

    private Player _player1;
    private Player _player2;
    private HUD _hudPlayer1;
    private HUD _hudPlayer2;
    private boolean _isBossBattle;
    private boolean _isBoss1Dead;
    private boolean _isBoss2Dead;
    private boolean _isBossBattleDone;

    private Portal portal;
    private Transition _transition;
    private Camera _camera;

    public LevelTwo(GameStateManager gsm, GameObjectHandler gameObjectHandler, KeyInputHandler keyInputHandler) {
        this._gsm = gsm;
        this._gameObjectHandler = gameObjectHandler;
        this._keyInputHandler = keyInputHandler;
        init();
        setTransition(TransitionType.TRANSITION_OUT);
    }

    @Override
    public void init() {
        _gameObjectHandler.clearAllObjects();

        this._tileMap = new TileMap(30);
        _tileMap.loadTiles(ResourceHandler.grassTileSetFilePath);
        _tileMap.loadMap(ResourceHandler.level2MapFilePath);
        _tileMap.setPosition(0, 0);
        _tileMap.setCameraMultiplier(0.05);

        this._background = new Background(ResourceHandler.grassBackgroundFilePath, 0.1);
        this.portal = null;
        this._hudPlayer1 = null;
        this._hudPlayer2 = null;

        initializePlayers();
        _gameObjectHandler.setAllPlayerData();
        populateEnemies();

        _keyInputHandler.init();
        this._camera = new Camera(_tileMap, _background, _gameObjectHandler);
    }


    @Override
    public void update() {
        _gameObjectHandler.update();

        if (!_isBossBattle) {
            if (!_tileMap.isEndOfMap()) {
                _camera.update();
            }
            else {
                spawnBoss();
            }
        }
        else if (!_isBoss1Dead && _gameObjectHandler.bossObjects.size() == 0) {
            this._isBoss1Dead = true;
            spawnBoss();
        }
        else if (_isBoss1Dead && !_isBoss2Dead && _gameObjectHandler.bossObjects.size() == 0) {
            this._isBoss2Dead = true;
            this._isBossBattleDone = true;
        }
        else if (_isBossBattleDone && portal == null) {
            setPortal(4350, 190, ResourceType.GREEN_PORTAL);
        }

        if (_transition != null) {
            if (_transition.isTransitionComplete()) {
                this._transition = null;
            }
            else {
                _transition.update();
            }
        }

        if (portal != null) {
            portal.update();

            if (portal.isPlayerInPortal()) {
                gameOver("YOU WIN");
            }
        }

        if (_gameObjectHandler.playerObjects.size() < 1) {
            gameOver("YOU LOSE");
        }

        Timer.updateCurrentTime();
    }

    @Override
    public void draw(Graphics2D g) {
        _background.draw(g);
        _tileMap.draw(g);
        _gameObjectHandler.draw(g);

        _hudPlayer1.draw(g);

        if (_hudPlayer2 != null) {
            _hudPlayer2.draw(g);
        }

        Timer.draw(g);

        if (portal != null) {
            portal.draw(g);
        }

        if (_transition != null) {
            _transition.draw(g);
        }
    }

    public void setPortal(double x, double y, ResourceType portalType) {
        portal = new Portal(_tileMap, _gameObjectHandler, portalType, (int)x, (int)y);
    }

    public void setTransition(TransitionType transitionType) {
        this._transition = new Transition(transitionType);
    }

    @Override
    public void keyPressed(int k) {
        _keyInputHandler.keyPressed(k);
    }

    @Override
    public void keyReleased(int k) {
        _keyInputHandler.keyReleased(k);
    }

    private void setNextLevel() {
        _gameObjectHandler.saveAllPlayerData();
    }

    private void spawnBoss() {
        Minotaur minotaur = new Minotaur(_tileMap, _gameObjectHandler);
        minotaur.setPosition(4350,150);
        _gameObjectHandler.addGameObject(minotaur);
        this._isBossBattle = true;
    }

    private void gameOver(String message) {
        if (_transition == null) {
            Timer.stopTimer();
            Timer.setTimeElapsedInSeconds();

            setTransition(TransitionType.TRANSITION_IN);
        }
        if (_transition.isTransitionComplete()) {
            GameState endState = _gsm.getGameState(State.END);

            if (endState == null) {
                _gsm.addState(State.END, new End(_gsm, _gameObjectHandler, _keyInputHandler, message, Timer.getElapsedTime()));
            }

            _gsm.setState(State.END);
        }
    }

    private void initializePlayers() {
        switch (_gameObjectHandler.numPlayerSelection) {
            case ONEPLAYER:
                initializePlayer(_gameObjectHandler.player1CharacterSelection, ObjectID.PLAYER1);
                break;

            case TWOPLAYERS:
                initializePlayer(_gameObjectHandler.player1CharacterSelection, ObjectID.PLAYER1);
                initializePlayer(_gameObjectHandler.player2CharacterSelection, ObjectID.PLAYER2);
                break;
        }

        for(GameObject player : _gameObjectHandler.playerObjects) {
            if (player.getID() == ObjectID.PLAYER1) {
                this._player1 = (Player)player;
                this._hudPlayer1 = new HUD(_player1, ResourceType.HUD_PLAYER1);

            }
            if (player.getID() == ObjectID.PLAYER2) {
                this._player2 = (Player)player;
                this._hudPlayer2 = new HUD(_player2, ResourceType.HUD_PLAYER2);
            }
        }
    }

    private void initializePlayer(MenuSelection character, ObjectID playerId) {
        Player player;

        switch (character) {
            case DRAGON:
                player = new Dragon(_tileMap, "/dragon_sprites.gif", _gameObjectHandler, playerId);
                player.setPosition(100, 190);
                _gameObjectHandler.addGameObjectDirect(player);
                break;
            case WARRIOR:
                player = new Warrior(_tileMap, "/warrior_sprites.gif", _gameObjectHandler, playerId);
                player.setPosition(100, 190);
                _gameObjectHandler.addGameObjectDirect(player);
                break;
        }
    }

    private void populateEnemies() {
        Point[] snailPositions = new Point[] {
                new Point(460, 170),
                new Point(915, 170),
                new Point(1050, 140),
                new Point(1180,170),
                new Point(2050, 200),
                new Point(2100, 200),
                new Point(3230, 200),
                new Point(3360, 200),
                new Point(3520, 200),
                new Point(3650, 200)
        };
        Point[] impPositions = new Point[] {
                new Point(600, 100),
                new Point(930, 40),
                new Point(1380,160),
                new Point(1315, 40),
                new Point(1785, 70),
                new Point(3100, 160),
                new Point(3310, 160),
                new Point(3810, 40)
        };

        for (Point position : snailPositions) {
            Snail snail = new Snail(_tileMap, _gameObjectHandler);
            snail.setPosition(position.x, position.y);
            _gameObjectHandler.addGameObject(snail);
        }
        for (Point position : impPositions) {
            Imp imp = new Imp(_tileMap, _gameObjectHandler);
            imp.setPosition(position.x, position.y);
            _gameObjectHandler.addGameObject(imp);
        }
    }
}
