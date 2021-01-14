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

public class BlockStoneBrick extends Block {
    public static final PropertyEnum VARIANT_PROP = PropertyEnum.create("variant", BlockStoneBrick.EnumType.class);
    public static final int DEFAULT_META = BlockStoneBrick.EnumType.DEFAULT.getMetaFromState();
    public static final int MOSSY_META = BlockStoneBrick.EnumType.MOSSY.getMetaFromState();
    public static final int CRACKED_META = BlockStoneBrick.EnumType.CRACKED.getMetaFromState();
    public static final int CHISELED_META = BlockStoneBrick.EnumType.CHISELED.getMetaFromState();
    private static final String __OBFID = "CL_00000318";

    public BlockStoneBrick() {
        super(Material.rock);
        this.setDefaultState(this.blockState.getBaseState().withProperty(VARIANT_PROP, BlockStoneBrick.EnumType.DEFAULT));
        this.setCreativeTab(CreativeTabs.tabBlock);
    }

    /**
     * Get the damage value that this Block should drop
     */
    public int damageDropped(IBlockState state) {
        return ((BlockStoneBrick.EnumType) state.getValue(VARIANT_PROP)).getMetaFromState();
    }

    /**
     * returns a list of blocks with the same ID, but different meta (eg: wood returns 4 blocks)
     */
    public void getSubBlocks(Item itemIn, CreativeTabs tab, List list) {
        BlockStoneBrick.EnumType[] var4 = BlockStoneBrick.EnumType.values();
        int var5 = var4.length;

        for (int var6 = 0; var6 < var5; ++var6) {
            BlockStoneBrick.EnumType var7 = var4[var6];
            list.add(new ItemStack(itemIn, 1, var7.getMetaFromState()));
        }
    }

    /**
     * Convert the given metadata into a BlockState for this Block
     */
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(VARIANT_PROP, BlockStoneBrick.EnumType.getStateFromMeta(meta));
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    public int getMetaFromState(IBlockState state) {
        return ((BlockStoneBrick.EnumType) state.getValue(VARIANT_PROP)).getMetaFromState();
    }

    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[]{VARIANT_PROP});
    }

    public static enum EnumType implements IStringSerializable {
        DEFAULT("DEFAULT", 0, 0, "stonebrick", "default"),
        MOSSY("MOSSY", 1, 1, "mossy_stonebrick", "mossy"),
        CRACKED("CRACKED", 2, 2, "cracked_stonebrick", "cracked"),
        CHISELED("CHISELED", 3, 3, "chiseled_stonebrick", "chiseled");
        private static final BlockStoneBrick.EnumType[] TYPES_ARRAY = new BlockStoneBrick.EnumType[values().length];
        private final int field_176615_f;
        private final String field_176616_g;
        private final String field_176622_h;

        private static final BlockStoneBrick.EnumType[] $VALUES = new BlockStoneBrick.EnumType[]{DEFAULT, MOSSY, CRACKED, CHISELED};
        private static final String __OBFID = "CL_00002057";

        private EnumType(String p_i45679_1_, int p_i45679_2_, int p_i45679_3_, String p_i45679_4_, String p_i45679_5_) {
            this.field_176615_f = p_i45679_3_;
            this.field_176616_g = p_i45679_4_;
            this.field_176622_h = p_i45679_5_;
        }

        public int getMetaFromState() {
            return this.field_176615_f;
        }

        public String toString() {
            return this.field_176616_g;
        }

        public static BlockStoneBrick.EnumType getStateFromMeta(int p_176613_0_) {
            if (p_176613_0_ < 0 || p_176613_0_ >= TYPES_ARRAY.length) {
                p_176613_0_ = 0;
            }

            return TYPES_ARRAY[p_176613_0_];
        }

        public String getName() {
            return this.field_176616_g;
        }

        public String getVariantName() {
            return this.field_176622_h;
        }

        static {
            BlockStoneBrick.EnumType[] var0 = values();
            int var1 = var0.length;

            for (int var2 = 0; var2 < var1; ++var2) {
                BlockStoneBrick.EnumType var3 = var0[var2];
                TYPES_ARRAY[var3.getMetaFromState()] = var3;
            }
        }
    }
}
