package org.m0jang.crystal.GUI.click.component;

import net.minecraft.client.Minecraft;
import org.m0jang.crystal.Font.Fonts;
import org.m0jang.crystal.GUI.click.RenderUtils;
import org.m0jang.crystal.GUI.click.WolframGui;
import org.m0jang.crystal.GUI.click.window.Window;

public class Info extends Component {
   boolean heightInitialized = false;

   public Info(Window window, int id, int offX, int offY, String title) {
      super(window, id, offX, offY, title);
      this.width = Math.max(WolframGui.defaultWidth, window.width);
      this.height = WolframGui.defaultWidth;
      this.type = "Info";
      this.editable = false;
   }

   public void render(int mouseX, int mouseY) {
      int fontHeight = Fonts.segoe18.FONT_HEIGHT;
      int y = this.y;
      StringBuilder var10000 = new StringBuilder("X:");
      Minecraft.getMinecraft();
      var10000 = var10000.append((int)Minecraft.thePlayer.posX).append(" ").append("Y:");
      Minecraft.getMinecraft();
      var10000 = var10000.append((int)Minecraft.thePlayer.posY).append(" ").append("Z:");
      Minecraft.getMinecraft();
      String coords = var10000.append((int)Minecraft.thePlayer.posZ).toString();
      RenderUtils.drawRect((float)this.x, (float)y, (float)this.width, 14.0F, WolframGui.backgroundColor);
      Fonts.segoe18.drawString(coords, (float)(this.x + 2), (float)(y + 7) - (float)fontHeight / 2.0F, 16777215);
      y += 14;
      y += 14;
      this.height = y - this.y;
      if (!this.heightInitialized) {
         this.heightInitialized = true;
         this.window.repositionComponents();
      }

   }

   public void mouseUpdates(int mouseX, int mouseY, boolean isPressed) {
   }
}
