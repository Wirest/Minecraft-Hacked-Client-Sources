/*    */ package me.slowly.client.irc.ui;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import me.slowly.client.Client;
/*    */ import me.slowly.client.irc.IRCProfile;
/*    */ import me.slowly.client.util.Colors;
/*    */ import me.slowly.client.util.fontmanager.UnicodeFontRenderer;
/*    */ import me.slowly.client.util.handler.MouseInputHandler;
/*    */ import net.minecraft.client.gui.Gui;
/*    */ 
/*    */ public class UIProfile
/*    */ {
/*    */   private IRCProfile profile;
/*    */   private MouseInputHandler handler;
/*    */   public boolean closeRequested;
/*    */   public boolean drag;
/*    */   private int x;
/*    */   private int y;
/*    */   private int x2;
/*    */   private int y2;
/*    */   private int width;
/*    */   private int height;
/*    */   
/*    */   public UIProfile(IRCProfile profile, MouseInputHandler handler)
/*    */   {
/* 26 */     this.profile = profile;
/* 27 */     this.width = 150;
/* 28 */     this.height = 200;
/* 29 */     this.handler = handler;
/*    */   }
/*    */   
/*    */   public void draw(int mouseX, int mouseY) {
/* 33 */     this.width = 150;
/* 34 */     this.height = 200;
/* 35 */     int stripHeight = 10;
/* 36 */     boolean hoverStrip = (mouseX >= this.x) && (mouseX <= this.x + this.width) && (mouseY >= this.y) && (mouseY <= this.y + stripHeight);
/* 37 */     me.slowly.client.util.RenderUtil.drawRoundedRect(this.x, this.y, this.x + this.width, this.y + this.height, 3.0F, -14866123);
/* 38 */     Gui.drawRect(this.x, this.y, this.x + this.width, this.y + stripHeight, -15525340);
/*    */     
/* 40 */     net.minecraft.client.gui.ScaledResolution res = new net.minecraft.client.gui.ScaledResolution(net.minecraft.client.Minecraft.getMinecraft());
/* 41 */     UnicodeFontRenderer font = Client.getInstance().getFontManager().simpleton16;
/* 42 */     font.drawString(this.profile.getName(), this.x + (this.width - font.getStringWidth(this.profile.getName())) / 2, this.y + stripHeight + 32 + 10, -1);
/* 43 */     font = Client.getInstance().getFontManager().simpleton13;
/* 44 */     String mcName = "MC-Name: " + this.profile.getMcName();
/* 45 */     String serverIP = "Server-IP: : " + this.profile.getStatus();
/* 46 */     font.drawString(mcName, this.x + (this.width - font.getStringWidth(mcName)) / 2, this.y + stripHeight + 32 + 30, -1);
/* 47 */     font.drawString(serverIP, this.x + (this.width - font.getStringWidth(serverIP)) / 2, this.y + stripHeight + 32 + 30 + font.FONT_HEIGHT + 2, -1);
/*    */     
/* 49 */     int imageSize = 32;
/* 50 */     me.slowly.client.util.RenderUtil.drawImage(new net.minecraft.util.ResourceLocation("slowly/user.png"), this.x + (this.width - imageSize) / 2, this.y + stripHeight + 5, imageSize, imageSize);
/*    */     
/* 52 */     int xClose = this.x + this.width - stripHeight;
/* 53 */     Gui.drawLine(xClose + 2, this.y + 2, this.x + this.width - 2, this.y + stripHeight - 2, new Color(Colors.GREY.c));
/* 54 */     Gui.drawLine(this.x + this.width - 2, this.y + 2, xClose + 2, this.y + stripHeight - 2, new Color(Colors.GREY.c));
/* 55 */     boolean hoverClose = (mouseX >= xClose) && (mouseX <= this.x + this.width) && (mouseY >= this.y) && (mouseY <= this.y + stripHeight);
/*    */     
/*    */ 
/*    */ 
/* 59 */     if (this.handler.canExcecute()) {
/* 60 */       if (hoverClose) {
/* 61 */         this.closeRequested = true;
/* 62 */       } else if (hoverStrip) {
/* 63 */         this.drag = true;
/* 64 */         this.x2 = (mouseX - this.x);
/* 65 */         this.y2 = (mouseY - this.y);
/*    */       } else {
/* 67 */         this.handler.clicked = false;
/*    */       }
/*    */     }
/* 70 */     if (!org.lwjgl.input.Mouse.isButtonDown(0))
/* 71 */       this.drag = false;
/* 72 */     if (this.drag) {
/* 73 */       this.x = (mouseX - this.x2);
/* 74 */       this.y = (mouseY - this.y2);
/*    */     }
/*    */   }
/*    */   
/*    */   public IRCProfile getProfile() {
/* 79 */     return this.profile;
/*    */   }
/*    */   
/*    */   public void setX(int x) {
/* 83 */     this.x = x;
/*    */   }
/*    */   
/*    */   public void setY(int y) {
/* 87 */     this.y = y;
/*    */   }
/*    */   
/*    */   public int getWidth() {
/* 91 */     return this.width;
/*    */   }
/*    */   
/*    */   public int getHeight() {
/* 95 */     return this.height;
/*    */   }
/*    */ }


 