package com.AdventureGame.Entity.Misc;

import com.AdventureGame.main.GamePanel;

import java.awt.*;
import java.util.ArrayList;

public class Transition {

    private boolean _isTransitionComplete;
    private TransitionType _transitionType;
    private ArrayList<Rectangle> _transitionBoxes;
    private Integer _transitionCount;

    public Transition(TransitionType transitionType) {
        _isTransitionComplete = false;
        _transitionType = transitionType;
        _transitionBoxes = new ArrayList<Rectangle>();
        _transitionCount = 0;
        setTransitions();
    }

    public void setTransitions() {
        switch (_transitionType) {
            case TRANSITION_IN:
                _transitionBoxes.add(new Rectangle(0, 0, GamePanel.WIDTH, 0));
                _transitionBoxes.add(new Rectangle(0, 0, 0, GamePanel.HEIGHT));
                _transitionBoxes.add(new Rectangle(0, GamePanel.HEIGHT, GamePanel.WIDTH, 0));
                _transitionBoxes.add(new Rectangle(GamePanel.WIDTH, 0, 0, GamePanel.HEIGHT));
                break;

            case TRANSITION_OUT:
                _transitionBoxes.add(new Rectangle(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT / 2));
                _transitionBoxes.add(new Rectangle(0, 0, GamePanel.WIDTH / 2, GamePanel.HEIGHT));
                _transitionBoxes.add(new Rectangle(0, GamePanel.HEIGHT / 2, GamePanel.WIDTH, GamePanel.HEIGHT / 2));
                _transitionBoxes.add(new Rectangle(GamePanel.WIDTH / 2, 0, GamePanel.WIDTH / 2, GamePanel.HEIGHT));
                break;
        }
    }

    public boolean isTransitionComplete() {
        return _isTransitionComplete;
    }

    public void update() {
        if (!_isTransitionComplete) {
            switch (_transitionType) {
                case TRANSITION_IN:
                    if (_transitionCount < 60) {
                        _transitionBoxes.get(0).height += 4;
                        _transitionBoxes.get(1).width += 6;
                        _transitionBoxes.get(2).y -= 4;
                        _transitionBoxes.get(2).height += 4;
                        _transitionBoxes.get(3).x -= 6;
                        _transitionBoxes.get(3).width += 6;
                        _transitionCount++;
                    } else {
                        _isTransitionComplete = true;
                    }
                    break;

                case TRANSITION_OUT:
                    if (_transitionCount < 60) {
                        _transitionBoxes.get(0).height -= 4;
                        _transitionBoxes.get(1).width -= 6;
                        _transitionBoxes.get(2).y += 4;
                        _transitionBoxes.get(3).x += 6;
                        _transitionCount++;
                    } else {
                        _isTransitionComplete = true;
                    }
                    break;
            }
        }
    }

    public void draw(Graphics2D g) {
        if (!_isTransitionComplete) {
            g.setColor(Color.BLACK);
            for (int i = 0; i < _transitionBoxes.size(); i++) {
                g.fill(_transitionBoxes.get(i));
            }
        }
    }
}
