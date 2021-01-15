/*    */ package net.minecraft.server.network;
/*    */ 
/*    */ import net.minecraft.network.NetworkManager;
/*    */ import net.minecraft.network.status.INetHandlerStatusServer;
/*    */ import net.minecraft.network.status.client.C00PacketServerQuery;
/*    */ import net.minecraft.network.status.client.C01PacketPing;
/*    */ import net.minecraft.network.status.server.S00PacketServerInfo;
/*    */ import net.minecraft.network.status.server.S01PacketPong;
/*    */ import net.minecraft.server.MinecraftServer;
/*    */ import net.minecraft.util.ChatComponentText;
/*    */ import net.minecraft.util.IChatComponent;
/*    */ 
/*    */ public class NetHandlerStatusServer implements INetHandlerStatusServer
/*    */ {
/* 15 */   private static final IChatComponent field_183007_a = new ChatComponentText("Status request has been handled.");
/*    */   private final MinecraftServer server;
/*    */   private final NetworkManager networkManager;
/*    */   private boolean field_183008_d;
/*    */   
/*    */   public NetHandlerStatusServer(MinecraftServer serverIn, NetworkManager netManager)
/*    */   {
/* 22 */     this.server = serverIn;
/* 23 */     this.networkManager = netManager;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void onDisconnect(IChatComponent reason) {}
/*    */   
/*    */ 
/*    */ 
/*    */   public void processServerQuery(C00PacketServerQuery packetIn)
/*    */   {
/* 35 */     if (this.field_183008_d)
/*    */     {
/* 37 */       this.networkManager.closeChannel(field_183007_a);
/*    */     }
/*    */     else
/*    */     {
/* 41 */       this.field_183008_d = true;
/* 42 */       this.networkManager.sendPacket(new S00PacketServerInfo(this.server.getServerStatusResponse()));
/*    */     }
/*    */   }
/*    */   
/*    */   public void processPing(C01PacketPing packetIn)
/*    */   {
/* 48 */     this.networkManager.sendPacket(new S01PacketPong(packetIn.getClientTime()));
/* 49 */     this.networkManager.closeChannel(field_183007_a);
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\server\network\NetHandlerStatusServer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */