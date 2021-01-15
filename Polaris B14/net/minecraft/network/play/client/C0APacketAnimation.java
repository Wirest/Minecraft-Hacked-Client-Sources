/*    */ package net.minecraft.network.play.client;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import net.minecraft.network.Packet;
/*    */ import net.minecraft.network.PacketBuffer;
/*    */ import net.minecraft.network.play.INetHandlerPlayServer;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class C0APacketAnimation
/*    */   implements Packet<INetHandlerPlayServer>
/*    */ {
/*    */   public void readPacketData(PacketBuffer buf)
/*    */     throws IOException
/*    */   {}
/*    */   
/*    */   public void writePacketData(PacketBuffer buf)
/*    */     throws IOException
/*    */   {}
/*    */   
/*    */   public void processPacket(INetHandlerPlayServer handler)
/*    */   {
/* 29 */     handler.handleAnimation(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\network\play\client\C0APacketAnimation.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */