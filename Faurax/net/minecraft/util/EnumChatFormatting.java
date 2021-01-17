package net.minecraft.util;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.regex.Pattern;

public enum EnumChatFormatting
{
    BLACK("BLACK", 0, "BLACK", '0', 0),
    DARK_BLUE("DARK_BLUE", 1, "DARK_BLUE", '1', 1),
    DARK_GREEN("DARK_GREEN", 2, "DARK_GREEN", '2', 2),
    DARK_AQUA("DARK_AQUA", 3, "DARK_AQUA", '3', 3),
    DARK_RED("DARK_RED", 4, "DARK_RED", '4', 4),
    DARK_PURPLE("DARK_PURPLE", 5, "DARK_PURPLE", '5', 5),
    GOLD("GOLD", 6, "GOLD", '6', 6),
    GRAY("GRAY", 7, "GRAY", '7', 7),
    DARK_GRAY("DARK_GRAY", 8, "DARK_GRAY", '8', 8),
    BLUE("BLUE", 9, "BLUE", '9', 9),
    GREEN("GREEN", 10, "GREEN", 'a', 10),
    AQUA("AQUA", 11, "AQUA", 'b', 11),
    RED("RED", 12, "RED", 'c', 12),
    LIGHT_PURPLE("LIGHT_PURPLE", 13, "LIGHT_PURPLE", 'd', 13),
    YELLOW("YELLOW", 14, "YELLOW", 'e', 14),
    WHITE("WHITE", 15, "WHITE", 'f', 15),
    OBFUSCATED("OBFUSCATED", 16, "OBFUSCATED", 'k', true),
    BOLD("BOLD", 17, "BOLD", 'l', true),
    STRIKETHROUGH("STRIKETHROUGH", 18, "STRIKETHROUGH", 'm', true),
    UNDERLINE("UNDERLINE", 19, "UNDERLINE", 'n', true),
    ITALIC("ITALIC", 20, "ITALIC", 'o', true),
    RESET("RESET", 21, "RESET", 'r', -1);

    /**
     * Maps a name (e.g., 'underline') to its corresponding enum value (e.g., UNDERLINE).
     */
    private static final Map nameMapping = Maps.newHashMap();

    /**
     * Matches formatting codes that indicate that the client should treat the following text as bold, recolored,
     * obfuscated, etc.
     */
    private static final Pattern formattingCodePattern = Pattern.compile("(?i)" + String.valueOf('\u00a7') + "[0-9A-FK-OR]");
    private final String field_175748_y;

    /** The formatting code that produces this format. */
    private final char formattingCode;
    private final boolean fancyStyling;

    /**
     * The control string (section sign + formatting code) that can be inserted into client-side text to display
     * subsequent text in this format.
     */
    private final String controlString;
    private final int field_175747_C;

    private static final EnumChatFormatting[] $VALUES = new EnumChatFormatting[]{BLACK, DARK_BLUE, DARK_GREEN, DARK_AQUA, DARK_RED, DARK_PURPLE, GOLD, GRAY, DARK_GRAY, BLUE, GREEN, AQUA, RED, LIGHT_PURPLE, YELLOW, WHITE, OBFUSCATED, BOLD, STRIKETHROUGH, UNDERLINE, ITALIC, RESET};
    private static final String __OBFID = "CL_00000342";

    private static String func_175745_c(String p_175745_0_)
    {
        return p_175745_0_.toLowerCase().replaceAll("[^a-z]", "");
    }

    private EnumChatFormatting(String p_i46291_1_, int p_i46291_2_, String p_i46291_3_, char p_i46291_4_, int p_i46291_5_)
    {
        this(p_i46291_1_, p_i46291_2_, p_i46291_3_, p_i46291_4_, false, p_i46291_5_);
    }

    private EnumChatFormatting(String p_i46292_1_, int p_i46292_2_, String p_i46292_3_, char p_i46292_4_, boolean p_i46292_5_)
    {
        this(p_i46292_1_, p_i46292_2_, p_i46292_3_, p_i46292_4_, p_i46292_5_, -1);
    }

    private EnumChatFormatting(String p_i46293_1_, int p_i46293_2_, String p_i46293_3_, char p_i46293_4_, boolean p_i46293_5_, int p_i46293_6_)
    {
        this.field_175748_y = p_i46293_3_;
        this.formattingCode = p_i46293_4_;
        this.fancyStyling = p_i46293_5_;
        this.field_175747_C = p_i46293_6_;
        this.controlString = "\u00a7" + p_i46293_4_;
    }

    public int func_175746_b()
    {
        return this.field_175747_C;
    }

    /**
     * False if this is just changing the color or resetting; true otherwise.
     */
    public boolean isFancyStyling()
    {
        return this.fancyStyling;
    }

    /**
     * Checks if this is a color code.
     */
    public boolean isColor()
    {
        return !this.fancyStyling && this != RESET;
    }

    /**
     * Gets the friendly name of this value.
     */
    public String getFriendlyName()
    {
        return this.name().toLowerCase();
    }

    public String toString()
    {
        return this.controlString;
    }

    /**
     * Returns a copy of the given string, with formatting codes stripped away.
     */
    public static String getTextWithoutFormattingCodes(String p_110646_0_)
    {
        return p_110646_0_ == null ? null : formattingCodePattern.matcher(p_110646_0_).replaceAll("");
    }

    /**
     * Gets a value by its friendly name; null if the given name does not map to a defined value.
     */
    public static EnumChatFormatting getValueByName(String p_96300_0_)
    {
        return p_96300_0_ == null ? null : (EnumChatFormatting)nameMapping.get(func_175745_c(p_96300_0_));
    }

    public static EnumChatFormatting func_175744_a(int p_175744_0_)
    {
        if (p_175744_0_ < 0)
        {
            return RESET;
        }
        else
        {
            EnumChatFormatting[] var1 = values();
            int var2 = var1.length;

            for (int var3 = 0; var3 < var2; ++var3)
            {
                EnumChatFormatting var4 = var1[var3];

                if (var4.func_175746_b() == p_175744_0_)
                {
                    return var4;
                }
            }

            return null;
        }
    }

    /**
     * Gets all the valid values. Args: @param par0: Whether or not to include color values. @param par1: Whether or not
     * to include fancy-styling values (anything that isn't a color value or the "reset" value).
     */
    public static Collection getValidValues(boolean p_96296_0_, boolean p_96296_1_)
    {
        ArrayList var2 = Lists.newArrayList();
        EnumChatFormatting[] var3 = values();
        int var4 = var3.length;

        for (int var5 = 0; var5 < var4; ++var5)
        {
            EnumChatFormatting var6 = var3[var5];

            if ((!var6.isColor() || p_96296_0_) && (!var6.isFancyStyling() || p_96296_1_))
            {
                var2.add(var6.getFriendlyName());
            }
        }

        return var2;
    }

    static {
        EnumChatFormatting[] var0 = values();
        int var1 = var0.length;

        for (int var2 = 0; var2 < var1; ++var2)
        {
            EnumChatFormatting var3 = var0[var2];
            nameMapping.put(func_175745_c(var3.field_175748_y), var3);
        }
    }
}
