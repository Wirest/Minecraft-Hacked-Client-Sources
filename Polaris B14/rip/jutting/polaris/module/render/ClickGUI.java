/*    */ package rip.jutting.polaris.module.render;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import rip.jutting.polaris.Polaris;
/*    */ import rip.jutting.polaris.module.Module;
/*    */ import rip.jutting.polaris.ui.click.settings.Setting;
/*    */ import rip.jutting.polaris.ui.click.settings.SettingsManager;
/*    */ 
/*    */ public class ClickGUI extends Module
/*    */ {
/*    */   public ClickGUI()
/*    */   {
/* 14 */     super("ClickGUI", 54, rip.jutting.polaris.module.Category.RENDER);
/*    */   }
/*    */   
/*    */ 
/*    */   public void setup()
/*    */   {
/* 20 */     ArrayList<String> options = new ArrayList();
/*    */     
/* 22 */     options.add("Meme");
/* 23 */     Polaris.instance.settingsManager.rSetting(new Setting("Design", this, "Meme", options));
/* 24 */     Polaris.instance.settingsManager.rSetting(new Setting("Sound", this, false));
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void onEnable()
/*    */   {
/* 32 */     super.onEnable();
/* 33 */     mc.displayGuiScreen(Polaris.instance.clickGui);
/* 34 */     toggle();
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\rip\jutting\polaris\module\render\ClickGUI.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */