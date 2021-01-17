// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.inventory;

import net.minecraft.util.IChatComponent;

public class AnimalChest extends InventoryBasic
{
    public AnimalChest(final String inventoryName, final int slotCount) {
        super(inventoryName, false, slotCount);
    }
    
    public AnimalChest(final IChatComponent invTitle, final int slotCount) {
        super(invTitle, slotCount);
    }
}
