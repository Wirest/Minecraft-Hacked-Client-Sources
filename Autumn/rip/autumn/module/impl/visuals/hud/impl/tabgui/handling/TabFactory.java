package rip.autumn.module.impl.visuals.hud.impl.tabgui.handling;

import rip.autumn.module.impl.visuals.hud.impl.tabgui.TabHandler;
import rip.autumn.module.impl.visuals.hud.impl.tabgui.block.TabBlock;
import rip.autumn.module.impl.visuals.hud.impl.tabgui.tab.Tab;

public interface TabFactory {
   Tab parse(TabHandler var1, Object var2, Tab var3, TabBlock var4, TabBlock var5);

   Class getHandledType();
}
