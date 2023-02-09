package main.system.ui.maps;

import gameworld.PRJ_Control;
import gameworld.entities.ENTITY;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import main.MainGame;
import main.system.WorldRender;
import main.system.ui.Colors;
import main.system.ui.FonT;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class GameMap {
    public final Rectangle mapMover;
    private final MainGame mg;
    private final int mapPanelX = 175;
    private final int mapPanelY = 75;
    private final Point previousMousePosition = new Point(500, 500);
    private int xTile;
    private int yTile;
    private boolean followPlayer = true;
    private Image mapImage;
    public float zoom = 5;
    public float deltaZoom = 0;

    /**
     * The big ingame map when you press "M"
     *
     * @param mg Main-game reference
     */
    public GameMap(MainGame mg) {
        this.mg = mg;
        this.mapMover = new Rectangle(mapPanelX, mapPanelY, 1_570, 940);
        xTile = mg.playerX;
        yTile = mg.playerY;
        hideMapCollision();
        dragMap();
    }

    public void draw(GraphicsContext gc) {
        drawGameMapBackground(gc);
        gc.drawImage(mapImage, 175, 80);
        drawTop(gc);
    }

    public void dragMap() {
        if (followPlayer) {
            xTile = mg.playerX;
            yTile = mg.playerY;
        }
        if (mapMover.contains(mg.inputH.lastMousePosition) && mg.inputH.mouse1Pressed) {
            followPlayer = false;
            xTile += Math.max(-3, Math.min(3, previousMousePosition.x - mg.inputH.lastMousePosition.x));
            yTile += Math.max(-3, Math.min(3, previousMousePosition.y - mg.inputH.lastMousePosition.y));
        }
        if (mg.inputH.mouse2Pressed) {
            followPlayer = true;
        }
        previousMousePosition.x = mg.inputH.lastMousePosition.x;
        previousMousePosition.y = mg.inputH.lastMousePosition.y;
    }

    public void getImage() {
        BufferedImage image = new BufferedImage(1_570, 935, BufferedImage.TYPE_INT_ARGB);
        int yTileOffset, xTileOffset, entityX, entityY;
        for (int y = 0; y < 940 / zoom; y++) {
            for (int x = 0; x < 1570 / zoom; x++) {
                yTileOffset = (int) Math.max(Math.min(yTile - (940 / (zoom * 2)) + y, mg.wRender.worldSize.x - 1), 0);
                xTileOffset = (int) Math.max(Math.min(xTile - (1_570 / (zoom * 2)) + x, mg.wRender.worldSize.x - 1), 0);
                if (WorldRender.tileStorage[WorldRender.worldData[xTileOffset][yTileOffset]].collision) {
                    for (float i = y * zoom; i < y * zoom + zoom; i++) {
                        for (float b = x * zoom; b < x * zoom + zoom; b++) {
                            if (i < 935 && b < 1570 && i > 0 && b > 0) {
                                image.setRGB((int) b, (int) i, 0xD05A_6988);
                            }
                        }
                    }
                } else {
                    for (float i = y * zoom; i < y * zoom + zoom; i++) {
                        for (float b = x * zoom; b < x * zoom + zoom; b++) {
                            if (i < 935 && b < 1570 && i > 0 && b > 0) {
                                image.setRGB((int) b, (int) i, 0xD063_C74D);
                            }
                        }
                    }
                }
            }
        }

        float y = 470 + (mg.playerY - yTile) * zoom;
        float x = 785 + (mg.playerX - xTile) * zoom;
        for (float i = y; i < y + zoom; i++) {
            for (float b = x; b < x + zoom; b++) {
                if (i < 935 && b < 1_570 && i > 0 && b > 0) {
                    image.setRGB((int) b, (int) i, 0xD000_99DB);
                }
            }
        }
        synchronized (mg.PROXIMITY_ENTITIES) {
            for (gameworld.entities.ENTITY entity : mg.PROXIMITY_ENTITIES) {
                entityX = (entity.worldX + 24) / 48;
                entityY = (entity.worldY + 24) / 48;
                y = 465 + (entityY - yTile) * zoom;
                x = 785 + (entityX - xTile) * zoom;
                for (float i = y; i < y + zoom; i++) {
                    for (float b = x; b < x + zoom; b++) {
                        if (i < 935 && b < 1570 && i > 0 && b > 0) {
                            image.setRGB((int) b, (int) i, 0xD0FF_0044);
                        }
                    }
                }
            }
        }

        synchronized (mg.PROJECTILES) {
            for (PRJ_Control PRJControl : mg.PROJECTILES) {
                entityX = (int) ((PRJControl.worldPos.x + 24) / 48);
                entityY = (int) ((PRJControl.worldPos.y + 24) / 48);
                if ((entityX - xTile) < 157 && xTile - entityX <= 157 && (entityY - yTile) <= 93 && yTile - entityY < 93) {
                    y = 465 + (entityY - yTile) * zoom;
                    x = 785 + (entityX - xTile) * zoom;
                    for (float i = y; i < y + 2; i++) {
                        for (float b = x; b < x + 2; b++) {
                            image.setRGB((int) b, (int) i, 0xD0FF_0044);
                        }
                    }
                }
            }
        }
        for (ENTITY entity : mg.npcControl.NPC_Active) {
            entityX = (entity.worldX + 24) / 48;
            entityY = (entity.worldY + 24) / 48;
            y = 465 + (entityY - yTile) * zoom;
            x = 785 + (entityX - xTile) * zoom;
            if ((entityX - xTile) < 157 && xTile - entityX <= 157 && (entityY - yTile) <= 93 && yTile - entityY < 93) {
                for (float i = y; i < y + zoom; i++) {
                    for (float b = x; b < x + zoom; b++) {
                        if (i < 935 && b < 1570 && i > 0 && b > 0) {
                            image.setRGB((int) b, (int) i, 0xD012_4E89);
                        }
                    }
                }
            }
        }
        mapImage = SwingFXUtils.toFXImage(image, null);
    }

    private void drawGameMapBackground(GraphicsContext gc) {
        gc.setFill(Colors.LightGreyAlpha);
        gc.fillRoundRect(175, 75, 1_570, 940, 25, 25);
    }

    private void drawTop(GraphicsContext gc) {
        gc.setStroke(Colors.darkBackground);
        gc.setLineWidth(5);
        gc.strokeRoundRect(175, 70, 1_570, 945, 15, 15);
        gc.strokeRoundRect(175, 70, 1_570, 15, 15, 15);
        gc.setLineWidth(1);
        gc.setFill(Colors.mediumVeryLight);
        gc.fillRoundRect(175, 70, 1_570, 15, 10, 10);
        gc.setFill(Colors.darkBackground);
        gc.setFont(FonT.minecraftBold13);
        gc.fillText("World Map", 872, 82);
    }


    public void hideMapCollision() {
        mapMover.y = -1_100;
    }

    public void resetMapCollision() {
        mapMover.y = 75;
    }
}
