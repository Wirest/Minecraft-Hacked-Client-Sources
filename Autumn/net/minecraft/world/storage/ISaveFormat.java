package net.minecraft.world.storage;

import java.util.List;
import net.minecraft.client.AnvilConverterException;
import net.minecraft.util.IProgressUpdate;

public interface ISaveFormat {
   String getName();

   ISaveHandler getSaveLoader(String var1, boolean var2);

   List getSaveList() throws AnvilConverterException;

   void flushCache();

   WorldInfo getWorldInfo(String var1);

   boolean func_154335_d(String var1);

   boolean deleteWorldDirectory(String var1);

   void renameWorld(String var1, String var2);

   boolean func_154334_a(String var1);

   boolean isOldMapFormat(String var1);

   boolean convertMapFormat(String var1, IProgressUpdate var2);

   boolean canLoadWorld(String var1);
}
