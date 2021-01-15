/*    */ package optfine;
/*    */ 
/*    */ import net.minecraft.client.entity.AbstractClientPlayer;
/*    */ import net.minecraft.client.model.ModelBiped;
/*    */ 
/*    */ public class PlayerConfiguration
/*    */ {
/*  8 */   private PlayerItemModel[] playerItemModels = new PlayerItemModel[0];
/*  9 */   private boolean initialized = false;
/*    */   
/*    */   public void renderPlayerItems(ModelBiped p_renderPlayerItems_1_, AbstractClientPlayer p_renderPlayerItems_2_, float p_renderPlayerItems_3_, float p_renderPlayerItems_4_)
/*    */   {
/* 13 */     if (this.initialized)
/*    */     {
/* 15 */       for (int i = 0; i < this.playerItemModels.length; i++)
/*    */       {
/* 17 */         PlayerItemModel playeritemmodel = this.playerItemModels[i];
/* 18 */         playeritemmodel.render(p_renderPlayerItems_1_, p_renderPlayerItems_2_, p_renderPlayerItems_3_, p_renderPlayerItems_4_);
/*    */       }
/*    */     }
/*    */   }
/*    */   
/*    */   public boolean isInitialized()
/*    */   {
/* 25 */     return this.initialized;
/*    */   }
/*    */   
/*    */   public void setInitialized(boolean p_setInitialized_1_)
/*    */   {
/* 30 */     this.initialized = p_setInitialized_1_;
/*    */   }
/*    */   
/*    */   public PlayerItemModel[] getPlayerItemModels()
/*    */   {
/* 35 */     return this.playerItemModels;
/*    */   }
/*    */   
/*    */   public void addPlayerItemModel(PlayerItemModel p_addPlayerItemModel_1_)
/*    */   {
/* 40 */     this.playerItemModels = ((PlayerItemModel[])Config.addObjectToArray(this.playerItemModels, p_addPlayerItemModel_1_));
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\optfine\PlayerConfiguration.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */