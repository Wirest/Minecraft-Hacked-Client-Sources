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

public class BlockSandStone extends Block {
    public static final PropertyEnum field_176297_a = PropertyEnum.create("type", BlockSandStone.EnumType.class);
    private static final String __OBFID = "CL_00000304";

    public BlockSandStone() {
        super(Material.rock);
        setDefaultState(blockState.getBaseState().withProperty(BlockSandStone.field_176297_a, BlockSandStone.EnumType.DEFAULT));
        setCreativeTab(CreativeTabs.tabBlock);
    }

    /**
     * Get the damage value that this Block should drop
     */
    @Override
    public int damageDropped(IBlockState state) {
        return ((BlockSandStone.EnumType) state.getValue(BlockSandStone.field_176297_a)).func_176675_a();
    }

    /**
     * returns a list of blocks with the same ID, but different meta (eg: wood
     * returns 4 blocks)
     */
    @Override
    public void getSubBlocks(Item itemIn, CreativeTabs tab, List list) {
        BlockSandStone.EnumType[] var4 = BlockSandStone.EnumType.values();
        int var5 = var4.length;

        for (int var6 = 0; var6 < var5; ++var6) {
            BlockSandStone.EnumType var7 = var4[var6];
            list.add(new ItemStack(itemIn, 1, var7.func_176675_a()));
        }
    }

    /**
     * Convert the given metadata into a BlockState for this Block
     */
    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(BlockSandStone.field_176297_a, BlockSandStone.EnumType.func_176673_a(meta));
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    @Override
    public int getMetaFromState(IBlockState state) {
        return ((BlockSandStone.EnumType) state.getValue(BlockSandStone.field_176297_a)).func_176675_a();
    }

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[]{BlockSandStone.field_176297_a});
    }

    public static enum EnumType implements IStringSerializable {
        DEFAULT("DEFAULT", 0, 0, "sandstone", "default"), CHISELED("CHISELED", 1, 1, "chiseled_sandstone", "chiseled"), SMOOTH("SMOOTH", 2, 2, "smooth_sandstone", "smooth");
        private static final BlockSandStone.EnumType[] field_176679_d = new BlockSandStone.EnumType[EnumType.values().length];
        private final int field_176680_e;
        private final String field_176677_f;
        private final String field_176678_g;

        private static final BlockSandStone.EnumType[] $VALUES = new BlockSandStone.EnumType[]{DEFAULT, CHISELED, SMOOTH};
        private static final String __OBFID = "CL_00002068";

        private EnumType(String p_i45686_1_, int p_i45686_2_, int p_i45686_3_, String p_i45686_4_, String p_i45686_5_) {
            field_176680_e = p_i45686_3_;
            field_176677_f = p_i45686_4_;
            field_176678_g = p_i45686_5_;
        }

        public int func_176675_a() {
            return field_176680_e;
        }

        @Override
        public String toString() {
            return field_176677_f;
        }

        public static BlockSandStone.EnumType func_176673_a(int p_176673_0_) {
            if (p_176673_0_ < 0 || p_176673_0_ >= EnumType.field_176679_d.length) {
                p_176673_0_ = 0;
            }

            return EnumType.field_176679_d[p_176673_0_];
        }

        @Override
        public String getName() {
            return field_176677_f;
        }

        public String func_176676_c() {
            return field_176678_g;
        }

        static {
            BlockSandStone.EnumType[] var0 = EnumType.values();
            int var1 = var0.length;

            for (int var2 = 0; var2 < var1; ++var2) {
                BlockSandStone.EnumType var3 = var0[var2];
                EnumType.field_176679_d[var3.func_176675_a()] = var3;
            }
        }
    }
}
