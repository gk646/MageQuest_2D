package gameworld;


import gameworld.entitys.Player;
import gameworld.entitys.Player2;
import gameworld.maps.OverWorld;
import main.WorldRender;

import java.awt.*;

public class World {
    public static int[][] worldData;
    public static Point worldSize;

    public static void setWorldData() {
        WorldRender.worldData = OverWorld.loadMap();
        WorldRender.worldSize = OverWorld.loadMapSize();
        Player.startingPoint = OverWorld.loadMapStartPoint();
        Player2.startingPoint = OverWorld.loadMapStartPoint();
    }
}