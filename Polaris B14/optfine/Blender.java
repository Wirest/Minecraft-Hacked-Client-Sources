/*     */ package optfine;
/*     */ 
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ 
/*     */ public class Blender
/*     */ {
/*     */   public static final int BLEND_ALPHA = 0;
/*     */   public static final int BLEND_ADD = 1;
/*     */   public static final int BLEND_SUBSTRACT = 2;
/*     */   public static final int BLEND_MULTIPLY = 3;
/*     */   public static final int BLEND_DODGE = 4;
/*     */   public static final int BLEND_BURN = 5;
/*     */   public static final int BLEND_SCREEN = 6;
/*     */   public static final int BLEND_REPLACE = 7;
/*     */   public static final int BLEND_DEFAULT = 1;
/*     */   
/*     */   public static int parseBlend(String p_parseBlend_0_)
/*     */   {
/*  19 */     if (p_parseBlend_0_ == null)
/*     */     {
/*  21 */       return 1;
/*     */     }
/*     */     
/*     */ 
/*  25 */     p_parseBlend_0_ = p_parseBlend_0_.toLowerCase().trim();
/*     */     
/*  27 */     if (p_parseBlend_0_.equals("alpha"))
/*     */     {
/*  29 */       return 0;
/*     */     }
/*  31 */     if (p_parseBlend_0_.equals("add"))
/*     */     {
/*  33 */       return 1;
/*     */     }
/*  35 */     if (p_parseBlend_0_.equals("subtract"))
/*     */     {
/*  37 */       return 2;
/*     */     }
/*  39 */     if (p_parseBlend_0_.equals("multiply"))
/*     */     {
/*  41 */       return 3;
/*     */     }
/*  43 */     if (p_parseBlend_0_.equals("dodge"))
/*     */     {
/*  45 */       return 4;
/*     */     }
/*  47 */     if (p_parseBlend_0_.equals("burn"))
/*     */     {
/*  49 */       return 5;
/*     */     }
/*  51 */     if (p_parseBlend_0_.equals("screen"))
/*     */     {
/*  53 */       return 6;
/*     */     }
/*  55 */     if (p_parseBlend_0_.equals("replace"))
/*     */     {
/*  57 */       return 7;
/*     */     }
/*     */     
/*     */ 
/*  61 */     Config.warn("Unknown blend: " + p_parseBlend_0_);
/*  62 */     return 1;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static void setupBlend(int p_setupBlend_0_, float p_setupBlend_1_)
/*     */   {
/*  69 */     switch (p_setupBlend_0_)
/*     */     {
/*     */     case 0: 
/*  72 */       GlStateManager.disableAlpha();
/*  73 */       GlStateManager.enableBlend();
/*  74 */       GlStateManager.blendFunc(770, 771);
/*  75 */       GlStateManager.color(1.0F, 1.0F, 1.0F, p_setupBlend_1_);
/*  76 */       break;
/*     */     
/*     */     case 1: 
/*  79 */       GlStateManager.disableAlpha();
/*  80 */       GlStateManager.enableBlend();
/*  81 */       GlStateManager.blendFunc(770, 1);
/*  82 */       GlStateManager.color(1.0F, 1.0F, 1.0F, p_setupBlend_1_);
/*  83 */       break;
/*     */     
/*     */     case 2: 
/*  86 */       GlStateManager.disableAlpha();
/*  87 */       GlStateManager.enableBlend();
/*  88 */       GlStateManager.blendFunc(775, 0);
/*  89 */       GlStateManager.color(p_setupBlend_1_, p_setupBlend_1_, p_setupBlend_1_, 1.0F);
/*  90 */       break;
/*     */     
/*     */     case 3: 
/*  93 */       GlStateManager.disableAlpha();
/*  94 */       GlStateManager.enableBlend();
/*  95 */       GlStateManager.blendFunc(774, 771);
/*  96 */       GlStateManager.color(p_setupBlend_1_, p_setupBlend_1_, p_setupBlend_1_, p_setupBlend_1_);
/*  97 */       break;
/*     */     
/*     */     case 4: 
/* 100 */       GlStateManager.disableAlpha();
/* 101 */       GlStateManager.enableBlend();
/* 102 */       GlStateManager.blendFunc(1, 1);
/* 103 */       GlStateManager.color(p_setupBlend_1_, p_setupBlend_1_, p_setupBlend_1_, 1.0F);
/* 104 */       break;
/*     */     
/*     */     case 5: 
/* 107 */       GlStateManager.disableAlpha();
/* 108 */       GlStateManager.enableBlend();
/* 109 */       GlStateManager.blendFunc(0, 769);
/* 110 */       GlStateManager.color(p_setupBlend_1_, p_setupBlend_1_, p_setupBlend_1_, 1.0F);
/* 111 */       break;
/*     */     
/*     */     case 6: 
/* 114 */       GlStateManager.disableAlpha();
/* 115 */       GlStateManager.enableBlend();
/* 116 */       GlStateManager.blendFunc(1, 769);
/* 117 */       GlStateManager.color(p_setupBlend_1_, p_setupBlend_1_, p_setupBlend_1_, 1.0F);
/* 118 */       break;
/*     */     
/*     */     case 7: 
/* 121 */       GlStateManager.enableAlpha();
/* 122 */       GlStateManager.disableBlend();
/* 123 */       GlStateManager.color(1.0F, 1.0F, 1.0F, p_setupBlend_1_);
/*     */     }
/*     */     
/* 126 */     GlStateManager.enableTexture2D();
/*     */   }
/*     */   
/*     */   public static void clearBlend(float p_clearBlend_0_)
/*     */   {
/* 131 */     GlStateManager.disableAlpha();
/* 132 */     GlStateManager.enableBlend();
/* 133 */     GlStateManager.blendFunc(770, 1);
/* 134 */     GlStateManager.color(1.0F, 1.0F, 1.0F, p_clearBlend_0_);
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\optfine\Blender.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */