/*    */ package net.minecraft.network.play.server;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import net.minecraft.network.Packet;
/*    */ import net.minecraft.network.PacketBuffer;
/*    */ import net.minecraft.network.play.INetHandlerPlayClient;
/*    */ import net.minecraft.potion.PotionEffect;
/*    */ 
/*    */ 
/*    */ public class S1EPacketRemoveEntityEffect
/*    */   implements Packet<INetHandlerPlayClient>
/*    */ {
/*    */   private int entityId;
/*    */   private int effectId;
/*    */   
/*    */   public S1EPacketRemoveEntityEffect() {}
/*    */   
/*    */   public S1EPacketRemoveEntityEffect(int entityIdIn, PotionEffect effect)
/*    */   {
/* 20 */     this.entityId = entityIdIn;
/* 21 */     this.effectId = effect.getPotionID();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void readPacketData(PacketBuffer buf)
/*    */     throws IOException
/*    */   {
/* 29 */     this.entityId = buf.readVarIntFromBuffer();
/* 30 */     this.effectId = buf.readUnsignedByte();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void writePacketData(PacketBuffer buf)
/*    */     throws IOException
/*    */   {
/* 38 */     buf.writeVarIntToBuffer(this.entityId);
/* 39 */     buf.writeByte(this.effectId);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void processPacket(INetHandlerPlayClient handler)
/*    */   {
/* 47 */     handler.handleRemoveEntityEffect(this);
/*    */   }
/*    */   
/*    */   public int getEntityId()
/*    */   {
/* 52 */     return this.entityId;
/*    */   }
/*    */   
/*    */   public int getEffectId()
/*    */   {
/* 57 */     return this.effectId;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\network\play\server\S1EPacketRemoveEntityEffect.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */