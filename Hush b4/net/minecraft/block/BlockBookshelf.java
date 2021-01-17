// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.block.state.IBlockState;
import java.util.Random;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.block.material.Material;

public class BlockBookshelf extends Block
{
    public BlockBookshelf() {
        super(Material.wood);
        this.setCreativeTab(CreativeTabs.tabBlock);
    }
    
    @Override
    public int quantityDropped(final Random random) {
        return 3;
    }
    
    @Override
    public Item getItemDropped(final IBlockState state, final Random rand, final int fortune) {
        return Items.book;
    }
}
