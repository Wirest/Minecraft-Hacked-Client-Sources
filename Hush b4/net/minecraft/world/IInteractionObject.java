// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world;

import net.minecraft.inventory.Container;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;

public interface IInteractionObject extends IWorldNameable
{
    Container createContainer(final InventoryPlayer p0, final EntityPlayer p1);
    
    String getGuiID();
}
