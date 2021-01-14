package net.minecraft.potion;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.BaseAttributeMap;
import net.minecraft.util.ResourceLocation;

public class PotionHealthBoost extends Potion {
    private static final String __OBFID = "CL_00001526";

    public PotionHealthBoost(int p_i45899_1_, ResourceLocation p_i45899_2_, boolean p_i45899_3_, int p_i45899_4_) {
        super(p_i45899_1_, p_i45899_2_, p_i45899_3_, p_i45899_4_);
    }

    public void removeAttributesModifiersFromEntity(EntityLivingBase p_111187_1_, BaseAttributeMap p_111187_2_, int p_111187_3_) {
        super.removeAttributesModifiersFromEntity(p_111187_1_, p_111187_2_, p_111187_3_);

        if (p_111187_1_.getHealth() > p_111187_1_.getMaxHealth()) {
            p_111187_1_.setHealth(p_111187_1_.getMaxHealth());
        }
    }
}
