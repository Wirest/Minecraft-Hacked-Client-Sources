package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.ColorizerFoliage;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeColorHelper;

public abstract class BlockLeaves extends BlockLeavesBase
{
    public static final PropertyBool field_176237_a = PropertyBool.create("decayable");
    public static final PropertyBool field_176236_b = PropertyBool.create("check_decay");
    int[] field_150128_a;
    protected int field_150127_b;
    protected boolean field_176238_O;
    private static final String __OBFID = "CL_00000263";

    public BlockLeaves()
    {
        super(Material.leaves, false);
        this.setTickRandomly(true);
        this.setCreativeTab(CreativeTabs.tabDecorations);
        this.setHardness(0.2F);
        this.setLightOpacity(1);
        this.setStepSound(soundTypeGrass);
    }

    public int getBlockColor()
    {
        return ColorizerFoliage.getFoliageColor(0.5D, 1.0D);
    }

    public int getRenderColor(IBlockState state)
    {
        return ColorizerFoliage.getFoliageColorBasic();
    }

    public int colorMultiplier(IBlockAccess worldIn, BlockPos pos, int renderPass)
    {
        return BiomeColorHelper.func_180287_b(worldIn, pos);
    }

    public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
        byte var4 = 1;
        int var5 = var4 + 1;
        int var6 = pos.getX();
        int var7 = pos.getY();
        int var8 = pos.getZ();

        if (worldIn.isAreaLoaded(new BlockPos(var6 - var5, var7 - var5, var8 - var5), new BlockPos(var6 + var5, var7 + var5, var8 + var5)))
        {
            for (int var9 = -var4; var9 <= var4; ++var9)
            {
                for (int var10 = -var4; var10 <= var4; ++var10)
                {
                    for (int var11 = -var4; var11 <= var4; ++var11)
                    {
                        BlockPos var12 = pos.add(var9, var10, var11);
                        IBlockState var13 = worldIn.getBlockState(var12);

                        if (var13.getBlock().getMaterial() == Material.leaves && !((Boolean)var13.getValue(field_176236_b)).booleanValue())
                        {
                            worldIn.setBlockState(var12, var13.withProperty(field_176236_b, Boolean.valueOf(true)), 4);
                        }
                    }
                }
            }
        }
    }

    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {
        if (!worldIn.isRemote)
        {
            if (((Boolean)state.getValue(field_176236_b)).booleanValue() && ((Boolean)state.getValue(field_176237_a)).booleanValue())
            {
                byte var5 = 4;
                int var6 = var5 + 1;
                int var7 = pos.getX();
                int var8 = pos.getY();
                int var9 = pos.getZ();
                byte var10 = 32;
                int var11 = var10 * var10;
                int var12 = var10 / 2;

                if (this.field_150128_a == null)
                {
                    this.field_150128_a = new int[var10 * var10 * var10];
                }

                int var13;

                if (worldIn.isAreaLoaded(new BlockPos(var7 - var6, var8 - var6, var9 - var6), new BlockPos(var7 + var6, var8 + var6, var9 + var6)))
                {
                    int var14;
                    int var15;

                    for (var13 = -var5; var13 <= var5; ++var13)
                    {
                        for (var14 = -var5; var14 <= var5; ++var14)
                        {
                            for (var15 = -var5; var15 <= var5; ++var15)
                            {
                                Block var16 = worldIn.getBlockState(new BlockPos(var7 + var13, var8 + var14, var9 + var15)).getBlock();

                                if (var16 != Blocks.log && var16 != Blocks.log2)
                                {
                                    if (var16.getMaterial() == Material.leaves)
                                    {
                                        this.field_150128_a[(var13 + var12) * var11 + (var14 + var12) * var10 + var15 + var12] = -2;
                                    }
                                    else
                                    {
                                        this.field_150128_a[(var13 + var12) * var11 + (var14 + var12) * var10 + var15 + var12] = -1;
                                    }
                                }
                                else
                                {
                                    this.field_150128_a[(var13 + var12) * var11 + (var14 + var12) * var10 + var15 + var12] = 0;
                                }
                            }
                        }
                    }

                    for (var13 = 1; var13 <= 4; ++var13)
                    {
                        for (var14 = -var5; var14 <= var5; ++var14)
                        {
                            for (var15 = -var5; var15 <= var5; ++var15)
                            {
                                for (int var17 = -var5; var17 <= var5; ++var17)
                                {
                                    if (this.field_150128_a[(var14 + var12) * var11 + (var15 + var12) * var10 + var17 + var12] == var13 - 1)
                                    {
                                        if (this.field_150128_a[(var14 + var12 - 1) * var11 + (var15 + var12) * var10 + var17 + var12] == -2)
                                        {
                                            this.field_150128_a[(var14 + var12 - 1) * var11 + (var15 + var12) * var10 + var17 + var12] = var13;
                                        }

                                        if (this.field_150128_a[(var14 + var12 + 1) * var11 + (var15 + var12) * var10 + var17 + var12] == -2)
                                        {
                                            this.field_150128_a[(var14 + var12 + 1) * var11 + (var15 + var12) * var10 + var17 + var12] = var13;
                                        }

                                        if (this.field_150128_a[(var14 + var12) * var11 + (var15 + var12 - 1) * var10 + var17 + var12] == -2)
                                        {
                                            this.field_150128_a[(var14 + var12) * var11 + (var15 + var12 - 1) * var10 + var17 + var12] = var13;
                                        }

                                        if (this.field_150128_a[(var14 + var12) * var11 + (var15 + var12 + 1) * var10 + var17 + var12] == -2)
                                        {
                                            this.field_150128_a[(var14 + var12) * var11 + (var15 + var12 + 1) * var10 + var17 + var12] = var13;
                                        }

                                        if (this.field_150128_a[(var14 + var12) * var11 + (var15 + var12) * var10 + (var17 + var12 - 1)] == -2)
                                        {
                                            this.field_150128_a[(var14 + var12) * var11 + (var15 + var12) * var10 + (var17 + var12 - 1)] = var13;
                                        }

                                        if (this.field_150128_a[(var14 + var12) * var11 + (var15 + var12) * var10 + var17 + var12 + 1] == -2)
                                        {
                                            this.field_150128_a[(var14 + var12) * var11 + (var15 + var12) * var10 + var17 + var12 + 1] = var13;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                var13 = this.field_150128_a[var12 * var11 + var12 * var10 + var12];

                if (var13 >= 0)
                {
                    worldIn.setBlockState(pos, state.withProperty(field_176236_b, Boolean.valueOf(false)), 4);
                }
                else
                {
                    this.func_176235_d(worldIn, pos);
                }
            }
        }
    }

    public void randomDisplayTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {
        if (worldIn.func_175727_C(pos.offsetUp()) && !World.doesBlockHaveSolidTopSurface(worldIn, pos.offsetDown()) && rand.nextInt(15) == 1)
        {
            double var5 = (double)((float)pos.getX() + rand.nextFloat());
            double var7 = (double)pos.getY() - 0.05D;
            double var9 = (double)((float)pos.getZ() + rand.nextFloat());
            worldIn.spawnParticle(EnumParticleTypes.DRIP_WATER, var5, var7, var9, 0.0D, 0.0D, 0.0D, new int[0]);
        }
    }

    private void func_176235_d(World worldIn, BlockPos p_176235_2_)
    {
        this.dropBlockAsItem(worldIn, p_176235_2_, worldIn.getBlockState(p_176235_2_), 0);
        worldIn.setBlockToAir(p_176235_2_);
    }

    /**
     * Returns the quantity of items to drop on block destruction.
     */
    public int quantityDropped(Random random)
    {
        return random.nextInt(20) == 0 ? 1 : 0;
    }

    /**
     * Get the Item that this Block should drop when harvested.
     *  
     * @param fortune the level of the Fortune enchantment on the player's tool
     */
    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        return Item.getItemFromBlock(Blocks.sapling);
    }

    /**
     * Spawns this Block's drops into the World as EntityItems.
     *  
     * @param chance The chance that each Item is actually spawned (1.0 = always, 0.0 = never)
     * @param fortune The player's fortune level
     */
    public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune)
    {
        if (!worldIn.isRemote)
        {
            int var6 = this.func_176232_d(state);

            if (fortune > 0)
            {
                var6 -= 2 << fortune;

                if (var6 < 10)
                {
                    var6 = 10;
                }
            }

            if (worldIn.rand.nextInt(var6) == 0)
            {
                Item var7 = this.getItemDropped(state, worldIn.rand, fortune);
                spawnAsEntity(worldIn, pos, new ItemStack(var7, 1, this.damageDropped(state)));
            }

            var6 = 200;

            if (fortune > 0)
            {
                var6 -= 10 << fortune;

                if (var6 < 40)
                {
                    var6 = 40;
                }
            }

            this.func_176234_a(worldIn, pos, state, var6);
        }
    }

    protected void func_176234_a(World worldIn, BlockPos p_176234_2_, IBlockState p_176234_3_, int p_176234_4_) {}

    protected int func_176232_d(IBlockState p_176232_1_)
    {
        return 20;
    }

    public boolean isOpaqueCube()
    {
        return !this.field_150121_P;
    }

    /**
     * Pass true to draw this block using fancy graphics, or false for fast graphics.
     */
    public void setGraphicsLevel(boolean p_150122_1_)
    {
        this.field_176238_O = p_150122_1_;
        this.field_150121_P = p_150122_1_;
        this.field_150127_b = p_150122_1_ ? 0 : 1;
    }

    public EnumWorldBlockLayer getBlockLayer()
    {
        return this.field_176238_O ? EnumWorldBlockLayer.CUTOUT_MIPPED : EnumWorldBlockLayer.SOLID;
    }

    public boolean isVisuallyOpaque()
    {
        return false;
    }

    public abstract BlockPlanks.EnumType func_176233_b(int var1);
}
