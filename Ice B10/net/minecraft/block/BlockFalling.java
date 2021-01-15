package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class BlockFalling extends Block
{
    public static boolean fallInstantly;
    private static final String __OBFID = "CL_00000240";

    public BlockFalling()
    {
        super(Material.sand);
        this.setCreativeTab(CreativeTabs.tabBlock);
    }

    public BlockFalling(Material materialIn)
    {
        super(materialIn);
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
        if (!worldIn.isRemote)
        {
            this.checkFallable(worldIn, pos);
        }
    }

    private void checkFallable(World worldIn, BlockPos pos)
    {
        if (canFallInto(worldIn, pos.offsetDown()) && pos.getY() >= 0)
        {
            byte var3 = 32;

            if (!fallInstantly && worldIn.isAreaLoaded(pos.add(-var3, -var3, -var3), pos.add(var3, var3, var3)))
            {
                if (!worldIn.isRemote)
                {
                    EntityFallingBlock var5 = new EntityFallingBlock(worldIn, (double)pos.getX() + 0.5D, (double)pos.getY(), (double)pos.getZ() + 0.5D, worldIn.getBlockState(pos));
                    this.onStartFalling(var5);
                    worldIn.spawnEntityInWorld(var5);
                }
            }
            else
            {
                worldIn.setBlockToAir(pos);
                BlockPos var4;

                for (var4 = pos.offsetDown(); canFallInto(worldIn, var4) && var4.getY() > 0; var4 = var4.offsetDown())
                {
                    ;
                }

                if (var4.getY() > 0)
                {
                    worldIn.setBlockState(var4.offsetUp(), this.getDefaultState());
                }
            }
        }
    }

    protected void onStartFalling(EntityFallingBlock fallingEntity) {}

    /**
     * How many world ticks before ticking
     */
    public int tickRate(World worldIn)
    {
        return 2;
    }

    public static boolean canFallInto(World worldIn, BlockPos pos)
    {
        Block var2 = worldIn.getBlockState(pos).getBlock();
        Material var3 = var2.blockMaterial;
        return var2 == Blocks.fire || var3 == Material.air || var3 == Material.water || var3 == Material.lava;
    }

    public void onEndFalling(World worldIn, BlockPos pos) {}
}
