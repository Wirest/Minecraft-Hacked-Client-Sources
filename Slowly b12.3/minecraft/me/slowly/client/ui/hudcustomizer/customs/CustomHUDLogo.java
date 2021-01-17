package me.slowly.client.ui.hudcustomizer.customs;

import java.util.ArrayList;
import me.slowly.client.ui.hudcustomizer.CustomHUDOptions;
import me.slowly.client.ui.hudcustomizer.CustomValue;

public class CustomHUDLogo {
   public static ArrayList optionList = new ArrayList();
   public static CustomValue mode = new CustomValue("Mode");
   public static CustomHUDOptions otherTab;

   public void addOtherTab() {
      otherTab = new CustomHUDOptions("Select");
      mode.mode.add("Old");
      mode.mode.add("Style 1");
      mode.mode.add("Style 2");
      mode.mode.add("Style 3");
      mode.mode.add("Style 4");
      otherTab.addValue(mode);
   }

   public CustomHUDLogo() {
      this.addOtherTab();
      optionList.add(otherTab);
   }
}
