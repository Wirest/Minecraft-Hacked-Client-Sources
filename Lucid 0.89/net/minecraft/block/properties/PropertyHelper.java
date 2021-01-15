package net.minecraft.block.properties;

import com.google.common.base.Objects;

public abstract class PropertyHelper implements IProperty
{
    private final Class valueClass;
    private final String name;

    protected PropertyHelper(String name, Class valueClass)
    {
        this.valueClass = valueClass;
        this.name = name;
    }

    @Override
	public String getName()
    {
        return this.name;
    }

    /**
     * The class of the values of this property
     */
    @Override
	public Class getValueClass()
    {
        return this.valueClass;
    }

    @Override
	public String toString()
    {
        return Objects.toStringHelper(this).add("name", this.name).add("clazz", this.valueClass).add("values", this.getAllowedValues()).toString();
    }

    @Override
	public boolean equals(Object p_equals_1_)
    {
        if (this == p_equals_1_)
        {
            return true;
        }
        else if (p_equals_1_ != null && this.getClass() == p_equals_1_.getClass())
        {
            PropertyHelper var2 = (PropertyHelper)p_equals_1_;
            return this.valueClass.equals(var2.valueClass) && this.name.equals(var2.name);
        }
        else
        {
            return false;
        }
    }

    @Override
	public int hashCode()
    {
        return 31 * this.valueClass.hashCode() + this.name.hashCode();
    }
}
