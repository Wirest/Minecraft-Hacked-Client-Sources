package net.minecraft.block;

import java.util.Iterator;
import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeColorHelper;

public abstract class BlockLiquid extends Block
{
    public static final PropertyInteger LEVEL = PropertyInteger.create("level", 0, 15);

    protected BlockLiquid(Material materialIn)
    {
        super(materialIn);
        this.setDefaultState(this.blockState.getBaseState().withProperty(LEVEL, Integer.valueOf(0)));
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        this.setTickRandomly(true);
    }

    @Override
	public boolean isPassable(IBlockAccess worldIn, BlockPos pos)
    {
        return this.blockMaterial != Material.lava;
    }

    @Override
	public int colorMultiplier(IBlockAccess worldIn, BlockPos pos, int renderPass)
    {
        return this.blockMaterial == Material.water ? BiomeColorHelper.getWaterColorAtPos(worldIn, pos) : 16777215;
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

        return (meta + 1) / 9.0F;
    }

    protected int getLevel(IBlockAccess worldIn, BlockPos pos)
    {
        return worldIn.getBlockState(pos).getBlock().getMaterial() == this.blockMaterial ? ((Integer)worldIn.getBlockState(pos).getValue(LEVEL)).intValue() : -1;
    }

    protected int getEffectiveFlowDecay(IBlockAccess worldIn, BlockPos pos)
    {
        int var3 = this.getLevel(worldIn, pos);
        return var3 >= 8 ? 0 : var3;
    }

    @Override
	public boolean isFullCube()
    {
        return false;
    }

    @Override
	public boolean isOpaqueCube()
    {
        return false;
    }

    @Override
	public boolean canCollideCheck(IBlockState state, boolean hitIfLiquid)
    {
        return hitIfLiquid && ((Integer)state.getValue(LEVEL)).intValue() == 0;
    }

    /**
     * Whether this Block is solid on the given Side
     */
    @Override
	public boolean isBlockSolid(IBlockAccess worldIn, BlockPos pos, EnumFacing side)
    {
        Material var4 = worldIn.getBlockState(pos).getBlock().getMaterial();
        return var4 == this.blockMaterial ? false : (side == EnumFacing.UP ? true : (var4 == Material.ice ? false : super.isBlockSolid(worldIn, pos, side)));
    }

    @Override
	public boolean shouldSideBeRendered(IBlockAccess worldIn, BlockPos pos, EnumFacing side)
    {
        return worldIn.getBlockState(pos).getBlock().getMaterial() == this.blockMaterial ? false : (side == EnumFacing.UP ? true : super.shouldSideBeRendered(worldIn, pos, side));
    }

    public boolean func_176364_g(IBlockAccess blockAccess, BlockPos pos)
    {
        for (int var3 = -1; var3 <= 1; ++var3)
        {
            for (int var4 = -1; var4 <= 1; ++var4)
            {
                IBlockState var5 = blockAccess.getBlockState(pos.add(var3, 0, var4));
                Block var6 = var5.getBlock();
                Material var7 = var6.getMaterial();

                if (var7 != this.blockMaterial && !var6.isFullBlock())
                {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
	public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state)
    {
        return null;
    }

    /**
     * The type of render function that is called for this block
     */
    @Override
	public int getRenderType()
    {
        return 1;
    }

    /**
     * Get the Item that this Block should drop when harvested.
     *  
     * @param fortune the level of the Fortune enchantment on the player's tool
     */
    @Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        return null;
    }

    /**
     * Returns the quantity of items to drop on block destruction.
     */
    @Override
	public int quantityDropped(Random random)
    {
        return 0;
    }

    protected Vec3 getFlowVector(IBlockAccess worldIn, BlockPos pos)
    {
        Vec3 var3 = new Vec3(0.0D, 0.0D, 0.0D);
        int var4 = this.getEffectiveFlowDecay(worldIn, pos);
        Iterator var5 = EnumFacing.Plane.HORIZONTAL.iterator();
        EnumFacing var6;
        BlockPos var7;

        while (var5.hasNext())
        {
            var6 = (EnumFacing)var5.next();
            var7 = pos.offset(var6);
            int var8 = this.getEffectiveFlowDecay(worldIn, var7);
            int var9;

            if (var8 < 0)
            {
                if (!worldIn.getBlockState(var7).getBlock().getMaterial().blocksMovement())
                {
                    var8 = this.getEffectiveFlowDecay(worldIn, var7.down());

                    if (var8 >= 0)
                    {
                        var9 = var8 - (var4 - 8);
                        var3 = var3.addVector((var7.getX() - pos.getX()) * var9, (var7.getY() - pos.getY()) * var9, (var7.getZ() - pos.getZ()) * var9);
                    }
                }
            }
            else if (var8 >= 0)
            {
                var9 = var8 - var4;
                var3 = var3.addVector((var7.getX() - pos.getX()) * var9, (var7.getY() - pos.getY()) * var9, (var7.getZ() - pos.getZ()) * var9);
            }
        }

        if (((Integer)worldIn.getBlockState(pos).getValue(LEVEL)).intValue() >= 8)
        {
            var5 = EnumFacing.Plane.HORIZONTAL.iterator();

            while (var5.hasNext())
            {
                var6 = (EnumFacing)var5.next();
                var7 = pos.offset(var6);

                if (this.isBlockSolid(worldIn, var7, var6) || this.isBlockSolid(worldIn, var7.up(), var6))
                {
                    var3 = var3.normalize().addVector(0.0D, -6.0D, 0.0D);
                    break;
                }
            }
        }

        return var3.normalize();
    }

    @Override
	public Vec3 modifyAcceleration(World worldIn, BlockPos pos, Entity entityIn, Vec3 motion)
    {
        return motion.add(this.getFlowVector(worldIn, pos));
    }

    /**
     * How many world ticks before ticking
     */
    @Override
	public int tickRate(World worldIn)
    {
        return this.blockMaterial == Material.water ? 5 : (this.blockMaterial == Material.lava ? (worldIn.provider.getHasNoSky() ? 10 : 30) : 0);
    }

    @Override
	public int getMixedBrightnessForBlock(IBlockAccess worldIn, BlockPos pos)
    {
        int var3 = worldIn.getCombinedLight(pos, 0);
        int var4 = worldIn.getCombinedLight(pos.up(), 0);
        int var5 = var3 & 255;
        int var6 = var4 & 255;
        int var7 = var3 >> 16 & 255;
        int var8 = var4 >> 16 & 255;
        return (var5 > var6 ? var5 : var6) | (var7 > var8 ? var7 : var8) << 16;
    }

    @Override
	public EnumWorldBlockLayer getBlockLayer()
    {
        return this.blockMaterial == Material.water ? EnumWorldBlockLayer.TRANSLUCENT : EnumWorldBlockLayer.SOLID;
    }

    @Override
	public void randomDisplayTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {
        double var5 = pos.getX();
        double var7 = pos.getY();
        double var9 = pos.getZ();

        if (this.blockMaterial == Material.water)
        {
            int var11 = ((Integer)state.getValue(LEVEL)).intValue();

            if (var11 > 0 && var11 < 8)
            {
                if (rand.nextInt(64) == 0)
                {
                    worldIn.playSound(var5 + 0.5D, var7 + 0.5D, var9 + 0.5D, "liquid.water", rand.nextFloat() * 0.25F + 0.75F, rand.nextFloat() * 1.0F + 0.5F, false);
                }
            }
            else if (rand.nextInt(10) == 0)
            {
                worldIn.spawnParticle(EnumParticleTypes.SUSPENDED, var5 + rand.nextFloat(), var7 + rand.nextFloat(), var9 + rand.nextFloat(), 0.0D, 0.0D, 0.0D, new int[0]);
            }
        }

        if (this.blockMaterial == Material.lava && worldIn.getBlockState(pos.up()).getBlock().getMaterial() == Material.air && !worldIn.getBlockState(pos.up()).getBlock().isOpaqueCube())
        {
            if (rand.nextInt(100) == 0)
            {
                double var18 = var5 + rand.nextFloat();
                double var13 = var7 + this.maxY;
                double var15 = var9 + rand.nextFloat();
                worldIn.spawnParticle(EnumParticleTypes.LAVA, var18, var13, var15, 0.0D, 0.0D, 0.0D, new int[0]);
                worldIn.playSound(var18, var13, var15, "liquid.lavapop", 0.2F + rand.nextFloat() * 0.2F, 0.9F + rand.nextFloat() * 0.15F, false);
            }

            if (rand.nextInt(200) == 0)
            {
                worldIn.playSound(var5, var7, var9, "liquid.lava", 0.2F + rand.nextFloat() * 0.2F, 0.9F + rand.nextFloat() * 0.15F, false);
            }
        }

        if (rand.nextInt(10) == 0 && World.doesBlockHaveSolidTopSurface(worldIn, pos.down()))
        {
            Material var19 = worldIn.getBlockState(pos.down(2)).getBlock().getMaterial();

            if (!var19.blocksMovement() && !var19.isLiquid())
            {
                double var12 = var5 + rand.nextFloat();
                double var14 = var7 - 1.05D;
                double var16 = var9 + rand.nextFloat();

                if (this.blockMaterial == Material.water)
                {
                    worldIn.spawnParticle(EnumParticleTypes.DRIP_WATER, var12, var14, var16, 0.0D, 0.0D, 0.0D, new int[0]);
                }
                else
                {
                    worldIn.spawnParticle(EnumParticleTypes.DRIP_LAVA, var12, var14, var16, 0.0D, 0.0D, 0.0D, new int[0]);
                }
            }
        }
    }

    public static double getFlowDirection(IBlockAccess worldIn, BlockPos pos, Material materialIn)
    {
        Vec3 var3 = getFlowingBlock(materialIn).getFlowVector(worldIn, pos);
        return var3.xCoord == 0.0D && var3.zCoord == 0.0D ? -1000.0D : Math.atan2(var3.zCoord, var3.xCoord) - (Math.PI / 2D);
    }

    @Override
	public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
    {
        this.checkForMixing(worldIn, pos, state);
    }

    /**
     * Called when a neighboring block changes.
     */
    @Override
	public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock)
    {
        this.checkForMixing(worldIn, pos, state);
    }

    public boolean checkForMixing(World worldIn, BlockPos pos, IBlockState state)
    {
        if (this.blockMaterial == Material.lava)
        {
            boolean var4 = false;
            EnumFacing[] var5 = EnumFacing.values();
            int var6 = var5.length;

            for (int var7 = 0; var7 < var6; ++var7)
            {
                EnumFacing var8 = var5[var7];

                if (var8 != EnumFacing.DOWN && worldIn.getBlockState(pos.offset(var8)).getBlock().getMaterial() == Material.water)
                {
                    var4 = true;
                    break;
                }
            }

            if (var4)
            {
                Integer var9 = (Integer)state.getValue(LEVEL);

                if (var9.intValue() == 0)
                {
                    worldIn.setBlockState(pos, Blocks.obsidian.getDefaultState());
                    this.triggerMixEffects(worldIn, pos);
                    return true;
                }

                if (var9.intValue() <= 4)
                {
                    worldIn.setBlockState(pos, Blocks.cobblestone.getDefaultState());
                    this.triggerMixEffects(worldIn, pos);
                    return true;
                }
            }
        }

        return false;
    }

    protected void triggerMixEffects(World worldIn, BlockPos pos)
    {
        double var3 = pos.getX();
        double var5 = pos.getY();
        double var7 = pos.getZ();
        worldIn.playSoundEffect(var3 + 0.5D, var5 + 0.5D, var7 + 0.5D, "random.fizz", 0.5F, 2.6F + (worldIn.rand.nextFloat() - worldIn.rand.nextFloat()) * 0.8F);

        for (int var9 = 0; var9 < 8; ++var9)
        {
            worldIn.spawnParticle(EnumParticleTypes.SMOKE_LARGE, var3 + Math.random(), var5 + 1.2D, var7 + Math.random(), 0.0D, 0.0D, 0.0D, new int[0]);
        }
    }

    /**
     * Convert the given metadata into a BlockState for this Block
     */
    @Override
	public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(LEVEL, Integer.valueOf(meta));
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    @Override
	public int getMetaFromState(IBlockState state)
    {
        return ((Integer)state.getValue(LEVEL)).intValue();
    }

    @Override
	protected BlockState createBlockState()
    {
        return new BlockState(this, new IProperty[] {LEVEL});
    }

    public static BlockDynamicLiquid getFlowingBlock(Material materialIn)
    {
        if (materialIn == Material.water)
        {
            return Blocks.flowing_water;
        }
        else if (materialIn == Material.lava)
        {
            return Blocks.flowing_lava;
        }
        else
        {
            throw new IllegalArgumentException("Invalid material");
        }
    }

    public static BlockStaticLiquid getStaticBlock(Material materialIn)
    {
        if (materialIn == Material.water)
        {
            return Blocks.water;
        }
        else if (materialIn == Material.lava)
        {
            return Blocks.lava;
        }
        else
        {
            throw new IllegalArgumentException("Invalid material");
        }
    }
}
