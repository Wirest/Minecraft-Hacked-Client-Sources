package org.m0jang.crystal.Mod;

import com.darkmagician6.eventapi.EventManager;
import net.minecraft.client.Minecraft;
import org.m0jang.crystal.Wrapper;

public class SubModule {
   private String name;
   private String parentModName;
   private boolean isEnabled;
   protected Minecraft mc;

   public SubModule(String name, String mainmodule) {
      this.mc = Wrapper.mc;
      this.name = name;
      this.parentModName = mainmodule;
      this.name = name;
   }

   public void onEnable() {
      EventManager.register(this);
   }

   public void onDisable() {
      EventManager.unregister(this);
   }

   public void setEnabled(boolean enabled) {
      if (enabled) {
         this.isEnabled = true;
         this.onEnable();
      } else {
         this.isEnabled = false;
         this.onDisable();
      }

   }

   public String getName() {
      return this.name;
   }

   public String getParentModName() {
      return this.parentModName;
   }

   public void setName(String name) {
      this.name = name;
   }

   public void setParentModName(String parentModName) {
      this.parentModName = parentModName;
   }

   public boolean isEnabled() {
      return this.isEnabled;
   }

   public Minecraft getMc() {
      return this.mc;
   }
}
