package rip.autumn.module.impl.visuals;

import rip.autumn.annotations.Label;
import rip.autumn.clickgui.ClickGuiScreen;
import rip.autumn.module.Module;
import rip.autumn.module.ModuleCategory;
import rip.autumn.module.annotations.Aliases;
import rip.autumn.module.annotations.Bind;
import rip.autumn.module.annotations.Category;

@Label("Click GUI")
@Bind("RSHIFT")
@Category(ModuleCategory.VISUALS)
@Aliases({"clickgui", "gui"})
public final class ClickGUIMod extends Module {
   public ClickGUIMod() {
      this.setHidden(true);
   }

   public void onEnabled() {
      mc.displayGuiScreen(ClickGuiScreen.getInstance());
      this.setEnabled(false);
   }
}
