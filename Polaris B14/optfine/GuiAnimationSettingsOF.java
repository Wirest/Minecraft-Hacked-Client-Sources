/*     */ package optfine;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.GuiButton;
/*     */ import net.minecraft.client.gui.GuiOptionButton;
/*     */ import net.minecraft.client.gui.GuiScreen;
/*     */ import net.minecraft.client.gui.ScaledResolution;
/*     */ import net.minecraft.client.settings.GameSettings;
/*     */ import net.minecraft.client.settings.GameSettings.Options;
/*     */ 
/*     */ public class GuiAnimationSettingsOF extends GuiScreen
/*     */ {
/*     */   private GuiScreen prevScreen;
/*  15 */   protected String title = "Animation Settings";
/*     */   private GameSettings settings;
/*  17 */   private static GameSettings.Options[] enumOptions = { GameSettings.Options.ANIMATED_WATER, GameSettings.Options.ANIMATED_LAVA, GameSettings.Options.ANIMATED_FIRE, GameSettings.Options.ANIMATED_PORTAL, GameSettings.Options.ANIMATED_REDSTONE, GameSettings.Options.ANIMATED_EXPLOSION, GameSettings.Options.ANIMATED_FLAME, GameSettings.Options.ANIMATED_SMOKE, GameSettings.Options.VOID_PARTICLES, GameSettings.Options.WATER_PARTICLES, GameSettings.Options.RAIN_SPLASH, GameSettings.Options.PORTAL_PARTICLES, GameSettings.Options.POTION_PARTICLES, GameSettings.Options.DRIPPING_WATER_LAVA, GameSettings.Options.ANIMATED_TERRAIN, GameSettings.Options.ANIMATED_TEXTURES, GameSettings.Options.FIREWORK_PARTICLES, GameSettings.Options.PARTICLES };
/*     */   
/*     */   public GuiAnimationSettingsOF(GuiScreen p_i34_1_, GameSettings p_i34_2_)
/*     */   {
/*  21 */     this.prevScreen = p_i34_1_;
/*  22 */     this.settings = p_i34_2_;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void initGui()
/*     */   {
/*  31 */     int i = 0;
/*     */     GameSettings.Options[] arrayOfOptions;
/*  33 */     int j = (arrayOfOptions = enumOptions).length; for (int i = 0; i < j; i++) { GameSettings.Options gamesettings$options = arrayOfOptions[i];
/*     */       
/*  35 */       int j = width / 2 - 155 + i % 2 * 160;
/*  36 */       int k = height / 6 + 21 * (i / 2) - 10;
/*     */       
/*  38 */       if (!gamesettings$options.getEnumFloat())
/*     */       {
/*  40 */         this.buttonList.add(new GuiOptionButton(gamesettings$options.returnEnumOrdinal(), j, k, gamesettings$options, this.settings.getKeyBinding(gamesettings$options)));
/*     */       }
/*     */       else
/*     */       {
/*  44 */         this.buttonList.add(new net.minecraft.client.gui.GuiOptionSlider(gamesettings$options.returnEnumOrdinal(), j, k, gamesettings$options));
/*     */       }
/*     */       
/*  47 */       i++;
/*     */     }
/*     */     
/*  50 */     this.buttonList.add(new GuiButton(210, width / 2 - 155, height / 6 + 168 + 11, 70, 20, "All ON"));
/*  51 */     this.buttonList.add(new GuiButton(211, width / 2 - 155 + 80, height / 6 + 168 + 11, 70, 20, "All OFF"));
/*  52 */     this.buttonList.add(new GuiOptionButton(200, width / 2 + 5, height / 6 + 168 + 11, net.minecraft.client.resources.I18n.format("gui.done", new Object[0])));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void actionPerformed(GuiButton button)
/*     */   {
/*  60 */     if (button.enabled)
/*     */     {
/*  62 */       if ((button.id < 200) && ((button instanceof GuiOptionButton)))
/*     */       {
/*  64 */         this.settings.setOptionValue(((GuiOptionButton)button).returnEnumOptions(), 1);
/*  65 */         button.displayString = this.settings.getKeyBinding(GameSettings.Options.getEnumOptions(button.id));
/*     */       }
/*     */       
/*  68 */       if (button.id == 200)
/*     */       {
/*  70 */         this.mc.gameSettings.saveOptions();
/*  71 */         this.mc.displayGuiScreen(this.prevScreen);
/*     */       }
/*     */       
/*  74 */       if (button.id == 210)
/*     */       {
/*  76 */         this.mc.gameSettings.setAllAnimations(true);
/*     */       }
/*     */       
/*  79 */       if (button.id == 211)
/*     */       {
/*  81 */         this.mc.gameSettings.setAllAnimations(false);
/*     */       }
/*     */       
/*  84 */       if (button.id != GameSettings.Options.CLOUD_HEIGHT.ordinal())
/*     */       {
/*  86 */         ScaledResolution scaledresolution = new ScaledResolution(this.mc, Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight);
/*  87 */         int i = ScaledResolution.getScaledWidth();
/*  88 */         int j = ScaledResolution.getScaledHeight();
/*  89 */         setWorldAndResolution(this.mc, i, j);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void drawScreen(int mouseX, int mouseY, float partialTicks)
/*     */   {
/*  99 */     drawDefaultBackground();
/* 100 */     drawCenteredString(this.fontRendererObj, this.title, width / 2, 20, 16777215);
/* 101 */     super.drawScreen(mouseX, mouseY, partialTicks);
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\optfine\GuiAnimationSettingsOF.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */