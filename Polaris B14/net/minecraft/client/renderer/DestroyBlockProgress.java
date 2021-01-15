/*    */ package net.minecraft.client.renderer;
/*    */ 
/*    */ import net.minecraft.util.BlockPos;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DestroyBlockProgress
/*    */ {
/*    */   private final int miningPlayerEntId;
/*    */   private final BlockPos position;
/*    */   private int partialBlockProgress;
/*    */   private int createdAtCloudUpdateTick;
/*    */   
/*    */   public DestroyBlockProgress(int miningPlayerEntIdIn, BlockPos positionIn)
/*    */   {
/* 26 */     this.miningPlayerEntId = miningPlayerEntIdIn;
/* 27 */     this.position = positionIn;
/*    */   }
/*    */   
/*    */   public BlockPos getPosition()
/*    */   {
/* 32 */     return this.position;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public void setPartialBlockDamage(int damage)
/*    */   {
/* 41 */     if (damage > 10)
/*    */     {
/* 43 */       damage = 10;
/*    */     }
/*    */     
/* 46 */     this.partialBlockProgress = damage;
/*    */   }
/*    */   
/*    */   public int getPartialBlockDamage()
/*    */   {
/* 51 */     return this.partialBlockProgress;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void setCloudUpdateTick(int createdAtCloudUpdateTickIn)
/*    */   {
/* 59 */     this.createdAtCloudUpdateTick = createdAtCloudUpdateTickIn;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public int getCreationCloudUpdateTick()
/*    */   {
/* 67 */     return this.createdAtCloudUpdateTick;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\renderer\DestroyBlockProgress.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */