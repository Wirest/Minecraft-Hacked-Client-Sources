/*     */ package net.minecraft.client.multiplayer;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.InetAddress;
/*     */ import java.net.UnknownHostException;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.GuiButton;
/*     */ import net.minecraft.client.gui.GuiDisconnected;
/*     */ import net.minecraft.client.gui.GuiScreen;
/*     */ import net.minecraft.client.resources.I18n;
/*     */ import net.minecraft.client.settings.GameSettings;
/*     */ import net.minecraft.network.NetworkManager;
/*     */ import net.minecraft.util.ChatComponentText;
/*     */ import net.minecraft.util.ChatComponentTranslation;
/*     */ import net.minecraft.util.Session;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ public class GuiConnecting extends GuiScreen
/*     */ {
/*  23 */   private static final AtomicInteger CONNECTION_ID = new AtomicInteger(0);
/*  24 */   private static final Logger logger = LogManager.getLogger();
/*     */   private NetworkManager networkManager;
/*     */   private boolean cancel;
/*     */   private final GuiScreen previousGuiScreen;
/*     */   
/*     */   public GuiConnecting(GuiScreen p_i1181_1_, Minecraft mcIn, ServerData p_i1181_3_) {
/*  30 */     this.mc = mcIn;
/*  31 */     this.previousGuiScreen = p_i1181_1_;
/*  32 */     ServerAddress serveraddress = ServerAddress.func_78860_a(p_i1181_3_.serverIP);
/*  33 */     mcIn.loadWorld(null);
/*  34 */     mcIn.setServerData(p_i1181_3_);
/*  35 */     connect(serveraddress.getIP(), serveraddress.getPort());
/*     */   }
/*     */   
/*     */   public GuiConnecting(GuiScreen p_i1182_1_, Minecraft mcIn, String hostName, int port) {
/*  39 */     this.mc = mcIn;
/*  40 */     this.previousGuiScreen = p_i1182_1_;
/*  41 */     mcIn.loadWorld(null);
/*  42 */     connect(hostName, port);
/*     */   }
/*     */   
/*     */   private void connect(final String ip, final int port) {
/*  46 */     logger.info("Connecting to " + ip + ", " + port);
/*  47 */     new Thread("Server Connector #" + CONNECTION_ID.incrementAndGet()) {
/*     */       public void run() {
/*  49 */         InetAddress inetaddress = null;
/*     */         try
/*     */         {
/*  52 */           if (GuiConnecting.this.cancel) {
/*  53 */             return;
/*     */           }
/*     */           
/*  56 */           inetaddress = InetAddress.getByName(ip);
/*  57 */           GuiConnecting.this.networkManager = NetworkManager.func_181124_a(inetaddress, port, GuiConnecting.this.mc.gameSettings.func_181148_f());
/*  58 */           GuiConnecting.this.networkManager.setNetHandler(new net.minecraft.client.network.NetHandlerLoginClient(GuiConnecting.this.networkManager, GuiConnecting.this.mc, GuiConnecting.this.previousGuiScreen));
/*  59 */           GuiConnecting.this.networkManager.sendPacket(new net.minecraft.network.handshake.client.C00Handshake(47, ip, port, net.minecraft.network.EnumConnectionState.LOGIN));
/*  60 */           GuiConnecting.this.networkManager.sendPacket(new net.minecraft.network.login.client.C00PacketLoginStart(GuiConnecting.this.mc.getSession().getProfile()));
/*     */         } catch (UnknownHostException unknownhostexception) {
/*  62 */           if (GuiConnecting.this.cancel) {
/*  63 */             return;
/*     */           }
/*     */           
/*  66 */           GuiConnecting.logger.error("Couldn't connect to server", unknownhostexception);
/*  67 */           GuiConnecting.this.mc.displayGuiScreen(new GuiDisconnected(GuiConnecting.this.previousGuiScreen, "connect.failed", new ChatComponentTranslation("disconnect.genericReason", new Object[] { "Unknown host" })));
/*     */         } catch (Exception exception) {
/*  69 */           if (GuiConnecting.this.cancel) {
/*  70 */             return;
/*     */           }
/*     */           
/*  73 */           GuiConnecting.logger.error("Couldn't connect to server", exception);
/*  74 */           String s = exception.toString();
/*  75 */           if (inetaddress != null) {
/*  76 */             String s1 = inetaddress.toString() + ":" + port;
/*  77 */             s = s.replaceAll(s1, "");
/*     */           }
/*     */           
/*  80 */           GuiConnecting.this.mc.displayGuiScreen(new GuiDisconnected(GuiConnecting.this.previousGuiScreen, "connect.failed", new ChatComponentTranslation("disconnect.genericReason", new Object[] { s })));
/*     */         }
/*     */       }
/*     */     }.start();
/*     */   }
/*     */   
/*     */   public void updateScreen() {
/*  87 */     if (this.networkManager != null) {
/*  88 */       if (this.networkManager.isChannelOpen()) {
/*  89 */         this.networkManager.processReceivedPackets();
/*     */       } else {
/*  91 */         this.networkManager.checkDisconnected();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   protected void keyTyped(char typedChar, int keyCode) throws IOException
/*     */   {}
/*     */   
/*     */   public void initGui() {
/* 100 */     this.buttonList.clear();
/* 101 */     this.buttonList.add(new GuiButton(0, width / 2 - 100, height / 4 + 120 + 12, I18n.format("gui.cancel", new Object[0])));
/*     */   }
/*     */   
/*     */   protected void actionPerformed(GuiButton button) throws IOException {
/* 105 */     if (button.id == 0) {
/* 106 */       this.cancel = true;
/* 107 */       if (this.networkManager != null) {
/* 108 */         this.networkManager.closeChannel(new ChatComponentText("Aborted"));
/*     */       }
/*     */       
/* 111 */       this.mc.displayGuiScreen(this.previousGuiScreen);
/*     */     }
/*     */   }
/*     */   
/*     */   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
/* 116 */     drawDefaultBackground();
/* 117 */     if (this.networkManager == null) {
/* 118 */       drawCenteredString(this.fontRendererObj, I18n.format("connect.connecting", new Object[0]), width / 2, height / 2 - 50, 16777215);
/*     */     } else {
/* 120 */       drawCenteredString(this.fontRendererObj, I18n.format("connect.authorizing", new Object[0]), width / 2, height / 2 - 50, 16777215);
/*     */     }
/*     */     
/* 123 */     super.drawScreen(mouseX, mouseY, partialTicks);
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\multiplayer\GuiConnecting.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */