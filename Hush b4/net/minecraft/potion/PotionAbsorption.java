// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.potion;

import net.minecraft.entity.ai.attributes.BaseAttributeMap;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;

public class PotionAbsorption extends Potion
{
    protected PotionAbsorption(final int potionID, final ResourceLocation location, final boolean badEffect, final int potionColor) {
        super(potionID, location, badEffect, potionColor);
    }
    
    @Override
    public void removeAttributesModifiersFromEntity(final EntityLivingBase entityLivingBaseIn, final BaseAttributeMap p_111187_2_, final int amplifier) {
        entityLivingBaseIn.setAbsorptionAmount(entityLivingBaseIn.getAbsorptionAmount() - 4 * (amplifier + 1));
        super.removeAttributesModifiersFromEntity(entityLivingBaseIn, p_111187_2_, amplifier);
    }
    
    @Override
    public void applyAttributesModifiersToEntity(final EntityLivingBase entityLivingBaseIn, final BaseAttributeMap p_111185_2_, final int amplifier) {
        entityLivingBaseIn.setAbsorptionAmount(entityLivingBaseIn.getAbsorptionAmount() + 4 * (amplifier + 1));
        super.applyAttributesModifiersToEntity(entityLivingBaseIn, p_111185_2_, amplifier);
    }
}
