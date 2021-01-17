// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.potion;

import net.minecraft.entity.ai.attributes.BaseAttributeMap;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;

public class PotionHealthBoost extends Potion
{
    public PotionHealthBoost(final int potionID, final ResourceLocation location, final boolean badEffect, final int potionColor) {
        super(potionID, location, badEffect, potionColor);
    }
    
    @Override
    public void removeAttributesModifiersFromEntity(final EntityLivingBase entityLivingBaseIn, final BaseAttributeMap p_111187_2_, final int amplifier) {
        super.removeAttributesModifiersFromEntity(entityLivingBaseIn, p_111187_2_, amplifier);
        if (entityLivingBaseIn.getHealth() > entityLivingBaseIn.getMaxHealth()) {
            entityLivingBaseIn.setHealth(entityLivingBaseIn.getMaxHealth());
        }
    }
}
