package net.minecraft.potion;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.BaseAttributeMap;
import net.minecraft.util.ResourceLocation;

public class PotionAbsoption extends Potion
{
    private static final String __OBFID = "CL_00001524";

    protected PotionAbsoption(int p_i45901_1_, ResourceLocation p_i45901_2_, boolean p_i45901_3_, int p_i45901_4_)
    {
        super(p_i45901_1_, p_i45901_2_, p_i45901_3_, p_i45901_4_);
    }

    public void removeAttributesModifiersFromEntity(EntityLivingBase p_111187_1_, BaseAttributeMap p_111187_2_, int p_111187_3_)
    {
        p_111187_1_.setAbsorptionAmount(p_111187_1_.getAbsorptionAmount() - (float)(4 * (p_111187_3_ + 1)));
        super.removeAttributesModifiersFromEntity(p_111187_1_, p_111187_2_, p_111187_3_);
    }

    public void applyAttributesModifiersToEntity(EntityLivingBase p_111185_1_, BaseAttributeMap p_111185_2_, int p_111185_3_)
    {
        p_111185_1_.setAbsorptionAmount(p_111185_1_.getAbsorptionAmount() + (float)(4 * (p_111185_3_ + 1)));
        super.applyAttributesModifiersToEntity(p_111185_1_, p_111185_2_, p_111185_3_);
    }
}
