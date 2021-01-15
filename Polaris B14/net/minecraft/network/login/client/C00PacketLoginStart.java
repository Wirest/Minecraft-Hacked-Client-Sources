/*    */ package net.minecraft.network.login.client;
/*    */ 
/*    */ import com.mojang.authlib.GameProfile;
/*    */ import java.io.IOException;
/*    */ import net.minecraft.network.Packet;
/*    */ import net.minecraft.network.PacketBuffer;
/*    */ import net.minecraft.network.login.INetHandlerLoginServer;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class C00PacketLoginStart
/*    */   implements Packet<INetHandlerLoginServer>
/*    */ {
/*    */   private GameProfile profile;
/*    */   
/*    */   public C00PacketLoginStart() {}
/*    */   
/*    */   public C00PacketLoginStart(GameProfile profileIn)
/*    */   {
/* 20 */     this.profile = profileIn;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void readPacketData(PacketBuffer buf)
/*    */     throws IOException
/*    */   {
/* 28 */     this.profile = new GameProfile(null, buf.readStringFromBuffer(16));
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void writePacketData(PacketBuffer buf)
/*    */     throws IOException
/*    */   {
/* 36 */     buf.writeString(this.profile.getName());
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void processPacket(INetHandlerLoginServer handler)
/*    */   {
/* 44 */     handler.processLoginStart(this);
/*    */   }
/*    */   
/*    */   public GameProfile getProfile()
/*    */   {
/* 49 */     return this.profile;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\network\login\client\C00PacketLoginStart.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */