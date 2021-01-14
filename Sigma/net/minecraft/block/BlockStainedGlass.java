package net.minecraft.block;

import java.util.List;
import java.util.Random;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.World;

public class BlockStainedGlass extends BlockBreakable {
    public static final PropertyEnum field_176547_a = PropertyEnum.create("color", EnumDyeColor.class);
    private static final String __OBFID = "CL_00000312";

    public BlockStainedGlass(Material p_i45427_1_) {
        super(p_i45427_1_, false);
        setDefaultState(blockState.getBaseState().withProperty(BlockStainedGlass.field_176547_a, EnumDyeColor.WHITE));
        setCreativeTab(CreativeTabs.tabBlock);
    }

    /**
     * Get the damage value that this Block should drop
     */
    @Override
    public int damageDropped(IBlockState state) {
        return ((EnumDyeColor) state.getValue(BlockStainedGlass.field_176547_a)).func_176765_a();
    }

    /**
     * returns a list of blocks with the same ID, but different meta (eg: wood
     * returns 4 blocks)
     */
    @Override
    public void getSubBlocks(Item itemIn, CreativeTabs tab, List list) {
        EnumDyeColor[] var4 = EnumDyeColor.values();
        int var5 = var4.length;

        for (int var6 = 0; var6 < var5; ++var6) {
            EnumDyeColor var7 = var4[var6];
            list.add(new ItemStack(itemIn, 1, var7.func_176765_a()));
        }
    }

    /**
     * Get the MapColor for this Block and the given BlockState
     */
    @Override
    public MapColor getMapColor(IBlockState state) {
        return ((EnumDyeColor) state.getValue(BlockStainedGlass.field_176547_a)).func_176768_e();
    }

    @Override
    public EnumWorldBlockLayer getBlockLayer() {
        return EnumWorldBlockLayer.TRANSLUCENT;
    }

    /**
     * Returns the quantity of items to drop on block destruction.
     */
    @Override
    public int quantityDropped(Random random) {
        return 0;
    }

    @Override
    protected boolean canSilkHarvest() {
        return true;
    }

    @Override
    public boolean isFullCube() {
        return false;
    }

    /**
     * Convert the given metadata into a BlockState for this Block
     */
    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(BlockStainedGlass.field_176547_a, EnumDyeColor.func_176764_b(meta));
    }

    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        if (!worldIn.isRemote) {
            BlockBeacon.func_176450_d(worldIn, pos);
        }
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        if (!worldIn.isRemote) {
            BlockBeacon.func_176450_d(worldIn, pos);
        }
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    @Override
    public int getMetaFromState(IBlockState state) {
        return ((EnumDyeColor) state.getValue(BlockStainedGlass.field_176547_a)).func_176765_a();
    }

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[]{BlockStainedGlass.field_176547_a});
    }
}
