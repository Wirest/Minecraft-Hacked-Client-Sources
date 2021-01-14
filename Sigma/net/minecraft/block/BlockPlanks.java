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

public class BlockPlanks extends Block {
    public static final PropertyEnum VARIANT_PROP = PropertyEnum.create("variant", BlockPlanks.EnumType.class);
    private static final String __OBFID = "CL_00002082";

    public BlockPlanks() {
        super(Material.wood);
        setDefaultState(blockState.getBaseState().withProperty(BlockPlanks.VARIANT_PROP, BlockPlanks.EnumType.OAK));
        setCreativeTab(CreativeTabs.tabBlock);
    }

    /**
     * Get the damage value that this Block should drop
     */
    @Override
    public int damageDropped(IBlockState state) {
        return ((BlockPlanks.EnumType) state.getValue(BlockPlanks.VARIANT_PROP)).func_176839_a();
    }

    /**
     * returns a list of blocks with the same ID, but different meta (eg: wood
     * returns 4 blocks)
     */
    @Override
    public void getSubBlocks(Item itemIn, CreativeTabs tab, List list) {
        BlockPlanks.EnumType[] var4 = BlockPlanks.EnumType.values();
        int var5 = var4.length;

        for (int var6 = 0; var6 < var5; ++var6) {
            BlockPlanks.EnumType var7 = var4[var6];
            list.add(new ItemStack(itemIn, 1, var7.func_176839_a()));
        }
    }

    /**
     * Convert the given metadata into a BlockState for this Block
     */
    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(BlockPlanks.VARIANT_PROP, BlockPlanks.EnumType.func_176837_a(meta));
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    @Override
    public int getMetaFromState(IBlockState state) {
        return ((BlockPlanks.EnumType) state.getValue(BlockPlanks.VARIANT_PROP)).func_176839_a();
    }

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[]{BlockPlanks.VARIANT_PROP});
    }

    public static enum EnumType implements IStringSerializable {
        OAK("OAK", 0, 0, "oak"), SPRUCE("SPRUCE", 1, 1, "spruce"), BIRCH("BIRCH", 2, 2, "birch"), JUNGLE("JUNGLE", 3, 3, "jungle"), ACACIA("ACACIA", 4, 4, "acacia"), DARK_OAK("DARK_OAK", 5, 5, "dark_oak", "big_oak");
        private static final BlockPlanks.EnumType[] field_176842_g = new BlockPlanks.EnumType[EnumType.values().length];
        private final int field_176850_h;
        private final String field_176851_i;
        private final String field_176848_j;

        private static final BlockPlanks.EnumType[] $VALUES = new BlockPlanks.EnumType[]{OAK, SPRUCE, BIRCH, JUNGLE, ACACIA, DARK_OAK};
        private static final String __OBFID = "CL_00002081";

        private EnumType(String p_i45695_1_, int p_i45695_2_, int p_i45695_3_, String p_i45695_4_) {
            this(p_i45695_1_, p_i45695_2_, p_i45695_3_, p_i45695_4_, p_i45695_4_);
        }

        private EnumType(String p_i45696_1_, int p_i45696_2_, int p_i45696_3_, String p_i45696_4_, String p_i45696_5_) {
            field_176850_h = p_i45696_3_;
            field_176851_i = p_i45696_4_;
            field_176848_j = p_i45696_5_;
        }

        public int func_176839_a() {
            return field_176850_h;
        }

        @Override
        public String toString() {
            return field_176851_i;
        }

        public static BlockPlanks.EnumType func_176837_a(int p_176837_0_) {
            if (p_176837_0_ < 0 || p_176837_0_ >= EnumType.field_176842_g.length) {
                p_176837_0_ = 0;
            }

            return EnumType.field_176842_g[p_176837_0_];
        }

        @Override
        public String getName() {
            return field_176851_i;
        }

        public String func_176840_c() {
            return field_176848_j;
        }

        static {
            BlockPlanks.EnumType[] var0 = EnumType.values();
            int var1 = var0.length;

            for (int var2 = 0; var2 < var1; ++var2) {
                BlockPlanks.EnumType var3 = var0[var2];
                EnumType.field_176842_g[var3.func_176839_a()] = var3;
            }
        }
    }
}
