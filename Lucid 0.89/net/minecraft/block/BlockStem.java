package net.minecraft.block;

import java.util.Iterator;
import java.util.Random;

import com.google.common.base.Predicate;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockStem extends BlockBush implements IGrowable
{
    public static final PropertyInteger AGE = PropertyInteger.create("age", 0, 7);
    public static final PropertyDirection FACING = PropertyDirection.create("facing", new Predicate()
    {
        public boolean apply(EnumFacing facing)
        {
            return facing != EnumFacing.DOWN;
        }
        @Override
		public boolean apply(Object p_apply_1_)
        {
            return this.apply((EnumFacing)p_apply_1_);
        }
    });
    private final Block crop;

    protected BlockStem(Block crop)
    {
        this.setDefaultState(this.blockState.getBaseState().withProperty(AGE, Integer.valueOf(0)).withProperty(FACING, EnumFacing.UP));
        this.crop = crop;
        this.setTickRandomly(true);
        float var2 = 0.125F;
        this.setBlockBounds(0.5F - var2, 0.0F, 0.5F - var2, 0.5F + var2, 0.25F, 0.5F + var2);
        this.setCreativeTab((CreativeTabs)null);
    }

    /**
     * Get the actual Block state of this Block at the given position. This applies properties not visible in the
     * metadata, such as fence connections.
     */
    @Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
    {
        state = state.withProperty(FACING, EnumFacing.UP);
        Iterator var4 = EnumFacing.Plane.HORIZONTAL.iterator();

        while (var4.hasNext())
        {
            EnumFacing var5 = (EnumFacing)var4.next();

            if (worldIn.getBlockState(pos.offset(var5)).getBlock() == this.crop)
            {
                state = state.withProperty(FACING, var5);
                break;
            }
        }

        return state;
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
            float var5 = BlockCrops.getGrowthChance(this, worldIn, pos);

            if (rand.nextInt((int)(25.0F / var5) + 1) == 0)
            {
                int var6 = ((Integer)state.getValue(AGE)).intValue();

                if (var6 < 7)
                {
                    state = state.withProperty(AGE, Integer.valueOf(var6 + 1));
                    worldIn.setBlockState(pos, state, 2);
                }
                else
                {
                    Iterator var7 = EnumFacing.Plane.HORIZONTAL.iterator();

                    while (var7.hasNext())
                    {
                        EnumFacing var8 = (EnumFacing)var7.next();

                        if (worldIn.getBlockState(pos.offset(var8)).getBlock() == this.crop)
                        {
                            return;
                        }
                    }

                    pos = pos.offset(EnumFacing.Plane.HORIZONTAL.random(rand));
                    Block var9 = worldIn.getBlockState(pos.down()).getBlock();

                    if (worldIn.getBlockState(pos).getBlock().blockMaterial == Material.air && (var9 == Blocks.farmland || var9 == Blocks.dirt || var9 == Blocks.grass))
                    {
                        worldIn.setBlockState(pos, this.crop.getDefaultState());
                    }
                }
            }
        }
    }

    public void growStem(World worldIn, BlockPos pos, IBlockState state)
    {
        int var4 = ((Integer)state.getValue(AGE)).intValue() + MathHelper.getRandomIntegerInRange(worldIn.rand, 2, 5);
        worldIn.setBlockState(pos, state.withProperty(AGE, Integer.valueOf(Math.min(7, var4))), 2);
    }

    @Override
	public int getRenderColor(IBlockState state)
    {
        if (state.getBlock() != this)
        {
            return super.getRenderColor(state);
        }
        else
        {
            int var2 = ((Integer)state.getValue(AGE)).intValue();
            int var3 = var2 * 32;
            int var4 = 255 - var2 * 8;
            int var5 = var2 * 4;
            return var3 << 16 | var4 << 8 | var5;
        }
    }

    @Override
	public int colorMultiplier(IBlockAccess worldIn, BlockPos pos, int renderPass)
    {
        return this.getRenderColor(worldIn.getBlockState(pos));
    }

    /**
     * Sets the block's bounds for rendering it as an item
     */
    @Override
	public void setBlockBoundsForItemRender()
    {
        float var1 = 0.125F;
        this.setBlockBounds(0.5F - var1, 0.0F, 0.5F - var1, 0.5F + var1, 0.25F, 0.5F + var1);
    }

    @Override
	public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos)
    {
        this.maxY = (((Integer)worldIn.getBlockState(pos).getValue(AGE)).intValue() * 2 + 2) / 16.0F;
        float var3 = 0.125F;
        this.setBlockBounds(0.5F - var3, 0.0F, 0.5F - var3, 0.5F + var3, (float)this.maxY, 0.5F + var3);
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
        super.dropBlockAsItemWithChance(worldIn, pos, state, chance, fortune);

        if (!worldIn.isRemote)
        {
            Item var6 = this.getSeedItem();

            if (var6 != null)
            {
                int var7 = ((Integer)state.getValue(AGE)).intValue();

                for (int var8 = 0; var8 < 3; ++var8)
                {
                    if (worldIn.rand.nextInt(15) <= var7)
                    {
                        spawnAsEntity(worldIn, pos, new ItemStack(var6));
                    }
                }
            }
        }
    }

    protected Item getSeedItem()
    {
        return this.crop == Blocks.pumpkin ? Items.pumpkin_seeds : (this.crop == Blocks.melon_block ? Items.melon_seeds : null);
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

    @Override
	public Item getItem(World worldIn, BlockPos pos)
    {
        Item var3 = this.getSeedItem();
        return var3 != null ? var3 : null;
    }

    /**
     * Whether this IGrowable can grow
     */
    @Override
	public boolean canGrow(World worldIn, BlockPos pos, IBlockState state, boolean isClient)
    {
        return ((Integer)state.getValue(AGE)).intValue() != 7;
    }

    @Override
	public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, IBlockState state)
    {
        return true;
    }

    @Override
	public void grow(World worldIn, Random rand, BlockPos pos, IBlockState state)
    {
        this.growStem(worldIn, pos, state);
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
        return new BlockState(this, new IProperty[] {AGE, FACING});
    }
}
