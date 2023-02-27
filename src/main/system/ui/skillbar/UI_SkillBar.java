package main.system.ui.skillbar;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import main.MainGame;
import main.system.ui.Colors;
import main.system.ui.FonT;
import main.system.ui.skillbar.skills.SKL_AutoShot;
import main.system.ui.skillbar.skills.SKL_EnergySphere;
import main.system.ui.skillbar.skills.SKL_Filler;
import main.system.ui.skillbar.skills.SKL_Lightning;
import main.system.ui.skillbar.skills.SKL_RingSalvo;
import main.system.ui.skillbar.skills.SKL_SelfRegen;
import main.system.ui.skillbar.skills.SKL_VoidField;

import java.awt.Rectangle;
import java.util.Objects;

public class UI_SkillBar {


    public final SKILL[] skills = new SKILL[8];
    private final int skillBarX = 651;
    private final int skillBarY = 1_005;


    private final Image skillSlot = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/resources/ui/skillbar/ui/slot.png")));

    private final Image character = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/resources/ui/skillbar/ui/character.png")));
    private final Image bag = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/resources/ui/skillbar/ui/bag.png")));
    private final Image skilltree = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/resources/ui/skillbar/ui/skilltree.png")));
    private final Image abilities = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/resources/ui/skillbar/ui/abilities.png")));
    private final Image map = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/resources/ui/skillbar/ui/map.png")));
    private final Image settings = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/resources/ui/skillbar/ui/settings.png")));

    public Rectangle wholeSkillBar = new Rectangle(651, 1000, 743, 85);
    public Rectangle characterBox = new Rectangle(skillBarX + 64 * 7 + 110, skillBarY - 6, 32, 32);
    public Rectangle bagBox = new Rectangle(skillBarX + 64 * 7 + 144, skillBarY - 6, 32, 32);
    public Rectangle skilltreeBox = new Rectangle(skillBarX + 64 * 7 + 178, skillBarY - 6, 32, 32);

    public Rectangle abilitiesBox = new Rectangle(skillBarX + 64 * 7 + 110, skillBarY + 28, 32, 32);
    public Rectangle mapBox = new Rectangle(skillBarX + 64 * 7 + 144, skillBarY + 28, 32, 32);
    public Rectangle settingsBox = new Rectangle(skillBarX + 64 * 7 + 178, skillBarY + 28, 32, 32);

    public UI_SkillBar(MainGame mg) {
        skills[0] = new SKL_RingSalvo(mg);
        skills[1] = new SKL_Lightning(mg);
        skills[2] = new SKL_SelfRegen(mg);
        skills[3] = new SKL_VoidField(mg);
        skills[4] = new SKL_Filler(mg);
        skills[5] = new SKL_AutoShot(mg);
        skills[6] = new SKL_EnergySphere(mg);
        skills[7] = new SKL_Filler(mg);
    }

    public void draw(GraphicsContext gc) {
        drawBackGround(gc);
        for (int i = 0; i < skills.length; i++) {
            if (skills[i] != null) {
                if (i > 6) {
                    skills[i].draw(gc, skillBarX + 12 + i * 64 + 20, skillBarY + 2);
                } else if (i > 4) {
                    skills[i].draw(gc, skillBarX + 12 + i * 64 + 5, skillBarY + 2);
                } else {
                    skills[i].draw(gc, skillBarX + 12 + i * 64, skillBarY + 2);
                }
            }
        }
    }

    private void drawBackGround(GraphicsContext gc) {
        gc.setFill(Colors.mediumVeryLight);
        gc.fillRoundRect(skillBarX, skillBarY - 10, 693, 85, 15, 15);
        gc.setFill(Colors.white);
        gc.drawImage(skillSlot, skillBarX + 5, skillBarY - 5);
        gc.drawImage(skillSlot, skillBarX + 64 + 5, skillBarY - 5);
        gc.drawImage(skillSlot, skillBarX + 64 * 2 + 5, skillBarY - 5);
        gc.drawImage(skillSlot, skillBarX + 64 * 3 + 5, skillBarY - 5);
        gc.drawImage(skillSlot, skillBarX + 64 * 4 + 5, skillBarY - 5);

        gc.drawImage(skillSlot, skillBarX + 64 * 5 + 10, skillBarY - 5);
        gc.drawImage(skillSlot, skillBarX + 64 * 6 + 10, skillBarY - 5);

        gc.drawImage(skillSlot, skillBarX + 64 * 7 + 20, skillBarY - 5);
        gc.setFont(FonT.minecraftBoldItalic15);

        gc.fillText("1", skillBarX + 30, skillBarY + 73);
        gc.fillText("2", skillBarX + 64 + 30, skillBarY + 73);
        gc.fillText("3", skillBarX + 64 * 2 + 30, skillBarY + 73);
        gc.fillText("4", skillBarX + 64 * 3 + 30, skillBarY + 73);
        gc.fillText("5", skillBarX + 64 * 4 + 30, skillBarY + 73);

        // gc.fillText("5",skillBarX + 5+25, skillBarY + 70);
        //gc.fillText("5",skillBarX + 5+25, skillBarY + 70);

        gc.fillText("Q", skillBarX + 64 * 7 + 30 + 20, skillBarY + 73);

        gc.drawImage(character, skillBarX + 64 * 7 + 110, skillBarY - 6);
        gc.drawImage(bag, skillBarX + 64 * 7 + 144, skillBarY - 6);
        gc.drawImage(skilltree, skillBarX + 64 * 7 + 178, skillBarY - 6);

        gc.drawImage(abilities, skillBarX + 64 * 7 + 110, skillBarY + 28);
        gc.drawImage(map, skillBarX + 64 * 7 + 144, skillBarY + 28);
        gc.drawImage(settings, skillBarX + 64 * 7 + 178, skillBarY + 28);
    }


    public void update() {
        for (SKILL skill : skills) {
            skill.update();
        }
    }
}
