package gameworld.maps;

import gameworld.entitys.Grunt;
import main.MainGame;

public class MapQuadrant {

    public int size, difficulty, numberOfEnemies, startTileX, startTileY;
    public boolean spawned;
    MainGame mg;

    public MapQuadrant(int difficulty, MainGame mg, int startTileX, int startTileY, int size, int numberOfEnemies) {
        this.startTileX = startTileX;
        this.startTileY = startTileY;
        this.size = size;
        this.mg = mg;
        this.numberOfEnemies = numberOfEnemies;
        this.difficulty = difficulty;
    }


    public void spawnEnemies() {
        if (!spawned) {
            int spawnedEnemies = 0;
            int xTile, yTile;
            while (spawnedEnemies < numberOfEnemies) {
                xTile = Math.max(0, Math.min((int) (Math.random() * size + 1) + startTileX, 499));
                yTile = Math.max(0, Math.min((int) (Math.random() * size + 1) + startTileY, 499));
                if (!mg.wRender.tileStorage[mg.wRender.worldData[xTile][yTile]].collision) {
                    mg.ENTITIES.add(new Grunt(mg, xTile * mg.tileSize, yTile * mg.tileSize, difficulty));
                    spawnedEnemies++;
                }
            }
            spawned = true;
        }
    }
}

