// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import net.minecraft.item.ItemStack;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.init.Items;
import net.minecraft.item.Item;

public class BlockPotato extends BlockCrops
{
    @Override
    protected Item getSeed() {
        return Items.potato;
    }
    
    @Override
    protected Item getCrop() {
        return Items.potato;
    }
    
    @Override
    public void dropBlockAsItemWithChance(final World worldIn, final BlockPos pos, final IBlockState state, final float chance, final int fortune) {
        super.dropBlockAsItemWithChance(worldIn, pos, state, chance, fortune);
        if (!worldIn.isRemote && state.getValue((IProperty<Integer>)BlockPotato.AGE) >= 7 && worldIn.rand.nextInt(50) == 0) {
            Block.spawnAsEntity(worldIn, pos, new ItemStack(Items.poisonous_potato));
        }
    }
}
