/*     */ package optfine;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.GuiButton;
/*     */ import net.minecraft.client.gui.GuiOptionButton;
/*     */ import net.minecraft.client.gui.GuiOptionSlider;
/*     */ import net.minecraft.client.gui.GuiScreen;
/*     */ import net.minecraft.client.gui.GuiVideoSettings;
/*     */ import net.minecraft.client.gui.GuiYesNo;
/*     */ import net.minecraft.client.gui.ScaledResolution;
/*     */ import net.minecraft.client.settings.GameSettings;
/*     */ import net.minecraft.client.settings.GameSettings.Options;
/*     */ 
/*     */ public class GuiOtherSettingsOF extends GuiScreen implements net.minecraft.client.gui.GuiYesNoCallback
/*     */ {
/*     */   private GuiScreen prevScreen;
/*  18 */   protected String title = "Other Settings";
/*     */   private GameSettings settings;
/*  20 */   private static GameSettings.Options[] enumOptions = { GameSettings.Options.LAGOMETER, GameSettings.Options.PROFILER, GameSettings.Options.WEATHER, GameSettings.Options.TIME, GameSettings.Options.USE_FULLSCREEN, GameSettings.Options.FULLSCREEN_MODE, GameSettings.Options.SHOW_FPS, GameSettings.Options.AUTOSAVE_TICKS };
/*  21 */   private int lastMouseX = 0;
/*  22 */   private int lastMouseY = 0;
/*  23 */   private long mouseStillTime = 0L;
/*     */   
/*     */   public GuiOtherSettingsOF(GuiScreen p_i36_1_, GameSettings p_i36_2_)
/*     */   {
/*  27 */     this.prevScreen = p_i36_1_;
/*  28 */     this.settings = p_i36_2_;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void initGui()
/*     */   {
/*  37 */     int i = 0;
/*     */     GameSettings.Options[] arrayOfOptions;
/*  39 */     int j = (arrayOfOptions = enumOptions).length; for (int i = 0; i < j; i++) { GameSettings.Options gamesettings$options = arrayOfOptions[i];
/*     */       
/*  41 */       int j = width / 2 - 155 + i % 2 * 160;
/*  42 */       int k = height / 6 + 21 * (i / 2) - 10;
/*     */       
/*  44 */       if (!gamesettings$options.getEnumFloat())
/*     */       {
/*  46 */         this.buttonList.add(new GuiOptionButton(gamesettings$options.returnEnumOrdinal(), j, k, gamesettings$options, this.settings.getKeyBinding(gamesettings$options)));
/*     */       }
/*     */       else
/*     */       {
/*  50 */         this.buttonList.add(new GuiOptionSlider(gamesettings$options.returnEnumOrdinal(), j, k, gamesettings$options));
/*     */       }
/*     */       
/*  53 */       i++;
/*     */     }
/*     */     
/*  56 */     this.buttonList.add(new GuiButton(210, width / 2 - 100, height / 6 + 168 + 11 - 44, "Reset Video Settings..."));
/*  57 */     this.buttonList.add(new GuiButton(200, width / 2 - 100, height / 6 + 168 + 11, net.minecraft.client.resources.I18n.format("gui.done", new Object[0])));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void actionPerformed(GuiButton button)
/*     */   {
/*  65 */     if (button.enabled)
/*     */     {
/*  67 */       if ((button.id < 200) && ((button instanceof GuiOptionButton)))
/*     */       {
/*  69 */         this.settings.setOptionValue(((GuiOptionButton)button).returnEnumOptions(), 1);
/*  70 */         button.displayString = this.settings.getKeyBinding(GameSettings.Options.getEnumOptions(button.id));
/*     */       }
/*     */       
/*  73 */       if (button.id == 200)
/*     */       {
/*  75 */         this.mc.gameSettings.saveOptions();
/*  76 */         this.mc.displayGuiScreen(this.prevScreen);
/*     */       }
/*     */       
/*  79 */       if (button.id == 210)
/*     */       {
/*  81 */         this.mc.gameSettings.saveOptions();
/*  82 */         GuiYesNo guiyesno = new GuiYesNo(this, "Reset all video settings to their default values?", "", 9999);
/*  83 */         this.mc.displayGuiScreen(guiyesno);
/*     */       }
/*     */       
/*  86 */       if (button.id != GameSettings.Options.CLOUD_HEIGHT.ordinal())
/*     */       {
/*  88 */         ScaledResolution scaledresolution = new ScaledResolution(this.mc, Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight);
/*  89 */         int i = ScaledResolution.getScaledWidth();
/*  90 */         int j = ScaledResolution.getScaledHeight();
/*  91 */         setWorldAndResolution(this.mc, i, j);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void confirmClicked(boolean result, int id)
/*     */   {
/*  98 */     if (result)
/*     */     {
/* 100 */       this.mc.gameSettings.resetSettings();
/*     */     }
/*     */     
/* 103 */     this.mc.displayGuiScreen(this);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void drawScreen(int mouseX, int mouseY, float partialTicks)
/*     */   {
/* 111 */     drawDefaultBackground();
/* 112 */     drawCenteredString(this.fontRendererObj, this.title, width / 2, 20, 16777215);
/* 113 */     super.drawScreen(mouseX, mouseY, partialTicks);
/*     */     
/* 115 */     if ((Math.abs(mouseX - this.lastMouseX) <= 5) && (Math.abs(mouseY - this.lastMouseY) <= 5))
/*     */     {
/* 117 */       int i = 700;
/*     */       
/* 119 */       if (System.currentTimeMillis() >= this.mouseStillTime + i)
/*     */       {
/* 121 */         int j = width / 2 - 150;
/* 122 */         int k = height / 6 - 5;
/*     */         
/* 124 */         if (mouseY <= k + 98)
/*     */         {
/* 126 */           k += 105;
/*     */         }
/*     */         
/* 129 */         int l = j + 150 + 150;
/* 130 */         int i1 = k + 84 + 10;
/* 131 */         GuiButton guibutton = getSelectedButton(mouseX, mouseY);
/*     */         
/* 133 */         if (guibutton != null)
/*     */         {
/* 135 */           String s = getButtonName(guibutton.displayString);
/* 136 */           String[] astring = getTooltipLines(s);
/*     */           
/* 138 */           if (astring == null)
/*     */           {
/* 140 */             return;
/*     */           }
/*     */           
/* 143 */           drawGradientRect(j, k, l, i1, -536870912, -536870912);
/*     */           
/* 145 */           for (int j1 = 0; j1 < astring.length; j1++)
/*     */           {
/* 147 */             String s1 = astring[j1];
/* 148 */             this.fontRendererObj.drawStringWithShadow(s1, j + 5, k + 5 + j1 * 11, 14540253);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/* 155 */       this.lastMouseX = mouseX;
/* 156 */       this.lastMouseY = mouseY;
/* 157 */       this.mouseStillTime = System.currentTimeMillis();
/*     */     }
/*     */   }
/*     */   
/*     */   private String[] getTooltipLines(String p_getTooltipLines_1_)
/*     */   {
/* 163 */     return p_getTooltipLines_1_.equals("Show FPS") ? new String[] { "Shows compact FPS and render information", "  C: - chunk renderers", "  E: - rendered entities + block entities", "  U: - chunk updates", "The compact FPS information is only shown when the", "debug screen is not visible." } : p_getTooltipLines_1_.equals("3D Anaglyph") ? new String[] { "3D mode used with red-cyan 3D glasses." } : p_getTooltipLines_1_.equals("Fullscreen Mode") ? new String[] { "Fullscreen mode", "  Default - use desktop screen resolution, slower", "  WxH - use custom screen resolution, may be faster", "The selected resolution is used in fullscreen mode (F11).", "Lower resolutions should generally be faster." } : p_getTooltipLines_1_.equals("Fullscreen") ? new String[] { "Fullscreen", "  ON - use fullscreen mode", "  OFF - use window mode", "Fullscreen mode may be faster or slower than", "window mode, depending on the graphics card." } : p_getTooltipLines_1_.equals("Weather") ? new String[] { "Weather", "  ON - weather is active, slower", "  OFF - weather is not active, faster", "The weather controls rain, snow and thunderstorms.", "Weather control is only possible for local worlds." } : p_getTooltipLines_1_.equals("Time") ? new String[] { "Time", " Default - normal day/night cycles", " Day Only - day only", " Night Only - night only", "The time setting is only effective in CREATIVE mode", "and for local worlds." } : p_getTooltipLines_1_.equals("Debug Profiler") ? new String[] { "Debug Profiler", "  ON - debug profiler is active, slower", "  OFF - debug profiler is not active, faster", "The debug profiler collects and shows debug information", "when the debug screen is open (F3)" } : p_getTooltipLines_1_.equals("Lagometer") ? new String[] { "Shows the lagometer on the debug screen (F3).", "* Orange - Memory garbage collection", "* Cyan - Tick", "* Blue - Scheduled executables", "* Purple - Chunk upload", "* Red - Chunk updates", "* Yellow - Visibility check", "* Green - Render terrain" } : p_getTooltipLines_1_.equals("Autosave") ? new String[] { "Autosave interval", "Default autosave interval (2s) is NOT RECOMMENDED.", "Autosave causes the famous Lag Spike of Death." } : null;
/*     */   }
/*     */   
/*     */   private String getButtonName(String p_getButtonName_1_)
/*     */   {
/* 168 */     int i = p_getButtonName_1_.indexOf(':');
/* 169 */     return i < 0 ? p_getButtonName_1_ : p_getButtonName_1_.substring(0, i);
/*     */   }
/*     */   
/*     */   private GuiButton getSelectedButton(int p_getSelectedButton_1_, int p_getSelectedButton_2_)
/*     */   {
/* 174 */     for (int i = 0; i < this.buttonList.size(); i++)
/*     */     {
/* 176 */       GuiButton guibutton = (GuiButton)this.buttonList.get(i);
/* 177 */       int j = GuiVideoSettings.getButtonWidth(guibutton);
/* 178 */       int k = GuiVideoSettings.getButtonHeight(guibutton);
/* 179 */       boolean flag = (p_getSelectedButton_1_ >= guibutton.xPosition) && (p_getSelectedButton_2_ >= guibutton.yPosition) && (p_getSelectedButton_1_ < guibutton.xPosition + j) && (p_getSelectedButton_2_ < guibutton.yPosition + k);
/*     */       
/* 181 */       if (flag)
/*     */       {
/* 183 */         return guibutton;
/*     */       }
/*     */     }
/*     */     
/* 187 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\optfine\GuiOtherSettingsOF.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */