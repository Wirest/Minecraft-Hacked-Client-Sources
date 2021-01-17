// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.item;

import net.minecraft.network.Packet;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class ItemMapBase extends Item
{
    @Override
    public boolean isMap() {
        return true;
    }
    
    public Packet createMapDataPacket(final ItemStack stack, final World worldIn, final EntityPlayer player) {
        return null;
    }
}
