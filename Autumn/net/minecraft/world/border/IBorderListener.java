package net.minecraft.world.border;

public interface IBorderListener {
   void onSizeChanged(WorldBorder var1, double var2);

   void onTransitionStarted(WorldBorder var1, double var2, double var4, long var6);

   void onCenterChanged(WorldBorder var1, double var2, double var4);

   void onWarningTimeChanged(WorldBorder var1, int var2);

   void onWarningDistanceChanged(WorldBorder var1, int var2);

   void onDamageAmountChanged(WorldBorder var1, double var2);

   void onDamageBufferChanged(WorldBorder var1, double var2);
}
