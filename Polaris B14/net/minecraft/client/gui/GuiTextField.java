/*     */ package net.minecraft.client.gui;
/*     */ 
/*     */ import com.google.common.base.Predicate;
/*     */ import com.google.common.base.Predicates;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.client.renderer.Tessellator;
/*     */ import net.minecraft.client.renderer.WorldRenderer;
/*     */ import net.minecraft.util.ChatAllowedCharacters;
/*     */ import net.minecraft.util.MathHelper;
/*     */ 
/*     */ public class GuiTextField extends Gui
/*     */ {
/*     */   private final int id;
/*     */   private final FontRenderer fontRendererInstance;
/*     */   public int xPosition;
/*     */   public int yPosition;
/*     */   private final int width;
/*     */   private final int height;
/*  19 */   private String text = "";
/*  20 */   private int maxStringLength = 69;
/*     */   private int cursorCounter;
/*  22 */   private boolean enableBackgroundDrawing = true;
/*  23 */   private boolean canLoseFocus = true;
/*     */   private boolean isFocused;
/*  25 */   private boolean isEnabled = true;
/*     */   private int lineScrollOffset;
/*     */   private int cursorPosition;
/*     */   private int selectionEnd;
/*  29 */   private int enabledColor = 14737632;
/*  30 */   private int disabledColor = 7368816;
/*  31 */   private boolean visible = true;
/*     */   private GuiPageButtonList.GuiResponder field_175210_x;
/*  33 */   private Predicate<String> field_175209_y = Predicates.alwaysTrue();
/*     */   
/*     */   public GuiTextField(int componentId, FontRenderer fontrendererObj, int x, int y, int par5Width, int par6Height) {
/*  36 */     this.id = componentId;
/*  37 */     this.fontRendererInstance = fontrendererObj;
/*  38 */     this.xPosition = x;
/*  39 */     this.yPosition = y;
/*  40 */     this.width = par5Width;
/*  41 */     this.height = par6Height;
/*     */   }
/*     */   
/*     */   public void func_175207_a(GuiPageButtonList.GuiResponder p_175207_1_) {
/*  45 */     this.field_175210_x = p_175207_1_;
/*     */   }
/*     */   
/*     */   public void updateCursorCounter() {
/*  49 */     this.cursorCounter += 1;
/*     */   }
/*     */   
/*     */   public void setText(String p_146180_1_) {
/*  53 */     if (this.field_175209_y.apply(p_146180_1_)) {
/*  54 */       if (p_146180_1_.length() > this.maxStringLength) {
/*  55 */         this.text = p_146180_1_.substring(0, this.maxStringLength);
/*     */       } else {
/*  57 */         this.text = p_146180_1_;
/*     */       }
/*     */       
/*  60 */       setCursorPositionEnd();
/*     */     }
/*     */   }
/*     */   
/*     */   public String getText() {
/*  65 */     return this.text;
/*     */   }
/*     */   
/*     */   public String getSelectedText() {
/*  69 */     int i = this.cursorPosition < this.selectionEnd ? this.cursorPosition : this.selectionEnd;
/*  70 */     int j = this.cursorPosition < this.selectionEnd ? this.selectionEnd : this.cursorPosition;
/*  71 */     return this.text.substring(i, j);
/*     */   }
/*     */   
/*     */   public void func_175205_a(Predicate<String> p_175205_1_) {
/*  75 */     this.field_175209_y = p_175205_1_;
/*     */   }
/*     */   
/*     */   public void writeText(String p_146191_1_) {
/*  79 */     String s = "";
/*  80 */     String s1 = ChatAllowedCharacters.filterAllowedCharacters(p_146191_1_);
/*  81 */     int i = this.cursorPosition < this.selectionEnd ? this.cursorPosition : this.selectionEnd;
/*  82 */     int j = this.cursorPosition < this.selectionEnd ? this.selectionEnd : this.cursorPosition;
/*  83 */     int k = this.maxStringLength - this.text.length() - (i - j);
/*  84 */     int l = 0;
/*  85 */     if (this.text.length() > 0) {
/*  86 */       s = s + this.text.substring(0, i);
/*     */     }
/*     */     
/*  89 */     if (k < s1.length()) {
/*  90 */       s = s + s1.substring(0, k);
/*  91 */       l = k;
/*     */     } else {
/*  93 */       s = s + s1;
/*  94 */       l = s1.length();
/*     */     }
/*     */     
/*  97 */     if ((this.text.length() > 0) && (j < this.text.length())) {
/*  98 */       s = s + this.text.substring(j);
/*     */     }
/*     */     
/* 101 */     if (this.field_175209_y.apply(s)) {
/* 102 */       this.text = s;
/* 103 */       moveCursorBy(i - this.selectionEnd + l);
/* 104 */       if (this.field_175210_x != null) {
/* 105 */         this.field_175210_x.func_175319_a(this.id, this.text);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void deleteWords(int p_146177_1_) {
/* 111 */     if (this.text.length() != 0) {
/* 112 */       if (this.selectionEnd != this.cursorPosition) {
/* 113 */         writeText("");
/*     */       } else {
/* 115 */         deleteFromCursor(getNthWordFromCursor(p_146177_1_) - this.cursorPosition);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void deleteFromCursor(int p_146175_1_) {
/* 121 */     if (this.text.length() != 0) {
/* 122 */       if (this.selectionEnd != this.cursorPosition) {
/* 123 */         writeText("");
/*     */       } else {
/* 125 */         boolean flag = p_146175_1_ < 0;
/* 126 */         int i = flag ? this.cursorPosition + p_146175_1_ : this.cursorPosition;
/* 127 */         int j = flag ? this.cursorPosition : this.cursorPosition + p_146175_1_;
/* 128 */         String s = "";
/* 129 */         if (i >= 0) {
/* 130 */           s = this.text.substring(0, i);
/*     */         }
/*     */         
/* 133 */         if (j < this.text.length()) {
/* 134 */           s = s + this.text.substring(j);
/*     */         }
/*     */         
/* 137 */         if (this.field_175209_y.apply(s)) {
/* 138 */           this.text = s;
/* 139 */           if (flag) {
/* 140 */             moveCursorBy(p_146175_1_);
/*     */           }
/*     */           
/* 143 */           if (this.field_175210_x != null) {
/* 144 */             this.field_175210_x.func_175319_a(this.id, this.text);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public int getId() {
/* 152 */     return this.id;
/*     */   }
/*     */   
/*     */   public int getNthWordFromCursor(int p_146187_1_) {
/* 156 */     return getNthWordFromPos(p_146187_1_, getCursorPosition());
/*     */   }
/*     */   
/*     */   public int getNthWordFromPos(int p_146183_1_, int p_146183_2_) {
/* 160 */     return func_146197_a(p_146183_1_, p_146183_2_, true);
/*     */   }
/*     */   
/*     */   public int func_146197_a(int p_146197_1_, int p_146197_2_, boolean p_146197_3_) {
/* 164 */     int i = p_146197_2_;
/* 165 */     boolean flag = p_146197_1_ < 0;
/* 166 */     int j = Math.abs(p_146197_1_);
/*     */     
/* 168 */     for (int k = 0; k < j; k++) {
/* 169 */       if (!flag) {
/* 170 */         int l = this.text.length();
/* 171 */         i = this.text.indexOf(' ', i);
/* 172 */         if (i == -1) {
/* 173 */           i = l;
/*     */         } else {
/*     */           do {
/* 176 */             i++;
/* 175 */             if ((!p_146197_3_) || (i >= l)) break; } while (this.text.charAt(i) == ' ');
/*     */         }
/*     */       }
/*     */       else
/*     */       {
/*     */         do {
/* 181 */           i--;
/* 180 */           if ((!p_146197_3_) || (i <= 0)) break; } while (this.text.charAt(i - 1) == ' ');
/*     */         
/*     */ 
/*     */ 
/* 184 */         while ((i > 0) && (this.text.charAt(i - 1) != ' ')) {
/* 185 */           i--;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 190 */     return i;
/*     */   }
/*     */   
/*     */   public void moveCursorBy(int p_146182_1_) {
/* 194 */     setCursorPosition(this.selectionEnd + p_146182_1_);
/*     */   }
/*     */   
/*     */   public void setCursorPosition(int p_146190_1_) {
/* 198 */     this.cursorPosition = p_146190_1_;
/* 199 */     int i = this.text.length();
/* 200 */     this.cursorPosition = MathHelper.clamp_int(this.cursorPosition, 0, i);
/* 201 */     setSelectionPos(this.cursorPosition);
/*     */   }
/*     */   
/*     */   public void setCursorPositionZero() {
/* 205 */     setCursorPosition(0);
/*     */   }
/*     */   
/*     */   public void setCursorPositionEnd() {
/* 209 */     setCursorPosition(this.text.length());
/*     */   }
/*     */   
/*     */   public boolean textboxKeyTyped(char p_146201_1_, int p_146201_2_) {
/* 213 */     if (!this.isFocused)
/* 214 */       return false;
/* 215 */     if (GuiScreen.isKeyComboCtrlA(p_146201_2_)) {
/* 216 */       setCursorPositionEnd();
/* 217 */       setSelectionPos(0);
/* 218 */       return true; }
/* 219 */     if (GuiScreen.isKeyComboCtrlC(p_146201_2_)) {
/* 220 */       GuiScreen.setClipboardString(getSelectedText());
/* 221 */       return true; }
/* 222 */     if (GuiScreen.isKeyComboCtrlV(p_146201_2_)) {
/* 223 */       if (this.isEnabled) {
/* 224 */         writeText(GuiScreen.getClipboardString());
/*     */       }
/*     */       
/* 227 */       return true; }
/* 228 */     if (GuiScreen.isKeyComboCtrlX(p_146201_2_)) {
/* 229 */       GuiScreen.setClipboardString(getSelectedText());
/* 230 */       if (this.isEnabled) {
/* 231 */         writeText("");
/*     */       }
/*     */       
/* 234 */       return true;
/*     */     }
/* 236 */     switch (p_146201_2_) {
/*     */     case 14: 
/* 238 */       if (GuiScreen.isCtrlKeyDown()) {
/* 239 */         if (this.isEnabled) {
/* 240 */           deleteWords(-1);
/*     */         }
/* 242 */       } else if (this.isEnabled) {
/* 243 */         deleteFromCursor(-1);
/*     */       }
/*     */       
/* 246 */       return true;
/*     */     case 199: 
/* 248 */       if (GuiScreen.isShiftKeyDown()) {
/* 249 */         setSelectionPos(0);
/*     */       } else {
/* 251 */         setCursorPositionZero();
/*     */       }
/*     */       
/* 254 */       return true;
/*     */     case 203: 
/* 256 */       if (GuiScreen.isShiftKeyDown()) {
/* 257 */         if (GuiScreen.isCtrlKeyDown()) {
/* 258 */           setSelectionPos(getNthWordFromPos(-1, getSelectionEnd()));
/*     */         } else {
/* 260 */           setSelectionPos(getSelectionEnd() - 1);
/*     */         }
/* 262 */       } else if (GuiScreen.isCtrlKeyDown()) {
/* 263 */         setCursorPosition(getNthWordFromCursor(-1));
/*     */       } else {
/* 265 */         moveCursorBy(-1);
/*     */       }
/*     */       
/* 268 */       return true;
/*     */     case 205: 
/* 270 */       if (GuiScreen.isShiftKeyDown()) {
/* 271 */         if (GuiScreen.isCtrlKeyDown()) {
/* 272 */           setSelectionPos(getNthWordFromPos(1, getSelectionEnd()));
/*     */         } else {
/* 274 */           setSelectionPos(getSelectionEnd() + 1);
/*     */         }
/* 276 */       } else if (GuiScreen.isCtrlKeyDown()) {
/* 277 */         setCursorPosition(getNthWordFromCursor(1));
/*     */       } else {
/* 279 */         moveCursorBy(1);
/*     */       }
/*     */       
/* 282 */       return true;
/*     */     case 207: 
/* 284 */       if (GuiScreen.isShiftKeyDown()) {
/* 285 */         setSelectionPos(this.text.length());
/*     */       } else {
/* 287 */         setCursorPositionEnd();
/*     */       }
/*     */       
/* 290 */       return true;
/*     */     case 211: 
/* 292 */       if (GuiScreen.isCtrlKeyDown()) {
/* 293 */         if (this.isEnabled) {
/* 294 */           deleteWords(1);
/*     */         }
/* 296 */       } else if (this.isEnabled) {
/* 297 */         deleteFromCursor(1);
/*     */       }
/*     */       
/* 300 */       return true;
/*     */     }
/* 302 */     if (ChatAllowedCharacters.isAllowedCharacter(p_146201_1_)) {
/* 303 */       if (this.isEnabled) {
/* 304 */         writeText(Character.toString(p_146201_1_));
/*     */       }
/*     */       
/* 307 */       return true;
/*     */     }
/* 309 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void mouseClicked(int p_146192_1_, int p_146192_2_, int p_146192_3_)
/*     */   {
/* 316 */     boolean flag = (p_146192_1_ >= this.xPosition) && (p_146192_1_ < this.xPosition + this.width) && (p_146192_2_ >= this.yPosition) && (p_146192_2_ < this.yPosition + this.height);
/* 317 */     if (this.canLoseFocus) {
/* 318 */       setFocused(flag);
/*     */     }
/*     */     
/* 321 */     if ((this.isFocused) && (flag) && (p_146192_3_ == 0)) {
/* 322 */       int i = p_146192_1_ - this.xPosition;
/* 323 */       if (this.enableBackgroundDrawing) {
/* 324 */         i -= 4;
/*     */       }
/*     */       
/* 327 */       String s = this.fontRendererInstance.trimStringToWidth(this.text.substring(this.lineScrollOffset), getWidth());
/* 328 */       setCursorPosition(this.fontRendererInstance.trimStringToWidth(s, i).length() + this.lineScrollOffset);
/*     */     }
/*     */   }
/*     */   
/*     */   public void drawTextBox() {
/* 333 */     if (getVisible()) {
/* 334 */       if (getEnableBackgroundDrawing()) {
/* 335 */         drawRect(this.xPosition - 1, this.yPosition - 1, this.xPosition + this.width + 1, this.yPosition + this.height + 1, -6250336);
/* 336 */         drawRect(this.xPosition, this.yPosition, this.xPosition + this.width, this.yPosition + this.height, -16777216);
/*     */       }
/*     */       
/* 339 */       int i = this.isEnabled ? this.enabledColor : this.disabledColor;
/* 340 */       int j = this.cursorPosition - this.lineScrollOffset;
/* 341 */       int k = this.selectionEnd - this.lineScrollOffset;
/* 342 */       String s = this.fontRendererInstance.trimStringToWidth(this.text.substring(this.lineScrollOffset), getWidth());
/* 343 */       boolean flag = (j >= 0) && (j <= s.length());
/* 344 */       boolean flag1 = (this.isFocused) && (this.cursorCounter / 6 % 2 == 0) && (flag);
/* 345 */       int l = this.enableBackgroundDrawing ? this.xPosition + 4 : this.xPosition;
/* 346 */       int i1 = this.enableBackgroundDrawing ? this.yPosition + (this.height - 8) / 2 : this.yPosition;
/* 347 */       int j1 = l;
/* 348 */       if (k > s.length()) {
/* 349 */         k = s.length();
/*     */       }
/*     */       
/* 352 */       if (s.length() > 0) {
/* 353 */         String s1 = flag ? s.substring(0, j) : s;
/* 354 */         j1 = this.fontRendererInstance.drawStringWithShadow(s1, l, i1, i);
/*     */       }
/*     */       
/* 357 */       boolean flag2 = (this.cursorPosition < this.text.length()) || (this.text.length() >= getMaxStringLength());
/* 358 */       int k1 = j1;
/* 359 */       if (!flag) {
/* 360 */         k1 = j > 0 ? l + this.width : l;
/* 361 */       } else if (flag2) {
/* 362 */         k1 = j1 - 1;
/* 363 */         j1--;
/*     */       }
/*     */       
/* 366 */       if ((s.length() > 0) && (flag) && (j < s.length())) {
/* 367 */         j1 = this.fontRendererInstance.drawStringWithShadow(s.substring(j), j1, i1, i);
/*     */       }
/*     */       
/* 370 */       if (flag1) {
/* 371 */         if (flag2) {
/* 372 */           Gui.drawRect(k1, i1 - 1, k1 + 1, i1 + 1 + this.fontRendererInstance.FONT_HEIGHT, -3092272);
/*     */         } else {
/* 374 */           this.fontRendererInstance.drawStringWithShadow("_", k1, i1, i);
/*     */         }
/*     */       }
/*     */       
/* 378 */       if (k != j) {
/* 379 */         int l1 = l + this.fontRendererInstance.getStringWidth(s.substring(0, k));
/* 380 */         drawCursorVertical(k1, i1 - 1, l1 - 1, i1 + 1 + this.fontRendererInstance.FONT_HEIGHT);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private void drawCursorVertical(int p_146188_1_, int p_146188_2_, int p_146188_3_, int p_146188_4_) {
/* 386 */     if (p_146188_1_ < p_146188_3_) {
/* 387 */       int i = p_146188_1_;
/* 388 */       p_146188_1_ = p_146188_3_;
/* 389 */       p_146188_3_ = i;
/*     */     }
/*     */     
/* 392 */     if (p_146188_2_ < p_146188_4_) {
/* 393 */       int j = p_146188_2_;
/* 394 */       p_146188_2_ = p_146188_4_;
/* 395 */       p_146188_4_ = j;
/*     */     }
/*     */     
/* 398 */     if (p_146188_3_ > this.xPosition + this.width) {
/* 399 */       p_146188_3_ = this.xPosition + this.width;
/*     */     }
/*     */     
/* 402 */     if (p_146188_1_ > this.xPosition + this.width) {
/* 403 */       p_146188_1_ = this.xPosition + this.width;
/*     */     }
/*     */     
/* 406 */     Tessellator tessellator = Tessellator.getInstance();
/* 407 */     WorldRenderer worldrenderer = tessellator.getWorldRenderer();
/* 408 */     GlStateManager.color(0.0F, 0.0F, 255.0F, 255.0F);
/* 409 */     GlStateManager.disableTexture2D();
/* 410 */     GlStateManager.enableColorLogic();
/* 411 */     GlStateManager.colorLogicOp(5387);
/* 412 */     worldrenderer.begin(7, net.minecraft.client.renderer.vertex.DefaultVertexFormats.POSITION);
/* 413 */     worldrenderer.pos(p_146188_1_, p_146188_4_, 0.0D).endVertex();
/* 414 */     worldrenderer.pos(p_146188_3_, p_146188_4_, 0.0D).endVertex();
/* 415 */     worldrenderer.pos(p_146188_3_, p_146188_2_, 0.0D).endVertex();
/* 416 */     worldrenderer.pos(p_146188_1_, p_146188_2_, 0.0D).endVertex();
/* 417 */     tessellator.draw();
/* 418 */     GlStateManager.disableColorLogic();
/* 419 */     GlStateManager.enableTexture2D();
/*     */   }
/*     */   
/*     */   public void setMaxStringLength(int p_146203_1_) {
/* 423 */     this.maxStringLength = p_146203_1_;
/* 424 */     if (this.text.length() > p_146203_1_) {
/* 425 */       this.text = this.text.substring(0, p_146203_1_);
/*     */     }
/*     */   }
/*     */   
/*     */   public int getMaxStringLength() {
/* 430 */     return this.maxStringLength;
/*     */   }
/*     */   
/*     */   public int getCursorPosition() {
/* 434 */     return this.cursorPosition;
/*     */   }
/*     */   
/*     */   public boolean getEnableBackgroundDrawing() {
/* 438 */     return this.enableBackgroundDrawing;
/*     */   }
/*     */   
/*     */   public void setEnableBackgroundDrawing(boolean p_146185_1_) {
/* 442 */     this.enableBackgroundDrawing = p_146185_1_;
/*     */   }
/*     */   
/*     */   public void setTextColor(int p_146193_1_) {
/* 446 */     this.enabledColor = p_146193_1_;
/*     */   }
/*     */   
/*     */   public void setDisabledTextColour(int p_146204_1_) {
/* 450 */     this.disabledColor = p_146204_1_;
/*     */   }
/*     */   
/*     */   public void setFocused(boolean p_146195_1_) {
/* 454 */     if ((p_146195_1_) && (!this.isFocused)) {
/* 455 */       this.cursorCounter = 0;
/*     */     }
/*     */     
/* 458 */     this.isFocused = p_146195_1_;
/*     */   }
/*     */   
/*     */   public boolean isFocused() {
/* 462 */     return this.isFocused;
/*     */   }
/*     */   
/*     */   public void setEnabled(boolean p_146184_1_) {
/* 466 */     this.isEnabled = p_146184_1_;
/*     */   }
/*     */   
/*     */   public int getSelectionEnd() {
/* 470 */     return this.selectionEnd;
/*     */   }
/*     */   
/*     */   public int getWidth() {
/* 474 */     return getEnableBackgroundDrawing() ? this.width - 8 : this.width;
/*     */   }
/*     */   
/*     */   public void setSelectionPos(int p_146199_1_) {
/* 478 */     int i = this.text.length();
/* 479 */     if (p_146199_1_ > i) {
/* 480 */       p_146199_1_ = i;
/*     */     }
/*     */     
/* 483 */     if (p_146199_1_ < 0) {
/* 484 */       p_146199_1_ = 0;
/*     */     }
/*     */     
/* 487 */     this.selectionEnd = p_146199_1_;
/* 488 */     if (this.fontRendererInstance != null) {
/* 489 */       if (this.lineScrollOffset > i) {
/* 490 */         this.lineScrollOffset = i;
/*     */       }
/*     */       
/* 493 */       int j = getWidth();
/* 494 */       String s = this.fontRendererInstance.trimStringToWidth(this.text.substring(this.lineScrollOffset), j);
/* 495 */       int k = s.length() + this.lineScrollOffset;
/* 496 */       if (p_146199_1_ == this.lineScrollOffset) {
/* 497 */         this.lineScrollOffset -= this.fontRendererInstance.trimStringToWidth(this.text, j, true).length();
/*     */       }
/*     */       
/* 500 */       if (p_146199_1_ > k) {
/* 501 */         this.lineScrollOffset += p_146199_1_ - k;
/* 502 */       } else if (p_146199_1_ <= this.lineScrollOffset) {
/* 503 */         this.lineScrollOffset -= this.lineScrollOffset - p_146199_1_;
/*     */       }
/*     */       
/* 506 */       this.lineScrollOffset = MathHelper.clamp_int(this.lineScrollOffset, 0, i);
/*     */     }
/*     */   }
/*     */   
/*     */   public void setCanLoseFocus(boolean p_146205_1_) {
/* 511 */     this.canLoseFocus = p_146205_1_;
/*     */   }
/*     */   
/*     */   public boolean getVisible() {
/* 515 */     return this.visible;
/*     */   }
/*     */   
/*     */   public void setVisible(boolean p_146189_1_) {
/* 519 */     this.visible = p_146189_1_;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\gui\GuiTextField.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */