/*    */ package net.minecraft.client.renderer.tileentity;
/*    */ 
/*    */ import net.minecraft.client.gui.FontRenderer;
/*    */ import net.minecraft.client.renderer.texture.TextureManager;
/*    */ import net.minecraft.tileentity.TileEntity;
/*    */ import net.minecraft.util.ResourceLocation;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ public abstract class TileEntitySpecialRenderer<T extends TileEntity>
/*    */ {
/* 11 */   protected static final ResourceLocation[] DESTROY_STAGES = { new ResourceLocation("textures/blocks/destroy_stage_0.png"), new ResourceLocation("textures/blocks/destroy_stage_1.png"), new ResourceLocation("textures/blocks/destroy_stage_2.png"), new ResourceLocation("textures/blocks/destroy_stage_3.png"), new ResourceLocation("textures/blocks/destroy_stage_4.png"), new ResourceLocation("textures/blocks/destroy_stage_5.png"), new ResourceLocation("textures/blocks/destroy_stage_6.png"), new ResourceLocation("textures/blocks/destroy_stage_7.png"), new ResourceLocation("textures/blocks/destroy_stage_8.png"), new ResourceLocation("textures/blocks/destroy_stage_9.png") };
/*    */   protected TileEntityRendererDispatcher rendererDispatcher;
/*    */   
/*    */   public abstract void renderTileEntityAt(T paramT, double paramDouble1, double paramDouble2, double paramDouble3, float paramFloat, int paramInt);
/*    */   
/*    */   protected void bindTexture(ResourceLocation location)
/*    */   {
/* 18 */     TextureManager texturemanager = this.rendererDispatcher.renderEngine;
/*    */     
/* 20 */     if (texturemanager != null)
/*    */     {
/* 22 */       texturemanager.bindTexture(location);
/*    */     }
/*    */   }
/*    */   
/*    */   protected World getWorld()
/*    */   {
/* 28 */     return this.rendererDispatcher.worldObj;
/*    */   }
/*    */   
/*    */   public void setRendererDispatcher(TileEntityRendererDispatcher rendererDispatcherIn)
/*    */   {
/* 33 */     this.rendererDispatcher = rendererDispatcherIn;
/*    */   }
/*    */   
/*    */   public FontRenderer getFontRenderer()
/*    */   {
/* 38 */     return this.rendererDispatcher.getFontRenderer();
/*    */   }
/*    */   
/*    */   public boolean func_181055_a()
/*    */   {
/* 43 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\renderer\tileentity\TileEntitySpecialRenderer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */