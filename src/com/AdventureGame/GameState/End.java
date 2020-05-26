package com.AdventureGame.GameState;

import com.AdventureGame.Handler.GameObjectHandler;
import com.AdventureGame.Handler.KeyInputHandler;
import com.AdventureGame.TileMap.Background;
import com.AdventureGame.main.Game;
import com.AdventureGame.main.GamePanel;

import java.awt.*;
import java.awt.event.KeyEvent;

public class End extends GameState {

    private Background _background;

    private String title = "GAME OVER";
    private String _subText;
    private String _completionTime;

    private MenuSelection[] currentOptions;

    private MenuSelection[] menuOptions = {
            MenuSelection.MAINMENU,
            MenuSelection.QUIT
    };

    private int _currentSelectionIndex;
    private MenuSelection _currentSelection;

    private Color _titleColor;
    private Font _titleFont;
    private Font _subTextFont;
    private Font _timeFont;

    private Font _font;

    public End(GameStateManager gsm, GameObjectHandler gameObjectHandler, KeyInputHandler keyInputHandler, String message, String completionTime) {
        this._gsm = gsm;
        this._gameObjectHandler = gameObjectHandler;
        this._keyInputHandler = keyInputHandler;
        this._subText = message.toUpperCase();
        this._completionTime = String.format("Time: %s", completionTime);
        init();
    }

    @Override
    public void init() {
        try {
            this._background = new Background("/menu_background_1.gif", 1);
            _background.setVector(-0.1, 0);

            this._titleColor = new Color(128,0,0);
            this._titleFont = new Font("Cambria", Font.PLAIN, 28);
            this._subTextFont = new Font("Cambria", Font.PLAIN, 22);
            this._timeFont = new Font("Cambria", Font.PLAIN, 12);

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
        g.drawString(title, (GamePanel.WIDTH / 2) - (stringLength / 2), 60);

        g.setFont(_subTextFont);
        fontMetrics = g.getFontMetrics(_subTextFont);
        stringLength = fontMetrics.stringWidth(_subText);
        g.drawString(_subText, (GamePanel.WIDTH / 2) - (stringLength / 2), 85);

        g.setFont(_timeFont);
        fontMetrics = g.getFontMetrics(_timeFont);
        stringLength = fontMetrics.stringWidth(_completionTime);
        g.drawString(_completionTime, (GamePanel.WIDTH / 2) - (stringLength / 2), 105);

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
            g.drawString(currentOptions[i].getString(), (GamePanel.WIDTH / 2) - (stringLength / 2), 150 + (i * 20));
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
            case MAINMENU:
                mainMenu();
                break;
            case QUIT:
                System.exit(0);
                break;
        }
    }

    private void mainMenu() {
        for (State state : State.values()) {
            if (state == State.MENU || state == State.END) {
                continue;
            }
            _gsm.removeState(state);
        }

        _gsm.setState(State.MENU);
        _gsm.getCurrentGameState().init();
    }
}
