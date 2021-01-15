/*    */ package net.minecraft.network.login.server;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import net.minecraft.network.Packet;
/*    */ import net.minecraft.network.PacketBuffer;
/*    */ import net.minecraft.network.login.INetHandlerLoginClient;
/*    */ import net.minecraft.util.IChatComponent;
/*    */ 
/*    */ 
/*    */ public class S00PacketDisconnect
/*    */   implements Packet<INetHandlerLoginClient>
/*    */ {
/*    */   private IChatComponent reason;
/*    */   
/*    */   public S00PacketDisconnect() {}
/*    */   
/*    */   public S00PacketDisconnect(IChatComponent reasonIn)
/*    */   {
/* 19 */     this.reason = reasonIn;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void readPacketData(PacketBuffer buf)
/*    */     throws IOException
/*    */   {
/* 27 */     this.reason = buf.readChatComponent();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void writePacketData(PacketBuffer buf)
/*    */     throws IOException
/*    */   {
/* 35 */     buf.writeChatComponent(this.reason);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void processPacket(INetHandlerLoginClient handler)
/*    */   {
/* 43 */     handler.handleDisconnect(this);
/*    */   }
/*    */   
/*    */   public IChatComponent func_149603_c()
/*    */   {
/* 48 */     return this.reason;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\network\login\server\S00PacketDisconnect.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */