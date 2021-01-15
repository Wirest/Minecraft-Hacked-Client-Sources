package net.minecraft.block;

import java.util.Random;

import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class BlockCrops extends BlockBush implements IGrowable
{
    public static final PropertyInteger AGE = PropertyInteger.create("age", 0, 7);

    protected BlockCrops()
    {
        this.setDefaultState(this.blockState.getBaseState().withProperty(AGE, Integer.valueOf(0)));
        this.setTickRandomly(true);
        float var1 = 0.5F;
        this.setBlockBounds(0.5F - var1, 0.0F, 0.5F - var1, 0.5F + var1, 0.25F, 0.5F + var1);
        this.setCreativeTab((CreativeTabs)null);
        this.setHardness(0.0F);
        this.setStepSound(soundTypeGrass);
        this.disableStats();
    }

    /**
     * is the block grass, dirt or farmland
     */
    @Override
	protected boolean canPlaceBlockOn(Block ground)
    {
        return ground == Blocks.farmland;
    }

    @Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {
        super.updateTick(worldIn, pos, state, rand);

        if (worldIn.getLightFromNeighbors(pos.up()) >= 9)
        {
            int var5 = ((Integer)state.getValue(AGE)).intValue();

            if (var5 < 7)
            {
                float var6 = getGrowthChance(this, worldIn, pos);

                if (rand.nextInt((int)(25.0F / var6) + 1) == 0)
                {
                    worldIn.setBlockState(pos, state.withProperty(AGE, Integer.valueOf(var5 + 1)), 2);
                }
            }
        }
    }

    public void grow(World worldIn, BlockPos pos, IBlockState state)
    {
        int var4 = ((Integer)state.getValue(AGE)).intValue() + MathHelper.getRandomIntegerInRange(worldIn.rand, 2, 5);

        if (var4 > 7)
        {
            var4 = 7;
        }

        worldIn.setBlockState(pos, state.withProperty(AGE, Integer.valueOf(var4)), 2);
    }

    protected static float getGrowthChance(Block blockIn, World worldIn, BlockPos pos)
    {
        float var3 = 1.0F;
        BlockPos var4 = pos.down();

        for (int var5 = -1; var5 <= 1; ++var5)
        {
            for (int var6 = -1; var6 <= 1; ++var6)
            {
                float var7 = 0.0F;
                IBlockState var8 = worldIn.getBlockState(var4.add(var5, 0, var6));

                if (var8.getBlock() == Blocks.farmland)
                {
                    var7 = 1.0F;

                    if (((Integer)var8.getValue(BlockFarmland.MOISTURE)).intValue() > 0)
                    {
                        var7 = 3.0F;
                    }
                }

                if (var5 != 0 || var6 != 0)
                {
                    var7 /= 4.0F;
                }

                var3 += var7;
            }
        }

        BlockPos var12 = pos.north();
        BlockPos var13 = pos.south();
        BlockPos var14 = pos.west();
        BlockPos var15 = pos.east();
        boolean var9 = blockIn == worldIn.getBlockState(var14).getBlock() || blockIn == worldIn.getBlockState(var15).getBlock();
        boolean var10 = blockIn == worldIn.getBlockState(var12).getBlock() || blockIn == worldIn.getBlockState(var13).getBlock();

        if (var9 && var10)
        {
            var3 /= 2.0F;
        }
        else
        {
            boolean var11 = blockIn == worldIn.getBlockState(var14.north()).getBlock() || blockIn == worldIn.getBlockState(var15.north()).getBlock() || blockIn == worldIn.getBlockState(var15.south()).getBlock() || blockIn == worldIn.getBlockState(var14.south()).getBlock();

            if (var11)
            {
                var3 /= 2.0F;
            }
        }

        return var3;
    }

    @Override
	public boolean canBlockStay(World worldIn, BlockPos pos, IBlockState state)
    {
        return (worldIn.getLight(pos) >= 8 || worldIn.canSeeSky(pos)) && this.canPlaceBlockOn(worldIn.getBlockState(pos.down()).getBlock());
    }

    protected Item getSeed()
    {
        return Items.wheat_seeds;
    }

    protected Item getCrop()
    {
        return Items.wheat;
    }

    /**
     * Spawns this Block's drops into the World as EntityItems.
     *  
     * @param chance The chance that each Item is actually spawned (1.0 = always, 0.0 = never)
     * @param fortune The player's fortune level
     */
    @Override
	public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune)
    {
        super.dropBlockAsItemWithChance(worldIn, pos, state, chance, 0);

        if (!worldIn.isRemote)
        {
            int var6 = ((Integer)state.getValue(AGE)).intValue();

            if (var6 >= 7)
            {
                int var7 = 3 + fortune;

                for (int var8 = 0; var8 < var7; ++var8)
                {
                    if (worldIn.rand.nextInt(15) <= var6)
                    {
                        spawnAsEntity(worldIn, pos, new ItemStack(this.getSeed(), 1, 0));
                    }
                }
            }
        }
    }

    /**
     * Get the Item that this Block should drop when harvested.
     *  
     * @param fortune the level of the Fortune enchantment on the player's tool
     */
    @Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        return ((Integer)state.getValue(AGE)).intValue() == 7 ? this.getCrop() : this.getSeed();
    }

    @Override
	public Item getItem(World worldIn, BlockPos pos)
    {
        return this.getSeed();
    }

    /**
     * Whether this IGrowable can grow
     */
    @Override
	public boolean canGrow(World worldIn, BlockPos pos, IBlockState state, boolean isClient)
    {
        return ((Integer)state.getValue(AGE)).intValue() < 7;
    }

    @Override
	public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, IBlockState state)
    {
        return true;
    }

    @Override
	public void grow(World worldIn, Random rand, BlockPos pos, IBlockState state)
    {
        this.grow(worldIn, pos, state);
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
        return new BlockState(this, new IProperty[] {AGE});
    }
}
