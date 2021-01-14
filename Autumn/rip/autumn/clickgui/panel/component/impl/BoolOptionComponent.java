package rip.autumn.clickgui.panel.component.impl;

import java.awt.Color;
import net.minecraft.client.gui.Gui;
import rip.autumn.clickgui.ClickGuiScreen;
import rip.autumn.clickgui.panel.Panel;
import rip.autumn.clickgui.panel.component.Component;
import rip.autumn.module.option.impl.BoolOption;
import rip.autumn.utils.ColorUtils;

public final class BoolOptionComponent extends Component {
   private final BoolOption option;
   private int opacity = 120;

   public BoolOptionComponent(BoolOption option, Panel panel, int x, int y, int width, int height) {
      super(panel, x, y, width, height);
      this.option = option;
   }

   public void onDraw(int mouseX, int mouseY) {
      Panel parent = this.getPanel();
      int x = parent.getX() + this.getX();
      int y = parent.getY() + this.getY();
      boolean hovered = this.isMouseOver(mouseX, mouseY);
      if (hovered) {
         if (this.opacity < 200) {
            this.opacity += 5;
         }
      } else if (this.opacity > 120) {
         this.opacity -= 5;
      }

      Gui.drawRect((double)x, (double)y, (double)(x + this.getWidth()), (double)(y + this.getHeight()), ColorUtils.getColorWithOpacity(BACKGROUND, 255 - this.opacity).getRGB());
      int color = this.option.getValue() ? ClickGuiScreen.getColor().getRGB() : (new Color(this.opacity, this.opacity, this.opacity)).getRGB();
      FONT_RENDERER.drawStringWithShadow(this.option.getLabel(), (float)x + 2.0F, (float)y + (float)this.getHeight() / 2.0F - 4.0F, color);
   }

   public void onMouseClick(int mouseX, int mouseY, int mouseButton) {
      if (this.isMouseOver(mouseX, mouseY)) {
         this.option.setValue(!this.option.getValue());
      }

   }

   public boolean isHidden() {
      return !this.option.check();
   }
}
