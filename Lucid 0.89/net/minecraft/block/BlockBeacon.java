package net.minecraft.block;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBeacon;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.util.HttpUtil;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;

public class BlockBeacon extends BlockContainer
{

    public BlockBeacon()
    {
        super(Material.glass);
        this.setHardness(3.0F);
        this.setCreativeTab(CreativeTabs.tabMisc);
    }

    /**
     * Returns a new instance of a block's tile entity class. Called on placing the block.
     */
    @Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
    {
        return new TileEntityBeacon();
    }

    @Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        if (worldIn.isRemote)
        {
            return true;
        }
        else
        {
            TileEntity var9 = worldIn.getTileEntity(pos);

            if (var9 instanceof TileEntityBeacon)
            {
                playerIn.displayGUIChest((TileEntityBeacon)var9);
            }

            return true;
        }
    }

    @Override
	public boolean isOpaqueCube()
    {
        return false;
    }

    @Override
	public boolean isFullCube()
    {
        return false;
    }

    /**
     * The type of render function that is called for this block
     */
    @Override
	public int getRenderType()
    {
        return 3;
    }

    @Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
    {
        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);

        if (stack.hasDisplayName())
        {
            TileEntity var6 = worldIn.getTileEntity(pos);

            if (var6 instanceof TileEntityBeacon)
            {
                ((TileEntityBeacon)var6).setName(stack.getDisplayName());
            }
        }
    }

    /**
     * Called when a neighboring block changes.
     */
    @Override
	public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock)
    {
        TileEntity var5 = worldIn.getTileEntity(pos);

        if (var5 instanceof TileEntityBeacon)
        {
            ((TileEntityBeacon)var5).func_174908_m();
            worldIn.addBlockEvent(pos, this, 1, 0);
        }
    }

    @Override
	public EnumWorldBlockLayer getBlockLayer()
    {
        return EnumWorldBlockLayer.CUTOUT;
    }

    public static void updateColorAsync(final World worldIn, final BlockPos glassPos)
    {
        HttpUtil.field_180193_a.submit(new Runnable()
        {
            @Override
			public void run()
            {
                Chunk var1 = worldIn.getChunkFromBlockCoords(glassPos);

                for (int var2 = glassPos.getY() - 1; var2 >= 0; --var2)
                {
                    final BlockPos var3 = new BlockPos(glassPos.getX(), var2, glassPos.getZ());

                    if (!var1.canSeeSky(var3))
                    {
                        break;
                    }

                    IBlockState var4 = worldIn.getBlockState(var3);

                    if (var4.getBlock() == Blocks.beacon)
                    {
                        ((WorldServer)worldIn).addScheduledTask(new Runnable()
                        {
                            @Override
							public void run()
                            {
                                TileEntity var1 = worldIn.getTileEntity(var3);

                                if (var1 instanceof TileEntityBeacon)
                                {
                                    ((TileEntityBeacon)var1).func_174908_m();
                                    worldIn.addBlockEvent(var3, Blocks.beacon, 1, 0);
                                }
                            }
                        });
                    }
                }
            }
        });
    }
}
