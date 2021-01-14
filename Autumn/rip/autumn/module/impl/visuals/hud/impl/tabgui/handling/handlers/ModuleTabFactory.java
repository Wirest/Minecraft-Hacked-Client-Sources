package rip.autumn.module.impl.visuals.hud.impl.tabgui.handling.handlers;

import rip.autumn.module.Module;
import rip.autumn.module.impl.visuals.hud.impl.tabgui.TabHandler;
import rip.autumn.module.impl.visuals.hud.impl.tabgui.block.TabBlock;
import rip.autumn.module.impl.visuals.hud.impl.tabgui.handling.TabFactory;
import rip.autumn.module.impl.visuals.hud.impl.tabgui.tab.Tab;
import rip.autumn.module.impl.visuals.hud.impl.tabgui.tab.tabs.ModuleTab;

public class ModuleTabFactory implements TabFactory {
   public Tab parse(TabHandler handler, Module stateObject, Tab parent, TabBlock children, TabBlock container) {
      return new ModuleTab(handler, stateObject, parent, children, container);
   }

   public Class getHandledType() {
      return Module.class;
   }
}
