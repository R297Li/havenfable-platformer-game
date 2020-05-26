package com.AdventureGame.Handler;

import com.AdventureGame.Entity.*;
import com.AdventureGame.Entity.Players.PlayerSave;
import com.AdventureGame.GameState.MenuSelection;

import java.awt.*;
import java.util.HashMap;
import java.util.LinkedList;

public class GameObjectHandler {

    public HashMap<ObjectID, LinkedList<GameObject>> idForObjectList;

    public HashMap<ObjectID, PlayerSave> playersSavedData;

    public LinkedList<GameObject> enemyObjects;
    public LinkedList<GameObject> bossObjects;
    public LinkedList<GameObject> playerObjects;
    public LinkedList<GameObject> playerSpecialAtkObjects;
    public LinkedList<GameObject> enemySpecialAtkObjects;
    public LinkedList<GameObject> explosionObjects;

    private LinkedList<GameObject> _removeObjects;
    private LinkedList<GameObject> _addObjects;

    public MenuSelection numPlayerSelection;
    public MenuSelection player1CharacterSelection;
    public MenuSelection player2CharacterSelection;

    public GameObjectHandler() {

        this.idForObjectList = new HashMap<ObjectID, LinkedList<GameObject>>();
        this.playersSavedData = new HashMap<ObjectID, PlayerSave>();

        this._removeObjects = new LinkedList<GameObject>();
        this._addObjects = new LinkedList<GameObject>();

        for (ObjectID objectId : ObjectID.values()) {
            if (objectId == ObjectID.PLAYER2) {
                continue;
            }

            idForObjectList.put(objectId, new LinkedList<GameObject>());

            switch (objectId) {
                case PLAYER1:
                    playerObjects = idForObjectList.get(objectId);
                    break;
                case ENEMY:
                    enemyObjects = idForObjectList.get(objectId);
                    break;
                case BOSS:
                    bossObjects = idForObjectList.get(objectId);
                    break;
                case PLAYER_SPECIALATK:
                    playerSpecialAtkObjects = idForObjectList.get(objectId);
                    break;
                case ENEMY_SPECIALATK:
                    enemySpecialAtkObjects = idForObjectList.get(objectId);
                    break;
                case EXPLOSION:
                    explosionObjects = idForObjectList.get(objectId);
                    break;
            }
        }
    }

    public void addGameObject(GameObject gameObject) {
        _addObjects.add(gameObject);
    }

    public void addGameObjectDirect(GameObject gameObject) {
        ObjectID id = gameObject.getID();

        if (id == ObjectID.PLAYER2) {
            id = ObjectID.PLAYER1;
        }

        LinkedList<GameObject> gameObjects = idForObjectList.get(id);
        gameObjects.add(gameObject);
    }

    public void removeGameObject(GameObject gameObject) {
        boolean containsObject = _removeObjects.contains(gameObject);
        if (!containsObject) {
            _removeObjects.add(gameObject);
        }
    }

    public void update() {
        for (GameObject gameObject : _addObjects) {
            ObjectID id = gameObject.getID();

            if (id == ObjectID.PLAYER2) {
                id = ObjectID.PLAYER1;
            }

            LinkedList<GameObject> gameObjects = idForObjectList.get(id);
            gameObjects.add(gameObject);
        }
        for (GameObject gameObject : _removeObjects) {
            ObjectID id = gameObject.getID();

            if (id == ObjectID.PLAYER2) {
                id = ObjectID.PLAYER1;
            }

            LinkedList<GameObject> gameObjects = idForObjectList.get(id);
            gameObjects.remove(gameObject);
        }
        _addObjects.clear();
        _removeObjects.clear();

        for(GameObject gameObject : playerObjects) {
            if (gameObject.getState() == ObjectState.ALIVE) {
                gameObject.update();
            }
            else if (gameObject.getState() == ObjectState.RESPAWN) {
                gameObject.respawnPlayer();
            }
            else {
                this.removeGameObject(gameObject);
            }
        }
        for(GameObject gameObject : playerSpecialAtkObjects) {
            if (gameObject.getState() == ObjectState.ALIVE) {
                gameObject.update();
            }
            else {
                this.removeGameObject(gameObject);
            }
        }
        for(GameObject gameObject : enemyObjects) {
            if (gameObject.getState() == ObjectState.ALIVE) {
                gameObject.update();
            }
            else {
                this.removeGameObject(gameObject);
            }
        }
        for(GameObject gameObject : bossObjects) {
            if (gameObject.getState() == ObjectState.ALIVE) {
                gameObject.update();
            }
            else {
                this.removeGameObject(gameObject);
            }
        }
        for(GameObject gameObject : enemySpecialAtkObjects) {
            if (gameObject.getState() == ObjectState.ALIVE) {
                gameObject.update();
            }
            else {
                this.removeGameObject(gameObject);
            }
        }
        for(GameObject gameObject : explosionObjects) {
            if (gameObject.getState() == ObjectState.ALIVE) {
                gameObject.update();
            }
            else {
                this.removeGameObject(gameObject);
            }
        }
    }

    public void draw(Graphics2D g) {
        for(GameObject gameObject : playerObjects) {
            if (gameObject.getState() == ObjectState.ALIVE || gameObject.getState() == ObjectState.RESPAWN) {
                gameObject.draw(g);
            }
        }
        for(GameObject gameObject : playerSpecialAtkObjects) {
            if (gameObject.getState() == ObjectState.ALIVE) {
                gameObject.draw(g);
            }
        }
        for(GameObject gameObject : enemyObjects) {
            if (gameObject.getState() == ObjectState.ALIVE) {
                gameObject.draw(g);
            }
        }
        for(GameObject gameObject : bossObjects) {
            if (gameObject.getState() == ObjectState.ALIVE) {
                gameObject.draw(g);
            }
        }
        for(GameObject gameObject : enemySpecialAtkObjects) {
            if (gameObject.getState() == ObjectState.ALIVE) {
                gameObject.draw(g);
            }
        }
        for(GameObject gameObject : explosionObjects) {
            if (gameObject.getState() == ObjectState.ALIVE) {
                gameObject.draw(g);
            }
        }
    }

    public void clearAllObjectsExceptPlayers() {
        enemyObjects.clear();
        bossObjects.clear();
        playerSpecialAtkObjects.clear();
        enemySpecialAtkObjects.clear();
        explosionObjects.clear();
        _addObjects.clear();
        _removeObjects.clear();
    }

    public void clearAllObjects() {
        playerObjects.clear();
        clearAllObjectsExceptPlayers();
    }

    public void saveAllPlayerData() {
        playersSavedData.clear();
        for (GameObject gameObject : playerObjects) {
            ObjectID playerId = gameObject.getID();
            int health = gameObject.getHealth();

            playersSavedData.put(playerId, new PlayerSave(playerId, health));
        }
    }

    public void setAllPlayerData() {
        for (GameObject gameObject : playerObjects) {
            PlayerSave playerData = playersSavedData.get(gameObject.getID());

            if (playerData != null) {
                gameObject.setHealth(playerData.getHealth());
            }
        }
    }
}
