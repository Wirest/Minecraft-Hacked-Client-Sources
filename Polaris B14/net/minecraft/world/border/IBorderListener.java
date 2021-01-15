package net.minecraft.world.border;

public abstract interface IBorderListener
{
  public abstract void onSizeChanged(WorldBorder paramWorldBorder, double paramDouble);
  
  public abstract void onTransitionStarted(WorldBorder paramWorldBorder, double paramDouble1, double paramDouble2, long paramLong);
  
  public abstract void onCenterChanged(WorldBorder paramWorldBorder, double paramDouble1, double paramDouble2);
  
  public abstract void onWarningTimeChanged(WorldBorder paramWorldBorder, int paramInt);
  
  public abstract void onWarningDistanceChanged(WorldBorder paramWorldBorder, int paramInt);
  
  public abstract void onDamageAmountChanged(WorldBorder paramWorldBorder, double paramDouble);
  
  public abstract void onDamageBufferChanged(WorldBorder paramWorldBorder, double paramDouble);
}


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\world\border\IBorderListener.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */