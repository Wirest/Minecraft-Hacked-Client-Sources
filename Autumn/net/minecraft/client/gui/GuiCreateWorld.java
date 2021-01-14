package net.minecraft.client.gui;

import java.io.IOException;
import java.util.Random;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.WorldType;
import net.minecraft.world.storage.ISaveFormat;
import net.minecraft.world.storage.WorldInfo;
import org.apache.commons.lang3.StringUtils;
import org.lwjgl.input.Keyboard;

public class GuiCreateWorld extends GuiScreen {
   private GuiScreen parentScreen;
   private GuiTextField field_146333_g;
   private GuiTextField field_146335_h;
   private String field_146336_i;
   private String gameMode = "survival";
   private String field_175300_s;
   private boolean field_146341_s = true;
   private boolean allowCheats;
   private boolean field_146339_u;
   private boolean field_146338_v;
   private boolean field_146337_w;
   private boolean field_146345_x;
   private boolean field_146344_y;
   private GuiButton btnGameMode;
   private GuiButton btnMoreOptions;
   private GuiButton btnMapFeatures;
   private GuiButton btnBonusItems;
   private GuiButton btnMapType;
   private GuiButton btnAllowCommands;
   private GuiButton btnCustomizeType;
   private String field_146323_G;
   private String field_146328_H;
   private String field_146329_I;
   private String field_146330_J;
   private int selectedIndex;
   public String chunkProviderSettingsJson = "";
   private static final String[] disallowedFilenames = new String[]{"CON", "COM", "PRN", "AUX", "CLOCK$", "NUL", "COM1", "COM2", "COM3", "COM4", "COM5", "COM6", "COM7", "COM8", "COM9", "LPT1", "LPT2", "LPT3", "LPT4", "LPT5", "LPT6", "LPT7", "LPT8", "LPT9"};

   public GuiCreateWorld(GuiScreen p_i46320_1_) {
      this.parentScreen = p_i46320_1_;
      this.field_146329_I = "";
      this.field_146330_J = I18n.format("selectWorld.newWorld");
   }

   public void updateScreen() {
      this.field_146333_g.updateCursorCounter();
      this.field_146335_h.updateCursorCounter();
   }

   public void initGui() {
      Keyboard.enableRepeatEvents(true);
      this.buttonList.clear();
      this.buttonList.add(new GuiButton(0, this.width / 2 - 155, this.height - 28, 150, 20, I18n.format("selectWorld.create")));
      this.buttonList.add(new GuiButton(1, this.width / 2 + 5, this.height - 28, 150, 20, I18n.format("gui.cancel")));
      this.buttonList.add(this.btnGameMode = new GuiButton(2, this.width / 2 - 75, 115, 150, 20, I18n.format("selectWorld.gameMode")));
      this.buttonList.add(this.btnMoreOptions = new GuiButton(3, this.width / 2 - 75, 187, 150, 20, I18n.format("selectWorld.moreWorldOptions")));
      this.buttonList.add(this.btnMapFeatures = new GuiButton(4, this.width / 2 - 155, 100, 150, 20, I18n.format("selectWorld.mapFeatures")));
      this.btnMapFeatures.visible = false;
      this.buttonList.add(this.btnBonusItems = new GuiButton(7, this.width / 2 + 5, 151, 150, 20, I18n.format("selectWorld.bonusItems")));
      this.btnBonusItems.visible = false;
      this.buttonList.add(this.btnMapType = new GuiButton(5, this.width / 2 + 5, 100, 150, 20, I18n.format("selectWorld.mapType")));
      this.btnMapType.visible = false;
      this.buttonList.add(this.btnAllowCommands = new GuiButton(6, this.width / 2 - 155, 151, 150, 20, I18n.format("selectWorld.allowCommands")));
      this.btnAllowCommands.visible = false;
      this.buttonList.add(this.btnCustomizeType = new GuiButton(8, this.width / 2 + 5, 120, 150, 20, I18n.format("selectWorld.customizeType")));
      this.btnCustomizeType.visible = false;
      this.field_146333_g = new GuiTextField(9, this.fontRendererObj, this.width / 2 - 100, 60, 200, 20);
      this.field_146333_g.setFocused(true);
      this.field_146333_g.setText(this.field_146330_J);
      this.field_146335_h = new GuiTextField(10, this.fontRendererObj, this.width / 2 - 100, 60, 200, 20);
      this.field_146335_h.setText(this.field_146329_I);
      this.func_146316_a(this.field_146344_y);
      this.func_146314_g();
      this.func_146319_h();
   }

   private void func_146314_g() {
      this.field_146336_i = this.field_146333_g.getText().trim();
      char[] var1 = ChatAllowedCharacters.allowedCharactersArray;
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         char c0 = var1[var3];
         this.field_146336_i = this.field_146336_i.replace(c0, '_');
      }

      if (StringUtils.isEmpty(this.field_146336_i)) {
         this.field_146336_i = "World";
      }

      this.field_146336_i = func_146317_a(this.mc.getSaveLoader(), this.field_146336_i);
   }

   private void func_146319_h() {
      this.btnGameMode.displayString = I18n.format("selectWorld.gameMode") + ": " + I18n.format("selectWorld.gameMode." + this.gameMode);
      this.field_146323_G = I18n.format("selectWorld.gameMode." + this.gameMode + ".line1");
      this.field_146328_H = I18n.format("selectWorld.gameMode." + this.gameMode + ".line2");
      this.btnMapFeatures.displayString = I18n.format("selectWorld.mapFeatures") + " ";
      if (this.field_146341_s) {
         this.btnMapFeatures.displayString = this.btnMapFeatures.displayString + I18n.format("options.on");
      } else {
         this.btnMapFeatures.displayString = this.btnMapFeatures.displayString + I18n.format("options.off");
      }

      this.btnBonusItems.displayString = I18n.format("selectWorld.bonusItems") + " ";
      if (this.field_146338_v && !this.field_146337_w) {
         this.btnBonusItems.displayString = this.btnBonusItems.displayString + I18n.format("options.on");
      } else {
         this.btnBonusItems.displayString = this.btnBonusItems.displayString + I18n.format("options.off");
      }

      this.btnMapType.displayString = I18n.format("selectWorld.mapType") + " " + I18n.format(WorldType.worldTypes[this.selectedIndex].getTranslateName());
      this.btnAllowCommands.displayString = I18n.format("selectWorld.allowCommands") + " ";
      if (this.allowCheats && !this.field_146337_w) {
         this.btnAllowCommands.displayString = this.btnAllowCommands.displayString + I18n.format("options.on");
      } else {
         this.btnAllowCommands.displayString = this.btnAllowCommands.displayString + I18n.format("options.off");
      }

   }

   public static String func_146317_a(ISaveFormat p_146317_0_, String p_146317_1_) {
      p_146317_1_ = p_146317_1_.replaceAll("[\\./\"]", "_");
      String[] var2 = disallowedFilenames;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         String s = var2[var4];
         if (p_146317_1_.equalsIgnoreCase(s)) {
            p_146317_1_ = "_" + p_146317_1_ + "_";
         }
      }

      while(p_146317_0_.getWorldInfo(p_146317_1_) != null) {
         p_146317_1_ = p_146317_1_ + "-";
      }

      return p_146317_1_;
   }

   public void onGuiClosed() {
      Keyboard.enableRepeatEvents(false);
   }

   protected void actionPerformed(GuiButton button) throws IOException {
      if (button.enabled) {
         if (button.id == 1) {
            this.mc.displayGuiScreen(this.parentScreen);
         } else if (button.id == 0) {
            this.mc.displayGuiScreen((GuiScreen)null);
            if (this.field_146345_x) {
               return;
            }

            this.field_146345_x = true;
            long i = (new Random()).nextLong();
            String s = this.field_146335_h.getText();
            if (!StringUtils.isEmpty(s)) {
               try {
                  long j = Long.parseLong(s);
                  if (j != 0L) {
                     i = j;
                  }
               } catch (NumberFormatException var7) {
                  i = (long)s.hashCode();
               }
            }

            WorldSettings.GameType worldsettings$gametype = WorldSettings.GameType.getByName(this.gameMode);
            WorldSettings worldsettings = new WorldSettings(i, worldsettings$gametype, this.field_146341_s, this.field_146337_w, WorldType.worldTypes[this.selectedIndex]);
            worldsettings.setWorldName(this.chunkProviderSettingsJson);
            if (this.field_146338_v && !this.field_146337_w) {
               worldsettings.enableBonusChest();
            }

            if (this.allowCheats && !this.field_146337_w) {
               worldsettings.enableCommands();
            }

            this.mc.launchIntegratedServer(this.field_146336_i, this.field_146333_g.getText().trim(), worldsettings);
         } else if (button.id == 3) {
            this.func_146315_i();
         } else if (button.id == 2) {
            if (this.gameMode.equals("survival")) {
               if (!this.field_146339_u) {
                  this.allowCheats = false;
               }

               this.field_146337_w = false;
               this.gameMode = "hardcore";
               this.field_146337_w = true;
               this.btnAllowCommands.enabled = false;
               this.btnBonusItems.enabled = false;
               this.func_146319_h();
            } else if (this.gameMode.equals("hardcore")) {
               if (!this.field_146339_u) {
                  this.allowCheats = true;
               }

               this.field_146337_w = false;
               this.gameMode = "creative";
               this.func_146319_h();
               this.field_146337_w = false;
               this.btnAllowCommands.enabled = true;
               this.btnBonusItems.enabled = true;
            } else {
               if (!this.field_146339_u) {
                  this.allowCheats = false;
               }

               this.gameMode = "survival";
               this.func_146319_h();
               this.btnAllowCommands.enabled = true;
               this.btnBonusItems.enabled = true;
               this.field_146337_w = false;
            }

            this.func_146319_h();
         } else if (button.id == 4) {
            this.field_146341_s = !this.field_146341_s;
            this.func_146319_h();
         } else if (button.id == 7) {
            this.field_146338_v = !this.field_146338_v;
            this.func_146319_h();
         } else if (button.id == 5) {
            ++this.selectedIndex;
            if (this.selectedIndex >= WorldType.worldTypes.length) {
               this.selectedIndex = 0;
            }

            while(!this.func_175299_g()) {
               ++this.selectedIndex;
               if (this.selectedIndex >= WorldType.worldTypes.length) {
                  this.selectedIndex = 0;
               }
            }

            this.chunkProviderSettingsJson = "";
            this.func_146319_h();
            this.func_146316_a(this.field_146344_y);
         } else if (button.id == 6) {
            this.field_146339_u = true;
            this.allowCheats = !this.allowCheats;
            this.func_146319_h();
         } else if (button.id == 8) {
            if (WorldType.worldTypes[this.selectedIndex] == WorldType.FLAT) {
               this.mc.displayGuiScreen(new GuiCreateFlatWorld(this, this.chunkProviderSettingsJson));
            } else {
               this.mc.displayGuiScreen(new GuiCustomizeWorldScreen(this, this.chunkProviderSettingsJson));
            }
         }
      }

   }

   private boolean func_175299_g() {
      WorldType worldtype = WorldType.worldTypes[this.selectedIndex];
      return worldtype != null && worldtype.getCanBeCreated() ? (worldtype == WorldType.DEBUG_WORLD ? isShiftKeyDown() : true) : false;
   }

   private void func_146315_i() {
      this.func_146316_a(!this.field_146344_y);
   }

   private void func_146316_a(boolean p_146316_1_) {
      this.field_146344_y = p_146316_1_;
      if (WorldType.worldTypes[this.selectedIndex] == WorldType.DEBUG_WORLD) {
         this.btnGameMode.visible = !this.field_146344_y;
         this.btnGameMode.enabled = false;
         if (this.field_175300_s == null) {
            this.field_175300_s = this.gameMode;
         }

         this.gameMode = "spectator";
         this.btnMapFeatures.visible = false;
         this.btnBonusItems.visible = false;
         this.btnMapType.visible = this.field_146344_y;
         this.btnAllowCommands.visible = false;
         this.btnCustomizeType.visible = false;
      } else {
         this.btnGameMode.visible = !this.field_146344_y;
         this.btnGameMode.enabled = true;
         if (this.field_175300_s != null) {
            this.gameMode = this.field_175300_s;
            this.field_175300_s = null;
         }

         this.btnMapFeatures.visible = this.field_146344_y && WorldType.worldTypes[this.selectedIndex] != WorldType.CUSTOMIZED;
         this.btnBonusItems.visible = this.field_146344_y;
         this.btnMapType.visible = this.field_146344_y;
         this.btnAllowCommands.visible = this.field_146344_y;
         this.btnCustomizeType.visible = this.field_146344_y && (WorldType.worldTypes[this.selectedIndex] == WorldType.FLAT || WorldType.worldTypes[this.selectedIndex] == WorldType.CUSTOMIZED);
      }

      this.func_146319_h();
      if (this.field_146344_y) {
         this.btnMoreOptions.displayString = I18n.format("gui.done");
      } else {
         this.btnMoreOptions.displayString = I18n.format("selectWorld.moreWorldOptions");
      }

   }

   protected void keyTyped(char typedChar, int keyCode) throws IOException {
      if (this.field_146333_g.isFocused() && !this.field_146344_y) {
         this.field_146333_g.textboxKeyTyped(typedChar, keyCode);
         this.field_146330_J = this.field_146333_g.getText();
      } else if (this.field_146335_h.isFocused() && this.field_146344_y) {
         this.field_146335_h.textboxKeyTyped(typedChar, keyCode);
         this.field_146329_I = this.field_146335_h.getText();
      }

      if (keyCode == 28 || keyCode == 156) {
         this.actionPerformed((GuiButton)this.buttonList.get(0));
      }

      ((GuiButton)this.buttonList.get(0)).enabled = this.field_146333_g.getText().length() > 0;
      this.func_146314_g();
   }

   protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
      super.mouseClicked(mouseX, mouseY, mouseButton);
      if (this.field_146344_y) {
         this.field_146335_h.mouseClicked(mouseX, mouseY, mouseButton);
      } else {
         this.field_146333_g.mouseClicked(mouseX, mouseY, mouseButton);
      }

   }

   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
      this.drawDefaultBackground();
      this.drawCenteredString(this.fontRendererObj, I18n.format("selectWorld.create"), this.width / 2, 20, -1);
      if (this.field_146344_y) {
         this.drawString(this.fontRendererObj, I18n.format("selectWorld.enterSeed"), this.width / 2 - 100, 47, -6250336);
         this.drawString(this.fontRendererObj, I18n.format("selectWorld.seedInfo"), this.width / 2 - 100, 85, -6250336);
         if (this.btnMapFeatures.visible) {
            this.drawString(this.fontRendererObj, I18n.format("selectWorld.mapFeatures.info"), this.width / 2 - 150, 122, -6250336);
         }

         if (this.btnAllowCommands.visible) {
            this.drawString(this.fontRendererObj, I18n.format("selectWorld.allowCommands.info"), this.width / 2 - 150, 172, -6250336);
         }

         this.field_146335_h.drawTextBox();
         if (WorldType.worldTypes[this.selectedIndex].showWorldInfoNotice()) {
            this.fontRendererObj.drawSplitString(I18n.format(WorldType.worldTypes[this.selectedIndex].func_151359_c()), this.btnMapType.xPosition + 2, this.btnMapType.yPosition + 22, this.btnMapType.getButtonWidth(), 10526880);
         }
      } else {
         this.drawString(this.fontRendererObj, I18n.format("selectWorld.enterName"), this.width / 2 - 100, 47, -6250336);
         this.drawString(this.fontRendererObj, I18n.format("selectWorld.resultFolder") + " " + this.field_146336_i, this.width / 2 - 100, 85, -6250336);
         this.field_146333_g.drawTextBox();
         this.drawString(this.fontRendererObj, this.field_146323_G, this.width / 2 - 100, 137, -6250336);
         this.drawString(this.fontRendererObj, this.field_146328_H, this.width / 2 - 100, 149, -6250336);
      }

      super.drawScreen(mouseX, mouseY, partialTicks);
   }

   public void func_146318_a(WorldInfo p_146318_1_) {
      this.field_146330_J = I18n.format("selectWorld.newWorld.copyOf", p_146318_1_.getWorldName());
      this.field_146329_I = p_146318_1_.getSeed() + "";
      this.selectedIndex = p_146318_1_.getTerrainType().getWorldTypeID();
      this.chunkProviderSettingsJson = p_146318_1_.getGeneratorOptions();
      this.field_146341_s = p_146318_1_.isMapFeaturesEnabled();
      this.allowCheats = p_146318_1_.areCommandsAllowed();
      if (p_146318_1_.isHardcoreModeEnabled()) {
         this.gameMode = "hardcore";
      } else if (p_146318_1_.getGameType().isSurvivalOrAdventure()) {
         this.gameMode = "survival";
      } else if (p_146318_1_.getGameType().isCreative()) {
         this.gameMode = "creative";
      }

   }
}
