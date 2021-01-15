package net.minecraft.block;

import java.util.List;
import java.util.Random;

import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenBigTree;
import net.minecraft.world.gen.feature.WorldGenCanopyTree;
import net.minecraft.world.gen.feature.WorldGenForest;
import net.minecraft.world.gen.feature.WorldGenMegaJungle;
import net.minecraft.world.gen.feature.WorldGenMegaPineTree;
import net.minecraft.world.gen.feature.WorldGenSavannaTree;
import net.minecraft.world.gen.feature.WorldGenTaiga2;
import net.minecraft.world.gen.feature.WorldGenTrees;
import net.minecraft.world.gen.feature.WorldGenerator;

public class BlockSapling extends BlockBush implements IGrowable
{
    public static final PropertyEnum TYPE = PropertyEnum.create("type", BlockPlanks.EnumType.class);
    public static final PropertyInteger STAGE = PropertyInteger.create("stage", 0, 1);

    protected BlockSapling()
    {
        this.setDefaultState(this.blockState.getBaseState().withProperty(TYPE, BlockPlanks.EnumType.OAK).withProperty(STAGE, Integer.valueOf(0)));
        float var1 = 0.4F;
        this.setBlockBounds(0.5F - var1, 0.0F, 0.5F - var1, 0.5F + var1, var1 * 2.0F, 0.5F + var1);
        this.setCreativeTab(CreativeTabs.tabDecorations);
    }

    @Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {
        if (!worldIn.isRemote)
        {
            super.updateTick(worldIn, pos, state, rand);

            if (worldIn.getLightFromNeighbors(pos.up()) >= 9 && rand.nextInt(7) == 0)
            {
                this.grow(worldIn, pos, state, rand);
            }
        }
    }

    public void grow(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {
        if (((Integer)state.getValue(STAGE)).intValue() == 0)
        {
            worldIn.setBlockState(pos, state.cycleProperty(STAGE), 4);
        }
        else
        {
            this.generateTree(worldIn, pos, state, rand);
        }
    }

    public void generateTree(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {
        Object var5 = rand.nextInt(10) == 0 ? new WorldGenBigTree(true) : new WorldGenTrees(true);
        int var6 = 0;
        int var7 = 0;
        boolean var8 = false;

        switch (BlockSapling.SwitchEnumType.WOOD_TYPE_LOOKUP[((BlockPlanks.EnumType)state.getValue(TYPE)).ordinal()])
        {
            case 1:
                label78:
                for (var6 = 0; var6 >= -1; --var6)
                {
                    for (var7 = 0; var7 >= -1; --var7)
                    {
                        if (this.isTypeAt(worldIn, pos.add(var6, 0, var7), BlockPlanks.EnumType.SPRUCE) && this.isTypeAt(worldIn, pos.add(var6 + 1, 0, var7), BlockPlanks.EnumType.SPRUCE) && this.isTypeAt(worldIn, pos.add(var6, 0, var7 + 1), BlockPlanks.EnumType.SPRUCE) && this.isTypeAt(worldIn, pos.add(var6 + 1, 0, var7 + 1), BlockPlanks.EnumType.SPRUCE))
                        {
                            var5 = new WorldGenMegaPineTree(false, rand.nextBoolean());
                            var8 = true;
                            break label78;
                        }
                    }
                }

                if (!var8)
                {
                    var7 = 0;
                    var6 = 0;
                    var5 = new WorldGenTaiga2(true);
                }

                break;

            case 2:
                var5 = new WorldGenForest(true, false);
                break;

            case 3:
                label93:
                for (var6 = 0; var6 >= -1; --var6)
                {
                    for (var7 = 0; var7 >= -1; --var7)
                    {
                        if (this.isTypeAt(worldIn, pos.add(var6, 0, var7), BlockPlanks.EnumType.JUNGLE) && this.isTypeAt(worldIn, pos.add(var6 + 1, 0, var7), BlockPlanks.EnumType.JUNGLE) && this.isTypeAt(worldIn, pos.add(var6, 0, var7 + 1), BlockPlanks.EnumType.JUNGLE) && this.isTypeAt(worldIn, pos.add(var6 + 1, 0, var7 + 1), BlockPlanks.EnumType.JUNGLE))
                        {
                            var5 = new WorldGenMegaJungle(true, 10, 20, BlockPlanks.EnumType.JUNGLE.getMetadata(), BlockPlanks.EnumType.JUNGLE.getMetadata());
                            var8 = true;
                            break label93;
                        }
                    }
                }

                if (!var8)
                {
                    var7 = 0;
                    var6 = 0;
                    var5 = new WorldGenTrees(true, 4 + rand.nextInt(7), BlockPlanks.EnumType.JUNGLE.getMetadata(), BlockPlanks.EnumType.JUNGLE.getMetadata(), false);
                }

                break;

            case 4:
                var5 = new WorldGenSavannaTree(true);
                break;

            case 5:
                label108:
                for (var6 = 0; var6 >= -1; --var6)
                {
                    for (var7 = 0; var7 >= -1; --var7)
                    {
                        if (this.isTypeAt(worldIn, pos.add(var6, 0, var7), BlockPlanks.EnumType.DARK_OAK) && this.isTypeAt(worldIn, pos.add(var6 + 1, 0, var7), BlockPlanks.EnumType.DARK_OAK) && this.isTypeAt(worldIn, pos.add(var6, 0, var7 + 1), BlockPlanks.EnumType.DARK_OAK) && this.isTypeAt(worldIn, pos.add(var6 + 1, 0, var7 + 1), BlockPlanks.EnumType.DARK_OAK))
                        {
                            var5 = new WorldGenCanopyTree(true);
                            var8 = true;
                            break label108;
                        }
                    }
                }

                if (!var8)
                {
                    return;
                }

            case 6:
        }

        IBlockState var9 = Blocks.air.getDefaultState();

        if (var8)
        {
            worldIn.setBlockState(pos.add(var6, 0, var7), var9, 4);
            worldIn.setBlockState(pos.add(var6 + 1, 0, var7), var9, 4);
            worldIn.setBlockState(pos.add(var6, 0, var7 + 1), var9, 4);
            worldIn.setBlockState(pos.add(var6 + 1, 0, var7 + 1), var9, 4);
        }
        else
        {
            worldIn.setBlockState(pos, var9, 4);
        }

        if (!((WorldGenerator)var5).generate(worldIn, rand, pos.add(var6, 0, var7)))
        {
            if (var8)
            {
                worldIn.setBlockState(pos.add(var6, 0, var7), state, 4);
                worldIn.setBlockState(pos.add(var6 + 1, 0, var7), state, 4);
                worldIn.setBlockState(pos.add(var6, 0, var7 + 1), state, 4);
                worldIn.setBlockState(pos.add(var6 + 1, 0, var7 + 1), state, 4);
            }
            else
            {
                worldIn.setBlockState(pos, state, 4);
            }
        }
    }

    /**
     * Check whether the given BlockPos has a Sapling of the given type
     */
    public boolean isTypeAt(World worldIn, BlockPos pos, BlockPlanks.EnumType type)
    {
        IBlockState var4 = worldIn.getBlockState(pos);
        return var4.getBlock() == this && var4.getValue(TYPE) == type;
    }

    /**
     * Get the damage value that this Block should drop
     */
    @Override
	public int damageDropped(IBlockState state)
    {
        return ((BlockPlanks.EnumType)state.getValue(TYPE)).getMetadata();
    }

    /**
     * returns a list of blocks with the same ID, but different meta (eg: wood returns 4 blocks)
     */
    @Override
	public void getSubBlocks(Item itemIn, CreativeTabs tab, List list)
    {
        BlockPlanks.EnumType[] var4 = BlockPlanks.EnumType.values();
        int var5 = var4.length;

        for (int var6 = 0; var6 < var5; ++var6)
        {
            BlockPlanks.EnumType var7 = var4[var6];
            list.add(new ItemStack(itemIn, 1, var7.getMetadata()));
        }
    }

    /**
     * Whether this IGrowable can grow
     */
    @Override
	public boolean canGrow(World worldIn, BlockPos pos, IBlockState state, boolean isClient)
    {
        return true;
    }

    @Override
	public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, IBlockState state)
    {
        return worldIn.rand.nextFloat() < 0.45D;
    }

    @Override
	public void grow(World worldIn, Random rand, BlockPos pos, IBlockState state)
    {
        this.grow(worldIn, pos, state, rand);
    }

    /**
     * Convert the given metadata into a BlockState for this Block
     */
    @Override
	public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(TYPE, BlockPlanks.EnumType.byMetadata(meta & 7)).withProperty(STAGE, Integer.valueOf((meta & 8) >> 3));
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    @Override
	public int getMetaFromState(IBlockState state)
    {
        byte var2 = 0;
        int var3 = var2 | ((BlockPlanks.EnumType)state.getValue(TYPE)).getMetadata();
        var3 |= ((Integer)state.getValue(STAGE)).intValue() << 3;
        return var3;
    }

    @Override
	protected BlockState createBlockState()
    {
        return new BlockState(this, new IProperty[] {TYPE, STAGE});
    }

    static final class SwitchEnumType
    {
        static final int[] WOOD_TYPE_LOOKUP = new int[BlockPlanks.EnumType.values().length];

        static
        {
            try
            {
                WOOD_TYPE_LOOKUP[BlockPlanks.EnumType.SPRUCE.ordinal()] = 1;
            }
            catch (NoSuchFieldError var6)
            {
                ;
            }

            try
            {
                WOOD_TYPE_LOOKUP[BlockPlanks.EnumType.BIRCH.ordinal()] = 2;
            }
            catch (NoSuchFieldError var5)
            {
                ;
            }

            try
            {
                WOOD_TYPE_LOOKUP[BlockPlanks.EnumType.JUNGLE.ordinal()] = 3;
            }
            catch (NoSuchFieldError var4)
            {
                ;
            }

            try
            {
                WOOD_TYPE_LOOKUP[BlockPlanks.EnumType.ACACIA.ordinal()] = 4;
            }
            catch (NoSuchFieldError var3)
            {
                ;
            }

            try
            {
                WOOD_TYPE_LOOKUP[BlockPlanks.EnumType.DARK_OAK.ordinal()] = 5;
            }
            catch (NoSuchFieldError var2)
            {
                ;
            }

            try
            {
                WOOD_TYPE_LOOKUP[BlockPlanks.EnumType.OAK.ordinal()] = 6;
            }
            catch (NoSuchFieldError var1)
            {
                ;
            }
        }
    }
}
