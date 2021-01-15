/*
 * Decompiled with CFR 0_114.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.client.multiplayer.PlayerControllerMP
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.entity.player.InventoryPlayer
 *  net.minecraft.inventory.Container
 *  net.minecraft.inventory.Slot
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemStack
 */
package me.aristhena.lucid.modules.misc;

import java.util.ArrayList;
import java.util.List;
import me.aristhena.lucid.eventapi.EventTarget;
import me.aristhena.lucid.eventapi.events.UpdateEvent;
import me.aristhena.lucid.management.module.Mod;
import me.aristhena.lucid.management.module.Module;
import me.aristhena.lucid.management.option.Op;
import me.aristhena.lucid.management.value.Val;
import me.aristhena.lucid.util.Timer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

@Mod
public class InventoryCleaner
extends Module {
    @Op
    private boolean cleanAll;
    @Val(min=0.0, max=1000.0, increment=100.0)
    private double delay = 50.0;
    private Timer timer = new Timer();
    public static List<Integer> cleanIDs = new ArrayList<Integer>();

    @EventTarget
    private void onPreUpdate(UpdateEvent e) {
        InventoryPlayer invp = this.mc.thePlayer.inventory;
        int i = 9;
        while (i < 45) {
            ItemStack itemStack = this.mc.thePlayer.inventoryContainer.getSlot(i).getStack();
            if (itemStack != null) {
                itemStack.getItem();
                if (this.shouldClean(Item.getIdFromItem((Item)itemStack.getItem())) && this.timer.delay((float)this.delay)) {
                    this.mc.playerController.windowClick(0, i, 0, 0, (EntityPlayer)this.mc.thePlayer);
                    this.mc.playerController.windowClick(0, -999, 0, 0, (EntityPlayer)this.mc.thePlayer);
                    this.timer.reset();
                }
            }
            ++i;
        }
    }

    private boolean shouldClean(int i) {
        if (this.cleanAll) {
            return true;
        }
        if (cleanIDs.contains(i)) {
            return true;
        }
        return false;
    }
}

