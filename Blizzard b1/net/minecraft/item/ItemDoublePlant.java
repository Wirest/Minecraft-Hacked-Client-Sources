package net.minecraft.item;

import com.google.common.base.Function;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.world.ColorizerGrass;

public class ItemDoublePlant extends ItemMultiTexture
{
    private static final String __OBFID = "CL_00000021";

    public ItemDoublePlant(Block p_i45787_1_, Block p_i45787_2_, Function p_i45787_3_)
    {
        super(p_i45787_1_, p_i45787_2_, p_i45787_3_);
    }

    public int getColorFromItemStack(ItemStack stack, int renderPass)
    {
        BlockDoublePlant.EnumPlantType var3 = BlockDoublePlant.EnumPlantType.func_176938_a(stack.getMetadata());
        return var3 != BlockDoublePlant.EnumPlantType.GRASS && var3 != BlockDoublePlant.EnumPlantType.FERN ? super.getColorFromItemStack(stack, renderPass) : ColorizerGrass.getGrassColor(0.5D, 1.0D);
    }
}
