package net.minecraft.util;

public class StatCollector {
   private static StringTranslate localizedName = StringTranslate.getInstance();
   private static StringTranslate fallbackTranslator = new StringTranslate();
   private static final String __OBFID = "CL_00001211";

   public static String translateToLocal(String p_74838_0_) {
      return localizedName.translateKey(p_74838_0_);
   }

   public static String translateToLocalFormatted(String p_74837_0_, Object... p_74837_1_) {
      return localizedName.translateKeyFormat(p_74837_0_, p_74837_1_);
   }

   public static String translateToFallback(String p_150826_0_) {
      return fallbackTranslator.translateKey(p_150826_0_);
   }

   public static boolean canTranslate(String p_94522_0_) {
      return localizedName.isKeyTranslated(p_94522_0_);
   }

   public static long getLastTranslationUpdateTimeInMilliseconds() {
      return localizedName.getLastUpdateTimeInMilliseconds();
   }
}
