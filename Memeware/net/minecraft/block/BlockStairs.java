package net.minecraft.block;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockStairs extends Block {
    public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
    public static final PropertyEnum HALF = PropertyEnum.create("half", BlockStairs.EnumHalf.class);
    public static final PropertyEnum SHAPE = PropertyEnum.create("shape", BlockStairs.EnumShape.class);
    private static final int[][] field_150150_a = new int[][]{{4, 5}, {5, 7}, {6, 7}, {4, 6}, {0, 1}, {1, 3}, {2, 3}, {0, 2}};
    private final Block modelBlock;
    private final IBlockState modelState;
    private boolean field_150152_N;
    private int field_150153_O;
    private static final String __OBFID = "CL_00000314";

    protected BlockStairs(IBlockState modelState) {
        super(modelState.getBlock().blockMaterial);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(HALF, BlockStairs.EnumHalf.BOTTOM).withProperty(SHAPE, BlockStairs.EnumShape.STRAIGHT));
        this.modelBlock = modelState.getBlock();
        this.modelState = modelState;
        this.setHardness(this.modelBlock.blockHardness);
        this.setResistance(this.modelBlock.blockResistance / 3.0F);
        this.setStepSound(this.modelBlock.stepSound);
        this.setLightOpacity(255);
        this.setCreativeTab(CreativeTabs.tabBlock);
    }

    public void setBlockBoundsBasedOnState(IBlockAccess access, BlockPos pos) {
        if (this.field_150152_N) {
            this.setBlockBounds(0.5F * (float) (this.field_150153_O % 2), 0.5F * (float) (this.field_150153_O / 4 % 2), 0.5F * (float) (this.field_150153_O / 2 % 2), 0.5F + 0.5F * (float) (this.field_150153_O % 2), 0.5F + 0.5F * (float) (this.field_150153_O / 4 % 2), 0.5F + 0.5F * (float) (this.field_150153_O / 2 % 2));
        } else {
            this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        }
    }

    public boolean isOpaqueCube() {
        return false;
    }

    public boolean isFullCube() {
        return false;
    }

    /**
     * Set the block bounds as the collision bounds for the stairs at the given position
     */
    public void setBaseCollisionBounds(IBlockAccess worldIn, BlockPos pos) {
        if (worldIn.getBlockState(pos).getValue(HALF) == BlockStairs.EnumHalf.TOP) {
            this.setBlockBounds(0.0F, 0.5F, 0.0F, 1.0F, 1.0F, 1.0F);
        } else {
            this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.5F, 1.0F);
        }
    }

    /**
     * Checks if a block is stairs
     */
    public static boolean isBlockStairs(Block p_150148_0_) {
        return p_150148_0_ instanceof BlockStairs;
    }

    /**
     * Check whether there is a stair block at the given position and it has the same properties as the given BlockState
     */
    public static boolean isSameStair(IBlockAccess worldIn, BlockPos pos, IBlockState state) {
        IBlockState var3 = worldIn.getBlockState(pos);
        Block var4 = var3.getBlock();
        return isBlockStairs(var4) && var3.getValue(HALF) == state.getValue(HALF) && var3.getValue(FACING) == state.getValue(FACING);
    }

    public int func_176307_f(IBlockAccess p_176307_1_, BlockPos p_176307_2_) {
        IBlockState var3 = p_176307_1_.getBlockState(p_176307_2_);
        EnumFacing var4 = (EnumFacing) var3.getValue(FACING);
        BlockStairs.EnumHalf var5 = (BlockStairs.EnumHalf) var3.getValue(HALF);
        boolean var6 = var5 == BlockStairs.EnumHalf.TOP;
        IBlockState var7;
        Block var8;
        EnumFacing var9;

        if (var4 == EnumFacing.EAST) {
            var7 = p_176307_1_.getBlockState(p_176307_2_.offsetEast());
            var8 = var7.getBlock();

            if (isBlockStairs(var8) && var5 == var7.getValue(HALF)) {
                var9 = (EnumFacing) var7.getValue(FACING);

                if (var9 == EnumFacing.NORTH && !isSameStair(p_176307_1_, p_176307_2_.offsetSouth(), var3)) {
                    return var6 ? 1 : 2;
                }

                if (var9 == EnumFacing.SOUTH && !isSameStair(p_176307_1_, p_176307_2_.offsetNorth(), var3)) {
                    return var6 ? 2 : 1;
                }
            }
        } else if (var4 == EnumFacing.WEST) {
            var7 = p_176307_1_.getBlockState(p_176307_2_.offsetWest());
            var8 = var7.getBlock();

            if (isBlockStairs(var8) && var5 == var7.getValue(HALF)) {
                var9 = (EnumFacing) var7.getValue(FACING);

                if (var9 == EnumFacing.NORTH && !isSameStair(p_176307_1_, p_176307_2_.offsetSouth(), var3)) {
                    return var6 ? 2 : 1;
                }

                if (var9 == EnumFacing.SOUTH && !isSameStair(p_176307_1_, p_176307_2_.offsetNorth(), var3)) {
                    return var6 ? 1 : 2;
                }
            }
        } else if (var4 == EnumFacing.SOUTH) {
            var7 = p_176307_1_.getBlockState(p_176307_2_.offsetSouth());
            var8 = var7.getBlock();

            if (isBlockStairs(var8) && var5 == var7.getValue(HALF)) {
                var9 = (EnumFacing) var7.getValue(FACING);

                if (var9 == EnumFacing.WEST && !isSameStair(p_176307_1_, p_176307_2_.offsetEast(), var3)) {
                    return var6 ? 2 : 1;
                }

                if (var9 == EnumFacing.EAST && !isSameStair(p_176307_1_, p_176307_2_.offsetWest(), var3)) {
                    return var6 ? 1 : 2;
                }
            }
        } else if (var4 == EnumFacing.NORTH) {
            var7 = p_176307_1_.getBlockState(p_176307_2_.offsetNorth());
            var8 = var7.getBlock();

            if (isBlockStairs(var8) && var5 == var7.getValue(HALF)) {
                var9 = (EnumFacing) var7.getValue(FACING);

                if (var9 == EnumFacing.WEST && !isSameStair(p_176307_1_, p_176307_2_.offsetEast(), var3)) {
                    return var6 ? 1 : 2;
                }

                if (var9 == EnumFacing.EAST && !isSameStair(p_176307_1_, p_176307_2_.offsetWest(), var3)) {
                    return var6 ? 2 : 1;
                }
            }
        }

        return 0;
    }

    public int func_176305_g(IBlockAccess p_176305_1_, BlockPos p_176305_2_) {
        IBlockState var3 = p_176305_1_.getBlockState(p_176305_2_);
        EnumFacing var4 = (EnumFacing) var3.getValue(FACING);
        BlockStairs.EnumHalf var5 = (BlockStairs.EnumHalf) var3.getValue(HALF);
        boolean var6 = var5 == BlockStairs.EnumHalf.TOP;
        IBlockState var7;
        Block var8;
        EnumFacing var9;

        if (var4 == EnumFacing.EAST) {
            var7 = p_176305_1_.getBlockState(p_176305_2_.offsetWest());
            var8 = var7.getBlock();

            if (isBlockStairs(var8) && var5 == var7.getValue(HALF)) {
                var9 = (EnumFacing) var7.getValue(FACING);

                if (var9 == EnumFacing.NORTH && !isSameStair(p_176305_1_, p_176305_2_.offsetNorth(), var3)) {
                    return var6 ? 1 : 2;
                }

                if (var9 == EnumFacing.SOUTH && !isSameStair(p_176305_1_, p_176305_2_.offsetSouth(), var3)) {
                    return var6 ? 2 : 1;
                }
            }
        } else if (var4 == EnumFacing.WEST) {
            var7 = p_176305_1_.getBlockState(p_176305_2_.offsetEast());
            var8 = var7.getBlock();

            if (isBlockStairs(var8) && var5 == var7.getValue(HALF)) {
                var9 = (EnumFacing) var7.getValue(FACING);

                if (var9 == EnumFacing.NORTH && !isSameStair(p_176305_1_, p_176305_2_.offsetNorth(), var3)) {
                    return var6 ? 2 : 1;
                }

                if (var9 == EnumFacing.SOUTH && !isSameStair(p_176305_1_, p_176305_2_.offsetSouth(), var3)) {
                    return var6 ? 1 : 2;
                }
            }
        } else if (var4 == EnumFacing.SOUTH) {
            var7 = p_176305_1_.getBlockState(p_176305_2_.offsetNorth());
            var8 = var7.getBlock();

            if (isBlockStairs(var8) && var5 == var7.getValue(HALF)) {
                var9 = (EnumFacing) var7.getValue(FACING);

                if (var9 == EnumFacing.WEST && !isSameStair(p_176305_1_, p_176305_2_.offsetWest(), var3)) {
                    return var6 ? 2 : 1;
                }

                if (var9 == EnumFacing.EAST && !isSameStair(p_176305_1_, p_176305_2_.offsetEast(), var3)) {
                    return var6 ? 1 : 2;
                }
            }
        } else if (var4 == EnumFacing.NORTH) {
            var7 = p_176305_1_.getBlockState(p_176305_2_.offsetSouth());
            var8 = var7.getBlock();

            if (isBlockStairs(var8) && var5 == var7.getValue(HALF)) {
                var9 = (EnumFacing) var7.getValue(FACING);

                if (var9 == EnumFacing.WEST && !isSameStair(p_176305_1_, p_176305_2_.offsetWest(), var3)) {
                    return var6 ? 1 : 2;
                }

                if (var9 == EnumFacing.EAST && !isSameStair(p_176305_1_, p_176305_2_.offsetEast(), var3)) {
                    return var6 ? 2 : 1;
                }
            }
        }

        return 0;
    }

    public boolean func_176306_h(IBlockAccess p_176306_1_, BlockPos p_176306_2_) {
        IBlockState var3 = p_176306_1_.getBlockState(p_176306_2_);
        EnumFacing var4 = (EnumFacing) var3.getValue(FACING);
        BlockStairs.EnumHalf var5 = (BlockStairs.EnumHalf) var3.getValue(HALF);
        boolean var6 = var5 == BlockStairs.EnumHalf.TOP;
        float var7 = 0.5F;
        float var8 = 1.0F;

        if (var6) {
            var7 = 0.0F;
            var8 = 0.5F;
        }

        float var9 = 0.0F;
        float var10 = 1.0F;
        float var11 = 0.0F;
        float var12 = 0.5F;
        boolean var13 = true;
        IBlockState var14;
        Block var15;
        EnumFacing var16;

        if (var4 == EnumFacing.EAST) {
            var9 = 0.5F;
            var12 = 1.0F;
            var14 = p_176306_1_.getBlockState(p_176306_2_.offsetEast());
            var15 = var14.getBlock();

            if (isBlockStairs(var15) && var5 == var14.getValue(HALF)) {
                var16 = (EnumFacing) var14.getValue(FACING);

                if (var16 == EnumFacing.NORTH && !isSameStair(p_176306_1_, p_176306_2_.offsetSouth(), var3)) {
                    var12 = 0.5F;
                    var13 = false;
                } else if (var16 == EnumFacing.SOUTH && !isSameStair(p_176306_1_, p_176306_2_.offsetNorth(), var3)) {
                    var11 = 0.5F;
                    var13 = false;
                }
            }
        } else if (var4 == EnumFacing.WEST) {
            var10 = 0.5F;
            var12 = 1.0F;
            var14 = p_176306_1_.getBlockState(p_176306_2_.offsetWest());
            var15 = var14.getBlock();

            if (isBlockStairs(var15) && var5 == var14.getValue(HALF)) {
                var16 = (EnumFacing) var14.getValue(FACING);

                if (var16 == EnumFacing.NORTH && !isSameStair(p_176306_1_, p_176306_2_.offsetSouth(), var3)) {
                    var12 = 0.5F;
                    var13 = false;
                } else if (var16 == EnumFacing.SOUTH && !isSameStair(p_176306_1_, p_176306_2_.offsetNorth(), var3)) {
                    var11 = 0.5F;
                    var13 = false;
                }
            }
        } else if (var4 == EnumFacing.SOUTH) {
            var11 = 0.5F;
            var12 = 1.0F;
            var14 = p_176306_1_.getBlockState(p_176306_2_.offsetSouth());
            var15 = var14.getBlock();

            if (isBlockStairs(var15) && var5 == var14.getValue(HALF)) {
                var16 = (EnumFacing) var14.getValue(FACING);

                if (var16 == EnumFacing.WEST && !isSameStair(p_176306_1_, p_176306_2_.offsetEast(), var3)) {
                    var10 = 0.5F;
                    var13 = false;
                } else if (var16 == EnumFacing.EAST && !isSameStair(p_176306_1_, p_176306_2_.offsetWest(), var3)) {
                    var9 = 0.5F;
                    var13 = false;
                }
            }
        } else if (var4 == EnumFacing.NORTH) {
            var14 = p_176306_1_.getBlockState(p_176306_2_.offsetNorth());
            var15 = var14.getBlock();

            if (isBlockStairs(var15) && var5 == var14.getValue(HALF)) {
                var16 = (EnumFacing) var14.getValue(FACING);

                if (var16 == EnumFacing.WEST && !isSameStair(p_176306_1_, p_176306_2_.offsetEast(), var3)) {
                    var10 = 0.5F;
                    var13 = false;
                } else if (var16 == EnumFacing.EAST && !isSameStair(p_176306_1_, p_176306_2_.offsetWest(), var3)) {
                    var9 = 0.5F;
                    var13 = false;
                }
            }
        }

        this.setBlockBounds(var9, var7, var11, var10, var8, var12);
        return var13;
    }

    public boolean func_176304_i(IBlockAccess p_176304_1_, BlockPos p_176304_2_) {
        IBlockState var3 = p_176304_1_.getBlockState(p_176304_2_);
        EnumFacing var4 = (EnumFacing) var3.getValue(FACING);
        BlockStairs.EnumHalf var5 = (BlockStairs.EnumHalf) var3.getValue(HALF);
        boolean var6 = var5 == BlockStairs.EnumHalf.TOP;
        float var7 = 0.5F;
        float var8 = 1.0F;

        if (var6) {
            var7 = 0.0F;
            var8 = 0.5F;
        }

        float var9 = 0.0F;
        float var10 = 0.5F;
        float var11 = 0.5F;
        float var12 = 1.0F;
        boolean var13 = false;
        IBlockState var14;
        Block var15;
        EnumFacing var16;

        if (var4 == EnumFacing.EAST) {
            var14 = p_176304_1_.getBlockState(p_176304_2_.offsetWest());
            var15 = var14.getBlock();

            if (isBlockStairs(var15) && var5 == var14.getValue(HALF)) {
                var16 = (EnumFacing) var14.getValue(FACING);

                if (var16 == EnumFacing.NORTH && !isSameStair(p_176304_1_, p_176304_2_.offsetNorth(), var3)) {
                    var11 = 0.0F;
                    var12 = 0.5F;
                    var13 = true;
                } else if (var16 == EnumFacing.SOUTH && !isSameStair(p_176304_1_, p_176304_2_.offsetSouth(), var3)) {
                    var11 = 0.5F;
                    var12 = 1.0F;
                    var13 = true;
                }
            }
        } else if (var4 == EnumFacing.WEST) {
            var14 = p_176304_1_.getBlockState(p_176304_2_.offsetEast());
            var15 = var14.getBlock();

            if (isBlockStairs(var15) && var5 == var14.getValue(HALF)) {
                var9 = 0.5F;
                var10 = 1.0F;
                var16 = (EnumFacing) var14.getValue(FACING);

                if (var16 == EnumFacing.NORTH && !isSameStair(p_176304_1_, p_176304_2_.offsetNorth(), var3)) {
                    var11 = 0.0F;
                    var12 = 0.5F;
                    var13 = true;
                } else if (var16 == EnumFacing.SOUTH && !isSameStair(p_176304_1_, p_176304_2_.offsetSouth(), var3)) {
                    var11 = 0.5F;
                    var12 = 1.0F;
                    var13 = true;
                }
            }
        } else if (var4 == EnumFacing.SOUTH) {
            var14 = p_176304_1_.getBlockState(p_176304_2_.offsetNorth());
            var15 = var14.getBlock();

            if (isBlockStairs(var15) && var5 == var14.getValue(HALF)) {
                var11 = 0.0F;
                var12 = 0.5F;
                var16 = (EnumFacing) var14.getValue(FACING);

                if (var16 == EnumFacing.WEST && !isSameStair(p_176304_1_, p_176304_2_.offsetWest(), var3)) {
                    var13 = true;
                } else if (var16 == EnumFacing.EAST && !isSameStair(p_176304_1_, p_176304_2_.offsetEast(), var3)) {
                    var9 = 0.5F;
                    var10 = 1.0F;
                    var13 = true;
                }
            }
        } else if (var4 == EnumFacing.NORTH) {
            var14 = p_176304_1_.getBlockState(p_176304_2_.offsetSouth());
            var15 = var14.getBlock();

            if (isBlockStairs(var15) && var5 == var14.getValue(HALF)) {
                var16 = (EnumFacing) var14.getValue(FACING);

                if (var16 == EnumFacing.WEST && !isSameStair(p_176304_1_, p_176304_2_.offsetWest(), var3)) {
                    var13 = true;
                } else if (var16 == EnumFacing.EAST && !isSameStair(p_176304_1_, p_176304_2_.offsetEast(), var3)) {
                    var9 = 0.5F;
                    var10 = 1.0F;
                    var13 = true;
                }
            }
        }

        if (var13) {
            this.setBlockBounds(var9, var7, var11, var10, var8, var12);
        }

        return var13;
    }

    /**
     * Add all collision boxes of this Block to the list that intersect with the given mask.
     *
     * @param collidingEntity the Entity colliding with this Block
     */
    public void addCollisionBoxesToList(World worldIn, BlockPos pos, IBlockState state, AxisAlignedBB mask, List list, Entity collidingEntity) {
        this.setBaseCollisionBounds(worldIn, pos);
        super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
        boolean var7 = this.func_176306_h(worldIn, pos);
        super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);

        if (var7 && this.func_176304_i(worldIn, pos)) {
            super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
        }

        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    }

    public void randomDisplayTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        this.modelBlock.randomDisplayTick(worldIn, pos, state, rand);
    }

    public void onBlockClicked(World worldIn, BlockPos pos, EntityPlayer playerIn) {
        this.modelBlock.onBlockClicked(worldIn, pos, playerIn);
    }

    /**
     * Called when a player destroys this Block
     */
    public void onBlockDestroyedByPlayer(World worldIn, BlockPos pos, IBlockState state) {
        this.modelBlock.onBlockDestroyedByPlayer(worldIn, pos, state);
    }

    public int getMixedBrightnessForBlock(IBlockAccess worldIn, BlockPos pos) {
        return this.modelBlock.getMixedBrightnessForBlock(worldIn, pos);
    }

    /**
     * Returns how much this block can resist explosions from the passed in entity.
     */
    public float getExplosionResistance(Entity exploder) {
        return this.modelBlock.getExplosionResistance(exploder);
    }

    public EnumWorldBlockLayer getBlockLayer() {
        return this.modelBlock.getBlockLayer();
    }

    /**
     * How many world ticks before ticking
     */
    public int tickRate(World worldIn) {
        return this.modelBlock.tickRate(worldIn);
    }

    public AxisAlignedBB getSelectedBoundingBox(World worldIn, BlockPos pos) {
        return this.modelBlock.getSelectedBoundingBox(worldIn, pos);
    }

    public Vec3 modifyAcceleration(World worldIn, BlockPos pos, Entity entityIn, Vec3 motion) {
        return this.modelBlock.modifyAcceleration(worldIn, pos, entityIn, motion);
    }

    /**
     * Returns if this block is collidable (only used by Fire). Args: x, y, z
     */
    public boolean isCollidable() {
        return this.modelBlock.isCollidable();
    }

    public boolean canCollideCheck(IBlockState state, boolean p_176209_2_) {
        return this.modelBlock.canCollideCheck(state, p_176209_2_);
    }

    public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
        return this.modelBlock.canPlaceBlockAt(worldIn, pos);
    }

    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        this.onNeighborBlockChange(worldIn, pos, this.modelState, Blocks.air);
        this.modelBlock.onBlockAdded(worldIn, pos, this.modelState);
    }

    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        this.modelBlock.breakBlock(worldIn, pos, this.modelState);
    }

    /**
     * Triggered whenever an entity collides with this block (enters into the block)
     */
    public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, Entity entityIn) {
        this.modelBlock.onEntityCollidedWithBlock(worldIn, pos, entityIn);
    }

    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        this.modelBlock.updateTick(worldIn, pos, state, rand);
    }

    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
        return this.modelBlock.onBlockActivated(worldIn, pos, this.modelState, playerIn, EnumFacing.DOWN, 0.0F, 0.0F, 0.0F);
    }

    /**
     * Called when this Block is destroyed by an Explosion
     */
    public void onBlockDestroyedByExplosion(World worldIn, BlockPos pos, Explosion explosionIn) {
        this.modelBlock.onBlockDestroyedByExplosion(worldIn, pos, explosionIn);
    }

    /**
     * Get the MapColor for this Block and the given BlockState
     */
    public MapColor getMapColor(IBlockState state) {
        return this.modelBlock.getMapColor(this.modelState);
    }

    public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        IBlockState var9 = super.onBlockPlaced(worldIn, pos, facing, hitX, hitY, hitZ, meta, placer);
        var9 = var9.withProperty(FACING, placer.func_174811_aO()).withProperty(SHAPE, BlockStairs.EnumShape.STRAIGHT);
        return facing != EnumFacing.DOWN && (facing == EnumFacing.UP || (double) hitY <= 0.5D) ? var9.withProperty(HALF, BlockStairs.EnumHalf.BOTTOM) : var9.withProperty(HALF, BlockStairs.EnumHalf.TOP);
    }

    /**
     * Ray traces through the blocks collision from start vector to end vector returning a ray trace hit.
     *
     * @param start The start vector
     * @param end   The end vector
     */
    public MovingObjectPosition collisionRayTrace(World worldIn, BlockPos pos, Vec3 start, Vec3 end) {
        MovingObjectPosition[] var5 = new MovingObjectPosition[8];
        IBlockState var6 = worldIn.getBlockState(pos);
        int var7 = ((EnumFacing) var6.getValue(FACING)).getHorizontalIndex();
        boolean var8 = var6.getValue(HALF) == BlockStairs.EnumHalf.TOP;
        int[] var9 = field_150150_a[var7 + (var8 ? 4 : 0)];
        this.field_150152_N = true;

        for (int var10 = 0; var10 < 8; ++var10) {
            this.field_150153_O = var10;

            if (Arrays.binarySearch(var9, var10) < 0) {
                var5[var10] = super.collisionRayTrace(worldIn, pos, start, end);
            }
        }

        int[] var19 = var9;
        int var11 = var9.length;

        for (int var12 = 0; var12 < var11; ++var12) {
            int var13 = var19[var12];
            var5[var13] = null;
        }

        MovingObjectPosition var20 = null;
        double var21 = 0.0D;
        MovingObjectPosition[] var22 = var5;
        int var14 = var5.length;

        for (int var15 = 0; var15 < var14; ++var15) {
            MovingObjectPosition var16 = var22[var15];

            if (var16 != null) {
                double var17 = var16.hitVec.squareDistanceTo(end);

                if (var17 > var21) {
                    var20 = var16;
                    var21 = var17;
                }
            }
        }

        return var20;
    }

    /**
     * Convert the given metadata into a BlockState for this Block
     */
    public IBlockState getStateFromMeta(int meta) {
        IBlockState var2 = this.getDefaultState().withProperty(HALF, (meta & 4) > 0 ? BlockStairs.EnumHalf.TOP : BlockStairs.EnumHalf.BOTTOM);
        var2 = var2.withProperty(FACING, EnumFacing.getFront(5 - (meta & 3)));
        return var2;
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    public int getMetaFromState(IBlockState state) {
        int var2 = 0;

        if (state.getValue(HALF) == BlockStairs.EnumHalf.TOP) {
            var2 |= 4;
        }

        var2 |= 5 - ((EnumFacing) state.getValue(FACING)).getIndex();
        return var2;
    }

    /**
     * Get the actual Block state of this Block at the given position. This applies properties not visible in the
     * metadata, such as fence connections.
     */
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        if (this.func_176306_h(worldIn, pos)) {
            switch (this.func_176305_g(worldIn, pos)) {
                case 0:
                    state = state.withProperty(SHAPE, BlockStairs.EnumShape.STRAIGHT);
                    break;

                case 1:
                    state = state.withProperty(SHAPE, BlockStairs.EnumShape.INNER_RIGHT);
                    break;

                case 2:
                    state = state.withProperty(SHAPE, BlockStairs.EnumShape.INNER_LEFT);
            }
        } else {
            switch (this.func_176307_f(worldIn, pos)) {
                case 0:
                    state = state.withProperty(SHAPE, BlockStairs.EnumShape.STRAIGHT);
                    break;

                case 1:
                    state = state.withProperty(SHAPE, BlockStairs.EnumShape.OUTER_RIGHT);
                    break;

                case 2:
                    state = state.withProperty(SHAPE, BlockStairs.EnumShape.OUTER_LEFT);
            }
        }

        return state;
    }

    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[]{FACING, HALF, SHAPE});
    }

    public static enum EnumHalf implements IStringSerializable {
        TOP("TOP", 0, "top"),
        BOTTOM("BOTTOM", 1, "bottom");
        private final String field_176709_c;

        private static final BlockStairs.EnumHalf[] $VALUES = new BlockStairs.EnumHalf[]{TOP, BOTTOM};
        private static final String __OBFID = "CL_00002062";

        private EnumHalf(String p_i45683_1_, int p_i45683_2_, String p_i45683_3_) {
            this.field_176709_c = p_i45683_3_;
        }

        public String toString() {
            return this.field_176709_c;
        }

        public String getName() {
            return this.field_176709_c;
        }
    }

    public static enum EnumShape implements IStringSerializable {
        STRAIGHT("STRAIGHT", 0, "straight"),
        INNER_LEFT("INNER_LEFT", 1, "inner_left"),
        INNER_RIGHT("INNER_RIGHT", 2, "inner_right"),
        OUTER_LEFT("OUTER_LEFT", 3, "outer_left"),
        OUTER_RIGHT("OUTER_RIGHT", 4, "outer_right");
        private final String field_176699_f;

        private static final BlockStairs.EnumShape[] $VALUES = new BlockStairs.EnumShape[]{STRAIGHT, INNER_LEFT, INNER_RIGHT, OUTER_LEFT, OUTER_RIGHT};
        private static final String __OBFID = "CL_00002061";

        private EnumShape(String p_i45682_1_, int p_i45682_2_, String p_i45682_3_) {
            this.field_176699_f = p_i45682_3_;
        }

        public String toString() {
            return this.field_176699_f;
        }

        public String getName() {
            return this.field_176699_f;
        }
    }
}
