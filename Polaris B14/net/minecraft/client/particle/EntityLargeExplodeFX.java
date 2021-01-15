/*    */ package net.minecraft.client.particle;
/*    */ 
/*    */ import java.util.Random;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.renderer.GlStateManager;
/*    */ import net.minecraft.client.renderer.Tessellator;
/*    */ import net.minecraft.client.renderer.WorldRenderer;
/*    */ import net.minecraft.client.renderer.texture.TextureManager;
/*    */ import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
/*    */ import net.minecraft.client.renderer.vertex.VertexFormat;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.util.ResourceLocation;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ public class EntityLargeExplodeFX extends EntityFX
/*    */ {
/* 17 */   private static final ResourceLocation EXPLOSION_TEXTURE = new ResourceLocation("textures/entity/explosion.png");
/* 18 */   private static final VertexFormat field_181549_az = new VertexFormat().func_181721_a(DefaultVertexFormats.POSITION_3F).func_181721_a(DefaultVertexFormats.TEX_2F).func_181721_a(DefaultVertexFormats.COLOR_4UB).func_181721_a(DefaultVertexFormats.TEX_2S).func_181721_a(DefaultVertexFormats.NORMAL_3B).func_181721_a(DefaultVertexFormats.PADDING_1B);
/*    */   
/*    */   private int field_70581_a;
/*    */   
/*    */   private int field_70584_aq;
/*    */   private TextureManager theRenderEngine;
/*    */   private float field_70582_as;
/*    */   
/*    */   protected EntityLargeExplodeFX(TextureManager renderEngine, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double p_i1213_9_, double p_i1213_11_, double p_i1213_13_)
/*    */   {
/* 28 */     super(worldIn, xCoordIn, yCoordIn, zCoordIn, 0.0D, 0.0D, 0.0D);
/* 29 */     this.theRenderEngine = renderEngine;
/* 30 */     this.field_70584_aq = (6 + this.rand.nextInt(4));
/* 31 */     this.particleRed = (this.particleGreen = this.particleBlue = this.rand.nextFloat() * 0.6F + 0.4F);
/* 32 */     this.field_70582_as = (1.0F - (float)p_i1213_9_ * 0.5F);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void renderParticle(WorldRenderer worldRendererIn, Entity entityIn, float partialTicks, float p_180434_4_, float p_180434_5_, float p_180434_6_, float p_180434_7_, float p_180434_8_)
/*    */   {
/* 40 */     int i = (int)((this.field_70581_a + partialTicks) * 15.0F / this.field_70584_aq);
/*    */     
/* 42 */     if (i <= 15)
/*    */     {
/* 44 */       this.theRenderEngine.bindTexture(EXPLOSION_TEXTURE);
/* 45 */       float f = i % 4 / 4.0F;
/* 46 */       float f1 = f + 0.24975F;
/* 47 */       float f2 = i / 4 / 4.0F;
/* 48 */       float f3 = f2 + 0.24975F;
/* 49 */       float f4 = 2.0F * this.field_70582_as;
/* 50 */       float f5 = (float)(this.prevPosX + (this.posX - this.prevPosX) * partialTicks - interpPosX);
/* 51 */       float f6 = (float)(this.prevPosY + (this.posY - this.prevPosY) * partialTicks - interpPosY);
/* 52 */       float f7 = (float)(this.prevPosZ + (this.posZ - this.prevPosZ) * partialTicks - interpPosZ);
/* 53 */       GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/* 54 */       GlStateManager.disableLighting();
/* 55 */       net.minecraft.client.renderer.RenderHelper.disableStandardItemLighting();
/* 56 */       worldRendererIn.begin(7, field_181549_az);
/* 57 */       worldRendererIn.pos(f5 - p_180434_4_ * f4 - p_180434_7_ * f4, f6 - p_180434_5_ * f4, f7 - p_180434_6_ * f4 - p_180434_8_ * f4).tex(f1, f3).color(this.particleRed, this.particleGreen, this.particleBlue, 1.0F).lightmap(0, 240).normal(0.0F, 1.0F, 0.0F).endVertex();
/* 58 */       worldRendererIn.pos(f5 - p_180434_4_ * f4 + p_180434_7_ * f4, f6 + p_180434_5_ * f4, f7 - p_180434_6_ * f4 + p_180434_8_ * f4).tex(f1, f2).color(this.particleRed, this.particleGreen, this.particleBlue, 1.0F).lightmap(0, 240).normal(0.0F, 1.0F, 0.0F).endVertex();
/* 59 */       worldRendererIn.pos(f5 + p_180434_4_ * f4 + p_180434_7_ * f4, f6 + p_180434_5_ * f4, f7 + p_180434_6_ * f4 + p_180434_8_ * f4).tex(f, f2).color(this.particleRed, this.particleGreen, this.particleBlue, 1.0F).lightmap(0, 240).normal(0.0F, 1.0F, 0.0F).endVertex();
/* 60 */       worldRendererIn.pos(f5 + p_180434_4_ * f4 - p_180434_7_ * f4, f6 - p_180434_5_ * f4, f7 + p_180434_6_ * f4 - p_180434_8_ * f4).tex(f, f3).color(this.particleRed, this.particleGreen, this.particleBlue, 1.0F).lightmap(0, 240).normal(0.0F, 1.0F, 0.0F).endVertex();
/* 61 */       Tessellator.getInstance().draw();
/* 62 */       GlStateManager.enableLighting();
/*    */     }
/*    */   }
/*    */   
/*    */   public int getBrightnessForRender(float partialTicks)
/*    */   {
/* 68 */     return 61680;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void onUpdate()
/*    */   {
/* 76 */     this.prevPosX = this.posX;
/* 77 */     this.prevPosY = this.posY;
/* 78 */     this.prevPosZ = this.posZ;
/* 79 */     this.field_70581_a += 1;
/*    */     
/* 81 */     if (this.field_70581_a == this.field_70584_aq)
/*    */     {
/* 83 */       setDead();
/*    */     }
/*    */   }
/*    */   
/*    */   public int getFXLayer()
/*    */   {
/* 89 */     return 3;
/*    */   }
/*    */   
/*    */   public static class Factory implements IParticleFactory
/*    */   {
/*    */     public EntityFX getEntityFX(int particleID, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int... p_178902_15_)
/*    */     {
/* 96 */       return new EntityLargeExplodeFX(Minecraft.getMinecraft().getTextureManager(), worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\particle\EntityLargeExplodeFX.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */