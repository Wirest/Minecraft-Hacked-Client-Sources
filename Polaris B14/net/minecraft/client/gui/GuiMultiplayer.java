/*     */ package net.minecraft.client.gui;
/*     */ 
/*     */ import com.google.common.base.Splitter;
/*     */ import java.io.IOException;
/*     */ import java.util.List;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.multiplayer.ServerData;
/*     */ import net.minecraft.client.multiplayer.ServerList;
/*     */ import net.minecraft.client.network.LanServerDetector.LanServer;
/*     */ import net.minecraft.client.network.LanServerDetector.LanServerList;
/*     */ import net.minecraft.client.network.LanServerDetector.ThreadLanServerFind;
/*     */ import net.minecraft.client.network.OldServerPinger;
/*     */ import net.minecraft.client.resources.I18n;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ public class GuiMultiplayer extends GuiScreen implements GuiYesNoCallback
/*     */ {
/*  18 */   private static final Logger logger = ;
/*  19 */   private final OldServerPinger oldServerPinger = new OldServerPinger();
/*     */   private GuiScreen parentScreen;
/*     */   private ServerSelectionList serverListSelector;
/*     */   private ServerList savedServerList;
/*     */   private GuiButton btnEditServer;
/*     */   private GuiButton btnSelectServer;
/*     */   private GuiButton btnDeleteServer;
/*     */   private boolean deletingServer;
/*     */   private boolean addingServer;
/*     */   private boolean editingServer;
/*     */   private boolean directConnect;
/*     */   private String hoveringText;
/*     */   private ServerData selectedServer;
/*     */   private LanServerDetector.LanServerList lanServerList;
/*     */   private LanServerDetector.ThreadLanServerFind lanServerDetector;
/*     */   private boolean initialized;
/*     */   
/*     */   public GuiMultiplayer(GuiScreen parentScreen) {
/*  37 */     this.parentScreen = parentScreen;
/*     */   }
/*     */   
/*     */   public void initGui() {
/*  41 */     org.lwjgl.input.Keyboard.enableRepeatEvents(true);
/*  42 */     this.buttonList.clear();
/*  43 */     if (!this.initialized) {
/*  44 */       this.initialized = true;
/*  45 */       this.savedServerList = new ServerList(this.mc);
/*  46 */       this.savedServerList.loadServerList();
/*  47 */       this.lanServerList = new LanServerDetector.LanServerList();
/*     */       try
/*     */       {
/*  50 */         this.lanServerDetector = new LanServerDetector.ThreadLanServerFind(this.lanServerList);
/*  51 */         this.lanServerDetector.start();
/*     */       } catch (Exception exception) {
/*  53 */         logger.warn("Unable to start LAN server detection: " + exception.getMessage());
/*     */       }
/*     */       
/*  56 */       this.serverListSelector = new ServerSelectionList(this, this.mc, width, height, 32, height - 64, 36);
/*  57 */       this.serverListSelector.func_148195_a(this.savedServerList);
/*     */     } else {
/*  59 */       this.serverListSelector.setDimensions(width, height, 32, height - 64);
/*     */     }
/*     */     
/*  62 */     createButtons();
/*     */   }
/*     */   
/*     */   public void handleMouseInput() throws IOException {
/*  66 */     super.handleMouseInput();
/*  67 */     this.serverListSelector.handleMouseInput();
/*     */   }
/*     */   
/*     */   public void createButtons() {
/*  71 */     this.buttonList.add(this.btnEditServer = new GuiButton(7, width / 2 - 154, height - 28, 70, 20, I18n.format("selectServer.edit", new Object[0])));
/*  72 */     this.buttonList.add(this.btnDeleteServer = new GuiButton(2, width / 2 - 74, height - 28, 70, 20, I18n.format("selectServer.delete", new Object[0])));
/*  73 */     this.buttonList.add(this.btnSelectServer = new GuiButton(1, width / 2 - 154, height - 52, 100, 20, I18n.format("selectServer.select", new Object[0])));
/*  74 */     this.buttonList.add(new GuiButton(4, width / 2 - 50, height - 52, 100, 20, I18n.format("selectServer.direct", new Object[0])));
/*  75 */     this.buttonList.add(new GuiButton(3, width / 2 + 4 + 50, height - 52, 100, 20, I18n.format("selectServer.add", new Object[0])));
/*  76 */     this.buttonList.add(new GuiButton(8, width / 2 + 4, height - 28, 70, 20, I18n.format("selectServer.refresh", new Object[0])));
/*  77 */     this.buttonList.add(new GuiButton(0, width / 2 + 4 + 76, height - 28, 75, 20, I18n.format("gui.cancel", new Object[0])));
/*  78 */     selectServer(this.serverListSelector.func_148193_k());
/*     */   }
/*     */   
/*     */   public void updateScreen() {
/*  82 */     super.updateScreen();
/*  83 */     if (this.lanServerList.getWasUpdated()) {
/*  84 */       List<LanServerDetector.LanServer> list = this.lanServerList.getLanServers();
/*  85 */       this.lanServerList.setWasNotUpdated();
/*  86 */       this.serverListSelector.func_148194_a(list);
/*     */     }
/*     */     
/*  89 */     this.oldServerPinger.pingPendingNetworks();
/*     */   }
/*     */   
/*     */   public void onGuiClosed() {
/*  93 */     org.lwjgl.input.Keyboard.enableRepeatEvents(false);
/*  94 */     if (this.lanServerDetector != null) {
/*  95 */       this.lanServerDetector.interrupt();
/*  96 */       this.lanServerDetector = null;
/*     */     }
/*     */     
/*  99 */     this.oldServerPinger.clearPendingNetworks();
/*     */   }
/*     */   
/*     */   protected void actionPerformed(GuiButton button) throws IOException {
/* 103 */     if (button.enabled) {
/* 104 */       GuiListExtended.IGuiListEntry guilistextended$iguilistentry = this.serverListSelector.func_148193_k() < 0 ? null : this.serverListSelector.getListEntry(this.serverListSelector.func_148193_k());
/* 105 */       if ((button.id == 2) && ((guilistextended$iguilistentry instanceof ServerListEntryNormal))) {
/* 106 */         String s4 = ((ServerListEntryNormal)guilistextended$iguilistentry).getServerData().serverName;
/* 107 */         if (s4 != null) {
/* 108 */           this.deletingServer = true;
/* 109 */           String s = I18n.format("selectServer.deleteQuestion", new Object[0]);
/* 110 */           String s1 = "'" + s4 + "' " + I18n.format("selectServer.deleteWarning", new Object[0]);
/* 111 */           String s2 = I18n.format("selectServer.deleteButton", new Object[0]);
/* 112 */           String s3 = I18n.format("gui.cancel", new Object[0]);
/* 113 */           GuiYesNo guiyesno = new GuiYesNo(this, s, s1, s2, s3, this.serverListSelector.func_148193_k());
/* 114 */           this.mc.displayGuiScreen(guiyesno);
/*     */         }
/* 116 */       } else if (button.id == 1) {
/* 117 */         connectToSelected();
/* 118 */       } else if (button.id == 4) {
/* 119 */         this.directConnect = true;
/* 120 */         this.mc.displayGuiScreen(new GuiScreenServerList(this, this.selectedServer = new ServerData(I18n.format("selectServer.defaultName", new Object[0]), "", false)));
/* 121 */       } else if (button.id == 3) {
/* 122 */         this.addingServer = true;
/* 123 */         this.mc.displayGuiScreen(new GuiScreenAddServer(this, this.selectedServer = new ServerData(I18n.format("selectServer.defaultName", new Object[0]), "", false)));
/* 124 */       } else if ((button.id == 7) && ((guilistextended$iguilistentry instanceof ServerListEntryNormal))) {
/* 125 */         this.editingServer = true;
/* 126 */         ServerData serverdata = ((ServerListEntryNormal)guilistextended$iguilistentry).getServerData();
/* 127 */         this.selectedServer = new ServerData(serverdata.serverName, serverdata.serverIP, false);
/* 128 */         this.selectedServer.copyFrom(serverdata);
/* 129 */         this.mc.displayGuiScreen(new GuiScreenAddServer(this, this.selectedServer));
/* 130 */       } else if (button.id == 0) {
/* 131 */         this.mc.displayGuiScreen(this.parentScreen);
/* 132 */       } else if (button.id == 8) {
/* 133 */         refreshServerList();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private void refreshServerList() {
/* 139 */     this.mc.displayGuiScreen(new GuiMultiplayer(this.parentScreen));
/*     */   }
/*     */   
/*     */   public void confirmClicked(boolean result, int id) {
/* 143 */     GuiListExtended.IGuiListEntry guilistextended$iguilistentry = this.serverListSelector.func_148193_k() < 0 ? null : this.serverListSelector.getListEntry(this.serverListSelector.func_148193_k());
/* 144 */     if (this.deletingServer) {
/* 145 */       this.deletingServer = false;
/* 146 */       if ((result) && ((guilistextended$iguilistentry instanceof ServerListEntryNormal))) {
/* 147 */         this.savedServerList.removeServerData(this.serverListSelector.func_148193_k());
/* 148 */         this.savedServerList.saveServerList();
/* 149 */         this.serverListSelector.setSelectedSlotIndex(-1);
/* 150 */         this.serverListSelector.func_148195_a(this.savedServerList);
/*     */       }
/*     */       
/* 153 */       this.mc.displayGuiScreen(this);
/* 154 */     } else if (this.directConnect) {
/* 155 */       this.directConnect = false;
/* 156 */       if (result) {
/* 157 */         connectToServer(this.selectedServer);
/*     */       } else {
/* 159 */         this.mc.displayGuiScreen(this);
/*     */       }
/* 161 */     } else if (this.addingServer) {
/* 162 */       this.addingServer = false;
/* 163 */       if (result) {
/* 164 */         this.savedServerList.addServerData(this.selectedServer);
/* 165 */         this.savedServerList.saveServerList();
/* 166 */         this.serverListSelector.setSelectedSlotIndex(-1);
/* 167 */         this.serverListSelector.func_148195_a(this.savedServerList);
/*     */       }
/*     */       
/* 170 */       this.mc.displayGuiScreen(this);
/* 171 */     } else if (this.editingServer) {
/* 172 */       this.editingServer = false;
/* 173 */       if ((result) && ((guilistextended$iguilistentry instanceof ServerListEntryNormal))) {
/* 174 */         ServerData serverdata = ((ServerListEntryNormal)guilistextended$iguilistentry).getServerData();
/* 175 */         serverdata.serverName = this.selectedServer.serverName;
/* 176 */         serverdata.serverIP = this.selectedServer.serverIP;
/* 177 */         serverdata.copyFrom(this.selectedServer);
/* 178 */         this.savedServerList.saveServerList();
/* 179 */         this.serverListSelector.func_148195_a(this.savedServerList);
/*     */       }
/*     */       
/* 182 */       this.mc.displayGuiScreen(this);
/*     */     }
/*     */   }
/*     */   
/*     */   protected void keyTyped(char typedChar, int keyCode) throws IOException {
/* 187 */     int i = this.serverListSelector.func_148193_k();
/* 188 */     GuiListExtended.IGuiListEntry guilistextended$iguilistentry = i < 0 ? null : this.serverListSelector.getListEntry(i);
/* 189 */     if (keyCode == 63) {
/* 190 */       refreshServerList();
/*     */     }
/* 192 */     else if (i >= 0) {
/* 193 */       if (keyCode == 200) {
/* 194 */         if (isShiftKeyDown()) {
/* 195 */           if ((i > 0) && ((guilistextended$iguilistentry instanceof ServerListEntryNormal))) {
/* 196 */             this.savedServerList.swapServers(i, i - 1);
/* 197 */             selectServer(this.serverListSelector.func_148193_k() - 1);
/* 198 */             this.serverListSelector.scrollBy(-this.serverListSelector.getSlotHeight());
/* 199 */             this.serverListSelector.func_148195_a(this.savedServerList);
/*     */           }
/* 201 */         } else if (i > 0) {
/* 202 */           selectServer(this.serverListSelector.func_148193_k() - 1);
/* 203 */           this.serverListSelector.scrollBy(-this.serverListSelector.getSlotHeight());
/* 204 */           if ((this.serverListSelector.getListEntry(this.serverListSelector.func_148193_k()) instanceof ServerListEntryLanScan)) {
/* 205 */             if (this.serverListSelector.func_148193_k() > 0) {
/* 206 */               selectServer(this.serverListSelector.getSize() - 1);
/* 207 */               this.serverListSelector.scrollBy(-this.serverListSelector.getSlotHeight());
/*     */             } else {
/* 209 */               selectServer(-1);
/*     */             }
/*     */           }
/*     */         } else {
/* 213 */           selectServer(-1);
/*     */         }
/* 215 */       } else if (keyCode == 208) {
/* 216 */         if (isShiftKeyDown()) {
/* 217 */           if (i < this.savedServerList.countServers() - 1) {
/* 218 */             this.savedServerList.swapServers(i, i + 1);
/* 219 */             selectServer(i + 1);
/* 220 */             this.serverListSelector.scrollBy(this.serverListSelector.getSlotHeight());
/* 221 */             this.serverListSelector.func_148195_a(this.savedServerList);
/*     */           }
/* 223 */         } else if (i < this.serverListSelector.getSize()) {
/* 224 */           selectServer(this.serverListSelector.func_148193_k() + 1);
/* 225 */           this.serverListSelector.scrollBy(this.serverListSelector.getSlotHeight());
/* 226 */           if ((this.serverListSelector.getListEntry(this.serverListSelector.func_148193_k()) instanceof ServerListEntryLanScan)) {
/* 227 */             if (this.serverListSelector.func_148193_k() < this.serverListSelector.getSize() - 1) {
/* 228 */               selectServer(this.serverListSelector.getSize() + 1);
/* 229 */               this.serverListSelector.scrollBy(this.serverListSelector.getSlotHeight());
/*     */             } else {
/* 231 */               selectServer(-1);
/*     */             }
/*     */           }
/*     */         } else {
/* 235 */           selectServer(-1);
/*     */         }
/* 237 */       } else if ((keyCode != 28) && (keyCode != 156)) {
/* 238 */         super.keyTyped(typedChar, keyCode);
/*     */       } else {
/* 240 */         actionPerformed((GuiButton)this.buttonList.get(2));
/*     */       }
/*     */     } else {
/* 243 */       super.keyTyped(typedChar, keyCode);
/*     */     }
/*     */   }
/*     */   
/*     */   public void drawScreen(int mouseX, int mouseY, float partialTicks)
/*     */   {
/* 249 */     this.hoveringText = null;
/* 250 */     drawDefaultBackground();
/* 251 */     this.serverListSelector.drawScreen(mouseX, mouseY, partialTicks);
/* 252 */     drawCenteredString(this.fontRendererObj, I18n.format("multiplayer.title", new Object[0]), width / 2, 20, 16777215);
/* 253 */     super.drawScreen(mouseX, mouseY, partialTicks);
/* 254 */     if (this.hoveringText != null) {
/* 255 */       drawHoveringText(com.google.common.collect.Lists.newArrayList(Splitter.on("\n").split(this.hoveringText)), mouseX, mouseY);
/*     */     }
/*     */   }
/*     */   
/*     */   public void connectToSelected() {
/* 260 */     GuiListExtended.IGuiListEntry guilistextended$iguilistentry = this.serverListSelector.func_148193_k() < 0 ? null : this.serverListSelector.getListEntry(this.serverListSelector.func_148193_k());
/* 261 */     if ((guilistextended$iguilistentry instanceof ServerListEntryNormal)) {
/* 262 */       connectToServer(((ServerListEntryNormal)guilistextended$iguilistentry).getServerData());
/* 263 */     } else if ((guilistextended$iguilistentry instanceof ServerListEntryLanDetected)) {
/* 264 */       LanServerDetector.LanServer lanserverdetector$lanserver = ((ServerListEntryLanDetected)guilistextended$iguilistentry).getLanServer();
/* 265 */       connectToServer(new ServerData(lanserverdetector$lanserver.getServerMotd(), lanserverdetector$lanserver.getServerIpPort(), true));
/*     */     }
/*     */   }
/*     */   
/*     */   private void connectToServer(ServerData server) {
/* 270 */     this.mc.displayGuiScreen(new net.minecraft.client.multiplayer.GuiConnecting(this, this.mc, server));
/*     */   }
/*     */   
/*     */   public void selectServer(int index) {
/* 274 */     this.serverListSelector.setSelectedSlotIndex(index);
/* 275 */     GuiListExtended.IGuiListEntry guilistextended$iguilistentry = index < 0 ? null : this.serverListSelector.getListEntry(index);
/* 276 */     this.btnSelectServer.enabled = false;
/* 277 */     this.btnEditServer.enabled = false;
/* 278 */     this.btnDeleteServer.enabled = false;
/* 279 */     if ((guilistextended$iguilistentry != null) && (!(guilistextended$iguilistentry instanceof ServerListEntryLanScan))) {
/* 280 */       this.btnSelectServer.enabled = true;
/* 281 */       if ((guilistextended$iguilistentry instanceof ServerListEntryNormal)) {
/* 282 */         this.btnEditServer.enabled = true;
/* 283 */         this.btnDeleteServer.enabled = true;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public OldServerPinger getOldServerPinger() {
/* 289 */     return this.oldServerPinger;
/*     */   }
/*     */   
/*     */   public void setHoveringText(String p_146793_1_) {
/* 293 */     this.hoveringText = p_146793_1_;
/*     */   }
/*     */   
/*     */   protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
/* 297 */     super.mouseClicked(mouseX, mouseY, mouseButton);
/* 298 */     this.serverListSelector.mouseClicked(mouseX, mouseY, mouseButton);
/*     */   }
/*     */   
/*     */   protected void mouseReleased(int mouseX, int mouseY, int state) {
/* 302 */     super.mouseReleased(mouseX, mouseY, state);
/* 303 */     this.serverListSelector.mouseReleased(mouseX, mouseY, state);
/*     */   }
/*     */   
/*     */   public ServerList getServerList() {
/* 307 */     return this.savedServerList;
/*     */   }
/*     */   
/*     */   public boolean func_175392_a(ServerListEntryNormal p_175392_1_, int p_175392_2_) {
/* 311 */     return p_175392_2_ > 0;
/*     */   }
/*     */   
/*     */   public boolean func_175394_b(ServerListEntryNormal p_175394_1_, int p_175394_2_) {
/* 315 */     return p_175394_2_ < this.savedServerList.countServers() - 1;
/*     */   }
/*     */   
/*     */   public void func_175391_a(ServerListEntryNormal p_175391_1_, int p_175391_2_, boolean p_175391_3_) {
/* 319 */     int i = p_175391_3_ ? 0 : p_175391_2_ - 1;
/* 320 */     this.savedServerList.swapServers(p_175391_2_, i);
/* 321 */     if (this.serverListSelector.func_148193_k() == p_175391_2_) {
/* 322 */       selectServer(i);
/*     */     }
/*     */     
/* 325 */     this.serverListSelector.func_148195_a(this.savedServerList);
/*     */   }
/*     */   
/*     */   public void func_175393_b(ServerListEntryNormal p_175393_1_, int p_175393_2_, boolean p_175393_3_) {
/* 329 */     int i = p_175393_3_ ? this.savedServerList.countServers() - 1 : p_175393_2_ + 1;
/* 330 */     this.savedServerList.swapServers(p_175393_2_, i);
/* 331 */     if (this.serverListSelector.func_148193_k() == p_175393_2_) {
/* 332 */       selectServer(i);
/*     */     }
/*     */     
/* 335 */     this.serverListSelector.func_148195_a(this.savedServerList);
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\gui\GuiMultiplayer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */