// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import net.minecraft.item.ItemStack;
import java.util.List;
import net.minecraft.block.state.BlockState;
import net.minecraft.tileentity.TileEntityDaylightDetector;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.item.Item;
import java.util.Random;
import net.minecraft.init.Blocks;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraft.util.EnumFacing;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;

public class BlockDaylightDetector extends BlockContainer
{
    public static final PropertyInteger POWER;
    private final boolean inverted;
    
    static {
        POWER = PropertyInteger.create("power", 0, 15);
    }
    
    public BlockDaylightDetector(final boolean inverted) {
        super(Material.wood);
        this.inverted = inverted;
        this.setDefaultState(this.blockState.getBaseState().withProperty((IProperty<Comparable>)BlockDaylightDetector.POWER, 0));
        this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 0.375f, 1.0f);
        this.setCreativeTab(CreativeTabs.tabRedstone);
        this.setHardness(0.2f);
        this.setStepSound(BlockDaylightDetector.soundTypeWood);
        this.setUnlocalizedName("daylightDetector");
    }
    
    @Override
    public void setBlockBoundsBasedOnState(final IBlockAccess worldIn, final BlockPos pos) {
        this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 0.375f, 1.0f);
    }
    
    @Override
    public int getWeakPower(final IBlockAccess worldIn, final BlockPos pos, final IBlockState state, final EnumFacing side) {
        return state.getValue((IProperty<Integer>)BlockDaylightDetector.POWER);
    }
    
    public void updatePower(final World worldIn, final BlockPos pos) {
        if (!worldIn.provider.getHasNoSky()) {
            final IBlockState iblockstate = worldIn.getBlockState(pos);
            int i = worldIn.getLightFor(EnumSkyBlock.SKY, pos) - worldIn.getSkylightSubtracted();
            float f = worldIn.getCelestialAngleRadians(1.0f);
            final float f2 = (f < 3.1415927f) ? 0.0f : 6.2831855f;
            f += (f2 - f) * 0.2f;
            i = Math.round(i * MathHelper.cos(f));
            i = MathHelper.clamp_int(i, 0, 15);
            if (this.inverted) {
                i = 15 - i;
            }
            if (iblockstate.getValue((IProperty<Integer>)BlockDaylightDetector.POWER) != i) {
                worldIn.setBlockState(pos, iblockstate.withProperty((IProperty<Comparable>)BlockDaylightDetector.POWER, i), 3);
            }
        }
    }
    
    @Override
    public boolean onBlockActivated(final World worldIn, final BlockPos pos, final IBlockState state, final EntityPlayer playerIn, final EnumFacing side, final float hitX, final float hitY, final float hitZ) {
        if (!playerIn.isAllowEdit()) {
            return super.onBlockActivated(worldIn, pos, state, playerIn, side, hitX, hitY, hitZ);
        }
        if (worldIn.isRemote) {
            return true;
        }
        if (this.inverted) {
            worldIn.setBlockState(pos, Blocks.daylight_detector.getDefaultState().withProperty((IProperty<Comparable>)BlockDaylightDetector.POWER, (Integer)state.getValue((IProperty<V>)BlockDaylightDetector.POWER)), 4);
            Blocks.daylight_detector.updatePower(worldIn, pos);
        }
        else {
            worldIn.setBlockState(pos, Blocks.daylight_detector_inverted.getDefaultState().withProperty((IProperty<Comparable>)BlockDaylightDetector.POWER, (Integer)state.getValue((IProperty<V>)BlockDaylightDetector.POWER)), 4);
            Blocks.daylight_detector_inverted.updatePower(worldIn, pos);
        }
        return true;
    }
    
    @Override
    public Item getItemDropped(final IBlockState state, final Random rand, final int fortune) {
        return Item.getItemFromBlock(Blocks.daylight_detector);
    }
    
    @Override
    public Item getItem(final World worldIn, final BlockPos pos) {
        return Item.getItemFromBlock(Blocks.daylight_detector);
    }
    
    @Override
    public boolean isFullCube() {
        return false;
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
    public boolean canProvidePower() {
        return true;
    }
    
    @Override
    public TileEntity createNewTileEntity(final World worldIn, final int meta) {
        return new TileEntityDaylightDetector();
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        return this.getDefaultState().withProperty((IProperty<Comparable>)BlockDaylightDetector.POWER, meta);
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        return state.getValue((IProperty<Integer>)BlockDaylightDetector.POWER);
    }
    
    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[] { BlockDaylightDetector.POWER });
    }
    
    @Override
    public void getSubBlocks(final Item itemIn, final CreativeTabs tab, final List<ItemStack> list) {
        if (!this.inverted) {
            super.getSubBlocks(itemIn, tab, list);
        }
    }
}
