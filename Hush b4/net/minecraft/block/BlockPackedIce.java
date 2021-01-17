// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import java.util.Random;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.block.material.Material;

public class BlockPackedIce extends Block
{
    public BlockPackedIce() {
        super(Material.packedIce);
        this.slipperiness = 0.98f;
        this.setCreativeTab(CreativeTabs.tabBlock);
    }
    
    @Override
    public int quantityDropped(final Random random) {
        return 0;
    }
}
