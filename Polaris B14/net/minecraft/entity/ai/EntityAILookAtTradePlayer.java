/*    */ package net.minecraft.entity.ai;
/*    */ 
/*    */ import net.minecraft.entity.passive.EntityVillager;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ 
/*    */ public class EntityAILookAtTradePlayer extends EntityAIWatchClosest
/*    */ {
/*    */   private final EntityVillager theMerchant;
/*    */   
/*    */   public EntityAILookAtTradePlayer(EntityVillager theMerchantIn)
/*    */   {
/* 12 */     super(theMerchantIn, EntityPlayer.class, 8.0F);
/* 13 */     this.theMerchant = theMerchantIn;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public boolean shouldExecute()
/*    */   {
/* 21 */     if (this.theMerchant.isTrading())
/*    */     {
/* 23 */       this.closestEntity = this.theMerchant.getCustomer();
/* 24 */       return true;
/*    */     }
/*    */     
/*    */ 
/* 28 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\entity\ai\EntityAILookAtTradePlayer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */