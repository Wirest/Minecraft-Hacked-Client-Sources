package me.razerboy420.weepcraft.gui.click.items;

import me.razerboy420.weepcraft.Weepcraft;
import me.razerboy420.weepcraft.gui.click.Window;
import me.razerboy420.weepcraft.util.ColorUtil;
import me.razerboy420.weepcraft.util.MouseUtils;
import me.razerboy420.weepcraft.util.Wrapper;
import net.minecraft.client.gui.Gui;

public class ClickItem {

   public int x;
   public int y;
   public Window window;


   public ClickItem(int x, int y, Window window) {
      this.x = x;
      this.y = y;
      this.window = window;
   }

   public void draw() {
      Gui.drawString(Wrapper.fr(), (this.window.isOpen()?ColorUtil.getColor(Weepcraft.enabledColor):ColorUtil.getColor(Weepcraft.whiteColor)) + this.window.getTitle(), (float)this.x, (float)this.y, -1);
   }

   public boolean isHovered() {
      return this.x < MouseUtils.getMouseX() && this.x + 90 > MouseUtils.getMouseX() && this.y < MouseUtils.getMouseY() && this.y + 10 > MouseUtils.getMouseY();
   }
}
