/*    */ package net.minecraft.network.play.server;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import net.minecraft.network.Packet;
/*    */ import net.minecraft.network.PacketBuffer;
/*    */ import net.minecraft.network.play.INetHandlerPlayClient;
/*    */ 
/*    */ 
/*    */ public class S0DPacketCollectItem
/*    */   implements Packet<INetHandlerPlayClient>
/*    */ {
/*    */   private int collectedItemEntityId;
/*    */   private int entityId;
/*    */   
/*    */   public S0DPacketCollectItem() {}
/*    */   
/*    */   public S0DPacketCollectItem(int collectedItemEntityIdIn, int entityIdIn)
/*    */   {
/* 19 */     this.collectedItemEntityId = collectedItemEntityIdIn;
/* 20 */     this.entityId = entityIdIn;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void readPacketData(PacketBuffer buf)
/*    */     throws IOException
/*    */   {
/* 28 */     this.collectedItemEntityId = buf.readVarIntFromBuffer();
/* 29 */     this.entityId = buf.readVarIntFromBuffer();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void writePacketData(PacketBuffer buf)
/*    */     throws IOException
/*    */   {
/* 37 */     buf.writeVarIntToBuffer(this.collectedItemEntityId);
/* 38 */     buf.writeVarIntToBuffer(this.entityId);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void processPacket(INetHandlerPlayClient handler)
/*    */   {
/* 46 */     handler.handleCollectItem(this);
/*    */   }
/*    */   
/*    */   public int getCollectedItemEntityID()
/*    */   {
/* 51 */     return this.collectedItemEntityId;
/*    */   }
/*    */   
/*    */   public int getEntityID()
/*    */   {
/* 56 */     return this.entityId;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\network\play\server\S0DPacketCollectItem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */