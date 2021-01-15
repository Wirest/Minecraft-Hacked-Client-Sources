/*    */ package net.minecraft.network.play.server;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import net.minecraft.network.Packet;
/*    */ import net.minecraft.network.PacketBuffer;
/*    */ import net.minecraft.network.play.INetHandlerPlayClient;
/*    */ 
/*    */ 
/*    */ public class S46PacketSetCompressionLevel
/*    */   implements Packet<INetHandlerPlayClient>
/*    */ {
/*    */   private int field_179761_a;
/*    */   
/*    */   public void readPacketData(PacketBuffer buf)
/*    */     throws IOException
/*    */   {
/* 17 */     this.field_179761_a = buf.readVarIntFromBuffer();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void writePacketData(PacketBuffer buf)
/*    */     throws IOException
/*    */   {
/* 25 */     buf.writeVarIntToBuffer(this.field_179761_a);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void processPacket(INetHandlerPlayClient handler)
/*    */   {
/* 33 */     handler.handleSetCompressionLevel(this);
/*    */   }
/*    */   
/*    */   public int func_179760_a()
/*    */   {
/* 38 */     return this.field_179761_a;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\network\play\server\S46PacketSetCompressionLevel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */