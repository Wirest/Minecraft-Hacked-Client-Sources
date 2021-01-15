package net.minecraft.item;

import com.google.common.base.Function;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.world.ColorizerGrass;

public class ItemDoublePlant extends ItemMultiTexture
{

    public ItemDoublePlant(Block block, Block block2, Function nameFunction)
    {
        super(block, block2, nameFunction);
    }

    @Override
	public int getColorFromItemStack(ItemStack stack, int renderPass)
    {
        BlockDoublePlant.EnumPlantType var3 = BlockDoublePlant.EnumPlantType.byMetadata(stack.getMetadata());
        return var3 != BlockDoublePlant.EnumPlantType.GRASS && var3 != BlockDoublePlant.EnumPlantType.FERN ? super.getColorFromItemStack(stack, renderPass) : ColorizerGrass.getGrassColor(0.5D, 1.0D);
    }
}
