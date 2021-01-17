// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import net.minecraft.block.state.BlockState;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.stats.StatList;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import java.util.Random;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.material.Material;
import com.google.common.base.Predicate;
import net.minecraft.util.EnumFacing;
import net.minecraft.block.properties.PropertyDirection;

public class BlockFurnace extends BlockContainer
{
    public static final PropertyDirection FACING;
    private final boolean isBurning;
    private static boolean keepInventory;
    
    static {
        FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
    }
    
    protected BlockFurnace(final boolean isBurning) {
        super(Material.rock);
        this.setDefaultState(this.blockState.getBaseState().withProperty((IProperty<Comparable>)BlockFurnace.FACING, EnumFacing.NORTH));
        this.isBurning = isBurning;
    }
    
    @Override
    public Item getItemDropped(final IBlockState state, final Random rand, final int fortune) {
        return Item.getItemFromBlock(Blocks.furnace);
    }
    
    @Override
    public void onBlockAdded(final World worldIn, final BlockPos pos, final IBlockState state) {
        this.setDefaultFacing(worldIn, pos, state);
    }
    
    private void setDefaultFacing(final World worldIn, final BlockPos pos, final IBlockState state) {
        if (!worldIn.isRemote) {
            final Block block = worldIn.getBlockState(pos.north()).getBlock();
            final Block block2 = worldIn.getBlockState(pos.south()).getBlock();
            final Block block3 = worldIn.getBlockState(pos.west()).getBlock();
            final Block block4 = worldIn.getBlockState(pos.east()).getBlock();
            EnumFacing enumfacing = state.getValue((IProperty<EnumFacing>)BlockFurnace.FACING);
            if (enumfacing == EnumFacing.NORTH && block.isFullBlock() && !block2.isFullBlock()) {
                enumfacing = EnumFacing.SOUTH;
            }
            else if (enumfacing == EnumFacing.SOUTH && block2.isFullBlock() && !block.isFullBlock()) {
                enumfacing = EnumFacing.NORTH;
            }
            else if (enumfacing == EnumFacing.WEST && block3.isFullBlock() && !block4.isFullBlock()) {
                enumfacing = EnumFacing.EAST;
            }
            else if (enumfacing == EnumFacing.EAST && block4.isFullBlock() && !block3.isFullBlock()) {
                enumfacing = EnumFacing.WEST;
            }
            worldIn.setBlockState(pos, state.withProperty((IProperty<Comparable>)BlockFurnace.FACING, enumfacing), 2);
        }
    }
    
    @Override
    public void randomDisplayTick(final World worldIn, final BlockPos pos, final IBlockState state, final Random rand) {
        if (this.isBurning) {
            final EnumFacing enumfacing = state.getValue((IProperty<EnumFacing>)BlockFurnace.FACING);
            final double d0 = pos.getX() + 0.5;
            final double d2 = pos.getY() + rand.nextDouble() * 6.0 / 16.0;
            final double d3 = pos.getZ() + 0.5;
            final double d4 = 0.52;
            final double d5 = rand.nextDouble() * 0.6 - 0.3;
            switch (enumfacing) {
                case WEST: {
                    worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0 - d4, d2, d3 + d5, 0.0, 0.0, 0.0, new int[0]);
                    worldIn.spawnParticle(EnumParticleTypes.FLAME, d0 - d4, d2, d3 + d5, 0.0, 0.0, 0.0, new int[0]);
                    break;
                }
                case EAST: {
                    worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0 + d4, d2, d3 + d5, 0.0, 0.0, 0.0, new int[0]);
                    worldIn.spawnParticle(EnumParticleTypes.FLAME, d0 + d4, d2, d3 + d5, 0.0, 0.0, 0.0, new int[0]);
                    break;
                }
                case NORTH: {
                    worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0 + d5, d2, d3 - d4, 0.0, 0.0, 0.0, new int[0]);
                    worldIn.spawnParticle(EnumParticleTypes.FLAME, d0 + d5, d2, d3 - d4, 0.0, 0.0, 0.0, new int[0]);
                    break;
                }
                case SOUTH: {
                    worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0 + d5, d2, d3 + d4, 0.0, 0.0, 0.0, new int[0]);
                    worldIn.spawnParticle(EnumParticleTypes.FLAME, d0 + d5, d2, d3 + d4, 0.0, 0.0, 0.0, new int[0]);
                    break;
                }
            }
        }
    }
    
    @Override
    public boolean onBlockActivated(final World worldIn, final BlockPos pos, final IBlockState state, final EntityPlayer playerIn, final EnumFacing side, final float hitX, final float hitY, final float hitZ) {
        if (worldIn.isRemote) {
            return true;
        }
        final TileEntity tileentity = worldIn.getTileEntity(pos);
        if (tileentity instanceof TileEntityFurnace) {
            playerIn.displayGUIChest((IInventory)tileentity);
            playerIn.triggerAchievement(StatList.field_181741_Y);
        }
        return true;
    }
    
    public static void setState(final boolean active, final World worldIn, final BlockPos pos) {
        final IBlockState iblockstate = worldIn.getBlockState(pos);
        final TileEntity tileentity = worldIn.getTileEntity(pos);
        BlockFurnace.keepInventory = true;
        if (active) {
            worldIn.setBlockState(pos, Blocks.lit_furnace.getDefaultState().withProperty((IProperty<Comparable>)BlockFurnace.FACING, (EnumFacing)iblockstate.getValue((IProperty<V>)BlockFurnace.FACING)), 3);
            worldIn.setBlockState(pos, Blocks.lit_furnace.getDefaultState().withProperty((IProperty<Comparable>)BlockFurnace.FACING, (EnumFacing)iblockstate.getValue((IProperty<V>)BlockFurnace.FACING)), 3);
        }
        else {
            worldIn.setBlockState(pos, Blocks.furnace.getDefaultState().withProperty((IProperty<Comparable>)BlockFurnace.FACING, (EnumFacing)iblockstate.getValue((IProperty<V>)BlockFurnace.FACING)), 3);
            worldIn.setBlockState(pos, Blocks.furnace.getDefaultState().withProperty((IProperty<Comparable>)BlockFurnace.FACING, (EnumFacing)iblockstate.getValue((IProperty<V>)BlockFurnace.FACING)), 3);
        }
        BlockFurnace.keepInventory = false;
        if (tileentity != null) {
            tileentity.validate();
            worldIn.setTileEntity(pos, tileentity);
        }
    }
    
    @Override
    public TileEntity createNewTileEntity(final World worldIn, final int meta) {
        return new TileEntityFurnace();
    }
    
    @Override
    public IBlockState onBlockPlaced(final World worldIn, final BlockPos pos, final EnumFacing facing, final float hitX, final float hitY, final float hitZ, final int meta, final EntityLivingBase placer) {
        return this.getDefaultState().withProperty((IProperty<Comparable>)BlockFurnace.FACING, placer.getHorizontalFacing().getOpposite());
    }
    
    @Override
    public void onBlockPlacedBy(final World worldIn, final BlockPos pos, final IBlockState state, final EntityLivingBase placer, final ItemStack stack) {
        worldIn.setBlockState(pos, state.withProperty((IProperty<Comparable>)BlockFurnace.FACING, placer.getHorizontalFacing().getOpposite()), 2);
        if (stack.hasDisplayName()) {
            final TileEntity tileentity = worldIn.getTileEntity(pos);
            if (tileentity instanceof TileEntityFurnace) {
                ((TileEntityFurnace)tileentity).setCustomInventoryName(stack.getDisplayName());
            }
        }
    }
    
    @Override
    public void breakBlock(final World worldIn, final BlockPos pos, final IBlockState state) {
        if (!BlockFurnace.keepInventory) {
            final TileEntity tileentity = worldIn.getTileEntity(pos);
            if (tileentity instanceof TileEntityFurnace) {
                InventoryHelper.dropInventoryItems(worldIn, pos, (IInventory)tileentity);
                worldIn.updateComparatorOutputLevel(pos, this);
            }
        }
        super.breakBlock(worldIn, pos, state);
    }
    
    @Override
    public boolean hasComparatorInputOverride() {
        return true;
    }
    
    @Override
    public int getComparatorInputOverride(final World worldIn, final BlockPos pos) {
        return Container.calcRedstone(worldIn.getTileEntity(pos));
    }
    
    @Override
    public Item getItem(final World worldIn, final BlockPos pos) {
        return Item.getItemFromBlock(Blocks.furnace);
    }
    
    @Override
    public int getRenderType() {
        return 3;
    }
    
    @Override
    public IBlockState getStateForEntityRender(final IBlockState state) {
        return this.getDefaultState().withProperty((IProperty<Comparable>)BlockFurnace.FACING, EnumFacing.SOUTH);
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        EnumFacing enumfacing = EnumFacing.getFront(meta);
        if (enumfacing.getAxis() == EnumFacing.Axis.Y) {
            enumfacing = EnumFacing.NORTH;
        }
        return this.getDefaultState().withProperty((IProperty<Comparable>)BlockFurnace.FACING, enumfacing);
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        return state.getValue((IProperty<EnumFacing>)BlockFurnace.FACING).getIndex();
    }
    
    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[] { BlockFurnace.FACING });
    }
}
