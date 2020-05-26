package com.AdventureGame.Entity;

import com.AdventureGame.Entity.Misc.Animation;
import com.AdventureGame.Entity.Misc.AnimationAction;
import com.AdventureGame.TileMap.TileMap;
import com.AdventureGame.TileMap.TileType;
import com.AdventureGame.main.GamePanel;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class MapObject {

    protected TileMap _tileMap;
    protected int _tileSize;
    protected double _mapXPosition;
    protected double _mapYPosition;

    // Position and Vector
    protected double _x;
    protected double _y;
    protected double _dx;
    protected double _dy;

    // Dimensions
    protected int _spriteWidth;
    protected int _spriteHeight;

    // Collision box
    protected int _collisionWidth;
    protected int _collisionHeight;

    // Collision
    protected int _currentRow;
    protected int _currentCol;
    protected double _xDestination;
    protected double _yDestination;
    protected double _xTemp;
    protected double _yTemp;
    protected boolean _isTopLeftCollision;
    protected boolean _isTopRightCollision;
    protected boolean _isBottomLeftCollision;
    protected boolean _isBottomRightCollision;

    // Ledge
    protected boolean _isBottomRightLedge;
    protected boolean _isBottomLeftLedge;


    // Animation
    protected Animation _animation;
    protected AnimationAction _currentAction;
    protected AnimationAction _previousAction;
    protected boolean _isFacingRight;

    // Movement
    protected boolean _isMovingLeft;
    protected boolean _isMovingRight;
    protected boolean _isMovingUp;
    protected boolean _isMovingDown;
    protected boolean _isJumping;
    protected boolean _isDoubleJumping;
    protected boolean _hasJumpedOnce;
    protected boolean _isFalling;
    protected boolean _isDashing;

    // Movement attributes
    protected double _moveSpeed;
    protected double _maxSpeed; // Max velocity
    protected double _stopSpeed; // Decceleration to 0
    protected double _fallSpeed; // Gravity
    protected double _maxFallSpeed; // Terminal velocity
    protected double _jumpStart; // How high can jump
    protected double _stopJumpSpeed; // Jump height based on how long jump is held for
    protected double _doubleJumpStart;

    public MapObject(TileMap tileMap) {
        this._tileMap = tileMap;
        this._tileSize = tileMap.getTileSize();
    }

    public int getX() {
        return (int)_x;
    }

    public int getY() {
        return (int)_y;
    }

    public int getWidth() {
        return (int) _spriteWidth;
    }

    public int getHeight() {
        return (int) _spriteHeight;
    }

    public int getCollisionWidth() {
        return (int)_collisionWidth;
    }

    public int getCollisionHeight() {
        return (int)_collisionHeight;
    }

    public void setPosition(double x, double y) {
        this._x = x;
        this._y = y;
    }

    public void setVector(double dx, double dy) {
        this._dx = dx;
        this._dy = dy;
    }

    public void setMapPosition() {
        this._mapXPosition = _tileMap.getX();
        this._mapYPosition = _tileMap.getY();
    }

    public void setTileMap(TileMap tileMap) {
        this._tileMap = tileMap;
    }

    public TileMap getTileMap() {
        return _tileMap;
    }

    public void setMovingLeft(boolean movingLeft) {
        this._isMovingLeft = movingLeft;
    }

    public void setMovingRight(boolean movingRight) {
        this._isMovingRight = movingRight;
    }

    public void setMovingUp(boolean movingUp) {
        this._isMovingUp = movingUp;
    }

    public void setMovingDown(boolean movingDown) {
        this._isMovingDown = movingDown;
    }

    public void setJumping(boolean jumping) {
        this._isJumping = jumping;
    }

    // Double check
    public boolean isOnScreen() {
        double globalXPosition = _x + _mapXPosition;
        double globalYPosition = _y + _mapYPosition;

        if (globalXPosition + _spriteWidth < 0 || globalXPosition - _spriteWidth > GamePanel.WIDTH) {
            return false;
        }
        if (globalYPosition + _spriteHeight < 0 || globalYPosition - _spriteHeight > GamePanel.HEIGHT) {
            return false;
        }
        return true;
    }

    public boolean isFallingOutOfWorld() {
        double globalYPosition = _y + _mapYPosition;

        if (globalYPosition - _spriteHeight > GamePanel.HEIGHT) {
            return true;
        }
        return false;
    }

    public boolean isThereIntersection(MapObject object) {
        Rectangle currentObject = this.getRectangle();
        Rectangle collisionObject = object.getRectangle();

        boolean isCollided = currentObject.intersects(collisionObject);

        return isCollided;
    }

    public void checkTileMapCollision() {
        this._currentCol = (int)_x / _tileSize;
        this._currentRow = (int)_y / _tileSize;

        this._xDestination = _x + _dx;
        this._yDestination = _y + _dy;

        this._xTemp = _x;
        this._yTemp = _y;

        setCollisionCorners(_x, _yDestination);

        // If object is travelling upwards
        if (_dy < 0) {
            if (_isTopLeftCollision || _isTopRightCollision) {
                this._dy = 0;
                this._yTemp = (_currentRow * _tileSize) + (_collisionHeight / 2);
            }
            else {
                this._yTemp += _dy;
            }
        }
        // If object is travelling downwards
        else if (_dy > 0) {
            if (_isBottomLeftCollision || _isBottomRightCollision) {
                this._dy = 0;
                this._isFalling = false;
                this._yTemp = ((_currentRow + 1) * _tileSize) - (_collisionHeight / 2);
            }
            else {
                this._yTemp += _dy;
            }
        }

        setCollisionCorners(_xDestination, _y);

        // If object is travelling left
        if (_dx < 0) {
            if (_isTopLeftCollision || _isBottomLeftCollision) {
                this._dx = 0;
                this._xTemp = (_currentCol * _tileSize) + (_collisionWidth / 2);
            }
            else {
                this._xTemp += _dx;
            }
        }
        // If object is travelling right
        else if (_dx > 0) {
            if (_isTopRightCollision || _isBottomRightCollision) {
                this._dx = 0;
                // Position object in center of current column
                // Need to increment current column by 1 to ensure that user gets placed inside current column
                // rather than getting sent back 1
                this._xTemp = ((_currentCol + 1) * _tileSize) - (_collisionWidth / 2);
            }
            else {
                this._xTemp += _dx;
            }
        }

        if (!_isFalling) {
            setCollisionCorners(_x, _yDestination + 1);

            if (!_isBottomLeftCollision && !_isBottomRightCollision) {
                this._isFalling = true;
            }

        }
    }

    public abstract void draw(Graphics2D g);

    public void drawNextPosition(Graphics g) {
        BufferedImage image = _animation.getImage();
        double screenXPosition = _x + _mapXPosition - (_spriteWidth / 2);
        double screenYPosition = _y + _mapYPosition - (_spriteHeight / 2);

        if (_isFacingRight) {
            g.drawImage(image, (int)screenXPosition, (int)screenYPosition, null);
        }
        else {
            screenXPosition += _spriteWidth;
            g.drawImage(image, (int)screenXPosition, (int)screenYPosition, -_spriteWidth, _spriteHeight, null);
        }
    }


    private void setCollisionCorners(double x, double y) {
        int collisionWidthCenter = _collisionWidth / 2;
        int collisionHeightCenter = _collisionHeight / 2;

        int leftTile = (int)(x - collisionWidthCenter) / _tileSize; // Get tile to left of object
        int rightTile = (int)(x + collisionWidthCenter - 1) / _tileSize; // Get tile to right of object. Need to subtract 1 to ensure it does not step into a further right tile
        int topTile = (int)(y - collisionHeightCenter) / _tileSize;
        int bottomTile = (int)(y + collisionHeightCenter - 1) / _tileSize;

        if (topTile < 0 || bottomTile >= _tileMap.getMapNumRows() || leftTile < 0 || rightTile >= _tileMap.getMapNumCols()) {
            this._isTopLeftCollision = false;
            this._isTopRightCollision = false;
            this._isBottomLeftCollision = false;
            this._isBottomRightCollision = false;
            return;
        }

        int topLeftCorner = _tileMap.getTileType(topTile, leftTile).getValue();
        int topRightCorner = _tileMap.getTileType(topTile, rightTile).getValue();
        int bottomLeftCorner = _tileMap.getTileType(bottomTile, leftTile).getValue();
        int bottomRightCorner = _tileMap.getTileType(bottomTile, rightTile).getValue();

        this._isTopLeftCollision = (topLeftCorner == TileType.BLOCK.getValue()) ? true : false;
        this._isTopRightCollision = (topRightCorner == TileType.BLOCK.getValue()) ? true : false;
        this._isBottomLeftCollision = (bottomLeftCorner == TileType.BLOCK.getValue()) ? true : false;
        this._isBottomRightCollision = (bottomRightCorner == TileType.BLOCK.getValue()) ? true : false;

        this._isBottomLeftLedge = (bottomLeftCorner == TileType.NORMAL.getValue()) ? true : false;
        this._isBottomRightLedge = (bottomRightCorner == TileType.NORMAL.getValue()) ? true : false;
    }

    public Rectangle getRectangle() {
        int x = (int)_x - (_collisionWidth / 2);
        int y = (int)_y - (_collisionHeight / 3);

        return new Rectangle(x, y, _collisionWidth, _collisionHeight);
    }

    public Rectangle getRectangleBody() {
        int x = (int)_x - (_spriteWidth / 2);
        int y = (int)_y - (_spriteHeight / 2);

        return new Rectangle(x, y, _spriteWidth, _spriteHeight);
    }

}
