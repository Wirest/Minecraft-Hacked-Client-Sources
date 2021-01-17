package me.ihaq.iClient.utils;

import java.util.regex.Pattern;

public class StringUtil {
    private static final Pattern patternControlCode;
    private static final Pattern patternColorCode;
    private static final Pattern patternFormatCode;

    static {
        patternControlCode = Pattern.compile("(?i)\\u00A7[0-9A-FK-OR]");
        patternColorCode = Pattern.compile("(?i)\\u00A7[0-9A-F]");
        patternFormatCode = Pattern.compile("(?i)\\u00A7[K-O]");
    }

    public static String capitalize(String line) {
        return Character.toUpperCase(line.charAt(0)) + line.substring(1);
    }

    public static String stripControlCodes(final String s) {
        return StringUtil.patternControlCode.matcher(s).replaceAll("");
    }

    public static String stripColorCodes(final String s) {
        return StringUtil.patternColorCode.matcher(s).replaceAll("");
    }

    public static String stripFormatCodes(final String s) {
        return StringUtil.patternFormatCode.matcher(s).replaceAll("");
    }
}
