// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import net.minecraft.util.IStringSerializable;
import net.minecraft.block.state.BlockState;
import java.util.Arrays;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.block.material.MapColor;
import net.minecraft.world.Explosion;
import net.minecraft.init.Blocks;
import net.minecraft.util.Vec3;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.entity.player.EntityPlayer;
import java.util.Random;
import net.minecraft.entity.Entity;
import java.util.List;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.block.properties.IProperty;
import com.google.common.base.Predicate;
import net.minecraft.util.EnumFacing;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.properties.PropertyDirection;

public class BlockStairs extends Block
{
    public static final PropertyDirection FACING;
    public static final PropertyEnum<EnumHalf> HALF;
    public static final PropertyEnum<EnumShape> SHAPE;
    private static final int[][] field_150150_a;
    private final Block modelBlock;
    private final IBlockState modelState;
    private boolean hasRaytraced;
    private int rayTracePass;
    
    static {
        FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
        HALF = PropertyEnum.create("half", EnumHalf.class);
        SHAPE = PropertyEnum.create("shape", EnumShape.class);
        field_150150_a = new int[][] { { 4, 5 }, { 5, 7 }, { 6, 7 }, { 4, 6 }, { 0, 1 }, { 1, 3 }, { 2, 3 }, { 0, 2 } };
    }
    
    protected BlockStairs(final IBlockState modelState) {
        super(modelState.getBlock().blockMaterial);
        this.setDefaultState(this.blockState.getBaseState().withProperty((IProperty<Comparable>)BlockStairs.FACING, EnumFacing.NORTH).withProperty(BlockStairs.HALF, EnumHalf.BOTTOM).withProperty(BlockStairs.SHAPE, EnumShape.STRAIGHT));
        this.modelBlock = modelState.getBlock();
        this.modelState = modelState;
        this.setHardness(this.modelBlock.blockHardness);
        this.setResistance(this.modelBlock.blockResistance / 3.0f);
        this.setStepSound(this.modelBlock.stepSound);
        this.setLightOpacity(255);
        this.setCreativeTab(CreativeTabs.tabBlock);
    }
    
    @Override
    public void setBlockBoundsBasedOnState(final IBlockAccess worldIn, final BlockPos pos) {
        if (this.hasRaytraced) {
            this.setBlockBounds(0.5f * (this.rayTracePass % 2), 0.5f * (this.rayTracePass / 4 % 2), 0.5f * (this.rayTracePass / 2 % 2), 0.5f + 0.5f * (this.rayTracePass % 2), 0.5f + 0.5f * (this.rayTracePass / 4 % 2), 0.5f + 0.5f * (this.rayTracePass / 2 % 2));
        }
        else {
            this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
        }
    }
    
    @Override
    public boolean isOpaqueCube() {
        return false;
    }
    
    @Override
    public boolean isFullCube() {
        return false;
    }
    
    public void setBaseCollisionBounds(final IBlockAccess worldIn, final BlockPos pos) {
        if (worldIn.getBlockState(pos).getValue(BlockStairs.HALF) == EnumHalf.TOP) {
            this.setBlockBounds(0.0f, 0.5f, 0.0f, 1.0f, 1.0f, 1.0f);
        }
        else {
            this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 0.5f, 1.0f);
        }
    }
    
    public static boolean isBlockStairs(final Block blockIn) {
        return blockIn instanceof BlockStairs;
    }
    
    public static boolean isSameStair(final IBlockAccess worldIn, final BlockPos pos, final IBlockState state) {
        final IBlockState iblockstate = worldIn.getBlockState(pos);
        final Block block = iblockstate.getBlock();
        return isBlockStairs(block) && iblockstate.getValue(BlockStairs.HALF) == state.getValue(BlockStairs.HALF) && iblockstate.getValue((IProperty<Comparable>)BlockStairs.FACING) == state.getValue((IProperty<Comparable>)BlockStairs.FACING);
    }
    
    public int func_176307_f(final IBlockAccess blockAccess, final BlockPos pos) {
        final IBlockState iblockstate = blockAccess.getBlockState(pos);
        final EnumFacing enumfacing = iblockstate.getValue((IProperty<EnumFacing>)BlockStairs.FACING);
        final EnumHalf blockstairs$enumhalf = iblockstate.getValue(BlockStairs.HALF);
        final boolean flag = blockstairs$enumhalf == EnumHalf.TOP;
        if (enumfacing == EnumFacing.EAST) {
            final IBlockState iblockstate2 = blockAccess.getBlockState(pos.east());
            final Block block = iblockstate2.getBlock();
            if (isBlockStairs(block) && blockstairs$enumhalf == iblockstate2.getValue(BlockStairs.HALF)) {
                final EnumFacing enumfacing2 = iblockstate2.getValue((IProperty<EnumFacing>)BlockStairs.FACING);
                if (enumfacing2 == EnumFacing.NORTH && !isSameStair(blockAccess, pos.south(), iblockstate)) {
                    return flag ? 1 : 2;
                }
                if (enumfacing2 == EnumFacing.SOUTH && !isSameStair(blockAccess, pos.north(), iblockstate)) {
                    return flag ? 2 : 1;
                }
            }
        }
        else if (enumfacing == EnumFacing.WEST) {
            final IBlockState iblockstate3 = blockAccess.getBlockState(pos.west());
            final Block block2 = iblockstate3.getBlock();
            if (isBlockStairs(block2) && blockstairs$enumhalf == iblockstate3.getValue(BlockStairs.HALF)) {
                final EnumFacing enumfacing3 = iblockstate3.getValue((IProperty<EnumFacing>)BlockStairs.FACING);
                if (enumfacing3 == EnumFacing.NORTH && !isSameStair(blockAccess, pos.south(), iblockstate)) {
                    return flag ? 2 : 1;
                }
                if (enumfacing3 == EnumFacing.SOUTH && !isSameStair(blockAccess, pos.north(), iblockstate)) {
                    return flag ? 1 : 2;
                }
            }
        }
        else if (enumfacing == EnumFacing.SOUTH) {
            final IBlockState iblockstate4 = blockAccess.getBlockState(pos.south());
            final Block block3 = iblockstate4.getBlock();
            if (isBlockStairs(block3) && blockstairs$enumhalf == iblockstate4.getValue(BlockStairs.HALF)) {
                final EnumFacing enumfacing4 = iblockstate4.getValue((IProperty<EnumFacing>)BlockStairs.FACING);
                if (enumfacing4 == EnumFacing.WEST && !isSameStair(blockAccess, pos.east(), iblockstate)) {
                    return flag ? 2 : 1;
                }
                if (enumfacing4 == EnumFacing.EAST && !isSameStair(blockAccess, pos.west(), iblockstate)) {
                    return flag ? 1 : 2;
                }
            }
        }
        else if (enumfacing == EnumFacing.NORTH) {
            final IBlockState iblockstate5 = blockAccess.getBlockState(pos.north());
            final Block block4 = iblockstate5.getBlock();
            if (isBlockStairs(block4) && blockstairs$enumhalf == iblockstate5.getValue(BlockStairs.HALF)) {
                final EnumFacing enumfacing5 = iblockstate5.getValue((IProperty<EnumFacing>)BlockStairs.FACING);
                if (enumfacing5 == EnumFacing.WEST && !isSameStair(blockAccess, pos.east(), iblockstate)) {
                    return flag ? 1 : 2;
                }
                if (enumfacing5 == EnumFacing.EAST && !isSameStair(blockAccess, pos.west(), iblockstate)) {
                    return flag ? 2 : 1;
                }
            }
        }
        return 0;
    }
    
    public int func_176305_g(final IBlockAccess blockAccess, final BlockPos pos) {
        final IBlockState iblockstate = blockAccess.getBlockState(pos);
        final EnumFacing enumfacing = iblockstate.getValue((IProperty<EnumFacing>)BlockStairs.FACING);
        final EnumHalf blockstairs$enumhalf = iblockstate.getValue(BlockStairs.HALF);
        final boolean flag = blockstairs$enumhalf == EnumHalf.TOP;
        if (enumfacing == EnumFacing.EAST) {
            final IBlockState iblockstate2 = blockAccess.getBlockState(pos.west());
            final Block block = iblockstate2.getBlock();
            if (isBlockStairs(block) && blockstairs$enumhalf == iblockstate2.getValue(BlockStairs.HALF)) {
                final EnumFacing enumfacing2 = iblockstate2.getValue((IProperty<EnumFacing>)BlockStairs.FACING);
                if (enumfacing2 == EnumFacing.NORTH && !isSameStair(blockAccess, pos.north(), iblockstate)) {
                    return flag ? 1 : 2;
                }
                if (enumfacing2 == EnumFacing.SOUTH && !isSameStair(blockAccess, pos.south(), iblockstate)) {
                    return flag ? 2 : 1;
                }
            }
        }
        else if (enumfacing == EnumFacing.WEST) {
            final IBlockState iblockstate3 = blockAccess.getBlockState(pos.east());
            final Block block2 = iblockstate3.getBlock();
            if (isBlockStairs(block2) && blockstairs$enumhalf == iblockstate3.getValue(BlockStairs.HALF)) {
                final EnumFacing enumfacing3 = iblockstate3.getValue((IProperty<EnumFacing>)BlockStairs.FACING);
                if (enumfacing3 == EnumFacing.NORTH && !isSameStair(blockAccess, pos.north(), iblockstate)) {
                    return flag ? 2 : 1;
                }
                if (enumfacing3 == EnumFacing.SOUTH && !isSameStair(blockAccess, pos.south(), iblockstate)) {
                    return flag ? 1 : 2;
                }
            }
        }
        else if (enumfacing == EnumFacing.SOUTH) {
            final IBlockState iblockstate4 = blockAccess.getBlockState(pos.north());
            final Block block3 = iblockstate4.getBlock();
            if (isBlockStairs(block3) && blockstairs$enumhalf == iblockstate4.getValue(BlockStairs.HALF)) {
                final EnumFacing enumfacing4 = iblockstate4.getValue((IProperty<EnumFacing>)BlockStairs.FACING);
                if (enumfacing4 == EnumFacing.WEST && !isSameStair(blockAccess, pos.west(), iblockstate)) {
                    return flag ? 2 : 1;
                }
                if (enumfacing4 == EnumFacing.EAST && !isSameStair(blockAccess, pos.east(), iblockstate)) {
                    return flag ? 1 : 2;
                }
            }
        }
        else if (enumfacing == EnumFacing.NORTH) {
            final IBlockState iblockstate5 = blockAccess.getBlockState(pos.south());
            final Block block4 = iblockstate5.getBlock();
            if (isBlockStairs(block4) && blockstairs$enumhalf == iblockstate5.getValue(BlockStairs.HALF)) {
                final EnumFacing enumfacing5 = iblockstate5.getValue((IProperty<EnumFacing>)BlockStairs.FACING);
                if (enumfacing5 == EnumFacing.WEST && !isSameStair(blockAccess, pos.west(), iblockstate)) {
                    return flag ? 1 : 2;
                }
                if (enumfacing5 == EnumFacing.EAST && !isSameStair(blockAccess, pos.east(), iblockstate)) {
                    return flag ? 2 : 1;
                }
            }
        }
        return 0;
    }
    
    public boolean func_176306_h(final IBlockAccess blockAccess, final BlockPos pos) {
        final IBlockState iblockstate = blockAccess.getBlockState(pos);
        final EnumFacing enumfacing = iblockstate.getValue((IProperty<EnumFacing>)BlockStairs.FACING);
        final EnumHalf blockstairs$enumhalf = iblockstate.getValue(BlockStairs.HALF);
        final boolean flag = blockstairs$enumhalf == EnumHalf.TOP;
        float f = 0.5f;
        float f2 = 1.0f;
        if (flag) {
            f = 0.0f;
            f2 = 0.5f;
        }
        float f3 = 0.0f;
        float f4 = 1.0f;
        float f5 = 0.0f;
        float f6 = 0.5f;
        boolean flag2 = true;
        if (enumfacing == EnumFacing.EAST) {
            f3 = 0.5f;
            f6 = 1.0f;
            final IBlockState iblockstate2 = blockAccess.getBlockState(pos.east());
            final Block block = iblockstate2.getBlock();
            if (isBlockStairs(block) && blockstairs$enumhalf == iblockstate2.getValue(BlockStairs.HALF)) {
                final EnumFacing enumfacing2 = iblockstate2.getValue((IProperty<EnumFacing>)BlockStairs.FACING);
                if (enumfacing2 == EnumFacing.NORTH && !isSameStair(blockAccess, pos.south(), iblockstate)) {
                    f6 = 0.5f;
                    flag2 = false;
                }
                else if (enumfacing2 == EnumFacing.SOUTH && !isSameStair(blockAccess, pos.north(), iblockstate)) {
                    f5 = 0.5f;
                    flag2 = false;
                }
            }
        }
        else if (enumfacing == EnumFacing.WEST) {
            f4 = 0.5f;
            f6 = 1.0f;
            final IBlockState iblockstate3 = blockAccess.getBlockState(pos.west());
            final Block block2 = iblockstate3.getBlock();
            if (isBlockStairs(block2) && blockstairs$enumhalf == iblockstate3.getValue(BlockStairs.HALF)) {
                final EnumFacing enumfacing3 = iblockstate3.getValue((IProperty<EnumFacing>)BlockStairs.FACING);
                if (enumfacing3 == EnumFacing.NORTH && !isSameStair(blockAccess, pos.south(), iblockstate)) {
                    f6 = 0.5f;
                    flag2 = false;
                }
                else if (enumfacing3 == EnumFacing.SOUTH && !isSameStair(blockAccess, pos.north(), iblockstate)) {
                    f5 = 0.5f;
                    flag2 = false;
                }
            }
        }
        else if (enumfacing == EnumFacing.SOUTH) {
            f5 = 0.5f;
            f6 = 1.0f;
            final IBlockState iblockstate4 = blockAccess.getBlockState(pos.south());
            final Block block3 = iblockstate4.getBlock();
            if (isBlockStairs(block3) && blockstairs$enumhalf == iblockstate4.getValue(BlockStairs.HALF)) {
                final EnumFacing enumfacing4 = iblockstate4.getValue((IProperty<EnumFacing>)BlockStairs.FACING);
                if (enumfacing4 == EnumFacing.WEST && !isSameStair(blockAccess, pos.east(), iblockstate)) {
                    f4 = 0.5f;
                    flag2 = false;
                }
                else if (enumfacing4 == EnumFacing.EAST && !isSameStair(blockAccess, pos.west(), iblockstate)) {
                    f3 = 0.5f;
                    flag2 = false;
                }
            }
        }
        else if (enumfacing == EnumFacing.NORTH) {
            final IBlockState iblockstate5 = blockAccess.getBlockState(pos.north());
            final Block block4 = iblockstate5.getBlock();
            if (isBlockStairs(block4) && blockstairs$enumhalf == iblockstate5.getValue(BlockStairs.HALF)) {
                final EnumFacing enumfacing5 = iblockstate5.getValue((IProperty<EnumFacing>)BlockStairs.FACING);
                if (enumfacing5 == EnumFacing.WEST && !isSameStair(blockAccess, pos.east(), iblockstate)) {
                    f4 = 0.5f;
                    flag2 = false;
                }
                else if (enumfacing5 == EnumFacing.EAST && !isSameStair(blockAccess, pos.west(), iblockstate)) {
                    f3 = 0.5f;
                    flag2 = false;
                }
            }
        }
        this.setBlockBounds(f3, f, f5, f4, f2, f6);
        return flag2;
    }
    
    public boolean func_176304_i(final IBlockAccess blockAccess, final BlockPos pos) {
        final IBlockState iblockstate = blockAccess.getBlockState(pos);
        final EnumFacing enumfacing = iblockstate.getValue((IProperty<EnumFacing>)BlockStairs.FACING);
        final EnumHalf blockstairs$enumhalf = iblockstate.getValue(BlockStairs.HALF);
        final boolean flag = blockstairs$enumhalf == EnumHalf.TOP;
        float f = 0.5f;
        float f2 = 1.0f;
        if (flag) {
            f = 0.0f;
            f2 = 0.5f;
        }
        float f3 = 0.0f;
        float f4 = 0.5f;
        float f5 = 0.5f;
        float f6 = 1.0f;
        boolean flag2 = false;
        if (enumfacing == EnumFacing.EAST) {
            final IBlockState iblockstate2 = blockAccess.getBlockState(pos.west());
            final Block block = iblockstate2.getBlock();
            if (isBlockStairs(block) && blockstairs$enumhalf == iblockstate2.getValue(BlockStairs.HALF)) {
                final EnumFacing enumfacing2 = iblockstate2.getValue((IProperty<EnumFacing>)BlockStairs.FACING);
                if (enumfacing2 == EnumFacing.NORTH && !isSameStair(blockAccess, pos.north(), iblockstate)) {
                    f5 = 0.0f;
                    f6 = 0.5f;
                    flag2 = true;
                }
                else if (enumfacing2 == EnumFacing.SOUTH && !isSameStair(blockAccess, pos.south(), iblockstate)) {
                    f5 = 0.5f;
                    f6 = 1.0f;
                    flag2 = true;
                }
            }
        }
        else if (enumfacing == EnumFacing.WEST) {
            final IBlockState iblockstate3 = blockAccess.getBlockState(pos.east());
            final Block block2 = iblockstate3.getBlock();
            if (isBlockStairs(block2) && blockstairs$enumhalf == iblockstate3.getValue(BlockStairs.HALF)) {
                f3 = 0.5f;
                f4 = 1.0f;
                final EnumFacing enumfacing3 = iblockstate3.getValue((IProperty<EnumFacing>)BlockStairs.FACING);
                if (enumfacing3 == EnumFacing.NORTH && !isSameStair(blockAccess, pos.north(), iblockstate)) {
                    f5 = 0.0f;
                    f6 = 0.5f;
                    flag2 = true;
                }
                else if (enumfacing3 == EnumFacing.SOUTH && !isSameStair(blockAccess, pos.south(), iblockstate)) {
                    f5 = 0.5f;
                    f6 = 1.0f;
                    flag2 = true;
                }
            }
        }
        else if (enumfacing == EnumFacing.SOUTH) {
            final IBlockState iblockstate4 = blockAccess.getBlockState(pos.north());
            final Block block3 = iblockstate4.getBlock();
            if (isBlockStairs(block3) && blockstairs$enumhalf == iblockstate4.getValue(BlockStairs.HALF)) {
                f5 = 0.0f;
                f6 = 0.5f;
                final EnumFacing enumfacing4 = iblockstate4.getValue((IProperty<EnumFacing>)BlockStairs.FACING);
                if (enumfacing4 == EnumFacing.WEST && !isSameStair(blockAccess, pos.west(), iblockstate)) {
                    flag2 = true;
                }
                else if (enumfacing4 == EnumFacing.EAST && !isSameStair(blockAccess, pos.east(), iblockstate)) {
                    f3 = 0.5f;
                    f4 = 1.0f;
                    flag2 = true;
                }
            }
        }
        else if (enumfacing == EnumFacing.NORTH) {
            final IBlockState iblockstate5 = blockAccess.getBlockState(pos.south());
            final Block block4 = iblockstate5.getBlock();
            if (isBlockStairs(block4) && blockstairs$enumhalf == iblockstate5.getValue(BlockStairs.HALF)) {
                final EnumFacing enumfacing5 = iblockstate5.getValue((IProperty<EnumFacing>)BlockStairs.FACING);
                if (enumfacing5 == EnumFacing.WEST && !isSameStair(blockAccess, pos.west(), iblockstate)) {
                    flag2 = true;
                }
                else if (enumfacing5 == EnumFacing.EAST && !isSameStair(blockAccess, pos.east(), iblockstate)) {
                    f3 = 0.5f;
                    f4 = 1.0f;
                    flag2 = true;
                }
            }
        }
        if (flag2) {
            this.setBlockBounds(f3, f, f5, f4, f2, f6);
        }
        return flag2;
    }
    
    @Override
    public void addCollisionBoxesToList(final World worldIn, final BlockPos pos, final IBlockState state, final AxisAlignedBB mask, final List<AxisAlignedBB> list, final Entity collidingEntity) {
        this.setBaseCollisionBounds(worldIn, pos);
        super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
        final boolean flag = this.func_176306_h(worldIn, pos);
        super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
        if (flag && this.func_176304_i(worldIn, pos)) {
            super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
        }
        this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
    }
    
    @Override
    public void randomDisplayTick(final World worldIn, final BlockPos pos, final IBlockState state, final Random rand) {
        this.modelBlock.randomDisplayTick(worldIn, pos, state, rand);
    }
    
    @Override
    public void onBlockClicked(final World worldIn, final BlockPos pos, final EntityPlayer playerIn) {
        this.modelBlock.onBlockClicked(worldIn, pos, playerIn);
    }
    
    @Override
    public void onBlockDestroyedByPlayer(final World worldIn, final BlockPos pos, final IBlockState state) {
        this.modelBlock.onBlockDestroyedByPlayer(worldIn, pos, state);
    }
    
    @Override
    public int getMixedBrightnessForBlock(final IBlockAccess worldIn, final BlockPos pos) {
        return this.modelBlock.getMixedBrightnessForBlock(worldIn, pos);
    }
    
    @Override
    public float getExplosionResistance(final Entity exploder) {
        return this.modelBlock.getExplosionResistance(exploder);
    }
    
    @Override
    public EnumWorldBlockLayer getBlockLayer() {
        return this.modelBlock.getBlockLayer();
    }
    
    @Override
    public int tickRate(final World worldIn) {
        return this.modelBlock.tickRate(worldIn);
    }
    
    @Override
    public AxisAlignedBB getSelectedBoundingBox(final World worldIn, final BlockPos pos) {
        return this.modelBlock.getSelectedBoundingBox(worldIn, pos);
    }
    
    @Override
    public Vec3 modifyAcceleration(final World worldIn, final BlockPos pos, final Entity entityIn, final Vec3 motion) {
        return this.modelBlock.modifyAcceleration(worldIn, pos, entityIn, motion);
    }
    
    @Override
    public boolean isCollidable() {
        return this.modelBlock.isCollidable();
    }
    
    @Override
    public boolean canCollideCheck(final IBlockState state, final boolean hitIfLiquid) {
        return this.modelBlock.canCollideCheck(state, hitIfLiquid);
    }
    
    @Override
    public boolean canPlaceBlockAt(final World worldIn, final BlockPos pos) {
        return this.modelBlock.canPlaceBlockAt(worldIn, pos);
    }
    
    @Override
    public void onBlockAdded(final World worldIn, final BlockPos pos, final IBlockState state) {
        this.onNeighborBlockChange(worldIn, pos, this.modelState, Blocks.air);
        this.modelBlock.onBlockAdded(worldIn, pos, this.modelState);
    }
    
    @Override
    public void breakBlock(final World worldIn, final BlockPos pos, final IBlockState state) {
        this.modelBlock.breakBlock(worldIn, pos, this.modelState);
    }
    
    @Override
    public void onEntityCollidedWithBlock(final World worldIn, final BlockPos pos, final Entity entityIn) {
        this.modelBlock.onEntityCollidedWithBlock(worldIn, pos, entityIn);
    }
    
    @Override
    public void updateTick(final World worldIn, final BlockPos pos, final IBlockState state, final Random rand) {
        this.modelBlock.updateTick(worldIn, pos, state, rand);
    }
    
    @Override
    public boolean onBlockActivated(final World worldIn, final BlockPos pos, final IBlockState state, final EntityPlayer playerIn, final EnumFacing side, final float hitX, final float hitY, final float hitZ) {
        return this.modelBlock.onBlockActivated(worldIn, pos, this.modelState, playerIn, EnumFacing.DOWN, 0.0f, 0.0f, 0.0f);
    }
    
    @Override
    public void onBlockDestroyedByExplosion(final World worldIn, final BlockPos pos, final Explosion explosionIn) {
        this.modelBlock.onBlockDestroyedByExplosion(worldIn, pos, explosionIn);
    }
    
    @Override
    public MapColor getMapColor(final IBlockState state) {
        return this.modelBlock.getMapColor(this.modelState);
    }
    
    @Override
    public IBlockState onBlockPlaced(final World worldIn, final BlockPos pos, final EnumFacing facing, final float hitX, final float hitY, final float hitZ, final int meta, final EntityLivingBase placer) {
        IBlockState iblockstate = super.onBlockPlaced(worldIn, pos, facing, hitX, hitY, hitZ, meta, placer);
        iblockstate = iblockstate.withProperty((IProperty<Comparable>)BlockStairs.FACING, placer.getHorizontalFacing()).withProperty(BlockStairs.SHAPE, EnumShape.STRAIGHT);
        return (facing != EnumFacing.DOWN && (facing == EnumFacing.UP || hitY <= 0.5)) ? iblockstate.withProperty(BlockStairs.HALF, EnumHalf.BOTTOM) : iblockstate.withProperty(BlockStairs.HALF, EnumHalf.TOP);
    }
    
    @Override
    public MovingObjectPosition collisionRayTrace(final World worldIn, final BlockPos pos, final Vec3 start, final Vec3 end) {
        final MovingObjectPosition[] amovingobjectposition = new MovingObjectPosition[8];
        final IBlockState iblockstate = worldIn.getBlockState(pos);
        final int i = iblockstate.getValue((IProperty<EnumFacing>)BlockStairs.FACING).getHorizontalIndex();
        final boolean flag = iblockstate.getValue(BlockStairs.HALF) == EnumHalf.TOP;
        final int[] aint = BlockStairs.field_150150_a[i + (flag ? 4 : 0)];
        this.hasRaytraced = true;
        for (int j = 0; j < 8; ++j) {
            this.rayTracePass = j;
            if (Arrays.binarySearch(aint, j) < 0) {
                amovingobjectposition[j] = super.collisionRayTrace(worldIn, pos, start, end);
            }
        }
        int[] array;
        for (int length = (array = aint).length, l = 0; l < length; ++l) {
            final int k = array[l];
            amovingobjectposition[k] = null;
        }
        MovingObjectPosition movingobjectposition1 = null;
        double d1 = 0.0;
        MovingObjectPosition[] array2;
        for (int length2 = (array2 = amovingobjectposition).length, n = 0; n < length2; ++n) {
            final MovingObjectPosition movingobjectposition2 = array2[n];
            if (movingobjectposition2 != null) {
                final double d2 = movingobjectposition2.hitVec.squareDistanceTo(end);
                if (d2 > d1) {
                    movingobjectposition1 = movingobjectposition2;
                    d1 = d2;
                }
            }
        }
        return movingobjectposition1;
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        IBlockState iblockstate = this.getDefaultState().withProperty(BlockStairs.HALF, ((meta & 0x4) > 0) ? EnumHalf.TOP : EnumHalf.BOTTOM);
        iblockstate = iblockstate.withProperty((IProperty<Comparable>)BlockStairs.FACING, EnumFacing.getFront(5 - (meta & 0x3)));
        return iblockstate;
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        int i = 0;
        if (state.getValue(BlockStairs.HALF) == EnumHalf.TOP) {
            i |= 0x4;
        }
        i |= 5 - state.getValue((IProperty<EnumFacing>)BlockStairs.FACING).getIndex();
        return i;
    }
    
    @Override
    public IBlockState getActualState(IBlockState state, final IBlockAccess worldIn, final BlockPos pos) {
        if (this.func_176306_h(worldIn, pos)) {
            switch (this.func_176305_g(worldIn, pos)) {
                case 0: {
                    state = state.withProperty(BlockStairs.SHAPE, EnumShape.STRAIGHT);
                    break;
                }
                case 1: {
                    state = state.withProperty(BlockStairs.SHAPE, EnumShape.INNER_RIGHT);
                    break;
                }
                case 2: {
                    state = state.withProperty(BlockStairs.SHAPE, EnumShape.INNER_LEFT);
                    break;
                }
            }
        }
        else {
            switch (this.func_176307_f(worldIn, pos)) {
                case 0: {
                    state = state.withProperty(BlockStairs.SHAPE, EnumShape.STRAIGHT);
                    break;
                }
                case 1: {
                    state = state.withProperty(BlockStairs.SHAPE, EnumShape.OUTER_RIGHT);
                    break;
                }
                case 2: {
                    state = state.withProperty(BlockStairs.SHAPE, EnumShape.OUTER_LEFT);
                    break;
                }
            }
        }
        return state;
    }
    
    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[] { BlockStairs.FACING, BlockStairs.HALF, BlockStairs.SHAPE });
    }
    
    public enum EnumHalf implements IStringSerializable
    {
        TOP("TOP", 0, "top"), 
        BOTTOM("BOTTOM", 1, "bottom");
        
        private final String name;
        
        private EnumHalf(final String name2, final int ordinal, final String name) {
            this.name = name;
        }
        
        @Override
        public String toString() {
            return this.name;
        }
        
        @Override
        public String getName() {
            return this.name;
        }
    }
    
    public enum EnumShape implements IStringSerializable
    {
        STRAIGHT("STRAIGHT", 0, "straight"), 
        INNER_LEFT("INNER_LEFT", 1, "inner_left"), 
        INNER_RIGHT("INNER_RIGHT", 2, "inner_right"), 
        OUTER_LEFT("OUTER_LEFT", 3, "outer_left"), 
        OUTER_RIGHT("OUTER_RIGHT", 4, "outer_right");
        
        private final String name;
        
        private EnumShape(final String name2, final int ordinal, final String name) {
            this.name = name;
        }
        
        @Override
        public String toString() {
            return this.name;
        }
        
        @Override
        public String getName() {
            return this.name;
        }
    }
}
