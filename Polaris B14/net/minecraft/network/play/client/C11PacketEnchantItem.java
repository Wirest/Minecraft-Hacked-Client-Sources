/*    */ package net.minecraft.network.play.client;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import net.minecraft.network.Packet;
/*    */ import net.minecraft.network.PacketBuffer;
/*    */ import net.minecraft.network.play.INetHandlerPlayServer;
/*    */ 
/*    */ 
/*    */ public class C11PacketEnchantItem
/*    */   implements Packet<INetHandlerPlayServer>
/*    */ {
/*    */   private int windowId;
/*    */   private int button;
/*    */   
/*    */   public C11PacketEnchantItem() {}
/*    */   
/*    */   public C11PacketEnchantItem(int windowId, int button)
/*    */   {
/* 19 */     this.windowId = windowId;
/* 20 */     this.button = button;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void processPacket(INetHandlerPlayServer handler)
/*    */   {
/* 28 */     handler.processEnchantItem(this);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void readPacketData(PacketBuffer buf)
/*    */     throws IOException
/*    */   {
/* 36 */     this.windowId = buf.readByte();
/* 37 */     this.button = buf.readByte();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void writePacketData(PacketBuffer buf)
/*    */     throws IOException
/*    */   {
/* 45 */     buf.writeByte(this.windowId);
/* 46 */     buf.writeByte(this.button);
/*    */   }
/*    */   
/*    */   public int getWindowId()
/*    */   {
/* 51 */     return this.windowId;
/*    */   }
/*    */   
/*    */   public int getButton()
/*    */   {
/* 56 */     return this.button;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\network\play\client\C11PacketEnchantItem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */