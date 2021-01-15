/*    */ package net.minecraft.network.play.server;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import net.minecraft.item.ItemStack;
/*    */ import net.minecraft.network.Packet;
/*    */ import net.minecraft.network.PacketBuffer;
/*    */ import net.minecraft.network.play.INetHandlerPlayClient;
/*    */ 
/*    */ 
/*    */ public class S2FPacketSetSlot
/*    */   implements Packet<INetHandlerPlayClient>
/*    */ {
/*    */   private int windowId;
/*    */   private int slot;
/*    */   private ItemStack item;
/*    */   
/*    */   public S2FPacketSetSlot() {}
/*    */   
/*    */   public S2FPacketSetSlot(int windowIdIn, int slotIn, ItemStack itemIn)
/*    */   {
/* 21 */     this.windowId = windowIdIn;
/* 22 */     this.slot = slotIn;
/* 23 */     this.item = (itemIn == null ? null : itemIn.copy());
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void processPacket(INetHandlerPlayClient handler)
/*    */   {
/* 31 */     handler.handleSetSlot(this);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void readPacketData(PacketBuffer buf)
/*    */     throws IOException
/*    */   {
/* 39 */     this.windowId = buf.readByte();
/* 40 */     this.slot = buf.readShort();
/* 41 */     this.item = buf.readItemStackFromBuffer();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void writePacketData(PacketBuffer buf)
/*    */     throws IOException
/*    */   {
/* 49 */     buf.writeByte(this.windowId);
/* 50 */     buf.writeShort(this.slot);
/* 51 */     buf.writeItemStackToBuffer(this.item);
/*    */   }
/*    */   
/*    */   public int func_149175_c()
/*    */   {
/* 56 */     return this.windowId;
/*    */   }
/*    */   
/*    */   public int func_149173_d()
/*    */   {
/* 61 */     return this.slot;
/*    */   }
/*    */   
/*    */   public ItemStack func_149174_e()
/*    */   {
/* 66 */     return this.item;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\network\play\server\S2FPacketSetSlot.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */