/*
 * MIT License
 *
 * Copyright (c) 2023 gk646
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package gameworld.entities.damage.effects;

import gameworld.player.PlayerPrompts;
import gameworld.world.WorldController;
import gameworld.world.objects.drops.DRP_ChestItem;
import main.MainGame;
import main.system.rendering.WorldRender;
import main.system.rendering.enhancements.ScriptedAnimationList;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class TileBasedEffects {
    public static int activeTile = 0;
    private static int activeTile1 = 0;
    private int[] tilesData;
    private int[] tilesData1;
    private int[] tilesData2;
    private int previousTile;
    private final ArrayList<ScriptedAnimationList> animationList = new ArrayList<>();
    private final MainGame mg;
    private boolean spawnSkeletons;

    public TileBasedEffects(MainGame mg) {
        this.mg = mg;
    }


    public void update() {
        activeTile = WorldRender.worldData[mg.playerX][mg.playerY];
        activeTile1 = WorldRender.worldData1[mg.playerX][mg.playerY];
        checkForTileEffects();
        previousTile = activeTile;
        if (animationList.size() > 0) {
            Iterator<ScriptedAnimationList> iter = animationList.iterator();
            while (iter.hasNext()) {
                ScriptedAnimationList list = iter.next();
                if (list.finished) {
                    iter.remove();
                } else {
                    list.progress();
                }
            }
        }
        if (spawnSkeletons) {

        }
    }

    public void getNearbyTiles() {
        int[][] copy = WorldRender.worldData;
        int[][] copy1 = WorldRender.worldData1;
        int[][] copy2 = WorldRender.worldData2;
        tilesData = getValuesInRadius(copy, mg.playerX, mg.playerY);
        tilesData1 = getValuesInRadius(copy1, mg.playerX, mg.playerY);
        tilesData2 = getValuesInRadius(copy2, mg.playerX, mg.playerY);
    }

    private void checkForTileEffects() {

        if (activeTile == 225 && activeTile != previousTile) { // SPIKES
            mg.player.setHealth(mg.player.getHealth() - (mg.player.maxHealth * 0.4f));
            mg.sound.playEffectSound(6);
        } else if (activeTile1 == 137) { // CHEST COMMON
            PlayerPrompts.setETrue();
            if (mg.inputH.e_typed && animationList.size() == 0) {
                mg.WORLD_DROPS.add(new DRP_ChestItem(mg, mg.playerX * 48 + 24, mg.playerY * 48 + 24, WorldController.currentWorld, mg.player.level, false));
                mg.sound.playEffectSound(4);
                animationList.add(new ScriptedAnimationList(new int[]{137, 138, 139}, 15, new Point(mg.playerX, mg.playerY), 8));
            }
        } else if (activeTile1 == 1_382) { // LEVER
            PlayerPrompts.setETrue();
            if (mg.inputH.e_typed && animationList.size() == 0) {
                animationList.add(new ScriptedAnimationList(new int[]{1_382, 1_383}, 15, new Point(mg.playerX, mg.playerY), 0));
            }
        } else if (activeTile1 == 2_191) { // CHEST EPIC
            PlayerPrompts.setETrue();
            if (mg.inputH.e_typed && animationList.size() == 0) {
                mg.dropManager.epicChestEvent(mg.playerX * 48 + 24, mg.playerY * 48 + 24, WorldController.currentWorld, mg.player.level);
                mg.sound.playEffectSound(13);
                animationList.add(new ScriptedAnimationList(new int[]{2_193, 2_195}, 15, new Point(mg.playerX, mg.playerY), 8));
            }
        }
    }

    public boolean isLavaNearby() {
        for (int i = 0; i < tilesData.length; i++) {
            int num1 = tilesData[i];
            int num2 = tilesData1[i];
            int num3 = tilesData2[i];
            if ((num1 >= 95 && num1 <= 97) || (num2 >= 95 && num2 <= 97) || (num3 >= 95 && num3 <= 97)) {
                return true;
            }
            if ((num1 >= 108 && num1 <= 110) || (num2 >= 108 && num2 <= 110) || (num3 >= 108 && num3 <= 110)) {
                return true;
            }
        }
        return false;
    }

    public boolean isWaterNearby() {
        for (int i = 0; i < tilesData.length; i++) {
            int num1 = tilesData[i];
            int num2 = tilesData1[i];
            int num3 = tilesData2[i];
            if ((num1 >= 910 && num1 <= 1_035) || (num2 >= 910 && num2 <= 1_035) || (num3 >= 910 && num3 <= 1_035)) {
                return true;
            }
            if ((num1 >= 1_118 && num1 <= 1_177) || (num2 >= 1_118 && num2 <= 1_177) || (num3 >= 1_118 && num3 <= 1_177)) {
                return true;
            }
        }
        return false;
    }

    public boolean isInOpen() {
        return (activeTile >= 338 && activeTile <= 402) || (activeTile >= 442 && activeTile <= 1338) || (activeTile >= 2086 && activeTile <= 2157);
    }


    private int[] getValuesInRadius(int[][] array, int centerX, int centerY) {
        int radius = 7;
        int size = (radius * 2 + 1) * (radius * 2 + 1);
        int[] values = new int[size];
        int index = 0;

        for (int i = centerX - radius; i <= centerX + radius; i++) {
            for (int j = centerY - radius; j <= centerY + radius; j++) {
                if (i >= 0 && i < array.length && j >= 0 && j < array[0].length) {
                    // Calculate the distance between the current point and the center point
                    double distance = Math.sqrt((i - centerX) * (i - centerX) + (j - centerY) * (j - centerY));

                    // If the distance is less than or equal to the radius, add the value to the array
                    if (distance <= radius) {
                        values[index] = array[i][j];
                        index++;
                    }
                }
            }
        }

        return Arrays.copyOf(values, index);
    }

    public boolean isFireNearby() {
        for (int i = 0; i < tilesData.length; i++) {
            int num2 = tilesData1[i];
            if ((num2 >= 1_528 && num2 <= 1_531)) {
                return true;
            }
        }
        return false;
    }
}
