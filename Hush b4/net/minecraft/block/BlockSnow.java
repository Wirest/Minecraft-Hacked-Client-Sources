// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import net.minecraft.block.state.BlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.item.Item;
import java.util.Random;
import net.minecraft.stats.StatList;
import net.minecraft.item.ItemStack;
import net.minecraft.init.Items;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.block.state.IBlockState;
import net.minecraft.world.World;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;

public class BlockSnow extends Block
{
    public static final PropertyInteger LAYERS;
    
    static {
        LAYERS = PropertyInteger.create("layers", 1, 8);
    }
    
    protected BlockSnow() {
        super(Material.snow);
        this.setDefaultState(this.blockState.getBaseState().withProperty((IProperty<Comparable>)BlockSnow.LAYERS, 1));
        this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 0.125f, 1.0f);
        this.setTickRandomly(true);
        this.setCreativeTab(CreativeTabs.tabDecorations);
        this.setBlockBoundsForItemRender();
    }
    
    @Override
    public boolean isPassable(final IBlockAccess worldIn, final BlockPos pos) {
        return worldIn.getBlockState(pos).getValue((IProperty<Integer>)BlockSnow.LAYERS) < 5;
    }
    
    @Override
    public AxisAlignedBB getCollisionBoundingBox(final World worldIn, final BlockPos pos, final IBlockState state) {
        final int i = state.getValue((IProperty<Integer>)BlockSnow.LAYERS) - 1;
        final float f = 0.125f;
        return new AxisAlignedBB(pos.getX() + this.minX, pos.getY() + this.minY, pos.getZ() + this.minZ, pos.getX() + this.maxX, pos.getY() + i * f, pos.getZ() + this.maxZ);
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
    public void setBlockBoundsForItemRender() {
        this.getBoundsForLayers(0);
    }
    
    @Override
    public void setBlockBoundsBasedOnState(final IBlockAccess worldIn, final BlockPos pos) {
        final IBlockState iblockstate = worldIn.getBlockState(pos);
        this.getBoundsForLayers(iblockstate.getValue((IProperty<Integer>)BlockSnow.LAYERS));
    }
    
    protected void getBoundsForLayers(final int p_150154_1_) {
        this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, p_150154_1_ / 8.0f, 1.0f);
    }
    
    @Override
    public boolean canPlaceBlockAt(final World worldIn, final BlockPos pos) {
        final IBlockState iblockstate = worldIn.getBlockState(pos.down());
        final Block block = iblockstate.getBlock();
        return block != Blocks.ice && block != Blocks.packed_ice && (block.getMaterial() == Material.leaves || (block == this && iblockstate.getValue((IProperty<Integer>)BlockSnow.LAYERS) >= 7) || (block.isOpaqueCube() && block.blockMaterial.blocksMovement()));
    }
    
    @Override
    public void onNeighborBlockChange(final World worldIn, final BlockPos pos, final IBlockState state, final Block neighborBlock) {
        this.checkAndDropBlock(worldIn, pos, state);
    }
    
    private boolean checkAndDropBlock(final World worldIn, final BlockPos pos, final IBlockState state) {
        if (!this.canPlaceBlockAt(worldIn, pos)) {
            this.dropBlockAsItem(worldIn, pos, state, 0);
            worldIn.setBlockToAir(pos);
            return false;
        }
        return true;
    }
    
    @Override
    public void harvestBlock(final World worldIn, final EntityPlayer player, final BlockPos pos, final IBlockState state, final TileEntity te) {
        Block.spawnAsEntity(worldIn, pos, new ItemStack(Items.snowball, state.getValue((IProperty<Integer>)BlockSnow.LAYERS) + 1, 0));
        worldIn.setBlockToAir(pos);
        player.triggerAchievement(StatList.mineBlockStatArray[Block.getIdFromBlock(this)]);
    }
    
    @Override
    public Item getItemDropped(final IBlockState state, final Random rand, final int fortune) {
        return Items.snowball;
    }
    
    @Override
    public int quantityDropped(final Random random) {
        return 0;
    }
    
    @Override
    public void updateTick(final World worldIn, final BlockPos pos, final IBlockState state, final Random rand) {
        if (worldIn.getLightFor(EnumSkyBlock.BLOCK, pos) > 11) {
            this.dropBlockAsItem(worldIn, pos, worldIn.getBlockState(pos), 0);
            worldIn.setBlockToAir(pos);
        }
    }
    
    @Override
    public boolean shouldSideBeRendered(final IBlockAccess worldIn, final BlockPos pos, final EnumFacing side) {
        return side == EnumFacing.UP || super.shouldSideBeRendered(worldIn, pos, side);
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        return this.getDefaultState().withProperty((IProperty<Comparable>)BlockSnow.LAYERS, (meta & 0x7) + 1);
    }
    
    @Override
    public boolean isReplaceable(final World worldIn, final BlockPos pos) {
        return worldIn.getBlockState(pos).getValue((IProperty<Integer>)BlockSnow.LAYERS) == 1;
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        return state.getValue((IProperty<Integer>)BlockSnow.LAYERS) - 1;
    }
    
    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[] { BlockSnow.LAYERS });
    }
}
