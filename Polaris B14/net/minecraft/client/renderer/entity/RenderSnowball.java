/*    */ package net.minecraft.client.renderer.entity;
/*    */ 
/*    */ import net.minecraft.client.renderer.GlStateManager;
/*    */ import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
/*    */ import net.minecraft.client.renderer.texture.TextureMap;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.item.Item;
/*    */ import net.minecraft.item.ItemStack;
/*    */ import net.minecraft.util.ResourceLocation;
/*    */ 
/*    */ public class RenderSnowball<T extends Entity> extends Render<T>
/*    */ {
/*    */   protected final Item field_177084_a;
/*    */   private final RenderItem field_177083_e;
/*    */   
/*    */   public RenderSnowball(RenderManager renderManagerIn, Item p_i46137_2_, RenderItem p_i46137_3_)
/*    */   {
/* 18 */     super(renderManagerIn);
/* 19 */     this.field_177084_a = p_i46137_2_;
/* 20 */     this.field_177083_e = p_i46137_3_;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public void doRender(T entity, double x, double y, double z, float entityYaw, float partialTicks)
/*    */   {
/* 31 */     GlStateManager.pushMatrix();
/* 32 */     GlStateManager.translate((float)x, (float)y, (float)z);
/* 33 */     GlStateManager.enableRescaleNormal();
/* 34 */     GlStateManager.scale(0.5F, 0.5F, 0.5F);
/* 35 */     GlStateManager.rotate(-RenderManager.playerViewY, 0.0F, 1.0F, 0.0F);
/* 36 */     GlStateManager.rotate(RenderManager.playerViewX, 1.0F, 0.0F, 0.0F);
/* 37 */     bindTexture(TextureMap.locationBlocksTexture);
/* 38 */     this.field_177083_e.func_181564_a(func_177082_d(entity), ItemCameraTransforms.TransformType.GROUND);
/* 39 */     GlStateManager.disableRescaleNormal();
/* 40 */     GlStateManager.popMatrix();
/* 41 */     super.doRender(entity, x, y, z, entityYaw, partialTicks);
/*    */   }
/*    */   
/*    */   public ItemStack func_177082_d(T entityIn)
/*    */   {
/* 46 */     return new ItemStack(this.field_177084_a, 1, 0);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   protected ResourceLocation getEntityTexture(Entity entity)
/*    */   {
/* 54 */     return TextureMap.locationBlocksTexture;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\renderer\entity\RenderSnowball.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */