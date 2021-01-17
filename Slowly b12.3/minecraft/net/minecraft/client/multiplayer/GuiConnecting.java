package net.minecraft.client.multiplayer;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.atomic.AtomicInteger;
import me.slowly.client.Client;
import me.slowly.client.shaders.fragment.LoadingScreenShader;
import me.slowly.client.util.RenderUtil;
import me.slowly.client.util.TimeHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiDisconnected;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.network.NetHandlerLoginClient;
import net.minecraft.client.resources.I18n;
import net.minecraft.network.EnumConnectionState;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.handshake.client.C00Handshake;
import net.minecraft.network.login.client.C00PacketLoginStart;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GuiConnecting extends GuiScreen {
   private static final AtomicInteger CONNECTION_ID = new AtomicInteger(0);
   private static final Logger logger = LogManager.getLogger();
   private NetworkManager networkManager;
   private boolean cancel;
   private final GuiScreen previousGuiScreen;
   private LoadingScreenShader loadingShader;
   private boolean loaded;
   private TimeHelper timer = new TimeHelper();
   private String text = "...";

   public GuiConnecting(GuiScreen p_i1181_1_, Minecraft mcIn, ServerData p_i1181_3_) {
      this.mc = mcIn;
      this.previousGuiScreen = p_i1181_1_;
      ServerAddress serveraddress = ServerAddress.func_78860_a(p_i1181_3_.serverIP);
      mcIn.loadWorld((WorldClient)null);
      mcIn.setServerData(p_i1181_3_);
      this.connect(serveraddress.getIP(), serveraddress.getPort());
      this.loadingShader = new LoadingScreenShader();
   }

   public GuiConnecting(GuiScreen p_i1182_1_, Minecraft mcIn, String hostName, int port) {
      this.mc = mcIn;
      this.previousGuiScreen = p_i1182_1_;
      mcIn.loadWorld((WorldClient)null);
      this.connect(hostName, port);
      this.loadingShader = new LoadingScreenShader();
   }

   private void connect(final String ip, final int port) {
      logger.info("Connecting to " + ip + ", " + port);
      (new Thread("Server Connector #" + CONNECTION_ID.incrementAndGet()) {
         public void run() {
            InetAddress inetaddress = null;

            try {
               if (GuiConnecting.this.cancel) {
                  return;
               }

               inetaddress = InetAddress.getByName(ip);
               GuiConnecting.this.networkManager = NetworkManager.func_181124_a(inetaddress, port, GuiConnecting.this.mc.gameSettings.func_181148_f());
               GuiConnecting.this.networkManager.setNetHandler(new NetHandlerLoginClient(GuiConnecting.this.networkManager, GuiConnecting.this.mc, GuiConnecting.this.previousGuiScreen));
               GuiConnecting.this.networkManager.sendPacket(new C00Handshake(47, ip, port, EnumConnectionState.LOGIN));
               GuiConnecting.this.networkManager.sendPacket(new C00PacketLoginStart(GuiConnecting.this.mc.getSession().getProfile()));
            } catch (UnknownHostException var5) {
               if (GuiConnecting.this.cancel) {
                  return;
               }

               GuiConnecting.logger.error("Couldn't connect to server", var5);
               GuiConnecting.this.mc.displayGuiScreen(new GuiDisconnected(GuiConnecting.this.previousGuiScreen, "connect.failed", new ChatComponentTranslation("disconnect.genericReason", new Object[]{"Unknown host"})));
            } catch (Exception var6) {
               if (GuiConnecting.this.cancel) {
                  return;
               }

               GuiConnecting.logger.error("Couldn't connect to server", var6);
               String s = var6.toString();
               if (inetaddress != null) {
                  String s1 = inetaddress.toString() + ":" + port;
                  s = s.replaceAll(s1, "");
               }

               GuiConnecting.this.mc.displayGuiScreen(new GuiDisconnected(GuiConnecting.this.previousGuiScreen, "connect.failed", new ChatComponentTranslation("disconnect.genericReason", new Object[]{s})));
            }

         }
      }).start();
   }

   public void updateScreen() {
      if (this.networkManager != null) {
         if (this.networkManager.isChannelOpen()) {
            this.networkManager.processReceivedPackets();
         } else {
            this.networkManager.checkDisconnected();
         }
      }

   }

   protected void keyTyped(char typedChar, int keyCode) throws IOException {
   }

   public void initGui() {
      this.buttonList.clear();
      this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height - 50, I18n.format("gui.cancel")));
   }

   protected void actionPerformed(GuiButton button) throws IOException {
      if (button.id == 0) {
         this.cancel = true;
         if (this.networkManager != null) {
            this.networkManager.closeChannel(new ChatComponentText("Aborted"));
         }

         this.mc.displayGuiScreen(this.previousGuiScreen);
      }

   }

   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
      this.drawDefaultBackground();
      ScaledResolution res = new ScaledResolution(this.mc);
      if (this.mc.gameSettings.ofFastRender) {
         RenderUtil.drawImage(new ResourceLocation("slowly/flat2.jpg"), 0, 0, res.getScaledWidth(), res.getScaledHeight());
      } else {
         this.loadingShader.startShader();
         RenderUtil.drawImage(new ResourceLocation("slowly/flat2.jpg"), 0, 0, res.getScaledWidth(), res.getScaledHeight());
         this.loadingShader.stopShader();
      }

      if (!this.loaded && !this.mc.gameSettings.ofFastRender) {
         this.loadingShader.deleteShader();
         this.loaded = true;
         this.timer.reset();
      }

      if (this.timer.isDelayComplete(500L)) {
         if (this.text.equalsIgnoreCase(".")) {
            this.text = "..";
         } else if (this.text.equalsIgnoreCase("..")) {
            this.text = "...";
         } else {
            this.text = ".";
         }

         this.timer.reset();
      }

      Client.getInstance().getFontManager().consolas20.drawCenteredString("Connecting" + this.text, (float)(this.width / 2), (float)(this.height / 2 - 100), 16777215);
      super.drawScreen(mouseX, mouseY, partialTicks);
   }
}
