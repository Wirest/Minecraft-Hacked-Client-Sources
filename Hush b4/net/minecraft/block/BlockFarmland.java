// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import net.minecraft.block.state.BlockState;
import net.minecraft.item.Item;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import java.util.Iterator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import java.util.Random;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;

public class BlockFarmland extends Block
{
    public static final PropertyInteger MOISTURE;
    
    static {
        MOISTURE = PropertyInteger.create("moisture", 0, 7);
    }
    
    protected BlockFarmland() {
        super(Material.ground);
        this.setDefaultState(this.blockState.getBaseState().withProperty((IProperty<Comparable>)BlockFarmland.MOISTURE, 0));
        this.setTickRandomly(true);
        this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 0.9375f, 1.0f);
        this.setLightOpacity(255);
    }
    
    @Override
    public AxisAlignedBB getCollisionBoundingBox(final World worldIn, final BlockPos pos, final IBlockState state) {
        return new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1);
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
    public void updateTick(final World worldIn, final BlockPos pos, final IBlockState state, final Random rand) {
        final int i = state.getValue((IProperty<Integer>)BlockFarmland.MOISTURE);
        if (!this.hasWater(worldIn, pos) && !worldIn.canLightningStrike(pos.up())) {
            if (i > 0) {
                worldIn.setBlockState(pos, state.withProperty((IProperty<Comparable>)BlockFarmland.MOISTURE, i - 1), 2);
            }
            else if (!this.hasCrops(worldIn, pos)) {
                worldIn.setBlockState(pos, Blocks.dirt.getDefaultState());
            }
        }
        else if (i < 7) {
            worldIn.setBlockState(pos, state.withProperty((IProperty<Comparable>)BlockFarmland.MOISTURE, 7), 2);
        }
    }
    
    @Override
    public void onFallenUpon(final World worldIn, final BlockPos pos, final Entity entityIn, final float fallDistance) {
        if (entityIn instanceof EntityLivingBase) {
            if (!worldIn.isRemote && worldIn.rand.nextFloat() < fallDistance - 0.5f) {
                if (!(entityIn instanceof EntityPlayer) && !worldIn.getGameRules().getBoolean("mobGriefing")) {
                    return;
                }
                worldIn.setBlockState(pos, Blocks.dirt.getDefaultState());
            }
            super.onFallenUpon(worldIn, pos, entityIn, fallDistance);
        }
    }
    
    private boolean hasCrops(final World worldIn, final BlockPos pos) {
        final Block block = worldIn.getBlockState(pos.up()).getBlock();
        return block instanceof BlockCrops || block instanceof BlockStem;
    }
    
    private boolean hasWater(final World worldIn, final BlockPos pos) {
        for (final BlockPos.MutableBlockPos blockpos$mutableblockpos : BlockPos.getAllInBoxMutable(pos.add(-4, 0, -4), pos.add(4, 1, 4))) {
            if (worldIn.getBlockState(blockpos$mutableblockpos).getBlock().getMaterial() == Material.water) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public void onNeighborBlockChange(final World worldIn, final BlockPos pos, final IBlockState state, final Block neighborBlock) {
        super.onNeighborBlockChange(worldIn, pos, state, neighborBlock);
        if (worldIn.getBlockState(pos.up()).getBlock().getMaterial().isSolid()) {
            worldIn.setBlockState(pos, Blocks.dirt.getDefaultState());
        }
    }
    
    @Override
    public boolean shouldSideBeRendered(final IBlockAccess worldIn, final BlockPos pos, final EnumFacing side) {
        switch (side) {
            case UP: {
                return true;
            }
            case NORTH:
            case SOUTH:
            case WEST:
            case EAST: {
                final Block block = worldIn.getBlockState(pos).getBlock();
                return !block.isOpaqueCube() && block != Blocks.farmland;
            }
            default: {
                return super.shouldSideBeRendered(worldIn, pos, side);
            }
        }
    }
    
    @Override
    public Item getItemDropped(final IBlockState state, final Random rand, final int fortune) {
        return Blocks.dirt.getItemDropped(Blocks.dirt.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.DIRT), rand, fortune);
    }
    
    @Override
    public Item getItem(final World worldIn, final BlockPos pos) {
        return Item.getItemFromBlock(Blocks.dirt);
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        return this.getDefaultState().withProperty((IProperty<Comparable>)BlockFarmland.MOISTURE, meta & 0x7);
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        return state.getValue((IProperty<Integer>)BlockFarmland.MOISTURE);
    }
    
    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[] { BlockFarmland.MOISTURE });
    }
}
