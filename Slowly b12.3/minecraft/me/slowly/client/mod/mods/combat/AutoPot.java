/*
 * Decompiled with CFR 0_132 Helper by Lightcolour E-mail wyy-666@hotmail.com.
 */
package me.slowly.client.mod.mods.combat;

import com.darkmagician6.eventapi.EventTarget;
import me.slowly.client.events.EventPreMotion;
import me.slowly.client.mod.Mod;
import me.slowly.client.ui.notifiactions.ClientNotification;
import me.slowly.client.util.ClientUtil;
import me.slowly.client.util.Colors;
import me.slowly.client.util.TimeHelper;
import me.slowly.client.value.Value;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.util.BlockPos;

public class AutoPot
extends Mod {
    private Value<Double> delay = new Value<Double>("AutoPot_Delay", 50.0, 0.0, 1000.0, 10.0);
    private Value<Double> health = new Value<Double>("AutoPot_Health", 6.0, 0.5, 9.5, 0.5);
    private Value<Boolean> throwInInv = new Value<Boolean>("AutoPot_OnlyInv", true);
    private TimeHelper timer = new TimeHelper();
    private TimeHelper throwTimer = new TimeHelper();
    private boolean nextTick = false;

    public AutoPot() {
        super("AutoPot", Mod.Category.COMBAT, Colors.RED.c);
    }
    @Override
    public void onDisable() {
        super.onDisable();
        ClientUtil.sendClientMessage("AutoPot Disable", ClientNotification.Type.ERROR);
    }
    public void onEnable() {
    	super.isEnabled();
        ClientUtil.sendClientMessage("Autopot Enable", ClientNotification.Type.SUCCESS);
    }
    @EventTarget
    public void onPre(EventPreMotion event) {
        if ((double)(this.mc.thePlayer.getHealth() / 2.0f) <= this.health.getValueState()) {
            event.pitch = 90.0f;
            this.getPotion();
            this.nextTick = true;
        }
        if (this.nextTick) {
            event.pitch = 90.0f;
            this.throwPotion();
            this.nextTick = false;
        }
    }
    

    private void getPotion() {
        int slotId = this.getFreeSlot();
        if (slotId != -1) {
            int id = 9;
            while (id <= 35) {
                ItemStack currentItem;
                Slot currentSlot = this.mc.thePlayer.inventoryContainer.getSlot(id);
                if (currentSlot.getHasStack() && (currentItem = currentSlot.getStack()).getItem() instanceof ItemPotion && this.isSplashPotion(currentItem) && this.timer.isDelayComplete(this.delay.getValueState().intValue())) {
                    this.mc.playerController.windowClick(0, id, 0, 1, this.mc.thePlayer);
                    slotId = this.getFreeSlot();
                    this.timer.reset();
                }
                ++id;
            }
        }
    }

    private void throwPotion() {
        int slotId = this.getFreeSlot();
        if (slotId != -1) {
            int id = 36;
            while (id <= 44) {
                ItemStack currentItem;
                Slot currentSlot = this.mc.thePlayer.inventoryContainer.getSlot(id);
                if (currentSlot.getHasStack() && (currentItem = currentSlot.getStack()).getItem() instanceof ItemPotion && this.isSplashPotion(currentItem) && this.throwTimer.isDelayComplete(this.delay.getValueState().intValue())) {
                    int old = this.mc.thePlayer.inventory.currentItem;
                    this.mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(id - 36));
                    this.mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(new BlockPos(-1, -1, -1), -1, this.mc.thePlayer.inventoryContainer.getSlot(id).getStack(), 0.0f, 0.0f, 0.0f));
                    this.mc.thePlayer.inventory.currentItem = id - 36;
                    this.mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(this.mc.thePlayer.inventory.getCurrentItem()));
                    this.mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(old));
                    this.mc.thePlayer.inventory.currentItem = old;
                    this.throwTimer.reset();
                }
                ++id;
            }
        }
    }

    private boolean isSplashPotion(ItemStack itemStack) {
        return ItemPotion.isSplash(itemStack.getMetadata());
    }

    private int getFreeSlot() {
        int id = 36;
        while (id < 45) {
            Slot currentSlot = this.mc.thePlayer.inventoryContainer.getSlot(id);
            if (!currentSlot.getHasStack()) {
                return 1;
            }
            ++id;
        }
        return -1;
    }
}

