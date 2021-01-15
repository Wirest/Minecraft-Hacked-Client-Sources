/*     */ package net.minecraft.client.gui.stream;
/*     */ 
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.FontRenderer;
/*     */ import net.minecraft.client.gui.GuiButton;
/*     */ import net.minecraft.client.gui.GuiScreen;
/*     */ import net.minecraft.client.settings.GameSettings;
/*     */ import net.minecraft.client.stream.IStream;
/*     */ import net.minecraft.client.stream.IngestServerTester;
/*     */ import net.minecraft.util.EnumChatFormatting;
/*     */ import tv.twitch.broadcast.IngestServer;
/*     */ 
/*     */ public class GuiIngestServers extends GuiScreen
/*     */ {
/*     */   private final GuiScreen field_152309_a;
/*     */   private String field_152310_f;
/*     */   private ServerList field_152311_g;
/*     */   
/*     */   public GuiIngestServers(GuiScreen p_i46312_1_)
/*     */   {
/*  21 */     this.field_152309_a = p_i46312_1_;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void initGui()
/*     */   {
/*  30 */     this.field_152310_f = net.minecraft.client.resources.I18n.format("options.stream.ingest.title", new Object[0]);
/*  31 */     this.field_152311_g = new ServerList(this.mc);
/*     */     
/*  33 */     if (!this.mc.getTwitchStream().func_152908_z())
/*     */     {
/*  35 */       this.mc.getTwitchStream().func_152909_x();
/*     */     }
/*     */     
/*  38 */     this.buttonList.add(new GuiButton(1, width / 2 - 155, height - 24 - 6, 150, 20, net.minecraft.client.resources.I18n.format("gui.done", new Object[0])));
/*  39 */     this.buttonList.add(new GuiButton(2, width / 2 + 5, height - 24 - 6, 150, 20, net.minecraft.client.resources.I18n.format("options.stream.ingest.reset", new Object[0])));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void handleMouseInput()
/*     */     throws java.io.IOException
/*     */   {
/*  47 */     super.handleMouseInput();
/*  48 */     this.field_152311_g.handleMouseInput();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onGuiClosed()
/*     */   {
/*  56 */     if (this.mc.getTwitchStream().func_152908_z())
/*     */     {
/*  58 */       this.mc.getTwitchStream().func_152932_y().func_153039_l();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected void actionPerformed(GuiButton button)
/*     */     throws java.io.IOException
/*     */   {
/*  67 */     if (button.enabled)
/*     */     {
/*  69 */       if (button.id == 1)
/*     */       {
/*  71 */         this.mc.displayGuiScreen(this.field_152309_a);
/*     */       }
/*     */       else
/*     */       {
/*  75 */         this.mc.gameSettings.streamPreferredServer = "";
/*  76 */         this.mc.gameSettings.saveOptions();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void drawScreen(int mouseX, int mouseY, float partialTicks)
/*     */   {
/*  86 */     drawDefaultBackground();
/*  87 */     this.field_152311_g.drawScreen(mouseX, mouseY, partialTicks);
/*  88 */     drawCenteredString(this.fontRendererObj, this.field_152310_f, width / 2, 20, 16777215);
/*  89 */     super.drawScreen(mouseX, mouseY, partialTicks);
/*     */   }
/*     */   
/*     */   class ServerList extends net.minecraft.client.gui.GuiSlot
/*     */   {
/*     */     public ServerList(Minecraft mcIn)
/*     */     {
/*  96 */       super(GuiIngestServers.width, GuiIngestServers.height, 32, GuiIngestServers.height - 35, (int)(mcIn.fontRendererObj.FONT_HEIGHT * 3.5D));
/*  97 */       setShowSelectionBox(false);
/*     */     }
/*     */     
/*     */     protected int getSize()
/*     */     {
/* 102 */       return this.mc.getTwitchStream().func_152925_v().length;
/*     */     }
/*     */     
/*     */     protected void elementClicked(int slotIndex, boolean isDoubleClick, int mouseX, int mouseY)
/*     */     {
/* 107 */       this.mc.gameSettings.streamPreferredServer = this.mc.getTwitchStream().func_152925_v()[slotIndex].serverUrl;
/* 108 */       this.mc.gameSettings.saveOptions();
/*     */     }
/*     */     
/*     */     protected boolean isSelected(int slotIndex)
/*     */     {
/* 113 */       return this.mc.getTwitchStream().func_152925_v()[slotIndex].serverUrl.equals(this.mc.gameSettings.streamPreferredServer);
/*     */     }
/*     */     
/*     */ 
/*     */     protected void drawBackground() {}
/*     */     
/*     */ 
/*     */     protected void drawSlot(int entryID, int p_180791_2_, int p_180791_3_, int p_180791_4_, int mouseXIn, int mouseYIn)
/*     */     {
/* 122 */       IngestServer ingestserver = this.mc.getTwitchStream().func_152925_v()[entryID];
/* 123 */       String s = ingestserver.serverUrl.replaceAll("\\{stream_key\\}", "");
/* 124 */       String s1 = (int)ingestserver.bitrateKbps + " kbps";
/* 125 */       String s2 = null;
/* 126 */       IngestServerTester ingestservertester = this.mc.getTwitchStream().func_152932_y();
/*     */       
/* 128 */       if (ingestservertester != null)
/*     */       {
/* 130 */         if (ingestserver == ingestservertester.func_153040_c())
/*     */         {
/* 132 */           s = EnumChatFormatting.GREEN + s;
/* 133 */           s1 = (int)(ingestservertester.func_153030_h() * 100.0F) + "%";
/*     */         }
/* 135 */         else if (entryID < ingestservertester.func_153028_p())
/*     */         {
/* 137 */           if (ingestserver.bitrateKbps == 0.0F)
/*     */           {
/* 139 */             s1 = EnumChatFormatting.RED + "Down!";
/*     */           }
/*     */         }
/*     */         else
/*     */         {
/* 144 */           s1 = EnumChatFormatting.OBFUSCATED + "1234" + EnumChatFormatting.RESET + " kbps";
/*     */         }
/*     */       }
/* 147 */       else if (ingestserver.bitrateKbps == 0.0F)
/*     */       {
/* 149 */         s1 = EnumChatFormatting.RED + "Down!";
/*     */       }
/*     */       
/* 152 */       p_180791_2_ -= 15;
/*     */       
/* 154 */       if (isSelected(entryID))
/*     */       {
/* 156 */         s2 = EnumChatFormatting.BLUE + "(Preferred)";
/*     */       }
/* 158 */       else if (ingestserver.defaultServer)
/*     */       {
/* 160 */         s2 = EnumChatFormatting.GREEN + "(Default)";
/*     */       }
/*     */       
/* 163 */       GuiIngestServers.this.drawString(GuiIngestServers.this.fontRendererObj, ingestserver.serverName, p_180791_2_ + 2, p_180791_3_ + 5, 16777215);
/* 164 */       GuiIngestServers.this.drawString(GuiIngestServers.this.fontRendererObj, s, p_180791_2_ + 2, p_180791_3_ + GuiIngestServers.this.fontRendererObj.FONT_HEIGHT + 5 + 3, 3158064);
/* 165 */       GuiIngestServers.this.drawString(GuiIngestServers.this.fontRendererObj, s1, getScrollBarX() - 5 - GuiIngestServers.this.fontRendererObj.getStringWidth(s1), p_180791_3_ + 5, 8421504);
/*     */       
/* 167 */       if (s2 != null)
/*     */       {
/* 169 */         GuiIngestServers.this.drawString(GuiIngestServers.this.fontRendererObj, s2, getScrollBarX() - 5 - GuiIngestServers.this.fontRendererObj.getStringWidth(s2), p_180791_3_ + 5 + 3 + GuiIngestServers.this.fontRendererObj.FONT_HEIGHT, 8421504);
/*     */       }
/*     */     }
/*     */     
/*     */     protected int getScrollBarX()
/*     */     {
/* 175 */       return super.getScrollBarX() + 15;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\gui\stream\GuiIngestServers.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */