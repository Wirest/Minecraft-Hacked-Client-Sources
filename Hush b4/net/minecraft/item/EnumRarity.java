// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.item;

import net.minecraft.util.EnumChatFormatting;

public enum EnumRarity
{
    COMMON("COMMON", 0, EnumChatFormatting.WHITE, "Common"), 
    UNCOMMON("UNCOMMON", 1, EnumChatFormatting.YELLOW, "Uncommon"), 
    RARE("RARE", 2, EnumChatFormatting.AQUA, "Rare"), 
    EPIC("EPIC", 3, EnumChatFormatting.LIGHT_PURPLE, "Epic");
    
    public final EnumChatFormatting rarityColor;
    public final String rarityName;
    
    private EnumRarity(final String name2, final int ordinal, final EnumChatFormatting color, final String name) {
        this.rarityColor = color;
        this.rarityName = name;
    }
}
