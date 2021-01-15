/*     */ package rip.jutting.polaris.ui.altmanager;
/*     */ 
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.FontRenderer;
/*     */ import net.minecraft.client.gui.Gui;
/*     */ import net.minecraft.client.gui.GuiScreen;
/*     */ import net.minecraft.client.renderer.Tessellator;
/*     */ import net.minecraft.client.renderer.WorldRenderer;
/*     */ import net.minecraft.util.ChatAllowedCharacters;
/*     */ import org.lwjgl.opengl.GL11;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PasswordField
/*     */   extends Gui
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
/*  39 */     this.text = "";
/*  40 */     this.maxStringLength = 50;
/*  41 */     this.enableBackgroundDrawing = true;
/*  42 */     this.canLoseFocus = true;
/*  43 */     this.isFocused = false;
/*  44 */     this.isEnabled = true;
/*  45 */     this.i = 0;
/*  46 */     this.cursorPosition = 0;
/*  47 */     this.selectionEnd = 0;
/*  48 */     this.enabledColor = 14737632;
/*  49 */     this.disabledColor = 7368816;
/*  50 */     this.b = true;
/*  51 */     this.fontRenderer = par1FontRenderer;
/*  52 */     this.xPos = par2;
/*  53 */     this.yPos = par3;
/*  54 */     this.width = par4;
/*  55 */     this.height = par5;
/*     */   }
/*     */   
/*     */   public void updateCursorCounter() {
/*  59 */     this.cursorCounter += 1;
/*     */   }
/*     */   
/*     */   public void setText(String par1Str) {
/*  63 */     if (par1Str.length() > this.maxStringLength) {
/*  64 */       this.text = par1Str.substring(0, this.maxStringLength);
/*     */     } else {
/*  66 */       this.text = par1Str;
/*     */     }
/*  68 */     setCursorPositionEnd();
/*     */   }
/*     */   
/*     */   public String getText() {
/*  72 */     String newtext = this.text.replaceAll(" ", "");
/*  73 */     return newtext;
/*     */   }
/*     */   
/*     */   public String getSelectedtext() {
/*  77 */     int var1 = this.cursorPosition < this.selectionEnd ? this.cursorPosition : this.selectionEnd;
/*  78 */     int var2 = this.cursorPosition < this.selectionEnd ? this.selectionEnd : this.cursorPosition;
/*  79 */     return this.text.substring(var1, var2);
/*     */   }
/*     */   
/*     */   public void writeText(String par1Str) {
/*  83 */     String var2 = "";
/*  84 */     String var3 = ChatAllowedCharacters.filterAllowedCharacters(par1Str);
/*  85 */     int var4 = this.cursorPosition < this.selectionEnd ? this.cursorPosition : this.selectionEnd;
/*  86 */     int var5 = this.cursorPosition < this.selectionEnd ? this.selectionEnd : this.cursorPosition;
/*  87 */     int var6 = this.maxStringLength - this.text.length() - (var4 - this.selectionEnd);
/*  88 */     boolean var7 = false;
/*  89 */     if (this.text.length() > 0)
/*  90 */       var2 = String.valueOf(String.valueOf(var2)) + this.text.substring(0, var4);
/*     */     int var8;
/*     */     int var8;
/*  93 */     if (var6 < var3.length()) {
/*  94 */       var2 = String.valueOf(String.valueOf(var2)) + var3.substring(0, var6);
/*  95 */       var8 = var6;
/*     */     } else {
/*  97 */       var2 = String.valueOf(String.valueOf(var2)) + var3;
/*  98 */       var8 = var3.length();
/*     */     }
/* 100 */     if ((this.text.length() > 0) && (var5 < this.text.length())) {
/* 101 */       var2 = String.valueOf(String.valueOf(var2)) + this.text.substring(var5);
/*     */     }
/* 103 */     this.text = var2.replaceAll(" ", "");
/* 104 */     cursorPos(var4 - this.selectionEnd + var8);
/*     */   }
/*     */   
/*     */   public void func_73779_a(int par1) {
/* 108 */     if (this.text.length() != 0) {
/* 109 */       if (this.selectionEnd != this.cursorPosition) {
/* 110 */         writeText("");
/*     */       } else {
/* 112 */         deleteFromCursor(getNthWordFromCursor(par1) - this.cursorPosition);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void deleteFromCursor(int par1) {
/* 118 */     if (this.text.length() != 0) {
/* 119 */       if (this.selectionEnd != this.cursorPosition) {
/* 120 */         writeText("");
/*     */       } else {
/* 122 */         boolean var2 = par1 < 0;
/* 123 */         int var3 = var2 ? this.cursorPosition + par1 : this.cursorPosition;
/* 124 */         int var4 = var2 ? this.cursorPosition : this.cursorPosition + par1;
/* 125 */         String var5 = "";
/* 126 */         if (var3 >= 0) {
/* 127 */           var5 = this.text.substring(0, var3);
/*     */         }
/* 129 */         if (var4 < this.text.length()) {
/* 130 */           var5 = String.valueOf(String.valueOf(var5)) + this.text.substring(var4);
/*     */         }
/* 132 */         this.text = var5;
/* 133 */         if (var2) {
/* 134 */           cursorPos(par1);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public int getNthWordFromCursor(int par1) {
/* 141 */     return getNthWordFromPos(par1, getCursorPosition());
/*     */   }
/*     */   
/*     */   public int getNthWordFromPos(int par1, int par2) {
/* 145 */     return type(par1, getCursorPosition(), true);
/*     */   }
/*     */   
/*     */   public int type(int par1, int par2, boolean par3) {
/* 149 */     int var4 = par2;
/* 150 */     boolean var5 = par1 < 0;
/* 151 */     int var6 = Math.abs(par1); for (int var7 = 0; var7 < var6; var7++) {
/* 152 */       if (!var5) {
/* 153 */         var8 = this.text.length();
/* 154 */         var4 = this.text.indexOf(' ', var4);
/* 155 */         if (var4 == -1) {
/* 156 */           var4 = var8;
/*     */         } else {
/* 158 */           while (par3) {
/* 159 */             if (var4 >= var8) {
/*     */               break;
/*     */             }
/* 162 */             if (this.text.charAt(var4) != ' ') {
/*     */               break;
/*     */             }
/* 165 */             var4++;
/*     */           }
/*     */         }
/*     */       } else {
/* 169 */         while (par3) { int var8;
/* 170 */           if (var4 <= 0) {
/*     */             break;
/*     */           }
/* 173 */           if (this.text.charAt(var4 - 1) != ' ') {
/*     */             break;
/*     */           }
/* 176 */           var4--;
/*     */         }
/* 178 */         while ((var4 > 0) && (this.text.charAt(var4 - 1) != ' ')) {
/* 179 */           var4--;
/*     */         }
/*     */       }
/*     */     }
/* 183 */     return var4;
/*     */   }
/*     */   
/*     */   public void cursorPos(int par1) {
/* 187 */     setCursorPosition(this.selectionEnd + par1);
/*     */   }
/*     */   
/*     */   public void setCursorPosition(int par1) {
/* 191 */     this.cursorPosition = par1;
/* 192 */     int var2 = this.text.length();
/* 193 */     if (this.cursorPosition < 0) {
/* 194 */       this.cursorPosition = 0;
/*     */     }
/* 196 */     if (this.cursorPosition > var2) {
/* 197 */       this.cursorPosition = var2;
/*     */     }
/* 199 */     func_73800_i(this.cursorPosition);
/*     */   }
/*     */   
/*     */   public void setCursorPositionZero() {
/* 203 */     setCursorPosition(0);
/*     */   }
/*     */   
/*     */   public void setCursorPositionEnd() {
/* 207 */     setCursorPosition(this.text.length());
/*     */   }
/*     */   
/*     */   public boolean textboxKeyTyped(char par1, int par2) {
/* 211 */     if ((!this.isEnabled) || (!this.isFocused)) {
/* 212 */       return false;
/*     */     }
/* 214 */     switch (par1) {
/*     */     case '\001': 
/* 216 */       setCursorPositionEnd();
/* 217 */       func_73800_i(0);
/* 218 */       return true;
/*     */     
/*     */     case '\003': 
/* 221 */       GuiScreen.setClipboardString(getSelectedtext());
/* 222 */       return true;
/*     */     
/*     */     case '\026': 
/* 225 */       writeText(GuiScreen.getClipboardString());
/* 226 */       return true;
/*     */     
/*     */     case '\030': 
/* 229 */       GuiScreen.setClipboardString(getSelectedtext());
/* 230 */       writeText("");
/* 231 */       return true;
/*     */     }
/*     */     
/* 234 */     switch (par2) {
/*     */     case 14: 
/* 236 */       if (GuiScreen.isCtrlKeyDown()) {
/* 237 */         func_73779_a(-1);
/*     */       } else {
/* 239 */         deleteFromCursor(-1);
/*     */       }
/* 241 */       return true;
/*     */     
/*     */     case 199: 
/* 244 */       if (GuiScreen.isShiftKeyDown()) {
/* 245 */         func_73800_i(0);
/*     */       } else {
/* 247 */         setCursorPositionZero();
/*     */       }
/* 249 */       return true;
/*     */     
/*     */     case 203: 
/* 252 */       if (GuiScreen.isShiftKeyDown()) {
/* 253 */         if (GuiScreen.isCtrlKeyDown()) {
/* 254 */           func_73800_i(getNthWordFromPos(-1, getSelectionEnd()));
/*     */         } else {
/* 256 */           func_73800_i(getSelectionEnd() - 1);
/*     */         }
/* 258 */       } else if (GuiScreen.isCtrlKeyDown()) {
/* 259 */         setCursorPosition(getNthWordFromCursor(-1));
/*     */       } else {
/* 261 */         cursorPos(-1);
/*     */       }
/* 263 */       return true;
/*     */     
/*     */     case 205: 
/* 266 */       if (GuiScreen.isShiftKeyDown()) {
/* 267 */         if (GuiScreen.isCtrlKeyDown()) {
/* 268 */           func_73800_i(getNthWordFromPos(1, getSelectionEnd()));
/*     */         } else {
/* 270 */           func_73800_i(getSelectionEnd() + 1);
/*     */         }
/* 272 */       } else if (GuiScreen.isCtrlKeyDown()) {
/* 273 */         setCursorPosition(getNthWordFromCursor(1));
/*     */       } else {
/* 275 */         cursorPos(1);
/*     */       }
/* 277 */       return true;
/*     */     
/*     */     case 207: 
/* 280 */       if (GuiScreen.isShiftKeyDown()) {
/* 281 */         func_73800_i(this.text.length());
/*     */       } else {
/* 283 */         setCursorPositionEnd();
/*     */       }
/* 285 */       return true;
/*     */     
/*     */     case 211: 
/* 288 */       if (GuiScreen.isCtrlKeyDown()) {
/* 289 */         func_73779_a(1);
/*     */       } else {
/* 291 */         deleteFromCursor(1);
/*     */       }
/* 293 */       return true;
/*     */     }
/*     */     
/* 296 */     if (ChatAllowedCharacters.isAllowedCharacter(par1)) {
/* 297 */       writeText(Character.toString(par1));
/* 298 */       return true;
/*     */     }
/* 300 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void mouseClicked(int par1, int par2, int par3)
/*     */   {
/* 308 */     boolean var4 = (par1 >= this.xPos) && (par1 < this.xPos + this.width) && (par2 >= this.yPos) && (
/* 309 */       par2 < this.yPos + this.height);
/* 310 */     if (this.canLoseFocus) {
/* 311 */       setFocused((this.isEnabled) && (var4));
/*     */     }
/* 313 */     if ((this.isFocused) && (par3 == 0)) {
/* 314 */       int var5 = par1 - this.xPos;
/* 315 */       if (this.enableBackgroundDrawing) {
/* 316 */         var5 -= 4;
/*     */       }
/* 318 */       String var6 = this.fontRenderer.trimStringToWidth(this.text.substring(this.i), getWidth());
/* 319 */       setCursorPosition(this.fontRenderer.trimStringToWidth(var6, var5).length() + this.i);
/*     */     }
/*     */   }
/*     */   
/*     */   public void drawTextBox() {
/* 324 */     if (func_73778_q()) {
/* 325 */       if (getEnableBackgroundDrawing()) {
/* 326 */         Gui.drawRect(this.xPos - 1, this.yPos - 1, this.xPos + this.width + 1, this.yPos + this.height + 1, 
/* 327 */           -6250336);
/* 328 */         Gui.drawRect(this.xPos, this.yPos, this.xPos + this.width, this.yPos + this.height, -16777216);
/*     */       }
/* 330 */       int var1 = this.isEnabled ? this.enabledColor : this.disabledColor;
/* 331 */       int var2 = this.cursorPosition - this.i;
/* 332 */       int var3 = this.selectionEnd - this.i;
/* 333 */       String var4 = this.fontRenderer.trimStringToWidth(this.text.substring(this.i), getWidth());
/* 334 */       boolean var5 = (var2 >= 0) && (var2 <= var4.length());
/* 335 */       boolean var6 = (this.isFocused) && (this.cursorCounter / 6 % 2 == 0) && (var5);
/* 336 */       int var7 = this.enableBackgroundDrawing ? this.xPos + 4 : this.xPos;
/* 337 */       int var8 = this.enableBackgroundDrawing ? this.yPos + (this.height - 8) / 2 : this.yPos;
/* 338 */       int var9 = var7;
/* 339 */       if (var3 > var4.length()) {
/* 340 */         var3 = var4.length();
/*     */       }
/* 342 */       if (var4.length() > 0) {
/* 343 */         if (var5) {
/* 344 */           var4.substring(0, var2);
/*     */         }
/* 346 */         var9 = Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(this.text.replaceAll("(?s).", "*"), 
/* 347 */           var7, var8, var1);
/*     */       }
/* 349 */       boolean var10 = (this.cursorPosition < this.text.length()) || (
/* 350 */         this.text.length() >= getMaxStringLength());
/* 351 */       int var11 = var9;
/* 352 */       if (!var5) {
/* 353 */         var11 = var2 > 0 ? var7 + this.width : var7;
/* 354 */       } else if (var10) {
/* 355 */         var11 = var9 - 1;
/* 356 */         var9--;
/*     */       }
/* 358 */       if ((var4.length() > 0) && (var5) && (var2 < var4.length())) {
/* 359 */         Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(var4.substring(var2), var9, var8, var1);
/*     */       }
/* 361 */       if (var6) {
/* 362 */         if (var10) {
/* 363 */           Gui.drawRect(var11, var8 - 1, var11 + 1, var8 + 1 + this.fontRenderer.FONT_HEIGHT, -3092272);
/*     */         } else {
/* 365 */           Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow("_", var11, var8, var1);
/*     */         }
/*     */       }
/* 368 */       if (var3 != var2) {
/* 369 */         int var12 = var7 + this.fontRenderer.getStringWidth(var4.substring(0, var3));
/* 370 */         drawCursorVertical(var11, var8 - 1, var12 - 1, var8 + 1 + this.fontRenderer.FONT_HEIGHT);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private void drawCursorVertical(int par1, int par2, int par3, int par4) {
/* 376 */     if (par1 < par3) {
/* 377 */       int var5 = par1;
/* 378 */       par1 = par3;
/* 379 */       par3 = var5;
/*     */     }
/* 381 */     if (par2 < par4) {
/* 382 */       int var5 = par2;
/* 383 */       par2 = par4;
/* 384 */       par4 = var5;
/*     */     }
/* 386 */     Tessellator var6 = Tessellator.getInstance();
/* 387 */     WorldRenderer var7 = var6.getWorldRenderer();
/* 388 */     GL11.glColor4f(0.0F, 0.0F, 255.0F, 255.0F);
/* 389 */     GL11.glDisable(3553);
/* 390 */     GL11.glEnable(3058);
/* 391 */     GL11.glLogicOp(5387);
/* 392 */     var7.begin(7, var7.getVertexFormat());
/* 393 */     var7.pos(par1, par4, 0.0D);
/* 394 */     var7.pos(par3, par4, 0.0D);
/* 395 */     var7.pos(par3, par2, 0.0D);
/* 396 */     var7.pos(par1, par2, 0.0D);
/* 397 */     var7.finishDrawing();
/* 398 */     GL11.glDisable(3058);
/* 399 */     GL11.glEnable(3553);
/*     */   }
/*     */   
/*     */   public void setMaxStringLength(int par1) {
/* 403 */     this.maxStringLength = par1;
/* 404 */     if (this.text.length() > par1) {
/* 405 */       this.text = this.text.substring(0, par1);
/*     */     }
/*     */   }
/*     */   
/*     */   public int getMaxStringLength() {
/* 410 */     return this.maxStringLength;
/*     */   }
/*     */   
/*     */   public int getCursorPosition() {
/* 414 */     return this.cursorPosition;
/*     */   }
/*     */   
/*     */   public boolean getEnableBackgroundDrawing() {
/* 418 */     return this.enableBackgroundDrawing;
/*     */   }
/*     */   
/*     */   public void setEnableBackgroundDrawing(boolean par1) {
/* 422 */     this.enableBackgroundDrawing = par1;
/*     */   }
/*     */   
/*     */   public void func_73794_g(int par1) {
/* 426 */     this.enabledColor = par1;
/*     */   }
/*     */   
/*     */   public void setFocused(boolean par1) {
/* 430 */     if ((par1) && (!this.isFocused)) {
/* 431 */       this.cursorCounter = 0;
/*     */     }
/* 433 */     this.isFocused = par1;
/*     */   }
/*     */   
/*     */   public boolean isFocused() {
/* 437 */     return this.isFocused;
/*     */   }
/*     */   
/*     */   public int getSelectionEnd() {
/* 441 */     return this.selectionEnd;
/*     */   }
/*     */   
/*     */   public int getWidth() {
/* 445 */     return getEnableBackgroundDrawing() ? this.width - 8 : this.width;
/*     */   }
/*     */   
/*     */   public void func_73800_i(int par1) {
/* 449 */     int var2 = this.text.length();
/* 450 */     if (par1 > var2) {
/* 451 */       par1 = var2;
/*     */     }
/* 453 */     if (par1 < 0) {
/* 454 */       par1 = 0;
/*     */     }
/* 456 */     this.selectionEnd = par1;
/* 457 */     if (this.fontRenderer != null) {
/* 458 */       if (this.i > var2) {
/* 459 */         this.i = var2;
/*     */       }
/* 461 */       int var3 = getWidth();
/* 462 */       String var4 = this.fontRenderer.trimStringToWidth(this.text.substring(this.i), var3);
/* 463 */       int var5 = var4.length() + this.i;
/* 464 */       if (par1 == this.i) {
/* 465 */         this.i -= this.fontRenderer.trimStringToWidth(this.text, var3, true).length();
/*     */       }
/* 467 */       if (par1 > var5) {
/* 468 */         this.i += par1 - var5;
/* 469 */       } else if (par1 <= this.i) {
/* 470 */         this.i -= this.i - par1;
/*     */       }
/* 472 */       if (this.i < 0) {
/* 473 */         this.i = 0;
/*     */       }
/* 475 */       if (this.i > var2) {
/* 476 */         this.i = var2;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void setCanLoseFocus(boolean par1) {
/* 482 */     this.canLoseFocus = par1;
/*     */   }
/*     */   
/*     */   public boolean func_73778_q() {
/* 486 */     return this.b;
/*     */   }
/*     */   
/*     */   public void func_73790_e(boolean par1) {
/* 490 */     this.b = par1;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\rip\jutting\polaris\ui\altmanager\PasswordField.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */