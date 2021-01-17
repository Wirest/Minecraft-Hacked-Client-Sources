package me.slowly.client.ui.hudcustomizer;

import java.util.ArrayList;

public class CustomHUDOptions {
   public String name;
   public ArrayList values = new ArrayList();

   public CustomHUDOptions(String name) {
      this.name = name;
   }

   public void addValue(CustomValue value) {
      this.values.add(value);
   }
}
