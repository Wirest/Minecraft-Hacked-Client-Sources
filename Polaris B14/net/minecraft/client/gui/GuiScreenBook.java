/*     */ package net.minecraft.client.gui;
/*     */ 
/*     */ import com.google.gson.JsonParseException;
/*     */ import java.io.IOException;
/*     */ import java.util.List;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.client.renderer.texture.TextureManager;
/*     */ import net.minecraft.client.resources.I18n;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.event.ClickEvent;
/*     */ import net.minecraft.event.ClickEvent.Action;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.nbt.NBTTagList;
/*     */ import net.minecraft.nbt.NBTTagString;
/*     */ import net.minecraft.network.PacketBuffer;
/*     */ import net.minecraft.network.play.client.C17PacketCustomPayload;
/*     */ import net.minecraft.util.ChatAllowedCharacters;
/*     */ import net.minecraft.util.ChatComponentText;
/*     */ import net.minecraft.util.ChatStyle;
/*     */ import net.minecraft.util.EnumChatFormatting;
/*     */ import net.minecraft.util.IChatComponent;
/*     */ import net.minecraft.util.IChatComponent.Serializer;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.lwjgl.input.Keyboard;
/*     */ 
/*     */ public class GuiScreenBook extends GuiScreen
/*     */ {
/*  31 */   private static final org.apache.logging.log4j.Logger logger = ;
/*  32 */   private static final ResourceLocation bookGuiTextures = new ResourceLocation("textures/gui/book.png");
/*     */   private final EntityPlayer editingPlayer;
/*     */   private final ItemStack bookObj;
/*     */   private final boolean bookIsUnsigned;
/*     */   private boolean bookIsModified;
/*     */   private boolean bookGettingSigned;
/*     */   private int updateCount;
/*  39 */   private int bookImageWidth = 192;
/*  40 */   private int bookImageHeight = 192;
/*  41 */   private int bookTotalPages = 1;
/*     */   private int currPage;
/*     */   private NBTTagList bookPages;
/*  44 */   private String bookTitle = "";
/*     */   private List<IChatComponent> field_175386_A;
/*  46 */   private int field_175387_B = -1;
/*     */   private NextPageButton buttonNextPage;
/*     */   private NextPageButton buttonPreviousPage;
/*     */   private GuiButton buttonDone;
/*     */   private GuiButton buttonSign;
/*     */   private GuiButton buttonFinalize;
/*     */   private GuiButton buttonCancel;
/*     */   
/*     */   public GuiScreenBook(EntityPlayer player, ItemStack book, boolean isUnsigned) {
/*  55 */     this.editingPlayer = player;
/*  56 */     this.bookObj = book;
/*  57 */     this.bookIsUnsigned = isUnsigned;
/*  58 */     if (book.hasTagCompound()) {
/*  59 */       NBTTagCompound nbttagcompound = book.getTagCompound();
/*  60 */       this.bookPages = nbttagcompound.getTagList("pages", 8);
/*  61 */       if (this.bookPages != null) {
/*  62 */         this.bookPages = ((NBTTagList)this.bookPages.copy());
/*  63 */         this.bookTotalPages = this.bookPages.tagCount();
/*  64 */         if (this.bookTotalPages < 1) {
/*  65 */           this.bookTotalPages = 1;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*  70 */     if ((this.bookPages == null) && (isUnsigned)) {
/*  71 */       this.bookPages = new NBTTagList();
/*  72 */       this.bookPages.appendTag(new NBTTagString(""));
/*  73 */       this.bookTotalPages = 1;
/*     */     }
/*     */   }
/*     */   
/*     */   public void updateScreen() {
/*  78 */     super.updateScreen();
/*  79 */     this.updateCount += 1;
/*     */   }
/*     */   
/*     */   public void initGui() {
/*  83 */     this.buttonList.clear();
/*  84 */     Keyboard.enableRepeatEvents(true);
/*  85 */     if (this.bookIsUnsigned) {
/*  86 */       this.buttonList.add(this.buttonSign = new GuiButton(3, width / 2 - 100, 4 + this.bookImageHeight, 98, 20, I18n.format("book.signButton", new Object[0])));
/*  87 */       this.buttonList.add(this.buttonDone = new GuiButton(0, width / 2 + 2, 4 + this.bookImageHeight, 98, 20, I18n.format("gui.done", new Object[0])));
/*  88 */       this.buttonList.add(this.buttonFinalize = new GuiButton(5, width / 2 - 100, 4 + this.bookImageHeight, 98, 20, I18n.format("book.finalizeButton", new Object[0])));
/*  89 */       this.buttonList.add(this.buttonCancel = new GuiButton(4, width / 2 + 2, 4 + this.bookImageHeight, 98, 20, I18n.format("gui.cancel", new Object[0])));
/*     */     } else {
/*  91 */       this.buttonList.add(this.buttonDone = new GuiButton(0, width / 2 - 100, 4 + this.bookImageHeight, 200, 20, I18n.format("gui.done", new Object[0])));
/*     */     }
/*     */     
/*  94 */     int i = (width - this.bookImageWidth) / 2;
/*  95 */     int j = 2;
/*  96 */     this.buttonList.add(this.buttonNextPage = new NextPageButton(1, i + 120, j + 154, true));
/*  97 */     this.buttonList.add(this.buttonPreviousPage = new NextPageButton(2, i + 38, j + 154, false));
/*  98 */     updateButtons();
/*     */   }
/*     */   
/*     */   public void onGuiClosed() {
/* 102 */     Keyboard.enableRepeatEvents(false);
/*     */   }
/*     */   
/*     */   private void updateButtons() {
/* 106 */     this.buttonNextPage.visible = ((!this.bookGettingSigned) && ((this.currPage < this.bookTotalPages - 1) || (this.bookIsUnsigned)));
/* 107 */     this.buttonPreviousPage.visible = ((!this.bookGettingSigned) && (this.currPage > 0));
/* 108 */     this.buttonDone.visible = ((!this.bookIsUnsigned) || (!this.bookGettingSigned));
/* 109 */     if (this.bookIsUnsigned) {
/* 110 */       this.buttonSign.visible = (!this.bookGettingSigned);
/* 111 */       this.buttonCancel.visible = this.bookGettingSigned;
/* 112 */       this.buttonFinalize.visible = this.bookGettingSigned;
/* 113 */       this.buttonFinalize.enabled = (this.bookTitle.trim().length() > 0);
/*     */     }
/*     */   }
/*     */   
/*     */   private void sendBookToServer(boolean publish) throws IOException {
/* 118 */     if ((this.bookIsUnsigned) && (this.bookIsModified) && 
/* 119 */       (this.bookPages != null)) {
/* 120 */       while (this.bookPages.tagCount() > 1) {
/* 121 */         String s = this.bookPages.getStringTagAt(this.bookPages.tagCount() - 1);
/* 122 */         if (s.length() != 0) {
/*     */           break;
/*     */         }
/*     */         
/* 126 */         this.bookPages.removeTag(this.bookPages.tagCount() - 1);
/*     */       }
/*     */       
/* 129 */       if (this.bookObj.hasTagCompound()) {
/* 130 */         NBTTagCompound nbttagcompound = this.bookObj.getTagCompound();
/* 131 */         nbttagcompound.setTag("pages", this.bookPages);
/*     */       } else {
/* 133 */         this.bookObj.setTagInfo("pages", this.bookPages);
/*     */       }
/*     */       
/* 136 */       String s2 = "MC|BEdit";
/* 137 */       if (publish) {
/* 138 */         s2 = "MC|BSign";
/* 139 */         this.bookObj.setTagInfo("author", new NBTTagString(this.editingPlayer.getName()));
/* 140 */         this.bookObj.setTagInfo("title", new NBTTagString(this.bookTitle.trim()));
/*     */         
/* 142 */         for (int i = 0; i < this.bookPages.tagCount(); i++) {
/* 143 */           String s1 = this.bookPages.getStringTagAt(i);
/* 144 */           IChatComponent ichatcomponent = new ChatComponentText(s1);
/* 145 */           s1 = IChatComponent.Serializer.componentToJson(ichatcomponent);
/* 146 */           this.bookPages.set(i, new NBTTagString(s1));
/*     */         }
/*     */         
/* 149 */         this.bookObj.setItem(net.minecraft.init.Items.written_book);
/*     */       }
/*     */       
/* 152 */       PacketBuffer packetbuffer = new PacketBuffer(io.netty.buffer.Unpooled.buffer());
/* 153 */       packetbuffer.writeItemStackToBuffer(this.bookObj);
/* 154 */       this.mc.getNetHandler().addToSendQueue(new C17PacketCustomPayload(s2, packetbuffer));
/*     */     }
/*     */   }
/*     */   
/*     */   protected void actionPerformed(GuiButton button) throws IOException
/*     */   {
/* 160 */     if (button.enabled) {
/* 161 */       if (button.id == 0) {
/* 162 */         this.mc.displayGuiScreen(null);
/* 163 */         sendBookToServer(false);
/* 164 */       } else if ((button.id == 3) && (this.bookIsUnsigned)) {
/* 165 */         this.bookGettingSigned = true;
/* 166 */       } else if (button.id == 1) {
/* 167 */         if (this.currPage < this.bookTotalPages - 1) {
/* 168 */           this.currPage += 1;
/* 169 */         } else if (this.bookIsUnsigned) {
/* 170 */           addNewPage();
/* 171 */           if (this.currPage < this.bookTotalPages - 1) {
/* 172 */             this.currPage += 1;
/*     */           }
/*     */         }
/* 175 */       } else if (button.id == 2) {
/* 176 */         if (this.currPage > 0) {
/* 177 */           this.currPage -= 1;
/*     */         }
/* 179 */       } else if ((button.id == 5) && (this.bookGettingSigned)) {
/* 180 */         sendBookToServer(true);
/* 181 */         this.mc.displayGuiScreen(null);
/* 182 */       } else if ((button.id == 4) && (this.bookGettingSigned)) {
/* 183 */         this.bookGettingSigned = false;
/*     */       }
/*     */       
/* 186 */       updateButtons();
/*     */     }
/*     */   }
/*     */   
/*     */   private void addNewPage() {
/* 191 */     if ((this.bookPages != null) && (this.bookPages.tagCount() < 50)) {
/* 192 */       this.bookPages.appendTag(new NBTTagString(""));
/* 193 */       this.bookTotalPages += 1;
/* 194 */       this.bookIsModified = true;
/*     */     }
/*     */   }
/*     */   
/*     */   protected void keyTyped(char typedChar, int keyCode) throws IOException {
/* 199 */     super.keyTyped(typedChar, keyCode);
/* 200 */     if (this.bookIsUnsigned) {
/* 201 */       if (this.bookGettingSigned) {
/* 202 */         keyTypedInTitle(typedChar, keyCode);
/*     */       } else {
/* 204 */         keyTypedInBook(typedChar, keyCode);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private void keyTypedInBook(char typedChar, int keyCode) {
/* 210 */     if (GuiScreen.isKeyComboCtrlV(keyCode)) {
/* 211 */       pageInsertIntoCurrent(GuiScreen.getClipboardString());
/*     */     } else {
/* 213 */       switch (keyCode) {
/*     */       case 14: 
/* 215 */         String s = pageGetCurrent();
/* 216 */         if (s.length() > 0) {
/* 217 */           pageSetCurrent(s.substring(0, s.length() - 1));
/*     */         }
/*     */         
/* 220 */         return;
/*     */       case 28: 
/*     */       case 156: 
/* 223 */         pageInsertIntoCurrent("\n");
/* 224 */         return;
/*     */       }
/* 226 */       if (ChatAllowedCharacters.isAllowedCharacter(typedChar)) {
/* 227 */         pageInsertIntoCurrent(Character.toString(typedChar));
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private void keyTypedInTitle(char p_146460_1_, int p_146460_2_) throws IOException
/*     */   {
/* 234 */     switch (p_146460_2_) {
/*     */     case 14: 
/* 236 */       if (!this.bookTitle.isEmpty()) {
/* 237 */         this.bookTitle = this.bookTitle.substring(0, this.bookTitle.length() - 1);
/* 238 */         updateButtons();
/*     */       }
/*     */       
/* 241 */       return;
/*     */     case 28: 
/*     */     case 156: 
/* 244 */       if (!this.bookTitle.isEmpty()) {
/* 245 */         sendBookToServer(true);
/* 246 */         this.mc.displayGuiScreen(null);
/*     */       }
/*     */       
/* 249 */       return;
/*     */     }
/* 251 */     if ((this.bookTitle.length() < 16) && (ChatAllowedCharacters.isAllowedCharacter(p_146460_1_))) {
/* 252 */       this.bookTitle += Character.toString(p_146460_1_);
/* 253 */       updateButtons();
/* 254 */       this.bookIsModified = true;
/*     */     }
/*     */   }
/*     */   
/*     */   private String pageGetCurrent()
/*     */   {
/* 260 */     return (this.bookPages != null) && (this.currPage >= 0) && (this.currPage < this.bookPages.tagCount()) ? this.bookPages.getStringTagAt(this.currPage) : "";
/*     */   }
/*     */   
/*     */   private void pageSetCurrent(String p_146457_1_) {
/* 264 */     if ((this.bookPages != null) && (this.currPage >= 0) && (this.currPage < this.bookPages.tagCount())) {
/* 265 */       this.bookPages.set(this.currPage, new NBTTagString(p_146457_1_));
/* 266 */       this.bookIsModified = true;
/*     */     }
/*     */   }
/*     */   
/*     */   private void pageInsertIntoCurrent(String p_146459_1_) {
/* 271 */     String s = pageGetCurrent();
/* 272 */     String s1 = s + p_146459_1_;
/* 273 */     int i = this.fontRendererObj.splitStringWidth(s1 + EnumChatFormatting.BLACK + "_", 118);
/* 274 */     if ((i <= 128) && (s1.length() < 256)) {
/* 275 */       pageSetCurrent(s1);
/*     */     }
/*     */   }
/*     */   
/*     */   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
/* 280 */     GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/* 281 */     this.mc.getTextureManager().bindTexture(bookGuiTextures);
/* 282 */     int i = (width - this.bookImageWidth) / 2;
/* 283 */     int j = 2;
/* 284 */     drawTexturedModalRect(i, j, 0, 0, this.bookImageWidth, this.bookImageHeight);
/* 285 */     if (this.bookGettingSigned) {
/* 286 */       String s = this.bookTitle;
/* 287 */       if (this.bookIsUnsigned) {
/* 288 */         if (this.updateCount / 6 % 2 == 0) {
/* 289 */           s = s + EnumChatFormatting.BLACK + "_";
/*     */         } else {
/* 291 */           s = s + EnumChatFormatting.GRAY + "_";
/*     */         }
/*     */       }
/*     */       
/* 295 */       String s1 = I18n.format("book.editTitle", new Object[0]);
/* 296 */       int k = this.fontRendererObj.getStringWidth(s1);
/* 297 */       this.fontRendererObj.drawString(s1, i + 36 + (116 - k) / 2, j + 16 + 16, 0);
/* 298 */       int l = this.fontRendererObj.getStringWidth(s);
/* 299 */       this.fontRendererObj.drawString(s, i + 36 + (116 - l) / 2, j + 48, 0);
/* 300 */       String s2 = I18n.format("book.byAuthor", new Object[] { this.editingPlayer.getName() });
/* 301 */       int i1 = this.fontRendererObj.getStringWidth(s2);
/* 302 */       this.fontRendererObj.drawString(EnumChatFormatting.DARK_GRAY + s2, i + 36 + (116 - i1) / 2, j + 48 + 10, 0);
/* 303 */       String s3 = I18n.format("book.finalizeWarning", new Object[0]);
/* 304 */       this.fontRendererObj.drawSplitString(s3, i + 36, j + 80, 116, 0);
/*     */     } else {
/* 306 */       String s4 = I18n.format("book.pageIndicator", new Object[] { Integer.valueOf(this.currPage + 1), Integer.valueOf(this.bookTotalPages) });
/* 307 */       String s5 = "";
/* 308 */       if ((this.bookPages != null) && (this.currPage >= 0) && (this.currPage < this.bookPages.tagCount())) {
/* 309 */         s5 = this.bookPages.getStringTagAt(this.currPage);
/*     */       }
/*     */       
/* 312 */       if (this.bookIsUnsigned) {
/* 313 */         if (this.fontRendererObj.getBidiFlag()) {
/* 314 */           s5 = s5 + "_";
/* 315 */         } else if (this.updateCount / 6 % 2 == 0) {
/* 316 */           s5 = s5 + EnumChatFormatting.BLACK + "_";
/*     */         } else {
/* 318 */           s5 = s5 + EnumChatFormatting.GRAY + "_";
/*     */         }
/* 320 */       } else if (this.field_175387_B != this.currPage) {
/* 321 */         if (net.minecraft.item.ItemEditableBook.validBookTagContents(this.bookObj.getTagCompound())) {
/*     */           try {
/* 323 */             IChatComponent ichatcomponent = IChatComponent.Serializer.jsonToComponent(s5);
/* 324 */             this.field_175386_A = (ichatcomponent != null ? GuiUtilRenderComponents.func_178908_a(ichatcomponent, 116, this.fontRendererObj, true, true) : null);
/*     */           } catch (JsonParseException var13) {
/* 326 */             this.field_175386_A = null;
/*     */           }
/*     */         } else {
/* 329 */           ChatComponentText chatcomponenttext = new ChatComponentText(EnumChatFormatting.DARK_RED.toString() + "* Invalid book tag *");
/* 330 */           this.field_175386_A = com.google.common.collect.Lists.newArrayList(chatcomponenttext);
/*     */         }
/*     */         
/* 333 */         this.field_175387_B = this.currPage;
/*     */       }
/*     */       
/* 336 */       int j1 = this.fontRendererObj.getStringWidth(s4);
/* 337 */       this.fontRendererObj.drawString(s4, i - j1 + this.bookImageWidth - 44, j + 16, 0);
/* 338 */       if (this.field_175386_A == null) {
/* 339 */         this.fontRendererObj.drawSplitString(s5, i + 36, j + 16 + 16, 116, 0);
/*     */       } else {
/* 341 */         int k1 = Math.min(128 / this.fontRendererObj.FONT_HEIGHT, this.field_175386_A.size());
/*     */         
/* 343 */         for (int l1 = 0; l1 < k1; l1++) {
/* 344 */           IChatComponent ichatcomponent2 = (IChatComponent)this.field_175386_A.get(l1);
/* 345 */           this.fontRendererObj.drawString(ichatcomponent2.getUnformattedText(), i + 36, j + 16 + 16 + l1 * this.fontRendererObj.FONT_HEIGHT, 0);
/*     */         }
/*     */         
/* 348 */         IChatComponent ichatcomponent1 = func_175385_b(mouseX, mouseY);
/* 349 */         if (ichatcomponent1 != null) {
/* 350 */           handleComponentHover(ichatcomponent1, mouseX, mouseY);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 355 */     super.drawScreen(mouseX, mouseY, partialTicks);
/*     */   }
/*     */   
/*     */   protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
/* 359 */     if (mouseButton == 0) {
/* 360 */       IChatComponent ichatcomponent = func_175385_b(mouseX, mouseY);
/* 361 */       if (handleComponentClick(ichatcomponent)) {
/* 362 */         return;
/*     */       }
/*     */     }
/*     */     
/* 366 */     super.mouseClicked(mouseX, mouseY, mouseButton);
/*     */   }
/*     */   
/*     */   protected boolean handleComponentClick(IChatComponent p_175276_1_) {
/* 370 */     ClickEvent clickevent = p_175276_1_ == null ? null : p_175276_1_.getChatStyle().getChatClickEvent();
/* 371 */     if (clickevent == null)
/* 372 */       return false;
/* 373 */     if (clickevent.getAction() == ClickEvent.Action.CHANGE_PAGE) {
/* 374 */       String s = clickevent.getValue();
/*     */       try
/*     */       {
/* 377 */         int i = Integer.parseInt(s) - 1;
/* 378 */         if ((i >= 0) && (i < this.bookTotalPages) && (i != this.currPage)) {
/* 379 */           this.currPage = i;
/* 380 */           updateButtons();
/* 381 */           return true;
/*     */         }
/*     */       }
/*     */       catch (Throwable localThrowable) {}
/*     */       
/*     */ 
/* 387 */       return false;
/*     */     }
/* 389 */     boolean flag = super.handleComponentClick(p_175276_1_);
/* 390 */     if ((flag) && (clickevent.getAction() == ClickEvent.Action.RUN_COMMAND)) {
/* 391 */       this.mc.displayGuiScreen(null);
/*     */     }
/*     */     
/* 394 */     return flag;
/*     */   }
/*     */   
/*     */   public IChatComponent func_175385_b(int p_175385_1_, int p_175385_2_)
/*     */   {
/* 399 */     if (this.field_175386_A == null) {
/* 400 */       return null;
/*     */     }
/* 402 */     int i = p_175385_1_ - (width - this.bookImageWidth) / 2 - 36;
/* 403 */     int j = p_175385_2_ - 2 - 16 - 16;
/* 404 */     if ((i >= 0) && (j >= 0)) {
/* 405 */       int k = Math.min(128 / this.fontRendererObj.FONT_HEIGHT, this.field_175386_A.size());
/* 406 */       if ((i <= 116) && (j < this.mc.fontRendererObj.FONT_HEIGHT * k + k)) {
/* 407 */         int l = j / this.mc.fontRendererObj.FONT_HEIGHT;
/* 408 */         if ((l >= 0) && (l < this.field_175386_A.size())) {
/* 409 */           IChatComponent ichatcomponent = (IChatComponent)this.field_175386_A.get(l);
/* 410 */           int i1 = 0;
/*     */           
/* 412 */           for (IChatComponent ichatcomponent1 : ichatcomponent) {
/* 413 */             if ((ichatcomponent1 instanceof ChatComponentText)) {
/* 414 */               i1 += this.mc.fontRendererObj.getStringWidth(((ChatComponentText)ichatcomponent1).getChatComponentText_TextValue());
/* 415 */               if (i1 > i) {
/* 416 */                 return ichatcomponent1;
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*     */         
/* 422 */         return null;
/*     */       }
/* 424 */       return null;
/*     */     }
/*     */     
/* 427 */     return null;
/*     */   }
/*     */   
/*     */   static class NextPageButton extends GuiButton
/*     */   {
/*     */     private final boolean field_146151_o;
/*     */     
/*     */     public NextPageButton(int p_i46316_1_, int p_i46316_2_, int p_i46316_3_, boolean p_i46316_4_)
/*     */     {
/* 436 */       super(p_i46316_2_, p_i46316_3_, 23, 13, "");
/* 437 */       this.field_146151_o = p_i46316_4_;
/*     */     }
/*     */     
/*     */     public void drawButton(Minecraft mc, int mouseX, int mouseY) {
/* 441 */       if (this.visible) {
/* 442 */         boolean flag = (mouseX >= this.xPosition) && (mouseY >= this.yPosition) && (mouseX < this.xPosition + this.width) && (mouseY < this.yPosition + this.height);
/* 443 */         GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/* 444 */         mc.getTextureManager().bindTexture(GuiScreenBook.bookGuiTextures);
/* 445 */         int i = 0;
/* 446 */         int j = 192;
/* 447 */         if (flag) {
/* 448 */           i += 23;
/*     */         }
/*     */         
/* 451 */         if (!this.field_146151_o) {
/* 452 */           j += 13;
/*     */         }
/*     */         
/* 455 */         drawTexturedModalRect(this.xPosition, this.yPosition, i, j, 23, 13);
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\gui\GuiScreenBook.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */