package me.existdev.exist.module.modules.movement;

import com.darkmagician6.eventapi.EventTarget;
import com.mojang.realmsclient.gui.ChatFormatting;
import java.util.ArrayList;
import me.existdev.exist.Exist;
import me.existdev.exist.module.Module;
import me.existdev.exist.setting.Setting;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemBow;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public class NoSlowdown extends Module {
   // $FF: synthetic field
   ArrayList Modes = new ArrayList();
   // $FF: synthetic field
   static Minecraft mc = Minecraft.getMinecraft();

   // $FF: synthetic method
   public NoSlowdown() {
      super("NoSlowdown", 0, Module.Category.Movement);
      this.Modes.add("NCP");
      this.Modes.add("AAC");
      Exist.settingManager.addSetting(new Setting(this, "Mode", "AAC", this.Modes));
   }

   // $FF: synthetic method
   @EventTarget
   public void onUpdate() {
      if(this.isToggled()) {
         if(Exist.settingManager.getSetting(this, "Mode").getCurrentOption().equalsIgnoreCase("Hypixel")) {
            this.setDisplayName(this.getName() + ChatFormatting.WHITE + " Hypixel");
         } else {
            if(Exist.settingManager.getSetting(this, "Mode").getCurrentOption().equalsIgnoreCase("AAC")) {
               this.setDisplayName(this.getName() + ChatFormatting.WHITE + " AAC");
               if(Minecraft.thePlayer.isBlocking()) {
                  Minecraft.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, new BlockPos(0, 0, 0), EnumFacing.DOWN));
               }

               if(Minecraft.thePlayer.isEating()) {
                  this.setSpeed(0.08D);
               }

               if(Minecraft.thePlayer.isUsingItem() && Minecraft.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemBow && Minecraft.thePlayer.onGround) {
                  this.setSpeed(0.08D);
               }
            }

            if(Exist.settingManager.getSetting(this, "Mode").getCurrentOption().equalsIgnoreCase("NCP")) {
               this.setDisplayName(this.getName() + ChatFormatting.WHITE + " NCP");
               if(Minecraft.thePlayer.isBlocking()) {
                  Minecraft.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, new BlockPos(0, 0, 0), EnumFacing.DOWN));
               }
            }

         }
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
