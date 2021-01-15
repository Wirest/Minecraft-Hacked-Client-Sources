// 
// Decompiled by Procyon v0.5.30
// 

package me.aristhena.client.module.modules.player;

import me.aristhena.event.EventTarget;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.player.EntityPlayer;
import me.aristhena.utils.ClientUtils;
import net.minecraft.client.gui.inventory.GuiChest;
import me.aristhena.event.Event;
import me.aristhena.event.events.UpdateEvent;
import me.aristhena.client.module.Module;
import me.aristhena.client.module.Module.Mod;
@Mod(displayName = "Chest Stealer")
public class ChestStealer extends Module
{
    @EventTarget
    private void onUpdate(final UpdateEvent event) {
        if (event.getState().equals(Event.State.PRE) && ClientUtils.mc().currentScreen instanceof GuiChest) {
            final GuiChest guiChest = (GuiChest)ClientUtils.mc().currentScreen;
            boolean full = true;
            ItemStack[] mainInventory;
            for (int length = (mainInventory = ClientUtils.player().inventory.mainInventory).length, i = 0; i < length; ++i) {
                final ItemStack item = mainInventory[i];
                if (item == null) {
                    full = false;
                    break;
                }
            }
            if (!full) {
                for (int index = 0; index < guiChest.lowerChestInventory.getSizeInventory(); ++index) {
                    final ItemStack stack = guiChest.lowerChestInventory.getStackInSlot(index);
                    if (stack != null) {
                        ClientUtils.playerController().windowClick(guiChest.inventorySlots.windowId, index, 0, 1, ClientUtils.player());
                        break;
                    }
                }
            }
        }
    }
}
