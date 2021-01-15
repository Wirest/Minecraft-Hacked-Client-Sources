/*    */ package net.minecraft.network.play.client;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import net.minecraft.item.ItemStack;
/*    */ import net.minecraft.network.Packet;
/*    */ import net.minecraft.network.PacketBuffer;
/*    */ import net.minecraft.network.play.INetHandlerPlayServer;
/*    */ 
/*    */ 
/*    */ public class C10PacketCreativeInventoryAction
/*    */   implements Packet<INetHandlerPlayServer>
/*    */ {
/*    */   private int slotId;
/*    */   private ItemStack stack;
/*    */   
/*    */   public C10PacketCreativeInventoryAction() {}
/*    */   
/*    */   public C10PacketCreativeInventoryAction(int slotIdIn, ItemStack stackIn)
/*    */   {
/* 20 */     this.slotId = slotIdIn;
/* 21 */     this.stack = (stackIn != null ? stackIn.copy() : null);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void processPacket(INetHandlerPlayServer handler)
/*    */   {
/* 29 */     handler.processCreativeInventoryAction(this);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void readPacketData(PacketBuffer buf)
/*    */     throws IOException
/*    */   {
/* 37 */     this.slotId = buf.readShort();
/* 38 */     this.stack = buf.readItemStackFromBuffer();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void writePacketData(PacketBuffer buf)
/*    */     throws IOException
/*    */   {
/* 46 */     buf.writeShort(this.slotId);
/* 47 */     buf.writeItemStackToBuffer(this.stack);
/*    */   }
/*    */   
/*    */   public int getSlotId()
/*    */   {
/* 52 */     return this.slotId;
/*    */   }
/*    */   
/*    */   public ItemStack getStack()
/*    */   {
/* 57 */     return this.stack;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\network\play\client\C10PacketCreativeInventoryAction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */