package gameworld.entities.npcs.quests;

import gameworld.entities.loadinghelper.ResourceLoaderEntity;
import gameworld.entities.npcs.generic.NPC_Generic;
import gameworld.player.Player;
import gameworld.quest.Dialog;
import javafx.scene.canvas.GraphicsContext;
import main.MainGame;
import main.system.enums.Zone;

import java.awt.Rectangle;


public class NPC_GroveReceptionist extends NPC_Generic {


    public NPC_GroveReceptionist(MainGame mainGame, int xTile, int yTile, Zone zone) {
        this.dialog = new Dialog();
        this.animation = new ResourceLoaderEntity("npc/woman");
        animation.load();
        this.mg = mainGame;
        this.zone = zone;
        worldX = xTile * 48;
        worldY = yTile * 48;
        this.entityHeight = 48;
        this.entityWidth = 48;
        this.movementSpeed = 1.5f;
        this.collisionBox = new Rectangle(0, 0, 42, 42);
        direction = "updownleftright";
        spriteCounter = (int) (Math.random() * 20);
    }

    @Override
    public void update() {
        super.update();
        if (show_dialog) {
            dialogHideDelay++;
            show_dialog = !mg.wControl.player_went_away(playerTalkLocation, 500);
        }
        if (dialogHideDelay > 600) {
            show_dialog = false;
            dialogHideDelay = 0;
        }
    }

    public void drawDialog(GraphicsContext gc) {

        dialog.drawDialog(gc, this);
    }

    @Override
    public void draw(GraphicsContext gc) {
        screenX = (int) (worldX - Player.worldX + Player.screenX);
        screenY = (int) (worldY - Player.worldY + Player.screenY);

        if (onPath) {
            drawWalk(gc);
        } else {
            drawIdle(gc);
        }
        spriteCounter++;
        drawNPCName(gc, "Receptionist");
    }

    private void drawIdle(GraphicsContext gc) {
        switch (spriteCounter % 120 / 30) {
            case 0 -> gc.drawImage(animation.idle.get(0), screenX - 6, screenY - 20);
            case 1 -> gc.drawImage(animation.idle.get(1), screenX - 6, screenY - 20);
            case 2 -> gc.drawImage(animation.idle.get(2), screenX - 6, screenY - 20);
            case 3 -> gc.drawImage(animation.idle.get(3), screenX - 6, screenY - 20);
        }
    }

    private void drawWalk(GraphicsContext gc) {
        switch (spriteCounter % 180 / 30) {
            case 0 -> gc.drawImage(animation.walk.get(0), screenX - 6, screenY - 20);
            case 1 -> gc.drawImage(animation.walk.get(1), screenX - 6, screenY - 20);
            case 2 -> gc.drawImage(animation.walk.get(2), screenX - 6, screenY - 20);
            case 3 -> gc.drawImage(animation.walk.get(3), screenX - 6, screenY - 20);
            case 4 -> gc.drawImage(animation.walk.get(4), screenX - 6, screenY - 20);
            case 5 -> gc.drawImage(animation.walk.get(5), screenX - 6, screenY - 20);
        }
    }
}
