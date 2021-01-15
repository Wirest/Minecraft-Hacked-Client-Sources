/*    */ package net.minecraft.network.play.client;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import net.minecraft.network.Packet;
/*    */ import net.minecraft.network.PacketBuffer;
/*    */ import net.minecraft.network.play.INetHandlerPlayServer;
/*    */ 
/*    */ 
/*    */ public class C19PacketResourcePackStatus
/*    */   implements Packet<INetHandlerPlayServer>
/*    */ {
/*    */   private String hash;
/*    */   private Action status;
/*    */   
/*    */   public C19PacketResourcePackStatus() {}
/*    */   
/*    */   public C19PacketResourcePackStatus(String hashIn, Action statusIn)
/*    */   {
/* 19 */     if (hashIn.length() > 40)
/*    */     {
/* 21 */       hashIn = hashIn.substring(0, 40);
/*    */     }
/*    */     
/* 24 */     this.hash = hashIn;
/* 25 */     this.status = statusIn;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void readPacketData(PacketBuffer buf)
/*    */     throws IOException
/*    */   {
/* 33 */     this.hash = buf.readStringFromBuffer(40);
/* 34 */     this.status = ((Action)buf.readEnumValue(Action.class));
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void writePacketData(PacketBuffer buf)
/*    */     throws IOException
/*    */   {
/* 42 */     buf.writeString(this.hash);
/* 43 */     buf.writeEnumValue(this.status);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void processPacket(INetHandlerPlayServer handler)
/*    */   {
/* 51 */     handler.handleResourcePackStatus(this);
/*    */   }
/*    */   
/*    */   public static enum Action
/*    */   {
/* 56 */     SUCCESSFULLY_LOADED, 
/* 57 */     DECLINED, 
/* 58 */     FAILED_DOWNLOAD, 
/* 59 */     ACCEPTED;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\network\play\client\C19PacketResourcePackStatus.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */