package net.minecraft.util;

public class RegistryDefaulted extends RegistrySimple
{
    /**
     * Default object for this registry, returned when an object is not found.
     */
    private final Object defaultObject;

    public RegistryDefaulted(Object defaultObjectIn)
    {
        this.defaultObject = defaultObjectIn;
    }

    @Override
	public Object getObject(Object name)
    {
        Object var2 = super.getObject(name);
        return var2 == null ? this.defaultObject : var2;
    }
}
