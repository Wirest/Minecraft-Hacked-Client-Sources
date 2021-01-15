/*    */ package net.minecraft.network.play.server;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import net.minecraft.network.Packet;
/*    */ import net.minecraft.network.PacketBuffer;
/*    */ import net.minecraft.network.play.INetHandlerPlayClient;
/*    */ 
/*    */ 
/*    */ public class S13PacketDestroyEntities
/*    */   implements Packet<INetHandlerPlayClient>
/*    */ {
/*    */   private int[] entityIDs;
/*    */   
/*    */   public S13PacketDestroyEntities() {}
/*    */   
/*    */   public S13PacketDestroyEntities(int... entityIDsIn)
/*    */   {
/* 18 */     this.entityIDs = entityIDsIn;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void readPacketData(PacketBuffer buf)
/*    */     throws IOException
/*    */   {
/* 26 */     this.entityIDs = new int[buf.readVarIntFromBuffer()];
/*    */     
/* 28 */     for (int i = 0; i < this.entityIDs.length; i++)
/*    */     {
/* 30 */       this.entityIDs[i] = buf.readVarIntFromBuffer();
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void writePacketData(PacketBuffer buf)
/*    */     throws IOException
/*    */   {
/* 39 */     buf.writeVarIntToBuffer(this.entityIDs.length);
/*    */     
/* 41 */     for (int i = 0; i < this.entityIDs.length; i++)
/*    */     {
/* 43 */       buf.writeVarIntToBuffer(this.entityIDs[i]);
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void processPacket(INetHandlerPlayClient handler)
/*    */   {
/* 52 */     handler.handleDestroyEntities(this);
/*    */   }
/*    */   
/*    */   public int[] getEntityIDs()
/*    */   {
/* 57 */     return this.entityIDs;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\network\play\server\S13PacketDestroyEntities.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */