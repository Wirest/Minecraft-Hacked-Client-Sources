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
    public static final PropertyBool field_176293_a = PropertyBool.create("powered");
    public static final PropertyBool field_176290_b = PropertyBool.create("suspended");
    public static final PropertyBool field_176294_M = PropertyBool.create("attached");
    public static final PropertyBool field_176295_N = PropertyBool.create("disarmed");
    public static final PropertyBool field_176296_O = PropertyBool.create("north");
    public static final PropertyBool field_176291_P = PropertyBool.create("east");
    public static final PropertyBool field_176289_Q = PropertyBool.create("south");
    public static final PropertyBool field_176292_R = PropertyBool.create("west");
    private static final String __OBFID = "CL_00000328";

    public BlockTripWire()
    {
        super(Material.circuits);
        this.setDefaultState(this.blockState.getBaseState().withProperty(field_176293_a, Boolean.valueOf(false)).withProperty(field_176290_b, Boolean.valueOf(false)).withProperty(field_176294_M, Boolean.valueOf(false)).withProperty(field_176295_N, Boolean.valueOf(false)).withProperty(field_176296_O, Boolean.valueOf(false)).withProperty(field_176291_P, Boolean.valueOf(false)).withProperty(field_176289_Q, Boolean.valueOf(false)).withProperty(field_176292_R, Boolean.valueOf(false)));
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.15625F, 1.0F);
        this.setTickRandomly(true);
    }

    /**
     * Get the actual Block state of this Block at the given position. This applies properties not visible in the
     * metadata, such as fence connections.
     */
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
    {
        return state.withProperty(field_176296_O, Boolean.valueOf(func_176287_c(worldIn, pos, state, EnumFacing.NORTH))).withProperty(field_176291_P, Boolean.valueOf(func_176287_c(worldIn, pos, state, EnumFacing.EAST))).withProperty(field_176289_Q, Boolean.valueOf(func_176287_c(worldIn, pos, state, EnumFacing.SOUTH))).withProperty(field_176292_R, Boolean.valueOf(func_176287_c(worldIn, pos, state, EnumFacing.WEST)));
    }

    public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state)
    {
        return null;
    }

    public boolean isOpaqueCube()
    {
        return false;
    }

    public boolean isFullCube()
    {
        return false;
    }

    public EnumWorldBlockLayer getBlockLayer()
    {
        return EnumWorldBlockLayer.TRANSLUCENT;
    }

    /**
     * Get the Item that this Block should drop when harvested.
     *  
     * @param fortune the level of the Fortune enchantment on the player's tool
     */
    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        return Items.string;
    }

    public Item getItem(World worldIn, BlockPos pos)
    {
        return Items.string;
    }

    public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock)
    {
        boolean var5 = ((Boolean)state.getValue(field_176290_b)).booleanValue();
        boolean var6 = !World.doesBlockHaveSolidTopSurface(worldIn, pos.offsetDown());

        if (var5 != var6)
        {
            this.dropBlockAsItem(worldIn, pos, state, 0);
            worldIn.setBlockToAir(pos);
        }
    }

    public void setBlockBoundsBasedOnState(IBlockAccess access, BlockPos pos)
    {
        IBlockState var3 = access.getBlockState(pos);
        boolean var4 = ((Boolean)var3.getValue(field_176294_M)).booleanValue();
        boolean var5 = ((Boolean)var3.getValue(field_176290_b)).booleanValue();

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

    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
    {
        state = state.withProperty(field_176290_b, Boolean.valueOf(!World.doesBlockHaveSolidTopSurface(worldIn, pos.offsetDown())));
        worldIn.setBlockState(pos, state, 3);
        this.func_176286_e(worldIn, pos, state);
    }

    public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
        this.func_176286_e(worldIn, pos, state.withProperty(field_176293_a, Boolean.valueOf(true)));
    }

    public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn)
    {
        if (!worldIn.isRemote)
        {
            if (playerIn.getCurrentEquippedItem() != null && playerIn.getCurrentEquippedItem().getItem() == Items.shears)
            {
                worldIn.setBlockState(pos, state.withProperty(field_176295_N, Boolean.valueOf(true)), 4);
            }
        }
    }

    private void func_176286_e(World worldIn, BlockPos p_176286_2_, IBlockState p_176286_3_)
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
                    BlockPos var9 = p_176286_2_.offset(var7, var8);
                    IBlockState var10 = worldIn.getBlockState(var9);

                    if (var10.getBlock() == Blocks.tripwire_hook)
                    {
                        if (var10.getValue(BlockTripWireHook.field_176264_a) == var7.getOpposite())
                        {
                            Blocks.tripwire_hook.func_176260_a(worldIn, var9, var10, false, true, var8, p_176286_3_);
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
    public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn)
    {
        if (!worldIn.isRemote)
        {
            if (!((Boolean)state.getValue(field_176293_a)).booleanValue())
            {
                this.func_176288_d(worldIn, pos);
            }
        }
    }

    /**
     * Called randomly when setTickRandomly is set to true (used by e.g. crops to grow, etc.)
     */
    public void randomTick(World worldIn, BlockPos pos, IBlockState state, Random random) {}

    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {
        if (!worldIn.isRemote)
        {
            if (((Boolean)worldIn.getBlockState(pos).getValue(field_176293_a)).booleanValue())
            {
                this.func_176288_d(worldIn, pos);
            }
        }
    }

    private void func_176288_d(World worldIn, BlockPos p_176288_2_)
    {
        IBlockState var3 = worldIn.getBlockState(p_176288_2_);
        boolean var4 = ((Boolean)var3.getValue(field_176293_a)).booleanValue();
        boolean var5 = false;
        List var6 = worldIn.getEntitiesWithinAABBExcludingEntity((Entity)null, new AxisAlignedBB((double)p_176288_2_.getX() + this.minX, (double)p_176288_2_.getY() + this.minY, (double)p_176288_2_.getZ() + this.minZ, (double)p_176288_2_.getX() + this.maxX, (double)p_176288_2_.getY() + this.maxY, (double)p_176288_2_.getZ() + this.maxZ));

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
            var3 = var3.withProperty(field_176293_a, Boolean.valueOf(var5));
            worldIn.setBlockState(p_176288_2_, var3, 3);
            this.func_176286_e(worldIn, p_176288_2_, var3);
        }

        if (var5)
        {
            worldIn.scheduleUpdate(p_176288_2_, this, this.tickRate(worldIn));
        }
    }

    public static boolean func_176287_c(IBlockAccess p_176287_0_, BlockPos p_176287_1_, IBlockState p_176287_2_, EnumFacing p_176287_3_)
    {
        BlockPos var4 = p_176287_1_.offset(p_176287_3_);
        IBlockState var5 = p_176287_0_.getBlockState(var4);
        Block var6 = var5.getBlock();

        if (var6 == Blocks.tripwire_hook)
        {
            EnumFacing var9 = p_176287_3_.getOpposite();
            return var5.getValue(BlockTripWireHook.field_176264_a) == var9;
        }
        else if (var6 == Blocks.tripwire)
        {
            boolean var7 = ((Boolean)p_176287_2_.getValue(field_176290_b)).booleanValue();
            boolean var8 = ((Boolean)var5.getValue(field_176290_b)).booleanValue();
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
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(field_176293_a, Boolean.valueOf((meta & 1) > 0)).withProperty(field_176290_b, Boolean.valueOf((meta & 2) > 0)).withProperty(field_176294_M, Boolean.valueOf((meta & 4) > 0)).withProperty(field_176295_N, Boolean.valueOf((meta & 8) > 0));
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    public int getMetaFromState(IBlockState state)
    {
        int var2 = 0;

        if (((Boolean)state.getValue(field_176293_a)).booleanValue())
        {
            var2 |= 1;
        }

        if (((Boolean)state.getValue(field_176290_b)).booleanValue())
        {
            var2 |= 2;
        }

        if (((Boolean)state.getValue(field_176294_M)).booleanValue())
        {
            var2 |= 4;
        }

        if (((Boolean)state.getValue(field_176295_N)).booleanValue())
        {
            var2 |= 8;
        }

        return var2;
    }

    protected BlockState createBlockState()
    {
        return new BlockState(this, new IProperty[] {field_176293_a, field_176290_b, field_176294_M, field_176295_N, field_176296_O, field_176291_P, field_176292_R, field_176289_Q});
    }
}
