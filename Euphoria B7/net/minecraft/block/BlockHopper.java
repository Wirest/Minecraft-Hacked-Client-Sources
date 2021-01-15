package net.minecraft.block;

import com.google.common.base.Predicate;
import java.util.List;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockHopper extends BlockContainer
{
    public static final PropertyDirection field_176430_a = PropertyDirection.create("facing", new Predicate()
    {
        private static final String __OBFID = "CL_00002106";
        public boolean func_180180_a(EnumFacing p_180180_1_)
        {
            return p_180180_1_ != EnumFacing.UP;
        }
        public boolean apply(Object p_apply_1_)
        {
            return this.func_180180_a((EnumFacing)p_apply_1_);
        }
    });
    public static final PropertyBool field_176429_b = PropertyBool.create("enabled");
    private static final String __OBFID = "CL_00000257";

    public BlockHopper()
    {
        super(Material.iron);
        this.setDefaultState(this.blockState.getBaseState().withProperty(field_176430_a, EnumFacing.DOWN).withProperty(field_176429_b, Boolean.valueOf(true)));
        this.setCreativeTab(CreativeTabs.tabRedstone);
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    }

    public void setBlockBoundsBasedOnState(IBlockAccess access, BlockPos pos)
    {
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    }

    /**
     * Add all collision boxes of this Block to the list that intersect with the given mask.
     *  
     * @param collidingEntity the Entity colliding with this Block
     */
    public void addCollisionBoxesToList(World worldIn, BlockPos pos, IBlockState state, AxisAlignedBB mask, List list, Entity collidingEntity)
    {
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.625F, 1.0F);
        super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
        float var7 = 0.125F;
        this.setBlockBounds(0.0F, 0.0F, 0.0F, var7, 1.0F, 1.0F);
        super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, var7);
        super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
        this.setBlockBounds(1.0F - var7, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
        this.setBlockBounds(0.0F, 0.0F, 1.0F - var7, 1.0F, 1.0F, 1.0F);
        super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    }

    public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {
        EnumFacing var9 = facing.getOpposite();

        if (var9 == EnumFacing.UP)
        {
            var9 = EnumFacing.DOWN;
        }

        return this.getDefaultState().withProperty(field_176430_a, var9).withProperty(field_176429_b, Boolean.valueOf(true));
    }

    /**
     * Returns a new instance of a block's tile entity class. Called on placing the block.
     */
    public TileEntity createNewTileEntity(World worldIn, int meta)
    {
        return new TileEntityHopper();
    }

    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
    {
        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);

        if (stack.hasDisplayName())
        {
            TileEntity var6 = worldIn.getTileEntity(pos);

            if (var6 instanceof TileEntityHopper)
            {
                ((TileEntityHopper)var6).setCustomName(stack.getDisplayName());
            }
        }
    }

    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
    {
        this.func_176427_e(worldIn, pos, state);
    }

    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        if (worldIn.isRemote)
        {
            return true;
        }
        else
        {
            TileEntity var9 = worldIn.getTileEntity(pos);

            if (var9 instanceof TileEntityHopper)
            {
                playerIn.displayGUIChest((TileEntityHopper)var9);
            }

            return true;
        }
    }

    public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock)
    {
        this.func_176427_e(worldIn, pos, state);
    }

    private void func_176427_e(World worldIn, BlockPos p_176427_2_, IBlockState p_176427_3_)
    {
        boolean var4 = !worldIn.isBlockPowered(p_176427_2_);

        if (var4 != ((Boolean)p_176427_3_.getValue(field_176429_b)).booleanValue())
        {
            worldIn.setBlockState(p_176427_2_, p_176427_3_.withProperty(field_176429_b, Boolean.valueOf(var4)), 4);
        }
    }

    public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
        TileEntity var4 = worldIn.getTileEntity(pos);

        if (var4 instanceof TileEntityHopper)
        {
            InventoryHelper.dropInventoryItems(worldIn, pos, (TileEntityHopper)var4);
            worldIn.updateComparatorOutputLevel(pos, this);
        }

        super.breakBlock(worldIn, pos, state);
    }

    /**
     * The type of render function that is called for this block
     */
    public int getRenderType()
    {
        return 3;
    }

    public boolean isFullCube()
    {
        return false;
    }

    public boolean isOpaqueCube()
    {
        return false;
    }

    public boolean shouldSideBeRendered(IBlockAccess worldIn, BlockPos pos, EnumFacing side)
    {
        return true;
    }

    public static EnumFacing func_176428_b(int p_176428_0_)
    {
        return EnumFacing.getFront(p_176428_0_ & 7);
    }

    /**
     * Get's the hopper's active status from the 8-bit of the metadata. Note that the metadata stores whether the block
     * is powered, so this returns true when that bit is 0.
     */
    public static boolean getActiveStateFromMetadata(int p_149917_0_)
    {
        return (p_149917_0_ & 8) != 8;
    }

    public boolean hasComparatorInputOverride()
    {
        return true;
    }

    public int getComparatorInputOverride(World worldIn, BlockPos pos)
    {
        return Container.calcRedstoneFromInventory(worldIn.getTileEntity(pos));
    }

    public EnumWorldBlockLayer getBlockLayer()
    {
        return EnumWorldBlockLayer.CUTOUT_MIPPED;
    }

    /**
     * Convert the given metadata into a BlockState for this Block
     */
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(field_176430_a, func_176428_b(meta)).withProperty(field_176429_b, Boolean.valueOf(getActiveStateFromMetadata(meta)));
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    public int getMetaFromState(IBlockState state)
    {
        byte var2 = 0;
        int var3 = var2 | ((EnumFacing)state.getValue(field_176430_a)).getIndex();

        if (!((Boolean)state.getValue(field_176429_b)).booleanValue())
        {
            var3 |= 8;
        }

        return var3;
    }

    protected BlockState createBlockState()
    {
        return new BlockState(this, new IProperty[] {field_176430_a, field_176429_b});
    }
}
