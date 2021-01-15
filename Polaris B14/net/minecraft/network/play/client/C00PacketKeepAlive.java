/*    */ package net.minecraft.network.play.client;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import net.minecraft.network.Packet;
/*    */ import net.minecraft.network.PacketBuffer;
/*    */ import net.minecraft.network.play.INetHandlerPlayServer;
/*    */ 
/*    */ 
/*    */ public class C00PacketKeepAlive
/*    */   implements Packet<INetHandlerPlayServer>
/*    */ {
/*    */   private int key;
/*    */   
/*    */   public C00PacketKeepAlive() {}
/*    */   
/*    */   public C00PacketKeepAlive(int key)
/*    */   {
/* 18 */     this.key = key;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void processPacket(INetHandlerPlayServer handler)
/*    */   {
/* 26 */     handler.processKeepAlive(this);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void readPacketData(PacketBuffer buf)
/*    */     throws IOException
/*    */   {
/* 34 */     this.key = buf.readVarIntFromBuffer();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void writePacketData(PacketBuffer buf)
/*    */     throws IOException
/*    */   {
/* 42 */     buf.writeVarIntToBuffer(this.key);
/*    */   }
/*    */   
/*    */   public int getKey()
/*    */   {
/* 47 */     return this.key;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\network\play\client\C00PacketKeepAlive.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */