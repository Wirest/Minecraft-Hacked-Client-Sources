package net.minecraft.block;

import java.util.List;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;

public class BlockRedSandstone extends Block
{
    public static final PropertyEnum TYPE = PropertyEnum.create("type", BlockRedSandstone.EnumType.class);
    private static final String __OBFID = "CL_00002072";

    public BlockRedSandstone()
    {
        super(Material.rock);
        this.setDefaultState(this.blockState.getBaseState().withProperty(TYPE, BlockRedSandstone.EnumType.DEFAULT));
        this.setCreativeTab(CreativeTabs.tabBlock);
    }

    /**
     * Get the damage value that this Block should drop
     */
    public int damageDropped(IBlockState state)
    {
        return ((BlockRedSandstone.EnumType)state.getValue(TYPE)).getMetaFromState();
    }

    /**
     * returns a list of blocks with the same ID, but different meta (eg: wood returns 4 blocks)
     */
    public void getSubBlocks(Item itemIn, CreativeTabs tab, List list)
    {
        BlockRedSandstone.EnumType[] var4 = BlockRedSandstone.EnumType.values();
        int var5 = var4.length;

        for (int var6 = 0; var6 < var5; ++var6)
        {
            BlockRedSandstone.EnumType var7 = var4[var6];
            list.add(new ItemStack(itemIn, 1, var7.getMetaFromState()));
        }
    }

    /**
     * Convert the given metadata into a BlockState for this Block
     */
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(TYPE, BlockRedSandstone.EnumType.func_176825_a(meta));
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    public int getMetaFromState(IBlockState state)
    {
        return ((BlockRedSandstone.EnumType)state.getValue(TYPE)).getMetaFromState();
    }

    protected BlockState createBlockState()
    {
        return new BlockState(this, new IProperty[] {TYPE});
    }

    public static enum EnumType implements IStringSerializable
    {
        DEFAULT("DEFAULT", 0, 0, "red_sandstone", "default"),
        CHISELED("CHISELED", 1, 1, "chiseled_red_sandstone", "chiseled"),
        SMOOTH("SMOOTH", 2, 2, "smooth_red_sandstone", "smooth");
        private static final BlockRedSandstone.EnumType[] field_176831_d = new BlockRedSandstone.EnumType[values().length];
        private final int field_176832_e;
        private final String field_176829_f;
        private final String field_176830_g;

        private static final BlockRedSandstone.EnumType[] $VALUES = new BlockRedSandstone.EnumType[]{DEFAULT, CHISELED, SMOOTH};
        private static final String __OBFID = "CL_00002071";

        private EnumType(String p_i45690_1_, int p_i45690_2_, int p_i45690_3_, String p_i45690_4_, String p_i45690_5_)
        {
            this.field_176832_e = p_i45690_3_;
            this.field_176829_f = p_i45690_4_;
            this.field_176830_g = p_i45690_5_;
        }

        public int getMetaFromState()
        {
            return this.field_176832_e;
        }

        public String toString()
        {
            return this.field_176829_f;
        }

        public static BlockRedSandstone.EnumType func_176825_a(int p_176825_0_)
        {
            if (p_176825_0_ < 0 || p_176825_0_ >= field_176831_d.length)
            {
                p_176825_0_ = 0;
            }

            return field_176831_d[p_176825_0_];
        }

        public String getName()
        {
            return this.field_176829_f;
        }

        public String func_176828_c()
        {
            return this.field_176830_g;
        }

        static {
            BlockRedSandstone.EnumType[] var0 = values();
            int var1 = var0.length;

            for (int var2 = 0; var2 < var1; ++var2)
            {
                BlockRedSandstone.EnumType var3 = var0[var2];
                field_176831_d[var3.getMetaFromState()] = var3;
            }
        }
    }
}
