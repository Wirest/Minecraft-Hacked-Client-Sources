// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import net.minecraft.block.state.BlockState;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.item.Item;
import java.util.Random;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeColorHelper;
import net.minecraft.world.ColorizerGrass;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;

public class BlockGrass extends Block implements IGrowable
{
    public static final PropertyBool SNOWY;
    
    static {
        SNOWY = PropertyBool.create("snowy");
    }
    
    protected BlockGrass() {
        super(Material.grass);
        this.setDefaultState(this.blockState.getBaseState().withProperty((IProperty<Comparable>)BlockGrass.SNOWY, false));
        this.setTickRandomly(true);
        this.setCreativeTab(CreativeTabs.tabBlock);
    }
    
    @Override
    public IBlockState getActualState(final IBlockState state, final IBlockAccess worldIn, final BlockPos pos) {
        final Block block = worldIn.getBlockState(pos.up()).getBlock();
        return state.withProperty((IProperty<Comparable>)BlockGrass.SNOWY, block == Blocks.snow || block == Blocks.snow_layer);
    }
    
    @Override
    public int getBlockColor() {
        return ColorizerGrass.getGrassColor(0.5, 1.0);
    }
    
    @Override
    public int getRenderColor(final IBlockState state) {
        return this.getBlockColor();
    }
    
    @Override
    public int colorMultiplier(final IBlockAccess worldIn, final BlockPos pos, final int renderPass) {
        return BiomeColorHelper.getGrassColorAtPos(worldIn, pos);
    }
    
    @Override
    public void updateTick(final World worldIn, final BlockPos pos, final IBlockState state, final Random rand) {
        if (!worldIn.isRemote) {
            if (worldIn.getLightFromNeighbors(pos.up()) < 4 && worldIn.getBlockState(pos.up()).getBlock().getLightOpacity() > 2) {
                worldIn.setBlockState(pos, Blocks.dirt.getDefaultState());
            }
            else if (worldIn.getLightFromNeighbors(pos.up()) >= 9) {
                for (int i = 0; i < 4; ++i) {
                    final BlockPos blockpos = pos.add(rand.nextInt(3) - 1, rand.nextInt(5) - 3, rand.nextInt(3) - 1);
                    final Block block = worldIn.getBlockState(blockpos.up()).getBlock();
                    final IBlockState iblockstate = worldIn.getBlockState(blockpos);
                    if (iblockstate.getBlock() == Blocks.dirt && iblockstate.getValue(BlockDirt.VARIANT) == BlockDirt.DirtType.DIRT && worldIn.getLightFromNeighbors(blockpos.up()) >= 4 && block.getLightOpacity() <= 2) {
                        worldIn.setBlockState(blockpos, Blocks.grass.getDefaultState());
                    }
                }
            }
        }
    }
    
    @Override
    public Item getItemDropped(final IBlockState state, final Random rand, final int fortune) {
        return Blocks.dirt.getItemDropped(Blocks.dirt.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.DIRT), rand, fortune);
    }
    
    @Override
    public boolean canGrow(final World worldIn, final BlockPos pos, final IBlockState state, final boolean isClient) {
        return true;
    }
    
    @Override
    public boolean canUseBonemeal(final World worldIn, final Random rand, final BlockPos pos, final IBlockState state) {
        return true;
    }
    
    @Override
    public void grow(final World worldIn, final Random rand, final BlockPos pos, final IBlockState state) {
        final BlockPos blockpos = pos.up();
        int i = 0;
    Label_0255_Outer:
        while (i < 128) {
            BlockPos blockpos2 = blockpos;
            int j = 0;
            while (true) {
                while (j < i / 16) {
                    blockpos2 = blockpos2.add(rand.nextInt(3) - 1, (rand.nextInt(3) - 1) * rand.nextInt(3) / 2, rand.nextInt(3) - 1);
                    if (worldIn.getBlockState(blockpos2.down()).getBlock() == Blocks.grass) {
                        if (!worldIn.getBlockState(blockpos2).getBlock().isNormalCube()) {
                            ++j;
                            continue Label_0255_Outer;
                        }
                    }
                    ++i;
                    continue Label_0255_Outer;
                }
                if (worldIn.getBlockState(blockpos2).getBlock().blockMaterial != Material.air) {
                    continue;
                }
                if (rand.nextInt(8) == 0) {
                    final BlockFlower.EnumFlowerType blockflower$enumflowertype = worldIn.getBiomeGenForCoords(blockpos2).pickRandomFlower(rand, blockpos2);
                    final BlockFlower blockflower = blockflower$enumflowertype.getBlockType().getBlock();
                    final IBlockState iblockstate = blockflower.getDefaultState().withProperty(blockflower.getTypeProperty(), blockflower$enumflowertype);
                    if (blockflower.canBlockStay(worldIn, blockpos2, iblockstate)) {
                        worldIn.setBlockState(blockpos2, iblockstate, 3);
                    }
                    continue;
                }
                else {
                    final IBlockState iblockstate2 = Blocks.tallgrass.getDefaultState().withProperty(BlockTallGrass.TYPE, BlockTallGrass.EnumType.GRASS);
                    if (Blocks.tallgrass.canBlockStay(worldIn, blockpos2, iblockstate2)) {
                        worldIn.setBlockState(blockpos2, iblockstate2, 3);
                    }
                    continue;
                }
                break;
            }
        }
    }
    
    @Override
    public EnumWorldBlockLayer getBlockLayer() {
        return EnumWorldBlockLayer.CUTOUT_MIPPED;
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        return 0;
    }
    
    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[] { BlockGrass.SNOWY });
    }
}
