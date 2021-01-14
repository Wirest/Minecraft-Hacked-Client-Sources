package net.minecraft.world.gen.layer;

import com.google.common.collect.Lists;
import java.util.List;

public class IntCache {
   private static int intCacheSize = 256;
   private static List freeSmallArrays = Lists.newArrayList();
   private static List inUseSmallArrays = Lists.newArrayList();
   private static List freeLargeArrays = Lists.newArrayList();
   private static List inUseLargeArrays = Lists.newArrayList();

   public static synchronized int[] getIntCache(int p_76445_0_) {
      int[] aint;
      if (p_76445_0_ <= 256) {
         if (freeSmallArrays.isEmpty()) {
            aint = new int[256];
            inUseSmallArrays.add(aint);
            return aint;
         } else {
            aint = (int[])((int[])freeSmallArrays.remove(freeSmallArrays.size() - 1));
            inUseSmallArrays.add(aint);
            return aint;
         }
      } else if (p_76445_0_ > intCacheSize) {
         intCacheSize = p_76445_0_;
         freeLargeArrays.clear();
         inUseLargeArrays.clear();
         aint = new int[intCacheSize];
         inUseLargeArrays.add(aint);
         return aint;
      } else if (freeLargeArrays.isEmpty()) {
         aint = new int[intCacheSize];
         inUseLargeArrays.add(aint);
         return aint;
      } else {
         aint = (int[])((int[])freeLargeArrays.remove(freeLargeArrays.size() - 1));
         inUseLargeArrays.add(aint);
         return aint;
      }
   }

   public static synchronized void resetIntCache() {
      if (!freeLargeArrays.isEmpty()) {
         freeLargeArrays.remove(freeLargeArrays.size() - 1);
      }

      if (!freeSmallArrays.isEmpty()) {
         freeSmallArrays.remove(freeSmallArrays.size() - 1);
      }

      freeLargeArrays.addAll(inUseLargeArrays);
      freeSmallArrays.addAll(inUseSmallArrays);
      inUseLargeArrays.clear();
      inUseSmallArrays.clear();
   }

   public static synchronized String getCacheSizes() {
      return "cache: " + freeLargeArrays.size() + ", tcache: " + freeSmallArrays.size() + ", allocated: " + inUseLargeArrays.size() + ", tallocated: " + inUseSmallArrays.size();
   }
}
