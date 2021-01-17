// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import net.minecraft.block.state.BlockState;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.inventory.Container;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.util.EnumParticleTypes;
import java.util.Random;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.stats.StatList;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.EnumFacing;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.Entity;
import java.util.List;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.tileentity.TileEntityBrewingStand;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraft.util.StatCollector;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;

public class BlockBrewingStand extends BlockContainer
{
    public static final PropertyBool[] HAS_BOTTLE;
    
    static {
        HAS_BOTTLE = new PropertyBool[] { PropertyBool.create("has_bottle_0"), PropertyBool.create("has_bottle_1"), PropertyBool.create("has_bottle_2") };
    }
    
    public BlockBrewingStand() {
        super(Material.iron);
        this.setDefaultState(this.blockState.getBaseState().withProperty((IProperty<Comparable>)BlockBrewingStand.HAS_BOTTLE[0], false).withProperty((IProperty<Comparable>)BlockBrewingStand.HAS_BOTTLE[1], false).withProperty((IProperty<Comparable>)BlockBrewingStand.HAS_BOTTLE[2], false));
    }
    
    @Override
    public String getLocalizedName() {
        return StatCollector.translateToLocal("item.brewingStand.name");
    }
    
    @Override
    public boolean isOpaqueCube() {
        return false;
    }
    
    @Override
    public int getRenderType() {
        return 3;
    }
    
    @Override
    public TileEntity createNewTileEntity(final World worldIn, final int meta) {
        return new TileEntityBrewingStand();
    }
    
    @Override
    public boolean isFullCube() {
        return false;
    }
    
    @Override
    public void addCollisionBoxesToList(final World worldIn, final BlockPos pos, final IBlockState state, final AxisAlignedBB mask, final List<AxisAlignedBB> list, final Entity collidingEntity) {
        this.setBlockBounds(0.4375f, 0.0f, 0.4375f, 0.5625f, 0.875f, 0.5625f);
        super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
        this.setBlockBoundsForItemRender();
        super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
    }
    
    @Override
    public void setBlockBoundsForItemRender() {
        this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 0.125f, 1.0f);
    }
    
    @Override
    public boolean onBlockActivated(final World worldIn, final BlockPos pos, final IBlockState state, final EntityPlayer playerIn, final EnumFacing side, final float hitX, final float hitY, final float hitZ) {
        if (worldIn.isRemote) {
            return true;
        }
        final TileEntity tileentity = worldIn.getTileEntity(pos);
        if (tileentity instanceof TileEntityBrewingStand) {
            playerIn.displayGUIChest((IInventory)tileentity);
            playerIn.triggerAchievement(StatList.field_181729_M);
        }
        return true;
    }
    
    @Override
    public void onBlockPlacedBy(final World worldIn, final BlockPos pos, final IBlockState state, final EntityLivingBase placer, final ItemStack stack) {
        if (stack.hasDisplayName()) {
            final TileEntity tileentity = worldIn.getTileEntity(pos);
            if (tileentity instanceof TileEntityBrewingStand) {
                ((TileEntityBrewingStand)tileentity).setName(stack.getDisplayName());
            }
        }
    }
    
    @Override
    public void randomDisplayTick(final World worldIn, final BlockPos pos, final IBlockState state, final Random rand) {
        final double d0 = pos.getX() + 0.4f + rand.nextFloat() * 0.2f;
        final double d2 = pos.getY() + 0.7f + rand.nextFloat() * 0.3f;
        final double d3 = pos.getZ() + 0.4f + rand.nextFloat() * 0.2f;
        worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0, d2, d3, 0.0, 0.0, 0.0, new int[0]);
    }
    
    @Override
    public void breakBlock(final World worldIn, final BlockPos pos, final IBlockState state) {
        final TileEntity tileentity = worldIn.getTileEntity(pos);
        if (tileentity instanceof TileEntityBrewingStand) {
            InventoryHelper.dropInventoryItems(worldIn, pos, (IInventory)tileentity);
        }
        super.breakBlock(worldIn, pos, state);
    }
    
    @Override
    public Item getItemDropped(final IBlockState state, final Random rand, final int fortune) {
        return Items.brewing_stand;
    }
    
    @Override
    public Item getItem(final World worldIn, final BlockPos pos) {
        return Items.brewing_stand;
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
    public EnumWorldBlockLayer getBlockLayer() {
        return EnumWorldBlockLayer.CUTOUT;
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        IBlockState iblockstate = this.getDefaultState();
        for (int i = 0; i < 3; ++i) {
            iblockstate = iblockstate.withProperty((IProperty<Comparable>)BlockBrewingStand.HAS_BOTTLE[i], (meta & 1 << i) > 0);
        }
        return iblockstate;
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        int i = 0;
        for (int j = 0; j < 3; ++j) {
            if (state.getValue((IProperty<Boolean>)BlockBrewingStand.HAS_BOTTLE[j])) {
                i |= 1 << j;
            }
        }
        return i;
    }
    
    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[] { BlockBrewingStand.HAS_BOTTLE[0], BlockBrewingStand.HAS_BOTTLE[1], BlockBrewingStand.HAS_BOTTLE[2] });
    }
}
