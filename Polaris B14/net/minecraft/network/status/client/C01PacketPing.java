/*    */ package net.minecraft.network.status.client;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import net.minecraft.network.Packet;
/*    */ import net.minecraft.network.PacketBuffer;
/*    */ import net.minecraft.network.status.INetHandlerStatusServer;
/*    */ 
/*    */ 
/*    */ public class C01PacketPing
/*    */   implements Packet<INetHandlerStatusServer>
/*    */ {
/*    */   private long clientTime;
/*    */   
/*    */   public C01PacketPing() {}
/*    */   
/*    */   public C01PacketPing(long ping)
/*    */   {
/* 18 */     this.clientTime = ping;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void readPacketData(PacketBuffer buf)
/*    */     throws IOException
/*    */   {
/* 26 */     this.clientTime = buf.readLong();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void writePacketData(PacketBuffer buf)
/*    */     throws IOException
/*    */   {
/* 34 */     buf.writeLong(this.clientTime);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void processPacket(INetHandlerStatusServer handler)
/*    */   {
/* 42 */     handler.processPing(this);
/*    */   }
/*    */   
/*    */   public long getClientTime()
/*    */   {
/* 47 */     return this.clientTime;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\network\status\client\C01PacketPing.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */