package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class WorldGenMegaPineTree extends WorldGenHugeTrees
{
    private boolean field_150542_e;
    private static final String __OBFID = "CL_00000421";

    public WorldGenMegaPineTree(boolean p_i45457_1_, boolean p_i45457_2_)
    {
        super(p_i45457_1_, 13, 15, BlockPlanks.EnumType.SPRUCE.func_176839_a(), BlockPlanks.EnumType.SPRUCE.func_176839_a());
        this.field_150542_e = p_i45457_2_;
    }

    public boolean generate(World worldIn, Random p_180709_2_, BlockPos p_180709_3_)
    {
        int var4 = this.func_150533_a(p_180709_2_);

        if (!this.func_175929_a(worldIn, p_180709_2_, p_180709_3_, var4))
        {
            return false;
        }
        else
        {
            this.func_150541_c(worldIn, p_180709_3_.getX(), p_180709_3_.getZ(), p_180709_3_.getY() + var4, 0, p_180709_2_);

            for (int var5 = 0; var5 < var4; ++var5)
            {
                Block var6 = worldIn.getBlockState(p_180709_3_.offsetUp(var5)).getBlock();

                if (var6.getMaterial() == Material.air || var6.getMaterial() == Material.leaves)
                {
                    this.func_175905_a(worldIn, p_180709_3_.offsetUp(var5), Blocks.log, this.woodMetadata);
                }

                if (var5 < var4 - 1)
                {
                    var6 = worldIn.getBlockState(p_180709_3_.add(1, var5, 0)).getBlock();

                    if (var6.getMaterial() == Material.air || var6.getMaterial() == Material.leaves)
                    {
                        this.func_175905_a(worldIn, p_180709_3_.add(1, var5, 0), Blocks.log, this.woodMetadata);
                    }

                    var6 = worldIn.getBlockState(p_180709_3_.add(1, var5, 1)).getBlock();

                    if (var6.getMaterial() == Material.air || var6.getMaterial() == Material.leaves)
                    {
                        this.func_175905_a(worldIn, p_180709_3_.add(1, var5, 1), Blocks.log, this.woodMetadata);
                    }

                    var6 = worldIn.getBlockState(p_180709_3_.add(0, var5, 1)).getBlock();

                    if (var6.getMaterial() == Material.air || var6.getMaterial() == Material.leaves)
                    {
                        this.func_175905_a(worldIn, p_180709_3_.add(0, var5, 1), Blocks.log, this.woodMetadata);
                    }
                }
            }

            return true;
        }
    }

    private void func_150541_c(World worldIn, int p_150541_2_, int p_150541_3_, int p_150541_4_, int p_150541_5_, Random p_150541_6_)
    {
        int var7 = p_150541_6_.nextInt(5) + (this.field_150542_e ? this.baseHeight : 3);
        int var8 = 0;

        for (int var9 = p_150541_4_ - var7; var9 <= p_150541_4_; ++var9)
        {
            int var10 = p_150541_4_ - var9;
            int var11 = p_150541_5_ + MathHelper.floor_float((float)var10 / (float)var7 * 3.5F);
            this.func_175925_a(worldIn, new BlockPos(p_150541_2_, var9, p_150541_3_), var11 + (var10 > 0 && var11 == var8 && (var9 & 1) == 0 ? 1 : 0));
            var8 = var11;
        }
    }

    public void func_180711_a(World worldIn, Random p_180711_2_, BlockPos p_180711_3_)
    {
        this.func_175933_b(worldIn, p_180711_3_.offsetWest().offsetNorth());
        this.func_175933_b(worldIn, p_180711_3_.offsetEast(2).offsetNorth());
        this.func_175933_b(worldIn, p_180711_3_.offsetWest().offsetSouth(2));
        this.func_175933_b(worldIn, p_180711_3_.offsetEast(2).offsetSouth(2));

        for (int var4 = 0; var4 < 5; ++var4)
        {
            int var5 = p_180711_2_.nextInt(64);
            int var6 = var5 % 8;
            int var7 = var5 / 8;

            if (var6 == 0 || var6 == 7 || var7 == 0 || var7 == 7)
            {
                this.func_175933_b(worldIn, p_180711_3_.add(-3 + var6, 0, -3 + var7));
            }
        }
    }

    private void func_175933_b(World worldIn, BlockPos p_175933_2_)
    {
        for (int var3 = -2; var3 <= 2; ++var3)
        {
            for (int var4 = -2; var4 <= 2; ++var4)
            {
                if (Math.abs(var3) != 2 || Math.abs(var4) != 2)
                {
                    this.func_175934_c(worldIn, p_175933_2_.add(var3, 0, var4));
                }
            }
        }
    }

    private void func_175934_c(World worldIn, BlockPos p_175934_2_)
    {
        for (int var3 = 2; var3 >= -3; --var3)
        {
            BlockPos var4 = p_175934_2_.offsetUp(var3);
            Block var5 = worldIn.getBlockState(var4).getBlock();

            if (var5 == Blocks.grass || var5 == Blocks.dirt)
            {
                this.func_175905_a(worldIn, var4, Blocks.dirt, BlockDirt.DirtType.PODZOL.getMetadata());
                break;
            }

            if (var5.getMaterial() != Material.air && var3 < 0)
            {
                break;
            }
        }
    }
}
