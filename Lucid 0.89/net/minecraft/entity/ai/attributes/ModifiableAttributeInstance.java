package net.minecraft.entity.ai.attributes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public class ModifiableAttributeInstance implements IAttributeInstance
{
    /** The BaseAttributeMap this attributeInstance can be found in */
    private final BaseAttributeMap attributeMap;

    /** The Attribute this is an instance of */
    private final IAttribute genericAttribute;
    private final Map mapByOperation = Maps.newHashMap();
    private final Map mapByName = Maps.newHashMap();
    private final Map mapByUUID = Maps.newHashMap();
    private double baseValue;
    private boolean needsUpdate = true;
    private double cachedValue;

    public ModifiableAttributeInstance(BaseAttributeMap attributeMapIn, IAttribute genericAttributeIn)
    {
        this.attributeMap = attributeMapIn;
        this.genericAttribute = genericAttributeIn;
        this.baseValue = genericAttributeIn.getDefaultValue();

        for (int var3 = 0; var3 < 3; ++var3)
        {
            this.mapByOperation.put(Integer.valueOf(var3), Sets.newHashSet());
        }
    }

    /**
     * Get the Attribute this is an instance of
     */
    @Override
	public IAttribute getAttribute()
    {
        return this.genericAttribute;
    }

    @Override
	public double getBaseValue()
    {
        return this.baseValue;
    }

    @Override
	public void setBaseValue(double baseValue)
    {
        if (baseValue != this.getBaseValue())
        {
            this.baseValue = baseValue;
            this.flagForUpdate();
        }
    }

    @Override
	public Collection getModifiersByOperation(int operation)
    {
        return (Collection)this.mapByOperation.get(Integer.valueOf(operation));
    }

    @Override
	public Collection func_111122_c()
    {
        HashSet var1 = Sets.newHashSet();

        for (int var2 = 0; var2 < 3; ++var2)
        {
            var1.addAll(this.getModifiersByOperation(var2));
        }

        return var1;
    }

    /**
     * Returns attribute modifier, if any, by the given UUID
     */
    @Override
	public AttributeModifier getModifier(UUID uuid)
    {
        return (AttributeModifier)this.mapByUUID.get(uuid);
    }

    @Override
	public boolean func_180374_a(AttributeModifier modifier)
    {
        return this.mapByUUID.get(modifier.getID()) != null;
    }

    @Override
	public void applyModifier(AttributeModifier modifier)
    {
        if (this.getModifier(modifier.getID()) != null)
        {
            throw new IllegalArgumentException("Modifier is already applied on this attribute!");
        }
        else
        {
            Object var2 = this.mapByName.get(modifier.getName());

            if (var2 == null)
            {
                var2 = Sets.newHashSet();
                this.mapByName.put(modifier.getName(), var2);
            }

            ((Set)this.mapByOperation.get(Integer.valueOf(modifier.getOperation()))).add(modifier);
            ((Set)var2).add(modifier);
            this.mapByUUID.put(modifier.getID(), modifier);
            this.flagForUpdate();
        }
    }

    protected void flagForUpdate()
    {
        this.needsUpdate = true;
        this.attributeMap.func_180794_a(this);
    }

    @Override
	public void removeModifier(AttributeModifier modifier)
    {
        for (int var2 = 0; var2 < 3; ++var2)
        {
            Set var3 = (Set)this.mapByOperation.get(Integer.valueOf(var2));
            var3.remove(modifier);
        }

        Set var4 = (Set)this.mapByName.get(modifier.getName());

        if (var4 != null)
        {
            var4.remove(modifier);

            if (var4.isEmpty())
            {
                this.mapByName.remove(modifier.getName());
            }
        }

        this.mapByUUID.remove(modifier.getID());
        this.flagForUpdate();
    }

    @Override
	public void removeAllModifiers()
    {
        Collection var1 = this.func_111122_c();

        if (var1 != null)
        {
            ArrayList var4 = Lists.newArrayList(var1);
            Iterator var2 = var4.iterator();

            while (var2.hasNext())
            {
                AttributeModifier var3 = (AttributeModifier)var2.next();
                this.removeModifier(var3);
            }
        }
    }

    @Override
	public double getAttributeValue()
    {
        if (this.needsUpdate)
        {
            this.cachedValue = this.computeValue();
            this.needsUpdate = false;
        }

        return this.cachedValue;
    }

    private double computeValue()
    {
        double var1 = this.getBaseValue();
        AttributeModifier var4;

        for (Iterator var3 = this.func_180375_b(0).iterator(); var3.hasNext(); var1 += var4.getAmount())
        {
            var4 = (AttributeModifier)var3.next();
        }

        double var7 = var1;
        Iterator var5;
        AttributeModifier var6;

        for (var5 = this.func_180375_b(1).iterator(); var5.hasNext(); var7 += var1 * var6.getAmount())
        {
            var6 = (AttributeModifier)var5.next();
        }

        for (var5 = this.func_180375_b(2).iterator(); var5.hasNext(); var7 *= 1.0D + var6.getAmount())
        {
            var6 = (AttributeModifier)var5.next();
        }

        return this.genericAttribute.clampValue(var7);
    }

    private Collection func_180375_b(int p_180375_1_)
    {
        HashSet var2 = Sets.newHashSet(this.getModifiersByOperation(p_180375_1_));

        for (IAttribute var3 = this.genericAttribute.func_180372_d(); var3 != null; var3 = var3.func_180372_d())
        {
            IAttributeInstance var4 = this.attributeMap.getAttributeInstance(var3);

            if (var4 != null)
            {
                var2.addAll(var4.getModifiersByOperation(p_180375_1_));
            }
        }

        return var2;
    }
}
