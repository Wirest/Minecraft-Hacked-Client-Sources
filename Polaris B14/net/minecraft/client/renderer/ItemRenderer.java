/*     */ package net.minecraft.client.renderer;
/*     */ 
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.entity.AbstractClientPlayer;
/*     */ import net.minecraft.client.entity.EntityPlayerSP;
/*     */ import net.minecraft.client.multiplayer.WorldClient;
/*     */ import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
/*     */ import net.minecraft.client.renderer.entity.Render;
/*     */ import net.minecraft.client.renderer.entity.RenderItem;
/*     */ import net.minecraft.client.renderer.entity.RenderManager;
/*     */ import net.minecraft.client.renderer.entity.RenderPlayer;
/*     */ import net.minecraft.client.renderer.texture.TextureAtlasSprite;
/*     */ import net.minecraft.client.renderer.texture.TextureManager;
/*     */ import net.minecraft.client.renderer.texture.TextureMap;
/*     */ import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.entity.player.InventoryPlayer;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.ItemMap;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.EnumWorldBlockLayer;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import rip.jutting.polaris.Polaris;
/*     */ import rip.jutting.polaris.module.Module;
/*     */ 
/*     */ public class ItemRenderer
/*     */ {
/*  33 */   private static final ResourceLocation RES_MAP_BACKGROUND = new ResourceLocation("textures/map/map_background.png");
/*  34 */   private static final ResourceLocation RES_UNDERWATER_OVERLAY = new ResourceLocation("textures/misc/underwater.png");
/*     */   
/*     */ 
/*     */   private final Minecraft mc;
/*     */   
/*     */   private ItemStack itemToRender;
/*     */   
/*     */   private float equippedProgress;
/*     */   
/*     */   private float prevEquippedProgress;
/*     */   
/*     */   private final RenderManager renderManager;
/*     */   
/*     */   private final RenderItem itemRenderer;
/*     */   
/*  49 */   private int equippedItemSlot = -1;
/*     */   
/*     */   public ItemRenderer(Minecraft mcIn) {
/*  52 */     this.mc = mcIn;
/*  53 */     this.renderManager = mcIn.getRenderManager();
/*  54 */     this.itemRenderer = mcIn.getRenderItem();
/*     */   }
/*     */   
/*     */   public void renderItem(EntityLivingBase entityIn, ItemStack heldStack, ItemCameraTransforms.TransformType transform)
/*     */   {
/*  59 */     if (heldStack != null) {
/*  60 */       net.minecraft.item.Item item = heldStack.getItem();
/*  61 */       Block block = Block.getBlockFromItem(item);
/*  62 */       GlStateManager.pushMatrix();
/*     */       
/*  64 */       if (this.itemRenderer.shouldRenderItemIn3D(heldStack)) {
/*  65 */         GlStateManager.scale(2.0F, 2.0F, 2.0F);
/*     */         
/*  67 */         if (isBlockTranslucent(block)) {
/*  68 */           GlStateManager.depthMask(false);
/*     */         }
/*     */       }
/*     */       
/*  72 */       this.itemRenderer.renderItemModelForEntity(heldStack, entityIn, transform);
/*     */       
/*  74 */       if (isBlockTranslucent(block)) {
/*  75 */         GlStateManager.depthMask(true);
/*     */       }
/*     */       
/*  78 */       GlStateManager.popMatrix();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private boolean isBlockTranslucent(Block blockIn)
/*     */   {
/*  86 */     return (blockIn != null) && (blockIn.getBlockLayer() == EnumWorldBlockLayer.TRANSLUCENT);
/*     */   }
/*     */   
/*     */   private void func_178101_a(float angle, float p_178101_2_) {
/*  90 */     GlStateManager.pushMatrix();
/*  91 */     GlStateManager.rotate(angle, 1.0F, 0.0F, 0.0F);
/*  92 */     GlStateManager.rotate(p_178101_2_, 0.0F, 1.0F, 0.0F);
/*  93 */     RenderHelper.enableStandardItemLighting();
/*  94 */     GlStateManager.popMatrix();
/*     */   }
/*     */   
/*     */   private void func_178109_a(AbstractClientPlayer clientPlayer) {
/*  98 */     int i = this.mc.theWorld.getCombinedLight(new BlockPos(clientPlayer.posX, 
/*  99 */       clientPlayer.posY + clientPlayer.getEyeHeight(), clientPlayer.posZ), 0);
/* 100 */     float f = i & 0xFFFF;
/* 101 */     float f1 = i >> 16;
/* 102 */     OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, f, f1);
/*     */   }
/*     */   
/*     */   private void func_178110_a(EntityPlayerSP entityplayerspIn, float partialTicks) {
/* 106 */     float f = entityplayerspIn.prevRenderArmPitch + 
/* 107 */       (entityplayerspIn.renderArmPitch - entityplayerspIn.prevRenderArmPitch) * partialTicks;
/* 108 */     float f1 = entityplayerspIn.prevRenderArmYaw + 
/* 109 */       (entityplayerspIn.renderArmYaw - entityplayerspIn.prevRenderArmYaw) * partialTicks;
/* 110 */     GlStateManager.rotate((entityplayerspIn.rotationPitch - f) * 0.1F, 1.0F, 0.0F, 0.0F);
/* 111 */     GlStateManager.rotate((entityplayerspIn.rotationYaw - f1) * 0.1F, 0.0F, 1.0F, 0.0F);
/*     */   }
/*     */   
/*     */   private float func_178100_c(float p_178100_1_) {
/* 115 */     float f = 1.0F - p_178100_1_ / 45.0F + 0.1F;
/* 116 */     f = MathHelper.clamp_float(f, 0.0F, 1.0F);
/* 117 */     f = -MathHelper.cos(f * 3.1415927F) * 0.5F + 0.5F;
/* 118 */     return f;
/*     */   }
/*     */   
/*     */   private void renderRightArm(RenderPlayer renderPlayerIn) {
/* 122 */     GlStateManager.pushMatrix();
/* 123 */     GlStateManager.rotate(54.0F, 0.0F, 1.0F, 0.0F);
/* 124 */     GlStateManager.rotate(64.0F, 1.0F, 0.0F, 0.0F);
/* 125 */     GlStateManager.rotate(-62.0F, 0.0F, 0.0F, 1.0F);
/* 126 */     GlStateManager.translate(0.25F, -0.85F, 0.75F);
/* 127 */     renderPlayerIn.renderRightArm(this.mc.thePlayer);
/* 128 */     GlStateManager.popMatrix();
/*     */   }
/*     */   
/*     */   private void renderLeftArm(RenderPlayer renderPlayerIn) {
/* 132 */     GlStateManager.pushMatrix();
/* 133 */     GlStateManager.rotate(92.0F, 0.0F, 1.0F, 0.0F);
/* 134 */     GlStateManager.rotate(45.0F, 1.0F, 0.0F, 0.0F);
/* 135 */     GlStateManager.rotate(41.0F, 0.0F, 0.0F, 1.0F);
/* 136 */     GlStateManager.translate(-0.3F, -1.1F, 0.45F);
/* 137 */     renderPlayerIn.renderLeftArm(this.mc.thePlayer);
/* 138 */     GlStateManager.popMatrix();
/*     */   }
/*     */   
/*     */   private void renderPlayerArms(AbstractClientPlayer clientPlayer) {
/* 142 */     this.mc.getTextureManager().bindTexture(clientPlayer.getLocationSkin());
/* 143 */     Render<AbstractClientPlayer> render = this.renderManager
/* 144 */       .getEntityRenderObject(this.mc.thePlayer);
/* 145 */     RenderPlayer renderplayer = (RenderPlayer)render;
/*     */     
/* 147 */     if (!clientPlayer.isInvisible()) {
/* 148 */       GlStateManager.disableCull();
/* 149 */       renderRightArm(renderplayer);
/* 150 */       renderLeftArm(renderplayer);
/* 151 */       GlStateManager.enableCull();
/*     */     }
/*     */   }
/*     */   
/*     */   private void renderItemMap(AbstractClientPlayer clientPlayer, float p_178097_2_, float p_178097_3_, float p_178097_4_)
/*     */   {
/* 157 */     float f = -0.4F * MathHelper.sin(MathHelper.sqrt_float(p_178097_4_) * 3.1415927F);
/* 158 */     float f1 = 0.2F * MathHelper.sin(MathHelper.sqrt_float(p_178097_4_) * 3.1415927F * 2.0F);
/* 159 */     float f2 = -0.2F * MathHelper.sin(p_178097_4_ * 3.1415927F);
/* 160 */     GlStateManager.translate(f, f1, f2);
/* 161 */     float f3 = func_178100_c(p_178097_2_);
/* 162 */     GlStateManager.translate(0.0F, 0.04F, -0.72F);
/* 163 */     GlStateManager.translate(0.0F, p_178097_3_ * -1.2F, 0.0F);
/* 164 */     GlStateManager.translate(0.0F, f3 * -0.5F, 0.0F);
/* 165 */     GlStateManager.rotate(90.0F, 0.0F, 1.0F, 0.0F);
/* 166 */     GlStateManager.rotate(f3 * -85.0F, 0.0F, 0.0F, 1.0F);
/* 167 */     GlStateManager.rotate(0.0F, 1.0F, 0.0F, 0.0F);
/* 168 */     renderPlayerArms(clientPlayer);
/* 169 */     float f4 = MathHelper.sin(p_178097_4_ * p_178097_4_ * 3.1415927F);
/* 170 */     float f5 = MathHelper.sin(MathHelper.sqrt_float(p_178097_4_) * 3.1415927F);
/* 171 */     GlStateManager.rotate(f4 * -20.0F, 0.0F, 1.0F, 0.0F);
/* 172 */     GlStateManager.rotate(f5 * -20.0F, 0.0F, 0.0F, 1.0F);
/* 173 */     GlStateManager.rotate(f5 * -80.0F, 1.0F, 0.0F, 0.0F);
/* 174 */     GlStateManager.scale(0.38F, 0.38F, 0.38F);
/* 175 */     GlStateManager.rotate(90.0F, 0.0F, 1.0F, 0.0F);
/* 176 */     GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
/* 177 */     GlStateManager.rotate(0.0F, 1.0F, 0.0F, 0.0F);
/* 178 */     GlStateManager.translate(-1.0F, -1.0F, 0.0F);
/* 179 */     GlStateManager.scale(0.015625F, 0.015625F, 0.015625F);
/* 180 */     this.mc.getTextureManager().bindTexture(RES_MAP_BACKGROUND);
/* 181 */     Tessellator tessellator = Tessellator.getInstance();
/* 182 */     WorldRenderer worldrenderer = tessellator.getWorldRenderer();
/* 183 */     org.lwjgl.opengl.GL11.glNormal3f(0.0F, 0.0F, -1.0F);
/* 184 */     worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
/* 185 */     worldrenderer.pos(-7.0D, 135.0D, 0.0D).tex(0.0D, 1.0D).endVertex();
/* 186 */     worldrenderer.pos(135.0D, 135.0D, 0.0D).tex(1.0D, 1.0D).endVertex();
/* 187 */     worldrenderer.pos(135.0D, -7.0D, 0.0D).tex(1.0D, 0.0D).endVertex();
/* 188 */     worldrenderer.pos(-7.0D, -7.0D, 0.0D).tex(0.0D, 0.0D).endVertex();
/* 189 */     tessellator.draw();
/* 190 */     net.minecraft.world.storage.MapData mapdata = Items.filled_map.getMapData(this.itemToRender, this.mc.theWorld);
/*     */     
/* 192 */     if (mapdata != null) {
/* 193 */       this.mc.entityRenderer.getMapItemRenderer().renderMap(mapdata, false);
/*     */     }
/*     */   }
/*     */   
/*     */   private void func_178095_a(AbstractClientPlayer clientPlayer, float p_178095_2_, float p_178095_3_) {
/* 198 */     float f = -0.3F * MathHelper.sin(MathHelper.sqrt_float(p_178095_3_) * 3.1415927F);
/* 199 */     float f1 = 0.4F * MathHelper.sin(MathHelper.sqrt_float(p_178095_3_) * 3.1415927F * 2.0F);
/* 200 */     float f2 = -0.4F * MathHelper.sin(p_178095_3_ * 3.1415927F);
/* 201 */     GlStateManager.translate(f, f1, f2);
/* 202 */     GlStateManager.translate(0.64000005F, -0.6F, -0.71999997F);
/* 203 */     GlStateManager.translate(0.0F, p_178095_2_ * -0.6F, 0.0F);
/* 204 */     GlStateManager.rotate(45.0F, 0.0F, 1.0F, 0.0F);
/* 205 */     float f3 = MathHelper.sin(p_178095_3_ * p_178095_3_ * 3.1415927F);
/* 206 */     float f4 = MathHelper.sin(MathHelper.sqrt_float(p_178095_3_) * 3.1415927F);
/* 207 */     GlStateManager.rotate(f4 * 70.0F, 0.0F, 1.0F, 0.0F);
/* 208 */     GlStateManager.rotate(f3 * -20.0F, 0.0F, 0.0F, 1.0F);
/* 209 */     this.mc.getTextureManager().bindTexture(clientPlayer.getLocationSkin());
/* 210 */     GlStateManager.translate(-1.0F, 3.6F, 3.5F);
/* 211 */     GlStateManager.rotate(120.0F, 0.0F, 0.0F, 1.0F);
/* 212 */     GlStateManager.rotate(200.0F, 1.0F, 0.0F, 0.0F);
/* 213 */     GlStateManager.rotate(-135.0F, 0.0F, 1.0F, 0.0F);
/* 214 */     GlStateManager.scale(1.0F, 1.0F, 1.0F);
/* 215 */     GlStateManager.translate(5.6F, 0.0F, 0.0F);
/* 216 */     Render<AbstractClientPlayer> render = this.renderManager
/* 217 */       .getEntityRenderObject(this.mc.thePlayer);
/* 218 */     GlStateManager.disableCull();
/* 219 */     RenderPlayer renderplayer = (RenderPlayer)render;
/* 220 */     renderplayer.renderRightArm(this.mc.thePlayer);
/* 221 */     GlStateManager.enableCull();
/*     */   }
/*     */   
/*     */   private void func_178105_d(float p_178105_1_) {
/* 225 */     float f = -0.4F * MathHelper.sin(MathHelper.sqrt_float(p_178105_1_) * 3.1415927F);
/* 226 */     float f1 = 0.2F * MathHelper.sin(MathHelper.sqrt_float(p_178105_1_) * 3.1415927F * 2.0F);
/* 227 */     float f2 = -0.2F * MathHelper.sin(p_178105_1_ * 3.1415927F);
/* 228 */     GlStateManager.translate(f, f1, f2);
/*     */   }
/*     */   
/*     */   private void func_178104_a(AbstractClientPlayer clientPlayer, float p_178104_2_) {
/* 232 */     float f = clientPlayer.getItemInUseCount() - p_178104_2_ + 1.0F;
/* 233 */     float f1 = f / this.itemToRender.getMaxItemUseDuration();
/* 234 */     float f2 = MathHelper.abs(MathHelper.cos(f / 4.0F * 3.1415927F) * 0.1F);
/*     */     
/* 236 */     if (f1 >= 0.8F) {
/* 237 */       f2 = 0.0F;
/*     */     }
/*     */     
/* 240 */     GlStateManager.translate(0.0F, f2, 0.0F);
/* 241 */     float f3 = 1.0F - (float)Math.pow(f1, 27.0D);
/* 242 */     GlStateManager.translate(f3 * 0.6F, f3 * -0.5F, f3 * 0.0F);
/* 243 */     GlStateManager.rotate(f3 * 90.0F, 0.0F, 1.0F, 0.0F);
/* 244 */     GlStateManager.rotate(f3 * 10.0F, 1.0F, 0.0F, 0.0F);
/* 245 */     GlStateManager.rotate(f3 * 30.0F, 0.0F, 0.0F, 1.0F);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private void transformFirstPersonItem(float equipProgress, float swingProgress)
/*     */   {
/* 253 */     GlStateManager.translate(0.56F, -0.52F, -0.71999997F);
/* 254 */     GlStateManager.translate(0.0F, equipProgress * -0.6F, 0.0F);
/* 255 */     GlStateManager.rotate(45.0F, 0.0F, 1.0F, 0.0F);
/* 256 */     float f = MathHelper.sin(swingProgress * swingProgress * 3.1415927F);
/* 257 */     float f1 = MathHelper.sin(MathHelper.sqrt_float(swingProgress) * 3.1415927F);
/* 258 */     GlStateManager.rotate(f * -20.0F, 0.0F, 1.0F, 0.0F);
/* 259 */     GlStateManager.rotate(f1 * -20.0F, 0.0F, 0.0F, 1.0F);
/* 260 */     GlStateManager.rotate(f1 * -80.0F, 1.0F, 0.0F, 0.0F);
/* 261 */     GlStateManager.scale(0.4F, 0.4F, 0.4F);
/*     */   }
/*     */   
/*     */   public void coolAnimations(float equipProgress, float swingProgress) {
/* 265 */     GlStateManager.translate(0.56F, -0.52F, -0.71999997F);
/* 266 */     GlStateManager.translate(0.0F, equipProgress * -0.6F, 0.0F);
/* 267 */     GlStateManager.rotate(45.0F, 0.0F, 1.0F, 0.0F);
/* 268 */     float f = MathHelper.sin(swingProgress * swingProgress * 3.1415927F);
/* 269 */     float f2 = MathHelper.sin(MathHelper.sqrt_float(swingProgress) * 3.1415927F);
/* 270 */     GlStateManager.rotate(f * -15.0F, 15.0F, 1.0F, 1.0F);
/* 271 */     GlStateManager.rotate(f2 * -15.0F, 15.0F, 1.0F, 0.0F);
/* 272 */     GlStateManager.scale(0.4F, 0.4F, 0.4F);
/*     */   }
/*     */   
/*     */   public void consleAnimations(float equipProgress, float swingProgress) {
/* 276 */     GlStateManager.translate(0.56F, -0.52F, -0.71999997F);
/* 277 */     GlStateManager.translate(0.0F, equipProgress * -0.6F, 0.0F);
/* 278 */     GlStateManager.rotate(45.0F, 0.0F, 1.0F, 0.0F);
/* 279 */     float f = MathHelper.sin(swingProgress * swingProgress * 3.1415927F);
/* 280 */     float f2 = MathHelper.sin(MathHelper.sqrt_float(swingProgress) * 3.1415927F);
/* 281 */     GlStateManager.rotate(f * -0.0F, 0.0F, 0.0F, 0.0F);
/* 282 */     GlStateManager.rotate(f2 * -50.0F, 0.0F, 0.0F, 0.0F);
/* 283 */     GlStateManager.rotate(f2 * -65.0F, 1.0F, 0.0F, 0.0F);
/* 284 */     GlStateManager.scale(0.4F, 0.4F, 0.4F);
/*     */   }
/*     */   
/*     */   private void func_178098_a(float p_178098_1_, AbstractClientPlayer clientPlayer) {
/* 288 */     GlStateManager.rotate(-18.0F, 0.0F, 0.0F, 1.0F);
/* 289 */     GlStateManager.rotate(-12.0F, 0.0F, 1.0F, 0.0F);
/* 290 */     GlStateManager.rotate(-8.0F, 1.0F, 0.0F, 0.0F);
/* 291 */     GlStateManager.translate(-0.9F, 0.2F, 0.0F);
/* 292 */     float f = this.itemToRender.getMaxItemUseDuration() - (
/* 293 */       clientPlayer.getItemInUseCount() - p_178098_1_ + 1.0F);
/* 294 */     float f1 = f / 20.0F;
/* 295 */     f1 = (f1 * f1 + f1 * 2.0F) / 3.0F;
/*     */     
/* 297 */     if (f1 > 1.0F) {
/* 298 */       f1 = 1.0F;
/*     */     }
/*     */     
/* 301 */     if (f1 > 0.1F) {
/* 302 */       float f2 = MathHelper.sin((f - 0.1F) * 1.3F);
/* 303 */       float f3 = f1 - 0.1F;
/* 304 */       float f4 = f2 * f3;
/* 305 */       GlStateManager.translate(f4 * 0.0F, f4 * 0.01F, f4 * 0.0F);
/*     */     }
/*     */     
/* 308 */     GlStateManager.translate(f1 * 0.0F, f1 * 0.0F, f1 * 0.1F);
/* 309 */     GlStateManager.scale(1.0F, 1.0F, 1.0F + f1 * 0.2F);
/*     */   }
/*     */   
/*     */   private void func_178103_d() {
/* 313 */     GlStateManager.translate(-0.5F, 0.2F, 0.0F);
/* 314 */     GlStateManager.rotate(30.0F, 0.0F, 1.0F, 0.0F);
/* 315 */     GlStateManager.rotate(-80.0F, 1.0F, 0.0F, 0.0F);
/* 316 */     GlStateManager.rotate(60.0F, 0.0F, 1.0F, 0.0F);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void renderItemInFirstPerson(float partialTicks)
/*     */   {
/* 324 */     float f = 1.0F - (this.prevEquippedProgress + (this.equippedProgress - this.prevEquippedProgress) * partialTicks);
/* 325 */     AbstractClientPlayer abstractclientplayer = this.mc.thePlayer;
/* 326 */     float f2 = abstractclientplayer.getSwingProgress(partialTicks);
/* 327 */     float f3 = abstractclientplayer.prevRotationPitch + (abstractclientplayer.rotationPitch - abstractclientplayer.prevRotationPitch) * partialTicks;
/* 328 */     float f4 = abstractclientplayer.prevRotationYaw + (abstractclientplayer.rotationYaw - abstractclientplayer.prevRotationYaw) * partialTicks;
/* 329 */     func_178101_a(f3, f4);
/* 330 */     func_178109_a(abstractclientplayer);
/* 331 */     func_178110_a((EntityPlayerSP)abstractclientplayer, partialTicks);
/* 332 */     GlStateManager.enableRescaleNormal();
/* 333 */     GlStateManager.pushMatrix();
/* 334 */     if (this.itemToRender != null) {
/* 335 */       if (this.itemToRender.getItem() == Items.filled_map) {
/* 336 */         renderItemMap(abstractclientplayer, f3, f, f2);
/*     */       }
/* 338 */       else if (abstractclientplayer.getItemInUseCount() > 0) {
/* 339 */         net.minecraft.item.EnumAction enumaction = this.itemToRender.getItemUseAction();
/* 340 */         switch (enumaction) {
/*     */         case BLOCK: 
/* 342 */           transformFirstPersonItem(f, 0.0F);
/* 343 */           break;
/*     */         
/*     */         case BOW: 
/*     */         case DRINK: 
/* 347 */           func_178104_a(abstractclientplayer, partialTicks);
/* 348 */           transformFirstPersonItem(f, 0.0F);
/* 349 */           break;
/*     */         
/*     */         case EAT: 
/* 352 */           if (Polaris.instance.moduleManager.getModuleByName("Animations").isToggled()) {
/* 353 */             coolAnimations(-0.1F, f2);
/*     */           }
/*     */           else {
/* 356 */             transformFirstPersonItem(-0.05F, f2);
/*     */           }
/* 358 */           func_178103_d();
/* 359 */           break;
/*     */         
/*     */         case NONE: 
/* 362 */           transformFirstPersonItem(f, 0.0F);
/* 363 */           func_178098_a(partialTicks, abstractclientplayer);
/*     */         }
/*     */         
/*     */       }
/*     */       else
/*     */       {
/* 369 */         func_178105_d(f2);
/* 370 */         transformFirstPersonItem(f, f2);
/*     */       }
/* 372 */       renderItem(abstractclientplayer, this.itemToRender, ItemCameraTransforms.TransformType.FIRST_PERSON);
/*     */     }
/* 374 */     else if (!abstractclientplayer.isInvisible()) {
/* 375 */       func_178095_a(abstractclientplayer, f, f2);
/*     */     }
/* 377 */     GlStateManager.popMatrix();
/* 378 */     GlStateManager.disableRescaleNormal();
/* 379 */     RenderHelper.disableStandardItemLighting();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void renderOverlays(float partialTicks)
/*     */   {
/*     */     
/*     */     
/* 388 */     if (this.mc.thePlayer.isEntityInsideOpaqueBlock()) {
/* 389 */       IBlockState iblockstate = this.mc.theWorld.getBlockState(new BlockPos(this.mc.thePlayer));
/* 390 */       EntityPlayer entityplayer = this.mc.thePlayer;
/*     */       
/* 392 */       for (int i = 0; i < 8; i++) {
/* 393 */         double d0 = entityplayer.posX + ((i >> 0) % 2 - 0.5F) * entityplayer.width * 0.8F;
/* 394 */         double d1 = entityplayer.posY + ((i >> 1) % 2 - 0.5F) * 0.1F;
/* 395 */         double d2 = entityplayer.posZ + ((i >> 2) % 2 - 0.5F) * entityplayer.width * 0.8F;
/* 396 */         BlockPos blockpos = new BlockPos(d0, d1 + entityplayer.getEyeHeight(), d2);
/* 397 */         IBlockState iblockstate1 = this.mc.theWorld.getBlockState(blockpos);
/*     */         
/* 399 */         if (iblockstate1.getBlock().isVisuallyOpaque()) {
/* 400 */           iblockstate = iblockstate1;
/*     */         }
/*     */       }
/*     */       
/* 404 */       if (iblockstate.getBlock().getRenderType() != -1) {
/* 405 */         func_178108_a(partialTicks, 
/* 406 */           this.mc.getBlockRendererDispatcher().getBlockModelShapes().getTexture(iblockstate));
/*     */       }
/*     */     }
/*     */     
/* 410 */     if (!this.mc.thePlayer.isSpectator()) {
/* 411 */       if (this.mc.thePlayer.isInsideOfMaterial(net.minecraft.block.material.Material.water)) {
/* 412 */         renderWaterOverlayTexture(partialTicks);
/*     */       }
/*     */       
/* 415 */       if (this.mc.thePlayer.isBurning()) {
/* 416 */         renderFireInFirstPerson(partialTicks);
/*     */       }
/*     */     }
/*     */     
/* 420 */     GlStateManager.enableAlpha();
/*     */   }
/*     */   
/*     */   private void func_178108_a(float p_178108_1_, TextureAtlasSprite p_178108_2_) {
/* 424 */     this.mc.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
/* 425 */     Tessellator tessellator = Tessellator.getInstance();
/* 426 */     WorldRenderer worldrenderer = tessellator.getWorldRenderer();
/* 427 */     float f = 0.1F;
/* 428 */     GlStateManager.color(0.1F, 0.1F, 0.1F, 0.5F);
/* 429 */     GlStateManager.pushMatrix();
/* 430 */     float f1 = -1.0F;
/* 431 */     float f2 = 1.0F;
/* 432 */     float f3 = -1.0F;
/* 433 */     float f4 = 1.0F;
/* 434 */     float f5 = -0.5F;
/* 435 */     float f6 = p_178108_2_.getMinU();
/* 436 */     float f7 = p_178108_2_.getMaxU();
/* 437 */     float f8 = p_178108_2_.getMinV();
/* 438 */     float f9 = p_178108_2_.getMaxV();
/* 439 */     worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
/* 440 */     worldrenderer.pos(-1.0D, -1.0D, -0.5D).tex(f7, f9).endVertex();
/* 441 */     worldrenderer.pos(1.0D, -1.0D, -0.5D).tex(f6, f9).endVertex();
/* 442 */     worldrenderer.pos(1.0D, 1.0D, -0.5D).tex(f6, f8).endVertex();
/* 443 */     worldrenderer.pos(-1.0D, 1.0D, -0.5D).tex(f7, f8).endVertex();
/* 444 */     tessellator.draw();
/* 445 */     GlStateManager.popMatrix();
/* 446 */     GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void renderWaterOverlayTexture(float p_78448_1_)
/*     */   {
/* 455 */     this.mc.getTextureManager().bindTexture(RES_UNDERWATER_OVERLAY);
/* 456 */     Tessellator tessellator = Tessellator.getInstance();
/* 457 */     WorldRenderer worldrenderer = tessellator.getWorldRenderer();
/* 458 */     float f = this.mc.thePlayer.getBrightness(p_78448_1_);
/* 459 */     GlStateManager.color(f, f, f, 0.5F);
/* 460 */     GlStateManager.enableBlend();
/* 461 */     GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
/* 462 */     GlStateManager.pushMatrix();
/* 463 */     float f1 = 4.0F;
/* 464 */     float f2 = -1.0F;
/* 465 */     float f3 = 1.0F;
/* 466 */     float f4 = -1.0F;
/* 467 */     float f5 = 1.0F;
/* 468 */     float f6 = -0.5F;
/* 469 */     float f7 = -this.mc.thePlayer.rotationYaw / 64.0F;
/* 470 */     float f8 = this.mc.thePlayer.rotationPitch / 64.0F;
/* 471 */     worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
/* 472 */     worldrenderer.pos(-1.0D, -1.0D, -0.5D).tex(4.0F + f7, 4.0F + f8).endVertex();
/* 473 */     worldrenderer.pos(1.0D, -1.0D, -0.5D).tex(0.0F + f7, 4.0F + f8).endVertex();
/* 474 */     worldrenderer.pos(1.0D, 1.0D, -0.5D).tex(0.0F + f7, 0.0F + f8).endVertex();
/* 475 */     worldrenderer.pos(-1.0D, 1.0D, -0.5D).tex(4.0F + f7, 0.0F + f8).endVertex();
/* 476 */     tessellator.draw();
/* 477 */     GlStateManager.popMatrix();
/* 478 */     GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/* 479 */     GlStateManager.disableBlend();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private void renderFireInFirstPerson(float p_78442_1_)
/*     */   {
/* 486 */     Tessellator tessellator = Tessellator.getInstance();
/* 487 */     WorldRenderer worldrenderer = tessellator.getWorldRenderer();
/* 488 */     GlStateManager.color(1.0F, 1.0F, 1.0F, 0.9F);
/* 489 */     GlStateManager.depthFunc(519);
/* 490 */     GlStateManager.depthMask(false);
/* 491 */     GlStateManager.enableBlend();
/* 492 */     GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
/* 493 */     float f = 1.0F;
/*     */     
/* 495 */     for (int i = 0; i < 2; i++) {
/* 496 */       GlStateManager.pushMatrix();
/* 497 */       TextureAtlasSprite textureatlassprite = this.mc.getTextureMapBlocks()
/* 498 */         .getAtlasSprite("minecraft:blocks/fire_layer_1");
/* 499 */       this.mc.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
/* 500 */       float f1 = textureatlassprite.getMinU();
/* 501 */       float f2 = textureatlassprite.getMaxU();
/* 502 */       float f3 = textureatlassprite.getMinV();
/* 503 */       float f4 = textureatlassprite.getMaxV();
/* 504 */       float f5 = (0.0F - f) / 2.0F;
/* 505 */       float f6 = f5 + f;
/* 506 */       float f7 = 0.0F - f / 2.0F;
/* 507 */       float f8 = f7 + f;
/* 508 */       float f9 = -0.5F;
/* 509 */       GlStateManager.translate(-(i * 2 - 1) * 0.24F, -0.3F, 0.0F);
/* 510 */       GlStateManager.rotate((i * 2 - 1) * 10.0F, 0.0F, 1.0F, 0.0F);
/* 511 */       worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
/* 512 */       worldrenderer.pos(f5, f7, f9).tex(f2, f4).endVertex();
/* 513 */       worldrenderer.pos(f6, f7, f9).tex(f1, f4).endVertex();
/* 514 */       worldrenderer.pos(f6, f8, f9).tex(f1, f3).endVertex();
/* 515 */       worldrenderer.pos(f5, f8, f9).tex(f2, f3).endVertex();
/* 516 */       tessellator.draw();
/* 517 */       GlStateManager.popMatrix();
/*     */     }
/*     */     
/* 520 */     GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/* 521 */     GlStateManager.disableBlend();
/* 522 */     GlStateManager.depthMask(true);
/* 523 */     GlStateManager.depthFunc(515);
/*     */   }
/*     */   
/*     */   public void updateEquippedItem() {
/* 527 */     this.prevEquippedProgress = this.equippedProgress;
/* 528 */     EntityPlayer entityplayer = this.mc.thePlayer;
/* 529 */     ItemStack itemstack = entityplayer.inventory.getCurrentItem();
/* 530 */     boolean flag = false;
/*     */     
/* 532 */     if ((this.itemToRender != null) && (itemstack != null)) {
/* 533 */       if (!this.itemToRender.getIsItemStackEqual(itemstack)) {
/* 534 */         flag = true;
/*     */       }
/* 536 */     } else if ((this.itemToRender == null) && (itemstack == null)) {
/* 537 */       flag = false;
/*     */     } else {
/* 539 */       flag = true;
/*     */     }
/*     */     
/* 542 */     float f = 0.4F;
/* 543 */     float f1 = flag ? 0.0F : 1.0F;
/* 544 */     float f2 = MathHelper.clamp_float(f1 - this.equippedProgress, -f, f);
/* 545 */     this.equippedProgress += f2;
/*     */     
/* 547 */     if (this.equippedProgress < 0.1F) {
/* 548 */       this.itemToRender = itemstack;
/* 549 */       this.equippedItemSlot = entityplayer.inventory.currentItem;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void resetEquippedProgress()
/*     */   {
/* 557 */     this.equippedProgress = 0.0F;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void resetEquippedProgress2()
/*     */   {
/* 564 */     this.equippedProgress = 0.0F;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\renderer\ItemRenderer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */