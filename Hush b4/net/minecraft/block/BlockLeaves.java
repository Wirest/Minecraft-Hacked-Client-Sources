// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Item;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.init.Blocks;
import java.util.Random;
import net.minecraft.block.properties.IProperty;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeColorHelper;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.block.state.IBlockState;
import net.minecraft.world.ColorizerFoliage;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;

public abstract class BlockLeaves extends BlockLeavesBase
{
    public static final PropertyBool DECAYABLE;
    public static final PropertyBool CHECK_DECAY;
    int[] surroundings;
    protected int iconIndex;
    protected boolean isTransparent;
    
    static {
        DECAYABLE = PropertyBool.create("decayable");
        CHECK_DECAY = PropertyBool.create("check_decay");
    }
    
    public BlockLeaves() {
        super(Material.leaves, false);
        this.setTickRandomly(true);
        this.setCreativeTab(CreativeTabs.tabDecorations);
        this.setHardness(0.2f);
        this.setLightOpacity(1);
        this.setStepSound(BlockLeaves.soundTypeGrass);
    }
    
    @Override
    public int getBlockColor() {
        return ColorizerFoliage.getFoliageColor(0.5, 1.0);
    }
    
    @Override
    public int getRenderColor(final IBlockState state) {
        return ColorizerFoliage.getFoliageColorBasic();
    }
    
    @Override
    public int colorMultiplier(final IBlockAccess worldIn, final BlockPos pos, final int renderPass) {
        return BiomeColorHelper.getFoliageColorAtPos(worldIn, pos);
    }
    
    @Override
    public void breakBlock(final World worldIn, final BlockPos pos, final IBlockState state) {
        final int i = 1;
        final int j = i + 1;
        final int k = pos.getX();
        final int l = pos.getY();
        final int i2 = pos.getZ();
        if (worldIn.isAreaLoaded(new BlockPos(k - j, l - j, i2 - j), new BlockPos(k + j, l + j, i2 + j))) {
            for (int j2 = -i; j2 <= i; ++j2) {
                for (int k2 = -i; k2 <= i; ++k2) {
                    for (int l2 = -i; l2 <= i; ++l2) {
                        final BlockPos blockpos = pos.add(j2, k2, l2);
                        final IBlockState iblockstate = worldIn.getBlockState(blockpos);
                        if (iblockstate.getBlock().getMaterial() == Material.leaves && !iblockstate.getValue((IProperty<Boolean>)BlockLeaves.CHECK_DECAY)) {
                            worldIn.setBlockState(blockpos, iblockstate.withProperty((IProperty<Comparable>)BlockLeaves.CHECK_DECAY, true), 4);
                        }
                    }
                }
            }
        }
    }
    
    @Override
    public void updateTick(final World worldIn, final BlockPos pos, final IBlockState state, final Random rand) {
        if (!worldIn.isRemote && state.getValue((IProperty<Boolean>)BlockLeaves.CHECK_DECAY) && state.getValue((IProperty<Boolean>)BlockLeaves.DECAYABLE)) {
            final int i = 4;
            final int j = i + 1;
            final int k = pos.getX();
            final int l = pos.getY();
            final int i2 = pos.getZ();
            final int j2 = 32;
            final int k2 = j2 * j2;
            final int l2 = j2 / 2;
            if (this.surroundings == null) {
                this.surroundings = new int[j2 * j2 * j2];
            }
            if (worldIn.isAreaLoaded(new BlockPos(k - j, l - j, i2 - j), new BlockPos(k + j, l + j, i2 + j))) {
                final BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
                for (int i3 = -i; i3 <= i; ++i3) {
                    for (int j3 = -i; j3 <= i; ++j3) {
                        for (int k3 = -i; k3 <= i; ++k3) {
                            final Block block = worldIn.getBlockState(blockpos$mutableblockpos.func_181079_c(k + i3, l + j3, i2 + k3)).getBlock();
                            if (block != Blocks.log && block != Blocks.log2) {
                                if (block.getMaterial() == Material.leaves) {
                                    this.surroundings[(i3 + l2) * k2 + (j3 + l2) * j2 + k3 + l2] = -2;
                                }
                                else {
                                    this.surroundings[(i3 + l2) * k2 + (j3 + l2) * j2 + k3 + l2] = -1;
                                }
                            }
                            else {
                                this.surroundings[(i3 + l2) * k2 + (j3 + l2) * j2 + k3 + l2] = 0;
                            }
                        }
                    }
                }
                for (int i4 = 1; i4 <= 4; ++i4) {
                    for (int j4 = -i; j4 <= i; ++j4) {
                        for (int k4 = -i; k4 <= i; ++k4) {
                            for (int l3 = -i; l3 <= i; ++l3) {
                                if (this.surroundings[(j4 + l2) * k2 + (k4 + l2) * j2 + l3 + l2] == i4 - 1) {
                                    if (this.surroundings[(j4 + l2 - 1) * k2 + (k4 + l2) * j2 + l3 + l2] == -2) {
                                        this.surroundings[(j4 + l2 - 1) * k2 + (k4 + l2) * j2 + l3 + l2] = i4;
                                    }
                                    if (this.surroundings[(j4 + l2 + 1) * k2 + (k4 + l2) * j2 + l3 + l2] == -2) {
                                        this.surroundings[(j4 + l2 + 1) * k2 + (k4 + l2) * j2 + l3 + l2] = i4;
                                    }
                                    if (this.surroundings[(j4 + l2) * k2 + (k4 + l2 - 1) * j2 + l3 + l2] == -2) {
                                        this.surroundings[(j4 + l2) * k2 + (k4 + l2 - 1) * j2 + l3 + l2] = i4;
                                    }
                                    if (this.surroundings[(j4 + l2) * k2 + (k4 + l2 + 1) * j2 + l3 + l2] == -2) {
                                        this.surroundings[(j4 + l2) * k2 + (k4 + l2 + 1) * j2 + l3 + l2] = i4;
                                    }
                                    if (this.surroundings[(j4 + l2) * k2 + (k4 + l2) * j2 + (l3 + l2 - 1)] == -2) {
                                        this.surroundings[(j4 + l2) * k2 + (k4 + l2) * j2 + (l3 + l2 - 1)] = i4;
                                    }
                                    if (this.surroundings[(j4 + l2) * k2 + (k4 + l2) * j2 + l3 + l2 + 1] == -2) {
                                        this.surroundings[(j4 + l2) * k2 + (k4 + l2) * j2 + l3 + l2 + 1] = i4;
                                    }
                                }
                            }
                        }
                    }
                }
            }
            final int l4 = this.surroundings[l2 * k2 + l2 * j2 + l2];
            if (l4 >= 0) {
                worldIn.setBlockState(pos, state.withProperty((IProperty<Comparable>)BlockLeaves.CHECK_DECAY, false), 4);
            }
            else {
                this.destroy(worldIn, pos);
            }
        }
    }
    
    @Override
    public void randomDisplayTick(final World worldIn, final BlockPos pos, final IBlockState state, final Random rand) {
        if (worldIn.canLightningStrike(pos.up()) && !World.doesBlockHaveSolidTopSurface(worldIn, pos.down()) && rand.nextInt(15) == 1) {
            final double d0 = pos.getX() + rand.nextFloat();
            final double d2 = pos.getY() - 0.05;
            final double d3 = pos.getZ() + rand.nextFloat();
            worldIn.spawnParticle(EnumParticleTypes.DRIP_WATER, d0, d2, d3, 0.0, 0.0, 0.0, new int[0]);
        }
    }
    
    private void destroy(final World worldIn, final BlockPos pos) {
        this.dropBlockAsItem(worldIn, pos, worldIn.getBlockState(pos), 0);
        worldIn.setBlockToAir(pos);
    }
    
    @Override
    public int quantityDropped(final Random random) {
        return (random.nextInt(20) == 0) ? 1 : 0;
    }
    
    @Override
    public Item getItemDropped(final IBlockState state, final Random rand, final int fortune) {
        return Item.getItemFromBlock(Blocks.sapling);
    }
    
    @Override
    public void dropBlockAsItemWithChance(final World worldIn, final BlockPos pos, final IBlockState state, final float chance, final int fortune) {
        if (!worldIn.isRemote) {
            int i = this.getSaplingDropChance(state);
            if (fortune > 0) {
                i -= 2 << fortune;
                if (i < 10) {
                    i = 10;
                }
            }
            if (worldIn.rand.nextInt(i) == 0) {
                final Item item = this.getItemDropped(state, worldIn.rand, fortune);
                Block.spawnAsEntity(worldIn, pos, new ItemStack(item, 1, this.damageDropped(state)));
            }
            i = 200;
            if (fortune > 0) {
                i -= 10 << fortune;
                if (i < 40) {
                    i = 40;
                }
            }
            this.dropApple(worldIn, pos, state, i);
        }
    }
    
    protected void dropApple(final World worldIn, final BlockPos pos, final IBlockState state, final int chance) {
    }
    
    protected int getSaplingDropChance(final IBlockState state) {
        return 20;
    }
    
    @Override
    public boolean isOpaqueCube() {
        return !this.fancyGraphics;
    }
    
    public void setGraphicsLevel(final boolean fancy) {
        this.isTransparent = fancy;
        this.fancyGraphics = fancy;
        this.iconIndex = (fancy ? 0 : 1);
    }
    
    @Override
    public EnumWorldBlockLayer getBlockLayer() {
        return this.isTransparent ? EnumWorldBlockLayer.CUTOUT_MIPPED : EnumWorldBlockLayer.SOLID;
    }
    
    @Override
    public boolean isVisuallyOpaque() {
        return false;
    }
    
    public abstract BlockPlanks.EnumType getWoodType(final int p0);
}
