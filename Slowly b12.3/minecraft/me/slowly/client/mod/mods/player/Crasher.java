package me.slowly.client.mod.mods.player;

import com.darkmagician6.eventapi.EventTarget;
import me.slowly.client.events.EventPreMotion;
import me.slowly.client.events.EventSendPacket;
import me.slowly.client.events.EventTick;
import me.slowly.client.events.UpdateEvent;
import me.slowly.client.mod.Mod;
import me.slowly.client.ui.notifiactions.ClientNotification;
import me.slowly.client.util.ClientUtil;
import me.slowly.client.util.Colors;
import net.minecraft.network.play.client.C03PacketPlayer;

public class Crasher extends Mod {
   private boolean speedTick;
   private double yVal;
   double health;
   boolean hasDamaged = false;
   boolean hasJumped = false;
   double posY = 0.0D;

   public Crasher() {
      super("Crasher", Mod.Category.PLAYER, Colors.RED.c);
   }

   @EventTarget
   public void onUpdatePreMotion(UpdateEvent event) {
      if (this.hasJumped) {
         this.mc.thePlayer.motionY = 0.0D;
      }

      for(int i = 0; i < 50; ++i) {
         if (!(this.hasDamaged = (double)((int)this.mc.thePlayer.getHealth()) < this.health)) {
            this.damage();
         }

         this.health = (double)this.mc.thePlayer.getHealth();
         if (!this.hasJumped) {
            this.hasJumped = true;
            this.mc.thePlayer.motionY = 0.3D;
         }
      }

   }

   @EventTarget
   public void onTick(EventTick event) {
      if (this.mc.thePlayer.onGround) {
         this.speedTick = false;
      }

   }

   @EventTarget
   public void onSendPacket(EventSendPacket event) {
   }

   @EventTarget
   public void onPreMovePlayer(EventPreMotion event) {
      if (this.speedTick) {
         event.y *= 1.0E-13D;
         double movementMultiplier = (double)this.mc.thePlayer.capabilities.getFlySpeed() * 20.0D;
         this.mc.thePlayer.fallDistance = 0.0F;
         this.mc.thePlayer.onGround = true;
      }
   }

   private void damage() {
      for(int i = 0; i < 70; ++i) {
         this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY + 0.06D, this.mc.thePlayer.posZ, false));
         this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY, this.mc.thePlayer.posZ, false));
      }

      this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY + 0.1D, this.mc.thePlayer.posZ, false));
   }

   public void onEnable() {
      this.posY = this.mc.thePlayer.posY;

      for(int i = 0; i < 70; ++i) {
         this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY + 0.06D, this.mc.thePlayer.posZ, false));
         this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY, this.mc.thePlayer.posZ, false));
      }

      this.mc.thePlayer.motionY = 0.4D;
      this.hasJumped = false;
      ClientUtil.sendClientMessage("Crasher Enable", ClientNotification.Type.SUCCESS);
   }

   public void onDisable() {
      this.mc.thePlayer.capabilities.allowFlying = false;
      ClientUtil.sendClientMessage("Crasher Disable", ClientNotification.Type.ERROR);
   }
}
