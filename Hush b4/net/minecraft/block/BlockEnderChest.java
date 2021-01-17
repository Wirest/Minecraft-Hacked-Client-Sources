// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import net.minecraft.block.state.BlockState;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.inventory.InventoryEnderChest;
import net.minecraft.stats.StatList;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntityEnderChest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import java.util.Random;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.material.Material;
import com.google.common.base.Predicate;
import net.minecraft.util.EnumFacing;
import net.minecraft.block.properties.PropertyDirection;

public class BlockEnderChest extends BlockContainer
{
    public static final PropertyDirection FACING;
    
    static {
        FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
    }
    
    protected BlockEnderChest() {
        super(Material.rock);
        this.setDefaultState(this.blockState.getBaseState().withProperty((IProperty<Comparable>)BlockEnderChest.FACING, EnumFacing.NORTH));
        this.setCreativeTab(CreativeTabs.tabDecorations);
        this.setBlockBounds(0.0625f, 0.0f, 0.0625f, 0.9375f, 0.875f, 0.9375f);
    }
    
    @Override
    public boolean isOpaqueCube() {
        return false;
    }
    
    @Override
    public boolean isFullCube() {
        return false;
    }
    
    @Override
    public int getRenderType() {
        return 2;
    }
    
    @Override
    public Item getItemDropped(final IBlockState state, final Random rand, final int fortune) {
        return Item.getItemFromBlock(Blocks.obsidian);
    }
    
    @Override
    public int quantityDropped(final Random random) {
        return 8;
    }
    
    @Override
    protected boolean canSilkHarvest() {
        return true;
    }
    
    @Override
    public IBlockState onBlockPlaced(final World worldIn, final BlockPos pos, final EnumFacing facing, final float hitX, final float hitY, final float hitZ, final int meta, final EntityLivingBase placer) {
        return this.getDefaultState().withProperty((IProperty<Comparable>)BlockEnderChest.FACING, placer.getHorizontalFacing().getOpposite());
    }
    
    @Override
    public void onBlockPlacedBy(final World worldIn, final BlockPos pos, final IBlockState state, final EntityLivingBase placer, final ItemStack stack) {
        worldIn.setBlockState(pos, state.withProperty((IProperty<Comparable>)BlockEnderChest.FACING, placer.getHorizontalFacing().getOpposite()), 2);
    }
    
    @Override
    public boolean onBlockActivated(final World worldIn, final BlockPos pos, final IBlockState state, final EntityPlayer playerIn, final EnumFacing side, final float hitX, final float hitY, final float hitZ) {
        final InventoryEnderChest inventoryenderchest = playerIn.getInventoryEnderChest();
        final TileEntity tileentity = worldIn.getTileEntity(pos);
        if (inventoryenderchest == null || !(tileentity instanceof TileEntityEnderChest)) {
            return true;
        }
        if (worldIn.getBlockState(pos.up()).getBlock().isNormalCube()) {
            return true;
        }
        if (worldIn.isRemote) {
            return true;
        }
        inventoryenderchest.setChestTileEntity((TileEntityEnderChest)tileentity);
        playerIn.displayGUIChest(inventoryenderchest);
        playerIn.triggerAchievement(StatList.field_181738_V);
        return true;
    }
    
    @Override
    public TileEntity createNewTileEntity(final World worldIn, final int meta) {
        return new TileEntityEnderChest();
    }
    
    @Override
    public void randomDisplayTick(final World worldIn, final BlockPos pos, final IBlockState state, final Random rand) {
        for (int i = 0; i < 3; ++i) {
            final int j = rand.nextInt(2) * 2 - 1;
            final int k = rand.nextInt(2) * 2 - 1;
            final double d0 = pos.getX() + 0.5 + 0.25 * j;
            final double d2 = pos.getY() + rand.nextFloat();
            final double d3 = pos.getZ() + 0.5 + 0.25 * k;
            final double d4 = rand.nextFloat() * j;
            final double d5 = (rand.nextFloat() - 0.5) * 0.125;
            final double d6 = rand.nextFloat() * k;
            worldIn.spawnParticle(EnumParticleTypes.PORTAL, d0, d2, d3, d4, d5, d6, new int[0]);
        }
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        EnumFacing enumfacing = EnumFacing.getFront(meta);
        if (enumfacing.getAxis() == EnumFacing.Axis.Y) {
            enumfacing = EnumFacing.NORTH;
        }
        return this.getDefaultState().withProperty((IProperty<Comparable>)BlockEnderChest.FACING, enumfacing);
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        return state.getValue((IProperty<EnumFacing>)BlockEnderChest.FACING).getIndex();
    }
    
    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[] { BlockEnderChest.FACING });
    }
}
