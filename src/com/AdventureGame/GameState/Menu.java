package com.AdventureGame.GameState;

import com.AdventureGame.Handler.GameObjectHandler;
import com.AdventureGame.Handler.KeyInputHandler;
import com.AdventureGame.TileMap.Background;
import com.AdventureGame.main.Game;
import com.AdventureGame.main.GamePanel;

import java.awt.*;
import java.awt.event.KeyEvent;

public class Menu extends GameState {

    private Background _background;

    private String title = "Haven Fable";

    private MenuSelection[] currentOptions;

    private MenuSelection[] menuOptions = {
            MenuSelection.START,
            MenuSelection.HELP,
            MenuSelection.QUIT
    };
    private MenuSelection[] numPlayerOptions = {
            MenuSelection.ONEPLAYER,
            MenuSelection.TWOPLAYERS
    };
    private MenuSelection[] characterOptions = {
            MenuSelection.DRAGON,
            MenuSelection.WARRIOR
    };

    private int _currentSelectionIndex;
    private MenuSelection _currentSelection;
    private MenuSelection _numPlayerSelection;
    private MenuSelection _player1CharacterSelection;
    private MenuSelection _player2CharacterSelection;

    private Color _titleColor;
    private Font _titleFont;

    private Font _font;

    public Menu(GameStateManager gsm, GameObjectHandler gameObjectHandler, KeyInputHandler keyInputHandler) {
        this._gsm = gsm;
        this._gameObjectHandler = gameObjectHandler;
        this._keyInputHandler = keyInputHandler;

        init();
    }

    @Override
    public void init() {
        try {
            this._background = new Background("/menu_background_2.gif", 1);
            _background.setVector(-0.1, 0);

            this._titleColor = new Color(128,0,0);
            this._titleFont = new Font("Cambria", Font.PLAIN, 28);

            this._font = new Font("Arial", Font.PLAIN, 12);
        } catch (Exception e) {
            e.printStackTrace();
        }
        setCurrentOptions(menuOptions);
    }

    @Override
    public void update() {
        _background.update();
    }

    @Override
    public void draw(Graphics2D g) {
        _background.draw(g);

        g.setColor(_titleColor);
        g.setFont(_titleFont);
        FontMetrics fontMetrics = g.getFontMetrics(_titleFont);
        int stringLength = fontMetrics.stringWidth(title);
        g.drawString(title, (GamePanel.WIDTH / 2) - (stringLength / 2), 70);

        g.setFont(_font);
        fontMetrics = g.getFontMetrics(_font);

        for (int i = 0; i < currentOptions.length; i++) {
            if (currentOptions[i] == _currentSelection) {
                g.setColor(Color.RED);
            }
            else {
                g.setColor(Color.BLACK);
            }

            stringLength = fontMetrics.stringWidth(currentOptions[i].getString());
            g.drawString(currentOptions[i].getString(), (GamePanel.WIDTH / 2) - (stringLength / 2), 130 + (i * 20));
        }
    }

    @Override
    public void keyPressed(int k) {
        if (k == KeyEvent.VK_ENTER) {
            setNextSelection();
        }
        if (k == KeyEvent.VK_UP) {
            this._currentSelectionIndex--;
        }
        if (k == KeyEvent.VK_DOWN) {
            this._currentSelectionIndex++;
        }

        this._currentSelectionIndex = Game.clamp(_currentSelectionIndex, 0, currentOptions.length - 1);
        this._currentSelection = currentOptions[_currentSelectionIndex];
    }

    private void setCurrentOptions(MenuSelection[] options) {
        this.currentOptions = options;
        this._currentSelectionIndex = 0;
        this._currentSelection = currentOptions[_currentSelectionIndex];
    }

    private void setNextSelection() {
        switch (_currentSelection) {
            case START:
                setCurrentOptions(numPlayerOptions);
                setNumCharacterSelectionProperties();
                break;
            case HELP:

                break;
            case QUIT:
                System.exit(0);
                break;
            default:
                if (_currentSelection == MenuSelection.ONEPLAYER || _currentSelection == MenuSelection.TWOPLAYERS) {
                    this._numPlayerSelection = _currentSelection;
                    setPlayer1CharacterSelectionTitle();
                    setCurrentOptions(characterOptions);
                }
                else if (_currentSelection == MenuSelection.DRAGON || _currentSelection == MenuSelection.WARRIOR) {
                    setPlayerSelection();
                    if (_numPlayerSelection == MenuSelection.TWOPLAYERS) {
                        setPlayer2CharacterSelectionTitle();
                        setCurrentOptions(characterOptions);
                    }
                }
                break;
        }
    }

    private void setPlayerSelection() {
        switch (_numPlayerSelection) {
            case ONEPLAYER:
                this._player1CharacterSelection = _currentSelection;
                _gameObjectHandler.player1CharacterSelection = _player1CharacterSelection;
                _gameObjectHandler.player2CharacterSelection = null;
                _gameObjectHandler.numPlayerSelection = _numPlayerSelection;
                startGame();
                break;

            case TWOPLAYERS:
                if (this._player1CharacterSelection == null) {
                    this._player1CharacterSelection = _currentSelection;
                }
                else if (this._player2CharacterSelection == null) {
                    this._player2CharacterSelection = _currentSelection;
                }

                if (this._player1CharacterSelection != null && this._player2CharacterSelection != null) {
                    _gameObjectHandler.player1CharacterSelection = _player1CharacterSelection;
                    _gameObjectHandler.player2CharacterSelection = _player2CharacterSelection;
                    _gameObjectHandler.numPlayerSelection = _numPlayerSelection;
                    startGame();
                }
                break;
        }
    }

    private void startGame() {
        _gsm.addState(State.LEVEL1, new LevelOne(this._gsm, this._gameObjectHandler, this._keyInputHandler));
        _gsm.setState(State.LEVEL1);
    }

    private void setMenuSelectionProperties() {
        this.title = "Haven Fable";
    }

    private void setNumCharacterSelectionProperties() {
        this.title = "Solo or Dual?";
    }

    private void setPlayer1CharacterSelectionTitle() {
        this.title = "Player 1 Character";
    }

    private void setPlayer2CharacterSelectionTitle() {
        this.title = "Player 2 Character";
    }

    @Override
    public void keyReleased(int k) {

    }
}
