package gameworld.entities.damage.effects;

import gameworld.entities.ENTITY;
import gameworld.entities.damage.DamageType;
import gameworld.player.Player;
import javafx.scene.image.Image;

abstract public class Effect {
    protected DamageType type;
    protected float amount;
    protected float amount_secondary;
    protected float amount_third;
    protected boolean fromPlayer;
    protected String name;
    protected Image icon;
    protected String description;
    protected int ticker = 0;
    protected int tickRate;// how many ticks needed to activate
    public float full_duration, rest_duration;

    Effect(float duration, float amount, boolean fromPlayer) {
        this.full_duration = duration;
        this.amount = amount;
        this.fromPlayer = fromPlayer;
        if (fromPlayer) {
            this.full_duration += (this.full_duration / 100.0f) * Player.effects[5];
            this.amount += (this.amount / 100.0f) * Player.effects[4];
        }
        this.rest_duration = full_duration;
    }

    abstract public void tick(ENTITY entity);
}