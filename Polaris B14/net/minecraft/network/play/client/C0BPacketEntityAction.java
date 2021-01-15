/*    */ package net.minecraft.network.play.client;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.network.Packet;
/*    */ import net.minecraft.network.PacketBuffer;
/*    */ import net.minecraft.network.play.INetHandlerPlayServer;
/*    */ 
/*    */ 
/*    */ public class C0BPacketEntityAction
/*    */   implements Packet<INetHandlerPlayServer>
/*    */ {
/*    */   private int entityID;
/*    */   private Action action;
/*    */   private int auxData;
/*    */   
/*    */   public C0BPacketEntityAction() {}
/*    */   
/*    */   public C0BPacketEntityAction(Entity entity, Action action)
/*    */   {
/* 21 */     this(entity, action, 0);
/*    */   }
/*    */   
/*    */   public C0BPacketEntityAction(Entity entity, Action action, int auxData)
/*    */   {
/* 26 */     this.entityID = entity.getEntityId();
/* 27 */     this.action = action;
/* 28 */     this.auxData = auxData;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void readPacketData(PacketBuffer buf)
/*    */     throws IOException
/*    */   {
/* 36 */     this.entityID = buf.readVarIntFromBuffer();
/* 37 */     this.action = ((Action)buf.readEnumValue(Action.class));
/* 38 */     this.auxData = buf.readVarIntFromBuffer();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void writePacketData(PacketBuffer buf)
/*    */     throws IOException
/*    */   {
/* 46 */     buf.writeVarIntToBuffer(this.entityID);
/* 47 */     buf.writeEnumValue(this.action);
/* 48 */     buf.writeVarIntToBuffer(this.auxData);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void processPacket(INetHandlerPlayServer handler)
/*    */   {
/* 56 */     handler.processEntityAction(this);
/*    */   }
/*    */   
/*    */   public Action getAction()
/*    */   {
/* 61 */     return this.action;
/*    */   }
/*    */   
/*    */   public int getAuxData()
/*    */   {
/* 66 */     return this.auxData;
/*    */   }
/*    */   
/*    */   public static enum Action
/*    */   {
/* 71 */     START_SNEAKING, 
/* 72 */     STOP_SNEAKING, 
/* 73 */     STOP_SLEEPING, 
/* 74 */     START_SPRINTING, 
/* 75 */     STOP_SPRINTING, 
/* 76 */     RIDING_JUMP, 
/* 77 */     OPEN_INVENTORY;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\network\play\client\C0BPacketEntityAction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */