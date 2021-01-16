package org.m0jang.crystal.GUI.click.component;

import org.m0jang.crystal.Font.Fonts;
import org.m0jang.crystal.GUI.click.RenderUtils;
import org.m0jang.crystal.GUI.click.WolframGui;
import org.m0jang.crystal.GUI.click.window.Window;

public class Label extends Component {
   public Label(Window window, int id, int offX, int offY, String title) {
      super(window, id, offX, offY, title);
      this.width = WolframGui.settingsWidth;
      this.height = Fonts.segoe16.getHeight() + Fonts.segoe16.getHeight() / 2;
      this.type = "Label";
   }

   public void render(int mouseX, int mouseY) {
      RenderUtils.drawRect((float)this.x, (float)this.y, (float)(this.width - (this.window.scrollbarEnabled ? WolframGui.scrollbarWidth : 0)), (float)this.height, WolframGui.backgroundColor);
      Fonts.segoe16.drawString(this.title, (float)(this.x + 2), (float)(this.y + (this.height - Fonts.segoe16.getHeight())), 16777215, false);
   }

   public void mouseUpdates(int var1, int var2, boolean var3) {
   }
}
