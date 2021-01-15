/*    */ package net.minecraft.network.play.client;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import net.minecraft.network.Packet;
/*    */ import net.minecraft.network.PacketBuffer;
/*    */ import net.minecraft.network.play.INetHandlerPlayServer;
/*    */ 
/*    */ 
/*    */ public class C0FPacketConfirmTransaction
/*    */   implements Packet<INetHandlerPlayServer>
/*    */ {
/*    */   private int windowId;
/*    */   private short uid;
/*    */   private boolean accepted;
/*    */   
/*    */   public C0FPacketConfirmTransaction() {}
/*    */   
/*    */   public C0FPacketConfirmTransaction(int windowId, short uid, boolean accepted)
/*    */   {
/* 20 */     this.windowId = windowId;
/* 21 */     this.uid = uid;
/* 22 */     this.accepted = accepted;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void processPacket(INetHandlerPlayServer handler)
/*    */   {
/* 30 */     handler.processConfirmTransaction(this);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void readPacketData(PacketBuffer buf)
/*    */     throws IOException
/*    */   {
/* 38 */     this.windowId = buf.readByte();
/* 39 */     this.uid = buf.readShort();
/* 40 */     this.accepted = (buf.readByte() != 0);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void writePacketData(PacketBuffer buf)
/*    */     throws IOException
/*    */   {
/* 48 */     buf.writeByte(this.windowId);
/* 49 */     buf.writeShort(this.uid);
/* 50 */     buf.writeByte(this.accepted ? 1 : 0);
/*    */   }
/*    */   
/*    */   public int getWindowId()
/*    */   {
/* 55 */     return this.windowId;
/*    */   }
/*    */   
/*    */   public short getUid()
/*    */   {
/* 60 */     return this.uid;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\network\play\client\C0FPacketConfirmTransaction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */