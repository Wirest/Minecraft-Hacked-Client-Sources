package org.m0jang.crystal.Mod.Collection.Render;

import org.m0jang.crystal.Crystal;
import org.m0jang.crystal.Wrapper;
import org.m0jang.crystal.Mod.Category;
import org.m0jang.crystal.Mod.Module;
import org.m0jang.crystal.Values.Value;

public class ClickGUI extends Module {
   public static Value blur;

   static {
      blur = new Value("ClickGUI", Boolean.TYPE, "Blur", false);
   }

   public ClickGUI() {
      super("ClickGUI", Category.Render, false);
   }

   public void onEnable() {
      Wrapper.mc.displayGuiScreen(Crystal.INSTANCE.guiManager.gui);
      this.toggle();
   }
}
