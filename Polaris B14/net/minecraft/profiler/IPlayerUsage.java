package net.minecraft.profiler;

public abstract interface IPlayerUsage
{
  public abstract void addServerStatsToSnooper(PlayerUsageSnooper paramPlayerUsageSnooper);
  
  public abstract void addServerTypeToSnooper(PlayerUsageSnooper paramPlayerUsageSnooper);
  
  public abstract boolean isSnooperEnabled();
}


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\profiler\IPlayerUsage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */