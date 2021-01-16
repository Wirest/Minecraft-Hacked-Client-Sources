package net.minecraft.block;

import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public abstract class BlockLiquid extends Block
{
    public static final PropertyInteger LEVEL = PropertyInteger.create("level", 0, 15);

    protected BlockLiquid(Material materialIn)
    {
        super(materialIn);
        this.setDefaultState(this.blockState.getBaseState().withProperty(LEVEL, Integer.valueOf(0)));
        this.setTickRandomly(true);
    }

    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        return FULL_BLOCK_AABB;
    }

    @Nullable
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos)
    {
        return NULL_AABB;
    }

    public boolean isPassable(IBlockAccess worldIn, BlockPos pos)
    {
        return this.blockMaterial != Material.LAVA;
    }

    /**
     * Returns the percentage of the liquid block that is air, based on the given flow decay of the liquid
     */
    public static float getLiquidHeightPercent(int meta)
    {
        if (meta >= 8)
        {
            meta = 0;
        }

        return (float)(meta + 1) / 9.0F;
    }

    protected int getDepth(IBlockState p_189542_1_)
    {
        return p_189542_1_.getMaterial() == this.blockMaterial ? ((Integer)p_189542_1_.getValue(LEVEL)).intValue() : -1;
    }

    protected int getRenderedDepth(IBlockState p_189545_1_)
    {
        int i = this.getDepth(p_189545_1_);
        return i >= 8 ? 0 : i;
    }

    public boolean isFullCube(IBlockState state)
    {
        return false;
    }

    /**
     * Used to determine ambient occlusion and culling when rebuilding chunks for render
     */
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

    public boolean canCollideCheck(IBlockState state, boolean hitIfLiquid)
    {
        return hitIfLiquid && ((Integer)state.getValue(LEVEL)).intValue() == 0;
    }

    /**
     * Whether this Block is solid on the given Side
     */
    private boolean isBlockSolid(IBlockAccess worldIn, BlockPos pos, EnumFacing side)
    {
        IBlockState iblockstate = worldIn.getBlockState(pos);
        Block block = iblockstate.getBlock();
        Material material = iblockstate.getMaterial();

        if (material == this.blockMaterial)
        {
            return false;
        }
        else if (side == EnumFacing.UP)
        {
            return true;
        }
        else if (material == Material.ICE)
        {
            return false;
        }
        else
        {
            boolean flag = func_193382_c(block) || block instanceof BlockStairs;
            return !flag && iblockstate.func_193401_d(worldIn, pos, side) == BlockFaceShape.SOLID;
        }
    }

    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side)
    {
        if (blockAccess.getBlockState(pos.offset(side)).getMaterial() == this.blockMaterial)
        {
            return false;
        }
        else
        {
            return side == EnumFacing.UP ? true : super.shouldSideBeRendered(blockState, blockAccess, pos, side);
        }
    }

    public boolean shouldRenderSides(IBlockAccess blockAccess, BlockPos pos)
    {
        for (int i = -1; i <= 1; ++i)
        {
            for (int j = -1; j <= 1; ++j)
            {
                IBlockState iblockstate = blockAccess.getBlockState(pos.add(i, 0, j));

                if (iblockstate.getMaterial() != this.blockMaterial && !iblockstate.isFullBlock())
                {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * The type of render function called. MODEL for mixed tesr and static model, MODELBLOCK_ANIMATED for TESR-only,
     * LIQUID for vanilla liquids, INVISIBLE to skip all rendering
     */
    public EnumBlockRenderType getRenderType(IBlockState state)
    {
        return EnumBlockRenderType.LIQUID;
    }

    /**
     * Get the Item that this Block should drop when harvested.
     */
    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        return Items.field_190931_a;
    }

    /**
     * Returns the quantity of items to drop on block destruction.
     */
    public int quantityDropped(Random random)
    {
        return 0;
    }

    protected Vec3d getFlow(IBlockAccess p_189543_1_, BlockPos p_189543_2_, IBlockState p_189543_3_)
    {
        double d0 = 0.0D;
        double d1 = 0.0D;
        double d2 = 0.0D;
        int i = this.getRenderedDepth(p_189543_3_);
        BlockPos.PooledMutableBlockPos blockpos$pooledmutableblockpos = BlockPos.PooledMutableBlockPos.retain();

        for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL)
        {
            blockpos$pooledmutableblockpos.setPos(p_189543_2_).move(enumfacing);
            int j = this.getRenderedDepth(p_189543_1_.getBlockState(blockpos$pooledmutableblockpos));

            if (j < 0)
            {
                if (!p_189543_1_.getBlockState(blockpos$pooledmutableblockpos).getMaterial().blocksMovement())
                {
                    j = this.getRenderedDepth(p_189543_1_.getBlockState(blockpos$pooledmutableblockpos.down()));

                    if (j >= 0)
                    {
                        int k = j - (i - 8);
                        d0 += (double)(enumfacing.getFrontOffsetX() * k);
                        d1 += (double)(enumfacing.getFrontOffsetY() * k);
                        d2 += (double)(enumfacing.getFrontOffsetZ() * k);
                    }
                }
            }
            else if (j >= 0)
            {
                int l = j - i;
                d0 += (double)(enumfacing.getFrontOffsetX() * l);
                d1 += (double)(enumfacing.getFrontOffsetY() * l);
                d2 += (double)(enumfacing.getFrontOffsetZ() * l);
            }
        }

        Vec3d vec3d = new Vec3d(d0, d1, d2);

        if (((Integer)p_189543_3_.getValue(LEVEL)).intValue() >= 8)
        {
            for (EnumFacing enumfacing1 : EnumFacing.Plane.HORIZONTAL)
            {
                blockpos$pooledmutableblockpos.setPos(p_189543_2_).move(enumfacing1);

                if (this.isBlockSolid(p_189543_1_, blockpos$pooledmutableblockpos, enumfacing1) || this.isBlockSolid(p_189543_1_, blockpos$pooledmutableblockpos.up(), enumfacing1))
                {
                    vec3d = vec3d.normalize().addVector(0.0D, -6.0D, 0.0D);
                    break;
                }
            }
        }

        blockpos$pooledmutableblockpos.release();
        return vec3d.normalize();
    }

    public Vec3d modifyAcceleration(World worldIn, BlockPos pos, Entity entityIn, Vec3d motion)
    {
        return motion.add(this.getFlow(worldIn, pos, worldIn.getBlockState(pos)));
    }

    /**
     * How many world ticks before ticking
     */
    public int tickRate(World worldIn)
    {
        if (this.blockMaterial == Material.WATER)
        {
            return 5;
        }
        else if (this.blockMaterial == Material.LAVA)
        {
            return worldIn.provider.getHasNoSky() ? 10 : 30;
        }
        else
        {
            return 0;
        }
    }

    public int getPackedLightmapCoords(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        int i = source.getCombinedLight(pos, 0);
        int j = source.getCombinedLight(pos.up(), 0);
        int k = i & 255;
        int l = j & 255;
        int i1 = i >> 16 & 255;
        int j1 = j >> 16 & 255;
        return (k > l ? k : l) | (i1 > j1 ? i1 : j1) << 16;
    }

    public BlockRenderLayer getBlockLayer()
    {
        return this.blockMaterial == Material.WATER ? BlockRenderLayer.TRANSLUCENT : BlockRenderLayer.SOLID;
    }

    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand)
    {
        double d0 = (double)pos.getX();
        double d1 = (double)pos.getY();
        double d2 = (double)pos.getZ();

        if (this.blockMaterial == Material.WATER)
        {
            int i = ((Integer)stateIn.getValue(LEVEL)).intValue();

            if (i > 0 && i < 8)
            {
                if (rand.nextInt(64) == 0)
                {
                    worldIn.playSound(d0 + 0.5D, d1 + 0.5D, d2 + 0.5D, SoundEvents.BLOCK_WATER_AMBIENT, SoundCategory.BLOCKS, rand.nextFloat() * 0.25F + 0.75F, rand.nextFloat() + 0.5F, false);
                }
            }
            else if (rand.nextInt(10) == 0)
            {
                worldIn.spawnParticle(EnumParticleTypes.SUSPENDED, d0 + (double)rand.nextFloat(), d1 + (double)rand.nextFloat(), d2 + (double)rand.nextFloat(), 0.0D, 0.0D, 0.0D);
            }
        }

        if (this.blockMaterial == Material.LAVA && worldIn.getBlockState(pos.up()).getMaterial() == Material.AIR && !worldIn.getBlockState(pos.up()).isOpaqueCube())
        {
            if (rand.nextInt(100) == 0)
            {
                double d8 = d0 + (double)rand.nextFloat();
                double d4 = d1 + stateIn.getBoundingBox(worldIn, pos).maxY;
                double d6 = d2 + (double)rand.nextFloat();
                worldIn.spawnParticle(EnumParticleTypes.LAVA, d8, d4, d6, 0.0D, 0.0D, 0.0D);
                worldIn.playSound(d8, d4, d6, SoundEvents.BLOCK_LAVA_POP, SoundCategory.BLOCKS, 0.2F + rand.nextFloat() * 0.2F, 0.9F + rand.nextFloat() * 0.15F, false);
            }

            if (rand.nextInt(200) == 0)
            {
                worldIn.playSound(d0, d1, d2, SoundEvents.BLOCK_LAVA_AMBIENT, SoundCategory.BLOCKS, 0.2F + rand.nextFloat() * 0.2F, 0.9F + rand.nextFloat() * 0.15F, false);
            }
        }

        if (rand.nextInt(10) == 0 && worldIn.getBlockState(pos.down()).isFullyOpaque())
        {
            Material material = worldIn.getBlockState(pos.down(2)).getMaterial();

            if (!material.blocksMovement() && !material.isLiquid())
            {
                double d3 = d0 + (double)rand.nextFloat();
                double d5 = d1 - 1.05D;
                double d7 = d2 + (double)rand.nextFloat();

                if (this.blockMaterial == Material.WATER)
                {
                    worldIn.spawnParticle(EnumParticleTypes.DRIP_WATER, d3, d5, d7, 0.0D, 0.0D, 0.0D);
                }
                else
                {
                    worldIn.spawnParticle(EnumParticleTypes.DRIP_LAVA, d3, d5, d7, 0.0D, 0.0D, 0.0D);
                }
            }
        }
    }

    public static float getSlopeAngle(IBlockAccess p_189544_0_, BlockPos p_189544_1_, Material p_189544_2_, IBlockState p_189544_3_)
    {
        Vec3d vec3d = getFlowingBlock(p_189544_2_).getFlow(p_189544_0_, p_189544_1_, p_189544_3_);
        return vec3d.xCoord == 0.0D && vec3d.zCoord == 0.0D ? -1000.0F : (float)MathHelper.atan2(vec3d.zCoord, vec3d.xCoord) - ((float)Math.PI / 2F);
    }

    /**
     * Called after the block is set in the Chunk data, but before the Tile Entity is set
     */
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
    {
        this.checkForMixing(worldIn, pos, state);
    }

    /**
     * Called when a neighboring block was changed and marks that this state should perform any checks during a neighbor
     * change. Cases may include when redstone power is updated, cactus blocks popping off due to a neighboring solid
     * block, etc.
     */
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos p_189540_5_)
    {
        this.checkForMixing(worldIn, pos, state);
    }

    public boolean checkForMixing(World worldIn, BlockPos pos, IBlockState state)
    {
        if (this.blockMaterial == Material.LAVA)
        {
            boolean flag = false;

            for (EnumFacing enumfacing : EnumFacing.values())
            {
                if (enumfacing != EnumFacing.DOWN && worldIn.getBlockState(pos.offset(enumfacing)).getMaterial() == Material.WATER)
                {
                    flag = true;
                    break;
                }
            }

            if (flag)
            {
                Integer integer = (Integer)state.getValue(LEVEL);

                if (integer.intValue() == 0)
                {
                    worldIn.setBlockState(pos, Blocks.OBSIDIAN.getDefaultState());
                    this.triggerMixEffects(worldIn, pos);
                    return true;
                }

                if (integer.intValue() <= 4)
                {
                    worldIn.setBlockState(pos, Blocks.COBBLESTONE.getDefaultState());
                    this.triggerMixEffects(worldIn, pos);
                    return true;
                }
            }
        }

        return false;
    }

    protected void triggerMixEffects(World worldIn, BlockPos pos)
    {
        double d0 = (double)pos.getX();
        double d1 = (double)pos.getY();
        double d2 = (double)pos.getZ();
        worldIn.playSound((EntityPlayer)null, pos, SoundEvents.BLOCK_LAVA_EXTINGUISH, SoundCategory.BLOCKS, 0.5F, 2.6F + (worldIn.rand.nextFloat() - worldIn.rand.nextFloat()) * 0.8F);

        for (int i = 0; i < 8; ++i)
        {
            worldIn.spawnParticle(EnumParticleTypes.SMOKE_LARGE, d0 + Math.random(), d1 + 1.2D, d2 + Math.random(), 0.0D, 0.0D, 0.0D);
        }
    }

    /**
     * Convert the given metadata into a BlockState for this Block
     */
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(LEVEL, Integer.valueOf(meta));
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    public int getMetaFromState(IBlockState state)
    {
        return ((Integer)state.getValue(LEVEL)).intValue();
    }

    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[] {LEVEL});
    }

    public static BlockDynamicLiquid getFlowingBlock(Material materialIn)
    {
        if (materialIn == Material.WATER)
        {
            return Blocks.FLOWING_WATER;
        }
        else if (materialIn == Material.LAVA)
        {
            return Blocks.FLOWING_LAVA;
        }
        else
        {
            throw new IllegalArgumentException("Invalid material");
        }
    }

    public static BlockStaticLiquid getStaticBlock(Material materialIn)
    {
        if (materialIn == Material.WATER)
        {
            return Blocks.WATER;
        }
        else if (materialIn == Material.LAVA)
        {
            return Blocks.LAVA;
        }
        else
        {
            throw new IllegalArgumentException("Invalid material");
        }
    }

    public static float func_190973_f(IBlockState p_190973_0_, IBlockAccess p_190973_1_, BlockPos p_190973_2_)
    {
        int i = ((Integer)p_190973_0_.getValue(LEVEL)).intValue();
        return (i & 7) == 0 && p_190973_1_.getBlockState(p_190973_2_.up()).getMaterial() == Material.WATER ? 1.0F : 1.0F - getLiquidHeightPercent(i);
    }

    public static float func_190972_g(IBlockState p_190972_0_, IBlockAccess p_190972_1_, BlockPos p_190972_2_)
    {
        return (float)p_190972_2_.getY() + func_190973_f(p_190972_0_, p_190972_1_, p_190972_2_);
    }

    public BlockFaceShape func_193383_a(IBlockAccess p_193383_1_, IBlockState p_193383_2_, BlockPos p_193383_3_, EnumFacing p_193383_4_)
    {
        return BlockFaceShape.UNDEFINED;
    }
}
