package net.minecraft.block;

import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockDoor extends Block
{
    public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
    public static final PropertyBool OPEN = PropertyBool.create("open");
    public static final PropertyEnum HINGE = PropertyEnum.create("hinge", BlockDoor.EnumHingePosition.class);
    public static final PropertyBool POWERED = PropertyBool.create("powered");
    public static final PropertyEnum HALF = PropertyEnum.create("half", BlockDoor.EnumDoorHalf.class);

    protected BlockDoor(Material materialIn)
    {
        super(materialIn);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(OPEN, Boolean.valueOf(false)).withProperty(HINGE, BlockDoor.EnumHingePosition.LEFT).withProperty(POWERED, Boolean.valueOf(false)).withProperty(HALF, BlockDoor.EnumDoorHalf.LOWER));
    }

    @Override
	public boolean isOpaqueCube()
    {
        return false;
    }

    @Override
	public boolean isPassable(IBlockAccess worldIn, BlockPos pos)
    {
        return isOpen(combineMetadata(worldIn, pos));
    }

    @Override
	public boolean isFullCube()
    {
        return false;
    }

    @Override
	public AxisAlignedBB getSelectedBoundingBox(World worldIn, BlockPos pos)
    {
        this.setBlockBoundsBasedOnState(worldIn, pos);
        return super.getSelectedBoundingBox(worldIn, pos);
    }

    @Override
	public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state)
    {
        this.setBlockBoundsBasedOnState(worldIn, pos);
        return super.getCollisionBoundingBox(worldIn, pos, state);
    }

    @Override
	public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos)
    {
        this.setBoundBasedOnMeta(combineMetadata(worldIn, pos));
    }

    private void setBoundBasedOnMeta(int combinedMeta)
    {
        float var2 = 0.1875F;
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 2.0F, 1.0F);
        EnumFacing var3 = getFacing(combinedMeta);
        boolean var4 = isOpen(combinedMeta);
        boolean var5 = isHingeLeft(combinedMeta);

        if (var4)
        {
            if (var3 == EnumFacing.EAST)
            {
                if (!var5)
                {
                    this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, var2);
                }
                else
                {
                    this.setBlockBounds(0.0F, 0.0F, 1.0F - var2, 1.0F, 1.0F, 1.0F);
                }
            }
            else if (var3 == EnumFacing.SOUTH)
            {
                if (!var5)
                {
                    this.setBlockBounds(1.0F - var2, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
                }
                else
                {
                    this.setBlockBounds(0.0F, 0.0F, 0.0F, var2, 1.0F, 1.0F);
                }
            }
            else if (var3 == EnumFacing.WEST)
            {
                if (!var5)
                {
                    this.setBlockBounds(0.0F, 0.0F, 1.0F - var2, 1.0F, 1.0F, 1.0F);
                }
                else
                {
                    this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, var2);
                }
            }
            else if (var3 == EnumFacing.NORTH)
            {
                if (!var5)
                {
                    this.setBlockBounds(0.0F, 0.0F, 0.0F, var2, 1.0F, 1.0F);
                }
                else
                {
                    this.setBlockBounds(1.0F - var2, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
                }
            }
        }
        else if (var3 == EnumFacing.EAST)
        {
            this.setBlockBounds(0.0F, 0.0F, 0.0F, var2, 1.0F, 1.0F);
        }
        else if (var3 == EnumFacing.SOUTH)
        {
            this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, var2);
        }
        else if (var3 == EnumFacing.WEST)
        {
            this.setBlockBounds(1.0F - var2, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        }
        else if (var3 == EnumFacing.NORTH)
        {
            this.setBlockBounds(0.0F, 0.0F, 1.0F - var2, 1.0F, 1.0F, 1.0F);
        }
    }

    @Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        if (this.blockMaterial == Material.iron)
        {
            return true;
        }
        else
        {
            BlockPos var9 = state.getValue(HALF) == BlockDoor.EnumDoorHalf.LOWER ? pos : pos.down();
            IBlockState var10 = pos.equals(var9) ? state : worldIn.getBlockState(var9);

            if (var10.getBlock() != this)
            {
                return false;
            }
            else
            {
                state = var10.cycleProperty(OPEN);
                worldIn.setBlockState(var9, state, 2);
                worldIn.markBlockRangeForRenderUpdate(var9, pos);
                worldIn.playAuxSFXAtEntity(playerIn, ((Boolean)state.getValue(OPEN)).booleanValue() ? 1003 : 1006, pos, 0);
                return true;
            }
        }
    }

    public void toggleDoor(World worldIn, BlockPos pos, boolean open)
    {
        IBlockState var4 = worldIn.getBlockState(pos);

        if (var4.getBlock() == this)
        {
            BlockPos var5 = var4.getValue(HALF) == BlockDoor.EnumDoorHalf.LOWER ? pos : pos.down();
            IBlockState var6 = pos == var5 ? var4 : worldIn.getBlockState(var5);

            if (var6.getBlock() == this && ((Boolean)var6.getValue(OPEN)).booleanValue() != open)
            {
                worldIn.setBlockState(var5, var6.withProperty(OPEN, Boolean.valueOf(open)), 2);
                worldIn.markBlockRangeForRenderUpdate(var5, pos);
                worldIn.playAuxSFXAtEntity((EntityPlayer)null, open ? 1003 : 1006, pos, 0);
            }
        }
    }

    /**
     * Called when a neighboring block changes.
     */
    @Override
	public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock)
    {
        if (state.getValue(HALF) == BlockDoor.EnumDoorHalf.UPPER)
        {
            BlockPos var5 = pos.down();
            IBlockState var6 = worldIn.getBlockState(var5);

            if (var6.getBlock() != this)
            {
                worldIn.setBlockToAir(pos);
            }
            else if (neighborBlock != this)
            {
                this.onNeighborBlockChange(worldIn, var5, var6, neighborBlock);
            }
        }
        else
        {
            boolean var9 = false;
            BlockPos var10 = pos.up();
            IBlockState var7 = worldIn.getBlockState(var10);

            if (var7.getBlock() != this)
            {
                worldIn.setBlockToAir(pos);
                var9 = true;
            }

            if (!World.doesBlockHaveSolidTopSurface(worldIn, pos.down()))
            {
                worldIn.setBlockToAir(pos);
                var9 = true;

                if (var7.getBlock() == this)
                {
                    worldIn.setBlockToAir(var10);
                }
            }

            if (var9)
            {
                if (!worldIn.isRemote)
                {
                    this.dropBlockAsItem(worldIn, pos, state, 0);
                }
            }
            else
            {
                boolean var8 = worldIn.isBlockPowered(pos) || worldIn.isBlockPowered(var10);

                if ((var8 || neighborBlock.canProvidePower()) && neighborBlock != this && var8 != ((Boolean)var7.getValue(POWERED)).booleanValue())
                {
                    worldIn.setBlockState(var10, var7.withProperty(POWERED, Boolean.valueOf(var8)), 2);

                    if (var8 != ((Boolean)state.getValue(OPEN)).booleanValue())
                    {
                        worldIn.setBlockState(pos, state.withProperty(OPEN, Boolean.valueOf(var8)), 2);
                        worldIn.markBlockRangeForRenderUpdate(pos, pos);
                        worldIn.playAuxSFXAtEntity((EntityPlayer)null, var8 ? 1003 : 1006, pos, 0);
                    }
                }
            }
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
        return state.getValue(HALF) == BlockDoor.EnumDoorHalf.UPPER ? null : this.getItem();
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
        this.setBlockBoundsBasedOnState(worldIn, pos);
        return super.collisionRayTrace(worldIn, pos, start, end);
    }

    @Override
	public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
    {
        return pos.getY() >= 255 ? false : World.doesBlockHaveSolidTopSurface(worldIn, pos.down()) && super.canPlaceBlockAt(worldIn, pos) && super.canPlaceBlockAt(worldIn, pos.up());
    }

    @Override
	public int getMobilityFlag()
    {
        return 1;
    }

    public static int combineMetadata(IBlockAccess worldIn, BlockPos pos)
    {
        IBlockState var2 = worldIn.getBlockState(pos);
        int var3 = var2.getBlock().getMetaFromState(var2);
        boolean var4 = isTop(var3);
        IBlockState var5 = worldIn.getBlockState(pos.down());
        int var6 = var5.getBlock().getMetaFromState(var5);
        int var7 = var4 ? var6 : var3;
        IBlockState var8 = worldIn.getBlockState(pos.up());
        int var9 = var8.getBlock().getMetaFromState(var8);
        int var10 = var4 ? var3 : var9;
        boolean var11 = (var10 & 1) != 0;
        boolean var12 = (var10 & 2) != 0;
        return removeHalfBit(var7) | (var4 ? 8 : 0) | (var11 ? 16 : 0) | (var12 ? 32 : 0);
    }

    @Override
	public Item getItem(World worldIn, BlockPos pos)
    {
        return this.getItem();
    }

    private Item getItem()
    {
        return this == Blocks.iron_door ? Items.iron_door : (this == Blocks.spruce_door ? Items.spruce_door : (this == Blocks.birch_door ? Items.birch_door : (this == Blocks.jungle_door ? Items.jungle_door : (this == Blocks.acacia_door ? Items.acacia_door : (this == Blocks.dark_oak_door ? Items.dark_oak_door : Items.oak_door)))));
    }

    @Override
	public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player)
    {
        BlockPos var5 = pos.down();

        if (player.capabilities.isCreativeMode && state.getValue(HALF) == BlockDoor.EnumDoorHalf.UPPER && worldIn.getBlockState(var5).getBlock() == this)
        {
            worldIn.setBlockToAir(var5);
        }
    }

    @Override
	public EnumWorldBlockLayer getBlockLayer()
    {
        return EnumWorldBlockLayer.CUTOUT;
    }

    /**
     * Get the actual Block state of this Block at the given position. This applies properties not visible in the
     * metadata, such as fence connections.
     */
    @Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
    {
        IBlockState var4;

        if (state.getValue(HALF) == BlockDoor.EnumDoorHalf.LOWER)
        {
            var4 = worldIn.getBlockState(pos.up());

            if (var4.getBlock() == this)
            {
                state = state.withProperty(HINGE, var4.getValue(HINGE)).withProperty(POWERED, var4.getValue(POWERED));
            }
        }
        else
        {
            var4 = worldIn.getBlockState(pos.down());

            if (var4.getBlock() == this)
            {
                state = state.withProperty(FACING, var4.getValue(FACING)).withProperty(OPEN, var4.getValue(OPEN));
            }
        }

        return state;
    }

    /**
     * Convert the given metadata into a BlockState for this Block
     */
    @Override
	public IBlockState getStateFromMeta(int meta)
    {
        return (meta & 8) > 0 ? this.getDefaultState().withProperty(HALF, BlockDoor.EnumDoorHalf.UPPER).withProperty(HINGE, (meta & 1) > 0 ? BlockDoor.EnumHingePosition.RIGHT : BlockDoor.EnumHingePosition.LEFT).withProperty(POWERED, Boolean.valueOf((meta & 2) > 0)) : this.getDefaultState().withProperty(HALF, BlockDoor.EnumDoorHalf.LOWER).withProperty(FACING, EnumFacing.getHorizontal(meta & 3).rotateYCCW()).withProperty(OPEN, Boolean.valueOf((meta & 4) > 0));
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    @Override
	public int getMetaFromState(IBlockState state)
    {
        byte var2 = 0;
        int var3;

        if (state.getValue(HALF) == BlockDoor.EnumDoorHalf.UPPER)
        {
            var3 = var2 | 8;

            if (state.getValue(HINGE) == BlockDoor.EnumHingePosition.RIGHT)
            {
                var3 |= 1;
            }

            if (((Boolean)state.getValue(POWERED)).booleanValue())
            {
                var3 |= 2;
            }
        }
        else
        {
            var3 = var2 | ((EnumFacing)state.getValue(FACING)).rotateY().getHorizontalIndex();

            if (((Boolean)state.getValue(OPEN)).booleanValue())
            {
                var3 |= 4;
            }
        }

        return var3;
    }

    protected static int removeHalfBit(int meta)
    {
        return meta & 7;
    }

    public static boolean isOpen(IBlockAccess worldIn, BlockPos pos)
    {
        return isOpen(combineMetadata(worldIn, pos));
    }

    public static EnumFacing getFacing(IBlockAccess worldIn, BlockPos pos)
    {
        return getFacing(combineMetadata(worldIn, pos));
    }

    public static EnumFacing getFacing(int combinedMeta)
    {
        return EnumFacing.getHorizontal(combinedMeta & 3).rotateYCCW();
    }

    protected static boolean isOpen(int combinedMeta)
    {
        return (combinedMeta & 4) != 0;
    }

    protected static boolean isTop(int meta)
    {
        return (meta & 8) != 0;
    }

    protected static boolean isHingeLeft(int combinedMeta)
    {
        return (combinedMeta & 16) != 0;
    }

    @Override
	protected BlockState createBlockState()
    {
        return new BlockState(this, new IProperty[] {HALF, FACING, OPEN, HINGE, POWERED});
    }

    public static enum EnumDoorHalf implements IStringSerializable
    {
        UPPER("UPPER", 0),
        LOWER("LOWER", 1); 

        private EnumDoorHalf(String p_i45726_1_, int p_i45726_2_) {}

        @Override
		public String toString()
        {
            return this.getName();
        }

        @Override
		public String getName()
        {
            return this == UPPER ? "upper" : "lower";
        }
    }

    public static enum EnumHingePosition implements IStringSerializable
    {
        LEFT("LEFT", 0),
        RIGHT("RIGHT", 1); 

        private EnumHingePosition(String p_i45725_1_, int p_i45725_2_) {}

        @Override
		public String toString()
        {
            return this.getName();
        }

        @Override
		public String getName()
        {
            return this == LEFT ? "left" : "right";
        }
    }
}
