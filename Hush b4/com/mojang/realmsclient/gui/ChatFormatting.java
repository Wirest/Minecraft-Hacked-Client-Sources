// 
// Decompiled by Procyon v0.5.36
// 

package com.mojang.realmsclient.gui;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import java.util.regex.Pattern;
import java.util.Map;

public enum ChatFormatting
{
    BLACK('0'), 
    DARK_BLUE('1'), 
    DARK_GREEN('2'), 
    DARK_AQUA('3'), 
    DARK_RED('4'), 
    DARK_PURPLE('5'), 
    GOLD('6'), 
    GRAY('7'), 
    DARK_GRAY('8'), 
    BLUE('9'), 
    GREEN('a'), 
    AQUA('b'), 
    RED('c'), 
    LIGHT_PURPLE('d'), 
    YELLOW('e'), 
    WHITE('f'), 
    OBFUSCATED('k', true), 
    BOLD('l', true), 
    STRIKETHROUGH('m', true), 
    UNDERLINE('n', true), 
    ITALIC('o', true), 
    RESET('r');
    
    public static final char PREFIX_CODE = '§';
    private static final Map<Character, ChatFormatting> FORMATTING_BY_CHAR;
    private static final Map<String, ChatFormatting> FORMATTING_BY_NAME;
    private static final Pattern STRIP_FORMATTING_PATTERN;
    private final char code;
    private final boolean isFormat;
    private final String toString;
    
    private ChatFormatting(final char code) {
        this(code, false);
    }
    
    private ChatFormatting(final char code, final boolean isFormat) {
        this.code = code;
        this.isFormat = isFormat;
        this.toString = "§" + code;
    }
    
    public char getChar() {
        return this.code;
    }
    
    public boolean isFormat() {
        return this.isFormat;
    }
    
    public boolean isColor() {
        return !this.isFormat && this != ChatFormatting.RESET;
    }
    
    public String getName() {
        return this.name().toLowerCase();
    }
    
    @Override
    public String toString() {
        return this.toString;
    }
    
    public static String stripFormatting(final String input) {
        return (input == null) ? null : ChatFormatting.STRIP_FORMATTING_PATTERN.matcher(input).replaceAll("");
    }
    
    public static ChatFormatting getByChar(final char code) {
        return ChatFormatting.FORMATTING_BY_CHAR.get(code);
    }
    
    public static ChatFormatting getByName(final String name) {
        if (name == null) {
            return null;
        }
        return ChatFormatting.FORMATTING_BY_NAME.get(name.toLowerCase());
    }
    
    public static Collection<String> getNames(final boolean getColors, final boolean getFormats) {
        final List<String> result = new ArrayList<String>();
        for (final ChatFormatting format : values()) {
            if (!format.isColor() || getColors) {
                if (!format.isFormat() || getFormats) {
                    result.add(format.getName());
                }
            }
        }
        return result;
    }
    
    static {
        FORMATTING_BY_CHAR = new HashMap<Character, ChatFormatting>();
        FORMATTING_BY_NAME = new HashMap<String, ChatFormatting>();
        STRIP_FORMATTING_PATTERN = Pattern.compile("(?i)" + String.valueOf('§') + "[0-9A-FK-OR]");
        for (final ChatFormatting format : values()) {
            ChatFormatting.FORMATTING_BY_CHAR.put(format.getChar(), format);
            ChatFormatting.FORMATTING_BY_NAME.put(format.getName(), format);
        }
    }
}
