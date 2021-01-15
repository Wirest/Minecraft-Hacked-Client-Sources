/*    */ package net.minecraft.network.status.server;
/*    */ 
/*    */ import com.google.gson.Gson;
/*    */ import com.google.gson.GsonBuilder;
/*    */ import java.io.IOException;
/*    */ import net.minecraft.network.PacketBuffer;
/*    */ import net.minecraft.network.ServerStatusResponse;
/*    */ import net.minecraft.network.ServerStatusResponse.MinecraftProtocolVersionIdentifier;
/*    */ import net.minecraft.network.ServerStatusResponse.MinecraftProtocolVersionIdentifier.Serializer;
/*    */ import net.minecraft.network.status.INetHandlerStatusClient;
/*    */ import net.minecraft.util.IChatComponent;
/*    */ import net.minecraft.util.IChatComponent.Serializer;
/*    */ 
/*    */ public class S00PacketServerInfo implements net.minecraft.network.Packet<INetHandlerStatusClient>
/*    */ {
/* 16 */   private static final Gson GSON = new GsonBuilder().registerTypeAdapter(ServerStatusResponse.MinecraftProtocolVersionIdentifier.class, new ServerStatusResponse.MinecraftProtocolVersionIdentifier.Serializer()).registerTypeAdapter(net.minecraft.network.ServerStatusResponse.PlayerCountData.class, new net.minecraft.network.ServerStatusResponse.PlayerCountData.Serializer()).registerTypeAdapter(ServerStatusResponse.class, new net.minecraft.network.ServerStatusResponse.Serializer()).registerTypeHierarchyAdapter(IChatComponent.class, new IChatComponent.Serializer()).registerTypeHierarchyAdapter(net.minecraft.util.ChatStyle.class, new net.minecraft.util.ChatStyle.Serializer()).registerTypeAdapterFactory(new net.minecraft.util.EnumTypeAdapterFactory()).create();
/*    */   
/*    */   private ServerStatusResponse response;
/*    */   
/*    */ 
/*    */   public S00PacketServerInfo() {}
/*    */   
/*    */   public S00PacketServerInfo(ServerStatusResponse responseIn)
/*    */   {
/* 25 */     this.response = responseIn;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void readPacketData(PacketBuffer buf)
/*    */     throws IOException
/*    */   {
/* 33 */     this.response = ((ServerStatusResponse)GSON.fromJson(buf.readStringFromBuffer(32767), ServerStatusResponse.class));
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void writePacketData(PacketBuffer buf)
/*    */     throws IOException
/*    */   {
/* 41 */     buf.writeString(GSON.toJson(this.response));
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void processPacket(INetHandlerStatusClient handler)
/*    */   {
/* 49 */     handler.handleServerInfo(this);
/*    */   }
/*    */   
/*    */   public ServerStatusResponse getResponse()
/*    */   {
/* 54 */     return this.response;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\network\status\server\S00PacketServerInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */