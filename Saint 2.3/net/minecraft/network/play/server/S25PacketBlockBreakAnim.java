package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.BlockPos;

public class S25PacketBlockBreakAnim implements Packet {
   private int breakerId;
   private BlockPos position;
   private int progress;
   private static final String __OBFID = "CL_00001284";

   public S25PacketBlockBreakAnim() {
   }

   public S25PacketBlockBreakAnim(int breakerId, BlockPos pos, int progress) {
      this.breakerId = breakerId;
      this.position = pos;
      this.progress = progress;
   }

   public void readPacketData(PacketBuffer data) throws IOException {
      this.breakerId = data.readVarIntFromBuffer();
      this.position = data.readBlockPos();
      this.progress = data.readUnsignedByte();
   }

   public void writePacketData(PacketBuffer data) throws IOException {
      data.writeVarIntToBuffer(this.breakerId);
      data.writeBlockPos(this.position);
      data.writeByte(this.progress);
   }

   public void handle(INetHandlerPlayClient handler) {
      handler.handleBlockBreakAnim(this);
   }

   public int func_148845_c() {
      return this.breakerId;
   }

   public BlockPos func_179821_b() {
      return this.position;
   }

   public int func_148846_g() {
      return this.progress;
   }

   public void processPacket(INetHandler handler) {
      this.handle((INetHandlerPlayClient)handler);
   }
}
