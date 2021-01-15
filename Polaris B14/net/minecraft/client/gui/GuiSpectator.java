/*     */ package net.minecraft.client.gui;
/*     */ 
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.spectator.ISpectatorMenuObject;
/*     */ import net.minecraft.client.gui.spectator.SpectatorMenu;
/*     */ import net.minecraft.client.gui.spectator.categories.SpectatorDetails;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.client.renderer.RenderHelper;
/*     */ import net.minecraft.client.renderer.texture.TextureManager;
/*     */ import net.minecraft.util.IChatComponent;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ 
/*     */ public class GuiSpectator extends Gui implements net.minecraft.client.gui.spectator.ISpectatorMenuRecipient
/*     */ {
/*  15 */   private static final ResourceLocation field_175267_f = new ResourceLocation("textures/gui/widgets.png");
/*  16 */   public static final ResourceLocation field_175269_a = new ResourceLocation("textures/gui/spectator_widgets.png");
/*     */   private final Minecraft field_175268_g;
/*     */   private long field_175270_h;
/*     */   private SpectatorMenu field_175271_i;
/*     */   
/*     */   public GuiSpectator(Minecraft mcIn) {
/*  22 */     this.field_175268_g = mcIn;
/*     */   }
/*     */   
/*     */   public void func_175260_a(int p_175260_1_) {
/*  26 */     this.field_175270_h = Minecraft.getSystemTime();
/*  27 */     if (this.field_175271_i != null) {
/*  28 */       this.field_175271_i.func_178644_b(p_175260_1_);
/*     */     } else {
/*  30 */       this.field_175271_i = new SpectatorMenu(this);
/*     */     }
/*     */   }
/*     */   
/*     */   private float func_175265_c() {
/*  35 */     long i = this.field_175270_h - Minecraft.getSystemTime() + 5000L;
/*  36 */     return net.minecraft.util.MathHelper.clamp_float((float)i / 2000.0F, 0.0F, 1.0F);
/*     */   }
/*     */   
/*     */   public void renderTooltip(ScaledResolution p_175264_1_, float p_175264_2_) {
/*  40 */     if (this.field_175271_i != null) {
/*  41 */       float f = func_175265_c();
/*  42 */       if (f <= 0.0F) {
/*  43 */         this.field_175271_i.func_178641_d();
/*     */       } else {
/*  45 */         int i = ScaledResolution.getScaledWidth() / 2;
/*  46 */         float f1 = this.zLevel;
/*  47 */         this.zLevel = -90.0F;
/*  48 */         float f2 = ScaledResolution.getScaledHeight() - 22.0F * f;
/*  49 */         SpectatorDetails spectatordetails = this.field_175271_i.func_178646_f();
/*  50 */         func_175258_a(p_175264_1_, f, i, f2, spectatordetails);
/*  51 */         this.zLevel = f1;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   protected void func_175258_a(ScaledResolution p_175258_1_, float p_175258_2_, int p_175258_3_, float p_175258_4_, SpectatorDetails p_175258_5_) {
/*  57 */     GlStateManager.enableRescaleNormal();
/*  58 */     GlStateManager.enableBlend();
/*  59 */     GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
/*  60 */     GlStateManager.color(1.0F, 1.0F, 1.0F, p_175258_2_);
/*  61 */     this.field_175268_g.getTextureManager().bindTexture(field_175267_f);
/*  62 */     drawTexturedModalRect(p_175258_3_ - 91, p_175258_4_, 0, 0, 182, 22);
/*  63 */     if (p_175258_5_.func_178681_b() >= 0) {
/*  64 */       drawTexturedModalRect(p_175258_3_ - 91 - 1 + p_175258_5_.func_178681_b() * 20, p_175258_4_ - 1.0F, 0, 22, 24, 22);
/*     */     }
/*     */     
/*  67 */     RenderHelper.enableGUIStandardItemLighting();
/*     */     
/*  69 */     for (int i = 0; i < 9; i++) {
/*  70 */       func_175266_a(i, ScaledResolution.getScaledWidth() / 2 - 90 + i * 20 + 2, p_175258_4_ + 3.0F, p_175258_2_, p_175258_5_.func_178680_a(i));
/*     */     }
/*     */     
/*  73 */     RenderHelper.disableStandardItemLighting();
/*  74 */     GlStateManager.disableRescaleNormal();
/*  75 */     GlStateManager.disableBlend();
/*     */   }
/*     */   
/*     */   private void func_175266_a(int p_175266_1_, int p_175266_2_, float p_175266_3_, float p_175266_4_, ISpectatorMenuObject p_175266_5_) {
/*  79 */     this.field_175268_g.getTextureManager().bindTexture(field_175269_a);
/*  80 */     if (p_175266_5_ != SpectatorMenu.field_178657_a) {
/*  81 */       int i = (int)(p_175266_4_ * 255.0F);
/*  82 */       GlStateManager.pushMatrix();
/*  83 */       GlStateManager.translate(p_175266_2_, p_175266_3_, 0.0F);
/*  84 */       float f = p_175266_5_.func_178662_A_() ? 1.0F : 0.25F;
/*  85 */       GlStateManager.color(f, f, f, p_175266_4_);
/*  86 */       p_175266_5_.func_178663_a(f, i);
/*  87 */       GlStateManager.popMatrix();
/*  88 */       String s = String.valueOf(net.minecraft.client.settings.GameSettings.getKeyDisplayString(this.field_175268_g.gameSettings.keyBindsHotbar[p_175266_1_].getKeyCode()));
/*  89 */       if ((i > 3) && (p_175266_5_.func_178662_A_())) {
/*  90 */         this.field_175268_g.fontRendererObj.drawStringWithShadow(s, p_175266_2_ + 19 - 2 - this.field_175268_g.fontRendererObj.getStringWidth(s), p_175266_3_ + 6.0F + 3.0F, 16777215 + (i << 24));
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void func_175263_a(ScaledResolution p_175263_1_) {
/*  96 */     int i = (int)(func_175265_c() * 255.0F);
/*  97 */     if ((i > 3) && (this.field_175271_i != null)) {
/*  98 */       ISpectatorMenuObject ispectatormenuobject = this.field_175271_i.func_178645_b();
/*  99 */       String s = ispectatormenuobject != SpectatorMenu.field_178657_a ? ispectatormenuobject.getSpectatorName().getFormattedText() : this.field_175271_i.func_178650_c().func_178670_b().getFormattedText();
/* 100 */       if (s != null) {
/* 101 */         int j = (ScaledResolution.getScaledWidth() - this.field_175268_g.fontRendererObj.getStringWidth(s)) / 2;
/* 102 */         int k = ScaledResolution.getScaledHeight() - 35;
/* 103 */         GlStateManager.pushMatrix();
/* 104 */         GlStateManager.enableBlend();
/* 105 */         GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
/* 106 */         this.field_175268_g.fontRendererObj.drawStringWithShadow(s, j, k, 16777215 + (i << 24));
/* 107 */         GlStateManager.disableBlend();
/* 108 */         GlStateManager.popMatrix();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void func_175257_a(SpectatorMenu p_175257_1_) {
/* 114 */     this.field_175271_i = null;
/* 115 */     this.field_175270_h = 0L;
/*     */   }
/*     */   
/*     */   public boolean func_175262_a() {
/* 119 */     return this.field_175271_i != null;
/*     */   }
/*     */   
/*     */   public void func_175259_b(int p_175259_1_)
/*     */   {
/* 124 */     for (int i = this.field_175271_i.func_178648_e() + p_175259_1_; (i >= 0) && (i <= 8) && ((this.field_175271_i.func_178643_a(i) == SpectatorMenu.field_178657_a) || (!this.field_175271_i.func_178643_a(i).func_178662_A_())); i += p_175259_1_) {}
/*     */     
/*     */ 
/*     */ 
/* 128 */     if ((i >= 0) && (i <= 8)) {
/* 129 */       this.field_175271_i.func_178644_b(i);
/* 130 */       this.field_175270_h = Minecraft.getSystemTime();
/*     */     }
/*     */   }
/*     */   
/*     */   public void func_175261_b() {
/* 135 */     this.field_175270_h = Minecraft.getSystemTime();
/* 136 */     if (func_175262_a()) {
/* 137 */       int i = this.field_175271_i.func_178648_e();
/* 138 */       if (i != -1) {
/* 139 */         this.field_175271_i.func_178644_b(i);
/*     */       }
/*     */     } else {
/* 142 */       this.field_175271_i = new SpectatorMenu(this);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\gui\GuiSpectator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */