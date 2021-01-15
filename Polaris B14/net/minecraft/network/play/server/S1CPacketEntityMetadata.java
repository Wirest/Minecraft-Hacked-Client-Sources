/*    */ package net.minecraft.network.play.server;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.util.List;
/*    */ import net.minecraft.entity.DataWatcher;
/*    */ import net.minecraft.entity.DataWatcher.WatchableObject;
/*    */ import net.minecraft.network.Packet;
/*    */ import net.minecraft.network.PacketBuffer;
/*    */ import net.minecraft.network.play.INetHandlerPlayClient;
/*    */ 
/*    */ public class S1CPacketEntityMetadata
/*    */   implements Packet<INetHandlerPlayClient>
/*    */ {
/*    */   private int entityId;
/*    */   private List<DataWatcher.WatchableObject> field_149378_b;
/*    */   
/*    */   public S1CPacketEntityMetadata() {}
/*    */   
/*    */   public S1CPacketEntityMetadata(int entityIdIn, DataWatcher p_i45217_2_, boolean p_i45217_3_)
/*    */   {
/* 21 */     this.entityId = entityIdIn;
/*    */     
/* 23 */     if (p_i45217_3_)
/*    */     {
/* 25 */       this.field_149378_b = p_i45217_2_.getAllWatched();
/*    */     }
/*    */     else
/*    */     {
/* 29 */       this.field_149378_b = p_i45217_2_.getChanged();
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void readPacketData(PacketBuffer buf)
/*    */     throws IOException
/*    */   {
/* 38 */     this.entityId = buf.readVarIntFromBuffer();
/* 39 */     this.field_149378_b = DataWatcher.readWatchedListFromPacketBuffer(buf);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void writePacketData(PacketBuffer buf)
/*    */     throws IOException
/*    */   {
/* 47 */     buf.writeVarIntToBuffer(this.entityId);
/* 48 */     DataWatcher.writeWatchedListToPacketBuffer(this.field_149378_b, buf);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void processPacket(INetHandlerPlayClient handler)
/*    */   {
/* 56 */     handler.handleEntityMetadata(this);
/*    */   }
/*    */   
/*    */   public List<DataWatcher.WatchableObject> func_149376_c()
/*    */   {
/* 61 */     return this.field_149378_b;
/*    */   }
/*    */   
/*    */   public int getEntityId()
/*    */   {
/* 66 */     return this.entityId;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\network\play\server\S1CPacketEntityMetadata.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */