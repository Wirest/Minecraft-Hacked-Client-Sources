package rip.autumn.clickgui.panel.component.impl;

import java.awt.Color;
import java.math.BigDecimal;
import java.math.RoundingMode;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import rip.autumn.clickgui.panel.Panel;
import rip.autumn.clickgui.panel.component.Component;
import rip.autumn.module.option.impl.DoubleOption;
import rip.autumn.utils.ColorUtils;

public final class NumberOptionComponent extends Component {
   private final DoubleOption option;
   private boolean dragging = false;
   private int opacity = 120;

   public NumberOptionComponent(DoubleOption option, Panel panel, int x, int y, int width, int height) {
      super(panel, x, y, width, height);
      this.option = option;
   }

   public void onDraw(int mouseX, int mouseY) {
      Panel parent = this.getPanel();
      int x = parent.getX() + this.getX();
      int y = parent.getY() + this.getY();
      boolean hovered = this.isMouseOver(mouseX, mouseY);
      int height = this.getHeight();
      int width = this.getWidth();
      DoubleOption option = this.option;
      double min = option.getMinValue();
      double max = option.getMaxValue();
      double inc = option.getIncrement();
      if (this.dragging) {
         option.setValue(this.round((double)(mouseX - x) * (max - min) / (double)width + min, inc));
         if ((Double)option.getValue() > max) {
            option.setValue(max);
         } else if ((Double)option.getValue() < min) {
            option.setValue(min);
         }
      }

      double optionValue = this.round((Double)option.getValue(), inc);
      String optionValueStr = String.valueOf(optionValue);
      int color = Color.WHITE.getRGB();
      double renderPerc = (double)(width - 3) / (max - min);
      double barWidth = renderPerc * optionValue - renderPerc * min;
      if (hovered) {
         if (this.opacity < 200) {
            this.opacity += 5;
         }
      } else if (this.opacity > 120) {
         this.opacity -= 5;
      }

      FontRenderer small = FONT_RENDERER_SMALL;
      Gui.drawRect((double)x, (double)y, (double)(x + width), (double)(y + height), ColorUtils.getColorWithOpacity(BACKGROUND, 255 - this.opacity).getRGB());
      Gui.drawRect((double)(x + 3), (double)(y + height - 4), (double)(x + width - 3), (double)(y + height - 3), -1436524448);
      Gui.drawRect((double)(x + 3), (double)(y + height - 4), (double)x + Math.max(barWidth, 4.0D), (double)(y + height - 3), color);
      small.drawStringWithShadow(option.getLabel(), (float)x + 2.0F, (float)y + (float)height / 2.0F - 4.0F, color);
      small.drawStringWithShadow(optionValueStr, (float)(x + width - small.getStringWidth(optionValueStr) - 3), (float)y + (float)height / 2.0F - 4.0F, color);
   }

   public void onMouseClick(int mouseX, int mouseY, int mouseButton) {
      if (this.isMouseOver(mouseX, mouseY)) {
         this.dragging = true;
      }

   }

   public void onMouseRelease(int mouseX, int mouseY, int mouseButton) {
      this.dragging = false;
   }

   public boolean isHidden() {
      return !this.option.check();
   }

   private double round(double num, double increment) {
      double v = (double)Math.round(num / increment) * increment;
      BigDecimal bd = new BigDecimal(v);
      bd = bd.setScale(2, RoundingMode.HALF_UP);
      return bd.doubleValue();
   }
}
