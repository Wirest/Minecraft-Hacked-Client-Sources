package net.minecraft.util;

public interface IRegistry extends Iterable {
   Object getObject(Object var1);

   void putObject(Object var1, Object var2);
}
