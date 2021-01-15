/*     */ package net.minecraft.client.renderer.entity;
/*     */ 
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.model.ModelBase;
/*     */ import net.minecraft.client.renderer.BlockRendererDispatcher;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.entity.item.EntityMinecart;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import net.minecraft.util.Vec3;
/*     */ 
/*     */ public class RenderMinecart<T extends EntityMinecart> extends Render<T>
/*     */ {
/*  16 */   private static final ResourceLocation minecartTextures = new ResourceLocation("textures/entity/minecart.png");
/*     */   
/*     */ 
/*  19 */   protected ModelBase modelMinecart = new net.minecraft.client.model.ModelMinecart();
/*     */   
/*     */   public RenderMinecart(RenderManager renderManagerIn)
/*     */   {
/*  23 */     super(renderManagerIn);
/*  24 */     this.shadowSize = 0.5F;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void doRender(T entity, double x, double y, double z, float entityYaw, float partialTicks)
/*     */   {
/*  35 */     GlStateManager.pushMatrix();
/*  36 */     bindEntityTexture(entity);
/*  37 */     long i = entity.getEntityId() * 493286711L;
/*  38 */     i = i * i * 4392167121L + i * 98761L;
/*  39 */     float f = (((float)(i >> 16 & 0x7) + 0.5F) / 8.0F - 0.5F) * 0.004F;
/*  40 */     float f1 = (((float)(i >> 20 & 0x7) + 0.5F) / 8.0F - 0.5F) * 0.004F;
/*  41 */     float f2 = (((float)(i >> 24 & 0x7) + 0.5F) / 8.0F - 0.5F) * 0.004F;
/*  42 */     GlStateManager.translate(f, f1, f2);
/*  43 */     double d0 = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * partialTicks;
/*  44 */     double d1 = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * partialTicks;
/*  45 */     double d2 = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * partialTicks;
/*  46 */     double d3 = 0.30000001192092896D;
/*  47 */     Vec3 vec3 = entity.func_70489_a(d0, d1, d2);
/*  48 */     float f3 = entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks;
/*     */     
/*  50 */     if (vec3 != null)
/*     */     {
/*  52 */       Vec3 vec31 = entity.func_70495_a(d0, d1, d2, d3);
/*  53 */       Vec3 vec32 = entity.func_70495_a(d0, d1, d2, -d3);
/*     */       
/*  55 */       if (vec31 == null)
/*     */       {
/*  57 */         vec31 = vec3;
/*     */       }
/*     */       
/*  60 */       if (vec32 == null)
/*     */       {
/*  62 */         vec32 = vec3;
/*     */       }
/*     */       
/*  65 */       x += vec3.xCoord - d0;
/*  66 */       y += (vec31.yCoord + vec32.yCoord) / 2.0D - d1;
/*  67 */       z += vec3.zCoord - d2;
/*  68 */       Vec3 vec33 = vec32.addVector(-vec31.xCoord, -vec31.yCoord, -vec31.zCoord);
/*     */       
/*  70 */       if (vec33.lengthVector() != 0.0D)
/*     */       {
/*  72 */         vec33 = vec33.normalize();
/*  73 */         entityYaw = (float)(Math.atan2(vec33.zCoord, vec33.xCoord) * 180.0D / 3.141592653589793D);
/*  74 */         f3 = (float)(Math.atan(vec33.yCoord) * 73.0D);
/*     */       }
/*     */     }
/*     */     
/*  78 */     GlStateManager.translate((float)x, (float)y + 0.375F, (float)z);
/*  79 */     GlStateManager.rotate(180.0F - entityYaw, 0.0F, 1.0F, 0.0F);
/*  80 */     GlStateManager.rotate(-f3, 0.0F, 0.0F, 1.0F);
/*  81 */     float f5 = entity.getRollingAmplitude() - partialTicks;
/*  82 */     float f6 = entity.getDamage() - partialTicks;
/*     */     
/*  84 */     if (f6 < 0.0F)
/*     */     {
/*  86 */       f6 = 0.0F;
/*     */     }
/*     */     
/*  89 */     if (f5 > 0.0F)
/*     */     {
/*  91 */       GlStateManager.rotate(MathHelper.sin(f5) * f5 * f6 / 10.0F * entity.getRollingDirection(), 1.0F, 0.0F, 0.0F);
/*     */     }
/*     */     
/*  94 */     int j = entity.getDisplayTileOffset();
/*  95 */     IBlockState iblockstate = entity.getDisplayTile();
/*     */     
/*  97 */     if (iblockstate.getBlock().getRenderType() != -1)
/*     */     {
/*  99 */       GlStateManager.pushMatrix();
/* 100 */       bindTexture(net.minecraft.client.renderer.texture.TextureMap.locationBlocksTexture);
/* 101 */       float f4 = 0.75F;
/* 102 */       GlStateManager.scale(f4, f4, f4);
/* 103 */       GlStateManager.translate(-0.5F, (j - 8) / 16.0F, 0.5F);
/* 104 */       func_180560_a(entity, partialTicks, iblockstate);
/* 105 */       GlStateManager.popMatrix();
/* 106 */       GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/* 107 */       bindEntityTexture(entity);
/*     */     }
/*     */     
/* 110 */     GlStateManager.scale(-1.0F, -1.0F, 1.0F);
/* 111 */     this.modelMinecart.render(entity, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
/* 112 */     GlStateManager.popMatrix();
/* 113 */     super.doRender(entity, x, y, z, entityYaw, partialTicks);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected ResourceLocation getEntityTexture(T entity)
/*     */   {
/* 121 */     return minecartTextures;
/*     */   }
/*     */   
/*     */   protected void func_180560_a(T minecart, float partialTicks, IBlockState state)
/*     */   {
/* 126 */     GlStateManager.pushMatrix();
/* 127 */     Minecraft.getMinecraft().getBlockRendererDispatcher().renderBlockBrightness(state, minecart.getBrightness(partialTicks));
/* 128 */     GlStateManager.popMatrix();
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\renderer\entity\RenderMinecart.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */