package net.minecraft.world;

public class ColorizerFoliage {
   private static int[] foliageBuffer = new int[65536];
   private static final String __OBFID = "CL_00000135";

   public static void setFoliageBiomeColorizer(int[] p_77467_0_) {
      foliageBuffer = p_77467_0_;
   }

   public static int getFoliageColor(double p_77470_0_, double p_77470_2_) {
      p_77470_2_ *= p_77470_0_;
      int var4 = (int)((1.0D - p_77470_0_) * 255.0D);
      int var5 = (int)((1.0D - p_77470_2_) * 255.0D);
      return foliageBuffer[var5 << 8 | var4];
   }

   public static int getFoliageColorPine() {
      return 6396257;
   }

   public static int getFoliageColorBirch() {
      return 8431445;
   }

   public static int getFoliageColorBasic() {
      return 4764952;
   }
}
