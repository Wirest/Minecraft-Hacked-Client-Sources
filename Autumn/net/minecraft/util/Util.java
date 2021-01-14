package net.minecraft.util;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import org.apache.logging.log4j.Logger;

public class Util {
   public static Util.EnumOS getOSType() {
      String s = System.getProperty("os.name").toLowerCase();
      return s.contains("win") ? Util.EnumOS.WINDOWS : (s.contains("mac") ? Util.EnumOS.OSX : (s.contains("solaris") ? Util.EnumOS.SOLARIS : (s.contains("sunos") ? Util.EnumOS.SOLARIS : (s.contains("linux") ? Util.EnumOS.LINUX : (s.contains("unix") ? Util.EnumOS.LINUX : Util.EnumOS.UNKNOWN)))));
   }

   public static Object func_181617_a(FutureTask p_181617_0_, Logger p_181617_1_) {
      try {
         p_181617_0_.run();
         return p_181617_0_.get();
      } catch (ExecutionException var3) {
         p_181617_1_.fatal("Error executing task", var3);
      } catch (InterruptedException var4) {
         p_181617_1_.fatal("Error executing task", var4);
      }

      return null;
   }

   public static enum EnumOS {
      LINUX,
      SOLARIS,
      WINDOWS,
      OSX,
      UNKNOWN;
   }
}
