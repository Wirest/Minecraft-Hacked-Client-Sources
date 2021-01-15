package net.minecraft.network.play.client;

import java.io.IOException;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;

public class C0DPacketCloseWindow implements Packet {
   private int windowId;
   private static final String __OBFID = "CL_00001354";

   public C0DPacketCloseWindow() {
   }

   public C0DPacketCloseWindow(int p_i45247_1_) {
      this.windowId = p_i45247_1_;
   }

   public void func_180759_a(INetHandlerPlayServer p_180759_1_) {
      p_180759_1_.processCloseWindow(this);
   }

   public void readPacketData(PacketBuffer data) throws IOException {
      this.windowId = data.readByte();
   }

   public void writePacketData(PacketBuffer data) throws IOException {
      data.writeByte(this.windowId);
   }

   public void processPacket(INetHandler handler) {
      this.func_180759_a((INetHandlerPlayServer)handler);
   }
}
