/*    */ package net.minecraft.client.gui;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.resources.I18n;
/*    */ import net.minecraft.server.integrated.IntegratedServer;
/*    */ import net.minecraft.util.IChatComponent;
/*    */ 
/*    */ public class GuiShareToLan extends GuiScreen
/*    */ {
/*    */   private final GuiScreen field_146598_a;
/*    */   private GuiButton field_146596_f;
/*    */   private GuiButton field_146597_g;
/* 14 */   private String field_146599_h = "survival";
/*    */   private boolean field_146600_i;
/*    */   
/*    */   public GuiShareToLan(GuiScreen p_i1055_1_) {
/* 18 */     this.field_146598_a = p_i1055_1_;
/*    */   }
/*    */   
/*    */   public void initGui() {
/* 22 */     this.buttonList.clear();
/* 23 */     this.buttonList.add(new GuiButton(101, width / 2 - 155, height - 28, 150, 20, I18n.format("lanServer.start", new Object[0])));
/* 24 */     this.buttonList.add(new GuiButton(102, width / 2 + 5, height - 28, 150, 20, I18n.format("gui.cancel", new Object[0])));
/* 25 */     this.buttonList.add(this.field_146597_g = new GuiButton(104, width / 2 - 155, 100, 150, 20, I18n.format("selectWorld.gameMode", new Object[0])));
/* 26 */     this.buttonList.add(this.field_146596_f = new GuiButton(103, width / 2 + 5, 100, 150, 20, I18n.format("selectWorld.allowCommands", new Object[0])));
/* 27 */     func_146595_g();
/*    */   }
/*    */   
/*    */   private void func_146595_g() {
/* 31 */     this.field_146597_g.displayString = (I18n.format("selectWorld.gameMode", new Object[0]) + " " + I18n.format(new StringBuilder("selectWorld.gameMode.").append(this.field_146599_h).toString(), new Object[0]));
/* 32 */     this.field_146596_f.displayString = (I18n.format("selectWorld.allowCommands", new Object[0]) + " ");
/* 33 */     if (this.field_146600_i) {
/* 34 */       this.field_146596_f.displayString += I18n.format("options.on", new Object[0]);
/*    */     } else {
/* 36 */       this.field_146596_f.displayString += I18n.format("options.off", new Object[0]);
/*    */     }
/*    */   }
/*    */   
/*    */   protected void actionPerformed(GuiButton button) throws java.io.IOException {
/* 41 */     if (button.id == 102) {
/* 42 */       this.mc.displayGuiScreen(this.field_146598_a);
/* 43 */     } else if (button.id == 104) {
/* 44 */       if (this.field_146599_h.equals("spectator")) {
/* 45 */         this.field_146599_h = "creative";
/* 46 */       } else if (this.field_146599_h.equals("creative")) {
/* 47 */         this.field_146599_h = "adventure";
/* 48 */       } else if (this.field_146599_h.equals("adventure")) {
/* 49 */         this.field_146599_h = "survival";
/*    */       } else {
/* 51 */         this.field_146599_h = "spectator";
/*    */       }
/*    */       
/* 54 */       func_146595_g();
/* 55 */     } else if (button.id == 103) {
/* 56 */       this.field_146600_i = (!this.field_146600_i);
/* 57 */       func_146595_g();
/* 58 */     } else if (button.id == 101) {
/* 59 */       this.mc.displayGuiScreen(null);
/* 60 */       String s = this.mc.getIntegratedServer().shareToLAN(net.minecraft.world.WorldSettings.GameType.getByName(this.field_146599_h), this.field_146600_i);
/*    */       IChatComponent ichatcomponent;
/* 62 */       IChatComponent ichatcomponent; if (s != null) {
/* 63 */         ichatcomponent = new net.minecraft.util.ChatComponentTranslation("commands.publish.started", new Object[] { s });
/*    */       } else {
/* 65 */         ichatcomponent = new net.minecraft.util.ChatComponentText("commands.publish.failed");
/*    */       }
/*    */       
/* 68 */       this.mc.ingameGUI.getChatGUI().printChatMessage(ichatcomponent);
/*    */     }
/*    */   }
/*    */   
/*    */   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
/* 73 */     drawDefaultBackground();
/* 74 */     drawCenteredString(this.fontRendererObj, I18n.format("lanServer.title", new Object[0]), width / 2, 50, 16777215);
/* 75 */     drawCenteredString(this.fontRendererObj, I18n.format("lanServer.otherPlayers", new Object[0]), width / 2, 82, 16777215);
/* 76 */     super.drawScreen(mouseX, mouseY, partialTicks);
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\gui\GuiShareToLan.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */