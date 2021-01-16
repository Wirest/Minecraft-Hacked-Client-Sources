package me.existdev.exist.module.modules.render;

import me.existdev.exist.Exist;
import me.existdev.exist.module.Module;
import me.existdev.exist.setting.Setting;

public class HUD extends Module {
   public HUD() {
      super("HUD", 0, Module.Category.Render);
      Exist.settingManager.addSetting(new Setting(this, "Name", true));
      Exist.settingManager.addSetting(new Setting(this, "ArrayList", true));
      Exist.settingManager.addSetting(new Setting(this, "TabGUI", true));
      Exist.settingManager.addSetting(new Setting(this, "Rainbow", false));
      Exist.settingManager.addSetting(new Setting(this, "Icon", true));
      Exist.settingManager.addSetting(new Setting(this, "Info", true));
      Exist.settingManager.addSetting(new Setting(this, "Red", 66.0D, 1.0D, 255.0D, false));
      Exist.settingManager.addSetting(new Setting(this, "Blue", 134.0D, 1.0D, 255.0D, false));
      Exist.settingManager.addSetting(new Setting(this, "Green", 244.0D, 1.0D, 255.0D, false));
   }
}
