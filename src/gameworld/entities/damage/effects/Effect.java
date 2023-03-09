package gameworld.entities.damage.effects;

import gameworld.entities.ENTITY;
import gameworld.entities.damage.DamageType;
import gameworld.player.PROJECTILE;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import main.system.ui.Colors;

import java.util.Objects;

abstract public class Effect {
    protected DamageType type;
    public float amount;
    protected float amount_secondary;
    protected float amount_third;
    protected final boolean fromPlayer;
    protected float coolDownCoefficient;
    protected String name;
    protected Image icon;
    protected String description;
    public Class<? extends PROJECTILE> sourceProjectile;
    protected int ticker = 0;
    public EffectType effectType;
    protected int tickRate;// how many ticks needed to activate
    public float full_duration, rest_duration;

    protected Effect(float duration, float amount, boolean fromPlayer) {
        this.full_duration = duration;
        this.amount = amount;
        this.fromPlayer = fromPlayer;
        this.rest_duration = full_duration;
    }

    abstract public void tick(ENTITY entity);

    protected Image setupPicture(String name) {
        return new Image(Objects.requireNonNull(getClass().getResourceAsStream("/resources/ui/buffs/" + name + ".png")));
    }

    public void draw(GraphicsContext gc, int x, int y) {
        gc.drawImage(icon, x, y);
        drawCooldown(gc, x, y);
    }

    protected void drawCooldown(GraphicsContext gc, int skillBarX, int skillBarY) {
        int side1;
        int side2;
        int side3;
        int side4;
        int side5;
        if (rest_duration != full_duration) {
            coolDownCoefficient = (rest_duration * (128.0f / full_duration));
            side1 = 16;
            side2 = 0;
            side3 = 0;
            side4 = 0;
            side5 = 0;
        } else {
            coolDownCoefficient = 0;
            side1 = 100;
            side2 = 110;
            side3 = 110;
            side4 = 111;
            side5 = 110;
        }
        if (coolDownCoefficient > 0) {
            side1 = (int) (side1 + coolDownCoefficient);
        }
        if (coolDownCoefficient > 16) {
            side2 = (int) (side2 + (coolDownCoefficient - 16));
        }
        if (coolDownCoefficient > 48) {
            side3 = (int) (side3 + (coolDownCoefficient - 48));
        }
        if (coolDownCoefficient > 80) {
            side4 = (int) (side4 + (coolDownCoefficient - 80));
        }
        if (coolDownCoefficient > 112) {
            side5 = (int) (side5 + (coolDownCoefficient - 112));
        }
        gc.setStroke(Colors.LightGreyLessTransp);
        for (int i = side1; i <= 32; i++) {
            gc.strokeLine(skillBarX + 16, skillBarY + 16, skillBarX + i, skillBarY);
        }
        for (int i = side2; i <= 32; i++) {
            gc.strokeLine(skillBarX + 16, skillBarY + 16, skillBarX + 32, skillBarY + i);
        }
        for (int i = side3; i <= 32; i++) {
            gc.strokeLine(skillBarX + 16, skillBarY + 16, skillBarX + 32 - i, skillBarY + 32);
        }
        for (int i = side4; i <= 32; i++) {
            gc.strokeLine(skillBarX + 16, skillBarY + 16, skillBarX, skillBarY + 32 - i);
        }
        for (int i = side5; i <= 16; i++) {
            gc.strokeLine(skillBarX + 16, skillBarY + 16, skillBarX + i, skillBarY);
        }
    }

    protected void drawCooldownSmall(GraphicsContext gc, int skillBarX, int skillBarY) {
        int side1;
        int side2;
        int side3;
        int side4;
        int side5;
        if (rest_duration != full_duration) {
            coolDownCoefficient = (rest_duration * (128.0f / full_duration));
            side1 = 16;
            side2 = 0;
            side3 = 0;
            side4 = 0;
            side5 = 0;
        } else {
            coolDownCoefficient = 0;
            side1 = 100;
            side2 = 110;
            side3 = 110;
            side4 = 111;
            side5 = 110;
        }
        if (coolDownCoefficient > 0) {
            side1 = (int) (side1 + coolDownCoefficient);
        }
        if (coolDownCoefficient > 16) {
            side2 = (int) (side2 + (coolDownCoefficient - 16));
        }
        if (coolDownCoefficient > 48) {
            side3 = (int) (side3 + (coolDownCoefficient - 48));
        }
        if (coolDownCoefficient > 80) {
            side4 = (int) (side4 + (coolDownCoefficient - 80));
        }
        if (coolDownCoefficient > 112) {
            side5 = (int) (side5 + (coolDownCoefficient - 112));
        }
        gc.setStroke(Colors.LightGreyLessTransp);
        for (int i = side1; i <= 32; i++) {
            gc.strokeLine(skillBarX + 16, skillBarY + 16, skillBarX + i, skillBarY);
        }
        for (int i = side2; i <= 32; i++) {
            gc.strokeLine(skillBarX + 16, skillBarY + 16, skillBarX + 32, skillBarY + i);
        }
        for (int i = side3; i <= 32; i++) {
            gc.strokeLine(skillBarX + 16, skillBarY + 16, skillBarX + 32 - i, skillBarY + 32);
        }
        for (int i = side4; i <= 32; i++) {
            gc.strokeLine(skillBarX + 16, skillBarY + 16, skillBarX, skillBarY + 32 - i);
        }
        for (int i = side5; i <= 16; i++) {
            gc.strokeLine(skillBarX + 16, skillBarY + 16, skillBarX + i, skillBarY);
        }
    }
}
