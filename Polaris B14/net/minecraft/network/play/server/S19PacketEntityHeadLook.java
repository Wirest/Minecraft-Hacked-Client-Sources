/*    */ package net.minecraft.network.play.server;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.network.Packet;
/*    */ import net.minecraft.network.PacketBuffer;
/*    */ import net.minecraft.network.play.INetHandlerPlayClient;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ 
/*    */ public class S19PacketEntityHeadLook
/*    */   implements Packet<INetHandlerPlayClient>
/*    */ {
/*    */   private int entityId;
/*    */   private byte yaw;
/*    */   
/*    */   public S19PacketEntityHeadLook() {}
/*    */   
/*    */   public S19PacketEntityHeadLook(Entity entityIn, byte p_i45214_2_)
/*    */   {
/* 21 */     this.entityId = entityIn.getEntityId();
/* 22 */     this.yaw = p_i45214_2_;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void readPacketData(PacketBuffer buf)
/*    */     throws IOException
/*    */   {
/* 30 */     this.entityId = buf.readVarIntFromBuffer();
/* 31 */     this.yaw = buf.readByte();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void writePacketData(PacketBuffer buf)
/*    */     throws IOException
/*    */   {
/* 39 */     buf.writeVarIntToBuffer(this.entityId);
/* 40 */     buf.writeByte(this.yaw);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void processPacket(INetHandlerPlayClient handler)
/*    */   {
/* 48 */     handler.handleEntityHeadLook(this);
/*    */   }
/*    */   
/*    */   public Entity getEntity(World worldIn)
/*    */   {
/* 53 */     return worldIn.getEntityByID(this.entityId);
/*    */   }
/*    */   
/*    */   public byte getYaw()
/*    */   {
/* 58 */     return this.yaw;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\network\play\server\S19PacketEntityHeadLook.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */