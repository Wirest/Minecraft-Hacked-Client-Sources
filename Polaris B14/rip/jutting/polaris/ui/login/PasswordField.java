/*     */ package rip.jutting.polaris.ui.login;
/*     */ 
/*     */ import net.minecraft.client.gui.FontRenderer;
/*     */ import net.minecraft.client.gui.GuiScreen;
/*     */ import net.minecraft.client.renderer.WorldRenderer;
/*     */ 
/*     */ public class PasswordField extends net.minecraft.client.gui.Gui
/*     */ {
/*     */   private final FontRenderer fontRenderer;
/*     */   private final int xPos;
/*     */   private final int yPos;
/*     */   private final int width;
/*     */   private final int height;
/*     */   private String text;
/*     */   private int maxStringLength;
/*     */   private int cursorCounter;
/*     */   private boolean enableBackgroundDrawing;
/*     */   private boolean canLoseFocus;
/*     */   public boolean isFocused;
/*     */   private boolean isEnabled;
/*     */   private int i;
/*     */   private int cursorPosition;
/*     */   private int selectionEnd;
/*     */   private int enabledColor;
/*     */   private int disabledColor;
/*     */   private boolean b;
/*     */   
/*     */   public PasswordField(FontRenderer par1FontRenderer, int par2, int par3, int par4, int par5)
/*     */   {
/*  30 */     this.text = "";
/*  31 */     this.maxStringLength = 50;
/*  32 */     this.enableBackgroundDrawing = true;
/*  33 */     this.canLoseFocus = true;
/*  34 */     this.isFocused = false;
/*  35 */     this.isEnabled = true;
/*  36 */     this.i = 0;
/*  37 */     this.cursorPosition = 0;
/*  38 */     this.selectionEnd = 0;
/*  39 */     this.enabledColor = 14737632;
/*  40 */     this.disabledColor = 7368816;
/*  41 */     this.b = true;
/*  42 */     this.fontRenderer = par1FontRenderer;
/*  43 */     this.xPos = par2;
/*  44 */     this.yPos = par3;
/*  45 */     this.width = par4;
/*  46 */     this.height = par5;
/*     */   }
/*     */   
/*     */   public void updateCursorCounter() {
/*  50 */     this.cursorCounter += 1;
/*     */   }
/*     */   
/*     */   public void setText(String par1Str) {
/*  54 */     if (par1Str.length() > this.maxStringLength) {
/*  55 */       this.text = par1Str.substring(0, this.maxStringLength);
/*     */     }
/*     */     else {
/*  58 */       this.text = par1Str;
/*     */     }
/*  60 */     setCursorPositionEnd();
/*     */   }
/*     */   
/*     */   public String getText() {
/*  64 */     String newtext = this.text.replaceAll(" ", "");
/*  65 */     return newtext;
/*     */   }
/*     */   
/*     */   public String getSelectedtext() {
/*  69 */     int var1 = this.cursorPosition < this.selectionEnd ? this.cursorPosition : this.selectionEnd;
/*  70 */     int var2 = this.cursorPosition < this.selectionEnd ? this.selectionEnd : this.cursorPosition;
/*  71 */     return this.text.substring(var1, var2);
/*     */   }
/*     */   
/*     */   public void writeText(String par1Str) {
/*  75 */     String var2 = "";
/*  76 */     String var3 = net.minecraft.util.ChatAllowedCharacters.filterAllowedCharacters(par1Str);
/*  77 */     int var4 = this.cursorPosition < this.selectionEnd ? this.cursorPosition : this.selectionEnd;
/*  78 */     int var5 = this.cursorPosition < this.selectionEnd ? this.selectionEnd : this.cursorPosition;
/*  79 */     int var6 = this.maxStringLength - this.text.length() - (var4 - this.selectionEnd);
/*  80 */     boolean var7 = false;
/*  81 */     if (this.text.length() > 0)
/*  82 */       var2 = String.valueOf(String.valueOf(var2)) + this.text.substring(0, var4);
/*     */     int var8;
/*     */     int var8;
/*  85 */     if (var6 < var3.length()) {
/*  86 */       var2 = String.valueOf(String.valueOf(var2)) + var3.substring(0, var6);
/*  87 */       var8 = var6;
/*     */     }
/*     */     else {
/*  90 */       var2 = String.valueOf(String.valueOf(var2)) + var3;
/*  91 */       var8 = var3.length();
/*     */     }
/*  93 */     if ((this.text.length() > 0) && (var5 < this.text.length())) {
/*  94 */       var2 = String.valueOf(String.valueOf(var2)) + this.text.substring(var5);
/*     */     }
/*  96 */     this.text = var2.replaceAll(" ", "");
/*  97 */     cursorPos(var4 - this.selectionEnd + var8);
/*     */   }
/*     */   
/*     */   public void func_73779_a(int par1) {
/* 101 */     if (this.text.length() != 0) {
/* 102 */       if (this.selectionEnd != this.cursorPosition) {
/* 103 */         writeText("");
/*     */       }
/*     */       else {
/* 106 */         deleteFromCursor(getNthWordFromCursor(par1) - this.cursorPosition);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void deleteFromCursor(int par1) {
/* 112 */     if (this.text.length() != 0) {
/* 113 */       if (this.selectionEnd != this.cursorPosition) {
/* 114 */         writeText("");
/*     */       }
/*     */       else {
/* 117 */         boolean var2 = par1 < 0;
/* 118 */         int var3 = var2 ? this.cursorPosition + par1 : this.cursorPosition;
/* 119 */         int var4 = var2 ? this.cursorPosition : this.cursorPosition + par1;
/* 120 */         String var5 = "";
/* 121 */         if (var3 >= 0) {
/* 122 */           var5 = this.text.substring(0, var3);
/*     */         }
/* 124 */         if (var4 < this.text.length()) {
/* 125 */           var5 = String.valueOf(String.valueOf(var5)) + this.text.substring(var4);
/*     */         }
/* 127 */         this.text = var5;
/* 128 */         if (var2) {
/* 129 */           cursorPos(par1);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public int getNthWordFromCursor(int par1) {
/* 136 */     return getNthWordFromPos(par1, getCursorPosition());
/*     */   }
/*     */   
/*     */   public int getNthWordFromPos(int par1, int par2) {
/* 140 */     return type(par1, getCursorPosition(), true);
/*     */   }
/*     */   
/*     */   public int type(int par1, int par2, boolean par3) {
/* 144 */     int var4 = par2;
/* 145 */     boolean var5 = par1 < 0;
/* 146 */     int var6 = Math.abs(par1); for (int var7 = 0; var7 < var6; var7++) {
/* 147 */       if (!var5) {
/* 148 */         var8 = this.text.length();
/* 149 */         var4 = this.text.indexOf(' ', var4);
/* 150 */         if (var4 == -1) {
/* 151 */           var4 = var8;
/*     */         }
/*     */         else {
/* 154 */           while (par3) {
/* 155 */             if (var4 >= var8) {
/*     */               break;
/*     */             }
/* 158 */             if (this.text.charAt(var4) != ' ') {
/*     */               break;
/*     */             }
/* 161 */             var4++;
/*     */           }
/*     */         }
/*     */       }
/*     */       else {
/* 166 */         while (par3) { int var8;
/* 167 */           if (var4 <= 0) {
/*     */             break;
/*     */           }
/* 170 */           if (this.text.charAt(var4 - 1) != ' ') {
/*     */             break;
/*     */           }
/* 173 */           var4--;
/*     */         }
/* 175 */         while ((var4 > 0) && (this.text.charAt(var4 - 1) != ' ')) {
/* 176 */           var4--;
/*     */         }
/*     */       }
/*     */     }
/* 180 */     return var4;
/*     */   }
/*     */   
/*     */   public void cursorPos(int par1) {
/* 184 */     setCursorPosition(this.selectionEnd + par1);
/*     */   }
/*     */   
/*     */   public void setCursorPosition(int par1) {
/* 188 */     this.cursorPosition = par1;
/* 189 */     int var2 = this.text.length();
/* 190 */     if (this.cursorPosition < 0) {
/* 191 */       this.cursorPosition = 0;
/*     */     }
/* 193 */     if (this.cursorPosition > var2) {
/* 194 */       this.cursorPosition = var2;
/*     */     }
/* 196 */     func_73800_i(this.cursorPosition);
/*     */   }
/*     */   
/*     */   public void setCursorPositionZero() {
/* 200 */     setCursorPosition(0);
/*     */   }
/*     */   
/*     */   public void setCursorPositionEnd() {
/* 204 */     setCursorPosition(this.text.length());
/*     */   }
/*     */   
/*     */   public boolean textboxKeyTyped(char par1, int par2) {
/* 208 */     if ((!this.isEnabled) || (!this.isFocused)) {
/* 209 */       return false;
/*     */     }
/* 211 */     switch (par1) {
/*     */     case '\001': 
/* 213 */       setCursorPositionEnd();
/* 214 */       func_73800_i(0);
/* 215 */       return true;
/*     */     
/*     */     case '\003': 
/* 218 */       GuiScreen.setClipboardString(getSelectedtext());
/* 219 */       return true;
/*     */     
/*     */     case '\026': 
/* 222 */       writeText(GuiScreen.getClipboardString());
/* 223 */       return true;
/*     */     
/*     */     case '\030': 
/* 226 */       GuiScreen.setClipboardString(getSelectedtext());
/* 227 */       writeText("");
/* 228 */       return true;
/*     */     }
/*     */     
/* 231 */     switch (par2) {
/*     */     case 14: 
/* 233 */       if (GuiScreen.isCtrlKeyDown()) {
/* 234 */         func_73779_a(-1);
/*     */       }
/*     */       else {
/* 237 */         deleteFromCursor(-1);
/*     */       }
/* 239 */       return true;
/*     */     
/*     */     case 199: 
/* 242 */       if (GuiScreen.isShiftKeyDown()) {
/* 243 */         func_73800_i(0);
/*     */       }
/*     */       else {
/* 246 */         setCursorPositionZero();
/*     */       }
/* 248 */       return true;
/*     */     
/*     */     case 203: 
/* 251 */       if (GuiScreen.isShiftKeyDown()) {
/* 252 */         if (GuiScreen.isCtrlKeyDown()) {
/* 253 */           func_73800_i(getNthWordFromPos(-1, getSelectionEnd()));
/*     */         }
/*     */         else {
/* 256 */           func_73800_i(getSelectionEnd() - 1);
/*     */         }
/*     */       }
/* 259 */       else if (GuiScreen.isCtrlKeyDown()) {
/* 260 */         setCursorPosition(getNthWordFromCursor(-1));
/*     */       }
/*     */       else {
/* 263 */         cursorPos(-1);
/*     */       }
/* 265 */       return true;
/*     */     
/*     */     case 205: 
/* 268 */       if (GuiScreen.isShiftKeyDown()) {
/* 269 */         if (GuiScreen.isCtrlKeyDown()) {
/* 270 */           func_73800_i(getNthWordFromPos(1, getSelectionEnd()));
/*     */         }
/*     */         else {
/* 273 */           func_73800_i(getSelectionEnd() + 1);
/*     */         }
/*     */       }
/* 276 */       else if (GuiScreen.isCtrlKeyDown()) {
/* 277 */         setCursorPosition(getNthWordFromCursor(1));
/*     */       }
/*     */       else {
/* 280 */         cursorPos(1);
/*     */       }
/* 282 */       return true;
/*     */     
/*     */     case 207: 
/* 285 */       if (GuiScreen.isShiftKeyDown()) {
/* 286 */         func_73800_i(this.text.length());
/*     */       }
/*     */       else {
/* 289 */         setCursorPositionEnd();
/*     */       }
/* 291 */       return true;
/*     */     
/*     */     case 211: 
/* 294 */       if (GuiScreen.isCtrlKeyDown()) {
/* 295 */         func_73779_a(1);
/*     */       }
/*     */       else {
/* 298 */         deleteFromCursor(1);
/*     */       }
/* 300 */       return true;
/*     */     }
/*     */     
/* 303 */     if (net.minecraft.util.ChatAllowedCharacters.isAllowedCharacter(par1)) {
/* 304 */       writeText(Character.toString(par1));
/* 305 */       return true;
/*     */     }
/* 307 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void mouseClicked(int par1, int par2, int par3)
/*     */   {
/* 315 */     boolean var4 = (par1 >= this.xPos) && (par1 < this.xPos + this.width) && (par2 >= this.yPos) && (par2 < this.yPos + this.height);
/* 316 */     if (this.canLoseFocus) {
/* 317 */       setFocused((this.isEnabled) && (var4));
/*     */     }
/* 319 */     if ((this.isFocused) && (par3 == 0)) {
/* 320 */       int var5 = par1 - this.xPos;
/* 321 */       if (this.enableBackgroundDrawing) {
/* 322 */         var5 -= 4;
/*     */       }
/* 324 */       String var6 = this.fontRenderer.trimStringToWidth(this.text.substring(this.i), getWidth());
/* 325 */       setCursorPosition(this.fontRenderer.trimStringToWidth(var6, var5).length() + this.i);
/*     */     }
/*     */   }
/*     */   
/*     */   public void drawTextBox() {
/* 330 */     if (func_73778_q()) {
/* 331 */       if (getEnableBackgroundDrawing()) {
/* 332 */         net.minecraft.client.gui.Gui.drawRect(this.xPos - 1, this.yPos - 1, this.xPos + this.width + 1, this.yPos + this.height + 1, -6250336);
/* 333 */         net.minecraft.client.gui.Gui.drawRect(this.xPos, this.yPos, this.xPos + this.width, this.yPos + this.height, -16777216);
/*     */       }
/* 335 */       int var1 = this.isEnabled ? this.enabledColor : this.disabledColor;
/* 336 */       int var2 = this.cursorPosition - this.i;
/* 337 */       int var3 = this.selectionEnd - this.i;
/* 338 */       String var4 = this.fontRenderer.trimStringToWidth(this.text.substring(this.i), getWidth());
/* 339 */       boolean var5 = (var2 >= 0) && (var2 <= var4.length());
/* 340 */       boolean var6 = (this.isFocused) && (this.cursorCounter / 6 % 2 == 0) && (var5);
/* 341 */       int var7 = this.enableBackgroundDrawing ? this.xPos + 4 : this.xPos;
/* 342 */       int var8 = this.enableBackgroundDrawing ? this.yPos + (this.height - 8) / 2 : this.yPos;
/* 343 */       int var9 = var7;
/* 344 */       if (var3 > var4.length()) {
/* 345 */         var3 = var4.length();
/*     */       }
/* 347 */       if (var4.length() > 0) {
/* 348 */         if (var5) {
/* 349 */           var4.substring(0, var2);
/*     */         }
/* 351 */         var9 = net.minecraft.client.Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(this.text.replaceAll("(?s).", "*"), var7, var8, var1);
/*     */       }
/* 353 */       boolean var10 = (this.cursorPosition < this.text.length()) || (this.text.length() >= getMaxStringLength());
/* 354 */       int var11 = var9;
/* 355 */       if (!var5) {
/* 356 */         var11 = var2 > 0 ? var7 + this.width : var7;
/*     */       }
/* 358 */       else if (var10) {
/* 359 */         var11 = var9 - 1;
/* 360 */         var9--;
/*     */       }
/* 362 */       if ((var4.length() > 0) && (var5) && (var2 < var4.length())) {
/* 363 */         net.minecraft.client.Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(var4.substring(var2), var9, var8, var1);
/*     */       }
/* 365 */       if (var6) {
/* 366 */         if (var10) {
/* 367 */           net.minecraft.client.gui.Gui.drawRect(var11, var8 - 1, var11 + 1, var8 + 1 + this.fontRenderer.FONT_HEIGHT, -3092272);
/*     */         }
/*     */         else {
/* 370 */           net.minecraft.client.Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow("_", var11, var8, var1);
/*     */         }
/*     */       }
/* 373 */       if (var3 != var2) {
/* 374 */         int var12 = var7 + this.fontRenderer.getStringWidth(var4.substring(0, var3));
/* 375 */         drawCursorVertical(var11, var8 - 1, var12 - 1, var8 + 1 + this.fontRenderer.FONT_HEIGHT);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private void drawCursorVertical(int par1, int par2, int par3, int par4) {
/* 381 */     if (par1 < par3) {
/* 382 */       int var5 = par1;
/* 383 */       par1 = par3;
/* 384 */       par3 = var5;
/*     */     }
/* 386 */     if (par2 < par4) {
/* 387 */       int var5 = par2;
/* 388 */       par2 = par4;
/* 389 */       par4 = var5;
/*     */     }
/* 391 */     net.minecraft.client.renderer.Tessellator var6 = net.minecraft.client.renderer.Tessellator.getInstance();
/* 392 */     WorldRenderer var7 = var6.getWorldRenderer();
/* 393 */     org.lwjgl.opengl.GL11.glColor4f(0.0F, 0.0F, 255.0F, 255.0F);
/* 394 */     org.lwjgl.opengl.GL11.glDisable(3553);
/* 395 */     org.lwjgl.opengl.GL11.glEnable(3058);
/* 396 */     org.lwjgl.opengl.GL11.glLogicOp(5387);
/* 397 */     var7.begin(7, var7.getVertexFormat());
/* 398 */     var7.pos(par1, par4, 0.0D);
/* 399 */     var7.pos(par3, par4, 0.0D);
/* 400 */     var7.pos(par3, par2, 0.0D);
/* 401 */     var7.pos(par1, par2, 0.0D);
/* 402 */     var7.finishDrawing();
/* 403 */     org.lwjgl.opengl.GL11.glDisable(3058);
/* 404 */     org.lwjgl.opengl.GL11.glEnable(3553);
/*     */   }
/*     */   
/*     */   public void setMaxStringLength(int par1) {
/* 408 */     this.maxStringLength = par1;
/* 409 */     if (this.text.length() > par1) {
/* 410 */       this.text = this.text.substring(0, par1);
/*     */     }
/*     */   }
/*     */   
/*     */   public int getMaxStringLength() {
/* 415 */     return this.maxStringLength;
/*     */   }
/*     */   
/*     */   public int getCursorPosition() {
/* 419 */     return this.cursorPosition;
/*     */   }
/*     */   
/*     */   public boolean getEnableBackgroundDrawing() {
/* 423 */     return this.enableBackgroundDrawing;
/*     */   }
/*     */   
/*     */   public void setEnableBackgroundDrawing(boolean par1) {
/* 427 */     this.enableBackgroundDrawing = par1;
/*     */   }
/*     */   
/*     */   public void func_73794_g(int par1) {
/* 431 */     this.enabledColor = par1;
/*     */   }
/*     */   
/*     */   public void setFocused(boolean par1) {
/* 435 */     if ((par1) && (!this.isFocused)) {
/* 436 */       this.cursorCounter = 0;
/*     */     }
/* 438 */     this.isFocused = par1;
/*     */   }
/*     */   
/*     */   public boolean isFocused() {
/* 442 */     return this.isFocused;
/*     */   }
/*     */   
/*     */   public int getSelectionEnd() {
/* 446 */     return this.selectionEnd;
/*     */   }
/*     */   
/*     */   public int getWidth() {
/* 450 */     return getEnableBackgroundDrawing() ? this.width - 8 : this.width;
/*     */   }
/*     */   
/*     */   public void func_73800_i(int par1) {
/* 454 */     int var2 = this.text.length();
/* 455 */     if (par1 > var2) {
/* 456 */       par1 = var2;
/*     */     }
/* 458 */     if (par1 < 0) {
/* 459 */       par1 = 0;
/*     */     }
/* 461 */     this.selectionEnd = par1;
/* 462 */     if (this.fontRenderer != null) {
/* 463 */       if (this.i > var2) {
/* 464 */         this.i = var2;
/*     */       }
/* 466 */       int var3 = getWidth();
/* 467 */       String var4 = this.fontRenderer.trimStringToWidth(this.text.substring(this.i), var3);
/* 468 */       int var5 = var4.length() + this.i;
/* 469 */       if (par1 == this.i) {
/* 470 */         this.i -= this.fontRenderer.trimStringToWidth(this.text, var3, true).length();
/*     */       }
/* 472 */       if (par1 > var5) {
/* 473 */         this.i += par1 - var5;
/*     */       }
/* 475 */       else if (par1 <= this.i) {
/* 476 */         this.i -= this.i - par1;
/*     */       }
/* 478 */       if (this.i < 0) {
/* 479 */         this.i = 0;
/*     */       }
/* 481 */       if (this.i > var2) {
/* 482 */         this.i = var2;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void setCanLoseFocus(boolean par1) {
/* 488 */     this.canLoseFocus = par1;
/*     */   }
/*     */   
/*     */   public boolean func_73778_q() {
/* 492 */     return this.b;
/*     */   }
/*     */   
/*     */   public void func_73790_e(boolean par1) {
/* 496 */     this.b = par1;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\rip\jutting\polaris\ui\login\PasswordField.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */