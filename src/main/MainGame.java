package main;

import gameworld.ENT_Control;
import gameworld.NPC_Control;
import gameworld.PRJ_Control;
import gameworld.entities.ENTITY;
import gameworld.entities.monsters.ENT_Grunt;
import gameworld.entities.multiplayer.ENT_Player2;
import gameworld.player.Player;
import gameworld.world.MAP_UTILS;
import gameworld.world.WorldController;
import gameworld.world.effects.DayNightCycle;
import gameworld.world.objects.DROP;
import gameworld.world.objects.drops.DRP_DroppedItem;
import gameworld.world.objects.items.ITEM;
import input.InputHandler;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Duration;
import main.system.CollisionChecker;
import main.system.Multiplayer;
import main.system.Storage;
import main.system.WorldRender;
import main.system.ai.PathFinder;
import main.system.database.SQLite;
import main.system.enums.State;
import main.system.sound.Sound;
import main.system.ui.Effects;
import main.system.ui.FonT;
import main.system.ui.UI;
import main.system.ui.inventory.UI_InventoryPanel;
import main.system.ui.maps.GameMap;
import main.system.ui.maps.MiniMap;
import main.system.ui.questpanel.UI_QuestPanel;
import main.system.ui.skillbar.UI_SkillBar;
import main.system.ui.talentpane.UI_TalentPanel;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;


public class MainGame {


    private static final float FRAMES_PER_SECOND = 120;
    public static int SCREEN_WIDTH = 1_920;
    public static int SCREEN_HEIGHT = 1_080;
    public final int HALF_WIDTH;
    public final int HALF_HEIGHT;
    public static final List<ENTITY> ENTITIES = Collections.synchronizedList(new ArrayList<>());
    //---------VARIABLES----------
    public final List<PRJ_Control> PROJECTILES = Collections.synchronizedList(new ArrayList<>());
    public final List<ENTITY> PROXIMITY_ENTITIES = Collections.synchronizedList(new ArrayList<>());
    public Random random;
    //ITEMS
    public final ArrayList<DROP> WORLD_DROPS = new ArrayList<>();
    public final ArrayList<ITEM> AMULET = new ArrayList<>();
    public final ArrayList<ITEM> BOOTS = new ArrayList<>();
    public final ArrayList<ITEM> CHEST = new ArrayList<>();
    public final ArrayList<ITEM> HEAD = new ArrayList<>();
    public final ArrayList<ITEM> OFFHAND = new ArrayList<>();
    public final ArrayList<ITEM> ONEHAND = new ArrayList<>();
    public final ArrayList<ITEM> PANTS = new ArrayList<>();
    public final ArrayList<ITEM> RELICS = new ArrayList<>();
    public final ArrayList<ITEM> RINGS = new ArrayList<>();
    public final ArrayList<ITEM> TWOHANDS = new ArrayList<>();
    public final ArrayList<ITEM> MISC = new ArrayList<>();

    public final int tileSize = 48;
    public final UI ui = new UI(this);


    //---------Input-----------
    public final GraphicsContext gc;
    //----------SCREEN SETTINGS---------------
    private final Scene scene;
    public String player2Information = "";
    public InputHandler inputH;
    public int playerX, playerY;
    public CollisionChecker collisionChecker;
    public WorldController wControl;
    public WorldRender wRender;
    public ENT_Player2 ENTPlayer2;
    public Player player;
    public State gameState;
    public PathFinder pathF;

    public Storage imageSto;
    public SQLite sqLite;
    public boolean client = false, showBag, showChar, showTalents, loadingScreen, showMap;
    public PRJ_Control prj_control;
    public UI_InventoryPanel inventP;
    public UI_TalentPanel talentP;
    public NPC_Control npcControl;
    public MAP_UTILS map_utils;
    public GameMap gameMap;
    public DayNightCycle cycle = new DayNightCycle(this);
    public UI_SkillBar sBar = new UI_SkillBar(this);
    public UI_QuestPanel qPanel = new UI_QuestPanel(this);
    //---------System---------
    private MiniMap miniM;
    private Multiplayer multiplayer;
    private int counter = 0;
    private ENT_Control ent_control;
    public Sound sound;


    /**
     * Main class for the game logic and center point for information
     */
    public MainGame(int width, int height, GraphicsContext gc, Scene scene) {
        this.scene = scene;
        this.gc = gc;
        SCREEN_WIDTH = width;
        SCREEN_HEIGHT = height;
        HALF_WIDTH = SCREEN_WIDTH / 2;
        HALF_HEIGHT = SCREEN_HEIGHT / 2;
        this.loadingScreen = true;
        gameState = State.START;
        cycle.start();
    }


    void run() {
        loadGame(gc);
    }

    /**
     * Starts the 4 game threads
     */
    private void startThreads() {
        float logicCounter = 1_000_000_000 / 60.0f;

        Thread renderHelper = new Thread(() -> {
            long firstTimeGate1;
            long lastTime1 = System.nanoTime();
            float difference = 0;
            float difference1 = 0;
            float difference2 = 0;
            float fastRenderCounter = 1_000_000_000 / 360.0f;
            float fastRenderCounter2 = 1_000_000_000 / 120.0f;
            while (true) {
                firstTimeGate1 = System.nanoTime();
                difference += (firstTimeGate1 - lastTime1) / fastRenderCounter;
                difference1 += (firstTimeGate1 - lastTime1) / 1_000_000_000.0f;
                difference2 += (firstTimeGate1 - lastTime1) / fastRenderCounter2;
                lastTime1 = firstTimeGate1;
                if (difference >= 1) {
                    inventP.interactWithWindows();
                    getPlayerTile();
                    player.checkPlayerIsMoving();
                    difference = 0;
                }
                if (difference2 >= 1) {
                    if (gameState == State.PLAY) {
                        if (showMap) {
                            gameMap.dragMap();
                            gameMap.getImage();
                        }
                        player.pickupDroppedItem();
                        inventP.interactWithWindows();
                        getPlayerTile();
                        map_utils.update();
                    }
                    difference2 = 0;
                }
                if (difference1 >= 0.5) {
                    synchronized (PROXIMITY_ENTITIES) {
                        proximitySorterENTITIES();
                    }
                    difference1 = 0;
                    //System.out.println(counter);
                    counter = 0;
                }
            }
        });
        renderHelper.start();
        Timeline gameLoop = new Timeline();
        gameLoop.setCycleCount(Timeline.INDEFINITE);

        KeyFrame kf = new KeyFrame(Duration.seconds(0.007_9), ae -> drawGame(gc));

        gameLoop.getKeyFrames().add(kf);
        gameLoop.play();
        Thread playerThread = new Thread(() -> {
            long firstTimeGate1;
            long lastTime1 = System.nanoTime();
            float difference = 0;
            while (true) {
                firstTimeGate1 = System.nanoTime();
                difference += (firstTimeGate1 - lastTime1) / logicCounter;
                lastTime1 = firstTimeGate1;
                if (difference >= 1) {
                    if (gameState == State.PLAY) {
                        player.update();
                        sBar.update();
                        qPanel.update();
                    }
                    difference = 0;
                }
            }
        });
        playerThread.start();
        Thread updateThread = new Thread(() -> {
            long firstTimeGate1;
            long lastTime1 = 0;
            float difference = 0;
            while (true) {
                firstTimeGate1 = System.nanoTime();
                difference += (firstTimeGate1 - lastTime1) / logicCounter;
                lastTime1 = firstTimeGate1;
                if (difference >= 1) {
                    if (gameState == State.PLAY || gameState == State.OPTION || gameState == State.TALENT) {
                        /*
                        if (inputH.debugFps && inputH.f_pressed) {
                            multiplayer.startMultiplayerClient();
                        }
                        if (inputH.debugFps && inputH.multiplayer) {
                            multiplayer.startMultiplayerServer();
                        }
                        if (multiplayer.multiplayerStarted) {
                            multiplayer.updateMultiplayerInput();
                        }

                         */
                        prj_control.update();
                        ent_control.update();
                        npcControl.update();
                        /*
                        if (multiplayer.multiplayerStarted) {
                            multiplayer.updateMultiplayerOutput();
                        }

                         */
                    }
                    difference = 0;
                }
            }
        });
        updateThread.start();
    }

    /**
     * The main painting method to draw the screen
     */

    private void drawGame(GraphicsContext gc) {
        //Debug
        long drawStart = System.nanoTime();
        //RENDER START
        if (gameState == State.PLAY || gameState == State.OPTION) {
            wRender.draw(gc);
            drawDroppedItems(gc);
            prj_control.draw(gc);
            ent_control.draw(gc);
            npcControl.draw(gc);
            //ENTPlayer2.draw(gc);
            player.draw(gc);
            miniM.draw(gc);
            ui.draw(gc);
            qPanel.draw(gc);
            sBar.draw(gc);
            if (showMap) {
                gameMap.draw(gc);
            }
            if (showBag) {
                inventP.drawBagWindow(gc);
                inventP.drawBagTooltip(gc);
                inventP.drawDragAndDrop(gc);
            }
            if (showChar) {
                inventP.drawCharacterWindow(gc);
                inventP.drawCharTooltip(gc);
                inventP.drawDragAndDrop(gc);
            }
            if (showTalents) {
                talentP.drawTalentWindow(gc);
            }
        } else if (gameState == State.TITLE || gameState == State.TITLE_OPTION || loadingScreen) {
            ui.draw(gc);
        }
        //RENDER END
        long drawEnd = System.nanoTime();
        long difference = drawEnd - drawStart;
        if (inputH.debugFps) {
            gc.setFont(FonT.minecraftBold30);
            gc.setFill(Color.BLACK);
            gc.setFont(ui.maruMonica30);
            gc.fillText(("Draw Time" + difference), 500, 600);
            gc.fillText((24 + Player.worldX) / 48 + " " + (Player.worldY + 24) / 48, 500, 650);
            gc.fillText((int) (Player.worldX / 48) + " " + (int) (Player.worldY / 48), 500, 700);
        }
    }


    private void drawDroppedItems(GraphicsContext gc) {
        synchronized (WORLD_DROPS) {
            Iterator<DROP> iterator = WORLD_DROPS.iterator();
            while (iterator.hasNext()) {
                DROP drop = iterator.next();
                if (drop instanceof DRP_DroppedItem && drop.item == null) {
                    iterator.remove();
                } else {
                    drop.draw(gc);
                }
            }
        }
    }


    /**
     * Loads the game and updates loading screen
     *
     * @param gc gc
     */
    private void loadGame(GraphicsContext gc) {
        FonT.minecraftBold30 = Font.loadFont(FonT.class.getResourceAsStream("/Fonts/MinecraftBold-nMK1.otf"), 30);
        ui.updateLoadingScreen(0, gc);
        // 0 %
        inventP = new UI_InventoryPanel(this);
        wControl = new WorldController(this);
        //12 %
        ui.updateLoadingScreen(12, gc);
        wRender = new WorldRender(this);
        wControl.getWorldsData();
        wControl.makeOverWorldQuadrants();

        //24%
        ui.updateLoadingScreen(12, gc);
        miniM = new MiniMap(this);
        ent_control = new ENT_Control(this);
        collisionChecker = new CollisionChecker(this);
        //36%
        ui.updateLoadingScreen(12, gc);
        imageSto = new Storage();
        imageSto.loadImages();
        prj_control = new PRJ_Control(this);
        player = new Player(this);

        //48%
        ui.updateLoadingScreen(12, gc);
        sqLite = new SQLite(this);
        sqLite.readItemsFromDB();

        //60%
        ui.updateLoadingScreen(12, gc);
        ENTPlayer2 = new ENT_Player2(this);
        map_utils = new MAP_UTILS(this);
        SecureRandom secureRandom = new SecureRandom();
        long seed = secureRandom.nextLong();
        random = new Random(seed);

        //72%
        ui.updateLoadingScreen(12, gc);
        pathF = new PathFinder(this);
        pathF.instantiateNodes();
        Effects.loadEffects();

        //84%
        ui.updateLoadingScreen(12, gc);
        multiplayer = new Multiplayer(this, ENTPlayer2);
        talentP = new UI_TalentPanel(this);
        player.updateEquippedItems();
        player.health = player.maxHealth;
        player.mana = player.maxMana;
        npcControl = new NPC_Control(this);
        gameMap = new GameMap(this);
        FonT.loadFonts();
        sound = new Sound();
        //100%
        ui.updateLoadingScreen(16, gc);
        map_utils.loadSpawnLevel();
        countItems();
        gameMap.getImage();
        /*
        inventP.bag_Slots[14].item = DRP_DroppedItem.cloneItemWithLevelQuality(CHEST.get(8), 100, 60);
        inventP.bag_Slots[13].item = DRP_DroppedItem.cloneItemWithLevelQuality(CHEST.get(4), 100, 60);
        inventP.bag_Slots[11].item = DRP_DroppedItem.cloneItemWithLevelQuality(PANTS.get(3), 100, 60);
        inventP.bag_Slots[10].item = DRP_DroppedItem.cloneItemWithLevelQuality(BOOTS.get(4), 100, 60);

         */
        for (int i = 0; i < 1; i++) {
            ENTITIES.add(new ENT_Grunt(this, 87 * 48, 87 * 48, 100));
        }
        //ENTITIES.add(new ENT_Shooter(this, 35 * 48, 19 * 48, 111));
        Player.worldX = 86 * 48;
        Player.worldY = 98 * 48;
        loadingScreen = false;
        gameState = State.TITLE;
        startThreads();
        //sound.mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        //sound.mediaPlayer.play();
    }

    /**
     * Prints out the total item count across all categories
     */
    private void countItems() {
        System.out.println(-12 + MISC.size() + AMULET.size() + BOOTS.size() + CHEST.size() + HEAD.size() + OFFHAND.size() + ONEHAND.size() + PANTS.size() + RELICS.size() + RINGS.size() + TWOHANDS.size() + " total Items!");
    }

    /**
     * Filters the bigger Entities array to only have objects that are less than 2000 worldPixels away
     * Only used for the gameMap
     *
     * @see GameMap
     */
    private void proximitySorterENTITIES() {
        synchronized (ENTITIES) {
            PROXIMITY_ENTITIES.clear();
            for (ENTITY entity : ENTITIES) {
                if (Math.abs(entity.worldX - Player.worldX) + Math.abs(entity.worldY - Player.worldY) < 2_000) {
                    PROXIMITY_ENTITIES.add(entity);
                }
            }
        }
    }

    private void getPlayerTile() {
        playerX = (int) ((Player.worldX + 24) / 48);
        playerY = (int) ((Player.worldY + 24) / 48);
    }
}

/*





 */
