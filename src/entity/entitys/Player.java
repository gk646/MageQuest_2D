package entity.entitys;

import main.Display;
import handlers.KeyHandler;
import entity.Entity;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class Player extends Entity {
    Display display;
    KeyHandler keyHandler;

    public Player(Display display, KeyHandler keyHandler) {
        //Handlers
        this.display = display;
        this.keyHandler = keyHandler;

        //Initialize default values
        setDefaultValues();
        getPlayerImage();

        //Collision
        this.solidArea = new Rectangle();
        solidArea.x = 8;
        solidArea.y = 16;
        solidArea.width= entityWidth- solidArea.x;
        solidArea.height = 32;
    }

    public void getPlayerImage() {
        try {
            up1 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/resources/player/boy_down_1.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setDefaultValues() {
        xPosition = 100;
        yPosition = 100;
        movementSpeed = 4;
        direction = "facingUp";
        this.entityHeight = 48;
        this.entityWidth = 48;
    }

    public void update() {
        if (keyHandler.upPressed) {
            yPosition -= movementSpeed;
        }
        if (keyHandler.downPressed) {
            yPosition += movementSpeed;
        }
        if (keyHandler.leftPressed) {
            xPosition -= movementSpeed;
        }
        if (keyHandler.rightPressed) {
            xPosition += movementSpeed;
        }
    }

    public void draw(Graphics2D g2) {
        BufferedImage playerSprite;
        switch (direction) {
            case "facingUp":
                playerSprite = up1;
            case default:
                playerSprite = up1;

        }
        g2.drawImage(playerSprite, xPosition, yPosition, 48, 48, null);

    }
    public Point getPlayerPosition(){
        return new Point(xPosition+entityWidth/4,yPosition+entityHeight/4);
    }

}
