package me.rigamortis.faurax.module.modules.player;

import me.rigamortis.faurax.module.helpers.*;
import me.rigamortis.faurax.module.*;
import me.rigamortis.faurax.*;
import me.rigamortis.faurax.values.*;
import com.darkmagician6.eventapi.*;
import me.rigamortis.faurax.events.*;
import net.minecraft.entity.player.*;
import me.rigamortis.faurax.utils.*;
import net.minecraft.world.*;
import me.rigamortis.faurax.module.modules.movement.*;
import net.minecraft.network.play.client.*;
import net.minecraft.network.*;
import net.minecraft.potion.*;
import net.minecraft.item.*;

public class Regen extends Module implements CombatHelper, WorldHelper
{
    public int potDelay;
    private float oldYaw;
    private float oldPitch;
    public static boolean potting;
    public static Value mode;
    public static Value health;
    
    static {
        Regen.mode = new Value("Regen", String.class, "Mode", "AutoPot", new String[] { "AutoPot", "AutoSoup", "Vanilla", "NCP" });
        Regen.health = new Value("Regen", Float.TYPE, "Health", 4.0f, 0.5f, 20.0f);
    }
    
    public Regen() {
        this.setName("Regen");
        this.setKey("COMMA");
        this.setType(ModuleType.PLAYER);
        this.setColor(-15104089);
        this.setModInfo("");
        this.setVisible(true);
        Client.getValues();
        ValueManager.values.add(Regen.mode);
        Client.getValues();
        ValueManager.values.add(Regen.health);
    }
    
    @Override
    public void onDisabled() {
        super.onDisabled();
        Regen.potting = false;
    }
    
    @Override
    public void onEnabled() {
        super.onEnabled();
    }
    
    @EventTarget(4)
    public void postTick(final EventPostTick e) {
        if (this.isToggled() && Regen.mode.getSelectedOption().equalsIgnoreCase("AutoPot")) {
            Regen.mc.thePlayer.rotationPitch = this.oldPitch;
        }
    }
    
    @EventTarget(0)
    public void preTick(final EventPreTick e) {
        if (this.isToggled()) {
            this.oldPitch = Regen.mc.thePlayer.rotationPitch;
            if (Regen.mode.getSelectedOption().equalsIgnoreCase("AutoSoup")) {
                this.setModInfo(" §7Soup " + this.getSoupAmount());
                if (this.requiresHealth(Regen.mc.thePlayer) && Inventory.findHotbarItem(282, 1) != -1) {
                    final int lastSlot = Regen.mc.thePlayer.inventory.currentItem;
                    final int hotbarSlot = Inventory.findHotbarItem(282, 1);
                    Regen.mc.thePlayer.inventory.currentItem = hotbarSlot;
                    Regen.mc.playerController.sendUseItem(Regen.mc.thePlayer, Regen.mc.theWorld, Regen.mc.thePlayer.inventory.getCurrentItem());
                    Regen.mc.thePlayer.inventory.currentItem = lastSlot;
                }
                if (Inventory.findHotbarItem(281, 0) != -1) {
                    final int hotbarSlot2 = Inventory.findHotbarItem(281, 0);
                    if (Inventory.findAvailableSlotInventory(0, 281) != -1) {
                        Inventory.clickSlot(hotbarSlot2, 0, true);
                    }
                    else {
                        Inventory.clickSlot(hotbarSlot2, 0, false);
                        Inventory.clickSlot(9, 0, false);
                        Inventory.clickSlot(hotbarSlot2, 0, false);
                    }
                }
                if (Inventory.findEmptyHotbarItem(1) != -1 && Inventory.findInventoryItem(282) != -1) {
                    final int inventorySlot = Inventory.findInventoryItem(282);
                    Inventory.clickSlot(inventorySlot, 0, true);
                }
            }
            if (Regen.mode.getSelectedOption().equalsIgnoreCase("AutoPot")) {
                this.setModInfo(" §7Pot " + this.getPotsAmount());
                if (this.requiresHealth(Regen.mc.thePlayer)) {
                    Regen.potting = true;
                    if (Inventory.findPots(373, 1) != -1) {
                        final int lastSlot = Regen.mc.thePlayer.inventory.currentItem;
                        final int hotbarSlot = Inventory.findPots(373, 1);
                        Regen.mc.thePlayer.rotationPitch = 90.0f;
                        ++this.potDelay;
                        if (this.potDelay >= 6) {
                            Regen.mc.thePlayer.inventory.currentItem = hotbarSlot;
                            Regen.mc.playerController.sendUseItem(Regen.mc.thePlayer, Regen.mc.theWorld, Regen.mc.thePlayer.inventory.getCurrentItem());
                            Regen.mc.thePlayer.inventory.currentItem = lastSlot;
                            this.potDelay = 0;
                        }
                    }
                }
                else {
                    Regen.potting = false;
                }
                if (Inventory.findEmptyHotbarItem(1) != -1 && Inventory.findInventoryPots(373) != -1) {
                    final int inventorySlot = Inventory.findInventoryPots(373);
                    Inventory.clickSlot(inventorySlot, 0, true);
                }
            }
            if (Regen.mode.getSelectedOption().equalsIgnoreCase("Vanilla")) {
                if (Flight.enabled) {
                    return;
                }
                this.setModInfo(" §7Vanilla");
                if (this.requiresHealth(Regen.mc.thePlayer) && Regen.mc.thePlayer.onGround && Regen.mc.thePlayer.isCollidedVertically) {
                    for (int i = 0; i < 200; ++i) {
                        Regen.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Regen.mc.thePlayer.posX, Regen.mc.thePlayer.posY, Regen.mc.thePlayer.posZ, Regen.mc.thePlayer.onGround));
                    }
                }
            }
            if (Regen.mode.getSelectedOption().equalsIgnoreCase("NCP")) {
                if (Flight.enabled) {
                    return;
                }
                this.setModInfo(" §7NCP");
                if (this.requiresHealth(Regen.mc.thePlayer) && Regen.mc.thePlayer.onGround && Regen.mc.thePlayer.isCollidedVertically && Regen.mc.thePlayer.isPotionActive(Potion.regeneration.id) && !Regen.mc.thePlayer.isUsingItem()) {
                    for (int i = 0; i < 200; ++i) {
                        Regen.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Regen.mc.thePlayer.posX, Regen.mc.thePlayer.posY, Regen.mc.thePlayer.posZ, Regen.mc.thePlayer.onGround));
                    }
                }
            }
        }
    }
    
    private boolean requiresHealth(final EntityPlayer entity) {
        return entity.getHealth() <= Regen.health.getFloatValue();
    }
    
    public int getSoupAmount() {
        int amount = 0;
        for (int slot = 0; slot <= 44; ++slot) {
            final ItemStack item = Regen.mc.thePlayer.inventoryContainer.getSlot(slot).getStack();
            if (item != null && Item.getIdFromItem(item.getItem()) == 282) {
                ++amount;
            }
        }
        return amount;
    }
    
    public int getPotsAmount() {
        int amount = 0;
        for (int slot = 0; slot <= 44; ++slot) {
            final ItemStack item = Regen.mc.thePlayer.inventoryContainer.getSlot(slot).getStack();
            if (item != null && Item.getIdFromItem(item.getItem()) == 373 && Inventory.isStackSplashHealthPot(item)) {
                ++amount;
            }
        }
        return amount;
    }
}
