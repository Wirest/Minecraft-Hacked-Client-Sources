package me.razerboy420.weepcraft.module.modules.misc;

import java.util.ArrayList;
import java.util.Iterator;

import darkmagician6.EventTarget;
import darkmagician6.events.EventPacketSent;
import darkmagician6.events.EventPreMotionUpdates;
import me.razerboy420.weepcraft.module.Module;
import me.razerboy420.weepcraft.util.Wrapper;
import me.razerboy420.weepcraft.value.Value;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraft.network.play.client.CPacketClientStatus;
import net.minecraft.network.play.client.CPacketKeepAlive;
import net.minecraft.network.play.client.CPacketPlayer;

@Module.Mod(
   category = Module.Category.MISC,
   description = "Look like you are lagging to everyone else",
   key = 0,
   name = "FakeLag"
)
public class FakeLag extends Module {

   public Value packetcount = new Value("fakelag_Packet Count", Float.valueOf(10.0F), Float.valueOf(1.0F), Float.valueOf(30.0F), Float.valueOf(1.0F));
   public ArrayList packetlist = new ArrayList();
   public int packets = 0;


   @EventTarget(4)
   public void onPacket(EventPacketSent event) {
      if(Wrapper.getPlayer() != null && !(event.getPacket() instanceof CPacketKeepAlive) && !(event.getPacket() instanceof CPacketChatMessage) && !(event.getPacket() instanceof CPacketClientStatus) && (float)this.packets < this.packetcount.value.floatValue()) {
         this.packetlist.add(event.getPacket());
         if(event.getPacket() instanceof CPacketPlayer) {
            ++this.packets;
         }

         event.setCancelled(true);
      }

   }

   public void onDisable() {
      super.onDisable();
      this.packetlist.clear();
      this.packets = 0;
   }

   @EventTarget
   public void onEvent(EventPreMotionUpdates event) {
      this.setDisplayName("FakeLag [" + this.packets + "/" + this.packetcount.value + "]");
      if((float)this.packets >= this.packetcount.value.floatValue()) {
         Iterator var3 = this.packetlist.iterator();

         while(var3.hasNext()) {
            Packet p = (Packet)var3.next();
            Wrapper.sendPacket(p);
         }

         this.packetlist.clear();
         this.packets = 0;
      }

   }
}
