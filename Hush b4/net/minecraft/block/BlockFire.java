// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import net.minecraft.block.state.BlockState;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.block.material.MapColor;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.WorldProviderEnd;
import java.util.Random;
import net.minecraft.util.AxisAlignedBB;
import com.google.common.collect.Maps;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.block.state.IBlockState;
import java.util.Map;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyInteger;

public class BlockFire extends Block
{
    public static final PropertyInteger AGE;
    public static final PropertyBool FLIP;
    public static final PropertyBool ALT;
    public static final PropertyBool NORTH;
    public static final PropertyBool EAST;
    public static final PropertyBool SOUTH;
    public static final PropertyBool WEST;
    public static final PropertyInteger UPPER;
    private final Map<Block, Integer> encouragements;
    private final Map<Block, Integer> flammabilities;
    
    static {
        AGE = PropertyInteger.create("age", 0, 15);
        FLIP = PropertyBool.create("flip");
        ALT = PropertyBool.create("alt");
        NORTH = PropertyBool.create("north");
        EAST = PropertyBool.create("east");
        SOUTH = PropertyBool.create("south");
        WEST = PropertyBool.create("west");
        UPPER = PropertyInteger.create("upper", 0, 2);
    }
    
    @Override
    public IBlockState getActualState(final IBlockState state, final IBlockAccess worldIn, final BlockPos pos) {
        final int i = pos.getX();
        final int j = pos.getY();
        final int k = pos.getZ();
        if (!World.doesBlockHaveSolidTopSurface(worldIn, pos.down()) && !Blocks.fire.canCatchFire(worldIn, pos.down())) {
            final boolean flag = (i + j + k & 0x1) == 0x1;
            final boolean flag2 = (i / 2 + j / 2 + k / 2 & 0x1) == 0x1;
            int l = 0;
            if (this.canCatchFire(worldIn, pos.up())) {
                l = (flag ? 1 : 2);
            }
            return state.withProperty((IProperty<Comparable>)BlockFire.NORTH, this.canCatchFire(worldIn, pos.north())).withProperty((IProperty<Comparable>)BlockFire.EAST, this.canCatchFire(worldIn, pos.east())).withProperty((IProperty<Comparable>)BlockFire.SOUTH, this.canCatchFire(worldIn, pos.south())).withProperty((IProperty<Comparable>)BlockFire.WEST, this.canCatchFire(worldIn, pos.west())).withProperty((IProperty<Comparable>)BlockFire.UPPER, l).withProperty((IProperty<Comparable>)BlockFire.FLIP, flag2).withProperty((IProperty<Comparable>)BlockFire.ALT, flag);
        }
        return this.getDefaultState();
    }
    
    protected BlockFire() {
        super(Material.fire);
        this.encouragements = (Map<Block, Integer>)Maps.newIdentityHashMap();
        this.flammabilities = (Map<Block, Integer>)Maps.newIdentityHashMap();
        this.setDefaultState(this.blockState.getBaseState().withProperty((IProperty<Comparable>)BlockFire.AGE, 0).withProperty((IProperty<Comparable>)BlockFire.FLIP, false).withProperty((IProperty<Comparable>)BlockFire.ALT, false).withProperty((IProperty<Comparable>)BlockFire.NORTH, false).withProperty((IProperty<Comparable>)BlockFire.EAST, false).withProperty((IProperty<Comparable>)BlockFire.SOUTH, false).withProperty((IProperty<Comparable>)BlockFire.WEST, false).withProperty((IProperty<Comparable>)BlockFire.UPPER, 0));
        this.setTickRandomly(true);
    }
    
    public static void init() {
        Blocks.fire.setFireInfo(Blocks.planks, 5, 20);
        Blocks.fire.setFireInfo(Blocks.double_wooden_slab, 5, 20);
        Blocks.fire.setFireInfo(Blocks.wooden_slab, 5, 20);
        Blocks.fire.setFireInfo(Blocks.oak_fence_gate, 5, 20);
        Blocks.fire.setFireInfo(Blocks.spruce_fence_gate, 5, 20);
        Blocks.fire.setFireInfo(Blocks.birch_fence_gate, 5, 20);
        Blocks.fire.setFireInfo(Blocks.jungle_fence_gate, 5, 20);
        Blocks.fire.setFireInfo(Blocks.dark_oak_fence_gate, 5, 20);
        Blocks.fire.setFireInfo(Blocks.acacia_fence_gate, 5, 20);
        Blocks.fire.setFireInfo(Blocks.oak_fence, 5, 20);
        Blocks.fire.setFireInfo(Blocks.spruce_fence, 5, 20);
        Blocks.fire.setFireInfo(Blocks.birch_fence, 5, 20);
        Blocks.fire.setFireInfo(Blocks.jungle_fence, 5, 20);
        Blocks.fire.setFireInfo(Blocks.dark_oak_fence, 5, 20);
        Blocks.fire.setFireInfo(Blocks.acacia_fence, 5, 20);
        Blocks.fire.setFireInfo(Blocks.oak_stairs, 5, 20);
        Blocks.fire.setFireInfo(Blocks.birch_stairs, 5, 20);
        Blocks.fire.setFireInfo(Blocks.spruce_stairs, 5, 20);
        Blocks.fire.setFireInfo(Blocks.jungle_stairs, 5, 20);
        Blocks.fire.setFireInfo(Blocks.log, 5, 5);
        Blocks.fire.setFireInfo(Blocks.log2, 5, 5);
        Blocks.fire.setFireInfo(Blocks.leaves, 30, 60);
        Blocks.fire.setFireInfo(Blocks.leaves2, 30, 60);
        Blocks.fire.setFireInfo(Blocks.bookshelf, 30, 20);
        Blocks.fire.setFireInfo(Blocks.tnt, 15, 100);
        Blocks.fire.setFireInfo(Blocks.tallgrass, 60, 100);
        Blocks.fire.setFireInfo(Blocks.double_plant, 60, 100);
        Blocks.fire.setFireInfo(Blocks.yellow_flower, 60, 100);
        Blocks.fire.setFireInfo(Blocks.red_flower, 60, 100);
        Blocks.fire.setFireInfo(Blocks.deadbush, 60, 100);
        Blocks.fire.setFireInfo(Blocks.wool, 30, 60);
        Blocks.fire.setFireInfo(Blocks.vine, 15, 100);
        Blocks.fire.setFireInfo(Blocks.coal_block, 5, 5);
        Blocks.fire.setFireInfo(Blocks.hay_block, 60, 20);
        Blocks.fire.setFireInfo(Blocks.carpet, 60, 20);
    }
    
    public void setFireInfo(final Block blockIn, final int encouragement, final int flammability) {
        this.encouragements.put(blockIn, encouragement);
        this.flammabilities.put(blockIn, flammability);
    }
    
    @Override
    public AxisAlignedBB getCollisionBoundingBox(final World worldIn, final BlockPos pos, final IBlockState state) {
        return null;
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
    public int quantityDropped(final Random random) {
        return 0;
    }
    
    @Override
    public int tickRate(final World worldIn) {
        return 30;
    }
    
    @Override
    public void updateTick(final World worldIn, final BlockPos pos, IBlockState state, final Random rand) {
        if (worldIn.getGameRules().getBoolean("doFireTick")) {
            if (!this.canPlaceBlockAt(worldIn, pos)) {
                worldIn.setBlockToAir(pos);
            }
            final Block block = worldIn.getBlockState(pos.down()).getBlock();
            boolean flag = block == Blocks.netherrack;
            if (worldIn.provider instanceof WorldProviderEnd && block == Blocks.bedrock) {
                flag = true;
            }
            if (!flag && worldIn.isRaining() && this.canDie(worldIn, pos)) {
                worldIn.setBlockToAir(pos);
            }
            else {
                final int i = state.getValue((IProperty<Integer>)BlockFire.AGE);
                if (i < 15) {
                    state = state.withProperty((IProperty<Comparable>)BlockFire.AGE, i + rand.nextInt(3) / 2);
                    worldIn.setBlockState(pos, state, 4);
                }
                worldIn.scheduleUpdate(pos, this, this.tickRate(worldIn) + rand.nextInt(10));
                if (!flag) {
                    if (!this.canNeighborCatchFire(worldIn, pos)) {
                        if (!World.doesBlockHaveSolidTopSurface(worldIn, pos.down()) || i > 3) {
                            worldIn.setBlockToAir(pos);
                        }
                        return;
                    }
                    if (!this.canCatchFire(worldIn, pos.down()) && i == 15 && rand.nextInt(4) == 0) {
                        worldIn.setBlockToAir(pos);
                        return;
                    }
                }
                final boolean flag2 = worldIn.isBlockinHighHumidity(pos);
                int j = 0;
                if (flag2) {
                    j = -50;
                }
                this.catchOnFire(worldIn, pos.east(), 300 + j, rand, i);
                this.catchOnFire(worldIn, pos.west(), 300 + j, rand, i);
                this.catchOnFire(worldIn, pos.down(), 250 + j, rand, i);
                this.catchOnFire(worldIn, pos.up(), 250 + j, rand, i);
                this.catchOnFire(worldIn, pos.north(), 300 + j, rand, i);
                this.catchOnFire(worldIn, pos.south(), 300 + j, rand, i);
                for (int k = -1; k <= 1; ++k) {
                    for (int l = -1; l <= 1; ++l) {
                        for (int i2 = -1; i2 <= 4; ++i2) {
                            if (k != 0 || i2 != 0 || l != 0) {
                                int j2 = 100;
                                if (i2 > 1) {
                                    j2 += (i2 - 1) * 100;
                                }
                                final BlockPos blockpos = pos.add(k, i2, l);
                                final int k2 = this.getNeighborEncouragement(worldIn, blockpos);
                                if (k2 > 0) {
                                    int l2 = (k2 + 40 + worldIn.getDifficulty().getDifficultyId() * 7) / (i + 30);
                                    if (flag2) {
                                        l2 /= 2;
                                    }
                                    if (l2 > 0 && rand.nextInt(j2) <= l2 && (!worldIn.isRaining() || !this.canDie(worldIn, blockpos))) {
                                        int i3 = i + rand.nextInt(5) / 4;
                                        if (i3 > 15) {
                                            i3 = 15;
                                        }
                                        worldIn.setBlockState(blockpos, state.withProperty((IProperty<Comparable>)BlockFire.AGE, i3), 3);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    
    protected boolean canDie(final World worldIn, final BlockPos pos) {
        return worldIn.canLightningStrike(pos) || worldIn.canLightningStrike(pos.west()) || worldIn.canLightningStrike(pos.east()) || worldIn.canLightningStrike(pos.north()) || worldIn.canLightningStrike(pos.south());
    }
    
    @Override
    public boolean requiresUpdates() {
        return false;
    }
    
    private int getFlammability(final Block blockIn) {
        final Integer integer = this.flammabilities.get(blockIn);
        return (integer == null) ? 0 : integer;
    }
    
    private int getEncouragement(final Block blockIn) {
        final Integer integer = this.encouragements.get(blockIn);
        return (integer == null) ? 0 : integer;
    }
    
    private void catchOnFire(final World worldIn, final BlockPos pos, final int chance, final Random random, final int age) {
        final int i = this.getFlammability(worldIn.getBlockState(pos).getBlock());
        if (random.nextInt(chance) < i) {
            final IBlockState iblockstate = worldIn.getBlockState(pos);
            if (random.nextInt(age + 10) < 5 && !worldIn.canLightningStrike(pos)) {
                int j = age + random.nextInt(5) / 4;
                if (j > 15) {
                    j = 15;
                }
                worldIn.setBlockState(pos, this.getDefaultState().withProperty((IProperty<Comparable>)BlockFire.AGE, j), 3);
            }
            else {
                worldIn.setBlockToAir(pos);
            }
            if (iblockstate.getBlock() == Blocks.tnt) {
                Blocks.tnt.onBlockDestroyedByPlayer(worldIn, pos, iblockstate.withProperty((IProperty<Comparable>)BlockTNT.EXPLODE, true));
            }
        }
    }
    
    private boolean canNeighborCatchFire(final World worldIn, final BlockPos pos) {
        EnumFacing[] values;
        for (int length = (values = EnumFacing.values()).length, i = 0; i < length; ++i) {
            final EnumFacing enumfacing = values[i];
            if (this.canCatchFire(worldIn, pos.offset(enumfacing))) {
                return true;
            }
        }
        return false;
    }
    
    private int getNeighborEncouragement(final World worldIn, final BlockPos pos) {
        if (!worldIn.isAirBlock(pos)) {
            return 0;
        }
        int i = 0;
        EnumFacing[] values;
        for (int length = (values = EnumFacing.values()).length, j = 0; j < length; ++j) {
            final EnumFacing enumfacing = values[j];
            i = Math.max(this.getEncouragement(worldIn.getBlockState(pos.offset(enumfacing)).getBlock()), i);
        }
        return i;
    }
    
    @Override
    public boolean isCollidable() {
        return false;
    }
    
    public boolean canCatchFire(final IBlockAccess worldIn, final BlockPos pos) {
        return this.getEncouragement(worldIn.getBlockState(pos).getBlock()) > 0;
    }
    
    @Override
    public boolean canPlaceBlockAt(final World worldIn, final BlockPos pos) {
        return World.doesBlockHaveSolidTopSurface(worldIn, pos.down()) || this.canNeighborCatchFire(worldIn, pos);
    }
    
    @Override
    public void onNeighborBlockChange(final World worldIn, final BlockPos pos, final IBlockState state, final Block neighborBlock) {
        if (!World.doesBlockHaveSolidTopSurface(worldIn, pos.down()) && !this.canNeighborCatchFire(worldIn, pos)) {
            worldIn.setBlockToAir(pos);
        }
    }
    
    @Override
    public void onBlockAdded(final World worldIn, final BlockPos pos, final IBlockState state) {
        if (worldIn.provider.getDimensionId() > 0 || !Blocks.portal.func_176548_d(worldIn, pos)) {
            if (!World.doesBlockHaveSolidTopSurface(worldIn, pos.down()) && !this.canNeighborCatchFire(worldIn, pos)) {
                worldIn.setBlockToAir(pos);
            }
            else {
                worldIn.scheduleUpdate(pos, this, this.tickRate(worldIn) + worldIn.rand.nextInt(10));
            }
        }
    }
    
    @Override
    public void randomDisplayTick(final World worldIn, final BlockPos pos, final IBlockState state, final Random rand) {
        if (rand.nextInt(24) == 0) {
            worldIn.playSound(pos.getX() + 0.5f, pos.getY() + 0.5f, pos.getZ() + 0.5f, "fire.fire", 1.0f + rand.nextFloat(), rand.nextFloat() * 0.7f + 0.3f, false);
        }
        if (!World.doesBlockHaveSolidTopSurface(worldIn, pos.down()) && !Blocks.fire.canCatchFire(worldIn, pos.down())) {
            if (Blocks.fire.canCatchFire(worldIn, pos.west())) {
                for (int j = 0; j < 2; ++j) {
                    final double d3 = pos.getX() + rand.nextDouble() * 0.10000000149011612;
                    final double d4 = pos.getY() + rand.nextDouble();
                    final double d5 = pos.getZ() + rand.nextDouble();
                    worldIn.spawnParticle(EnumParticleTypes.SMOKE_LARGE, d3, d4, d5, 0.0, 0.0, 0.0, new int[0]);
                }
            }
            if (Blocks.fire.canCatchFire(worldIn, pos.east())) {
                for (int k = 0; k < 2; ++k) {
                    final double d6 = pos.getX() + 1 - rand.nextDouble() * 0.10000000149011612;
                    final double d7 = pos.getY() + rand.nextDouble();
                    final double d8 = pos.getZ() + rand.nextDouble();
                    worldIn.spawnParticle(EnumParticleTypes.SMOKE_LARGE, d6, d7, d8, 0.0, 0.0, 0.0, new int[0]);
                }
            }
            if (Blocks.fire.canCatchFire(worldIn, pos.north())) {
                for (int l = 0; l < 2; ++l) {
                    final double d9 = pos.getX() + rand.nextDouble();
                    final double d10 = pos.getY() + rand.nextDouble();
                    final double d11 = pos.getZ() + rand.nextDouble() * 0.10000000149011612;
                    worldIn.spawnParticle(EnumParticleTypes.SMOKE_LARGE, d9, d10, d11, 0.0, 0.0, 0.0, new int[0]);
                }
            }
            if (Blocks.fire.canCatchFire(worldIn, pos.south())) {
                for (int i1 = 0; i1 < 2; ++i1) {
                    final double d12 = pos.getX() + rand.nextDouble();
                    final double d13 = pos.getY() + rand.nextDouble();
                    final double d14 = pos.getZ() + 1 - rand.nextDouble() * 0.10000000149011612;
                    worldIn.spawnParticle(EnumParticleTypes.SMOKE_LARGE, d12, d13, d14, 0.0, 0.0, 0.0, new int[0]);
                }
            }
            if (Blocks.fire.canCatchFire(worldIn, pos.up())) {
                for (int j2 = 0; j2 < 2; ++j2) {
                    final double d15 = pos.getX() + rand.nextDouble();
                    final double d16 = pos.getY() + 1 - rand.nextDouble() * 0.10000000149011612;
                    final double d17 = pos.getZ() + rand.nextDouble();
                    worldIn.spawnParticle(EnumParticleTypes.SMOKE_LARGE, d15, d16, d17, 0.0, 0.0, 0.0, new int[0]);
                }
            }
        }
        else {
            for (int m = 0; m < 3; ++m) {
                final double d18 = pos.getX() + rand.nextDouble();
                final double d19 = pos.getY() + rand.nextDouble() * 0.5 + 0.5;
                final double d20 = pos.getZ() + rand.nextDouble();
                worldIn.spawnParticle(EnumParticleTypes.SMOKE_LARGE, d18, d19, d20, 0.0, 0.0, 0.0, new int[0]);
            }
        }
    }
    
    @Override
    public MapColor getMapColor(final IBlockState state) {
        return MapColor.tntColor;
    }
    
    @Override
    public EnumWorldBlockLayer getBlockLayer() {
        return EnumWorldBlockLayer.CUTOUT;
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        return this.getDefaultState().withProperty((IProperty<Comparable>)BlockFire.AGE, meta);
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        return state.getValue((IProperty<Integer>)BlockFire.AGE);
    }
    
    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[] { BlockFire.AGE, BlockFire.NORTH, BlockFire.EAST, BlockFire.SOUTH, BlockFire.WEST, BlockFire.UPPER, BlockFire.FLIP, BlockFire.ALT });
    }
}
