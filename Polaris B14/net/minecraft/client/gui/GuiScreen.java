/*     */ package net.minecraft.client.gui;
/*     */ 
/*     */ import com.google.common.base.Splitter;
/*     */ import com.google.common.collect.Lists;
/*     */ import java.awt.Toolkit;
/*     */ import java.awt.datatransfer.Clipboard;
/*     */ import java.awt.datatransfer.DataFlavor;
/*     */ import java.awt.datatransfer.StringSelection;
/*     */ import java.awt.datatransfer.Transferable;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.entity.EntityPlayerSP;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.client.renderer.RenderHelper;
/*     */ import net.minecraft.client.renderer.Tessellator;
/*     */ import net.minecraft.client.renderer.WorldRenderer;
/*     */ import net.minecraft.client.renderer.texture.TextureManager;
/*     */ import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
/*     */ import net.minecraft.client.settings.GameSettings;
/*     */ import net.minecraft.entity.EntityList;
/*     */ import net.minecraft.event.ClickEvent;
/*     */ import net.minecraft.event.ClickEvent.Action;
/*     */ import net.minecraft.event.HoverEvent;
/*     */ import net.minecraft.event.HoverEvent.Action;
/*     */ import net.minecraft.item.EnumRarity;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.nbt.JsonToNBT;
/*     */ import net.minecraft.nbt.NBTBase;
/*     */ import net.minecraft.nbt.NBTException;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.stats.Achievement;
/*     */ import net.minecraft.stats.StatBase;
/*     */ import net.minecraft.util.ChatStyle;
/*     */ import net.minecraft.util.EnumChatFormatting;
/*     */ import net.minecraft.util.IChatComponent;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.lwjgl.input.Keyboard;
/*     */ import org.lwjgl.input.Mouse;
/*     */ 
/*     */ public abstract class GuiScreen extends Gui implements GuiYesNoCallback
/*     */ {
/*  50 */   private static final Logger LOGGER = ;
/*  51 */   private static final Set<String> PROTOCOLS = com.google.common.collect.Sets.newHashSet(new String[] { "http", "https" });
/*  52 */   private static final Splitter NEWLINE_SPLITTER = Splitter.on('\n');
/*     */   protected Minecraft mc;
/*     */   protected net.minecraft.client.renderer.entity.RenderItem itemRender;
/*     */   public static int width;
/*     */   public static int height;
/*  57 */   protected List<GuiButton> buttonList = Lists.newArrayList();
/*  58 */   protected List<GuiLabel> labelList = Lists.newArrayList();
/*     */   public boolean allowUserInput;
/*     */   protected FontRenderer fontRendererObj;
/*     */   private GuiButton selectedButton;
/*     */   protected int eventButton;
/*     */   private long lastMouseEvent;
/*     */   private int touchValue;
/*     */   private URI clickedLinkURI;
/*     */   
/*     */   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
/*  68 */     for (int i = 0; i < this.buttonList.size(); i++) {
/*  69 */       ((GuiButton)this.buttonList.get(i)).drawButton(this.mc, mouseX, mouseY);
/*     */     }
/*     */     
/*  72 */     for (int j = 0; j < this.labelList.size(); j++) {
/*  73 */       ((GuiLabel)this.labelList.get(j)).drawLabel(this.mc, mouseX, mouseY);
/*     */     }
/*     */   }
/*     */   
/*     */   protected void keyTyped(char typedChar, int keyCode) throws IOException {
/*  78 */     if (keyCode == 1) {
/*  79 */       this.mc.displayGuiScreen(null);
/*  80 */       if (this.mc.currentScreen == null) {
/*  81 */         this.mc.setIngameFocus();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public static String getClipboardString() {
/*     */     try {
/*  88 */       Transferable transferable = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);
/*  89 */       if ((transferable != null) && (transferable.isDataFlavorSupported(DataFlavor.stringFlavor))) {
/*  90 */         return (String)transferable.getTransferData(DataFlavor.stringFlavor);
/*     */       }
/*     */     }
/*     */     catch (Exception localException) {}
/*     */     
/*     */ 
/*  96 */     return "";
/*     */   }
/*     */   
/*     */   public static void setClipboardString(String copyText) {
/* 100 */     if (!org.apache.commons.lang3.StringUtils.isEmpty(copyText)) {
/*     */       try {
/* 102 */         StringSelection stringselection = new StringSelection(copyText);
/* 103 */         Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringselection, null);
/*     */       }
/*     */       catch (Exception localException) {}
/*     */     }
/*     */   }
/*     */   
/*     */   protected void renderToolTip(ItemStack stack, int x, int y)
/*     */   {
/* 111 */     List<String> list = stack.getTooltip(this.mc.thePlayer, this.mc.gameSettings.advancedItemTooltips);
/*     */     
/* 113 */     for (int i = 0; i < list.size(); i++) {
/* 114 */       if (i == 0) {
/* 115 */         list.set(i, stack.getRarity().rarityColor + (String)list.get(i));
/*     */       } else {
/* 117 */         list.set(i, EnumChatFormatting.GRAY + (String)list.get(i));
/*     */       }
/*     */     }
/*     */     
/* 121 */     drawHoveringText(list, x, y);
/*     */   }
/*     */   
/*     */   protected void drawCreativeTabHoveringText(String tabName, int mouseX, int mouseY) {
/* 125 */     drawHoveringText(Arrays.asList(new String[] { tabName }), mouseX, mouseY);
/*     */   }
/*     */   
/*     */   protected void drawHoveringText(List<String> textLines, int x, int y) {
/* 129 */     if (!textLines.isEmpty()) {
/* 130 */       GlStateManager.disableRescaleNormal();
/* 131 */       RenderHelper.disableStandardItemLighting();
/* 132 */       GlStateManager.disableLighting();
/* 133 */       GlStateManager.disableDepth();
/* 134 */       int i = 0;
/*     */       
/* 136 */       for (String s : textLines) {
/* 137 */         int j = this.fontRendererObj.getStringWidth(s);
/* 138 */         if (j > i) {
/* 139 */           i = j;
/*     */         }
/*     */       }
/*     */       
/* 143 */       int l1 = x + 12;
/* 144 */       int i2 = y - 12;
/* 145 */       int k = 8;
/* 146 */       if (textLines.size() > 1) {
/* 147 */         k += 2 + (textLines.size() - 1) * 10;
/*     */       }
/*     */       
/* 150 */       if (l1 + i > width) {
/* 151 */         l1 -= 28 + i;
/*     */       }
/*     */       
/* 154 */       if (i2 + k + 6 > height) {
/* 155 */         i2 = height - k - 6;
/*     */       }
/*     */       
/* 158 */       this.zLevel = 300.0F;
/* 159 */       this.itemRender.zLevel = 300.0F;
/* 160 */       int l = -267386864;
/* 161 */       drawGradientRect(l1 - 3, i2 - 4, l1 + i + 3, i2 - 3, l, l);
/* 162 */       drawGradientRect(l1 - 3, i2 + k + 3, l1 + i + 3, i2 + k + 4, l, l);
/* 163 */       drawGradientRect(l1 - 3, i2 - 3, l1 + i + 3, i2 + k + 3, l, l);
/* 164 */       drawGradientRect(l1 - 4, i2 - 3, l1 - 3, i2 + k + 3, l, l);
/* 165 */       drawGradientRect(l1 + i + 3, i2 - 3, l1 + i + 4, i2 + k + 3, l, l);
/* 166 */       int i1 = 1347420415;
/* 167 */       int j1 = (i1 & 0xFEFEFE) >> 1 | i1 & 0xFF000000;
/* 168 */       drawGradientRect(l1 - 3, i2 - 3 + 1, l1 - 3 + 1, i2 + k + 3 - 1, i1, j1);
/* 169 */       drawGradientRect(l1 + i + 2, i2 - 3 + 1, l1 + i + 3, i2 + k + 3 - 1, i1, j1);
/* 170 */       drawGradientRect(l1 - 3, i2 - 3, l1 + i + 3, i2 - 3 + 1, i1, i1);
/* 171 */       drawGradientRect(l1 - 3, i2 + k + 2, l1 + i + 3, i2 + k + 3, j1, j1);
/*     */       
/* 173 */       for (int k1 = 0; k1 < textLines.size(); k1++) {
/* 174 */         String s1 = (String)textLines.get(k1);
/* 175 */         this.fontRendererObj.drawStringWithShadow(s1, l1, i2, -1);
/* 176 */         if (k1 == 0) {
/* 177 */           i2 += 2;
/*     */         }
/*     */         
/* 180 */         i2 += 10;
/*     */       }
/*     */       
/* 183 */       this.zLevel = 0.0F;
/* 184 */       this.itemRender.zLevel = 0.0F;
/* 185 */       GlStateManager.enableLighting();
/* 186 */       GlStateManager.enableDepth();
/* 187 */       RenderHelper.enableStandardItemLighting();
/* 188 */       GlStateManager.enableRescaleNormal();
/*     */     }
/*     */   }
/*     */   
/*     */   protected void handleComponentHover(IChatComponent p_175272_1_, int p_175272_2_, int p_175272_3_) {
/* 193 */     if ((p_175272_1_ != null) && (p_175272_1_.getChatStyle().getChatHoverEvent() != null)) {
/* 194 */       HoverEvent hoverevent = p_175272_1_.getChatStyle().getChatHoverEvent();
/* 195 */       if (hoverevent.getAction() == HoverEvent.Action.SHOW_ITEM) {
/* 196 */         ItemStack itemstack = null;
/*     */         try
/*     */         {
/* 199 */           NBTBase nbtbase = JsonToNBT.getTagFromJson(hoverevent.getValue().getUnformattedText());
/* 200 */           if ((nbtbase instanceof NBTTagCompound)) {
/* 201 */             itemstack = ItemStack.loadItemStackFromNBT((NBTTagCompound)nbtbase);
/*     */           }
/*     */         }
/*     */         catch (NBTException localNBTException1) {}
/*     */         
/*     */ 
/* 207 */         if (itemstack != null) {
/* 208 */           renderToolTip(itemstack, p_175272_2_, p_175272_3_);
/*     */         } else {
/* 210 */           drawCreativeTabHoveringText(EnumChatFormatting.RED + "Invalid Item!", p_175272_2_, 
/* 211 */             p_175272_3_);
/*     */         }
/* 213 */       } else if (hoverevent.getAction() == HoverEvent.Action.SHOW_ENTITY) {
/* 214 */         if (this.mc.gameSettings.advancedItemTooltips) {
/*     */           try {
/* 216 */             NBTBase nbtbase1 = JsonToNBT.getTagFromJson(hoverevent.getValue().getUnformattedText());
/* 217 */             if ((nbtbase1 instanceof NBTTagCompound)) {
/* 218 */               List<String> list1 = Lists.newArrayList();
/* 219 */               NBTTagCompound nbttagcompound = (NBTTagCompound)nbtbase1;
/* 220 */               list1.add(nbttagcompound.getString("name"));
/* 221 */               if (nbttagcompound.hasKey("type", 8)) {
/* 222 */                 String s = nbttagcompound.getString("type");
/* 223 */                 list1.add("Type: " + s + " (" + EntityList.getIDFromString(s) + ")");
/*     */               }
/*     */               
/* 226 */               list1.add(nbttagcompound.getString("id"));
/* 227 */               drawHoveringText(list1, p_175272_2_, p_175272_3_);
/*     */             } else {
/* 229 */               drawCreativeTabHoveringText(EnumChatFormatting.RED + "Invalid Entity!", p_175272_2_, 
/* 230 */                 p_175272_3_);
/*     */             }
/*     */           } catch (NBTException var10) {
/* 233 */             drawCreativeTabHoveringText(EnumChatFormatting.RED + "Invalid Entity!", p_175272_2_, 
/* 234 */               p_175272_3_);
/*     */           }
/*     */         }
/* 237 */       } else if (hoverevent.getAction() == HoverEvent.Action.SHOW_TEXT) {
/* 238 */         drawHoveringText(NEWLINE_SPLITTER.splitToList(hoverevent.getValue().getFormattedText()), 
/* 239 */           p_175272_2_, p_175272_3_);
/* 240 */       } else if (hoverevent.getAction() == HoverEvent.Action.SHOW_ACHIEVEMENT) {
/* 241 */         StatBase statbase = net.minecraft.stats.StatList.getOneShotStat(hoverevent.getValue().getUnformattedText());
/* 242 */         if (statbase != null) {
/* 243 */           IChatComponent ichatcomponent = statbase.getStatName();
/* 244 */           IChatComponent ichatcomponent1 = new net.minecraft.util.ChatComponentTranslation(
/* 245 */             "stats.tooltip.type." + (statbase.isAchievement() ? "achievement" : "statistic"), 
/* 246 */             new Object[0]);
/* 247 */           ichatcomponent1.getChatStyle().setItalic(Boolean.valueOf(true));
/* 248 */           String s1 = (statbase instanceof Achievement) ? ((Achievement)statbase).getDescription() : null;
/* 249 */           List<String> list = Lists.newArrayList(
/* 250 */             new String[] { ichatcomponent.getFormattedText(), ichatcomponent1.getFormattedText() });
/* 251 */           if (s1 != null) {
/* 252 */             list.addAll(this.fontRendererObj.listFormattedStringToWidth(s1, 150));
/*     */           }
/*     */           
/* 255 */           drawHoveringText(list, p_175272_2_, p_175272_3_);
/*     */         } else {
/* 257 */           drawCreativeTabHoveringText(EnumChatFormatting.RED + "Invalid statistic/achievement!", 
/* 258 */             p_175272_2_, p_175272_3_);
/*     */         }
/*     */       }
/*     */       
/* 262 */       GlStateManager.disableLighting();
/*     */     }
/*     */   }
/*     */   
/*     */   protected void setText(String newChatText, boolean shouldOverwrite) {}
/*     */   
/*     */   protected boolean handleComponentClick(IChatComponent p_175276_1_)
/*     */   {
/* 270 */     if (p_175276_1_ == null) {
/* 271 */       return false;
/*     */     }
/* 273 */     ClickEvent clickevent = p_175276_1_.getChatStyle().getChatClickEvent();
/* 274 */     if (isShiftKeyDown()) {
/* 275 */       if (p_175276_1_.getChatStyle().getInsertion() != null) {
/* 276 */         setText(p_175276_1_.getChatStyle().getInsertion(), false);
/*     */       }
/* 278 */     } else if (clickevent != null) {
/* 279 */       if (clickevent.getAction() == ClickEvent.Action.OPEN_URL) {
/* 280 */         if (!this.mc.gameSettings.chatLinks) {
/* 281 */           return false;
/*     */         }
/*     */         try
/*     */         {
/* 285 */           URI uri = new URI(clickevent.getValue());
/* 286 */           String s = uri.getScheme();
/* 287 */           if (s == null) {
/* 288 */             throw new URISyntaxException(clickevent.getValue(), "Missing protocol");
/*     */           }
/*     */           
/* 291 */           if (!PROTOCOLS.contains(s.toLowerCase())) {
/* 292 */             throw new URISyntaxException(clickevent.getValue(), 
/* 293 */               "Unsupported protocol: " + s.toLowerCase());
/*     */           }
/*     */           
/* 296 */           if (this.mc.gameSettings.chatLinksPrompt) {
/* 297 */             this.clickedLinkURI = uri;
/* 298 */             this.mc.displayGuiScreen(
/* 299 */               new GuiConfirmOpenLink(this, clickevent.getValue(), 31102009, false));
/*     */           } else {
/* 301 */             openWebLink(uri);
/*     */           }
/*     */         } catch (URISyntaxException urisyntaxexception) {
/* 304 */           LOGGER.error("Can't open url for " + clickevent, urisyntaxexception);
/*     */         }
/* 306 */       } else if (clickevent.getAction() == ClickEvent.Action.OPEN_FILE) {
/* 307 */         URI uri1 = new File(clickevent.getValue()).toURI();
/* 308 */         openWebLink(uri1);
/* 309 */       } else if (clickevent.getAction() == ClickEvent.Action.SUGGEST_COMMAND) {
/* 310 */         setText(clickevent.getValue(), true);
/* 311 */       } else if (clickevent.getAction() == ClickEvent.Action.RUN_COMMAND) {
/* 312 */         sendChatMessage(clickevent.getValue(), false);
/* 313 */       } else if (clickevent.getAction() == ClickEvent.Action.TWITCH_USER_INFO) {
/* 314 */         tv.twitch.chat.ChatUserInfo chatuserinfo = this.mc.getTwitchStream().func_152926_a(clickevent.getValue());
/* 315 */         if (chatuserinfo != null) {
/* 316 */           this.mc.displayGuiScreen(new net.minecraft.client.gui.stream.GuiTwitchUserMode(this.mc.getTwitchStream(), chatuserinfo));
/*     */         } else {
/* 318 */           LOGGER.error("Tried to handle twitch user but couldn't find them!");
/*     */         }
/*     */       } else {
/* 321 */         LOGGER.error("Don't know how to handle " + clickevent);
/*     */       }
/*     */       
/* 324 */       return true;
/*     */     }
/*     */     
/* 327 */     return false;
/*     */   }
/*     */   
/*     */   public void sendChatMessage(String msg)
/*     */   {
/* 332 */     sendChatMessage(msg, true);
/*     */   }
/*     */   
/*     */   public void sendChatMessage(String msg, boolean addToChat) {
/* 336 */     if (addToChat) {
/* 337 */       this.mc.ingameGUI.getChatGUI().addToSentMessages(msg);
/*     */     }
/*     */     
/* 340 */     this.mc.thePlayer.sendChatMessage(msg);
/*     */   }
/*     */   
/*     */   protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
/* 344 */     if (mouseButton == 0) {
/* 345 */       for (int i = 0; i < this.buttonList.size(); i++) {
/* 346 */         GuiButton guibutton = (GuiButton)this.buttonList.get(i);
/* 347 */         if (guibutton.mousePressed(this.mc, mouseX, mouseY)) {
/* 348 */           this.selectedButton = guibutton;
/* 349 */           guibutton.playPressSound(this.mc.getSoundHandler());
/* 350 */           actionPerformed(guibutton);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   protected void mouseReleased(int mouseX, int mouseY, int state) {
/* 357 */     if ((this.selectedButton != null) && (state == 0)) {
/* 358 */       this.selectedButton.mouseReleased(mouseX, mouseY);
/* 359 */       this.selectedButton = null;
/*     */     }
/*     */   }
/*     */   
/*     */   protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {}
/*     */   
/*     */   protected void actionPerformed(GuiButton button) throws IOException
/*     */   {}
/*     */   
/*     */   public void setWorldAndResolution(Minecraft mc, int width, int height)
/*     */   {
/* 370 */     this.mc = mc;
/* 371 */     this.itemRender = mc.getRenderItem();
/* 372 */     this.fontRendererObj = mc.fontRendererObj;
/* 373 */     width = width;
/* 374 */     height = height;
/* 375 */     this.buttonList.clear();
/* 376 */     initGui();
/*     */   }
/*     */   
/*     */   public void initGui() {}
/*     */   
/*     */   public void handleInput() throws IOException
/*     */   {
/* 383 */     if (Mouse.isCreated()) {
/* 384 */       while (Mouse.next()) {
/* 385 */         handleMouseInput();
/*     */       }
/*     */     }
/*     */     
/* 389 */     if (Keyboard.isCreated()) {
/* 390 */       while (Keyboard.next()) {
/* 391 */         handleKeyboardInput();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void drawCoolBackground() {
/* 397 */     ScaledResolution scaledRes = new ScaledResolution(this.mc, this.mc.displayWidth, this.mc.displayHeight);
/* 398 */     this.mc.getTextureManager().bindTexture(new net.minecraft.util.ResourceLocation("polaris/background.jpg"));
/* 399 */     Gui.drawScaledCustomSizeModalRect(0, 0, 0.0F, 0.0F, ScaledResolution.getScaledWidth(), 
/* 400 */       ScaledResolution.getScaledHeight(), ScaledResolution.getScaledWidth(), ScaledResolution.getScaledHeight(), 
/* 401 */       ScaledResolution.getScaledWidth(), ScaledResolution.getScaledHeight());
/*     */   }
/*     */   
/*     */   public void handleMouseInput() throws IOException {
/* 405 */     int i = Mouse.getEventX() * width / this.mc.displayWidth;
/* 406 */     int j = height - Mouse.getEventY() * height / this.mc.displayHeight - 1;
/* 407 */     int k = Mouse.getEventButton();
/* 408 */     if (Mouse.getEventButtonState()) {
/* 409 */       if ((this.mc.gameSettings.touchscreen) && (this.touchValue++ > 0)) {
/* 410 */         return;
/*     */       }
/*     */       
/* 413 */       this.eventButton = k;
/* 414 */       this.lastMouseEvent = Minecraft.getSystemTime();
/* 415 */       mouseClicked(i, j, this.eventButton);
/* 416 */     } else if (k != -1) {
/* 417 */       if ((this.mc.gameSettings.touchscreen) && (--this.touchValue > 0)) {
/* 418 */         return;
/*     */       }
/*     */       
/* 421 */       this.eventButton = -1;
/* 422 */       mouseReleased(i, j, k);
/* 423 */     } else if ((this.eventButton != -1) && (this.lastMouseEvent > 0L)) {
/* 424 */       long l = Minecraft.getSystemTime() - this.lastMouseEvent;
/* 425 */       mouseClickMove(i, j, this.eventButton, l);
/*     */     }
/*     */   }
/*     */   
/*     */   public void handleKeyboardInput() throws IOException {
/* 430 */     if (Keyboard.getEventKeyState()) {
/* 431 */       keyTyped(Keyboard.getEventCharacter(), Keyboard.getEventKey());
/*     */     }
/*     */     
/* 434 */     this.mc.dispatchKeypresses();
/*     */   }
/*     */   
/*     */ 
/*     */   public void updateScreen() {}
/*     */   
/*     */   public void onGuiClosed() {}
/*     */   
/*     */   public void drawDefaultBackground()
/*     */   {
/* 444 */     drawWorldBackground(0);
/*     */   }
/*     */   
/*     */   public void drawWorldBackground(int tint) {
/* 448 */     if (this.mc.theWorld != null) {
/* 449 */       drawGradientRect(0, 0, width, height, -1072689136, -804253680);
/*     */     } else {
/* 451 */       drawBackground(tint);
/*     */     }
/*     */   }
/*     */   
/*     */   public void drawBackground(int tint) {
/* 456 */     GlStateManager.disableLighting();
/* 457 */     GlStateManager.disableFog();
/* 458 */     Tessellator tessellator = Tessellator.getInstance();
/* 459 */     WorldRenderer worldrenderer = tessellator.getWorldRenderer();
/* 460 */     this.mc.getTextureManager().bindTexture(optionsBackground);
/* 461 */     GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/* 462 */     float f = 32.0F;
/* 463 */     worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
/* 464 */     worldrenderer.pos(0.0D, height, 0.0D)
/* 465 */       .tex(0.0D, height / 32.0F + tint).color(64, 64, 64, 255).endVertex();
/* 466 */     worldrenderer.pos(width, height, 0.0D)
/* 467 */       .tex(width / 32.0F, height / 32.0F + tint)
/* 468 */       .color(64, 64, 64, 255).endVertex();
/* 469 */     worldrenderer.pos(width, 0.0D, 0.0D).tex(width / 32.0F, tint)
/* 470 */       .color(64, 64, 64, 255).endVertex();
/* 471 */     worldrenderer.pos(0.0D, 0.0D, 0.0D).tex(0.0D, tint).color(64, 64, 64, 255).endVertex();
/* 472 */     tessellator.draw();
/*     */   }
/*     */   
/*     */   public boolean doesGuiPauseGame() {
/* 476 */     return true;
/*     */   }
/*     */   
/*     */   public void confirmClicked(boolean result, int id) {
/* 480 */     if (id == 31102009) {
/* 481 */       if (result) {
/* 482 */         openWebLink(this.clickedLinkURI);
/*     */       }
/*     */       
/* 485 */       this.clickedLinkURI = null;
/* 486 */       this.mc.displayGuiScreen(this);
/*     */     }
/*     */   }
/*     */   
/*     */   private void openWebLink(URI p_175282_1_) {
/*     */     try {
/* 492 */       Class<?> oclass = Class.forName("java.awt.Desktop");
/* 493 */       Object object = oclass.getMethod("getDesktop", new Class[0]).invoke(null, new Object[0]);
/* 494 */       oclass.getMethod("browse", new Class[] { URI.class }).invoke(object, new Object[] { p_175282_1_ });
/*     */     } catch (Throwable throwable) {
/* 496 */       LOGGER.error("Couldn't open link", throwable);
/*     */     }
/*     */   }
/*     */   
/*     */   public static boolean isCtrlKeyDown() {
/* 501 */     return (Keyboard.isKeyDown(219)) || (Keyboard.isKeyDown(220));
/*     */   }
/*     */   
/*     */   public static boolean isShiftKeyDown()
/*     */   {
/* 506 */     return (Keyboard.isKeyDown(42)) || (Keyboard.isKeyDown(54));
/*     */   }
/*     */   
/*     */   public static boolean isAltKeyDown() {
/* 510 */     return (Keyboard.isKeyDown(56)) || (Keyboard.isKeyDown(184));
/*     */   }
/*     */   
/*     */   public static boolean isKeyComboCtrlX(int p_175277_0_) {
/* 514 */     return (p_175277_0_ == 45) && (isCtrlKeyDown()) && (!isShiftKeyDown()) && (!isAltKeyDown());
/*     */   }
/*     */   
/*     */   public static boolean isKeyComboCtrlV(int p_175279_0_) {
/* 518 */     return (p_175279_0_ == 47) && (isCtrlKeyDown()) && (!isShiftKeyDown()) && (!isAltKeyDown());
/*     */   }
/*     */   
/*     */   public static boolean isKeyComboCtrlC(int p_175280_0_) {
/* 522 */     return (p_175280_0_ == 46) && (isCtrlKeyDown()) && (!isShiftKeyDown()) && (!isAltKeyDown());
/*     */   }
/*     */   
/*     */   public static boolean isKeyComboCtrlA(int p_175278_0_) {
/* 526 */     return (p_175278_0_ == 30) && (isCtrlKeyDown()) && (!isShiftKeyDown()) && (!isAltKeyDown());
/*     */   }
/*     */   
/*     */   public void onResize(Minecraft mcIn, int p_175273_2_, int p_175273_3_) {
/* 530 */     setWorldAndResolution(mcIn, p_175273_2_, p_175273_3_);
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\gui\GuiScreen.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */