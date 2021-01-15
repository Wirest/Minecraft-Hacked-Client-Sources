/*     */ package optfine;
/*     */ 
/*     */ import java.awt.Dimension;
/*     */ import java.awt.image.BufferedImage;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.entity.AbstractClientPlayer;
/*     */ import net.minecraft.client.model.ModelBiped;
/*     */ import net.minecraft.client.model.ModelRenderer;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.client.renderer.texture.DynamicTexture;
/*     */ import net.minecraft.client.renderer.texture.TextureManager;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ 
/*     */ public class PlayerItemModel
/*     */ {
/*  16 */   private Dimension textureSize = null;
/*  17 */   private boolean usePlayerTexture = false;
/*  18 */   private PlayerItemRenderer[] modelRenderers = new PlayerItemRenderer[0];
/*  19 */   private ResourceLocation textureLocation = null;
/*  20 */   private BufferedImage textureImage = null;
/*  21 */   private DynamicTexture texture = null;
/*  22 */   private ResourceLocation locationMissing = new ResourceLocation("textures/blocks/wool_colored_red.png");
/*     */   public static final int ATTACH_BODY = 0;
/*     */   public static final int ATTACH_HEAD = 1;
/*     */   public static final int ATTACH_LEFT_ARM = 2;
/*     */   public static final int ATTACH_RIGHT_ARM = 3;
/*     */   public static final int ATTACH_LEFT_LEG = 4;
/*     */   public static final int ATTACH_RIGHT_LEG = 5;
/*     */   public static final int ATTACH_CAPE = 6;
/*     */   
/*     */   public PlayerItemModel(Dimension p_i49_1_, boolean p_i49_2_, PlayerItemRenderer[] p_i49_3_)
/*     */   {
/*  33 */     this.textureSize = p_i49_1_;
/*  34 */     this.usePlayerTexture = p_i49_2_;
/*  35 */     this.modelRenderers = p_i49_3_;
/*     */   }
/*     */   
/*     */   public void render(ModelBiped p_render_1_, AbstractClientPlayer p_render_2_, float p_render_3_, float p_render_4_)
/*     */   {
/*  40 */     TextureManager texturemanager = Config.getTextureManager();
/*     */     
/*  42 */     if (this.usePlayerTexture)
/*     */     {
/*  44 */       texturemanager.bindTexture(p_render_2_.getLocationSkin());
/*     */     }
/*  46 */     else if (this.textureLocation != null)
/*     */     {
/*  48 */       if ((this.texture == null) && (this.textureImage != null))
/*     */       {
/*  50 */         this.texture = new DynamicTexture(this.textureImage);
/*  51 */         Minecraft.getMinecraft().getTextureManager().loadTexture(this.textureLocation, this.texture);
/*     */       }
/*     */       
/*  54 */       texturemanager.bindTexture(this.textureLocation);
/*     */     }
/*     */     else
/*     */     {
/*  58 */       texturemanager.bindTexture(this.locationMissing);
/*     */     }
/*     */     
/*  61 */     for (int i = 0; i < this.modelRenderers.length; i++)
/*     */     {
/*  63 */       PlayerItemRenderer playeritemrenderer = this.modelRenderers[i];
/*  64 */       GlStateManager.pushMatrix();
/*     */       
/*  66 */       if (p_render_2_.isSneaking())
/*     */       {
/*  68 */         GlStateManager.translate(0.0F, 0.2F, 0.0F);
/*     */       }
/*     */       
/*  71 */       playeritemrenderer.render(p_render_1_, p_render_3_);
/*  72 */       GlStateManager.popMatrix();
/*     */     }
/*     */   }
/*     */   
/*     */   public static ModelRenderer getAttachModel(ModelBiped p_getAttachModel_0_, int p_getAttachModel_1_)
/*     */   {
/*  78 */     switch (p_getAttachModel_1_)
/*     */     {
/*     */     case 0: 
/*  81 */       return p_getAttachModel_0_.bipedBody;
/*     */     
/*     */     case 1: 
/*  84 */       return p_getAttachModel_0_.bipedHead;
/*     */     
/*     */     case 2: 
/*  87 */       return p_getAttachModel_0_.bipedLeftArm;
/*     */     
/*     */     case 3: 
/*  90 */       return p_getAttachModel_0_.bipedRightArm;
/*     */     
/*     */     case 4: 
/*  93 */       return p_getAttachModel_0_.bipedLeftLeg;
/*     */     
/*     */     case 5: 
/*  96 */       return p_getAttachModel_0_.bipedRightLeg;
/*     */     }
/*     */     
/*  99 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */   public BufferedImage getTextureImage()
/*     */   {
/* 105 */     return this.textureImage;
/*     */   }
/*     */   
/*     */   public void setTextureImage(BufferedImage p_setTextureImage_1_)
/*     */   {
/* 110 */     this.textureImage = p_setTextureImage_1_;
/*     */   }
/*     */   
/*     */   public DynamicTexture getTexture()
/*     */   {
/* 115 */     return this.texture;
/*     */   }
/*     */   
/*     */   public ResourceLocation getTextureLocation()
/*     */   {
/* 120 */     return this.textureLocation;
/*     */   }
/*     */   
/*     */   public void setTextureLocation(ResourceLocation p_setTextureLocation_1_)
/*     */   {
/* 125 */     this.textureLocation = p_setTextureLocation_1_;
/*     */   }
/*     */   
/*     */   public boolean isUsePlayerTexture()
/*     */   {
/* 130 */     return this.usePlayerTexture;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\optfine\PlayerItemModel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */