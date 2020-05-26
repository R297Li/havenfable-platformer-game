package com.AdventureGame.GameState;

import com.AdventureGame.Handler.GameObjectHandler;
import com.AdventureGame.Handler.KeyInputHandler;
import com.AdventureGame.TileMap.Background;
import com.AdventureGame.main.Game;
import com.AdventureGame.main.GamePanel;

import java.awt.*;
import java.awt.event.KeyEvent;

public class Pause extends GameState {

    private Background _background;

    private String title = "GAME PAUSED";

    private MenuSelection[] currentOptions;

    private MenuSelection[] menuOptions = {
            MenuSelection.RESUME,
            MenuSelection.RESTART,
            MenuSelection.HELP,
            MenuSelection.MAINMENU,
            MenuSelection.QUIT
    };

    private int _currentSelectionIndex;
    private MenuSelection _currentSelection;

    private Color _titleColor;
    private Font _titleFont;

    private Font _font;

    private State _currentPausedState;

    public Pause(GameStateManager gsm, GameObjectHandler gameObjectHandler, KeyInputHandler keyInputHandler, State currentPausedState) {
        this._gsm = gsm;
        this._gameObjectHandler = gameObjectHandler;
        this._keyInputHandler = keyInputHandler;
        this._currentPausedState = currentPausedState;

        init();
    }

    @Override
    public void init() {
        try {
            this._background = new Background("/menu_background_1.gif", 1);
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

    @Override
    public void keyReleased(int k) {

    }

    private void setCurrentOptions(MenuSelection[] options) {
        this.currentOptions = options;
        this._currentSelectionIndex = 0;
        this._currentSelection = currentOptions[_currentSelectionIndex];
    }

    private void setNextSelection() {
        switch (_currentSelection) {
            case RESUME:
                _keyInputHandler.keyReleased(KeyEvent.VK_ESCAPE);
                _gsm.setState(_currentPausedState);
                break;
            case RESTART:
                _keyInputHandler.keyReleased(KeyEvent.VK_ESCAPE);
                restart();
                break;
            case HELP:

                break;

            case MAINMENU:
                _keyInputHandler.keyReleased(KeyEvent.VK_ESCAPE);
                mainMenu();
                break;
            case QUIT:
                System.exit(0);
                break;
        }
    }

    private void mainMenu() {
        for (State state : State.values()) {
            if (state == State.MENU || state == State.PAUSE) {
                continue;
            }
            _gsm.removeState(state);
        }

        _gsm.setState(State.MENU);
        _gsm.getCurrentGameState().init();
    }

    private void restart() {
        GameState gameState = _gsm.getGameState(_currentPausedState);
        _gsm.setState(_currentPausedState);
        gameState.init();
    }
}
