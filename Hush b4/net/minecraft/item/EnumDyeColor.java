// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.item;

import net.minecraft.util.EnumChatFormatting;
import net.minecraft.block.material.MapColor;
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
    
    private static final EnumDyeColor[] META_LOOKUP;
    private static final EnumDyeColor[] DYE_DMG_LOOKUP;
    private final int meta;
    private final int dyeDamage;
    private final String name;
    private final String unlocalizedName;
    private final MapColor mapColor;
    private final EnumChatFormatting chatColor;
    
    static {
        META_LOOKUP = new EnumDyeColor[values().length];
        DYE_DMG_LOOKUP = new EnumDyeColor[values().length];
        EnumDyeColor[] values;
        for (int length = (values = values()).length, i = 0; i < length; ++i) {
            final EnumDyeColor enumdyecolor = values[i];
            EnumDyeColor.META_LOOKUP[enumdyecolor.getMetadata()] = enumdyecolor;
            EnumDyeColor.DYE_DMG_LOOKUP[enumdyecolor.getDyeDamage()] = enumdyecolor;
        }
    }
    
    private EnumDyeColor(final String name2, final int ordinal, final int meta, final int dyeDamage, final String name, final String unlocalizedName, final MapColor mapColorIn, final EnumChatFormatting chatColor) {
        this.meta = meta;
        this.dyeDamage = dyeDamage;
        this.name = name;
        this.unlocalizedName = unlocalizedName;
        this.mapColor = mapColorIn;
        this.chatColor = chatColor;
    }
    
    public int getMetadata() {
        return this.meta;
    }
    
    public int getDyeDamage() {
        return this.dyeDamage;
    }
    
    public String getUnlocalizedName() {
        return this.unlocalizedName;
    }
    
    public MapColor getMapColor() {
        return this.mapColor;
    }
    
    public static EnumDyeColor byDyeDamage(int damage) {
        if (damage < 0 || damage >= EnumDyeColor.DYE_DMG_LOOKUP.length) {
            damage = 0;
        }
        return EnumDyeColor.DYE_DMG_LOOKUP[damage];
    }
    
    public static EnumDyeColor byMetadata(int meta) {
        if (meta < 0 || meta >= EnumDyeColor.META_LOOKUP.length) {
            meta = 0;
        }
        return EnumDyeColor.META_LOOKUP[meta];
    }
    
    @Override
    public String toString() {
        return this.unlocalizedName;
    }
    
    @Override
    public String getName() {
        return this.name;
    }
}
