/*     */ package net.minecraft.client.gui;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.List;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import org.lwjgl.input.Keyboard;
/*     */ import rip.jutting.polaris.utils.MiscUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class GuiUseProxy
/*     */   extends GuiScreen
/*     */ {
/*     */   private GuiMainMenu prevMenu;
/*     */   private GuiTextField proxyBox;
/*  19 */   private String error = "";
/*     */   
/*     */   public GuiUseProxy(GuiMainMenu prevMainMenu)
/*     */   {
/*  23 */     this.prevMenu = prevMainMenu;
/*     */   }
/*     */   
/*     */ 
/*     */   public void updateScreen()
/*     */   {
/*  29 */     this.proxyBox.updateCursorCounter();
/*     */   }
/*     */   
/*     */ 
/*     */   public void initGui()
/*     */   {
/*  35 */     Keyboard.enableRepeatEvents(true);
/*  36 */     this.buttonList.clear();
/*  37 */     this.buttonList.add(
/*  38 */       new GuiButton(0, width / 2 - 100, height / 4 + 120 + 12, "Back"));
/*  39 */     this.buttonList.add(
/*  40 */       new GuiButton(1, width / 2 - 100, height / 4 + 72 + 12, "Connect"));
/*  41 */     this.buttonList.add(
/*  42 */       new GuiButton(2, width / 2 - 100, height / 4 + 96 + 12, "Disconnect"));
/*  43 */     this.proxyBox = 
/*  44 */       new GuiTextField(0, this.fontRendererObj, width / 2 - 100, 60, 200, 20);
/*  45 */     this.proxyBox.setFocused(true);
/*     */   }
/*     */   
/*     */   public void onGuiClosed()
/*     */   {
/*  50 */     Keyboard.enableRepeatEvents(false);
/*     */   }
/*     */   
/*     */ 
/*     */   protected void actionPerformed(GuiButton clickedButton)
/*     */   {
/*  56 */     if (clickedButton.enabled) {
/*  57 */       if (clickedButton.id == 0)
/*     */       {
/*  59 */         this.mc.displayGuiScreen(this.prevMenu);
/*  60 */       } else if (clickedButton.id == 1)
/*     */       {
/*     */ 
/*  63 */         if ((!this.proxyBox.getText().contains(":")) || 
/*  64 */           (this.proxyBox.getText().split(":").length != 2))
/*     */         {
/*  66 */           this.error = "Not a proxy!";
/*  67 */           return;
/*     */         }
/*     */         
/*  70 */         String[] parts = this.proxyBox.getText().split(":");
/*     */         
/*  72 */         if ((MiscUtils.isInteger(parts[1])) && 
/*  73 */           (Integer.parseInt(parts[1]) <= 65536)) {
/*  74 */           Integer.parseInt(parts[1]);
/*     */         }
/*     */         
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         try
/*     */         {
/*  87 */           System.setProperty("proxySet", "true");
/*  88 */           System.setProperty("socksProxyHost", parts[0]);
/*  89 */           System.setProperty("socksProxyPort", parts[1]);
/*     */         }
/*     */         catch (Exception e) {
/*  92 */           this.error = e.toString();
/*  93 */           return;
/*     */         }
/*     */         
/*  96 */         if (this.error.isEmpty())
/*     */         {
/*  98 */           this.mc.displayGuiScreen(this.prevMenu);
/*     */         }
/*     */       }
/* 101 */       else if (clickedButton.id == 2)
/*     */       {
/*     */ 
/* 104 */         System.setProperty("socksProxyHost", "");
/* 105 */         System.setProperty("socksProxyPort", "");
/*     */         
/* 107 */         this.mc.displayGuiScreen(this.prevMenu);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   protected void keyTyped(char par1, int par2)
/*     */   {
/* 114 */     this.proxyBox.textboxKeyTyped(par1, par2);
/*     */     
/* 116 */     if ((par2 == 28) || (par2 == 156)) {
/* 117 */       actionPerformed((GuiButton)this.buttonList.get(1));
/*     */     }
/*     */   }
/*     */   
/*     */   protected void mouseClicked(int par1, int par2, int par3) throws IOException
/*     */   {
/* 123 */     super.mouseClicked(par1, par2, par3);
/* 124 */     this.proxyBox.mouseClicked(par1, par2, par3);
/* 125 */     if (this.proxyBox.isFocused()) {
/* 126 */       this.error = "";
/*     */     }
/*     */   }
/*     */   
/*     */   public void drawScreen(int par1, int par2, float par3)
/*     */   {
/* 132 */     drawDefaultBackground();
/* 133 */     drawCenteredString(this.fontRendererObj, "Set proxy", width / 2, 20, 
/* 134 */       16777215);
/* 135 */     drawString(this.fontRendererObj, "IP:Port (SOCKS Only)", 
/* 136 */       width / 2 - 100, 47, 10526880);
/* 137 */     drawCenteredString(this.fontRendererObj, this.error, width / 2, 87, 16711680);
/* 138 */     String currentProxy = System.getProperty("socksProxyHost") + ":" + 
/* 139 */       System.getProperty("socksProxyPort");
/* 140 */     if ((currentProxy.equals(":")) || (currentProxy.equals("null:null")))
/* 141 */       currentProxy = "none";
/* 142 */     drawString(this.fontRendererObj, "Current proxy: " + currentProxy, 
/* 143 */       width / 2 - 100, 97, 10526880);
/* 144 */     this.proxyBox.drawTextBox();
/* 145 */     super.drawScreen(par1, par2, par3);
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\gui\GuiUseProxy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */