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
import net.minecraft.world.World;

public class BlockNetherWart extends BlockBush {
    public static final PropertyInteger AGE_PROP = PropertyInteger.create("age", 0, 3);
    private static final String __OBFID = "CL_00000274";

    protected BlockNetherWart() {
        setDefaultState(blockState.getBaseState().withProperty(BlockNetherWart.AGE_PROP, Integer.valueOf(0)));
        setTickRandomly(true);
        float var1 = 0.5F;
        setBlockBounds(0.5F - var1, 0.0F, 0.5F - var1, 0.5F + var1, 0.25F, 0.5F + var1);
        setCreativeTab((CreativeTabs) null);
    }

    /**
     * is the block grass, dirt or farmland
     */
    @Override
    protected boolean canPlaceBlockOn(Block ground) {
        return ground == Blocks.soul_sand;
    }

    @Override
    public boolean canBlockStay(World worldIn, BlockPos p_180671_2_, IBlockState p_180671_3_) {
        return canPlaceBlockOn(worldIn.getBlockState(p_180671_2_.offsetDown()).getBlock());
    }

    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        int var5 = ((Integer) state.getValue(BlockNetherWart.AGE_PROP)).intValue();

        if (var5 < 3 && rand.nextInt(10) == 0) {
            state = state.withProperty(BlockNetherWart.AGE_PROP, Integer.valueOf(var5 + 1));
            worldIn.setBlockState(pos, state, 2);
        }

        super.updateTick(worldIn, pos, state, rand);
    }

    /**
     * Spawns this Block's drops into the World as EntityItems.
     *
     * @param chance  The chance that each Item is actually spawned (1.0 = always,
     *                0.0 = never)
     * @param fortune The player's fortune level
     */
    @Override
    public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune) {
        if (!worldIn.isRemote) {
            int var6 = 1;

            if (((Integer) state.getValue(BlockNetherWart.AGE_PROP)).intValue() >= 3) {
                var6 = 2 + worldIn.rand.nextInt(3);

                if (fortune > 0) {
                    var6 += worldIn.rand.nextInt(fortune + 1);
                }
            }

            for (int var7 = 0; var7 < var6; ++var7) {
                Block.spawnAsEntity(worldIn, pos, new ItemStack(Items.nether_wart));
            }
        }
    }

    /**
     * Get the Item that this Block should drop when harvested.
     *
     * @param fortune the level of the Fortune enchantment on the player's tool
     */
    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return null;
    }

    /**
     * Returns the quantity of items to drop on block destruction.
     */
    @Override
    public int quantityDropped(Random random) {
        return 0;
    }

    @Override
    public Item getItem(World worldIn, BlockPos pos) {
        return Items.nether_wart;
    }

    /**
     * Convert the given metadata into a BlockState for this Block
     */
    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(BlockNetherWart.AGE_PROP, Integer.valueOf(meta));
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    @Override
    public int getMetaFromState(IBlockState state) {
        return ((Integer) state.getValue(BlockNetherWart.AGE_PROP)).intValue();
    }

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[]{BlockNetherWart.AGE_PROP});
    }
}
