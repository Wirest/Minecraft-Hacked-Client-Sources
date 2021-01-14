package net.minecraft.block;

import java.util.Random;

import info.sigmaclient.Client;
import info.sigmaclient.module.impl.render.Xray;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.ColorizerGrass;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeColorHelper;

public class BlockGrass extends Block implements IGrowable {
    public static final PropertyBool SNOWY = PropertyBool.create("snowy");
    private static final String __OBFID = "CL_00000251";

    protected BlockGrass() {
        super(Material.grass);
        setDefaultState(blockState.getBaseState().withProperty(BlockGrass.SNOWY, Boolean.valueOf(false)));
        setTickRandomly(true);
        setCreativeTab(CreativeTabs.tabBlock);
    }

    /**
     * Get the actual Block state of this Block at the given position. This
     * applies properties not visible in the metadata, such as fence
     * connections.
     */
    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        Block var4 = worldIn.getBlockState(pos.offsetUp()).getBlock();
        return state.withProperty(BlockGrass.SNOWY, Boolean.valueOf(var4 == Blocks.snow || var4 == Blocks.snow_layer));
    }

    @Override
    public int getBlockColor() {
        return ColorizerGrass.getGrassColor(0.5D, 1.0D);
    }

    @Override
    public int getRenderColor(IBlockState state) {
        return getBlockColor();
    }

    @Override
    public int colorMultiplier(IBlockAccess worldIn, BlockPos pos, int renderPass) {
        return BiomeColorHelper.func_180286_a(worldIn, pos);
    }

    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        if (!worldIn.isRemote) {
            if (worldIn.getLightFromNeighbors(pos.offsetUp()) < 4 && worldIn.getBlockState(pos.offsetUp()).getBlock().getLightOpacity() > 2) {
                worldIn.setBlockState(pos, Blocks.dirt.getDefaultState());
            } else {
                if (worldIn.getLightFromNeighbors(pos.offsetUp()) >= 9) {
                    for (int var5 = 0; var5 < 4; ++var5) {
                        BlockPos var6 = pos.add(rand.nextInt(3) - 1, rand.nextInt(5) - 3, rand.nextInt(3) - 1);
                        Block var7 = worldIn.getBlockState(var6.offsetUp()).getBlock();
                        IBlockState var8 = worldIn.getBlockState(var6);

                        if (var8.getBlock() == Blocks.dirt && var8.getValue(BlockDirt.VARIANT) == BlockDirt.DirtType.DIRT && worldIn.getLightFromNeighbors(var6.offsetUp()) >= 4 && var7.getLightOpacity() <= 2) {
                            worldIn.setBlockState(var6, Blocks.grass.getDefaultState());
                        }
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
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Blocks.dirt.getItemDropped(Blocks.dirt.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.DIRT), rand, fortune);
    }

    @Override
    public boolean isStillGrowing(World worldIn, BlockPos p_176473_2_, IBlockState p_176473_3_, boolean p_176473_4_) {
        return true;
    }

    @Override
    public boolean canUseBonemeal(World worldIn, Random p_180670_2_, BlockPos p_180670_3_, IBlockState p_180670_4_) {
        return true;
    }

    @Override
    public void grow(World worldIn, Random p_176474_2_, BlockPos p_176474_3_, IBlockState p_176474_4_) {
        BlockPos var5 = p_176474_3_.offsetUp();
        int var6 = 0;

        while (var6 < 128) {
            BlockPos var7 = var5;
            int var8 = 0;

            while (true) {
                if (var8 < var6 / 16) {
                    var7 = var7.add(p_176474_2_.nextInt(3) - 1, (p_176474_2_.nextInt(3) - 1) * p_176474_2_.nextInt(3) / 2, p_176474_2_.nextInt(3) - 1);

                    if (worldIn.getBlockState(var7.offsetDown()).getBlock() == Blocks.grass && !worldIn.getBlockState(var7).getBlock().isNormalCube()) {
                        ++var8;
                        continue;
                    }
                } else if (worldIn.getBlockState(var7).getBlock().blockMaterial == Material.air) {
                    if (p_176474_2_.nextInt(8) == 0) {
                        BlockFlower.EnumFlowerType var11 = worldIn.getBiomeGenForCoords(var7).pickRandomFlower(p_176474_2_, var7);
                        BlockFlower var9 = var11.func_176964_a().func_180346_a();
                        IBlockState var10 = var9.getDefaultState().withProperty(var9.func_176494_l(), var11);

                        if (var9.canBlockStay(worldIn, var7, var10)) {
                            worldIn.setBlockState(var7, var10, 3);
                        }
                    } else {
                        IBlockState var12 = Blocks.tallgrass.getDefaultState().withProperty(BlockTallGrass.field_176497_a, BlockTallGrass.EnumType.GRASS);

                        if (Blocks.tallgrass.canBlockStay(worldIn, var7, var12)) {
                            worldIn.setBlockState(var7, var12, 3);
                        }
                    }
                }

                ++var6;
                break;
            }
        }
    }

    @Override
    public EnumWorldBlockLayer getBlockLayer() {
        if (Client.getModuleManager().isEnabled(Xray.class)) {
            return super.getBlockLayer();
        }
        return EnumWorldBlockLayer.CUTOUT_MIPPED;
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    @Override
    public int getMetaFromState(IBlockState state) {
        return 0;
    }

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[]{BlockGrass.SNOWY});
    }
}
