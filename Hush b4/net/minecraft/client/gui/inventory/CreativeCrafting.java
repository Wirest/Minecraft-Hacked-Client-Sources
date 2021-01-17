// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.gui.inventory;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import java.util.List;
import net.minecraft.inventory.Container;
import net.minecraft.client.Minecraft;
import net.minecraft.inventory.ICrafting;

public class CreativeCrafting implements ICrafting
{
    private final Minecraft mc;
    
    public CreativeCrafting(final Minecraft mc) {
        this.mc = mc;
    }
    
    @Override
    public void updateCraftingInventory(final Container containerToSend, final List<ItemStack> itemsList) {
    }
    
    @Override
    public void sendSlotContents(final Container containerToSend, final int slotInd, final ItemStack stack) {
        this.mc.playerController.sendSlotPacket(stack, slotInd);
    }
    
    @Override
    public void sendProgressBarUpdate(final Container containerIn, final int varToUpdate, final int newValue) {
    }
    
    @Override
    public void func_175173_a(final Container p_175173_1_, final IInventory p_175173_2_) {
    }
}
