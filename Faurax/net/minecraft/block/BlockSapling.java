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
    public static final PropertyEnum TYPE_PROP = PropertyEnum.create("type", BlockPlanks.EnumType.class);
    public static final PropertyInteger STAGE_PROP = PropertyInteger.create("stage", 0, 1);
    private static final String __OBFID = "CL_00000305";

    protected BlockSapling()
    {
        this.setDefaultState(this.blockState.getBaseState().withProperty(TYPE_PROP, BlockPlanks.EnumType.OAK).withProperty(STAGE_PROP, Integer.valueOf(0)));
        float var1 = 0.4F;
        this.setBlockBounds(0.5F - var1, 0.0F, 0.5F - var1, 0.5F + var1, var1 * 2.0F, 0.5F + var1);
        this.setCreativeTab(CreativeTabs.tabDecorations);
    }

    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {
        if (!worldIn.isRemote)
        {
            super.updateTick(worldIn, pos, state, rand);

            if (worldIn.getLightFromNeighbors(pos.offsetUp()) >= 9 && rand.nextInt(7) == 0)
            {
                this.func_176478_d(worldIn, pos, state, rand);
            }
        }
    }

    public void func_176478_d(World worldIn, BlockPos p_176478_2_, IBlockState p_176478_3_, Random p_176478_4_)
    {
        if (((Integer)p_176478_3_.getValue(STAGE_PROP)).intValue() == 0)
        {
            worldIn.setBlockState(p_176478_2_, p_176478_3_.cycleProperty(STAGE_PROP), 4);
        }
        else
        {
            this.func_176476_e(worldIn, p_176478_2_, p_176478_3_, p_176478_4_);
        }
    }

    public void func_176476_e(World worldIn, BlockPos p_176476_2_, IBlockState p_176476_3_, Random p_176476_4_)
    {
        Object var5 = p_176476_4_.nextInt(10) == 0 ? new WorldGenBigTree(true) : new WorldGenTrees(true);
        int var6 = 0;
        int var7 = 0;
        boolean var8 = false;

        switch (BlockSapling.SwitchEnumType.field_177065_a[((BlockPlanks.EnumType)p_176476_3_.getValue(TYPE_PROP)).ordinal()])
        {
            case 1:
                label78:
                for (var6 = 0; var6 >= -1; --var6)
                {
                    for (var7 = 0; var7 >= -1; --var7)
                    {
                        if (this.func_176477_a(worldIn, p_176476_2_.add(var6, 0, var7), BlockPlanks.EnumType.SPRUCE) && this.func_176477_a(worldIn, p_176476_2_.add(var6 + 1, 0, var7), BlockPlanks.EnumType.SPRUCE) && this.func_176477_a(worldIn, p_176476_2_.add(var6, 0, var7 + 1), BlockPlanks.EnumType.SPRUCE) && this.func_176477_a(worldIn, p_176476_2_.add(var6 + 1, 0, var7 + 1), BlockPlanks.EnumType.SPRUCE))
                        {
                            var5 = new WorldGenMegaPineTree(false, p_176476_4_.nextBoolean());
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
                        if (this.func_176477_a(worldIn, p_176476_2_.add(var6, 0, var7), BlockPlanks.EnumType.JUNGLE) && this.func_176477_a(worldIn, p_176476_2_.add(var6 + 1, 0, var7), BlockPlanks.EnumType.JUNGLE) && this.func_176477_a(worldIn, p_176476_2_.add(var6, 0, var7 + 1), BlockPlanks.EnumType.JUNGLE) && this.func_176477_a(worldIn, p_176476_2_.add(var6 + 1, 0, var7 + 1), BlockPlanks.EnumType.JUNGLE))
                        {
                            var5 = new WorldGenMegaJungle(true, 10, 20, BlockPlanks.EnumType.JUNGLE.func_176839_a(), BlockPlanks.EnumType.JUNGLE.func_176839_a());
                            var8 = true;
                            break label93;
                        }
                    }
                }

                if (!var8)
                {
                    var7 = 0;
                    var6 = 0;
                    var5 = new WorldGenTrees(true, 4 + p_176476_4_.nextInt(7), BlockPlanks.EnumType.JUNGLE.func_176839_a(), BlockPlanks.EnumType.JUNGLE.func_176839_a(), false);
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
                        if (this.func_176477_a(worldIn, p_176476_2_.add(var6, 0, var7), BlockPlanks.EnumType.DARK_OAK) && this.func_176477_a(worldIn, p_176476_2_.add(var6 + 1, 0, var7), BlockPlanks.EnumType.DARK_OAK) && this.func_176477_a(worldIn, p_176476_2_.add(var6, 0, var7 + 1), BlockPlanks.EnumType.DARK_OAK) && this.func_176477_a(worldIn, p_176476_2_.add(var6 + 1, 0, var7 + 1), BlockPlanks.EnumType.DARK_OAK))
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
            worldIn.setBlockState(p_176476_2_.add(var6, 0, var7), var9, 4);
            worldIn.setBlockState(p_176476_2_.add(var6 + 1, 0, var7), var9, 4);
            worldIn.setBlockState(p_176476_2_.add(var6, 0, var7 + 1), var9, 4);
            worldIn.setBlockState(p_176476_2_.add(var6 + 1, 0, var7 + 1), var9, 4);
        }
        else
        {
            worldIn.setBlockState(p_176476_2_, var9, 4);
        }

        if (!((WorldGenerator)var5).generate(worldIn, p_176476_4_, p_176476_2_.add(var6, 0, var7)))
        {
            if (var8)
            {
                worldIn.setBlockState(p_176476_2_.add(var6, 0, var7), p_176476_3_, 4);
                worldIn.setBlockState(p_176476_2_.add(var6 + 1, 0, var7), p_176476_3_, 4);
                worldIn.setBlockState(p_176476_2_.add(var6, 0, var7 + 1), p_176476_3_, 4);
                worldIn.setBlockState(p_176476_2_.add(var6 + 1, 0, var7 + 1), p_176476_3_, 4);
            }
            else
            {
                worldIn.setBlockState(p_176476_2_, p_176476_3_, 4);
            }
        }
    }

    public boolean func_176477_a(World worldIn, BlockPos p_176477_2_, BlockPlanks.EnumType p_176477_3_)
    {
        IBlockState var4 = worldIn.getBlockState(p_176477_2_);
        return var4.getBlock() == this && var4.getValue(TYPE_PROP) == p_176477_3_;
    }

    /**
     * Get the damage value that this Block should drop
     */
    public int damageDropped(IBlockState state)
    {
        return ((BlockPlanks.EnumType)state.getValue(TYPE_PROP)).func_176839_a();
    }

    /**
     * returns a list of blocks with the same ID, but different meta (eg: wood returns 4 blocks)
     */
    public void getSubBlocks(Item itemIn, CreativeTabs tab, List list)
    {
        BlockPlanks.EnumType[] var4 = BlockPlanks.EnumType.values();
        int var5 = var4.length;

        for (int var6 = 0; var6 < var5; ++var6)
        {
            BlockPlanks.EnumType var7 = var4[var6];
            list.add(new ItemStack(itemIn, 1, var7.func_176839_a()));
        }
    }

    public boolean isStillGrowing(World worldIn, BlockPos p_176473_2_, IBlockState p_176473_3_, boolean p_176473_4_)
    {
        return true;
    }

    public boolean canUseBonemeal(World worldIn, Random p_180670_2_, BlockPos p_180670_3_, IBlockState p_180670_4_)
    {
        return (double)worldIn.rand.nextFloat() < 0.45D;
    }

    public void grow(World worldIn, Random p_176474_2_, BlockPos p_176474_3_, IBlockState p_176474_4_)
    {
        this.func_176478_d(worldIn, p_176474_3_, p_176474_4_, p_176474_2_);
    }

    /**
     * Convert the given metadata into a BlockState for this Block
     */
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(TYPE_PROP, BlockPlanks.EnumType.func_176837_a(meta & 7)).withProperty(STAGE_PROP, Integer.valueOf((meta & 8) >> 3));
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    public int getMetaFromState(IBlockState state)
    {
        byte var2 = 0;
        int var3 = var2 | ((BlockPlanks.EnumType)state.getValue(TYPE_PROP)).func_176839_a();
        var3 |= ((Integer)state.getValue(STAGE_PROP)).intValue() << 3;
        return var3;
    }

    protected BlockState createBlockState()
    {
        return new BlockState(this, new IProperty[] {TYPE_PROP, STAGE_PROP});
    }

    static final class SwitchEnumType
    {
        static final int[] field_177065_a = new int[BlockPlanks.EnumType.values().length];
        private static final String __OBFID = "CL_00002067";

        static
        {
            try
            {
                field_177065_a[BlockPlanks.EnumType.SPRUCE.ordinal()] = 1;
            }
            catch (NoSuchFieldError var6)
            {
                ;
            }

            try
            {
                field_177065_a[BlockPlanks.EnumType.BIRCH.ordinal()] = 2;
            }
            catch (NoSuchFieldError var5)
            {
                ;
            }

            try
            {
                field_177065_a[BlockPlanks.EnumType.JUNGLE.ordinal()] = 3;
            }
            catch (NoSuchFieldError var4)
            {
                ;
            }

            try
            {
                field_177065_a[BlockPlanks.EnumType.ACACIA.ordinal()] = 4;
            }
            catch (NoSuchFieldError var3)
            {
                ;
            }

            try
            {
                field_177065_a[BlockPlanks.EnumType.DARK_OAK.ordinal()] = 5;
            }
            catch (NoSuchFieldError var2)
            {
                ;
            }

            try
            {
                field_177065_a[BlockPlanks.EnumType.OAK.ordinal()] = 6;
            }
            catch (NoSuchFieldError var1)
            {
                ;
            }
        }
    }
}
