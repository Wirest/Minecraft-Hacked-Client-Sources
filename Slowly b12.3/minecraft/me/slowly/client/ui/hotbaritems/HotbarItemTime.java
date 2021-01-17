package me.slowly.client.ui.hotbaritems;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import me.slowly.client.ui.hudcustomizer.customs.CustomHUDHotbar;

public class HotbarItemTime extends HotbarItem {
   public HotbarItemTime() {
      super(CustomHUDHotbar.timeEnabled, CustomHUDHotbar.timePosition, CustomHUDHotbar.timeFont, CustomHUDHotbar.timeFontSize, CustomHUDHotbar.timeUseShadow, CustomHUDHotbar.timeShadowSize, CustomHUDHotbar.timeFontColor);
   }

   public String getStr() {
      SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
      Calendar cal = Calendar.getInstance();
      String strDate = dateFormat.format(cal.getTime());
      return strDate;
   }
}
