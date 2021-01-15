/*    */ package net.minecraft.network.login.server;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import net.minecraft.network.Packet;
/*    */ import net.minecraft.network.PacketBuffer;
/*    */ import net.minecraft.network.login.INetHandlerLoginClient;
/*    */ 
/*    */ 
/*    */ public class S03PacketEnableCompression
/*    */   implements Packet<INetHandlerLoginClient>
/*    */ {
/*    */   private int compressionTreshold;
/*    */   
/*    */   public S03PacketEnableCompression() {}
/*    */   
/*    */   public S03PacketEnableCompression(int compressionTresholdIn)
/*    */   {
/* 18 */     this.compressionTreshold = compressionTresholdIn;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void readPacketData(PacketBuffer buf)
/*    */     throws IOException
/*    */   {
/* 26 */     this.compressionTreshold = buf.readVarIntFromBuffer();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void writePacketData(PacketBuffer buf)
/*    */     throws IOException
/*    */   {
/* 34 */     buf.writeVarIntToBuffer(this.compressionTreshold);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void processPacket(INetHandlerLoginClient handler)
/*    */   {
/* 42 */     handler.handleEnableCompression(this);
/*    */   }
/*    */   
/*    */   public int getCompressionTreshold()
/*    */   {
/* 47 */     return this.compressionTreshold;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\network\login\server\S03PacketEnableCompression.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */