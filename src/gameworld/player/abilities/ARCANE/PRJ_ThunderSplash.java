package gameworld.player.abilities.ARCANE;

import gameworld.entities.damage.DamageType;
import gameworld.entities.damage.effects.debuffs.DEBUF_Stun;
import gameworld.entities.loadinghelper.ProjectilePreloader;
import gameworld.player.PROJECTILE;
import gameworld.player.Player;
import gameworld.player.ProjectileType;
import input.InputHandler;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.media.MediaPlayer;

import java.awt.Rectangle;
import java.awt.geom.Point2D;

public class PRJ_ThunderSplash extends PROJECTILE {


    public PRJ_ThunderSplash(float weapon_damage_percent) {
        this.weapon_damage_percent = weapon_damage_percent;
        this.procEffects[0] = new DEBUF_Stun(180, 1, 1, false, this.getClass());
        this.type = DamageType.Arcane;
        damageDead = true;
        this.resource = ProjectilePreloader.thunderSplash;
        this.sounds[0] = new MediaPlayer(resource.sounds.get(0).getMedia());
        this.sounds[0].setOnEndOfMedia(() -> sounds[0].dispose());
        this.worldPos = new Point2D.Double(Player.worldX - Player.screenX + InputHandler.instance.lastMousePosition.x - 24, Player.worldY + InputHandler.instance.lastMousePosition.y - Player.screenY - 24);
        collisionBox = new Rectangle(0, 0, 48, 48);
        direction = "leftrightdownup";
        projectileType = ProjectileType.OneHitNoDMG;
        playStartSound();
    }


    @Override
    public void draw(GraphicsContext gc) {
        switch (spriteCounter % 160 / 10) {
            case 0 ->
                    gc.drawImage(resource.images1.get(0), (int) worldPos.x - Player.worldX + Player.screenX, (int) worldPos.y - Player.worldY + Player.screenY);
            case 1 ->
                    gc.drawImage(resource.images1.get(1), (int) worldPos.x - Player.worldX + Player.screenX, (int) worldPos.y - Player.worldY + Player.screenY);
            case 2 ->
                    gc.drawImage(resource.images1.get(2), (int) worldPos.x - Player.worldX + Player.screenX, (int) worldPos.y - Player.worldY + Player.screenY);
            case 3 ->
                    gc.drawImage(resource.images1.get(3), (int) worldPos.x - Player.worldX + Player.screenX, (int) worldPos.y - Player.worldY + Player.screenY);
            case 4 ->
                    gc.drawImage(resource.images1.get(4), (int) worldPos.x - Player.worldX + Player.screenX, (int) worldPos.y - Player.worldY + Player.screenY);
            case 5 ->
                    gc.drawImage(resource.images1.get(5), (int) worldPos.x - Player.worldX + Player.screenX, (int) worldPos.y - Player.worldY + Player.screenY);
            case 6 ->
                    gc.drawImage(resource.images1.get(6), (int) worldPos.x - Player.worldX + Player.screenX, (int) worldPos.y - Player.worldY + Player.screenY);
            case 7 ->
                    gc.drawImage(resource.images1.get(7), (int) worldPos.x - Player.worldX + Player.screenX, (int) worldPos.y - Player.worldY + Player.screenY);
            case 8 ->
                    gc.drawImage(resource.images1.get(8), (int) worldPos.x - Player.worldX + Player.screenX, (int) worldPos.y - Player.worldY + Player.screenY);
            case 9 ->
                    gc.drawImage(resource.images1.get(9), (int) worldPos.x - Player.worldX + Player.screenX, (int) worldPos.y - Player.worldY + Player.screenY);
            case 10 ->
                    gc.drawImage(resource.images1.get(10), (int) worldPos.x - Player.worldX + Player.screenX, (int) worldPos.y - Player.worldY + Player.screenY);
            case 11 ->
                    gc.drawImage(resource.images1.get(11), (int) worldPos.x - Player.worldX + Player.screenX, (int) worldPos.y - Player.worldY + Player.screenY);
            case 12 ->
                    gc.drawImage(resource.images1.get(12), (int) worldPos.x - Player.worldX + Player.screenX, (int) worldPos.y - Player.worldY + Player.screenY);
            case 13 ->
                    gc.drawImage(resource.images1.get(13), (int) worldPos.x - Player.worldX + Player.screenX, (int) worldPos.y - Player.worldY + Player.screenY);
            case 14 -> dead = true;
        }

        if (spriteCounter == 60) {
            damageDead = false;
        }
        spriteCounter++;
    }

    @Override
    public void playHitSound() {

    }

    /**
     *
     */
    @Override
    public void update() {

    }
}
