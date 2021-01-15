/*    */ package rip.jutting.polaris.ui.config;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import rip.jutting.polaris.Polaris;
/*    */ import rip.jutting.polaris.ui.click.settings.Setting;
/*    */ import rip.jutting.polaris.ui.click.settings.SettingsManager;
/*    */ import rip.jutting.polaris.ui.fonth.CFontRenderer;
/*    */ 
/*    */ public class ConfigSlot extends net.minecraft.client.gui.GuiSlot
/*    */ {
/*    */   private GuiConfig guiConfig;
/*    */   int selected;
/*    */   
/*    */   public ConfigSlot(GuiConfig guiConfig)
/*    */   {
/* 16 */     super(net.minecraft.client.Minecraft.getMinecraft(), GuiConfig.width, GuiConfig.height, 32, GuiConfig.height - 60, 40);
/* 17 */     this.guiConfig = guiConfig;
/* 18 */     this.selected = 0;
/*    */   }
/*    */   
/*    */   protected int getContentHeight()
/*    */   {
/* 23 */     return getSize() * 40;
/*    */   }
/*    */   
/*    */   protected int getSize()
/*    */   {
/* 28 */     return this.guiConfig.getConfigs().size();
/*    */   }
/*    */   
/*    */ 
/*    */   protected void elementClicked(int slotIndex, boolean isDoubleClick, int mouseX, int mouseY)
/*    */   {
/* 34 */     this.selected = slotIndex;
/*    */   }
/*    */   
/*    */   protected boolean isSelected(int slotIndex)
/*    */   {
/* 39 */     return this.selected == slotIndex;
/*    */   }
/*    */   
/*    */   protected int getSelected() {
/* 43 */     return this.selected;
/*    */   }
/*    */   
/*    */   protected void drawBackground()
/*    */   {
/* 48 */     this.guiConfig.drawDefaultBackground();
/*    */   }
/*    */   
/*    */ 
/*    */   protected void drawSlot(int p_180791_1_, int p_180791_2_, int p_180791_3_, int p_180791_4_, int p_180791_5_, int p_180791_6_)
/*    */   {
/* 54 */     CFontRenderer font = rip.jutting.polaris.ui.fonth.FontLoaders.vardana12;
/* 55 */     Config config = (Config)this.guiConfig.getConfigs().get(p_180791_1_);
/* 56 */     font.drawStringWithShadow(config.getLabel(), p_180791_2_ + 2, p_180791_3_ + 2, 
/* 57 */       new Color((int)Polaris.instance.settingsManager.getSettingByName("Red").getValDouble(), 
/* 58 */       (int)Polaris.instance.settingsManager.getSettingByName("Green").getValDouble(), 
/* 59 */       (int)Polaris.instance.settingsManager.getSettingByName("Blue").getValDouble()).getRGB());
/* 60 */     font.drawStringWithShadow(config.getName(), p_180791_2_ + 2, p_180791_3_ + 14, new Color(255, 255, 255).getRGB());
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\rip\jutting\polaris\ui\config\ConfigSlot.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */