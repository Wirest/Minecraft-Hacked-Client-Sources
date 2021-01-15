package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockDragonEgg extends Block
{
    private static final String __OBFID = "CL_00000232";

    public BlockDragonEgg()
    {
        super(Material.dragonEgg);
        this.setBlockBounds(0.0625F, 0.0F, 0.0625F, 0.9375F, 1.0F, 0.9375F);
    }

    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
    {
        worldIn.scheduleUpdate(pos, this, this.tickRate(worldIn));
    }

    public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock)
    {
        worldIn.scheduleUpdate(pos, this, this.tickRate(worldIn));
    }

    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {
        this.func_180683_d(worldIn, pos);
    }

    private void func_180683_d(World worldIn, BlockPos p_180683_2_)
    {
        if (BlockFalling.canFallInto(worldIn, p_180683_2_.offsetDown()) && p_180683_2_.getY() >= 0)
        {
            byte var3 = 32;

            if (!BlockFalling.fallInstantly && worldIn.isAreaLoaded(p_180683_2_.add(-var3, -var3, -var3), p_180683_2_.add(var3, var3, var3)))
            {
                worldIn.spawnEntityInWorld(new EntityFallingBlock(worldIn, (double)((float)p_180683_2_.getX() + 0.5F), (double)p_180683_2_.getY(), (double)((float)p_180683_2_.getZ() + 0.5F), this.getDefaultState()));
            }
            else
            {
                worldIn.setBlockToAir(p_180683_2_);
                BlockPos var4;

                for (var4 = p_180683_2_; BlockFalling.canFallInto(worldIn, var4) && var4.getY() > 0; var4 = var4.offsetDown())
                {
                    ;
                }

                if (var4.getY() > 0)
                {
                    worldIn.setBlockState(var4, this.getDefaultState(), 2);
                }
            }
        }
    }

    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        this.func_180684_e(worldIn, pos);
        return true;
    }

    public void onBlockClicked(World worldIn, BlockPos pos, EntityPlayer playerIn)
    {
        this.func_180684_e(worldIn, pos);
    }

    private void func_180684_e(World worldIn, BlockPos p_180684_2_)
    {
        IBlockState var3 = worldIn.getBlockState(p_180684_2_);

        if (var3.getBlock() == this)
        {
            for (int var4 = 0; var4 < 1000; ++var4)
            {
                BlockPos var5 = p_180684_2_.add(worldIn.rand.nextInt(16) - worldIn.rand.nextInt(16), worldIn.rand.nextInt(8) - worldIn.rand.nextInt(8), worldIn.rand.nextInt(16) - worldIn.rand.nextInt(16));

                if (worldIn.getBlockState(var5).getBlock().blockMaterial == Material.air)
                {
                    if (worldIn.isRemote)
                    {
                        for (int var6 = 0; var6 < 128; ++var6)
                        {
                            double var7 = worldIn.rand.nextDouble();
                            float var9 = (worldIn.rand.nextFloat() - 0.5F) * 0.2F;
                            float var10 = (worldIn.rand.nextFloat() - 0.5F) * 0.2F;
                            float var11 = (worldIn.rand.nextFloat() - 0.5F) * 0.2F;
                            double var12 = (double)var5.getX() + (double)(p_180684_2_.getX() - var5.getX()) * var7 + (worldIn.rand.nextDouble() - 0.5D) * 1.0D + 0.5D;
                            double var14 = (double)var5.getY() + (double)(p_180684_2_.getY() - var5.getY()) * var7 + worldIn.rand.nextDouble() * 1.0D - 0.5D;
                            double var16 = (double)var5.getZ() + (double)(p_180684_2_.getZ() - var5.getZ()) * var7 + (worldIn.rand.nextDouble() - 0.5D) * 1.0D + 0.5D;
                            worldIn.spawnParticle(EnumParticleTypes.PORTAL, var12, var14, var16, (double)var9, (double)var10, (double)var11, new int[0]);
                        }
                    }
                    else
                    {
                        worldIn.setBlockState(var5, var3, 2);
                        worldIn.setBlockToAir(p_180684_2_);
                    }

                    return;
                }
            }
        }
    }

    /**
     * How many world ticks before ticking
     */
    public int tickRate(World worldIn)
    {
        return 5;
    }

    public boolean isOpaqueCube()
    {
        return false;
    }

    public boolean isFullCube()
    {
        return false;
    }

    public boolean shouldSideBeRendered(IBlockAccess worldIn, BlockPos pos, EnumFacing side)
    {
        return true;
    }

    public Item getItem(World worldIn, BlockPos pos)
    {
        return null;
    }
}
