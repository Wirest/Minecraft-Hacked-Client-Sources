/*    */ package rip.jutting.polaris.module.other;
/*    */ 
/*    */ import java.util.Random;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.entity.EntityPlayerSP;
/*    */ import rip.jutting.polaris.Polaris;
/*    */ import rip.jutting.polaris.command.commands.SpammerCommand;
/*    */ import rip.jutting.polaris.event.events.EventUpdate;
/*    */ import rip.jutting.polaris.module.Module;
/*    */ import rip.jutting.polaris.ui.click.settings.Setting;
/*    */ import rip.jutting.polaris.ui.click.settings.SettingsManager;
/*    */ import rip.jutting.polaris.utils.Timer;
/*    */ 
/*    */ public class Spammer extends Module
/*    */ {
/* 16 */   private Timer timer = new Timer();
/*    */   
/*    */ 
/*    */ 
/* 20 */   private static Random random = new Random();
/*    */   
/*    */   public Spammer()
/*    */   {
/* 24 */     super("Spammer", 0, rip.jutting.polaris.module.Category.OTHER);
/*    */   }
/*    */   
/*    */   public void setup()
/*    */   {
/* 29 */     Polaris.instance.settingsManager.rSetting(new Setting("Bypass", this, true));
/* 30 */     Polaris.instance.settingsManager.rSetting(new Setting("Message Delay", this, 3.0D, 1.0D, 10.0D, true));
/*    */   }
/*    */   
/*    */   @rip.jutting.polaris.event.EventTarget
/*    */   public void onUpdate(EventUpdate event) {
/* 35 */     if (Polaris.instance.settingsManager.getSettingByName("Bypass").getValBoolean()) {
/* 36 */       if (this.timer.delay(Polaris.instance.settingsManager.getSettingByName("Message Delay").getValDouble() * 1050.0D)) {
/* 37 */         double min = 1.0D;
/* 38 */         double max = 9.0D;
/* 39 */         double xdx = Math.random() * (max - min + 1.0D) + min;
/* 40 */         String xd = String.valueOf(xdx);
/* 41 */         mc.thePlayer.sendChatMessage(SpammerCommand.msg + " " + xd);
/* 42 */         this.timer.reset();
/*    */       }
/*    */       
/*    */     }
/* 46 */     else if (this.timer.delay(Polaris.instance.settingsManager.getSettingByName("Message Delay").getValDouble() * 1050.0D)) {
/* 47 */       mc.thePlayer.sendChatMessage(SpammerCommand.msg);
/* 48 */       this.timer.reset();
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\rip\jutting\polaris\module\other\Spammer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */