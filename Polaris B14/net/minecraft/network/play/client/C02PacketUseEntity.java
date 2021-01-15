/*    */ package net.minecraft.network.play.client;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.network.Packet;
/*    */ import net.minecraft.network.PacketBuffer;
/*    */ import net.minecraft.network.play.INetHandlerPlayServer;
/*    */ import net.minecraft.util.Vec3;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ 
/*    */ public class C02PacketUseEntity
/*    */   implements Packet<INetHandlerPlayServer>
/*    */ {
/*    */   private int entityId;
/*    */   private Action action;
/*    */   private Vec3 hitVec;
/*    */   
/*    */   public C02PacketUseEntity() {}
/*    */   
/*    */   public C02PacketUseEntity(Entity entity, Action action)
/*    */   {
/* 23 */     this.entityId = entity.getEntityId();
/* 24 */     this.action = action;
/*    */   }
/*    */   
/*    */   public C02PacketUseEntity(Entity entity, Vec3 hitVec)
/*    */   {
/* 29 */     this(entity, Action.INTERACT_AT);
/* 30 */     this.hitVec = hitVec;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void readPacketData(PacketBuffer buf)
/*    */     throws IOException
/*    */   {
/* 38 */     this.entityId = buf.readVarIntFromBuffer();
/* 39 */     this.action = ((Action)buf.readEnumValue(Action.class));
/*    */     
/* 41 */     if (this.action == Action.INTERACT_AT)
/*    */     {
/* 43 */       this.hitVec = new Vec3(buf.readFloat(), buf.readFloat(), buf.readFloat());
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void writePacketData(PacketBuffer buf)
/*    */     throws IOException
/*    */   {
/* 52 */     buf.writeVarIntToBuffer(this.entityId);
/* 53 */     buf.writeEnumValue(this.action);
/*    */     
/* 55 */     if (this.action == Action.INTERACT_AT)
/*    */     {
/* 57 */       buf.writeFloat((float)this.hitVec.xCoord);
/* 58 */       buf.writeFloat((float)this.hitVec.yCoord);
/* 59 */       buf.writeFloat((float)this.hitVec.zCoord);
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void processPacket(INetHandlerPlayServer handler)
/*    */   {
/* 68 */     handler.processUseEntity(this);
/*    */   }
/*    */   
/*    */   public Entity getEntityFromWorld(World worldIn)
/*    */   {
/* 73 */     return worldIn.getEntityByID(this.entityId);
/*    */   }
/*    */   
/*    */   public Action getAction()
/*    */   {
/* 78 */     return this.action;
/*    */   }
/*    */   
/*    */   public Vec3 getHitVec()
/*    */   {
/* 83 */     return this.hitVec;
/*    */   }
/*    */   
/*    */   public static enum Action
/*    */   {
/* 88 */     INTERACT, 
/* 89 */     ATTACK, 
/* 90 */     INTERACT_AT;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\network\play\client\C02PacketUseEntity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */