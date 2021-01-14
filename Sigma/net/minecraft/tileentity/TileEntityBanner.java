package net.minecraft.tileentity;

import com.google.common.collect.Lists;

import java.util.List;

import net.minecraft.block.BlockFlower;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;

public class TileEntityBanner extends TileEntity {
    private int baseColor;
    private NBTTagList field_175118_f;
    private boolean field_175119_g;
    private List field_175122_h;
    private List field_175123_i;
    private String field_175121_j;
    private static final String __OBFID = "CL_00002044";

    public void setItemValues(ItemStack p_175112_1_) {
        field_175118_f = null;

        if (p_175112_1_.hasTagCompound() && p_175112_1_.getTagCompound().hasKey("BlockEntityTag", 10)) {
            NBTTagCompound var2 = p_175112_1_.getTagCompound().getCompoundTag("BlockEntityTag");

            if (var2.hasKey("Patterns")) {
                field_175118_f = (NBTTagList) var2.getTagList("Patterns", 10).copy();
            }

            if (var2.hasKey("Base", 99)) {
                baseColor = var2.getInteger("Base");
            } else {
                baseColor = p_175112_1_.getMetadata() & 15;
            }
        } else {
            baseColor = p_175112_1_.getMetadata() & 15;
        }

        field_175122_h = null;
        field_175123_i = null;
        field_175121_j = "";
        field_175119_g = true;
    }

    @Override
    public void writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setInteger("Base", baseColor);

        if (field_175118_f != null) {
            compound.setTag("Patterns", field_175118_f);
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        baseColor = compound.getInteger("Base");
        field_175118_f = compound.getTagList("Patterns", 10);
        field_175122_h = null;
        field_175123_i = null;
        field_175121_j = null;
        field_175119_g = true;
    }

    /**
     * Overriden in a sign to provide the text.
     */
    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound var1 = new NBTTagCompound();
        writeToNBT(var1);
        return new S35PacketUpdateTileEntity(pos, 6, var1);
    }

    public int getBaseColor() {
        return baseColor;
    }

    public static int getBaseColor(ItemStack stack) {
        NBTTagCompound var1 = stack.getSubCompound("BlockEntityTag", false);
        return var1 != null && var1.hasKey("Base") ? var1.getInteger("Base") : stack.getMetadata();
    }

    public static int func_175113_c(ItemStack p_175113_0_) {
        NBTTagCompound var1 = p_175113_0_.getSubCompound("BlockEntityTag", false);
        return var1 != null && var1.hasKey("Patterns") ? var1.getTagList("Patterns", 10).tagCount() : 0;
    }

    public List func_175114_c() {
        func_175109_g();
        return field_175122_h;
    }

    public List func_175110_d() {
        func_175109_g();
        return field_175123_i;
    }

    public String func_175116_e() {
        func_175109_g();
        return field_175121_j;
    }

    private void func_175109_g() {
        if (field_175122_h == null || field_175123_i == null || field_175121_j == null) {
            if (!field_175119_g) {
                field_175121_j = "";
            } else {
                field_175122_h = Lists.newArrayList();
                field_175123_i = Lists.newArrayList();
                field_175122_h.add(TileEntityBanner.EnumBannerPattern.BASE);
                field_175123_i.add(EnumDyeColor.func_176766_a(baseColor));
                field_175121_j = "b" + baseColor;

                if (field_175118_f != null) {
                    for (int var1 = 0; var1 < field_175118_f.tagCount(); ++var1) {
                        NBTTagCompound var2 = field_175118_f.getCompoundTagAt(var1);
                        TileEntityBanner.EnumBannerPattern var3 = TileEntityBanner.EnumBannerPattern.func_177268_a(var2.getString("Pattern"));

                        if (var3 != null) {
                            field_175122_h.add(var3);
                            int var4 = var2.getInteger("Color");
                            field_175123_i.add(EnumDyeColor.func_176766_a(var4));
                            field_175121_j = field_175121_j + var3.func_177273_b() + var4;
                        }
                    }
                }
            }
        }
    }

    public static void func_175117_e(ItemStack p_175117_0_) {
        NBTTagCompound var1 = p_175117_0_.getSubCompound("BlockEntityTag", false);

        if (var1 != null && var1.hasKey("Patterns", 9)) {
            NBTTagList var2 = var1.getTagList("Patterns", 10);

            if (var2.tagCount() > 0) {
                var2.removeTag(var2.tagCount() - 1);

                if (var2.hasNoTags()) {
                    p_175117_0_.getTagCompound().removeTag("BlockEntityTag");

                    if (p_175117_0_.getTagCompound().hasNoTags()) {
                        p_175117_0_.setTagCompound((NBTTagCompound) null);
                    }
                }
            }
        }
    }

    public static enum EnumBannerPattern {
        BASE("BASE", 0, "base", "b"), SQUARE_BOTTOM_LEFT("SQUARE_BOTTOM_LEFT", 1, "square_bottom_left", "bl", "   ", "   ", "#  "), SQUARE_BOTTOM_RIGHT("SQUARE_BOTTOM_RIGHT", 2, "square_bottom_right", "br", "   ", "   ", "  #"), SQUARE_TOP_LEFT("SQUARE_TOP_LEFT", 3, "square_top_left", "tl", "#  ", "   ", "   "), SQUARE_TOP_RIGHT("SQUARE_TOP_RIGHT", 4, "square_top_right", "tr", "  #", "   ", "   "), STRIPE_BOTTOM("STRIPE_BOTTOM", 5, "stripe_bottom", "bs", "   ", "   ", "###"), STRIPE_TOP("STRIPE_TOP", 6, "stripe_top", "ts", "###", "   ", "   "), STRIPE_LEFT("STRIPE_LEFT", 7, "stripe_left", "ls", "#  ", "#  ", "#  "), STRIPE_RIGHT("STRIPE_RIGHT", 8, "stripe_right", "rs", "  #", "  #", "  #"), STRIPE_CENTER("STRIPE_CENTER", 9, "stripe_center", "cs", " # ", " # ", " # "), STRIPE_MIDDLE("STRIPE_MIDDLE", 10, "stripe_middle", "ms", "   ", "###", "   "), STRIPE_DOWNRIGHT("STRIPE_DOWNRIGHT", 11, "stripe_downright", "drs", "#  ", " # ", "  #"), STRIPE_DOWNLEFT("STRIPE_DOWNLEFT", 12,
                "stripe_downleft", "dls", "  #", " # ", "#  "), STRIPE_SMALL("STRIPE_SMALL", 13, "small_stripes", "ss", "# #", "# #", "   "), CROSS("CROSS", 14, "cross", "cr", "# #", " # ", "# #"), STRAIGHT_CROSS("STRAIGHT_CROSS", 15, "straight_cross", "sc", " # ", "###", " # "), TRIANGLE_BOTTOM("TRIANGLE_BOTTOM", 16, "triangle_bottom", "bt", "   ", " # ", "# #"), TRIANGLE_TOP("TRIANGLE_TOP", 17, "triangle_top", "tt", "# #", " # ", "   "), TRIANGLES_BOTTOM("TRIANGLES_BOTTOM", 18, "triangles_bottom", "bts", "   ", "# #", " # "), TRIANGLES_TOP("TRIANGLES_TOP", 19, "triangles_top", "tts", " # ", "# #", "   "), DIAGONAL_LEFT("DIAGONAL_LEFT", 20, "diagonal_left", "ld", "## ", "#  ", "   "), DIAGONAL_RIGHT("DIAGONAL_RIGHT", 21, "diagonal_up_right", "rd", "   ", "  #", " ##"), DIAGONAL_LEFT_MIRROR("DIAGONAL_LEFT_MIRROR", 22, "diagonal_up_left", "lud", "   ", "#  ", "## "), DIAGONAL_RIGHT_MIRROR("DIAGONAL_RIGHT_MIRROR", 23, "diagonal_right", "rud", " ##", "  #",
                "   "), CIRCLE_MIDDLE("CIRCLE_MIDDLE", 24, "circle", "mc", "   ", " # ", "   "), RHOMBUS_MIDDLE("RHOMBUS_MIDDLE", 25, "rhombus", "mr", " # ", "# #", " # "), HALF_VERTICAL("HALF_VERTICAL", 26, "half_vertical", "vh", "## ", "## ", "## "), HALF_HORIZONTAL("HALF_HORIZONTAL", 27, "half_horizontal", "hh", "###", "###", "   "), HALF_VERTICAL_MIRROR("HALF_VERTICAL_MIRROR", 28, "half_vertical_right", "vhr", " ##", " ##", " ##"), HALF_HORIZONTAL_MIRROR("HALF_HORIZONTAL_MIRROR", 29, "half_horizontal_bottom", "hhb", "   ", "###", "###"), BORDER("BORDER", 30, "border", "bo", "###", "# #", "###"), CURLY_BORDER("CURLY_BORDER", 31, "curly_border", "cbo", new ItemStack(Blocks.vine)), CREEPER("CREEPER", 32, "creeper", "cre", new ItemStack(Items.skull, 1, 4)), GRADIENT("GRADIENT", 33, "gradient", "gra", "# #", " # ", " # "), GRADIENT_UP("GRADIENT_UP", 34, "gradient_up", "gru", " # ", " # ", "# #"), BRICKS("BRICKS", 35, "bricks", "bri",
                new ItemStack(Blocks.brick_block)), SKULL("SKULL", 36, "skull", "sku", new ItemStack(Items.skull, 1, 1)), FLOWER("FLOWER", 37, "flower", "flo", new ItemStack(Blocks.red_flower, 1, BlockFlower.EnumFlowerType.OXEYE_DAISY.func_176968_b())), MOJANG("MOJANG", 38, "mojang", "moj", new ItemStack(Items.golden_apple, 1, 1));
        private String field_177284_N;
        private String field_177285_O;
        private String[] field_177291_P;
        private ItemStack field_177290_Q;

        private static final TileEntityBanner.EnumBannerPattern[] $VALUES = new TileEntityBanner.EnumBannerPattern[]{BASE, SQUARE_BOTTOM_LEFT, SQUARE_BOTTOM_RIGHT, SQUARE_TOP_LEFT, SQUARE_TOP_RIGHT, STRIPE_BOTTOM, STRIPE_TOP, STRIPE_LEFT, STRIPE_RIGHT, STRIPE_CENTER, STRIPE_MIDDLE, STRIPE_DOWNRIGHT, STRIPE_DOWNLEFT, STRIPE_SMALL, CROSS, STRAIGHT_CROSS, TRIANGLE_BOTTOM, TRIANGLE_TOP, TRIANGLES_BOTTOM, TRIANGLES_TOP, DIAGONAL_LEFT, DIAGONAL_RIGHT, DIAGONAL_LEFT_MIRROR, DIAGONAL_RIGHT_MIRROR, CIRCLE_MIDDLE, RHOMBUS_MIDDLE, HALF_VERTICAL, HALF_HORIZONTAL, HALF_VERTICAL_MIRROR, HALF_HORIZONTAL_MIRROR, BORDER, CURLY_BORDER, CREEPER, GRADIENT, GRADIENT_UP, BRICKS, SKULL, FLOWER, MOJANG};
        private static final String __OBFID = "CL_00002043";

        private EnumBannerPattern(String p_i45670_1_, int p_i45670_2_, String p_i45670_3_, String p_i45670_4_) {
            field_177291_P = new String[3];
            field_177284_N = p_i45670_3_;
            field_177285_O = p_i45670_4_;
        }

        private EnumBannerPattern(String p_i45671_1_, int p_i45671_2_, String p_i45671_3_, String p_i45671_4_, ItemStack p_i45671_5_) {
            this(p_i45671_1_, p_i45671_2_, p_i45671_3_, p_i45671_4_);
            field_177290_Q = p_i45671_5_;
        }

        private EnumBannerPattern(String p_i45672_1_, int p_i45672_2_, String p_i45672_3_, String p_i45672_4_, String p_i45672_5_, String p_i45672_6_, String p_i45672_7_) {
            this(p_i45672_1_, p_i45672_2_, p_i45672_3_, p_i45672_4_);
            field_177291_P[0] = p_i45672_5_;
            field_177291_P[1] = p_i45672_6_;
            field_177291_P[2] = p_i45672_7_;
        }

        public String func_177271_a() {
            return field_177284_N;
        }

        public String func_177273_b() {
            return field_177285_O;
        }

        public String[] func_177267_c() {
            return field_177291_P;
        }

        public boolean func_177270_d() {
            return field_177290_Q != null || field_177291_P[0] != null;
        }

        public boolean func_177269_e() {
            return field_177290_Q != null;
        }

        public ItemStack func_177272_f() {
            return field_177290_Q;
        }

        public static TileEntityBanner.EnumBannerPattern func_177268_a(String p_177268_0_) {
            TileEntityBanner.EnumBannerPattern[] var1 = EnumBannerPattern.values();
            int var2 = var1.length;

            for (int var3 = 0; var3 < var2; ++var3) {
                TileEntityBanner.EnumBannerPattern var4 = var1[var3];

                if (var4.field_177285_O.equals(p_177268_0_)) {
                    return var4;
                }
            }

            return null;
        }
    }
}
