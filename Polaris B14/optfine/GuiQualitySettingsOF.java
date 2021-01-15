/*     */ package optfine;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.GuiButton;
/*     */ import net.minecraft.client.gui.GuiOptionButton;
/*     */ import net.minecraft.client.gui.GuiScreen;
/*     */ import net.minecraft.client.gui.GuiVideoSettings;
/*     */ import net.minecraft.client.gui.ScaledResolution;
/*     */ import net.minecraft.client.settings.GameSettings;
/*     */ import net.minecraft.client.settings.GameSettings.Options;
/*     */ 
/*     */ public class GuiQualitySettingsOF extends GuiScreen
/*     */ {
/*     */   private GuiScreen prevScreen;
/*  16 */   protected String title = "Quality Settings";
/*     */   private GameSettings settings;
/*  18 */   private static GameSettings.Options[] enumOptions = { GameSettings.Options.MIPMAP_LEVELS, GameSettings.Options.MIPMAP_TYPE, GameSettings.Options.AF_LEVEL, GameSettings.Options.AA_LEVEL, GameSettings.Options.CLEAR_WATER, GameSettings.Options.RANDOM_MOBS, GameSettings.Options.BETTER_GRASS, GameSettings.Options.BETTER_SNOW, GameSettings.Options.CUSTOM_FONTS, GameSettings.Options.CUSTOM_COLORS, GameSettings.Options.SWAMP_COLORS, GameSettings.Options.SMOOTH_BIOMES, GameSettings.Options.CONNECTED_TEXTURES, GameSettings.Options.NATURAL_TEXTURES, GameSettings.Options.CUSTOM_SKY };
/*  19 */   private int lastMouseX = 0;
/*  20 */   private int lastMouseY = 0;
/*  21 */   private long mouseStillTime = 0L;
/*     */   
/*     */   public GuiQualitySettingsOF(GuiScreen p_i38_1_, GameSettings p_i38_2_)
/*     */   {
/*  25 */     this.prevScreen = p_i38_1_;
/*  26 */     this.settings = p_i38_2_;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void initGui()
/*     */   {
/*  35 */     int i = 0;
/*     */     GameSettings.Options[] arrayOfOptions;
/*  37 */     int j = (arrayOfOptions = enumOptions).length; for (int i = 0; i < j; i++) { GameSettings.Options gamesettings$options = arrayOfOptions[i];
/*     */       
/*  39 */       int j = width / 2 - 155 + i % 2 * 160;
/*  40 */       int k = height / 6 + 21 * (i / 2) - 10;
/*     */       
/*  42 */       if (!gamesettings$options.getEnumFloat())
/*     */       {
/*  44 */         this.buttonList.add(new GuiOptionButton(gamesettings$options.returnEnumOrdinal(), j, k, gamesettings$options, this.settings.getKeyBinding(gamesettings$options)));
/*     */       }
/*     */       else
/*     */       {
/*  48 */         this.buttonList.add(new net.minecraft.client.gui.GuiOptionSlider(gamesettings$options.returnEnumOrdinal(), j, k, gamesettings$options));
/*     */       }
/*     */       
/*  51 */       i++;
/*     */     }
/*     */     
/*  54 */     this.buttonList.add(new GuiButton(200, width / 2 - 100, height / 6 + 168 + 11, net.minecraft.client.resources.I18n.format("gui.done", new Object[0])));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void actionPerformed(GuiButton button)
/*     */   {
/*  62 */     if (button.enabled)
/*     */     {
/*  64 */       if ((button.id < 200) && ((button instanceof GuiOptionButton)))
/*     */       {
/*  66 */         this.settings.setOptionValue(((GuiOptionButton)button).returnEnumOptions(), 1);
/*  67 */         button.displayString = this.settings.getKeyBinding(GameSettings.Options.getEnumOptions(button.id));
/*     */       }
/*     */       
/*  70 */       if (button.id == 200)
/*     */       {
/*  72 */         this.mc.gameSettings.saveOptions();
/*  73 */         this.mc.displayGuiScreen(this.prevScreen);
/*     */       }
/*     */       
/*  76 */       if (button.id != GameSettings.Options.CLOUD_HEIGHT.ordinal())
/*     */       {
/*  78 */         ScaledResolution scaledresolution = new ScaledResolution(this.mc, Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight);
/*  79 */         int i = ScaledResolution.getScaledWidth();
/*  80 */         int j = ScaledResolution.getScaledHeight();
/*  81 */         setWorldAndResolution(this.mc, i, j);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void drawScreen(int mouseX, int mouseY, float partialTicks)
/*     */   {
/*  91 */     drawDefaultBackground();
/*  92 */     drawCenteredString(this.fontRendererObj, this.title, width / 2, 20, 16777215);
/*  93 */     super.drawScreen(mouseX, mouseY, partialTicks);
/*     */     
/*  95 */     if ((Math.abs(mouseX - this.lastMouseX) <= 5) && (Math.abs(mouseY - this.lastMouseY) <= 5))
/*     */     {
/*  97 */       int i = 700;
/*     */       
/*  99 */       if (System.currentTimeMillis() >= this.mouseStillTime + i)
/*     */       {
/* 101 */         int j = width / 2 - 150;
/* 102 */         int k = height / 6 - 5;
/*     */         
/* 104 */         if (mouseY <= k + 98)
/*     */         {
/* 106 */           k += 105;
/*     */         }
/*     */         
/* 109 */         int l = j + 150 + 150;
/* 110 */         int i1 = k + 84 + 10;
/* 111 */         GuiButton guibutton = getSelectedButton(mouseX, mouseY);
/*     */         
/* 113 */         if (guibutton != null)
/*     */         {
/* 115 */           String s = getButtonName(guibutton.displayString);
/* 116 */           String[] astring = getTooltipLines(s);
/*     */           
/* 118 */           if (astring == null)
/*     */           {
/* 120 */             return;
/*     */           }
/*     */           
/* 123 */           drawGradientRect(j, k, l, i1, -536870912, -536870912);
/*     */           
/* 125 */           for (int j1 = 0; j1 < astring.length; j1++)
/*     */           {
/* 127 */             String s1 = astring[j1];
/* 128 */             int k1 = 14540253;
/*     */             
/* 130 */             if (s1.endsWith("!"))
/*     */             {
/* 132 */               k1 = 16719904;
/*     */             }
/*     */             
/* 135 */             this.fontRendererObj.drawStringWithShadow(s1, j + 5, k + 5 + j1 * 11, k1);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/* 142 */       this.lastMouseX = mouseX;
/* 143 */       this.lastMouseY = mouseY;
/* 144 */       this.mouseStillTime = System.currentTimeMillis();
/*     */     }
/*     */   }
/*     */   
/*     */   private String[] getTooltipLines(String p_getTooltipLines_1_)
/*     */   {
/* 150 */     return p_getTooltipLines_1_.equals("Custom Sky") ? new String[] { "Custom Sky", "  ON - custom sky textures (default), slow", "  OFF - default sky, faster", "The custom sky textures are supplied by the current", "texture pack" } : p_getTooltipLines_1_.equals("Natural Textures") ? new String[] { "Natural Textures", "  OFF - no natural textures (default)", "  ON - use natural textures", "Natural textures remove the gridlike pattern", "created by repeating blocks of the same type.", "It uses rotated and flipped variants of the base", "block texture. The configuration for the natural", "textures is supplied by the current texture pack" } : p_getTooltipLines_1_.equals("Far View") ? new String[] { "Far View", " OFF - (default) standard view distance", " ON - 3x view distance", "Far View is very resource demanding!", "3x view distance => 9x chunks to be loaded => FPS / 9", "Standard view distances: 32, 64, 128, 256", "Far view distances: 96, 192, 384, 512" } : p_getTooltipLines_1_.equals("Connected Textures") ? new String[] { "Connected Textures", "  OFF - no connected textures (default)", "  Fast - fast connected textures", "  Fancy - fancy connected textures", "Connected textures joins the textures of glass,", "sandstone and bookshelves when placed next to", "each other. The connected textures are supplied", "by the current texture pack." } : p_getTooltipLines_1_.equals("Show Capes") ? new String[] { "Show Capes", "  ON - show player capes (default)", "  OFF - do not show player capes" } : p_getTooltipLines_1_.equals("Custom Colors") ? new String[] { "Custom Colors", "  ON - uses custom colors (default), slower", "  OFF - uses default colors, faster", "The custom colors are supplied by the current", "texture pack" } : p_getTooltipLines_1_.equals("Custom Fonts") ? new String[] { "Custom Fonts", "  ON - uses custom fonts (default), slower", "  OFF - uses default font, faster", "The custom fonts are supplied by the current", "texture pack" } : p_getTooltipLines_1_.equals("Smooth Biomes") ? new String[] { "Smooth Biomes", "  ON - smoothing of biome borders (default), slower", "  OFF - no smoothing of biome borders, faster", "The smoothing of biome borders is done by sampling and", "averaging the color of all surrounding blocks.", "Affected are grass, leaves, vines and water." } : p_getTooltipLines_1_.equals("Swamp Colors") ? new String[] { "Swamp Colors", "  ON - use swamp colors (default), slower", "  OFF - do not use swamp colors, faster", "The swamp colors affect grass, leaves, vines and water." } : p_getTooltipLines_1_.equals("Random Mobs") ? new String[] { "Random Mobs", "  OFF - no random mobs, faster", "  ON - random mobs, slower", "Random mobs uses random textures for the game creatures.", "It needs a texture pack which has multiple mob textures." } : p_getTooltipLines_1_.equals("Better Snow") ? new String[] { "Better Snow", "  OFF - default snow, faster", "  ON - better snow, slower", "Shows snow under transparent blocks (fence, tall grass)", "when bordering with snow blocks" } : p_getTooltipLines_1_.equals("Better Grass") ? new String[] { "Better Grass", "  OFF - default side grass texture, fastest", "  Fast - full side grass texture, slower", "  Fancy - dynamic side grass texture, slowest" } : p_getTooltipLines_1_.equals("Clear Water") ? new String[] { "Clear Water", "  ON - clear, transparent water", "  OFF - default water" } : p_getTooltipLines_1_.equals("Antialiasing") ? new String[] { "Antialiasing", " OFF - (default) no antialiasing (faster)", " 2-16 - antialiased lines and edges (slower)", "The Antialiasing smooths jagged lines and ", "sharp color transitions.", "When enabled it may substantially decrease the FPS.", "Not all levels are supported by all graphics cards.", "Effective after a RESTART!" } : p_getTooltipLines_1_.equals("Anisotropic Filtering") ? new String[] { "Anisotropic Filtering", " OFF - (default) standard texture detail (faster)", " 2-16 - finer details in mipmapped textures (slower)", "The Anisotropic Filtering restores details in", "mipmapped textures.", "When enabled it may substantially decrease the FPS." } : p_getTooltipLines_1_.equals("Mipmap Type") ? new String[] { "Visual effect which makes distant objects look better", "by smoothing the texture details", "  Nearest - rough smoothing (fastest)", "  Linear - normal smoothing", "  Bilinear - fine smoothing", "  Trilinear - finest smoothing (slowest)" } : p_getTooltipLines_1_.equals("Mipmap Levels") ? new String[] { "Visual effect which makes distant objects look better", "by smoothing the texture details", "  OFF - no smoothing", "  1 - minimum smoothing", "  4 - maximum smoothing", "This option usually does not affect the performance." } : null;
/*     */   }
/*     */   
/*     */   private String getButtonName(String p_getButtonName_1_)
/*     */   {
/* 155 */     int i = p_getButtonName_1_.indexOf(':');
/* 156 */     return i < 0 ? p_getButtonName_1_ : p_getButtonName_1_.substring(0, i);
/*     */   }
/*     */   
/*     */   private GuiButton getSelectedButton(int p_getSelectedButton_1_, int p_getSelectedButton_2_)
/*     */   {
/* 161 */     for (int i = 0; i < this.buttonList.size(); i++)
/*     */     {
/* 163 */       GuiButton guibutton = (GuiButton)this.buttonList.get(i);
/* 164 */       int j = GuiVideoSettings.getButtonWidth(guibutton);
/* 165 */       int k = GuiVideoSettings.getButtonHeight(guibutton);
/* 166 */       boolean flag = (p_getSelectedButton_1_ >= guibutton.xPosition) && (p_getSelectedButton_2_ >= guibutton.yPosition) && (p_getSelectedButton_1_ < guibutton.xPosition + j) && (p_getSelectedButton_2_ < guibutton.yPosition + k);
/*     */       
/* 168 */       if (flag)
/*     */       {
/* 170 */         return guibutton;
/*     */       }
/*     */     }
/*     */     
/* 174 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\optfine\GuiQualitySettingsOF.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */