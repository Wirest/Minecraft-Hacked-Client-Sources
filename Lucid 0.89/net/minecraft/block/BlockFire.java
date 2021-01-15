package net.minecraft.block;

import java.util.Map;
import java.util.Random;

import com.google.common.collect.Maps;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldProviderEnd;

public class BlockFire extends Block
{
    public static final PropertyInteger AGE = PropertyInteger.create("age", 0, 15);
    public static final PropertyBool FLIP = PropertyBool.create("flip");
    public static final PropertyBool ALT = PropertyBool.create("alt");
    public static final PropertyBool NORTH = PropertyBool.create("north");
    public static final PropertyBool EAST = PropertyBool.create("east");
    public static final PropertyBool SOUTH = PropertyBool.create("south");
    public static final PropertyBool WEST = PropertyBool.create("west");
    public static final PropertyInteger UPPER = PropertyInteger.create("upper", 0, 2);
    private final Map encouragements = Maps.newIdentityHashMap();
    private final Map flammabilities = Maps.newIdentityHashMap();

    /**
     * Get the actual Block state of this Block at the given position. This applies properties not visible in the
     * metadata, such as fence connections.
     */
    @Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
    {
        int var4 = pos.getX();
        int var5 = pos.getY();
        int var6 = pos.getZ();

        if (!World.doesBlockHaveSolidTopSurface(worldIn, pos.down()) && !Blocks.fire.canCatchFire(worldIn, pos.down()))
        {
            boolean var7 = (var4 + var5 + var6 & 1) == 1;
            boolean var8 = (var4 / 2 + var5 / 2 + var6 / 2 & 1) == 1;
            int var9 = 0;

            if (this.canCatchFire(worldIn, pos.up()))
            {
                var9 = var7 ? 1 : 2;
            }

            return state.withProperty(NORTH, Boolean.valueOf(this.canCatchFire(worldIn, pos.north()))).withProperty(EAST, Boolean.valueOf(this.canCatchFire(worldIn, pos.east()))).withProperty(SOUTH, Boolean.valueOf(this.canCatchFire(worldIn, pos.south()))).withProperty(WEST, Boolean.valueOf(this.canCatchFire(worldIn, pos.west()))).withProperty(UPPER, Integer.valueOf(var9)).withProperty(FLIP, Boolean.valueOf(var8)).withProperty(ALT, Boolean.valueOf(var7));
        }
        else
        {
            return this.getDefaultState();
        }
    }

    protected BlockFire()
    {
        super(Material.fire);
        this.setDefaultState(this.blockState.getBaseState().withProperty(AGE, Integer.valueOf(0)).withProperty(FLIP, Boolean.valueOf(false)).withProperty(ALT, Boolean.valueOf(false)).withProperty(NORTH, Boolean.valueOf(false)).withProperty(EAST, Boolean.valueOf(false)).withProperty(SOUTH, Boolean.valueOf(false)).withProperty(WEST, Boolean.valueOf(false)).withProperty(UPPER, Integer.valueOf(0)));
        this.setTickRandomly(true);
    }

    public static void init()
    {
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

    public void setFireInfo(Block blockIn, int encouragement, int flammability)
    {
        this.encouragements.put(blockIn, Integer.valueOf(encouragement));
        this.flammabilities.put(blockIn, Integer.valueOf(flammability));
    }

    @Override
	public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state)
    {
        return null;
    }

    @Override
	public boolean isOpaqueCube()
    {
        return false;
    }

    @Override
	public boolean isFullCube()
    {
        return false;
    }

    /**
     * Returns the quantity of items to drop on block destruction.
     */
    @Override
	public int quantityDropped(Random random)
    {
        return 0;
    }

    /**
     * How many world ticks before ticking
     */
    @Override
	public int tickRate(World worldIn)
    {
        return 30;
    }

    @Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {
        if (worldIn.getGameRules().getGameRuleBooleanValue("doFireTick"))
        {
            if (!this.canPlaceBlockAt(worldIn, pos))
            {
                worldIn.setBlockToAir(pos);
            }

            Block var5 = worldIn.getBlockState(pos.down()).getBlock();
            boolean var6 = var5 == Blocks.netherrack;

            if (worldIn.provider instanceof WorldProviderEnd && var5 == Blocks.bedrock)
            {
                var6 = true;
            }

            if (!var6 && worldIn.isRaining() && this.canDie(worldIn, pos))
            {
                worldIn.setBlockToAir(pos);
            }
            else
            {
                int var7 = ((Integer)state.getValue(AGE)).intValue();

                if (var7 < 15)
                {
                    state = state.withProperty(AGE, Integer.valueOf(var7 + rand.nextInt(3) / 2));
                    worldIn.setBlockState(pos, state, 4);
                }

                worldIn.scheduleUpdate(pos, this, this.tickRate(worldIn) + rand.nextInt(10));

                if (!var6)
                {
                    if (!this.canNeighborCatchFire(worldIn, pos))
                    {
                        if (!World.doesBlockHaveSolidTopSurface(worldIn, pos.down()) || var7 > 3)
                        {
                            worldIn.setBlockToAir(pos);
                        }

                        return;
                    }

                    if (!this.canCatchFire(worldIn, pos.down()) && var7 == 15 && rand.nextInt(4) == 0)
                    {
                        worldIn.setBlockToAir(pos);
                        return;
                    }
                }

                boolean var8 = worldIn.isBlockinHighHumidity(pos);
                byte var9 = 0;

                if (var8)
                {
                    var9 = -50;
                }

                this.catchOnFire(worldIn, pos.east(), 300 + var9, rand, var7);
                this.catchOnFire(worldIn, pos.west(), 300 + var9, rand, var7);
                this.catchOnFire(worldIn, pos.down(), 250 + var9, rand, var7);
                this.catchOnFire(worldIn, pos.up(), 250 + var9, rand, var7);
                this.catchOnFire(worldIn, pos.north(), 300 + var9, rand, var7);
                this.catchOnFire(worldIn, pos.south(), 300 + var9, rand, var7);

                for (int var10 = -1; var10 <= 1; ++var10)
                {
                    for (int var11 = -1; var11 <= 1; ++var11)
                    {
                        for (int var12 = -1; var12 <= 4; ++var12)
                        {
                            if (var10 != 0 || var12 != 0 || var11 != 0)
                            {
                                int var13 = 100;

                                if (var12 > 1)
                                {
                                    var13 += (var12 - 1) * 100;
                                }

                                BlockPos var14 = pos.add(var10, var12, var11);
                                int var15 = this.getNeighborEncouragement(worldIn, var14);

                                if (var15 > 0)
                                {
                                    int var16 = (var15 + 40 + worldIn.getDifficulty().getDifficultyId() * 7) / (var7 + 30);

                                    if (var8)
                                    {
                                        var16 /= 2;
                                    }

                                    if (var16 > 0 && rand.nextInt(var13) <= var16 && (!worldIn.isRaining() || !this.canDie(worldIn, var14)))
                                    {
                                        int var17 = var7 + rand.nextInt(5) / 4;

                                        if (var17 > 15)
                                        {
                                            var17 = 15;
                                        }

                                        worldIn.setBlockState(var14, state.withProperty(AGE, Integer.valueOf(var17)), 3);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    protected boolean canDie(World worldIn, BlockPos pos)
    {
        return worldIn.canLightningStrike(pos) || worldIn.canLightningStrike(pos.west()) || worldIn.canLightningStrike(pos.east()) || worldIn.canLightningStrike(pos.north()) || worldIn.canLightningStrike(pos.south());
    }

    @Override
	public boolean requiresUpdates()
    {
        return false;
    }

    private int getFlammability(Block blockIn)
    {
        Integer var2 = (Integer)this.flammabilities.get(blockIn);
        return var2 == null ? 0 : var2.intValue();
    }

    private int getEncouragement(Block blockIn)
    {
        Integer var2 = (Integer)this.encouragements.get(blockIn);
        return var2 == null ? 0 : var2.intValue();
    }

    private void catchOnFire(World worldIn, BlockPos pos, int chance, Random random, int age)
    {
        int var6 = this.getFlammability(worldIn.getBlockState(pos).getBlock());

        if (random.nextInt(chance) < var6)
        {
            IBlockState var7 = worldIn.getBlockState(pos);

            if (random.nextInt(age + 10) < 5 && !worldIn.canLightningStrike(pos))
            {
                int var8 = age + random.nextInt(5) / 4;

                if (var8 > 15)
                {
                    var8 = 15;
                }

                worldIn.setBlockState(pos, this.getDefaultState().withProperty(AGE, Integer.valueOf(var8)), 3);
            }
            else
            {
                worldIn.setBlockToAir(pos);
            }

            if (var7.getBlock() == Blocks.tnt)
            {
                Blocks.tnt.onBlockDestroyedByPlayer(worldIn, pos, var7.withProperty(BlockTNT.EXPLODE, Boolean.valueOf(true)));
            }
        }
    }

    private boolean canNeighborCatchFire(World worldIn, BlockPos pos)
    {
        EnumFacing[] var3 = EnumFacing.values();
        int var4 = var3.length;

        for (int var5 = 0; var5 < var4; ++var5)
        {
            EnumFacing var6 = var3[var5];

            if (this.canCatchFire(worldIn, pos.offset(var6)))
            {
                return true;
            }
        }

        return false;
    }

    private int getNeighborEncouragement(World worldIn, BlockPos pos)
    {
        if (!worldIn.isAirBlock(pos))
        {
            return 0;
        }
        else
        {
            int var3 = 0;
            EnumFacing[] var4 = EnumFacing.values();
            int var5 = var4.length;

            for (int var6 = 0; var6 < var5; ++var6)
            {
                EnumFacing var7 = var4[var6];
                var3 = Math.max(this.getEncouragement(worldIn.getBlockState(pos.offset(var7)).getBlock()), var3);
            }

            return var3;
        }
    }

    /**
     * Returns if this block is collidable (only used by Fire). Args: x, y, z
     */
    @Override
	public boolean isCollidable()
    {
        return false;
    }

    /**
     * Checks if the block can be caught on fire
     */
    public boolean canCatchFire(IBlockAccess worldIn, BlockPos pos)
    {
        return this.getEncouragement(worldIn.getBlockState(pos).getBlock()) > 0;
    }

    @Override
	public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
    {
        return World.doesBlockHaveSolidTopSurface(worldIn, pos.down()) || this.canNeighborCatchFire(worldIn, pos);
    }

    /**
     * Called when a neighboring block changes.
     */
    @Override
	public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock)
    {
        if (!World.doesBlockHaveSolidTopSurface(worldIn, pos.down()) && !this.canNeighborCatchFire(worldIn, pos))
        {
            worldIn.setBlockToAir(pos);
        }
    }

    @Override
	public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
    {
        if (worldIn.provider.getDimensionId() > 0 || !Blocks.portal.func_176548_d(worldIn, pos))
        {
            if (!World.doesBlockHaveSolidTopSurface(worldIn, pos.down()) && !this.canNeighborCatchFire(worldIn, pos))
            {
                worldIn.setBlockToAir(pos);
            }
            else
            {
                worldIn.scheduleUpdate(pos, this, this.tickRate(worldIn) + worldIn.rand.nextInt(10));
            }
        }
    }

    @Override
	public void randomDisplayTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {
        if (rand.nextInt(24) == 0)
        {
            worldIn.playSound(pos.getX() + 0.5F, pos.getY() + 0.5F, pos.getZ() + 0.5F, "fire.fire", 1.0F + rand.nextFloat(), rand.nextFloat() * 0.7F + 0.3F, false);
        }

        int var5;
        double var6;
        double var8;
        double var10;

        if (!World.doesBlockHaveSolidTopSurface(worldIn, pos.down()) && !Blocks.fire.canCatchFire(worldIn, pos.down()))
        {
            if (Blocks.fire.canCatchFire(worldIn, pos.west()))
            {
                for (var5 = 0; var5 < 2; ++var5)
                {
                    var6 = pos.getX() + rand.nextDouble() * 0.10000000149011612D;
                    var8 = pos.getY() + rand.nextDouble();
                    var10 = pos.getZ() + rand.nextDouble();
                    worldIn.spawnParticle(EnumParticleTypes.SMOKE_LARGE, var6, var8, var10, 0.0D, 0.0D, 0.0D, new int[0]);
                }
            }

            if (Blocks.fire.canCatchFire(worldIn, pos.east()))
            {
                for (var5 = 0; var5 < 2; ++var5)
                {
                    var6 = pos.getX() + 1 - rand.nextDouble() * 0.10000000149011612D;
                    var8 = pos.getY() + rand.nextDouble();
                    var10 = pos.getZ() + rand.nextDouble();
                    worldIn.spawnParticle(EnumParticleTypes.SMOKE_LARGE, var6, var8, var10, 0.0D, 0.0D, 0.0D, new int[0]);
                }
            }

            if (Blocks.fire.canCatchFire(worldIn, pos.north()))
            {
                for (var5 = 0; var5 < 2; ++var5)
                {
                    var6 = pos.getX() + rand.nextDouble();
                    var8 = pos.getY() + rand.nextDouble();
                    var10 = pos.getZ() + rand.nextDouble() * 0.10000000149011612D;
                    worldIn.spawnParticle(EnumParticleTypes.SMOKE_LARGE, var6, var8, var10, 0.0D, 0.0D, 0.0D, new int[0]);
                }
            }

            if (Blocks.fire.canCatchFire(worldIn, pos.south()))
            {
                for (var5 = 0; var5 < 2; ++var5)
                {
                    var6 = pos.getX() + rand.nextDouble();
                    var8 = pos.getY() + rand.nextDouble();
                    var10 = pos.getZ() + 1 - rand.nextDouble() * 0.10000000149011612D;
                    worldIn.spawnParticle(EnumParticleTypes.SMOKE_LARGE, var6, var8, var10, 0.0D, 0.0D, 0.0D, new int[0]);
                }
            }

            if (Blocks.fire.canCatchFire(worldIn, pos.up()))
            {
                for (var5 = 0; var5 < 2; ++var5)
                {
                    var6 = pos.getX() + rand.nextDouble();
                    var8 = pos.getY() + 1 - rand.nextDouble() * 0.10000000149011612D;
                    var10 = pos.getZ() + rand.nextDouble();
                    worldIn.spawnParticle(EnumParticleTypes.SMOKE_LARGE, var6, var8, var10, 0.0D, 0.0D, 0.0D, new int[0]);
                }
            }
        }
        else
        {
            for (var5 = 0; var5 < 3; ++var5)
            {
                var6 = pos.getX() + rand.nextDouble();
                var8 = pos.getY() + rand.nextDouble() * 0.5D + 0.5D;
                var10 = pos.getZ() + rand.nextDouble();
                worldIn.spawnParticle(EnumParticleTypes.SMOKE_LARGE, var6, var8, var10, 0.0D, 0.0D, 0.0D, new int[0]);
            }
        }
    }

    /**
     * Get the MapColor for this Block and the given BlockState
     */
    @Override
	public MapColor getMapColor(IBlockState state)
    {
        return MapColor.tntColor;
    }

    @Override
	public EnumWorldBlockLayer getBlockLayer()
    {
        return EnumWorldBlockLayer.CUTOUT;
    }

    /**
     * Convert the given metadata into a BlockState for this Block
     */
    @Override
	public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(AGE, Integer.valueOf(meta));
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    @Override
	public int getMetaFromState(IBlockState state)
    {
        return ((Integer)state.getValue(AGE)).intValue();
    }

    @Override
	protected BlockState createBlockState()
    {
        return new BlockState(this, new IProperty[] {AGE, NORTH, EAST, SOUTH, WEST, UPPER, FLIP, ALT});
    }
}
