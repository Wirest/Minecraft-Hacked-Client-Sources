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
/*     */ public class GuiDetailSettingsOF extends GuiScreen
/*     */ {
/*     */   private GuiScreen prevScreen;
/*  16 */   protected String title = "Detail Settings";
/*     */   private GameSettings settings;
/*  18 */   private static GameSettings.Options[] enumOptions = { GameSettings.Options.CLOUDS, GameSettings.Options.CLOUD_HEIGHT, GameSettings.Options.TREES, GameSettings.Options.RAIN, GameSettings.Options.SKY, GameSettings.Options.STARS, GameSettings.Options.SUN_MOON, GameSettings.Options.SHOW_CAPES, GameSettings.Options.TRANSLUCENT_BLOCKS, GameSettings.Options.HELD_ITEM_TOOLTIPS, GameSettings.Options.DROPPED_ITEMS, GameSettings.Options.ENTITY_SHADOWS, GameSettings.Options.VIGNETTE };
/*  19 */   private int lastMouseX = 0;
/*  20 */   private int lastMouseY = 0;
/*  21 */   private long mouseStillTime = 0L;
/*     */   
/*     */   public GuiDetailSettingsOF(GuiScreen p_i35_1_, GameSettings p_i35_2_)
/*     */   {
/*  25 */     this.prevScreen = p_i35_1_;
/*  26 */     this.settings = p_i35_2_;
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
/* 128 */             this.fontRendererObj.drawStringWithShadow(s1, j + 5, k + 5 + j1 * 11, 14540253);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/* 135 */       this.lastMouseX = mouseX;
/* 136 */       this.lastMouseY = mouseY;
/* 137 */       this.mouseStillTime = System.currentTimeMillis();
/*     */     }
/*     */   }
/*     */   
/*     */   private String[] getTooltipLines(String p_getTooltipLines_1_)
/*     */   {
/* 143 */     return p_getTooltipLines_1_.equals("Vignette") ? new String[] { "Visual effect which slightly darkens the screen corners", "  Default - as set by the setting Graphics (default)", "  Fast - vignette disabled (faster)", "  Fancy - vignette enabled (slower)", "The vignette may have a significant effect on the FPS,", "especially when playing fullscreen.", "The vignette effect is very subtle and can safely", "be disabled" } : p_getTooltipLines_1_.equals("Translucent Blocks") ? new String[] { "Translucent Blocks", "  Fancy - correct color blending (default)", "  Fast - fast color blending (faster)", "Controls the color blending of translucent blocks", "with different color (stained glass, water, ice)", "when placed behind each other with air between them." } : p_getTooltipLines_1_.equals("Held Item Tooltips") ? new String[] { "Held item tooltips", "  ON - show tooltips for held items (default)", "  OFF - do not show tooltips for held items" } : p_getTooltipLines_1_.equals("Show Capes") ? new String[] { "Show Capes", "  ON - show player capes (default)", "  OFF - do not show player capes" } : p_getTooltipLines_1_.equals("Depth Fog") ? new String[] { "Depth Fog", "  ON - fog moves closer at bedrock levels (default)", "  OFF - same fog at all levels" } : p_getTooltipLines_1_.equals("Stars") ? new String[] { "Stars", "  ON - stars are visible, slower", "  OFF  - stars are not visible, faster" } : p_getTooltipLines_1_.equals("Sun & Moon") ? new String[] { "Sun & Moon", "  ON - sun and moon are visible (default)", "  OFF  - sun and moon are not visible (faster)" } : p_getTooltipLines_1_.equals("Sky") ? new String[] { "Sky", "  ON - sky is visible, slower", "  OFF  - sky is not visible, faster", "When sky is OFF the moon and sun are still visible." } : p_getTooltipLines_1_.equals("Rain & Snow") ? new String[] { "Rain & Snow", "  Default - as set by setting Graphics", "  Fast  - light rain/snow, faster", "  Fancy - heavy rain/snow, slower", "  OFF - no rain/snow, fastest", "When rain is OFF the splashes and rain sounds", "are still active." } : p_getTooltipLines_1_.equals("Water") ? new String[] { "Water", "  Default - as set by setting Graphics", "  Fast  - lower quality, faster", "  Fancy - higher quality, slower", "Fast water (1 pass) has some visual artifacts", "Fancy water (2 pass) has no visual artifacts" } : p_getTooltipLines_1_.equals("Dropped Items") ? new String[] { "Dropped Items", "  Default - as set by setting Graphics", "  Fast - 2D dropped items, faster", "  Fancy - 3D dropped items, slower" } : p_getTooltipLines_1_.equals("Grass") ? new String[] { "Grass", "  Default - as set by setting Graphics", "  Fast - lower quality, faster", "  Fancy - higher quality, slower", "Fast grass uses default side texture.", "Fancy grass uses biome side texture." } : p_getTooltipLines_1_.equals("Trees") ? new String[] { "Trees", "  Default - as set by setting Graphics", "  Fast - lower quality, faster", "  Fancy - higher quality, slower", "Fast trees have opaque leaves.", "Fancy trees have transparent leaves." } : p_getTooltipLines_1_.equals("Cloud Height") ? new String[] { "Cloud Height", "  OFF - default height", "  100% - above world height limit" } : p_getTooltipLines_1_.equals("Clouds") ? new String[] { "Clouds", "  Default - as set by setting Graphics", "  Fast - lower quality, faster", "  Fancy - higher quality, slower", "  OFF - no clouds, fastest", "Fast clouds are rendered 2D.", "Fancy clouds are rendered 3D." } : null;
/*     */   }
/*     */   
/*     */   private String getButtonName(String p_getButtonName_1_)
/*     */   {
/* 148 */     int i = p_getButtonName_1_.indexOf(':');
/* 149 */     return i < 0 ? p_getButtonName_1_ : p_getButtonName_1_.substring(0, i);
/*     */   }
/*     */   
/*     */   private GuiButton getSelectedButton(int p_getSelectedButton_1_, int p_getSelectedButton_2_)
/*     */   {
/* 154 */     for (int i = 0; i < this.buttonList.size(); i++)
/*     */     {
/* 156 */       GuiButton guibutton = (GuiButton)this.buttonList.get(i);
/* 157 */       int j = GuiVideoSettings.getButtonWidth(guibutton);
/* 158 */       int k = GuiVideoSettings.getButtonHeight(guibutton);
/* 159 */       boolean flag = (p_getSelectedButton_1_ >= guibutton.xPosition) && (p_getSelectedButton_2_ >= guibutton.yPosition) && (p_getSelectedButton_1_ < guibutton.xPosition + j) && (p_getSelectedButton_2_ < guibutton.yPosition + k);
/*     */       
/* 161 */       if (flag)
/*     */       {
/* 163 */         return guibutton;
/*     */       }
/*     */     }
/*     */     
/* 167 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\optfine\GuiDetailSettingsOF.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */