package net.minecraft.block;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockRedstoneWire extends Block
{
    public static final PropertyEnum NORTH = PropertyEnum.create("north", BlockRedstoneWire.EnumAttachPosition.class);
    public static final PropertyEnum EAST = PropertyEnum.create("east", BlockRedstoneWire.EnumAttachPosition.class);
    public static final PropertyEnum SOUTH = PropertyEnum.create("south", BlockRedstoneWire.EnumAttachPosition.class);
    public static final PropertyEnum WEST = PropertyEnum.create("west", BlockRedstoneWire.EnumAttachPosition.class);
    public static final PropertyInteger POWER = PropertyInteger.create("power", 0, 15);
    private boolean canProvidePower = true;

    /** List of blocks to update with redstone. */
    private final Set blocksNeedingUpdate = Sets.newHashSet();

    public BlockRedstoneWire()
    {
        super(Material.circuits);
        this.setDefaultState(this.blockState.getBaseState().withProperty(NORTH, BlockRedstoneWire.EnumAttachPosition.NONE).withProperty(EAST, BlockRedstoneWire.EnumAttachPosition.NONE).withProperty(SOUTH, BlockRedstoneWire.EnumAttachPosition.NONE).withProperty(WEST, BlockRedstoneWire.EnumAttachPosition.NONE).withProperty(POWER, Integer.valueOf(0)));
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.0625F, 1.0F);
    }

    /**
     * Get the actual Block state of this Block at the given position. This applies properties not visible in the
     * metadata, such as fence connections.
     */
    @Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
    {
        state = state.withProperty(WEST, this.getAttachPosition(worldIn, pos, EnumFacing.WEST));
        state = state.withProperty(EAST, this.getAttachPosition(worldIn, pos, EnumFacing.EAST));
        state = state.withProperty(NORTH, this.getAttachPosition(worldIn, pos, EnumFacing.NORTH));
        state = state.withProperty(SOUTH, this.getAttachPosition(worldIn, pos, EnumFacing.SOUTH));
        return state;
    }

    private BlockRedstoneWire.EnumAttachPosition getAttachPosition(IBlockAccess worldIn, BlockPos pos, EnumFacing direction)
    {
        BlockPos var4 = pos.offset(direction);
        Block var5 = worldIn.getBlockState(pos.offset(direction)).getBlock();

        if (!canConnectTo(worldIn.getBlockState(var4), direction) && (var5.isBlockNormalCube() || !canConnectUpwardsTo(worldIn.getBlockState(var4.down()))))
        {
            Block var6 = worldIn.getBlockState(pos.up()).getBlock();
            return !var6.isBlockNormalCube() && var5.isBlockNormalCube() && canConnectUpwardsTo(worldIn.getBlockState(var4.up())) ? BlockRedstoneWire.EnumAttachPosition.UP : BlockRedstoneWire.EnumAttachPosition.NONE;
        }
        else
        {
            return BlockRedstoneWire.EnumAttachPosition.SIDE;
        }
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
	public int colorMultiplier(IBlockAccess worldIn, BlockPos pos, int renderPass)
    {
        IBlockState var4 = worldIn.getBlockState(pos);
        return var4.getBlock() != this ? super.colorMultiplier(worldIn, pos, renderPass) : this.colorMultiplier(((Integer)var4.getValue(POWER)).intValue());
    }

    @Override
	public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
    {
        return World.doesBlockHaveSolidTopSurface(worldIn, pos.down()) || worldIn.getBlockState(pos.down()).getBlock() == Blocks.glowstone;
    }

    private IBlockState updateSurroundingRedstone(World worldIn, BlockPos pos, IBlockState state)
    {
        state = this.calculateCurrentChanges(worldIn, pos, pos, state);
        ArrayList var4 = Lists.newArrayList(this.blocksNeedingUpdate);
        this.blocksNeedingUpdate.clear();
        Iterator var5 = var4.iterator();

        while (var5.hasNext())
        {
            BlockPos var6 = (BlockPos)var5.next();
            worldIn.notifyNeighborsOfStateChange(var6, this);
        }

        return state;
    }

    private IBlockState calculateCurrentChanges(World worldIn, BlockPos pos1, BlockPos pos2, IBlockState state)
    {
        IBlockState var5 = state;
        int var6 = ((Integer)state.getValue(POWER)).intValue();
        byte var7 = 0;
        int var14 = this.getMaxCurrentStrength(worldIn, pos2, var7);
        this.canProvidePower = false;
        int var8 = worldIn.isBlockIndirectlyGettingPowered(pos1);
        this.canProvidePower = true;

        if (var8 > 0 && var8 > var14 - 1)
        {
            var14 = var8;
        }

        int var9 = 0;
        Iterator var10 = EnumFacing.Plane.HORIZONTAL.iterator();

        while (var10.hasNext())
        {
            EnumFacing var11 = (EnumFacing)var10.next();
            BlockPos var12 = pos1.offset(var11);
            boolean var13 = var12.getX() != pos2.getX() || var12.getZ() != pos2.getZ();

            if (var13)
            {
                var9 = this.getMaxCurrentStrength(worldIn, var12, var9);
            }

            if (worldIn.getBlockState(var12).getBlock().isNormalCube() && !worldIn.getBlockState(pos1.up()).getBlock().isNormalCube())
            {
                if (var13 && pos1.getY() >= pos2.getY())
                {
                    var9 = this.getMaxCurrentStrength(worldIn, var12.up(), var9);
                }
            }
            else if (!worldIn.getBlockState(var12).getBlock().isNormalCube() && var13 && pos1.getY() <= pos2.getY())
            {
                var9 = this.getMaxCurrentStrength(worldIn, var12.down(), var9);
            }
        }

        if (var9 > var14)
        {
            var14 = var9 - 1;
        }
        else if (var14 > 0)
        {
            --var14;
        }
        else
        {
            var14 = 0;
        }

        if (var8 > var14 - 1)
        {
            var14 = var8;
        }

        if (var6 != var14)
        {
            state = state.withProperty(POWER, Integer.valueOf(var14));

            if (worldIn.getBlockState(pos1) == var5)
            {
                worldIn.setBlockState(pos1, state, 2);
            }

            this.blocksNeedingUpdate.add(pos1);
            EnumFacing[] var15 = EnumFacing.values();
            int var16 = var15.length;

            for (int var17 = 0; var17 < var16; ++var17)
            {
                EnumFacing var18 = var15[var17];
                this.blocksNeedingUpdate.add(pos1.offset(var18));
            }
        }

        return state;
    }

    /**
     * Calls World.notifyNeighborsOfStateChange() for all neighboring blocks, but only if the given block is a redstone
     * wire.
     */
    private void notifyWireNeighborsOfStateChange(World worldIn, BlockPos pos)
    {
        if (worldIn.getBlockState(pos).getBlock() == this)
        {
            worldIn.notifyNeighborsOfStateChange(pos, this);
            EnumFacing[] var3 = EnumFacing.values();
            int var4 = var3.length;

            for (int var5 = 0; var5 < var4; ++var5)
            {
                EnumFacing var6 = var3[var5];
                worldIn.notifyNeighborsOfStateChange(pos.offset(var6), this);
            }
        }
    }

    @Override
	public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
    {
        if (!worldIn.isRemote)
        {
            this.updateSurroundingRedstone(worldIn, pos, state);
            Iterator var4 = EnumFacing.Plane.VERTICAL.iterator();
            EnumFacing var5;

            while (var4.hasNext())
            {
                var5 = (EnumFacing)var4.next();
                worldIn.notifyNeighborsOfStateChange(pos.offset(var5), this);
            }

            var4 = EnumFacing.Plane.HORIZONTAL.iterator();

            while (var4.hasNext())
            {
                var5 = (EnumFacing)var4.next();
                this.notifyWireNeighborsOfStateChange(worldIn, pos.offset(var5));
            }

            var4 = EnumFacing.Plane.HORIZONTAL.iterator();

            while (var4.hasNext())
            {
                var5 = (EnumFacing)var4.next();
                BlockPos var6 = pos.offset(var5);

                if (worldIn.getBlockState(var6).getBlock().isNormalCube())
                {
                    this.notifyWireNeighborsOfStateChange(worldIn, var6.up());
                }
                else
                {
                    this.notifyWireNeighborsOfStateChange(worldIn, var6.down());
                }
            }
        }
    }

    @Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
        super.breakBlock(worldIn, pos, state);

        if (!worldIn.isRemote)
        {
            EnumFacing[] var4 = EnumFacing.values();
            int var5 = var4.length;

            for (int var6 = 0; var6 < var5; ++var6)
            {
                EnumFacing var7 = var4[var6];
                worldIn.notifyNeighborsOfStateChange(pos.offset(var7), this);
            }

            this.updateSurroundingRedstone(worldIn, pos, state);
            Iterator var8 = EnumFacing.Plane.HORIZONTAL.iterator();
            EnumFacing var9;

            while (var8.hasNext())
            {
                var9 = (EnumFacing)var8.next();
                this.notifyWireNeighborsOfStateChange(worldIn, pos.offset(var9));
            }

            var8 = EnumFacing.Plane.HORIZONTAL.iterator();

            while (var8.hasNext())
            {
                var9 = (EnumFacing)var8.next();
                BlockPos var10 = pos.offset(var9);

                if (worldIn.getBlockState(var10).getBlock().isNormalCube())
                {
                    this.notifyWireNeighborsOfStateChange(worldIn, var10.up());
                }
                else
                {
                    this.notifyWireNeighborsOfStateChange(worldIn, var10.down());
                }
            }
        }
    }

    private int getMaxCurrentStrength(World worldIn, BlockPos pos, int strength)
    {
        if (worldIn.getBlockState(pos).getBlock() != this)
        {
            return strength;
        }
        else
        {
            int var4 = ((Integer)worldIn.getBlockState(pos).getValue(POWER)).intValue();
            return var4 > strength ? var4 : strength;
        }
    }

    /**
     * Called when a neighboring block changes.
     */
    @Override
	public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock)
    {
        if (!worldIn.isRemote)
        {
            if (this.canPlaceBlockAt(worldIn, pos))
            {
                this.updateSurroundingRedstone(worldIn, pos, state);
            }
            else
            {
                this.dropBlockAsItem(worldIn, pos, state, 0);
                worldIn.setBlockToAir(pos);
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
        return Items.redstone;
    }

    @Override
	public int isProvidingStrongPower(IBlockAccess worldIn, BlockPos pos, IBlockState state, EnumFacing side)
    {
        return !this.canProvidePower ? 0 : this.isProvidingWeakPower(worldIn, pos, state, side);
    }

    @Override
	public int isProvidingWeakPower(IBlockAccess worldIn, BlockPos pos, IBlockState state, EnumFacing side)
    {
        if (!this.canProvidePower)
        {
            return 0;
        }
        else
        {
            int var5 = ((Integer)state.getValue(POWER)).intValue();

            if (var5 == 0)
            {
                return 0;
            }
            else if (side == EnumFacing.UP)
            {
                return var5;
            }
            else
            {
                EnumSet var6 = EnumSet.noneOf(EnumFacing.class);
                Iterator var7 = EnumFacing.Plane.HORIZONTAL.iterator();

                while (var7.hasNext())
                {
                    EnumFacing var8 = (EnumFacing)var7.next();

                    if (this.func_176339_d(worldIn, pos, var8))
                    {
                        var6.add(var8);
                    }
                }

                if (side.getAxis().isHorizontal() && var6.isEmpty())
                {
                    return var5;
                }
                else if (var6.contains(side) && !var6.contains(side.rotateYCCW()) && !var6.contains(side.rotateY()))
                {
                    return var5;
                }
                else
                {
                    return 0;
                }
            }
        }
    }

    private boolean func_176339_d(IBlockAccess worldIn, BlockPos pos, EnumFacing side)
    {
        BlockPos var4 = pos.offset(side);
        IBlockState var5 = worldIn.getBlockState(var4);
        Block var6 = var5.getBlock();
        boolean var7 = var6.isNormalCube();
        boolean var8 = worldIn.getBlockState(pos.up()).getBlock().isNormalCube();
        return !var8 && var7 && canConnectUpwardsTo(worldIn, var4.up()) ? true : (canConnectTo(var5, side) ? true : (var6 == Blocks.powered_repeater && var5.getValue(BlockDirectional.FACING) == side ? true : !var7 && canConnectUpwardsTo(worldIn, var4.down())));
    }

    protected static boolean canConnectUpwardsTo(IBlockAccess worldIn, BlockPos pos)
    {
        return canConnectUpwardsTo(worldIn.getBlockState(pos));
    }

    protected static boolean canConnectUpwardsTo(IBlockState state)
    {
        return canConnectTo(state, (EnumFacing)null);
    }

    protected static boolean canConnectTo(IBlockState blockState, EnumFacing side)
    {
        Block var2 = blockState.getBlock();

        if (var2 == Blocks.redstone_wire)
        {
            return true;
        }
        else if (Blocks.unpowered_repeater.isAssociated(var2))
        {
            EnumFacing var3 = (EnumFacing)blockState.getValue(BlockDirectional.FACING);
            return var3 == side || var3.getOpposite() == side;
        }
        else
        {
            return var2.canProvidePower() && side != null;
        }
    }

    /**
     * Can this block provide power. Only wire currently seems to have this change based on its state.
     */
    @Override
	public boolean canProvidePower()
    {
        return this.canProvidePower;
    }

    private int colorMultiplier(int powerLevel)
    {
        float var2 = powerLevel / 15.0F;
        float var3 = var2 * 0.6F + 0.4F;

        if (powerLevel == 0)
        {
            var3 = 0.3F;
        }

        float var4 = var2 * var2 * 0.7F - 0.5F;
        float var5 = var2 * var2 * 0.6F - 0.7F;

        if (var4 < 0.0F)
        {
            var4 = 0.0F;
        }

        if (var5 < 0.0F)
        {
            var5 = 0.0F;
        }

        int var6 = MathHelper.clamp_int((int)(var3 * 255.0F), 0, 255);
        int var7 = MathHelper.clamp_int((int)(var4 * 255.0F), 0, 255);
        int var8 = MathHelper.clamp_int((int)(var5 * 255.0F), 0, 255);
        return -16777216 | var6 << 16 | var7 << 8 | var8;
    }

    @Override
	public void randomDisplayTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {
        int var5 = ((Integer)state.getValue(POWER)).intValue();

        if (var5 != 0)
        {
            double var6 = pos.getX() + 0.5D + (rand.nextFloat() - 0.5D) * 0.2D;
            double var8 = pos.getY() + 0.0625F;
            double var10 = pos.getZ() + 0.5D + (rand.nextFloat() - 0.5D) * 0.2D;
            float var12 = var5 / 15.0F;
            float var13 = var12 * 0.6F + 0.4F;
            float var14 = Math.max(0.0F, var12 * var12 * 0.7F - 0.5F);
            float var15 = Math.max(0.0F, var12 * var12 * 0.6F - 0.7F);
            worldIn.spawnParticle(EnumParticleTypes.REDSTONE, var6, var8, var10, var13, var14, var15, new int[0]);
        }
    }

    @Override
	public Item getItem(World worldIn, BlockPos pos)
    {
        return Items.redstone;
    }

    @Override
	public EnumWorldBlockLayer getBlockLayer()
    {
        return EnumWorldBlockLayer.CUTOUT;
    }

    /**
     * Convert the given metadata into a BlockState for this Block
     */
    @Override
	public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(POWER, Integer.valueOf(meta));
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    @Override
	public int getMetaFromState(IBlockState state)
    {
        return ((Integer)state.getValue(POWER)).intValue();
    }

    @Override
	protected BlockState createBlockState()
    {
        return new BlockState(this, new IProperty[] {NORTH, EAST, SOUTH, WEST, POWER});
    }

    static enum EnumAttachPosition implements IStringSerializable
    {
        UP("UP", 0, "up"),
        SIDE("SIDE", 1, "side"),
        NONE("NONE", 2, "none");
        private final String name; 

        private EnumAttachPosition(String p_i45689_1_, int p_i45689_2_, String name)
        {
            this.name = name;
        }

        @Override
		public String toString()
        {
            return this.getName();
        }

        @Override
		public String getName()
        {
            return this.name;
        }
    }
}
