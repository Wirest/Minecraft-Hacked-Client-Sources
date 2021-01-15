/*     */ package net.minecraft.client.gui;
/*     */ 
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.client.renderer.Tessellator;
/*     */ import net.minecraft.client.renderer.WorldRenderer;
/*     */ import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
/*     */ import net.minecraft.realms.RealmsSimpleScrolledSelectionList;
/*     */ 
/*     */ public class GuiSimpleScrolledSelectionListProxy extends GuiSlot
/*     */ {
/*     */   private final RealmsSimpleScrolledSelectionList field_178050_u;
/*     */   
/*     */   public GuiSimpleScrolledSelectionListProxy(RealmsSimpleScrolledSelectionList p_i45525_1_, int widthIn, int heightIn, int topIn, int bottomIn, int slotHeightIn)
/*     */   {
/*  15 */     super(net.minecraft.client.Minecraft.getMinecraft(), widthIn, heightIn, topIn, bottomIn, slotHeightIn);
/*  16 */     this.field_178050_u = p_i45525_1_;
/*     */   }
/*     */   
/*     */   protected int getSize() {
/*  20 */     return this.field_178050_u.getItemCount();
/*     */   }
/*     */   
/*     */   protected void elementClicked(int slotIndex, boolean isDoubleClick, int mouseX, int mouseY) {
/*  24 */     this.field_178050_u.selectItem(slotIndex, isDoubleClick, mouseX, mouseY);
/*     */   }
/*     */   
/*     */   protected boolean isSelected(int slotIndex) {
/*  28 */     return this.field_178050_u.isSelectedItem(slotIndex);
/*     */   }
/*     */   
/*     */   protected void drawBackground() {
/*  32 */     this.field_178050_u.renderBackground();
/*     */   }
/*     */   
/*     */   protected void drawSlot(int entryID, int p_180791_2_, int p_180791_3_, int p_180791_4_, int mouseXIn, int mouseYIn) {
/*  36 */     this.field_178050_u.renderItem(entryID, p_180791_2_, p_180791_3_, p_180791_4_, mouseXIn, mouseYIn);
/*     */   }
/*     */   
/*     */   public int getWidth() {
/*  40 */     return this.width;
/*     */   }
/*     */   
/*     */   public int getMouseY() {
/*  44 */     return this.mouseY;
/*     */   }
/*     */   
/*     */   public int getMouseX() {
/*  48 */     return this.mouseX;
/*     */   }
/*     */   
/*     */   protected int getContentHeight() {
/*  52 */     return this.field_178050_u.getMaxPosition();
/*     */   }
/*     */   
/*     */   protected int getScrollBarX() {
/*  56 */     return this.field_178050_u.getScrollbarPosition();
/*     */   }
/*     */   
/*     */   public void handleMouseInput() {
/*  60 */     super.handleMouseInput();
/*     */   }
/*     */   
/*     */   public void drawScreen(int mouseXIn, int mouseYIn, float p_148128_3_) {
/*  64 */     if (this.field_178041_q) {
/*  65 */       this.mouseX = mouseXIn;
/*  66 */       this.mouseY = mouseYIn;
/*  67 */       drawBackground();
/*  68 */       int i = getScrollBarX();
/*  69 */       int j = i + 6;
/*  70 */       bindAmountScrolled();
/*  71 */       GlStateManager.disableLighting();
/*  72 */       GlStateManager.disableFog();
/*  73 */       Tessellator tessellator = Tessellator.getInstance();
/*  74 */       WorldRenderer worldrenderer = tessellator.getWorldRenderer();
/*  75 */       int k = this.left + this.width / 2 - getListWidth() / 2 + 2;
/*  76 */       int l = this.top + 4 - (int)this.amountScrolled;
/*  77 */       if (this.hasListHeader) {
/*  78 */         drawListHeader(k, l, tessellator);
/*     */       }
/*     */       
/*  81 */       drawSelectionBox(k, l, mouseXIn, mouseYIn);
/*  82 */       GlStateManager.disableDepth();
/*  83 */       int i1 = 4;
/*  84 */       overlayBackground(0, this.top, 255, 255);
/*  85 */       overlayBackground(this.bottom, this.height, 255, 255);
/*  86 */       GlStateManager.enableBlend();
/*  87 */       GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
/*  88 */       GlStateManager.disableAlpha();
/*  89 */       GlStateManager.shadeModel(7425);
/*  90 */       GlStateManager.disableTexture2D();
/*  91 */       int j1 = func_148135_f();
/*  92 */       if (j1 > 0) {
/*  93 */         int k1 = (this.bottom - this.top) * (this.bottom - this.top) / getContentHeight();
/*  94 */         k1 = net.minecraft.util.MathHelper.clamp_int(k1, 32, this.bottom - this.top - 8);
/*  95 */         int l1 = (int)this.amountScrolled * (this.bottom - this.top - k1) / j1 + this.top;
/*  96 */         if (l1 < this.top) {
/*  97 */           l1 = this.top;
/*     */         }
/*     */         
/* 100 */         worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
/* 101 */         worldrenderer.pos(i, this.bottom, 0.0D).tex(0.0D, 1.0D).color(0, 0, 0, 255).endVertex();
/* 102 */         worldrenderer.pos(j, this.bottom, 0.0D).tex(1.0D, 1.0D).color(0, 0, 0, 255).endVertex();
/* 103 */         worldrenderer.pos(j, this.top, 0.0D).tex(1.0D, 0.0D).color(0, 0, 0, 255).endVertex();
/* 104 */         worldrenderer.pos(i, this.top, 0.0D).tex(0.0D, 0.0D).color(0, 0, 0, 255).endVertex();
/* 105 */         tessellator.draw();
/* 106 */         worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
/* 107 */         worldrenderer.pos(i, l1 + k1, 0.0D).tex(0.0D, 1.0D).color(128, 128, 128, 255).endVertex();
/* 108 */         worldrenderer.pos(j, l1 + k1, 0.0D).tex(1.0D, 1.0D).color(128, 128, 128, 255).endVertex();
/* 109 */         worldrenderer.pos(j, l1, 0.0D).tex(1.0D, 0.0D).color(128, 128, 128, 255).endVertex();
/* 110 */         worldrenderer.pos(i, l1, 0.0D).tex(0.0D, 0.0D).color(128, 128, 128, 255).endVertex();
/* 111 */         tessellator.draw();
/* 112 */         worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
/* 113 */         worldrenderer.pos(i, l1 + k1 - 1, 0.0D).tex(0.0D, 1.0D).color(192, 192, 192, 255).endVertex();
/* 114 */         worldrenderer.pos(j - 1, l1 + k1 - 1, 0.0D).tex(1.0D, 1.0D).color(192, 192, 192, 255).endVertex();
/* 115 */         worldrenderer.pos(j - 1, l1, 0.0D).tex(1.0D, 0.0D).color(192, 192, 192, 255).endVertex();
/* 116 */         worldrenderer.pos(i, l1, 0.0D).tex(0.0D, 0.0D).color(192, 192, 192, 255).endVertex();
/* 117 */         tessellator.draw();
/*     */       }
/*     */       
/* 120 */       func_148142_b(mouseXIn, mouseYIn);
/* 121 */       GlStateManager.enableTexture2D();
/* 122 */       GlStateManager.shadeModel(7424);
/* 123 */       GlStateManager.enableAlpha();
/* 124 */       GlStateManager.disableBlend();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\gui\GuiSimpleScrolledSelectionListProxy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */