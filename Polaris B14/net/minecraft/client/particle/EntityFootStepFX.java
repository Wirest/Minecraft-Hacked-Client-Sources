/*    */ package net.minecraft.client.particle;
/*    */ 
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.renderer.GlStateManager;
/*    */ import net.minecraft.client.renderer.Tessellator;
/*    */ import net.minecraft.client.renderer.WorldRenderer;
/*    */ import net.minecraft.client.renderer.texture.TextureManager;
/*    */ import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.util.BlockPos;
/*    */ import net.minecraft.util.ResourceLocation;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ public class EntityFootStepFX extends EntityFX
/*    */ {
/* 16 */   private static final ResourceLocation FOOTPRINT_TEXTURE = new ResourceLocation("textures/particle/footprint.png");
/*    */   private int footstepAge;
/*    */   private int footstepMaxAge;
/*    */   private TextureManager currentFootSteps;
/*    */   
/*    */   protected EntityFootStepFX(TextureManager currentFootStepsIn, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn)
/*    */   {
/* 23 */     super(worldIn, xCoordIn, yCoordIn, zCoordIn, 0.0D, 0.0D, 0.0D);
/* 24 */     this.currentFootSteps = currentFootStepsIn;
/* 25 */     this.motionX = (this.motionY = this.motionZ = 0.0D);
/* 26 */     this.footstepMaxAge = 200;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void renderParticle(WorldRenderer worldRendererIn, Entity entityIn, float partialTicks, float p_180434_4_, float p_180434_5_, float p_180434_6_, float p_180434_7_, float p_180434_8_)
/*    */   {
/* 34 */     float f = (this.footstepAge + partialTicks) / this.footstepMaxAge;
/* 35 */     f *= f;
/* 36 */     float f1 = 2.0F - f * 2.0F;
/*    */     
/* 38 */     if (f1 > 1.0F)
/*    */     {
/* 40 */       f1 = 1.0F;
/*    */     }
/*    */     
/* 43 */     f1 *= 0.2F;
/* 44 */     GlStateManager.disableLighting();
/* 45 */     float f2 = 0.125F;
/* 46 */     float f3 = (float)(this.posX - interpPosX);
/* 47 */     float f4 = (float)(this.posY - interpPosY);
/* 48 */     float f5 = (float)(this.posZ - interpPosZ);
/* 49 */     float f6 = this.worldObj.getLightBrightness(new BlockPos(this));
/* 50 */     this.currentFootSteps.bindTexture(FOOTPRINT_TEXTURE);
/* 51 */     GlStateManager.enableBlend();
/* 52 */     GlStateManager.blendFunc(770, 771);
/* 53 */     worldRendererIn.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
/* 54 */     worldRendererIn.pos(f3 - 0.125F, f4, f5 + 0.125F).tex(0.0D, 1.0D).color(f6, f6, f6, f1).endVertex();
/* 55 */     worldRendererIn.pos(f3 + 0.125F, f4, f5 + 0.125F).tex(1.0D, 1.0D).color(f6, f6, f6, f1).endVertex();
/* 56 */     worldRendererIn.pos(f3 + 0.125F, f4, f5 - 0.125F).tex(1.0D, 0.0D).color(f6, f6, f6, f1).endVertex();
/* 57 */     worldRendererIn.pos(f3 - 0.125F, f4, f5 - 0.125F).tex(0.0D, 0.0D).color(f6, f6, f6, f1).endVertex();
/* 58 */     Tessellator.getInstance().draw();
/* 59 */     GlStateManager.disableBlend();
/* 60 */     GlStateManager.enableLighting();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void onUpdate()
/*    */   {
/* 68 */     this.footstepAge += 1;
/*    */     
/* 70 */     if (this.footstepAge == this.footstepMaxAge)
/*    */     {
/* 72 */       setDead();
/*    */     }
/*    */   }
/*    */   
/*    */   public int getFXLayer()
/*    */   {
/* 78 */     return 3;
/*    */   }
/*    */   
/*    */   public static class Factory implements IParticleFactory
/*    */   {
/*    */     public EntityFX getEntityFX(int particleID, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int... p_178902_15_)
/*    */     {
/* 85 */       return new EntityFootStepFX(Minecraft.getMinecraft().getTextureManager(), worldIn, xCoordIn, yCoordIn, zCoordIn);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\particle\EntityFootStepFX.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */