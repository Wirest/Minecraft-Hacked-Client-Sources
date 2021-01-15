package net.minecraft.world.pathfinder;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;

public class SwimNodeProcessor extends NodeProcessor
{
    private static final String __OBFID = "CL_00001966";

    public void func_176162_a(IBlockAccess p_176162_1_, Entity p_176162_2_)
    {
        super.func_176162_a(p_176162_1_, p_176162_2_);
    }

    public void func_176163_a()
    {
        super.func_176163_a();
    }

    public PathPoint func_176161_a(Entity p_176161_1_)
    {
        return this.func_176159_a(MathHelper.floor_double(p_176161_1_.getEntityBoundingBox().minX), MathHelper.floor_double(p_176161_1_.getEntityBoundingBox().minY + 0.5D), MathHelper.floor_double(p_176161_1_.getEntityBoundingBox().minZ));
    }

    public PathPoint func_176160_a(Entity p_176160_1_, double p_176160_2_, double p_176160_4_, double p_176160_6_)
    {
        return this.func_176159_a(MathHelper.floor_double(p_176160_2_ - (double)(p_176160_1_.width / 2.0F)), MathHelper.floor_double(p_176160_4_ + 0.5D), MathHelper.floor_double(p_176160_6_ - (double)(p_176160_1_.width / 2.0F)));
    }

    public int func_176164_a(PathPoint[] p_176164_1_, Entity p_176164_2_, PathPoint p_176164_3_, PathPoint p_176164_4_, float p_176164_5_)
    {
        int var6 = 0;
        EnumFacing[] var7 = EnumFacing.values();
        int var8 = var7.length;

        for (int var9 = 0; var9 < var8; ++var9)
        {
            EnumFacing var10 = var7[var9];
            PathPoint var11 = this.func_176185_a(p_176164_2_, p_176164_3_.xCoord + var10.getFrontOffsetX(), p_176164_3_.yCoord + var10.getFrontOffsetY(), p_176164_3_.zCoord + var10.getFrontOffsetZ());

            if (var11 != null && !var11.visited && var11.distanceTo(p_176164_4_) < p_176164_5_)
            {
                p_176164_1_[var6++] = var11;
            }
        }

        return var6;
    }

    private PathPoint func_176185_a(Entity p_176185_1_, int p_176185_2_, int p_176185_3_, int p_176185_4_)
    {
        int var5 = this.func_176186_b(p_176185_1_, p_176185_2_, p_176185_3_, p_176185_4_);
        return var5 == -1 ? this.func_176159_a(p_176185_2_, p_176185_3_, p_176185_4_) : null;
    }

    private int func_176186_b(Entity p_176186_1_, int p_176186_2_, int p_176186_3_, int p_176186_4_)
    {
        for (int var5 = p_176186_2_; var5 < p_176186_2_ + this.field_176168_c; ++var5)
        {
            for (int var6 = p_176186_3_; var6 < p_176186_3_ + this.field_176165_d; ++var6)
            {
                for (int var7 = p_176186_4_; var7 < p_176186_4_ + this.field_176166_e; ++var7)
                {
                    BlockPos var8 = new BlockPos(var5, var6, var7);
                    Block var9 = this.field_176169_a.getBlockState(var8).getBlock();

                    if (var9.getMaterial() != Material.water)
                    {
                        return 0;
                    }
                }
            }
        }

        return -1;
    }
}
