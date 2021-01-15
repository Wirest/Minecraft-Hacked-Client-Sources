/*    */ package optfine;
/*    */ 
/*    */ import java.lang.reflect.Field;
/*    */ import java.util.Map;
/*    */ import net.minecraft.client.entity.AbstractClientPlayer;
/*    */ import net.minecraft.client.model.ModelBiped;
/*    */ import net.minecraft.client.renderer.GlStateManager;
/*    */ import net.minecraft.client.renderer.entity.RenderManager;
/*    */ import net.minecraft.client.renderer.entity.RenderPlayer;
/*    */ import net.minecraft.entity.EntityLivingBase;
/*    */ 
/*    */ public class RenderPlayerOF extends RenderPlayer
/*    */ {
/*    */   public RenderPlayerOF(RenderManager p_i62_1_, boolean p_i62_2_)
/*    */   {
/* 16 */     super(p_i62_1_, p_i62_2_);
/*    */   }
/*    */   
/*    */   protected void renderLayers(AbstractClientPlayer entitylivingbaseIn, float p_177093_2_, float p_177093_3_, float partialTicks, float p_177093_5_, float p_177093_6_, float p_177093_7_, float p_177093_8_)
/*    */   {
/* 21 */     super.renderLayers(entitylivingbaseIn, p_177093_2_, p_177093_3_, partialTicks, p_177093_5_, p_177093_6_, p_177093_7_, p_177093_8_);
/* 22 */     renderEquippedItems(entitylivingbaseIn, p_177093_8_, partialTicks);
/*    */   }
/*    */   
/*    */   protected void renderEquippedItems(EntityLivingBase p_renderEquippedItems_1_, float p_renderEquippedItems_2_, float p_renderEquippedItems_3_)
/*    */   {
/* 27 */     if ((p_renderEquippedItems_1_ instanceof AbstractClientPlayer))
/*    */     {
/* 29 */       AbstractClientPlayer abstractclientplayer = (AbstractClientPlayer)p_renderEquippedItems_1_;
/* 30 */       GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/* 31 */       GlStateManager.disableRescaleNormal();
/* 32 */       ModelBiped modelbiped = (ModelBiped)this.mainModel;
/* 33 */       PlayerConfigurations.renderPlayerItems(modelbiped, abstractclientplayer, p_renderEquippedItems_2_, p_renderEquippedItems_3_);
/*    */     }
/*    */   }
/*    */   
/*    */   public static void register()
/*    */   {
/* 39 */     RenderManager rendermanager = Config.getMinecraft().getRenderManager();
/* 40 */     Map map = getMapRenderTypes(rendermanager);
/*    */     
/* 42 */     if (map == null)
/*    */     {
/* 44 */       Config.warn("RenderPlayerOF init() failed: RenderManager.MapRenderTypes not found");
/*    */     }
/*    */     else
/*    */     {
/* 48 */       map.put("default", new RenderPlayerOF(rendermanager, false));
/* 49 */       map.put("slim", new RenderPlayerOF(rendermanager, true));
/*    */     }
/*    */   }
/*    */   
/*    */   private static Map getMapRenderTypes(RenderManager p_getMapRenderTypes_0_)
/*    */   {
/*    */     try
/*    */     {
/* 57 */       Field[] afield = Reflector.getFields(RenderManager.class, Map.class);
/*    */       
/* 59 */       for (int i = 0; i < afield.length; i++)
/*    */       {
/* 61 */         Field field = afield[i];
/* 62 */         Map map = (Map)field.get(p_getMapRenderTypes_0_);
/*    */         
/* 64 */         if (map != null)
/*    */         {
/* 66 */           Object object = map.get("default");
/*    */           
/* 68 */           if ((object instanceof RenderPlayer))
/*    */           {
/* 70 */             return map;
/*    */           }
/*    */         }
/*    */       }
/*    */       
/* 75 */       return null;
/*    */     }
/*    */     catch (Exception exception)
/*    */     {
/* 79 */       Config.warn("Error getting RenderManager.mapRenderTypes");
/* 80 */       Config.warn(exception.getClass().getName() + ": " + exception.getMessage()); }
/* 81 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\optfine\RenderPlayerOF.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */