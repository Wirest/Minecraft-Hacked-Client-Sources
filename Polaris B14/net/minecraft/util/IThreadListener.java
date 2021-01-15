package net.minecraft.util;

import com.google.common.util.concurrent.ListenableFuture;

public abstract interface IThreadListener
{
  public abstract ListenableFuture<Object> addScheduledTask(Runnable paramRunnable);
  
  public abstract boolean isCallingFromMinecraftThread();
}


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\util\IThreadListener.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */