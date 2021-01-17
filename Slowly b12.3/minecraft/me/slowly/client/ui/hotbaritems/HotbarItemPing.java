package me.slowly.client.ui.hotbaritems;

import com.darkmagician6.eventapi.EventTarget;
import me.slowly.client.events.UpdateEvent;
import me.slowly.client.ui.hudcustomizer.customs.CustomHUDHotbar;
import net.minecraft.client.Minecraft;

public class HotbarItemPing extends HotbarItem {
   private long ping;

   public HotbarItemPing() {
      super(CustomHUDHotbar.pingEnabled, CustomHUDHotbar.pingPosition, CustomHUDHotbar.pingFont, CustomHUDHotbar.pingFontSize, CustomHUDHotbar.pingUseShadow, CustomHUDHotbar.pingShadowSize, CustomHUDHotbar.pingFontColor);
   }

   @EventTarget
   public void onUpdate(UpdateEvent event) {
      this.ping = Minecraft.getMinecraft().isSingleplayer() ? 0L : Minecraft.getMinecraft().getCurrentServerData().pingToServer;
   }

   public String getStr() {
      return "Ping: " + (Minecraft.getMinecraft().isSingleplayer() ? 0L : this.ping);
   }
}
