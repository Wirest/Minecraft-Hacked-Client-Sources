/*    */ package net.minecraft.network.play.server;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import net.minecraft.item.ItemStack;
/*    */ import net.minecraft.network.Packet;
/*    */ import net.minecraft.network.PacketBuffer;
/*    */ import net.minecraft.network.play.INetHandlerPlayClient;
/*    */ 
/*    */ 
/*    */ public class S04PacketEntityEquipment
/*    */   implements Packet<INetHandlerPlayClient>
/*    */ {
/*    */   private int entityID;
/*    */   private int equipmentSlot;
/*    */   private ItemStack itemStack;
/*    */   
/*    */   public S04PacketEntityEquipment() {}
/*    */   
/*    */   public S04PacketEntityEquipment(int entityIDIn, int p_i45221_2_, ItemStack itemStackIn)
/*    */   {
/* 21 */     this.entityID = entityIDIn;
/* 22 */     this.equipmentSlot = p_i45221_2_;
/* 23 */     this.itemStack = (itemStackIn == null ? null : itemStackIn.copy());
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void readPacketData(PacketBuffer buf)
/*    */     throws IOException
/*    */   {
/* 31 */     this.entityID = buf.readVarIntFromBuffer();
/* 32 */     this.equipmentSlot = buf.readShort();
/* 33 */     this.itemStack = buf.readItemStackFromBuffer();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void writePacketData(PacketBuffer buf)
/*    */     throws IOException
/*    */   {
/* 41 */     buf.writeVarIntToBuffer(this.entityID);
/* 42 */     buf.writeShort(this.equipmentSlot);
/* 43 */     buf.writeItemStackToBuffer(this.itemStack);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void processPacket(INetHandlerPlayClient handler)
/*    */   {
/* 51 */     handler.handleEntityEquipment(this);
/*    */   }
/*    */   
/*    */   public ItemStack getItemStack()
/*    */   {
/* 56 */     return this.itemStack;
/*    */   }
/*    */   
/*    */   public int getEntityID()
/*    */   {
/* 61 */     return this.entityID;
/*    */   }
/*    */   
/*    */   public int getEquipmentSlot()
/*    */   {
/* 66 */     return this.equipmentSlot;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\network\play\server\S04PacketEntityEquipment.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */