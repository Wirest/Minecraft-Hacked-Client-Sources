package net.minecraft.block;

import java.util.List;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemLead;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockFence extends Block
{
    /** Whether this fence connects in the northern direction */
    public static final PropertyBool NORTH = PropertyBool.create("north");

    /** Whether this fence connects in the eastern direction */
    public static final PropertyBool EAST = PropertyBool.create("east");

    /** Whether this fence connects in the southern direction */
    public static final PropertyBool SOUTH = PropertyBool.create("south");

    /** Whether this fence connects in the western direction */
    public static final PropertyBool WEST = PropertyBool.create("west");

    public BlockFence(Material materialIn)
    {
        super(materialIn);
        this.setDefaultState(this.blockState.getBaseState().withProperty(NORTH, Boolean.valueOf(false)).withProperty(EAST, Boolean.valueOf(false)).withProperty(SOUTH, Boolean.valueOf(false)).withProperty(WEST, Boolean.valueOf(false)));
        this.setCreativeTab(CreativeTabs.tabDecorations);
    }

    /**
     * Add all collision boxes of this Block to the list that intersect with the given mask.
     *  
     * @param collidingEntity the Entity colliding with this Block
     */
    @Override
	public void addCollisionBoxesToList(World worldIn, BlockPos pos, IBlockState state, AxisAlignedBB mask, List list, Entity collidingEntity)
    {
        boolean var7 = this.canConnectTo(worldIn, pos.north());
        boolean var8 = this.canConnectTo(worldIn, pos.south());
        boolean var9 = this.canConnectTo(worldIn, pos.west());
        boolean var10 = this.canConnectTo(worldIn, pos.east());
        float var11 = 0.375F;
        float var12 = 0.625F;
        float var13 = 0.375F;
        float var14 = 0.625F;

        if (var7)
        {
            var13 = 0.0F;
        }

        if (var8)
        {
            var14 = 1.0F;
        }

        if (var7 || var8)
        {
            this.setBlockBounds(var11, 0.0F, var13, var12, 1.5F, var14);
            super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
        }

        var13 = 0.375F;
        var14 = 0.625F;

        if (var9)
        {
            var11 = 0.0F;
        }

        if (var10)
        {
            var12 = 1.0F;
        }

        if (var9 || var10 || !var7 && !var8)
        {
            this.setBlockBounds(var11, 0.0F, var13, var12, 1.5F, var14);
            super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
        }

        if (var7)
        {
            var13 = 0.0F;
        }

        if (var8)
        {
            var14 = 1.0F;
        }

        this.setBlockBounds(var11, 0.0F, var13, var12, 1.0F, var14);
    }

    @Override
	public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos)
    {
        boolean var3 = this.canConnectTo(worldIn, pos.north());
        boolean var4 = this.canConnectTo(worldIn, pos.south());
        boolean var5 = this.canConnectTo(worldIn, pos.west());
        boolean var6 = this.canConnectTo(worldIn, pos.east());
        float var7 = 0.375F;
        float var8 = 0.625F;
        float var9 = 0.375F;
        float var10 = 0.625F;

        if (var3)
        {
            var9 = 0.0F;
        }

        if (var4)
        {
            var10 = 1.0F;
        }

        if (var5)
        {
            var7 = 0.0F;
        }

        if (var6)
        {
            var8 = 1.0F;
        }

        this.setBlockBounds(var7, 0.0F, var9, var8, 1.0F, var10);
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
	public boolean isPassable(IBlockAccess worldIn, BlockPos pos)
    {
        return false;
    }

    public boolean canConnectTo(IBlockAccess worldIn, BlockPos pos)
    {
        Block var3 = worldIn.getBlockState(pos).getBlock();
        return var3 == Blocks.barrier ? false : ((!(var3 instanceof BlockFence) || var3.blockMaterial != this.blockMaterial) && !(var3 instanceof BlockFenceGate) ? (var3.blockMaterial.isOpaque() && var3.isFullCube() ? var3.blockMaterial != Material.gourd : false) : true);
    }

    @Override
	public boolean shouldSideBeRendered(IBlockAccess worldIn, BlockPos pos, EnumFacing side)
    {
        return true;
    }

    @Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        return worldIn.isRemote ? true : ItemLead.attachToFence(playerIn, worldIn, pos);
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    @Override
	public int getMetaFromState(IBlockState state)
    {
        return 0;
    }

    /**
     * Get the actual Block state of this Block at the given position. This applies properties not visible in the
     * metadata, such as fence connections.
     */
    @Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
    {
        return state.withProperty(NORTH, Boolean.valueOf(this.canConnectTo(worldIn, pos.north()))).withProperty(EAST, Boolean.valueOf(this.canConnectTo(worldIn, pos.east()))).withProperty(SOUTH, Boolean.valueOf(this.canConnectTo(worldIn, pos.south()))).withProperty(WEST, Boolean.valueOf(this.canConnectTo(worldIn, pos.west())));
    }

    @Override
	protected BlockState createBlockState()
    {
        return new BlockState(this, new IProperty[] {NORTH, EAST, WEST, SOUTH});
    }
}
