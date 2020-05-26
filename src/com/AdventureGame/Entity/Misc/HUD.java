package com.AdventureGame.Entity.Misc;

import com.AdventureGame.Entity.Players.Player;
import com.AdventureGame.Handler.ResourceHandler;
import com.AdventureGame.Handler.ResourceType;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;

public class HUD {

    private Player _player;
    private ResourceType _hudType;

    private BufferedImage _image;
    private Font _font;
    private Color _fontColor;


    public HUD(Player player, ResourceType hudType) {
        this._player = player;
        this._hudType = hudType;

        try {
            switch (hudType) {
                case HUD_PLAYER1:
                    this._image = ImageIO.read(getClass().getResourceAsStream(ResourceHandler.hudPlayer1FilePath));
                    this._fontColor = Color.WHITE;
                    break;

                case HUD_PLAYER2:
                    this._image = ImageIO.read(getClass().getResourceAsStream(ResourceHandler.hudPlayer2FilePath));
                    this._fontColor = Color.BLACK;
                    break;
            }
            this._font = new Font("Arial", Font.PLAIN, 10);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void draw(Graphics2D g) {
        g.setFont(_font);
        g.setColor(_fontColor);

        String health = String.format("%d/%d", _player.getHealth(), _player.getMaxHealth());
        String specialAtk = String.format("%d/%d", _player.getSpecialAtkCharge() / 100, _player.getMaxSpecialAtkCharge() / 100);

        switch (_hudType) {
            case HUD_PLAYER1:
                g.drawImage(_image, 0, 5, null);
                g.drawString(health, 26, 16);
                g.drawString(specialAtk, 26, 32);
                break;
            case HUD_PLAYER2:
                g.drawImage(_image, 0, 40, null);
                g.drawString(health, 26, 51);
                g.drawString(specialAtk, 26, 67);
                break;
        }
    }
}
