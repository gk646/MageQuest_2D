package gameworld.entities.npcs.trader;

import gameworld.entities.NPC;
import gameworld.world.objects.items.ITEM;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import main.system.ui.Colors;
import main.system.ui.FonT;
import main.system.ui.inventory.UI_InventorySlot;

import java.awt.Rectangle;
import java.util.ArrayList;

abstract public class MERCHANT extends NPC {
    protected Image tradeWindow = new Image(getClass().getResourceAsStream("/resources/ui/inventory/merchant_first.png"));
    protected Image buyback = new Image(getClass().getResourceAsStream("/resources/ui/inventory/merchant_second.png"));
    public boolean show_trade;
    public Rectangle firstWindow = new Rectangle(50, 50, 115, 27), secondWindow = new Rectangle(50, 50, 115, 27);
    public boolean show_buyback;
    protected int tradeWindowX, tradeWindowY;

    public ArrayList<UI_InventorySlot> buySlots = new ArrayList<>();
    public ArrayList<UI_InventorySlot> soldSlots = new ArrayList<>();

    public boolean sellItem(ITEM item) {
        addItemSold(item);
        mg.player.coins += item.level * item.rarity;
        mg.sound.playEffectSound(5);
        return true;
    }

    private void addItemSold(ITEM item) {
        soldSlots.remove(27);
        soldSlots.add(0, new UI_InventorySlot(item, 123, 123));
    }

    protected void drawMerchantWindow(GraphicsContext gc, int startX, int startY) {
        gc.drawImage(tradeWindow, startX - 50, startY - 50);
        gc.setFont(FonT.minecraftBold14);
        gc.setFill(Colors.darkBackground);
        gc.fillText("Merchant", startX - 20, startY - 35 + 408);
        gc.fillText("Buyback", startX + 105, startY - 35 + 408);
        firstWindow.x = startX - 30;
        firstWindow.y = startY - 50 + 408;
        secondWindow.x = startX - 50 + 139;
        secondWindow.y = startY - 50 + 408;
        int i = 0;
        int y;
        gc.setLineWidth(2);
        for (UI_InventorySlot slot : buySlots) {
            y = 50 * (i / 7);
            slot.boundBox.x = i % 7 * 50 + startX + 10;
            slot.boundBox.y = 60 + y + startY;
            gc.setFill(Colors.mediumVeryLight);
            gc.fillRoundRect(i % 7 * 50 + 10 + startX, 60 + y + startY, 45, 45, 20, 20);
            mg.inventP.setRarityColor(gc, slot);
            slot.drawSlot(gc, i % 7 * 50 + startX + 10, 60 + y + startY);
            if (slot.item != null && !slot.grabbed) {
                slot.drawIcon(gc, i % 7 * 50 + startX + 10, 60 + y + startY, 45);
            }
            i++;
        }
    }

    protected void drawBuyBack(GraphicsContext gc, int startX, int startY) {
        gc.drawImage(buyback, startX - 50, startY - 50);
        gc.setFont(FonT.minecraftBold14);
        gc.setFill(Colors.darkBackground);
        gc.fillText("Merchant", startX - 20, startY - 35 + 408);
        gc.fillText("Buyback", startX + 105, startY - 35 + 408);
        firstWindow.x = startX - 30;
        firstWindow.y = startY - 50 + 408;
        secondWindow.x = startX - 50 + 139;
        secondWindow.y = startY - 50 + 408;
        int i = 0;
        int y;
        gc.setLineWidth(2);
        for (UI_InventorySlot slot : soldSlots) {
            y = 50 * (i / 7);
            slot.boundBox.x = i % 7 * 50 + startX + 10;
            slot.boundBox.y = 60 + y + startY;
            gc.setFill(Colors.mediumVeryLight);
            gc.fillRoundRect(i % 7 * 50 + 10 + startX, 60 + y + startY, 45, 45, 20, 20);
            mg.inventP.setRarityColor(gc, slot);
            slot.drawSlot(gc, i % 7 * 50 + startX + 10, 60 + y + startY);
            if (slot.item != null && !slot.grabbed) {
                slot.drawIcon(gc, i % 7 * 50 + startX + 10, 60 + y + startY, 45);
            }
            i++;
        }
    }

    public boolean buyItem(ITEM item) {
        if (mg.player.coins >= item.level * 25 + item.rarity * 25) {
            for (UI_InventorySlot slot : mg.inventP.bag_Slots) {
                if (slot.item == null) {
                    slot.item = item;
                    mg.player.coins -= item.level * 25 + item.rarity * 25;
                    mg.sound.playEffectSound(7);
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    public boolean buyBackItem(ITEM item) {
        if (mg.player.coins >= item.level + item.rarity) {
            for (UI_InventorySlot slot : mg.inventP.bag_Slots) {
                if (slot.item == null) {
                    slot.item = item;
                    mg.player.coins -= item.level + item.rarity;
                    mg.sound.playEffectSound(7);
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    protected void drawMerchantTooltip(GraphicsContext gc) {
        if (show_trade) {
            for (UI_InventorySlot invSlot : buySlots) {
                if (invSlot.item != null && invSlot.toolTipTimer >= 30) {
                    mg.inventP.getTooltip(gc, invSlot, mg.inputH.lastMousePosition.x, mg.inputH.lastMousePosition.y);
                }
                if (invSlot.boundBox.contains(mg.inputH.lastMousePosition)) {
                    invSlot.toolTipTimer++;
                    break;
                } else {
                    invSlot.toolTipTimer = 0;
                }
            }
        } else if (show_buyback) {
            for (UI_InventorySlot invSlot : buySlots) {
                if (invSlot.item != null && invSlot.toolTipTimer >= 30) {
                    mg.inventP.getTooltip(gc, invSlot, mg.inputH.lastMousePosition.x, mg.inputH.lastMousePosition.y);
                }
                if (invSlot.boundBox.contains(mg.inputH.lastMousePosition)) {
                    invSlot.toolTipTimer++;
                    break;
                } else {
                    invSlot.toolTipTimer = 0;
                }
            }
        }
    }
}

