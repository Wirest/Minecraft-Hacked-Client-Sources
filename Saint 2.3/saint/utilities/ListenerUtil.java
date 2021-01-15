package saint.utilities;

import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.network.play.server.S3FPacketCustomPayload;
import saint.Saint;
import saint.eventstuff.Event;
import saint.eventstuff.Listener;
import saint.eventstuff.events.EveryTick;
import saint.eventstuff.events.PacketSent;
import saint.eventstuff.events.RecievePacket;

public class ListenerUtil implements Listener {
   private Minecraft mc = Minecraft.getMinecraft();
   public static PositionData serverPos = new PositionData();
   public static String serverBrand = "Server";

   public ListenerUtil() {
      Saint.getEventManager().addListener(this);
      if (this.mc.thePlayer != null) {
         serverPos.posX = this.mc.thePlayer.posX;
         serverPos.posY = this.mc.thePlayer.posY;
         serverPos.posZ = this.mc.thePlayer.posZ;
         serverPos.rotationYaw = this.mc.thePlayer.rotationYaw;
         serverPos.rotationPitch = this.mc.thePlayer.rotationPitch;
      }

   }

   public void onEvent(Event event) {
      if (event instanceof RecievePacket) {
         RecievePacket rec = (RecievePacket)event;
         if (rec.getPacket() instanceof S08PacketPlayerPosLook) {
            S08PacketPlayerPosLook packet = (S08PacketPlayerPosLook)rec.getPacket();
            serverPos.posX = packet.func_148932_c();
            serverPos.posY = packet.func_148928_d();
            serverPos.posZ = packet.func_148933_e();
            serverPos.rotationYaw = packet.func_148931_f();
            serverPos.rotationPitch = packet.func_148930_g();
         }

         if (rec.getPacket() instanceof S3FPacketCustomPayload) {
            S3FPacketCustomPayload packet = (S3FPacketCustomPayload)rec.getPacket();
            if (packet.getChannelName().equals("MC|Brand")) {
               try {
                  serverBrand = packet.getBufferData().readStringFromBuffer(packet.getBufferData().readableBytes());
               } catch (Exception var5) {
                  var5.printStackTrace();
               }
            }
         }
      } else if (event instanceof PacketSent) {
         PacketSent sent = (PacketSent)event;
         C03PacketPlayer prepacket;
         if (sent.getPacket() instanceof C03PacketPlayer) {
            prepacket = (C03PacketPlayer)sent.getPacket();
            C03PacketPlayer packet = new C03PacketPlayer.C06PacketPlayerPosLook(prepacket.field_149480_h ? prepacket.x : serverPos.posX, prepacket.field_149480_h ? prepacket.y : serverPos.posY, prepacket.field_149480_h ? prepacket.z : serverPos.posZ, prepacket.rotating ? prepacket.getYaw() : serverPos.rotationYaw, prepacket.rotating ? prepacket.getPitch() : serverPos.rotationPitch, prepacket.field_149474_g);
            packet.rotating = prepacket.rotating;
            packet.field_149480_h = prepacket.field_149480_h;
            sent.setPacket(packet);
         }

         if (sent.getPacket() instanceof C03PacketPlayer) {
            prepacket = (C03PacketPlayer)sent.getPacket();
            if (prepacket.rotating) {
               serverPos.rotationYaw = prepacket.getYaw();
               serverPos.rotationPitch = prepacket.getPitch();
            }

            if (prepacket.field_149480_h) {
               serverPos.posX = prepacket.getPositionX();
               serverPos.posY = prepacket.getPositionY();
               serverPos.posZ = prepacket.getPositionZ();
            }
         }
      } else if (event instanceof EveryTick && this.mc.theWorld != null) {
         Saint.getPlayer().stop();
      }

   }
}
