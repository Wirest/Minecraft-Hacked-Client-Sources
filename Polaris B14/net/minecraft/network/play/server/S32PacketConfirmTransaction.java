/*    */ package net.minecraft.network.play.server;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import net.minecraft.network.Packet;
/*    */ import net.minecraft.network.PacketBuffer;
/*    */ import net.minecraft.network.play.INetHandlerPlayClient;
/*    */ 
/*    */ 
/*    */ public class S32PacketConfirmTransaction
/*    */   implements Packet<INetHandlerPlayClient>
/*    */ {
/*    */   private int windowId;
/*    */   private short actionNumber;
/*    */   private boolean field_148893_c;
/*    */   
/*    */   public S32PacketConfirmTransaction() {}
/*    */   
/*    */   public S32PacketConfirmTransaction(int windowIdIn, short actionNumberIn, boolean p_i45182_3_)
/*    */   {
/* 20 */     this.windowId = windowIdIn;
/* 21 */     this.actionNumber = actionNumberIn;
/* 22 */     this.field_148893_c = p_i45182_3_;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void processPacket(INetHandlerPlayClient handler)
/*    */   {
/* 30 */     handler.handleConfirmTransaction(this);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void readPacketData(PacketBuffer buf)
/*    */     throws IOException
/*    */   {
/* 38 */     this.windowId = buf.readUnsignedByte();
/* 39 */     this.actionNumber = buf.readShort();
/* 40 */     this.field_148893_c = buf.readBoolean();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void writePacketData(PacketBuffer buf)
/*    */     throws IOException
/*    */   {
/* 48 */     buf.writeByte(this.windowId);
/* 49 */     buf.writeShort(this.actionNumber);
/* 50 */     buf.writeBoolean(this.field_148893_c);
/*    */   }
/*    */   
/*    */   public int getWindowId()
/*    */   {
/* 55 */     return this.windowId;
/*    */   }
/*    */   
/*    */   public short getActionNumber()
/*    */   {
/* 60 */     return this.actionNumber;
/*    */   }
/*    */   
/*    */   public boolean func_148888_e()
/*    */   {
/* 65 */     return this.field_148893_c;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\network\play\server\S32PacketConfirmTransaction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */