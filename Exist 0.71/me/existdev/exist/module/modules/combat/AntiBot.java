package me.existdev.exist.module.modules.combat;

import com.mojang.realmsclient.gui.ChatFormatting;
import java.util.ArrayList;
import java.util.Iterator;
import me.existdev.exist.Exist;
import me.existdev.exist.module.Module;
import me.existdev.exist.setting.Setting;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

public class AntiBot extends Module {
   // $FF: synthetic field
   ArrayList Modes = new ArrayList();

   // $FF: synthetic method
   public AntiBot() {
      super("AntiBot", 0, Module.Category.Combat);
      this.Modes.add("WatchDog");
      this.Modes.add("Advanced");
      Exist.settingManager.addSetting(new Setting(this, "Mode", "Hypixel", this.Modes));
   }

   // $FF: synthetic method
   public void onUpdate() {
      if(this.isToggled()) {
         if(Exist.settingManager.getSetting(this, "Mode").getCurrentOption().equalsIgnoreCase("WatchDog")) {
            this.setDisplayName(this.getName() + ChatFormatting.WHITE + " WatchDog");
            Minecraft var10000 = mc;
            Iterator var2 = Minecraft.theWorld.loadedEntityList.iterator();

            while(var2.hasNext()) {
               Object entity = var2.next();
               if(entity instanceof EntityPlayer) {
                  var10000 = mc;
                  if(!Minecraft.thePlayer.equals(entity) && ((Entity)entity).isInvisible() && ((Entity)entity).ticksExisted > 105) {
                     var10000 = mc;
                     Minecraft.theWorld.removeEntity((Entity)entity);
                  }
               }
            }
         }

         if(Exist.settingManager.getSetting(this, "Mode").getCurrentOption().equalsIgnoreCase("Advanced")) {
            this.setDisplayName(this.getName() + ChatFormatting.WHITE + " Advanced");
         }

         super.onUpdate();
      }
   }

   // $FF: synthetic method
   public void onEnable() {
      super.onEnable();
   }

   // $FF: synthetic method
   public void onDisable() {
      super.onDisable();
   }
}
