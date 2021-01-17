package me.slowly.client.ui.scriptmenu.system;

import java.util.HashMap;
import java.util.Map;

public class Script {
   private String name;
   private Map propertyList;
   private String scriptMode;

   public Script(String name, Map propertyList, String scriptMode) {
      this.name = name;
      this.propertyList = propertyList;
      this.scriptMode = scriptMode;
   }

   public String getName() {
      return this.name;
   }

   public Map getPropertyList() {
      return this.propertyList;
   }

   public void setPropertyList(HashMap propertyList) {
      this.propertyList = propertyList;
   }

   public void setName(String name) {
      this.name = name;
   }

   public String getScriptMode() {
      return this.scriptMode;
   }
}
