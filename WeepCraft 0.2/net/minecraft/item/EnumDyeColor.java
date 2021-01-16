package net.minecraft.item;

import net.minecraft.util.IStringSerializable;
import net.minecraft.util.text.TextFormatting;

public enum EnumDyeColor implements IStringSerializable
{
    WHITE(0, 15, "white", "white", 16383998, TextFormatting.WHITE),
    ORANGE(1, 14, "orange", "orange", 16351261, TextFormatting.GOLD),
    MAGENTA(2, 13, "magenta", "magenta", 13061821, TextFormatting.AQUA),
    LIGHT_BLUE(3, 12, "light_blue", "lightBlue", 3847130, TextFormatting.BLUE),
    YELLOW(4, 11, "yellow", "yellow", 16701501, TextFormatting.YELLOW),
    LIME(5, 10, "lime", "lime", 8439583, TextFormatting.GREEN),
    PINK(6, 9, "pink", "pink", 15961002, TextFormatting.LIGHT_PURPLE),
    GRAY(7, 8, "gray", "gray", 4673362, TextFormatting.DARK_GRAY),
    SILVER(8, 7, "silver", "silver", 10329495, TextFormatting.GRAY),
    CYAN(9, 6, "cyan", "cyan", 1481884, TextFormatting.DARK_AQUA),
    PURPLE(10, 5, "purple", "purple", 8991416, TextFormatting.DARK_PURPLE),
    BLUE(11, 4, "blue", "blue", 3949738, TextFormatting.DARK_BLUE),
    BROWN(12, 3, "brown", "brown", 8606770, TextFormatting.GOLD),
    GREEN(13, 2, "green", "green", 6192150, TextFormatting.DARK_GREEN),
    RED(14, 1, "red", "red", 11546150, TextFormatting.DARK_RED),
    BLACK(15, 0, "black", "black", 1908001, TextFormatting.BLACK);

    private static final EnumDyeColor[] META_LOOKUP = new EnumDyeColor[values().length];
    private static final EnumDyeColor[] DYE_DMG_LOOKUP = new EnumDyeColor[values().length];
    private final int meta;
    private final int dyeDamage;
    private final String name;
    private final String unlocalizedName;
    private final int field_193351_w;
    private final float[] field_193352_x;
    private final TextFormatting chatColor;

    private EnumDyeColor(int p_i47505_3_, int p_i47505_4_, String p_i47505_5_, String p_i47505_6_, int p_i47505_7_, TextFormatting p_i47505_8_)
    {
        this.meta = p_i47505_3_;
        this.dyeDamage = p_i47505_4_;
        this.name = p_i47505_5_;
        this.unlocalizedName = p_i47505_6_;
        this.field_193351_w = p_i47505_7_;
        this.chatColor = p_i47505_8_;
        int i = (p_i47505_7_ & 16711680) >> 16;
        int j = (p_i47505_7_ & 65280) >> 8;
        int k = (p_i47505_7_ & 255) >> 0;
        this.field_193352_x = new float[] {(float)i / 255.0F, (float)j / 255.0F, (float)k / 255.0F};
    }

    public int getMetadata()
    {
        return this.meta;
    }

    public int getDyeDamage()
    {
        return this.dyeDamage;
    }

    public String func_192396_c()
    {
        return this.name;
    }

    public String getUnlocalizedName()
    {
        return this.unlocalizedName;
    }

    public int func_193350_e()
    {
        return this.field_193351_w;
    }

    public float[] func_193349_f()
    {
        return this.field_193352_x;
    }

    public static EnumDyeColor byDyeDamage(int damage)
    {
        if (damage < 0 || damage >= DYE_DMG_LOOKUP.length)
        {
            damage = 0;
        }

        return DYE_DMG_LOOKUP[damage];
    }

    public static EnumDyeColor byMetadata(int meta)
    {
        if (meta < 0 || meta >= META_LOOKUP.length)
        {
            meta = 0;
        }

        return META_LOOKUP[meta];
    }

    public String toString()
    {
        return this.unlocalizedName;
    }

    public String getName()
    {
        return this.name;
    }

    static {
        for (EnumDyeColor enumdyecolor : values())
        {
            META_LOOKUP[enumdyecolor.getMetadata()] = enumdyecolor;
            DYE_DMG_LOOKUP[enumdyecolor.getDyeDamage()] = enumdyecolor;
        }
    }
}
