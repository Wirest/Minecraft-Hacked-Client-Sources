/*
 * Decompiled with CFR 0_114.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.client.network.NetHandlerPlayClient
 *  net.minecraft.entity.player.InventoryPlayer
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemFood
 *  net.minecraft.item.ItemStack
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.C03PacketPlayer
 *  net.minecraft.network.play.client.C08PacketPlayerBlockPlacement
 *  net.minecraft.network.play.client.C09PacketHeldItemChange
 *  net.minecraft.util.FoodStats
 */
package me.aristhena.lucid.modules.player;

import me.aristhena.lucid.eventapi.Event;
import me.aristhena.lucid.eventapi.EventTarget;
import me.aristhena.lucid.eventapi.events.UpdateEvent;
import me.aristhena.lucid.management.module.Mod;
import me.aristhena.lucid.management.module.Module;
import me.aristhena.lucid.management.value.Val;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.util.FoodStats;

@Mod
public class AutoEat
extends Module {
    @Val(min=0.0, max=10.0, increment=0.5)
    private double hunger = 9.0;

    @EventTarget
    private void onUpdate(UpdateEvent event) {
        int foodSlot;
        if (event.state == Event.State.PRE && (foodSlot = this.getFoodSlotInHotbar()) != -1 && (double)this.mc.thePlayer.getFoodStats().getFoodLevel() < this.hunger * 2.0 && this.mc.thePlayer.isCollidedVertically) {
            this.mc.thePlayer.sendQueue.addToSendQueue((Packet)new C09PacketHeldItemChange(foodSlot));
            this.mc.thePlayer.sendQueue.addToSendQueue((Packet)new C08PacketPlayerBlockPlacement(this.mc.thePlayer.inventory.mainInventory[foodSlot]));
            int i = 0;
            while (i < 32) {
                this.mc.thePlayer.sendQueue.addToSendQueue((Packet)new C03PacketPlayer(false));
                ++i;
            }
            this.mc.thePlayer.stopUsingItem();
            this.mc.thePlayer.sendQueue.addToSendQueue((Packet)new C09PacketHeldItemChange(this.mc.thePlayer.inventory.currentItem));
        }
    }

    private int getFoodSlotInHotbar() {
        int i = 0;
        while (i < 9) {
            if (this.mc.thePlayer.inventory.mainInventory[i] != null && this.mc.thePlayer.inventory.mainInventory[i].getItem() != null && this.mc.thePlayer.inventory.mainInventory[i].getItem() instanceof ItemFood) {
                return i;
            }
            ++i;
        }
        return -1;
    }
}

