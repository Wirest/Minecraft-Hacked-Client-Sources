package net.minecraft.util;

import org.apache.commons.lang3.Validate;

public class RegistryNamespacedDefaultedByKey extends RegistryNamespaced
{
    private final Object field_148760_d;
    private Object field_148761_e;
    private static final String __OBFID = "CL_00001196";

    public RegistryNamespacedDefaultedByKey(Object p_i46017_1_)
    {
        this.field_148760_d = p_i46017_1_;
    }

    public void register(int p_177775_1_, Object p_177775_2_, Object p_177775_3_)
    {
        if (this.field_148760_d.equals(p_177775_2_))
        {
            this.field_148761_e = p_177775_3_;
        }

        super.register(p_177775_1_, p_177775_2_, p_177775_3_);
    }

    /**
     * validates that this registry's key is non-null
     */
    public void validateKey()
    {
        Validate.notNull(this.field_148760_d);
    }

    public Object getObject(Object p_82594_1_)
    {
        Object var2 = super.getObject(p_82594_1_);
        return var2 == null ? this.field_148761_e : var2;
    }

    /**
     * Gets the object identified by the given ID.
     */
    public Object getObjectById(int p_148754_1_)
    {
        Object var2 = super.getObjectById(p_148754_1_);
        return var2 == null ? this.field_148761_e : var2;
    }
}
