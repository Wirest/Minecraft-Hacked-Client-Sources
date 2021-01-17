// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.potion;

import net.minecraft.util.ResourceLocation;

public class PotionHealth extends Potion
{
    public PotionHealth(final int potionID, final ResourceLocation location, final boolean badEffect, final int potionColor) {
        super(potionID, location, badEffect, potionColor);
    }
    
    @Override
    public boolean isInstant() {
        return true;
    }
    
    @Override
    public boolean isReady(final int p_76397_1_, final int p_76397_2_) {
        return p_76397_1_ >= 1;
    }
}
