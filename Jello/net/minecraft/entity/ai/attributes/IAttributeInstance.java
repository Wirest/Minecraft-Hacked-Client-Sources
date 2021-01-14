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

    void setBaseValue(double var1);

    Collection getModifiersByOperation(int var1);

    Collection func_111122_c();

    boolean func_180374_a(AttributeModifier var1);

    /**
     * Returns attribute modifier, if any, by the given UUID
     */
    AttributeModifier getModifier(UUID var1);

    void applyModifier(AttributeModifier var1);

    void removeModifier(AttributeModifier var1);

    void removeAllModifiers();

    double getAttributeValue();
}
