package main.system;

import gameworld.PRJ_Control;
import gameworld.entities.ENTITY;
import gameworld.player.Player;
import main.MainGame;

import java.awt.Rectangle;

public class CollisionChecker {
    private final MainGame mg;

    public CollisionChecker(MainGame mainGame) {
        this.mg = mainGame;
    }

    public void checkEntityAgainstTile(ENTITY ENTITYControl) {
        int entityLeftWorldX = ENTITYControl.worldX + ENTITYControl.collisionBox.x;
        int entityRightWorldX = ENTITYControl.worldX + ENTITYControl.collisionBox.x + ENTITYControl.collisionBox.width;
        int entityTopWorldY = ENTITYControl.worldY + ENTITYControl.collisionBox.y;
        int entityBottomWorldY = ENTITYControl.worldY + ENTITYControl.collisionBox.y + ENTITYControl.collisionBox.height;

        int entityLeftCol;
        int entityRightCol;
        int entityTopRow = Math.max(Math.min(entityTopWorldY / 48, mg.wRender.worldSize.x), 0);
        int entityBottomRow = Math.min(entityBottomWorldY / 48, mg.wRender.worldSize.x);

        int tileNum1, tileNum2;
        if (ENTITYControl.direction.contains("right")) {
            entityRightCol = Math.min((entityRightWorldX + ENTITYControl.movementSpeed) / 48, mg.wRender.worldSize.x);
            tileNum1 = mg.wRender.worldData[entityRightCol][entityTopRow];
            tileNum2 = mg.wRender.worldData[entityRightCol][entityBottomRow];
            if (mg.wRender.tileStorage[tileNum1].collision || mg.wRender.tileStorage[tileNum2].collision) {
                ENTITYControl.collisionRight = true;
            }
        }
        if (ENTITYControl.direction.contains("left")) {
            entityLeftCol = Math.min((entityLeftWorldX - ENTITYControl.movementSpeed) / 48, mg.wRender.worldSize.x);
            tileNum1 = mg.wRender.worldData[entityLeftCol][entityTopRow];
            tileNum2 = mg.wRender.worldData[entityLeftCol][entityBottomRow];
            if (mg.wRender.tileStorage[tileNum1].collision || mg.wRender.tileStorage[tileNum2].collision) {
                ENTITYControl.collisionLeft = true;
            }
        }
        entityLeftCol = Math.min(entityLeftWorldX / 48, mg.wRender.worldSize.x);
        entityRightCol = Math.min(entityRightWorldX / 48, mg.wRender.worldSize.x);
        if (ENTITYControl.direction.contains("up")) {

            entityTopRow = Math.min((entityTopWorldY - ENTITYControl.movementSpeed) / 48, mg.wRender.worldSize.x);
            tileNum1 = mg.wRender.worldData[entityLeftCol][entityTopRow];
            tileNum2 = mg.wRender.worldData[entityRightCol][entityTopRow];
            if (mg.wRender.tileStorage[tileNum1].collision || mg.wRender.tileStorage[tileNum2].collision) {
                ENTITYControl.collisionUp = true;
            }
        }
        entityLeftCol = Math.min(entityLeftWorldX / 48, mg.wRender.worldSize.x);

        if (ENTITYControl.direction.contains("down")) {
            entityBottomRow = Math.min((entityBottomWorldY + ENTITYControl.movementSpeed) / 48, mg.wRender.worldSize.x);
            tileNum1 = mg.wRender.worldData[entityLeftCol][entityBottomRow];
            tileNum2 = mg.wRender.worldData[entityRightCol][entityBottomRow];
            if (mg.wRender.tileStorage[tileNum1].collision || mg.wRender.tileStorage[tileNum2].collision) {
                ENTITYControl.collisionDown = true;
            }
        }
    }

    public boolean checkEntityAgainstEntity(ENTITY checkingForHit, ENTITY incomingToHit) {
        return new Rectangle(checkingForHit.worldX, checkingForHit.worldY, checkingForHit.collisionBox.width, checkingForHit.collisionBox.height).intersects(new Rectangle(incomingToHit.worldX, incomingToHit.worldY, incomingToHit.collisionBox.width, incomingToHit.collisionBox.height));
    }

    public boolean checkEntityAgainstProjectile(ENTITY checkingForHit, PRJ_Control incomingToHit) {
        return new Rectangle(checkingForHit.worldX, checkingForHit.worldY, checkingForHit.collisionBox.width, checkingForHit.collisionBox.height).intersects(new Rectangle((int) incomingToHit.worldPos.x, (int) incomingToHit.worldPos.y, incomingToHit.collisionBox.width, incomingToHit.collisionBox.height));
    }

    public void checkProjectileAgainstTile(PRJ_Control projectile) {
        int entityLeftWorldX = (int) (projectile.worldPos.x + projectile.collisionBox.x);
        int entityRightWorldX = (int) (projectile.worldPos.x + projectile.collisionBox.x + projectile.collisionBox.width);
        int entityTopWorldY = (int) (projectile.worldPos.y + projectile.collisionBox.y);
        int entityBottomWorldY = (int) (projectile.worldPos.y + projectile.collisionBox.y + projectile.collisionBox.height);

        int entityLeftCol;
        int entityRightCol;
        int entityTopRow = Math.max(Math.min(entityTopWorldY / 48, 499), 0);
        int entityBottomRow = Math.max(0, Math.min(entityBottomWorldY / 48, 499));

        int tileNum1, tileNum2;
        if (projectile.direction.contains("right")) {
            entityRightCol = Math.max(0, Math.min((entityRightWorldX + projectile.movementSpeed) / 48, 499));
            tileNum1 = mg.wRender.worldData[entityRightCol][entityTopRow];
            tileNum2 = mg.wRender.worldData[entityRightCol][entityBottomRow];
            if (mg.wRender.tileStorage[tileNum1].collision || mg.wRender.tileStorage[tileNum2].collision) {
                projectile.collisionRight = true;
            }
        }
        if (projectile.direction.contains("left")) {
            entityLeftCol = Math.max(0, Math.min((entityLeftWorldX - projectile.movementSpeed) / 48, 499));
            tileNum1 = mg.wRender.worldData[entityLeftCol][entityTopRow];
            tileNum2 = mg.wRender.worldData[entityLeftCol][entityBottomRow];
            if (mg.wRender.tileStorage[tileNum1].collision || mg.wRender.tileStorage[tileNum2].collision) {
                projectile.collisionLeft = true;
            }
        }
        entityLeftCol = Math.max(0, Math.min(entityLeftWorldX / 48, 499));
        entityRightCol = Math.max(0, Math.min(entityRightWorldX / 48, 499));
        if (projectile.direction.contains("up")) {
            entityTopRow = Math.max(0, Math.min((entityTopWorldY - projectile.movementSpeed) / 48, 499));
            tileNum1 = mg.wRender.worldData[entityLeftCol][entityTopRow];
            tileNum2 = mg.wRender.worldData[entityRightCol][entityTopRow];
            if (mg.wRender.tileStorage[tileNum1].collision || mg.wRender.tileStorage[tileNum2].collision) {
                projectile.collisionUp = true;
            }
        }
        entityLeftCol = Math.max(0, Math.min(entityLeftWorldX / 48, 499));

        if (projectile.direction.contains("down")) {
            entityBottomRow = Math.max(0, Math.min((entityBottomWorldY + projectile.movementSpeed) / 48, 499));
            tileNum1 = mg.wRender.worldData[entityLeftCol][entityBottomRow];
            tileNum2 = mg.wRender.worldData[entityRightCol][entityBottomRow];
            if (mg.wRender.tileStorage[tileNum1].collision || mg.wRender.tileStorage[tileNum2].collision) {
                projectile.collisionDown = true;
            }
        }
    }

    public void checkPlayerAgainstTile(Player player) {
        int entityLeftWorldX = Player.worldX + player.collisionBox.x;
        int entityRightWorldX = Player.worldX + player.collisionBox.x + player.collisionBox.width;
        int entityTopWorldY = Player.worldY + player.collisionBox.y;
        int entityBottomWorldY = Player.worldY + player.collisionBox.y + player.collisionBox.height;

        int entityLeftCol;
        int entityRightCol;
        int entityTopRow = Math.max(Math.min(entityTopWorldY / 48, mg.wRender.worldSize.x), 0);
        int entityBottomRow = Math.min(entityBottomWorldY / 48, mg.wRender.worldSize.x);

        int tileNum1, tileNum2;
        if (player.direction.contains("right")) {
            entityRightCol = Math.min((entityRightWorldX + player.movementSpeed) / 48, mg.wRender.worldSize.x);
            tileNum1 = mg.wRender.worldData[entityRightCol][entityTopRow];
            tileNum2 = mg.wRender.worldData[entityRightCol][entityBottomRow];
            if (mg.wRender.tileStorage[tileNum1].collision || mg.wRender.tileStorage[tileNum2].collision) {
                player.collisionRight = true;
            }
        }
        if (player.direction.contains("left")) {
            entityLeftCol = Math.min((entityLeftWorldX - player.movementSpeed) / 48, mg.wRender.worldSize.x);
            tileNum1 = mg.wRender.worldData[entityLeftCol][entityTopRow];
            tileNum2 = mg.wRender.worldData[entityLeftCol][entityBottomRow];
            if (mg.wRender.tileStorage[tileNum1].collision || mg.wRender.tileStorage[tileNum2].collision) {
                player.collisionLeft = true;
            }
        }
        entityLeftCol = Math.min(entityLeftWorldX / 48, mg.wRender.worldSize.x);
        entityRightCol = Math.min(entityRightWorldX / 48, mg.wRender.worldSize.x);
        if (player.direction.contains("up")) {

            entityTopRow = Math.min((entityTopWorldY - player.movementSpeed) / 48, mg.wRender.worldSize.x);
            tileNum1 = mg.wRender.worldData[entityLeftCol][entityTopRow];
            tileNum2 = mg.wRender.worldData[entityRightCol][entityTopRow];
            if (mg.wRender.tileStorage[tileNum1].collision || mg.wRender.tileStorage[tileNum2].collision) {
                player.collisionUp = true;
            }
        }
        entityLeftCol = Math.min(entityLeftWorldX / 48, mg.wRender.worldSize.x);

        if (player.direction.contains("down")) {
            entityBottomRow = Math.min((entityBottomWorldY + player.movementSpeed) / 48, mg.wRender.worldSize.x);
            tileNum1 = mg.wRender.worldData[entityLeftCol][entityBottomRow];
            tileNum2 = mg.wRender.worldData[entityRightCol][entityBottomRow];
            if (mg.wRender.tileStorage[tileNum1].collision || mg.wRender.tileStorage[tileNum2].collision) {
                player.collisionDown = true;
            }
        }
    }
}


