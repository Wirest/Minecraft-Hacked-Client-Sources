package me.existdev.exist.module.modules.render;

import me.existdev.exist.Exist;
import me.existdev.exist.module.Module;
import me.existdev.exist.setting.Setting;

public class ClickGUI extends Module {
   public ClickGUI() {
      super("ClickGUI", 54, Module.Category.Render);
      Exist.settingManager.addSetting(new Setting(this, "Blur", true));
   }

   public void onEnable() {
      this.toggle();
      mc.displayGuiScreen(Exist.clickgui);
      super.onEnable();
   }
}
