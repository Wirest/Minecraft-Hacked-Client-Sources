/*    */ package net.minecraft.network.play.server;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import net.minecraft.network.Packet;
/*    */ import net.minecraft.network.PacketBuffer;
/*    */ import net.minecraft.network.play.INetHandlerPlayClient;
/*    */ import net.minecraft.util.BlockPos;
/*    */ 
/*    */ 
/*    */ public class S25PacketBlockBreakAnim
/*    */   implements Packet<INetHandlerPlayClient>
/*    */ {
/*    */   private int breakerId;
/*    */   private BlockPos position;
/*    */   private int progress;
/*    */   
/*    */   public S25PacketBlockBreakAnim() {}
/*    */   
/*    */   public S25PacketBlockBreakAnim(int breakerId, BlockPos pos, int progress)
/*    */   {
/* 21 */     this.breakerId = breakerId;
/* 22 */     this.position = pos;
/* 23 */     this.progress = progress;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void readPacketData(PacketBuffer buf)
/*    */     throws IOException
/*    */   {
/* 31 */     this.breakerId = buf.readVarIntFromBuffer();
/* 32 */     this.position = buf.readBlockPos();
/* 33 */     this.progress = buf.readUnsignedByte();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void writePacketData(PacketBuffer buf)
/*    */     throws IOException
/*    */   {
/* 41 */     buf.writeVarIntToBuffer(this.breakerId);
/* 42 */     buf.writeBlockPos(this.position);
/* 43 */     buf.writeByte(this.progress);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void processPacket(INetHandlerPlayClient handler)
/*    */   {
/* 51 */     handler.handleBlockBreakAnim(this);
/*    */   }
/*    */   
/*    */   public int getBreakerId()
/*    */   {
/* 56 */     return this.breakerId;
/*    */   }
/*    */   
/*    */   public BlockPos getPosition()
/*    */   {
/* 61 */     return this.position;
/*    */   }
/*    */   
/*    */   public int getProgress()
/*    */   {
/* 66 */     return this.progress;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\network\play\server\S25PacketBlockBreakAnim.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */