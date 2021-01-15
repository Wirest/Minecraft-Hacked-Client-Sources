package net.minecraft.potion;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.BaseAttributeMap;
import net.minecraft.util.ResourceLocation;

public class PotionAbsoption extends Potion
{

    protected PotionAbsoption(int potionID, ResourceLocation location, boolean badEffect, int potionColor)
    {
        super(potionID, location, badEffect, potionColor);
    }

    @Override
	public void removeAttributesModifiersFromEntity(EntityLivingBase entityLivingBaseIn, BaseAttributeMap p_111187_2_, int p_111187_3_)
    {
        entityLivingBaseIn.setAbsorptionAmount(entityLivingBaseIn.getAbsorptionAmount() - 4 * (p_111187_3_ + 1));
        super.removeAttributesModifiersFromEntity(entityLivingBaseIn, p_111187_2_, p_111187_3_);
    }

    @Override
	public void applyAttributesModifiersToEntity(EntityLivingBase entityLivingBaseIn, BaseAttributeMap p_111185_2_, int p_111185_3_)
    {
        entityLivingBaseIn.setAbsorptionAmount(entityLivingBaseIn.getAbsorptionAmount() + 4 * (p_111185_3_ + 1));
        super.applyAttributesModifiersToEntity(entityLivingBaseIn, p_111185_2_, p_111185_3_);
    }
}
