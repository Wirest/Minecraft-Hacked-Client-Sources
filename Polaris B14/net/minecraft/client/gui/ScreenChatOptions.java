/*    */ package net.minecraft.client.gui;
/*    */ 
/*    */ import net.minecraft.client.settings.GameSettings;
/*    */ import net.minecraft.client.settings.GameSettings.Options;
/*    */ 
/*    */ public class ScreenChatOptions extends GuiScreen
/*    */ {
/*  8 */   private static final GameSettings.Options[] field_146399_a = { GameSettings.Options.CHAT_VISIBILITY, GameSettings.Options.CHAT_COLOR, GameSettings.Options.CHAT_LINKS, GameSettings.Options.CHAT_OPACITY, GameSettings.Options.CHAT_LINKS_PROMPT, GameSettings.Options.CHAT_SCALE, GameSettings.Options.CHAT_HEIGHT_FOCUSED, GameSettings.Options.CHAT_HEIGHT_UNFOCUSED, GameSettings.Options.CHAT_WIDTH, GameSettings.Options.REDUCED_DEBUG_INFO };
/*    */   private final GuiScreen parentScreen;
/*    */   private final GameSettings game_settings;
/*    */   private String field_146401_i;
/*    */   
/*    */   public ScreenChatOptions(GuiScreen parentScreenIn, GameSettings gameSettingsIn) {
/* 14 */     this.parentScreen = parentScreenIn;
/* 15 */     this.game_settings = gameSettingsIn;
/*    */   }
/*    */   
/*    */   public void initGui() {
/* 19 */     int i = 0;
/* 20 */     this.field_146401_i = net.minecraft.client.resources.I18n.format("options.chat.title", new Object[0]);
/*    */     GameSettings.Options[] arrayOfOptions;
/* 22 */     int j = (arrayOfOptions = field_146399_a).length; for (int i = 0; i < j; i++) { GameSettings.Options gamesettings$options = arrayOfOptions[i];
/* 23 */       if (gamesettings$options.getEnumFloat()) {
/* 24 */         this.buttonList.add(new GuiOptionSlider(gamesettings$options.returnEnumOrdinal(), width / 2 - 155 + i % 2 * 160, height / 6 + 24 * (i >> 1), gamesettings$options));
/*    */       } else {
/* 26 */         this.buttonList.add(new GuiOptionButton(gamesettings$options.returnEnumOrdinal(), width / 2 - 155 + i % 2 * 160, height / 6 + 24 * (i >> 1), gamesettings$options, this.game_settings.getKeyBinding(gamesettings$options)));
/*    */       }
/*    */       
/* 29 */       i++;
/*    */     }
/*    */     
/* 32 */     this.buttonList.add(new GuiButton(200, width / 2 - 100, height / 6 + 120, net.minecraft.client.resources.I18n.format("gui.done", new Object[0])));
/*    */   }
/*    */   
/*    */   protected void actionPerformed(GuiButton button) throws java.io.IOException {
/* 36 */     if (button.enabled) {
/* 37 */       if ((button.id < 100) && ((button instanceof GuiOptionButton))) {
/* 38 */         this.game_settings.setOptionValue(((GuiOptionButton)button).returnEnumOptions(), 1);
/* 39 */         button.displayString = this.game_settings.getKeyBinding(GameSettings.Options.getEnumOptions(button.id));
/*    */       }
/*    */       
/* 42 */       if (button.id == 200) {
/* 43 */         this.mc.gameSettings.saveOptions();
/* 44 */         this.mc.displayGuiScreen(this.parentScreen);
/*    */       }
/*    */     }
/*    */   }
/*    */   
/*    */   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
/* 50 */     drawDefaultBackground();
/* 51 */     drawCenteredString(this.fontRendererObj, this.field_146401_i, width / 2, 20, 16777215);
/* 52 */     super.drawScreen(mouseX, mouseY, partialTicks);
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\gui\ScreenChatOptions.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */