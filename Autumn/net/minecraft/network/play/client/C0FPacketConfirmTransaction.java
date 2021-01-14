package net.minecraft.network.play.client;

import java.io.IOException;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;

public class C0FPacketConfirmTransaction implements Packet {
   private int windowId;
   private short uid;
   private boolean accepted;

   public C0FPacketConfirmTransaction() {
   }

   public C0FPacketConfirmTransaction(int windowId, short uid, boolean accepted) {
      this.windowId = windowId;
      this.uid = uid;
      this.accepted = accepted;
   }

   public void processPacket(INetHandlerPlayServer handler) {
      handler.processConfirmTransaction(this);
   }

   public void readPacketData(PacketBuffer buf) throws IOException {
      this.windowId = buf.readByte();
      this.uid = buf.readShort();
      this.accepted = buf.readByte() != 0;
   }

   public void writePacketData(PacketBuffer buf) throws IOException {
      buf.writeByte(this.windowId);
      buf.writeShort(this.uid);
      buf.writeByte(this.accepted ? 1 : 0);
   }

   public int getWindowId() {
      return this.windowId;
   }

   public short getUid() {
      return this.uid;
   }
}
