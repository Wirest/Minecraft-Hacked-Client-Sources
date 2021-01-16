package org.m0jang.crystal.GUI.click.component;

import org.m0jang.crystal.GUI.click.WolframGui;
import org.m0jang.crystal.GUI.click.window.Window;

public class Placeholder extends Component {
   public Placeholder(Window window, int id, int offX, int offY, Component target) {
      super(window, id, offX, offY, target.title);
      this.width = Math.max(WolframGui.defaultWidth, window.width);
      this.height = 0;
      this.type = "Placeholder";
   }

   public void render(int mouseX, int mouseY) {
   }

   public void mouseUpdates(int mouseX, int mouseY, boolean isPressed) {
   }
}
