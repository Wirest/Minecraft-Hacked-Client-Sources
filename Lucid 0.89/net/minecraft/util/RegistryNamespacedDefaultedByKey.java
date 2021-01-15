package net.minecraft.util;

import org.apache.commons.lang3.Validate;

public class RegistryNamespacedDefaultedByKey extends RegistryNamespaced
{
    /** The key of the default value. */
    private final Object defaultValueKey;

    /**
     * The default value for this registry, retrurned in the place of a null value.
     */
    private Object defaultValue;

    public RegistryNamespacedDefaultedByKey(Object p_i46017_1_)
    {
        this.defaultValueKey = p_i46017_1_;
    }

    @Override
	public void register(int id, Object p_177775_2_, Object p_177775_3_)
    {
        if (this.defaultValueKey.equals(p_177775_2_))
        {
            this.defaultValue = p_177775_3_;
        }

        super.register(id, p_177775_2_, p_177775_3_);
    }

    /**
     * validates that this registry's key is non-null
     */
    public void validateKey()
    {
        Validate.notNull(this.defaultValueKey);
    }

    @Override
	public Object getObject(Object name)
    {
        Object var2 = super.getObject(name);
        return var2 == null ? this.defaultValue : var2;
    }

    /**
     * Gets the object identified by the given ID.
     *  
     * @param id The id to fetch from the registry
     */
    @Override
	public Object getObjectById(int id)
    {
        Object var2 = super.getObjectById(id);
        return var2 == null ? this.defaultValue : var2;
    }
}
