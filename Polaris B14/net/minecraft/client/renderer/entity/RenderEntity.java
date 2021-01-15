/*    */ package net.minecraft.client.renderer.entity;
/*    */ 
/*    */ import net.minecraft.client.renderer.GlStateManager;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.util.ResourceLocation;
/*    */ 
/*    */ public class RenderEntity extends Render<Entity>
/*    */ {
/*    */   public RenderEntity(RenderManager renderManagerIn)
/*    */   {
/* 11 */     super(renderManagerIn);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public void doRender(Entity entity, double x, double y, double z, float entityYaw, float partialTicks)
/*    */   {
/* 22 */     GlStateManager.pushMatrix();
/* 23 */     renderOffsetAABB(entity.getEntityBoundingBox(), x - entity.lastTickPosX, y - entity.lastTickPosY, z - entity.lastTickPosZ);
/* 24 */     GlStateManager.popMatrix();
/* 25 */     super.doRender(entity, x, y, z, entityYaw, partialTicks);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   protected ResourceLocation getEntityTexture(Entity entity)
/*    */   {
/* 33 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\renderer\entity\RenderEntity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */