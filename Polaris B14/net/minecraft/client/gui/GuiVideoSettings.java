/*     */ package net.minecraft.client.gui;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.resources.I18n;
/*     */ import net.minecraft.client.settings.GameSettings;
/*     */ import net.minecraft.client.settings.GameSettings.Options;
/*     */ import optfine.GuiAnimationSettingsOF;
/*     */ import optfine.GuiDetailSettingsOF;
/*     */ import optfine.GuiPerformanceSettingsOF;
/*     */ 
/*     */ public class GuiVideoSettings extends GuiScreen
/*     */ {
/*     */   private GuiScreen parentGuiScreen;
/*  15 */   protected String screenTitle = "Video Settings";
/*     */   
/*     */   private GameSettings guiGameSettings;
/*     */   
/*     */   private boolean is64bit;
/*  20 */   private static GameSettings.Options[] videoOptions = { GameSettings.Options.GRAPHICS, GameSettings.Options.RENDER_DISTANCE, GameSettings.Options.AMBIENT_OCCLUSION, GameSettings.Options.FRAMERATE_LIMIT, GameSettings.Options.AO_LEVEL, GameSettings.Options.VIEW_BOBBING, GameSettings.Options.GUI_SCALE, GameSettings.Options.USE_VBO, GameSettings.Options.GAMMA, GameSettings.Options.BLOCK_ALTERNATIVES, GameSettings.Options.FOG_FANCY, GameSettings.Options.FOG_START, GameSettings.Options.ANAGLYPH };
/*     */   private static final String __OBFID = "CL_00000718";
/*  22 */   private int lastMouseX = 0;
/*  23 */   private int lastMouseY = 0;
/*  24 */   private long mouseStillTime = 0L;
/*     */   
/*     */   public GuiVideoSettings(GuiScreen parentScreenIn, GameSettings gameSettingsIn)
/*     */   {
/*  28 */     this.parentGuiScreen = parentScreenIn;
/*  29 */     this.guiGameSettings = gameSettingsIn;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void initGui()
/*     */   {
/*  38 */     this.screenTitle = I18n.format("options.videoTitle", new Object[0]);
/*  39 */     this.buttonList.clear();
/*  40 */     this.is64bit = false;
/*  41 */     String[] astring = { "sun.arch.data.model", "com.ibm.vm.bitmode", "os.arch" };
/*     */     String[] arrayOfString1;
/*  43 */     int j = (arrayOfString1 = astring).length; for (int i = 0; i < j; i++) { String s = arrayOfString1[i];
/*     */       
/*  45 */       String s1 = System.getProperty(s);
/*     */       
/*  47 */       if ((s1 != null) && (s1.contains("64")))
/*     */       {
/*  49 */         this.is64bit = true;
/*  50 */         break;
/*     */       }
/*     */     }
/*     */     
/*  54 */     int l = 0;
/*  55 */     boolean flag = !this.is64bit;
/*  56 */     GameSettings.Options[] agamesettings$options = videoOptions;
/*  57 */     int i1 = agamesettings$options.length;
/*  58 */     int i = 0;
/*     */     
/*  60 */     for (i = 0; i < i1; i++)
/*     */     {
/*  62 */       GameSettings.Options gamesettings$options = agamesettings$options[i];
/*     */       
/*  64 */       if (gamesettings$options != null)
/*     */       {
/*  66 */         int j = width / 2 - 155 + i % 2 * 160;
/*  67 */         int k = height / 6 + 21 * (i / 2) - 10;
/*     */         
/*  69 */         if (gamesettings$options.getEnumFloat())
/*     */         {
/*  71 */           this.buttonList.add(new GuiOptionSlider(gamesettings$options.returnEnumOrdinal(), j, k, gamesettings$options));
/*     */         }
/*     */         else
/*     */         {
/*  75 */           this.buttonList.add(new GuiOptionButton(gamesettings$options.returnEnumOrdinal(), j, k, gamesettings$options, this.guiGameSettings.getKeyBinding(gamesettings$options)));
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*  80 */     int j1 = height / 6 + 21 * (i / 2) - 10;
/*  81 */     int k1 = 0;
/*  82 */     k1 = width / 2 - 155 + 160;
/*  83 */     this.buttonList.add(new GuiOptionButton(202, k1, j1, "Quality..."));
/*  84 */     j1 += 21;
/*  85 */     k1 = width / 2 - 155 + 0;
/*  86 */     this.buttonList.add(new GuiOptionButton(201, k1, j1, "Details..."));
/*  87 */     k1 = width / 2 - 155 + 160;
/*  88 */     this.buttonList.add(new GuiOptionButton(212, k1, j1, "Performance..."));
/*  89 */     j1 += 21;
/*  90 */     k1 = width / 2 - 155 + 0;
/*  91 */     this.buttonList.add(new GuiOptionButton(211, k1, j1, "Animations..."));
/*  92 */     k1 = width / 2 - 155 + 160;
/*  93 */     this.buttonList.add(new GuiOptionButton(222, k1, j1, "Other..."));
/*  94 */     this.buttonList.add(new GuiButton(200, width / 2 - 100, height / 6 + 168 + 11, I18n.format("gui.done", new Object[0])));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected void actionPerformed(GuiButton button)
/*     */     throws java.io.IOException
/*     */   {
/* 102 */     if (button.enabled)
/*     */     {
/* 104 */       int i = this.guiGameSettings.guiScale;
/*     */       
/* 106 */       if ((button.id < 200) && ((button instanceof GuiOptionButton)))
/*     */       {
/* 108 */         this.guiGameSettings.setOptionValue(((GuiOptionButton)button).returnEnumOptions(), 1);
/* 109 */         button.displayString = this.guiGameSettings.getKeyBinding(GameSettings.Options.getEnumOptions(button.id));
/*     */       }
/*     */       
/* 112 */       if (button.id == 200)
/*     */       {
/* 114 */         this.mc.gameSettings.saveOptions();
/* 115 */         this.mc.displayGuiScreen(this.parentGuiScreen);
/*     */       }
/*     */       
/* 118 */       if (this.guiGameSettings.guiScale != i)
/*     */       {
/* 120 */         ScaledResolution scaledresolution = new ScaledResolution(this.mc, this.mc.displayWidth, this.mc.displayHeight);
/* 121 */         int j = ScaledResolution.getScaledWidth();
/* 122 */         int k = ScaledResolution.getScaledHeight();
/* 123 */         setWorldAndResolution(this.mc, j, k);
/*     */       }
/*     */       
/* 126 */       if (button.id == 201)
/*     */       {
/* 128 */         this.mc.gameSettings.saveOptions();
/* 129 */         GuiDetailSettingsOF guidetailsettingsof = new GuiDetailSettingsOF(this, this.guiGameSettings);
/* 130 */         this.mc.displayGuiScreen(guidetailsettingsof);
/*     */       }
/*     */       
/* 133 */       if (button.id == 202)
/*     */       {
/* 135 */         this.mc.gameSettings.saveOptions();
/* 136 */         optfine.GuiQualitySettingsOF guiqualitysettingsof = new optfine.GuiQualitySettingsOF(this, this.guiGameSettings);
/* 137 */         this.mc.displayGuiScreen(guiqualitysettingsof);
/*     */       }
/*     */       
/* 140 */       if (button.id == 211)
/*     */       {
/* 142 */         this.mc.gameSettings.saveOptions();
/* 143 */         GuiAnimationSettingsOF guianimationsettingsof = new GuiAnimationSettingsOF(this, this.guiGameSettings);
/* 144 */         this.mc.displayGuiScreen(guianimationsettingsof);
/*     */       }
/*     */       
/* 147 */       if (button.id == 212)
/*     */       {
/* 149 */         this.mc.gameSettings.saveOptions();
/* 150 */         GuiPerformanceSettingsOF guiperformancesettingsof = new GuiPerformanceSettingsOF(this, this.guiGameSettings);
/* 151 */         this.mc.displayGuiScreen(guiperformancesettingsof);
/*     */       }
/*     */       
/* 154 */       if (button.id == 222)
/*     */       {
/* 156 */         this.mc.gameSettings.saveOptions();
/* 157 */         optfine.GuiOtherSettingsOF guiothersettingsof = new optfine.GuiOtherSettingsOF(this, this.guiGameSettings);
/* 158 */         this.mc.displayGuiScreen(guiothersettingsof);
/*     */       }
/*     */       
/* 161 */       if (button.id == GameSettings.Options.AO_LEVEL.ordinal()) {}
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void drawScreen(int mouseX, int mouseY, float partialTicks)
/*     */   {
/* 173 */     drawDefaultBackground();
/* 174 */     drawCenteredString(this.fontRendererObj, this.screenTitle, width / 2, this.is64bit ? 20 : 5, 16777215);
/*     */     
/* 176 */     if ((!this.is64bit) && (this.guiGameSettings.renderDistanceChunks > 8)) {}
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 181 */     super.drawScreen(mouseX, mouseY, partialTicks);
/*     */     
/* 183 */     if ((Math.abs(mouseX - this.lastMouseX) <= 5) && (Math.abs(mouseY - this.lastMouseY) <= 5))
/*     */     {
/* 185 */       int i = 700;
/*     */       
/* 187 */       if (System.currentTimeMillis() >= this.mouseStillTime + i)
/*     */       {
/* 189 */         int j = width / 2 - 150;
/* 190 */         int k = height / 6 - 5;
/*     */         
/* 192 */         if (mouseY <= k + 98)
/*     */         {
/* 194 */           k += 105;
/*     */         }
/*     */         
/* 197 */         int l = j + 150 + 150;
/* 198 */         int i1 = k + 84 + 10;
/* 199 */         GuiButton guibutton = getSelectedButton(mouseX, mouseY);
/*     */         
/* 201 */         if (guibutton != null)
/*     */         {
/* 203 */           String s = getButtonName(guibutton.displayString);
/* 204 */           String[] astring = getTooltipLines(s);
/*     */           
/* 206 */           if (astring == null)
/*     */           {
/* 208 */             return;
/*     */           }
/*     */           
/* 211 */           drawGradientRect(j, k, l, i1, -536870912, -536870912);
/*     */           
/* 213 */           for (int j1 = 0; j1 < astring.length; j1++)
/*     */           {
/* 215 */             String s1 = astring[j1];
/* 216 */             this.fontRendererObj.drawStringWithShadow(s1, j + 5, k + 5 + j1 * 11, 14540253);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/* 223 */       this.lastMouseX = mouseX;
/* 224 */       this.lastMouseY = mouseY;
/* 225 */       this.mouseStillTime = System.currentTimeMillis();
/*     */     }
/*     */   }
/*     */   
/*     */   private String[] getTooltipLines(String p_getTooltipLines_1_)
/*     */   {
/* 231 */     return p_getTooltipLines_1_.equals("3D Anaglyph") ? new String[] { "3D Anaglyph", "Enables a stereoscopic 3D effect using different colors", "for each eye.", "Requires red-cyan glasses for proper viewing." } : p_getTooltipLines_1_.equals("Use VBOs") ? new String[] { "Vertex Buffer Objects", "Uses an alternative rendering model which is usually", "faster (5-10%) than the default rendering." } : p_getTooltipLines_1_.equals("Alternate Blocks") ? new String[] { "Alternate Blocks", "Uses alternative block models for some blocks.", "Depends on the selected resource pack." } : p_getTooltipLines_1_.equals("Chunk Loading") ? new String[] { "Chunk Loading", "  Default - unstable FPS when loading chunks", "  Smooth - stable FPS", "  Multi-Core - stable FPS, 3x faster world loading", "Smooth and Multi-Core remove the stuttering and ", "freezes caused by chunk loading.", "Multi-Core can speed up 3x the world loading and", "increase FPS by using a second CPU core." } : p_getTooltipLines_1_.equals("Brightness") ? new String[] { "Increases the brightness of darker objects", "  OFF - standard brightness", "  100% - maximum brightness for darker objects", "This options does not change the brightness of ", "fully black objects" } : p_getTooltipLines_1_.equals("Fog Start") ? new String[] { "Fog start", "  0.2 - the fog starts near the player", "  0.8 - the fog starts far from the player", "This option usually does not affect the performance." } : p_getTooltipLines_1_.equals("Fog") ? new String[] { "Fog type", "  Fast - faster fog", "  Fancy - slower fog, looks better", "  OFF - no fog, fastest", "The fancy fog is available only if it is supported by the ", "graphic card." } : p_getTooltipLines_1_.equals("Advanced OpenGL") ? new String[] { "Detect and render only visible geometry", "  OFF - all geometry is rendered (slower)", "  Fast - only visible geometry is rendered (fastest)", "  Fancy - conservative, avoids visual artifacts (faster)", "The option is available only if it is supported by the ", "graphic card." } : p_getTooltipLines_1_.equals("Server Textures") ? new String[] { "Server textures", "Use the resource pack recommended by the server" } : p_getTooltipLines_1_.equals("GUI Scale") ? new String[] { "GUI Scale", "Smaller GUI might be faster" } : p_getTooltipLines_1_.equals("View Bobbing") ? new String[] { "More realistic movement.", "When using mipmaps set it to OFF for best results." } : p_getTooltipLines_1_.equals("Max Framerate") ? new String[] { "Max framerate", "  VSync - limit to monitor framerate (60, 30, 20)", "  5-255 - variable", "  Unlimited - no limit (fastest)", "The framerate limit decreases the FPS even if", "the limit value is not reached." } : p_getTooltipLines_1_.equals("Smooth Lighting Level") ? new String[] { "Smooth lighting level", "  OFF - no shadows", "  50% - light shadows", "  100% - dark shadows" } : p_getTooltipLines_1_.equals("Smooth Lighting") ? new String[] { "Smooth lighting", "  OFF - no smooth lighting (faster)", "  Minimum - simple smooth lighting (slower)", "  Maximum - complex smooth lighting (slowest)" } : p_getTooltipLines_1_.equals("Render Distance") ? new String[] { "Visible distance", "  2 Tiny - 32m (fastest)", "  4 Short - 64m (faster)", "  8 Normal - 128m", "  16 Far - 256m (slower)", "  32 Extreme - 512m (slowest!)", "The Extreme view distance is very resource demanding!", "Values over 16 Far are only effective in local worlds." } : p_getTooltipLines_1_.equals("Graphics") ? new String[] { "Visual quality", "  Fast  - lower quality, faster", "  Fancy - higher quality, slower", "Changes the appearance of clouds, leaves, water,", "shadows and grass sides." } : null;
/*     */   }
/*     */   
/*     */   private String getButtonName(String p_getButtonName_1_)
/*     */   {
/* 236 */     int i = p_getButtonName_1_.indexOf(':');
/* 237 */     return i < 0 ? p_getButtonName_1_ : p_getButtonName_1_.substring(0, i);
/*     */   }
/*     */   
/*     */   private GuiButton getSelectedButton(int p_getSelectedButton_1_, int p_getSelectedButton_2_)
/*     */   {
/* 242 */     for (int i = 0; i < this.buttonList.size(); i++)
/*     */     {
/* 244 */       GuiButton guibutton = (GuiButton)this.buttonList.get(i);
/* 245 */       boolean flag = (p_getSelectedButton_1_ >= guibutton.xPosition) && (p_getSelectedButton_2_ >= guibutton.yPosition) && (p_getSelectedButton_1_ < guibutton.xPosition + guibutton.width) && (p_getSelectedButton_2_ < guibutton.yPosition + guibutton.height);
/*     */       
/* 247 */       if (flag)
/*     */       {
/* 249 */         return guibutton;
/*     */       }
/*     */     }
/*     */     
/* 253 */     return null;
/*     */   }
/*     */   
/*     */   public static int getButtonWidth(GuiButton p_getButtonWidth_0_)
/*     */   {
/* 258 */     return p_getButtonWidth_0_.width;
/*     */   }
/*     */   
/*     */   public static int getButtonHeight(GuiButton p_getButtonHeight_0_)
/*     */   {
/* 263 */     return p_getButtonHeight_0_.height;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\gui\GuiVideoSettings.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */