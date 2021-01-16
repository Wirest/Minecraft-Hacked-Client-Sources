package me.existdev.exist;

import me.existdev.exist.command.CommandManager;
import me.existdev.exist.file.CustomFileManager;
import me.existdev.exist.file.files.AltFile;
import me.existdev.exist.gui.account.AltManager;
import me.existdev.exist.gui.clickgui.ClickGUI;
import me.existdev.exist.module.ModuleManager;
import me.existdev.exist.setting.SettingManager;
import me.existdev.exist.ttf.CustomFontManager;
import net.minecraft.client.Minecraft;

public class Exist {
   // $FF: synthetic field
   private static final String name = "Exist";
   // $FF: synthetic field
   private static final double version = 0.71D;
   // $FF: synthetic field
   public static SettingManager settingManager;
   // $FF: synthetic field
   public static ModuleManager moduleManager;
   // $FF: synthetic field
   public static CustomFontManager fontManager;
   // $FF: synthetic field
   public static CommandManager commandManager;
   // $FF: synthetic field
   public static CustomFileManager fileManager;
   // $FF: synthetic field
   public static AltManager altManager;
   // $FF: synthetic field
   public static ClickGUI clickgui;
   // $FF: synthetic field
   public static AltFile altFile;
   // $FF: synthetic field
   public static Minecraft mc = Minecraft.getMinecraft();

   // $FF: synthetic method
   public Exist() {
      settingManager = new SettingManager();
      moduleManager = new ModuleManager();
      fontManager = new CustomFontManager();
      commandManager = new CommandManager();
      fileManager = new CustomFileManager();
      altManager = new AltManager();
      clickgui = new ClickGUI();
      altFile = new AltFile();
      commandManager.loadCommands();
      altManager.setupAlts();
      setConfig();
   }

   // $FF: synthetic method
   public static String getName() {
      return "Exist";
   }

   // $FF: synthetic method
   public static double getVersion() {
      return 0.71D;
   }

   // $FF: synthetic method
   public static void setConfig() {
      mc.gameSettings.limitFramerate = 60;
   }
}
