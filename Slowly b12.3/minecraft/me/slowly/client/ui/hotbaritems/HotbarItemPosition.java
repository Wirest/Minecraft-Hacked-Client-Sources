package me.slowly.client.ui.hotbaritems;

import me.slowly.client.ui.hudcustomizer.customs.CustomHUDHotbar;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

public class HotbarItemPosition extends HotbarItem {
   public HotbarItemPosition() {
      super(CustomHUDHotbar.positionEnabled, CustomHUDHotbar.positionPosition, CustomHUDHotbar.positionFont, CustomHUDHotbar.positionFontSize, CustomHUDHotbar.positionUseShadow, CustomHUDHotbar.positionShadowSize, CustomHUDHotbar.positionFontColor);
   }

   public String getStr() {
      EntityPlayer player = Minecraft.getMinecraft().thePlayer;
      String strPos = "X: " + (int)player.posX + " Y: " + (int)player.posY + " Z: " + (int)player.posZ;
      return strPos;
   }
}
