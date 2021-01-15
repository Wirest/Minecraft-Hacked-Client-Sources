package net.minecraft.entity.ai.attributes;

import java.util.Collection;
import java.util.UUID;

public interface IAttributeInstance
{
    /**
     * Get the Attribute this is an instance of
     */
    IAttribute getAttribute();

    double getBaseValue();

    void setBaseValue(double p_111128_1_);

    Collection getModifiersByOperation(int p_111130_1_);

    Collection func_111122_c();

    boolean func_180374_a(AttributeModifier p_180374_1_);

    /**
     * Returns attribute modifier, if any, by the given UUID
     */
    AttributeModifier getModifier(UUID p_111127_1_);

    void applyModifier(AttributeModifier p_111121_1_);

    void removeModifier(AttributeModifier p_111124_1_);

    void removeAllModifiers();

    double getAttributeValue();
}
