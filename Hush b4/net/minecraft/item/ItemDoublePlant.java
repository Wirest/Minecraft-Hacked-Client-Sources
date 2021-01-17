// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.item;

import net.minecraft.world.ColorizerGrass;
import net.minecraft.block.BlockDoublePlant;
import com.google.common.base.Function;
import net.minecraft.block.Block;

public class ItemDoublePlant extends ItemMultiTexture
{
    public ItemDoublePlant(final Block block, final Block block2, final Function<ItemStack, String> nameFunction) {
        super(block, block2, nameFunction);
    }
    
    @Override
    public int getColorFromItemStack(final ItemStack stack, final int renderPass) {
        final BlockDoublePlant.EnumPlantType blockdoubleplant$enumplanttype = BlockDoublePlant.EnumPlantType.byMetadata(stack.getMetadata());
        return (blockdoubleplant$enumplanttype != BlockDoublePlant.EnumPlantType.GRASS && blockdoubleplant$enumplanttype != BlockDoublePlant.EnumPlantType.FERN) ? super.getColorFromItemStack(stack, renderPass) : ColorizerGrass.getGrassColor(0.5, 1.0);
    }
}
