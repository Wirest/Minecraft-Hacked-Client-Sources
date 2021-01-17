package me.slowly.client.ui.hotbaritems;

import java.text.SimpleDateFormat;
import java.util.Date;
import me.slowly.client.ui.hudcustomizer.customs.CustomHUDHotbar;

public class HotbarItemDate extends HotbarItem {
   public HotbarItemDate() {
      super(CustomHUDHotbar.dateEnabled, CustomHUDHotbar.datePosition, CustomHUDHotbar.dateFont, CustomHUDHotbar.dateFontSize, CustomHUDHotbar.dateUseShadow, CustomHUDHotbar.dateShadowSize, CustomHUDHotbar.dateFontColor);
   }

   public String getStr() {
      SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
      Date date = new Date();
      String strDate = dateFormat.format(date);
      return strDate;
   }
}
