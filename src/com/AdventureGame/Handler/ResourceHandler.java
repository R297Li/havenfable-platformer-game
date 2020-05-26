package com.AdventureGame.Handler;

import com.AdventureGame.Entity.Misc.Sprite;
import com.AdventureGame.Entity.Misc.AnimationAction;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class ResourceHandler {

    public static LinkedHashMap<AnimationAction, Sprite> minotaurAnimationSpriteMap;
    public static LinkedHashMap<AnimationAction, Sprite> cyclopsAnimationSpriteMap;
    public static LinkedHashMap<AnimationAction, Sprite> fireBallPurpleAnimationSpriteMap;
    public static LinkedHashMap<AnimationAction, Sprite> fireBallOrangeAnimationSpriteMap;
    public static LinkedHashMap<AnimationAction, Sprite> snailAnimationSpriteMap;
    public static LinkedHashMap<AnimationAction, Sprite> impAnimationSpriteMap;
    public static LinkedHashMap<AnimationAction, Sprite> portalPurpleAnimationSpriteMap;
    public static LinkedHashMap<AnimationAction, Sprite> portalGreenAnimationSpriteMap;
    public static LinkedHashMap<AnimationAction, Sprite> warriorAnimationSpriteMap;
    public static LinkedHashMap<AnimationAction, Sprite> dragonAnimationSpriteMap;
    public static LinkedHashMap<AnimationAction, Sprite> deathAnimationSpriteMap;

    public static HashMap<ResourceType, LinkedHashMap<AnimationAction, Sprite>> resourceTypeAndAnimationSpriteMaps;
    public static HashMap<ResourceType, String> resourceTypeAndSpritePathMaps;

    public static String minotaurFilePath = "/minotaur.png";
    public static String cyclopsFilePath;
    public static String fireBallPurpleFilePath = "/fire_ball_purple.png";
    public static String fireBallOrangeFilePath = "/fire_ball_orange.gif";
    public static String snailFilePath = "/enemy_snail.gif";
    public static String impFilePath = "/imp_cropped.png";
    public static String portalPurpleFilePath = "/purple_portal.png";
    public static String portalGreenFilePath = "/green_portal.png";
    public static String warriorFilePath = "/warrior_sprites.gif";
    public static String dragonFilePath = "/dragon_sprites.gif";
    public static String deathAnimationFilePath = "/death_animation.gif";
    public static String hudPlayer1FilePath = "/hud_player_1_small.png";
    public static String hudPlayer2FilePath = "/hud_player_2_small.png";
    public static String hudTimerFilePath = "/hud_timer_small.png";
    public static String grassTileSetFilePath = "/grass_tile_set.gif";
    public static String level1MapFilePath = "/level_1-1.map";
    public static String level2MapFilePath = "/level_2-1.map";
    public static String grassBackgroundFilePath = "/grass_background_1.gif";

    public ResourceHandler() {

        resourceTypeAndAnimationSpriteMaps = new HashMap<ResourceType, LinkedHashMap<AnimationAction, Sprite>>();
        resourceTypeAndSpritePathMaps = new HashMap<ResourceType, String>();

        for (ResourceType resourceType : ResourceType.values()) {

            resourceTypeAndAnimationSpriteMaps.put(resourceType, new LinkedHashMap<AnimationAction, Sprite>());

            switch (resourceType) {
                case MINOTAUR:
                    minotaurAnimationSpriteMap = resourceTypeAndAnimationSpriteMaps.get(resourceType);
                    resourceTypeAndSpritePathMaps.put(resourceType, minotaurFilePath);
                    break;
                case DRAGON:
                    dragonAnimationSpriteMap = resourceTypeAndAnimationSpriteMaps.get(resourceType);
                    resourceTypeAndSpritePathMaps.put(resourceType, dragonFilePath);
                    break;
                case WARRIOR:
                    warriorAnimationSpriteMap = resourceTypeAndAnimationSpriteMaps.get(resourceType);
                    resourceTypeAndSpritePathMaps.put(resourceType, warriorFilePath);
                    break;
                case CYCLOPS:
                    cyclopsAnimationSpriteMap = resourceTypeAndAnimationSpriteMaps.get(resourceType);
                    resourceTypeAndSpritePathMaps.put(resourceType, cyclopsFilePath);
                    break;
                case PURPLE_PORTAL:
                    portalPurpleAnimationSpriteMap = resourceTypeAndAnimationSpriteMaps.get(resourceType);
                    resourceTypeAndSpritePathMaps.put(resourceType, portalPurpleFilePath);
                    break;
                case GREEN_PORTAL:
                    portalGreenAnimationSpriteMap = resourceTypeAndAnimationSpriteMaps.get(resourceType);
                    resourceTypeAndSpritePathMaps.put(resourceType, portalGreenFilePath);
                    break;
                case IMP:
                    impAnimationSpriteMap = resourceTypeAndAnimationSpriteMaps.get(resourceType);
                    resourceTypeAndSpritePathMaps.put(resourceType, impFilePath);
                    break;
                case SNAIL:
                    snailAnimationSpriteMap = resourceTypeAndAnimationSpriteMaps.get(resourceType);
                    resourceTypeAndSpritePathMaps.put(resourceType, snailFilePath);
                    break;
                case ORANGE_FIREBALL:
                    fireBallOrangeAnimationSpriteMap = resourceTypeAndAnimationSpriteMaps.get(resourceType);
                    resourceTypeAndSpritePathMaps.put(resourceType, fireBallOrangeFilePath);
                    break;
                case PURPLE_FIREBALL:
                    fireBallPurpleAnimationSpriteMap = resourceTypeAndAnimationSpriteMaps.get(resourceType);
                    resourceTypeAndSpritePathMaps.put(resourceType, fireBallPurpleFilePath);
                    break;
                case DEATHANIMATION:
                    deathAnimationSpriteMap = resourceTypeAndAnimationSpriteMaps.get(resourceType);
                    resourceTypeAndSpritePathMaps.put(resourceType, deathAnimationFilePath);
                    break;
            }
        }
        setDragonAnimationSprites();
        setImpAnimationSprites();
        setMinotaurAnimationSprites();
        setFireBallOrangeAnimationSprites();
        setFireBallPurpleAnimationSprites();
        setSnailAnimationSprites();
        setPortalPurpleAnimationSprites();
        setPortalGreenAnimationSprites();
        setWarriorAnimationSprites();
        setDeathAnimationSprites();
    }

    public BufferedImage getSpriteSheet(String path) {
        try {
            BufferedImage spriteSheet = ImageIO.read(getClass().getResourceAsStream(path));
            return spriteSheet;
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public BufferedImage[] getSpriteImages(BufferedImage spriteSheet, int numSprites, int row, int spriteWidth, int spriteHeight) {
        try{
            BufferedImage[] sprites = new BufferedImage[numSprites];

            for (int i = 0; i < numSprites; i++) {
                int x = i * spriteWidth;
                int y = row * spriteHeight;

                sprites[i] = spriteSheet.getSubimage(x, y, spriteWidth, spriteHeight);
            }
            return sprites;
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public BufferedImage[] getSpriteImages(BufferedImage spriteSheet, int numSprites, int row, int spriteWidth, int spriteHeight, int widthBetweenSprites, int heightBetweenSprites) {
        try{
            BufferedImage[] sprites = new BufferedImage[numSprites];

            int y = row * spriteHeight;

            if (row != 0) {
                y = y + (row * heightBetweenSprites);
            }

            for (int i = 0; i < numSprites; i++) {
                int x = i * spriteWidth;

                if (i != 0) {
                    x = x + (i * widthBetweenSprites);
                }

                sprites[i] = spriteSheet.getSubimage(x, y, spriteWidth, spriteHeight);
            }
            return sprites;
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public BufferedImage[] getSpriteImages(BufferedImage spriteSheet, int numSprites, int row, int spriteWidth, int spriteHeight, int widthBetweenSprites, int heightBetweenSprites, int scaleFactor) {
        try{
            BufferedImage[] sprites = new BufferedImage[numSprites];

            int y = row * spriteHeight;

            if (row != 0) {
                y = y + (row * heightBetweenSprites);
            }

            for (int i = 0; i < numSprites; i++) {
                int x = i * spriteWidth;

                if (i != 0) {
                    x = x + (i * widthBetweenSprites);
                }

                BufferedImage sprite = spriteSheet.getSubimage(x, y, spriteWidth, spriteHeight);
                sprites[i] = getScaledImage(sprite, spriteWidth, spriteHeight, scaleFactor);
            }
            return sprites;
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public BufferedImage[] getSpriteImages(BufferedImage spriteSheet, int numSprites, int row, int spriteWidth, int spriteHeight, int scaleFactor) {
        try{
            BufferedImage[] sprites = new BufferedImage[numSprites];

            for (int i = 0; i < numSprites; i++) {
                int x = i * spriteWidth;
                int y = row * spriteHeight;

                BufferedImage sprite = spriteSheet.getSubimage(x, y, spriteWidth, spriteHeight);
                sprites[i] = getScaledImage(sprite, spriteWidth, spriteHeight, scaleFactor);
            }
            return sprites;
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public BufferedImage[] getSpriteImages(BufferedImage spriteSheet, int numSprites, double y, int spriteWidth, int spriteHeight) {
        try{
            BufferedImage[] sprites = new BufferedImage[numSprites];

            for (int i = 0; i < numSprites; i++) {
                int x = i * spriteWidth;

                sprites[i] = spriteSheet.getSubimage(x, (int)y, spriteWidth, spriteHeight);
            }
            return sprites;
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public BufferedImage getScaledImage(BufferedImage image, int width, int height, int scaleFactor) {
        BufferedImage scaledImage = new BufferedImage(width * scaleFactor, height * scaleFactor, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g = (Graphics2D)scaledImage.getGraphics();
        g.scale(scaleFactor, scaleFactor);
        g.drawImage(image, 0, 0, null);
        g.dispose();

        return scaledImage;
    }

    private void setDeathAnimationSprites() {
        LinkedHashMap<AnimationAction, Sprite> animationAndSpriteMap = resourceTypeAndAnimationSpriteMaps.get(ResourceType.DEATHANIMATION);
        String path = resourceTypeAndSpritePathMaps.get(ResourceType.DEATHANIMATION);

        animationAndSpriteMap.put(AnimationAction.IDLE, new Sprite(AnimationAction.IDLE, 6, 0, 30, 30));

        BufferedImage spriteSheet = getSpriteSheet(path);

        try {
            for (Map.Entry<AnimationAction, Sprite> animationSpritePair : animationAndSpriteMap.entrySet()) {
                Sprite sprite = animationSpritePair.getValue();

                int numSprites = sprite.getNumSprites();
                int row = sprite.getRow();
                int spriteWidth = sprite.getSpriteWidth();
                int spriteHeight = sprite.getSpriteHeight();
                BufferedImage[] images;

                images = getSpriteImages(spriteSheet, numSprites, row, spriteWidth, spriteHeight);

                sprite.setImages(images);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setWarriorAnimationSprites() {
        LinkedHashMap<AnimationAction, Sprite> animationAndSpriteMap = resourceTypeAndAnimationSpriteMaps.get(ResourceType.WARRIOR);
        String path = resourceTypeAndSpritePathMaps.get(ResourceType.WARRIOR);

        animationAndSpriteMap.put(AnimationAction.IDLE, new Sprite(AnimationAction.IDLE, 1, 0, 40, 40));
        animationAndSpriteMap.put(AnimationAction.WALKING, new Sprite(AnimationAction.WALKING, 8, 1, 40, 40));
        animationAndSpriteMap.put(AnimationAction.BASICATK, new Sprite(AnimationAction.BASICATK, 5, 2, 80, 40));
        animationAndSpriteMap.put(AnimationAction.JUMPING, new Sprite(AnimationAction.JUMPING, 3, 3, 40, 40));
        animationAndSpriteMap.put(AnimationAction.FALLING, new Sprite(AnimationAction.FALLING, 3, 4, 40, 40));
        animationAndSpriteMap.put(AnimationAction.BASICATKUPWARDS, new Sprite(AnimationAction.BASICATKUPWARDS, 5, 5, 40, 80));
        animationAndSpriteMap.put(AnimationAction.SPECIALATK1, new Sprite(AnimationAction.SPECIALATK1, 3, 6, 80, 40));
        animationAndSpriteMap.put(AnimationAction.DASHING, new Sprite(AnimationAction.DASHING, 8, 7, 40, 40));
        animationAndSpriteMap.put(AnimationAction.KNOCKBACK, new Sprite(AnimationAction.KNOCKBACK, 2, 8, 40, 40));
        animationAndSpriteMap.put(AnimationAction.DEAD, new Sprite(AnimationAction.DEAD, 1, 9, 40, 40));

        BufferedImage spriteSheet = getSpriteSheet(path);
        int count = 0;
        try {
            for (Map.Entry<AnimationAction, Sprite> animationSpritePair : animationAndSpriteMap.entrySet()) {
                AnimationAction animationAction = animationSpritePair.getKey();
                Sprite sprite = animationSpritePair.getValue();

                int numSprites = sprite.getNumSprites();
                int row = sprite.getRow();
                int spriteWidth = sprite.getSpriteWidth();
                int spriteHeight = sprite.getSpriteHeight();
                BufferedImage[] images;

                if (animationAction == AnimationAction.SPECIALATK1) {
                    images = getSpriteImages(spriteSheet, numSprites, (double)count, spriteWidth, spriteHeight);
                }
                else if (animationAction == AnimationAction.DASHING) {
                    images = getSpriteImages(spriteSheet, numSprites, (double)count, spriteWidth, spriteHeight);
                }
                else {
                    images = getSpriteImages(spriteSheet, numSprites, row, spriteWidth, spriteHeight);
                }

                count += spriteHeight;
                sprite.setImages(images);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void setPortalGreenAnimationSprites() {
        LinkedHashMap<AnimationAction, Sprite> animationAndSpriteMap = resourceTypeAndAnimationSpriteMaps.get(ResourceType.GREEN_PORTAL);
        String path = resourceTypeAndSpritePathMaps.get(ResourceType.GREEN_PORTAL);

        animationAndSpriteMap.put(AnimationAction.IDLE, new Sprite(AnimationAction.IDLE, 8, 0, 64, 64));
        animationAndSpriteMap.put(AnimationAction.SPAWN, new Sprite(AnimationAction.SPAWN, 8, 1,64, 64));

        BufferedImage spriteSheet = getSpriteSheet(path);

        try {
            for (Map.Entry<AnimationAction, Sprite> animationSpritePair : animationAndSpriteMap.entrySet()) {
                Sprite sprite = animationSpritePair.getValue();

                int numSprites = sprite.getNumSprites();
                int row = sprite.getRow();
                int spriteWidth = sprite.getSpriteWidth();
                int spriteHeight = sprite.getSpriteHeight();
                BufferedImage[] images;

                images = getSpriteImages(spriteSheet, numSprites, row, spriteWidth, spriteHeight);

                sprite.setImages(images);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setPortalPurpleAnimationSprites() {
        LinkedHashMap<AnimationAction, Sprite> animationAndSpriteMap = resourceTypeAndAnimationSpriteMaps.get(ResourceType.PURPLE_PORTAL);
        String path = resourceTypeAndSpritePathMaps.get(ResourceType.PURPLE_PORTAL);

        animationAndSpriteMap.put(AnimationAction.IDLE, new Sprite(AnimationAction.IDLE, 8, 0, 64, 64));
        animationAndSpriteMap.put(AnimationAction.SPAWN, new Sprite(AnimationAction.SPAWN, 8, 1,64, 64));

        BufferedImage spriteSheet = getSpriteSheet(path);

        try {
            for (Map.Entry<AnimationAction, Sprite> animationSpritePair : animationAndSpriteMap.entrySet()) {
                Sprite sprite = animationSpritePair.getValue();

                int numSprites = sprite.getNumSprites();
                int row = sprite.getRow();
                int spriteWidth = sprite.getSpriteWidth();
                int spriteHeight = sprite.getSpriteHeight();
                BufferedImage[] images;

                images = getSpriteImages(spriteSheet, numSprites, row, spriteWidth, spriteHeight);

                sprite.setImages(images);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setSnailAnimationSprites() {
        LinkedHashMap<AnimationAction, Sprite> animationAndSpriteMap = resourceTypeAndAnimationSpriteMaps.get(ResourceType.SNAIL);
        String path = resourceTypeAndSpritePathMaps.get(ResourceType.SNAIL);

        animationAndSpriteMap.put(AnimationAction.WALKING, new Sprite(AnimationAction.WALKING, 3, 0, 30, 30));

        BufferedImage spriteSheet = getSpriteSheet(path);

        try {
            for (Map.Entry<AnimationAction, Sprite> animationSpritePair : animationAndSpriteMap.entrySet()) {
                Sprite sprite = animationSpritePair.getValue();

                int numSprites = sprite.getNumSprites();
                int row = sprite.getRow();
                int spriteWidth = sprite.getSpriteWidth();
                int spriteHeight = sprite.getSpriteHeight();
                BufferedImage[] images;

                images = getSpriteImages(spriteSheet, numSprites, row, spriteWidth, spriteHeight);

                sprite.setImages(images);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setFireBallPurpleAnimationSprites() {
        LinkedHashMap<AnimationAction, Sprite> animationAndSpriteMap = resourceTypeAndAnimationSpriteMaps.get(ResourceType.PURPLE_FIREBALL);
        String path = resourceTypeAndSpritePathMaps.get(ResourceType.PURPLE_FIREBALL);

        animationAndSpriteMap.put(AnimationAction.SPECIALATK1, new Sprite(AnimationAction.SPECIALATK1, 4, 0, 30, 30));
        animationAndSpriteMap.put(AnimationAction.DEAD, new Sprite(AnimationAction.DEAD, 3, 1, 30, 30));

        BufferedImage spriteSheet = getSpriteSheet(path);

        try {
            for (Map.Entry<AnimationAction, Sprite> animationSpritePair : animationAndSpriteMap.entrySet()) {
                Sprite sprite = animationSpritePair.getValue();

                int numSprites = sprite.getNumSprites();
                int row = sprite.getRow();
                int spriteWidth = sprite.getSpriteWidth();
                int spriteHeight = sprite.getSpriteHeight();
                BufferedImage[] images;

                images = getSpriteImages(spriteSheet, numSprites, row, spriteWidth, spriteHeight);

                sprite.setImages(images);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setFireBallOrangeAnimationSprites() {
        LinkedHashMap<AnimationAction, Sprite> animationAndSpriteMap = resourceTypeAndAnimationSpriteMaps.get(ResourceType.ORANGE_FIREBALL);
        String path = resourceTypeAndSpritePathMaps.get(ResourceType.ORANGE_FIREBALL);

        animationAndSpriteMap.put(AnimationAction.SPECIALATK1, new Sprite(AnimationAction.SPECIALATK1, 4, 0, 30, 30));
        animationAndSpriteMap.put(AnimationAction.DEAD, new Sprite(AnimationAction.DEAD, 3, 1, 30, 30));

        BufferedImage spriteSheet = getSpriteSheet(path);

        try {
            for (Map.Entry<AnimationAction, Sprite> animationSpritePair : animationAndSpriteMap.entrySet()) {
                Sprite sprite = animationSpritePair.getValue();

                int numSprites = sprite.getNumSprites();
                int row = sprite.getRow();
                int spriteWidth = sprite.getSpriteWidth();
                int spriteHeight = sprite.getSpriteHeight();
                BufferedImage[] images;

                images = getSpriteImages(spriteSheet, numSprites, row, spriteWidth, spriteHeight);

                sprite.setImages(images);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setMinotaurAnimationSprites() {
        LinkedHashMap<AnimationAction, Sprite> animationAndSpriteMap = resourceTypeAndAnimationSpriteMaps.get(ResourceType.MINOTAUR);
        String path = resourceTypeAndSpritePathMaps.get(ResourceType.MINOTAUR);

        animationAndSpriteMap.put(AnimationAction.IDLE, new Sprite(AnimationAction.IDLE, 5, 0, 96, 64));
        animationAndSpriteMap.put(AnimationAction.WALKING, new Sprite(AnimationAction.WALKING, 8, 1, 96, 64));
        animationAndSpriteMap.put(AnimationAction.DASHING, new Sprite(AnimationAction.DASHING, 8, 1, 96, 64));
        animationAndSpriteMap.put(AnimationAction.TAUNT, new Sprite(AnimationAction.TAUNT, 5, 2, 96, 64));
        animationAndSpriteMap.put(AnimationAction.SPECIALATK1, new Sprite(AnimationAction.SPECIALATK1, 9, 3, 96, 64));
        animationAndSpriteMap.put(AnimationAction.SPECIALATK2, new Sprite(AnimationAction.SPECIALATK2, 5, 4, 96, 64));
        animationAndSpriteMap.put(AnimationAction.SPECIALATK3, new Sprite(AnimationAction.SPECIALATK3, 9, 6, 96, 64));
        animationAndSpriteMap.put(AnimationAction.DEAD, new Sprite(AnimationAction.DEAD, 6, 9, 96, 64));

        BufferedImage spriteSheet = getSpriteSheet(path);

        int heightBetweenSprite = 32;
        int widthBetweenSprite = 0;

        try {
            for (Map.Entry<AnimationAction, Sprite> animationSpritePair : animationAndSpriteMap.entrySet()) {
                Sprite sprite = animationSpritePair.getValue();

                int numSprites = sprite.getNumSprites();
                int row = sprite.getRow();
                int spriteWidth = sprite.getSpriteWidth();
                int spriteHeight = sprite.getSpriteHeight();
                BufferedImage[] images = getSpriteImages(spriteSheet, numSprites, row, spriteWidth, spriteHeight, widthBetweenSprite, heightBetweenSprite);

                sprite.setImages(images);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setImpAnimationSprites() {
        LinkedHashMap<AnimationAction, Sprite> animationAndSpriteMap = resourceTypeAndAnimationSpriteMaps.get(ResourceType.IMP);
        String path = resourceTypeAndSpritePathMaps.get(ResourceType.IMP);

        animationAndSpriteMap.put(AnimationAction.IDLE, new Sprite(AnimationAction.IDLE, 7, 0, 16, 14));
        animationAndSpriteMap.put(AnimationAction.WALKING, new Sprite(AnimationAction.WALKING, 8, 1, 16, 14));
        animationAndSpriteMap.put(AnimationAction.SPECIALATK1, new Sprite(AnimationAction.SPECIALATK1, 6, 2, 16, 14));
        animationAndSpriteMap.put(AnimationAction.DEAD, new Sprite(AnimationAction.DEAD, 6, 4, 16, 14));

        BufferedImage spriteSheet = getSpriteSheet(path);

        int heightBetweenSprite = 16;
        int widthBetweenSprite = 16;

        try {
            for (Map.Entry<AnimationAction, Sprite> animationSpritePair : animationAndSpriteMap.entrySet()) {
                Sprite sprite = animationSpritePair.getValue();

                int numSprites = sprite.getNumSprites();
                int row = sprite.getRow();
                int spriteWidth = sprite.getSpriteWidth();
                int spriteHeight = sprite.getSpriteHeight();
                BufferedImage[] images = getSpriteImages(spriteSheet, numSprites, row, spriteWidth, spriteHeight, widthBetweenSprite, heightBetweenSprite, 2);

                sprite.setImages(images);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setDragonAnimationSprites() {
        LinkedHashMap<AnimationAction, Sprite> animationAndSpriteMap = resourceTypeAndAnimationSpriteMaps.get(ResourceType.DRAGON);
        String path = resourceTypeAndSpritePathMaps.get(ResourceType.DRAGON);

        animationAndSpriteMap.put(AnimationAction.IDLE, new Sprite(AnimationAction.IDLE, 2, 0, 30, 30));
        animationAndSpriteMap.put(AnimationAction.WALKING, new Sprite(AnimationAction.WALKING, 8, 1, 30, 30));
        animationAndSpriteMap.put(AnimationAction.JUMPING, new Sprite(AnimationAction.JUMPING, 1, 2, 30, 30));
        animationAndSpriteMap.put(AnimationAction.FALLING, new Sprite(AnimationAction.FALLING, 2, 3, 30, 30));
        animationAndSpriteMap.put(AnimationAction.GLIDING, new Sprite(AnimationAction.GLIDING, 4, 4, 30, 30));
        animationAndSpriteMap.put(AnimationAction.SPECIALATK1, new Sprite(AnimationAction.SPECIALATK1, 2, 5, 30, 30));
        animationAndSpriteMap.put(AnimationAction.BASICATK, new Sprite(AnimationAction.BASICATK, 5, 6, 30, 30));

        BufferedImage spriteSheet = getSpriteSheet(path);

        try {
            for (Map.Entry<AnimationAction, Sprite> animationSpritePair : animationAndSpriteMap.entrySet()) {
                AnimationAction animationAction = animationSpritePair.getKey();
                Sprite sprite = animationSpritePair.getValue();

                int numSprites = sprite.getNumSprites();
                int row = sprite.getRow();
                int spriteWidth = sprite.getSpriteWidth();
                int spriteHeight = sprite.getSpriteHeight();
                BufferedImage[] images;

                if (animationAction != AnimationAction.BASICATK) {
                    images = getSpriteImages(spriteSheet, numSprites, row, spriteWidth, spriteHeight);
                } else {
                    images = getSpriteImages(spriteSheet, numSprites, row, spriteWidth * 2, spriteHeight);
                }

                sprite.setImages(images);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
