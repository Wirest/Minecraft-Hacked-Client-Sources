package me.existdev.exist.module.modules.movement;

import com.darkmagician6.eventapi.EventManager;
import com.mojang.realmsclient.gui.ChatFormatting;
import java.util.ArrayList;
import me.existdev.exist.Exist;
import me.existdev.exist.module.Module;
import me.existdev.exist.module.modules.movement.fly.CubeCraft;
import me.existdev.exist.module.modules.movement.fly.Hypixel;
import me.existdev.exist.module.modules.movement.fly.Normal;
import me.existdev.exist.setting.Setting;
import net.minecraft.client.Minecraft;

public class Fly extends Module {
   // $FF: synthetic field
   public static int counter;
   // $FF: synthetic field
   ArrayList Modes = new ArrayList();
   // $FF: synthetic field
   Hypixel hypixel = new Hypixel();
   // $FF: synthetic field
   CubeCraft cubecraft = new CubeCraft();
   // $FF: synthetic field
   Normal normal = new Normal();

   // $FF: synthetic method
   public Fly() {
      super("Fly", 0, Module.Category.Movement);
      this.Modes.add("Hypixel");
      this.Modes.add("Normal");
      this.Modes.add("CubeCraft");
      Exist.settingManager.addSetting(new Setting(this, "Mode", "Hypixel", this.Modes));
      Exist.settingManager.addSetting(new Setting(this, "AirWalkMotion", true));
   }

   // $FF: synthetic method
   public void onUpdate() {
      if(this.isToggled()) {
         String options = Exist.settingManager.getSetting(this, "Mode").getCurrentOption();
         if(options.equalsIgnoreCase("Hypixel")) {
            if(Exist.settingManager.getSetting(this, "AirWalkMotion").getBooleanValue()) {
               this.setDisplayName(this.getName() + ChatFormatting.WHITE + " Hypixel - AirMotion");
            } else {
               this.setDisplayName(this.getName() + ChatFormatting.WHITE + " Hypixel");
            }
         }

         if(options.equalsIgnoreCase("CubeCraft")) {
            if(Exist.settingManager.getSetting(this, "AirWalkMotion").getBooleanValue()) {
               this.setDisplayName(this.getName() + ChatFormatting.WHITE + " CubeCraft - AirMotion");
            } else {
               this.setDisplayName(this.getName() + ChatFormatting.WHITE + " CubeCraft");
            }
         }

         if(options.equalsIgnoreCase("Normal")) {
            this.setDisplayName(this.getName() + ChatFormatting.WHITE + " Normal");
         }

         super.onUpdate();
      }
   }

   // $FF: synthetic method
   public void onEnable() {
      EventManager.register(this.hypixel);
      EventManager.register(this.cubecraft);
      EventManager.register(this.normal);
      super.onEnable();
   }

   // $FF: synthetic method
   public void onDisable() {
      EventManager.unregister(this.hypixel);
      EventManager.unregister(this.cubecraft);
      EventManager.unregister(this.normal);
      Minecraft var10000 = mc;
      Minecraft.thePlayer.motionX = 0.0D;
      var10000 = mc;
      Minecraft.thePlayer.motionZ = 0.0D;
      mc.timer.timerSpeed = 1.0F;
      super.onDisable();
   }
}
