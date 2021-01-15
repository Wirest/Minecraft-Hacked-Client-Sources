package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class S2EPacketCloseWindow implements Packet {
   private int field_148896_a;
   private static final String __OBFID = "CL_00001292";

   public S2EPacketCloseWindow() {
   }

   public S2EPacketCloseWindow(int p_i45183_1_) {
      this.field_148896_a = p_i45183_1_;
   }

   public void func_180731_a(INetHandlerPlayClient p_180731_1_) {
      p_180731_1_.handleCloseWindow(this);
   }

   public void readPacketData(PacketBuffer data) throws IOException {
      this.field_148896_a = data.readUnsignedByte();
   }

   public void writePacketData(PacketBuffer data) throws IOException {
      data.writeByte(this.field_148896_a);
   }

   public void processPacket(INetHandler handler) {
      this.func_180731_a((INetHandlerPlayClient)handler);
   }
}
