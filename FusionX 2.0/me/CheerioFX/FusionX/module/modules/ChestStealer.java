// 
// Decompiled by Procyon v0.5.30
// 

package me.CheerioFX.FusionX.module.modules;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.player.EntityPlayer;
import me.CheerioFX.FusionX.utils.Wrapper;
import net.minecraft.client.gui.inventory.GuiChest;
import me.CheerioFX.FusionX.events.EventPreMotionUpdates;
import me.CheerioFX.FusionX.module.Category;
import me.CheerioFX.FusionX.utils.TimerUtils;
import me.CheerioFX.FusionX.module.Module;

public class ChestStealer extends Module
{
    public Long delay;
    private final TimerUtils timer;
    public static boolean bypass;
    
    static {
        ChestStealer.bypass = false;
    }
    
    public ChestStealer() {
        super("ChestStealer", 37, Category.PLAYER);
        this.timer = new TimerUtils();
    }
    
    @Override
    public void onEnable() {
        if (ChestStealer.bypass) {
            this.delay = 50L;
        }
        else {
            this.delay = 1L;
        }
        super.onEnable();
    }
    
    @Override
    public void onDisable() {
        super.onDisable();
    }
    
    @Override
    public void onUpdate() {
        super.onUpdate();
    }
    
    @EventTarget
    public void onEventCalled(final EventPreMotionUpdates event) {
        if (Wrapper.mc.currentScreen instanceof GuiChest) {
            final GuiChest chest = (GuiChest)Wrapper.mc.currentScreen;
            if (this.isChestEmpty(chest) || this.isInventoryFull()) {
                Wrapper.mc.thePlayer.closeScreen();
            }
            for (int index = 0; index < chest.getLowerChestInventory().getSizeInventory(); ++index) {
                final ItemStack stack = chest.getLowerChestInventory().getStackInSlot(index);
                if (stack != null && this.timer.hasTimeElapsed(this.delay, false)) {
                    Wrapper.mc.playerController.windowClick(chest.inventorySlots.windowId, index, 0, 1, Wrapper.mc.thePlayer);
                    this.timer.reset();
                }
            }
        }
    }
    
    private boolean isChestEmpty(final GuiChest chest) {
        for (int index = 0; index <= chest.getLowerChestInventory().getSizeInventory(); ++index) {
            final ItemStack stack = chest.getLowerChestInventory().getStackInSlot(index);
            if (stack != null) {
                return false;
            }
        }
        return true;
    }
    
    public boolean isInventoryFull() {
        for (int index = 9; index <= 44; ++index) {
            final ItemStack stack = Wrapper.mc.thePlayer.inventoryContainer.getSlot(index).getStack();
            if (stack == null) {
                return false;
            }
        }
        return true;
    }
}
