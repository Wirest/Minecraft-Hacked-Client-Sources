package net.minecraft.block;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockTripWire extends Block
{
    public static final PropertyBool POWERED = PropertyBool.create("powered");
    public static final PropertyBool SUSPENDED = PropertyBool.create("suspended");
    public static final PropertyBool ATTACHED = PropertyBool.create("attached");
    public static final PropertyBool DISARMED = PropertyBool.create("disarmed");
    public static final PropertyBool NORTH = PropertyBool.create("north");
    public static final PropertyBool EAST = PropertyBool.create("east");
    public static final PropertyBool SOUTH = PropertyBool.create("south");
    public static final PropertyBool WEST = PropertyBool.create("west");

    public BlockTripWire()
    {
        super(Material.circuits);
        this.setDefaultState(this.blockState.getBaseState().withProperty(POWERED, Boolean.valueOf(false)).withProperty(SUSPENDED, Boolean.valueOf(false)).withProperty(ATTACHED, Boolean.valueOf(false)).withProperty(DISARMED, Boolean.valueOf(false)).withProperty(NORTH, Boolean.valueOf(false)).withProperty(EAST, Boolean.valueOf(false)).withProperty(SOUTH, Boolean.valueOf(false)).withProperty(WEST, Boolean.valueOf(false)));
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.15625F, 1.0F);
        this.setTickRandomly(true);
    }

    /**
     * Get the actual Block state of this Block at the given position. This applies properties not visible in the
     * metadata, such as fence connections.
     */
    @Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
    {
        return state.withProperty(NORTH, Boolean.valueOf(isConnectedTo(worldIn, pos, state, EnumFacing.NORTH))).withProperty(EAST, Boolean.valueOf(isConnectedTo(worldIn, pos, state, EnumFacing.EAST))).withProperty(SOUTH, Boolean.valueOf(isConnectedTo(worldIn, pos, state, EnumFacing.SOUTH))).withProperty(WEST, Boolean.valueOf(isConnectedTo(worldIn, pos, state, EnumFacing.WEST)));
    }

    @Override
	public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state)
    {
        return null;
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
	public EnumWorldBlockLayer getBlockLayer()
    {
        return EnumWorldBlockLayer.TRANSLUCENT;
    }

    /**
     * Get the Item that this Block should drop when harvested.
     *  
     * @param fortune the level of the Fortune enchantment on the player's tool
     */
    @Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        return Items.string;
    }

    @Override
	public Item getItem(World worldIn, BlockPos pos)
    {
        return Items.string;
    }

    /**
     * Called when a neighboring block changes.
     */
    @Override
	public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock)
    {
        boolean var5 = ((Boolean)state.getValue(SUSPENDED)).booleanValue();
        boolean var6 = !World.doesBlockHaveSolidTopSurface(worldIn, pos.down());

        if (var5 != var6)
        {
            this.dropBlockAsItem(worldIn, pos, state, 0);
            worldIn.setBlockToAir(pos);
        }
    }

    @Override
	public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos)
    {
        IBlockState var3 = worldIn.getBlockState(pos);
        boolean var4 = ((Boolean)var3.getValue(ATTACHED)).booleanValue();
        boolean var5 = ((Boolean)var3.getValue(SUSPENDED)).booleanValue();

        if (!var5)
        {
            this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.09375F, 1.0F);
        }
        else if (!var4)
        {
            this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.5F, 1.0F);
        }
        else
        {
            this.setBlockBounds(0.0F, 0.0625F, 0.0F, 1.0F, 0.15625F, 1.0F);
        }
    }

    @Override
	public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
    {
        state = state.withProperty(SUSPENDED, Boolean.valueOf(!World.doesBlockHaveSolidTopSurface(worldIn, pos.down())));
        worldIn.setBlockState(pos, state, 3);
        this.notifyHook(worldIn, pos, state);
    }

    @Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
        this.notifyHook(worldIn, pos, state.withProperty(POWERED, Boolean.valueOf(true)));
    }

    @Override
	public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player)
    {
        if (!worldIn.isRemote)
        {
            if (player.getCurrentEquippedItem() != null && player.getCurrentEquippedItem().getItem() == Items.shears)
            {
                worldIn.setBlockState(pos, state.withProperty(DISARMED, Boolean.valueOf(true)), 4);
            }
        }
    }

    private void notifyHook(World worldIn, BlockPos pos, IBlockState state)
    {
        EnumFacing[] var4 = new EnumFacing[] {EnumFacing.SOUTH, EnumFacing.WEST};
        int var5 = var4.length;
        int var6 = 0;

        while (var6 < var5)
        {
            EnumFacing var7 = var4[var6];
            int var8 = 1;

            while (true)
            {
                if (var8 < 42)
                {
                    BlockPos var9 = pos.offset(var7, var8);
                    IBlockState var10 = worldIn.getBlockState(var9);

                    if (var10.getBlock() == Blocks.tripwire_hook)
                    {
                        if (var10.getValue(BlockTripWireHook.FACING) == var7.getOpposite())
                        {
                            Blocks.tripwire_hook.func_176260_a(worldIn, var9, var10, false, true, var8, state);
                        }
                    }
                    else if (var10.getBlock() == Blocks.tripwire)
                    {
                        ++var8;
                        continue;
                    }
                }

                ++var6;
                break;
            }
        }
    }

    /**
     * Called When an Entity Collided with the Block
     */
    @Override
	public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn)
    {
        if (!worldIn.isRemote)
        {
            if (!((Boolean)state.getValue(POWERED)).booleanValue())
            {
                this.updateState(worldIn, pos);
            }
        }
    }

    /**
     * Called randomly when setTickRandomly is set to true (used by e.g. crops to grow, etc.)
     */
    @Override
	public void randomTick(World worldIn, BlockPos pos, IBlockState state, Random random) {}

    @Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {
        if (!worldIn.isRemote)
        {
            if (((Boolean)worldIn.getBlockState(pos).getValue(POWERED)).booleanValue())
            {
                this.updateState(worldIn, pos);
            }
        }
    }

    private void updateState(World worldIn, BlockPos pos)
    {
        IBlockState var3 = worldIn.getBlockState(pos);
        boolean var4 = ((Boolean)var3.getValue(POWERED)).booleanValue();
        boolean var5 = false;
        List var6 = worldIn.getEntitiesWithinAABBExcludingEntity((Entity)null, new AxisAlignedBB(pos.getX() + this.minX, pos.getY() + this.minY, pos.getZ() + this.minZ, pos.getX() + this.maxX, pos.getY() + this.maxY, pos.getZ() + this.maxZ));

        if (!var6.isEmpty())
        {
            Iterator var7 = var6.iterator();

            while (var7.hasNext())
            {
                Entity var8 = (Entity)var7.next();

                if (!var8.doesEntityNotTriggerPressurePlate())
                {
                    var5 = true;
                    break;
                }
            }
        }

        if (var5 != var4)
        {
            var3 = var3.withProperty(POWERED, Boolean.valueOf(var5));
            worldIn.setBlockState(pos, var3, 3);
            this.notifyHook(worldIn, pos, var3);
        }

        if (var5)
        {
            worldIn.scheduleUpdate(pos, this, this.tickRate(worldIn));
        }
    }

    public static boolean isConnectedTo(IBlockAccess worldIn, BlockPos pos, IBlockState state, EnumFacing direction)
    {
        BlockPos var4 = pos.offset(direction);
        IBlockState var5 = worldIn.getBlockState(var4);
        Block var6 = var5.getBlock();

        if (var6 == Blocks.tripwire_hook)
        {
            EnumFacing var9 = direction.getOpposite();
            return var5.getValue(BlockTripWireHook.FACING) == var9;
        }
        else if (var6 == Blocks.tripwire)
        {
            boolean var7 = ((Boolean)state.getValue(SUSPENDED)).booleanValue();
            boolean var8 = ((Boolean)var5.getValue(SUSPENDED)).booleanValue();
            return var7 == var8;
        }
        else
        {
            return false;
        }
    }

    /**
     * Convert the given metadata into a BlockState for this Block
     */
    @Override
	public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(POWERED, Boolean.valueOf((meta & 1) > 0)).withProperty(SUSPENDED, Boolean.valueOf((meta & 2) > 0)).withProperty(ATTACHED, Boolean.valueOf((meta & 4) > 0)).withProperty(DISARMED, Boolean.valueOf((meta & 8) > 0));
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    @Override
	public int getMetaFromState(IBlockState state)
    {
        int var2 = 0;

        if (((Boolean)state.getValue(POWERED)).booleanValue())
        {
            var2 |= 1;
        }

        if (((Boolean)state.getValue(SUSPENDED)).booleanValue())
        {
            var2 |= 2;
        }

        if (((Boolean)state.getValue(ATTACHED)).booleanValue())
        {
            var2 |= 4;
        }

        if (((Boolean)state.getValue(DISARMED)).booleanValue())
        {
            var2 |= 8;
        }

        return var2;
    }

    @Override
	protected BlockState createBlockState()
    {
        return new BlockState(this, new IProperty[] {POWERED, SUSPENDED, ATTACHED, DISARMED, NORTH, EAST, WEST, SOUTH});
    }
}
