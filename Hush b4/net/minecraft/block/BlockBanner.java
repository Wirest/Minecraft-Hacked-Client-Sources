// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import net.minecraft.block.state.BlockState;
import net.minecraft.block.properties.IProperty;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.item.ItemStack;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import java.util.Random;
import net.minecraft.tileentity.TileEntityBanner;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.util.StatCollector;
import net.minecraft.block.material.Material;
import com.google.common.base.Predicate;
import net.minecraft.util.EnumFacing;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.properties.PropertyDirection;

public class BlockBanner extends BlockContainer
{
    public static final PropertyDirection FACING;
    public static final PropertyInteger ROTATION;
    
    static {
        FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
        ROTATION = PropertyInteger.create("rotation", 0, 15);
    }
    
    protected BlockBanner() {
        super(Material.wood);
        final float f = 0.25f;
        final float f2 = 1.0f;
        this.setBlockBounds(0.5f - f, 0.0f, 0.5f - f, 0.5f + f, f2, 0.5f + f);
    }
    
    @Override
    public String getLocalizedName() {
        return StatCollector.translateToLocal("item.banner.white.name");
    }
    
    @Override
    public AxisAlignedBB getCollisionBoundingBox(final World worldIn, final BlockPos pos, final IBlockState state) {
        return null;
    }
    
    @Override
    public AxisAlignedBB getSelectedBoundingBox(final World worldIn, final BlockPos pos) {
        this.setBlockBoundsBasedOnState(worldIn, pos);
        return super.getSelectedBoundingBox(worldIn, pos);
    }
    
    @Override
    public boolean isFullCube() {
        return false;
    }
    
    @Override
    public boolean isPassable(final IBlockAccess worldIn, final BlockPos pos) {
        return true;
    }
    
    @Override
    public boolean isOpaqueCube() {
        return false;
    }
    
    @Override
    public boolean func_181623_g() {
        return true;
    }
    
    @Override
    public TileEntity createNewTileEntity(final World worldIn, final int meta) {
        return new TileEntityBanner();
    }
    
    @Override
    public Item getItemDropped(final IBlockState state, final Random rand, final int fortune) {
        return Items.banner;
    }
    
    @Override
    public Item getItem(final World worldIn, final BlockPos pos) {
        return Items.banner;
    }
    
    @Override
    public void dropBlockAsItemWithChance(final World worldIn, final BlockPos pos, final IBlockState state, final float chance, final int fortune) {
        final TileEntity tileentity = worldIn.getTileEntity(pos);
        if (tileentity instanceof TileEntityBanner) {
            final ItemStack itemstack = new ItemStack(Items.banner, 1, ((TileEntityBanner)tileentity).getBaseColor());
            final NBTTagCompound nbttagcompound = new NBTTagCompound();
            tileentity.writeToNBT(nbttagcompound);
            nbttagcompound.removeTag("x");
            nbttagcompound.removeTag("y");
            nbttagcompound.removeTag("z");
            nbttagcompound.removeTag("id");
            itemstack.setTagInfo("BlockEntityTag", nbttagcompound);
            Block.spawnAsEntity(worldIn, pos, itemstack);
        }
        else {
            super.dropBlockAsItemWithChance(worldIn, pos, state, chance, fortune);
        }
    }
    
    @Override
    public boolean canPlaceBlockAt(final World worldIn, final BlockPos pos) {
        return !this.func_181087_e(worldIn, pos) && super.canPlaceBlockAt(worldIn, pos);
    }
    
    @Override
    public void harvestBlock(final World worldIn, final EntityPlayer player, final BlockPos pos, final IBlockState state, final TileEntity te) {
        if (te instanceof TileEntityBanner) {
            final TileEntityBanner tileentitybanner = (TileEntityBanner)te;
            final ItemStack itemstack = new ItemStack(Items.banner, 1, ((TileEntityBanner)te).getBaseColor());
            final NBTTagCompound nbttagcompound = new NBTTagCompound();
            TileEntityBanner.func_181020_a(nbttagcompound, tileentitybanner.getBaseColor(), tileentitybanner.func_181021_d());
            itemstack.setTagInfo("BlockEntityTag", nbttagcompound);
            Block.spawnAsEntity(worldIn, pos, itemstack);
        }
        else {
            super.harvestBlock(worldIn, player, pos, state, null);
        }
    }
    
    public static class BlockBannerHanging extends BlockBanner
    {
        public BlockBannerHanging() {
            this.setDefaultState(this.blockState.getBaseState().withProperty((IProperty<Comparable>)BlockBannerHanging.FACING, EnumFacing.NORTH));
        }
        
        @Override
        public void setBlockBoundsBasedOnState(final IBlockAccess worldIn, final BlockPos pos) {
            final EnumFacing enumfacing = worldIn.getBlockState(pos).getValue((IProperty<EnumFacing>)BlockBannerHanging.FACING);
            final float f = 0.0f;
            final float f2 = 0.78125f;
            final float f3 = 0.0f;
            final float f4 = 1.0f;
            final float f5 = 0.125f;
            this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
            switch (enumfacing) {
                default: {
                    this.setBlockBounds(f3, f, 1.0f - f5, f4, f2, 1.0f);
                    break;
                }
                case SOUTH: {
                    this.setBlockBounds(f3, f, 0.0f, f4, f2, f5);
                    break;
                }
                case WEST: {
                    this.setBlockBounds(1.0f - f5, f, f3, 1.0f, f2, f4);
                    break;
                }
                case EAST: {
                    this.setBlockBounds(0.0f, f, f3, f5, f2, f4);
                    break;
                }
            }
        }
        
        @Override
        public void onNeighborBlockChange(final World worldIn, final BlockPos pos, final IBlockState state, final Block neighborBlock) {
            final EnumFacing enumfacing = state.getValue((IProperty<EnumFacing>)BlockBannerHanging.FACING);
            if (!worldIn.getBlockState(pos.offset(enumfacing.getOpposite())).getBlock().getMaterial().isSolid()) {
                this.dropBlockAsItem(worldIn, pos, state, 0);
                worldIn.setBlockToAir(pos);
            }
            super.onNeighborBlockChange(worldIn, pos, state, neighborBlock);
        }
        
        @Override
        public IBlockState getStateFromMeta(final int meta) {
            EnumFacing enumfacing = EnumFacing.getFront(meta);
            if (enumfacing.getAxis() == EnumFacing.Axis.Y) {
                enumfacing = EnumFacing.NORTH;
            }
            return this.getDefaultState().withProperty((IProperty<Comparable>)BlockBannerHanging.FACING, enumfacing);
        }
        
        @Override
        public int getMetaFromState(final IBlockState state) {
            return state.getValue((IProperty<EnumFacing>)BlockBannerHanging.FACING).getIndex();
        }
        
        @Override
        protected BlockState createBlockState() {
            return new BlockState(this, new IProperty[] { BlockBannerHanging.FACING });
        }
    }
    
    public static class BlockBannerStanding extends BlockBanner
    {
        public BlockBannerStanding() {
            this.setDefaultState(this.blockState.getBaseState().withProperty((IProperty<Comparable>)BlockBannerStanding.ROTATION, 0));
        }
        
        @Override
        public void onNeighborBlockChange(final World worldIn, final BlockPos pos, final IBlockState state, final Block neighborBlock) {
            if (!worldIn.getBlockState(pos.down()).getBlock().getMaterial().isSolid()) {
                this.dropBlockAsItem(worldIn, pos, state, 0);
                worldIn.setBlockToAir(pos);
            }
            super.onNeighborBlockChange(worldIn, pos, state, neighborBlock);
        }
        
        @Override
        public IBlockState getStateFromMeta(final int meta) {
            return this.getDefaultState().withProperty((IProperty<Comparable>)BlockBannerStanding.ROTATION, meta);
        }
        
        @Override
        public int getMetaFromState(final IBlockState state) {
            return state.getValue((IProperty<Integer>)BlockBannerStanding.ROTATION);
        }
        
        @Override
        protected BlockState createBlockState() {
            return new BlockState(this, new IProperty[] { BlockBannerStanding.ROTATION });
        }
    }
}
