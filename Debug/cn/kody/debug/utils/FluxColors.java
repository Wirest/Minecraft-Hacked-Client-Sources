package cn.kody.debug.utils;

import com.google.common.collect.Maps;
import java.util.ArrayList;
import com.google.common.collect.Lists;
import java.util.Collection;
import java.util.regex.Pattern;
import java.util.Map;

public enum FluxColors
{
    BLACK("BLACK", '0', 0), 
    DARK_BLUE("DARK_BLUE", '1', 1), 
    DARK_GREEN("DARK_GREEN", '2', 2), 
    DARK_AQUA("DARK_AQUA", '3', 3), 
    DARK_RED("DARK_RED", '4', 4), 
    DARK_PURPLE("DARK_PURPLE", '5', 5), 
    GOLD("GOLD", '6', 6), 
    GRAY("GRAY", '7', 7), 
    DARK_GRAY("DARK_GRAY", '8', 8), 
    BLUE("BLUE", '9', 9), 
    GREEN("GREEN", 'a', 10), 
    AQUA("AQUA", 'b', 11), 
    RED("RED", 'c', 12), 
    LIGHT_PURPLE("LIGHT_PURPLE", 'd', 13), 
    YELLOW("YELLOW", 'e', 14), 
    WHITE("WHITE", 'f', 15), 
    OBFUSCATED("OBFUSCATED", 'k', true), 
    BOLD("BOLD", 'l', true), 
    STRIKETHROUGH("STRIKETHROUGH", 'm', true), 
    UNDERLINE("UNDERLINE", 'n', true), 
    ITALIC("ITALIC", 'o', true), 
    RESET("RESET", 'r', -1);
    
    private static final Map<String, FluxColors> nameMapping;
    private static final Pattern formattingCodePattern;
    private final String name;
    private final char formattingCode;
    private final boolean fancyStyling;
    private final String controlString;
    private final int colorIndex;
    private static final FluxColors[] $VALUES;
    
//    public static FluxColors[] values() {
//        return FluxColors.$VALUES.clone();
//    }
//    
//    public static FluxColors valueOf(final String p_valueOf_0_) {
//        return Enum.valueOf(FluxColors.class, p_valueOf_0_);
//    }
    
    private static String func_175745_c(final String p_175745_0_) {
        return p_175745_0_.toLowerCase().replaceAll("[^a-z]", "");
    }
    
    private FluxColors(final String p_i1217_3_, final char p_i1217_4_, final int p_i1217_5_) {
        this(p_i1217_3_, p_i1217_4_, false, p_i1217_5_);
    }
    
    private FluxColors(final String p_i1218_3_, final char p_i1218_4_, final boolean p_i1218_5_) {
        this(p_i1218_3_, p_i1218_4_, p_i1218_5_, -1);
    }
    
    private FluxColors(final String p_i1219_3_, final char p_i1219_4_, final boolean p_i1219_5_, final int p_i1219_6_) {
        this.name = p_i1219_3_;
        this.formattingCode = p_i1219_4_;
        this.fancyStyling = p_i1219_5_;
        this.colorIndex = p_i1219_6_;
        this.controlString = "§" + p_i1219_4_;
    }
    
    public int getColorIndex() {
        return this.colorIndex;
    }
    
    public boolean isFancyStyling() {
        return this.fancyStyling;
    }
    
    public boolean isColor() {
        boolean b;
        if (!this.fancyStyling && this != FluxColors.RESET) {
            b = true;
        }
        else {
            b = false;
        }
        return b;
    }
    
    public String getFriendlyName() {
        return this.name().toLowerCase();
    }
    
    @Override
    public String toString() {
        return this.controlString;
    }
    
    public static String getTextWithoutFormattingCodes(final String p_getTextWithoutFormattingCodes_0_) {
        String replaceAll;
        if (p_getTextWithoutFormattingCodes_0_ == null) {
            replaceAll = null;
        }
        else {
            replaceAll = FluxColors.formattingCodePattern.matcher(p_getTextWithoutFormattingCodes_0_).replaceAll("");
        }
        return replaceAll;
    }
    
    public static FluxColors getValueByName(final String p_getValueByName_0_) {
        FluxColors class2442;
        if (p_getValueByName_0_ == null) {
            class2442 = null;
        }
        else {
            class2442 = FluxColors.nameMapping.get(func_175745_c(p_getValueByName_0_));
        }
        return class2442;
    }
    
    public static FluxColors func_175744_a(final int p_175744_0_) {
        if (p_175744_0_ < 0) {
            return FluxColors.RESET;
        }
        final FluxColors[] values = values();
        final int length = values.length;
        int i = 0;
        while (i < length) {
            final FluxColors class2442 = values[i];
            if (class2442.getColorIndex() == p_175744_0_) {
                return class2442;
            }
            ++i;
        }
        return null;
    }
    
    public static Collection<String> getValidValues(final boolean p_getValidValues_0_, final boolean p_getValidValues_1_) {
        final ArrayList arrayList = Lists.newArrayList();
        final FluxColors[] values = values();
        final int length = values.length;
        int i = 0;
        while (i < length) {
            final FluxColors class2442 = values[i];
            if ((!class2442.isColor() || p_getValidValues_0_) && (!class2442.isFancyStyling() || p_getValidValues_1_)) {
                arrayList.add(class2442.getFriendlyName());
            }
            ++i;
        }
        return (Collection<String>)arrayList;
    }
    
    static {
        $VALUES = new FluxColors[] { FluxColors.BLACK, FluxColors.DARK_BLUE, FluxColors.DARK_GREEN, FluxColors.DARK_AQUA, FluxColors.DARK_RED, FluxColors.DARK_PURPLE, FluxColors.GOLD, FluxColors.GRAY, FluxColors.DARK_GRAY, FluxColors.BLUE, FluxColors.GREEN, FluxColors.AQUA, FluxColors.RED, FluxColors.LIGHT_PURPLE, FluxColors.YELLOW, FluxColors.WHITE, FluxColors.OBFUSCATED, FluxColors.BOLD, FluxColors.STRIKETHROUGH, FluxColors.UNDERLINE, FluxColors.ITALIC, FluxColors.RESET };
        nameMapping = Maps.newHashMap();
        formattingCodePattern = Pattern.compile("(?i)" + String.valueOf('§') + "[0-9A-FK-OR]");
        final FluxColors[] values = values();
        final int length = values.length;
        int i = 0;
        while (i < length) {
            final FluxColors class2442 = values[i];
            FluxColors.nameMapping.put(func_175745_c(class2442.name), class2442);
            ++i;
        }
    }
}
