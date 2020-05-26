package com.AdventureGame.TileMap;

import com.AdventureGame.main.Game;
import com.AdventureGame.main.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class TileMap {

    // Position
    private double _x;
    private double _y;

    // Bounds
    private int _xMin;
    private int _xMax;
    private int _yMin;
    private int _yMax;

    private double _cameraMultiplier;

    // Map
    private int[][] _map;
    private int _tileSize;
    private int _mapNumRows;
    private int _mapNumCols;
    private int _mapWidth;
    private int _mapHeight;
    private int _padding = 2;

    // Tile set
    private BufferedImage _tileSet;
    private int _numTilesAcross;
    private int _numTilesVertical;
    private Tile[][] _tiles;

    // Improve rendering performance by not rendering everything
    private int _rowOffset;
    private int _colOffset;
    private int _numRowsToDraw;
    private int _numColsToDraw;

    private boolean _isEndOfMap;

    public TileMap(int tileSize) {
        this._tileSize = tileSize;
        _numRowsToDraw = ( GamePanel.HEIGHT / tileSize ) + _padding;
        _numColsToDraw = ( GamePanel.WIDTH / tileSize ) + _padding;
        _cameraMultiplier = 0.05;
    }

    public void loadTiles(String path) {
        try {
            this._tileSet = ImageIO.read(getClass().getResourceAsStream(path));
            this._numTilesAcross = _tileSet.getWidth() / _tileSize;
            this._numTilesVertical = _tileSet.getHeight() / _tileSize;
            this._tiles = new Tile[_numTilesVertical][_numTilesAcross];

            BufferedImage subImage;

            for (int row = 0; row < _numTilesVertical; row++) {
                for (int col = 0; col < _numTilesAcross; col++) {
                    subImage = _tileSet.getSubimage(col*_tileSize, row*_tileSize, _tileSize, _tileSize);

                    // Specific to this tile map
                    // Map blocks are on 2nd row
                    if (row == 0) {
                        _tiles[row][col] = new Tile(subImage, TileType.NORMAL);
                    }
                    else {
                        _tiles[row][col] = new Tile(subImage, TileType.BLOCK);
                    }
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadMap(String path) {
        try {
            // Load map file
            InputStream stream = getClass().getResourceAsStream(path);
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

            this._mapNumCols = Integer.parseInt(reader.readLine()); // 1st line in map represents # of cols
            this._mapNumRows = Integer.parseInt(reader.readLine()); // 2nd line in map represents # of rows

            this._map = new int[_mapNumRows][_mapNumCols];
            this._mapWidth = _mapNumCols * _tileSize;
            this._mapHeight = _mapNumRows * _tileSize;

            this._xMax = 0;
            this._xMin = GamePanel.WIDTH - _mapWidth;
            this._yMax = 0;
            this._yMin = GamePanel.HEIGHT - _mapHeight;

            String delimiter = "\\s+"; // Groups all white spaces as delimiters

            for (int row = 0; row < _mapNumRows; row++) {
                String line = reader.readLine();
                String[] item = line.split(delimiter);

                for (int col = 0; col < _mapNumCols; col++) {
                    _map[row][col] = Integer.parseInt(item[col]);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getTileSize() {
        return _tileSize;
    }

    public double getX() {
        return _x;
    }

    public double getY() {
        return _y;
    }

    public boolean isEndOfMap() { return _isEndOfMap; }

    public int getWidth() {
        return _mapWidth;
    }

    public int getHeight() {
        return _mapHeight;
    }

    public int getMapNumRows() {
        return _mapNumRows;
    }

    public int getMapNumCols() {
        return _mapNumCols;
    }

    private Tile getTile(int row, int col) {
        // In the map array, each tile is a number corresponding to the position of the
        // tile in the tileset. Counting from 0 starting at the upper left corner, each
        // tile can be represented by a number
        int tile = _map[row][col];
        int tileRow = tile / _numTilesAcross;
        int tileCol = tile % _numTilesAcross;
        return _tiles[tileRow][tileCol];
    }

    public TileType getTileType(int row, int col) {
        return getTile(row, col).getType();
    }

    public void setPosition(double x, double y) {
        this._x += (x - this._x) * _cameraMultiplier;
        this._y += (y - this._y) * _cameraMultiplier;

        this._x = Game.clamp(_x, _xMin, _xMax);
        this._y = Game.clamp(_y, _yMin, _yMax);

        _colOffset = -(int)this._x / _tileSize;
        _rowOffset = -(int)this._y / _tileSize;

        this._isEndOfMap = (_colOffset + _numColsToDraw > _mapNumCols) ? true : false;
    }

    public void setCameraMultiplier(double multiplier) {
        this._cameraMultiplier = multiplier;
    }

    public void draw(Graphics2D g) {
        for (int row = _rowOffset; row < _rowOffset + _numRowsToDraw; row++) {

            if (row >= _mapNumRows) {
                break;
            }

            for (int col = _colOffset; row < _colOffset + _numColsToDraw; col++) {
                if (col >= _mapNumCols) {
                    break;
                }

                Tile tile = getTile(row, col);
                int positionX = (int)_x + (col * _tileSize);
                int positionY = (int)_y + (row * _tileSize);

                g.drawImage(tile.getImage(), positionX, positionY, null);
            }
        }
    }

}
