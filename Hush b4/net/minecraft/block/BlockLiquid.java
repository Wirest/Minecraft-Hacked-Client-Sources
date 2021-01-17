// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import net.minecraft.block.state.BlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.MathHelper;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.entity.Entity;
import java.util.Iterator;
import net.minecraft.util.Vec3;
import net.minecraft.item.Item;
import java.util.Random;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraft.util.EnumFacing;
import net.minecraft.block.state.IBlockState;
import net.minecraft.world.biome.BiomeColorHelper;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;

public abstract class BlockLiquid extends Block
{
    public static final PropertyInteger LEVEL;
    
    static {
        LEVEL = PropertyInteger.create("level", 0, 15);
    }
    
    protected BlockLiquid(final Material materialIn) {
        super(materialIn);
        this.setDefaultState(this.blockState.getBaseState().withProperty((IProperty<Comparable>)BlockLiquid.LEVEL, 0));
        this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
        this.setTickRandomly(true);
    }
    
    @Override
    public boolean isPassable(final IBlockAccess worldIn, final BlockPos pos) {
        return this.blockMaterial != Material.lava;
    }
    
    @Override
    public int colorMultiplier(final IBlockAccess worldIn, final BlockPos pos, final int renderPass) {
        return (this.blockMaterial == Material.water) ? BiomeColorHelper.getWaterColorAtPos(worldIn, pos) : 16777215;
    }
    
    public static float getLiquidHeightPercent(int meta) {
        if (meta >= 8) {
            meta = 0;
        }
        return (meta + 1) / 9.0f;
    }
    
    protected int getLevel(final IBlockAccess worldIn, final BlockPos pos) {
        return (worldIn.getBlockState(pos).getBlock().getMaterial() == this.blockMaterial) ? worldIn.getBlockState(pos).getValue((IProperty<Integer>)BlockLiquid.LEVEL) : -1;
    }
    
    protected int getEffectiveFlowDecay(final IBlockAccess worldIn, final BlockPos pos) {
        final int i = this.getLevel(worldIn, pos);
        return (i >= 8) ? 0 : i;
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
    public boolean canCollideCheck(final IBlockState state, final boolean hitIfLiquid) {
        return hitIfLiquid && state.getValue((IProperty<Integer>)BlockLiquid.LEVEL) == 0;
    }
    
    @Override
    public boolean isBlockSolid(final IBlockAccess worldIn, final BlockPos pos, final EnumFacing side) {
        final Material material = worldIn.getBlockState(pos).getBlock().getMaterial();
        return material != this.blockMaterial && (side == EnumFacing.UP || (material != Material.ice && super.isBlockSolid(worldIn, pos, side)));
    }
    
    @Override
    public boolean shouldSideBeRendered(final IBlockAccess worldIn, final BlockPos pos, final EnumFacing side) {
        return worldIn.getBlockState(pos).getBlock().getMaterial() != this.blockMaterial && (side == EnumFacing.UP || super.shouldSideBeRendered(worldIn, pos, side));
    }
    
    public boolean func_176364_g(final IBlockAccess blockAccess, final BlockPos pos) {
        for (int i = -1; i <= 1; ++i) {
            for (int j = -1; j <= 1; ++j) {
                final IBlockState iblockstate = blockAccess.getBlockState(pos.add(i, 0, j));
                final Block block = iblockstate.getBlock();
                final Material material = block.getMaterial();
                if (material != this.blockMaterial && !block.isFullBlock()) {
                    return true;
                }
            }
        }
        return false;
    }
    
    @Override
    public AxisAlignedBB getCollisionBoundingBox(final World worldIn, final BlockPos pos, final IBlockState state) {
        return null;
    }
    
    @Override
    public int getRenderType() {
        return 1;
    }
    
    @Override
    public Item getItemDropped(final IBlockState state, final Random rand, final int fortune) {
        return null;
    }
    
    @Override
    public int quantityDropped(final Random random) {
        return 0;
    }
    
    protected Vec3 getFlowVector(final IBlockAccess worldIn, final BlockPos pos) {
        Vec3 vec3 = new Vec3(0.0, 0.0, 0.0);
        final int i = this.getEffectiveFlowDecay(worldIn, pos);
        for (final Object enumfacing : EnumFacing.Plane.HORIZONTAL) {
            final BlockPos blockpos = pos.offset((EnumFacing)enumfacing);
            int j = this.getEffectiveFlowDecay(worldIn, blockpos);
            if (j < 0) {
                if (worldIn.getBlockState(blockpos).getBlock().getMaterial().blocksMovement()) {
                    continue;
                }
                j = this.getEffectiveFlowDecay(worldIn, blockpos.down());
                if (j < 0) {
                    continue;
                }
                final int k = j - (i - 8);
                vec3 = vec3.addVector((blockpos.getX() - pos.getX()) * k, (blockpos.getY() - pos.getY()) * k, (blockpos.getZ() - pos.getZ()) * k);
            }
            else {
                if (j < 0) {
                    continue;
                }
                final int l = j - i;
                vec3 = vec3.addVector((blockpos.getX() - pos.getX()) * l, (blockpos.getY() - pos.getY()) * l, (blockpos.getZ() - pos.getZ()) * l);
            }
        }
        if (worldIn.getBlockState(pos).getValue((IProperty<Integer>)BlockLiquid.LEVEL) >= 8) {
            for (final Object enumfacing2 : EnumFacing.Plane.HORIZONTAL) {
                final BlockPos blockpos2 = pos.offset((EnumFacing)enumfacing2);
                if (this.isBlockSolid(worldIn, blockpos2, (EnumFacing)enumfacing2) || this.isBlockSolid(worldIn, blockpos2.up(), (EnumFacing)enumfacing2)) {
                    vec3 = vec3.normalize().addVector(0.0, -6.0, 0.0);
                    break;
                }
            }
        }
        return vec3.normalize();
    }
    
    @Override
    public Vec3 modifyAcceleration(final World worldIn, final BlockPos pos, final Entity entityIn, final Vec3 motion) {
        return motion.add(this.getFlowVector(worldIn, pos));
    }
    
    @Override
    public int tickRate(final World worldIn) {
        return (this.blockMaterial == Material.water) ? 5 : ((this.blockMaterial == Material.lava) ? (worldIn.provider.getHasNoSky() ? 10 : 30) : 0);
    }
    
    @Override
    public int getMixedBrightnessForBlock(final IBlockAccess worldIn, final BlockPos pos) {
        final int i = worldIn.getCombinedLight(pos, 0);
        final int j = worldIn.getCombinedLight(pos.up(), 0);
        final int k = i & 0xFF;
        final int l = j & 0xFF;
        final int i2 = i >> 16 & 0xFF;
        final int j2 = j >> 16 & 0xFF;
        return ((k > l) ? k : l) | ((i2 > j2) ? i2 : j2) << 16;
    }
    
    @Override
    public EnumWorldBlockLayer getBlockLayer() {
        return (this.blockMaterial == Material.water) ? EnumWorldBlockLayer.TRANSLUCENT : EnumWorldBlockLayer.SOLID;
    }
    
    @Override
    public void randomDisplayTick(final World worldIn, final BlockPos pos, final IBlockState state, final Random rand) {
        final double d0 = pos.getX();
        final double d2 = pos.getY();
        final double d3 = pos.getZ();
        if (this.blockMaterial == Material.water) {
            final int i = state.getValue((IProperty<Integer>)BlockLiquid.LEVEL);
            if (i > 0 && i < 8) {
                if (rand.nextInt(64) == 0) {
                    worldIn.playSound(d0 + 0.5, d2 + 0.5, d3 + 0.5, "liquid.water", rand.nextFloat() * 0.25f + 0.75f, rand.nextFloat() * 1.0f + 0.5f, false);
                }
            }
            else if (rand.nextInt(10) == 0) {
                worldIn.spawnParticle(EnumParticleTypes.SUSPENDED, d0 + rand.nextFloat(), d2 + rand.nextFloat(), d3 + rand.nextFloat(), 0.0, 0.0, 0.0, new int[0]);
            }
        }
        if (this.blockMaterial == Material.lava && worldIn.getBlockState(pos.up()).getBlock().getMaterial() == Material.air && !worldIn.getBlockState(pos.up()).getBlock().isOpaqueCube()) {
            if (rand.nextInt(100) == 0) {
                final double d4 = d0 + rand.nextFloat();
                final double d5 = d2 + this.maxY;
                final double d6 = d3 + rand.nextFloat();
                worldIn.spawnParticle(EnumParticleTypes.LAVA, d4, d5, d6, 0.0, 0.0, 0.0, new int[0]);
                worldIn.playSound(d4, d5, d6, "liquid.lavapop", 0.2f + rand.nextFloat() * 0.2f, 0.9f + rand.nextFloat() * 0.15f, false);
            }
            if (rand.nextInt(200) == 0) {
                worldIn.playSound(d0, d2, d3, "liquid.lava", 0.2f + rand.nextFloat() * 0.2f, 0.9f + rand.nextFloat() * 0.15f, false);
            }
        }
        if (rand.nextInt(10) == 0 && World.doesBlockHaveSolidTopSurface(worldIn, pos.down())) {
            final Material material = worldIn.getBlockState(pos.down(2)).getBlock().getMaterial();
            if (!material.blocksMovement() && !material.isLiquid()) {
                final double d7 = d0 + rand.nextFloat();
                final double d8 = d2 - 1.05;
                final double d9 = d3 + rand.nextFloat();
                if (this.blockMaterial == Material.water) {
                    worldIn.spawnParticle(EnumParticleTypes.DRIP_WATER, d7, d8, d9, 0.0, 0.0, 0.0, new int[0]);
                }
                else {
                    worldIn.spawnParticle(EnumParticleTypes.DRIP_LAVA, d7, d8, d9, 0.0, 0.0, 0.0, new int[0]);
                }
            }
        }
    }
    
    public static double getFlowDirection(final IBlockAccess worldIn, final BlockPos pos, final Material materialIn) {
        final Vec3 vec3 = getFlowingBlock(materialIn).getFlowVector(worldIn, pos);
        return (vec3.xCoord == 0.0 && vec3.zCoord == 0.0) ? -1000.0 : (MathHelper.func_181159_b(vec3.zCoord, vec3.xCoord) - 1.5707963267948966);
    }
    
    @Override
    public void onBlockAdded(final World worldIn, final BlockPos pos, final IBlockState state) {
        this.checkForMixing(worldIn, pos, state);
    }
    
    @Override
    public void onNeighborBlockChange(final World worldIn, final BlockPos pos, final IBlockState state, final Block neighborBlock) {
        this.checkForMixing(worldIn, pos, state);
    }
    
    public boolean checkForMixing(final World worldIn, final BlockPos pos, final IBlockState state) {
        if (this.blockMaterial == Material.lava) {
            boolean flag = false;
            EnumFacing[] values;
            for (int length = (values = EnumFacing.values()).length, i = 0; i < length; ++i) {
                final EnumFacing enumfacing = values[i];
                if (enumfacing != EnumFacing.DOWN && worldIn.getBlockState(pos.offset(enumfacing)).getBlock().getMaterial() == Material.water) {
                    flag = true;
                    break;
                }
            }
            if (flag) {
                final Integer integer = state.getValue((IProperty<Integer>)BlockLiquid.LEVEL);
                if (integer == 0) {
                    worldIn.setBlockState(pos, Blocks.obsidian.getDefaultState());
                    this.triggerMixEffects(worldIn, pos);
                    return true;
                }
                if (integer <= 4) {
                    worldIn.setBlockState(pos, Blocks.cobblestone.getDefaultState());
                    this.triggerMixEffects(worldIn, pos);
                    return true;
                }
            }
        }
        return false;
    }
    
    protected void triggerMixEffects(final World worldIn, final BlockPos pos) {
        final double d0 = pos.getX();
        final double d2 = pos.getY();
        final double d3 = pos.getZ();
        worldIn.playSoundEffect(d0 + 0.5, d2 + 0.5, d3 + 0.5, "random.fizz", 0.5f, 2.6f + (worldIn.rand.nextFloat() - worldIn.rand.nextFloat()) * 0.8f);
        for (int i = 0; i < 8; ++i) {
            worldIn.spawnParticle(EnumParticleTypes.SMOKE_LARGE, d0 + Math.random(), d2 + 1.2, d3 + Math.random(), 0.0, 0.0, 0.0, new int[0]);
        }
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        return this.getDefaultState().withProperty((IProperty<Comparable>)BlockLiquid.LEVEL, meta);
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        return state.getValue((IProperty<Integer>)BlockLiquid.LEVEL);
    }
    
    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[] { BlockLiquid.LEVEL });
    }
    
    public static BlockDynamicLiquid getFlowingBlock(final Material materialIn) {
        if (materialIn == Material.water) {
            return Blocks.flowing_water;
        }
        if (materialIn == Material.lava) {
            return Blocks.flowing_lava;
        }
        throw new IllegalArgumentException("Invalid material");
    }
    
    public static BlockStaticLiquid getStaticBlock(final Material materialIn) {
        if (materialIn == Material.water) {
            return Blocks.water;
        }
        if (materialIn == Material.lava) {
            return Blocks.lava;
        }
        throw new IllegalArgumentException("Invalid material");
    }
}
