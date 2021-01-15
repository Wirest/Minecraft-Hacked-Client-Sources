/*    */ package rip.jutting.polaris.module.other;
/*    */ 
/*    */ import net.minecraft.client.Minecraft;
/*    */ import rip.jutting.polaris.Polaris;
/*    */ import rip.jutting.polaris.event.events.EventUpdate;
/*    */ import rip.jutting.polaris.module.Module;
/*    */ import rip.jutting.polaris.ui.click.settings.Setting;
/*    */ import rip.jutting.polaris.ui.click.settings.SettingsManager;
/*    */ import rip.jutting.polaris.utils.TimeUtils;
/*    */ 
/*    */ public class PinCracker extends Module
/*    */ {
/* 13 */   private TimeUtils time = new TimeUtils();
/*    */   int num;
/*    */   
/*    */   public void setup()
/*    */   {
/* 18 */     Polaris.instance.settingsManager.rSetting(new Setting("Intervals", this, 1000.0D, 1.0D, 5000.0D, false));
/* 19 */     Polaris.instance.settingsManager.rSetting(new Setting("/login", this, false));
/*    */   }
/*    */   
/*    */   public PinCracker() {
/* 23 */     super("PinCracker", 0, rip.jutting.polaris.module.Category.OTHER);
/*    */   }
/*    */   
/*    */   @rip.jutting.polaris.event.EventTarget
/*    */   public void onUpdate(EventUpdate event) {
/* 28 */     if (Polaris.instance.settingsManager.getSettingByName("/login").getValBoolean()) {
/* 29 */       if (this.time.delay((float)Polaris.instance.settingsManager.getSettingByName("Intervals").getValDouble())) {
/* 30 */         mc.thePlayer.sendChatMessage("/login " + numbers());
/* 31 */         this.time.reset();
/*    */       }
/*    */       
/*    */     }
/* 35 */     else if (this.time.delay((float)Polaris.instance.settingsManager.getSettingByName("Intervals").getValDouble())) {
/* 36 */       mc.thePlayer.sendChatMessage("/pin " + numbers());
/* 37 */       this.time.reset();
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */   private int numbers()
/*    */   {
/* 44 */     if (this.num <= 10000) {
/* 45 */       this.num += 1;
/*    */     }
/* 47 */     return this.num;
/*    */   }
/*    */   
/*    */   public void onDisable()
/*    */   {
/* 52 */     this.num = 0;
/* 53 */     super.onDisable();
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\rip\jutting\polaris\module\other\PinCracker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */