/*    */ package net.minecraft.network.play.server;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import net.minecraft.network.Packet;
/*    */ import net.minecraft.network.PacketBuffer;
/*    */ import net.minecraft.network.play.INetHandlerPlayClient;
/*    */ 
/*    */ 
/*    */ public class S31PacketWindowProperty
/*    */   implements Packet<INetHandlerPlayClient>
/*    */ {
/*    */   private int windowId;
/*    */   private int varIndex;
/*    */   private int varValue;
/*    */   
/*    */   public S31PacketWindowProperty() {}
/*    */   
/*    */   public S31PacketWindowProperty(int windowIdIn, int varIndexIn, int varValueIn)
/*    */   {
/* 20 */     this.windowId = windowIdIn;
/* 21 */     this.varIndex = varIndexIn;
/* 22 */     this.varValue = varValueIn;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void processPacket(INetHandlerPlayClient handler)
/*    */   {
/* 30 */     handler.handleWindowProperty(this);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void readPacketData(PacketBuffer buf)
/*    */     throws IOException
/*    */   {
/* 38 */     this.windowId = buf.readUnsignedByte();
/* 39 */     this.varIndex = buf.readShort();
/* 40 */     this.varValue = buf.readShort();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void writePacketData(PacketBuffer buf)
/*    */     throws IOException
/*    */   {
/* 48 */     buf.writeByte(this.windowId);
/* 49 */     buf.writeShort(this.varIndex);
/* 50 */     buf.writeShort(this.varValue);
/*    */   }
/*    */   
/*    */   public int getWindowId()
/*    */   {
/* 55 */     return this.windowId;
/*    */   }
/*    */   
/*    */   public int getVarIndex()
/*    */   {
/* 60 */     return this.varIndex;
/*    */   }
/*    */   
/*    */   public int getVarValue()
/*    */   {
/* 65 */     return this.varValue;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\network\play\server\S31PacketWindowProperty.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */