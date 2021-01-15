/*     */ package net.minecraft.client.gui;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.resources.I18n;
/*     */ import net.minecraft.client.resources.Language;
/*     */ import net.minecraft.client.resources.LanguageManager;
/*     */ import net.minecraft.client.settings.GameSettings;
/*     */ 
/*     */ public class GuiLanguage extends GuiScreen
/*     */ {
/*     */   protected GuiScreen parentScreen;
/*     */   private List list;
/*     */   private final GameSettings game_settings_3;
/*     */   private final LanguageManager languageManager;
/*     */   private GuiOptionButton forceUnicodeFontBtn;
/*     */   private GuiOptionButton confirmSettingsBtn;
/*     */   
/*     */   public GuiLanguage(GuiScreen screen, GameSettings gameSettingsObj, LanguageManager manager)
/*     */   {
/*  22 */     this.parentScreen = screen;
/*  23 */     this.game_settings_3 = gameSettingsObj;
/*  24 */     this.languageManager = manager;
/*     */   }
/*     */   
/*     */   public void initGui() {
/*  28 */     this.buttonList.add(this.forceUnicodeFontBtn = new GuiOptionButton(100, width / 2 - 155, height - 38, net.minecraft.client.settings.GameSettings.Options.FORCE_UNICODE_FONT, this.game_settings_3.getKeyBinding(net.minecraft.client.settings.GameSettings.Options.FORCE_UNICODE_FONT)));
/*  29 */     this.buttonList.add(this.confirmSettingsBtn = new GuiOptionButton(6, width / 2 - 155 + 160, height - 38, I18n.format("gui.done", new Object[0])));
/*  30 */     this.list = new List(this.mc);
/*  31 */     this.list.registerScrollButtons(7, 8);
/*     */   }
/*     */   
/*     */   public void handleMouseInput() throws java.io.IOException {
/*  35 */     super.handleMouseInput();
/*  36 */     this.list.handleMouseInput();
/*     */   }
/*     */   
/*     */   protected void actionPerformed(GuiButton button) throws java.io.IOException {
/*  40 */     if (button.enabled) {
/*  41 */       switch (button.id) {
/*     */       case 5: 
/*     */         break;
/*     */       case 6: 
/*  45 */         this.mc.displayGuiScreen(this.parentScreen);
/*  46 */         break;
/*     */       case 100: 
/*  48 */         if ((button instanceof GuiOptionButton)) {
/*  49 */           this.game_settings_3.setOptionValue(((GuiOptionButton)button).returnEnumOptions(), 1);
/*  50 */           button.displayString = this.game_settings_3.getKeyBinding(net.minecraft.client.settings.GameSettings.Options.FORCE_UNICODE_FONT);
/*  51 */           ScaledResolution scaledresolution = new ScaledResolution(this.mc, this.mc.displayWidth, this.mc.displayHeight);
/*  52 */           int i = ScaledResolution.getScaledWidth();
/*  53 */           int j = ScaledResolution.getScaledHeight();
/*  54 */           setWorldAndResolution(this.mc, i, j);
/*     */         }
/*  56 */         break;
/*     */       default: 
/*  58 */         this.list.actionPerformed(button);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
/*  64 */     this.list.drawScreen(mouseX, mouseY, partialTicks);
/*  65 */     drawCenteredString(this.fontRendererObj, I18n.format("options.language", new Object[0]), width / 2, 16, 16777215);
/*  66 */     drawCenteredString(this.fontRendererObj, "(" + I18n.format("options.languageWarning", new Object[0]) + ")", width / 2, height - 56, 8421504);
/*  67 */     super.drawScreen(mouseX, mouseY, partialTicks);
/*     */   }
/*     */   
/*     */   class List extends GuiSlot {
/*  71 */     private final List<String> langCodeList = com.google.common.collect.Lists.newArrayList();
/*  72 */     private final Map<String, Language> languageMap = com.google.common.collect.Maps.newHashMap();
/*     */     
/*     */     public List(Minecraft mcIn) {
/*  75 */       super(GuiLanguage.width, GuiLanguage.height, 32, GuiLanguage.height - 65 + 4, 18);
/*     */       
/*  77 */       for (Language language : GuiLanguage.this.languageManager.getLanguages()) {
/*  78 */         this.languageMap.put(language.getLanguageCode(), language);
/*  79 */         this.langCodeList.add(language.getLanguageCode());
/*     */       }
/*     */     }
/*     */     
/*     */     protected int getSize() {
/*  84 */       return this.langCodeList.size();
/*     */     }
/*     */     
/*     */     protected void elementClicked(int slotIndex, boolean isDoubleClick, int mouseX, int mouseY) {
/*  88 */       Language language = (Language)this.languageMap.get(this.langCodeList.get(slotIndex));
/*  89 */       GuiLanguage.this.languageManager.setCurrentLanguage(language);
/*  90 */       GuiLanguage.this.game_settings_3.language = language.getLanguageCode();
/*  91 */       this.mc.refreshResources();
/*  92 */       GuiLanguage.this.fontRendererObj.setUnicodeFlag((GuiLanguage.this.languageManager.isCurrentLocaleUnicode()) || (GuiLanguage.this.game_settings_3.forceUnicodeFont));
/*  93 */       GuiLanguage.this.fontRendererObj.setBidiFlag(GuiLanguage.this.languageManager.isCurrentLanguageBidirectional());
/*  94 */       GuiLanguage.this.confirmSettingsBtn.displayString = I18n.format("gui.done", new Object[0]);
/*  95 */       GuiLanguage.this.forceUnicodeFontBtn.displayString = GuiLanguage.this.game_settings_3.getKeyBinding(net.minecraft.client.settings.GameSettings.Options.FORCE_UNICODE_FONT);
/*  96 */       GuiLanguage.this.game_settings_3.saveOptions();
/*     */     }
/*     */     
/*     */     protected boolean isSelected(int slotIndex) {
/* 100 */       return ((String)this.langCodeList.get(slotIndex)).equals(GuiLanguage.this.languageManager.getCurrentLanguage().getLanguageCode());
/*     */     }
/*     */     
/*     */     protected int getContentHeight() {
/* 104 */       return getSize() * 18;
/*     */     }
/*     */     
/*     */     protected void drawBackground() {
/* 108 */       GuiLanguage.this.drawDefaultBackground();
/*     */     }
/*     */     
/*     */     protected void drawSlot(int entryID, int p_180791_2_, int p_180791_3_, int p_180791_4_, int mouseXIn, int mouseYIn) {
/* 112 */       GuiLanguage.this.fontRendererObj.setBidiFlag(true);
/* 113 */       GuiLanguage.this.drawCenteredString(GuiLanguage.this.fontRendererObj, ((Language)this.languageMap.get(this.langCodeList.get(entryID))).toString(), this.width / 2, p_180791_3_ + 1, 16777215);
/* 114 */       GuiLanguage.this.fontRendererObj.setBidiFlag(GuiLanguage.this.languageManager.getCurrentLanguage().isBidirectional());
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\gui\GuiLanguage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */