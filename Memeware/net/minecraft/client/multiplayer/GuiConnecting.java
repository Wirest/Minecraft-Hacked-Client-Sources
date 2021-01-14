package net.minecraft.client.multiplayer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiDisconnected;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.network.NetHandlerLoginClient;
import net.minecraft.client.resources.I18n;
import net.minecraft.network.EnumConnectionState;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.handshake.client.C00Handshake;
import net.minecraft.network.login.client.C00PacketLoginStart;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.atomic.AtomicInteger;

public class GuiConnecting extends GuiScreen {
    private static final AtomicInteger CONNECTION_ID = new AtomicInteger(0);
    private static final Logger logger = LogManager.getLogger();
    private NetworkManager networkManager;
    private boolean cancel;
    private final GuiScreen previousGuiScreen;
    private static final String __OBFID = "CL_00000685";

    public GuiConnecting(GuiScreen p_i1181_1_, Minecraft mcIn, ServerData p_i1181_3_) {
        this.mc = mcIn;
        this.previousGuiScreen = p_i1181_1_;
        ServerAddress var4 = ServerAddress.func_78860_a(p_i1181_3_.serverIP);
        mcIn.loadWorld((WorldClient) null);
        mcIn.setServerData(p_i1181_3_);
        this.connect(var4.getIP(), var4.getPort());
    }

    public GuiConnecting(GuiScreen p_i1182_1_, Minecraft mcIn, String p_i1182_3_, int p_i1182_4_) {
        this.mc = mcIn;
        this.previousGuiScreen = p_i1182_1_;
        mcIn.loadWorld((WorldClient) null);
        this.connect(p_i1182_3_, p_i1182_4_);
    }

    private void connect(final String ip, final int port) {
        logger.info("Connecting to " + ip + ", " + port);
        (new Thread("Server Connector #" + CONNECTION_ID.incrementAndGet()) {
            private static final String __OBFID = "CL_00000686";

            public void run() {
                InetAddress var1 = null;

                try {
                    if (GuiConnecting.this.cancel) {
                        return;
                    }

                    var1 = InetAddress.getByName(ip);
                    GuiConnecting.this.networkManager = NetworkManager.provideLanClient(var1, port);
                    GuiConnecting.this.networkManager.setNetHandler(new NetHandlerLoginClient(GuiConnecting.this.networkManager, GuiConnecting.this.mc, GuiConnecting.this.previousGuiScreen));
                    GuiConnecting.this.networkManager.sendPacket(new C00Handshake(47, ip, port, EnumConnectionState.LOGIN));
                    GuiConnecting.this.networkManager.sendPacket(new C00PacketLoginStart(GuiConnecting.this.mc.getSession().getProfile()));
                } catch (UnknownHostException var5) {
                    if (GuiConnecting.this.cancel) {
                        return;
                    }

                    GuiConnecting.logger.error("Couldn\'t connect to server", var5);
                    GuiConnecting.this.mc.displayGuiScreen(new GuiDisconnected(GuiConnecting.this.previousGuiScreen, "connect.failed", new ChatComponentTranslation("disconnect.genericReason", new Object[]{"Unknown host"})));
                } catch (Exception var6) {
                    if (GuiConnecting.this.cancel) {
                        return;
                    }

                    GuiConnecting.logger.error("Couldn\'t connect to server", var6);
                    String var3 = var6.toString();

                    if (var1 != null) {
                        String var4 = var1.toString() + ":" + port;
                        var3 = var3.replaceAll(var4, "");
                    }

                    GuiConnecting.this.mc.displayGuiScreen(new GuiDisconnected(GuiConnecting.this.previousGuiScreen, "connect.failed", new ChatComponentTranslation("disconnect.genericReason", new Object[]{var3})));
                }
            }
        }).start();
    }

    /**
     * Called from the main game loop to update the screen.
     */
    public void updateScreen() {
        if (this.networkManager != null) {
            if (this.networkManager.isChannelOpen()) {
                this.networkManager.processReceivedPackets();
            } else {
                this.networkManager.checkDisconnected();
            }
        }
    }

    /**
     * Fired when a key is typed (except F11 who toggle full screen). This is the equivalent of
     * KeyListener.keyTyped(KeyEvent e). Args : character (character on the key), keyCode (lwjgl Keyboard key code)
     */
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    public void initGui() {
        this.buttonList.clear();
        this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 120 + 12, I18n.format("gui.cancel", new Object[0])));
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

    /**
     * Draws the screen and all the components in it. Args : mouseX, mouseY, renderPartialTicks
     */
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();

        if (this.networkManager == null) {
            this.drawCenteredString(this.fontRendererObj, I18n.format("connect.connecting", new Object[0]), this.width / 2, this.height / 2 - 50, 16777215);
        } else {
            this.drawCenteredString(this.fontRendererObj, I18n.format("connect.authorizing", new Object[0]), this.width / 2, this.height / 2 - 50, 16777215);
        }

        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}
