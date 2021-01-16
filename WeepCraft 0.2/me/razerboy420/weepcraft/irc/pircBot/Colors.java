/*
 * Decompiled with CFR 0_122.
 */
package me.razerboy420.weepcraft.irc.pircBot;

public class Colors {
    public static final String NORMAL = "\u000f";
    public static final String BOLD = "\u0002";
    public static final String UNDERLINE = "\u001f";
    public static final String REVERSE = "\u0016";
    public static final String WHITE = "\u000300";
    public static final String BLACK = "\u000301";
    public static final String DARK_BLUE = "\u000302";
    public static final String DARK_GREEN = "\u000303";
    public static final String RED = "\u000304";
    public static final String BROWN = "\u000305";
    public static final String PURPLE = "\u000306";
    public static final String OLIVE = "\u000307";
    public static final String YELLOW = "\u000308";
    public static final String GREEN = "\u000309";
    public static final String TEAL = "\u000310";
    public static final String CYAN = "\u000311";
    public static final String BLUE = "\u000312";
    public static final String MAGENTA = "\u000313";
    public static final String DARK_GRAY = "\u000314";
    public static final String LIGHT_GRAY = "\u000315";

    private Colors() {
    }

    public static String removeColors(String line) {
        int length = line.length();
        StringBuffer buffer = new StringBuffer();
        int i = 0;
        while (i < length) {
            char ch = line.charAt(i);
            if (ch == '\u0003') {
                if (++i >= length || !Character.isDigit(ch = line.charAt(i))) continue;
                if (++i < length && Character.isDigit(ch = line.charAt(i))) {
                    ++i;
                }
                if (i >= length || (ch = line.charAt(i)) != ',') continue;
                if (++i < length) {
                    ch = line.charAt(i);
                    if (Character.isDigit(ch)) {
                        if (++i >= length || !Character.isDigit(ch = line.charAt(i))) continue;
                        ++i;
                        continue;
                    }
                    --i;
                    continue;
                }
                --i;
                continue;
            }
            if (ch == '\u000f') {
                ++i;
                continue;
            }
            buffer.append(ch);
            ++i;
        }
        return buffer.toString();
    }

    public static String removeFormatting(String line) {
        int length = line.length();
        StringBuffer buffer = new StringBuffer();
        int i = 0;
        while (i < length) {
            char ch = line.charAt(i);
            if (ch != '\u000f' && ch != '\u0002' && ch != '\u001f' && ch != '\u0016') {
                buffer.append(ch);
            }
            ++i;
        }
        return buffer.toString();
    }

    public static String removeFormattingAndColors(String line) {
        return Colors.removeFormatting(Colors.removeColors(line));
    }
}

