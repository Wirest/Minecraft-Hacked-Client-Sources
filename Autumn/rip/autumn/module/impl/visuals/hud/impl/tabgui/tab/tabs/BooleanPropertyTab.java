package rip.autumn.module.impl.visuals.hud.impl.tabgui.tab.tabs;

import net.minecraft.client.gui.FontRenderer;
import rip.autumn.module.impl.visuals.hud.impl.tabgui.TabHandler;
import rip.autumn.module.impl.visuals.hud.impl.tabgui.block.TabBlock;
import rip.autumn.module.impl.visuals.hud.impl.tabgui.tab.AbstractTab;
import rip.autumn.module.impl.visuals.hud.impl.tabgui.tab.Tab;
import rip.autumn.module.option.impl.BoolOption;

public class BooleanPropertyTab extends AbstractTab {
   public BooleanPropertyTab(TabHandler handler, BoolOption stateObject, Tab parent, TabBlock children, TabBlock container) {
      super(handler, stateObject, parent, children, container);
   }

   public void doInvocation() {
      ((BoolOption)this.stateObject).setValue(!((BoolOption)this.stateObject).getValue());
   }

   public void renderTabFront() {
      FontRenderer font = this.mc.fontRenderer;
      font.drawStringWithShadow(((BoolOption)this.stateObject).getLabel(), (float)(this.getPosX() + 3), (float)this.getPosY() + 2.5F, -855638017);
      font.drawStringWithShadow(": ", (float)(this.getPosX() + font.getStringWidth(((BoolOption)this.stateObject).getLabel()) + 4), (float)this.getPosY() + 2.5F, -5723992);
      String booleanValue = String.valueOf(((BoolOption)this.stateObject).getValue());
      booleanValue = booleanValue.substring(0, 1).toUpperCase() + booleanValue.substring(1).toLowerCase();
      font.drawStringWithShadow(booleanValue, (float)(this.getPosX() + this.getWidth() - font.getStringWidth(booleanValue) - 3), (float)this.getPosY() + 2.5F, ((BoolOption)this.stateObject).getValue() ? -2236963 : -5723992);
   }

   public int getTabWidth() {
      FontRenderer font = this.mc.fontRenderer;
      return font.getStringWidth(((BoolOption)this.stateObject).getLabel() + ": ") + Math.max(font.getStringWidth("False"), font.getStringWidth("True")) + 4;
   }
}
