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
/*     */ public class GuiPerformanceSettingsOF extends GuiScreen
/*     */ {
/*     */   private GuiScreen prevScreen;
/*  16 */   protected String title = "Performance Settings";
/*     */   private GameSettings settings;
/*  18 */   private static GameSettings.Options[] enumOptions = { GameSettings.Options.SMOOTH_FPS, GameSettings.Options.SMOOTH_WORLD, GameSettings.Options.FAST_RENDER, GameSettings.Options.FAST_MATH, GameSettings.Options.CHUNK_UPDATES, GameSettings.Options.CHUNK_UPDATES_DYNAMIC, GameSettings.Options.LAZY_CHUNK_LOADING };
/*  19 */   private int lastMouseX = 0;
/*  20 */   private int lastMouseY = 0;
/*  21 */   private long mouseStillTime = 0L;
/*     */   
/*     */   public GuiPerformanceSettingsOF(GuiScreen p_i37_1_, GameSettings p_i37_2_)
/*     */   {
/*  25 */     this.prevScreen = p_i37_1_;
/*  26 */     this.settings = p_i37_2_;
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
/* 143 */     return p_getTooltipLines_1_.equals("Fast Render") ? new String[] { "Fast Render", " OFF - standard rendering (default)", " ON - optimized rendering (faster)", "Uses optimized rendering algorithm which decreases", "the GPU load and may substantionally increase the FPS." } : p_getTooltipLines_1_.equals("Fast Math") ? new String[] { "Fast Math", " OFF - standard math (default)", " ON - faster math", "Uses optimized sin() and cos() functions which can", "better utilize the CPU cache and increase the FPS." } : p_getTooltipLines_1_.equals("Lazy Chunk Loading") ? new String[] { "Lazy Chunk Loading", " OFF - default server chunk loading", " ON - lazy server chunk loading (smoother)", "Smooths the integrated server chunk loading by", "distributing the chunks over several ticks.", "Turn it OFF if parts of the world do not load correctly.", "Effective only for local worlds and single-core CPU." } : p_getTooltipLines_1_.equals("Dynamic Updates") ? new String[] { "Dynamic chunk updates", " OFF - (default) standard chunk updates per frame", " ON - more updates while the player is standing still", "Dynamic updates force more chunk updates while", "the player is standing still to load the world faster." } : p_getTooltipLines_1_.equals("Chunk Updates") ? new String[] { "Chunk updates", " 1 - slower world loading, higher FPS (default)", " 3 - faster world loading, lower FPS", " 5 - fastest world loading, lowest FPS", "Number of chunk updates per rendered frame,", "higher values may destabilize the framerate." } : p_getTooltipLines_1_.equals("Preloaded Chunks") ? new String[] { "Defines an area in which no chunks will be loaded", "  OFF - after 5m new chunks will be loaded", "  2 - after 32m  new chunks will be loaded", "  8 - after 128m new chunks will be loaded", "Higher values need more time to load all the chunks", "and may decrease the FPS." } : p_getTooltipLines_1_.equals("Load Far") ? new String[] { "Loads the world chunks at distance Far.", "Switching the render distance does not cause all chunks ", "to be loaded again.", "  OFF - world chunks loaded up to render distance", "  ON - world chunks loaded at distance Far, allows", "       fast render distance switching" } : p_getTooltipLines_1_.equals("Smooth World") ? new String[] { "Removes lag spikes caused by the internal server.", "  OFF - no stabilization, FPS may fluctuate", "  ON - FPS stabilization", "Stabilizes FPS by distributing the internal server load.", "Effective only for local worlds (single player)." } : p_getTooltipLines_1_.equals("Smooth FPS") ? new String[] { "Stabilizes FPS by flushing the graphic driver buffers", "  OFF - no stabilization, FPS may fluctuate", "  ON - FPS stabilization", "This option is graphics driver dependant and its effect", "is not always visible" } : null;
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


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\optfine\GuiPerformanceSettingsOF.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */