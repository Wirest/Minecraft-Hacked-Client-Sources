/*
 * Decompiled with CFR 0_114.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.client.multiplayer.PlayerControllerMP
 *  net.minecraft.client.network.NetHandlerPlayClient
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.entity.player.InventoryPlayer
 *  net.minecraft.inventory.Container
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemStack
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.C08PacketPlayerBlockPlacement
 *  net.minecraft.network.play.client.C09PacketHeldItemChange
 */
package me.aristhena.lucid.modules.combat;

import me.aristhena.lucid.eventapi.Event;
import me.aristhena.lucid.eventapi.EventTarget;
import me.aristhena.lucid.eventapi.events.UpdateEvent;
import me.aristhena.lucid.management.module.Mod;
import me.aristhena.lucid.management.module.Module;
import me.aristhena.lucid.management.value.Val;
import me.aristhena.lucid.util.Timer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;

@Mod
public class AutoSoup
extends Module {
    @Val(min=0.5, max=10.0, increment=0.5)
    private double health = 4.5;
    @Val(min=0.0, max=1000.0, increment=25.0)
    private double delay = 500.0;
    private Timer time = new Timer();

    @EventTarget
    private void onPostUpdate(UpdateEvent event) {
        if (event.state == Event.State.POST) {
            int soupSlot = this.getSoupFromInventory();
            if ((double)this.mc.thePlayer.getHealth() < this.health * 2.0 && this.time.delay((float)this.delay) && soupSlot != -1) {
                int prevSlot = this.mc.thePlayer.inventory.currentItem;
                if (soupSlot < 9) {
                    this.mc.thePlayer.sendQueue.addToSendQueue((Packet)new C09PacketHeldItemChange(soupSlot));
                    this.mc.thePlayer.sendQueue.addToSendQueue((Packet)new C08PacketPlayerBlockPlacement(this.mc.thePlayer.inventory.getCurrentItem()));
                    this.mc.thePlayer.sendQueue.addToSendQueue((Packet)new C09PacketHeldItemChange(prevSlot));
                    this.mc.playerController.syncCurrentPlayItem();
                    this.mc.thePlayer.inventory.currentItem = prevSlot;
                } else {
                    this.swap(soupSlot, this.mc.thePlayer.inventory.currentItem + (this.mc.thePlayer.inventory.currentItem < 8 ? 1 : -1));
                    this.mc.thePlayer.sendQueue.addToSendQueue((Packet)new C09PacketHeldItemChange(this.mc.thePlayer.inventory.currentItem + (this.mc.thePlayer.inventory.currentItem < 8 ? 1 : -1)));
                    this.mc.thePlayer.sendQueue.addToSendQueue((Packet)new C08PacketPlayerBlockPlacement(this.mc.thePlayer.inventory.getCurrentItem()));
                    this.mc.thePlayer.sendQueue.addToSendQueue((Packet)new C09PacketHeldItemChange(prevSlot));
                }
                this.time.reset();
            }
        }
    }

    protected void swap(int slot, int hotbarNum) {
        this.mc.playerController.windowClick(this.mc.thePlayer.inventoryContainer.windowId, slot, hotbarNum, 2, (EntityPlayer)this.mc.thePlayer);
    }

    private int getSoupFromInventory() {
        int i = 0;
        while (i < 36) {
            Item item;
            ItemStack is;
            if (this.mc.thePlayer.inventory.mainInventory[i] != null && Item.getIdFromItem((Item)(item = (is = this.mc.thePlayer.inventory.mainInventory[i]).getItem())) == 282) {
                return i;
            }
            ++i;
        }
        return -1;
    }
}

