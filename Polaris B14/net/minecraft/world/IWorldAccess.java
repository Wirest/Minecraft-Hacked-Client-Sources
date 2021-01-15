package net.minecraft.world;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;

public abstract interface IWorldAccess
{
  public abstract void markBlockForUpdate(BlockPos paramBlockPos);
  
  public abstract void notifyLightSet(BlockPos paramBlockPos);
  
  public abstract void markBlockRangeForRenderUpdate(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6);
  
  public abstract void playSound(String paramString, double paramDouble1, double paramDouble2, double paramDouble3, float paramFloat1, float paramFloat2);
  
  public abstract void playSoundToNearExcept(EntityPlayer paramEntityPlayer, String paramString, double paramDouble1, double paramDouble2, double paramDouble3, float paramFloat1, float paramFloat2);
  
  public abstract void spawnParticle(int paramInt, boolean paramBoolean, double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, double paramDouble5, double paramDouble6, int... paramVarArgs);
  
  public abstract void onEntityAdded(Entity paramEntity);
  
  public abstract void onEntityRemoved(Entity paramEntity);
  
  public abstract void playRecord(String paramString, BlockPos paramBlockPos);
  
  public abstract void broadcastSound(int paramInt1, BlockPos paramBlockPos, int paramInt2);
  
  public abstract void playAuxSFX(EntityPlayer paramEntityPlayer, int paramInt1, BlockPos paramBlockPos, int paramInt2);
  
  public abstract void sendBlockBreakProgress(int paramInt1, BlockPos paramBlockPos, int paramInt2);
}


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\world\IWorldAccess.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */