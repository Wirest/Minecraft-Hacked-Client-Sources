/*     */ package net.minecraft.client.renderer.entity;
/*     */ 
/*     */ import java.nio.FloatBuffer;
/*     */ import java.util.List;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.entity.EntityPlayerSP;
/*     */ import net.minecraft.client.gui.FontRenderer;
/*     */ import net.minecraft.client.model.ModelBase;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.client.renderer.OpenGlHelper;
/*     */ import net.minecraft.client.renderer.Tessellator;
/*     */ import net.minecraft.client.renderer.WorldRenderer;
/*     */ import net.minecraft.client.renderer.entity.layers.LayerCape;
/*     */ import net.minecraft.client.renderer.entity.layers.LayerRenderer;
/*     */ import net.minecraft.client.renderer.texture.DynamicTexture;
/*     */ import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.scoreboard.ScorePlayerTeam;
/*     */ import net.minecraft.scoreboard.Team;
/*     */ import net.minecraft.scoreboard.Team.EnumVisible;
/*     */ import net.minecraft.util.EnumChatFormatting;
/*     */ import net.minecraft.util.IChatComponent;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.lwjgl.opengl.GL11;
/*     */ import rip.jutting.polaris.Polaris;
/*     */ import rip.jutting.polaris.event.events.EventNametags;
/*     */ import rip.jutting.polaris.module.Module;
/*     */ import rip.jutting.polaris.ui.click.settings.Setting;
/*     */ import rip.jutting.polaris.ui.click.settings.SettingsManager;
/*     */ import rip.jutting.polaris.utils.OutlineUtils;
/*     */ 
/*     */ public abstract class RendererLivingEntity<T extends EntityLivingBase> extends Render<T>
/*     */ {
/*  37 */   private static final Logger logger = ;
/*  38 */   private static final DynamicTexture field_177096_e = new DynamicTexture(16, 16);
/*     */   protected ModelBase mainModel;
/*  40 */   protected FloatBuffer brightnessBuffer = net.minecraft.client.renderer.GLAllocation.createDirectFloatBuffer(4);
/*  41 */   protected List<LayerRenderer<T>> layerRenderers = com.google.common.collect.Lists.newArrayList();
/*  42 */   protected boolean renderOutlines = false;
/*     */   
/*     */   public RendererLivingEntity(RenderManager renderManagerIn, ModelBase modelBaseIn, float shadowSizeIn) {
/*  45 */     super(renderManagerIn);
/*  46 */     this.mainModel = modelBaseIn;
/*  47 */     this.shadowSize = shadowSizeIn;
/*     */   }
/*     */   
/*     */   protected <V extends EntityLivingBase, U extends LayerRenderer<V>> boolean addLayer(U layer) {
/*  51 */     return this.layerRenderers.add(layer);
/*     */   }
/*     */   
/*     */   protected <V extends EntityLivingBase, U extends LayerRenderer<V>> boolean addLayer(LayerCape layerCape) {
/*  55 */     return this.layerRenderers.add(layerCape);
/*     */   }
/*     */   
/*     */   protected <V extends EntityLivingBase, U extends LayerRenderer<V>> boolean removeLayer(U layer) {
/*  59 */     return this.layerRenderers.remove(layer);
/*     */   }
/*     */   
/*     */   public ModelBase getMainModel() {
/*  63 */     return this.mainModel;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected float interpolateRotation(float par1, float par2, float par3)
/*     */   {
/*  75 */     for (float f = par2 - par1; f < -180.0F; f += 360.0F) {}
/*     */     
/*     */ 
/*     */ 
/*  79 */     while (f >= 180.0F) {
/*  80 */       f -= 360.0F;
/*     */     }
/*     */     
/*  83 */     return par1 + par3 * f;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void doRender(T entity, double x, double y, double z, float entityYaw, float partialTicks)
/*     */   {
/*  98 */     GlStateManager.pushMatrix();
/*  99 */     GlStateManager.disableCull();
/* 100 */     this.mainModel.swingProgress = getSwingProgress(entity, partialTicks);
/* 101 */     this.mainModel.isRiding = entity.isRiding();
/* 102 */     this.mainModel.isChild = entity.isChild();
/*     */     try
/*     */     {
/* 105 */       float f = interpolateRotation(entity.prevRenderYawOffset, entity.renderYawOffset, partialTicks);
/* 106 */       float f1 = interpolateRotation(entity.prevRotationYawHead, entity.rotationYawHead, partialTicks);
/* 107 */       float f2 = f1 - f;
/*     */       
/* 109 */       if ((entity.isRiding()) && ((entity.ridingEntity instanceof EntityLivingBase))) {
/* 110 */         EntityLivingBase entitylivingbase = (EntityLivingBase)entity.ridingEntity;
/* 111 */         f = interpolateRotation(entitylivingbase.prevRenderYawOffset, entitylivingbase.renderYawOffset, 
/* 112 */           partialTicks);
/* 113 */         f2 = f1 - f;
/* 114 */         float f3 = MathHelper.wrapAngleTo180_float(f2);
/*     */         
/* 116 */         if (f3 < -85.0F) {
/* 117 */           f3 = -85.0F;
/*     */         }
/*     */         
/* 120 */         if (f3 >= 85.0F) {
/* 121 */           f3 = 85.0F;
/*     */         }
/*     */         
/* 124 */         f = f1 - f3;
/*     */         
/* 126 */         if (f3 * f3 > 2500.0F) {
/* 127 */           f += f3 * 0.2F;
/*     */         }
/*     */       }
/*     */       
/* 131 */       float f7 = entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks;
/* 132 */       renderLivingAt(entity, x, y, z);
/* 133 */       float f8 = handleRotationFloat(entity, partialTicks);
/* 134 */       rotateCorpse(entity, f8, f, partialTicks);
/* 135 */       GlStateManager.enableRescaleNormal();
/* 136 */       GlStateManager.scale(-1.0F, -1.0F, 1.0F);
/* 137 */       preRenderCallback(entity, partialTicks);
/* 138 */       float f4 = 0.0625F;
/* 139 */       GlStateManager.translate(0.0F, -1.5078125F, 0.0F);
/* 140 */       float f5 = entity.prevLimbSwingAmount + 
/* 141 */         (entity.limbSwingAmount - entity.prevLimbSwingAmount) * partialTicks;
/* 142 */       float f6 = entity.limbSwing - entity.limbSwingAmount * (1.0F - partialTicks);
/*     */       
/* 144 */       if (entity.isChild()) {
/* 145 */         f6 *= 3.0F;
/*     */       }
/*     */       
/* 148 */       if (f5 > 1.0F) {
/* 149 */         f5 = 1.0F;
/*     */       }
/*     */       
/* 152 */       GlStateManager.enableAlpha();
/* 153 */       this.mainModel.setLivingAnimations(entity, f6, f5, partialTicks);
/* 154 */       this.mainModel.setRotationAngles(f6, f5, f8, f2, f7, 0.0625F, entity);
/*     */       
/* 156 */       String mode = Polaris.instance.settingsManager.getSettingByName("ESP Mode").getValString();
/*     */       
/* 158 */       if ((Polaris.instance.moduleManager.getModuleByName("ESP").isToggled()) && (mode.equalsIgnoreCase("Outline"))) {
/* 159 */         GlStateManager.depthMask(true);
/* 160 */         if ((!(entity instanceof EntityPlayer)) || (!((EntityPlayer)entity).isSpectator())) {
/* 161 */           renderLayers(entity, f6, f5, partialTicks, f8, f2, f7, f4);
/*     */         }
/*     */         
/* 164 */         if ((entity instanceof EntityPlayer)) {
/* 165 */           if (entity != Minecraft.getMinecraft().thePlayer) {
/* 166 */             renderModel(entity, f6, f5, f8, f2, f7, f4);
/* 167 */             OutlineUtils.renderOne();
/* 168 */             renderModel(entity, f6, f5, f8, f2, f7, f4);
/* 169 */             OutlineUtils.renderTwo();
/* 170 */             renderModel(entity, f6, f5, f8, f2, f7, f4);
/* 171 */             OutlineUtils.renderThree();
/* 172 */             OutlineUtils.renderFour();
/* 173 */             renderModel(entity, f6, f5, f8, f2, f7, f4);
/* 174 */             OutlineUtils.renderFive();
/*     */           }
/*     */         } else {
/* 177 */           renderModel(entity, f6, f5, f8, f2, f7, f4);
/*     */         }
/*     */       } else {
/* 180 */         renderModel(entity, f6, f5, f8, f2, f7, f4);
/*     */       }
/*     */       
/* 183 */       if (this.renderOutlines) {
/* 184 */         boolean flag1 = setScoreTeamColor(entity);
/* 185 */         renderModel(entity, f6, f5, f8, f2, f7, 0.0625F);
/*     */         
/* 187 */         if (flag1) {
/* 188 */           unsetScoreTeamColor();
/*     */         }
/*     */       } else {
/* 191 */         boolean flag = setDoRenderBrightness(entity, partialTicks);
/* 192 */         renderModel(entity, f6, f5, f8, f2, f7, 0.0625F);
/*     */         
/* 194 */         if (flag) {
/* 195 */           unsetBrightness();
/*     */         }
/*     */         
/* 198 */         GlStateManager.depthMask(true);
/*     */         
/* 200 */         if ((!(entity instanceof EntityPlayer)) || (!((EntityPlayer)entity).isSpectator())) {
/* 201 */           renderLayers(entity, f6, f5, partialTicks, f8, f2, f7, 0.0625F);
/*     */         }
/*     */       }
/*     */       
/* 205 */       GlStateManager.disableRescaleNormal();
/*     */     } catch (Exception exception) {
/* 207 */       logger.error("Couldn't render entity", exception);
/*     */     }
/*     */     
/* 210 */     GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
/* 211 */     GlStateManager.enableTexture2D();
/* 212 */     GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
/* 213 */     GlStateManager.enableCull();
/* 214 */     GlStateManager.popMatrix();
/*     */     
/* 216 */     if (!this.renderOutlines) {
/* 217 */       super.doRender(entity, x, y, z, entityYaw, partialTicks);
/*     */     }
/*     */   }
/*     */   
/*     */   protected boolean setScoreTeamColor(T entityLivingBaseIn) {
/* 222 */     int i = 16777215;
/*     */     
/* 224 */     if ((entityLivingBaseIn instanceof EntityPlayer)) {
/* 225 */       ScorePlayerTeam scoreplayerteam = (ScorePlayerTeam)entityLivingBaseIn.getTeam();
/*     */       
/* 227 */       if (scoreplayerteam != null) {
/* 228 */         String s = FontRenderer.getFormatFromString(scoreplayerteam.getColorPrefix());
/*     */         
/* 230 */         if (s.length() >= 2) {
/* 231 */           i = getFontRendererFromRenderManager().getColorCode(s.charAt(1));
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 236 */     float f1 = (i >> 16 & 0xFF) / 255.0F;
/* 237 */     float f2 = (i >> 8 & 0xFF) / 255.0F;
/* 238 */     float f = (i & 0xFF) / 255.0F;
/* 239 */     GlStateManager.disableLighting();
/* 240 */     GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
/* 241 */     GlStateManager.color(f1, f2, f, 1.0F);
/* 242 */     GlStateManager.disableTexture2D();
/* 243 */     GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
/* 244 */     GlStateManager.disableTexture2D();
/* 245 */     GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
/* 246 */     return true;
/*     */   }
/*     */   
/*     */   protected void unsetScoreTeamColor() {
/* 250 */     GlStateManager.enableLighting();
/* 251 */     GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
/* 252 */     GlStateManager.enableTexture2D();
/* 253 */     GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
/* 254 */     GlStateManager.enableTexture2D();
/* 255 */     GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void renderModel(T entitylivingbaseIn, float p_77036_2_, float p_77036_3_, float p_77036_4_, float p_77036_5_, float p_77036_6_, float p_77036_7_)
/*     */   {
/* 263 */     boolean flag = !entitylivingbaseIn.isInvisible();
/* 264 */     boolean flag1 = (!flag) && (!entitylivingbaseIn.isInvisibleToPlayer(Minecraft.getMinecraft().thePlayer));
/*     */     
/* 266 */     if ((flag) || (flag1)) {
/* 267 */       if (!bindEntityTexture(entitylivingbaseIn)) {
/* 268 */         return;
/*     */       }
/*     */       
/* 271 */       if (flag1) {
/* 272 */         GlStateManager.pushMatrix();
/* 273 */         GlStateManager.color(1.0F, 1.0F, 1.0F, 0.15F);
/* 274 */         GlStateManager.depthMask(false);
/* 275 */         GlStateManager.enableBlend();
/* 276 */         GlStateManager.blendFunc(770, 771);
/* 277 */         GlStateManager.alphaFunc(516, 0.003921569F);
/*     */       }
/* 279 */       float red = (float)Polaris.instance.settingsManager.getSettingByName("Red").getValDouble();
/* 280 */       float green = (float)Polaris.instance.settingsManager.getSettingByName("Green").getValDouble();
/* 281 */       float blue = (float)Polaris.instance.settingsManager.getSettingByName("Blue").getValDouble();
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 305 */       this.mainModel.render(entitylivingbaseIn, p_77036_2_, p_77036_3_, p_77036_4_, p_77036_5_, p_77036_6_, 
/* 306 */         p_77036_7_);
/*     */       
/* 308 */       if (flag1) {
/* 309 */         GlStateManager.disableBlend();
/* 310 */         GlStateManager.alphaFunc(516, 0.1F);
/* 311 */         GlStateManager.popMatrix();
/* 312 */         GlStateManager.depthMask(true);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   protected boolean setDoRenderBrightness(T entityLivingBaseIn, float partialTicks) {
/* 318 */     return setBrightness(entityLivingBaseIn, partialTicks, true);
/*     */   }
/*     */   
/*     */   protected boolean setBrightness(T entitylivingbaseIn, float partialTicks, boolean combineTextures) {
/* 322 */     float f = entitylivingbaseIn.getBrightness(partialTicks);
/* 323 */     int i = getColorMultiplier(entitylivingbaseIn, f, partialTicks);
/* 324 */     boolean flag = (i >> 24 & 0xFF) > 0;
/* 325 */     boolean flag1 = (entitylivingbaseIn.hurtTime > 0) || (entitylivingbaseIn.deathTime > 0);
/*     */     
/* 327 */     if ((!flag) && (!flag1))
/* 328 */       return false;
/* 329 */     if ((!flag) && (!combineTextures)) {
/* 330 */       return false;
/*     */     }
/* 332 */     GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
/* 333 */     GlStateManager.enableTexture2D();
/* 334 */     GL11.glTexEnvi(8960, 8704, OpenGlHelper.GL_COMBINE);
/* 335 */     GL11.glTexEnvi(8960, OpenGlHelper.GL_COMBINE_RGB, 8448);
/* 336 */     GL11.glTexEnvi(8960, OpenGlHelper.GL_SOURCE0_RGB, OpenGlHelper.defaultTexUnit);
/* 337 */     GL11.glTexEnvi(8960, OpenGlHelper.GL_SOURCE1_RGB, OpenGlHelper.GL_PRIMARY_COLOR);
/* 338 */     GL11.glTexEnvi(8960, OpenGlHelper.GL_OPERAND0_RGB, 768);
/* 339 */     GL11.glTexEnvi(8960, OpenGlHelper.GL_OPERAND1_RGB, 768);
/* 340 */     GL11.glTexEnvi(8960, OpenGlHelper.GL_COMBINE_ALPHA, 7681);
/* 341 */     GL11.glTexEnvi(8960, OpenGlHelper.GL_SOURCE0_ALPHA, OpenGlHelper.defaultTexUnit);
/* 342 */     GL11.glTexEnvi(8960, OpenGlHelper.GL_OPERAND0_ALPHA, 770);
/* 343 */     GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
/* 344 */     GlStateManager.enableTexture2D();
/* 345 */     GL11.glTexEnvi(8960, 8704, OpenGlHelper.GL_COMBINE);
/* 346 */     GL11.glTexEnvi(8960, OpenGlHelper.GL_COMBINE_RGB, OpenGlHelper.GL_INTERPOLATE);
/* 347 */     GL11.glTexEnvi(8960, OpenGlHelper.GL_SOURCE0_RGB, OpenGlHelper.GL_CONSTANT);
/* 348 */     GL11.glTexEnvi(8960, OpenGlHelper.GL_SOURCE1_RGB, OpenGlHelper.GL_PREVIOUS);
/* 349 */     GL11.glTexEnvi(8960, OpenGlHelper.GL_SOURCE2_RGB, OpenGlHelper.GL_CONSTANT);
/* 350 */     GL11.glTexEnvi(8960, OpenGlHelper.GL_OPERAND0_RGB, 768);
/* 351 */     GL11.glTexEnvi(8960, OpenGlHelper.GL_OPERAND1_RGB, 768);
/* 352 */     GL11.glTexEnvi(8960, OpenGlHelper.GL_OPERAND2_RGB, 770);
/* 353 */     GL11.glTexEnvi(8960, OpenGlHelper.GL_COMBINE_ALPHA, 7681);
/* 354 */     GL11.glTexEnvi(8960, OpenGlHelper.GL_SOURCE0_ALPHA, OpenGlHelper.GL_PREVIOUS);
/* 355 */     GL11.glTexEnvi(8960, OpenGlHelper.GL_OPERAND0_ALPHA, 770);
/* 356 */     this.brightnessBuffer.position(0);
/*     */     
/* 358 */     if (flag1) {
/* 359 */       this.brightnessBuffer.put(1.0F);
/* 360 */       this.brightnessBuffer.put(0.0F);
/* 361 */       this.brightnessBuffer.put(0.0F);
/* 362 */       this.brightnessBuffer.put(0.3F);
/*     */     } else {
/* 364 */       float f1 = (i >> 24 & 0xFF) / 255.0F;
/* 365 */       float f2 = (i >> 16 & 0xFF) / 255.0F;
/* 366 */       float f3 = (i >> 8 & 0xFF) / 255.0F;
/* 367 */       float f4 = (i & 0xFF) / 255.0F;
/* 368 */       this.brightnessBuffer.put(f2);
/* 369 */       this.brightnessBuffer.put(f3);
/* 370 */       this.brightnessBuffer.put(f4);
/* 371 */       this.brightnessBuffer.put(1.0F - f1);
/*     */     }
/*     */     
/* 374 */     this.brightnessBuffer.flip();
/* 375 */     GL11.glTexEnv(8960, 8705, this.brightnessBuffer);
/* 376 */     GlStateManager.setActiveTexture(OpenGlHelper.GL_TEXTURE2);
/* 377 */     GlStateManager.enableTexture2D();
/* 378 */     GlStateManager.bindTexture(field_177096_e.getGlTextureId());
/* 379 */     GL11.glTexEnvi(8960, 8704, OpenGlHelper.GL_COMBINE);
/* 380 */     GL11.glTexEnvi(8960, OpenGlHelper.GL_COMBINE_RGB, 8448);
/* 381 */     GL11.glTexEnvi(8960, OpenGlHelper.GL_SOURCE0_RGB, OpenGlHelper.GL_PREVIOUS);
/* 382 */     GL11.glTexEnvi(8960, OpenGlHelper.GL_SOURCE1_RGB, OpenGlHelper.lightmapTexUnit);
/* 383 */     GL11.glTexEnvi(8960, OpenGlHelper.GL_OPERAND0_RGB, 768);
/* 384 */     GL11.glTexEnvi(8960, OpenGlHelper.GL_OPERAND1_RGB, 768);
/* 385 */     GL11.glTexEnvi(8960, OpenGlHelper.GL_COMBINE_ALPHA, 7681);
/* 386 */     GL11.glTexEnvi(8960, OpenGlHelper.GL_SOURCE0_ALPHA, OpenGlHelper.GL_PREVIOUS);
/* 387 */     GL11.glTexEnvi(8960, OpenGlHelper.GL_OPERAND0_ALPHA, 770);
/* 388 */     GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
/* 389 */     return true;
/*     */   }
/*     */   
/*     */   protected void unsetBrightness()
/*     */   {
/* 394 */     GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
/* 395 */     GlStateManager.enableTexture2D();
/* 396 */     GL11.glTexEnvi(8960, 8704, OpenGlHelper.GL_COMBINE);
/* 397 */     GL11.glTexEnvi(8960, OpenGlHelper.GL_COMBINE_RGB, 8448);
/* 398 */     GL11.glTexEnvi(8960, OpenGlHelper.GL_SOURCE0_RGB, OpenGlHelper.defaultTexUnit);
/* 399 */     GL11.glTexEnvi(8960, OpenGlHelper.GL_SOURCE1_RGB, OpenGlHelper.GL_PRIMARY_COLOR);
/* 400 */     GL11.glTexEnvi(8960, OpenGlHelper.GL_OPERAND0_RGB, 768);
/* 401 */     GL11.glTexEnvi(8960, OpenGlHelper.GL_OPERAND1_RGB, 768);
/* 402 */     GL11.glTexEnvi(8960, OpenGlHelper.GL_COMBINE_ALPHA, 8448);
/* 403 */     GL11.glTexEnvi(8960, OpenGlHelper.GL_SOURCE0_ALPHA, OpenGlHelper.defaultTexUnit);
/* 404 */     GL11.glTexEnvi(8960, OpenGlHelper.GL_SOURCE1_ALPHA, OpenGlHelper.GL_PRIMARY_COLOR);
/* 405 */     GL11.glTexEnvi(8960, OpenGlHelper.GL_OPERAND0_ALPHA, 770);
/* 406 */     GL11.glTexEnvi(8960, OpenGlHelper.GL_OPERAND1_ALPHA, 770);
/* 407 */     GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
/* 408 */     GL11.glTexEnvi(8960, 8704, OpenGlHelper.GL_COMBINE);
/* 409 */     GL11.glTexEnvi(8960, OpenGlHelper.GL_COMBINE_RGB, 8448);
/* 410 */     GL11.glTexEnvi(8960, OpenGlHelper.GL_OPERAND0_RGB, 768);
/* 411 */     GL11.glTexEnvi(8960, OpenGlHelper.GL_OPERAND1_RGB, 768);
/* 412 */     GL11.glTexEnvi(8960, OpenGlHelper.GL_SOURCE0_RGB, 5890);
/* 413 */     GL11.glTexEnvi(8960, OpenGlHelper.GL_SOURCE1_RGB, OpenGlHelper.GL_PREVIOUS);
/* 414 */     GL11.glTexEnvi(8960, OpenGlHelper.GL_COMBINE_ALPHA, 8448);
/* 415 */     GL11.glTexEnvi(8960, OpenGlHelper.GL_OPERAND0_ALPHA, 770);
/* 416 */     GL11.glTexEnvi(8960, OpenGlHelper.GL_SOURCE0_ALPHA, 5890);
/* 417 */     GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/* 418 */     GlStateManager.setActiveTexture(OpenGlHelper.GL_TEXTURE2);
/* 419 */     GlStateManager.disableTexture2D();
/* 420 */     GlStateManager.bindTexture(0);
/* 421 */     GL11.glTexEnvi(8960, 8704, OpenGlHelper.GL_COMBINE);
/* 422 */     GL11.glTexEnvi(8960, OpenGlHelper.GL_COMBINE_RGB, 8448);
/* 423 */     GL11.glTexEnvi(8960, OpenGlHelper.GL_OPERAND0_RGB, 768);
/* 424 */     GL11.glTexEnvi(8960, OpenGlHelper.GL_OPERAND1_RGB, 768);
/* 425 */     GL11.glTexEnvi(8960, OpenGlHelper.GL_SOURCE0_RGB, 5890);
/* 426 */     GL11.glTexEnvi(8960, OpenGlHelper.GL_SOURCE1_RGB, OpenGlHelper.GL_PREVIOUS);
/* 427 */     GL11.glTexEnvi(8960, OpenGlHelper.GL_COMBINE_ALPHA, 8448);
/* 428 */     GL11.glTexEnvi(8960, OpenGlHelper.GL_OPERAND0_ALPHA, 770);
/* 429 */     GL11.glTexEnvi(8960, OpenGlHelper.GL_SOURCE0_ALPHA, 5890);
/* 430 */     GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected void renderLivingAt(T entityLivingBaseIn, double x, double y, double z)
/*     */   {
/* 437 */     GlStateManager.translate((float)x, (float)y, (float)z);
/*     */   }
/*     */   
/*     */   protected void rotateCorpse(T bat, float p_77043_2_, float p_77043_3_, float partialTicks) {
/* 441 */     GlStateManager.rotate(180.0F - p_77043_3_, 0.0F, 1.0F, 0.0F);
/*     */     
/* 443 */     if (bat.deathTime > 0) {
/* 444 */       float f = (bat.deathTime + partialTicks - 1.0F) / 20.0F * 1.6F;
/* 445 */       f = MathHelper.sqrt_float(f);
/*     */       
/* 447 */       if (f > 1.0F) {
/* 448 */         f = 1.0F;
/*     */       }
/*     */       
/* 451 */       GlStateManager.rotate(f * getDeathMaxRotation(bat), 0.0F, 0.0F, 1.0F);
/*     */     } else {
/* 453 */       String s = EnumChatFormatting.getTextWithoutFormattingCodes(bat.getName());
/*     */       
/* 455 */       if ((s != null) && ((s.equals("Dinnerbone")) || (s.equals("Grumm"))) && (
/* 456 */         (!(bat instanceof EntityPlayer)) || (((EntityPlayer)bat).isWearing(net.minecraft.entity.player.EnumPlayerModelParts.CAPE)))) {
/* 457 */         GlStateManager.translate(0.0F, bat.height + 0.1F, 0.0F);
/* 458 */         GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected float getSwingProgress(T livingBase, float partialTickTime)
/*     */   {
/* 468 */     return livingBase.getSwingProgress(partialTickTime);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected float handleRotationFloat(T livingBase, float partialTicks)
/*     */   {
/* 475 */     return livingBase.ticksExisted + partialTicks;
/*     */   }
/*     */   
/*     */   protected void renderLayers(T entitylivingbaseIn, float p_177093_2_, float p_177093_3_, float partialTicks, float p_177093_5_, float p_177093_6_, float p_177093_7_, float p_177093_8_)
/*     */   {
/* 480 */     for (LayerRenderer<T> layerrenderer : this.layerRenderers) {
/* 481 */       boolean flag = setBrightness(entitylivingbaseIn, partialTicks, layerrenderer.shouldCombineTextures());
/* 482 */       layerrenderer.doRenderLayer(entitylivingbaseIn, p_177093_2_, p_177093_3_, partialTicks, p_177093_5_, 
/* 483 */         p_177093_6_, p_177093_7_, p_177093_8_);
/*     */       
/* 485 */       if (flag) {
/* 486 */         unsetBrightness();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   protected float getDeathMaxRotation(T entityLivingBaseIn) {
/* 492 */     return 90.0F;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected int getColorMultiplier(T entitylivingbaseIn, float lightBrightness, float partialTickTime)
/*     */   {
/* 500 */     return 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void renderName(T entity, double x, double y, double z)
/*     */   {
/* 511 */     if (canRenderName(entity)) {
/* 512 */       double d0 = entity.getDistanceSqToEntity(this.renderManager.livingPlayer);
/* 513 */       float f = entity.isSneaking() ? 32.0F : 64.0F;
/*     */       
/* 515 */       if (d0 < f * f) {
/* 516 */         String s = entity.getDisplayName().getFormattedText();
/* 517 */         float f1 = 0.02666667F;
/* 518 */         GlStateManager.alphaFunc(516, 0.1F);
/*     */         
/* 520 */         if (entity.isSneaking()) {
/* 521 */           EventNametags e = new EventNametags();
/* 522 */           e.call();
/* 523 */           if (e.isCancelled()) {
/* 524 */             return;
/*     */           }
/* 526 */           FontRenderer fontrenderer = getFontRendererFromRenderManager();
/* 527 */           GlStateManager.pushMatrix();
/* 528 */           GlStateManager.translate((float)x, 
/* 529 */             (float)y + entity.height + 0.5F - (entity.isChild() ? entity.height / 2.0F : 0.0F), 
/* 530 */             (float)z);
/* 531 */           GL11.glNormal3f(0.0F, 1.0F, 0.0F);
/* 532 */           GlStateManager.rotate(-RenderManager.playerViewY, 0.0F, 1.0F, 0.0F);
/* 533 */           GlStateManager.rotate(RenderManager.playerViewX, 1.0F, 0.0F, 0.0F);
/* 534 */           GlStateManager.scale(-0.02666667F, -0.02666667F, 0.02666667F);
/* 535 */           GlStateManager.translate(0.0F, 9.374999F, 0.0F);
/* 536 */           GlStateManager.disableLighting();
/* 537 */           GlStateManager.depthMask(false);
/* 538 */           GlStateManager.enableBlend();
/* 539 */           GlStateManager.disableTexture2D();
/* 540 */           GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
/* 541 */           int i = fontrenderer.getStringWidth(s) / 2;
/* 542 */           Tessellator tessellator = Tessellator.getInstance();
/* 543 */           WorldRenderer worldrenderer = tessellator.getWorldRenderer();
/* 544 */           worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
/* 545 */           worldrenderer.pos(-i - 1, -1.0D, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
/* 546 */           worldrenderer.pos(-i - 1, 8.0D, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
/* 547 */           worldrenderer.pos(i + 1, 8.0D, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
/* 548 */           worldrenderer.pos(i + 1, -1.0D, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
/* 549 */           tessellator.draw();
/* 550 */           GlStateManager.enableTexture2D();
/* 551 */           GlStateManager.depthMask(true);
/* 552 */           fontrenderer.drawString(s, -fontrenderer.getStringWidth(s) / 2, 0.0D, 553648127);
/* 553 */           GlStateManager.enableLighting();
/* 554 */           GlStateManager.disableBlend();
/* 555 */           GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/* 556 */           GlStateManager.popMatrix();
/*     */         } else {
/* 558 */           renderOffsetLivingLabel(entity, x, 
/* 559 */             y - (entity.isChild() ? entity.height / 2.0F : 0.0D), z, s, 0.02666667F, d0);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   protected boolean canRenderName(T entity) {
/* 566 */     EntityPlayerSP entityplayersp = Minecraft.getMinecraft().thePlayer;
/*     */     
/* 568 */     if (((entity instanceof EntityPlayer)) && (entity != entityplayersp)) {
/* 569 */       Team team = entity.getTeam();
/* 570 */       Team team1 = entityplayersp.getTeam();
/*     */       
/* 572 */       if (team != null) {
/* 573 */         Team.EnumVisible team$enumvisible = team.getNameTagVisibility();
/*     */         
/* 575 */         switch (team$enumvisible) {
/*     */         case ALWAYS: 
/* 577 */           return true;
/*     */         
/*     */         case HIDE_FOR_OTHER_TEAMS: 
/* 580 */           return false;
/*     */         
/*     */         case HIDE_FOR_OWN_TEAM: 
/* 583 */           return (team1 == null) || (team.isSameTeam(team1));
/*     */         
/*     */         case NEVER: 
/* 586 */           return (team1 == null) || (!team.isSameTeam(team1));
/*     */         }
/*     */         
/* 589 */         return true;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 594 */     return (Minecraft.isGuiEnabled()) && (entity != this.renderManager.livingPlayer) && 
/* 595 */       (!entity.isInvisibleToPlayer(entityplayersp)) && (entity.riddenByEntity == null);
/*     */   }
/*     */   
/*     */   public void setRenderOutlines(boolean renderOutlinesIn) {
/* 599 */     this.renderOutlines = renderOutlinesIn;
/*     */   }
/*     */   
/*     */   static {
/* 603 */     int[] aint = field_177096_e.getTextureData();
/*     */     
/* 605 */     for (int i = 0; i < 256; i++) {
/* 606 */       aint[i] = -1;
/*     */     }
/*     */     
/* 609 */     field_177096_e.updateDynamicTexture();
/*     */   }
/*     */   
/*     */   public void transformHeldFull3DItemLayer() {}
/*     */   
/*     */   protected void preRenderCallback(T entitylivingbaseIn, float partialTickTime) {}
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\renderer\entity\RendererLivingEntity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */