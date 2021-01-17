package me.slowly.client.ui.scriptmenu.system.object;

import me.slowly.client.mod.Mod;

public class ScriptMod {
   private Mod mod;
   private boolean edit;
   private boolean guiOpen;

   public ScriptMod(Mod m) {
      this.mod = m;
      this.edit = true;
      this.guiOpen = false;
   }

   public Mod getMod() {
      return this.mod;
   }

   public void setMod(Mod mod) {
      this.mod = mod;
   }

   public boolean isEditable() {
      return this.edit;
   }

   public void setEditable(boolean edit) {
      this.edit = edit;
   }

   public boolean isGuiOpen() {
      return this.guiOpen;
   }

   public void setGuiOpen(boolean guiOpen) {
      this.guiOpen = guiOpen;
   }
}
