package rip.autumn.module.impl.visuals.hud.impl.tabgui.tab.tabs;

import java.awt.Color;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.MathHelper;
import rip.autumn.module.impl.visuals.hud.impl.tabgui.TabHandler;
import rip.autumn.module.impl.visuals.hud.impl.tabgui.block.TabBlock;
import rip.autumn.module.impl.visuals.hud.impl.tabgui.tab.AbstractTab;
import rip.autumn.module.impl.visuals.hud.impl.tabgui.tab.Tab;
import rip.autumn.module.option.impl.ColorOption;
import rip.autumn.utils.render.RenderUtils;

public class ColorPropertyTab extends AbstractTab {
   private boolean selected;

   public ColorPropertyTab(TabHandler handler, ColorOption stateObject, Tab parent, TabBlock children, TabBlock container) {
      super(handler, stateObject, parent, children, container);
   }

   public void doInvocation() {
      this.selected = true;
      this.handler.disableDefaultKeyListening();
      this.handler.subscribeActionToKey(this, 203, (key, tab) -> {
         float hue = MathHelper.clamp_float(this.getHue() - 5.0F, 0.0F, 360.0F) / 360.0F;
         ((ColorOption)this.stateObject).setValue(Color.getHSBColor(hue, 1.0F, 1.0F));
      });
      this.handler.subscribeActionToKey(this, 205, (key, tab) -> {
         float hue = MathHelper.clamp_float(this.getHue() + 5.0F, 0.0F, 360.0F) / 360.0F;
         ((ColorOption)this.stateObject).setValue(Color.getHSBColor(hue, 1.0F, 1.0F));
      });
      this.handler.subscribeActionToKey(this, 28, (key, tab) -> {
         this.selected = false;
         this.handler.unsubscribeSpecificActions(this);
         this.handler.enableDefaultKeyListening();
      });
   }

   public void renderTabFront() {
      FontRenderer fr = this.mc.fontRendererSmall;
      int x = this.getPosX();
      int y = this.getPosY();
      int width = this.getWidth();
      int height = this.getHeight();
      int lineRenderColor;
      if (this.selected) {
         Gui.drawRect((double)((float)x + 1.0F), (double)y, (double)((float)(x + width) - 1.0F), (double)(y + height), -16448251);
         lineRenderColor = -1;
      } else {
         lineRenderColor = -5723992;
      }

      double renderIncrement = (double)(width - 6) / 360.0D;
      RenderUtils.drawHsvScale((double)(x + 3), (double)(y + 9), (double)(x + width - 3), (double)(y + 10));
      Gui.drawRect((double)(x + 3) + renderIncrement * (double)this.getHue() - 0.5D, (double)(y + 8), (double)(x + 3) + renderIncrement * (double)this.getHue() + 0.5D, (double)(y + 11), lineRenderColor);
      fr.drawStringWithShadow(((ColorOption)this.stateObject).getLabel(), (float)(x + 3), (float)(y + 4), this.selected ? -1 : -2236963);
   }

   private float getHue() {
      int r = ((Color)((ColorOption)this.stateObject).getValue()).getRed();
      int b = ((Color)((ColorOption)this.stateObject).getValue()).getBlue();
      int g = ((Color)((ColorOption)this.stateObject).getValue()).getGreen();
      return Color.RGBtoHSB(r, g, b, (float[])null)[0] * 360.0F;
   }

   public int getTabWidth() {
      return 1;
   }
}
