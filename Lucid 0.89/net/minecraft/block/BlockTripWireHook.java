package net.minecraft.block;

import java.util.Iterator;
import java.util.Random;

import com.google.common.base.Objects;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockTripWireHook extends Block
{
    public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
    public static final PropertyBool POWERED = PropertyBool.create("powered");
    public static final PropertyBool ATTACHED = PropertyBool.create("attached");
    public static final PropertyBool SUSPENDED = PropertyBool.create("suspended");

    public BlockTripWireHook()
    {
        super(Material.circuits);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(POWERED, Boolean.valueOf(false)).withProperty(ATTACHED, Boolean.valueOf(false)).withProperty(SUSPENDED, Boolean.valueOf(false)));
        this.setCreativeTab(CreativeTabs.tabRedstone);
        this.setTickRandomly(true);
    }

    /**
     * Get the actual Block state of this Block at the given position. This applies properties not visible in the
     * metadata, such as fence connections.
     */
    @Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
    {
        return state.withProperty(SUSPENDED, Boolean.valueOf(!World.doesBlockHaveSolidTopSurface(worldIn, pos.down())));
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

    /**
     * Check whether this Block can be placed on the given side
     */
    @Override
	public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, EnumFacing side)
    {
        return side.getAxis().isHorizontal() && worldIn.getBlockState(pos.offset(side.getOpposite())).getBlock().isNormalCube();
    }

    @Override
	public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
    {
        Iterator var3 = EnumFacing.Plane.HORIZONTAL.iterator();
        EnumFacing var4;

        do
        {
            if (!var3.hasNext())
            {
                return false;
            }

            var4 = (EnumFacing)var3.next();
        }
        while (!worldIn.getBlockState(pos.offset(var4)).getBlock().isNormalCube());

        return true;
    }

    @Override
	public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {
        IBlockState var9 = this.getDefaultState().withProperty(POWERED, Boolean.valueOf(false)).withProperty(ATTACHED, Boolean.valueOf(false)).withProperty(SUSPENDED, Boolean.valueOf(false));

        if (facing.getAxis().isHorizontal())
        {
            var9 = var9.withProperty(FACING, facing);
        }

        return var9;
    }

    @Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
    {
        this.func_176260_a(worldIn, pos, state, false, false, -1, (IBlockState)null);
    }

    /**
     * Called when a neighboring block changes.
     */
    @Override
	public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock)
    {
        if (neighborBlock != this)
        {
            if (this.checkForDrop(worldIn, pos, state))
            {
                EnumFacing var5 = (EnumFacing)state.getValue(FACING);

                if (!worldIn.getBlockState(pos.offset(var5.getOpposite())).getBlock().isNormalCube())
                {
                    this.dropBlockAsItem(worldIn, pos, state, 0);
                    worldIn.setBlockToAir(pos);
                }
            }
        }
    }

    public void func_176260_a(World worldIn, BlockPos pos, IBlockState hookState, boolean p_176260_4_, boolean p_176260_5_, int p_176260_6_, IBlockState p_176260_7_)
    {
        EnumFacing var8 = (EnumFacing)hookState.getValue(FACING);
        boolean var9 = ((Boolean)hookState.getValue(ATTACHED)).booleanValue();
        boolean var10 = ((Boolean)hookState.getValue(POWERED)).booleanValue();
        boolean var11 = !World.doesBlockHaveSolidTopSurface(worldIn, pos.down());
        boolean var12 = !p_176260_4_;
        boolean var13 = false;
        int var14 = 0;
        IBlockState[] var15 = new IBlockState[42];
        BlockPos var17;

        for (int var16 = 1; var16 < 42; ++var16)
        {
            var17 = pos.offset(var8, var16);
            IBlockState var18 = worldIn.getBlockState(var17);

            if (var18.getBlock() == Blocks.tripwire_hook)
            {
                if (var18.getValue(FACING) == var8.getOpposite())
                {
                    var14 = var16;
                }

                break;
            }

            if (var18.getBlock() != Blocks.tripwire && var16 != p_176260_6_)
            {
                var15[var16] = null;
                var12 = false;
            }
            else
            {
                if (var16 == p_176260_6_)
                {
                    var18 = Objects.firstNonNull(p_176260_7_, var18);
                }

                boolean var19 = !((Boolean)var18.getValue(BlockTripWire.DISARMED)).booleanValue();
                boolean var20 = ((Boolean)var18.getValue(BlockTripWire.POWERED)).booleanValue();
                boolean var21 = ((Boolean)var18.getValue(BlockTripWire.SUSPENDED)).booleanValue();
                var12 &= var21 == var11;
                var13 |= var19 && var20;
                var15[var16] = var18;

                if (var16 == p_176260_6_)
                {
                    worldIn.scheduleUpdate(pos, this, this.tickRate(worldIn));
                    var12 &= var19;
                }
            }
        }

        var12 &= var14 > 1;
        var13 &= var12;
        IBlockState var22 = this.getDefaultState().withProperty(ATTACHED, Boolean.valueOf(var12)).withProperty(POWERED, Boolean.valueOf(var13));

        if (var14 > 0)
        {
            var17 = pos.offset(var8, var14);
            EnumFacing var24 = var8.getOpposite();
            worldIn.setBlockState(var17, var22.withProperty(FACING, var24), 3);
            this.func_176262_b(worldIn, var17, var24);
            this.func_180694_a(worldIn, var17, var12, var13, var9, var10);
        }

        this.func_180694_a(worldIn, pos, var12, var13, var9, var10);

        if (!p_176260_4_)
        {
            worldIn.setBlockState(pos, var22.withProperty(FACING, var8), 3);

            if (p_176260_5_)
            {
                this.func_176262_b(worldIn, pos, var8);
            }
        }

        if (var9 != var12)
        {
            for (int var23 = 1; var23 < var14; ++var23)
            {
                BlockPos var25 = pos.offset(var8, var23);
                IBlockState var26 = var15[var23];

                if (var26 != null && worldIn.getBlockState(var25).getBlock() != Blocks.air)
                {
                    worldIn.setBlockState(var25, var26.withProperty(ATTACHED, Boolean.valueOf(var12)), 3);
                }
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
        this.func_176260_a(worldIn, pos, state, false, true, -1, (IBlockState)null);
    }

    private void func_180694_a(World worldIn, BlockPos pos, boolean p_180694_3_, boolean p_180694_4_, boolean p_180694_5_, boolean p_180694_6_)
    {
        if (p_180694_4_ && !p_180694_6_)
        {
            worldIn.playSoundEffect(pos.getX() + 0.5D, pos.getY() + 0.1D, pos.getZ() + 0.5D, "random.click", 0.4F, 0.6F);
        }
        else if (!p_180694_4_ && p_180694_6_)
        {
            worldIn.playSoundEffect(pos.getX() + 0.5D, pos.getY() + 0.1D, pos.getZ() + 0.5D, "random.click", 0.4F, 0.5F);
        }
        else if (p_180694_3_ && !p_180694_5_)
        {
            worldIn.playSoundEffect(pos.getX() + 0.5D, pos.getY() + 0.1D, pos.getZ() + 0.5D, "random.click", 0.4F, 0.7F);
        }
        else if (!p_180694_3_ && p_180694_5_)
        {
            worldIn.playSoundEffect(pos.getX() + 0.5D, pos.getY() + 0.1D, pos.getZ() + 0.5D, "random.bowhit", 0.4F, 1.2F / (worldIn.rand.nextFloat() * 0.2F + 0.9F));
        }
    }

    private void func_176262_b(World worldIn, BlockPos p_176262_2_, EnumFacing p_176262_3_)
    {
        worldIn.notifyNeighborsOfStateChange(p_176262_2_, this);
        worldIn.notifyNeighborsOfStateChange(p_176262_2_.offset(p_176262_3_.getOpposite()), this);
    }

    private boolean checkForDrop(World worldIn, BlockPos pos, IBlockState state)
    {
        if (!this.canPlaceBlockAt(worldIn, pos))
        {
            this.dropBlockAsItem(worldIn, pos, state, 0);
            worldIn.setBlockToAir(pos);
            return false;
        }
        else
        {
            return true;
        }
    }

    @Override
	public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos)
    {
        float var3 = 0.1875F;

        switch (BlockTripWireHook.SwitchEnumFacing.FACING_LOOKUP[((EnumFacing)worldIn.getBlockState(pos).getValue(FACING)).ordinal()])
        {
            case 1:
                this.setBlockBounds(0.0F, 0.2F, 0.5F - var3, var3 * 2.0F, 0.8F, 0.5F + var3);
                break;

            case 2:
                this.setBlockBounds(1.0F - var3 * 2.0F, 0.2F, 0.5F - var3, 1.0F, 0.8F, 0.5F + var3);
                break;

            case 3:
                this.setBlockBounds(0.5F - var3, 0.2F, 0.0F, 0.5F + var3, 0.8F, var3 * 2.0F);
                break;

            case 4:
                this.setBlockBounds(0.5F - var3, 0.2F, 1.0F - var3 * 2.0F, 0.5F + var3, 0.8F, 1.0F);
        }
    }

    @Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
        boolean var4 = ((Boolean)state.getValue(ATTACHED)).booleanValue();
        boolean var5 = ((Boolean)state.getValue(POWERED)).booleanValue();

        if (var4 || var5)
        {
            this.func_176260_a(worldIn, pos, state, true, false, -1, (IBlockState)null);
        }

        if (var5)
        {
            worldIn.notifyNeighborsOfStateChange(pos, this);
            worldIn.notifyNeighborsOfStateChange(pos.offset(((EnumFacing)state.getValue(FACING)).getOpposite()), this);
        }

        super.breakBlock(worldIn, pos, state);
    }

    @Override
	public int isProvidingWeakPower(IBlockAccess worldIn, BlockPos pos, IBlockState state, EnumFacing side)
    {
        return ((Boolean)state.getValue(POWERED)).booleanValue() ? 15 : 0;
    }

    @Override
	public int isProvidingStrongPower(IBlockAccess worldIn, BlockPos pos, IBlockState state, EnumFacing side)
    {
        return !((Boolean)state.getValue(POWERED)).booleanValue() ? 0 : (state.getValue(FACING) == side ? 15 : 0);
    }

    /**
     * Can this block provide power. Only wire currently seems to have this change based on its state.
     */
    @Override
	public boolean canProvidePower()
    {
        return true;
    }

    @Override
	public EnumWorldBlockLayer getBlockLayer()
    {
        return EnumWorldBlockLayer.CUTOUT_MIPPED;
    }

    /**
     * Convert the given metadata into a BlockState for this Block
     */
    @Override
	public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(FACING, EnumFacing.getHorizontal(meta & 3)).withProperty(POWERED, Boolean.valueOf((meta & 8) > 0)).withProperty(ATTACHED, Boolean.valueOf((meta & 4) > 0));
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    @Override
	public int getMetaFromState(IBlockState state)
    {
        byte var2 = 0;
        int var3 = var2 | ((EnumFacing)state.getValue(FACING)).getHorizontalIndex();

        if (((Boolean)state.getValue(POWERED)).booleanValue())
        {
            var3 |= 8;
        }

        if (((Boolean)state.getValue(ATTACHED)).booleanValue())
        {
            var3 |= 4;
        }

        return var3;
    }

    @Override
	protected BlockState createBlockState()
    {
        return new BlockState(this, new IProperty[] {FACING, POWERED, ATTACHED, SUSPENDED});
    }

    static final class SwitchEnumFacing
    {
        static final int[] FACING_LOOKUP = new int[EnumFacing.values().length];

        static
        {
            try
            {
                FACING_LOOKUP[EnumFacing.EAST.ordinal()] = 1;
            }
            catch (NoSuchFieldError var4)
            {
                ;
            }

            try
            {
                FACING_LOOKUP[EnumFacing.WEST.ordinal()] = 2;
            }
            catch (NoSuchFieldError var3)
            {
                ;
            }

            try
            {
                FACING_LOOKUP[EnumFacing.SOUTH.ordinal()] = 3;
            }
            catch (NoSuchFieldError var2)
            {
                ;
            }

            try
            {
                FACING_LOOKUP[EnumFacing.NORTH.ordinal()] = 4;
            }
            catch (NoSuchFieldError var1)
            {
                ;
            }
        }
    }
}
