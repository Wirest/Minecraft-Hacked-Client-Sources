package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class WorldGenIcePath extends WorldGenerator
{
    private Block field_150555_a;
    private int field_150554_b;
    private static final String __OBFID = "CL_00000416";

    public WorldGenIcePath(int p_i45454_1_)
    {
        this.field_150555_a = Blocks.packed_ice;
        this.field_150554_b = p_i45454_1_;
    }

    public boolean generate(World worldIn, Random p_180709_2_, BlockPos p_180709_3_)
    {
        while (worldIn.isAirBlock(p_180709_3_) && p_180709_3_.getY() > 2)
        {
            p_180709_3_ = p_180709_3_.offsetDown();
        }

        if (worldIn.getBlockState(p_180709_3_).getBlock() != Blocks.snow)
        {
            return false;
        }
        else
        {
            int var4 = p_180709_2_.nextInt(this.field_150554_b - 2) + 2;
            byte var5 = 1;

            for (int var6 = p_180709_3_.getX() - var4; var6 <= p_180709_3_.getX() + var4; ++var6)
            {
                for (int var7 = p_180709_3_.getZ() - var4; var7 <= p_180709_3_.getZ() + var4; ++var7)
                {
                    int var8 = var6 - p_180709_3_.getX();
                    int var9 = var7 - p_180709_3_.getZ();

                    if (var8 * var8 + var9 * var9 <= var4 * var4)
                    {
                        for (int var10 = p_180709_3_.getY() - var5; var10 <= p_180709_3_.getY() + var5; ++var10)
                        {
                            BlockPos var11 = new BlockPos(var6, var10, var7);
                            Block var12 = worldIn.getBlockState(var11).getBlock();

                            if (var12 == Blocks.dirt || var12 == Blocks.snow || var12 == Blocks.ice)
                            {
                                worldIn.setBlockState(var11, this.field_150555_a.getDefaultState(), 2);
                            }
                        }
                    }
                }
            }

            return true;
        }
    }
}
