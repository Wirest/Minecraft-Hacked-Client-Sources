/*     */ package net.minecraft.realms;
/*     */ 
/*     */ import java.net.InetAddress;
/*     */ import java.net.UnknownHostException;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.network.NetHandlerLoginClient;
/*     */ import net.minecraft.client.settings.GameSettings;
/*     */ import net.minecraft.network.EnumConnectionState;
/*     */ import net.minecraft.network.NetworkManager;
/*     */ import net.minecraft.util.ChatComponentTranslation;
/*     */ import net.minecraft.util.Session;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ public class RealmsConnect
/*     */ {
/*  17 */   private static final Logger LOGGER = ;
/*     */   private final RealmsScreen onlineScreen;
/*  19 */   private volatile boolean aborted = false;
/*     */   private NetworkManager connection;
/*     */   
/*     */   public RealmsConnect(RealmsScreen p_i1079_1_)
/*     */   {
/*  24 */     this.onlineScreen = p_i1079_1_;
/*     */   }
/*     */   
/*     */   public void connect(final String p_connect_1_, final int p_connect_2_)
/*     */   {
/*  29 */     Realms.setConnectedToRealms(true);
/*  30 */     new Thread("Realms-connect-task")
/*     */     {
/*     */       public void run()
/*     */       {
/*  34 */         InetAddress inetaddress = null;
/*     */         
/*     */         try
/*     */         {
/*  38 */           inetaddress = InetAddress.getByName(p_connect_1_);
/*     */           
/*  40 */           if (RealmsConnect.this.aborted)
/*     */           {
/*  42 */             return;
/*     */           }
/*     */           
/*  45 */           RealmsConnect.this.connection = NetworkManager.func_181124_a(inetaddress, p_connect_2_, Minecraft.getMinecraft().gameSettings.func_181148_f());
/*     */           
/*  47 */           if (RealmsConnect.this.aborted)
/*     */           {
/*  49 */             return;
/*     */           }
/*     */           
/*  52 */           RealmsConnect.this.connection.setNetHandler(new NetHandlerLoginClient(RealmsConnect.this.connection, Minecraft.getMinecraft(), RealmsConnect.this.onlineScreen.getProxy()));
/*     */           
/*  54 */           if (RealmsConnect.this.aborted)
/*     */           {
/*  56 */             return;
/*     */           }
/*     */           
/*  59 */           RealmsConnect.this.connection.sendPacket(new net.minecraft.network.handshake.client.C00Handshake(47, p_connect_1_, p_connect_2_, EnumConnectionState.LOGIN));
/*     */           
/*  61 */           if (RealmsConnect.this.aborted)
/*     */           {
/*  63 */             return;
/*     */           }
/*     */           
/*  66 */           RealmsConnect.this.connection.sendPacket(new net.minecraft.network.login.client.C00PacketLoginStart(Minecraft.getMinecraft().getSession().getProfile()));
/*     */         }
/*     */         catch (UnknownHostException unknownhostexception)
/*     */         {
/*  70 */           Realms.clearResourcePack();
/*     */           
/*  72 */           if (RealmsConnect.this.aborted)
/*     */           {
/*  74 */             return;
/*     */           }
/*     */           
/*  77 */           RealmsConnect.LOGGER.error("Couldn't connect to world", unknownhostexception);
/*  78 */           Minecraft.getMinecraft().getResourcePackRepository().func_148529_f();
/*  79 */           Realms.setScreen(new DisconnectedRealmsScreen(RealmsConnect.this.onlineScreen, "connect.failed", new ChatComponentTranslation("disconnect.genericReason", new Object[] { "Unknown host '" + p_connect_1_ + "'" })));
/*     */         }
/*     */         catch (Exception exception)
/*     */         {
/*  83 */           Realms.clearResourcePack();
/*     */           
/*  85 */           if (RealmsConnect.this.aborted)
/*     */           {
/*  87 */             return;
/*     */           }
/*     */           
/*  90 */           RealmsConnect.LOGGER.error("Couldn't connect to world", exception);
/*  91 */           String s = exception.toString();
/*     */           
/*  93 */           if (inetaddress != null)
/*     */           {
/*  95 */             String s1 = inetaddress.toString() + ":" + p_connect_2_;
/*  96 */             s = s.replaceAll(s1, "");
/*     */           }
/*     */           
/*  99 */           Realms.setScreen(new DisconnectedRealmsScreen(RealmsConnect.this.onlineScreen, "connect.failed", new ChatComponentTranslation("disconnect.genericReason", new Object[] { s })));
/*     */         }
/*     */       }
/*     */     }.start();
/*     */   }
/*     */   
/*     */   public void abort()
/*     */   {
/* 107 */     this.aborted = true;
/*     */   }
/*     */   
/*     */   public void tick()
/*     */   {
/* 112 */     if (this.connection != null)
/*     */     {
/* 114 */       if (this.connection.isChannelOpen())
/*     */       {
/* 116 */         this.connection.processReceivedPackets();
/*     */       }
/*     */       else
/*     */       {
/* 120 */         this.connection.checkDisconnected();
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\realms\RealmsConnect.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */