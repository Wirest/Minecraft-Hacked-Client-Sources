// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.gui;

import java.io.IOException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;

import me.razerboy420.weepcraft.Weepcraft;
import me.razerboy420.weepcraft.gui.alts.GuiAltScreen;
import me.razerboy420.weepcraft.gui.servers.GuiWCServers;
import me.razerboy420.weepcraft.util.ColorUtil;
import me.razerboy420.weepcraft.util.Wrapper;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.ServerList;
import net.minecraft.client.network.LanServerDetector;
import net.minecraft.client.network.LanServerInfo;
import net.minecraft.client.network.ServerPinger;
import net.minecraft.client.resources.I18n;

public class GuiMultiplayer extends GuiScreen
{
    private static final Logger LOGGER;
    private final ServerPinger oldServerPinger;
    private final GuiScreen parentScreen;
    private ServerSelectionList serverListSelector;
    private ServerList savedServerList;
    private GuiButton btnEditServer;
    private GuiButton btnSelectServer;
    private GuiButton btnDeleteServer;
    private boolean deletingServer;
    private boolean addingServer;
    private boolean editingServer;
    private boolean directConnect;
    private String hoveringText;
    private ServerData selectedServer;
    private LanServerDetector.LanServerList lanServerList;
    private LanServerDetector.ThreadLanServerFind lanServerDetector;
    private boolean initialized;
    
    static {
        LOGGER = LogManager.getLogger();
    }
    
    public GuiMultiplayer(final GuiScreen parentScreen) {
        this.oldServerPinger = new ServerPinger();
        this.parentScreen = parentScreen;
    }
    
    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents(true);
        this.buttonList.clear();
        if (this.initialized) {
            this.serverListSelector.setDimensions(this.width, this.height, 32, this.height - 64);
        }
        else {
            this.initialized = true;
            (this.savedServerList = new ServerList(this.mc)).loadServerList();
            this.lanServerList = new LanServerDetector.LanServerList();
            try {
                (this.lanServerDetector = new LanServerDetector.ThreadLanServerFind(this.lanServerList)).start();
            }
            catch (Exception exception) {
                GuiMultiplayer.LOGGER.warn("Unable to start LAN server detection: {}", new Object[] { exception.getMessage() });
            }
            (this.serverListSelector = new ServerSelectionList(this, this.mc, this.width, this.height, 32, this.height - 64, 36)).updateOnlineServers(this.savedServerList);
        }
        this.createButtons();
    }
    
    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        this.serverListSelector.handleMouseInput();
    }
    
    public void createButtons() {
        this.btnEditServer = this.addButton(new GuiButton(7, this.width / 2 - 154, this.height - 28, 70, 20, I18n.format("selectServer.edit", new Object[0])));
        this.btnDeleteServer = this.addButton(new GuiButton(2, this.width / 2 - 74, this.height - 28, 70, 20, I18n.format("selectServer.delete", new Object[0])));
        this.btnSelectServer = this.addButton(new GuiButton(1, this.width / 2 - 154, this.height - 52, 100, 20, I18n.format("selectServer.select", new Object[0])));
        this.buttonList.add(new GuiButton(4, this.width / 2 - 50, this.height - 52, 100, 20, I18n.format("selectServer.direct", new Object[0])));
        this.buttonList.add(new GuiButton(3, this.width / 2 + 4 + 50, this.height - 52, 100, 20, I18n.format("selectServer.add", new Object[0])));
        this.buttonList.add(new GuiButton(8, this.width / 2 + 4, this.height - 28, 70, 20, I18n.format("selectServer.refresh", new Object[0])));
        this.buttonList.add(new GuiButton(0, this.width / 2 + 4 + 76, this.height - 28, 75, 20, I18n.format("gui.cancel", new Object[0])));
        this.buttonList.add(new GuiButton(300, this.width / 2 - 210, 10, 70, 20, String.valueOf(ColorUtil.getColor(Weepcraft.normalColor)) + "Alt Manager"));
        this.buttonList.add(new GuiButton(302, this.width / 2 - 35 + 120, 10, 120, 20, String.valueOf(ColorUtil.getColor(Weepcraft.normalColor)) + "Recommended Servers"));
        this.selectServer(this.serverListSelector.getSelected());
    }
    
    @Override
    public void updateScreen() {
        super.updateScreen();
        if (this.lanServerList.getWasUpdated()) {
            final List<LanServerInfo> list = this.lanServerList.getLanServers();
            this.lanServerList.setWasNotUpdated();
            this.serverListSelector.updateNetworkServers(list);
        }
        this.oldServerPinger.pingPendingNetworks();
    }
    
    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
        if (this.lanServerDetector != null) {
            this.lanServerDetector.interrupt();
            this.lanServerDetector = null;
        }
        this.oldServerPinger.clearPendingNetworks();
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
        if (button.enabled) {
            final GuiListExtended.IGuiListEntry guilistextended$iguilistentry = (this.serverListSelector.getSelected() < 0) ? null : this.serverListSelector.getListEntry(this.serverListSelector.getSelected());
            if (button.id == 2 && guilistextended$iguilistentry instanceof ServerListEntryNormal) {
                final String s4 = ((ServerListEntryNormal)guilistextended$iguilistentry).getServerData().serverName;
                if (s4 != null) {
                    this.deletingServer = true;
                    final String s5 = I18n.format("selectServer.deleteQuestion", new Object[0]);
                    final String s6 = "'" + s4 + "' " + I18n.format("selectServer.deleteWarning", new Object[0]);
                    final String s7 = I18n.format("selectServer.deleteButton", new Object[0]);
                    final String s8 = I18n.format("gui.cancel", new Object[0]);
                    final GuiYesNo guiyesno = new GuiYesNo(this, s5, s6, s7, s8, this.serverListSelector.getSelected());
                    this.mc.displayGuiScreen(guiyesno);
                }
            }
            else if (button.id == 1) {
                this.connectToSelected();
            }
            else if (button.id == 4) {
                this.directConnect = true;
                this.selectedServer = new ServerData(I18n.format("selectServer.defaultName", new Object[0]), "", false);
                this.mc.displayGuiScreen(new GuiScreenServerList(this, this.selectedServer));
            }
            else if (button.id == 3) {
                this.addingServer = true;
                this.selectedServer = new ServerData(I18n.format("selectServer.defaultName", new Object[0]), "", false);
                this.mc.displayGuiScreen(new GuiScreenAddServer(this, this.selectedServer));
            }
            else if (button.id == 7 && guilistextended$iguilistentry instanceof ServerListEntryNormal) {
                this.editingServer = true;
                final ServerData serverdata = ((ServerListEntryNormal)guilistextended$iguilistentry).getServerData();
                (this.selectedServer = new ServerData(serverdata.serverName, serverdata.serverIP, false)).copyFrom(serverdata);
                this.mc.displayGuiScreen(new GuiScreenAddServer(this, this.selectedServer));
            }
            else if (button.id == 0) {
                this.mc.displayGuiScreen(this.parentScreen);
            }
            else if (button.id == 8) {
                this.refreshServerList();
            }
            if (button.id == 300) {
                Wrapper.mc().displayGuiScreen(new GuiAltScreen());
            }
            if (button.id == 301) {
            }
            if (button.id == 302) {
                Wrapper.mc().displayGuiScreen(new GuiWCServers());
            }
        }
    }
    
    private void refreshServerList() {
        this.mc.displayGuiScreen(new GuiMultiplayer(this.parentScreen));
    }
    
    @Override
    public void confirmClicked(final boolean result, final int id) {
        final GuiListExtended.IGuiListEntry guilistextended$iguilistentry = (this.serverListSelector.getSelected() < 0) ? null : this.serverListSelector.getListEntry(this.serverListSelector.getSelected());
        if (this.deletingServer) {
            this.deletingServer = false;
            if (result && guilistextended$iguilistentry instanceof ServerListEntryNormal) {
                this.savedServerList.removeServerData(this.serverListSelector.getSelected());
                this.savedServerList.saveServerList();
                this.serverListSelector.setSelectedSlotIndex(-1);
                this.serverListSelector.updateOnlineServers(this.savedServerList);
            }
            this.mc.displayGuiScreen(this);
        }
        else if (this.directConnect) {
            this.directConnect = false;
            if (result) {
                this.connectToServer(this.selectedServer);
            }
            else {
                this.mc.displayGuiScreen(this);
            }
        }
        else if (this.addingServer) {
            this.addingServer = false;
            if (result) {
                this.savedServerList.addServerData(this.selectedServer);
                this.savedServerList.saveServerList();
                this.serverListSelector.setSelectedSlotIndex(-1);
                this.serverListSelector.updateOnlineServers(this.savedServerList);
            }
            this.mc.displayGuiScreen(this);
        }
        else if (this.editingServer) {
            this.editingServer = false;
            if (result && guilistextended$iguilistentry instanceof ServerListEntryNormal) {
                final ServerData serverdata = ((ServerListEntryNormal)guilistextended$iguilistentry).getServerData();
                serverdata.serverName = this.selectedServer.serverName;
                serverdata.serverIP = this.selectedServer.serverIP;
                serverdata.copyFrom(this.selectedServer);
                this.savedServerList.saveServerList();
                this.serverListSelector.updateOnlineServers(this.savedServerList);
            }
            this.mc.displayGuiScreen(this);
        }
    }
    
    @Override
    protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
        final int i = this.serverListSelector.getSelected();
        final GuiListExtended.IGuiListEntry guilistextended$iguilistentry = (i < 0) ? null : this.serverListSelector.getListEntry(i);
        if (keyCode == 63) {
            this.refreshServerList();
        }
        else if (i >= 0) {
            if (keyCode == 200) {
                if (isShiftKeyDown()) {
                    if (i > 0 && guilistextended$iguilistentry instanceof ServerListEntryNormal) {
                        this.savedServerList.swapServers(i, i - 1);
                        this.selectServer(this.serverListSelector.getSelected() - 1);
                        this.serverListSelector.scrollBy(-this.serverListSelector.getSlotHeight());
                        this.serverListSelector.updateOnlineServers(this.savedServerList);
                    }
                }
                else if (i > 0) {
                    this.selectServer(this.serverListSelector.getSelected() - 1);
                    this.serverListSelector.scrollBy(-this.serverListSelector.getSlotHeight());
                    if (this.serverListSelector.getListEntry(this.serverListSelector.getSelected()) instanceof ServerListEntryLanScan) {
                        if (this.serverListSelector.getSelected() > 0) {
                            this.selectServer(this.serverListSelector.getSize() - 1);
                            this.serverListSelector.scrollBy(-this.serverListSelector.getSlotHeight());
                        }
                        else {
                            this.selectServer(-1);
                        }
                    }
                }
                else {
                    this.selectServer(-1);
                }
            }
            else if (keyCode == 208) {
                if (isShiftKeyDown()) {
                    if (i < this.savedServerList.countServers() - 1) {
                        this.savedServerList.swapServers(i, i + 1);
                        this.selectServer(i + 1);
                        this.serverListSelector.scrollBy(this.serverListSelector.getSlotHeight());
                        this.serverListSelector.updateOnlineServers(this.savedServerList);
                    }
                }
                else if (i < this.serverListSelector.getSize()) {
                    this.selectServer(this.serverListSelector.getSelected() + 1);
                    this.serverListSelector.scrollBy(this.serverListSelector.getSlotHeight());
                    if (this.serverListSelector.getListEntry(this.serverListSelector.getSelected()) instanceof ServerListEntryLanScan) {
                        if (this.serverListSelector.getSelected() < this.serverListSelector.getSize() - 1) {
                            this.selectServer(this.serverListSelector.getSize() + 1);
                            this.serverListSelector.scrollBy(this.serverListSelector.getSlotHeight());
                        }
                        else {
                            this.selectServer(-1);
                        }
                    }
                }
                else {
                    this.selectServer(-1);
                }
            }
            else if (keyCode != 28 && keyCode != 156) {
                super.keyTyped(typedChar, keyCode);
            }
            else {
                this.actionPerformed(this.buttonList.get(2));
            }
        }
        else {
            super.keyTyped(typedChar, keyCode);
        }
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.hoveringText = null;
        this.drawDefaultBackground();
        this.serverListSelector.drawScreen(mouseX, mouseY, partialTicks);
        Gui.drawCenteredString(this.fontRendererObj, I18n.format("multiplayer.title", new Object[0]), this.width / 2, 20.0f, 16777215);
        super.drawScreen(mouseX, mouseY, partialTicks);
        if (this.hoveringText != null) {
            this.drawHoveringText(Lists.newArrayList(Splitter.on("\n").split((CharSequence)this.hoveringText)), mouseX, mouseY);
        }
    }
    
    public void connectToSelected() {
        final GuiListExtended.IGuiListEntry guilistextended$iguilistentry = (this.serverListSelector.getSelected() < 0) ? null : this.serverListSelector.getListEntry(this.serverListSelector.getSelected());
        if (guilistextended$iguilistentry instanceof ServerListEntryNormal) {
            this.connectToServer(((ServerListEntryNormal)guilistextended$iguilistentry).getServerData());
        }
        else if (guilistextended$iguilistentry instanceof ServerListEntryLanDetected) {
            final LanServerInfo lanserverinfo = ((ServerListEntryLanDetected)guilistextended$iguilistentry).getServerData();
            this.connectToServer(new ServerData(lanserverinfo.getServerMotd(), lanserverinfo.getServerIpPort(), true));
        }
    }
    
    private void connectToServer(final ServerData server) {
        this.mc.displayGuiScreen(new GuiConnecting(this, this.mc, server));
    }
    
    public void selectServer(final int index) {
        this.serverListSelector.setSelectedSlotIndex(index);
        final GuiListExtended.IGuiListEntry guilistextended$iguilistentry = (index < 0) ? null : this.serverListSelector.getListEntry(index);
        this.btnSelectServer.enabled = false;
        this.btnEditServer.enabled = false;
        this.btnDeleteServer.enabled = false;
        if (guilistextended$iguilistentry != null && !(guilistextended$iguilistentry instanceof ServerListEntryLanScan)) {
            this.btnSelectServer.enabled = true;
            if (guilistextended$iguilistentry instanceof ServerListEntryNormal) {
                this.btnEditServer.enabled = true;
                this.btnDeleteServer.enabled = true;
            }
        }
    }
    
    public ServerPinger getOldServerPinger() {
        return this.oldServerPinger;
    }
    
    public void setHoveringText(final String p_146793_1_) {
        this.hoveringText = p_146793_1_;
    }
    
    @Override
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        this.serverListSelector.mouseClicked(mouseX, mouseY, mouseButton);
    }
    
    @Override
    protected void mouseReleased(final int mouseX, final int mouseY, final int state) {
        super.mouseReleased(mouseX, mouseY, state);
        this.serverListSelector.mouseReleased(mouseX, mouseY, state);
    }
    
    public ServerList getServerList() {
        return this.savedServerList;
    }
    
    public boolean canMoveUp(final ServerListEntryNormal p_175392_1_, final int p_175392_2_) {
        return p_175392_2_ > 0;
    }
    
    public boolean canMoveDown(final ServerListEntryNormal p_175394_1_, final int p_175394_2_) {
        return p_175394_2_ < this.savedServerList.countServers() - 1;
    }
    
    public void moveServerUp(final ServerListEntryNormal p_175391_1_, final int p_175391_2_, final boolean p_175391_3_) {
        final int i = p_175391_3_ ? 0 : (p_175391_2_ - 1);
        this.savedServerList.swapServers(p_175391_2_, i);
        if (this.serverListSelector.getSelected() == p_175391_2_) {
            this.selectServer(i);
        }
        this.serverListSelector.updateOnlineServers(this.savedServerList);
    }
    
    public void moveServerDown(final ServerListEntryNormal p_175393_1_, final int p_175393_2_, final boolean p_175393_3_) {
        final int i = p_175393_3_ ? (this.savedServerList.countServers() - 1) : (p_175393_2_ + 1);
        this.savedServerList.swapServers(p_175393_2_, i);
        if (this.serverListSelector.getSelected() == p_175393_2_) {
            this.selectServer(i);
        }
        this.serverListSelector.updateOnlineServers(this.savedServerList);
    }
}
