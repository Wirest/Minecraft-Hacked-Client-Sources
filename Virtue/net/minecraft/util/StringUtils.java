package net.minecraft.util;

import java.util.regex.Pattern;

public class StringUtils
{
    private static final Pattern patternControlCode = Pattern.compile("(?i)\\u00A7[0-9A-FK-OR]");
    private static final String __OBFID = "CL_00001501";

    /**
     * Returns the time elapsed for the given number of ticks, in "mm:ss" format.
     */
    public static String ticksToElapsedTime(int p_76337_0_)
    {
        int var1 = p_76337_0_ / 20;
        int var2 = var1 / 60;
        var1 %= 60;
        return var1 < 10 ? var2 + ":0" + var1 : var2 + ":" + var1;
    }

    public static String stripControlCodes(String p_76338_0_)
    {
        return patternControlCode.matcher(p_76338_0_).replaceAll("");
    }

    /**
     * Returns a value indicating whether the given string is null or empty.
     */
    public static boolean isNullOrEmpty(String p_151246_0_)
    {
        return org.apache.commons.lang3.StringUtils.isEmpty(p_151246_0_);
    }
}
