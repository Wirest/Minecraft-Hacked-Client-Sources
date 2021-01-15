package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.IChatComponent;

public class S40PacketDisconnect implements Packet {
   private IChatComponent reason;
   private static final String __OBFID = "CL_00001298";

   public S40PacketDisconnect() {
   }

   public S40PacketDisconnect(IChatComponent reasonIn) {
      this.reason = reasonIn;
   }

   public void readPacketData(PacketBuffer data) throws IOException {
      this.reason = data.readChatComponent();
   }

   public void writePacketData(PacketBuffer data) throws IOException {
      data.writeChatComponent(this.reason);
   }

   public void processPacket(INetHandlerPlayClient handler) {
      handler.handleDisconnect(this);
   }

   public IChatComponent func_149165_c() {
      return this.reason;
   }

   public void processPacket(INetHandler handler) {
      this.processPacket((INetHandlerPlayClient)handler);
   }
}
