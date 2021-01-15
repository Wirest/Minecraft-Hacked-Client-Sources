package net.minecraft.block;

import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityPiston;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockPistonMoving extends BlockContainer
{
    public static final PropertyDirection FACING = BlockPistonExtension.FACING;
    public static final PropertyEnum TYPE = BlockPistonExtension.TYPE;

    public BlockPistonMoving()
    {
        super(Material.piston);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(TYPE, BlockPistonExtension.EnumPistonType.DEFAULT));
        this.setHardness(-1.0F);
    }

    /**
     * Returns a new instance of a block's tile entity class. Called on placing the block.
     */
    @Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
    {
        return null;
    }

    public static TileEntity newTileEntity(IBlockState state, EnumFacing facing, boolean extending, boolean renderHead)
    {
        return new TileEntityPiston(state, facing, extending, renderHead);
    }

    @Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
        TileEntity var4 = worldIn.getTileEntity(pos);

        if (var4 instanceof TileEntityPiston)
        {
            ((TileEntityPiston)var4).clearPistonTileEntity();
        }
        else
        {
            super.breakBlock(worldIn, pos, state);
        }
    }

    @Override
	public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
    {
        return false;
    }

    /**
     * Check whether this Block can be placed on the given side
     */
    @Override
	public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, EnumFacing side)
    {
        return false;
    }

    /**
     * Called when a player destroys this Block
     */
    @Override
	public void onBlockDestroyedByPlayer(World worldIn, BlockPos pos, IBlockState state)
    {
        BlockPos var4 = pos.offset(((EnumFacing)state.getValue(FACING)).getOpposite());
        IBlockState var5 = worldIn.getBlockState(var4);

        if (var5.getBlock() instanceof BlockPistonBase && ((Boolean)var5.getValue(BlockPistonBase.EXTENDED)).booleanValue())
        {
            worldIn.setBlockToAir(var4);
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

    @Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        if (!worldIn.isRemote && worldIn.getTileEntity(pos) == null)
        {
            worldIn.setBlockToAir(pos);
            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * Get the Item that this Block should drop when harvested.
     *  
     * @param fortune the level of the Fortune enchantment on the player's tool
     */
    @Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        return null;
    }

    /**
     * Spawns this Block's drops into the World as EntityItems.
     *  
     * @param chance The chance that each Item is actually spawned (1.0 = always, 0.0 = never)
     * @param fortune The player's fortune level
     */
    @Override
	public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune)
    {
        if (!worldIn.isRemote)
        {
            TileEntityPiston var6 = this.getTileEntity(worldIn, pos);

            if (var6 != null)
            {
                IBlockState var7 = var6.getPistonState();
                var7.getBlock().dropBlockAsItem(worldIn, pos, var7, 0);
            }
        }
    }

    /**
     * Ray traces through the blocks collision from start vector to end vector returning a ray trace hit.
     *  
     * @param start The start vector
     * @param end The end vector
     */
    @Override
	public MovingObjectPosition collisionRayTrace(World worldIn, BlockPos pos, Vec3 start, Vec3 end)
    {
        return null;
    }

    /**
     * Called when a neighboring block changes.
     */
    @Override
	public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock)
    {
        if (!worldIn.isRemote)
        {
            worldIn.getTileEntity(pos);
        }
    }

    @Override
	public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state)
    {
        TileEntityPiston var4 = this.getTileEntity(worldIn, pos);

        if (var4 == null)
        {
            return null;
        }
        else
        {
            float var5 = var4.func_145860_a(0.0F);

            if (var4.isExtending())
            {
                var5 = 1.0F - var5;
            }

            return this.getBoundingBox(worldIn, pos, var4.getPistonState(), var5, var4.getFacing());
        }
    }

    @Override
	public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos)
    {
        TileEntityPiston var3 = this.getTileEntity(worldIn, pos);

        if (var3 != null)
        {
            IBlockState var4 = var3.getPistonState();
            Block var5 = var4.getBlock();

            if (var5 == this || var5.getMaterial() == Material.air)
            {
                return;
            }

            float var6 = var3.func_145860_a(0.0F);

            if (var3.isExtending())
            {
                var6 = 1.0F - var6;
            }

            var5.setBlockBoundsBasedOnState(worldIn, pos);

            if (var5 == Blocks.piston || var5 == Blocks.sticky_piston)
            {
                var6 = 0.0F;
            }

            EnumFacing var7 = var3.getFacing();
            this.minX = var5.getBlockBoundsMinX() - var7.getFrontOffsetX() * var6;
            this.minY = var5.getBlockBoundsMinY() - var7.getFrontOffsetY() * var6;
            this.minZ = var5.getBlockBoundsMinZ() - var7.getFrontOffsetZ() * var6;
            this.maxX = var5.getBlockBoundsMaxX() - var7.getFrontOffsetX() * var6;
            this.maxY = var5.getBlockBoundsMaxY() - var7.getFrontOffsetY() * var6;
            this.maxZ = var5.getBlockBoundsMaxZ() - var7.getFrontOffsetZ() * var6;
        }
    }

    public AxisAlignedBB getBoundingBox(World worldIn, BlockPos pos, IBlockState extendingBlock, float progress, EnumFacing direction)
    {
        if (extendingBlock.getBlock() != this && extendingBlock.getBlock().getMaterial() != Material.air)
        {
            AxisAlignedBB var6 = extendingBlock.getBlock().getCollisionBoundingBox(worldIn, pos, extendingBlock);

            if (var6 == null)
            {
                return null;
            }
            else
            {
                double var7 = var6.minX;
                double var9 = var6.minY;
                double var11 = var6.minZ;
                double var13 = var6.maxX;
                double var15 = var6.maxY;
                double var17 = var6.maxZ;

                if (direction.getFrontOffsetX() < 0)
                {
                    var7 -= direction.getFrontOffsetX() * progress;
                }
                else
                {
                    var13 -= direction.getFrontOffsetX() * progress;
                }

                if (direction.getFrontOffsetY() < 0)
                {
                    var9 -= direction.getFrontOffsetY() * progress;
                }
                else
                {
                    var15 -= direction.getFrontOffsetY() * progress;
                }

                if (direction.getFrontOffsetZ() < 0)
                {
                    var11 -= direction.getFrontOffsetZ() * progress;
                }
                else
                {
                    var17 -= direction.getFrontOffsetZ() * progress;
                }

                return new AxisAlignedBB(var7, var9, var11, var13, var15, var17);
            }
        }
        else
        {
            return null;
        }
    }

    private TileEntityPiston getTileEntity(IBlockAccess worldIn, BlockPos pos)
    {
        TileEntity var3 = worldIn.getTileEntity(pos);
        return var3 instanceof TileEntityPiston ? (TileEntityPiston)var3 : null;
    }

    @Override
	public Item getItem(World worldIn, BlockPos pos)
    {
        return null;
    }

    /**
     * Convert the given metadata into a BlockState for this Block
     */
    @Override
	public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(FACING, BlockPistonExtension.getFacing(meta)).withProperty(TYPE, (meta & 8) > 0 ? BlockPistonExtension.EnumPistonType.STICKY : BlockPistonExtension.EnumPistonType.DEFAULT);
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    @Override
	public int getMetaFromState(IBlockState state)
    {
        byte var2 = 0;
        int var3 = var2 | ((EnumFacing)state.getValue(FACING)).getIndex();

        if (state.getValue(TYPE) == BlockPistonExtension.EnumPistonType.STICKY)
        {
            var3 |= 8;
        }

        return var3;
    }

    @Override
	protected BlockState createBlockState()
    {
        return new BlockState(this, new IProperty[] {FACING, TYPE});
    }
}
