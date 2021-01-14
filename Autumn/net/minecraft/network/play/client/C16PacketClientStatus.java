package net.minecraft.network.play.client;

import java.io.IOException;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;

public class C16PacketClientStatus implements Packet {
   private C16PacketClientStatus.EnumState status;

   public C16PacketClientStatus() {
   }

   public C16PacketClientStatus(C16PacketClientStatus.EnumState statusIn) {
      this.status = statusIn;
   }

   public void readPacketData(PacketBuffer buf) throws IOException {
      this.status = (C16PacketClientStatus.EnumState)buf.readEnumValue(C16PacketClientStatus.EnumState.class);
   }

   public void writePacketData(PacketBuffer buf) throws IOException {
      buf.writeEnumValue(this.status);
   }

   public void processPacket(INetHandlerPlayServer handler) {
      handler.processClientStatus(this);
   }

   public C16PacketClientStatus.EnumState getStatus() {
      return this.status;
   }

   public static enum EnumState {
      PERFORM_RESPAWN,
      REQUEST_STATS,
      OPEN_INVENTORY_ACHIEVEMENT;
   }
}
