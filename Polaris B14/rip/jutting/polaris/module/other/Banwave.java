/*    */ package rip.jutting.polaris.module.other;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.entity.EntityOtherPlayerMP;
/*    */ import net.minecraft.client.entity.EntityPlayerSP;
/*    */ import rip.jutting.polaris.Polaris;
/*    */ import rip.jutting.polaris.event.events.EventUpdate;
/*    */ import rip.jutting.polaris.friend.FriendManager;
/*    */ import rip.jutting.polaris.module.Category;
/*    */ import rip.jutting.polaris.module.Module;
/*    */ import rip.jutting.polaris.ui.click.settings.Setting;
/*    */ import rip.jutting.polaris.ui.click.settings.SettingsManager;
/*    */ import rip.jutting.polaris.utils.Timer;
/*    */ 
/*    */ public class Banwave extends Module
/*    */ {
/*    */   public ArrayList<net.minecraft.entity.Entity> banned;
/* 19 */   private final Timer timer = new Timer();
/*    */   
/*    */   public Banwave() {
/* 22 */     super("Banwave", 0, Category.OTHER);
/* 23 */     this.banned = new ArrayList();
/*    */   }
/*    */   
/*    */   public void setup()
/*    */   {
/* 28 */     Polaris.instance.settingsManager.rSetting(new Setting("Ban Message", this, true));
/* 29 */     Polaris.instance.settingsManager.rSetting(new Setting("Temp", this, false));
/* 30 */     Polaris.instance.settingsManager.rSetting(new Setting("Ban Delay", this, 10.0D, 1.0D, 20.0D, true));
/*    */   }
/*    */   
/*    */   public void onEnable()
/*    */   {
/* 35 */     this.banned.clear();
/* 36 */     super.onEnable();
/*    */   }
/*    */   
/*    */   @rip.jutting.polaris.event.EventTarget
/*    */   public void onUpdate(EventUpdate event) {
/* 41 */     for (Object o : mc.theWorld.getLoadedEntityList()) {
/* 42 */       if ((o instanceof EntityOtherPlayerMP)) {
/* 43 */         EntityOtherPlayerMP e = (EntityOtherPlayerMP)o;
/*    */         
/* 45 */         if ((this.timer.delay(Polaris.instance.settingsManager.getSettingByName("Ban Delay").getValDouble() * 100.0D)) && 
/* 46 */           (!FriendManager.isFriend(e.getName())) && (e.getName() != mc.thePlayer.getName()) && 
/* 47 */           (!this.banned.contains(e)))
/*    */         {
/*    */ 
/* 50 */           if (Polaris.instance.settingsManager.getSettingByName("Temp").getValBoolean()) {
/* 51 */             mc.thePlayer.sendChatMessage("/tempban " + e.getName() + "7d" + (
/* 52 */               Polaris.instance.settingsManager.getSettingByName("Ban Message").getValBoolean() ? 
/* 53 */               " #POLARIS #HACKED" : 
/* 54 */               ""));
/*    */           } else {
/* 56 */             mc.thePlayer.sendChatMessage("/ban " + e.getName() + (
/* 57 */               Polaris.instance.settingsManager.getSettingByName("Ban Message").getValBoolean() ? 
/* 58 */               " #POLARIS #HACKED" : 
/* 59 */               ""));
/*    */           }
/* 61 */           this.banned.add(e);
/* 62 */           this.timer.reset();
/*    */         }
/*    */       }
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\rip\jutting\polaris\module\other\Banwave.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */