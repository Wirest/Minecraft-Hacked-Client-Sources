// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.potion;

import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.util.ResourceLocation;

public class PotionAttackDamage extends Potion
{
    protected PotionAttackDamage(final int potionID, final ResourceLocation location, final boolean badEffect, final int potionColor) {
        super(potionID, location, badEffect, potionColor);
    }
    
    @Override
    public double getAttributeModifierAmount(final int p_111183_1_, final AttributeModifier modifier) {
        return (this.id == Potion.weakness.id) ? (-0.5f * (p_111183_1_ + 1)) : (1.3 * (p_111183_1_ + 1));
    }
}
