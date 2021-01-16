package org.m0jang.crystal.GUI.click.component;

import org.m0jang.crystal.GUI.GuiManager;
import org.m0jang.crystal.GUI.click.RenderUtils;
import org.m0jang.crystal.GUI.click.WolframGui;
import org.m0jang.crystal.GUI.click.window.Window;

public class Seplator extends Component {
   public Seplator(Window window, int id, int offX, int offY) {
      super(window, id, offX, offY, "");
      this.width = WolframGui.settingsWidth;
      this.height = 1;
      this.type = "Seplator";
   }

   public void render(int mouseX, int mouseY) {
      RenderUtils.drawRect((float)this.x, (float)this.y, (float)(this.width - (this.window.scrollbarEnabled ? WolframGui.scrollbarWidth : 0)), 1.0F, WolframGui.backgroundColor);
      RenderUtils.drawRect((float)(this.x + 2), (float)this.y, (float)(this.width - (this.window.scrollbarEnabled ? WolframGui.scrollbarWidth : 0) - 4), 1.0F, GuiManager.getHexMainColor());
   }

   public void mouseUpdates(int var1, int var2, boolean var3) {
   }
}
