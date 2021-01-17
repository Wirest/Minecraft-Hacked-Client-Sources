package net.minecraft.client.gui;

import java.io.IOException;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import optifine.GuiAnimationSettingsOF;
import optifine.GuiDetailSettingsOF;
import optifine.GuiOtherSettingsOF;
import optifine.GuiPerformanceSettingsOF;
import optifine.GuiQualitySettingsOF;

   public class GuiVideoSettings extends GuiScreen {
       private GuiScreen parentGuiScreen;
       protected String screenTitle;
       private GameSettings guiGameSettings;
       private boolean is64bit;
       private static GameSettings.Options[] videoOptions;
       private static final String __OBFID = "CL_00000718";
       private int lastMouseX;
       private int lastMouseY;
       private long mouseStillTime;
       
       static {
           GuiVideoSettings.videoOptions = new GameSettings.Options[] { GameSettings.Options.GRAPHICS, GameSettings.Options.RENDER_DISTANCE, GameSettings.Options.AMBIENT_OCCLUSION, GameSettings.Options.FRAMERATE_LIMIT, GameSettings.Options.AO_LEVEL, GameSettings.Options.VIEW_BOBBING, GameSettings.Options.GUI_SCALE, GameSettings.Options.USE_VBO, GameSettings.Options.GAMMA, GameSettings.Options.BLOCK_ALTERNATIVES, GameSettings.Options.FOG_FANCY, GameSettings.Options.FOG_START, GameSettings.Options.ANAGLYPH };
       }
       
       public GuiVideoSettings(final GuiScreen parentScreenIn, final GameSettings gameSettingsIn) {
           this.screenTitle = "Video Settings";
           this.lastMouseX = 0;
           this.lastMouseY = 0;
           this.mouseStillTime = 0L;
           this.parentGuiScreen = parentScreenIn;
           this.guiGameSettings = gameSettingsIn;
       }
       
       @Override
       public void initGui() {
           this.screenTitle = I18n.format("options.videoTitle", new Object[0]);
           this.buttonList.clear();
           this.is64bit = false;
           final String[] astring = { "sun.arch.data.model", "com.ibm.vm.bitmode", "os.arch" };
           String[] array;
           for (int length = (array = astring).length, n = 0; n < length; ++n) {
               final String s = array[n];
               final String s2 = System.getProperty(s);
               if (s2 != null && s2.contains("64")) {
                   this.is64bit = true;
                   break;
               }
           }
           final int l = 0;
           final boolean flag = !this.is64bit;
           final GameSettings.Options[] agamesettings$options = GuiVideoSettings.videoOptions;
           int i1;
           int j;
           GameSettings.Options gamesettings$options;
           int k;
           int m;
           for (i1 = agamesettings$options.length, j = 0, j = 0; j < i1; ++j) {
               gamesettings$options = agamesettings$options[j];
               if (gamesettings$options != null) {
                   k = this.width / 2 - 155 + j % 2 * 160;
                   m = this.height / 6 + 21 * (j / 2) - 10;
                   if (gamesettings$options.getEnumFloat()) {
                       this.buttonList.add(new GuiOptionSlider(gamesettings$options.returnEnumOrdinal(), k, m, gamesettings$options));
                   }
                   else {
                       this.buttonList.add(new GuiOptionButton(gamesettings$options.returnEnumOrdinal(), k, m, gamesettings$options, this.guiGameSettings.getKeyBinding(gamesettings$options)));
                   }
               }
           }
           int j2 = this.height / 6 + 21 * (j / 2) - 10;
           int k2 = 0;
           k2 = this.width / 2 - 155 + 160;
           this.buttonList.add(new GuiOptionButton(202, k2, j2, "Quality..."));
           j2 += 21;
           k2 = this.width / 2 - 155 + 0;
           this.buttonList.add(new GuiOptionButton(201, k2, j2, "Details..."));
           k2 = this.width / 2 - 155 + 160;
           this.buttonList.add(new GuiOptionButton(212, k2, j2, "Performance..."));
           j2 += 21;
           k2 = this.width / 2 - 155 + 0;
           this.buttonList.add(new GuiOptionButton(211, k2, j2, "Animations..."));
           k2 = this.width / 2 - 155 + 160;
           this.buttonList.add(new GuiOptionButton(222, k2, j2, "Other..."));
           this.buttonList.add(new GuiButton(200, this.width / 2 - 100, this.height / 6 + 168 + 11, I18n.format("gui.done", new Object[0])));
       }
       
       @Override
       protected void actionPerformed(final GuiButton button) throws IOException {
           if (button.enabled) {
               final int i = this.guiGameSettings.guiScale;
               if (button.id < 200 && button instanceof GuiOptionButton) {
                   this.guiGameSettings.setOptionValue(((GuiOptionButton)button).returnEnumOptions(), 1);
                   button.displayString = this.guiGameSettings.getKeyBinding(GameSettings.Options.getEnumOptions(button.id));
               }
               if (button.id == 200) {
                   this.mc.gameSettings.saveOptions();
                   this.mc.displayGuiScreen(this.parentGuiScreen);
               }
               if (this.guiGameSettings.guiScale != i) {
                   final ScaledResolution scaledresolution = new ScaledResolution(this.mc);
                   final int j = scaledresolution.getScaledWidth();
                   final int k = scaledresolution.getScaledHeight();
                   this.setWorldAndResolution(this.mc, j, k);
               }
               if (button.id == 201) {
                   this.mc.gameSettings.saveOptions();
                   final GuiDetailSettingsOF guidetailsettingsof = new GuiDetailSettingsOF(this, this.guiGameSettings);
                   this.mc.displayGuiScreen(guidetailsettingsof);
               }
               if (button.id == 202) {
                   this.mc.gameSettings.saveOptions();
                   final GuiQualitySettingsOF guiqualitysettingsof = new GuiQualitySettingsOF(this, this.guiGameSettings);
                   this.mc.displayGuiScreen(guiqualitysettingsof);
               }
               if (button.id == 211) {
                   this.mc.gameSettings.saveOptions();
                   final GuiAnimationSettingsOF guianimationsettingsof = new GuiAnimationSettingsOF(this, this.guiGameSettings);
                   this.mc.displayGuiScreen(guianimationsettingsof);
               }
               if (button.id == 212) {
                   this.mc.gameSettings.saveOptions();
                   final GuiPerformanceSettingsOF guiperformancesettingsof = new GuiPerformanceSettingsOF(this, this.guiGameSettings);
                   this.mc.displayGuiScreen(guiperformancesettingsof);
               }
               if (button.id == 222) {
                   this.mc.gameSettings.saveOptions();
                   final GuiOtherSettingsOF guiothersettingsof = new GuiOtherSettingsOF(this, this.guiGameSettings);
                   this.mc.displayGuiScreen(guiothersettingsof);
               }
               if (button.id == GameSettings.Options.AO_LEVEL.ordinal()) {
                   return;
               }
           }
       }
       
       @Override
       public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
           this.drawDefaultBackground();
           this.drawCenteredString(this.fontRendererObj, this.screenTitle, this.width / 2, this.is64bit ? 20 : 5, 16777215);
           if (this.is64bit || this.guiGameSettings.renderDistanceChunks > 8) {}
           super.drawScreen(mouseX, mouseY, partialTicks);
           if (Math.abs(mouseX - this.lastMouseX) <= 5 && Math.abs(mouseY - this.lastMouseY) <= 5) {
               final int i = 700;
               if (System.currentTimeMillis() >= this.mouseStillTime + i) {
                   final int j = this.width / 2 - 150;
                   int k = this.height / 6 - 5;
                   if (mouseY <= k + 98) {
                       k += 105;
                   }
                   final int l = j + 150 + 150;
                   final int i2 = k + 84 + 10;
                   final GuiButton guibutton = this.getSelectedButton(mouseX, mouseY);
                   if (guibutton != null) {
                       final String s = this.getButtonName(guibutton.displayString);
                       final String[] astring = this.getTooltipLines(s);
                       if (astring == null) {
                           return;
                       }
                       this.drawGradientRect(j, k, l, i2, -536870912, -536870912);
                       for (int j2 = 0; j2 < astring.length; ++j2) {
                           final String s2 = astring[j2];
                           this.fontRendererObj.drawStringWithShadow(s2, j + 5, k + 5 + j2 * 11, 14540253);
                       }
                   }
               }
           }
           else {
               this.lastMouseX = mouseX;
               this.lastMouseY = mouseY;
               this.mouseStillTime = System.currentTimeMillis();
           }
       }
       
       private String[] getTooltipLines(final String p_getTooltipLines_1_) {
           String[] array2;
           if (p_getTooltipLines_1_.equals("Graphics")) {
               final String[] array = array2 = new String[5];
               array[0] = "Visual quality";
               array[1] = "  Fast  - lower quality, faster";
               array[2] = "  Fancy - higher quality, slower";
               array[3] = "Changes the appearance of clouds, leaves, water,";
               array[4] = "shadows and grass sides.";
           }
           else if (p_getTooltipLines_1_.equals("Render Distance")) {
               final String[] array3 = array2 = new String[8];
               array3[0] = "Visible distance";
               array3[1] = "  2 Tiny - 32m (fastest)";
               array3[2] = "  4 Short - 64m (faster)";
               array3[3] = "  8 Normal - 128m";
               array3[4] = "  16 Far - 256m (slower)";
               array3[5] = "  32 Extreme - 512m (slowest!)";
               array3[6] = "The Extreme view distance is very resource demanding!";
               array3[7] = "Values over 16 Far are only effective in local worlds.";
           }
           else if (p_getTooltipLines_1_.equals("Smooth Lighting")) {
               final String[] array4 = array2 = new String[4];
               array4[0] = "Smooth lighting";
               array4[1] = "  OFF - no smooth lighting (faster)";
               array4[2] = "  Minimum - simple smooth lighting (slower)";
               array4[3] = "  Maximum - complex smooth lighting (slowest)";
           }
           else if (p_getTooltipLines_1_.equals("Smooth Lighting Level")) {
               final String[] array5 = array2 = new String[4];
               array5[0] = "Smooth lighting level";
               array5[1] = "  OFF - no shadows";
               array5[2] = "  50% - light shadows";
               array5[3] = "  100% - dark shadows";
           }
           else if (p_getTooltipLines_1_.equals("Max Framerate")) {
               final String[] array6 = array2 = new String[6];
               array6[0] = "Max framerate";
               array6[1] = "  VSync - limit to monitor framerate (60, 30, 20)";
               array6[2] = "  5-255 - variable";
               array6[3] = "  Unlimited - no limit (fastest)";
               array6[4] = "The framerate limit decreases the FPS even if";
               array6[5] = "the limit value is not reached.";
           }
           else if (p_getTooltipLines_1_.equals("View Bobbing")) {
               final String[] array7 = array2 = new String[2];
               array7[0] = "More realistic movement.";
               array7[1] = "When using mipmaps set it to OFF for best results.";
           }
           else if (p_getTooltipLines_1_.equals("GUI Scale")) {
               final String[] array8 = array2 = new String[2];
               array8[0] = "GUI Scale";
               array8[1] = "Smaller GUI might be faster";
           }
           else if (p_getTooltipLines_1_.equals("Server Textures")) {
               final String[] array9 = array2 = new String[2];
               array9[0] = "Server textures";
               array9[1] = "Use the resource pack recommended by the server";
           }
           else if (p_getTooltipLines_1_.equals("Advanced OpenGL")) {
               final String[] array10 = array2 = new String[6];
               array10[0] = "Detect and render only visible geometry";
               array10[1] = "  OFF - all geometry is rendered (slower)";
               array10[2] = "  Fast - only visible geometry is rendered (fastest)";
               array10[3] = "  Fancy - conservative, avoids visual artifacts (faster)";
               array10[4] = "The option is available only if it is supported by the ";
               array10[5] = "graphic card.";
           }
           else if (p_getTooltipLines_1_.equals("Fog")) {
               final String[] array11 = array2 = new String[6];
               array11[0] = "Fog type";
               array11[1] = "  Fast - faster fog";
               array11[2] = "  Fancy - slower fog, looks better";
               array11[3] = "  OFF - no fog, fastest";
               array11[4] = "The fancy fog is available only if it is supported by the ";
               array11[5] = "graphic card.";
           }
           else if (p_getTooltipLines_1_.equals("Fog Start")) {
               final String[] array12 = array2 = new String[4];
               array12[0] = "Fog start";
               array12[1] = "  0.2 - the fog starts near the player";
               array12[2] = "  0.8 - the fog starts far from the player";
               array12[3] = "This option usually does not affect the performance.";
           }
           else if (p_getTooltipLines_1_.equals("Brightness")) {
               final String[] array13 = array2 = new String[5];
               array13[0] = "Increases the brightness of darker objects";
               array13[1] = "  OFF - standard brightness";
               array13[2] = "  100% - maximum brightness for darker objects";
               array13[3] = "This options does not change the brightness of ";
               array13[4] = "fully black objects";
           }
           else if (p_getTooltipLines_1_.equals("Chunk Loading")) {
               final String[] array14 = array2 = new String[8];
               array14[0] = "Chunk Loading";
               array14[1] = "  Default - unstable FPS when loading chunks";
               array14[2] = "  Smooth - stable FPS";
               array14[3] = "  Multi-Core - stable FPS, 3x faster world loading";
               array14[4] = "Smooth and Multi-Core remove the stuttering and ";
               array14[5] = "freezes caused by chunk loading.";
               array14[6] = "Multi-Core can speed up 3x the world loading and";
               array14[7] = "increase FPS by using a second CPU core.";
           }
           else if (p_getTooltipLines_1_.equals("Alternate Blocks")) {
               final String[] array15 = array2 = new String[3];
               array15[0] = "Alternate Blocks";
               array15[1] = "Uses alternative block models for some blocks.";
               array15[2] = "Depends on the selected resource pack.";
           }
           else if (p_getTooltipLines_1_.equals("Use VBOs")) {
               final String[] array16 = array2 = new String[3];
               array16[0] = "Vertex Buffer Objects";
               array16[1] = "Uses an alternative rendering model which is usually";
               array16[2] = "faster (5-10%) than the default rendering.";
           }
           else if (p_getTooltipLines_1_.equals("3D Anaglyph")) {
               final String[] array17 = array2 = new String[4];
               array17[0] = "3D Anaglyph";
               array17[1] = "Enables a stereoscopic 3D effect using different colors";
               array17[2] = "for each eye.";
               array17[3] = "Requires red-cyan glasses for proper viewing.";
           }
           else {
               array2 = null;
           }
           return array2;
       }
       
       private String getButtonName(final String p_getButtonName_1_) {
           final int i = p_getButtonName_1_.indexOf(58);
           return (i < 0) ? p_getButtonName_1_ : p_getButtonName_1_.substring(0, i);
       }
       
       private GuiButton getSelectedButton(final int p_getSelectedButton_1_, final int p_getSelectedButton_2_) {
           for (int i = 0; i < this.buttonList.size(); ++i) {
               final GuiButton guibutton = (GuiButton) this.buttonList.get(i);
               final boolean flag = p_getSelectedButton_1_ >= guibutton.xPosition && p_getSelectedButton_2_ >= guibutton.yPosition && p_getSelectedButton_1_ < guibutton.xPosition + guibutton.width && p_getSelectedButton_2_ < guibutton.yPosition + guibutton.height;
               if (flag) {
                   return guibutton;
               }
           }
           return null;
       }
       
       public static int getButtonWidth(final GuiButton p_getButtonWidth_0_) {
           return p_getButtonWidth_0_.width;
       }
       
       public static int getButtonHeight(final GuiButton p_getButtonHeight_0_) {
           return p_getButtonHeight_0_.height;
       }
   }
