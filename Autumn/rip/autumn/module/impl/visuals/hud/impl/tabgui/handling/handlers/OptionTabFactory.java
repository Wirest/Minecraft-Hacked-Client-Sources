package rip.autumn.module.impl.visuals.hud.impl.tabgui.handling.handlers;

import net.minecraft.client.gui.FontRenderer;
import rip.autumn.module.impl.visuals.hud.impl.tabgui.TabHandler;
import rip.autumn.module.impl.visuals.hud.impl.tabgui.block.TabBlock;
import rip.autumn.module.impl.visuals.hud.impl.tabgui.handling.TabFactory;
import rip.autumn.module.impl.visuals.hud.impl.tabgui.tab.AbstractTab;
import rip.autumn.module.impl.visuals.hud.impl.tabgui.tab.Tab;
import rip.autumn.module.impl.visuals.hud.impl.tabgui.tab.tabs.BooleanPropertyTab;
import rip.autumn.module.impl.visuals.hud.impl.tabgui.tab.tabs.ColorPropertyTab;
import rip.autumn.module.impl.visuals.hud.impl.tabgui.tab.tabs.DoublePropertyTab;
import rip.autumn.module.impl.visuals.hud.impl.tabgui.tab.tabs.EnumPropertyTab;
import rip.autumn.module.option.Option;
import rip.autumn.module.option.impl.BoolOption;
import rip.autumn.module.option.impl.ColorOption;
import rip.autumn.module.option.impl.DoubleOption;
import rip.autumn.module.option.impl.EnumOption;

public class OptionTabFactory implements TabFactory {
   public final Tab parse(TabHandler handler, Option stateObject, Tab parent, TabBlock children, TabBlock container) {
      if (stateObject instanceof BoolOption) {
         return new BooleanPropertyTab(handler, (BoolOption)stateObject, parent, children, container);
      } else if (stateObject instanceof EnumOption) {
         return new EnumPropertyTab(handler, (EnumOption)stateObject, parent, children, container);
      } else if (stateObject instanceof DoubleOption) {
         return new DoublePropertyTab(handler, (DoubleOption)stateObject, parent, children, container);
      } else {
         return (Tab)(stateObject instanceof ColorOption ? new ColorPropertyTab(handler, (ColorOption)stateObject, parent, children, container) : new AbstractTab(handler, stateObject, parent, children, container) {
            public void doInvocation() {
            }

            public void renderTabFront() {
               FontRenderer font = this.mc.fontRenderer;
               font.drawStringWithShadow(((Option)this.stateObject).getLabel(), (float)(this.getPosX() + 3), (float)(this.getPosY() + 4), -2236963);
            }

            public int getTabWidth() {
               FontRenderer font = this.mc.fontRenderer;
               return font.getStringWidth(((Option)this.stateObject).getLabel()) + 7;
            }
         });
      }
   }

   public Class getHandledType() {
      return Option.class;
   }
}
