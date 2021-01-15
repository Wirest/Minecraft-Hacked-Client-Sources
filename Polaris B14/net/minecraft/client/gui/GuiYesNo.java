/*    */ package net.minecraft.client.gui;
/*    */ 
/*    */ import com.google.common.collect.Lists;
/*    */ import java.util.List;
/*    */ import net.minecraft.client.resources.I18n;
/*    */ 
/*    */ public class GuiYesNo extends GuiScreen
/*    */ {
/*    */   protected GuiYesNoCallback parentScreen;
/*    */   protected String messageLine1;
/*    */   private String messageLine2;
/* 12 */   private final List<String> field_175298_s = Lists.newArrayList();
/*    */   protected String confirmButtonText;
/*    */   protected String cancelButtonText;
/*    */   protected int parentButtonClickedId;
/*    */   private int ticksUntilEnable;
/*    */   
/*    */   public GuiYesNo(GuiYesNoCallback p_i1082_1_, String p_i1082_2_, String p_i1082_3_, int p_i1082_4_) {
/* 19 */     this.parentScreen = p_i1082_1_;
/* 20 */     this.messageLine1 = p_i1082_2_;
/* 21 */     this.messageLine2 = p_i1082_3_;
/* 22 */     this.parentButtonClickedId = p_i1082_4_;
/* 23 */     this.confirmButtonText = I18n.format("gui.yes", new Object[0]);
/* 24 */     this.cancelButtonText = I18n.format("gui.no", new Object[0]);
/*    */   }
/*    */   
/*    */   public GuiYesNo(GuiYesNoCallback p_i1083_1_, String p_i1083_2_, String p_i1083_3_, String p_i1083_4_, String p_i1083_5_, int p_i1083_6_) {
/* 28 */     this.parentScreen = p_i1083_1_;
/* 29 */     this.messageLine1 = p_i1083_2_;
/* 30 */     this.messageLine2 = p_i1083_3_;
/* 31 */     this.confirmButtonText = p_i1083_4_;
/* 32 */     this.cancelButtonText = p_i1083_5_;
/* 33 */     this.parentButtonClickedId = p_i1083_6_;
/*    */   }
/*    */   
/*    */   public void initGui() {
/* 37 */     this.buttonList.add(new GuiOptionButton(0, width / 2 - 155, height / 6 + 96, this.confirmButtonText));
/* 38 */     this.buttonList.add(new GuiOptionButton(1, width / 2 - 155 + 160, height / 6 + 96, this.cancelButtonText));
/* 39 */     this.field_175298_s.clear();
/* 40 */     this.field_175298_s.addAll(this.fontRendererObj.listFormattedStringToWidth(this.messageLine2, width - 50));
/*    */   }
/*    */   
/*    */   protected void actionPerformed(GuiButton button) throws java.io.IOException {
/* 44 */     this.parentScreen.confirmClicked(button.id == 0, this.parentButtonClickedId);
/*    */   }
/*    */   
/*    */   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
/* 48 */     drawDefaultBackground();
/* 49 */     drawCenteredString(this.fontRendererObj, this.messageLine1, width / 2, 70, 16777215);
/* 50 */     int i = 90;
/*    */     
/* 52 */     for (String s : this.field_175298_s) {
/* 53 */       drawCenteredString(this.fontRendererObj, s, width / 2, i, 16777215);
/* 54 */       i += this.fontRendererObj.FONT_HEIGHT;
/*    */     }
/*    */     
/* 57 */     super.drawScreen(mouseX, mouseY, partialTicks);
/*    */   }
/*    */   
/*    */   public void setButtonDelay(int p_146350_1_) {
/* 61 */     this.ticksUntilEnable = p_146350_1_;
/*    */     
/* 63 */     for (GuiButton guibutton : this.buttonList) {
/* 64 */       guibutton.enabled = false;
/*    */     }
/*    */   }
/*    */   
/*    */   public void updateScreen() {
/* 69 */     super.updateScreen();
/* 70 */     if (--this.ticksUntilEnable == 0) {
/* 71 */       for (GuiButton guibutton : this.buttonList) {
/* 72 */         guibutton.enabled = true;
/*    */       }
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\gui\GuiYesNo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */