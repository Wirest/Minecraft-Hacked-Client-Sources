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
        this.setDefaultState(this.blockState.getBaseState().withProperty(field_176547_a, EnumDyeColor.WHITE));
        this.setCreativeTab(CreativeTabs.tabBlock);
    }

    /**
     * Get the damage value that this Block should drop
     */
    public int damageDropped(IBlockState state) {
        return ((EnumDyeColor) state.getValue(field_176547_a)).func_176765_a();
    }

    /**
     * returns a list of blocks with the same ID, but different meta (eg: wood returns 4 blocks)
     */
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
    public MapColor getMapColor(IBlockState state) {
        return ((EnumDyeColor) state.getValue(field_176547_a)).func_176768_e();
    }

    public EnumWorldBlockLayer getBlockLayer() {
        return EnumWorldBlockLayer.TRANSLUCENT;
    }

    /**
     * Returns the quantity of items to drop on block destruction.
     */
    public int quantityDropped(Random random) {
        return 0;
    }

    protected boolean canSilkHarvest() {
        return true;
    }

    public boolean isFullCube() {
        return false;
    }

    /**
     * Convert the given metadata into a BlockState for this Block
     */
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(field_176547_a, EnumDyeColor.func_176764_b(meta));
    }

    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        if (!worldIn.isRemote) {
            BlockBeacon.func_176450_d(worldIn, pos);
        }
    }

    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        if (!worldIn.isRemote) {
            BlockBeacon.func_176450_d(worldIn, pos);
        }
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    public int getMetaFromState(IBlockState state) {
        return ((EnumDyeColor) state.getValue(field_176547_a)).func_176765_a();
    }

    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[]{field_176547_a});
    }
}
