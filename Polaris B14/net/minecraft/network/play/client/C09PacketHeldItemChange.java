/*    */ package net.minecraft.network.play.client;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import net.minecraft.network.Packet;
/*    */ import net.minecraft.network.PacketBuffer;
/*    */ import net.minecraft.network.play.INetHandlerPlayServer;
/*    */ 
/*    */ 
/*    */ public class C09PacketHeldItemChange
/*    */   implements Packet<INetHandlerPlayServer>
/*    */ {
/*    */   private int slotId;
/*    */   
/*    */   public C09PacketHeldItemChange() {}
/*    */   
/*    */   public C09PacketHeldItemChange(int slotId)
/*    */   {
/* 18 */     this.slotId = slotId;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void readPacketData(PacketBuffer buf)
/*    */     throws IOException
/*    */   {
/* 26 */     this.slotId = buf.readShort();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void writePacketData(PacketBuffer buf)
/*    */     throws IOException
/*    */   {
/* 34 */     buf.writeShort(this.slotId);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void processPacket(INetHandlerPlayServer handler)
/*    */   {
/* 42 */     handler.processHeldItemChange(this);
/*    */   }
/*    */   
/*    */   public int getSlotId()
/*    */   {
/* 47 */     return this.slotId;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\network\play\client\C09PacketHeldItemChange.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */