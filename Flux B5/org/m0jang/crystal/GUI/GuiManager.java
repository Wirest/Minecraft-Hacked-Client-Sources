package org.m0jang.crystal.GUI;

import java.awt.Color;
import org.m0jang.crystal.GUI.click.WolframGui;
import org.m0jang.crystal.Utils.RenderUtils;

public class GuiManager {
   public WolframGui gui;

   public static int getHexMainColor() {
      Color overrayColor = new Color(186, 70, 200, 200);
      return overrayColor.getRGB();
   }

   public static int getNegativeHexColor() {
      return 16777215 - getHexMainColor();
   }

   public void loadGui() {
      this.gui = new WolframGui();
   }

   public void render() {
      this.gui.update();
      this.gui.render();
   }

   private void renderTextUtils(int y) {
      RenderUtils.getDisplayWidth();
      y = RenderUtils.getDisplayHeight() - 1;
   }

   public void onKeyPress() {
   }
}
