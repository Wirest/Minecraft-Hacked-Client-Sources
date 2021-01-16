package me.existdev.exist.module.modules.movement;

import com.mojang.realmsclient.gui.ChatFormatting;
import java.util.ArrayList;
import me.existdev.exist.Exist;
import me.existdev.exist.module.Module;
import me.existdev.exist.setting.Setting;
import net.minecraft.client.Minecraft;

public class Step extends Module {
   // $FF: synthetic field
   private ArrayList Modes = new ArrayList();

   // $FF: synthetic method
   public Step() {
      super("Step", 0, Module.Category.Movement);
      this.Modes.add("AAC");
      this.Modes.add("NCP");
      Exist.settingManager.addSetting(new Setting(this, "Mode", "AAC", this.Modes));
   }

   // $FF: synthetic method
   public void onUpdate() {
      if(this.isToggled()) {
         Minecraft var10000;
         if(Exist.settingManager.getSetting(this, "Mode").getCurrentOption().equalsIgnoreCase("NCP")) {
            this.setDisplayName(this.getName() + ChatFormatting.WHITE + " NCP");
            var10000 = mc;
            if(Minecraft.thePlayer.isCollidedHorizontally) {
               var10000 = mc;
               if(Minecraft.thePlayer.onGround) {
                  var10000 = mc;
                  Minecraft.thePlayer.motionY = 0.3799999952316284D;
               }
            }
         }

         if(Exist.settingManager.getSetting(this, "Mode").getCurrentOption().equalsIgnoreCase("AAC")) {
            this.setDisplayName(this.getName() + ChatFormatting.WHITE + " AAC");
            var10000 = mc;
            if(Minecraft.thePlayer.onGround) {
               var10000 = mc;
               if(Minecraft.thePlayer.isCollidedHorizontally) {
                  var10000 = mc;
                  Minecraft.thePlayer.motionY = 0.4000000059604645D;
               }
            }
         }

      }
   }
}
