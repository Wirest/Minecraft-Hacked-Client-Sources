// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.player;

import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;

public enum EnumPlayerModelParts
{
    CAPE("CAPE", 0, 0, "cape"), 
    JACKET("JACKET", 1, 1, "jacket"), 
    LEFT_SLEEVE("LEFT_SLEEVE", 2, 2, "left_sleeve"), 
    RIGHT_SLEEVE("RIGHT_SLEEVE", 3, 3, "right_sleeve"), 
    LEFT_PANTS_LEG("LEFT_PANTS_LEG", 4, 4, "left_pants_leg"), 
    RIGHT_PANTS_LEG("RIGHT_PANTS_LEG", 5, 5, "right_pants_leg"), 
    HAT("HAT", 6, 6, "hat");
    
    private final int partId;
    private final int partMask;
    private final String partName;
    private final IChatComponent field_179339_k;
    
    private EnumPlayerModelParts(final String name, final int ordinal, final int partIdIn, final String partNameIn) {
        this.partId = partIdIn;
        this.partMask = 1 << partIdIn;
        this.partName = partNameIn;
        this.field_179339_k = new ChatComponentTranslation("options.modelPart." + partNameIn, new Object[0]);
    }
    
    public int getPartMask() {
        return this.partMask;
    }
    
    public int getPartId() {
        return this.partId;
    }
    
    public String getPartName() {
        return this.partName;
    }
    
    public IChatComponent func_179326_d() {
        return this.field_179339_k;
    }
}
