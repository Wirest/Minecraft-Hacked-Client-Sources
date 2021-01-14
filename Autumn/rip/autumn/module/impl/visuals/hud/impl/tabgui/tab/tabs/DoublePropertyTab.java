package rip.autumn.module.impl.visuals.hud.impl.tabgui.tab.tabs;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.MathHelper;
import rip.autumn.module.impl.visuals.hud.impl.tabgui.TabHandler;
import rip.autumn.module.impl.visuals.hud.impl.tabgui.block.TabBlock;
import rip.autumn.module.impl.visuals.hud.impl.tabgui.tab.AbstractTab;
import rip.autumn.module.impl.visuals.hud.impl.tabgui.tab.Tab;
import rip.autumn.module.option.impl.DoubleOption;

public class DoublePropertyTab extends AbstractTab {
   private boolean selected;

   public DoublePropertyTab(TabHandler handler, DoubleOption stateObject, Tab parent, TabBlock children, TabBlock container) {
      super(handler, stateObject, parent, children, container);
   }

   public void doInvocation() {
      this.selected = true;
      this.handler.disableDefaultKeyListening();
      this.handler.subscribeActionToKey(this, 203, (key, tab) -> {
         ((DoubleOption)this.stateObject).setValue(MathHelper.clamp_double((Double)((DoubleOption)this.stateObject).getValue() - ((DoubleOption)this.stateObject).getIncrement(), ((DoubleOption)this.stateObject).getMinValue(), ((DoubleOption)this.stateObject).getMaxValue()));
      });
      this.handler.subscribeActionToKey(this, 205, (key, tab) -> {
         ((DoubleOption)this.stateObject).setValue(MathHelper.clamp_double((Double)((DoubleOption)this.stateObject).getValue() + ((DoubleOption)this.stateObject).getIncrement(), ((DoubleOption)this.stateObject).getMinValue(), ((DoubleOption)this.stateObject).getMaxValue()));
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

      DoubleOption stateObject = (DoubleOption)this.stateObject;
      double renderIncrement = (double)(width - 6) / (stateObject.getMaxValue() - stateObject.getMinValue());
      Gui.drawRect((double)(x + 3), (double)(y + 9), (double)(x + width - 3), (double)(y + 10), -1436524448);
      Gui.drawRect((double)(x + 3), (double)(y + 9), (double)(x + 3) + renderIncrement * (double)((float)(Double)stateObject.getValue()) - renderIncrement * ((DoubleOption)this.stateObject).getMinValue(), (double)(y + 10), lineRenderColor);
      String valueString = String.valueOf((float)Math.round((Double)stateObject.getValue() * 10.0D) / 10.0F);
      fr.drawStringWithShadow(stateObject.getLabel(), (float)(x + 3), (float)(y + 4), this.selected ? -1 : -2236963);
      fr.drawStringWithShadow(valueString, (float)(x + width - fr.getStringWidth(valueString) - 4), (float)(y + 4), this.selected ? -1 : -2236963);
   }

   public int getTabWidth() {
      return this.mc.fontRendererSmall.getStringWidth(((DoubleOption)this.stateObject).getLabel() + " " + String.format("%.1f", ((DoubleOption)this.stateObject).getMaxValue())) + 7;
   }
}
