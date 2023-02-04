package gameworld.player.abilities;

import gameworld.PRJ_Control;
import gameworld.player.Player;
import javafx.scene.canvas.GraphicsContext;
import main.MainGame;
import main.system.Storage;

import java.awt.Point;
import java.awt.geom.Point2D;

public class PRJ_EnemyStandardShot extends PRJ_Control {

    /**
     * Projectile of enemy entities
     *
     * @param mg    to access display functions
     * @param level the level and damage to the player
     */
    public PRJ_EnemyStandardShot(MainGame mg, int x, int y, int level) {
        super(mg);
        //-------VALUES-----------
        this.movementSpeed = 3;
        this.projectileHeight = 16;
        this.projectileWidth = 16;
        this.collisionBox = Storage.box_primaryFire;
        this.level = level;
        this.direction = "downleftrightup";

        //------POSITION-----------
        this.worldPos = new Point2D.Double(x, y);
        this.updateVector = getTrajectory();
        getPlayerImage();
        this.endPos = new Point((int) (worldPos.x + 650), (int) (worldPos.y + 650));
    }

    @Override
    public void draw(GraphicsContext g2) {
        g2.drawImage(projectileImage1, (int) worldPos.x - Player.worldX + Player.screenX - 24 + 8, (int) worldPos.y - Player.worldY + Player.screenY - 24 + 8, projectileWidth, projectileHeight);
    }

    @Override
    public void update() {
        outOfBounds();
        tileCollision();
        worldPos.x += updateVector.x * movementSpeed;
        worldPos.y += updateVector.y * movementSpeed;
    }

    private Point2D.Double getTrajectory() {
        double angle = Math.atan2(Player.worldY - worldPos.y, Player.worldX - worldPos.x);
        return new Point2D.Double(Math.cos(angle), Math.sin(angle));
    }

    private void getPlayerImage() {
        projectileImage1 = Storage.primaryFire1;
    }
}