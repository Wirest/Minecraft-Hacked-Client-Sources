package net.minecraft.item;

import net.minecraft.util.EnumChatFormatting;

public enum EnumRarity
{
    COMMON("COMMON", 0, EnumChatFormatting.WHITE, "Common"),
    UNCOMMON("UNCOMMON", 1, EnumChatFormatting.YELLOW, "Uncommon"),
    RARE("RARE", 2, EnumChatFormatting.AQUA, "Rare"),
    EPIC("EPIC", 3, EnumChatFormatting.LIGHT_PURPLE, "Epic");

    /**
     * A decimal representation of the hex color codes of a the color assigned to this rarity type. (13 becomes d as in
     * \247d which is light purple)
     */
    public final EnumChatFormatting rarityColor;

    /** Rarity name. */
    public final String rarityName;

    private static final EnumRarity[] $VALUES = new EnumRarity[]{COMMON, UNCOMMON, RARE, EPIC};
    private static final String __OBFID = "CL_00000056";

    private EnumRarity(String p_i45349_1_, int p_i45349_2_, EnumChatFormatting p_i45349_3_, String p_i45349_4_)
    {
        this.rarityColor = p_i45349_3_;
        this.rarityName = p_i45349_4_;
    }
}
