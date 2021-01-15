/*     */ package net.minecraft.server.network;
/*     */ 
/*     */ import com.mojang.authlib.GameProfile;
/*     */ import com.mojang.authlib.exceptions.AuthenticationUnavailableException;
/*     */ import com.mojang.authlib.minecraft.MinecraftSessionService;
/*     */ import io.netty.channel.ChannelFuture;
/*     */ import io.netty.channel.ChannelFutureListener;
/*     */ import java.math.BigInteger;
/*     */ import java.security.KeyPair;
/*     */ import java.security.PrivateKey;
/*     */ import java.util.Arrays;
/*     */ import java.util.Random;
/*     */ import java.util.UUID;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ import javax.crypto.SecretKey;
/*     */ import net.minecraft.entity.player.EntityPlayerMP;
/*     */ import net.minecraft.network.NetworkManager;
/*     */ import net.minecraft.network.login.client.C00PacketLoginStart;
/*     */ import net.minecraft.network.login.client.C01PacketEncryptionResponse;
/*     */ import net.minecraft.network.login.server.S00PacketDisconnect;
/*     */ import net.minecraft.network.login.server.S01PacketEncryptionRequest;
/*     */ import net.minecraft.network.login.server.S02PacketLoginSuccess;
/*     */ import net.minecraft.network.login.server.S03PacketEnableCompression;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ import net.minecraft.server.management.ServerConfigurationManager;
/*     */ import net.minecraft.util.ChatComponentText;
/*     */ import net.minecraft.util.CryptManager;
/*     */ import net.minecraft.util.IChatComponent;
/*     */ import net.minecraft.util.ITickable;
/*     */ import org.apache.commons.lang3.Validate;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ public class NetHandlerLoginServer implements net.minecraft.network.login.INetHandlerLoginServer, ITickable
/*     */ {
/*  36 */   private static final AtomicInteger AUTHENTICATOR_THREAD_ID = new AtomicInteger(0);
/*  37 */   private static final Logger logger = LogManager.getLogger();
/*  38 */   private static final Random RANDOM = new Random();
/*  39 */   private final byte[] verifyToken = new byte[4];
/*     */   private final MinecraftServer server;
/*     */   public final NetworkManager networkManager;
/*  42 */   private LoginState currentLoginState = LoginState.HELLO;
/*     */   
/*     */   private int connectionTimer;
/*     */   
/*     */   private GameProfile loginGameProfile;
/*  47 */   private String serverId = "";
/*     */   private SecretKey secretKey;
/*     */   private EntityPlayerMP field_181025_l;
/*     */   
/*     */   public NetHandlerLoginServer(MinecraftServer p_i45298_1_, NetworkManager p_i45298_2_)
/*     */   {
/*  53 */     this.server = p_i45298_1_;
/*  54 */     this.networkManager = p_i45298_2_;
/*  55 */     RANDOM.nextBytes(this.verifyToken);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void update()
/*     */   {
/*  63 */     if (this.currentLoginState == LoginState.READY_TO_ACCEPT)
/*     */     {
/*  65 */       tryAcceptPlayer();
/*     */     }
/*  67 */     else if (this.currentLoginState == LoginState.DELAY_ACCEPT)
/*     */     {
/*  69 */       EntityPlayerMP entityplayermp = this.server.getConfigurationManager().getPlayerByUUID(this.loginGameProfile.getId());
/*     */       
/*  71 */       if (entityplayermp == null)
/*     */       {
/*  73 */         this.currentLoginState = LoginState.READY_TO_ACCEPT;
/*  74 */         this.server.getConfigurationManager().initializeConnectionToPlayer(this.networkManager, this.field_181025_l);
/*  75 */         this.field_181025_l = null;
/*     */       }
/*     */     }
/*     */     
/*  79 */     if (this.connectionTimer++ == 600)
/*     */     {
/*  81 */       closeConnection("Took too long to log in");
/*     */     }
/*     */   }
/*     */   
/*     */   public void closeConnection(String reason)
/*     */   {
/*     */     try
/*     */     {
/*  89 */       logger.info("Disconnecting " + getConnectionInfo() + ": " + reason);
/*  90 */       ChatComponentText chatcomponenttext = new ChatComponentText(reason);
/*  91 */       this.networkManager.sendPacket(new S00PacketDisconnect(chatcomponenttext));
/*  92 */       this.networkManager.closeChannel(chatcomponenttext);
/*     */     }
/*     */     catch (Exception exception)
/*     */     {
/*  96 */       logger.error("Error whilst disconnecting player", exception);
/*     */     }
/*     */   }
/*     */   
/*     */   public void tryAcceptPlayer()
/*     */   {
/* 102 */     if (!this.loginGameProfile.isComplete())
/*     */     {
/* 104 */       this.loginGameProfile = getOfflineProfile(this.loginGameProfile);
/*     */     }
/*     */     
/* 107 */     String s = this.server.getConfigurationManager().allowUserToConnect(this.networkManager.getRemoteAddress(), this.loginGameProfile);
/*     */     
/* 109 */     if (s != null)
/*     */     {
/* 111 */       closeConnection(s);
/*     */     }
/*     */     else
/*     */     {
/* 115 */       this.currentLoginState = LoginState.ACCEPTED;
/*     */       
/* 117 */       if ((this.server.getNetworkCompressionTreshold() >= 0) && (!this.networkManager.isLocalChannel()))
/*     */       {
/* 119 */         this.networkManager.sendPacket(new S03PacketEnableCompression(this.server.getNetworkCompressionTreshold()), new ChannelFutureListener()
/*     */         {
/*     */           public void operationComplete(ChannelFuture p_operationComplete_1_) throws Exception
/*     */           {
/* 123 */             NetHandlerLoginServer.this.networkManager.setCompressionTreshold(NetHandlerLoginServer.this.server.getNetworkCompressionTreshold());
/*     */           }
/* 125 */         }, new io.netty.util.concurrent.GenericFutureListener[0]);
/*     */       }
/*     */       
/* 128 */       this.networkManager.sendPacket(new S02PacketLoginSuccess(this.loginGameProfile));
/* 129 */       EntityPlayerMP entityplayermp = this.server.getConfigurationManager().getPlayerByUUID(this.loginGameProfile.getId());
/*     */       
/* 131 */       if (entityplayermp != null)
/*     */       {
/* 133 */         this.currentLoginState = LoginState.DELAY_ACCEPT;
/* 134 */         this.field_181025_l = this.server.getConfigurationManager().createPlayerForUser(this.loginGameProfile);
/*     */       }
/*     */       else
/*     */       {
/* 138 */         this.server.getConfigurationManager().initializeConnectionToPlayer(this.networkManager, this.server.getConfigurationManager().createPlayerForUser(this.loginGameProfile));
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onDisconnect(IChatComponent reason)
/*     */   {
/* 148 */     logger.info(getConnectionInfo() + " lost connection: " + reason.getUnformattedText());
/*     */   }
/*     */   
/*     */   public String getConnectionInfo()
/*     */   {
/* 153 */     return this.loginGameProfile != null ? this.loginGameProfile.toString() + " (" + this.networkManager.getRemoteAddress().toString() + ")" : String.valueOf(this.networkManager.getRemoteAddress());
/*     */   }
/*     */   
/*     */   public void processLoginStart(C00PacketLoginStart packetIn)
/*     */   {
/* 158 */     Validate.validState(this.currentLoginState == LoginState.HELLO, "Unexpected hello packet", new Object[0]);
/* 159 */     this.loginGameProfile = packetIn.getProfile();
/*     */     
/* 161 */     if ((this.server.isServerInOnlineMode()) && (!this.networkManager.isLocalChannel()))
/*     */     {
/* 163 */       this.currentLoginState = LoginState.KEY;
/* 164 */       this.networkManager.sendPacket(new S01PacketEncryptionRequest(this.serverId, this.server.getKeyPair().getPublic(), this.verifyToken));
/*     */     }
/*     */     else
/*     */     {
/* 168 */       this.currentLoginState = LoginState.READY_TO_ACCEPT;
/*     */     }
/*     */   }
/*     */   
/*     */   public void processEncryptionResponse(C01PacketEncryptionResponse packetIn)
/*     */   {
/* 174 */     Validate.validState(this.currentLoginState == LoginState.KEY, "Unexpected key packet", new Object[0]);
/* 175 */     PrivateKey privatekey = this.server.getKeyPair().getPrivate();
/*     */     
/* 177 */     if (!Arrays.equals(this.verifyToken, packetIn.getVerifyToken(privatekey)))
/*     */     {
/* 179 */       throw new IllegalStateException("Invalid nonce!");
/*     */     }
/*     */     
/*     */ 
/* 183 */     this.secretKey = packetIn.getSecretKey(privatekey);
/* 184 */     this.currentLoginState = LoginState.AUTHENTICATING;
/* 185 */     this.networkManager.enableEncryption(this.secretKey);
/* 186 */     new Thread("User Authenticator #" + AUTHENTICATOR_THREAD_ID.incrementAndGet())
/*     */     {
/*     */       public void run()
/*     */       {
/* 190 */         GameProfile gameprofile = NetHandlerLoginServer.this.loginGameProfile;
/*     */         
/*     */         try
/*     */         {
/* 194 */           String s = new BigInteger(CryptManager.getServerIdHash(NetHandlerLoginServer.this.serverId, NetHandlerLoginServer.this.server.getKeyPair().getPublic(), NetHandlerLoginServer.this.secretKey)).toString(16);
/* 195 */           NetHandlerLoginServer.this.loginGameProfile = NetHandlerLoginServer.this.server.getMinecraftSessionService().hasJoinedServer(new GameProfile(null, gameprofile.getName()), s);
/*     */           
/* 197 */           if (NetHandlerLoginServer.this.loginGameProfile != null)
/*     */           {
/* 199 */             NetHandlerLoginServer.logger.info("UUID of player " + NetHandlerLoginServer.this.loginGameProfile.getName() + " is " + NetHandlerLoginServer.this.loginGameProfile.getId());
/* 200 */             NetHandlerLoginServer.this.currentLoginState = NetHandlerLoginServer.LoginState.READY_TO_ACCEPT;
/*     */           }
/* 202 */           else if (NetHandlerLoginServer.this.server.isSinglePlayer())
/*     */           {
/* 204 */             NetHandlerLoginServer.logger.warn("Failed to verify username but will let them in anyway!");
/* 205 */             NetHandlerLoginServer.this.loginGameProfile = NetHandlerLoginServer.this.getOfflineProfile(gameprofile);
/* 206 */             NetHandlerLoginServer.this.currentLoginState = NetHandlerLoginServer.LoginState.READY_TO_ACCEPT;
/*     */           }
/*     */           else
/*     */           {
/* 210 */             NetHandlerLoginServer.this.closeConnection("Failed to verify username!");
/* 211 */             NetHandlerLoginServer.logger.error("Username '" + NetHandlerLoginServer.this.loginGameProfile.getName() + "' tried to join with an invalid session");
/*     */           }
/*     */         }
/*     */         catch (AuthenticationUnavailableException var3)
/*     */         {
/* 216 */           if (NetHandlerLoginServer.this.server.isSinglePlayer())
/*     */           {
/* 218 */             NetHandlerLoginServer.logger.warn("Authentication servers are down but will let them in anyway!");
/* 219 */             NetHandlerLoginServer.this.loginGameProfile = NetHandlerLoginServer.this.getOfflineProfile(gameprofile);
/* 220 */             NetHandlerLoginServer.this.currentLoginState = NetHandlerLoginServer.LoginState.READY_TO_ACCEPT;
/*     */           }
/*     */           else
/*     */           {
/* 224 */             NetHandlerLoginServer.this.closeConnection("Authentication servers are down. Please try again later, sorry!");
/* 225 */             NetHandlerLoginServer.logger.error("Couldn't verify username because servers are unavailable");
/*     */           }
/*     */         }
/*     */       }
/*     */     }.start();
/*     */   }
/*     */   
/*     */ 
/*     */   protected GameProfile getOfflineProfile(GameProfile original)
/*     */   {
/* 235 */     UUID uuid = UUID.nameUUIDFromBytes(("OfflinePlayer:" + original.getName()).getBytes(com.google.common.base.Charsets.UTF_8));
/* 236 */     return new GameProfile(uuid, original.getName());
/*     */   }
/*     */   
/*     */   static enum LoginState
/*     */   {
/* 241 */     HELLO, 
/* 242 */     KEY, 
/* 243 */     AUTHENTICATING, 
/* 244 */     READY_TO_ACCEPT, 
/* 245 */     DELAY_ACCEPT, 
/* 246 */     ACCEPTED;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\server\network\NetHandlerLoginServer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */