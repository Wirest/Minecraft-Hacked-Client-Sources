/*    */ package net.minecraft.network.play.server;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import net.minecraft.network.Packet;
/*    */ import net.minecraft.network.PacketBuffer;
/*    */ import net.minecraft.network.play.INetHandlerPlayClient;
/*    */ 
/*    */ 
/*    */ public class S48PacketResourcePackSend
/*    */   implements Packet<INetHandlerPlayClient>
/*    */ {
/*    */   private String url;
/*    */   private String hash;
/*    */   
/*    */   public S48PacketResourcePackSend() {}
/*    */   
/*    */   public S48PacketResourcePackSend(String url, String hash)
/*    */   {
/* 19 */     this.url = url;
/* 20 */     this.hash = hash;
/*    */     
/* 22 */     if (hash.length() > 40)
/*    */     {
/* 24 */       throw new IllegalArgumentException("Hash is too long (max 40, was " + hash.length() + ")");
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void readPacketData(PacketBuffer buf)
/*    */     throws IOException
/*    */   {
/* 33 */     this.url = buf.readStringFromBuffer(32767);
/* 34 */     this.hash = buf.readStringFromBuffer(40);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void writePacketData(PacketBuffer buf)
/*    */     throws IOException
/*    */   {
/* 42 */     buf.writeString(this.url);
/* 43 */     buf.writeString(this.hash);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void processPacket(INetHandlerPlayClient handler)
/*    */   {
/* 51 */     handler.handleResourcePack(this);
/*    */   }
/*    */   
/*    */   public String getURL()
/*    */   {
/* 56 */     return this.url;
/*    */   }
/*    */   
/*    */   public String getHash()
/*    */   {
/* 61 */     return this.hash;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\network\play\server\S48PacketResourcePackSend.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */