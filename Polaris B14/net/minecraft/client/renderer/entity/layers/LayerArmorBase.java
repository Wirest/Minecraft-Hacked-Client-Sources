/*     */ package net.minecraft.client.renderer.entity.layers;
/*     */ 
/*     */ import java.util.Map;
/*     */ import net.minecraft.client.model.ModelBase;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.client.renderer.entity.RendererLivingEntity;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.item.ItemArmor;
/*     */ import net.minecraft.item.ItemArmor.ArmorMaterial;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ 
/*     */ public abstract class LayerArmorBase<T extends ModelBase> implements LayerRenderer<EntityLivingBase>
/*     */ {
/*  15 */   protected static final ResourceLocation ENCHANTED_ITEM_GLINT_RES = new ResourceLocation("textures/misc/enchanted_item_glint.png");
/*     */   protected T field_177189_c;
/*     */   protected T field_177186_d;
/*     */   private final RendererLivingEntity<?> renderer;
/*  19 */   private float alpha = 1.0F;
/*  20 */   private float colorR = 1.0F;
/*  21 */   private float colorG = 1.0F;
/*  22 */   private float colorB = 1.0F;
/*     */   private boolean field_177193_i;
/*  24 */   private static final Map<String, ResourceLocation> ARMOR_TEXTURE_RES_MAP = com.google.common.collect.Maps.newHashMap();
/*     */   
/*     */   public LayerArmorBase(RendererLivingEntity<?> rendererIn)
/*     */   {
/*  28 */     this.renderer = rendererIn;
/*  29 */     initArmor();
/*     */   }
/*     */   
/*     */   public void doRenderLayer(EntityLivingBase entitylivingbaseIn, float p_177141_2_, float p_177141_3_, float partialTicks, float p_177141_5_, float p_177141_6_, float p_177141_7_, float scale)
/*     */   {
/*  34 */     renderLayer(entitylivingbaseIn, p_177141_2_, p_177141_3_, partialTicks, p_177141_5_, p_177141_6_, p_177141_7_, scale, 4);
/*  35 */     renderLayer(entitylivingbaseIn, p_177141_2_, p_177141_3_, partialTicks, p_177141_5_, p_177141_6_, p_177141_7_, scale, 3);
/*  36 */     renderLayer(entitylivingbaseIn, p_177141_2_, p_177141_3_, partialTicks, p_177141_5_, p_177141_6_, p_177141_7_, scale, 2);
/*  37 */     renderLayer(entitylivingbaseIn, p_177141_2_, p_177141_3_, partialTicks, p_177141_5_, p_177141_6_, p_177141_7_, scale, 1);
/*     */   }
/*     */   
/*     */   public boolean shouldCombineTextures()
/*     */   {
/*  42 */     return false;
/*     */   }
/*     */   
/*     */   private void renderLayer(EntityLivingBase entitylivingbaseIn, float p_177182_2_, float p_177182_3_, float p_177182_4_, float p_177182_5_, float p_177182_6_, float p_177182_7_, float p_177182_8_, int armorSlot)
/*     */   {
/*  47 */     ItemStack itemstack = getCurrentArmor(entitylivingbaseIn, armorSlot);
/*     */     
/*  49 */     if ((itemstack != null) && ((itemstack.getItem() instanceof ItemArmor)))
/*     */     {
/*  51 */       ItemArmor itemarmor = (ItemArmor)itemstack.getItem();
/*  52 */       T t = func_177175_a(armorSlot);
/*  53 */       t.setModelAttributes(this.renderer.getMainModel());
/*  54 */       t.setLivingAnimations(entitylivingbaseIn, p_177182_2_, p_177182_3_, p_177182_4_);
/*  55 */       func_177179_a(t, armorSlot);
/*  56 */       boolean flag = isSlotForLeggings(armorSlot);
/*  57 */       this.renderer.bindTexture(getArmorResource(itemarmor, flag));
/*     */       
/*  59 */       switch (itemarmor.getArmorMaterial())
/*     */       {
/*     */       case CHAIN: 
/*  62 */         int i = itemarmor.getColor(itemstack);
/*  63 */         float f = (i >> 16 & 0xFF) / 255.0F;
/*  64 */         float f1 = (i >> 8 & 0xFF) / 255.0F;
/*  65 */         float f2 = (i & 0xFF) / 255.0F;
/*  66 */         GlStateManager.color(this.colorR * f, this.colorG * f1, this.colorB * f2, this.alpha);
/*  67 */         t.render(entitylivingbaseIn, p_177182_2_, p_177182_3_, p_177182_5_, p_177182_6_, p_177182_7_, p_177182_8_);
/*  68 */         this.renderer.bindTexture(getArmorResource(itemarmor, flag, "overlay"));
/*     */       
/*     */       case DIAMOND: 
/*     */       case GOLD: 
/*     */       case IRON: 
/*     */       case LEATHER: 
/*  74 */         GlStateManager.color(this.colorR, this.colorG, this.colorB, this.alpha);
/*  75 */         t.render(entitylivingbaseIn, p_177182_2_, p_177182_3_, p_177182_5_, p_177182_6_, p_177182_7_, p_177182_8_);
/*     */       }
/*     */       
/*  78 */       if ((!this.field_177193_i) && (itemstack.isItemEnchanted()))
/*     */       {
/*  80 */         func_177183_a(entitylivingbaseIn, t, p_177182_2_, p_177182_3_, p_177182_4_, p_177182_5_, p_177182_6_, p_177182_7_, p_177182_8_);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public ItemStack getCurrentArmor(EntityLivingBase entitylivingbaseIn, int armorSlot)
/*     */   {
/*  88 */     return entitylivingbaseIn.getCurrentArmor(armorSlot - 1);
/*     */   }
/*     */   
/*     */   public T func_177175_a(int p_177175_1_)
/*     */   {
/*  93 */     return isSlotForLeggings(p_177175_1_) ? this.field_177189_c : this.field_177186_d;
/*     */   }
/*     */   
/*     */   private boolean isSlotForLeggings(int armorSlot)
/*     */   {
/*  98 */     return armorSlot == 2;
/*     */   }
/*     */   
/*     */   private void func_177183_a(EntityLivingBase entitylivingbaseIn, T modelbaseIn, float p_177183_3_, float p_177183_4_, float p_177183_5_, float p_177183_6_, float p_177183_7_, float p_177183_8_, float p_177183_9_)
/*     */   {
/* 103 */     float f = entitylivingbaseIn.ticksExisted + p_177183_5_;
/* 104 */     this.renderer.bindTexture(ENCHANTED_ITEM_GLINT_RES);
/* 105 */     GlStateManager.enableBlend();
/* 106 */     GlStateManager.depthFunc(514);
/* 107 */     GlStateManager.depthMask(false);
/* 108 */     float f1 = 0.5F;
/* 109 */     GlStateManager.color(f1, f1, f1, 1.0F);
/*     */     
/* 111 */     for (int i = 0; i < 2; i++)
/*     */     {
/* 113 */       GlStateManager.disableLighting();
/* 114 */       GlStateManager.blendFunc(768, 1);
/* 115 */       float f2 = 0.76F;
/* 116 */       GlStateManager.color(0.5F * f2, 0.25F * f2, 0.8F * f2, 1.0F);
/* 117 */       GlStateManager.matrixMode(5890);
/* 118 */       GlStateManager.loadIdentity();
/* 119 */       float f3 = 0.33333334F;
/* 120 */       GlStateManager.scale(f3, f3, f3);
/* 121 */       GlStateManager.rotate(30.0F - i * 60.0F, 0.0F, 0.0F, 1.0F);
/* 122 */       GlStateManager.translate(0.0F, f * (0.001F + i * 0.003F) * 20.0F, 0.0F);
/* 123 */       GlStateManager.matrixMode(5888);
/* 124 */       modelbaseIn.render(entitylivingbaseIn, p_177183_3_, p_177183_4_, p_177183_6_, p_177183_7_, p_177183_8_, p_177183_9_);
/*     */     }
/*     */     
/* 127 */     GlStateManager.matrixMode(5890);
/* 128 */     GlStateManager.loadIdentity();
/* 129 */     GlStateManager.matrixMode(5888);
/* 130 */     GlStateManager.enableLighting();
/* 131 */     GlStateManager.depthMask(true);
/* 132 */     GlStateManager.depthFunc(515);
/* 133 */     GlStateManager.disableBlend();
/*     */   }
/*     */   
/*     */   private ResourceLocation getArmorResource(ItemArmor p_177181_1_, boolean p_177181_2_)
/*     */   {
/* 138 */     return getArmorResource(p_177181_1_, p_177181_2_, null);
/*     */   }
/*     */   
/*     */   private ResourceLocation getArmorResource(ItemArmor p_177178_1_, boolean p_177178_2_, String p_177178_3_)
/*     */   {
/* 143 */     String s = String.format("textures/models/armor/%s_layer_%d%s.png", new Object[] { p_177178_1_.getArmorMaterial().getName(), Integer.valueOf(p_177178_2_ ? 2 : 1), p_177178_3_ == null ? "" : String.format("_%s", new Object[] { p_177178_3_ }) });
/* 144 */     ResourceLocation resourcelocation = (ResourceLocation)ARMOR_TEXTURE_RES_MAP.get(s);
/*     */     
/* 146 */     if (resourcelocation == null)
/*     */     {
/* 148 */       resourcelocation = new ResourceLocation(s);
/* 149 */       ARMOR_TEXTURE_RES_MAP.put(s, resourcelocation);
/*     */     }
/*     */     
/* 152 */     return resourcelocation;
/*     */   }
/*     */   
/*     */   protected abstract void initArmor();
/*     */   
/*     */   protected abstract void func_177179_a(T paramT, int paramInt);
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\renderer\entity\layers\LayerArmorBase.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */