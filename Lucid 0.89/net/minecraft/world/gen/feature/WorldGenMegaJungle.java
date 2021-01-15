package net.minecraft.world.gen.feature;

import java.util.Random;

import net.minecraft.block.BlockVine;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class WorldGenMegaJungle extends WorldGenHugeTrees
{

    public WorldGenMegaJungle(boolean p_i45456_1_, int p_i45456_2_, int p_i45456_3_, int p_i45456_4_, int p_i45456_5_)
    {
        super(p_i45456_1_, p_i45456_2_, p_i45456_3_, p_i45456_4_, p_i45456_5_);
    }

    @Override
	public boolean generate(World worldIn, Random rand, BlockPos position)
    {
        int var4 = this.func_150533_a(rand);

        if (!this.func_175929_a(worldIn, rand, position, var4))
        {
            return false;
        }
        else
        {
            this.func_175930_c(worldIn, position.up(var4), 2);

            for (int var5 = position.getY() + var4 - 2 - rand.nextInt(4); var5 > position.getY() + var4 / 2; var5 -= 2 + rand.nextInt(4))
            {
                float var6 = rand.nextFloat() * (float)Math.PI * 2.0F;
                int var7 = position.getX() + (int)(0.5F + MathHelper.cos(var6) * 4.0F);
                int var8 = position.getZ() + (int)(0.5F + MathHelper.sin(var6) * 4.0F);
                int var9;

                for (var9 = 0; var9 < 5; ++var9)
                {
                    var7 = position.getX() + (int)(1.5F + MathHelper.cos(var6) * var9);
                    var8 = position.getZ() + (int)(1.5F + MathHelper.sin(var6) * var9);
                    this.func_175905_a(worldIn, new BlockPos(var7, var5 - 3 + var9 / 2, var8), Blocks.log, this.woodMetadata);
                }

                var9 = 1 + rand.nextInt(2);
                int var10 = var5;

                for (int var11 = var5 - var9; var11 <= var10; ++var11)
                {
                    int var12 = var11 - var10;
                    this.func_175928_b(worldIn, new BlockPos(var7, var11, var8), 1 - var12);
                }
            }

            for (int var13 = 0; var13 < var4; ++var13)
            {
                BlockPos var14 = position.up(var13);

                if (this.func_175931_a(worldIn.getBlockState(var14).getBlock().getMaterial()))
                {
                    this.func_175905_a(worldIn, var14, Blocks.log, this.woodMetadata);

                    if (var13 > 0)
                    {
                        this.func_175932_b(worldIn, rand, var14.west(), BlockVine.EAST_FLAG);
                        this.func_175932_b(worldIn, rand, var14.north(), BlockVine.SOUTH_FLAG);
                    }
                }

                if (var13 < var4 - 1)
                {
                    BlockPos var15 = var14.east();

                    if (this.func_175931_a(worldIn.getBlockState(var15).getBlock().getMaterial()))
                    {
                        this.func_175905_a(worldIn, var15, Blocks.log, this.woodMetadata);

                        if (var13 > 0)
                        {
                            this.func_175932_b(worldIn, rand, var15.east(), BlockVine.WEST_FLAG);
                            this.func_175932_b(worldIn, rand, var15.north(), BlockVine.SOUTH_FLAG);
                        }
                    }

                    BlockPos var16 = var14.south().east();

                    if (this.func_175931_a(worldIn.getBlockState(var16).getBlock().getMaterial()))
                    {
                        this.func_175905_a(worldIn, var16, Blocks.log, this.woodMetadata);

                        if (var13 > 0)
                        {
                            this.func_175932_b(worldIn, rand, var16.east(), BlockVine.WEST_FLAG);
                            this.func_175932_b(worldIn, rand, var16.south(), BlockVine.NORTH_FLAG);
                        }
                    }

                    BlockPos var17 = var14.south();

                    if (this.func_175931_a(worldIn.getBlockState(var17).getBlock().getMaterial()))
                    {
                        this.func_175905_a(worldIn, var17, Blocks.log, this.woodMetadata);

                        if (var13 > 0)
                        {
                            this.func_175932_b(worldIn, rand, var17.west(), BlockVine.EAST_FLAG);
                            this.func_175932_b(worldIn, rand, var17.south(), BlockVine.NORTH_FLAG);
                        }
                    }
                }
            }

            return true;
        }
    }

    private boolean func_175931_a(Material p_175931_1_)
    {
        return p_175931_1_ == Material.air || p_175931_1_ == Material.leaves;
    }

    private void func_175932_b(World worldIn, Random p_175932_2_, BlockPos p_175932_3_, int p_175932_4_)
    {
        if (p_175932_2_.nextInt(3) > 0 && worldIn.isAirBlock(p_175932_3_))
        {
            this.func_175905_a(worldIn, p_175932_3_, Blocks.vine, p_175932_4_);
        }
    }

    private void func_175930_c(World worldIn, BlockPos p_175930_2_, int p_175930_3_)
    {
        byte var4 = 2;

        for (int var5 = -var4; var5 <= 0; ++var5)
        {
            this.func_175925_a(worldIn, p_175930_2_.up(var5), p_175930_3_ + 1 - var5);
        }
    }
}
