package net.minecraft.block;

import java.util.List;

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

public class BlockColored extends Block {
    public static final PropertyEnum COLOR = PropertyEnum.create("color", EnumDyeColor.class);
    private static final String __OBFID = "CL_00000217";

    public BlockColored(Material p_i45398_1_) {
        super(p_i45398_1_);
        this.setDefaultState(this.blockState.getBaseState().withProperty(COLOR, EnumDyeColor.WHITE));
        this.setCreativeTab(CreativeTabs.tabBlock);
    }

    /**
     * Get the damage value that this Block should drop
     */
    public int damageDropped(IBlockState state) {
        return ((EnumDyeColor) state.getValue(COLOR)).func_176765_a();
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
        return ((EnumDyeColor) state.getValue(COLOR)).func_176768_e();
    }

    /**
     * Convert the given metadata into a BlockState for this Block
     */
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(COLOR, EnumDyeColor.func_176764_b(meta));
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    public int getMetaFromState(IBlockState state) {
        return ((EnumDyeColor) state.getValue(COLOR)).func_176765_a();
    }

    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[]{COLOR});
    }
}
