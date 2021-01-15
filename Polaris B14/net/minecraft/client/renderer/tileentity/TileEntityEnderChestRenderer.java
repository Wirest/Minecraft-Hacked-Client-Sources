/*    */ package net.minecraft.client.renderer.tileentity;
/*    */ 
/*    */ import net.minecraft.client.model.ModelChest;
/*    */ import net.minecraft.client.renderer.GlStateManager;
/*    */ import net.minecraft.tileentity.TileEntityEnderChest;
/*    */ import net.minecraft.util.ResourceLocation;
/*    */ 
/*    */ public class TileEntityEnderChestRenderer extends TileEntitySpecialRenderer<TileEntityEnderChest>
/*    */ {
/* 10 */   private static final ResourceLocation ENDER_CHEST_TEXTURE = new ResourceLocation("textures/entity/chest/ender.png");
/* 11 */   private ModelChest field_147521_c = new ModelChest();
/*    */   
/*    */   public void renderTileEntityAt(TileEntityEnderChest te, double x, double y, double z, float partialTicks, int destroyStage)
/*    */   {
/* 15 */     int i = 0;
/*    */     
/* 17 */     if (te.hasWorldObj())
/*    */     {
/* 19 */       i = te.getBlockMetadata();
/*    */     }
/*    */     
/* 22 */     if (destroyStage >= 0)
/*    */     {
/* 24 */       bindTexture(DESTROY_STAGES[destroyStage]);
/* 25 */       GlStateManager.matrixMode(5890);
/* 26 */       GlStateManager.pushMatrix();
/* 27 */       GlStateManager.scale(4.0F, 4.0F, 1.0F);
/* 28 */       GlStateManager.translate(0.0625F, 0.0625F, 0.0625F);
/* 29 */       GlStateManager.matrixMode(5888);
/*    */     }
/*    */     else
/*    */     {
/* 33 */       bindTexture(ENDER_CHEST_TEXTURE);
/*    */     }
/*    */     
/* 36 */     GlStateManager.pushMatrix();
/* 37 */     GlStateManager.enableRescaleNormal();
/* 38 */     GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/* 39 */     GlStateManager.translate((float)x, (float)y + 1.0F, (float)z + 1.0F);
/* 40 */     GlStateManager.scale(1.0F, -1.0F, -1.0F);
/* 41 */     GlStateManager.translate(0.5F, 0.5F, 0.5F);
/* 42 */     int j = 0;
/*    */     
/* 44 */     if (i == 2)
/*    */     {
/* 46 */       j = 180;
/*    */     }
/*    */     
/* 49 */     if (i == 3)
/*    */     {
/* 51 */       j = 0;
/*    */     }
/*    */     
/* 54 */     if (i == 4)
/*    */     {
/* 56 */       j = 90;
/*    */     }
/*    */     
/* 59 */     if (i == 5)
/*    */     {
/* 61 */       j = -90;
/*    */     }
/*    */     
/* 64 */     GlStateManager.rotate(j, 0.0F, 1.0F, 0.0F);
/* 65 */     GlStateManager.translate(-0.5F, -0.5F, -0.5F);
/* 66 */     float f = te.prevLidAngle + (te.lidAngle - te.prevLidAngle) * partialTicks;
/* 67 */     f = 1.0F - f;
/* 68 */     f = 1.0F - f * f * f;
/* 69 */     this.field_147521_c.chestLid.rotateAngleX = (-(f * 3.1415927F / 2.0F));
/* 70 */     this.field_147521_c.renderAll();
/* 71 */     GlStateManager.disableRescaleNormal();
/* 72 */     GlStateManager.popMatrix();
/* 73 */     GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/*    */     
/* 75 */     if (destroyStage >= 0)
/*    */     {
/* 77 */       GlStateManager.matrixMode(5890);
/* 78 */       GlStateManager.popMatrix();
/* 79 */       GlStateManager.matrixMode(5888);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\renderer\tileentity\TileEntityEnderChestRenderer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */