package rip.autumn.module.impl.visuals.hud.impl.tabgui;

import me.zane.basicbus.api.annotations.Listener;
import rip.autumn.events.game.KeyPressEvent;
import rip.autumn.module.impl.visuals.hud.impl.TabComponent;

public final class TabInputHandler {
   private final TabComponent component;

   public TabInputHandler(TabComponent component) {
      this.component = component;
   }

   @Listener(KeyPressEvent.class)
   public final void onKeyPress(KeyPressEvent event) {
      this.component.getHandler().doKeyInput(event.getKeyCode());
   }
}
