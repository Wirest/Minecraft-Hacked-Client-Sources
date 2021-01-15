/*    */ package net.minecraft.client.gui;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.resources.I18n;
/*    */ import net.minecraft.client.settings.GameSettings;
/*    */ import net.minecraft.entity.player.EnumPlayerModelParts;
/*    */ import net.minecraft.util.IChatComponent;
/*    */ 
/*    */ public class GuiCustomizeSkin extends GuiScreen
/*    */ {
/*    */   private final GuiScreen parentScreen;
/*    */   private String title;
/*    */   
/*    */   public GuiCustomizeSkin(GuiScreen parentScreenIn)
/*    */   {
/* 17 */     this.parentScreen = parentScreenIn;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public void initGui()
/*    */   {
/* 26 */     int i = 0;
/* 27 */     this.title = I18n.format("options.skinCustomisation.title", new Object[0]);
/*    */     EnumPlayerModelParts[] arrayOfEnumPlayerModelParts;
/* 29 */     int j = (arrayOfEnumPlayerModelParts = EnumPlayerModelParts.values()).length; for (int i = 0; i < j; i++) { EnumPlayerModelParts enumplayermodelparts = arrayOfEnumPlayerModelParts[i];
/*    */       
/* 31 */       this.buttonList.add(new ButtonPart(enumplayermodelparts.getPartId(), width / 2 - 155 + i % 2 * 160, height / 6 + 24 * (i >> 1), 150, 20, enumplayermodelparts, null));
/* 32 */       i++;
/*    */     }
/*    */     
/* 35 */     if (i % 2 == 1)
/*    */     {
/* 37 */       i++;
/*    */     }
/*    */     
/* 40 */     this.buttonList.add(new GuiButton(200, width / 2 - 100, height / 6 + 24 * (i >> 1), I18n.format("gui.done", new Object[0])));
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   protected void actionPerformed(GuiButton button)
/*    */     throws java.io.IOException
/*    */   {
/* 48 */     if (button.enabled)
/*    */     {
/* 50 */       if (button.id == 200)
/*    */       {
/* 52 */         this.mc.gameSettings.saveOptions();
/* 53 */         this.mc.displayGuiScreen(this.parentScreen);
/*    */       }
/* 55 */       else if ((button instanceof ButtonPart))
/*    */       {
/* 57 */         EnumPlayerModelParts enumplayermodelparts = ((ButtonPart)button).playerModelParts;
/* 58 */         this.mc.gameSettings.switchModelPartEnabled(enumplayermodelparts);
/* 59 */         button.displayString = func_175358_a(enumplayermodelparts);
/*    */       }
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void drawScreen(int mouseX, int mouseY, float partialTicks)
/*    */   {
/* 69 */     drawDefaultBackground();
/* 70 */     drawCenteredString(this.fontRendererObj, this.title, width / 2, 20, 16777215);
/* 71 */     super.drawScreen(mouseX, mouseY, partialTicks);
/*    */   }
/*    */   
/*    */   private String func_175358_a(EnumPlayerModelParts playerModelParts)
/*    */   {
/*    */     String s;
/*    */     String s;
/* 78 */     if (this.mc.gameSettings.getModelParts().contains(playerModelParts))
/*    */     {
/* 80 */       s = I18n.format("options.on", new Object[0]);
/*    */     }
/*    */     else
/*    */     {
/* 84 */       s = I18n.format("options.off", new Object[0]);
/*    */     }
/*    */     
/* 87 */     return playerModelParts.func_179326_d().getFormattedText() + ": " + s;
/*    */   }
/*    */   
/*    */   class ButtonPart extends GuiButton
/*    */   {
/*    */     private final EnumPlayerModelParts playerModelParts;
/*    */     
/*    */     private ButtonPart(int p_i45514_2_, int p_i45514_3_, int p_i45514_4_, int p_i45514_5_, int p_i45514_6_, EnumPlayerModelParts playerModelParts)
/*    */     {
/* 96 */       super(p_i45514_3_, p_i45514_4_, p_i45514_5_, p_i45514_6_, GuiCustomizeSkin.this.func_175358_a(playerModelParts));
/* 97 */       this.playerModelParts = playerModelParts;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\gui\GuiCustomizeSkin.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */