package net.minecraft.world;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;

public interface IWorldAccess {
   void markBlockForUpdate(BlockPos var1);

   void notifyLightSet(BlockPos var1);

   void markBlockRangeForRenderUpdate(int var1, int var2, int var3, int var4, int var5, int var6);

   void playSound(String var1, double var2, double var4, double var6, float var8, float var9);

   void playSoundToNearExcept(EntityPlayer var1, String var2, double var3, double var5, double var7, float var9, float var10);

   void func_180442_a(int var1, boolean var2, double var3, double var5, double var7, double var9, double var11, double var13, int... var15);

   void onEntityAdded(Entity var1);

   void onEntityRemoved(Entity var1);

   void func_174961_a(String var1, BlockPos var2);

   void func_180440_a(int var1, BlockPos var2, int var3);

   void func_180439_a(EntityPlayer var1, int var2, BlockPos var3, int var4);

   void sendBlockBreakProgress(int var1, BlockPos var2, int var3);
}
