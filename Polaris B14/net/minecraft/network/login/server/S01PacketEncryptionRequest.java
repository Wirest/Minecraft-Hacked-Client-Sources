/*    */ package net.minecraft.network.login.server;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.security.PublicKey;
/*    */ import net.minecraft.network.Packet;
/*    */ import net.minecraft.network.PacketBuffer;
/*    */ import net.minecraft.network.login.INetHandlerLoginClient;
/*    */ import net.minecraft.util.CryptManager;
/*    */ 
/*    */ 
/*    */ public class S01PacketEncryptionRequest
/*    */   implements Packet<INetHandlerLoginClient>
/*    */ {
/*    */   private String hashedServerId;
/*    */   private PublicKey publicKey;
/*    */   private byte[] verifyToken;
/*    */   
/*    */   public S01PacketEncryptionRequest() {}
/*    */   
/*    */   public S01PacketEncryptionRequest(String serverId, PublicKey key, byte[] verifyToken)
/*    */   {
/* 22 */     this.hashedServerId = serverId;
/* 23 */     this.publicKey = key;
/* 24 */     this.verifyToken = verifyToken;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void readPacketData(PacketBuffer buf)
/*    */     throws IOException
/*    */   {
/* 32 */     this.hashedServerId = buf.readStringFromBuffer(20);
/* 33 */     this.publicKey = CryptManager.decodePublicKey(buf.readByteArray());
/* 34 */     this.verifyToken = buf.readByteArray();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void writePacketData(PacketBuffer buf)
/*    */     throws IOException
/*    */   {
/* 42 */     buf.writeString(this.hashedServerId);
/* 43 */     buf.writeByteArray(this.publicKey.getEncoded());
/* 44 */     buf.writeByteArray(this.verifyToken);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void processPacket(INetHandlerLoginClient handler)
/*    */   {
/* 52 */     handler.handleEncryptionRequest(this);
/*    */   }
/*    */   
/*    */   public String getServerId()
/*    */   {
/* 57 */     return this.hashedServerId;
/*    */   }
/*    */   
/*    */   public PublicKey getPublicKey()
/*    */   {
/* 62 */     return this.publicKey;
/*    */   }
/*    */   
/*    */   public byte[] getVerifyToken()
/*    */   {
/* 67 */     return this.verifyToken;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\network\login\server\S01PacketEncryptionRequest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */