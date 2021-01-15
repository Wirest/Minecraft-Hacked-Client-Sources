/*    */ package net.minecraft.network.play.client;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import net.minecraft.network.Packet;
/*    */ import net.minecraft.network.PacketBuffer;
/*    */ import net.minecraft.network.play.INetHandlerPlayServer;
/*    */ 
/*    */ 
/*    */ public class C16PacketClientStatus
/*    */   implements Packet<INetHandlerPlayServer>
/*    */ {
/*    */   private EnumState status;
/*    */   
/*    */   public C16PacketClientStatus() {}
/*    */   
/*    */   public C16PacketClientStatus(EnumState statusIn)
/*    */   {
/* 18 */     this.status = statusIn;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void readPacketData(PacketBuffer buf)
/*    */     throws IOException
/*    */   {
/* 26 */     this.status = ((EnumState)buf.readEnumValue(EnumState.class));
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void writePacketData(PacketBuffer buf)
/*    */     throws IOException
/*    */   {
/* 34 */     buf.writeEnumValue(this.status);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void processPacket(INetHandlerPlayServer handler)
/*    */   {
/* 42 */     handler.processClientStatus(this);
/*    */   }
/*    */   
/*    */   public EnumState getStatus()
/*    */   {
/* 47 */     return this.status;
/*    */   }
/*    */   
/*    */   public static enum EnumState
/*    */   {
/* 52 */     PERFORM_RESPAWN, 
/* 53 */     REQUEST_STATS, 
/* 54 */     OPEN_INVENTORY_ACHIEVEMENT;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\network\play\client\C16PacketClientStatus.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */