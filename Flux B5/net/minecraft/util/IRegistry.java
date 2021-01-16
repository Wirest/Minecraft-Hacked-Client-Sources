package net.minecraft.util;

public interface IRegistry extends Iterable
{
    Object getObject(Object var1);

    /**
     * Register an object on this registry.
     */
    void putObject(Object var1, Object var2);
}
