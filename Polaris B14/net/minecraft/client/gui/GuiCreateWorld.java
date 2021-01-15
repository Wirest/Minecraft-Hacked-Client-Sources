/*     */ package net.minecraft.client.gui;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.List;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.resources.I18n;
/*     */ import net.minecraft.world.WorldSettings;
/*     */ import net.minecraft.world.WorldSettings.GameType;
/*     */ import net.minecraft.world.WorldType;
/*     */ import net.minecraft.world.storage.ISaveFormat;
/*     */ import net.minecraft.world.storage.WorldInfo;
/*     */ 
/*     */ public class GuiCreateWorld extends GuiScreen
/*     */ {
/*     */   private GuiScreen parentScreen;
/*     */   private GuiTextField field_146333_g;
/*     */   private GuiTextField field_146335_h;
/*     */   private String field_146336_i;
/*  19 */   private String gameMode = "survival";
/*     */   private String field_175300_s;
/*  21 */   private boolean field_146341_s = true;
/*     */   private boolean allowCheats;
/*     */   private boolean field_146339_u;
/*     */   private boolean field_146338_v;
/*     */   private boolean field_146337_w;
/*     */   private boolean field_146345_x;
/*     */   private boolean field_146344_y;
/*     */   private GuiButton btnGameMode;
/*     */   private GuiButton btnMoreOptions;
/*     */   private GuiButton btnMapFeatures;
/*     */   private GuiButton btnBonusItems;
/*     */   private GuiButton btnMapType;
/*     */   private GuiButton btnAllowCommands;
/*     */   private GuiButton btnCustomizeType;
/*     */   private String field_146323_G;
/*     */   private String field_146328_H;
/*     */   private String field_146329_I;
/*     */   private String field_146330_J;
/*     */   private int selectedIndex;
/*  40 */   public String chunkProviderSettingsJson = "";
/*  41 */   private static final String[] disallowedFilenames = { "CON", "COM", "PRN", "AUX", "CLOCK$", "NUL", "COM1", "COM2", "COM3", "COM4", "COM5", "COM6", "COM7", "COM8", "COM9", "LPT1", "LPT2", "LPT3", "LPT4", "LPT5", "LPT6", "LPT7", "LPT8", "LPT9" };
/*     */   
/*     */   public GuiCreateWorld(GuiScreen p_i46320_1_) {
/*  44 */     this.parentScreen = p_i46320_1_;
/*  45 */     this.field_146329_I = "";
/*  46 */     this.field_146330_J = I18n.format("selectWorld.newWorld", new Object[0]);
/*     */   }
/*     */   
/*     */   public void updateScreen() {
/*  50 */     this.field_146333_g.updateCursorCounter();
/*  51 */     this.field_146335_h.updateCursorCounter();
/*     */   }
/*     */   
/*     */   public void initGui() {
/*  55 */     org.lwjgl.input.Keyboard.enableRepeatEvents(true);
/*  56 */     this.buttonList.clear();
/*  57 */     this.buttonList.add(new GuiButton(0, width / 2 - 155, height - 28, 150, 20, I18n.format("selectWorld.create", new Object[0])));
/*  58 */     this.buttonList.add(new GuiButton(1, width / 2 + 5, height - 28, 150, 20, I18n.format("gui.cancel", new Object[0])));
/*  59 */     this.buttonList.add(this.btnGameMode = new GuiButton(2, width / 2 - 75, 115, 150, 20, I18n.format("selectWorld.gameMode", new Object[0])));
/*  60 */     this.buttonList.add(this.btnMoreOptions = new GuiButton(3, width / 2 - 75, 187, 150, 20, I18n.format("selectWorld.moreWorldOptions", new Object[0])));
/*  61 */     this.buttonList.add(this.btnMapFeatures = new GuiButton(4, width / 2 - 155, 100, 150, 20, I18n.format("selectWorld.mapFeatures", new Object[0])));
/*  62 */     this.btnMapFeatures.visible = false;
/*  63 */     this.buttonList.add(this.btnBonusItems = new GuiButton(7, width / 2 + 5, 151, 150, 20, I18n.format("selectWorld.bonusItems", new Object[0])));
/*  64 */     this.btnBonusItems.visible = false;
/*  65 */     this.buttonList.add(this.btnMapType = new GuiButton(5, width / 2 + 5, 100, 150, 20, I18n.format("selectWorld.mapType", new Object[0])));
/*  66 */     this.btnMapType.visible = false;
/*  67 */     this.buttonList.add(this.btnAllowCommands = new GuiButton(6, width / 2 - 155, 151, 150, 20, I18n.format("selectWorld.allowCommands", new Object[0])));
/*  68 */     this.btnAllowCommands.visible = false;
/*  69 */     this.buttonList.add(this.btnCustomizeType = new GuiButton(8, width / 2 + 5, 120, 150, 20, I18n.format("selectWorld.customizeType", new Object[0])));
/*  70 */     this.btnCustomizeType.visible = false;
/*  71 */     this.field_146333_g = new GuiTextField(9, this.fontRendererObj, width / 2 - 100, 60, 200, 20);
/*  72 */     this.field_146333_g.setFocused(true);
/*  73 */     this.field_146333_g.setText(this.field_146330_J);
/*  74 */     this.field_146335_h = new GuiTextField(10, this.fontRendererObj, width / 2 - 100, 60, 200, 20);
/*  75 */     this.field_146335_h.setText(this.field_146329_I);
/*  76 */     func_146316_a(this.field_146344_y);
/*  77 */     func_146314_g();
/*  78 */     func_146319_h();
/*     */   }
/*     */   
/*     */   private void func_146314_g() {
/*  82 */     this.field_146336_i = this.field_146333_g.getText().trim();
/*     */     char[] arrayOfChar;
/*  84 */     int j = (arrayOfChar = net.minecraft.util.ChatAllowedCharacters.allowedCharactersArray).length; for (int i = 0; i < j; i++) { char c0 = arrayOfChar[i];
/*  85 */       this.field_146336_i = this.field_146336_i.replace(c0, '_');
/*     */     }
/*     */     
/*  88 */     if (org.apache.commons.lang3.StringUtils.isEmpty(this.field_146336_i)) {
/*  89 */       this.field_146336_i = "World";
/*     */     }
/*     */     
/*  92 */     this.field_146336_i = func_146317_a(this.mc.getSaveLoader(), this.field_146336_i);
/*     */   }
/*     */   
/*     */   private void func_146319_h() {
/*  96 */     this.btnGameMode.displayString = (I18n.format("selectWorld.gameMode", new Object[0]) + ": " + I18n.format(new StringBuilder("selectWorld.gameMode.").append(this.gameMode).toString(), new Object[0]));
/*  97 */     this.field_146323_G = I18n.format("selectWorld.gameMode." + this.gameMode + ".line1", new Object[0]);
/*  98 */     this.field_146328_H = I18n.format("selectWorld.gameMode." + this.gameMode + ".line2", new Object[0]);
/*  99 */     this.btnMapFeatures.displayString = (I18n.format("selectWorld.mapFeatures", new Object[0]) + " ");
/* 100 */     if (this.field_146341_s) {
/* 101 */       this.btnMapFeatures.displayString += I18n.format("options.on", new Object[0]);
/*     */     } else {
/* 103 */       this.btnMapFeatures.displayString += I18n.format("options.off", new Object[0]);
/*     */     }
/*     */     
/* 106 */     this.btnBonusItems.displayString = (I18n.format("selectWorld.bonusItems", new Object[0]) + " ");
/* 107 */     if ((this.field_146338_v) && (!this.field_146337_w)) {
/* 108 */       this.btnBonusItems.displayString += I18n.format("options.on", new Object[0]);
/*     */     } else {
/* 110 */       this.btnBonusItems.displayString += I18n.format("options.off", new Object[0]);
/*     */     }
/*     */     
/* 113 */     this.btnMapType.displayString = (I18n.format("selectWorld.mapType", new Object[0]) + " " + I18n.format(WorldType.worldTypes[this.selectedIndex].getTranslateName(), new Object[0]));
/* 114 */     this.btnAllowCommands.displayString = (I18n.format("selectWorld.allowCommands", new Object[0]) + " ");
/* 115 */     if ((this.allowCheats) && (!this.field_146337_w)) {
/* 116 */       this.btnAllowCommands.displayString += I18n.format("options.on", new Object[0]);
/*     */     } else {
/* 118 */       this.btnAllowCommands.displayString += I18n.format("options.off", new Object[0]);
/*     */     }
/*     */   }
/*     */   
/*     */   public static String func_146317_a(ISaveFormat p_146317_0_, String p_146317_1_) {
/* 123 */     p_146317_1_ = p_146317_1_.replaceAll("[\\./\"]", "_");
/*     */     String[] arrayOfString;
/* 125 */     int j = (arrayOfString = disallowedFilenames).length; for (int i = 0; i < j; i++) { String s = arrayOfString[i];
/* 126 */       if (p_146317_1_.equalsIgnoreCase(s)) {
/* 127 */         p_146317_1_ = "_" + p_146317_1_ + "_";
/*     */       }
/*     */     }
/*     */     
/* 131 */     while (p_146317_0_.getWorldInfo(p_146317_1_) != null) {
/* 132 */       p_146317_1_ = p_146317_1_ + "-";
/*     */     }
/*     */     
/* 135 */     return p_146317_1_;
/*     */   }
/*     */   
/*     */   public void onGuiClosed() {
/* 139 */     org.lwjgl.input.Keyboard.enableRepeatEvents(false);
/*     */   }
/*     */   
/*     */   protected void actionPerformed(GuiButton button) throws IOException {
/* 143 */     if (button.enabled) {
/* 144 */       if (button.id == 1) {
/* 145 */         this.mc.displayGuiScreen(this.parentScreen);
/* 146 */       } else if (button.id == 0) {
/* 147 */         this.mc.displayGuiScreen(null);
/* 148 */         if (this.field_146345_x) {
/* 149 */           return;
/*     */         }
/*     */         
/* 152 */         this.field_146345_x = true;
/* 153 */         long i = new java.util.Random().nextLong();
/* 154 */         String s = this.field_146335_h.getText();
/* 155 */         if (!org.apache.commons.lang3.StringUtils.isEmpty(s)) {
/*     */           try {
/* 157 */             long j = Long.parseLong(s);
/* 158 */             if (j != 0L) {
/* 159 */               i = j;
/*     */             }
/*     */           } catch (NumberFormatException var7) {
/* 162 */             i = s.hashCode();
/*     */           }
/*     */         }
/*     */         
/* 166 */         WorldSettings.GameType worldsettings$gametype = WorldSettings.GameType.getByName(this.gameMode);
/* 167 */         WorldSettings worldsettings = new WorldSettings(i, worldsettings$gametype, this.field_146341_s, this.field_146337_w, WorldType.worldTypes[this.selectedIndex]);
/* 168 */         worldsettings.setWorldName(this.chunkProviderSettingsJson);
/* 169 */         if ((this.field_146338_v) && (!this.field_146337_w)) {
/* 170 */           worldsettings.enableBonusChest();
/*     */         }
/*     */         
/* 173 */         if ((this.allowCheats) && (!this.field_146337_w)) {
/* 174 */           worldsettings.enableCommands();
/*     */         }
/*     */         
/* 177 */         this.mc.launchIntegratedServer(this.field_146336_i, this.field_146333_g.getText().trim(), worldsettings);
/* 178 */       } else if (button.id == 3) {
/* 179 */         func_146315_i();
/* 180 */       } else if (button.id == 2) {
/* 181 */         if (this.gameMode.equals("survival")) {
/* 182 */           if (!this.field_146339_u) {
/* 183 */             this.allowCheats = false;
/*     */           }
/*     */           
/* 186 */           this.field_146337_w = false;
/* 187 */           this.gameMode = "hardcore";
/* 188 */           this.field_146337_w = true;
/* 189 */           this.btnAllowCommands.enabled = false;
/* 190 */           this.btnBonusItems.enabled = false;
/* 191 */           func_146319_h();
/* 192 */         } else if (this.gameMode.equals("hardcore")) {
/* 193 */           if (!this.field_146339_u) {
/* 194 */             this.allowCheats = true;
/*     */           }
/*     */           
/* 197 */           this.field_146337_w = false;
/* 198 */           this.gameMode = "creative";
/* 199 */           func_146319_h();
/* 200 */           this.field_146337_w = false;
/* 201 */           this.btnAllowCommands.enabled = true;
/* 202 */           this.btnBonusItems.enabled = true;
/*     */         } else {
/* 204 */           if (!this.field_146339_u) {
/* 205 */             this.allowCheats = false;
/*     */           }
/*     */           
/* 208 */           this.gameMode = "survival";
/* 209 */           func_146319_h();
/* 210 */           this.btnAllowCommands.enabled = true;
/* 211 */           this.btnBonusItems.enabled = true;
/* 212 */           this.field_146337_w = false;
/*     */         }
/*     */         
/* 215 */         func_146319_h();
/* 216 */       } else if (button.id == 4) {
/* 217 */         this.field_146341_s = (!this.field_146341_s);
/* 218 */         func_146319_h();
/* 219 */       } else if (button.id == 7) {
/* 220 */         this.field_146338_v = (!this.field_146338_v);
/* 221 */         func_146319_h();
/* 222 */       } else if (button.id == 5) {
/* 223 */         this.selectedIndex += 1;
/* 224 */         if (this.selectedIndex >= WorldType.worldTypes.length) {
/* 225 */           this.selectedIndex = 0;
/*     */         }
/*     */         
/* 228 */         while (!func_175299_g()) {
/* 229 */           this.selectedIndex += 1;
/* 230 */           if (this.selectedIndex >= WorldType.worldTypes.length) {
/* 231 */             this.selectedIndex = 0;
/*     */           }
/*     */         }
/*     */         
/* 235 */         this.chunkProviderSettingsJson = "";
/* 236 */         func_146319_h();
/* 237 */         func_146316_a(this.field_146344_y);
/* 238 */       } else if (button.id == 6) {
/* 239 */         this.field_146339_u = true;
/* 240 */         this.allowCheats = (!this.allowCheats);
/* 241 */         func_146319_h();
/* 242 */       } else if (button.id == 8) {
/* 243 */         if (WorldType.worldTypes[this.selectedIndex] == WorldType.FLAT) {
/* 244 */           this.mc.displayGuiScreen(new GuiCreateFlatWorld(this, this.chunkProviderSettingsJson));
/*     */         } else {
/* 246 */           this.mc.displayGuiScreen(new GuiCustomizeWorldScreen(this, this.chunkProviderSettingsJson));
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private boolean func_175299_g() {
/* 253 */     WorldType worldtype = WorldType.worldTypes[this.selectedIndex];
/* 254 */     return (worldtype != null) && (worldtype.getCanBeCreated()) ? true : worldtype == WorldType.DEBUG_WORLD ? isShiftKeyDown() : false;
/*     */   }
/*     */   
/*     */   private void func_146315_i() {
/* 258 */     func_146316_a(!this.field_146344_y);
/*     */   }
/*     */   
/*     */   private void func_146316_a(boolean p_146316_1_) {
/* 262 */     this.field_146344_y = p_146316_1_;
/* 263 */     if (WorldType.worldTypes[this.selectedIndex] == WorldType.DEBUG_WORLD) {
/* 264 */       this.btnGameMode.visible = (!this.field_146344_y);
/* 265 */       this.btnGameMode.enabled = false;
/* 266 */       if (this.field_175300_s == null) {
/* 267 */         this.field_175300_s = this.gameMode;
/*     */       }
/*     */       
/* 270 */       this.gameMode = "spectator";
/* 271 */       this.btnMapFeatures.visible = false;
/* 272 */       this.btnBonusItems.visible = false;
/* 273 */       this.btnMapType.visible = this.field_146344_y;
/* 274 */       this.btnAllowCommands.visible = false;
/* 275 */       this.btnCustomizeType.visible = false;
/*     */     } else {
/* 277 */       this.btnGameMode.visible = (!this.field_146344_y);
/* 278 */       this.btnGameMode.enabled = true;
/* 279 */       if (this.field_175300_s != null) {
/* 280 */         this.gameMode = this.field_175300_s;
/* 281 */         this.field_175300_s = null;
/*     */       }
/*     */       
/* 284 */       this.btnMapFeatures.visible = ((this.field_146344_y) && (WorldType.worldTypes[this.selectedIndex] != WorldType.CUSTOMIZED));
/* 285 */       this.btnBonusItems.visible = this.field_146344_y;
/* 286 */       this.btnMapType.visible = this.field_146344_y;
/* 287 */       this.btnAllowCommands.visible = this.field_146344_y;
/* 288 */       this.btnCustomizeType.visible = ((this.field_146344_y) && ((WorldType.worldTypes[this.selectedIndex] == WorldType.FLAT) || (WorldType.worldTypes[this.selectedIndex] == WorldType.CUSTOMIZED)));
/*     */     }
/*     */     
/* 291 */     func_146319_h();
/* 292 */     if (this.field_146344_y) {
/* 293 */       this.btnMoreOptions.displayString = I18n.format("gui.done", new Object[0]);
/*     */     } else {
/* 295 */       this.btnMoreOptions.displayString = I18n.format("selectWorld.moreWorldOptions", new Object[0]);
/*     */     }
/*     */   }
/*     */   
/*     */   protected void keyTyped(char typedChar, int keyCode) throws IOException {
/* 300 */     if ((this.field_146333_g.isFocused()) && (!this.field_146344_y)) {
/* 301 */       this.field_146333_g.textboxKeyTyped(typedChar, keyCode);
/* 302 */       this.field_146330_J = this.field_146333_g.getText();
/* 303 */     } else if ((this.field_146335_h.isFocused()) && (this.field_146344_y)) {
/* 304 */       this.field_146335_h.textboxKeyTyped(typedChar, keyCode);
/* 305 */       this.field_146329_I = this.field_146335_h.getText();
/*     */     }
/*     */     
/* 308 */     if ((keyCode == 28) || (keyCode == 156)) {
/* 309 */       actionPerformed((GuiButton)this.buttonList.get(0));
/*     */     }
/*     */     
/* 312 */     ((GuiButton)this.buttonList.get(0)).enabled = (this.field_146333_g.getText().length() > 0);
/* 313 */     func_146314_g();
/*     */   }
/*     */   
/*     */   protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
/* 317 */     super.mouseClicked(mouseX, mouseY, mouseButton);
/* 318 */     if (this.field_146344_y) {
/* 319 */       this.field_146335_h.mouseClicked(mouseX, mouseY, mouseButton);
/*     */     } else {
/* 321 */       this.field_146333_g.mouseClicked(mouseX, mouseY, mouseButton);
/*     */     }
/*     */   }
/*     */   
/*     */   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
/* 326 */     drawDefaultBackground();
/* 327 */     drawCenteredString(this.fontRendererObj, I18n.format("selectWorld.create", new Object[0]), width / 2, 20, -1);
/* 328 */     if (this.field_146344_y) {
/* 329 */       drawString(this.fontRendererObj, I18n.format("selectWorld.enterSeed", new Object[0]), width / 2 - 100, 47, -6250336);
/* 330 */       drawString(this.fontRendererObj, I18n.format("selectWorld.seedInfo", new Object[0]), width / 2 - 100, 85, -6250336);
/* 331 */       if (this.btnMapFeatures.visible) {
/* 332 */         drawString(this.fontRendererObj, I18n.format("selectWorld.mapFeatures.info", new Object[0]), width / 2 - 150, 122, -6250336);
/*     */       }
/*     */       
/* 335 */       if (this.btnAllowCommands.visible) {
/* 336 */         drawString(this.fontRendererObj, I18n.format("selectWorld.allowCommands.info", new Object[0]), width / 2 - 150, 172, -6250336);
/*     */       }
/*     */       
/* 339 */       this.field_146335_h.drawTextBox();
/* 340 */       if (WorldType.worldTypes[this.selectedIndex].showWorldInfoNotice()) {
/* 341 */         this.fontRendererObj.drawSplitString(I18n.format(WorldType.worldTypes[this.selectedIndex].func_151359_c(), new Object[0]), this.btnMapType.xPosition + 2, this.btnMapType.yPosition + 22, this.btnMapType.getButtonWidth(), 10526880);
/*     */       }
/*     */     } else {
/* 344 */       drawString(this.fontRendererObj, I18n.format("selectWorld.enterName", new Object[0]), width / 2 - 100, 47, -6250336);
/* 345 */       drawString(this.fontRendererObj, I18n.format("selectWorld.resultFolder", new Object[0]) + " " + this.field_146336_i, width / 2 - 100, 85, -6250336);
/* 346 */       this.field_146333_g.drawTextBox();
/* 347 */       drawString(this.fontRendererObj, this.field_146323_G, width / 2 - 100, 137, -6250336);
/* 348 */       drawString(this.fontRendererObj, this.field_146328_H, width / 2 - 100, 149, -6250336);
/*     */     }
/*     */     
/* 351 */     super.drawScreen(mouseX, mouseY, partialTicks);
/*     */   }
/*     */   
/*     */   public void func_146318_a(WorldInfo p_146318_1_) {
/* 355 */     this.field_146330_J = I18n.format("selectWorld.newWorld.copyOf", new Object[] { p_146318_1_.getWorldName() });
/* 356 */     this.field_146329_I = p_146318_1_.getSeed();
/* 357 */     this.selectedIndex = p_146318_1_.getTerrainType().getWorldTypeID();
/* 358 */     this.chunkProviderSettingsJson = p_146318_1_.getGeneratorOptions();
/* 359 */     this.field_146341_s = p_146318_1_.isMapFeaturesEnabled();
/* 360 */     this.allowCheats = p_146318_1_.areCommandsAllowed();
/* 361 */     if (p_146318_1_.isHardcoreModeEnabled()) {
/* 362 */       this.gameMode = "hardcore";
/* 363 */     } else if (p_146318_1_.getGameType().isSurvivalOrAdventure()) {
/* 364 */       this.gameMode = "survival";
/* 365 */     } else if (p_146318_1_.getGameType().isCreative()) {
/* 366 */       this.gameMode = "creative";
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\gui\GuiCreateWorld.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */