package main.system;

import gameworld.player.Player;
import main.MainGame;
import main.system.enums.Map;

import java.awt.Point;

public class Utilities {
    // WORLD CODES
    // 0 = Tutorial
    // 1 = Grass Lands
    // 2 = City 1
    // 3 =
    //


    private final MainGame mg;

    public Utilities(MainGame mg) {
        this.mg = mg;
    }

    public void checkTeleports() {
        if (mg.wControl.currentWorld == Map.Tutorial) {
            if (mg.playerX == 1 && mg.playerY == 1) {
                mg.wControl.load_city1(10, 10);
                mg.player.spawnLevel = 1;
            }
        }
        if (mg.wControl.currentWorld == Map.GrassLands) {
            if (mg.playerX == 499 && mg.playerY == 499) {
                mg.wControl.load_city1(10, 10);
            }
        }
        if (mg.wControl.currentWorld == Map.City1) {
            if (mg.playerX == 32 && mg.playerY == 0 ||
                    mg.playerX == 33 && mg.playerY == 0 ||
                    mg.playerX == 34 && mg.playerY == 0 ||
                    mg.playerX == 35 && mg.playerY == 0 ||
                    mg.playerX == 36 && mg.playerY == 0 ||
                    mg.playerX == 37 && mg.playerY == 0) {
                mg.wControl.load_OverWorldMap(495, 495);
            }
        }
    }

    public void loadSpawnLevel() {
        if (mg.player.spawnLevel == 0) {
            mg.wControl.load_tutorial(4, 4);
        } else if (mg.player.spawnLevel == 1) {
            mg.wControl.load_city1(40, 18);
        }
    }

    public boolean player_went_away(Point playerLocation) {
        return Point.distance(playerLocation.x, playerLocation.y, Player.worldX, Player.worldY) > 500;
    }
}
