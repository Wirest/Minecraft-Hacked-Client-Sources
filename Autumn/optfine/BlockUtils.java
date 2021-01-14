package optfine;

import java.util.IdentityHashMap;
import java.util.Map;
import net.minecraft.block.Block;

public class BlockUtils {
   private static ReflectorClass ForgeBlock = new ReflectorClass(Block.class);
   private static ReflectorMethod ForgeBlock_setLightOpacity;
   private static boolean directAccessValid;
   private static Map mapOriginalOpacity;

   public static void setLightOpacity(Block p_setLightOpacity_0_, int p_setLightOpacity_1_) {
      if (!mapOriginalOpacity.containsKey(p_setLightOpacity_0_)) {
         mapOriginalOpacity.put(p_setLightOpacity_0_, p_setLightOpacity_0_.getLightOpacity());
      }

      if (directAccessValid) {
         try {
            p_setLightOpacity_0_.setLightOpacity(p_setLightOpacity_1_);
            return;
         } catch (IllegalAccessError var3) {
            directAccessValid = false;
            if (!ForgeBlock_setLightOpacity.exists()) {
               throw var3;
            }
         }
      }

      Reflector.callVoid(p_setLightOpacity_0_, ForgeBlock_setLightOpacity, p_setLightOpacity_1_);
   }

   public static void restoreLightOpacity(Block p_restoreLightOpacity_0_) {
      if (mapOriginalOpacity.containsKey(p_restoreLightOpacity_0_)) {
         int i = (Integer)mapOriginalOpacity.get(p_restoreLightOpacity_0_);
         setLightOpacity(p_restoreLightOpacity_0_, i);
      }

   }

   static {
      ForgeBlock_setLightOpacity = new ReflectorMethod(ForgeBlock, "setLightOpacity");
      directAccessValid = true;
      mapOriginalOpacity = new IdentityHashMap();
   }
}
