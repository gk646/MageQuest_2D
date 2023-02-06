package gameworld.world;


import gameworld.dialogue.SpawnTrigger;
import gameworld.world.maps.MAP_City1;
import gameworld.world.maps.MAP_OverWorld;
import gameworld.world.maps.MAP_Tutorial;
import main.MainGame;
import main.system.WorldRender;
import main.system.enums.Map;

import java.awt.Point;

public class WorldController {
    private final MainGame mg;
    public Map currentWorld;


    public final MapQuadrant[] overworldMapQuadrants = new MapQuadrant[100];
    private final SpawnTrigger[] overWorldTriggers = new SpawnTrigger[50];
    public SpawnTrigger[] tutorialSpawns;
    //-----OVERWORLD---------------------------
    public int[][] overWorldMapData;
    //-----HELL--------------------------
    public int[][] hell_MapData;
    //-----VALHALLA------------------------
    public int[][] valhalla_MapData;
    //-----CITY 1
    public int[][] city1_MapData;
    public Point hell_Size;
    public Point hell_StartPoint;
    //-----TUTORIAL-------------------
    private int[][] tutorialMapData;
    public Point valhalla_Size;
    public Point valhalla_StartPoint;
    private Point overWorldSize;
    private Point tutorialSize;


    public WorldController(MainGame mg) {
        this.mg = mg;
    }

    public void load_OverWorldMap(int xTile, int yTile) {
        currentWorld = Map.GrassLands;
        clearWorldArrays();
        for (MapQuadrant quadrant : overworldMapQuadrants) {
            if (quadrant.spawned) {
                quadrant.spawned = false;
            }
        }
        WorldRender.worldData = overWorldMapData;
        mg.wRender.worldSize = overWorldSize;
        mg.player.setPosition(xTile * 48, yTile * 48);
    }

    public void load_tutorial(int xTile, int yTile) {
        currentWorld = Map.Tutorial;
        clearWorldArrays();
        WorldRender.worldData = tutorialMapData;
        mg.wRender.worldSize = tutorialSize;
        mg.player.setPosition(xTile * 48, yTile * 48);
    }

    public void load_city1(int xTile, int yTile) {
        currentWorld = Map.City1;
        clearWorldArrays();
        WorldRender.worldData = city1_MapData;
        mg.wRender.worldSize = tutorialSize;
        mg.player.setPosition(xTile * 48, yTile * 48);
    }

    public void getWorldsData() {
        //Over world
        this.overWorldMapData = MAP_OverWorld.loadOverWorld();
        this.overWorldSize = MAP_OverWorld.loadMapSize();

        //Tutorial
        this.tutorialMapData = MAP_Tutorial.loadTutorial();
        this.tutorialSize = MAP_Tutorial.loadMapSize();
        this.tutorialSpawns = MAP_Tutorial.getTriggers();
        //
        this.city1_MapData = MAP_City1.loadCity();
    }

    public void makeOverWorldQuadrants() {
        int size = overWorldSize.x / 10;
        int counter = 0;
        for (int i = 0; i < 10; i++) {
            for (int b = 0; b < 10; b++) {
                if (counter != 99) {
                    overworldMapQuadrants[counter] = new MapQuadrant(19 - (i + b), mg, size * i, size * b, size, 30);
                    counter++;
                }
                overworldMapQuadrants[counter] = new MapQuadrant(19 - (i + b), mg, size * i, size * b, size, 0);
            }
        }
    }

    private void clearWorldArrays() {
        mg.npcControl.loadNPC(currentWorld);
        mg.WORLD_DROPS.clear();
        MainGame.ENTITIES.clear();
        mg.PROJECTILES.clear();
    }
}


