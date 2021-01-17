// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public interface ITileEntityProvider
{
    TileEntity createNewTileEntity(final World p0, final int p1);
}
