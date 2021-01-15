/*     */ package net.minecraft.client.gui;
/*     */ 
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.client.renderer.Tessellator;
/*     */ import net.minecraft.client.renderer.WorldRenderer;
/*     */ import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
/*     */ import org.lwjgl.input.Mouse;
/*     */ 
/*     */ public abstract class GuiSlot
/*     */ {
/*     */   protected final Minecraft mc;
/*     */   protected int width;
/*     */   protected int height;
/*     */   protected int top;
/*     */   protected int bottom;
/*     */   protected int right;
/*     */   protected int left;
/*     */   protected final int slotHeight;
/*     */   private int scrollUpButtonID;
/*     */   private int scrollDownButtonID;
/*     */   protected int mouseX;
/*     */   protected int mouseY;
/*  24 */   protected boolean field_148163_i = true;
/*  25 */   protected int initialClickY = -2;
/*     */   protected float scrollMultiplier;
/*     */   protected float amountScrolled;
/*  28 */   protected int selectedElement = -1;
/*     */   protected long lastClicked;
/*  30 */   protected boolean field_178041_q = true;
/*  31 */   protected boolean showSelectionBox = true;
/*     */   protected boolean hasListHeader;
/*     */   protected int headerPadding;
/*  34 */   private boolean enabled = true;
/*     */   
/*     */   public GuiSlot(Minecraft mcIn, int width, int height, int topIn, int bottomIn, int slotHeightIn) {
/*  37 */     this.mc = mcIn;
/*  38 */     this.width = width;
/*  39 */     this.height = height;
/*  40 */     this.top = topIn;
/*  41 */     this.bottom = bottomIn;
/*  42 */     this.slotHeight = slotHeightIn;
/*  43 */     this.left = 0;
/*  44 */     this.right = width;
/*     */   }
/*     */   
/*     */   public void setDimensions(int widthIn, int heightIn, int topIn, int bottomIn) {
/*  48 */     this.width = widthIn;
/*  49 */     this.height = heightIn;
/*  50 */     this.top = topIn;
/*  51 */     this.bottom = bottomIn;
/*  52 */     this.left = 0;
/*  53 */     this.right = widthIn;
/*     */   }
/*     */   
/*     */   public void setShowSelectionBox(boolean showSelectionBoxIn) {
/*  57 */     this.showSelectionBox = showSelectionBoxIn;
/*     */   }
/*     */   
/*     */   protected void setHasListHeader(boolean hasListHeaderIn, int headerPaddingIn) {
/*  61 */     this.hasListHeader = hasListHeaderIn;
/*  62 */     this.headerPadding = headerPaddingIn;
/*  63 */     if (!hasListHeaderIn) {
/*  64 */       this.headerPadding = 0;
/*     */     }
/*     */   }
/*     */   
/*     */   protected abstract int getSize();
/*     */   
/*     */   protected abstract void elementClicked(int paramInt1, boolean paramBoolean, int paramInt2, int paramInt3);
/*     */   
/*     */   protected abstract boolean isSelected(int paramInt);
/*     */   
/*     */   protected int getContentHeight() {
/*  75 */     return getSize() * this.slotHeight + this.headerPadding;
/*     */   }
/*     */   
/*     */ 
/*     */   protected abstract void drawBackground();
/*     */   
/*     */ 
/*     */   protected void func_178040_a(int p_178040_1_, int p_178040_2_, int p_178040_3_) {}
/*     */   
/*     */ 
/*     */   protected abstract void drawSlot(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6);
/*     */   
/*     */   protected void drawListHeader(int p_148129_1_, int p_148129_2_, Tessellator p_148129_3_) {}
/*     */   
/*     */   protected void func_148132_a(int p_148132_1_, int p_148132_2_) {}
/*     */   
/*     */   protected void func_148142_b(int p_148142_1_, int p_148142_2_) {}
/*     */   
/*     */   public int getSlotIndexFromScreenCoords(int p_148124_1_, int p_148124_2_)
/*     */   {
/*  95 */     int i = this.left + this.width / 2 - getListWidth() / 2;
/*  96 */     int j = this.left + this.width / 2 + getListWidth() / 2;
/*  97 */     int k = p_148124_2_ - this.top - this.headerPadding + (int)this.amountScrolled - 4;
/*  98 */     int l = k / this.slotHeight;
/*  99 */     return (p_148124_1_ < getScrollBarX()) && (p_148124_1_ >= i) && (p_148124_1_ <= j) && (l >= 0) && (k >= 0) && (l < getSize()) ? l : -1;
/*     */   }
/*     */   
/*     */   public void registerScrollButtons(int scrollUpButtonIDIn, int scrollDownButtonIDIn) {
/* 103 */     this.scrollUpButtonID = scrollUpButtonIDIn;
/* 104 */     this.scrollDownButtonID = scrollDownButtonIDIn;
/*     */   }
/*     */   
/*     */   protected void bindAmountScrolled() {
/* 108 */     this.amountScrolled = net.minecraft.util.MathHelper.clamp_float(this.amountScrolled, 0.0F, func_148135_f());
/*     */   }
/*     */   
/*     */   public int func_148135_f() {
/* 112 */     return Math.max(0, getContentHeight() - (this.bottom - this.top - 4));
/*     */   }
/*     */   
/*     */   public int getAmountScrolled() {
/* 116 */     return (int)this.amountScrolled;
/*     */   }
/*     */   
/*     */   public boolean isMouseYWithinSlotBounds(int p_148141_1_) {
/* 120 */     return (p_148141_1_ >= this.top) && (p_148141_1_ <= this.bottom) && (this.mouseX >= this.left) && (this.mouseX <= this.right);
/*     */   }
/*     */   
/*     */   public void scrollBy(int amount) {
/* 124 */     this.amountScrolled += amount;
/* 125 */     bindAmountScrolled();
/* 126 */     this.initialClickY = -2;
/*     */   }
/*     */   
/*     */   public void actionPerformed(GuiButton button) {
/* 130 */     if (button.enabled) {
/* 131 */       if (button.id == this.scrollUpButtonID) {
/* 132 */         this.amountScrolled -= this.slotHeight * 2 / 3;
/* 133 */         this.initialClickY = -2;
/* 134 */         bindAmountScrolled();
/* 135 */       } else if (button.id == this.scrollDownButtonID) {
/* 136 */         this.amountScrolled += this.slotHeight * 2 / 3;
/* 137 */         this.initialClickY = -2;
/* 138 */         bindAmountScrolled();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void drawScreen(int mouseXIn, int mouseYIn, float p_148128_3_) {
/* 144 */     if (this.field_178041_q) {
/* 145 */       this.mouseX = mouseXIn;
/* 146 */       this.mouseY = mouseYIn;
/* 147 */       drawBackground();
/* 148 */       int i = getScrollBarX();
/* 149 */       int j = i + 6;
/* 150 */       bindAmountScrolled();
/* 151 */       GlStateManager.disableLighting();
/* 152 */       GlStateManager.disableFog();
/* 153 */       Tessellator tessellator = Tessellator.getInstance();
/* 154 */       WorldRenderer worldrenderer = tessellator.getWorldRenderer();
/* 155 */       this.mc.getTextureManager().bindTexture(Gui.optionsBackground);
/* 156 */       GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/* 157 */       float f = 32.0F;
/* 158 */       worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
/* 159 */       worldrenderer.pos(this.left, this.bottom, 0.0D).tex(this.left / f, (this.bottom + (int)this.amountScrolled) / f).color(32, 32, 32, 255).endVertex();
/* 160 */       worldrenderer.pos(this.right, this.bottom, 0.0D).tex(this.right / f, (this.bottom + (int)this.amountScrolled) / f).color(32, 32, 32, 255).endVertex();
/* 161 */       worldrenderer.pos(this.right, this.top, 0.0D).tex(this.right / f, (this.top + (int)this.amountScrolled) / f).color(32, 32, 32, 255).endVertex();
/* 162 */       worldrenderer.pos(this.left, this.top, 0.0D).tex(this.left / f, (this.top + (int)this.amountScrolled) / f).color(32, 32, 32, 255).endVertex();
/* 163 */       tessellator.draw();
/* 164 */       int k = this.left + this.width / 2 - getListWidth() / 2 + 2;
/* 165 */       int l = this.top + 4 - (int)this.amountScrolled;
/* 166 */       if (this.hasListHeader) {
/* 167 */         drawListHeader(k, l, tessellator);
/*     */       }
/*     */       
/* 170 */       drawSelectionBox(k, l, mouseXIn, mouseYIn);
/* 171 */       GlStateManager.disableDepth();
/* 172 */       int i1 = 4;
/* 173 */       overlayBackground(0, this.top, 255, 255);
/* 174 */       overlayBackground(this.bottom, this.height, 255, 255);
/* 175 */       GlStateManager.enableBlend();
/* 176 */       GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
/* 177 */       GlStateManager.disableAlpha();
/* 178 */       GlStateManager.shadeModel(7425);
/* 179 */       GlStateManager.disableTexture2D();
/* 180 */       worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
/* 181 */       worldrenderer.pos(this.left, this.top + i1, 0.0D).tex(0.0D, 1.0D).color(0, 0, 0, 0).endVertex();
/* 182 */       worldrenderer.pos(this.right, this.top + i1, 0.0D).tex(1.0D, 1.0D).color(0, 0, 0, 0).endVertex();
/* 183 */       worldrenderer.pos(this.right, this.top, 0.0D).tex(1.0D, 0.0D).color(0, 0, 0, 255).endVertex();
/* 184 */       worldrenderer.pos(this.left, this.top, 0.0D).tex(0.0D, 0.0D).color(0, 0, 0, 255).endVertex();
/* 185 */       tessellator.draw();
/* 186 */       worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
/* 187 */       worldrenderer.pos(this.left, this.bottom, 0.0D).tex(0.0D, 1.0D).color(0, 0, 0, 255).endVertex();
/* 188 */       worldrenderer.pos(this.right, this.bottom, 0.0D).tex(1.0D, 1.0D).color(0, 0, 0, 255).endVertex();
/* 189 */       worldrenderer.pos(this.right, this.bottom - i1, 0.0D).tex(1.0D, 0.0D).color(0, 0, 0, 0).endVertex();
/* 190 */       worldrenderer.pos(this.left, this.bottom - i1, 0.0D).tex(0.0D, 0.0D).color(0, 0, 0, 0).endVertex();
/* 191 */       tessellator.draw();
/* 192 */       int j1 = func_148135_f();
/* 193 */       if (j1 > 0) {
/* 194 */         int k1 = (this.bottom - this.top) * (this.bottom - this.top) / getContentHeight();
/* 195 */         k1 = net.minecraft.util.MathHelper.clamp_int(k1, 32, this.bottom - this.top - 8);
/* 196 */         int l1 = (int)this.amountScrolled * (this.bottom - this.top - k1) / j1 + this.top;
/* 197 */         if (l1 < this.top) {
/* 198 */           l1 = this.top;
/*     */         }
/*     */         
/* 201 */         worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
/* 202 */         worldrenderer.pos(i, this.bottom, 0.0D).tex(0.0D, 1.0D).color(0, 0, 0, 255).endVertex();
/* 203 */         worldrenderer.pos(j, this.bottom, 0.0D).tex(1.0D, 1.0D).color(0, 0, 0, 255).endVertex();
/* 204 */         worldrenderer.pos(j, this.top, 0.0D).tex(1.0D, 0.0D).color(0, 0, 0, 255).endVertex();
/* 205 */         worldrenderer.pos(i, this.top, 0.0D).tex(0.0D, 0.0D).color(0, 0, 0, 255).endVertex();
/* 206 */         tessellator.draw();
/* 207 */         worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
/* 208 */         worldrenderer.pos(i, l1 + k1, 0.0D).tex(0.0D, 1.0D).color(128, 128, 128, 255).endVertex();
/* 209 */         worldrenderer.pos(j, l1 + k1, 0.0D).tex(1.0D, 1.0D).color(128, 128, 128, 255).endVertex();
/* 210 */         worldrenderer.pos(j, l1, 0.0D).tex(1.0D, 0.0D).color(128, 128, 128, 255).endVertex();
/* 211 */         worldrenderer.pos(i, l1, 0.0D).tex(0.0D, 0.0D).color(128, 128, 128, 255).endVertex();
/* 212 */         tessellator.draw();
/* 213 */         worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
/* 214 */         worldrenderer.pos(i, l1 + k1 - 1, 0.0D).tex(0.0D, 1.0D).color(192, 192, 192, 255).endVertex();
/* 215 */         worldrenderer.pos(j - 1, l1 + k1 - 1, 0.0D).tex(1.0D, 1.0D).color(192, 192, 192, 255).endVertex();
/* 216 */         worldrenderer.pos(j - 1, l1, 0.0D).tex(1.0D, 0.0D).color(192, 192, 192, 255).endVertex();
/* 217 */         worldrenderer.pos(i, l1, 0.0D).tex(0.0D, 0.0D).color(192, 192, 192, 255).endVertex();
/* 218 */         tessellator.draw();
/*     */       }
/*     */       
/* 221 */       func_148142_b(mouseXIn, mouseYIn);
/* 222 */       GlStateManager.enableTexture2D();
/* 223 */       GlStateManager.shadeModel(7424);
/* 224 */       GlStateManager.enableAlpha();
/* 225 */       GlStateManager.disableBlend();
/*     */     }
/*     */   }
/*     */   
/*     */   public void handleMouseInput() {
/* 230 */     if (isMouseYWithinSlotBounds(this.mouseY)) {
/* 231 */       if ((Mouse.getEventButton() == 0) && (Mouse.getEventButtonState()) && (this.mouseY >= this.top) && (this.mouseY <= this.bottom)) {
/* 232 */         int i = (this.width - getListWidth()) / 2;
/* 233 */         int j = (this.width + getListWidth()) / 2;
/* 234 */         int k = this.mouseY - this.top - this.headerPadding + (int)this.amountScrolled - 4;
/* 235 */         int l = k / this.slotHeight;
/* 236 */         if ((l < getSize()) && (this.mouseX >= i) && (this.mouseX <= j) && (l >= 0) && (k >= 0)) {
/* 237 */           elementClicked(l, false, this.mouseX, this.mouseY);
/* 238 */           this.selectedElement = l;
/* 239 */         } else if ((this.mouseX >= i) && (this.mouseX <= j) && (k < 0)) {
/* 240 */           func_148132_a(this.mouseX - i, this.mouseY - this.top + (int)this.amountScrolled - 4);
/*     */         }
/*     */       }
/*     */       
/* 244 */       if ((Mouse.isButtonDown(0)) && (getEnabled())) {
/* 245 */         if (this.initialClickY == -1) {
/* 246 */           boolean flag1 = true;
/* 247 */           if ((this.mouseY >= this.top) && (this.mouseY <= this.bottom)) {
/* 248 */             int j2 = (this.width - getListWidth()) / 2;
/* 249 */             int k2 = (this.width + getListWidth()) / 2;
/* 250 */             int l2 = this.mouseY - this.top - this.headerPadding + (int)this.amountScrolled - 4;
/* 251 */             int i1 = l2 / this.slotHeight;
/* 252 */             if ((i1 < getSize()) && (this.mouseX >= j2) && (this.mouseX <= k2) && (i1 >= 0) && (l2 >= 0)) {
/* 253 */               boolean flag = (i1 == this.selectedElement) && (Minecraft.getSystemTime() - this.lastClicked < 250L);
/* 254 */               elementClicked(i1, flag, this.mouseX, this.mouseY);
/* 255 */               this.selectedElement = i1;
/* 256 */               this.lastClicked = Minecraft.getSystemTime();
/* 257 */             } else if ((this.mouseX >= j2) && (this.mouseX <= k2) && (l2 < 0)) {
/* 258 */               func_148132_a(this.mouseX - j2, this.mouseY - this.top + (int)this.amountScrolled - 4);
/* 259 */               flag1 = false;
/*     */             }
/*     */             
/* 262 */             int i3 = getScrollBarX();
/* 263 */             int j1 = i3 + 6;
/* 264 */             if ((this.mouseX >= i3) && (this.mouseX <= j1)) {
/* 265 */               this.scrollMultiplier = -1.0F;
/* 266 */               int k1 = func_148135_f();
/* 267 */               if (k1 < 1) {
/* 268 */                 k1 = 1;
/*     */               }
/*     */               
/* 271 */               int l1 = (int)((this.bottom - this.top) * (this.bottom - this.top) / getContentHeight());
/* 272 */               l1 = net.minecraft.util.MathHelper.clamp_int(l1, 32, this.bottom - this.top - 8);
/* 273 */               this.scrollMultiplier /= (this.bottom - this.top - l1) / k1;
/*     */             } else {
/* 275 */               this.scrollMultiplier = 1.0F;
/*     */             }
/*     */             
/* 278 */             if (flag1) {
/* 279 */               this.initialClickY = this.mouseY;
/*     */             } else {
/* 281 */               this.initialClickY = -2;
/*     */             }
/*     */           } else {
/* 284 */             this.initialClickY = -2;
/*     */           }
/* 286 */         } else if (this.initialClickY >= 0) {
/* 287 */           this.amountScrolled -= (this.mouseY - this.initialClickY) * this.scrollMultiplier;
/* 288 */           this.initialClickY = this.mouseY;
/*     */         }
/*     */       } else {
/* 291 */         this.initialClickY = -1;
/*     */       }
/*     */       
/* 294 */       int i2 = Mouse.getEventDWheel();
/* 295 */       if (i2 != 0) {
/* 296 */         if (i2 > 0) {
/* 297 */           i2 = -1;
/* 298 */         } else if (i2 < 0) {
/* 299 */           i2 = 1;
/*     */         }
/*     */         
/* 302 */         this.amountScrolled += i2 * this.slotHeight / 2;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void setEnabled(boolean enabledIn) {
/* 308 */     this.enabled = enabledIn;
/*     */   }
/*     */   
/*     */   public boolean getEnabled() {
/* 312 */     return this.enabled;
/*     */   }
/*     */   
/*     */   public int getListWidth() {
/* 316 */     return 220;
/*     */   }
/*     */   
/*     */   protected void drawSelectionBox(int p_148120_1_, int p_148120_2_, int mouseXIn, int mouseYIn) {
/* 320 */     int i = getSize();
/* 321 */     Tessellator tessellator = Tessellator.getInstance();
/* 322 */     WorldRenderer worldrenderer = tessellator.getWorldRenderer();
/*     */     
/* 324 */     for (int j = 0; j < i; j++) {
/* 325 */       int k = p_148120_2_ + j * this.slotHeight + this.headerPadding;
/* 326 */       int l = this.slotHeight - 4;
/* 327 */       if ((k > this.bottom) || (k + l < this.top)) {
/* 328 */         func_178040_a(j, p_148120_1_, k);
/*     */       }
/*     */       
/* 331 */       if ((this.showSelectionBox) && (isSelected(j))) {
/* 332 */         int i1 = this.left + (this.width / 2 - getListWidth() / 2);
/* 333 */         int j1 = this.left + this.width / 2 + getListWidth() / 2;
/* 334 */         GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/* 335 */         GlStateManager.disableTexture2D();
/* 336 */         worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
/* 337 */         worldrenderer.pos(i1, k + l + 2, 0.0D).tex(0.0D, 1.0D).color(128, 128, 128, 255).endVertex();
/* 338 */         worldrenderer.pos(j1, k + l + 2, 0.0D).tex(1.0D, 1.0D).color(128, 128, 128, 255).endVertex();
/* 339 */         worldrenderer.pos(j1, k - 2, 0.0D).tex(1.0D, 0.0D).color(128, 128, 128, 255).endVertex();
/* 340 */         worldrenderer.pos(i1, k - 2, 0.0D).tex(0.0D, 0.0D).color(128, 128, 128, 255).endVertex();
/* 341 */         worldrenderer.pos(i1 + 1, k + l + 1, 0.0D).tex(0.0D, 1.0D).color(0, 0, 0, 255).endVertex();
/* 342 */         worldrenderer.pos(j1 - 1, k + l + 1, 0.0D).tex(1.0D, 1.0D).color(0, 0, 0, 255).endVertex();
/* 343 */         worldrenderer.pos(j1 - 1, k - 1, 0.0D).tex(1.0D, 0.0D).color(0, 0, 0, 255).endVertex();
/* 344 */         worldrenderer.pos(i1 + 1, k - 1, 0.0D).tex(0.0D, 0.0D).color(0, 0, 0, 255).endVertex();
/* 345 */         tessellator.draw();
/* 346 */         GlStateManager.enableTexture2D();
/*     */       }
/*     */       
/* 349 */       drawSlot(j, p_148120_1_, k, l, mouseXIn, mouseYIn);
/*     */     }
/*     */   }
/*     */   
/*     */   protected int getScrollBarX() {
/* 354 */     return this.width / 2 + 124;
/*     */   }
/*     */   
/*     */   protected void overlayBackground(int startY, int endY, int startAlpha, int endAlpha) {
/* 358 */     Tessellator tessellator = Tessellator.getInstance();
/* 359 */     WorldRenderer worldrenderer = tessellator.getWorldRenderer();
/* 360 */     this.mc.getTextureManager().bindTexture(Gui.optionsBackground);
/* 361 */     GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/* 362 */     float f = 32.0F;
/* 363 */     worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
/* 364 */     worldrenderer.pos(this.left, endY, 0.0D).tex(0.0D, endY / 32.0F).color(64, 64, 64, endAlpha).endVertex();
/* 365 */     worldrenderer.pos(this.left + this.width, endY, 0.0D).tex(this.width / 32.0F, endY / 32.0F).color(64, 64, 64, endAlpha).endVertex();
/* 366 */     worldrenderer.pos(this.left + this.width, startY, 0.0D).tex(this.width / 32.0F, startY / 32.0F).color(64, 64, 64, startAlpha).endVertex();
/* 367 */     worldrenderer.pos(this.left, startY, 0.0D).tex(0.0D, startY / 32.0F).color(64, 64, 64, startAlpha).endVertex();
/* 368 */     tessellator.draw();
/*     */   }
/*     */   
/*     */   public void setSlotXBoundsFromLeft(int leftIn) {
/* 372 */     this.left = leftIn;
/* 373 */     this.right = (leftIn + this.width);
/*     */   }
/*     */   
/*     */   public int getSlotHeight() {
/* 377 */     return this.slotHeight;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\gui\GuiSlot.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */