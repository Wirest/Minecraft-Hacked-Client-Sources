/*    */ package net.minecraft.network.login.server;
/*    */ 
/*    */ import com.mojang.authlib.GameProfile;
/*    */ import java.io.IOException;
/*    */ import java.util.UUID;
/*    */ import net.minecraft.network.Packet;
/*    */ import net.minecraft.network.PacketBuffer;
/*    */ import net.minecraft.network.login.INetHandlerLoginClient;
/*    */ 
/*    */ 
/*    */ public class S02PacketLoginSuccess
/*    */   implements Packet<INetHandlerLoginClient>
/*    */ {
/*    */   private GameProfile profile;
/*    */   
/*    */   public S02PacketLoginSuccess() {}
/*    */   
/*    */   public S02PacketLoginSuccess(GameProfile profileIn)
/*    */   {
/* 20 */     this.profile = profileIn;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void readPacketData(PacketBuffer buf)
/*    */     throws IOException
/*    */   {
/* 28 */     String s = buf.readStringFromBuffer(36);
/* 29 */     String s1 = buf.readStringFromBuffer(16);
/* 30 */     UUID uuid = UUID.fromString(s);
/* 31 */     this.profile = new GameProfile(uuid, s1);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void writePacketData(PacketBuffer buf)
/*    */     throws IOException
/*    */   {
/* 39 */     UUID uuid = this.profile.getId();
/* 40 */     buf.writeString(uuid == null ? "" : uuid.toString());
/* 41 */     buf.writeString(this.profile.getName());
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void processPacket(INetHandlerLoginClient handler)
/*    */   {
/* 49 */     handler.handleLoginSuccess(this);
/*    */   }
/*    */   
/*    */   public GameProfile getProfile()
/*    */   {
/* 54 */     return this.profile;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\network\login\server\S02PacketLoginSuccess.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */