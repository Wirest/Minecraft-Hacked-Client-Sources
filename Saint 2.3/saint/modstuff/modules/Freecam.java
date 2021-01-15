package saint.modstuff.modules;

import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.network.play.client.C03PacketPlayer;
import org.lwjgl.input.Keyboard;
import saint.eventstuff.Event;
import saint.eventstuff.events.EntityUpdates;
import saint.eventstuff.events.InsideBlock;
import saint.eventstuff.events.PacketSent;
import saint.eventstuff.events.PlayerMovement;
import saint.eventstuff.events.PreMotion;
import saint.eventstuff.events.PushOut;
import saint.modstuff.ModManager;
import saint.modstuff.Module;

public class Freecam extends Module {
   private double x;
   private double y;
   private double minY;
   private double z;
   private float yaw;
   private float pitch;

   public Freecam() {
      super("Freecam", -5192482, ModManager.Category.WORLD);
   }

   public void onDisabled() {
      super.onDisabled();
      if (mc.thePlayer != null) {
         mc.thePlayer.noClip = false;
         mc.theWorld.removeEntityFromWorld(-1);
         mc.thePlayer.setPositionAndRotation(this.x, this.y, this.z, this.yaw, this.pitch);
      }

   }

   public void onEnabled() {
      super.onEnabled();
      if (mc.thePlayer != null) {
         this.x = mc.thePlayer.posX;
         this.y = mc.thePlayer.posY;
         this.minY = mc.thePlayer.boundingBox.minY;
         this.z = mc.thePlayer.posZ;
         this.yaw = mc.thePlayer.rotationYaw;
         this.pitch = mc.thePlayer.rotationPitch;
         EntityOtherPlayerMP ent = new EntityOtherPlayerMP(mc.theWorld, mc.thePlayer.getGameProfile());
         ent.inventory = mc.thePlayer.inventory;
         ent.inventoryContainer = mc.thePlayer.inventoryContainer;
         ent.setPositionAndRotation(this.x, this.minY, this.z, this.yaw, this.pitch);
         ent.rotationYawHead = mc.thePlayer.rotationYawHead;
         mc.theWorld.addEntityToWorld(-1, ent);
         mc.thePlayer.noClip = true;
      }

   }

   public void onEvent(Event event) {
      if (event instanceof PlayerMovement) {
         PlayerMovement movement = (PlayerMovement)event;
         double speed = 4.0D;
         if (Keyboard.isKeyDown(29)) {
            speed /= 2.0D;
         }

         movement.setY(0.0D);
         mc.thePlayer.motionY = 0.0D;
         if (!mc.inGameHasFocus) {
            return;
         }

         if (mc.thePlayer.movementInput.moveForward != 0.0F || mc.thePlayer.movementInput.moveStrafe != 0.0F) {
            movement.setX(movement.getX() * speed);
            movement.setZ(movement.getZ() * speed);
         }

         if (Keyboard.isKeyDown(mc.gameSettings.keyBindJump.getKeyCode())) {
            movement.setY(speed / 4.0D);
         } else if (Keyboard.isKeyDown(mc.gameSettings.keyBindSneak.getKeyCode())) {
            movement.setY(-(speed / 4.0D));
         }
      } else if (event instanceof InsideBlock) {
         ((InsideBlock)event).setCancelled(true);
      } else if (event instanceof PushOut) {
         ((PushOut)event).setCancelled(true);
      } else if (event instanceof PreMotion) {
         EntityPlayerSP var10000 = mc.thePlayer;
         var10000.renderArmPitch += 400.0F;
      } else if (event instanceof EntityUpdates && mc.thePlayer != null) {
         mc.thePlayer.noClip = true;
      } else if (event instanceof PacketSent) {
         PacketSent sent = (PacketSent)event;
         if (sent.getPacket() instanceof C03PacketPlayer) {
            C03PacketPlayer player = (C03PacketPlayer)sent.getPacket();
            player.x = this.x;
            player.y = this.y;
            player.z = this.z;
            player.yaw = this.yaw;
            player.pitch = this.pitch;
         }
      }

   }
}
