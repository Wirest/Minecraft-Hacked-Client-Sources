/*     */ package net.minecraft.client.renderer.entity;
/*     */ 
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.FontRenderer;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.client.renderer.Tessellator;
/*     */ import net.minecraft.client.renderer.WorldRenderer;
/*     */ import net.minecraft.client.renderer.culling.ICamera;
/*     */ import net.minecraft.client.renderer.texture.TextureAtlasSprite;
/*     */ import net.minecraft.client.renderer.texture.TextureManager;
/*     */ import net.minecraft.client.renderer.texture.TextureMap;
/*     */ import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
/*     */ import net.minecraft.client.settings.GameSettings;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityLiving;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.util.AxisAlignedBB;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import net.minecraft.world.World;
/*     */ import rip.jutting.polaris.event.events.EventNametags;
/*     */ 
/*     */ public abstract class Render<T extends Entity>
/*     */ {
/*  27 */   private static final ResourceLocation shadowTextures = new ResourceLocation("textures/misc/shadow.png");
/*     */   
/*     */ 
/*     */   protected final RenderManager renderManager;
/*     */   
/*     */   protected float shadowSize;
/*     */   
/*  34 */   protected float shadowOpaque = 1.0F;
/*     */   
/*     */   protected Render(RenderManager renderManager)
/*     */   {
/*  38 */     this.renderManager = renderManager;
/*     */   }
/*     */   
/*     */   public boolean shouldRender(T livingEntity, ICamera camera, double camX, double camY, double camZ)
/*     */   {
/*  43 */     AxisAlignedBB axisalignedbb = livingEntity.getEntityBoundingBox();
/*     */     
/*  45 */     if ((axisalignedbb.func_181656_b()) || (axisalignedbb.getAverageEdgeLength() == 0.0D))
/*     */     {
/*  47 */       axisalignedbb = new AxisAlignedBB(livingEntity.posX - 2.0D, livingEntity.posY - 2.0D, livingEntity.posZ - 2.0D, livingEntity.posX + 2.0D, livingEntity.posY + 2.0D, livingEntity.posZ + 2.0D);
/*     */     }
/*     */     
/*  50 */     return (livingEntity.isInRangeToRender3d(camX, camY, camZ)) && ((livingEntity.ignoreFrustumCheck) || (camera.isBoundingBoxInFrustum(axisalignedbb)));
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
/*  61 */     renderName(entity, x, y, z);
/*     */   }
/*     */   
/*     */   protected void renderName(T entity, double x, double y, double z)
/*     */   {
/*  66 */     if (canRenderName(entity))
/*     */     {
/*  68 */       renderLivingLabel(entity, entity.getDisplayName().getFormattedText(), x, y, z, 64);
/*     */     }
/*     */   }
/*     */   
/*     */   protected boolean canRenderName(T entity)
/*     */   {
/*  74 */     return (entity.getAlwaysRenderNameTagForRender()) && (entity.hasCustomName());
/*     */   }
/*     */   
/*     */   protected void renderOffsetLivingLabel(T entityIn, double x, double y, double z, String str, float p_177069_9_, double p_177069_10_)
/*     */   {
/*  79 */     renderLivingLabel(entityIn, str, x, y, z, 64);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected abstract ResourceLocation getEntityTexture(T paramT);
/*     */   
/*     */ 
/*     */   protected boolean bindEntityTexture(T entity)
/*     */   {
/*  89 */     ResourceLocation resourcelocation = getEntityTexture(entity);
/*     */     
/*  91 */     if (resourcelocation == null)
/*     */     {
/*  93 */       return false;
/*     */     }
/*     */     
/*     */ 
/*  97 */     bindTexture(resourcelocation);
/*  98 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */   public void bindTexture(ResourceLocation location)
/*     */   {
/* 104 */     this.renderManager.renderEngine.bindTexture(location);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private void renderEntityOnFire(Entity entity, double x, double y, double z, float partialTicks)
/*     */   {
/* 112 */     GlStateManager.disableLighting();
/* 113 */     TextureMap texturemap = Minecraft.getMinecraft().getTextureMapBlocks();
/* 114 */     TextureAtlasSprite textureatlassprite = texturemap.getAtlasSprite("minecraft:blocks/fire_layer_0");
/* 115 */     TextureAtlasSprite textureatlassprite1 = texturemap.getAtlasSprite("minecraft:blocks/fire_layer_1");
/* 116 */     GlStateManager.pushMatrix();
/* 117 */     GlStateManager.translate((float)x, (float)y, (float)z);
/* 118 */     float f = entity.width * 1.4F;
/* 119 */     GlStateManager.scale(f, f, f);
/* 120 */     Tessellator tessellator = Tessellator.getInstance();
/* 121 */     WorldRenderer worldrenderer = tessellator.getWorldRenderer();
/* 122 */     float f1 = 0.5F;
/* 123 */     float f2 = 0.0F;
/* 124 */     float f3 = entity.height / f;
/* 125 */     float f4 = (float)(entity.posY - entity.getEntityBoundingBox().minY);
/* 126 */     GlStateManager.rotate(-RenderManager.playerViewY, 0.0F, 1.0F, 0.0F);
/* 127 */     GlStateManager.translate(0.0F, 0.0F, -0.3F + (int)f3 * 0.02F);
/* 128 */     GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/* 129 */     float f5 = 0.0F;
/* 130 */     int i = 0;
/* 131 */     worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
/*     */     
/* 133 */     while (f3 > 0.0F)
/*     */     {
/* 135 */       TextureAtlasSprite textureatlassprite2 = i % 2 == 0 ? textureatlassprite : textureatlassprite1;
/* 136 */       bindTexture(TextureMap.locationBlocksTexture);
/* 137 */       float f6 = textureatlassprite2.getMinU();
/* 138 */       float f7 = textureatlassprite2.getMinV();
/* 139 */       float f8 = textureatlassprite2.getMaxU();
/* 140 */       float f9 = textureatlassprite2.getMaxV();
/*     */       
/* 142 */       if (i / 2 % 2 == 0)
/*     */       {
/* 144 */         float f10 = f8;
/* 145 */         f8 = f6;
/* 146 */         f6 = f10;
/*     */       }
/*     */       
/* 149 */       worldrenderer.pos(f1 - f2, 0.0F - f4, f5).tex(f8, f9).endVertex();
/* 150 */       worldrenderer.pos(-f1 - f2, 0.0F - f4, f5).tex(f6, f9).endVertex();
/* 151 */       worldrenderer.pos(-f1 - f2, 1.4F - f4, f5).tex(f6, f7).endVertex();
/* 152 */       worldrenderer.pos(f1 - f2, 1.4F - f4, f5).tex(f8, f7).endVertex();
/* 153 */       f3 -= 0.45F;
/* 154 */       f4 -= 0.45F;
/* 155 */       f1 *= 0.9F;
/* 156 */       f5 += 0.03F;
/* 157 */       i++;
/*     */     }
/*     */     
/* 160 */     tessellator.draw();
/* 161 */     GlStateManager.popMatrix();
/* 162 */     GlStateManager.enableLighting();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void renderShadow(Entity entityIn, double x, double y, double z, float shadowAlpha, float partialTicks)
/*     */   {
/* 171 */     GlStateManager.enableBlend();
/* 172 */     GlStateManager.blendFunc(770, 771);
/* 173 */     this.renderManager.renderEngine.bindTexture(shadowTextures);
/* 174 */     World world = getWorldFromRenderManager();
/* 175 */     GlStateManager.depthMask(false);
/* 176 */     float f = this.shadowSize;
/*     */     
/* 178 */     if ((entityIn instanceof EntityLiving))
/*     */     {
/* 180 */       EntityLiving entityliving = (EntityLiving)entityIn;
/* 181 */       f *= entityliving.getRenderSizeModifier();
/*     */       
/* 183 */       if (entityliving.isChild())
/*     */       {
/* 185 */         f *= 0.5F;
/*     */       }
/*     */     }
/*     */     
/* 189 */     double d5 = entityIn.lastTickPosX + (entityIn.posX - entityIn.lastTickPosX) * partialTicks;
/* 190 */     double d0 = entityIn.lastTickPosY + (entityIn.posY - entityIn.lastTickPosY) * partialTicks;
/* 191 */     double d1 = entityIn.lastTickPosZ + (entityIn.posZ - entityIn.lastTickPosZ) * partialTicks;
/* 192 */     int i = MathHelper.floor_double(d5 - f);
/* 193 */     int j = MathHelper.floor_double(d5 + f);
/* 194 */     int k = MathHelper.floor_double(d0 - f);
/* 195 */     int l = MathHelper.floor_double(d0);
/* 196 */     int i1 = MathHelper.floor_double(d1 - f);
/* 197 */     int j1 = MathHelper.floor_double(d1 + f);
/* 198 */     double d2 = x - d5;
/* 199 */     double d3 = y - d0;
/* 200 */     double d4 = z - d1;
/* 201 */     Tessellator tessellator = Tessellator.getInstance();
/* 202 */     WorldRenderer worldrenderer = tessellator.getWorldRenderer();
/* 203 */     worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
/*     */     
/* 205 */     for (BlockPos blockpos : BlockPos.getAllInBoxMutable(new BlockPos(i, k, i1), new BlockPos(j, l, j1)))
/*     */     {
/* 207 */       Block block = world.getBlockState(blockpos.down()).getBlock();
/*     */       
/* 209 */       if ((block.getRenderType() != -1) && (world.getLightFromNeighbors(blockpos) > 3))
/*     */       {
/* 211 */         func_180549_a(block, x, y, z, blockpos, shadowAlpha, f, d2, d3, d4);
/*     */       }
/*     */     }
/*     */     
/* 215 */     tessellator.draw();
/* 216 */     GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/* 217 */     GlStateManager.disableBlend();
/* 218 */     GlStateManager.depthMask(true);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private World getWorldFromRenderManager()
/*     */   {
/* 226 */     return this.renderManager.worldObj;
/*     */   }
/*     */   
/*     */   private void func_180549_a(Block blockIn, double p_180549_2_, double p_180549_4_, double p_180549_6_, BlockPos pos, float p_180549_9_, float p_180549_10_, double p_180549_11_, double p_180549_13_, double p_180549_15_)
/*     */   {
/* 231 */     if (blockIn.isFullCube())
/*     */     {
/* 233 */       Tessellator tessellator = Tessellator.getInstance();
/* 234 */       WorldRenderer worldrenderer = tessellator.getWorldRenderer();
/* 235 */       double d0 = (p_180549_9_ - (p_180549_4_ - (pos.getY() + p_180549_13_)) / 2.0D) * 0.5D * getWorldFromRenderManager().getLightBrightness(pos);
/*     */       
/* 237 */       if (d0 >= 0.0D)
/*     */       {
/* 239 */         if (d0 > 1.0D)
/*     */         {
/* 241 */           d0 = 1.0D;
/*     */         }
/*     */         
/* 244 */         double d1 = pos.getX() + blockIn.getBlockBoundsMinX() + p_180549_11_;
/* 245 */         double d2 = pos.getX() + blockIn.getBlockBoundsMaxX() + p_180549_11_;
/* 246 */         double d3 = pos.getY() + blockIn.getBlockBoundsMinY() + p_180549_13_ + 0.015625D;
/* 247 */         double d4 = pos.getZ() + blockIn.getBlockBoundsMinZ() + p_180549_15_;
/* 248 */         double d5 = pos.getZ() + blockIn.getBlockBoundsMaxZ() + p_180549_15_;
/* 249 */         float f = (float)((p_180549_2_ - d1) / 2.0D / p_180549_10_ + 0.5D);
/* 250 */         float f1 = (float)((p_180549_2_ - d2) / 2.0D / p_180549_10_ + 0.5D);
/* 251 */         float f2 = (float)((p_180549_6_ - d4) / 2.0D / p_180549_10_ + 0.5D);
/* 252 */         float f3 = (float)((p_180549_6_ - d5) / 2.0D / p_180549_10_ + 0.5D);
/* 253 */         worldrenderer.pos(d1, d3, d4).tex(f, f2).color(1.0F, 1.0F, 1.0F, (float)d0).endVertex();
/* 254 */         worldrenderer.pos(d1, d3, d5).tex(f, f3).color(1.0F, 1.0F, 1.0F, (float)d0).endVertex();
/* 255 */         worldrenderer.pos(d2, d3, d5).tex(f1, f3).color(1.0F, 1.0F, 1.0F, (float)d0).endVertex();
/* 256 */         worldrenderer.pos(d2, d3, d4).tex(f1, f2).color(1.0F, 1.0F, 1.0F, (float)d0).endVertex();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void renderOffsetAABB(AxisAlignedBB boundingBox, double x, double y, double z)
/*     */   {
/* 266 */     GlStateManager.disableTexture2D();
/* 267 */     Tessellator tessellator = Tessellator.getInstance();
/* 268 */     WorldRenderer worldrenderer = tessellator.getWorldRenderer();
/* 269 */     GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/* 270 */     worldrenderer.setTranslation(x, y, z);
/* 271 */     worldrenderer.begin(7, DefaultVertexFormats.POSITION_NORMAL);
/* 272 */     worldrenderer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).normal(0.0F, 0.0F, -1.0F).endVertex();
/* 273 */     worldrenderer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).normal(0.0F, 0.0F, -1.0F).endVertex();
/* 274 */     worldrenderer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ).normal(0.0F, 0.0F, -1.0F).endVertex();
/* 275 */     worldrenderer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).normal(0.0F, 0.0F, -1.0F).endVertex();
/* 276 */     worldrenderer.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ).normal(0.0F, 0.0F, 1.0F).endVertex();
/* 277 */     worldrenderer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ).normal(0.0F, 0.0F, 1.0F).endVertex();
/* 278 */     worldrenderer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).normal(0.0F, 0.0F, 1.0F).endVertex();
/* 279 */     worldrenderer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).normal(0.0F, 0.0F, 1.0F).endVertex();
/* 280 */     worldrenderer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).normal(0.0F, -1.0F, 0.0F).endVertex();
/* 281 */     worldrenderer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ).normal(0.0F, -1.0F, 0.0F).endVertex();
/* 282 */     worldrenderer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ).normal(0.0F, -1.0F, 0.0F).endVertex();
/* 283 */     worldrenderer.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ).normal(0.0F, -1.0F, 0.0F).endVertex();
/* 284 */     worldrenderer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).normal(0.0F, 1.0F, 0.0F).endVertex();
/* 285 */     worldrenderer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).normal(0.0F, 1.0F, 0.0F).endVertex();
/* 286 */     worldrenderer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).normal(0.0F, 1.0F, 0.0F).endVertex();
/* 287 */     worldrenderer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).normal(0.0F, 1.0F, 0.0F).endVertex();
/* 288 */     worldrenderer.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ).normal(-1.0F, 0.0F, 0.0F).endVertex();
/* 289 */     worldrenderer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).normal(-1.0F, 0.0F, 0.0F).endVertex();
/* 290 */     worldrenderer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).normal(-1.0F, 0.0F, 0.0F).endVertex();
/* 291 */     worldrenderer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).normal(-1.0F, 0.0F, 0.0F).endVertex();
/* 292 */     worldrenderer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ).normal(1.0F, 0.0F, 0.0F).endVertex();
/* 293 */     worldrenderer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).normal(1.0F, 0.0F, 0.0F).endVertex();
/* 294 */     worldrenderer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).normal(1.0F, 0.0F, 0.0F).endVertex();
/* 295 */     worldrenderer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ).normal(1.0F, 0.0F, 0.0F).endVertex();
/* 296 */     tessellator.draw();
/* 297 */     worldrenderer.setTranslation(0.0D, 0.0D, 0.0D);
/* 298 */     GlStateManager.enableTexture2D();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void doRenderShadowAndFire(Entity entityIn, double x, double y, double z, float yaw, float partialTicks)
/*     */   {
/* 306 */     if (this.renderManager.options != null)
/*     */     {
/* 308 */       if ((this.renderManager.options.field_181151_V) && (this.shadowSize > 0.0F) && (!entityIn.isInvisible()) && (this.renderManager.isRenderShadow()))
/*     */       {
/* 310 */         double d0 = this.renderManager.getDistanceToCamera(entityIn.posX, entityIn.posY, entityIn.posZ);
/* 311 */         float f = (float)((1.0D - d0 / 256.0D) * this.shadowOpaque);
/*     */         
/* 313 */         if (f > 0.0F)
/*     */         {
/* 315 */           renderShadow(entityIn, x, y, z, f, partialTicks);
/*     */         }
/*     */       }
/*     */       
/* 319 */       if ((entityIn.canRenderOnFire()) && ((!(entityIn instanceof EntityPlayer)) || (!((EntityPlayer)entityIn).isSpectator())))
/*     */       {
/* 321 */         renderEntityOnFire(entityIn, x, y, z, partialTicks);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public FontRenderer getFontRendererFromRenderManager()
/*     */   {
/* 331 */     return this.renderManager.getFontRenderer();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void renderLivingLabel(T entityIn, String str, double x, double y, double z, int maxDistance)
/*     */   {
/* 339 */     if ((entityIn instanceof EntityPlayer))
/*     */     {
/* 341 */       EventNametags e = new EventNametags();
/* 342 */       e.call();
/* 343 */       if (e.isCancelled()) {
/* 344 */         return;
/*     */       }
/*     */     }
/* 347 */     double d0 = entityIn.getDistanceSqToEntity(this.renderManager.livingPlayer);
/*     */     
/* 349 */     if (d0 <= maxDistance * maxDistance)
/*     */     {
/* 351 */       FontRenderer fontrenderer = getFontRendererFromRenderManager();
/* 352 */       float f = 1.6F;
/* 353 */       float f1 = 0.016666668F * f;
/* 354 */       GlStateManager.pushMatrix();
/* 355 */       GlStateManager.translate((float)x + 0.0F, (float)y + entityIn.height + 0.5F, (float)z);
/* 356 */       org.lwjgl.opengl.GL11.glNormal3f(0.0F, 1.0F, 0.0F);
/* 357 */       GlStateManager.rotate(-RenderManager.playerViewY, 0.0F, 1.0F, 0.0F);
/* 358 */       GlStateManager.rotate(RenderManager.playerViewX, 1.0F, 0.0F, 0.0F);
/* 359 */       GlStateManager.scale(-f1, -f1, f1);
/* 360 */       GlStateManager.disableLighting();
/* 361 */       GlStateManager.depthMask(false);
/* 362 */       GlStateManager.disableDepth();
/* 363 */       GlStateManager.enableBlend();
/* 364 */       GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
/* 365 */       Tessellator tessellator = Tessellator.getInstance();
/* 366 */       WorldRenderer worldrenderer = tessellator.getWorldRenderer();
/* 367 */       int i = 0;
/*     */       
/* 369 */       if (str.equals("deadmau5"))
/*     */       {
/* 371 */         i = -10;
/*     */       }
/*     */       
/* 374 */       int j = fontrenderer.getStringWidth(str) / 2;
/* 375 */       GlStateManager.disableTexture2D();
/* 376 */       worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
/* 377 */       worldrenderer.pos(-j - 1, -1 + i, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
/* 378 */       worldrenderer.pos(-j - 1, 8 + i, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
/* 379 */       worldrenderer.pos(j + 1, 8 + i, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
/* 380 */       worldrenderer.pos(j + 1, -1 + i, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
/* 381 */       tessellator.draw();
/* 382 */       GlStateManager.enableTexture2D();
/* 383 */       fontrenderer.drawString(str, -fontrenderer.getStringWidth(str) / 2, i, 553648127);
/* 384 */       GlStateManager.enableDepth();
/* 385 */       GlStateManager.depthMask(true);
/* 386 */       fontrenderer.drawString(str, -fontrenderer.getStringWidth(str) / 2, i, -1);
/* 387 */       GlStateManager.enableLighting();
/* 388 */       GlStateManager.disableBlend();
/* 389 */       GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/* 390 */       GlStateManager.popMatrix();
/*     */     }
/*     */   }
/*     */   
/*     */   public RenderManager getRenderManager()
/*     */   {
/* 396 */     return this.renderManager;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\renderer\entity\Render.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */