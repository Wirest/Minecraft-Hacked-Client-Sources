// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import net.minecraft.block.state.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.MathHelper;
import java.util.Random;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.init.Blocks;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;

public class BlockCrops extends BlockBush implements IGrowable
{
    public static final PropertyInteger AGE;
    
    static {
        AGE = PropertyInteger.create("age", 0, 7);
    }
    
    protected BlockCrops() {
        this.setDefaultState(this.blockState.getBaseState().withProperty((IProperty<Comparable>)BlockCrops.AGE, 0));
        this.setTickRandomly(true);
        final float f = 0.5f;
        this.setBlockBounds(0.5f - f, 0.0f, 0.5f - f, 0.5f + f, 0.25f, 0.5f + f);
        this.setCreativeTab(null);
        this.setHardness(0.0f);
        this.setStepSound(BlockCrops.soundTypeGrass);
        this.disableStats();
    }
    
    @Override
    protected boolean canPlaceBlockOn(final Block ground) {
        return ground == Blocks.farmland;
    }
    
    @Override
    public void updateTick(final World worldIn, final BlockPos pos, final IBlockState state, final Random rand) {
        super.updateTick(worldIn, pos, state, rand);
        if (worldIn.getLightFromNeighbors(pos.up()) >= 9) {
            final int i = state.getValue((IProperty<Integer>)BlockCrops.AGE);
            if (i < 7) {
                final float f = getGrowthChance(this, worldIn, pos);
                if (rand.nextInt((int)(25.0f / f) + 1) == 0) {
                    worldIn.setBlockState(pos, state.withProperty((IProperty<Comparable>)BlockCrops.AGE, i + 1), 2);
                }
            }
        }
    }
    
    public void grow(final World worldIn, final BlockPos pos, final IBlockState state) {
        int i = state.getValue((IProperty<Integer>)BlockCrops.AGE) + MathHelper.getRandomIntegerInRange(worldIn.rand, 2, 5);
        if (i > 7) {
            i = 7;
        }
        worldIn.setBlockState(pos, state.withProperty((IProperty<Comparable>)BlockCrops.AGE, i), 2);
    }
    
    protected static float getGrowthChance(final Block blockIn, final World worldIn, final BlockPos pos) {
        float f = 1.0f;
        final BlockPos blockpos = pos.down();
        for (int i = -1; i <= 1; ++i) {
            for (int j = -1; j <= 1; ++j) {
                float f2 = 0.0f;
                final IBlockState iblockstate = worldIn.getBlockState(blockpos.add(i, 0, j));
                if (iblockstate.getBlock() == Blocks.farmland) {
                    f2 = 1.0f;
                    if (iblockstate.getValue((IProperty<Integer>)BlockFarmland.MOISTURE) > 0) {
                        f2 = 3.0f;
                    }
                }
                if (i != 0 || j != 0) {
                    f2 /= 4.0f;
                }
                f += f2;
            }
        }
        final BlockPos blockpos2 = pos.north();
        final BlockPos blockpos3 = pos.south();
        final BlockPos blockpos4 = pos.west();
        final BlockPos blockpos5 = pos.east();
        final boolean flag = blockIn == worldIn.getBlockState(blockpos4).getBlock() || blockIn == worldIn.getBlockState(blockpos5).getBlock();
        final boolean flag2 = blockIn == worldIn.getBlockState(blockpos2).getBlock() || blockIn == worldIn.getBlockState(blockpos3).getBlock();
        if (flag && flag2) {
            f /= 2.0f;
        }
        else {
            final boolean flag3 = blockIn == worldIn.getBlockState(blockpos4.north()).getBlock() || blockIn == worldIn.getBlockState(blockpos5.north()).getBlock() || blockIn == worldIn.getBlockState(blockpos5.south()).getBlock() || blockIn == worldIn.getBlockState(blockpos4.south()).getBlock();
            if (flag3) {
                f /= 2.0f;
            }
        }
        return f;
    }
    
    @Override
    public boolean canBlockStay(final World worldIn, final BlockPos pos, final IBlockState state) {
        return (worldIn.getLight(pos) >= 8 || worldIn.canSeeSky(pos)) && this.canPlaceBlockOn(worldIn.getBlockState(pos.down()).getBlock());
    }
    
    protected Item getSeed() {
        return Items.wheat_seeds;
    }
    
    protected Item getCrop() {
        return Items.wheat;
    }
    
    @Override
    public void dropBlockAsItemWithChance(final World worldIn, final BlockPos pos, final IBlockState state, final float chance, final int fortune) {
        super.dropBlockAsItemWithChance(worldIn, pos, state, chance, 0);
        if (!worldIn.isRemote) {
            final int i = state.getValue((IProperty<Integer>)BlockCrops.AGE);
            if (i >= 7) {
                for (int j = 3 + fortune, k = 0; k < j; ++k) {
                    if (worldIn.rand.nextInt(15) <= i) {
                        Block.spawnAsEntity(worldIn, pos, new ItemStack(this.getSeed(), 1, 0));
                    }
                }
            }
        }
    }
    
    @Override
    public Item getItemDropped(final IBlockState state, final Random rand, final int fortune) {
        return (state.getValue((IProperty<Integer>)BlockCrops.AGE) == 7) ? this.getCrop() : this.getSeed();
    }
    
    @Override
    public Item getItem(final World worldIn, final BlockPos pos) {
        return this.getSeed();
    }
    
    @Override
    public boolean canGrow(final World worldIn, final BlockPos pos, final IBlockState state, final boolean isClient) {
        return state.getValue((IProperty<Integer>)BlockCrops.AGE) < 7;
    }
    
    @Override
    public boolean canUseBonemeal(final World worldIn, final Random rand, final BlockPos pos, final IBlockState state) {
        return true;
    }
    
    @Override
    public void grow(final World worldIn, final Random rand, final BlockPos pos, final IBlockState state) {
        this.grow(worldIn, pos, state);
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        return this.getDefaultState().withProperty((IProperty<Comparable>)BlockCrops.AGE, meta);
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        return state.getValue((IProperty<Integer>)BlockCrops.AGE);
    }
    
    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[] { BlockCrops.AGE });
    }
}
