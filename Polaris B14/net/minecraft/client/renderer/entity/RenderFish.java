/*     */ package net.minecraft.client.renderer.entity;
/*     */ 
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.client.renderer.Tessellator;
/*     */ import net.minecraft.client.renderer.WorldRenderer;
/*     */ import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.entity.projectile.EntityFishHook;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import net.minecraft.util.Vec3;
/*     */ 
/*     */ public class RenderFish extends Render<EntityFishHook>
/*     */ {
/*  15 */   private static final ResourceLocation FISH_PARTICLES = new ResourceLocation("textures/particle/particles.png");
/*     */   
/*     */   public RenderFish(RenderManager renderManagerIn)
/*     */   {
/*  19 */     super(renderManagerIn);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void doRender(EntityFishHook entity, double x, double y, double z, float entityYaw, float partialTicks)
/*     */   {
/*  30 */     GlStateManager.pushMatrix();
/*  31 */     GlStateManager.translate((float)x, (float)y, (float)z);
/*  32 */     GlStateManager.enableRescaleNormal();
/*  33 */     GlStateManager.scale(0.5F, 0.5F, 0.5F);
/*  34 */     bindEntityTexture(entity);
/*  35 */     Tessellator tessellator = Tessellator.getInstance();
/*  36 */     WorldRenderer worldrenderer = tessellator.getWorldRenderer();
/*  37 */     int i = 1;
/*  38 */     int j = 2;
/*  39 */     float f = 0.0625F;
/*  40 */     float f1 = 0.125F;
/*  41 */     float f2 = 0.125F;
/*  42 */     float f3 = 0.1875F;
/*  43 */     float f4 = 1.0F;
/*  44 */     float f5 = 0.5F;
/*  45 */     float f6 = 0.5F;
/*  46 */     GlStateManager.rotate(180.0F - RenderManager.playerViewY, 0.0F, 1.0F, 0.0F);
/*  47 */     GlStateManager.rotate(-RenderManager.playerViewX, 1.0F, 0.0F, 0.0F);
/*  48 */     worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_NORMAL);
/*  49 */     worldrenderer.pos(-0.5D, -0.5D, 0.0D).tex(0.0625D, 0.1875D).normal(0.0F, 1.0F, 0.0F).endVertex();
/*  50 */     worldrenderer.pos(0.5D, -0.5D, 0.0D).tex(0.125D, 0.1875D).normal(0.0F, 1.0F, 0.0F).endVertex();
/*  51 */     worldrenderer.pos(0.5D, 0.5D, 0.0D).tex(0.125D, 0.125D).normal(0.0F, 1.0F, 0.0F).endVertex();
/*  52 */     worldrenderer.pos(-0.5D, 0.5D, 0.0D).tex(0.0625D, 0.125D).normal(0.0F, 1.0F, 0.0F).endVertex();
/*  53 */     tessellator.draw();
/*  54 */     GlStateManager.disableRescaleNormal();
/*  55 */     GlStateManager.popMatrix();
/*     */     
/*  57 */     if (entity.angler != null)
/*     */     {
/*  59 */       float f7 = entity.angler.getSwingProgress(partialTicks);
/*  60 */       float f8 = MathHelper.sin(MathHelper.sqrt_float(f7) * 3.1415927F);
/*  61 */       Vec3 vec3 = new Vec3(-0.36D, 0.03D, 0.35D);
/*  62 */       vec3 = vec3.rotatePitch(-(entity.angler.prevRotationPitch + (entity.angler.rotationPitch - entity.angler.prevRotationPitch) * partialTicks) * 3.1415927F / 180.0F);
/*  63 */       vec3 = vec3.rotateYaw(-(entity.angler.prevRotationYaw + (entity.angler.rotationYaw - entity.angler.prevRotationYaw) * partialTicks) * 3.1415927F / 180.0F);
/*  64 */       vec3 = vec3.rotateYaw(f8 * 0.5F);
/*  65 */       vec3 = vec3.rotatePitch(-f8 * 0.7F);
/*  66 */       double d0 = entity.angler.prevPosX + (entity.angler.posX - entity.angler.prevPosX) * partialTicks + vec3.xCoord;
/*  67 */       double d1 = entity.angler.prevPosY + (entity.angler.posY - entity.angler.prevPosY) * partialTicks + vec3.yCoord;
/*  68 */       double d2 = entity.angler.prevPosZ + (entity.angler.posZ - entity.angler.prevPosZ) * partialTicks + vec3.zCoord;
/*  69 */       double d3 = entity.angler.getEyeHeight();
/*     */       
/*  71 */       if (((this.renderManager.options != null) && (this.renderManager.options.thirdPersonView > 0)) || (entity.angler != net.minecraft.client.Minecraft.getMinecraft().thePlayer))
/*     */       {
/*  73 */         float f9 = (entity.angler.prevRenderYawOffset + (entity.angler.renderYawOffset - entity.angler.prevRenderYawOffset) * partialTicks) * 3.1415927F / 180.0F;
/*  74 */         double d4 = MathHelper.sin(f9);
/*  75 */         double d6 = MathHelper.cos(f9);
/*  76 */         double d8 = 0.35D;
/*  77 */         double d10 = 0.8D;
/*  78 */         d0 = entity.angler.prevPosX + (entity.angler.posX - entity.angler.prevPosX) * partialTicks - d6 * 0.35D - d4 * 0.8D;
/*  79 */         d1 = entity.angler.prevPosY + d3 + (entity.angler.posY - entity.angler.prevPosY) * partialTicks - 0.45D;
/*  80 */         d2 = entity.angler.prevPosZ + (entity.angler.posZ - entity.angler.prevPosZ) * partialTicks - d4 * 0.35D + d6 * 0.8D;
/*  81 */         d3 = entity.angler.isSneaking() ? -0.1875D : 0.0D;
/*     */       }
/*     */       
/*  84 */       double d13 = entity.prevPosX + (entity.posX - entity.prevPosX) * partialTicks;
/*  85 */       double d5 = entity.prevPosY + (entity.posY - entity.prevPosY) * partialTicks + 0.25D;
/*  86 */       double d7 = entity.prevPosZ + (entity.posZ - entity.prevPosZ) * partialTicks;
/*  87 */       double d9 = (float)(d0 - d13);
/*  88 */       double d11 = (float)(d1 - d5) + d3;
/*  89 */       double d12 = (float)(d2 - d7);
/*  90 */       GlStateManager.disableTexture2D();
/*  91 */       GlStateManager.disableLighting();
/*  92 */       worldrenderer.begin(3, DefaultVertexFormats.POSITION_COLOR);
/*  93 */       int k = 16;
/*     */       
/*  95 */       for (int l = 0; l <= 16; l++)
/*     */       {
/*  97 */         float f10 = l / 16.0F;
/*  98 */         worldrenderer.pos(x + d9 * f10, y + d11 * (f10 * f10 + f10) * 0.5D + 0.25D, z + d12 * f10).color(0, 0, 0, 255).endVertex();
/*     */       }
/*     */       
/* 101 */       tessellator.draw();
/* 102 */       GlStateManager.enableLighting();
/* 103 */       GlStateManager.enableTexture2D();
/* 104 */       super.doRender(entity, x, y, z, entityYaw, partialTicks);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected ResourceLocation getEntityTexture(EntityFishHook entity)
/*     */   {
/* 113 */     return FISH_PARTICLES;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\renderer\entity\RenderFish.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */