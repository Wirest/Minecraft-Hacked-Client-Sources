package saint.modstuff.modules;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import saint.eventstuff.Event;
import saint.eventstuff.events.PacketSent;
import saint.eventstuff.events.PreMotion;
import saint.modstuff.ModManager;
import saint.modstuff.Module;

public class Blink extends Module {
   private final List packets = new CopyOnWriteArrayList();
   private final List positions = new ArrayList();
   private double[] startingPosition;

   public Blink() {
      super("Blink", -14308402, ModManager.Category.COMBAT);
   }

   public void onDisabled() {
      super.onDisabled();
      if (mc.theWorld != null) {
         mc.theWorld.removeEntityFromWorld(-1);
      }

      Iterator var2 = this.packets.iterator();

      while(var2.hasNext()) {
         Packet packet = (Packet)var2.next();
         mc.getNetHandler().addToSendQueue(packet);
      }

      this.packets.clear();
      this.positions.clear();
      this.setTag("Blink §7" + this.packets.size());
   }

   public void onEnabled() {
      super.onEnabled();
      if (mc.thePlayer != null && mc.theWorld != null) {
         double x = mc.thePlayer.posX;
         double y = mc.thePlayer.posY;
         double z = mc.thePlayer.posZ;
         float yaw = mc.thePlayer.rotationYaw;
         float pitch = mc.thePlayer.rotationPitch;
         EntityOtherPlayerMP ent = new EntityOtherPlayerMP(mc.theWorld, mc.thePlayer.getGameProfile());
         ent.inventory = mc.thePlayer.inventory;
         ent.inventoryContainer = mc.thePlayer.inventoryContainer;
         ent.setPositionAndRotation(x, y, z, yaw, pitch);
         ent.rotationYawHead = mc.thePlayer.rotationYawHead;
         mc.theWorld.addEntityToWorld(-1, ent);
      }

   }

   public void onEvent(Event event) {
      if (event instanceof PacketSent) {
         PacketSent sent = (PacketSent)event;
         if (sent.getPacket() instanceof C03PacketPlayer) {
            if (mc.thePlayer.movementInput.moveForward != 0.0F || mc.gameSettings.keyBindJump.pressed || mc.thePlayer.movementInput.moveStrafe != 0.0F) {
               this.packets.add(sent.getPacket());
               this.setTag("Blink §7" + this.packets.size());
            }

            sent.setCancelled(true);
         } else if (sent.getPacket() instanceof C08PacketPlayerBlockPlacement || sent.getPacket() instanceof C07PacketPlayerDigging || sent.getPacket() instanceof C09PacketHeldItemChange || sent.getPacket() instanceof C02PacketUseEntity) {
            this.packets.add(sent.getPacket());
            this.setTag("Blink §7" + this.packets.size());
            sent.setCancelled(true);
         }
      } else if (event instanceof PreMotion && (mc.thePlayer.movementInput.moveForward != 0.0F || mc.thePlayer.movementInput.moveStrafe != 0.0F)) {
         this.positions.add(new double[]{mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ});
      }

   }
}
