package net.minecraft.item;

import net.minecraft.block.material.MapColor;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IStringSerializable;

public enum EnumDyeColor implements IStringSerializable
{
    WHITE("WHITE", 0, 0, 15, "white", "white", MapColor.snowColor, EnumChatFormatting.WHITE),
    ORANGE("ORANGE", 1, 1, 14, "orange", "orange", MapColor.adobeColor, EnumChatFormatting.GOLD),
    MAGENTA("MAGENTA", 2, 2, 13, "magenta", "magenta", MapColor.magentaColor, EnumChatFormatting.AQUA),
    LIGHT_BLUE("LIGHT_BLUE", 3, 3, 12, "light_blue", "lightBlue", MapColor.lightBlueColor, EnumChatFormatting.BLUE),
    YELLOW("YELLOW", 4, 4, 11, "yellow", "yellow", MapColor.yellowColor, EnumChatFormatting.YELLOW),
    LIME("LIME", 5, 5, 10, "lime", "lime", MapColor.limeColor, EnumChatFormatting.GREEN),
    PINK("PINK", 6, 6, 9, "pink", "pink", MapColor.pinkColor, EnumChatFormatting.LIGHT_PURPLE),
    GRAY("GRAY", 7, 7, 8, "gray", "gray", MapColor.grayColor, EnumChatFormatting.DARK_GRAY),
    SILVER("SILVER", 8, 8, 7, "silver", "silver", MapColor.silverColor, EnumChatFormatting.GRAY),
    CYAN("CYAN", 9, 9, 6, "cyan", "cyan", MapColor.cyanColor, EnumChatFormatting.DARK_AQUA),
    PURPLE("PURPLE", 10, 10, 5, "purple", "purple", MapColor.purpleColor, EnumChatFormatting.DARK_PURPLE),
    BLUE("BLUE", 11, 11, 4, "blue", "blue", MapColor.blueColor, EnumChatFormatting.DARK_BLUE),
    BROWN("BROWN", 12, 12, 3, "brown", "brown", MapColor.brownColor, EnumChatFormatting.GOLD),
    GREEN("GREEN", 13, 13, 2, "green", "green", MapColor.greenColor, EnumChatFormatting.DARK_GREEN),
    RED("RED", 14, 14, 1, "red", "red", MapColor.redColor, EnumChatFormatting.DARK_RED),
    BLACK("BLACK", 15, 15, 0, "black", "black", MapColor.blackColor, EnumChatFormatting.BLACK);
    private static final EnumDyeColor[] field_176790_q = new EnumDyeColor[values().length];
    private static final EnumDyeColor[] field_176789_r = new EnumDyeColor[values().length];
    private final int field_176788_s;
    private final int field_176787_t;
    private final String field_176786_u;
    private final String field_176785_v;
    private final MapColor field_176784_w;
    private final EnumChatFormatting field_176793_x;

    private static final EnumDyeColor[] $VALUES = new EnumDyeColor[]{WHITE, ORANGE, MAGENTA, LIGHT_BLUE, YELLOW, LIME, PINK, GRAY, SILVER, CYAN, PURPLE, BLUE, BROWN, GREEN, RED, BLACK};
    private static final String __OBFID = "CL_00002180";

    private EnumDyeColor(String p_i45786_1_, int p_i45786_2_, int p_i45786_3_, int p_i45786_4_, String p_i45786_5_, String p_i45786_6_, MapColor p_i45786_7_, EnumChatFormatting p_i45786_8_)
    {
        this.field_176788_s = p_i45786_3_;
        this.field_176787_t = p_i45786_4_;
        this.field_176786_u = p_i45786_5_;
        this.field_176785_v = p_i45786_6_;
        this.field_176784_w = p_i45786_7_;
        this.field_176793_x = p_i45786_8_;
    }

    public int func_176765_a()
    {
        return this.field_176788_s;
    }

    public int getDyeColorDamage()
    {
        return this.field_176787_t;
    }

    public String func_176762_d()
    {
        return this.field_176785_v;
    }

    public MapColor func_176768_e()
    {
        return this.field_176784_w;
    }

    public static EnumDyeColor func_176766_a(int p_176766_0_)
    {
        if (p_176766_0_ < 0 || p_176766_0_ >= field_176789_r.length)
        {
            p_176766_0_ = 0;
        }

        return field_176789_r[p_176766_0_];
    }

    public static EnumDyeColor func_176764_b(int p_176764_0_)
    {
        if (p_176764_0_ < 0 || p_176764_0_ >= field_176790_q.length)
        {
            p_176764_0_ = 0;
        }

        return field_176790_q[p_176764_0_];
    }

    public String toString()
    {
        return this.field_176785_v;
    }

    public String getName()
    {
        return this.field_176786_u;
    }

    static {
        EnumDyeColor[] var0 = values();
        int var1 = var0.length;

        for (int var2 = 0; var2 < var1; ++var2)
        {
            EnumDyeColor var3 = var0[var2];
            field_176790_q[var3.func_176765_a()] = var3;
            field_176789_r[var3.getDyeColorDamage()] = var3;
        }
    }
}
