package org.m0jang.crystal.Mod.Collection.Misc;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Timer;
import org.m0jang.crystal.Events.EventState;
import org.m0jang.crystal.Events.EventUpdate;
import org.m0jang.crystal.Mod.Category;
import org.m0jang.crystal.Mod.Module;

public class Bowfly extends Module {
   boolean enable;
   int bow;
   public static int yprocents = 160;
   public static int xzprocents = 190;
   public static float ymultiplier;
   public static float xzmulitplier;
   public static boolean boost = true;

   public Bowfly() {
      super("Bowfly", Category.Misc, false);
   }

   public void onEnable() {
      super.onEnable();
   }

   public void onDisable() {
      super.onDisable();
   }

   @EventTarget
   public void onUpdate(EventUpdate event) {
      ymultiplier = (float)(yprocents / 100);
      if (event.state == EventState.PRE && this.enable) {
         if (boost) {
            Timer.timerSpeed = 0.2F;
         }

         this.enable = false;
      }

      if (event.state == EventState.POST && !this.enable) {
         Minecraft.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, new BlockPos(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY, Minecraft.thePlayer.posZ), EnumFacing.DOWN));
         Timer.timerSpeed = 1.0F;
         this.enable = true;
      }

      if (event.state == EventState.POST && !doesMove()) {
         Minecraft.thePlayer.motionX = 0.0D;
         Minecraft.thePlayer.motionZ = 0.0D;
      }

      if (event.state == EventState.POST && !boost) {
         if (Minecraft.thePlayer.motionY <= 0.0D) {
            Timer.timerSpeed = 0.2F;
         } else {
            Timer.timerSpeed = 1.0F;
         }
      }

      if (event.state == EventState.POST) {
         ItemStack stack = Minecraft.thePlayer.getCurrentEquippedItem();
         if (stack != null && stack.getItem() instanceof ItemBow) {
            ++this.bow;
            if (this.bow >= 4) {
               Minecraft.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, new BlockPos(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY, Minecraft.thePlayer.posZ), EnumFacing.DOWN));
               this.bow = 0;
            } else {
               Minecraft.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(Minecraft.thePlayer.inventory.getCurrentItem()));
            }
         }
      }

      if (event.state == EventState.POST) {
         if (Minecraft.thePlayer.rotationPitch == -90.0F) {
            xzmulitplier = 0.0F;
         } else {
            xzmulitplier = (float)(xzprocents / 100);
         }
      } else {
         xzmulitplier = (float)(xzprocents / 100);
      }

   }

   public static boolean doesMove() {
      return Minecraft.gameSettings.keyBindForward.pressed || Minecraft.gameSettings.keyBindRight.pressed || Minecraft.gameSettings.keyBindBack.pressed || Minecraft.gameSettings.keyBindLeft.pressed;
   }
}
