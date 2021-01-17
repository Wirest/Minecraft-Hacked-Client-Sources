// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import net.minecraft.block.state.BlockState;
import net.minecraft.item.ItemStack;
import java.util.List;
import net.minecraft.item.Item;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraft.world.gen.feature.WorldGenCanopyTree;
import net.minecraft.world.gen.feature.WorldGenSavannaTree;
import net.minecraft.world.gen.feature.WorldGenMegaJungle;
import net.minecraft.init.Blocks;
import net.minecraft.world.gen.feature.WorldGenForest;
import net.minecraft.world.gen.feature.WorldGenTaiga2;
import net.minecraft.world.gen.feature.WorldGenMegaPineTree;
import net.minecraft.world.gen.feature.WorldGenTrees;
import net.minecraft.world.gen.feature.WorldGenBigTree;
import java.util.Random;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.util.StatCollector;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.properties.PropertyEnum;

public class BlockSapling extends BlockBush implements IGrowable
{
    public static final PropertyEnum<BlockPlanks.EnumType> TYPE;
    public static final PropertyInteger STAGE;
    
    static {
        TYPE = PropertyEnum.create("type", BlockPlanks.EnumType.class);
        STAGE = PropertyInteger.create("stage", 0, 1);
    }
    
    protected BlockSapling() {
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockSapling.TYPE, BlockPlanks.EnumType.OAK).withProperty((IProperty<Comparable>)BlockSapling.STAGE, 0));
        final float f = 0.4f;
        this.setBlockBounds(0.5f - f, 0.0f, 0.5f - f, 0.5f + f, f * 2.0f, 0.5f + f);
        this.setCreativeTab(CreativeTabs.tabDecorations);
    }
    
    @Override
    public String getLocalizedName() {
        return StatCollector.translateToLocal(String.valueOf(this.getUnlocalizedName()) + "." + BlockPlanks.EnumType.OAK.getUnlocalizedName() + ".name");
    }
    
    @Override
    public void updateTick(final World worldIn, final BlockPos pos, final IBlockState state, final Random rand) {
        if (!worldIn.isRemote) {
            super.updateTick(worldIn, pos, state, rand);
            if (worldIn.getLightFromNeighbors(pos.up()) >= 9 && rand.nextInt(7) == 0) {
                this.grow(worldIn, pos, state, rand);
            }
        }
    }
    
    public void grow(final World worldIn, final BlockPos pos, final IBlockState state, final Random rand) {
        if (state.getValue((IProperty<Integer>)BlockSapling.STAGE) == 0) {
            worldIn.setBlockState(pos, state.cycleProperty((IProperty<Comparable>)BlockSapling.STAGE), 4);
        }
        else {
            this.generateTree(worldIn, pos, state, rand);
        }
    }
    
    public void generateTree(final World worldIn, final BlockPos pos, final IBlockState state, final Random rand) {
        WorldGenerator worldgenerator = (rand.nextInt(10) == 0) ? new WorldGenBigTree(true) : new WorldGenTrees(true);
        int i = 0;
        int j = 0;
        boolean flag = false;
        switch (state.getValue(BlockSapling.TYPE)) {
            case SPRUCE: {
            Label_0163:
                for (i = 0; i >= -1; --i) {
                    for (j = 0; j >= -1; --j) {
                        if (this.func_181624_a(worldIn, pos, i, j, BlockPlanks.EnumType.SPRUCE)) {
                            worldgenerator = new WorldGenMegaPineTree(false, rand.nextBoolean());
                            flag = true;
                            break Label_0163;
                        }
                    }
                }
                if (!flag) {
                    j = 0;
                    i = 0;
                    worldgenerator = new WorldGenTaiga2(true);
                    break;
                }
                break;
            }
            case BIRCH: {
                worldgenerator = new WorldGenForest(true, false);
                break;
            }
            case JUNGLE: {
                final IBlockState iblockstate = Blocks.log.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.JUNGLE);
                final IBlockState iblockstate2 = Blocks.leaves.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.JUNGLE).withProperty((IProperty<Comparable>)BlockLeaves.CHECK_DECAY, false);
            Label_0321:
                for (i = 0; i >= -1; --i) {
                    for (j = 0; j >= -1; --j) {
                        if (this.func_181624_a(worldIn, pos, i, j, BlockPlanks.EnumType.JUNGLE)) {
                            worldgenerator = new WorldGenMegaJungle(true, 10, 20, iblockstate, iblockstate2);
                            flag = true;
                            break Label_0321;
                        }
                    }
                }
                if (!flag) {
                    j = 0;
                    i = 0;
                    worldgenerator = new WorldGenTrees(true, 4 + rand.nextInt(7), iblockstate, iblockstate2, false);
                    break;
                }
                break;
            }
            case ACACIA: {
                worldgenerator = new WorldGenSavannaTree(true);
                break;
            }
            case DARK_OAK: {
            Label_0434:
                for (i = 0; i >= -1; --i) {
                    for (j = 0; j >= -1; --j) {
                        if (this.func_181624_a(worldIn, pos, i, j, BlockPlanks.EnumType.DARK_OAK)) {
                            worldgenerator = new WorldGenCanopyTree(true);
                            flag = true;
                            break Label_0434;
                        }
                    }
                }
                if (!flag) {
                    return;
                }
                break;
            }
        }
        final IBlockState iblockstate3 = Blocks.air.getDefaultState();
        if (flag) {
            worldIn.setBlockState(pos.add(i, 0, j), iblockstate3, 4);
            worldIn.setBlockState(pos.add(i + 1, 0, j), iblockstate3, 4);
            worldIn.setBlockState(pos.add(i, 0, j + 1), iblockstate3, 4);
            worldIn.setBlockState(pos.add(i + 1, 0, j + 1), iblockstate3, 4);
        }
        else {
            worldIn.setBlockState(pos, iblockstate3, 4);
        }
        if (!worldgenerator.generate(worldIn, rand, pos.add(i, 0, j))) {
            if (flag) {
                worldIn.setBlockState(pos.add(i, 0, j), state, 4);
                worldIn.setBlockState(pos.add(i + 1, 0, j), state, 4);
                worldIn.setBlockState(pos.add(i, 0, j + 1), state, 4);
                worldIn.setBlockState(pos.add(i + 1, 0, j + 1), state, 4);
            }
            else {
                worldIn.setBlockState(pos, state, 4);
            }
        }
    }
    
    private boolean func_181624_a(final World p_181624_1_, final BlockPos p_181624_2_, final int p_181624_3_, final int p_181624_4_, final BlockPlanks.EnumType p_181624_5_) {
        return this.isTypeAt(p_181624_1_, p_181624_2_.add(p_181624_3_, 0, p_181624_4_), p_181624_5_) && this.isTypeAt(p_181624_1_, p_181624_2_.add(p_181624_3_ + 1, 0, p_181624_4_), p_181624_5_) && this.isTypeAt(p_181624_1_, p_181624_2_.add(p_181624_3_, 0, p_181624_4_ + 1), p_181624_5_) && this.isTypeAt(p_181624_1_, p_181624_2_.add(p_181624_3_ + 1, 0, p_181624_4_ + 1), p_181624_5_);
    }
    
    public boolean isTypeAt(final World worldIn, final BlockPos pos, final BlockPlanks.EnumType type) {
        final IBlockState iblockstate = worldIn.getBlockState(pos);
        return iblockstate.getBlock() == this && iblockstate.getValue(BlockSapling.TYPE) == type;
    }
    
    @Override
    public int damageDropped(final IBlockState state) {
        return state.getValue(BlockSapling.TYPE).getMetadata();
    }
    
    @Override
    public void getSubBlocks(final Item itemIn, final CreativeTabs tab, final List<ItemStack> list) {
        BlockPlanks.EnumType[] values;
        for (int length = (values = BlockPlanks.EnumType.values()).length, i = 0; i < length; ++i) {
            final BlockPlanks.EnumType blockplanks$enumtype = values[i];
            list.add(new ItemStack(itemIn, 1, blockplanks$enumtype.getMetadata()));
        }
    }
    
    @Override
    public boolean canGrow(final World worldIn, final BlockPos pos, final IBlockState state, final boolean isClient) {
        return true;
    }
    
    @Override
    public boolean canUseBonemeal(final World worldIn, final Random rand, final BlockPos pos, final IBlockState state) {
        return worldIn.rand.nextFloat() < 0.45;
    }
    
    @Override
    public void grow(final World worldIn, final Random rand, final BlockPos pos, final IBlockState state) {
        this.grow(worldIn, pos, state, rand);
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        return this.getDefaultState().withProperty(BlockSapling.TYPE, BlockPlanks.EnumType.byMetadata(meta & 0x7)).withProperty((IProperty<Comparable>)BlockSapling.STAGE, (meta & 0x8) >> 3);
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        int i = 0;
        i |= state.getValue(BlockSapling.TYPE).getMetadata();
        i |= state.getValue((IProperty<Integer>)BlockSapling.STAGE) << 3;
        return i;
    }
    
    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[] { BlockSapling.TYPE, BlockSapling.STAGE });
    }
}
