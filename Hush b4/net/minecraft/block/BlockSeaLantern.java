// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import net.minecraft.block.material.MapColor;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.MathHelper;
import java.util.Random;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.block.material.Material;

public class BlockSeaLantern extends Block
{
    public BlockSeaLantern(final Material materialIn) {
        super(materialIn);
        this.setCreativeTab(CreativeTabs.tabBlock);
    }
    
    @Override
    public int quantityDropped(final Random random) {
        return 2 + random.nextInt(2);
    }
    
    @Override
    public int quantityDroppedWithBonus(final int fortune, final Random random) {
        return MathHelper.clamp_int(this.quantityDropped(random) + random.nextInt(fortune + 1), 1, 5);
    }
    
    @Override
    public Item getItemDropped(final IBlockState state, final Random rand, final int fortune) {
        return Items.prismarine_crystals;
    }
    
    @Override
    public MapColor getMapColor(final IBlockState state) {
        return MapColor.quartzColor;
    }
    
    @Override
    protected boolean canSilkHarvest() {
        return true;
    }
}
