/*     */ package net.minecraft.client.gui;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.audio.SoundCategory;
/*     */ import net.minecraft.client.audio.SoundHandler;
/*     */ import net.minecraft.client.multiplayer.WorldClient;
/*     */ import net.minecraft.client.resources.I18n;
/*     */ import net.minecraft.client.settings.GameSettings;
/*     */ import net.minecraft.client.settings.GameSettings.Options;
/*     */ import net.minecraft.client.stream.IStream;
/*     */ import net.minecraft.util.ChatComponentTranslation;
/*     */ import net.minecraft.util.IChatComponent;
/*     */ import net.minecraft.world.EnumDifficulty;
/*     */ import net.minecraft.world.storage.WorldInfo;
/*     */ 
/*     */ public class GuiOptions extends GuiScreen implements GuiYesNoCallback
/*     */ {
/*  19 */   private static final GameSettings.Options[] field_146440_f = { GameSettings.Options.FOV };
/*     */   private final GuiScreen field_146441_g;
/*     */   private final GameSettings game_settings_1;
/*     */   private GuiButton field_175357_i;
/*     */   private GuiLockIconButton field_175356_r;
/*  24 */   protected String field_146442_a = "Options";
/*     */   
/*     */   public GuiOptions(GuiScreen p_i1046_1_, GameSettings p_i1046_2_) {
/*  27 */     this.field_146441_g = p_i1046_1_;
/*  28 */     this.game_settings_1 = p_i1046_2_;
/*     */   }
/*     */   
/*     */   public void initGui() {
/*  32 */     int i = 0;
/*  33 */     this.field_146442_a = I18n.format("options.title", new Object[0]);
/*     */     GameSettings.Options[] arrayOfOptions;
/*  35 */     int j = (arrayOfOptions = field_146440_f).length; for (int i = 0; i < j; i++) { GameSettings.Options gamesettings$options = arrayOfOptions[i];
/*  36 */       if (gamesettings$options.getEnumFloat()) {
/*  37 */         this.buttonList.add(new GuiOptionSlider(gamesettings$options.returnEnumOrdinal(), width / 2 - 155 + i % 2 * 160, height / 6 - 12 + 24 * (i >> 1), gamesettings$options));
/*     */       } else {
/*  39 */         GuiOptionButton guioptionbutton = new GuiOptionButton(gamesettings$options.returnEnumOrdinal(), width / 2 - 155 + i % 2 * 160, height / 6 - 12 + 24 * (i >> 1), gamesettings$options, this.game_settings_1.getKeyBinding(gamesettings$options));
/*  40 */         this.buttonList.add(guioptionbutton);
/*     */       }
/*     */       
/*  43 */       i++;
/*     */     }
/*     */     
/*  46 */     if (this.mc.theWorld != null) {
/*  47 */       EnumDifficulty enumdifficulty = this.mc.theWorld.getDifficulty();
/*  48 */       this.field_175357_i = new GuiButton(108, width / 2 - 155 + i % 2 * 160, height / 6 - 12 + 24 * (i >> 1), 150, 20, func_175355_a(enumdifficulty));
/*  49 */       this.buttonList.add(this.field_175357_i);
/*  50 */       if ((this.mc.isSingleplayer()) && (!this.mc.theWorld.getWorldInfo().isHardcoreModeEnabled()))
/*     */       {
/*  52 */         this.field_175356_r = new GuiLockIconButton(109, this.field_175357_i.xPosition + this.field_175357_i.getButtonWidth(), this.field_175357_i.yPosition);
/*  53 */         this.buttonList.add(this.field_175356_r);
/*  54 */         this.field_175356_r.func_175229_b(this.mc.theWorld.getWorldInfo().isDifficultyLocked());
/*  55 */         this.field_175356_r.enabled = (!this.field_175356_r.func_175230_c());
/*  56 */         this.field_175357_i.enabled = (!this.field_175356_r.func_175230_c());
/*     */       } else {
/*  58 */         this.field_175357_i.enabled = false;
/*     */       }
/*     */     }
/*     */     
/*  62 */     this.buttonList.add(new GuiButton(110, width / 2 - 155, height / 6 + 48 - 6, 150, 20, I18n.format("options.skinCustomisation", new Object[0])));
/*  63 */     this.buttonList.add(new GuiButton(8675309, width / 2 + 5, height / 6 + 48 - 6, 150, 20, "Super Secret Settings...") {
/*     */       public void playPressSound(SoundHandler soundHandlerIn) {
/*  65 */         net.minecraft.client.audio.SoundEventAccessorComposite soundeventaccessorcomposite = soundHandlerIn.getRandomSoundFromCategories(new SoundCategory[] { SoundCategory.ANIMALS, SoundCategory.BLOCKS, SoundCategory.MOBS, SoundCategory.PLAYERS, SoundCategory.WEATHER });
/*  66 */         if (soundeventaccessorcomposite != null) {
/*  67 */           soundHandlerIn.playSound(net.minecraft.client.audio.PositionedSoundRecord.create(soundeventaccessorcomposite.getSoundEventLocation(), 0.5F));
/*     */         }
/*     */       }
/*  70 */     });
/*  71 */     this.buttonList.add(new GuiButton(106, width / 2 - 155, height / 6 + 72 - 6, 150, 20, I18n.format("options.sounds", new Object[0])));
/*  72 */     this.buttonList.add(new GuiButton(107, width / 2 + 5, height / 6 + 72 - 6, 150, 20, I18n.format("options.stream", new Object[0])));
/*  73 */     this.buttonList.add(new GuiButton(101, width / 2 - 155, height / 6 + 96 - 6, 150, 20, I18n.format("options.video", new Object[0])));
/*  74 */     this.buttonList.add(new GuiButton(100, width / 2 + 5, height / 6 + 96 - 6, 150, 20, I18n.format("options.controls", new Object[0])));
/*  75 */     this.buttonList.add(new GuiButton(102, width / 2 - 155, height / 6 + 120 - 6, 150, 20, I18n.format("options.language", new Object[0])));
/*  76 */     this.buttonList.add(new GuiButton(103, width / 2 + 5, height / 6 + 120 - 6, 150, 20, I18n.format("options.chat.title", new Object[0])));
/*  77 */     this.buttonList.add(new GuiButton(105, width / 2 - 155, height / 6 + 144 - 6, 150, 20, I18n.format("options.resourcepack", new Object[0])));
/*  78 */     this.buttonList.add(new GuiButton(104, width / 2 + 5, height / 6 + 144 - 6, 150, 20, I18n.format("options.snooper.view", new Object[0])));
/*  79 */     this.buttonList.add(new GuiButton(200, width / 2 - 100, height / 6 + 168, I18n.format("gui.done", new Object[0])));
/*     */   }
/*     */   
/*     */   public String func_175355_a(EnumDifficulty p_175355_1_) {
/*  83 */     IChatComponent ichatcomponent = new net.minecraft.util.ChatComponentText("");
/*  84 */     ichatcomponent.appendSibling(new ChatComponentTranslation("options.difficulty", new Object[0]));
/*  85 */     ichatcomponent.appendText(": ");
/*  86 */     ichatcomponent.appendSibling(new ChatComponentTranslation(p_175355_1_.getDifficultyResourceKey(), new Object[0]));
/*  87 */     return ichatcomponent.getFormattedText();
/*     */   }
/*     */   
/*     */   public void confirmClicked(boolean result, int id) {
/*  91 */     this.mc.displayGuiScreen(this);
/*  92 */     if ((id == 109) && (result) && (this.mc.theWorld != null)) {
/*  93 */       this.mc.theWorld.getWorldInfo().setDifficultyLocked(true);
/*  94 */       this.field_175356_r.func_175229_b(true);
/*  95 */       this.field_175356_r.enabled = false;
/*  96 */       this.field_175357_i.enabled = false;
/*     */     }
/*     */   }
/*     */   
/*     */   protected void actionPerformed(GuiButton button) throws java.io.IOException {
/* 101 */     if (button.enabled) {
/* 102 */       if ((button.id < 100) && ((button instanceof GuiOptionButton))) {
/* 103 */         GameSettings.Options gamesettings$options = ((GuiOptionButton)button).returnEnumOptions();
/* 104 */         this.game_settings_1.setOptionValue(gamesettings$options, 1);
/* 105 */         button.displayString = this.game_settings_1.getKeyBinding(GameSettings.Options.getEnumOptions(button.id));
/*     */       }
/*     */       
/* 108 */       if (button.id == 108) {
/* 109 */         this.mc.theWorld.getWorldInfo().setDifficulty(EnumDifficulty.getDifficultyEnum(this.mc.theWorld.getDifficulty().getDifficultyId() + 1));
/* 110 */         this.field_175357_i.displayString = func_175355_a(this.mc.theWorld.getDifficulty());
/*     */       }
/*     */       
/* 113 */       if (button.id == 109) {
/* 114 */         this.mc.displayGuiScreen(new GuiYesNo(this, new ChatComponentTranslation("difficulty.lock.title", new Object[0]).getFormattedText(), new ChatComponentTranslation("difficulty.lock.question", new Object[] { new ChatComponentTranslation(this.mc.theWorld.getWorldInfo().getDifficulty().getDifficultyResourceKey(), new Object[0]) }).getFormattedText(), 109));
/*     */       }
/*     */       
/* 117 */       if (button.id == 110) {
/* 118 */         this.mc.gameSettings.saveOptions();
/* 119 */         this.mc.displayGuiScreen(new GuiCustomizeSkin(this));
/*     */       }
/*     */       
/* 122 */       if (button.id == 8675309) {
/* 123 */         this.mc.entityRenderer.activateNextShader();
/*     */       }
/*     */       
/* 126 */       if (button.id == 101) {
/* 127 */         this.mc.gameSettings.saveOptions();
/* 128 */         this.mc.displayGuiScreen(new GuiVideoSettings(this, this.game_settings_1));
/*     */       }
/*     */       
/* 131 */       if (button.id == 100) {
/* 132 */         this.mc.gameSettings.saveOptions();
/* 133 */         this.mc.displayGuiScreen(new GuiControls(this, this.game_settings_1));
/*     */       }
/*     */       
/* 136 */       if (button.id == 102) {
/* 137 */         this.mc.gameSettings.saveOptions();
/* 138 */         this.mc.displayGuiScreen(new GuiLanguage(this, this.game_settings_1, this.mc.getLanguageManager()));
/*     */       }
/*     */       
/* 141 */       if (button.id == 103) {
/* 142 */         this.mc.gameSettings.saveOptions();
/* 143 */         this.mc.displayGuiScreen(new ScreenChatOptions(this, this.game_settings_1));
/*     */       }
/*     */       
/* 146 */       if (button.id == 104) {
/* 147 */         this.mc.gameSettings.saveOptions();
/* 148 */         this.mc.displayGuiScreen(new GuiSnooper(this, this.game_settings_1));
/*     */       }
/*     */       
/* 151 */       if (button.id == 200) {
/* 152 */         this.mc.gameSettings.saveOptions();
/* 153 */         this.mc.displayGuiScreen(this.field_146441_g);
/*     */       }
/*     */       
/* 156 */       if (button.id == 105) {
/* 157 */         this.mc.gameSettings.saveOptions();
/* 158 */         this.mc.displayGuiScreen(new GuiScreenResourcePacks(this));
/*     */       }
/*     */       
/* 161 */       if (button.id == 106) {
/* 162 */         this.mc.gameSettings.saveOptions();
/* 163 */         this.mc.displayGuiScreen(new GuiScreenOptionsSounds(this, this.game_settings_1));
/*     */       }
/*     */       
/* 166 */       if (button.id == 107) {
/* 167 */         this.mc.gameSettings.saveOptions();
/* 168 */         IStream istream = this.mc.getTwitchStream();
/* 169 */         if ((istream.func_152936_l()) && (istream.func_152928_D())) {
/* 170 */           this.mc.displayGuiScreen(new net.minecraft.client.gui.stream.GuiStreamOptions(this, this.game_settings_1));
/*     */         } else {
/* 172 */           net.minecraft.client.gui.stream.GuiStreamUnavailable.func_152321_a(this);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
/* 179 */     drawDefaultBackground();
/* 180 */     drawCenteredString(this.fontRendererObj, this.field_146442_a, width / 2, 15, 16777215);
/* 181 */     super.drawScreen(mouseX, mouseY, partialTicks);
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\gui\GuiOptions.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */